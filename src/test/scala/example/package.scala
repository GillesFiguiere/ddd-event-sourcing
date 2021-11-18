import scala.collection.mutable

package object example {


  case class IdProfessionelSante(value:Long) extends AnyVal


  trait Evt {
    val id: IdProfessionelSante
  }

  case class EvtProfessionelSanteReference(id: IdProfessionelSante) extends Evt

  case class EvtProfessionelSanteActive(id: IdProfessionelSante) extends Evt

  case class EvtProfessionelSanteDesactive(id: IdProfessionelSante) extends Evt
}
