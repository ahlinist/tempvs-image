package club.tempvs.image.domain;

import lombok.Data;

@Data
public class Image {

    private String objectId;
    private String imageInfo;
    private String entityId;
    private String belongsTo;
    private String content;
    private String fileName;

    public Image(String objectId, String imageInfo, String entityId, String belongsTo, String fileName) {
        this.objectId = objectId;
        this.imageInfo = imageInfo;
        this.entityId = entityId;
        this.belongsTo = belongsTo;
        this.fileName = fileName;
    }
}
