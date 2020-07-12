package enset.bdcc.pi.backend.dao;


import enset.bdcc.pi.backend.entities.DemandeReleve;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@CrossOrigin("*")
@RepositoryRestResource
@Repository
public interface DemandeReleveRepository extends JpaRepository<DemandeReleve, Long> {
    @RestResource(path = "/countNotSeen")
    @Query("select count(p) from DemandeReleve p where p.semestreEtudiant.etudiant.id=:idEtudiant and p.isSeen=false and p.isDone=true")
    Long getDemandeReleveCount(@Param("idEtudiant") Long idEtudiant);

    @RestResource(path = "/byProcessed")
    @Query("select p from DemandeReleve p where p.isDone=:processed")
    public List<DemandeReleve> getProcessedRequests(@Param("processed") Boolean processed);

    @RestResource(path = "/byEtudiant")
    @Query("select p from DemandeReleve p where p.semestreEtudiant.etudiant.id=:idEtudiant")
    public List<DemandeReleve> getRequestsByEtudiant(@Param("idEtudiant") long idEtudiant);

    @RestResource(path = "/byEtudiantNotSeen")
    @Query("select p from DemandeReleve p where p.semestreEtudiant.etudiant.id=:idEtudiant AND p.isSeen=false AND p.isDone=true")
    public List<DemandeReleve> getRequestsByEtudiantAndNotSeen(@Param("idEtudiant") long idEtudiant);
}
