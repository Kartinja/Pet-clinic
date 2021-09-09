package guru.springframework.sfgpetclinic.controllers;

import guru.springframework.sfgpetclinic.model.DogBreed;
import guru.springframework.sfgpetclinic.services.DBPediaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/dogbreeds")
public class DogBreedsController {

    private final DBPediaService dbPediaService;

    public DogBreedsController(DBPediaService dbPediaService) {
        this.dbPediaService = dbPediaService;
    }

    @GetMapping
    public String getAllDogBreeds(Model model) {
        List<DogBreed> list = dbPediaService.listAllDogBreeds();
        model.addAttribute("dogbreeds", list);
        return "dogbreeds/dogbreeds";
    }

    @GetMapping("/{dogName}")
    public String getDogBreedInfo(Model model, @PathVariable String dogName) {
        DogBreed dogBreed = dbPediaService.getDogBreed(dogName);
        model.addAttribute("dogbreed", dogBreed);
        return "dogbreeds/dogbreed";
    }
}
