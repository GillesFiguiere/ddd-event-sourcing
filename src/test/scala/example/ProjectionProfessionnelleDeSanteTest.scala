package example

import org.scalactic.anyvals.NonEmptyList
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable


class ProjectionProfessionnelleDeSanteTest extends AnyFlatSpec with Matchers {
  "ProjectionProfessionnelleDeSante" should "Ajout Professionel de santé référencé" in {
    val  RepositoryProfesionnelDeSanteNonActive: mutable.Set[IdProfessionelSante] = scala.collection.mutable.Set.empty[IdProfessionelSante]
    val idProfessionelSante = IdProfessionelSante(2)
    ProjectionProfessionnelleDeSante.handle(EvtProfessionelSanteReference(idProfessionelSante),RepositoryProfesionnelDeSanteNonActive)
    ProjectionProfessionnelleDeSante.handle(EvtProfessionelSanteReference(idProfessionelSante),RepositoryProfesionnelDeSanteNonActive)
    ProjectionProfessionnelleDeSante.handle(EvtProfessionelSanteReference(idProfessionelSante),RepositoryProfesionnelDeSanteNonActive)
    ProjectionProfessionnelleDeSante.handle(EvtProfessionelSanteReference(idProfessionelSante),RepositoryProfesionnelDeSanteNonActive)
    RepositoryProfesionnelDeSanteNonActive shouldBe mutable.Set(idProfessionelSante)
  }
  "ProjectionProfessionnelleDeSante" should "Ajout Professionel de santé Activé" in {
    val  RepositoryProfesionnelDeSanteNonActive: mutable.Set[IdProfessionelSante] = scala.collection.mutable.Set.empty[IdProfessionelSante]
    val idProfessionelSante = IdProfessionelSante(1)
    ProjectionProfessionnelleDeSante.handle(EvtProfessionelSanteReference(idProfessionelSante),RepositoryProfesionnelDeSanteNonActive)
    ProjectionProfessionnelleDeSante.handle(EvtProfessionelSanteActive(idProfessionelSante),RepositoryProfesionnelDeSanteNonActive)
    RepositoryProfesionnelDeSanteNonActive shouldBe mutable.Set()
  }

}


object ProjectionProfessionnelleDeSante {
  def handle(reference: Evt, repositoryProfessionelDeSanteNonActive: mutable.Set[IdProfessionelSante]) = {
    reference match {
      case EvtProfessionelSanteActive(id) => repositoryProfessionelDeSanteNonActive -= id
      case EvtProfessionelSanteDesactive(id) => repositoryProfessionelDeSanteNonActive += id
      case EvtProfessionelSanteReference(id) => repositoryProfessionelDeSanteNonActive += id
      case _ => repositoryProfessionelDeSanteNonActive
    }

  }


trait Handlerable {
  def handle(reference: Evt, repositoryProfessionelDeSanteNonActive: mutable.Set[IdProfessionelSante])
}
type EventHandler = Evt => Unit

}

