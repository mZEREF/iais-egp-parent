package sg.gov.moh.iais.egp.bsb.dto.info.bat;

import lombok.Data;

import java.io.Serializable;


@Data
public class BatCodeInfo implements Serializable {
    private String code;
    private String name;
}
