package com.zlatev.tournament_management_system.component;

import com.zlatev.tournament_management_system.dto.match.MatchUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class MatchUpdateBroadcaster {
    private final SimpMessagingTemplate messagingTemplate;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onMatchUpdated(MatchUpdatedEvent event) {
        messagingTemplate.convertAndSend("/topic/match/" + event.matchId(), event.match());
        messagingTemplate.convertAndSend("/topic/tournament/" + event.tournamentId() + "/matches", event.match());
    }
}
