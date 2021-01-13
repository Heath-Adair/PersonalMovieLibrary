package com.hadair.utilities.time;

import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Service
public class TimeService {

    public String getTimeOfDay() {
        return "Current server-side time is " + LocalTime.now().truncatedTo(ChronoUnit.SECONDS).toString();
    }
}
