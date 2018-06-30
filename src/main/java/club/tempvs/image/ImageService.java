package club.tempvs.image;

import club.tempvs.image.auth.AuthenticationException;
import club.tempvs.image.json.ImageSketch;
import club.tempvs.image.json.StorePayload;
import club.tempvs.image.mongodb.GridFSFactory;
import club.tempvs.image.json.ImagePayload;
import club.tempvs.image.json.Image;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;

import java.io.InputStream;
import java.util.Base64;

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

    public ImagePayload store(StorePayload payload, String token) {
        authenticate(token);
        payload.validate();

        ImagePayload imagePayload = new ImagePayload();

        for (ImageSketch image : payload.getImages()) {
            String collection = image.getCollection();
            GridFS gridFS = gridFSFactory.getGridFS(collection);
            Base64.Decoder decoder = Base64.getDecoder();
            byte[] bytes = decoder.decode(image.getContent());
            GridFSInputFile gridFSInputFile = gridFS.createFile(bytes);
            gridFSInputFile.save();
            String objectId = gridFSInputFile.getId().toString();
            imagePayload.addImage(new Image(objectId, collection, image.getImageInfo()));
        }

        return imagePayload;
    }

    public void delete(ImagePayload payload, String token) {
        authenticate(token);
        payload.validate();

        for (Image image : payload.getImages()) {
            GridFS gridFS = gridFSFactory.getGridFS(image.getCollection());
            ObjectId objectId = new ObjectId(image.getObjectId());
            GridFSDBFile gridFSDBFile = gridFS.findOne(objectId);

            if (gridFSDBFile != null) {
                gridFS.remove(gridFSDBFile);
            }
        }
    }

    private void authenticate(String receivedToken) {
        if (receivedToken == null || !receivedToken.equals(tokenHash)) {
            throw new AuthenticationException();
        }
    }
}
