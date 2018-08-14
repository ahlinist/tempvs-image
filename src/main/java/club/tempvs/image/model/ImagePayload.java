package club.tempvs.image.model;

import java.util.*;
import club.tempvs.rest.model.Payload;
import club.tempvs.rest.model.PayloadMalformedException;

public class ImagePayload implements Payload {

    private List<Image> images = new ArrayList<>();

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        images.removeIf(Objects::isNull);
        this.images = images;
    }

    public void addImage(Image image) {
        images.add(image);
    }

    public void validate() {
        if (images == null || images.isEmpty()) {
            throw new PayloadMalformedException("Payload doesn't contain any images");
        }

        Boolean payloadValid = Boolean.TRUE;
        Set<String> errors = new HashSet<>();

        for (Image image : images) {
            if (image.getObjectId() == null) {
                payloadValid = Boolean.FALSE;
                errors.add("Payload contains entries with missing objectId");
            }

            if (image.getCollection() == null) {
                payloadValid = Boolean.FALSE;
                errors.add("Payload contains entries with missing collection");
            }
        }

        if (!payloadValid) {
            throw new PayloadMalformedException(String.join(", ", errors));
        }
    }
}
