package sg.gov.moh.iais.egp.bsb.dto.datasubmission;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author tangtang
 * @date 2022/2/18 9:34
 */
@Data
public class BatDto implements Serializable {
    private String approvalNo;
    private String name;
    private String physicalPossession;
    private String qty;
    private List<String> sampleNatureList;
}
