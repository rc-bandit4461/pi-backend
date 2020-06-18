package enset.bdcc.pi.backend.dao;

import enset.bdcc.pi.backend.entities.Diplome;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin("*")
@Repository
@RepositoryRestResource
public interface DiplomeRepository extends JpaRepository<Diplome, Long> {
    public Diplome getByDescriptionContains(String description);

    public Diplome getByLibelleContains(String libelle);
}
