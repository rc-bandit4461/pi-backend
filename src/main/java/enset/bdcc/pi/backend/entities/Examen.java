package enset.bdcc.pi.backend.entities;

import enset.bdcc.pi.backend.entities.SemestreEtudiant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Examen implements Serializable {
    @Column(updatable = false,name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "nem_id")
    NoteElementModule noteElementModule;
    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime date;
    private float note;
    private float facteur;
}
