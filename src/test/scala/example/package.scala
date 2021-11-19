import scala.collection.mutable

package object example {
  case class IdProfessionelSante(value:Long) extends AnyVal

  trait Evt {
    val id: IdProfessionelSante
  }

  case class EvtProfessionelSanteReference(id: IdProfessionelSante) extends Evt

  case class EvtProfessionelSanteActive(id: IdProfessionelSante) extends Evt

  case class EvtProfessionelSanteDesactive(id: IdProfessionelSante) extends Evt

  abstract class EventStoreInMemory[ID, PAYLOAD]() {
    val repo: mutable.ListBuffer[(ID, PAYLOAD)]
    private def lastSequence(idProfessionelSante: ID) = get(idProfessionelSante).size
    def get(idProfessionelSante: ID) = repo
      .filter(_._1 == idProfessionelSante)
      .map(_._2)

    def store(idProfessionelSante: ID, eventList: List[PAYLOAD], currentSequence: Int) = {
      if (lastSequence(idProfessionelSante) == currentSequence)
        repo ++= eventList.map(el => (idProfessionelSante, el))
      else
        throw new Exception("Aie Aie Aie ça pique !!!")
    }
  }
}
