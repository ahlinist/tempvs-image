package club.tempvs.image.api;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

@Path("api")
public class Api {

    @GET
    @Path("ping")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPong() {
        return "pong!";
    }
}
