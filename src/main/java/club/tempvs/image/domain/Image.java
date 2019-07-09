package club.tempvs.image.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Image {

    private String objectId;
    private String imageInfo;
    private String entityId;
    private String belongsTo;
    private String content;
    private String fileName;
}
