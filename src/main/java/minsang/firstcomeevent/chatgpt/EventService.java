package minsang.firstcomeevent.chatgpt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class EventService {

    private static final String EVENT_KEY = "event:tickets";

    private final RedisTemplate<String, Object> redisTemplate;

    public boolean participate(String userId) {
        Long remainingTickets = redisTemplate.opsForValue().decrement(EVENT_KEY);

        if (remainingTickets != null && remainingTickets >= 0) {
            redisTemplate.opsForSet().add(EVENT_KEY + ":participants", userId);
            return true;
        } else {
            redisTemplate.opsForValue().increment(EVENT_KEY);
            return false;
        }
    }

    public void initializeTickets(int numberOfTickets) {
        redisTemplate.opsForValue().set(EVENT_KEY, numberOfTickets);
    }
}

