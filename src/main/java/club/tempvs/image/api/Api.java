package club.tempvs.image.api;

import club.tempvs.image.auth.AuthenticationException;
import club.tempvs.image.json.ImagePayload;
import club.tempvs.image.ImageService;
import club.tempvs.image.ImageServiceFactory;
import club.tempvs.image.json.PayloadMalformedException;
import club.tempvs.image.json.StorePayload;

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

    @POST
    @Path("store")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response store(StorePayload payload) {
        try {
            ImagePayload responsePayload = imageService.store(payload, token);
            return Response.ok().entity(responsePayload).build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        } catch (PayloadMalformedException e) {
            return Response.status(400, e.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("delete")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response delete(ImagePayload payload) {
        try {
            imageService.delete(payload, token);
            return Response.ok().build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        } catch (PayloadMalformedException e) {
            return Response.status(400, e.getMessage()).build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
