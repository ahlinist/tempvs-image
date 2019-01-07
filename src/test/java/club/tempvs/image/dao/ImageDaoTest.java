package club.tempvs.image.dao;

import club.tempvs.image.domain.Image;
import club.tempvs.image.dao.impl.GridFsImageDaoImpl;
import club.tempvs.image.util.ObjectFactory;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageDaoTest {

    private ImageDao imageDao;

    @Mock
    private ObjectFactory objectFactory;

    @Mock
    private GridFsTemplate gridFsTemplate;

    @Mock
    private GridFsResource gridFsResource;

    @Mock
    private GridFSFile gridFSFile;

    @Mock
    private Image image, resultImage;

    @Mock
    private ObjectId bsonObjectId;

    @Mock
    private Query query;

    @Before
    public void setup() {
        imageDao = new GridFsImageDaoImpl(objectFactory, gridFsTemplate);
    }

    @Test
    public void testGet() throws IOException {
        String id = "id";
        byte[] data = "data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);

        when(objectFactory.getInstance(Query.class, Criteria.where("_id").is(id))).thenReturn(query);
        when(gridFsTemplate.findOne(query)).thenReturn(gridFSFile);
        when(gridFsTemplate.getResource(gridFSFile)).thenReturn(gridFsResource);
        when(gridFsResource.getInputStream()).thenReturn(inputStream);

        byte[] result = imageDao.get(id);

        verify(objectFactory).getInstance(Query.class, Criteria.where("_id").is(id));
        verify(gridFsTemplate).findOne(query);
        verify(gridFsTemplate).getResource(gridFSFile);
        verify(gridFsResource).getInputStream();
        verifyNoMoreInteractions(image, resultImage, objectFactory, gridFsTemplate, bsonObjectId, query);

        assertTrue("The expected byte array is returned", Arrays.equals(data, result));
    }

    @Test
    public void testGetForNoResultFound() throws IOException {
        String id = "id";

        when(objectFactory.getInstance(Query.class, Criteria.where("_id").is(id))).thenReturn(query);
        when(gridFsTemplate.findOne(query)).thenReturn(null);

        byte[] result = imageDao.get(id);

        verify(objectFactory).getInstance(Query.class, Criteria.where("_id").is(id));
        verify(gridFsTemplate).findOne(query);
        verifyNoMoreInteractions(image, resultImage, objectFactory, gridFsTemplate, bsonObjectId, query);

        assertNull("The expected byte array is returned", result);
    }

    @Test
    public void testSave() {
        String content = "content";
        String imageInfo = "imageInfo";
        String fileName = "fileName";

        when(image.getContent()).thenReturn(content);
        when(image.getImageInfo()).thenReturn(imageInfo);
        when(image.getFileName()).thenReturn(fileName);
        when(gridFsTemplate.store(isA(InputStream.class), eq(fileName))).thenReturn(bsonObjectId);
        when(objectFactory.getInstance(eq(Image.class), anyString(), eq(imageInfo), eq(fileName))).thenReturn(resultImage);

        Image result = imageDao.save(image);

        verify(image).getContent();
        verify(image).getImageInfo();
        verify(image).getFileName();
        verify(gridFsTemplate).store(isA(InputStream.class), eq(fileName));
        verify(objectFactory).getInstance(eq(Image.class), anyString(), eq(imageInfo), eq(fileName));
        verifyNoMoreInteractions(image, resultImage, objectFactory, gridFsTemplate, bsonObjectId, query);

        assertEquals("Image is returned as a result", resultImage, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveForEmptyContent() {
        String content = "";
        String imageInfo = "imageInfo";
        String fileName = "fileName";

        when(image.getContent()).thenReturn(content);
        when(image.getImageInfo()).thenReturn(imageInfo);
        when(image.getFileName()).thenReturn(fileName);

        imageDao.save(image);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveForEmptyFileName() {
        String content = "content";
        String imageInfo = "imageInfo";
        String fileName = "";

        when(image.getContent()).thenReturn(content);
        when(image.getImageInfo()).thenReturn(imageInfo);
        when(image.getFileName()).thenReturn(fileName);

        imageDao.save(image);
    }

    @Test
    public void testDeleteById() {
        String id = "id";

        when(objectFactory.getInstance(Query.class, Criteria.where("_id").is(id))).thenReturn(query);

        imageDao.delete(id);

        verify(objectFactory).getInstance(Query.class, Criteria.where("_id").is(id));
        verify(gridFsTemplate).delete(query);
        verifyNoMoreInteractions(image, resultImage, objectFactory, gridFsTemplate, bsonObjectId, query);
    }

    @Test
    public void testDeleteByImage() {
        String id = "id";

        when(image.getObjectId()).thenReturn(id);
        when(objectFactory.getInstance(Query.class, Criteria.where("_id").is(id))).thenReturn(query);

        imageDao.delete(image);

        verify(image).getObjectId();
        verify(objectFactory).getInstance(Query.class, Criteria.where("_id").is(id));
        verify(gridFsTemplate).delete(query);
        verifyNoMoreInteractions(image, resultImage, objectFactory, gridFsTemplate, bsonObjectId, query);
    }
}
