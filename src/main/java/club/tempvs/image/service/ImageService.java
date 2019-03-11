package club.tempvs.image.service;

import club.tempvs.image.domain.Image;

import java.util.List;

public interface ImageService {

    byte[] getImage(String id);

    Image store(Image image);

    void deleteImage(String id);

    void deleteImages(List<Image> images);
}
