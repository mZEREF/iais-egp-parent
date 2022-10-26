package sg.gov.moh.iais.egp.bsb.dto.inspection.afc;

import lombok.Data;

import java.io.Serializable;

@Data
public class FacilityAfcDisplayDto implements Serializable {
    private String appointed;
    private String afc;
    private String reasonForChoose;
}
