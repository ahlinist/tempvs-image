package club.tempvs.image.amqp;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ImageEventProcessor {

    String DELETE_IMAGES = "image.delete";

    @Input(DELETE_IMAGES)
    SubscribableChannel delete();
}
