package club.tempvs.image.controller;

import club.tempvs.image.api.UnauthorizedException;
import club.tempvs.image.domain.Image;
import club.tempvs.image.dto.ImagePayload;
import club.tempvs.image.service.ImageService;
import club.tempvs.image.util.ObjectFactory;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import static org.springframework.http.MediaType.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;
    private final ObjectFactory objectFactory;

    @GetMapping(value = "/image/{id}", produces = IMAGE_JPEG_VALUE)
    public ResponseEntity getImage(@PathVariable("id") String id) {
        //TODO: apply caching
        byte[] image = imageService.getImage(id);
        return ResponseEntity.ok(image);
    }

    @PostMapping("/image")
    public ResponseEntity storeImages(@RequestBody ImagePayload payload) {
        List<Image> images = payload.getImages();

        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("Empty storing payload received");
        }

        List<Image> resultImages = imageService.storeImages(images);

        if (!resultImages.isEmpty()) {
            ImagePayload resultPayload = objectFactory.getInstance(ImagePayload.class, resultImages);
            return ResponseEntity.ok(resultPayload);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping("/image/{id}")
    public ResponseEntity delete(@PathVariable("id") String id) {
        imageService.deleteImage(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/image/delete")
    public ResponseEntity bulkDelete(@RequestBody ImagePayload payload) {
        List<Image> images = payload.getImages();

        if (images == null || images.isEmpty()) {
            throw new IllegalArgumentException("Empty deletion payload received");
        }

        imageService.deleteImages(images);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String throwUnauthorizedException(UnauthorizedException e) {
        return processException(e);
    }

    @ExceptionHandler(HystrixRuntimeException.class)
    @ResponseStatus(HttpStatus.REQUEST_TIMEOUT)
    public String throwTimeOutException(HystrixRuntimeException e) {
        return processException(e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String throwIllegalArgumentException(IllegalArgumentException e) {
        return processException(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String throwException(Exception e) {
        return processException(e);
    }

    private String processException(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTraceString = sw.toString();
        LOGGER.error(stackTraceString);
        return e.getMessage();
    }
}
