package enset.bdcc.pi.backend;

import enset.bdcc.pi.backend.dao.*;
import enset.bdcc.pi.backend.entities.*;
import enset.bdcc.pi.backend.entities.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {
    List<Element> elementList = new ArrayList<>();
    List<Filiere> filiereList = new ArrayList<>();
    ArrayList<Etudiant> etudiantArrayList = new ArrayList<>();
    List<Session> sessionList = new ArrayList<>();


    @Autowired
    private RepositoryRestConfiguration restConfiguration;
    @Autowired
    private EtudiantSessionRepository etudiantSessionRepository;
    @Autowired
    private SemestreFiliereRepository semestreFiliereRepository;
    @Autowired
    private ElementRepository elementRepository;
    @Autowired
    private ModuleRepository moduleRepository;
    @Autowired
    private FiliereRepository filiereRepository;
    @Autowired
    private EtudiantRepository etudiantRepository;
    @Autowired
    private SessionRepository sessionRepository;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        restConfiguration.exposeIdsFor(Element.class);
        restConfiguration.exposeIdsFor(Module.class);
        restConfiguration.exposeIdsFor(Filiere.class);
        restConfiguration.exposeIdsFor(SemestreFiliere.class);
        restConfiguration.exposeIdsFor(SemestreEtudiant.class);
        restConfiguration.exposeIdsFor(DemandeReleve.class);
        restConfiguration.exposeIdsFor(Reclamation.class);
        restConfiguration.exposeIdsFor(Session.class);
        restConfiguration.exposeIdsFor(Etudiant.class);
        restConfiguration.exposeIdsFor(EtudiantSession.class);
        preLoadEtudiants();
        preloadEleemnts();
        preloadFiliere1();
        preloadFiliere2();
        preloadSession();


    }

    @Transactional
    public void preloadSession() {
        Session session = new Session(2004, filiereList.get(0));
        List<SemestreFiliere> semestreFiliereList = new ArrayList<>(filiereList.get(0).getSemestreFilieres());
        List<SemestreEtudiant> semestreEtudiants = new ArrayList<>();
        semestreFiliereList.forEach(semestreFiliere -> {
            semestreFiliere.setId(null);
            etudiantArrayList.forEach(etudiant -> {
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
        session.setSemestreEtudiants(semestreEtudiants);
        sessionRepository.save(session);
        //Session ID is not available UNTIL we save the Session--> meaning that we need to save the Session, and then
        //we can save the session without
//        System.out.println(session.getId());
        for (Etudiant etudiant : etudiantArrayList) {
            //session is detached???
            session.getEtudiantSessions().add(new EtudiantSession(etudiant, session));
        }
        sessionRepository.save(session);
        sessionList.add(session);
    }


    public void preLoadEtudiants() {
        etudiantArrayList.add(new Etudiant("MA137551", "Zakaria", "Chadli"));
        etudiantArrayList.add(new Etudiant("RP137552", "Hamza", "Gueddi"));
        etudiantArrayList.add(new Etudiant("CA137553", "Yassine", "Faiq"));
        etudiantRepository.saveAll(etudiantArrayList);
    }

    public void preloadEleemnts() {
        elementList.add(new Element("Francais"));
        elementList.add(new Element("Anglais"));
        elementList.add(new Element("Statistique et Probabilité"));
        elementList.add(new Element("Recherche Operationnelle"));
        elementList.add(new Element("Architecture Web JEE"));
        elementList.add(new Element("Projet JEE"));
        elementList.add(new Element("Economie de gestion"));
        elementList.add(new Element("Comptabilité"));
        elementList.add(new Element("Projet PI"));
        elementRepository.saveAll(elementList);
    }

    @Transactional
    public void preloadFiliere1() {   //Filieres  + Modules + Elements

        List<Module> moduleList1 = new ArrayList<>();
        List<Module> moduleList2 = new ArrayList<>();
        Module m1 = new Module("TEC");
        Module m2 = new Module("Analyse 2");
        Module m3 = new Module("DeepLearning");
        Module m4 = new Module("Economie 2");
        Module m5 = new Module("Projet d'nnovation");

        m4.getElements().add(elementList.get(0));
        m4.getElements().add(elementList.get(1));
        m3.getElements().add(elementList.get(2));
        m3.getElements().add(elementList.get(3));
        m1.getElements().add(elementList.get(4));
        m1.getElements().add(elementList.get(5));
        m2.getElements().add(elementList.get(6));
        m2.getElements().add(elementList.get(7));
        m5.getElements().add(elementList.get(8));
        moduleList1.add(m3);
        moduleList1.add(m4);
        moduleList2.add(m1);
        moduleList2.add(m2);
        moduleList2.add(m5);
        Filiere filiere = new Filiere("GLSID");
        filiereList.add(filiere);
        SemestreFiliere semestre1 = new SemestreFiliere(1);
        for (Module module1 : moduleList1) {
            module1.setSemestreFiliere(semestre1);
        }
        semestre1.getModules().addAll(moduleList1);
        semestre1.setFiliere(filiere);
        filiere.getSemestreFilieres().add(semestre1);
        SemestreFiliere semestre2 = new SemestreFiliere(2);
        for (Module module : moduleList2) {
            module.setSemestreFiliere(semestre2);
        }
        semestre2.getModules().addAll(moduleList2);
        semestre2.setFiliere(filiere);
        filiere.getSemestreFilieres().add(semestre2);
        filiereRepository.save(filiere);

    }

    public void preloadFiliere2() {
        List<Module> moduleList1 = new ArrayList<>();
        List<Module> moduleList2 = new ArrayList<>();
        Module m1 = new Module("TEC");
        Module m2 = new Module("Analyse 1");
        Module m3 = new Module("Architecture JEE");
        Module m4 = new Module("Economie 2");
        Module m5 = new Module("Projet d'nnovation");

        m1.getElements().add(elementList.get(0));
        m1.getElements().add(elementList.get(1));
        m2.getElements().add(elementList.get(2));
        m2.getElements().add(elementList.get(3));
        m3.getElements().add(elementList.get(4));
        m3.getElements().add(elementList.get(5));
        m4.getElements().add(elementList.get(6));
        m4.getElements().add(elementList.get(7));
        m5.getElements().add(elementList.get(8));
        moduleList1.add(m1);
        moduleList1.add(m2);
        moduleList2.add(m3);
        moduleList2.add(m4);
        moduleList2.add(m5);
        Filiere filiere = new Filiere("BDCC");
        filiereList.add(filiere);

        SemestreFiliere semestre1 = new SemestreFiliere(1);
        for (Module module1 : moduleList1) {
            module1.setSemestreFiliere(semestre1);
        }
        semestre1.getModules().addAll(moduleList1);
        semestre1.setFiliere(filiere);
        filiere.getSemestreFilieres().add(semestre1);
        SemestreFiliere semestre2 = new SemestreFiliere(2);
        for (Module module : moduleList2) {
            module.setSemestreFiliere(semestre2);
        }
        semestre2.getModules().addAll(moduleList2);
        semestre2.setFiliere(filiere);
        filiere.getSemestreFilieres().add(semestre2);
        filiereRepository.save(filiere);

    }


}
