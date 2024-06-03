package org.example.service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.mongodb.MongoException;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import org.example.doa.MongoDBDoa;
import org.example.model.Address;
import org.example.model.MustacheDataModel;
import org.example.model.Person;
import java.io.StringWriter;
import java.util.List;

public class PersonService {
    private final MongoDBDoa mongoDBDoa;

    public PersonService() {
        this.mongoDBDoa = new MongoDBDoa();
        this.mongoDBDoa.createUniqueIndex("email");
//        this.mongoDBDoa.createUniqueIndex("firstName");
    }

    @GET
    @Path("/search")
    public Response sayHello(@QueryParam("firstName") String firstName) {
        MustacheFactory mustacheFactory = new DefaultMustacheFactory();

        Mustache mustache = mustacheFactory.compile("hello_template.mustache");

        List<Person> people = null;
        boolean searchComplete = false;

        if (firstName != null) {
            people = mongoDBDoa.getPerson(firstName);
            searchComplete = true;
            mongoDBDoa.close();
        }

        if (people != null) {
            System.out.println("People length = " + people.size());
        }

        MustacheDataModel data = new MustacheDataModel(people, searchComplete, null, null);

        StringWriter writer = new StringWriter();
        mustache.execute(writer, data);

        String htmlContent = writer.toString();

        // Build a ResponseBuilder and set the HTML content and status code
        Response.ResponseBuilder responseBuilder = Response.ok(htmlContent);

        // Optionally, set additional headers
        responseBuilder.header("Content-Type", "text/html");

        // Build the response and return it
        return responseBuilder.build();
    }

    @POST
    @Path("/create")
    public Response createPerson(
            @FormParam("firstName") String firstName,
            @FormParam("lastName") String lastName,
            @FormParam("email") String email,
            @FormParam("address1") String address1,
            @FormParam("address2") String address2,
            @FormParam("town") String town,
            @FormParam("postCode") String postCode) {
        Person person = new Person(firstName, lastName, email, new Address(address1, address2, town, postCode));
        System.out.println("FirstName = " + person.getFirstName() + ", LastName = " + person.getLastName() + ", Email = " + person.getEmail());

        String errorMessage = null;
        String creationSuccessMessage = null;
        try {
            mongoDBDoa.insertPerson(person);
            creationSuccessMessage = person + " has been successfully created";
        } catch (MongoException e) {
            errorMessage = e.getMessage();
        }


        MustacheFactory mustacheFactory = new DefaultMustacheFactory();
        Mustache mustache = mustacheFactory.compile("hello_template.mustache");
        MustacheDataModel data = new MustacheDataModel(null, false,errorMessage, creationSuccessMessage);

        StringWriter writer = new StringWriter();
        mustache.execute(writer, data);

        String htmlContent = writer.toString();

        // Build a ResponseBuilder and set the HTML content and status code
        Response.ResponseBuilder responseBuilder = Response.ok(htmlContent);

        // Optionally, set additional headers
        responseBuilder.header("Content-Type", "text/html");

        // Build the response and return it
        return responseBuilder.build();
    }
}
