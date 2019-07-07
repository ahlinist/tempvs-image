package club.tempvs.image.service;

import club.tempvs.image.domain.Image;

import java.util.List;

public interface ImageService {

    byte[] getImage(String id);

    void store(Image image);

    void delete(List<String> objectIds);
}
