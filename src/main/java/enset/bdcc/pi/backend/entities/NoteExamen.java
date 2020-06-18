package enset.bdcc.pi.backend.entities;

import lombok.*;
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
@AllArgsConstructor
public class NoteExamen implements Serializable {
    @Id
    @GeneratedValue
    Long id;
    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date
    private float note;
    @ManyToOne
    @JoinColumn(name = "noteElementModule")
    private NoteElementModule noteElementModule;
    @ManyToOne
    @JoinColumn(name = "examen")
    private Examen examen;
    public NoteExamen(Examen examen,NoteElementModule noteElementModule,float note){
        this.examen = examen;
        this.noteElementModule = noteElementModule;
        this.note = note;
    }
}
