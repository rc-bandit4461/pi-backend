package enset.bdcc.pi.backend.dao;

import enset.bdcc.pi.backend.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

@RepositoryRestResource
@CrossOrigin("*")
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findAllByDate(Date date);
    List<Booking> findByRoom_IdAndDateAndStartTimeBetween(int id, Date date, Time start, Time end);
    List<Booking> findByRoom_IdAndDateAndStartTimeIsLessThanEqualAndEndTimeIsGreaterThanEqual(int id, Date date, Time start, Time start1);

    @Query(value = "FROM Booking b WHERE b.date = ?1 and b.room_id = ?2 and ( ?3 BETWEEN b.startTime AND  b.endTime\r\n" +
            "    OR\r\n" +
            "    b.start_time BETWEEN ?3 AND ?4)", nativeQuery = true)
    List<Object[]> getConfirmedReservations(Date date, int room_id, Time start_time, Time end_time);
}
