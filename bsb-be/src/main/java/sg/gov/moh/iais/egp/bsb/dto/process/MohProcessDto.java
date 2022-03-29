package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2022/3/14
 */
@Data
public class MohProcessDto implements Serializable {
    private String moduleName;

    private InfoDto infoDto;
    //facility details
    private String facilityName;
    private String facilityClassification;
    private String approvedFacilityActivityType;

    private List<ApprovalFacilityActivityDto> approvalFacilityActivityDtoList;
    private List<ApprovalFacilityBatDto> approvalFacilityBatDtoList;

    private String remarks;
    private String processingDecision;
    private String inspectionRequired;
}
