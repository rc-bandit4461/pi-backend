package enset.bdcc.pi.backend.entities;

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
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public abstract class Demande implements Serializable {
    @Transient
    public static String TYPE_CERTIF_SCOLARITE = "scolarite";
    @Transient
    public static String TYPE_CERTIF_GRADUATION = "graduation";
    @Transient
    public static String TYPE_RECLAMATION = "reclamation";
    @Transient
    public static String TYPE_RELEVE = "releve";
    @Id
    @GeneratedValue
    protected Long id;
    @Column(name = "is_done")
    protected boolean isDone = false;
    protected boolean isRejected = false;
    protected String libelle;
    protected String detail;
    protected String type = null;
    protected String feedback;
    protected boolean isSeen = false;
    @Column(updatable = false, name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date

    public Demande(boolean isDone) {
        this.isDone = isDone;
    }
}
