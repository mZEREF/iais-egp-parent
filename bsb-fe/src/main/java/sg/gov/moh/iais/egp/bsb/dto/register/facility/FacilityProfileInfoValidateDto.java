package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.file.DocMeta;

import java.io.Serializable;
import java.util.List;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap(excluded = {"metaList"})
public class FacilityProfileInfoValidateDto implements Serializable {
    private String facName;
    private String facType;
    private String facTypeDetails;
    private String sameAddress;
    private String postalCode;
    private String addressType;
    private String block;
    private String floor;
    private String unitNo;
    private String streetName;
    private String building;
    private String facilityProtected;
    private List<DocMeta> metaList;

    private String inChargePersonName;
    private String inChargePersonDesignation;
    private String inChargePersonEmail;
    private String inChargePersonContactNo;

    private String opvSabin1IM;
    private String opvSabin2IM;
    private String opvSabin3IM;
    private String opvSabin1IMExpectedDestructDt;
    private String opvSabin2IMExpectedDestructDt;
    private String opvSabin3IMExpectedDestructDt;
    private String opvSabin1IMRetentionReason;
    private String opvSabin2IMRetentionReason;
    private String opvSabin3IMRetentionReason;

    private String opvSabin1PIM;
    private String opvSabin2PIM;
    private String opvSabin3PIM;
    private String opvSabin1PIMRiskLevel;
    private String opvSabin2PIMRiskLevel;
    private String opvSabin3PIMRiskLevel;
    private String opvSabin1PIMRetentionReason;
    private String opvSabin2PIMRetentionReason;
    private String opvSabin3PIMRetentionReason;
}
