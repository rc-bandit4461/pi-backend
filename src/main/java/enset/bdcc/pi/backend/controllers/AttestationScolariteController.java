package enset.bdcc.pi.backend.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import enset.bdcc.pi.backend.dao.EtudiantSessionRepository;
import enset.bdcc.pi.backend.entities.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import enset.bdcc.pi.backend.dao.AttestationScolariteRepository;
import enset.bdcc.pi.backend.entities.Attestation_scolarite;

@RestController
@CrossOrigin("*")
public class AttestationScolariteController {
    String libele_year = "";

    @Autowired
    private EtudiantSessionRepository etudiantSessionRepository;
    @Autowired
    private AttestationScolariteRepository attestationRepository;

    @GetMapping(path = "/attestations")
    public List<Attestation_scolarite> getAttestations() {
        return attestationRepository.findAll();
    }

    @GetMapping(path = "/attestation/{id}")
    public Attestation_scolarite getAttestation(@PathVariable Long id) {
        return attestationRepository.findAttestationByCodeEtudiant(id);
    }

    @PostMapping(path = "/attestation")
    public Attestation_scolarite saveAttestation(@RequestBody Attestation_scolarite attest) {
        return attestationRepository.save(attest);
    }

    @DeleteMapping(path = "/attestation/{id}")
    public void deleteAttestation(@PathVariable Long id) {
        attestationRepository.deleteById(id);
    }

    @PutMapping(path = "/attestation/{id}")
    public Attestation_scolarite updateAttestation(@RequestBody Attestation_scolarite attest, @PathVariable Long id) {
        System.out.println("Updating : " + id);
        Attestation_scolarite attestation = attestationRepository.findById(id).get();
        attestation.setId(attest.getId());
        attestation = attest;
        return attestationRepository.save(attestation);
    }

    public void initAttestations(Session session, int year) {
//        System.out.println("Annee session : " + session.getAnnee());
        if (session.getAnnee() == year) {
            etudiantSessionRepository.findAll().forEach(etudse -> {
                Attestation_scolarite attestation = new Attestation_scolarite();
                attestation.setCodeEtudiant(etudse.getEtudiant().getId());
                attestation.setNomComplet(etudse.getEtudiant().getNom() + " " + etudse.getEtudiant().getPrenom());
                attestation.setCin(etudse.getEtudiant().getCin());
                attestation.setCne(etudse.getEtudiant().getCne());
                try {
                    attestation.setDate_naissance(new SimpleDateFormat("yyyy-MM-dd").parse(etudse.getEtudiant().getDate_naissance().toString()));
                    attestation.setVille_naissance(etudse.getEtudiant().getVille_naissance());
                    attestation.setAnnee_session(etudse.getSession().getAnnee() + "/" + Integer.toString(etudse.getSession().getAnnee() + 1));
                    switch (etudse.getSession().getAnnee_courante()) {
                        case 1:
                            libele_year = " ère année";
                            break;
                        case 2:
                            libele_year = " ème année";
                            break;
                        default:
                            libele_year = " ème année";
                            break;
                    }
                    attestation.setAnnee_univers(Integer.toString(etudse.getSession().getAnnee_courante()) + libele_year + " " + etudse.getSession().getFiliere().getDiplome().getDescription() + " : " + etudse.getSession().getFiliere().getDescription());
                    attestation.setType_diplome("Fl. " + etudse.getSession().getFiliere().getDiplome().getDescription().replace("Cycle ", "") + " ." + etudse.getSession().getFiliere().getLibelle());
                    attestation.setEtudiant(etudse.getEtudiant());
                    attestationRepository.save(attestation);
                } catch (ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        } else {
            System.out.println("Impossible !!");
        }
    }
}
