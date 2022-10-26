package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import lombok.Data;

import java.io.Serializable;


@Data
public class ApprovalSelectionDto implements Serializable{
    private boolean isEnteredInbox;
    private String draftAppNo;
    private String facilityId;
    private String facilityName;
    private String processType;
}
