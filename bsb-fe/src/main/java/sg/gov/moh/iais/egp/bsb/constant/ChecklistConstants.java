package sg.gov.moh.iais.egp.bsb.constant;

import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

public class ChecklistConstants {
    private ChecklistConstants() {}

    public static final String ANSWER_YES = "YES";
    public static final String ANSWER_NO = "NO";
    public static final String ANSWER_NA = "NA";
    public static final Set<String> VALID_ANSWERS;

    public static final String ANSWER_YES_DISPLAY = "Yes";
    public static final String ANSWER_NO_DISPLAY = "No";
    public static final String ANSWER_NA_DISPLAY = "N/A";

    static {
        Set<String> validAnswers = Sets.newHashSetWithExpectedSize(3);
        validAnswers.add(ANSWER_YES);
        validAnswers.add(ANSWER_NO);
        validAnswers.add(ANSWER_NA);
        VALID_ANSWERS = Collections.unmodifiableSet(validAnswers);
    }

    public static String displayAnswer(String answer) {
        String display = null;
        switch (answer) {
            case ANSWER_YES:
                display = ANSWER_YES_DISPLAY;
                break;
            case ANSWER_NO:
                display = ANSWER_NO_DISPLAY;
                break;
            case ANSWER_NA:
                display = ANSWER_NA_DISPLAY;
                break;
            default:
                display = "";
                break;
        }
        return display;
    }
}
