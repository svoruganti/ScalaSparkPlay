package Impl

import Modules.GuiceModule
import com.google.inject.Guice
import org.scalatest.FunSuite
import traits.BusinessService

class BusinessServiceTests extends FunSuite{
  val injector = Guice.createInjector(new GuiceModule())
  val businessService = injector.getInstance(classOf[BusinessService])
  test("Get business plus items"){
    val dataSet = businessService.getAllBusinesses
    assert(dataSet.count() > 0)
  }

  test("Get city and count"){
    val dataSet = businessService.getBusinessCountByCity
    dataSet.show
    assert(dataSet.first()._2 > 0)
  }

  test("Get business name by inspection type"){
    val dataSet = businessService.getInpectionsCountByBusiness;
    dataSet.show()
    assert(dataSet.count() > 0)
  }
}
