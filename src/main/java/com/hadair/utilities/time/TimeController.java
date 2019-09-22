package com.hadair.utilities.time;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TimeController {

    private TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    @GetMapping("/api/timeOfDay")
    public String getTimeOfDay() {
        return timeService.getTimeOfDay();
    }
}
