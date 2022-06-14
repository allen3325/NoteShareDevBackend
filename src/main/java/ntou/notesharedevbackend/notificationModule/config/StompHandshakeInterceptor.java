package ntou.notesharedevbackend.notificationModule.config;

import lombok.*;
import lombok.extern.slf4j.*;
import org.keycloak.adapters.*;
import org.keycloak.adapters.rotation.*;
import org.keycloak.common.*;
import org.springframework.http.*;
import org.springframework.http.server.*;
import org.springframework.lang.*;
import org.springframework.web.socket.*;
import org.springframework.web.socket.server.*;
import org.keycloak.adapters.springboot.KeycloakSpringBootProperties;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class StompHandshakeInterceptor implements HandshakeInterceptor {

    private final KeycloakSpringBootProperties configuration;

    @Override
    public boolean beforeHandshake(ServerHttpRequest req, ServerHttpResponse resp, WebSocketHandler h, Map<String, Object> atts) {
        List<String> protocols = req.getHeaders().get("Sec-WebSocket-Protocol");
        try {
            String token = protocols.get(0).split(", ")[2];
            log.debug("Token: " + token);
            AdapterTokenVerifier.verifyToken(token, KeycloakDeploymentBuilder.build(configuration));
            resp.setStatusCode(HttpStatus.SWITCHING_PROTOCOLS);
            log.debug("token valid");
        } catch (IndexOutOfBoundsException e) {
            resp.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        catch (VerificationException e) {
            resp.setStatusCode(HttpStatus.FORBIDDEN);
            log.error(e.getMessage());
            return false;
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest rq, ServerHttpResponse rp, WebSocketHandler h, @Nullable Exception e) {}
}