package enset.bdcc.pi.backend.controllers;

import java.util.List;

import enset.bdcc.pi.backend.entities.Demande;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import enset.bdcc.pi.backend.dao.ReclamAttestationRepository;
import enset.bdcc.pi.backend.entities.ReclamAttestation;

import javax.transaction.Transactional;

@RestController
@CrossOrigin("*")
public class ReclamAttestationController {

	@Autowired
	private ReclamAttestationRepository reclamationRepository;

	@GetMapping(path="/attestReclamAttestations")
	public List<ReclamAttestation> getReclamAttestations(){
		return reclamationRepository.findAll();
	}

	@GetMapping(path="/attestReclamAttestation/{id}")
	public ReclamAttestation getReclamAttestation(@PathVariable Long id) {
		return reclamationRepository.findById(id).get();
	}

	@PostMapping(path="/attestReclamAttestation")
	public ReclamAttestation saveReclamAttestation(@RequestBody ReclamAttestation reclam) {
		reclam.setLibelle("Attestation");
		reclam.setType(Demande.TYPE_RECLAMATION);
		return reclamationRepository.save(reclam);
	}

	@DeleteMapping(path="/attestReclamAttestation/{id}")
	public void deleteReclamAttestation(@PathVariable Long id) {
		reclamationRepository.deleteById(id);
	}

	@PutMapping(path="/attestReclamAttestation/{id}")
	public ReclamAttestation updateReclamAttestation(@RequestBody ReclamAttestation reclam,@PathVariable Long id) {
		System.out.println("Updating : " + id);
		ReclamAttestation reclamation = reclamationRepository.findById(id).get();
		reclamation.setId(reclam.getId());
		reclamation = reclam;
		return reclamationRepository.save(reclamation);
	}
	@PostMapping(value = "/resolveReclamAttestation/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void resolveReclamAttestation(@PathVariable("id") Long id,@RequestBody ReclamAttestation reclamation) {
        ReclamAttestation demande = reclamationRepository.getOne(id);
                demande.setFeedback(reclamation.getFeedback());

        processReclamAttestation(demande, false);

    }

    @PostMapping(value = "/denyReclamAttestation/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void denyReclamAttestation(@PathVariable("id") Long id,@RequestBody ReclamAttestation reclamation) {

        ReclamAttestation demande = reclamationRepository.getOne(id);
        demande.setFeedback(reclamation.getFeedback());
        processReclamAttestation(demande, true);

    }

    public void processReclamAttestation(ReclamAttestation demande, boolean state) {
        demande.setDone(true);
        demande.setRejected(state);
        demande.setDone(true);
        reclamationRepository.save(demande);

    }

    @GetMapping(value = "/makeReclamAttestationSeen/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void makeReleveRequesSeen(@PathVariable("id") Long id) {
        ReclamAttestation demande = reclamationRepository.getOne(id);
        demande.setSeen(true);
        reclamationRepository.save(demande);
    }

    @DeleteMapping(value = "/deleteReclamAttestation/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void deleteRequest(@PathVariable("id") Long id) {
        ReclamAttestation demande = reclamationRepository.getOne(id);
        reclamationRepository.delete(demande);
    }

}
