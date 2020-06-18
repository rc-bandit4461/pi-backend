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
}
