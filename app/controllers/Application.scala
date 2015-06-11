package controllers

import play.api.mvc._
import utils.RabbitUtils._

class Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def test = Action {

    implicit val conn = getConnection
    implicit val channel = getChannel

    publishTestMessages(exchangeName = "rb-job-exchange", routingKey = "#")

    shutdown
    Ok
  }
}



