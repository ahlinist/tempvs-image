package club.tempvs.image.amqp;

import club.tempvs.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.util.List;

@RequiredArgsConstructor
@EnableBinding(ImageEventProcessor.class)
public class ImageEventHandler {

    private final ImageService imageService;

    @StreamListener(ImageEventProcessor.DELETE_IMAGES)
    public void deleteImages(List<String> objectIds) {
        imageService.delete(objectIds);
    }
}
