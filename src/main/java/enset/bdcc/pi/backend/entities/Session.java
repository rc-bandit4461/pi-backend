package enset.bdcc.pi.backend.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
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

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@ToString
public class Session implements Serializable {
    @Column(name = "etud_session")
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "session", orphanRemoval = true, fetch = FetchType.LAZY)
    List<EtudiantSession> etudiantSessions = new ArrayList<>();
    @Id
    @GeneratedValue
    private Long id;
    private int annee;
    private int annee_courante = 1;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "filiere_id")
    private Filiere filiere;
    @JsonProperty("is_done")
    @Column(name = "is_done")
    private boolean isDone = false;
    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @OneToMany(cascade = CascadeType.ALL,mappedBy = "session")
    private List<SemestreEtudiant> semestreEtudiants = new ArrayList<>();
        @OneToMany(cascade = CascadeType.ALL,mappedBy = "session")
    private List<SemestreFiliere> semestreFilieres = new ArrayList<>();
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize created date
    @Transient
    List<Etudiant> etudiants;
    public Session(int annee, Filiere filiere) {
        this.annee = annee;
        this.filiere = filiere;
    }
}
