package club.tempvs.image.controller;

import club.tempvs.image.domain.Image;
import club.tempvs.image.dto.ImagePayload;
import club.tempvs.image.service.ImageService;
import club.tempvs.image.util.ObjectFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageControllerTest {

    private ImageController imageController;

    @Mock
    private ImageService imageService;
    @Mock
    private ObjectFactory objectFactory;
    @Mock
    private ImagePayload imagePayload, resultImagePayload;
    @Mock
    private Image image, resultImage;

    @Before
    public void setup() {
        imageController = new ImageController(imageService, objectFactory);
    }

    @Test
    public void testGetImage() {
        String id = "id";
        byte[] image = "img".getBytes();

        when(imageService.getImage(id)).thenReturn(image);

        ResponseEntity result = imageController.getImage(id);

        verify(imageService).getImage(id);
        verifyNoMoreInteractions(imageService);

        assertEquals("ResponseEntity with OK status is returned", HttpStatus.OK, result.getStatusCode());
        assertEquals("The 'img' byteArrays is returned as a body", image, result.getBody());
    }

    @Test
    public void testStore() {
        when(imageService.store(image)).thenReturn(resultImage);

        ResponseEntity result = imageController.store(image);

        verify(imageService).store(image);
        verifyNoMoreInteractions(image, resultImage, imageService);

        assertEquals("ResponseEntity with status 200 is returned", HttpStatus.OK, result.getStatusCode());
        assertEquals("ResponseEntity with image list is returned", resultImage, result.getBody());
    }

    @Test
    public void testDelete() {
        String id = "id";

        ResponseEntity result = imageController.delete(id);

        verify(imageService).deleteImage(id);
        verifyNoMoreInteractions(image, resultImage, imagePayload, objectFactory, imageService);

        assertEquals("ResponseEntity with status 200 is returned", HttpStatus.OK, result.getStatusCode());
        assertEquals("ResponseEntity with image list is returned", null, result.getBody());
    }

    @Test
    public void testBulkDelete() {
        List<Image> images = Arrays.asList(image, image);

        when(imagePayload.getImages()).thenReturn(images);

        ResponseEntity result = imageController.bulkDelete(imagePayload);

        verify(imagePayload).getImages();
        verify(imageService).deleteImages(images);
        verifyNoMoreInteractions(image, resultImage, imagePayload, objectFactory, imageService);

        assertEquals("ResponseEntity with status 200 is returned", HttpStatus.OK, result.getStatusCode());
        assertEquals("ResponseEntity with image list is returned", null, result.getBody());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBulkDeleteForEmptyPayload() {
        List<Image> images = new ArrayList<>();

        when(imagePayload.getImages()).thenReturn(images);

        imageController.bulkDelete(imagePayload);
    }
}
