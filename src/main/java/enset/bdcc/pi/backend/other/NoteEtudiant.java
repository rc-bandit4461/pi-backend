package enset.bdcc.pi.backend.other;

import enset.bdcc.pi.backend.entities.Etudiant;
import lombok.*;

@AllArgsConstructor@NoArgsConstructor@Data@ToString
public class NoteEtudiant {

    private Etudiant etudiant;
    private float note;
}
