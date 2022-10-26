package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import lombok.Data;

import java.io.Serializable;


@Data
public class FacilityProfileInfo implements Serializable {
    private String facilityEntityId;

    private String facName;

    private String facType;

    private String facTypeDetails;

    private String sameAddress;

    private String block;

    private String addressType;

    private String streetName;

    private String floor;

    private String unitNo;

    private String postalCode;

    private String building;

    private String facilityProtected;

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
