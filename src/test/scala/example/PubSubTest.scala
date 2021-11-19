package example

import example.ProjectionProfessionnelleDeSante.Handlerable
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
    PubSub.store(eventList, eventStore)
    eventStore shouldBe eventList
  }


  "PubSub" should "Call handler" in {
    val  RepositoryProfesionnelDeSanteNonActive: mutable.Set[IdProfessionelSante] = scala.collection.mutable.Set.empty[IdProfessionelSante]
    val eventStore: mutable.ListBuffer[Evt] = scala.collection.mutable.ListBuffer.empty[Evt]
    val idProfessionelSante = IdProfessionelSante(2)
    val eventList = List(EvtProfessionelSanteReference(idProfessionelSante))
    PubSub.store(eventList, eventStore)
    var isHandlerCalled = false
    val handlers = List(new Handlerable {
      override def handle(reference: Evt, repositoryProfessionelDeSanteNonActive: mutable.Set[IdProfessionelSante]): Unit = isHandlerCalled = true
    })
    PubSub.call(handlers, eventList,RepositoryProfesionnelDeSanteNonActive)
    isHandlerCalled shouldBe true
  }
}



object PubSub {
  def call(handlers: List[Handlerable], eventList: List[EvtProfessionelSanteReference],repo: mutable.Set[IdProfessionelSante]) =
    eventList.foreach(evt => handlers.foreach(_.handle(evt,repo)))

  def store(eventList: List[EvtProfessionelSanteReference], eventStore: ListBuffer[Evt]) =
    eventStore ++= eventList


}

