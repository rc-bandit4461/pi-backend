package enset.bdcc.pi.backend.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
public class BookingCommand {

    private int id;
    private Room room;
    private Etudiant user;
    private String title;
    private Date date;
    private String startTime;
    private String endTime;


    public BookingCommand(Booking booking) {
        this.id = booking.getId();
        this.room = booking.getRoom();
        this.user = booking.getUser();
        this.title = booking.getTitle();
        if (booking.getDate() != null)
            this.date = booking.getDate();
        if (booking.getStartTime() != null)
            this.startTime = booking.getStartTime().toString();
        if (booking.getEndTime() != null)
            this.endTime = booking.getEndTime().toString();
    }



    public Booking toBooking() {
        java.sql.Time xStartTime = java.sql.Time.valueOf(startTime + ":00");
        java.sql.Time xEndTime = java.sql.Time.valueOf(endTime + ":00");
        Booking booking = new Booking(room,user,title,date,xStartTime,xEndTime);
        booking.setId(id);
        return booking;
    }
}
