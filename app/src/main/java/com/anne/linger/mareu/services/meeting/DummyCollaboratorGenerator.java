package com.anne.linger.mareu.services.meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
*Created by Anne Linger on 21/12/2021.
*/
public abstract class DummyCollaboratorGenerator {

    public static List<String> DUMMY_COLLABORATORS = Arrays.asList(
            "adam@lamzone.com",
            "caroline@lamzone.com",
            "karl@lamzone.com",
            "paul@lamzone.com",
            "victor@lamzone.com",
            "sophie@lamzone.com",
            "gwen@lamzone.com",
            "sonia@lamzone.com",
            "eric@lamzone.com",
            "martin@lamzone.com",
            "aurelie@lamzone.com",
            "thomas@lamzone.com"
    );

    static List<String> generateCollaborators(){
        return new ArrayList<>(DUMMY_COLLABORATORS);
    }
}
