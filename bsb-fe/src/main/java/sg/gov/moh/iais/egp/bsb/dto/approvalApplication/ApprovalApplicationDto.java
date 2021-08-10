package sg.gov.moh.iais.egp.bsb.dto.approvalApplication;

import com.ecquaria.cloud.moh.iais.common.annotation.CustomMsg;
import lombok.Getter;
import lombok.Setter;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Li Ran
 * @Date 2021/7/26 17:09
 **/
@Getter
@Setter
public class ApprovalApplicationDto implements Serializable {

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport","special"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport","special"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Facility Name")
    private String facilityId;

    private String facilityName;

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
    private String facilityTransferFrom;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Expected Date")
    private Date transferExpectedDt;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Contact person")
    private String importContactPersonName;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Email address contact person")
    private String importContactPersonEmail;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessLocalOthers","largeLocal"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessLocalOthers","largeLocal"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Contact No of Contact Person")
    private String importContactPersonNo;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"possessLocal","possessImport","possessLocalOthers","possessImportOthers","largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Facility Address")
    private String transferFacilityAddr1;

    private String transferFacilityAddr2;

    private String transferFacilityAddr3;

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
    private String productionMaximumVolumeLitres;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"largeLocal","largeImport"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"largeLocal","largeImport"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Method or system used")
    private String largeScaleProductionMethod;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"special"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"special"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "Name of Project")
    private String projectName;

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
    private Date startDt;

    @NotBlank(message = "GENERAL_ERR0006", profiles = {"special"})
    @NotNull(message = "GENERAL_ERR0006", profiles = {"special"})
    @CustomMsg(placeHolders ="field" ,replaceVals= "End Work Activities Date")
    private Date endDt;

}