package jongo;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.MongoURI;
import com.mongodb.gridfs.GridFS;
import org.jongo.MongoCollection;
import play.Logger;
import play.Play;

import java.net.UnknownHostException;

public class Jongo {

    private static volatile Jongo service = null;

    private Mongo mongo;
    private org.jongo.Jongo jongo;
    private GridFS gridfs;

    private Jongo() throws UnknownHostException, MongoException {

        MongoURI uri = new MongoURI(Play.configuration.getProperty("mongo.uri"));
        mongo = new Mongo(uri);
        DB db = mongo.getDB(uri.getDatabase());
        db.authenticate(uri.getUsername(), uri.getPassword());
        jongo = new org.jongo.Jongo(db);

        if (Boolean.parseBoolean(Play.configuration.getProperty("mongo.gridfs.enabled"))) {
            gridfs = new GridFS(jongo.getDatabase());
        }
    }

    public static void init() {

        synchronized (Jongo.class) {
            if (service == null) {
                try {
                    service = new Jongo();
                } catch (UnknownHostException e) {
                    Logger.error("UnknownHostException", e);
                } catch (MongoException e) {
                    Logger.error("MongoException", e);
                }
            }
        }
    }

    public static void shutdown() {

        service.mongo.close();
    }

    public static org.jongo.Jongo jongo() {

        return service.jongo;
    }

    public static GridFS gridfs() {
        return service.gridfs;
    }

    public static MongoCollection getCollection(String name) {
        return service.jongo.getCollection(name);
    }

}