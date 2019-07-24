package club.tempvs.image.amqp;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ImageEventProcessor {

    String DELETE_IMAGES_BY_IDS = "image.delete";
    String DELETE_IMAGES_FOR_ITEM = "image.deleteAll";

    @Input(DELETE_IMAGES_BY_IDS)
    SubscribableChannel deleteByIds();

    @Input(DELETE_IMAGES_FOR_ITEM)
    SubscribableChannel deleteForItem();
}
