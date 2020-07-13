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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    private String feedback;


    public Reclamation(String detail) {
        this.type = Demande.TYPE_RECLAMATION;
        this.detail = detail;
    }

}
