package club.tempvs.image.dao;

public interface ImageDao {

    byte[] get(String id);

    void save(String content, String fileName, Object metaData);

    void delete(String id);
}
