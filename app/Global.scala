

import play.api._
import utils.RabbitUtils._
import worker._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Global extends GlobalSettings {

  implicit val conn = getConnection
  implicit val channel = getChannel

  val numberOfWorkers = 1000

  val jobWorkers =
    for(i <- 1 to numberOfWorkers) yield {
      JobWorker(s"job worker $i")
    }

  val statusWorkers =
    for(i <- 1 to numberOfWorkers) yield {
      StatusWorker(s"status worker $i")
    }


  override def onStart(app: Application) {

    //Logger.info("starting 2 workers")
    startup(exchangeName = "rb-job-exchange", queueName = "rb-job", routingKey = "#")
    startup(exchangeName = "rb-worker-exchange", queueName = "rb-worker", routingKey = "#")
    startup(exchangeName = "rb-status-exchange", queueName = "rb-status", routingKey = "#")


    for(worker <- jobWorkers) {
      worker.consume(exchangeName = "rb-job-exchange", publisherExchangeName = "rb-worker-exchange", queueName = "rb-job", routingKey = "#")
    }

    for(worker <- statusWorkers) {
      worker.consume(exchangeName = "rb-worker-exchange", publisherExchangeName = "rb-status-exchange", queueName = "rb-worker", routingKey = "#")
    }

  }

   override def onStop(app: Application) {
     shutdown()
     for(worker <- jobWorkers) {
       worker.shutdown()
     }

     for(worker <- statusWorkers) {
       worker.shutdown()
     }
   }

}
