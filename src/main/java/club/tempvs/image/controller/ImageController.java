package club.tempvs.image.controller;

import club.tempvs.image.api.UnauthorizedException;
import club.tempvs.image.domain.Image;
import club.tempvs.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
public class ImageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ImageController.class);

    private final ImageService imageService;

    @GetMapping("/{belongsTo}/{entityId}")
    public List<Image> getImages(@PathVariable String belongsTo, @PathVariable String entityId) {
        return imageService.getImages(belongsTo, entityId);
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public String throwUnauthorizedException(UnauthorizedException e) {
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
