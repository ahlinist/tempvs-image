package club.tempvs.image.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import club.tempvs.rest.json.Payload;
import club.tempvs.rest.json.PayloadMalformedException;

public class StorePayload implements Payload {

    List<ImageSketch> images;

    public List<ImageSketch> getImages() {
        return images;
    }

    public void setImages(List<ImageSketch> images) {
        this.images = images;
    }

    public void validate() {
        if (images == null || images.isEmpty()) {
            throw new PayloadMalformedException("Payload doesn't contain any images");
        }

        Boolean payloadValid = Boolean.TRUE;
        Set<String> errors = new HashSet<>();

        for (ImageSketch imageSketch : images) {
            if (imageSketch.getContent() == null) {
                payloadValid = Boolean.FALSE;
                errors.add("Payload contains entries with missing image content");
            }

            if (imageSketch.getCollection() == null) {
                payloadValid = Boolean.FALSE;
                errors.add("Payload contains entries with missing collection");
            }
        }

        if (!payloadValid) {
            throw new PayloadMalformedException(String.join(", ", errors));
        }
    }
}
