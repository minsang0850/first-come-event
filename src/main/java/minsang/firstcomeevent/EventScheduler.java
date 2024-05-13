package minsang.firstcomeevent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventScheduler {

    private final GifticonService gifticonService;

    @Scheduled(fixedDelay = 1000)
    public void chickenEventScheduler(){
        if(gifticonService.validEnd()){
            log.info("===== 선착순 이벤트가 종료되었습니다. =====");
            return;
        }
        log.info("===== 스케줄러 가동 =====");
        gifticonService.publish(Event.CHICKEN);
        gifticonService.getOrder(Event.CHICKEN);
    }
}

