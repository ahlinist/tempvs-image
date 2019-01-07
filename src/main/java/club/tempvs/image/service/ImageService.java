package club.tempvs.image.service;

import club.tempvs.image.domain.Image;

import java.util.List;

public interface ImageService {

    byte[] getImage(String id);

    List<Image> storeImages(List<Image> images);

    void deleteImage(String id);

    void deleteImages(List<Image> images);
}
