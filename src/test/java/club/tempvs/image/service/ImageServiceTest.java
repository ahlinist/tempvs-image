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
    public void testSave() {
        when(imageDao.save(image)).thenReturn(resultImage);

        Image result = imageService.store(image);

        verify(imageDao).save(image);
        verifyNoMoreInteractions(imageDao, image, resultImage);

        assertEquals("An updated image is returned", resultImage, result);
        assertNotEquals("The result image is not the same object as an original one", resultImage, image);
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
