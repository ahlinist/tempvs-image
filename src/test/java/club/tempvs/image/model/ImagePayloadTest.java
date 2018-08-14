package club.tempvs.image.model;

import club.tempvs.image.model.Image;
import club.tempvs.image.model.ImagePayload;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import club.tempvs.rest.json.PayloadMalformedException;

@RunWith(MockitoJUnitRunner.class)
public class ImagePayloadTest {

    private static final String HEX_ID = new ObjectId().toString();
    private static final String COLLECTION = "collection";

    @Mock
    private Image image;

    private ImagePayload imagePayload;

    @Before
    public void setup() {
        imagePayload = new ImagePayload();
    }

    @Test
    public void testValidate() {
        List<Image> images = new ArrayList<>();
        images.add(image);
        imagePayload.setImages(images);

        when(image.getObjectId()).thenReturn(HEX_ID);
        when(image.getCollection()).thenReturn(COLLECTION);

        imagePayload.validate();

        verify(image).getObjectId();
        verify(image).getCollection();
        verifyNoMoreInteractions(image);
    }

    @Test(expected = PayloadMalformedException.class)
    public void testValidateForNoImages() {
        imagePayload.validate();

        verifyNoMoreInteractions(image);
    }

    @Test(expected = PayloadMalformedException.class)
    public void testValidateForInvalidImages() {
        List<Image> images = new ArrayList<>();
        images.add(image);
        imagePayload.setImages(images);

        when(image.getObjectId()).thenReturn(null);
        when(image.getCollection()).thenReturn(COLLECTION);

        imagePayload.validate();

        verify(image).getObjectId();
        verify(image).getCollection();
        verifyNoMoreInteractions(image);
    }
}
