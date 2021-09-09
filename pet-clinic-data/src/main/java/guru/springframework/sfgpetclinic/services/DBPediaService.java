package guru.springframework.sfgpetclinic.services;

import guru.springframework.sfgpetclinic.model.DogBreed;

import java.util.List;

public interface DBPediaService {
    List<DogBreed> listAllDogBreeds();

    DogBreed getDogBreed(String dogName);
}
