package sg.gov.moh.iais.egp.bsb.constant;

import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.google.common.collect.Sets;

import java.util.Collections;
import java.util.Set;

public class ChecklistConstants {
    private ChecklistConstants() {}

    public static final String ADHOC_SECTION_ID = "A5BD3570-68D2-EC11-8B82-000C293F0C99";
    public static final String ANSWER_YES = "YES";
    public static final String ANSWER_NO = "NO";
    public static final String ANSWER_NA = "NA";
    public static final Set<String> VALID_ANSWERS;

    public static final String ANSWER_YES_DISPLAY = "Yes";
    public static final String ANSWER_NO_DISPLAY = "No";
    public static final String ANSWER_NA_DISPLAY = "N/A";

    // rectified
    public static final String RECTIFIED_YES_DISPLAY = "Yes";
    public static final String RECTIFIED_NO_DISPLAY = "No";

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

    public static String getAnswer(String displayAnswer) {
        String result;
        if (StringUtil.isEmpty(displayAnswer)) {
            result = null;
        } else {
            switch (displayAnswer) {
                case ANSWER_YES_DISPLAY:
                    result = ANSWER_YES;
                    break;
                case ANSWER_NO_DISPLAY:
                    result = ANSWER_NO;
                    break;
                case ANSWER_NA_DISPLAY:
                    result = ANSWER_NA;
                    break;
                default:
                    result = "";
                    break;
            }
        }

        return result;
    }

    public static String getRectified(Boolean rectified) {
        if (rectified == null) {
            return "";
        }
        return rectified ? RECTIFIED_YES_DISPLAY : RECTIFIED_NO_DISPLAY;
    }

    public static Boolean getRectified(String rectified) {
        if (StringUtil.isEmpty(rectified)) {
            return null;
        }
        return RECTIFIED_YES_DISPLAY.equals(rectified);
    }

}
