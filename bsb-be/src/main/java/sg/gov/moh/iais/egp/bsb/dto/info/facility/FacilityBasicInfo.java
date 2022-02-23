package sg.gov.moh.iais.egp.bsb.dto.info.facility;

import lombok.Data;

import java.io.Serializable;


@Data
public class FacilityBasicInfo implements Serializable {
    private String id;

    private String name;

    private String classification;
}
