package enset.bdcc.pi.backend.dao;

import enset.bdcc.pi.backend.entities.LayoutCapacity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource
@CrossOrigin("*")
public interface LayoutCapacityRepository extends JpaRepository<LayoutCapacity, Integer> {

}
