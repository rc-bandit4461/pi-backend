package enset.bdcc.pi.backend.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)

public abstract class Demande implements Serializable {
    @Id
    @GeneratedValue

    protected Long id;
    @Column(name = "is_done")
    protected boolean isDone = false;
    @Column(updatable = false,name = "created_at")
    @CreationTimestamp
    private Date createdAt; // initialize created date
    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt; // initialize updated date

    public Demande(boolean isDone) {
        this.isDone = isDone;
    }
}
