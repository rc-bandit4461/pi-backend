package enset.bdcc.pi.backend.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum Layout {
    PCs("pc"),
    Projector("Projector"),
    BOARD("BOARD");

    private String description;

    public String getDescription() {
        return description;
    }

}
