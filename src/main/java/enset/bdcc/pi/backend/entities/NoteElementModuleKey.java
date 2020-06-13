package enset.bdcc.pi.backend.entities;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@Embeddable
@EqualsAndHashCode
@ToString
//deprecated
//Not used anymore
public class NoteElementModuleKey implements Serializable {
    @Column(name = "notemodule_id")
    private Long noteModuleId;
    @Column(name = "element_id")
    private Long elementId;


}
