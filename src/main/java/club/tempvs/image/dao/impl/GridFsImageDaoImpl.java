package club.tempvs.image.dao.impl;

import club.tempvs.image.domain.Image;
import club.tempvs.image.dao.ImageDao;
import club.tempvs.image.util.ObjectFactory;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GridFsImageDaoImpl implements ImageDao {

    private static final String ID = "_id";
    private static final Base64.Decoder BASE_64_DECODER = Base64.getDecoder();

    private final ObjectFactory objectFactory;
    private final GridFsTemplate gridFsTemplate;

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
    })
    public byte[] get(String id) {
        Query query = buildIdLookupQuery(id);
        GridFSFile gridFSFile = gridFsTemplate.findOne(query);

        if (gridFSFile == null) {
            return null;
        }

        GridFsResource gridFsResource = gridFsTemplate.getResource(gridFSFile);

        try(InputStream inputStream = gridFsResource.getInputStream()) {
            return IOUtils.toByteArray(inputStream);
        } catch (Exception e) {
            return null;
        }
    }

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
    })
    public Image save(Image image) {
        Optional<String> imageInfo = Optional.ofNullable(image.getImageInfo());
        String content = image.getContent();
        String fileName = image.getFileName();

        if (content == null || content.isEmpty() || fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("Some of the required parameters: content or filename are missing");
        }

        byte[] bytes = BASE_64_DECODER.decode(content);

        try(InputStream inputStream = new ByteArrayInputStream(bytes)) {
            ObjectId bsonObjectId = gridFsTemplate.store(inputStream, fileName);
            String objectId = bsonObjectId.toString();
            return objectFactory.getInstance(Image.class, objectId, imageInfo.orElse(""), fileName);
        } catch (IOException e) {
            return null;
        }
    }

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
    })
    public void delete(String id) {
        Query query = buildIdLookupQuery(id);
        gridFsTemplate.delete(query);
    }

    @HystrixCommand(commandProperties = {
            @HystrixProperty(name = "execution.isolation.strategy", value = "SEMAPHORE")
    })
    public void delete(Image image) {
        delete(image.getObjectId());
    }

    private Query buildIdLookupQuery(String id) {
        return objectFactory.getInstance(Query.class, Criteria.where(ID).is(id));
    }
}
