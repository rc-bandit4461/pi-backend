package enset.bdcc.pi.backend.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import enset.bdcc.pi.backend.dao.*;
import enset.bdcc.pi.backend.entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Transient;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Iterator;
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
            if (newNEM.getNoteNormale() < 12 && newNEM.getNoteNormale() >= 5) oldNEM.setRatt(true);
            else oldNEM.setRatt(false);
            oldNEM.setNoteDeliberation(newNEM.getNoteDeliberation());
//            if (!foundConsistent)
//                if (oldNEM.getNoteNormale() != newNEM.getNoteNormale() || oldNEM.getNoteRatt() != newNEM.getNoteRatt()) {
//                    oldNEM.setConsistent(false);
//                    oldNEM.getNoteModule().setConsistent(false);
//                    foundConsistent = true;
//                }
            if (oldNEM.isConsistent()) {
                if (oldNEM.getNoteNormale() != newNEM.getNoteNormale() || oldNEM.getNoteRatt() != newNEM.getNoteRatt()) {
                    oldNEM.setConsistent(false);
                    foundConsistent = false;
                    oldNEM.getNoteModule().setConsistent(false);
                }
            }
            oldNEM.setNoteNormale(newNEM.getNoteNormale());
            oldNEM.setNoteRatt(newNEM.getNoteRatt());
            oldNEM.setNoteDeliberation(Math.max(Math.max(oldNEM.getNoteNormale(), oldNEM.getNoteRatt()), oldNEM.getNoteDeliberation()));
            noteModuleList.add(oldNEM.getNoteModule());
            oldNEM.setRatt(newNEM.isRatt());
            noteElementModuleRepository.save(oldNEM);
        }
        noteModuleRepository.saveAll(noteModuleList);
        noteModuleList.forEach(noteModule -> {
            recalculateNoteModule(noteModule);
        });

    }

    @Transactional
    void recalculateNoteModule(NoteModule noteModule) {
        noteModule = noteModuleRepository.getOne(noteModule.getId());
        float noteNormale = 0, noteDeliberation = 0, noteRatt = 0;
        for (NoteElementModule noteElementModule : noteModule.getNoteElementModules()) {
            noteNormale += noteElementModule.getNoteNormale() * noteElementModule.getFacteur();
            noteDeliberation += noteElementModule.getNoteDeliberation() * noteElementModule.getFacteur();
            noteRatt += noteElementModule.getNoteRatt() * noteElementModule.getFacteur();
        }
        noteNormale /= noteModule.getNoteElementModules().size();
        noteRatt /= noteModule.getNoteElementModules().size();
        noteDeliberation /= noteModule.getNoteElementModules().size();
        noteDeliberation = Math.max(noteDeliberation, Math.max(noteNormale, noteRatt));
        noteModule.setNoteRatt(noteRatt);
        noteModule.setNoteNormale(noteNormale);
        noteModule.setNoteDeliberation(noteDeliberation);
        noteModuleRepository.save(noteModule);
    }

    @Transactional
    void consisteNoteElementModule(NoteElementModule noteElementModule) {

        float noteNormale = 0, noteRatt = 0;
        float facteurNormale = 0, facteurRatt = 0;
        List<Examen> examenList = new ArrayList<>();
        List<NoteModule> noteModuleList = new ArrayList<>();
        for (NoteExamen noteExamen : noteElementModule.getNoteExamens()) {
            if (!examenList.contains(noteExamen.getExamen())) {
                examenList.add(noteExamen.getExamen());
            }
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
        noteElementModule.setNoteDeliberation(Math.max(noteNormale, noteRatt));
        noteElementModuleRepository.save(noteElementModule);

    }

    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/consisteNoteElementModule/{id}")
    public ResponseEntity consistNoteElementModule(@PathVariable(name = "id") Long nemId) throws JsonProcessingException {
        NoteElementModule noteElementModule = noteElementModuleRepository.getOne(nemId);
        consisteNoteElementModule(noteElementModule);
        recalculateNoteModule(noteElementModule.getNoteModule());
        return new ResponseEntity(HttpStatus.OK);

    }

    @Transactional
    public void consist(NoteModule noteModule) {
        noteModule.setNoteNormale(0);
        noteModule.setNoteDeliberation(0);
        noteModule.setNoteRatt(0);
        float facteur = 0;
        float noteNormale = 0, noteRatt = 0, noteDeliberation = 0;
        List<NoteElementModule> noteElementModules = noteModule.getNoteElementModules();
        noteElementModules.forEach(this::consisteNoteElementModule);
        for (NoteElementModule noteElementModule : noteElementModules) {
            facteur += noteElementModule.getElementModule().getFacteur();
            noteNormale += noteElementModule.getNoteNormale() * noteElementModule.getElementModule().getFacteur();
            ;
            noteRatt += noteElementModule.getNoteRatt() * noteElementModule.getElementModule().getFacteur();
            ;
            noteDeliberation += noteElementModule.getNoteDeliberation() * noteElementModule.getElementModule().getFacteur();
            ;
        }
        noteModule.setNoteRatt(noteRatt / facteur);
        noteModule.setNoteDeliberation(noteDeliberation / facteur);
        noteModule.setNoteNormale(noteNormale / facteur);
        noteModule.setConsistent(true);
        noteModuleRepository.save(noteModule);
    }

    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/consisteNoteModule/{id}")
    public void consisteNoteModule(@PathVariable(name = "id") Long id) {
        NoteModule noteModule = noteModuleRepository.getOne(id);
        consist(noteModule);
//        if(true) return;
//        float facteur = 0;
//        float noteNormale = 0, noteRatt = 0, noteDeliberation = 0;
//        for (Iterator<NoteElementModule> noteElementModulesIterator = noteModule.getNoteElementModules().iterator(); noteElementModulesIterator.hasNext(); ) {
//            NoteElementModule noteElementModule = noteElementModulesIterator.next();
//            consisteNoteElementModule(noteElementModule);
//            noteNormale += noteElementModule.getNoteNormale() * noteElementModule.getFacteur();
//            noteRatt += noteElementModule.getNoteRatt() * noteElementModule.getFacteur();
//            noteDeliberation += noteElementModule.getNoteDeliberation() * noteElementModule.getFacteur();
//            facteur += noteElementModule.getFacteur();
//        }
//        noteElementModuleRepository.saveAll(noteModule.getNoteElementModules());
////        noteModuleRepository.save(noteModule);
//        noteModule.setConsistent(true);
//        if (noteRatt > 0) noteModule.setRatt(true);
//        noteNormale /= facteur;
//        noteRatt /= facteur;
//        noteDeliberation /= facteur;
//        noteModule.setNoteNormale(noteNormale);
//        noteModule.setNoteRatt(noteRatt);
//        noteModule.setNoteDeliberation(Math.max(Math.max(noteDeliberation, noteNormale), noteRatt));
//        noteModuleRepository.save(noteModule);

    }
}
