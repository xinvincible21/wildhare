package utils

import com.rabbitmq.client._

object RabbitUtils {
  def publishTestMessages(exchangeName: String, routingKey: String)(implicit conn: Connection, channel: Channel) = {
    for (i <- 1 to 1000000) {
      val msg = s"Hello, world! $i"
      publish(exchangeName, routingKey, msg)
    }
  }

  def shutdown()(implicit conn: Connection, channel: Channel) = {
    channel.close()
    conn.close()
  }

  def startup(exchangeName: String, queueName: String, routingKey: String)(implicit conn: Connection, channel: Channel) = {

    val exchange = declareExchange(exchangeName)
    val queue = declareQueue(queueName)
    bindQueue(queueName, exchangeName, routingKey)
  }

  def getConnection = {

    val factory = new ConnectionFactory()
    factory.setUsername("guest")
    //    factory.setPassword("guest")
    //    factory.setVirtualHost("/")
    //    factory.setHost("127.0.0.1")
    //    factory.setPort(15672)
    val conn = factory.newConnection()
    factory.setAutomaticRecoveryEnabled(true)
    conn
  }

  def getChannel(implicit conn:Connection) = { conn.createChannel() }

  def declareExchange(exchangeName:String)(implicit channel:Channel) = {
  channel.exchangeDeclare(exchangeName, "direct", true) }

  def declareQueue(queueName:String)(implicit channel:Channel) = { channel.queueDeclare(queueName, true,
  false, false, null) }

  def bindQueue(queueName:String, exchangeName:String, routingKey:String)(implicit channel:Channel) = {
  channel.queueBind(queueName, exchangeName, routingKey) }

  def publish(exchangeName:String, routingKey:String, msg:String)(implicit channel:Channel) = {

    val mp = MessageProperties.PERSISTENT_BASIC
    channel.basicPublish(exchangeName, routingKey, mp, msg.getBytes) }


}
