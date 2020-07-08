package enset.bdcc.pi.backend.dao;


import enset.bdcc.pi.backend.entities.Etudiant;
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
public interface EtudiantRepository extends JpaRepository<Etudiant, Long> {
    @RestResource(path = "/byCin")
    List<Etudiant> getEtudiantByCinLike(@Param("var") String mc);

    @RestResource(path = "/byEmail")
    Etudiant getEtudiantByEmailLike(@Param("email") String mc);

    @RestResource(path = "/bySession")
    @Query("select p from Etudiant p where p.id in(select etuds.etudiant.id from EtudiantSession etuds where etuds.session.id=:id)")
    List<Etudiant> getBySession(@Param("id") Long id);

    //    @RestResource(path = "/byExamen")
    @Query("select p from Etudiant p where p.id in(select se.etudiant.id from SemestreEtudiant se where se.id in(select nm.semestreEtudiant.id from NoteModule nm where nm.id in(select nem.noteModule.id from NoteElementModule nem where nem.id in(select noteexam.noteElementModule.id from NoteExamen noteexam where noteexam.examen.id=:idExam))))")
    List<Etudiant> getByExamenHardway(@Param("idExam") Long idExam);

    @RestResource(path = "/byExamen")
    @Query("select p from Etudiant p where p.id in(select nex.etudiant.id from NoteExamen nex where nex.examen.id=:id)")
    List<Etudiant> getByExamenQuickway(@Param("id") Long id);

}
