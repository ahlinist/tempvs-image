package club.tempvs.image.service;

import club.tempvs.image.domain.Image;
import club.tempvs.image.service.impl.ImageServiceImpl;
import club.tempvs.image.dao.ImageDao;
import club.tempvs.image.util.ObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTest {

    private ImageService imageService;

    @Mock
    private ObjectFactory objectFactory;

    @Mock
    private ImageDao imageDao;

    @Mock
    private Image image, resultImage;

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
        verifyNoMoreInteractions(objectFactory);

        assertEquals("The expected byte array is returned", resultArray, result);
    }

    @Test
    public void testGetImageIfNotFound() {
        String id = "id";
        byte[] resultArray = null;

        when(imageDao.get(id)).thenReturn(resultArray);

        byte[] result = imageService.getImage(id);

        verify(imageDao).get(id);
        verifyNoMoreInteractions(objectFactory);

        assertNotEquals("The empty byte array is NOT returned", resultArray, result);
        assertTrue(result instanceof byte[]);
    }

    @Test
    public void testSave() {
        List<Image> images = Arrays.asList(image, image);
        List<Image> resultImages = Arrays.asList(resultImage, resultImage);

        when(imageDao.save(image)).thenReturn(resultImage);

        List<Image> result = imageService.storeImages(images);

        verify(imageDao, times(2)).save(image);
        verifyNoMoreInteractions(imageDao, image, resultImage);

        assertEquals("A list of images is returned", resultImages, result);
    }

    @Test
    public void testSaveFailed() {
        List<Image> images = Arrays.asList(image, image);
        List<Image> resultImages = new ArrayList<>();

        when(imageDao.save(image)).thenReturn(null);

        List<Image> result = imageService.storeImages(images);

        verify(imageDao, times(2)).save(image);
        verifyNoMoreInteractions(imageDao, image, resultImage);

        assertEquals("A list of images is returned", resultImages, result);
    }

    @Test
    public void testDeleteImage() {
        String id = "id";

        imageService.deleteImage(id);

        verify(imageDao).delete(id);
        verifyNoMoreInteractions(imageDao, image, resultImage);
    }

    @Test
    public void testDeleteImages() {
        List<Image> images = Arrays.asList(image, image);

        imageService.deleteImages(images);

        verify(imageDao, times(2)).delete(image);
        verifyNoMoreInteractions(imageDao, image, resultImage);
    }
}
