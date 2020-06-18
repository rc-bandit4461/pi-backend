package enset.bdcc.pi.backend.entities;

import enset.bdcc.pi.backend.entities.Element;
import enset.bdcc.pi.backend.entities.EtudiantSessionKey;
import enset.bdcc.pi.backend.entities.NoteModule;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@ToString
public class NoteElementModule implements Serializable {
//    @EmbeddedId
//    EtudiantSessionKey id;
//    @ManyToOne
//    @MapsId("notemodule_id")
//    @JoinColumn(name = "notemodule_id")
//    NoteModule noteModule;
//    @ManyToOne
//    @MapsId("element_id")
//    @JoinColumn(name = "element_id")
//    private Element element;
  @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "notemodule_id")
    NoteModule noteModule;
    @ManyToOne
    @JoinColumn(name = "element_id")
    private Element element;
//    @OneToMany(fetch = FetchType.LAZY,mappedBy = "noteElementModule",cascade = CascadeType.REMOVE)
//    private List<Examen> examenList = new ArrayList<>();
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<NoteExamen> noteExamens = new ArrayList<>();
    @Column(name = "note_normale")
    private float noteNormale;
    @Column(name = "note_delib")
    private float noteDeliberation;
    @Column(name = "note_ratt")
    private float noteRatt;
    private float pourcentage = 1;
    @Column(name = "is_ratt")
    private boolean isRatt = false;
    @Column(updatable = false,name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date
    public NoteElementModule(NoteModule noteModule, Element element){
          this.noteModule = noteModule;
          this.element = element;
    }
    public NoteElementModule(float noteNormale, float noteRatt, float noteDeliberation, boolean isRatt) {
        this.isRatt = isRatt;
        this.noteNormale = noteNormale;
        this.noteRatt = noteRatt;
        this.noteDeliberation = noteDeliberation;
    }
}
