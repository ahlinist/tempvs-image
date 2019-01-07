package club.tempvs.image.dto;

import club.tempvs.image.domain.Image;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ImagePayload {
    private List<Image> images;

    public ImagePayload(List<Image> images) {
        this.images = images;
    }
}
