package sg.gov.moh.iais.egp.bsb.dto.approval;

import lombok.Data;
import net.sf.oval.constraint.NotBlank;
import net.sf.oval.constraint.NotNull;
import sg.gov.moh.iais.egp.bsb.entity.Facility;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2021/8/11
 */
@Data
public class ApprovalApplicationDto implements Serializable {

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private Facility facility;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String schedule;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private List<String> biologicalIdList;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private List<String> natureOfTheSampleList;

    private String biologicalName;

    private String sampleNature;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String sampleNatureOth;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String procurementMode;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String facTransferForm;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private Date transferExpectedDate;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String impCtcPersonName;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String impCtcPersonEmail;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String impCtcPersonNo;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String transferFacAddr1;


    private String transferFacAddr2;

    private String transferFacAddr3;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String transferCountry;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String transferCity;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String transferState;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String transferPostalCode;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String courierServiceProviderName;

    private String remarks;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String prodMaxVolumeLitres;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String lspMethod;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String prjName;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String principalInvestigatorName;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String workActivityIntended;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private Date startDate;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private Date endDate;

    private String appType;

    private String processType;

    private String status;

    private Date applicationDt;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String checkbox1;

    @NotNull(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    @NotBlank(message = "This is mandatory.", profiles = {"possessLocal","possessImport","possessOthersLocal","possessOthersImport","large","specialLocal","specialImport"})
    private String checkbox2;
}