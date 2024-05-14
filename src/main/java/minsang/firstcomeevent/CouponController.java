package minsang.firstcomeevent;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class CouponController {

    private final CouponService couponService;

    @GetMapping("/add/queue")
    void addQueue() {
        couponService.addQueue(Event.CHICKEN);
    }
}
