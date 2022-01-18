package sg.gov.moh.iais.egp.bsb.dto.deregorcancellation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * @author : LiRan
 * @date : 2022/1/11
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CancellationApprovalDto {
    private String approvalNo;
    private String facilityName;
    private String facilityAddress;
    private String approvalType;
    private String biologicalAgentToxin;
    private String physicalPossession;
    private String reasons;
    private String remarks;
    private String declaration1;
    private String declaration2;
    private String declaration3;
    private String declaration4;
    private String declaration5;
}
