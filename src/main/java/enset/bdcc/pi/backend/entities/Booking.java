package enset.bdcc.pi.backend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;
import java.sql.Time;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Etudiant user;
    private String title;
    private Date date;
    private Time startTime;
    private Time endTime;

    public Booking(Room room, Etudiant user, String title, Date date, Time startTime, Time endTime) {
        this.room = room;
        this.user = user;
        this.title = title;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }


    public Integer getDisplayDay() {
        return this.date.toLocalDate().getDayOfMonth();
    }

    public String getDisplayMonth() {
       return this.date.toLocalDate().getMonth().toString();
    }

    public String getDisplayDayOfWeek() {
        return this.date.toLocalDate().getDayOfWeek().toString();
    }

}
