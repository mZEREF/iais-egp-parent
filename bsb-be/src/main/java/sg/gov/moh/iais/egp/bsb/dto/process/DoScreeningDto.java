package sg.gov.moh.iais.egp.bsb.dto.process;

import lombok.Data;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.ValidateWithMethod;

import java.util.Date;

/**
 * @author : LiRan
 * @date : 2021/8/23
 */
@Data
public class DoScreeningDto {

    private String facilityId;

    @NotNull(message = "This is mandatory.", profiles = {"DOType1","DOType2"})
    @NotBlank(message = "This is mandatory.", profiles = {"DOType1","DOType2"})
    private String riskLevel;

    private String riskLevelComments;

    @NotNull(message = "This is mandatory.", profiles = {"AOType1"})
    @NotBlank(message = "This is mandatory.", profiles = {"AOType1"})
    private Date erpReportDt;

    @NotNull(message = "This is mandatory.", profiles = {"AOType1"})
    @NotBlank(message = "This is mandatory.", profiles = {"AOType1"})
    private Date redTeamingReportDt;

    private Date lentivirusReportDt;

    @NotNull(message = "This is mandatory.", profiles = {"AOType1","DOType2","AOType2"})
    @NotBlank(message = "This is mandatory.", profiles = {"AOType1","DOType2","AOType2"})
    private Date internalInspectionReportDt;

    @NotNull(message = "This is mandatory.", profiles = {"DOType1","AOType1","DOType2","AOType2"})
    @NotBlank(message = "This is mandatory.", profiles = {"DOType1","AOType1","DOType2","AOType2"})
    private Date validityStartDt;

    @NotNull(message = "This is mandatory.", profiles = {"DOType1","AOType1","DOType2","AOType2"})
    @NotBlank(message = "This is mandatory.", profiles = {"DOType1","AOType1","DOType2","AOType2"})
    @ValidateWithMethod(message = "EndDate can not be earlier than startDate.", methodName = "checkToAfterFrom", parameterType = Date.class, profiles = {"DOType1","AOType1","DOType2","AOType2"})
    private Date validityEndDt;

    private String selectedAfc;

    private String applicationId;

    private String applicationNo;

    private String status;

    private String appStatus;

    private String remarks;

    @NotNull(message = "This is mandatory.", profiles = {"DOType1","AOType1","HMType","DOType2","AOType2"})
    @NotBlank(message = "This is mandatory.", profiles = {"DOType1","AOType1","HMType","DOType2","AOType2"})
    private String processDecision;

    private String actionBy;

    private String reason;

    @NotNull(message = "This is mandatory.", profiles = {"DOType1","AOType1","DOType2","AOType2"})
    @NotBlank(message = "This is mandatory.", profiles = {"DOType1","AOType1","DOType2","AOType2"})
    private String finalRemarks;

    private Date approvalDate;

    private boolean checkToAfterFrom(Date validityEndDt) {
        if (validityStartDt == null || validityEndDt == null){
            return true;
        }
        return validityEndDt.after(validityStartDt);
    }
}
