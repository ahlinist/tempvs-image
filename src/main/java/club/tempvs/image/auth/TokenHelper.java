package club.tempvs.image.auth;

import java.math.BigInteger;
import java.security.MessageDigest;

public class TokenHelper {

    private String tokenHash;

    public TokenHelper() {
        this.tokenHash = generateHash(System.getenv("TOKEN"));
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public void setTokenHash(String tokenHash) {
        this.tokenHash = tokenHash;
    }

    private String generateHash(String token) {
        if (token == null) {
            return null;
        }

        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] tokenBytes = token.getBytes("UTF-8");
            byte[] digest = messageDigest.digest(tokenBytes);
            BigInteger number = new BigInteger(1, digest);
            return number.toString(16);
        } catch (Exception e) {
            return null;
        }
    }
}
