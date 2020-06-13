package enset.bdcc.pi.backend.dao;


import enset.bdcc.pi.backend.entities.Element;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin("*")
@RepositoryRestResource
@Repository
public interface ElementRepository extends JpaRepository<Element, Long> {
    @RestResource(path = "/byLibellePage")
    public Page<Element> getByLibelleContains(@Param("mc") String x, Pageable pa);

    @RestResource(path = "/byLibelle")
    public List<Element> findByLibelleContains(@Param("mc") String mc);
}
