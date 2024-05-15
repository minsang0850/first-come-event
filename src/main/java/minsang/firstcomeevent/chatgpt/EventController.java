package minsang.firstcomeevent.chatgpt;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    @PostMapping("/initialize")
    public ResponseEntity<String> initialize(@RequestParam("tickets") int tickets) {
        eventService.initializeTickets(tickets);
        return ResponseEntity.ok("Event initialized with " + tickets + " tickets.");
    }

    @PostMapping("/participate")
    public ResponseEntity<String> participate(@RequestParam("userId") String userId) {
        boolean success = eventService.participate(userId);
        if (success) {
            return ResponseEntity.ok("Participation successful.");
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body("Event is full.");
        }
    }
}
