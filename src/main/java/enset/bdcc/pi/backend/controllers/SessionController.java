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

public class SessionController {
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


    @PostMapping(value = "/saveSession2")
    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSession2(@RequestBody Session session) throws Exception {
        List<SemestreFiliere> filiereSemestres = new ArrayList<>(filiereRepository.getOne(session.getFiliere().getId()).getSemestreFilieres());
        List<SemestreFiliere> sessionSemestres = new ArrayList<>();
        List<SemestreEtudiant> semestreEtudiants = new ArrayList<>();
        List<Etudiant> etudiantList = new ArrayList<>(session.getEtudiants());
//        List<EtudiantSession> etudiantSessionList = new ArrayList<>(session.getEtudiantSessions());
        session.getEtudiants().forEach(etudiant -> {
            etudiantList.add(etudiantRepository.getOne(etudiant.getId()));
        });

        //EtudiantSession needs its ID to be persisted, aka we need to persist session first
        filiereSemestres.forEach(semestreFiliere -> {
            SemestreFiliere sessionSemestre = new SemestreFiliere();
            sessionSemestre.setSession(session);
            sessionSemestre.setNumero(semestreFiliere.getNumero());
            sessionSemestres.add(sessionSemestre);
            etudiantList.forEach(etudiant -> {
                SemestreEtudiant semestreEtudiant = new SemestreEtudiant(etudiant, session, semestreFiliere.getNumero(), false);
                etudiant.getSemestreEtudiants().add(semestreEtudiant);
                semestreEtudiants.add(semestreEtudiant);
            });
            semestreFiliere.getModules().forEach(module -> {
                Module sessionModule = new Module();
                sessionModule.setLibelle(module.getLibelle());
                sessionModule.setElements(new ArrayList<>(module.getElements()));
                sessionModule.setSemestreFiliere(sessionSemestre);
                module.getElements().forEach(element -> {
                    System.out.println(element.getLibelle());
                    element.getModules().add(sessionModule);

                });
                sessionSemestre.getModules().add(sessionModule);
            });
        });
        session.setSemestreFilieres(sessionSemestres);
        session.setSemestreEtudiants(semestreEtudiants); //cascade is not persiste, semestreEtudiants not saved yet
        sessionRepository.save(session);
        List<Module> moduleList = new ArrayList<>();
        sessionSemestres.forEach(semestreFiliere -> {
            moduleList.addAll(semestreFiliere.getModules());
        });
        moduleRepository.saveAll(moduleList);
        //Creation de notemOdules
        session.getSemestreFilieres().forEach(semestreFiliere -> {
            semestreEtudiants.forEach(semestreEtudiant -> {
                if (semestreEtudiant.getNumero() != semestreFiliere.getNumero()) return;
                semestreFiliere.getModules().forEach(module -> {
                    NoteModule noteModule = new NoteModule(module, semestreEtudiant);
                    module.getElements().forEach(element -> {
                        NoteElementModule noteElementModule = new NoteElementModule(noteModule, element);
                        noteModule.getNoteElementModules().add(noteElementModule);
                        element.getNoteElementModules().add(noteElementModule);
                    });
                    semestreEtudiant.getNoteModules().add(noteModule);
                });
            });
        });
        semestreEtudiantRepository.saveAll(semestreEtudiants);
        //Session ID is not available UNTIL we save the Session--> meaning that we need to save the Session, and then
        //we can save the session without
        for (Etudiant etudiant : etudiantList) {
            session.getEtudiantSessions().add(new EtudiantSession(etudiant, session));
        }
        etudiantSessionRepository.saveAll(session.getEtudiantSessions());

    }

    @PostMapping(value = "/saveSession")
    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSession3(@RequestBody Session session) {
        List<SemestreFiliere> semestreFiliereList = new ArrayList<>(filiereRepository.getOne(session.getFiliere().getId()).getSemestreFilieres());
        List<Etudiant> etudiantArrayList = new ArrayList<>();
        List<SemestreEtudiant> semestreEtudiants = new ArrayList<>();
        session.getEtudiants().forEach(etudiant -> {
            etudiantArrayList.add(etudiantRepository.getOne(etudiant.getId()));
        });
        semestreFiliereList.forEach(semestreFiliere -> {
            SemestreFiliere sessionSemestre = new SemestreFiliere();
            sessionSemestre.setNumero(semestreFiliere.getNumero());
            sessionSemestre.setSession(session);
            sessionSemestre.setFiliere(null);
            session.getSemestreFilieres().add(sessionSemestre);
            etudiantArrayList.forEach(etudiant -> {
                SemestreEtudiant semestreEtudiant = new SemestreEtudiant(etudiant, session, semestreFiliere.getNumero(), false);
                semestreFiliere.getModules().forEach(module -> {
                    NoteModule noteModule = new NoteModule(module, semestreEtudiant);
                    semestreEtudiant.getNoteModules().add(noteModule);
                    module.getElements().forEach(element -> {
                        NoteElementModule noteElementModule = new NoteElementModule(noteModule, element);
                        noteModule.getNoteElementModules().add(noteElementModule);
                    });
                });

                etudiant.getSemestreEtudiants().add(semestreEtudiant);
                semestreEtudiants.add(semestreEtudiant);
            });
            semestreFiliere.getModules().forEach(module -> {
                Module sessionModule = new Module();
                sessionSemestre.getModules().add(sessionModule);
                sessionModule.setLibelle(module.getLibelle());
                sessionModule.setSemestreFiliere(sessionSemestre);
                sessionModule.getElements().addAll(module.getElements());
                module.getElements().forEach(element -> {
                    System.out.println(element.getLibelle());
                    element.getModules().add(module);

                });
            });
//            semestreFiliere.setId(null);
        });
        session.setSemestreEtudiants(semestreEtudiants);
        sessionRepository.save(session);

        //update, now we have to save semestreEtudiant by ourselves, so we can use Module IDs in NoteModule,
        semestreEtudiantRepository.saveAll(semestreEtudiants);
        //Session ID is not available UNTIL we save the Session--> meaning that we need to save the Session, and then
        //we can save the session without
//        System.out.println(session.getId());
        sessionRepository.save(session);
        List<EtudiantSession> etudiantSessionList = new ArrayList<>();
        for (Etudiant etudiant : etudiantArrayList) {
            etudiantSessionList.add(new EtudiantSession(etudiant, session));
        }
        etudiantSessionRepository.saveAll(etudiantSessionList);
    }


    @PostMapping(value = "/saveExamen")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void saveExamen(@RequestBody Examen examen) {
        List<NoteExamen> noteExamenList = new ArrayList<>();
        Module module = moduleRepository.getOne(examen.getModule().getId());
        SemestreFiliere semestreFiliere = module.getSemestreFiliere();
        System.out.println("Semestre Filiere num" + semestreFiliere.getNumero());
        Session session = module.getSemestreFiliere().getSession();
        examenRepository.save(examen);
        examen.getNoteEtudiants().forEach(noteEtudiant -> {
            SemestreEtudiant semestreEtudiant = semestreEtudiantRepository.findByIdEtudiantAndIdSessionAndNumero(noteEtudiant.getEtudiant().getId(), session.getId(), module.getSemestreFiliere().getNumero());
            NoteModule noteModule = noteModuleRepository.getByModuleIdAndSEId(semestreEtudiant.getId(), module.getId());
            NoteElementModule noteElementModule = noteElementModuleRepository.getByElementAndNoteModule(examen.getElement().getId(), noteModule.getId());
//            if(!noteElementModule.isRatt() && examen.isRatt()) noteElementModule.setRatt(examen.isRatt()); //should we leave it?
            noteElementModuleRepository.save(noteElementModule);
            NoteExamen noteExamen = new NoteExamen(examen, noteElementModule, noteEtudiant.getNote());
            noteExamenList.add(noteExamen);
        });
        noteExamenRepository.saveAll(noteExamenList);
    }


}
