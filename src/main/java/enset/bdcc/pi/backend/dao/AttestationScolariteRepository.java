package enset.bdcc.pi.backend.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import enset.bdcc.pi.backend.entities.Attestation_scolarite;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin("*")
@RepositoryRestResource
public interface AttestationScolariteRepository extends JpaRepository<Attestation_scolarite, Long> {

	@Query("select a from Attestation_scolarite a where a.codeEtudiant=:x")
	public Attestation_scolarite findAttestationByCodeEtudiant(@Param("x") Long id);

}
