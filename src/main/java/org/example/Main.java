package org.example;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngine;
import org.apache.cxf.transport.http_jetty.JettyHTTPServerEngineFactory;
import org.eclipse.jetty.server.Server;
import org.example.service.PersonService;

import java.io.IOException;
import java.security.GeneralSecurityException;

@ApplicationPath("/api")
public class Main extends Application {

    public static void main(String[] args) throws GeneralSecurityException, IOException {
        Server server = new Server();

        JAXRSServerFactoryBean factoryBean = new JAXRSServerFactoryBean();
        factoryBean.setResourceClasses(PersonService.class);
        factoryBean.setAddress("http://localhost:8080");
        factoryBean.create();

        JettyHTTPServerEngineFactory factory = new JettyHTTPServerEngineFactory();
        factory.setBus(factoryBean.getBus());

        JettyHTTPServerEngine serverEngine = factory.createJettyHTTPServerEngine(8080, "http");

//        String mongodbUri = "mongodb://djhayes:password@0.0.0.0:27017/";
//
//        try (MongoClient mongoClient = MongoClients.create(mongodbUri)) {
//            CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
//            CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));
//
//            MongoDatabase database = mongoClient.getDatabase("staff").withCodecRegistry(pojoCodecRegistry);
//            MongoCollection<Person> collection = database.getCollection("people", Person.class);
//
//
//            Person person = new Person("Daniel", "Hayes", "myEmail@email.com", new Address("my address 1", "my address 2", "Manchester", "M4D FGR"));
//            collection.insertOne(person);
//        }

        try {
            server = serverEngine.getServer();
            server.start();
            System.out.println("Server started at http://localhost:8080/api");
            server.join(); // Keep the server running until interrupted
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("Shutting down the server...");
            server.destroy();
            System.out.println("Shutdown complete!");
        }
    }
}