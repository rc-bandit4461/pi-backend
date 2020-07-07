package enset.bdcc.pi.backend.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
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
@Getter
@Setter
@NoArgsConstructor
@ToString
public class EtudiantSession implements Serializable {
    @EmbeddedId

    EtudiantSessionKey id;
    @ManyToOne
    @MapsId("student_id")
    @JoinColumn(name = "student_id")
    Etudiant etudiant;
    @ManyToOne
    @MapsId("session_id")
    @JoinColumn(name = "session_id")
    Session session;
    @JsonProperty("is_passed")
    private boolean isPassed = false;
    @JsonProperty("is_dropped")
    private boolean isDropped = false;
    private float note = 0;
    @Column(updatable = false,name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date
    public EtudiantSession(Etudiant etudiant,Session session){
        this.etudiant = etudiant;
        this.session = session;
        id = new EtudiantSessionKey(etudiant.getId(),session.getId());
    }
    public void generateKeyFromCurrentAttributes(){
        id = new EtudiantSessionKey(etudiant.getId(),session.getId());

    }

}
