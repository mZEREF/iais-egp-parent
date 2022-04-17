package sg.gov.moh.iais.egp.bsb.dto.inspection;

import lombok.Data;
import java.io.Serializable;


@Data
public class InsFacInfoDto implements Serializable {
    private String facName;
    private String classification;
    private String blk;
    private String street;
    private String floor;
    private String unit;
    private String postalCode;
    private String adminName;
    private String adminMobileNo;
    private String adminEmail;
}
