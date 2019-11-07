package club.tempvs.image.controller;

import club.tempvs.image.domain.Image;
import club.tempvs.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping("/image/{belongsTo}/{entityId}")
    public List<Image> getImages(@PathVariable String belongsTo, @PathVariable String entityId) {
        return imageService.getImages(belongsTo, entityId);
    }
}
