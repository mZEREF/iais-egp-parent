package sg.gov.moh.iais.egp.bsb.dto.appview.approval;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
public class ApprovalProfileDto implements Serializable {
    @Data
    @NoArgsConstructor
    public static class BATInfo implements Serializable {
        private String batId;
        private String batName;
        private String prodMaxVolumeLitres;
        private String lspMethod;
        private String procurementMode;
        private String facilityNameOfTransfer;
        private String expectedDateOfImport;
        private String contactPersonNameOfTransfer;
        private String impCtcPersonNo;
        private String contactPersonEmailOfTransfer;
        private String transferFacAddr1;
        private String transferFacAddr2;
        private String transferFacAddr3;
        private String transferCountry;
        private String transferCity;
        private String transferState;
        private String transferPostalCode;
        private String courierServiceProviderName;
        private String remarks;
        private String prjName;
        private String principalInvestigatorName;
        private String workActivityIntended;
        private String startDate;
        private String endDate;
        private String processType;
    }

    private String schedule;
    private List<BATInfo> batInfos;
}
