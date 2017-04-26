package controllers

import javax.inject.Inject

import Model.Business
import play.api.libs.json.{JsPath, Writes}
import play.api.mvc.{Action, Controller}
import play.api.libs.json._
import play.api.libs.functional.syntax._
import traits.BusinessService
import org.apache.spark.sql.functions._

class BusinessController @Inject()(businessService: BusinessService) extends Controller{
  def all = Action { implicit request =>
    val businesses = businessService.getAllBusinesses.collect()
    Ok(Json.toJson(businesses))
  }

  def city = Action {
    val city = businessService.getBusinessCountByCity.collect()
    Ok(Json.toJson(city))
  }

  def inspection = Action {
    val inspections = businessService.getInpectionsCountByBusiness.orderBy(desc("_2"), asc("_1")).collect()
    Ok(Json.toJson(inspections))
  }

  implicit val writeBusiness : Writes[Business] = (
    (JsPath \ "business_id").write[String] and
    (JsPath \ "name").write[String] and
    (JsPath \ "address").write[String] and
    (JsPath \ "city").write[String]
    )(unlift(Business.unapply))

  implicit def tuple2Writes[A, B](implicit a: Writes[A], b: Writes[B]): Writes[(A, B)] = new Writes[(A, B)] {
    def writes(tuple: (A, B)) = JsArray(Array(a.writes(tuple._1), b.writes(tuple._2)))
  }
}
