package enset.bdcc.pi.backend.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import enset.bdcc.pi.backend.ApiClass.ApiBooking;
import enset.bdcc.pi.backend.dao.BookingRepository;
import enset.bdcc.pi.backend.dao.EtudiantRepository;
import enset.bdcc.pi.backend.dao.RoomRepository;
import enset.bdcc.pi.backend.dao.UserRepository;
import enset.bdcc.pi.backend.entities.Booking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("*")
//@RequestMapping("/api")

public class BookingController {

    @Autowired
    BookingRepository bookRepo;
    @Autowired
    RoomRepository roomRepository;
    @Autowired
    UserRepository userRepository;

//    @GetMapping("/booking")
//    public List<Booking> getBooking() {
//        return bookRepo.findAll();
//    }

    //	@GetMapping("/booking/{dateInString}")
//	public List<Booking> getBookingByDate(@PathVariable("dateInString") String date){
//		Date dateInSql=Date.valueOf(date);
//		return bookRepo.findAllByDate(dateInSql);
//	}
    @Transactional
    @DeleteMapping("/deleteBooking/{id}")
    public void deleteBooking(@PathVariable("id") Long id) {
        bookRepo.deleteById(id);
    }

    @GetMapping("/booking/{id}")
    public Booking getBookingById(@PathVariable("id") Long id) {
        return bookRepo.findById(id).get();
    }

    @Transactional
    @PostMapping("/booking/add")
    public String save(@RequestBody ApiBooking apiBooking) {
        Booking booking = new Booking();
        booking.setDate(apiBooking.getDate());
        booking.setStartTime(java.sql.Time.valueOf(apiBooking.getStart() + ":00"));
        booking.setEndTime(java.sql.Time.valueOf(apiBooking.getEnd() + ":00"));
        booking.setTitle(apiBooking.getTitle());
        booking.setRoom(roomRepository.findById(apiBooking.getRoomId()).get());
        booking.setUser(userRepository.findById(apiBooking.getUserId()).get());

//		//Check if new reservation can be created
//		boolean bookingStatus = bookingService.getBookingStatus(booking);
        List<Booking> book1 = bookRepo.findByRoom_IdAndDateAndStartTimeBetween(booking.getRoom().getId(), booking.getDate(), booking.getStartTime(), booking.getEndTime());
        List<Booking> book2 = bookRepo.findByRoom_IdAndDateAndStartTimeIsLessThanEqualAndEndTimeIsGreaterThanEqual(booking.getRoom().getId(), booking.getDate(), booking.getStartTime(), booking.getStartTime());

        //If status is true create a new reservation
        if (book1.isEmpty() && book2.isEmpty()) {
            bookRepo.save(booking);
            return "oui";
        } else {
            return "non";
        }
    }

    @Transactional
    @PutMapping("/booking/edit/{id}")
    public Map<String,Object> updateBooking(@PathVariable("id") Long id, @RequestBody ApiBooking apiBooking) {
        Booking b = bookRepo.findById(id).get();
        b.setRoom(roomRepository.findById(apiBooking.getRoomId()).get());
        b.setUser(userRepository.findById(apiBooking.getUserId()).get());
        b.setDate(apiBooking.getDate());
        b.setEndTime(java.sql.Time.valueOf(apiBooking.getEnd()));
        b.setStartTime(java.sql.Time.valueOf(apiBooking.getStart()));
        b.setTitle(apiBooking.getTitle());

        List<Booking> book1 = bookRepo.findByRoom_IdAndDateAndStartTimeBetween(b.getRoom().getId(), b.getDate(), b.getStartTime(), b.getEndTime());
        List<Booking> book2 = bookRepo.findByRoom_IdAndDateAndStartTimeIsLessThanEqualAndEndTimeIsGreaterThanEqual(b.getRoom().getId(), b.getDate(), b.getStartTime(), b.getStartTime());
//           ObjectMapper mapper = new ObjectMapper();
        HashMap<String,Object> map = new HashMap<>();
      //Converting the Object to JSONString
//      String jsonString = mapper.writeValueAsString(std);
//      System.out.println(jsonString);

        if (book1.size() == 1 || book2.size() == 1) {
            bookRepo.save(b);
            map.put("result",true);
//            return "oui";
        } else {
            map.put("result",false);
//            return "non";
        }
        return  map;
    }
}
