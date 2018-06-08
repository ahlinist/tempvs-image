package club.tempvs.image;

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
import java.io.IOException;
import java.io.InputStream;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTest {

    private static final String DEFAULT_IMAGE_NAME = "default_image.gif";
    private static final String HEX_ID = new ObjectId().toString();
    private static final String COLLECTION = "collection";

    private ImageService imageService;

    @Mock
    GridFS gridFS;
    @Mock
    GridFSDBFile gridFSDBFile;
    @Mock
    private GridFSFactory gridFSFactory;

    @Before
    public void setup() {
        imageService = new ImageService("token", gridFSFactory);
    }

    @Test
    public void testGetImageStream() throws IOException {
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
}
