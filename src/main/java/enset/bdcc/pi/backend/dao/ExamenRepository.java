package enset.bdcc.pi.backend.dao;


import enset.bdcc.pi.backend.entities.Examen;
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
public interface  ExamenRepository extends JpaRepository<Examen,Long> {
        @RestResource(path = "/bySession")
    @Query("select p from Examen p where p.module.id in(select m.id from Module m where m.semestreFiliere.id in(select sf.id from SemestreFiliere sf where sf.session.id=:id))")
    public List<Examen> getExamsByModuleId(@Param("id") Long id);
}
