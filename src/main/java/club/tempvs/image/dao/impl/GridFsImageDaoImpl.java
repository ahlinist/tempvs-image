package club.tempvs.image.dao.impl;

import static java.util.Collections.emptyList;
import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

import club.tempvs.image.dao.ImageDao;
import club.tempvs.image.domain.Image;
import club.tempvs.image.util.MongoHelper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Component
@RequiredArgsConstructor
public class GridFsImageDaoImpl implements ImageDao {

    private static final Base64.Decoder BASE_64_DECODER = Base64.getDecoder();
    private static final Base64.Encoder BASE_64_ENCODER = Base64.getEncoder();

    private static final String IMAGE_INFO = "imageInfo";
    private static final String BELONGS_TO = "belongsTo";
    private static final String ENTITY_ID = "entityId";

    private final GridFsTemplate gridFsTemplate;
    private final MongoHelper mongoHelper;

    @Override
    public List<Image> getAll(String belongsTo, String entityId) {
        Query query = mongoHelper.buildBulkQuery(belongsTo, entityId);
        GridFSFindIterable gridFSFindIterable = gridFsTemplate.find(query);

        if (isNull(gridFSFindIterable)) {
            return emptyList();
        }

        return mongoHelper.collectGridFSFiles(gridFSFindIterable).stream()
                .map(this::fetchImageFromGridFSFile)
                .filter(Objects::nonNull)
                .collect(toList());
    }

    @Override
    public void save(String content, String fileName, Map metaDataMap) {
        DBObject metaData = new BasicDBObject(metaDataMap);
        byte[] bytes = BASE_64_DECODER.decode(content);

        try(InputStream inputStream = new ByteArrayInputStream(bytes)) {
            gridFsTemplate.store(inputStream, fileName, metaData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(List<String> objectIds) {
        Query query = mongoHelper.buildIdQuery(objectIds);
        gridFsTemplate.delete(query);
    }

    @Override
    public void delete(String belongsTo, String entityId) {
        Query query = mongoHelper.buildBulkQuery(belongsTo, entityId);
        gridFsTemplate.delete(query);
    }

    //TODO: to be moved to a separate helper class
    private Image fetchImageFromGridFSFile(GridFSFile gridFSFile) {
        GridFsResource gridFsResource = gridFsTemplate.getResource(gridFSFile);
        String objectId = gridFSFile.getObjectId().toHexString();
        Document metaData = gridFSFile.getMetadata();
        String imageInfo = metaData.getString(IMAGE_INFO);
        String entityId = metaData.getString(ENTITY_ID);
        String belongsTo = metaData.getString(BELONGS_TO);
        String fileName = gridFSFile.getFilename();

        try(InputStream inputStream = gridFsResource.getInputStream()) {
            byte[] imageBytes = IOUtils.toByteArray(inputStream);
            String content = BASE_64_ENCODER.encodeToString(imageBytes);
            return new Image(objectId, imageInfo, entityId, belongsTo, content, fileName);
        } catch (IOException e) {
            return null;
        }
    }
}
