package enset.bdcc.pi.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import enset.bdcc.pi.backend.dao.*;
import enset.bdcc.pi.backend.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.web.servlet.function.ServerResponse.status;

@CrossOrigin("*")
@RestController
public class NoteModuleController {
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

    @PutMapping(value = "/putNoteModules")
    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public void updateNoteModule(@RequestBody List<NoteElementModule> newNEMs) throws JsonProcessingException {
        List<NoteModule> noteModuleList = new ArrayList<>();
        boolean foundConsistent = false;
        for (NoteElementModule newNEM : newNEMs) {
            NoteElementModule oldNEM = noteElementModuleRepository.getOne(newNEM.getId());
            oldNEM.setRatt(newNEM.isRatt());
            oldNEM.setNoteDeliberation(newNEM.getNoteDeliberation());
            if (!foundConsistent)
                if (oldNEM.getNoteNormale() != newNEM.getNoteNormale() || oldNEM.getNoteRatt() != newNEM.getNoteRatt()) {
                    oldNEM.setConsistent(false);
                    oldNEM.getNoteModule().setConsistent(false);
                    foundConsistent = true;
                }

            oldNEM.setNoteNormale(newNEM.getNoteNormale());
            oldNEM.setNoteRatt(newNEM.getNoteRatt());
            noteModuleList.add(oldNEM.getNoteModule());
            oldNEM.setRatt(newNEM.isRatt());
            noteElementModuleRepository.save(oldNEM);
        }

    }
    @Transactional
    void consisteNoteElementModule(NoteElementModule noteElementModule) {
        float noteNormale = 0, noteRatt = 0;
        float facteurNormale = 0, facteurRatt = 0;
        List<Examen> examenList = new ArrayList<>();
        for (NoteExamen noteExamen : noteElementModule.getNoteExamens()) {
            if (!examenList.contains(noteExamen.getExamen())) {
                examenList.add(noteExamen.getExamen());
            }
            System.out.println(noteExamen.getNote());
            if (noteExamen.getExamen().isRatt()) {
                noteRatt += noteExamen.getNote() * noteExamen.getExamen().getFacteur();

            } else {
                noteNormale += noteExamen.getNote() * noteExamen.getExamen().getFacteur();

            }
        }
        for (Examen examen : examenList) {
            if (examen.isRatt()) {
                facteurRatt = facteurRatt + examen.getFacteur();
            } else {
                facteurNormale = facteurNormale + examen.getFacteur();
            }

        }

        if (facteurRatt != 0) {
            noteElementModule.setRatt(true);
            noteRatt /= facteurRatt;
            noteElementModule.setRatt(true);
        }
        if (facteurNormale != 0) {
            noteNormale /= facteurNormale;
        }

        noteElementModule.setConsistent(true);
        noteElementModule.setNoteRatt(noteRatt);
        noteElementModule.setNoteNormale(noteNormale);
        noteElementModule.setNoteDeliberation(noteNormale);
        noteElementModuleRepository.save(noteElementModule);
    }

    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/consisteNoteElementModule/{id}")
    public ResponseEntity consistNoteElementModule(@PathVariable(name = "id") Long nemId) throws JsonProcessingException {
        NoteElementModule noteElementModule = noteElementModuleRepository.getOne(nemId);
        consisteNoteElementModule(noteElementModule);
        return new ResponseEntity(HttpStatus.OK);

    }

    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/consisteNoteModule/{id}")
    public void consisteNoteModule(@PathVariable(name = "id") Long id) {
        NoteModule noteModule = noteModuleRepository.getOne(id);
        float facteur = 0;
        float noteNormale = 0, noteRatt = 0, noteDeliberation = 0;
        noteModule.setConsistent(true);
        for (NoteElementModule noteElementModule : noteModule.getNoteElementModules()) {
            consisteNoteElementModule(noteElementModule);
            if (!noteElementModule.isConsistent()) noteModule.setConsistent(false);
            noteNormale += noteElementModule.getNoteNormale() * noteElementModule.getFacteur();
            noteRatt += noteElementModule.getNoteRatt() * noteElementModule.getFacteur();
            noteDeliberation += noteElementModule.getNoteDeliberation() * noteElementModule.getFacteur();
            facteur += noteElementModule.getFacteur();
        }
        if (noteRatt > 0) noteModule.setRatt(true);
        noteNormale /= facteur;
        noteRatt /= facteur;
        noteDeliberation /= facteur;
        noteModule.setNoteDeliberation(noteDeliberation);
        noteModule.setNoteNormale(noteNormale);
        noteModule.setNoteRatt(noteRatt);
    }
}
