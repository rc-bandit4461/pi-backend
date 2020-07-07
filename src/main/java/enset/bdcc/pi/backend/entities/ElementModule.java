package enset.bdcc.pi.backend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Entity
@Data
@ToString
@Table(name = "element_module_facteur")
public class ElementModule implements Serializable {
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "elementModule")
    List<NoteElementModule> noteElementModules = new ArrayList<>();
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "element_id")
    private Element eLement;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "module_id")
    private Module module;
    private double facteur = 1;
    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
