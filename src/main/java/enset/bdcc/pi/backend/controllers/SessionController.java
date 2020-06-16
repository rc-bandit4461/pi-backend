package enset.bdcc.pi.backend.controllers;

import enset.bdcc.pi.backend.dao.*;
import enset.bdcc.pi.backend.entities.*;
import enset.bdcc.pi.backend.entities.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.transaction.Transactional;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController

public class SessionController {
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


    @PostMapping(value = "/saveSession")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSession2(@RequestBody Session session) throws Exception {
        List<SemestreFiliere> semestreFiliereList2 = new ArrayList<>(filiereRepository.getOne(session.getFiliere().getId()).getSemestreFilieres());
        List<SemestreFiliere> semestreFiliereList = new ArrayList<>();
        List<SemestreEtudiant> semestreEtudiants = new ArrayList<>();
        List<Etudiant> etudiantList = new ArrayList<>();
        List<EtudiantSession> etudiantSessionList = new ArrayList<>(session.getEtudiantSessions());
        semestreFiliereList2.forEach(semestreFiliere -> {
            SemestreFiliere semestreFiliere1 = new SemestreFiliere();
            semestreFiliere1.setSession(session);
            semestreFiliere1.setModules(semestreFiliere.getModules());
            semestreFiliere1.setNumero(semestreFiliere.getNumero());
        });

        session.setEtudiantSessions(new ArrayList<>());
        etudiantSessionList.forEach(etudiantSession -> {
            Etudiant etudiant = etudiantRepository.getOne(etudiantSession.getEtudiant().getId());
            etudiantSession.setEtudiant(etudiant);
            etudiantSession.setSession(session);
            etudiantList.add(etudiant);

        });
        //EtudiantSession needs its ID to be persisted, aka we need to persist session first
        semestreFiliereList.forEach(semestreFiliere -> {
            semestreFiliere.setId(null);
            etudiantList.forEach(etudiant -> {
                SemestreEtudiant semestreEtudiant = new SemestreEtudiant(etudiant, session, semestreFiliere.getNumero(), false);
                etudiant.getSemestreEtudiants().add(semestreEtudiant);
                semestreEtudiants.add(semestreEtudiant);
            });
            semestreFiliere.getModules().forEach(module -> {
                semestreFiliere.setSession(session);
                semestreFiliere.setFiliere(null);
                module.setId(null);
                module.getElements().forEach(element -> {
                    element.getModules().add(module);

                });
            });
            semestreFiliere.setId(null);
        });
        session.setSemestreFilieres(semestreFiliereList);
        session.setSemestreEtudiants(semestreEtudiants); //cascade is not persiste, semestreEtudiants not saved yet
        sessionRepository.save(session);
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
        etudiantSessionList.forEach(EtudiantSession::generateKeyFromCurrentAttributes);
        etudiantSessionRepository.saveAll(etudiantSessionList);

    }
}
