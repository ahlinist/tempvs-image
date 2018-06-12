package club.tempvs.image;

import club.tempvs.image.auth.AuthenticationException;
import club.tempvs.image.json.DeletePayload;
import club.tempvs.image.json.Image;
import club.tempvs.image.json.PayloadMalformedException;
import club.tempvs.image.mongodb.GridFSFactory;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTest {

    private static final String HEX_ID = new ObjectId().toString();
    private static final String COLLECTION = "collection";
    private static final String TOKEN = "security_token";

    private ImageService imageService;

    @Mock
    GridFS gridFS;
    @Mock
    GridFSDBFile gridFSDBFile;
    @Mock
    private GridFSFactory gridFSFactory;
    @Mock
    private DeletePayload deletePayload;
    @Mock
    private Image image;

    @Before
    public void setup() {
        imageService = new ImageService("security_token", gridFSFactory);
    }

    @Test
    public void testGetImageStream() {
        ObjectId objectId = new ObjectId(HEX_ID);

        when(gridFSFactory.getGridFS(COLLECTION)).thenReturn(gridFS);
        when(gridFS.findOne(objectId)).thenReturn(gridFSDBFile);
        when(gridFSDBFile.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[]{}));

        InputStream result = imageService.getImageStream(HEX_ID, COLLECTION);

        verify(gridFSFactory).getGridFS(COLLECTION);
        verify(gridFS).findOne(objectId);
        verify(gridFSDBFile).getInputStream();
        verifyNoMoreInteractions(gridFSFactory);
        verifyNoMoreInteractions(gridFS);
        verifyNoMoreInteractions(gridFSDBFile);

        assertTrue(result instanceof InputStream);
    }

    @Test
    public void testGetImageStreamForMissingImage() {
        ObjectId objectId = new ObjectId(HEX_ID);

        when(gridFSFactory.getGridFS(COLLECTION)).thenReturn(gridFS);
        when(gridFS.findOne(objectId)).thenReturn(null);

        InputStream result = imageService.getImageStream(HEX_ID, COLLECTION);

        verify(gridFSFactory).getGridFS(COLLECTION);
        verify(gridFS).findOne(objectId);
        verifyNoMoreInteractions(gridFSFactory);
        verifyNoMoreInteractions(gridFS);
        verifyNoMoreInteractions(gridFSDBFile);

        assertTrue(result instanceof InputStream);
    }

    @Test
    public void testDelete() {
        ObjectId objectId = new ObjectId(HEX_ID);
        List<Image> images = new ArrayList<>();
        images.add(image);

        when(deletePayload.getImages()).thenReturn(images);
        when(image.getObjectId()).thenReturn(HEX_ID);
        when(image.getCollection()).thenReturn(COLLECTION);
        when(gridFSFactory.getGridFS(COLLECTION)).thenReturn(gridFS);
        when(gridFS.findOne(objectId)).thenReturn(gridFSDBFile);

        imageService.delete(deletePayload, TOKEN);

        verify(deletePayload).validate();
        verify(deletePayload).getImages();
        verify(image).getCollection();
        verify(image).getObjectId();
        verify(gridFSFactory).getGridFS(COLLECTION);
        verify(gridFS).findOne(objectId);
        verify(gridFS).remove(gridFSDBFile);
        verifyNoMoreInteractions(gridFSFactory);
        verifyNoMoreInteractions(gridFS);
        verifyNoMoreInteractions(gridFSDBFile);
        verifyNoMoreInteractions(deletePayload);
        verifyNoMoreInteractions(image);
    }

    @Test(expected = AuthenticationException.class)
    public void testDeleteUnauthorized() {
        imageService.delete(deletePayload, "wrong_token");

        verifyNoMoreInteractions(gridFSFactory);
        verifyNoMoreInteractions(gridFS);
        verifyNoMoreInteractions(gridFSDBFile);
        verifyNoMoreInteractions(deletePayload);
    }

    @Test(expected = NullPointerException.class)
    public void testDeletePayloadNull() {
        imageService.delete(null, TOKEN);

        verifyNoMoreInteractions(gridFSFactory);
        verifyNoMoreInteractions(gridFS);
        verifyNoMoreInteractions(gridFSDBFile);
        verifyNoMoreInteractions(deletePayload);
        verifyNoMoreInteractions(image);
    }
}
