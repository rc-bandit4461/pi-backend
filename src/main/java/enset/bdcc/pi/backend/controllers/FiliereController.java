package enset.bdcc.pi.backend.controllers;

import enset.bdcc.pi.backend.dao.ElementRepository;
import enset.bdcc.pi.backend.dao.FiliereRepository;
import enset.bdcc.pi.backend.dao.ModuleRepository;
import enset.bdcc.pi.backend.dao.SemestreFiliereRepository;
import enset.bdcc.pi.backend.entities.Element;
import enset.bdcc.pi.backend.entities.Filiere;
import enset.bdcc.pi.backend.entities.Module;
import enset.bdcc.pi.backend.entities.SemestreFiliere;
import net.minidev.json.JSONObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")

@RestController
public class FiliereController {
    @Autowired
    private ElementRepository elementRepository;
    @Autowired
    private FiliereRepository filiereRepository;
    @Autowired
    private SemestreFiliereRepository semestreFiliereRepository;
    @Autowired
    private ModuleRepository moduleRepository;



    @PostMapping(value = "/saveFiliere")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public void save2(@RequestBody Filiere filiere) throws ResponseStatusException {
        System.out.println(filiere.toString());
        int i = 1;
        for (SemestreFiliere semestreFiliere : filiere.getSemestreFilieres()) {
            semestreFiliere.setNumero(i++);
            for (Module module : semestreFiliere.getModules()) {
                List<Element> elementList = new ArrayList<>();
                for (Element element : module.getElements()) {
                    elementList.add(elementRepository.getOne(element.getId()));
                }
                module.setElements(elementList);
                module.setSemestreFiliere(semestreFiliere);
            }
            semestreFiliere.setFiliere(filiere);
        }
        System.out.println("Before Saving");
        filiereRepository.save(filiere);


    }

    @PutMapping(value = "/editFiliere/{id}")
    @Transactional
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void edit(@PathVariable(name = "id") Long id, @RequestBody Filiere filiere) {
        int i = 1;
        Filiere filiere1 = filiereRepository.getOne(id);
        filiere1.setLibelle(filiere.getLibelle());
        semestreFiliereRepository.deleteAll(filiere1.getSemestreFilieres());
        filiere1.getSemestreFilieres().clear();

        for (SemestreFiliere semestreFiliere : filiere.getSemestreFilieres()) {
            semestreFiliere.setNumero(i++);
            for (Module module : semestreFiliere.getModules()) {
                List<Element> elementList = new ArrayList<>();
                for (Element element : module.getElements()) {
                    elementList.add(elementRepository.getOne(element.getId()));
                }
                module.setElements(elementList);
                module.setSemestreFiliere(semestreFiliere);
            }
            semestreFiliere.setFiliere(filiere1);
            filiere1.getSemestreFilieres().add(semestreFiliere);
        }

        filiereRepository.save(filiere1);


    }
    @DeleteMapping(value = "/deleteFiliere/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public void delete(@PathVariable(name = "id") Long id) throws ResponseStatusException {
        filiereRepository.deleteById(id);


    }
}
