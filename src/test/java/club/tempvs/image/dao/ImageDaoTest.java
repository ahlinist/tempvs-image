package club.tempvs.image.dao;

import club.tempvs.image.domain.Image;
import club.tempvs.image.dao.impl.GridFsImageDaoImpl;
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
    private GridFsTemplate gridFsTemplate;
    @Mock
    private GridFsResource gridFsResource;
    @Mock
    private GridFSFile gridFSFile;
    @Mock
    private Image image;
    @Mock
    private ObjectId bsonObjectId;

    @Before
    public void setup() {
        imageDao = new GridFsImageDaoImpl(gridFsTemplate);
    }

    @Test
    public void testGet() throws IOException {
        String id = "id";
        byte[] data = "data".getBytes();
        InputStream inputStream = new ByteArrayInputStream(data);
        Query query = new Query(Criteria.where("_id").is(id));

        when(gridFsTemplate.findOne(query)).thenReturn(gridFSFile);
        when(gridFsTemplate.getResource(gridFSFile)).thenReturn(gridFsResource);
        when(gridFsResource.getInputStream()).thenReturn(inputStream);

        byte[] result = imageDao.get(id);

        verify(gridFsTemplate).findOne(query);
        verify(gridFsTemplate).getResource(gridFSFile);
        verify(gridFsResource).getInputStream();
        verifyNoMoreInteractions(image, gridFsTemplate, bsonObjectId);

        assertTrue("The expected byte array is returned", Arrays.equals(data, result));
    }

    @Test
    public void testGetForNoResultFound() {
        String id = "id";
        Query query = new Query(Criteria.where("_id").is(id));

        when(gridFsTemplate.findOne(query)).thenReturn(null);

        byte[] result = imageDao.get(id);

        verify(gridFsTemplate).findOne(query);
        verifyNoMoreInteractions(image, gridFsTemplate, bsonObjectId);

        assertNotNull("The result is not null", result);
    }

    @Test
    public void testSave() {
        String content = "content";
        String fileName = "fileName";
        Object metaData = new Object();

        when(gridFsTemplate.store(isA(InputStream.class), eq(fileName), eq(metaData))).thenReturn(bsonObjectId);

        imageDao.save(content, fileName, metaData);

        verify(gridFsTemplate).store(isA(InputStream.class), eq(fileName), eq(metaData));
        verifyNoMoreInteractions(gridFsTemplate, bsonObjectId);
    }

    @Test
    public void testDeleteById() {
        String id = "id";
        Query query = new Query(Criteria.where("_id").is(id));

        imageDao.delete(id);

        verify(gridFsTemplate).delete(query);
        verifyNoMoreInteractions(image, gridFsTemplate, bsonObjectId);
    }
}
