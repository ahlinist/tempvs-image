package club.tempvs.image.json;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class StorePayloadTest {

    private static final String CONTENT = "content";
    private static final String COLLECTION = "collection";

    private StorePayload storePayload;

    @Mock
    private ImageSketch imageSketch;

    @Before
    public void setup() {
        storePayload = new StorePayload();
    }

    @Test
    public void testValidate() {
        List<ImageSketch> imageSketches = new ArrayList<>();
        imageSketches.add(imageSketch);
        storePayload.setImages(imageSketches);

        when(imageSketch.getContent()).thenReturn(CONTENT);
        when(imageSketch.getCollection()).thenReturn(COLLECTION);

        storePayload.validate();

        verify(imageSketch).getContent();
        verify(imageSketch).getCollection();
        verifyNoMoreInteractions(imageSketch);
    }

    @Test(expected = PayloadMalformedException.class)
    public void testValidateForEmptySketches() {
        storePayload.validate();

        verifyNoMoreInteractions(imageSketch);
    }

    @Test(expected = PayloadMalformedException.class)
    public void testValidateForMissingCollection() {
        List<ImageSketch> imageSketches = new ArrayList<>();
        imageSketches.add(imageSketch);
        storePayload.setImages(imageSketches);

        when(imageSketch.getContent()).thenReturn(CONTENT);
        when(imageSketch.getCollection()).thenReturn(null);

        storePayload.validate();

        verify(imageSketch).getContent();
        verify(imageSketch).getCollection();
        verifyNoMoreInteractions(imageSketch);
    }

    @Test(expected = PayloadMalformedException.class)
    public void testValidateForMissingContent() {
        List<ImageSketch> imageSketches = new ArrayList<>();
        imageSketches.add(imageSketch);
        storePayload.setImages(imageSketches);

        when(imageSketch.getContent()).thenReturn(null);
        when(imageSketch.getCollection()).thenReturn(COLLECTION);

        storePayload.validate();

        verify(imageSketch).getContent();
        verify(imageSketch).getCollection();
        verifyNoMoreInteractions(imageSketch);
    }
}
