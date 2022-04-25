package sg.gov.moh.iais.egp.bsb.dto.chklst;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class ChklstItemAnswerDto implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userId;
    private String snNo;
    private String configId;
    private String sectionId;
    private String itemId;
    private String answer;
    private String remarks;
    private String findings;
    private String actionRequired;
    private Boolean rectified;
    private String followupItem;
    private String observeFollowup;
    private String followupAction;
    private String dueDate;
}
