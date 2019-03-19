package club.tempvs.image.controller;

import club.tempvs.image.domain.Image;
import club.tempvs.image.service.ImageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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
    private Image image, resultImage;

    @Before
    public void setup() {
        imageController = new ImageController(imageService);
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

        Image result = imageController.store(image);

        verify(imageService).store(image);
        verifyNoMoreInteractions(image, resultImage, imageService);

        assertEquals("ResponseEntity with image list is returned", resultImage, result);
    }

    @Test
    public void testDelete() {
        String id = "id";

        imageController.delete(id);

        verify(imageService).delete(id);
        verifyNoMoreInteractions(imageService);
    }

    @Test
    public void testBulkDelete() {
        List<String> objectIds = Arrays.asList("id1", "id2");

        imageController.bulkDelete(objectIds);

        verify(imageService).delete(objectIds);
        verifyNoMoreInteractions(imageService);
    }
}
