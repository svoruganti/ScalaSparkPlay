package Modules

import Impl.{AppSparkSessionImpl, BusinessServiceImpl}
import com.google.inject.AbstractModule
import traits.{AppSparkSession, BusinessService}

class GuiceModule extends AbstractModule{
  override def configure(): Unit = {
    bind(classOf[AppSparkSession]).to(classOf[AppSparkSessionImpl])
    bind(classOf[BusinessService]).to(classOf[BusinessServiceImpl])
  }
}
