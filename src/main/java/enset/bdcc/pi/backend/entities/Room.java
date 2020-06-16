package enset.bdcc.pi.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private int id;

    private String name;

    private String location;
    private int seats;
    @OneToOne(cascade = {CascadeType.ALL})
    private State state;
//    @OneToMany(mappedBy = "room")
//    private List<Booking> bookings;

    @OneToMany(cascade = {CascadeType.ALL})
    private List<LayoutCapacity> layoutCapacities;

    public Room(String name, String location,int seats) {
        this.name = name;
        this.location = location;
        this.seats=seats;
        layoutCapacities = new ArrayList<>();
        for (Layout layout : Layout.values()) {
            layoutCapacities.add(new LayoutCapacity(layout, 0));
        }
    }
    public Room(String name, String location,int seats,State state) {
        this.name = name;
        this.location = location;
        this.seats=seats;
        this.state=state;
        layoutCapacities = new ArrayList<>();
        for (Layout layout : Layout.values()) {
            layoutCapacities.add(new LayoutCapacity(layout, 0));
        }
    }




    public List<LayoutCapacity> getCapacities() {
        return layoutCapacities;
    }

    public void setCapacities(List<LayoutCapacity> capacities) {this.layoutCapacities = capacities;}

    public void setCapacity(LayoutCapacity capacity) {
        for (LayoutCapacity lc : layoutCapacities) {
            if (lc.getLayout() == capacity.getLayout()) {
                lc.setCapacity(capacity.getCapacity());
            }
        }
    }
}
