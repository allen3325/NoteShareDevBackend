package ntou.notesharedevbackend.notificationModule.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
    // TODO: now permit all. need to discussion.
//    @Override
//    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages
//                .nullDestMatcher().permitAll()
//                .simpSubscribeDestMatchers("/app/**").permitAll()
//                .simpDestMatchers("/topic/**",
//                        "/private/**").permitAll()
//                .anyMessage().permitAll();
//    }

    // Determines if a CSRF token is required for connecting. This protects against remote
    // sites from connecting to the application and being able to read/write data over the
    // connection. The default is false (the token is required).
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
