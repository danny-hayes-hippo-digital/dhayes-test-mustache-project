package org.example.doa;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.IndexOptions;
import org.bson.Document;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.example.model.Person;

import java.util.ArrayList;
import java.util.List;

import static com.mongodb.MongoClientSettings.getDefaultCodecRegistry;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

public class MongoDBDoa {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Person> collection;

    public MongoDBDoa() {
        String mongodbUri = "mongodb://djhayes:password@0.0.0.0:27017/";

        mongoClient = MongoClients.create(mongodbUri);
        CodecProvider pojoCodecProvider = PojoCodecProvider.builder().automatic(true).build();
        CodecRegistry pojoCodecRegistry = fromRegistries(getDefaultCodecRegistry(), fromProviders(pojoCodecProvider));

        database = mongoClient.getDatabase("staff").withCodecRegistry(pojoCodecRegistry);
        collection = database.getCollection("people", Person.class);
    }

    public void createUniqueIndex(String fieldName) {
        // Create a unique index on the specified field
        IndexOptions indexOptions = new IndexOptions().unique(true);
        collection.createIndex(new Document(fieldName, 1), indexOptions);
    }

    public void insertPerson(Person person) {
        // Insert a document into the collection
        collection.insertOne(person);
    }

    public List<Person> getPerson(String firstName) {
        Bson filter = Filters.eq("firstName", firstName);
        return collection.find(filter).into(new ArrayList<>());
    }

    public void close() {
        // Close the MongoDB client
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
