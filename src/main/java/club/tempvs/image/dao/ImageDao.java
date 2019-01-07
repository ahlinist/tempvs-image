package club.tempvs.image.dao;

import club.tempvs.image.domain.Image;

public interface ImageDao {

    byte[] get(String id);

    Image save(Image image);

    void delete(String id);

    void delete(Image image);
}
