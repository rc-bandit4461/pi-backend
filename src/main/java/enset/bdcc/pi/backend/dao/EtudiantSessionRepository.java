package enset.bdcc.pi.backend.dao;


import enset.bdcc.pi.backend.entities.EtudiantSession;
import enset.bdcc.pi.backend.entities.EtudiantSessionKey;
import enset.bdcc.pi.backend.entities.NoteElementModule;
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
public interface EtudiantSessionRepository extends JpaRepository<EtudiantSession, EtudiantSessionKey> {
    @RestResource(path = "/bySession")
    @Query("select p from EtudiantSession p where p.id.sessionId=:id")
    public List<EtudiantSession> getBySessionId(@Param("id") Long id);


}
