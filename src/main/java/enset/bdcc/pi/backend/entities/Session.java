package enset.bdcc.pi.backend.entities;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@ToString
public class Session implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private int annee;
    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "filiere_id")
    private Filiere filiere;
//    @OneToMany(fetch = FetchType.LAZY,mappedBy = "session")
//    private List<ReclamationRepository> reclamationList;

    public Session(int annee,Filiere filiere) {
        this.annee = annee;this.filiere = filiere;
    }
}
