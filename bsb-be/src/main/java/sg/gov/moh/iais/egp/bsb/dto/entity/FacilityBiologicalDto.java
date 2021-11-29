package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import com.googlecode.jmapper.annotations.JMap;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap(excluded = {"facilityBiologicalAgentId"})
public class FacilityBiologicalDto extends BaseEntityDto {
    private String id;

    @JMap(value = "${facilityBiologicalAgent.id}")
    private String facilityBiologicalAgentId;

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

    private Long prodMaxVolumeLitres;

    private String lspMethod;

    private String prjName;

    private String principalInvestigatorName;

    private String workActivityIntended;

    private Date startDate;

    private Date endDate;
}
