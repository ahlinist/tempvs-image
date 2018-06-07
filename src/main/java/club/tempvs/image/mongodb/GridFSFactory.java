package club.tempvs.image.mongodb;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.gridfs.GridFS;

public class GridFSFactory {

    private static final String MONGODB_URI = System.getenv("MONGODB_URI");
    private static final DB db = initDB();

    public GridFS getGridFS(String collection) {
        return new GridFS(db, collection);
    }

    private static DB initDB() {
        if (MONGODB_URI == null) {
            return null;
        }

        String[] splittedMongodbUri = MONGODB_URI.split("/");
        String name = splittedMongodbUri[splittedMongodbUri.length - 1];
        MongoClientURI mongoClientURI = new MongoClientURI(MONGODB_URI);
        MongoClient mongoClient = new MongoClient(mongoClientURI);

        return new DB(mongoClient, name);
    }
}
