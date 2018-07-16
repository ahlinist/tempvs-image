package club.tempvs.image;

import club.tempvs.image.auth.AuthenticationException;
import club.tempvs.image.auth.TokenHelper;
import club.tempvs.image.json.*;
import club.tempvs.image.mongodb.GridFSFactory;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;
import org.bson.types.ObjectId;

import java.io.InputStream;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

public class ImageService {

    private static final String ERROR_DELIMITER = ", ";
    private static final String DEFAULT_IMAGE_NAME = "default_image.gif";

    private String tokenHash;
    private GridFSFactory gridFSFactory;

    public ImageService(TokenHelper tokenHelper, GridFSFactory gridFSFactory) {
        this.tokenHash = tokenHelper.getTokenHash();
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
            deleteSingleImage(image.getCollection(), image.getObjectId());
        }
    }

    public void delete(String collection, String id, String token) {
        authenticate(token);
        validate(collection, id);
        deleteSingleImage(collection, id);
    }

    private void deleteSingleImage(String collection, String id) {
        GridFS gridFS = gridFSFactory.getGridFS(collection);
        ObjectId objectId = new ObjectId(id);
        GridFSDBFile gridFSDBFile = gridFS.findOne(objectId);

        if (gridFSDBFile != null) {
            gridFS.remove(gridFSDBFile);
        }
    }

    private void authenticate(String receivedToken) {
        if (receivedToken == null || !receivedToken.equals(tokenHash)) {
            throw new AuthenticationException();
        }
    }

    private void validate(String collection, String id) {
        Boolean payloadValid = Boolean.TRUE;
        Set<String> errors = new HashSet<>();

        if (collection == null) {
            payloadValid = Boolean.FALSE;
            errors.add("Payload contains entries with missing image content");
        }

        if (id == null) {
            payloadValid = Boolean.FALSE;
            errors.add("Payload contains entries with missing collection");
        }

        if (!payloadValid) {
            throw new IllegalArgumentException(String.join(ERROR_DELIMITER, errors));
        }
    }
}
