package highcouncil.web.websocket;

import highcouncil.web.websocket.dto.DiscussionDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.time.Instant;

import static highcouncil.config.WebsocketConfiguration.IP_ADDRESS;

@Controller
public class DiscussionService implements ApplicationListener<SessionDisconnectEvent> {

    private static final Logger log = LoggerFactory.getLogger(DiscussionService.class);

    private final SimpMessageSendingOperations messagingTemplate;

    public DiscussionService(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @SendToUser("/topic/discussion/{gameId}")
    public DiscussionDTO sendActivity(@Payload DiscussionDTO discussionDTO, StompHeaderAccessor stompHeaderAccessor, Principal principal) {
    	discussionDTO.setFromUser(principal.getName());
        discussionDTO.setTime(Instant.now());
        log.debug("Sending discussion {}", discussionDTO);
        return discussionDTO;
    }

    @Override
    public void onApplicationEvent(SessionDisconnectEvent event) {
    	DiscussionDTO discussionDTO = new DiscussionDTO();
    	discussionDTO.setMessage("logout");
        messagingTemplate.convertAndSend("/topic/discussion", discussionDTO);
    }
}
