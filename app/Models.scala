package models

import play.api.libs.json._
import org.joda.time.DateTime



case class UserReportingTemplate(id:Long, userId:Long, name:String, filters:String, dimensions:String, measures:String)
case class UserReportingTemplateJobs(id:Long, reportId:Long, startTime:DateTime, endTime:DateTime, status:String, statusMessage:String, downloadURL:String)
case class UserReports(id:Long, userId:Long, name:String, filters:String, dimensions:String, measures:String, scheduleCronString:Option[String], enabled:Boolean)

object formats {
  implicit val userReportingTemplateF = Json.format[UserReportingTemplate]
  implicit val userReportingTemplateJobsF = Json.format[UserReportingTemplateJobs]
  implicit val userReportsF = Json.format[UserReports]
}
