package club.tempvs.image.dao;

import static java.util.Collections.emptyList;

import club.tempvs.image.domain.Image;
import club.tempvs.image.dao.impl.GridFsImageDaoImpl;
import club.tempvs.image.util.MongoHelper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.gridfs.GridFSFindIterable;
import com.mongodb.client.gridfs.model.GridFSFile;
import org.apache.commons.io.IOUtils;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageDaoTest {

    @InjectMocks
    private GridFsImageDaoImpl imageDao;

    @Mock
    private GridFsTemplate gridFsTemplate;
    @Mock
    private MongoHelper mongoHelper;

    @Mock
    private GridFsResource gridFsResource;
    @Mock
    private GridFSFindIterable gridFSFindIterable;
    @Mock
    private GridFSFile gridFSFile;
    @Mock
    private ObjectId bsonObjectId;
    @Mock
    private Query query;

    @Test
    public void testGetAll() throws IOException {
        String belongsTo = "belongsTo";
        String entityId = "1";
        List<GridFSFile> gridFSFiles = Arrays.asList(gridFSFile, gridFSFile);
        InputStream inputStream = IOUtils.toInputStream("some test data for my input stream", "UTF-8");
        Document document = new Document();

        when(mongoHelper.buildBulkQuery(belongsTo, entityId)).thenReturn(query);
        when(gridFsTemplate.find(query)).thenReturn(gridFSFindIterable);
        when(mongoHelper.collectGridFSFiles(gridFSFindIterable)).thenReturn(gridFSFiles);
        when(gridFsTemplate.getResource(gridFSFile)).thenReturn(gridFsResource);
        when(gridFSFile.getObjectId()).thenReturn(bsonObjectId);
        when(gridFSFile.getMetadata()).thenReturn(document);
        when(gridFsResource.getInputStream()).thenReturn(inputStream);

        List<Image> result = imageDao.getAll(belongsTo, entityId);

        verify(mongoHelper).buildBulkQuery(belongsTo, entityId);
        verify(gridFsTemplate).find(query);
        verify(mongoHelper).collectGridFSFiles(gridFSFindIterable);
        verify(gridFsTemplate, times(2)).getResource(gridFSFile);
        verifyNoMoreInteractions(mongoHelper, gridFsTemplate);

        assertTrue("A list is returned", result instanceof List);
        assertEquals("A list of 2 items is returned", 2, result.size());
        assertTrue("A list Images is returned", result.iterator().next() instanceof Image);
    }

    @Test
    public void testGetAllForNoResult() {
        String belongsTo = "belongsTo";
        String entityId = "1";

        when(mongoHelper.buildBulkQuery(belongsTo, entityId)).thenReturn(query);
        when(gridFsTemplate.find(query)).thenReturn(null);

        List<Image> result = imageDao.getAll(belongsTo, entityId);

        verify(mongoHelper).buildBulkQuery(belongsTo, entityId);
        verify(gridFsTemplate).find(any(Query.class));
        verifyNoMoreInteractions(mongoHelper, gridFsTemplate);

        assertEquals("Empty list is returned", emptyList(), result);
    }

    @Test
    public void testSave() {
        String content = "content";
        String fileName = "fileName";
        Map<String, String> metaDataMap = new HashMap<>();
        DBObject metaData = new BasicDBObject(metaDataMap);

        when(gridFsTemplate.store(isA(InputStream.class), eq(fileName), eq(metaData))).thenReturn(bsonObjectId);

        imageDao.save(content, fileName, metaDataMap);

        verify(gridFsTemplate).store(isA(InputStream.class), eq(fileName), eq(metaData));
        verifyNoMoreInteractions(gridFsTemplate, bsonObjectId);
    }

    @Test
    public void testDeleteByIds() {
        List<String> objectIds = Arrays.asList("id");

        when(mongoHelper.buildIdQuery(objectIds)).thenReturn(query);

        imageDao.delete(objectIds);

        verify(mongoHelper).buildIdQuery(objectIds);
        verify(gridFsTemplate).delete(query);
        verifyNoMoreInteractions(mongoHelper, gridFsTemplate, bsonObjectId);
    }

    @Test
    public void testDeleteAllForItem() {
        String belongsTo = "belongsTo";
        String entityId = "entityId";

        when(mongoHelper.buildBulkQuery(belongsTo, entityId)).thenReturn(query);

        imageDao.delete(belongsTo, entityId);

        verify(mongoHelper).buildBulkQuery(belongsTo, entityId);
        verify(gridFsTemplate).delete(query);
        verifyNoMoreInteractions(mongoHelper, gridFsTemplate, bsonObjectId);
    }
}
