package ntou.notesharedevbackend.notificationModule.config;

import com.sun.security.auth.UserPrincipal;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.*;
import lombok.*;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.*;
import java.util.*;


public class UserHandshake extends DefaultHandshakeHandler {
    private final String WEBSOCKET_KEY = "NoteShareBackendAndItsShouldHaveVeryLongString";

    @Override
    protected Principal determineUser(@NonNull ServerHttpRequest request,@NonNull WebSocketHandler wsHandler, @NonNull Map<String, Object> attributes) {
        String cookies = request.getHeaders().get("cookie").get(0);
        String[] cookieList = cookies.split(";");
        int tokenIndex = 0;
        for (int i = 0; i < cookieList.length; i++) {
            if (cookieList[i].contains("token"))
                tokenIndex = i;
        }
        String token = cookieList[tokenIndex].split("=")[1];

        Key secretKey = Keys.hmacShaKeyFor(WEBSOCKET_KEY.getBytes());
        JwtParser parser = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build();
        Claims claims = parser
                .parseClaimsJws(token)
                .getBody();

        return new UserPrincipal(claims.get("email").toString());
    }
}
