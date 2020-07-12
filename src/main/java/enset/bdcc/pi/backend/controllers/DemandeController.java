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

public class DemandeController {
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
    private DemandeReleveRepository demandeReleveRepository;
    @Autowired
    private DemandeAttestationRepository demandeAttestationRepository;

    @PostMapping(value = "/saveDemandeReleve")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public DemandeReleve saveDemande(@RequestBody DemandeReleve demandeReleve) {

        SemestreEtudiant semestreEtudiant = semestreEtudiantRepository.getOne(demandeReleve.getSemestreEtudiant().getId());
        demandeReleve.setSemestreEtudiant(semestreEtudiant);

        semestreEtudiant.getDemandeReleves().add(demandeReleve);
        semestreEtudiantRepository.save(semestreEtudiant);
        demandeReleveRepository.save(demandeReleve);
        DemandeReleve demandeReleve1 = new DemandeReleve();
        demandeReleve1.setCreatedAt(demandeReleve.getCreatedAt());
        return demandeReleve1;

    }

    @GetMapping(value = "/requestGradCertif/{idSession}/{idEtudiant}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void requestGradCertif(@PathVariable("idSession") Long idSession, @PathVariable("idEtudiant") Long idEtudiant) {
        EtudiantSession etudiantSession = etudiantSessionRepository.getBySessionIdAndEtudiantId(idSession, idEtudiant);
        etudiantSession.setHasRequestedGraduation(true);
        etudiantSession.setNbrGraduationRequests(etudiantSession.getNbrGraduationRequests() + 1);
        etudiantSessionRepository.save(etudiantSession);

        DemandeAttestation demande = new DemandeAttestation();
        demande.setEtudiant(etudiantSession.getEtudiant());
        demande.setSession(etudiantSession.getSession());
        demande.setLibelle("Attest.Réussite");
        demande.setType(Demande.TYPE_CERTIF_GRADUATION);
        demande.setDetail("Demande attestation Réussite");
        demande.setDone(false);
        demandeAttestationRepository.save(demande);
    }

    @GetMapping(value = "/requestScolariteCertif/{idSession}/{idEtudiant}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void requestScolariteCertif(@PathVariable("idSession") Long idSession, @PathVariable("idEtudiant") Long idEtudiant) {
        EtudiantSession etudiantSession = etudiantSessionRepository.getBySessionIdAndEtudiantId(idSession, idEtudiant);
        etudiantSession.setHasRequestedScolarite(true);
        etudiantSession.setNbrScolariteRequests(etudiantSession.getNbrScolariteRequests() + 1);
        etudiantSessionRepository.save(etudiantSession);
        DemandeAttestation demande = new DemandeAttestation();
        demande.setEtudiant(etudiantSession.getEtudiant());
        demande.setSession(etudiantSession.getSession());
        demande.setLibelle("Attest.Scolarité");
        demande.setDetail("Demande attestation scolarité");
        demande.setType(Demande.TYPE_CERTIF_SCOLARITE);
        demande.setDone(false);
        demandeAttestationRepository.save(demande);

    }

    @GetMapping(value = "/resolveRequest/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void resolveRequest(@PathVariable("id") Long id) {
        DemandeAttestation demande = demandeAttestationRepository.getOne(id);
        processRequest(demande, false);

    }

    @GetMapping(value = "/denyRequest/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void denyRequest(@PathVariable("id") Long id) {
        DemandeAttestation demande = demandeAttestationRepository.getOne(id);
        processRequest(demande, true);

    }

    public void processRequest(DemandeAttestation demande, boolean state) {
        EtudiantSession etudiantSession = etudiantSessionRepository.getBySessionIdAndEtudiantId(demande.getSession().getId(), demande.getEtudiant().getId());
        demande.setDone(true);
        if (demande.getType().equals(Demande.TYPE_CERTIF_GRADUATION)) {
//            etudiantSession.setCanRequestGraduation(true);
            etudiantSession.setHasRequestedScolarite(false);
        }

        if (demande.getType().equals(Demande.TYPE_CERTIF_SCOLARITE)) {
//            etudiantSession.setCanRequestScolarite(true);
            etudiantSession.setHasRequestedScolarite(false);
        }
        etudiantSessionRepository.save(etudiantSession);
        demande.setRejected(state);
        demande.setDone(true);
        demandeAttestationRepository.save(demande);

    }

    @GetMapping(value = "/makeRequestSeen/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void makeRequestSeen(@PathVariable("id") Long id) {
        DemandeAttestation demande = demandeAttestationRepository.getOne(id);
        demande.setSeen(true);
//        EtudiantSession etudiantSession = etudiantSessionRepository.getBySessionIdAndEtudiantId(demande.getSession().getId(),demande.getEtudiant().getId());
        demandeAttestationRepository.save(demande);


    }

    @GetMapping(value = "/makeReleveRequestSeen/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void makeReleveRequesSeen(@PathVariable("id") Long id) {
        DemandeReleve demande = demandeReleveRepository.getOne(id);
        demande.setSeen(true);
//        EtudiantSession etudiantSession = etudiantSessionRepository.getBySessionIdAndEtudiantId(demande.getSession().getId(),demande.getEtudiant().getId());
        demandeReleveRepository.save(demande);


    }

    @PostMapping(value = "/makeRequestsSeen")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void makeRequestsSeen(@RequestBody List<DemandeAttestation> demandeAttestations) {
        List<DemandeAttestation> demandes = new ArrayList<>();
        for (DemandeAttestation demandeAttestation : demandeAttestations) {
            DemandeAttestation demandeAttestation1 = demandeAttestationRepository.getOne(demandeAttestation.getId());
            demandeAttestation1.setSeen(true);
            demandes.add(demandeAttestation1);
        }
        demandeAttestationRepository.saveAll(demandes);


    }


    @DeleteMapping(value = "/deleteRequest/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void deleteRequest(@PathVariable("id") Long id) {
        DemandeAttestation demande = demandeAttestationRepository.getOne(id);
        processRequest(demande, false);
        demandeAttestationRepository.delete(demande);
    }

    @PostMapping(value = "/makeReleveRequestsSeen")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void makeReleveRequestsSeen(@RequestBody List<DemandeReleve> demandes) {

        for (DemandeReleve demande : demandes) {
            DemandeReleve demande1 = demandeReleveRepository.getOne(demande.getId());
            demande1.setSeen(true);
            demandeReleveRepository.save(demande);
        }
//        EtudiantSession etudiantSession = etudiantSessionRepository.getBySessionIdAndEtudiantId(demande.getSession().getId(),demande.getEtudiant().getId());


    }

    //DemandeRelevés
    @GetMapping(value = "/requestReleve/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void RequestReleve(@PathVariable("id") Long id) {
        SemestreEtudiant semestreEtudiant = semestreEtudiantRepository.getOne(id);
//        semestreEtudiant.setCanRequestReleve(false);
        semestreEtudiant.setHasRequestedReleve(true);
        semestreEtudiant.setNbrReleveRequests(semestreEtudiant.getNbrReleveRequests() + 1);
        DemandeReleve demandeReleve = new DemandeReleve();
        demandeReleve.setSemestreEtudiant(semestreEtudiant);
        demandeReleve.setLibelle("Demande Relevé Semestre " + semestreEtudiant.getNumero());
        demandeReleve.setDetail("");
        demandeReleve.setType(Demande.TYPE_RELEVE);
        demandeReleve.setDone(false);
        demandeReleveRepository.save(demandeReleve);
        semestreEtudiantRepository.save(semestreEtudiant);

    }

    @DeleteMapping(value = "/deleteReleveRequest/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void deleteReleveRequest(@PathVariable("id") Long id) {
        DemandeReleve demande = demandeReleveRepository.getOne(id);
        processReleveRequest(demande, false);
        demandeReleveRepository.delete(demande);
    }

    @GetMapping(value = "/resolveReleveRequest/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void resolveReleveRequest(@PathVariable("id") Long id) {
        DemandeReleve demande = demandeReleveRepository.getOne(id);
        processReleveRequest(demande, false);

    }

    @GetMapping(value = "/denyReleveRequest/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void denyReleveRequest(@PathVariable("id") Long id) {
        DemandeReleve demande = demandeReleveRepository.getOne(id);
        processReleveRequest(demande, true);

    }

    public void processReleveRequest(DemandeReleve demande, boolean state) {
        SemestreEtudiant semestreEtudiant = semestreEtudiantRepository.getOne(demande.getSemestreEtudiant().getId());
//        semestreEtudiant.setCanRequestReleve(true);
        semestreEtudiant.setHasRequestedReleve(false);
        semestreEtudiantRepository.save(semestreEtudiant);
        demande.setDone(true);
        demande.setRejected(state);
        demandeReleveRepository.save(demande);

    }

}
