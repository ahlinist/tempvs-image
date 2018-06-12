package club.tempvs.image;

import club.tempvs.image.auth.AuthenticationException;
import club.tempvs.image.mongodb.GridFSFactory;
import club.tempvs.image.json.DeletePayload;
import club.tempvs.image.json.Image;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import org.bson.types.ObjectId;

import java.io.InputStream;

public class ImageService {

    private static final String DEFAULT_IMAGE_NAME = "default_image.gif";

    private String tokenHash;
    private GridFSFactory gridFSFactory;

    public ImageService(String tokenHash, GridFSFactory gridFSFactory) {
        this.tokenHash = tokenHash;
        this.gridFSFactory = gridFSFactory;
    }

    public InputStream getImageStream(String id, String collection) {
        ObjectId objectId = new ObjectId(id);
        GridFS gridFS = gridFSFactory.getGridFS(collection);
        GridFSDBFile gridFSDBFile = gridFS.findOne(objectId);

        if (gridFSDBFile == null) {
            return getDefaultImage();
        }

        return gridFSDBFile.getInputStream();
    }

    public InputStream getDefaultImage() {
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        return classloader.getResourceAsStream(DEFAULT_IMAGE_NAME);
    }

    public void delete(DeletePayload payload, String token) {
        authenticate(token);
        payload.validate();

        for (Image image : payload.getImages()) {
            GridFS gridFS = gridFSFactory.getGridFS(image.getCollection());
            ObjectId objectId = new ObjectId(image.getObjectId());
            GridFSDBFile gridFSDBFile = gridFS.findOne(objectId);
            gridFS.remove(gridFSDBFile);
        }
    }

    private void authenticate(String receivedToken) {
        if (receivedToken == null || !receivedToken.equals(tokenHash)) {
            throw new AuthenticationException();
        }
    }
}
