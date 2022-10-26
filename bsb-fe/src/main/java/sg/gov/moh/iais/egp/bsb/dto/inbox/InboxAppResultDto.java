package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.Data;

@Data
public class InboxAppResultDto {
    private String appId;
    private String appNo;
    private String facilityName;
    private String appType;
    private String appSubType;
    private String appStatus;
    private String submittedDt;
    private String assigned;
}
