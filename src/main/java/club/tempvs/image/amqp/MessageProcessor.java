package club.tempvs.image.amqp;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface MessageProcessor {

    String DELETE_IMAGES = "image.delete";

    @Input("image.delete")
    SubscribableChannel delete();
}
