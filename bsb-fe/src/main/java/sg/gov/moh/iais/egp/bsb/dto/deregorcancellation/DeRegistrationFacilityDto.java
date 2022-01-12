package sg.gov.moh.iais.egp.bsb.dto.deregorcancellation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2022/1/11
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeRegistrationFacilityDto implements Serializable {
    private String approvalNo;
    private String facilityName;
    private String facilityAddress;
    private String facilityClassification;
    private String reasons;
    private String remarks;
    private List<ApprovalInfo> approvalInfoList;
    private String declaration1;
    private String declaration2;
    private String declaration3;
    private String declaration4;
    private String declaration5;
    private String declaration6;

    @Data
    public static class ApprovalInfo implements Serializable{
        private String approvalType;
        private String biologicalAgentToxin;
        private String status;
        private String physicalPossession;
    }

}
