package enset.bdcc.pi.backend;

import enset.bdcc.pi.backend.dao.*;
import enset.bdcc.pi.backend.entities.*;
import enset.bdcc.pi.backend.entities.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.beans.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {


    @Autowired
    private RepositoryRestConfiguration restConfiguration;


    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
//    @PostConstruct
    public void run(String... args) throws Exception {

        restConfiguration.exposeIdsFor(Element.class);
        restConfiguration.exposeIdsFor(Diplome.class);
        restConfiguration.exposeIdsFor(Module.class);
        restConfiguration.exposeIdsFor(User.class);
        restConfiguration.exposeIdsFor(Examen.class);
        restConfiguration.exposeIdsFor(NoteExamen.class);
        restConfiguration.exposeIdsFor(NoteModule.class);
        restConfiguration.exposeIdsFor(Booking.class);
        restConfiguration.exposeIdsFor(NoteElementModule.class);
        restConfiguration.exposeIdsFor(Filiere.class);
        restConfiguration.exposeIdsFor(SemestreFiliere.class);
        restConfiguration.exposeIdsFor(SemestreEtudiant.class);
        restConfiguration.exposeIdsFor(DemandeAttestation.class);
        restConfiguration.exposeIdsFor(DemandeReleve.class);
        restConfiguration.exposeIdsFor(Reclamation.class);
        restConfiguration.exposeIdsFor(ReclamAttestation.class);
        restConfiguration.exposeIdsFor(Attestation_scolarite.class);
        restConfiguration.exposeIdsFor(Session.class);
        restConfiguration.exposeIdsFor(Etudiant.class);
        restConfiguration.exposeIdsFor(ElementModule.class);
        restConfiguration.exposeIdsFor(DemandeReleve.class);
        restConfiguration.exposeIdsFor(EtudiantSession.class);
        restConfiguration.exposeIdsFor(Room.class);


    }


}
