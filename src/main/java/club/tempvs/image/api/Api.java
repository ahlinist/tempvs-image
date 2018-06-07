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
        try {
            InputStream inputStream = imageService.getImageStream(id, collection);
            return Response.ok(inputStream).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }
}
