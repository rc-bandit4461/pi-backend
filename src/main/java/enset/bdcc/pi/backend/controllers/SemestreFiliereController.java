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
    @Autowired
    private NoteModuleController noteModuleController;

    @GetMapping(value = "/toggleCloseSemestre/{id}")
    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.OK)
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

    @GetMapping(value = "/updateSemestreNotes/{id}")
    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public void updateSemestreNotesWithConsistent(@PathVariable("id") Long idSemestre) {
        SemestreFiliere semestreSession = semestreFiliereRepository.getOne(idSemestre);
        List<SemestreEtudiant> semestreEtudiantList = semestreEtudiantRepository.getAllBySessionAndNumero(semestreSession.getSession().getId(), semestreSession.getNumero());
        for (SemestreEtudiant semestreEtudiant : semestreEtudiantList) {
            semestreEtudiant.getNoteModules().forEach(noteModule -> noteModuleController.consist(noteModule));
            updateNoteSemestre(semestreEtudiant);
        }
        semestreEtudiantRepository.saveAll(semestreEtudiantList);
    }

    @GetMapping(value = "/updateSemestreEtudiantNotes/{id}")
    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public void updateSemestreEtudiantNotesWithConsistent(@PathVariable("id") Long idSemestre) {
        SemestreEtudiant semestreEtudiant = semestreEtudiantRepository.getOne(idSemestre);
        semestreEtudiant.getNoteModules().forEach(noteModule -> noteModuleController.consist(noteModule));
        updateNoteSemestre(semestreEtudiant);
    }

    @GetMapping(value = "/updateSemestreNotes/{id}/noConsist")
    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public void updateSemestreNotesWithoutConsistent(@PathVariable("id") Long idSemestre) {
        SemestreFiliere semestreSession = semestreFiliereRepository.getOne(idSemestre);
        List<SemestreEtudiant> semestreEtudiantList = semestreEtudiantRepository.getAllBySessionAndNumero(semestreSession.getSession().getId(), semestreSession.getNumero());
        for (SemestreEtudiant semestreEtudiant : semestreEtudiantList) {
            updateNoteSemestre(semestreEtudiant);
        }
    }

    @GetMapping(value = "/updateSemestreEtudiantNotes/{id}/noConsist")
    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public void updateSemestreEtudiantNotesWithoutConsistent(@PathVariable("id") Long idSemestre) {
        SemestreEtudiant semestreEtudiant = semestreEtudiantRepository.getOne(idSemestre);
        updateNoteSemestre(semestreEtudiant);
    }


    @Transactional
    public void updateNoteSemestre(SemestreEtudiant semestreEtudiant) {
        float note = 0;
        float facteur = 0;
        for (NoteModule noteModule : semestreEtudiant.getNoteModules()) {
            float max = Math.max(noteModule.getNoteNormale(), Math.max(noteModule.getNoteDeliberation(), noteModule.getNoteRatt()));
            note += max * noteModule.getModule().getFacteur();
            facteur += noteModule.getModule().getFacteur();
        }
        note /= facteur;
        semestreEtudiant.setNote(note);
    }
}
