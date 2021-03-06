package club.tempvs.image.service;

import club.tempvs.image.domain.Image;

import java.util.List;

public interface ImageService {

    List<Image> getImages(String belongsTo, String entityId);

    void store(Image image);

    void replace(Image image);

    void delete(List<String> objectIds);

    void delete(String belongsTo, String entityId);
}
