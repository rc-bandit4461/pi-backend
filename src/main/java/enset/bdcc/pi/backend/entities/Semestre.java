package enset.bdcc.pi.backend.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Semestre implements Serializable {
    @Id
    @GeneratedValue
    protected Long id;
    protected int numero;
    protected boolean isDone = false;
    public Semestre(int num){
        this.numero = num;
    }
    public Semestre(int numero, boolean isDone) {
        this.numero = numero;
        this.isDone = isDone;
    }
}
