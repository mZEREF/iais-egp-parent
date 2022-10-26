package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InboxAFCResultDto {
    private String insCerFacRelId;
    private String mainAppId;
    private String appId;
    private String appNo;
    private String appType;
    private String appSubType;
    private String appStatus;
    private String facilityNo;
    private String facilityName;
    private String facilityAddress;
    private Date updateDate;
    private String dataType;
    private Boolean insCompleted;
    private Boolean cerCompleted;
}
