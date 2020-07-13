package enset.bdcc.pi.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public class User implements Serializable {
    @Transient
    public static String ROLE_STUDENT = "student";
    @Transient
    public static String ROLE_PROF = "prof";
    @Transient
    public static String ROLE_ADMIN = "admin";
    protected String email;
    protected String password;
    protected String role = User.ROLE_STUDENT;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;
    protected String sexe;
    protected String nom;
    protected String prenom;
    protected String infos;
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    protected List<Reclamation> reclamations = new ArrayList<>();
}
