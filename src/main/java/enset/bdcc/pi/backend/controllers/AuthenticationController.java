package enset.bdcc.pi.backend.controllers;

import enset.bdcc.pi.backend.dao.EtudiantRepository;
import enset.bdcc.pi.backend.entities.Etudiant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;

@RestController
@CrossOrigin("*")
public class AuthenticationController {
    @Autowired
    EtudiantRepository etudiantRepository;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @PostMapping(value = "/loginStudent")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Etudiant verifyStudentLogin(@RequestBody Etudiant etudiant) {
          Etudiant etudiant1 = etudiantRepository.getEtudiantByEmailLike(etudiant.getEmail());
          if(etudiant1 == null) return null;
          if(passwordEncoder.matches(etudiant.getPassword(),etudiant1.getPassword())){
              etudiant1.setEtudiantSessions(null);
              etudiant1.setSemestreEtudiants(null);
              etudiant1.setExamens(null);
              etudiant1.setReclamations(null);
              return etudiant1;
          }
          return null;
    }
}
