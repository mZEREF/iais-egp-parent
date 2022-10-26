package sg.gov.moh.iais.egp.bsb.dto.rfi;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.constant.RfiType;

import java.io.Serializable;

@Data
public class RfiDataDisplayDto implements Serializable {
    private String id;
    private String moduleName;
    private String internetProcessUrl;
    private String specialRFIIndicator;
    private Boolean completed;
    private RfiType rfiType;
}
