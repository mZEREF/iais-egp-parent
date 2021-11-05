package sg.gov.moh.iais.egp.bsb.entity;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.common.BaseEntity;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class FacilityBiological extends BaseEntity {
    private String id;

    private Facility facility;

    private String schedule;

    private String biologicalId;

    private String sampleNature;

    private String sampleNatureOth;

    private String procurementMode;

    private String facTransferForm;

    private String transferExpectedDate;

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
}
