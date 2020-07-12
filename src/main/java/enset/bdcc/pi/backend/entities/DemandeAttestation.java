package enset.bdcc.pi.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class DemandeAttestation extends Demande implements Serializable {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "etudiant_id")
    protected Etudiant etudiant;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id")
    protected Session session;
}
