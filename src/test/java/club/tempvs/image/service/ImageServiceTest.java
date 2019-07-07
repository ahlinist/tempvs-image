package club.tempvs.image.service;

import club.tempvs.image.domain.Image;
import club.tempvs.image.service.impl.ImageServiceImpl;
import club.tempvs.image.dao.ImageDao;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTest {

    private ImageService imageService;

    @Mock
    private ImageDao imageDao;
    @Mock
    private Image image;


    @Before
    public void setup() {
        imageService = new ImageServiceImpl(imageDao);
    }

    @Test
    public void testGetImage() {
        String id = "id";
        byte[] resultArray = "data".getBytes();

        when(imageDao.get(id)).thenReturn(resultArray);

        byte[] result = imageService.getImage(id);

        verify(imageDao).get(id);
        verifyNoMoreInteractions(imageDao);

        assertEquals("The expected byte array is returned", resultArray, result);
    }

    @Test
    public void testSave() {
        String content = "content in base 64";
        String fileName = "test.jpg";
        String belongsTo = "item";
        String entityId = "1";
        String imageInfo = "info";
        DBObject metaData = new BasicDBObject();
        metaData.put("imageInfo", imageInfo);
        metaData.put("entityId", entityId);
        metaData.put("belongsTo", belongsTo);

        when(image.getContent()).thenReturn(content);
        when(image.getFileName()).thenReturn(fileName);
        when(image.getEntityId()).thenReturn(entityId);
        when(image.getBelongsTo()).thenReturn(belongsTo);
        when(image.getImageInfo()).thenReturn(imageInfo);

        imageService.store(image);

        verify(image).getContent();
        verify(image).getFileName();
        verify(image).getEntityId();
        verify(image).getBelongsTo();
        verify(image).getImageInfo();
        verify(imageDao).save(content, fileName, metaData);
        verifyNoMoreInteractions(imageDao, image);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveForEmptyContent() {
        String content = "";
        String fileName = "test.jpg";

        when(image.getContent()).thenReturn(content);
        when(image.getFileName()).thenReturn(fileName);

        imageService.store(image);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveForEmptyFileName() {
        String content = "content";
        String fileName = "";

        when(image.getContent()).thenReturn(content);
        when(image.getFileName()).thenReturn(fileName);

        imageService.store(image);
    }

    @Test
    public void testDeleteImages() {
        List<String> objectIds = Arrays.asList("id", "id");

        imageService.delete(objectIds);

        verify(imageDao, times(2)).delete("id");
        verifyNoMoreInteractions(imageDao, image);
    }
}
