package club.tempvs.image.domain;

import lombok.Data;

@Data
public class Image {
    private String objectId;
    private String imageInfo;
    private String content;
    private String fileName;

    public Image(String objectId, String imageInfo, String fileName) {
        this.objectId = objectId;
        this.imageInfo = imageInfo;
        this.fileName = fileName;
    }
}
