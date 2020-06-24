package enset.bdcc.pi.backend.controllers;

import enset.bdcc.pi.backend.dao.*;
import enset.bdcc.pi.backend.entities.*;
import enset.bdcc.pi.backend.entities.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController

public class SemestreFiliereController {
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

    @GetMapping(value = "/toggleCloseSemestre/{id}")
    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public void toggleCloseSemestre(@PathVariable("id") Long idSemestre) {
        SemestreFiliere semestreSession = semestreFiliereRepository.getOne(idSemestre);
        boolean done = !semestreSession.isDone();
        Session session = semestreSession.getSession();

        semestreSession.setDone(done);
        List<SemestreEtudiant> semestreEtudiantList = semestreEtudiantRepository.getAllBySessionAndNumero(session.getId(), semestreSession.getNumero());
        semestreEtudiantList.forEach(semestreEtudiant -> {
            semestreEtudiant.setDone(done);
        });
        semestreEtudiantRepository.saveAll(semestreEtudiantList);
        semestreFiliereRepository.save(semestreSession);
    }
}
