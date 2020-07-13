package enset.bdcc.pi.backend.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import enset.bdcc.pi.backend.dao.*;
import enset.bdcc.pi.backend.entities.*;
import enset.bdcc.pi.backend.entities.Module;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.notFound;

@RestController
@CrossOrigin("*")
public class ReclamationController {
    @Autowired
    private NoteExamenRepository noteExamenRepository;
    @Autowired
    private ExamenRepository examenRepository;
    @Autowired
    private NoteElementModuleRepository noteElementModuleRepository;
    @Autowired
    private NoteModuleRepository noteModuleRepository;
    @Autowired
    private ElementRepository elementRepository;
    @Autowired
    private FiliereRepository filiereRepository;
    @Autowired
    private SemestreFiliereRepository semestreFiliereRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private EtudiantSessionRepository etudiantSessionRepository;
    @Autowired
    private SemestreEtudiantRepository semestreEtudiantRepository;
    @Autowired
    private DemandeReleveRepository demandeReleveRepository;
    @Autowired
    private DemandeAttestationRepository demandeAttestationRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReclamationRepository reclamationRepository;

    @PostMapping(value = "/saveReclamation")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void saveReclamation(@RequestBody Reclamation reclamation) {

        User user = userRepository.getOne(reclamation.getUser().getId());
        reclamation.setUser(user);
        reclamationRepository.save(reclamation);
    }

    @PostMapping(value = "/resolveReclamation/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void resolveReclamation(@PathVariable("id") Long id,@RequestBody Reclamation reclamation) {
        Reclamation demande = reclamationRepository.getOne(id);
                demande.setFeedback(reclamation.getFeedback());

        processReclamation(demande, false);

    }

    @PostMapping(value = "/denyReclamation/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void denyReclamation(@PathVariable("id") Long id,@RequestBody Reclamation reclamation) {

        Reclamation demande = reclamationRepository.getOne(id);
        demande.setFeedback(reclamation.getFeedback());
        processReclamation(demande, true);

    }

    public void processReclamation(Reclamation demande, boolean state) {
        demande.setDone(true);
        demande.setRejected(state);
        demande.setDone(true);
        reclamationRepository.save(demande);

    }

    @GetMapping(value = "/makeReclamationSeen/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void makeReleveRequesSeen(@PathVariable("id") Long id) {
        Reclamation demande = reclamationRepository.getOne(id);
        demande.setSeen(true);
        reclamationRepository.save(demande);
    }

    @DeleteMapping(value = "/deleteReclamation/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void deleteRequest(@PathVariable("id") Long id) {
        Reclamation demande = reclamationRepository.getOne(id);
        reclamationRepository.delete(demande);
    }
}
