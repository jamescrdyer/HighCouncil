package highcouncil.config;

import highcouncil.security.AuthoritiesConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class WebsocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
            .nullDestMatcher().authenticated()
            .simpDestMatchers("/user/topic/discussion/*").hasAuthority(AuthoritiesConstants.USER)
            .simpDestMatchers("/user/*/topic/discussion/*").hasAuthority(AuthoritiesConstants.USER)
            // matches any destination that starts with /topic/
            // (i.e. cannot send messages directly to /topic/)
            // (i.e. cannot subscribe to /topic/messages/* to get messages sent to
            // /topic/messages-user<id>)
            .simpDestMatchers("/topic/**").hasAuthority(AuthoritiesConstants.ADMIN)
            // message types other than MESSAGE and SUBSCRIBE
//            .simpDestMatchers("/topic/tracker").hasAuthority(AuthoritiesConstants.ADMIN)
//            .simpDestMatchers("/topic/**").authenticated()
            .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).denyAll()
            // catch all
            .anyMessage().denyAll();
    }

    /**
     * Disables CSRF for Websockets.
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
