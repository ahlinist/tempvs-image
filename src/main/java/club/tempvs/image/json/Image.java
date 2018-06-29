package club.tempvs.image.json;

public class Image {

    private String objectId;
    private String collection;
    private String imageInfo;

    public Image() {

    }

    public Image(String objectId, String collection, String imageInfo) {
        this.objectId = objectId;
        this.collection = collection;
        this.imageInfo = imageInfo;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getCollection() {
        return collection;
    }

    public void setCollection(String collection) {
        this.collection = collection;
    }

    public String getImageInfo() {
        return imageInfo;
    }

    public void setImageInfo(String imageInfo) {
        this.imageInfo = imageInfo;
    }
}
