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
public class DemandeReleve extends Demande implements Serializable {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "se_id")
    SemestreEtudiant semestreEtudiant;
}
