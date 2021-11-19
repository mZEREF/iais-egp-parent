package sg.gov.moh.iais.egp.bsb.dto.revocation;

import lombok.Data;
import java.util.Date;

/**
 * @author Zhu Tangtang
 * @date 2021/8/13 14:46
 */
@Data
public class SubmitRevokeDto {
    private String approvalId;
    //application
    private String appId;
    private String processType;
    private String appType;
    private String status;
    private Date applicationDt;
    private String applicationNo;
    //applicationMisc
    private String reason;
    private String reasonContent;
    private String remarks;
    //
    private String loginUser;
}
