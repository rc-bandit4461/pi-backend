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

public class ExamensController {
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

    @PostMapping(value = "/saveExamen")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody Examen examen) {
        List<NoteExamen> noteExamenList = new ArrayList<>();
        List<NoteModule> noteModuleList = new ArrayList<>();
        List<NoteElementModule> noteElementModules = new ArrayList<>();
        Module module = moduleRepository.getOne(examen.getModule().getId());
        SemestreFiliere semestreFiliere = module.getSemestreFiliere();
        System.out.println("Semestre Filiere num" + semestreFiliere.getNumero());
        Session session = module.getSemestreFiliere().getSession();
        examenRepository.save(examen);
        examen.getNoteEtudiants().forEach(noteEtudiant -> {
            SemestreEtudiant semestreEtudiant = semestreEtudiantRepository.findByIdEtudiantAndIdSessionAndNumero(noteEtudiant.getEtudiant().getId(), session.getId(), module.getSemestreFiliere().getNumero());
            NoteModule noteModule = noteModuleRepository.getByModuleIdAndSEId(semestreEtudiant.getId(), module.getId());
            NoteElementModule noteElementModule = noteElementModuleRepository.getByElementAndNoteModule(examen.getElement().getId(), noteModule.getId());
            noteElementModule.setConsistent(false);
            noteModule.setConsistent(false);
            noteModuleRepository.save(noteModule);
            noteElementModuleRepository.save(noteElementModule);
//            if(!noteElementModules.contains(noteElementModule)) noteElementModules.add(noteElementModule);
//            if(!noteModuleList.contains(noteElementModule.getNoteModule())) noteModuleList.add(noteModule);
            //            if(!noteElementModule.isRatt() && examen.isRatt()) noteElementModule.setRatt(examen.isRatt()); //should we leave it?
            noteElementModuleRepository.save(noteElementModule);
            NoteExamen noteExamen = new NoteExamen(examen, noteEtudiant.getEtudiant(), noteElementModule, noteEtudiant.getNote());

            noteExamenList.add(noteExamen);
        });
        noteExamenRepository.saveAll(noteExamenList);
    }

    @PutMapping(value = "/saveExamen")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity put(@RequestBody Examen newExam) throws Exception {
        Optional<Examen> oldExamOptional = examenRepository.findById(newExam.getId());
        Examen oldExam;
        if (oldExamOptional.isEmpty()) return new ResponseEntity(HttpStatus.NOT_FOUND);
        oldExam = oldExamOptional.get();
        noteExamenRepository.deleteAll(oldExam.getNoteExamens());
        oldExam.setRatt(newExam.isRatt());
        oldExam.setModule(newExam.getModule());
        oldExam.setFacteur(newExam.getFacteur());
        oldExam.setElement(newExam.getElement());
        oldExam.setDescription(newExam.getDescription());
        oldExam.setNoteExamens(newExam.getNoteExamens());
        oldExam.getNoteExamens().forEach(noteExamen -> {
            NoteElementModule noteElementModule = noteElementModuleRepository.getByElementAndModuleAndEtudiantAndSession(newExam.getElement().getId(), newExam.getModule().getId(), noteExamen.getEtudiant().getId(), newExam.getSession().getId());
            noteElementModule.setConsistent(false);
            NoteModule noteModule = noteElementModule.getNoteModule();
            noteModule.setConsistent(false);
            noteElementModuleRepository.save(noteElementModule);
            noteModuleRepository.save(noteModule);
            noteExamen.setNoteElementModule(noteElementModule);
            noteExamen.setExamen(oldExam);
        });
        examenRepository.save(oldExam);
        return new ResponseEntity(HttpStatus.OK);
    }
}
