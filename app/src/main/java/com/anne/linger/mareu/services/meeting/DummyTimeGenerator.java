package com.anne.linger.mareu.services.meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Generates a dummy list of time
 */
public abstract class DummyTimeGenerator {
    public static final List<String> DUMMY_TIMES = Arrays.asList(
            "8h00",
            "9h00",
            "10h00",
            "11h00",
            "12h00",
            "13h00",
            "14h00",
            "15h00",
            "16h00",
            "17h00",
            "18h00",
            "19h00",
            "20h00");

    static List<String> generateTimes() {
        return new ArrayList<>(DUMMY_TIMES);
    }
}
