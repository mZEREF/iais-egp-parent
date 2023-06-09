package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.Data;

import java.time.LocalDate;

@Data
public class InboxFacResultDto {
    private String facilityId;
    private String facilityNo;
    private String facilityName;
    private String facilityClassification;
    private String blkNo;
    private String streetName;
    private String floorNo;
    private String unitNo;
    private String postalCode;
    private String roleInFac;
    private String status;
    private LocalDate effectiveEndDate;
    private Boolean renewable;
}
