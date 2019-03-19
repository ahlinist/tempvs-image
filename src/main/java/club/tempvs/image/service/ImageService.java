package club.tempvs.image.service;

import club.tempvs.image.domain.Image;

import java.util.List;

public interface ImageService {

    byte[] getImage(String id);

    Image store(Image image);

    void delete(String id);

    void delete(List<String> objectIds);
}
