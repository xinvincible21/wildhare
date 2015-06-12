package worker

import utils.RabbitUtils._
import com.rabbitmq.client._
import play.api._


case class JobWorker(name:String)(implicit val conn:Connection) {
  //implicit val conn = getConnection
  implicit val channel = getChannel

  def consume(exchangeName: String, publisherExchangeName:String, queueName: String, routingKey: String) = {

    val autoAck = false
    channel.basicConsume(queueName, autoAck, name,
      new DefaultConsumer(channel) {
        override def handleDelivery(consumerTag: String,
                                    envelope: com.rabbitmq.client.Envelope,
                                    properties: AMQP.BasicProperties,
                                    body: Array[Byte]) {
          val routingKey = envelope.getRoutingKey
          val contentType = properties.getContentType
          val deliveryTag = envelope.getDeliveryTag
          val msg = new String(body)
          Logger.info(s"$name consuming message $msg")
          channel.basicAck(deliveryTag, false)
          publish(publisherExchangeName, routingKey, msg)
        }
      }
    )

  }

  def shutdown() = {
    channel.close()
    //conn.close()
  }
}


