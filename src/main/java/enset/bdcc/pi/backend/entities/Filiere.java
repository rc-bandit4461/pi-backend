package enset.bdcc.pi.backend.entities;

import enset.bdcc.pi.backend.entities.SemestreFiliere;
import enset.bdcc.pi.backend.entities.Session;
import lombok.*;
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
@AllArgsConstructor
@Entity
@ToString
public class Filiere implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String libelle;
    private String description;
    private int nbr_semestres;
    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "filiere")
    private List<Session> sessions = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "filiere", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<SemestreFiliere> semestreFilieres = new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    private Diplome diplome;


    public Filiere(String libelle, List<SemestreFiliere> list) {
        this.libelle = libelle;
        this.semestreFilieres = list;
    }

    public Filiere(String libelle, String description,int nbr_semestres, Diplome diplome) {
        this.libelle = libelle;
        this.nbr_semestres = nbr_semestres;
        this.description = description;
        this.diplome = diplome;
    }
}
