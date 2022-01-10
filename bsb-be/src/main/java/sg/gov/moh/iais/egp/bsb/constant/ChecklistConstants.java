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

    static {
        Set<String> validAnswers = Sets.newHashSetWithExpectedSize(3);
        validAnswers.add(ANSWER_YES);
        validAnswers.add(ANSWER_NO);
        validAnswers.add(ANSWER_NA);
        VALID_ANSWERS = Collections.unmodifiableSet(validAnswers);
    }
}
