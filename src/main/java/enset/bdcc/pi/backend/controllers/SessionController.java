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
        Filiere filiere = filiereRepository.getOne(session.getFiliere().getId());
        Session toCreateSession = new Session(2004, filiere);

        //On recopie les modules et elements de la filieres, pour avoir ses propres modules
        toCreateSession.getFiliere().getSemestreFilieres().forEach(semestreFiliere -> {
            SemestreFiliere semestreSession = new SemestreFiliere(semestreFiliere.getNumero(), semestreFiliere.isDone(), toCreateSession);
            semestreFiliere.getModules().forEach(sfModule -> { //sf = semestreFiliere, ss = semestreSesison
                System.out.println("HERE IS A FUCKING SF" + sfModule.getLibelle());
                Module ssModule = new Module(sfModule.getLibelle());
                ssModule.setSemestreFiliere(semestreSession);
                sfModule.getElements().forEach(element -> {
                    ssModule.getElements().add(element);
                });
                semestreSession.getModules().add(ssModule);
            });
            toCreateSession.getSemestreFilieres().add(semestreSession);
        });
        sessionRepository.save(toCreateSession);
        //Ensuite on crée les semestres des étudiants et on les lient avec les modules de la session
        toCreateSession.getSemestreFilieres().forEach(semestreSession -> {
            etudiantArrayList.forEach(etudiant -> {
                SemestreEtudiant semestreEtudiant = new SemestreEtudiant(etudiant, toCreateSession, semestreSession.getNumero(), false);
                semestreSession.getModules().forEach(ssModule -> {
                    NoteModule noteModule = new NoteModule(ssModule, semestreEtudiant);
                    ssModule.getElements().forEach(element -> {
                        NoteElementModule noteElementModule = new NoteElementModule(noteModule, element);
                        noteModule.getNoteElementModules().add(noteElementModule);
                    });
                    semestreEtudiant.getNoteModules().add(noteModule);
                });
                etudiant.getSemestreEtudiants().add(semestreEtudiant);
                toCreateSession.getSemestreEtudiants().add(semestreEtudiant);
            });
        });
        sessionRepository.save(toCreateSession);
        //Ensuite on crée les sessions des étudiants
        List<EtudiantSession> etudiantSessionList = new ArrayList<>();
        for (Etudiant etudiant : etudiantArrayList) {
            etudiantSessionList.add(new EtudiantSession(etudiant, toCreateSession));
        }
        etudiantSessionRepository.saveAll(etudiantSessionList);
    }


}
