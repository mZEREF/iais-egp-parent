package sg.gov.moh.iais.egp.bsb.dto.approval;

import lombok.Data;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/11
 */
@Data
public class ApprovalApplicationDto implements Serializable {

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","largeImport","special"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessNull","possessOthersNull","largeNull","largeLocal","possessOthersNull","largeImport","special"})
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

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","largeLocal","largeImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","largeLocal","largeImport"})
    private String facTransferForm;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","largeLocal","largeImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","largeLocal","largeImport"})
    private Date transferExpectedDate;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","largeLocal","largeImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","largeLocal","largeImport"})
    private String impCtcPersonName;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","largeLocal","largeImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","largeLocal","largeImport"})
    private String impCtcPersonEmail;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessOthersLocal","largeLocal"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessOthersLocal","largeLocal"})
    private String impCtcPersonNo;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","largeLocal","largeImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","largeLocal","largeImport"})
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
    private Date endDate;

    private String appType;

    private String processType;

    private String status;

    private Date applicationDt;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessOthersImport","possessNull","possessOthersNull","largeLocal","largeImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessOthersImport","possessNull","possessOthersNull","largeLocal","largeImport"})
    private String checkbox1;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessOthersImport","possessNull","possessOthersNull","largeLocal","largeImport","special"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","possessOthersImport","possessNull","possessOthersNull","largeLocal","largeImport","special"})
    private String checkbox2;
}