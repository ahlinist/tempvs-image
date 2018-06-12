package club.tempvs.image.json;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DeletePayload implements Payload {

    private List<Image> images;

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void validate() {
        List<Image> images = this.getImages();

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
