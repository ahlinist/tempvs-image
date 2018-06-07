package club.tempvs.image;

import club.tempvs.image.mongodb.GridFSFactory;

import java.math.BigInteger;
import java.security.MessageDigest;

public class ImageServiceFactory {

    private static final String TOKEN = System.getenv("TOKEN");
    private static final String TOKEN_HASH = createTokenHash();

    private static ImageService imageService;

    public static ImageService getInstance() {
        if (imageService == null) {
            imageService = new ImageService(TOKEN_HASH, new GridFSFactory());
        }

        return imageService;
    }

    public static String createTokenHash() {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] tokenBytes = TOKEN.getBytes("UTF-8");
            byte[] digest = messageDigest.digest(tokenBytes);
            BigInteger number = new BigInteger(1, digest);
            return number.toString(16);
        } catch (Exception e) {
            return null;
        }
    }
}
