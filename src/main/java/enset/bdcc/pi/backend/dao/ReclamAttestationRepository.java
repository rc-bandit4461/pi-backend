package enset.bdcc.pi.backend.dao;


import enset.bdcc.pi.backend.entities.ReclamAttestation;
import enset.bdcc.pi.backend.entities.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin("*")
@RepositoryRestResource
@Repository
public interface ReclamAttestationRepository extends JpaRepository<ReclamAttestation,Long> {
     @RestResource(path = "/byProcessed")
    @Query("select p from ReclamAttestation p where p.isDone=:processed")
    public List<ReclamAttestation> getProcessedRequests(@Param("processed") Boolean processed);

    @RestResource(path = "/countNotSeen")
    @Query("select count(p) from ReclamAttestation p where p.etudiant.id=:idEtudiant and p.isSeen=false AND p.isDone=true")
    Long getReclamAttestationsCount(@Param("idEtudiant") long idEtudiant);

    @RestResource(path = "/byUserNotSeen")
    @Query("select p from ReclamAttestation p where p.etudiant.id=:idEtudiant AND p.isSeen=false AND p.isDone=true")
    public List<ReclamAttestation> getRequestsByEtudiant(@Param("idEtudiant") long idEtudiant);

    @RestResource(path = "/countNotDone")
    @Query("select count(p) from ReclamAttestation p where p.isDone=false")
    Long getReclamAttestationsNotDone();
}
