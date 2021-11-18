package example

import org.scalactic.anyvals.NonEmptyList
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers

import scala.collection.mutable


class ProjectionProfessionnelleDeSanteTest extends AnyFlatSpec with Matchers {
  "ProjectionProfessionnelleDeSante" should "Ajout Professionel de santé référencé" in {
    val  RepositoryProfesionnelDeSanteNonActive: mutable.ListBuffer[IdProfessionelSante] = scala.collection.mutable.ListBuffer.empty[IdProfessionelSante]
    val idProfessionelSante = IdProfessionelSante(2)
    ProjectionProfessionnelleDeSante.handle(EvtProfessionelSanteReference(idProfessionelSante),RepositoryProfesionnelDeSanteNonActive)
    ProjectionProfessionnelleDeSante.handle(EvtProfessionelSanteReference(idProfessionelSante),RepositoryProfesionnelDeSanteNonActive)
    ProjectionProfessionnelleDeSante.handle(EvtProfessionelSanteReference(idProfessionelSante),RepositoryProfesionnelDeSanteNonActive)
    ProjectionProfessionnelleDeSante.handle(EvtProfessionelSanteReference(idProfessionelSante),RepositoryProfesionnelDeSanteNonActive)
    RepositoryProfesionnelDeSanteNonActive shouldBe List(idProfessionelSante)
  }
  "ProjectionProfessionnelleDeSante" should "Ajout Professionel de santé Activé" in {
    val  RepositoryProfesionnelDeSanteNonActive: mutable.ListBuffer[IdProfessionelSante] = scala.collection.mutable.ListBuffer.empty[IdProfessionelSante]
    val idProfessionelSante = IdProfessionelSante(1)
    ProjectionProfessionnelleDeSante.handle(EvtProfessionelSanteReference(idProfessionelSante),RepositoryProfesionnelDeSanteNonActive)
    ProjectionProfessionnelleDeSante.handle(EvtProfessionelSanteActive(idProfessionelSante),RepositoryProfesionnelDeSanteNonActive)
    RepositoryProfesionnelDeSanteNonActive shouldBe List()
  }

}


object ProjectionProfessionnelleDeSante {
  def handle(reference: Evt, repositoryProfessionelDeSanteNonActive: mutable.ListBuffer[IdProfessionelSante]) = {
    reference match {
      case EvtProfessionelSanteActive(id) => repositoryProfessionelDeSanteNonActive -= id
      case EvtProfessionelSanteDesactive(id) => repositoryProfessionelDeSanteNonActive += id
      case EvtProfessionelSanteReference(id) => repositoryProfessionelDeSanteNonActive += id
      case _ => repositoryProfessionelDeSanteNonActive
    }

  }


}

