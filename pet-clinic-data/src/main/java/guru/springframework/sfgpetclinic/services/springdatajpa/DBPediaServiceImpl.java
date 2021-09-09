package guru.springframework.sfgpetclinic.services.springdatajpa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import guru.springframework.sfgpetclinic.model.DogBreed;
import guru.springframework.sfgpetclinic.services.DBPediaService;
import org.apache.jena.atlas.json.JsonObject;
import org.apache.jena.atlas.json.JsonValue;
import org.apache.jena.query.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFParser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class DBPediaServiceImpl implements DBPediaService, CommandLineRunner {
    private List<DogBreed> dogBreeds = new ArrayList<>();

    public DBPediaServiceImpl() {
        Model model = ModelFactory.createDefaultModel();
        RDFParser.source("http://dbpedia.org/data/Dog_breed.ttl").httpAccept("text/turtle").parse(model.getGraph());

        Property dogBreedProperty = model.getProperty("http://dbpedia.org/ontology/wikiPageWikiLink");
        ResIterator iterator = model.listSubjectsWithProperty(dogBreedProperty);
        while (iterator.hasNext()) {
            String dogUrl = iterator.nextResource().getProperty(dogBreedProperty).getSubject().toString();
            DogBreed newDogBreed = new DogBreed();
            String wikiURL = dogUrl.replace("dbpedia.org/resource", "wikipedia.org/wiki");
            newDogBreed.setDogBreedUrl(wikiURL);
            newDogBreed.setDogBreedName(dogUrl.split("/")[4]);
            this.dogBreeds.add(newDogBreed);
        }
    }

    @Override
    public List<DogBreed> listAllDogBreeds() {
        return this.dogBreeds;
    }


    @Override
    public DogBreed getDogBreed(String dogName) {
        String queryString = "PREFIX dbo: <http://dbpedia.org/ontology/>\n" +
                "PREFIX dbp: <http://dbpedia.org/property/>\n" +
                "PREFIX dbr: <http://dbpedia.org/resource/>" +
                "select ?text ?country ?aname\n" +
                "WHERE" +
                "{\n" +
                "dbr:" + dogName + " dbo:abstract ?text;\n" +
                "dbp:country ?country;\n" +
                "dbp:altname ?aname.\n" +
                "FILTER(lang(?text) = \"en\")\n" +
                "}";

        Query query = QueryFactory.create(queryString);
        QueryExecution queryExecution = QueryExecutionFactory.sparqlService("https://dbpedia.org/sparql", query);
        ResultSet resultSet = queryExecution.execSelect();
        DogBreed newDogBreed = new DogBreed();
        newDogBreed.setDogBreedName(dogName);
        boolean flag = true;
        String []output = ResultSetFormatter.asText(resultSet).split("\\|");

        newDogBreed.setText(output[5].replace("@en", ""));
        newDogBreed.setCountry(output[6].replace("@en", "").split("/")[4].replace(">", ""));
        newDogBreed.setAltname(output[7].replace("@en", ""));

        return newDogBreed;
    }

    @Override
    public void run(String... args) throws Exception {


    }
}
