package club.tempvs.image.api;

import club.tempvs.image.ImageService;
import club.tempvs.image.ImageServiceFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;

@Path("api")
public class Api {

    private static ImageService imageService = ImageServiceFactory.getInstance();

    @HeaderParam("token")
    private String token;

    @GET
    @Path("ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPong() {
        return "pong!";
    }

    @GET
    @Path("get")
    @Produces("image/jpg")
    public Response get(@QueryParam("id") String id, @QueryParam("collection") String collection) {
        InputStream inputStream;

        try {
            inputStream = imageService.getImageStream(id, collection);
        } catch (Exception e) {
            inputStream = imageService.getDefaultImage();
        }

        return Response.ok(inputStream).build();
    }
}
