package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Zhu Tangtang
 * @date 2021/7/27 9:20
 */
@Getter
@Setter
public class ProcessingHistory implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userName;
    private String workingGroup;
    private String statusUpdate;
    private String remarks;
    private String lastUpdated;
}
