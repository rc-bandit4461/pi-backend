package enset.bdcc.pi.backend.entities;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import enset.bdcc.pi.backend.entities.SemestreEtudiant;
import enset.bdcc.pi.backend.other.NoteEtudiant;
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

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Examen implements Serializable {
    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date

    //    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "nem_id")
//    NoteElementModule noteElementModule;
    @OneToMany(mappedBy = "examen", cascade = CascadeType.REMOVE)
    private List<NoteExamen> noteExamens = new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module")
    private Module module;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "element")
    private Element element;
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime date;
    private float facteur;
    private String description;
    @Transient
    @JsonInclude
    private Session session;
//    @Transient
//    @JsonInclude
//    private int numero = 1; //numero de semestre
    @JsonInclude
    @Transient
    private List<NoteEtudiant> noteEtudiants = new ArrayList<>();
    @Column(name = "is_ratt",nullable = false)
    @JsonProperty("is_ratt")
    private boolean isRatt = false;

}
