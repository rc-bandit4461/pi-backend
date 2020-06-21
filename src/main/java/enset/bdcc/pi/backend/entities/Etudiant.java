package enset.bdcc.pi.backend.entities;

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
import java.util.Date;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Etudiant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(unique = true)
    protected String cin;
    protected String nom;
    protected String prenom;
    protected String password;
    @Column(name = "etud_session")
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "etudiant")
    List<EtudiantSession> etudiantSessions = new ArrayList<>();
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "etudiant")
    List<SemestreEtudiant> semestreEtudiants = new ArrayList<>();
    private String cne;
    private String sexe;
    private LocalDate date_naissance;
    private String ville_naissance;
    private String email;
    private String infos;
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "etudiant")
    private List<NoteExamen> examens = new ArrayList<>();
    @OneToMany(mappedBy = "etudiant", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Reclamation> reclamations = new ArrayList<>();
    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
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
}
