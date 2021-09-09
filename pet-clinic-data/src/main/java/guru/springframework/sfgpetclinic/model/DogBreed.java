package guru.springframework.sfgpetclinic.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class DogBreed {
    private String dogBreedName;
    private String dogBreedUrl;
    private String country;
    private String text;
    private String altname;
}
