package club.tempvs.image.json;

import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class DeletePayloadTest {

    private static final String HEX_ID = new ObjectId().toString();
    private static final String COLLECTION = "collection";

    @Mock
    private Image image;

    private DeletePayload deletePayload;

    @Before
    public void setup() {
        deletePayload = new DeletePayload();
    }

    @Test
    public void testValidate() {
        List<Image> images = new ArrayList<>();
        images.add(image);
        deletePayload.setImages(images);

        when(image.getObjectId()).thenReturn(HEX_ID);
        when(image.getCollection()).thenReturn(COLLECTION);

        deletePayload.validate();

        verify(image).getObjectId();
        verify(image).getCollection();
        verifyNoMoreInteractions(image);
    }

    @Test(expected = PayloadMalformedException.class)
    public void testValidateForNoImages() {
        deletePayload.validate();

        verifyNoMoreInteractions(image);
    }

    @Test(expected = PayloadMalformedException.class)
    public void testValidateForInvalidImages() {
        List<Image> images = new ArrayList<>();
        images.add(image);
        deletePayload.setImages(images);

        when(image.getObjectId()).thenReturn(null);
        when(image.getCollection()).thenReturn(COLLECTION);

        deletePayload.validate();

        verify(image).getObjectId();
        verify(image).getCollection();
        verifyNoMoreInteractions(image);
    }
}
