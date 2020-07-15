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
public class ReclamAttestation extends Demande implements Serializable {
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "student_id")
    private Etudiant etudiant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attestation_id")
    private Attestation_scolarite attestation;


    public ReclamAttestation(String detail) {
        this.detail = detail;
    }

}
