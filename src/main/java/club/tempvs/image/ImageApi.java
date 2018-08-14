package club.tempvs.image;

import club.tempvs.rest.auth.AuthenticationException;
import club.tempvs.image.model.ImagePayload;
import club.tempvs.rest.model.PayloadMalformedException;
import club.tempvs.image.model.StorePayload;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.InputStream;

@Path("api")
public class ImageApi {

    private static ImageService imageService = ImageServiceFactory.getInstance();

    @HeaderParam("token")
    private String token;

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

    @POST
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
            e.printStackTrace();
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("delete/{collection}/{id}")
    public Response delete(@PathParam("collection") String collection, @PathParam("id") String id) {
        try {
            imageService.delete(collection, id, token);
            return Response.ok().build();
        } catch (AuthenticationException e) {
            return Response.status(401).build();
        } catch (PayloadMalformedException e) {
            return Response.status(400, e.getMessage()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
