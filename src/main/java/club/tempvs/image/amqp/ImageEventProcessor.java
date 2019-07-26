package club.tempvs.image.amqp;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ImageEventProcessor {

    String STORE_IMAGE = "image.store";
    String DELETE_IMAGES_BY_IDS = "image.delete-by-ids";
    String DELETE_IMAGES_FOR_ENTITY = "image.delete-for-entity";

    @Input(STORE_IMAGE)
    SubscribableChannel storeImage();

    @Input(DELETE_IMAGES_BY_IDS)
    SubscribableChannel deleteByIds();

    @Input(DELETE_IMAGES_FOR_ENTITY)
    SubscribableChannel deleteForEntity();
}
