package sg.gov.moh.iais.egp.bsb.dto.approvalApp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;

import java.io.Serializable;

/**
 * @author : LiRan
 * @date : 2021/10/21
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalProfileDto extends ValidatableNodeValue {
    @Data
    @NoArgsConstructor
    public static class BATInfo implements Serializable {
        private String batName;
    }
}
