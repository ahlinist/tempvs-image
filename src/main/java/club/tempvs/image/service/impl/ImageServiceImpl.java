package club.tempvs.image.service.impl;

import club.tempvs.image.domain.Image;
import club.tempvs.image.service.ImageService;
import club.tempvs.image.dao.ImageDao;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public List<Image> getImages(String belongsTo, String entityId) {
        return imageDao.getAll(belongsTo, entityId);
    }

    @Override
    public void store(Image image) {
        String content = image.getContent();
        String fileName = image.getFileName();

        if (isNull(content) || content.isEmpty() || isNull(fileName) || fileName.isEmpty()) {
            throw new IllegalArgumentException("Some of the required parameters: content or filename are missing");
        }

        Map<String, String> metaDataMap = ImmutableMap.of(
                IMAGE_INFO, image.getImageInfo(),
                ENTITY_ID, image.getEntityId(),
                BELONGS_TO, image.getBelongsTo()
        );

        imageDao.save(content, fileName, metaDataMap);
    }

    @Override
    public void delete(List<String> objectIds) {
        objectIds.stream().forEach(imageDao::delete);
    }
}
