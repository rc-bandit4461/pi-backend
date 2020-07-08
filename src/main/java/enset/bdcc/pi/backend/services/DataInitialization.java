package enset.bdcc.pi.backend.services;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import enset.bdcc.pi.backend.dao.BookingRepository;
import enset.bdcc.pi.backend.dao.RoomRepository;
import enset.bdcc.pi.backend.dao.*;
import enset.bdcc.pi.backend.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
public class DataInitialization {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    EtudiantRepository etudiantRepository;


    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    BookingRepository bookingRepository;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddlAuto;

    @EventListener(ApplicationReadyEvent.class)
    public void initData() {
        if (ddlAuto.equals("update")) return;
        List<Room> rooms = roomRepository.findAll();
        if (rooms.size() == 0) {
            Room blueRoom = new Room("Salle 64", "DMI 1st Floor", 38, new State(true, ""));
            blueRoom.setCapacity(new LayoutCapacity(Layout.BOARD, 1, blueRoom));
            blueRoom.setCapacity(new LayoutCapacity(Layout.Projector, 2, blueRoom));
            blueRoom.setCapacity(new LayoutCapacity(Layout.PCs, 16, blueRoom));
            roomRepository.save(blueRoom);

            Room r2 = new Room("Salle 65", "DMI 1st Floor", 38, new State(true, ""));
            r2.setCapacity(new LayoutCapacity(Layout.BOARD, 1, r2));
            r2.setCapacity(new LayoutCapacity(Layout.Projector, 2, r2));
            r2.setCapacity(new LayoutCapacity(Layout.PCs, 19, r2));
            roomRepository.save(r2);

            Room r3 = new Room("Salle 74", "DMI 2st Floor", 38, new State(true, ""));
            r3.setCapacity(new LayoutCapacity(Layout.BOARD, 2, r3));
            r3.setCapacity(new LayoutCapacity(Layout.Projector, 2, r3));
            r3.setCapacity(new LayoutCapacity(Layout.PCs, 20, r3));
            roomRepository.save(r3);
            Room r4 = new Room("Salle 75", "DMI 2st Floor", 38, new State(true, ""));
            r4.setCapacity(new LayoutCapacity(Layout.BOARD, 1, r4));
            r4.setCapacity(new LayoutCapacity(Layout.Projector, 1, r4));
            r4.setCapacity(new LayoutCapacity(Layout.PCs, 12, r4));
            roomRepository.save(r4);
            Room r5 = new Room("Laboratoir", "DMI 2st Floor", 38, new State(true, ""));
            r5.setCapacity(new LayoutCapacity(Layout.BOARD, 2, r5));
            r5.setCapacity(new LayoutCapacity(Layout.Projector, 2, r5));
            r5.setCapacity(new LayoutCapacity(Layout.PCs, 2, r5));
            roomRepository.save(r5);


        Etudiant user = new Etudiant("ST12486", "Yassir", "BOURAS",passwordEncoder.encode("123"), "1525486868788", "homme", LocalDate.of(1998, 4, 7), "Casablanca", "yassir.bouras@gmail.com", "Pervert");
            etudiantRepository.save(user);

            Booking booking1 = new Booking();
            booking1.setDate(new java.sql.Date(new Date().getTime()));
            booking1.setStartTime(java.sql.Time.valueOf("11:00:00"));
            booking1.setEndTime(java.sql.Time.valueOf("12:30:00"));
            booking1.setTitle("Exam IA");
            booking1.setRoom(blueRoom);
            booking1.setUser(user);
            bookingRepository.save(booking1);

            Booking booking2 = new Booking();
            booking2.setDate(new java.sql.Date(new Date().getTime()));
            booking2.setStartTime(java.sql.Time.valueOf("09:00:00"));
            booking2.setEndTime(java.sql.Time.valueOf("11:30:00"));
            booking2.setTitle("Exam JEE");
            booking2.setRoom(r2);
            booking2.setUser(user);
            bookingRepository.save(booking2);

            Booking booking3 = new Booking();
            booking3.setDate(new java.sql.Date(new Date().getTime()));
            booking3.setStartTime(java.sql.Time.valueOf("14:00:00"));
            booking3.setEndTime(java.sql.Time.valueOf("16:00:00"));
            booking3.setTitle("Exam IOT");
            booking3.setRoom(r4);
            booking3.setUser(user);
            bookingRepository.save(booking3);
        }
    }
}
