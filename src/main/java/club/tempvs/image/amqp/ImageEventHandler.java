package club.tempvs.image.amqp;

import club.tempvs.image.domain.Image;
import club.tempvs.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;

import java.util.List;

@RequiredArgsConstructor
@EnableBinding(ImageEventProcessor.class)
public class ImageEventHandler {

    private final ImageService imageService;

    @StreamListener(ImageEventProcessor.STORE_IMAGE)
    public void storeImage(Image payload) {
        imageService.store(payload);
    }

    @StreamListener(ImageEventProcessor.DELETE_IMAGES_BY_IDS)
    public void deleteImagesByIds(List<String> objectIds) {
        imageService.delete(objectIds);
    }

    @StreamListener(ImageEventProcessor.DELETE_IMAGES_FOR_ENTITY)
    public void deleteImagesForEntity(String query) {
        String[] splittedQuery = query.split("::");
        String belongsTo = splittedQuery[0];
        String entityId = splittedQuery[1];
        imageService.delete(belongsTo, entityId);
    }
}
