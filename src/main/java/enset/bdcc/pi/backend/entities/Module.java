package enset.bdcc.pi.backend.entities;

import enset.bdcc.pi.backend.entities.SemestreFiliere;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.rest.core.annotation.RestResource;

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
//@ToString
public class Module implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String libelle;
    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date
    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "module")
    private List<Examen> examens = new ArrayList<>();
    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "module_element",
            joinColumns = @JoinColumn(name = "module_id"),
            inverseJoinColumns = @JoinColumn(name = "element_id"))
    private List<Element> elements = new ArrayList<>();
    //    @ManyToMany
//    @JoinTable(name = "module_sf",
//            joinColumns = @JoinColumn(name = "module_id"),
//            inverseJoinColumns = @JoinColumn(name = "sf_id"))
//    private List<SemestreFiliere> semestreFilieres = new ArrayList<>();
    //update
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "sf_id")
    private SemestreFiliere semestreFiliere;


    @OneToMany(mappedBy = "module", fetch = FetchType.LAZY)
    private List<NoteModule> noteModules = new ArrayList<>();

    public Module(String libelle) {
        this.libelle = libelle;
    }


}
