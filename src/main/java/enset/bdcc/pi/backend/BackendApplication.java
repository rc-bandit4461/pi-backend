package enset.bdcc.pi.backend;

import enset.bdcc.pi.backend.dao.*;
import enset.bdcc.pi.backend.entities.*;
import enset.bdcc.pi.backend.entities.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {
    List<Element> elementList = new ArrayList<>();
    List<Filiere> filiereList = new ArrayList<>();
    ArrayList<Etudiant> etudiantArrayList = new ArrayList<>();


    @Autowired
    private RepositoryRestConfiguration restConfiguration;
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
    @Autowired
    private EtudiantSessionRepository etudiantSessionRepository;

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
        preLoadEtudiants();
        preloadEleemnts();
        preloadFiliere1();
        preloadFiliere2();
        persist();


    }

    private void persist() {
        etudiantRepository.saveAll(etudiantArrayList);
        filiereRepository.saveAll(filiereList);
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
    }

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
//        filiereRepository.save(filiere);

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
//        filiereRepository.save(filiere);
//        filiereRepository.flush();

    }


}
