package sg.gov.moh.iais.egp.bsb.dto.facilitymanagement;

import lombok.Data;

import java.util.List;

@Data
public class FacilityManagementDisplayDto {
    private String id;
    private String facilityNo;
    private String name;
    private String classification;
    private List<String> facilityActivityTypes;
    private String status;
}
