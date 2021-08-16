package sg.gov.moh.iais.egp.bsb.dto.enquiry;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

/**
 * AUTHOR: YiMing
 * DATE:2021/7/26 10:48
 * DESCRIPTION: TODO
 **/

@Data
public class ApplicationInfoDto implements Serializable{
    private static final long serialVersionUID = 1L;

    private String id;

    private String applicationNo;

    private String applicationType;

    private String applicationStatus;

    private Date applicationSubmissionDate;

    private Date approvalDate;

    private String facilityClassification;

    private String facilityType;

    private String facilityName;

    private String biologicalAgent;

    private String scheduleType;

    private String riskLevelOfTheBiologicalAgent;

    private String processType;

    private Date verifiedByDO;

    private Date verifiedByAO;

    private Date verifiedByHM;
//    private String action;
}
