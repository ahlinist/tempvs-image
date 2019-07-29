package club.tempvs.image.dao;

import club.tempvs.image.domain.Image;

import java.util.List;
import java.util.Map;

public interface ImageDao {

    List<Image> getAll(String belongsTo, String entityId);

    void save(String content, String fileName, Map metaDataMap);

    void delete(List<String> objectIds);

    void delete(String belongsTo, String entityId);
}
