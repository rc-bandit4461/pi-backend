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
import java.beans.Transient;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {
    List<Element> semestre1Elements = new ArrayList<>();
    List<Element> semestre2Elements = new ArrayList<>();
    List<Element> semestre3Elements = new ArrayList<>();
    List<Element> semestre4Elements = new ArrayList<>();
    List<Filiere> filiereList = new ArrayList<>();
    List<Diplome> diplomeList = new ArrayList<>();
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
    private SemestreEtudiantRepository semestreEtudiantRepository;
    @Autowired
    private DiplomeRepository diplomeRepository;
    @Autowired
    private SessionRepository sessionRepository;

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        restConfiguration.exposeIdsFor(Element.class);
        restConfiguration.exposeIdsFor(Diplome.class);
        restConfiguration.exposeIdsFor(Module.class);
        restConfiguration.exposeIdsFor(Examen.class);
        restConfiguration.exposeIdsFor(NoteExamen.class);
        restConfiguration.exposeIdsFor(NoteModule.class);
        restConfiguration.exposeIdsFor(NoteElementModule.class);
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
        preloadDiplomes();
        preloadFiliere1();
        preloadFiliere2();
        preloadSession();


    }

    @Transactional
    public void preloadDiplomes() {
        diplomeList.add(new Diplome("cycle_ing", "Cycle d'ingénieur", 3));
        diplomeList.add(new Diplome("cycle_mast", "Cycle Master", 2));
        diplomeList.add(new Diplome("cycle_dut", "Cycle DUT", 2));
        diplomeList.add(new Diplome("cycle_lic", "Cycle Licence", 1));
        diplomeRepository.saveAll(diplomeList);

    }

    @Transactional
    public void preloadSession() {
        Session session = new Session(2004, filiereList.get(0));
        List<SemestreFiliere> semestreFiliereList = new ArrayList<>(filiereList.get(0).getSemestreFilieres());
        List<SemestreEtudiant> semestreEtudiants = new ArrayList<>();
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
        });
        session.setSemestreEtudiants(semestreEtudiants);
        sessionRepository.save(session);

        //update, now we have to save semestreEtudiant by ourselves, so we can use Module IDs in NoteModule,
        semestreEtudiantRepository.saveAll(semestreEtudiants);
        //Session ID is not available UNTIL we save the Session--> meaning that we need to save the Session, and then
        //we can save the session without
//        System.out.println(session.getId());
        sessionRepository.save(session);
        sessionList.add(session);
        List<EtudiantSession> etudiantSessionList = new ArrayList<>();
        for (Etudiant etudiant : etudiantArrayList) {
            etudiantSessionList.add(new EtudiantSession(etudiant, session));
        }
        etudiantSessionRepository.saveAll(etudiantSessionList);
    }


    public void preLoadEtudiants() {
        etudiantArrayList.add(new Etudiant("MA137551", "Zakaria", "Chadli", "15132215864", "homme", LocalDate.of(1997, 5, 20), "Kenitra", "zakaria.chadli@gmail.com", "dickhead"));
        etudiantArrayList.add(new Etudiant("RP137552", "Hamza", "Gueddi", "1525486868788", "homme", LocalDate.of(1997, 5, 20), "Salé", "hamza.gueddi@gmail.com", "homo"));
        etudiantArrayList.add(new Etudiant("CA137553", "Yassine", "Faiq", "1525486868788", "homme", LocalDate.of(1997, 5, 20), "Laayoune", "yassine.faiq@gmail.com", "simp"));
        etudiantRepository.saveAll(etudiantArrayList);
    }

    public void preloadEleemnts() {
        semestre1Elements.add(new Element("Analyse Numérique 1"));
        semestre1Elements.add(new Element("Logique et Algèbre Linéaire"));
        semestre1Elements.add(new Element("Probabilité"));
        semestre1Elements.add(new Element("Recherche Operationnelle"));
        semestre1Elements.add(new Element("Algorithmique"));
        semestre1Elements.add(new Element("Programmation en langage C"));
        semestre1Elements.add(new Element("Introduction aux bases de données"));
        semestre1Elements.add(new Element("SQL et SGBD"));
        semestre1Elements.add(new Element("Architecture des ordinateurs et assembleur"));
        semestre1Elements.add(new Element("Techniques de base pour les réseaux"));
        semestre1Elements.add(new Element("Economie générale"));
        semestre1Elements.add(new Element("Environnement socio-économique et institutionnel"));
        semestre1Elements.add(new Element("Anglais 1"));
        semestre1Elements.add(new Element("Techniques de communication en langue française 1"));
        elementRepository.saveAll(semestre1Elements);
        semestre1Elements.add(new Element("Analyse Numérique 2"));
        semestre2Elements.add(new Element("Statistiques"));
        semestre2Elements.add(new Element("Programmation fonctionnelle : concepts et outils"));
        semestre2Elements.add(new Element("Structures de données"));
        semestre2Elements.add(new Element("Conception et programmation orientée objet avec C++"));
        semestre2Elements.add(new Element("Projet programmation orientée objet avec C++"));
        semestre2Elements.add(new Element("Développement web"));
        semestre2Elements.add(new Element("Projet Développement web"));
        semestre2Elements.add(new Element("Systèmes d’exploitation Windows/Unix/Linux"));
        semestre2Elements.add(new Element("Théorie des systèmes d’exploitation"));
        semestre2Elements.add(new Element("Projet personnel 1"));
        semestre2Elements.add(new Element("Comptabilité générale"));
        semestre2Elements.add(new Element("Gestion"));
        semestre2Elements.add(new Element("Anglais 2"));
        semestre2Elements.add(new Element("Techniques de communication en langue française 2"));
        elementRepository.saveAll(semestre2Elements);
    }

    @Transactional
    public void preloadFiliere1() {   //Filieres  + Modules + Elements

        List<Module> moduleList1 = new ArrayList<>();
        List<Module> moduleList2 = new ArrayList<>();
        Module m1 = new Module("Mathematique Appliquée 1");
        Module m2 = new Module("Mathematique Appliquée 1");
        Module m3 = new Module("Techniques de programmation");
        Module m4 = new Module("Bases de données");
        Module m5 = new Module("Technologies");

        m1.getElements().add(semestre1Elements.get(0));
        m1.getElements().add(semestre1Elements.get(1));
        m2.getElements().add(semestre1Elements.get(2));
        m2.getElements().add(semestre1Elements.get(3));
        m3.getElements().add(semestre1Elements.get(4));
        m3.getElements().add(semestre1Elements.get(5));
        m4.getElements().add(semestre1Elements.get(6));
        m4.getElements().add(semestre1Elements.get(7));
        m5.getElements().add(semestre1Elements.get(8));
        moduleList1.add(m1);
        moduleList1.add(m2);
        moduleList2.add(m3);
        moduleList2.add(m4);
        moduleList2.add(m5);
        Filiere filiere = new Filiere("GLSID", "Génie Logiciel Système Informatique Distribuées", 2, diplomeRepository.getByLibelleContains("cycle_ing"));

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
        Module m1 = new Module("Mathematique Appliquée 3");
        Module m2 = new Module("Structures de dennées et programmation Fonctionnelles");
        Module m3 = new Module("Programmation oriéntée objet");
        Module m4 = new Module("Technologies web");
        Module m5 = new Module("Systeme d'exploitation");

        m1.getElements().add(semestre1Elements.get(0));
        m1.getElements().add(semestre1Elements.get(1));
        m2.getElements().add(semestre1Elements.get(2));
        m2.getElements().add(semestre1Elements.get(3));
        m3.getElements().add(semestre1Elements.get(4));
        m3.getElements().add(semestre1Elements.get(5));
        m4.getElements().add(semestre1Elements.get(6));
        m4.getElements().add(semestre1Elements.get(7));
        m5.getElements().add(semestre1Elements.get(8));
        moduleList1.add(m1);
        moduleList1.add(m2);
        moduleList2.add(m3);
        moduleList2.add(m4);
        moduleList2.add(m5);
        Filiere filiere = new Filiere("II-BDCC", "Ingénierie Informatique - Big Data & Cloud Computing", 2, diplomeRepository.getByLibelleContains("cycle_ing"));
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
