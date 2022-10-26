package sg.gov.moh.iais.egp.bsb.dto.audit;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;

import java.io.Serializable;

/**
 * @author Zhu Tangtang
 * @date 2021/8/31 15:35
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuditQueryDto extends PagingAndSortingDto implements Serializable {
    private String facilityName;

    private String activeType;

    private String facilityClassification;

    private String auditType;

    private String from;

    public void clearAllFields() {
        facilityName = "";
        activeType = "";
        facilityClassification = "";
        auditType = "";
        from = "";
    }
}
