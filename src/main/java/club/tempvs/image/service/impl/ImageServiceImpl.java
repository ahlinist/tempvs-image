package club.tempvs.image.service.impl;

import club.tempvs.image.domain.Image;
import club.tempvs.image.service.ImageService;
import club.tempvs.image.dao.ImageDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.Objects.isNull;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {

    private static final String IMAGE_INFO = "imageInfo";
    private static final String ENTITY_ID = "entityId";
    private static final String BELONGS_TO = "belongsTo";

    private final ImageDao imageDao;

    @Override
    public byte[] getImage(String id) {
        return imageDao.get(id);
    }

    @Override
    public void store(Image image) {
        String content = image.getContent();
        String fileName = image.getFileName();

        if (isNull(content) || content.isEmpty() || isNull(fileName) || fileName.isEmpty()) {
            throw new IllegalArgumentException("Some of the required parameters: content or filename are missing");
        }

        DBObject metaData = new BasicDBObject();
        metaData.put(IMAGE_INFO, image.getImageInfo());
        metaData.put(ENTITY_ID, image.getEntityId());
        metaData.put(BELONGS_TO, image.getBelongsTo());

        imageDao.save(content, fileName, metaData);
    }

    @Override
    public void delete(List<String> objectIds) {
        objectIds.stream().forEach(imageDao::delete);
    }
}
