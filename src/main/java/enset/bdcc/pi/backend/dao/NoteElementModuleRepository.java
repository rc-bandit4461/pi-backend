package enset.bdcc.pi.backend.dao;


import enset.bdcc.pi.backend.entities.NoteElementModule;
import enset.bdcc.pi.backend.entities.NoteElementModuleKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin("*")
@RepositoryRestResource
@Repository
public interface NoteElementModuleRepository extends JpaRepository<NoteElementModule, Long> {
    @RestResource(path = "/byElementAndNoteModule")
    @Query("select p from NoteElementModule p where p.element.id=:idElement and p.noteModule.id=:idNoteModule")
    public NoteElementModule getByElementAndNoteModule(@Param("idElement") Long idElement, @Param("idNoteModule") Long idNoteModule);
    @RestResource(path="/test2")
    @Query("select p from NoteElementModule  p where p.element.id=:idElement and p.noteModule.id=(select nm.id from NoteModule  nm where nm.module.id=:idModule and nm.semestreEtudiant.id in(select se.id from SemestreEtudiant se where se.etudiant.id=:idEtudiant and se.session.id=:idSession))")
    public NoteElementModule getByElementAndModuleAndEtudiantAndSession(@Param("idElement") Long element, @Param("idModule") Long idModule,@Param("idEtudiant") Long idEtudiant,@Param("idSession") Long idSession);
}
