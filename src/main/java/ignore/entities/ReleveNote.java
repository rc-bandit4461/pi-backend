package ignore.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ReleveNote implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    @CreatedDate
    @Column(name="created_at")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @Column(name="updated_at")
    private LocalDateTime updatedAt;


}
