package sg.gov.moh.iais.egp.bsb.dto.incident.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author YiMing
 * @version 2021/12/28 8:56
 **/
@Data
public class BatInfoDto implements Serializable {
    private String scheduleType;
    private String approvalNo;
    private String bat;
    private String physicalPossession;
    private String quantity;
    private List<String> sampleNatures;
}
