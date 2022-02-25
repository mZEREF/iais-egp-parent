package sg.gov.moh.iais.egp.bsb.dto.info.bat;

import lombok.Data;

import java.io.Serializable;


@Data
public class BatBasicInfo implements Serializable {
    private String id;
    private String name;
}
