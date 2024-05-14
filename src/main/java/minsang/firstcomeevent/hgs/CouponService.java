package minsang.firstcomeevent.hgs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
@Slf4j
public class CouponService {
    private final RedisTemplate<String,String> redisTemplate;
    private static final long FIRST_ELEMENT = 0;
    private static final long LAST_ELEMENT = -1;
    private static final long PUBLISH_SIZE = 10;
    private static final long LAST_INDEX = 1;
    private EventCount eventCount;

    public void setEventCount(Event event, int limit){
        this.eventCount = new EventCount(event, limit);
    }

    public void addQueue(Event event){
        final String people = Thread.currentThread().getName();
        final long now = System.currentTimeMillis();

        // key, value(id==name), score(시간)
        redisTemplate.opsForZSet().add(event.toString(), people, (int) now);
        log.info("대기열에 추가 - {} ({}초)", people, now);
    }

    // 대기열 순번 표출
    public void getOrder(Event event){
        final long start = FIRST_ELEMENT;
        final long end = LAST_ELEMENT;

        Set<String> queue = redisTemplate.opsForZSet().range(event.toString(), start, end);

        for (String people : queue) {
            Long rank = redisTemplate.opsForZSet().rank(event.toString(), people);
            log.info("'{}'님의 현재 대기열은 {}명 남았습니다.", people, rank);
        }
    }

    // 1초마다 이벤트에 참여하는 사람 수씩 기프티콘 발급 후 대기열에서 제거
    public void publish(Event event){
        final long start = FIRST_ELEMENT;
        final long end = PUBLISH_SIZE - LAST_INDEX;

        Set<String> queue = redisTemplate.opsForZSet().range(event.toString(), start, end);
        for (String people : queue) {
            final Gifticon gifticon = new Gifticon(event);
            log.info("'{}'님의 {} 기프티콘이 발급되었습니다 ({})",people, gifticon.getEvent().getName(), gifticon.getCode());
            redisTemplate.opsForZSet().remove(event.toString(), people);
            this.eventCount.decrease();
        }
    }

    public boolean validEnd(){
        return this.eventCount != null
                ? this.eventCount.end()
                : false;
    }

    public long getSize(Event event){
        return redisTemplate.opsForZSet().size(event.toString());
    }

}
