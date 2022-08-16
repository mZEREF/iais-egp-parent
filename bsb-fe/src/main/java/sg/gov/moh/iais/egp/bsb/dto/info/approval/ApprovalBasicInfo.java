package sg.gov.moh.iais.egp.bsb.dto.info.approval;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalBasicInfo implements Serializable {
    private String id;
    private String approveNo;
    private String processType;
    private String status;
    private String approvalStartDt;
    private String approvalExpiryDt;
}
