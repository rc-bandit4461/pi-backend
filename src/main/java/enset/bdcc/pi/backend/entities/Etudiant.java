package enset.bdcc.pi.backend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Etudiant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    @Column(unique = true)
    protected String cin;
    protected String nom;
    protected String prenom;
    protected String password;

    @OneToMany(mappedBy = "etudiant", fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    private List<Reclamation> reclamations = new ArrayList<>();


    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Date updatedAt; // initialize updated date
    @Column(name = "etud_session")
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "etudiant")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<EtudiantSession> etudiantSessions = new ArrayList<>();
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "etudiant")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    List<SemestreEtudiant> semestreEtudiants = new ArrayList<>();
    public Etudiant(String cin, String prenom, String nom) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
    }
}
