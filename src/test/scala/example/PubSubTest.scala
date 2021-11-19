package example

import example.ProjectionProfessionnelleDeSante.{EventHandler, Handlerable}
import org.scalactic.anyvals.NonEmptyList
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class PubSubTest extends AnyFlatSpec with Matchers {
  "PubSub" should "Store event when published event" in {
    val eventStore: mutable.ListBuffer[Evt] = scala.collection.mutable.ListBuffer.empty[Evt]
    val idProfessionelSante = IdProfessionelSante(2)
    val eventList = List(EvtProfessionelSanteReference(idProfessionelSante))
    val publishEvents = PubSub.publish(List.empty,eventStore,_)
    publishEvents(eventList)
    eventStore shouldBe eventList
  }

  "PubSub" should "Call handler" in {
    val  RepositoryProfesionnelDeSanteNonActive: mutable.Set[IdProfessionelSante] = scala.collection.mutable.Set.empty[IdProfessionelSante]
    val eventStore: mutable.ListBuffer[Evt] = scala.collection.mutable.ListBuffer.empty[Evt]
    val idProfessionelSante = IdProfessionelSante(2)
    val eventList = List(EvtProfessionelSanteReference(idProfessionelSante))
    var isHandlerCalled = false
    val handlerFoo = new Handlerable {
      override def handle(reference: Evt, repositoryProfessionelDeSanteNonActive: mutable.Set[IdProfessionelSante]): Unit = isHandlerCalled = true
    }
    val handler: Evt => Unit = handlerFoo.handle(_,RepositoryProfesionnelDeSanteNonActive)
    PubSub.publish(List(handler), eventStore, eventList)
    isHandlerCalled shouldBe true
  }
}

object PubSub {
  private def call(handlers: List[EventHandler], eventList: List[EvtProfessionelSanteReference]) =
    eventList.foreach(evt => handlers.foreach(_(evt)))

  private def store(eventList: List[EvtProfessionelSanteReference], eventStore: ListBuffer[Evt]) =
    eventStore ++= eventList

  def publish(handlers: List[EventHandler]
              , eventStore: ListBuffer[Evt], eventList: List[EvtProfessionelSanteReference]) = {
    store(eventList,eventStore)
    call(handlers,eventList)
  }
}

