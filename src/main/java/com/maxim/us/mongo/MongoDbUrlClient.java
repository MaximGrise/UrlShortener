package com.maxim.us.mongo;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;

import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.maxim.us.converter.BaseConverter;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;

@Component
public class MongoDbUrlClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(MongoDbUrlClient.class);

    @Value("${mongodb.user}")
    private String user;

    @Value("${mongodb.database}")
    private String database;

    @Value("${mongodb.password}")
    private String password;

    @Value("${mongodb.host}")
    private String host;

    @Value("${mongodb.port}")
    private Integer port;

    @Autowired
    private BaseConverter converter;

    private MongoClient client;

    private final static AtomicLong GLOBAL_COUNT = new AtomicLong();

    @PostConstruct
    public void postConstruct() {
        client = MongoClients.create(MongoClientSettings.builder()
                .credential(MongoCredential.createCredential(user, database, password.toCharArray()))
                .applyConnectionString(new ConnectionString(buildConnectionString())).build());

        synchronized (GLOBAL_COUNT) {
            GLOBAL_COUNT.set(client.getDatabase(database).getCollection(MongoDbFields.CountersCollection.value()).find()
                    .first().getLong(MongoDbFields.UrlCounterField.value()));
        }
    }

    /**
     * Get a long url corresponding to the short version.
     * 
     * @param shortUrl
     *            the short url to resolve
     * @return the long version of the url
     */
    public String get(String shortUrl) {
        return client.getDatabase(database).getCollection(MongoDbFields.UrlCollection.value())
                .find(Filters.eq(MongoDbFields.PseudoIdField.value(), converter.decode(shortUrl))).first()
                .get(MongoDbFields.LongUrlField.value(), String.class);
    }

    /**
     * Insert a longUrl in the DB and return the converted, short version.
     * 
     * @param longUrl
     *            the url to store and convert.
     * @return the short url
     */
    public String insertAndConvert(String longUrl) throws InterruptedException {
        // might be costly to find the entire DB every time we want to
        // insert...but how to know if it exist otherwise?
        Document exists = client.getDatabase(database).getCollection(MongoDbFields.UrlCollection.value())
                .find(Filters.eq(MongoDbFields.LongUrlField.value(), longUrl)).first();

        // prevent inserting the same ID multiple times
        synchronized (GLOBAL_COUNT) {
            if (exists != null) {
                // already exists, just return it
                LOGGER.info("The url already exists in the DB, it will not be re-inserted...");
                return converter.encode(exists.get(MongoDbFields.PseudoIdField.value(), Long.class));
            } else {
                // insert the new url entry
                Map<String, Object> doc = new HashMap<>();
                doc.put(MongoDbFields.LongUrlField.value(), longUrl);
                doc.put(MongoDbFields.PseudoIdField.value(), GLOBAL_COUNT.incrementAndGet());
                client.getDatabase(database).getCollection(MongoDbFields.UrlCollection.value())
                        .insertOne(new Document(doc));

                // update the global counter in DB
                client.getDatabase(database).getCollection(MongoDbFields.CountersCollection.value()).updateOne(
                        Filters.all(MongoDbFields.UrlCounterField.value()),
                        Updates.set(MongoDbFields.UrlCounterField.value(), GLOBAL_COUNT.get()));

                return converter.encode(GLOBAL_COUNT.get());
            }
        }
    }

    private final String buildConnectionString() {
        return "mongodb+srv://" + user + ":" + password + "@maximgrise-iqe1n.mongodb.net/" + database
                + "?retryWrites=true";
    }
}
