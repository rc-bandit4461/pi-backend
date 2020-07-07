package enset.bdcc.pi.backend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class LayoutCapacity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Enumerated(EnumType.ORDINAL)
    private Layout layout;
    private Integer capacity;
    @ManyToOne
    @JoinColumn(name= "room_id")
    private Room room;




    public LayoutCapacity(Layout layout, Integer capacity) {
        this.layout = layout;
        this.capacity = capacity;
    }
    public LayoutCapacity(Layout layout, Integer capacity,Room room) {
        this.layout = layout;
        this.capacity = capacity;
        this.room = room;
    }



}
