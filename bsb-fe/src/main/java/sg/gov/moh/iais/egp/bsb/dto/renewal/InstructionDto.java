package sg.gov.moh.iais.egp.bsb.dto.renewal;

import lombok.Data;

import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2021/12/11
 */
@Data
public class InstructionDto implements Serializable {
    private String approvalNo;
    private String type;
    private String facilityAddress;
    private String startDate;
    private String expiresOn;
}
