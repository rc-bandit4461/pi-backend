package enset.bdcc.pi.backend.dao;


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
public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
    @RestResource(path = "/byProcessed")
    @Query("select p from Reclamation p where p.isDone=:processed")
    public List<Reclamation> getProcessedRequests(@Param("processed") Boolean processed);

    @RestResource(path = "/countNotSeen")
    @Query("select count(p) from Reclamation p where p.user.id=:idUser and p.isSeen=false AND p.isDone=true")
    Long getReclamationsCount(@Param("idUser") long idUser);

    @RestResource(path = "/byUserNotSeen")
    @Query("select p from Reclamation p where p.user.id=:idUser AND p.isSeen=false AND p.isDone=true")
    public List<Reclamation> getRequestsByEtudiant(@Param("idUser") long idUser);

    @RestResource(path = "/countNotDone")
    @Query("select count(p) from Reclamation p where p.isDone=false")
    Long getReclamationsNotDone();
}
