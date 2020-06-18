package enset.bdcc.pi.backend.dao;


import enset.bdcc.pi.backend.entities.NoteModule;
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
public interface NoteModuleRepository extends JpaRepository<NoteModule,Long> {
    @RestResource(path="/byNoteModuleAndSE")
    @Query("select p from NoteModule p where p.semestreEtudiant.id=:idSE and p.module.id=:idModule")
    public NoteModule getByModuleIdAndSEId(@Param("idSE") Long idSE,@Param("idModule") Long idModule);
}
