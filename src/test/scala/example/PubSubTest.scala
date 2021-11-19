package example

import example.ProjectionProfessionnelleDeSante.{EventHandler, Handlerable}
import org.scalactic.anyvals.NonEmptyList
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class PubSubTest extends AnyFlatSpec with Matchers {
  "PubSub" should "Store event when published event" in {
    val eventStore: EventStoreInMemory[IdProfessionelSante, Evt] = new EventStoreInMemory[IdProfessionelSante, Evt] {
      override val repo: ListBuffer[(IdProfessionelSante, Evt)] = ListBuffer.empty
    }
    val idProfessionelSante = IdProfessionelSante(2)
    val eventList = List(EvtProfessionelSanteReference(idProfessionelSante))
    val publishEvents = PubSub.publish[IdProfessionelSante, Evt](
      _,
      handlers= List.empty,
      eventStore= eventStore,
      _,
      _)
    publishEvents(idProfessionelSante,eventList,0)
    eventStore.get(idProfessionelSante) should contain theSameElementsAs  eventList
  }

  "PubSub" should "Call handler" in {
    val eventStore: EventStoreInMemory[IdProfessionelSante, Evt] = new EventStoreInMemory[IdProfessionelSante, Evt] {
      override val repo: ListBuffer[(IdProfessionelSante, Evt)] = ListBuffer.empty
    }
//    val eventStore: mutable.ListBuffer[Evt] = scala.collection.mutable.ListBuffer.empty[Evt]
    val idProfessionelSante = IdProfessionelSante(2)
    val eventList = List(EvtProfessionelSanteReference(idProfessionelSante))
    var isHandlerCalled = false
    val handler = (_: Evt) => isHandlerCalled=true
    PubSub.publish(idProfessionelSante,List(handler), eventStore, eventList,0)
    isHandlerCalled shouldBe true
  }
}

object PubSub {
  private def call[ID, PAYLOAD](handlers: List[EventHandler[PAYLOAD]], eventList: List[PAYLOAD]) =
    eventList.foreach(evt => handlers.foreach(_ (evt)))

  private def store[ID, PAYLOAD](id: ID, numSequence:Int, eventList: List[PAYLOAD], eventStore: EventStoreInMemory[ID, PAYLOAD])
  = eventStore.store(id, eventList,numSequence)

  def publish[ID, PAYLOAD](id:ID, handlers: List[EventHandler[PAYLOAD]]
                           , eventStore: EventStoreInMemory[ID, PAYLOAD], newEvents: List[PAYLOAD],numSequence:Int) = {
    store[ID, PAYLOAD](id,numSequence,newEvents, eventStore)
    call(handlers, newEvents)
  }
}

