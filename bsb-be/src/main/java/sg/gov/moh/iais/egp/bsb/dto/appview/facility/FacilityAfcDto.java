package sg.gov.moh.iais.egp.bsb.dto.appview.facility;

import lombok.Data;

import java.io.Serializable;


@Data
public class FacilityAfcDto implements Serializable {
    private String appointed;
    private String afc;
    private String selectReason;
}
