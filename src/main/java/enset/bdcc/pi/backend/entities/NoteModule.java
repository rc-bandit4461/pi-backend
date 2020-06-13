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
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@ToString
public class NoteModule implements Serializable {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "module_id")
    Module module;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "se_id")
    SemestreEtudiant semestreEtudiant;
    @OneToMany(mappedBy = "noteModule", fetch = FetchType.LAZY)
    List<NoteElementModule> noteElementModules;
    @Id
    @GeneratedValue
    private Long id;
    @Column(updatable = false,name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date


}
