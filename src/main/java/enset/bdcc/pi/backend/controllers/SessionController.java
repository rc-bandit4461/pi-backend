package enset.bdcc.pi.backend.controllers;

import enset.bdcc.pi.backend.dao.*;
import enset.bdcc.pi.backend.entities.*;
import enset.bdcc.pi.backend.entities.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.lang.model.element.ModuleElement;
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
    private ElementModuleRepository elementModuleRepository;
    @Autowired
    private SemestreEtudiantRepository semestreEtudiantRepository;
    @Autowired
    private SemestreFiliereController semestreFiliereController;

    @PostMapping(value = "/saveSession")
    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    public Session saveSession4(@RequestBody Session session) {

        List<SemestreFiliere> semestreFiliereList = new ArrayList<>(filiereRepository.getOne(session.getFiliere().getId()).getSemestreFilieres());
        List<Etudiant> etudiantArrayList = new ArrayList<>();
        List<SemestreEtudiant> semestreEtudiants = new ArrayList<>();
        List<ElementModule> elementModuleList = new ArrayList<>();
//        System.out.println("LENGTH-->" + session.getEtudiantSessions().size());
        for (Etudiant etudiant1 : session.getEtudiants()) {
            Etudiant etudiant = etudiantRepository.getOne(etudiant1.getId());
            etudiantArrayList.add(etudiant);
        }
        Filiere filiere = filiereRepository.getOne(session.getFiliere().getId());
        for (int i = 0; i < filiere.getSemestreFilieres().size(); i++) {
            SemestreFiliere semestreFiliereSource = session.getFiliere().getSemestreFilieres().get(i);
            SemestreFiliere semestreFiliereTarget = filiere.getSemestreFilieres().get(i);
            for (int j = 0; j < semestreFiliereTarget.getModules().size(); j++) {
                Module moduleSource = semestreFiliereSource.getModules().get(j);
                Module moduleTarget = semestreFiliereTarget.getModules().get(j);
                moduleTarget.setFacteur(moduleSource.getFacteur());
                for (int k = 0; k < moduleTarget.getElements().size(); k++) {
                    Element elementSource = moduleSource.getElements().get(k);
                    Element elementTarget = moduleTarget.getElements().get(k);
                    elementTarget.setFacteur(elementSource.getFacteur());
                }
            }
        }
        Session toCreateSession = new Session(session.getAnnee(), filiere);

        //On recopie les modules et elements de la filieres, pour avoir ses propres modules
        toCreateSession.getFiliere().getSemestreFilieres().forEach(semestreFiliere -> {
            SemestreFiliere semestreSession = new SemestreFiliere(semestreFiliere.getNumero(), semestreFiliere.isDone(), toCreateSession);
            semestreFiliere.getModules().forEach(sfModule -> { //sf = semestreFiliere, ss = semestreSesison
                Module ssModule = new Module(sfModule.getLibelle());
                ssModule.setSemestreFiliere(semestreSession);
                ssModule.setFacteur(sfModule.getFacteur());
                for (Element element : sfModule.getElements()) {
                    ssModule.getElements().add(element);
                    ElementModule elementModule = new ElementModule();
                    elementModule.setELement(element);
                    elementModule.setFacteur(element.getFacteur());
                    elementModule.setModule(ssModule);
                    elementModuleList.add(elementModule);
                    ssModule.getElementModules().add(elementModule);
                }
                semestreSession.getModules().add(ssModule);
            });
            toCreateSession.getSemestreFilieres().add(semestreSession);
        });
        sessionRepository.save(toCreateSession);
        elementModuleRepository.saveAll(elementModuleList);
        //Ensuite on crée les semestres des étudiants et on les lient avec les modules de la session
        toCreateSession.getSemestreFilieres().forEach(semestreSession -> {
            for (Etudiant etudiant : etudiantArrayList) {
                SemestreEtudiant semestreEtudiant = new SemestreEtudiant(etudiant, toCreateSession, semestreSession.getNumero(), false);
                for (Module ssModule : semestreSession.getModules()) {
                    NoteModule noteModule = new NoteModule(ssModule, semestreEtudiant);
//                    ssModule.getElements().forEach(element -> {
//                        NoteElementModule noteElementModule = new NoteElementModule(noteModule, element);
//
//                        noteModule.getNoteElementModules().add(noteElementModule);
//                    });
                    for (ElementModule elementModule : ssModule.getElementModules()) {
                        NoteElementModule noteElementModule = new NoteElementModule();
                        noteElementModule.setNoteModule(noteModule);
                        noteElementModule.setElementModule(elementModule);
                        noteElementModule.setElement(elementModule.getELement());
                        noteModule.getNoteElementModules().add(noteElementModule);

                    }
                    semestreEtudiant.getNoteModules().add(noteModule);
                }
                etudiant.getSemestreEtudiants().add(semestreEtudiant);
                toCreateSession.getSemestreEtudiants().add(semestreEtudiant);
            }
        });
        sessionRepository.save(toCreateSession);
        //Ensuite on crée les sessions des étudiants
        List<EtudiantSession> etudiantSessions = new ArrayList<>();
        for (Etudiant etudiant : etudiantArrayList) {
            EtudiantSession etudiantSession = new EtudiantSession(etudiant, toCreateSession);
            etudiantSessions.add(etudiantSession);
        }
        etudiantSessionRepository.saveAll(etudiantSessions);
        Session session1 = new Session();
        session1.setId(toCreateSession.getId());
        return session1;
    }

    @GetMapping(value = "/updateEtudiantSessionState/{idSession}/{idEtudiant}/noConsist")
    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    public void updateEtudiantSessionStateWithoutConsist(@PathVariable("idSession") Long idSession, @PathVariable("idEtudiant") Long idEtudiant) {
        float note = 0;
        boolean existModuleNotValid = false;
        List<SemestreEtudiant> semestreEtudiantList = semestreEtudiantRepository.findByIdEtudiantAndIdSession(idEtudiant, idSession);
        for (SemestreEtudiant semestreEtudiant : semestreEtudiantList) {
            semestreFiliereController.updateNoteSemestre(semestreEtudiant);
            note += semestreEtudiant.getNote();

        }
        semestreEtudiantRepository.saveAll(semestreEtudiantList);
        EtudiantSession etudiantSession = etudiantSessionRepository.getBySessionIdAndEtudiantId(idSession, idEtudiant);

        for (SemestreEtudiant semestreEtudiant : semestreEtudiantList) {
            for (NoteModule noteModule : semestreEtudiant.getNoteModules()) {
                for (NoteElementModule noteElementModule : noteModule.getNoteElementModules()) {
                    if (noteElementModule.getNoteDeliberation() < 7)
                        existModuleNotValid = true;
                }
            }
        }
        Session session = sessionRepository.getOne(idSession);
        etudiantSession.setNote(note / session.getSemestreFilieres().size());
        if (note >= 12) etudiantSession.setPassed(true);
        else etudiantSession.setPassed(false);
        if (existModuleNotValid) etudiantSession.setPassed(false);
        etudiantSessionRepository.save(etudiantSession);
    }

    @ResponseBody
    @Transactional
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/updateEtudiantSessionState/{idSession}/{idEtudiant}/consist")
    public void updateEtudiantSessionStateWithConsist(@PathVariable("idSession") Long idSession, @PathVariable("idEtudiant") Long idEtudiant) {
        float note = 0;
        boolean existModuleNotValid = false;
        List<SemestreEtudiant> semestreEtudiantList = semestreEtudiantRepository.findByIdEtudiantAndIdSession(idEtudiant, idSession);

        for (SemestreEtudiant semestreEtudiant : semestreEtudiantList) {
            semestreFiliereController.updateSemestreEtudiantNotesWithConsistent(semestreEtudiant.getId());
            note += semestreEtudiant.getNote();

        }
        semestreEtudiantRepository.saveAll(semestreEtudiantList);
        EtudiantSession etudiantSession = etudiantSessionRepository.getBySessionIdAndEtudiantId(idSession, idEtudiant);

        for (SemestreEtudiant semestreEtudiant : semestreEtudiantList) {
            for (NoteModule noteModule : semestreEtudiant.getNoteModules()) {
                for (NoteElementModule noteElementModule : noteModule.getNoteElementModules()) {
                    if (noteElementModule.getNoteDeliberation() < 7)
                        existModuleNotValid = true;
                }
            }
        }
        Session session = sessionRepository.getOne(idSession);

        etudiantSession.setNote(note / session.getSemestreFilieres().size());
        if (note >= 12) etudiantSession.setPassed(true);
        else etudiantSession.setPassed(false);
        if (existModuleNotValid) etudiantSession.setPassed(false);
        etudiantSessionRepository.save(etudiantSession);
    }

    @Transactional
    @GetMapping(value = "/updateSessionNotes/{idSession}/noConsist")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void updateSessionNotesWithoutConsist(@PathVariable("idSession") Long idSession) {
        Session session = sessionRepository.getOne(idSession);
        List<Etudiant> etudiantList = etudiantRepository.getBySession(session.getId());
        for (Etudiant etudiant : etudiantList) {
            updateEtudiantSessionStateWithoutConsist(session.getId(), etudiant.getId());
        }
    }

    @Transactional
    @GetMapping(value = "/updateSessionNotes/{idSession}/consist")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void updateSessionNotesWithConsist(@PathVariable("idSession") Long idSession) {
        Session session = sessionRepository.getOne(idSession);
        List<Etudiant> etudiantList = etudiantRepository.getBySession(session.getId());
        for (Etudiant etudiant : etudiantList) {
            updateEtudiantSessionStateWithConsist(session.getId(), etudiant.getId());
        }
    }

    @Transactional
    @GetMapping(value = "/closeSession/{idSession}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void closeSession(@PathVariable("idSession") Long idSession) {
        Session session = sessionRepository.getOne(idSession);
        session.setDone(!session.isDone());
        sessionRepository.save(session);
    }
}
