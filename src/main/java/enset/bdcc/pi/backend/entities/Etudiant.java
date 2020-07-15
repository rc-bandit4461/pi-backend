package enset.bdcc.pi.backend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Etudiant extends User implements Serializable {


    @Column(unique = true)
    protected String cin;
    protected String cne;

    protected LocalDate date_naissance;
    protected String ville_naissance;

    @Column(name = "etud_session")
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "etudiant")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<EtudiantSession> etudiantSessions = new ArrayList<>();
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "etudiant")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<SemestreEtudiant> semestreEtudiants = new ArrayList<>();
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "etudiant", fetch = FetchType.LAZY)
    List<DemandeAttestation> demandeAttestations = new ArrayList<>();

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "etudiant")
    private List<NoteExamen> examens = new ArrayList<>();
    @OneToMany(mappedBy = "etudiant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private List<ReclamAttestation> reclamAttestations = new ArrayList<>();
    @OneToMany(mappedBy = "etudiant")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Collection<Attestation_scolarite> attestations;
    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date

    public Etudiant(String cin, String prenom, String nom, String cne, String sexe, LocalDate date_naissance, String ville_naissance, String email, String infos) {
        this.cin = cin;
        this.cne = cne;
        this.sexe = sexe;
        this.date_naissance = date_naissance;
        this.ville_naissance = ville_naissance;
        this.email = email;
        this.infos = infos;
        this.nom = nom;
        this.prenom = prenom;
    }

    public Etudiant(String cin, String prenom, String nom, String password, String cne, String sexe, LocalDate date_naissance, String ville_naissance, String email, String infos) {
        this(cin, prenom, nom, cne, sexe, date_naissance, ville_naissance, email, infos);
        this.password = password;
    }

    public Etudiant(String cin, String prenom, String nom) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
    }
}
