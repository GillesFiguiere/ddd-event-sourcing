package example

import example.ProjectionProfessionnelleDeSante.{EventHandler, Handlerable}
import org.scalactic.anyvals.NonEmptyList
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

class PersistenceTest extends AnyFlatSpec with Matchers {
  "Persistence Test" should "Store events" in {
    val idProfessionelSante = IdProfessionelSante(2)
    val eventList = List(EvtProfessionelSanteReference(idProfessionelSante))
    val eventStore = new EventStoreInMemory[IdProfessionelSante, Evt] {
      override val repo: ListBuffer[(IdProfessionelSante, Evt)] = ListBuffer.empty
    }
    eventStore.store(idProfessionelSante, eventList, 0)
    eventStore.get(idProfessionelSante) shouldBe eventList
  }

  "Persistence Test" should "Should return only Events on AggregatedId" in {
    val idProfessionelSante1 = IdProfessionelSante(1)
    val idProfessionelSante2 = IdProfessionelSante(2)
    val eventList = List(EvtProfessionelSanteReference(idProfessionelSante1))

    val eventStore = new EventStoreInMemory[IdProfessionelSante, Evt] {
      override val repo: ListBuffer[(IdProfessionelSante, Evt)] = ListBuffer.empty
    }

    eventStore.store(idProfessionelSante1, eventList, 0)
    eventStore.store(idProfessionelSante2, eventList, 0)

    eventStore.get(idProfessionelSante1) shouldBe eventList
    eventStore.get(idProfessionelSante2) shouldBe eventList
  }


  "Persistence Test" should "Test Sequence number should trow error" in {
    val idProfessionelSante1 = IdProfessionelSante(1)
    val eventList = List(EvtProfessionelSanteReference(idProfessionelSante1))
    val eventStore = new EventStoreInMemory[IdProfessionelSante, Evt] {
      override val repo: ListBuffer[(IdProfessionelSante, Evt)] = ListBuffer.empty
    }
    //    an[Exception] should be thrownBy {
    eventStore.store(idProfessionelSante1, eventList, 0)
    //    }
    an[Exception] should be thrownBy {
      eventStore.store(idProfessionelSante1, eventList, 0)
    }
  }
}
