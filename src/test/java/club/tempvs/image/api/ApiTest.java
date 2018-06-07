package club.tempvs.image.api;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.core.Application;

import static org.junit.Assert.*;

public class ApiTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig(Api.class);
    }

    @Test
    public void testPing() {
        String responseMsg = target().path("api/ping").request().get(String.class);

        assertEquals("pong!", responseMsg);
    }
}
