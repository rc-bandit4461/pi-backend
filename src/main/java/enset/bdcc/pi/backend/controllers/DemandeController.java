package enset.bdcc.pi.backend.controllers;

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

@CrossOrigin("*")
@RestController

public class DemandeController {
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

    @PostMapping(value = "/saveDemandeReleve")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public DemandeReleve saveDemande(@RequestBody DemandeReleve demandeReleve) {

        SemestreEtudiant semestreEtudiant = semestreEtudiantRepository.getOne(demandeReleve.getSemestreEtudiant().getId());
        demandeReleve.setSemestreEtudiant(semestreEtudiant);

        semestreEtudiant.getDemandeReleves().add(demandeReleve);
        semestreEtudiantRepository.save(semestreEtudiant);
        demandeReleveRepository.save(demandeReleve);
        DemandeReleve demandeReleve1 = new DemandeReleve();
        demandeReleve1.setCreatedAt(demandeReleve.getCreatedAt());
        return demandeReleve1;

    }
}
