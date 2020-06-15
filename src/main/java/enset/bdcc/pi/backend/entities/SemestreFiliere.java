package enset.bdcc.pi.backend.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
//@ToString
//Lombok toString causes  stackoverflow
public class SemestreFiliere extends Semestre implements Serializable {
    //    @ManyToMany(mappedBy = "module_sf")
    @OneToMany(mappedBy = "semestreFiliere",cascade = CascadeType.ALL)
    List<Module> modules = new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "session_id", nullable = true)
    private Session session;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "filiere_id", nullable = true)
    private Filiere filiere;
    public SemestreFiliere(int numero, boolean isDone) {
        super(numero, isDone);
    }

    public SemestreFiliere(int numero, boolean isDone, Session session) {
        super(numero, isDone);
        this.session = session;
    }

    public SemestreFiliere(int numero, boolean isDone, Filiere filiere) {
        super(numero, isDone);
        this.filiere = filiere;
    }

    public SemestreFiliere(int i) {
        super(i);
    }


}
