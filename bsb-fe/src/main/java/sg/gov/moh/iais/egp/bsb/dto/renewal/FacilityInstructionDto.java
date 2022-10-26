package sg.gov.moh.iais.egp.bsb.dto.renewal;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : Jiawei
 * @date : 2022/9/27
 */
@Data
public class FacilityInstructionDto implements Serializable {
    private String facilityNo;
    private String facilityName;
    private String classification;
    private String facilityAddress;
    private String validFrom;
    private String validUntil;
}
