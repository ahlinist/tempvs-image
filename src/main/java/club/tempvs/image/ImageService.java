package club.tempvs.image;

import club.tempvs.image.mongodb.GridFSFactory;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import org.bson.types.ObjectId;

import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageService {

    private String tokenHash;
    private GridFSFactory gridFSFactory;

    public ImageService(String tokenHash, GridFSFactory gridFSFactory) {
        this.tokenHash = tokenHash;
        this.gridFSFactory = gridFSFactory;
    }

    public InputStream getImageStream(String id, String collection) throws IOException {
        ObjectId objectId = new ObjectId(id);
        GridFS gridFS = gridFSFactory.getGridFS(collection);
        GridFSDBFile gridFSDBFile = gridFS.findOne(objectId);

        if (gridFSDBFile == null) {
            throw new NotFoundException();
        }

        try(InputStream inputStream = gridFSDBFile.getInputStream()) {
            return inputStream;
        }
    }
}
