package enset.bdcc.pi.backend.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable@EqualsAndHashCode
@ToString
public class EtudiantSessionKey implements Serializable {
    @Column(name = "student_id")
    private Long etudiantId;
    @Column(name = "session_id")
    private Long sessionId;


}
