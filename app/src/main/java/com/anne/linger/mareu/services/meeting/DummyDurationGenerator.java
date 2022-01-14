package com.anne.linger.mareu.services.meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
*Created by Anne Linger on 14/01/2022.
*/public abstract class DummyDurationGenerator {

    public static List<String> DUMMY_DURATIONS = Arrays.asList(
            "30 minutes",
            "1 heure",
            "1 heure 30 minutes",
            "2 heures",
            "2 heures 30 minutes",
            "3 heures",
            "3 heures 30 minutes",
            "4 heures"
    );

    static List<String> generateDurations(){
        return new ArrayList<>(DUMMY_DURATIONS);
    }
}
