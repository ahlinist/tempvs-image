package club.tempvs.image;

import club.tempvs.rest.auth.AuthenticationException;
import club.tempvs.rest.auth.TokenHelper;
import club.tempvs.image.model.ImagePayload;
import club.tempvs.image.model.Image;
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
    TokenHelper tokenHelper;
    @Mock
    private GridFSFactory gridFSFactory;
    @Mock
    private ImagePayload imagePayload;
    @Mock
    private Image image;

    @Before
    public void setup() {
        imageService = new ImageService(tokenHelper, gridFSFactory);
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

        when(gridFSFactory.getGridFS(COLLECTION)).thenReturn(gridFS);
        when(gridFS.findOne(objectId)).thenReturn(gridFSDBFile);

        imageService.delete(COLLECTION, HEX_ID, TOKEN);

        verify(gridFSFactory).getGridFS(COLLECTION);
        verify(gridFS).findOne(objectId);
        verify(gridFS).remove(gridFSDBFile);
        verify(tokenHelper).authenticate(TOKEN);
        verifyNoMoreInteractions(gridFSFactory);
        verifyNoMoreInteractions(gridFS);
        verifyNoMoreInteractions(gridFSDBFile);
        verifyNoMoreInteractions(tokenHelper);
    }

    @Test
    public void testDeleteMultiple() {
        ObjectId objectId = new ObjectId(HEX_ID);
        List<Image> images = new ArrayList<>();
        images.add(image);

        when(imagePayload.getImages()).thenReturn(images);
        when(image.getObjectId()).thenReturn(HEX_ID);
        when(image.getCollection()).thenReturn(COLLECTION);
        when(gridFSFactory.getGridFS(COLLECTION)).thenReturn(gridFS);
        when(gridFS.findOne(objectId)).thenReturn(gridFSDBFile);

        imageService.delete(imagePayload, TOKEN);

        verify(imagePayload).validate();
        verify(imagePayload).getImages();
        verify(image).getCollection();
        verify(image).getObjectId();
        verify(gridFSFactory).getGridFS(COLLECTION);
        verify(gridFS).findOne(objectId);
        verify(gridFS).remove(gridFSDBFile);
        verify(tokenHelper).authenticate(TOKEN);
        verifyNoMoreInteractions(gridFSFactory);
        verifyNoMoreInteractions(gridFS);
        verifyNoMoreInteractions(gridFSDBFile);
        verifyNoMoreInteractions(imagePayload);
        verifyNoMoreInteractions(image);
        verifyNoMoreInteractions(tokenHelper);
    }

    @Test(expected = AuthenticationException.class)
    public void testDeleteUnauthorized() {
        doThrow(new AuthenticationException()).when(tokenHelper).authenticate("wrong_token");
        imageService.delete(imagePayload, "wrong_token");

        verify(tokenHelper).authenticate("wrong_token");
        verifyNoMoreInteractions(gridFSFactory);
        verifyNoMoreInteractions(gridFS);
        verifyNoMoreInteractions(gridFSDBFile);
        verifyNoMoreInteractions(imagePayload);
        verifyNoMoreInteractions(tokenHelper);
    }

    @Test(expected = NullPointerException.class)
    public void testDeletePayloadNull() {
        imageService.delete(null, TOKEN);

        verify(tokenHelper).authenticate(TOKEN);
        verifyNoMoreInteractions(gridFSFactory);
        verifyNoMoreInteractions(gridFS);
        verifyNoMoreInteractions(gridFSDBFile);
        verifyNoMoreInteractions(imagePayload);
        verifyNoMoreInteractions(image);
        verifyNoMoreInteractions(tokenHelper);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteWithInsufficientParams() {
        imageService.delete(null, null, TOKEN);

        verifyNoMoreInteractions(gridFSFactory);
        verifyNoMoreInteractions(gridFS);
        verifyNoMoreInteractions(gridFSDBFile);
        verifyNoMoreInteractions(imagePayload);
        verifyNoMoreInteractions(image);
    }
}
