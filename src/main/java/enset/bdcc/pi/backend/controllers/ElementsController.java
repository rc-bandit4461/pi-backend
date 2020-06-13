package enset.bdcc.pi.backend.controllers;

import enset.bdcc.pi.backend.dao.ElementRepository;
import enset.bdcc.pi.backend.entities.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class ElementsController {
    @Autowired
    private ElementRepository elementRepository;
    @PostMapping(value = "/listElements")
    public Element save(@RequestBody Element element){
        return elementRepository.save(element);
    }
}
