package enset.bdcc.pi.backend.dao;

import enset.bdcc.pi.backend.entities.Demande;
import enset.bdcc.pi.backend.entities.DemandeAttestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin("*")
@Repository
@RepositoryRestResource
public interface DemandeAttestationRepository extends JpaRepository<DemandeAttestation, Long> {
    @RestResource(path = "/countNotSeen")
    @Query("select count(p) from DemandeAttestation p where p.etudiant.id=:idEtudiant and p.isSeen=false AND p.isDone=true")
    Long getRequestsCount(@Param("idEtudiant") long idEtudiant);

    @RestResource(path = "/byProcessed")
    @Query("select p from DemandeAttestation p where p.isDone=:processed")
    public List<DemandeAttestation> getProcessedRequests(@Param("processed") boolean processed);

    @RestResource(path = "/byEtudiant")
    @Query("select p from DemandeAttestation p where p.etudiant.id=:idEtudiant and p.isDone=true")
    public List<DemandeAttestation> getRequestsByEtudiant(@Param("idEtudiant") Long idEtudiant);

    @RestResource(path = "/byEtudiantNotSeen")
    @Query("select p from DemandeAttestation p where p.etudiant.id=:idEtudiant AND p.isSeen=false AND p.isDone=true")
    public List<DemandeAttestation> getRequestsByEtudiant(@Param("idEtudiant") long idEtudiant);
}
