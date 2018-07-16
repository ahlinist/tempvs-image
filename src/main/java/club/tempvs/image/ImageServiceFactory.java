package club.tempvs.image;

import club.tempvs.image.auth.TokenHelper;
import club.tempvs.image.mongodb.GridFSFactory;

public class ImageServiceFactory {

    private static ImageService imageService;

    public static ImageService getInstance() {
        if (imageService == null) {
            imageService = new ImageService(new TokenHelper(), new GridFSFactory());
        }

        return imageService;
    }
}
