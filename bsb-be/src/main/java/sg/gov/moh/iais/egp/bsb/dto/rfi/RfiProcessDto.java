package sg.gov.moh.iais.egp.bsb.dto.rfi;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;

@Data
public class RfiProcessDto implements Serializable {
    private String commentsToApplicant;
    private Map<String, Boolean> rfiSelectMap;

    public static final String KEY_COMMENTS_TO_APPLICANT = "commentsToApplicant";
}
