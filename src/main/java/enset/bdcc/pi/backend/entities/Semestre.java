package enset.bdcc.pi.backend.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

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
    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date



    public Semestre(int num) {
        this.numero = num;
    }

    public Semestre(int numero, boolean isDone) {
        this.numero = numero;
        this.isDone = isDone;
    }
}
