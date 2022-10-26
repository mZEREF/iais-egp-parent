package sg.gov.moh.iais.egp.bsb.dto.adhocInspection;

import lombok.Data;

import java.util.List;

@Data
public class AdhocInspectionDisplayDto {
    private String id;
    private String facilityNo;
    private String name;
    private String classification;
    private List<String> facilityActivityTypes;
    private String status;
    private String inspectionDate;
    private String validityEndDate;
    private String facilityAddress;
}
