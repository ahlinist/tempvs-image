package club.tempvs.image.controller;

import club.tempvs.image.api.UnauthorizedException;
import club.tempvs.image.domain.Image;
import club.tempvs.image.service.ImageService;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.MediaType.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ImageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;

    @GetMapping(value = "/image/{id}", produces = IMAGE_JPEG_VALUE)
    public ResponseEntity getImage(@PathVariable("id") String id) {
        //TODO: apply caching
        byte[] image = imageService.getImage(id);
        return ResponseEntity.ok(image);
    }

    @PostMapping("/image")
    public Image store(@RequestBody Image payload) {
        return imageService.store(payload);
    }

    @Deprecated
    @DeleteMapping("/image/{id}")
    public void delete(@PathVariable("id") String id) {
        imageService.delete(Arrays.asList(id));
    }

    @Deprecated
    @PostMapping("/image/delete")
    public void bulkDelete(@RequestBody List<String> objectIds) {
        imageService.delete(objectIds);
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
