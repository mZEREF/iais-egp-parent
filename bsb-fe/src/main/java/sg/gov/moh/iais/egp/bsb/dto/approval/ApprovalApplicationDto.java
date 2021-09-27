package sg.gov.moh.iais.egp.bsb.dto.approval;

import lombok.Data;
import net.sf.oval.constraint.Email;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;
import net.sf.oval.constraint.ValidateWithMethod;
import sg.gov.moh.iais.egp.bsb.common.constraints.MobileNo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/11
 */
@Data
public class ApprovalApplicationDto implements Serializable {

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeImport","special","draft"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","possessOthersNull","largeImport","special","draft"})
    private String facilityId;

    private String facilityName;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeLocal","largeImport","special"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeLocal","largeImport","special"})
    private String schedule;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeLocal","largeImport","special"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeLocal","largeImport","special"})
    private List<String> biologicalIdList;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull"})
    private List<String> natureOfTheSampleList;

    private String biologicalName;

    private String sampleNature;

    @NotNull(message = "This is mandatory.", profiles = {"possessOthersLocal","possessOthersImport","possessOthersNull"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessOthersLocal","possessOthersImport","possessOthersNull"})
    private String sampleNatureOth;

    @NotNull(message = "This is mandatory.", profiles = {"possessNull","possessOthersNull","largeNull","special"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessNull","possessOthersNull","largeNull","special"})
    private String procurementMode;

    @NotNull(message = "This is mandatory.", profiles = {"possessImport","possessOthersImport","largeImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessImport","possessOthersImport","largeImport"})
    private String facilityNameOfImport;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessOthersLocal","largeLocal"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessOthersLocal","largeLocal"})
    private String facilityNameOfTransfer;

    @NotNull(message = "This is mandatory.", profiles = {"possessImport","possessOthersImport","largeImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessImport","possessOthersImport","largeImport"})
    @ValidateWithMethod(message = "Date is not expect date.", methodName = "checkToExpecteddate", parameterType = Date.class, profiles = {"possessImport","possessOthersImport","largeImport"})
    private Date expectedDateOfImport;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessOthersLocal","largeLocal"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessOthersLocal","largeLocal"})
    @ValidateWithMethod(message = "Date is not expect date.", methodName = "checkToExpecteddate", parameterType = Date.class, profiles = {"possessLocal","possessOthersLocal","largeLocal"})
    private Date expectedDateOfTransfer;

    @NotNull(message = "This is mandatory.", profiles = {"possessImport","possessOthersImport","largeImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessImport","possessOthersImport","largeImport"})
    private String contactPersonNameOfImport;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessOthersLocal","largeLocal"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessOthersLocal","largeLocal"})
    private String contactPersonNameOfTransfer;

    @NotNull(message = "This is mandatory.", profiles = {"possessImport","possessOthersImport","largeImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessImport","possessOthersImport","largeImport"})
    @Email(message = "The email is incorrect", profiles = {"possessImport","possessOthersImport","largeImport"})
    private String contactPersonEmailOfImport;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessOthersLocal","largeLocal"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessOthersLocal","largeLocal"})
    @Email(message = "The email is incorrect", profiles = {"possessLocal","possessOthersLocal","largeLocal"})
    private String contactPersonEmailOfTransfer;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessOthersLocal","largeLocal"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessOthersLocal","largeLocal"})
    @MobileNo(message = "Contact No is not a valid mobile number", profiles = {"possessLocal","possessOthersLocal","largeLocal"})
    private String impCtcPersonNo;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","largeLocal","largeImport","possessNull","possessOthersNull","largeNull"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","largeLocal","largeImport","possessNull","possessOthersNull","largeNull"})
    private String transferFacAddr1;

    private String transferFacAddr2;

    private String transferFacAddr3;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeImport"})
    private String transferCountry;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeImport"})
    private String transferCity;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeImport"})
    private String transferState;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeImport"})
    private String transferPostalCode;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeImport"})
    private String courierServiceProviderName;

    private String remarks;

    @NotNull(message = "This is mandatory.", profiles = {"largeLocal","largeImport","largeNull"})
    @NotBlank(message = "This is mandatory.", profiles = {"largeLocal","largeImport","largeNull"})
    private String prodMaxVolumeLitres;

    @NotNull(message = "This is mandatory.", profiles = {"largeLocal","largeImport","largeNull"})
    @NotBlank(message = "This is mandatory.", profiles = {"largeLocal","largeImport","largeNull"})
    private String lspMethod;

    @NotNull(message = "This is mandatory.", profiles = {"special"})
    @NotBlank(message = "This is mandatory.", profiles = {"special"})
    private String prjName;

    @NotNull(message = "This is mandatory.", profiles = {"special"})
    @NotBlank(message = "This is mandatory.", profiles = {"special"})
    private String principalInvestigatorName;

    @NotNull(message = "This is mandatory.", profiles = {"special"})
    @NotBlank(message = "This is mandatory.", profiles = {"special"})
    private String workActivityIntended;

    @NotNull(message = "This is mandatory.", profiles = {"special"})
    @NotBlank(message = "This is mandatory.", profiles = {"special"})
    private Date startDate;

    @NotNull(message = "This is mandatory.", profiles = {"special"})
    @NotBlank(message = "This is mandatory.", profiles = {"special"})
    @ValidateWithMethod(message = "EndDate can not be earlier than startDate.", methodName = "checkToAfterFrom", parameterType = Date.class, profiles = {"special"})
    private Date endDate;

    private String appType;

    private String processType;

    private String status;

    private Date applicationDt;

    private String approvalType;

    @NotNull(message = "Please Check \"Declaration on the accuracy of submission\" to proceed.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessOthersImport","possessNull","possessOthersNull","largeLocal","largeImport","largeNull"})
    @NotBlank(message = "Please Check \"Declaration on the accuracy of submission\" to proceed.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessOthersImport","possessNull","possessOthersNull","largeLocal","largeImport","largeNull"})
    private String checkbox1;

    @NotNull(message = "Please Check \"Declaration of Compliance with BATA Transport Regulations\" to proceed.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessOthersImport","possessNull","possessOthersNull","largeLocal","largeImport","largeNull","special"})
    @NotBlank(message = "Please Check \"Declaration of Compliance with BATA Transport Regulations\" to proceed.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessOthersImport","possessNull","possessOthersNull","largeLocal","largeImport","largeNull","special"})
    private String checkbox2;

    private boolean checkToAfterFrom(Date endDate) {
        if (endDate == null || startDate == null){
            return true;
        }
        return endDate.after(startDate);
    }

    private boolean checkToExpecteddate(Date transferExpectedDate) {
        if (transferExpectedDate == null){
            return true;
        }
        return transferExpectedDate.after(new Date());
    }

}