package enset.bdcc.pi.backend.controllers;

import enset.bdcc.pi.backend.ApiClass.ApiRoom;
import enset.bdcc.pi.backend.dao.EtudiantRepository;
import enset.bdcc.pi.backend.dao.RoomRepository;
import enset.bdcc.pi.backend.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RoomController {
	
	@Autowired
	private RoomRepository roomRepository;
	
	@Autowired
	private EtudiantRepository etudiantRepository;
	
	@DeleteMapping("/deleteUser/{id}")
	public void deleteEtudient(@PathVariable("id")Long id) {
		etudiantRepository.deleteById(id);
	}
	
	@DeleteMapping("/deleteRoom/{id}")
	public void deleteRoom(@PathVariable("id") int id) {
		 roomRepository.deleteById(id);
	}
	
	@GetMapping("/rooms")
	public List<Room> getAllRooms(){
		return roomRepository.findAll();		
	}
	
	@GetMapping("/room/{id}")
	public ApiRoom getRoom(@PathVariable("id") int id) {
		Room r1=roomRepository.findById(id).get();
		ApiRoom room=new ApiRoom();
		room.setId(r1.getId());
		room.setName(r1.getName());
		room.setLocation(r1.getLocation());
		room.setSeats(r1.getSeats());
		room.setState(r1.getState().getStates());
		room.setDetail(r1.getState().getDetail());
		for (LayoutCapacity l:r1.getCapacities()){
			if (l.getLayout().getDescription()=="pc")room.setPcs(l.getCapacity());
			if (l.getLayout().getDescription()=="Projector")room.setProjector(l.getCapacity());
			if (l.getLayout().getDescription()=="BOARD")room.setBoard(l.getCapacity());
		}
		return room;
	}
	
	@PostMapping("/room/add")
	public Room addRoom(@RequestBody ApiRoom r1) {
		Room room1 = new Room(r1.getName(),r1.getLocation(),r1.getSeats(),new State(true,""));
		room1.setCapacity(new LayoutCapacity(Layout.PCs,r1.getPcs()));
		room1.setCapacity(new LayoutCapacity(Layout.Projector,r1.getProjector()));
		room1.setCapacity(new LayoutCapacity(Layout.BOARD,r1.getBoard()));

		return roomRepository.save(room1);
	}
	
	@PutMapping(value="/room/{id}")
	public Room updateRoom(@PathVariable("id") int id, @RequestBody ApiRoom r1) {
		Room room=roomRepository.findById(id).get();
		room.setName(r1.getName());
		room.setLocation(r1.getLocation());
		room.setSeats(r1.getSeats());
		room.getState().setStates(r1.getState());
		room.getState().setDetail(r1.getDetail());
		for (LayoutCapacity l:room.getCapacities()){
			if (l.getLayout().getDescription()=="pc")l.setCapacity(r1.getPcs());
			if (l.getLayout().getDescription()=="Projector")l.setCapacity(r1.getProjector());
			if (l.getLayout().getDescription()=="BOARD")l.setCapacity(r1.getBoard());
		}

		return roomRepository.save(room);
	}

	@GetMapping(value="/users")
	public List<Etudiant> getAllEtudient(){return etudiantRepository.findAll();
	}
	
	
	@GetMapping("/user/{id}")
	public Etudiant getEtudientById(@PathVariable("id") Long id){
		return etudiantRepository.findById(id).get();
	}
	

	
	@PostMapping("/user")
	public Etudiant addEtudient(@RequestBody Etudiant aUser) {
		return etudiantRepository.save(aUser);
	}
	
}
