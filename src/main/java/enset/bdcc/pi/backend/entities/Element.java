package enset.bdcc.pi.backend.entities;

import enset.bdcc.pi.backend.entities.Module;
import lombok.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

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
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
//@ToString
public class Element implements Serializable {
    @ManyToMany(mappedBy = "elements",cascade = CascadeType.REMOVE)
    List<Module> modules = new ArrayList<>();
    //IMPORTANT,  PEUT CAUSER DES PROBLEMES
    @OneToMany(mappedBy = "element", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    List<NoteElementModule> noteElementModules = new ArrayList<>();
    @OneToMany(mappedBy = "element", fetch = FetchType.LAZY,cascade = CascadeType.REMOVE)
    List<Examen> examens = new ArrayList<>();

    @Id
    @GeneratedValue
    private Long id;
    private String libelle;
    @Column(updatable = false,name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date

    public Element(String libele) {
        this.libelle = libele;
    }


}
