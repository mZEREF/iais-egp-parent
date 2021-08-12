package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Zhu Tangtang
 * @date 2021/7/26 17:48
 */
@Getter
@Setter
@NoArgsConstructor
public class ApprovalOfficerQueryDto implements Serializable {
    private String id;

    private String facilityName;

    private String facilityAddress;

    private String facilityClassification;

    private String facilityType;

    private String processType;

    private Date applicationDate;

    private String applicationNo;

    private String applicationType;

    private String applicationStatus;

}
