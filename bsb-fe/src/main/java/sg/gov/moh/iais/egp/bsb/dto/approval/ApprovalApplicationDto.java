package sg.gov.moh.iais.egp.bsb.dto.approval;

import lombok.Data;
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

    private Facility facility;

    private String schedule;

    private String sampleNatureOth;

    private String sampleNature;

    private String procurementMode;

    private String facTransferForm;

    private Date transferExpectedDate;

    private String impCtcPersonName;

    private String impCtcPersonEmail;

    private String impCtcPersonNo;

    private String transferFacAddr1;

    private String transferFacAddr2;

    private String transferFacAddr3;

    private String transferCountry;

    private String transferCity;

    private String transferState;

    private String transferPostalCode;

    private String courierServiceProviderName;

    private String remarks;

    private String prodMaxVolumeLitres;

    private String lspMethod;

    private String prjName;

    private String principalInvestigatorName;

    private String workActivityIntended;

    private Date startDate;

    private Date endDate;

    private String appType;

    private String processType;

    private String status;

    private Date applicationDt;

    private String checkbox1;

    private String checkbox2;

    private List<String> biologicalIdList;

    private List<String> natureOfTheSampleList;

    private String biologicalName;
}