package club.tempvs.image.controller;

import club.tempvs.image.domain.Image;
import club.tempvs.image.service.ImageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ImageControllerTest {

    @InjectMocks
    private ImageController imageController;

    @Mock
    private ImageService imageService;
    @Mock
    private Image image;

    @Test
    public void testGetImages() {
        String belongsTo = "item";
        String entityId = "1";
        List<Image> images = Arrays.asList(image, image);

        when(imageService.getImages(belongsTo, entityId)).thenReturn(images);

        List<Image> result = imageController.getImages(belongsTo, entityId);

        verify(imageService).getImages(belongsTo, entityId);
        verifyNoMoreInteractions(imageService);

        assertEquals("Image list is returned", images, result);
    }
}
