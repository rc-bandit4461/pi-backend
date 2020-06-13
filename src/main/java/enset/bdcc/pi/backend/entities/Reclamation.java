package enset.bdcc.pi.backend.entities;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class Reclamation extends Demande implements Serializable {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Etudiant etudiant;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id")
    private Session session;

    private String detail;

    public Reclamation(String detail) {
        this.detail = detail;
    }

}
