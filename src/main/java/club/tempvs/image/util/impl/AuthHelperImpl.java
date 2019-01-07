package club.tempvs.image.util.impl;

import club.tempvs.image.api.UnauthorizedException;
import club.tempvs.image.util.AuthHelper;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

@Component
public class AuthHelperImpl implements AuthHelper {

    private static final String CHAR_ENCODING = "UTF-8";
    private static final String TOKEN = System.getenv("TOKEN");

    public void authenticate(String receivedToken) {
        byte[] tokenBytes;

        try {
            tokenBytes = TOKEN.getBytes(CHAR_ENCODING);
        } catch (Exception e) {
            tokenBytes = new byte[]{};
        }

        String tokenHash = DigestUtils.md5DigestAsHex(tokenBytes);

        if (receivedToken == null || receivedToken.isEmpty() || !receivedToken.equals(tokenHash)) {
            throw new UnauthorizedException("Authentication failed. Wrong token is received.");
        }
    }
}