package sg.gov.moh.iais.egp.bsb.dto.revocation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import net.sf.oval.constraint.ValidateWithMethod;
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
    private static final String MESSAGE_END_DATE_EARLIER_THAN_START_DATE = "EndDate can not be earlier than startDate.";

    private static final long serialVersionUID = 1L;
    private String id;
    private String facilityName;
    private String facilityAddress;
    private String facilityClassification;
    private String facilityType;
    private String processType;
    private Date searchAppDateFrom;

    @ValidateWithMethod(message = MESSAGE_END_DATE_EARLIER_THAN_START_DATE, methodName = "checkApprovedDtToAfterFrom", parameterType = Date.class, profiles = {"appDate"})
    private Date searchAppDateTo;

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
        facilityClassification = "";
        facilityType = "";
        processType = "";
        searchAppDateFrom = null;
        searchAppDateTo = null;
        applicationNo = "";
        applicationType = "";
        applicationStatus = "";
    }

    private boolean checkApprovedDtToAfterFrom(Date searchAppDateTo) {
        if (searchAppDateTo == null || searchAppDateFrom == null){
            return true;
        }
        return searchAppDateTo.after(searchAppDateFrom);
    }
}
