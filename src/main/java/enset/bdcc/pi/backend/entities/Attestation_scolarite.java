package enset.bdcc.pi.backend.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor @ToString
public class Attestation_scolarite implements Serializable {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long codeEtudiant;
	private String nomComplet;
	private String cin;
	private String cne;
	@Temporal(TemporalType.DATE)
	private Date date_naissance;
	private String ville_naissance;
	private String annee_session;
	private String type_diplome;
	private String annee_univers;
	private int nbr_telechargement=2;

	@Column(columnDefinition="tinyint(1) default 0")
	private boolean state_completion;

	@Column(updatable = false,name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date

	@ManyToOne()
	private Etudiant etudiant;

    @OneToMany(mappedBy = "attestation")
    @JsonProperty(access=Access.WRITE_ONLY)
    Collection<ReclamAttestation>reclamations;

}
