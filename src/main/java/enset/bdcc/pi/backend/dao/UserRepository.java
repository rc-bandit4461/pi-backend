package enset.bdcc.pi.backend.dao;

import enset.bdcc.pi.backend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@RepositoryRestResource
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @RestResource(path = "/byEmail")
    public User getByEmailEquals(@Param("email") String email);
}
