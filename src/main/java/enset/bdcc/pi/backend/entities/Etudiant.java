package enset.bdcc.pi.backend.entities;

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

    @OneToMany(mappedBy = "etudiant",fetch = FetchType.EAGER)
    private List<Reclamation> reclamationList = new ArrayList<>();



    @Column(updatable = false,name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date



    public Etudiant(String cin, String nom, String prenom) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
    }
}
