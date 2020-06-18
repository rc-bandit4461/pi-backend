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
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Setter
@ToString

public class SemestreEtudiant extends Semestre implements Serializable {
//        @OneToMany(fetch = FetchType.LAZY, mappedBy = "semestreEtudiant")
//    List<DemandeReleve> demandeReleves;
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "semestreEtudiant")
//    List<Examen> examenList;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "semestreEtudiant",cascade = CascadeType.ALL)
    List<NoteModule> noteModules =  new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id")
    private Session session;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Etudiant etudiant;

    public SemestreEtudiant(int numero, boolean isDone) {
        super(numero, isDone);
    }
    public SemestreEtudiant(Etudiant etudiant,Session session,int numero, boolean isDone) {
        super(numero, isDone);
        this.etudiant = etudiant;this.session = session;
    }
}
