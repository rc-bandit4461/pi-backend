package enset.bdcc.pi.backend.dao;


import enset.bdcc.pi.backend.entities.Etudiant;
import enset.bdcc.pi.backend.entities.SemestreEtudiant;
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
public interface SemestreEtudiantRepository extends JpaRepository<SemestreEtudiant, Long> {
    @RestResource(path = "/bydick")
    @Query("select p from SemestreEtudiant  p where p.etudiant.id=:idEtudiant and p.numero=:numero and p.session.id=:idSession")
    public SemestreEtudiant findByIdEtudiantAndIdSessionAndNumero(@Param("idEtudiant") Long idEtudiant, @Param("idSession") Long idSession, @Param("numero") int numero);
}
