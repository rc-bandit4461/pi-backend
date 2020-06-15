package enset.bdcc.pi.backend.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Setter
@ToString
public class SemestreEtudiant extends Semestre implements Serializable {
    //    @OneToMany(fetch = FetchType.LAZY, mappedBy = "semestreEtudiant")
//    List<DemandeReleve> demandeReleves;
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "semestreEtudiant")
//    List<Examen> examenList;
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "semestreEtudiant")
//    List<NoteModule> noteModules;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    private Session session;
    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.DETACH)
    @JoinColumn(name = "student_id")
    private Etudiant etudiant;
    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public SemestreEtudiant(int numero, boolean isDone) {
        super(numero, isDone);
    }
}
