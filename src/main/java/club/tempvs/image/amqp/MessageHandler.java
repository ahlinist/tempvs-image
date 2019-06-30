package club.tempvs.image.amqp;

import club.tempvs.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.util.List;

@RequiredArgsConstructor
@EnableBinding(MessageProcessor.class)
public class MessageHandler {

    private final ImageService imageService;

    @StreamListener(MessageProcessor.DELETE_IMAGES)
    public void deleteImages(List<String> objectIds) {
        imageService.delete(objectIds);
    }
}
