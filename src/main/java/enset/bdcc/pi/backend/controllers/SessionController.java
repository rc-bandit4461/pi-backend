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

    @PostMapping(value = "/saveSession")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void saveSession(@RequestBody Session session) throws ResponseStatusException {
        System.out.println(session);

        List<EtudiantSession> etudiantSessionList = session.getEtudiantSessions();
        session.setEtudiantSessions(new ArrayList<>()); //saving to retrieve its id
        Filiere filiere1 = filiereRepository.getOne(session.getFiliere().getId());
        //getting filiere schema to save it
        List<SemestreFiliere> semestreFiliereList = new ArrayList<>(filiere1.getSemestreFilieres());
        for (SemestreFiliere semestreFiliere : semestreFiliereList) {
                semestreFiliere.setFiliere(null);
                semestreFiliere.setSession(session);
            for (Module module : semestreFiliere.getModules()) {
                module.setId(null);
            }
            semestreFiliere.setId(null);
        }
        session.setSemestreFilieres(semestreFiliereList);
        sessionRepository.save(session);
        //ORDER IMPORTANT, otherwise we cant create EtudaintSession, we need ID
        for (EtudiantSession etudiantSession : etudiantSessionList) {
            etudiantSession.setSession(session);
            etudiantSession.setEtudiant(etudiantRepository.getOne(etudiantSession.getEtudiant().getId()));
            etudiantSession.setId(new EtudiantSessionKey(etudiantSession.getEtudiant(), session));
        }

        session.setEtudiantSessions(etudiantSessionList);

        sessionRepository.save(session);

    }
}
