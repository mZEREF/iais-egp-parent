package sg.gov.moh.iais.egp.bsb.dto.audit;

import lombok.Data;

/**
 * @author Zhu Tangtang
 **/
@Data
public class SaveAuditDto {
    private String remarks;
    private String auditType;
    private String status;
    private String approvalId;
    private String processType;
}
