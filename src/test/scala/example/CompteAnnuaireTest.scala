package example

import org.scalactic.anyvals.NonEmptyList
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers


case class ProfSanteProj(id: IdProfessionelSante, is_active: Boolean = false)

object ProfSanteProj {


  def apply(evts:List[Evt]):ProfSanteProj = {

    applyAcc(ProfSanteProj(evts.head.id),evts)
  }

  private def applyAcc(profSante: ProfSanteProj, evts:List[Evt]):ProfSanteProj = evts match {
    case ::(EvtProfessionelSanteDesactive(_), next) =>
      applyAcc(profSante.copy(is_active = false),next)
    case ::(EvtProfessionelSanteActive(_), next) =>
      applyAcc(profSante.copy(is_active = true),next)
    case ::(_, next) =>
      applyAcc(profSante,next)
    case Nil => profSante
  }
}



class CompteAnnuaireTest extends AnyFlatSpec with Matchers {
  "CompteAnnuaire" should "Référence professionel de santé" in {

    val ps = IdProfessionelSante(1)
    val ps_evt_ok = EvtProfessionelSanteReference(ps)


    val actual: List[Evt] = CompteAnnuaire.reference(ps)


    actual shouldBe List(ps_evt_ok)
  }


  "CompteAnnuaire" should "Active le professionel de santé" in {

    val ps = IdProfessionelSante(1)
    val historique: NonEmptyList[Evt] = NonEmptyList(EvtProfessionelSanteReference(ps))
    val ps_evt_attendu: List[Evt] = historique ++ List(EvtProfessionelSanteActive(ps))


    val actual: List[Evt] = historique ++ CompteAnnuaire.active(historique)


    actual shouldBe ps_evt_attendu
  }


  "CompteAnnuaire" should "Desactive le professionel de santé" in {

    val ps = IdProfessionelSante(1)
    val historique: List[Evt] = CompteAnnuaire.active(List(EvtProfessionelSanteReference(ps)))
    val ps_evt_attendu: List[Evt] = historique ++ List(EvtProfessionelSanteDesactive(ps))

    val actual: List[Evt] = historique ++ CompteAnnuaire.desactive(historique)

    actual shouldBe ps_evt_attendu
  }

  "CompteAnnuaire" should "Desactive le professionel de santé 2 fois" in {

    val ps = IdProfessionelSante(1)
    val historique: List[Evt] = List(
      EvtProfessionelSanteReference(ps),
      EvtProfessionelSanteActive(ps),
      EvtProfessionelSanteDesactive(ps),
      EvtProfessionelSanteActive(ps)
    )

    CompteAnnuaire.desactive(historique) shouldBe List(EvtProfessionelSanteDesactive(ps))
  }



}

object CompteAnnuaire {


  def desactive(historique: List[Evt]): List[Evt] = {
    val proj = ProfSanteProj.apply(historique)
    if (proj.is_active)
      List(EvtProfessionelSanteDesactive(historique.last.id))
    else
      List.empty[Evt]
  }

  def active(ps_evt_ok: List[Evt]): List[Evt] = {
    List(EvtProfessionelSanteActive(ps_evt_ok.last.id))
  }

  def reference(ps: IdProfessionelSante): List[Evt] = {
    List(EvtProfessionelSanteReference(ps))
  }
}

