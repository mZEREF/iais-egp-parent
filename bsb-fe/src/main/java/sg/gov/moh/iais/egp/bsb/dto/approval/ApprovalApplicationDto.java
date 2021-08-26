package sg.gov.moh.iais.egp.bsb.dto.approval;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomMsg;
import lombok.Data;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;
import sg.gov.moh.iais.egp.bsb.entity.Facility;

import java.io.Serializable;
import java.util.Date;

/**
 * @author : LiRan
 * @date : 2021/8/11
 */
@Data
public class ApprovalApplicationDto implements Serializable {

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport","special"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport","special"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Facility Name")
    private Facility facility;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport","special"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport","special"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Schedule")
    private String schedule;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport","special"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport","special"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Agent/Toxin")
    private String biologicalId;

    private String listOfAgentsOrToxins;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Nature of the Sample")
    private String sampleNature;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocalOthers","possessImportOthers"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocalOthers","possessImportOthers"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Others, please specify")
    private String sampleNatureOth;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport","special"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport","special"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Mode of Procurement")
    private String procurementMode;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Source Facility Name")
    private String facTransferForm;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Expected Date")
    private Date transferExpectedDate;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Contact person")
    private String impCtcPersonName;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Email address contact person")
    private String impCtcPersonEmail;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessLocalOthers","largeLocal"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessLocalOthers","largeLocal"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Contact No of Contact Person")
    private String impCtcPersonNo;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Facility Address")
    private String transferFacAddr1;

    private String transferFacAddr2;

    private String transferFacAddr3;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Country")
    private String transferCountry;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "City")
    private String transferCity;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "State")
    private String transferState;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Postal Code")
    private String transferPostalCode;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Name of Courier Service Provider")
    private String courierServiceProviderName;

    private String remarks;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Estimated maximum volume")
    private String prodMaxVolumeLitres;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Method or system used")
    private String lspMethod;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"special"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"special"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Name of Project")
    private String prjName;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"special"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"special"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Mode of Procurement")
    private String principalInvestigatorName;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"special"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"special"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Intended Work Activities")
    private String workActivityIntended;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"special"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"special"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Start Work Activities Date")
    private Date startDate;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"special"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"special"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "End Work Activities Date")
    private Date endDate;

    private String appType;

    private String processType;

    private String status;

    private Date applicationDt;

    @NotBlank(message = "Please Check 'Declaration on the accuracy of submission' to proceed", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @NotNull(message = "Please Check 'Declaration on the accuracy of submission' to proceed", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    private String checkbox1;

    @NotBlank(message = "Please Check 'Declaration of Compliance with BATA Transport Regulations' to proceed", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport","special"})
    @NotNull(message = "Please Check 'Declaration of Compliance with BATA Transport Regulations' to proceed", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport","special"})
    private String checkbox2;

}