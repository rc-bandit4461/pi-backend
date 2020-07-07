package enset.bdcc.pi.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Diplome implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String libelle;
    private String description;
    private int nbr_annee;
    @OneToMany(mappedBy = "diplome")
    private List<Filiere> filieres = new ArrayList<>();
    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "session")
    private List<SemestreEtudiant> semestreEtudiants = new ArrayList<>();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "session")
    private List<SemestreFiliere> semestreFilieres = new ArrayList<>();
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize created date

    public Diplome(String libelle, String description, int nbr_annee) {
        this.libelle = libelle;
        this.description = description;
        this.nbr_annee = nbr_annee;
    }

}
