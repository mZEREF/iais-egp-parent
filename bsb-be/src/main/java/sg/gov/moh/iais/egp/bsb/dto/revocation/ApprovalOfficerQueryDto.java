package sg.gov.moh.iais.egp.bsb.dto.revocation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Zhu Tangtang
 * @date 2021/7/26 17:48
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApprovalOfficerQueryDto extends PagingAndSortingDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String facilityName;
    private String facilityAddress;
    private String blockNo;
    private String postalCode;
    private String floorNo;
    private String unitNo;
    private String streetName;
    private String facilityClassification;
    private String facilityType;
    private String processType;
    private Date applicationDate;
    private String applicationNo;
    private String applicationType;
    private String applicationStatus;

    /**
     * The empty values are intended for the select options since we use empty string for 'All' meaning
     */
    public void clearAllFields() {
        id = "";
        facilityName = "";
        facilityAddress = "";
        blockNo = "";
        postalCode = "";
        floorNo = "";
        unitNo = "";
        streetName = "";
        facilityClassification = "";
        facilityType = "";
        processType = "";
        applicationDate = null;
        applicationNo = "";
        applicationType = "";
        applicationStatus = "";
    }
}
