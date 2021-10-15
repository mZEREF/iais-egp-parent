package sg.gov.moh.iais.egp.bsb.dto.revocation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;

import java.io.Serializable;

/**
 * @author Zhu Tangtang
 * @date 2021/8/31 15:35
 */
@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ApprovalQueryDto extends PagingAndSortingDto implements Serializable {
    private String facilityName;

    private String activeType;

    private String facilityClassification;

    /**
     * The empty values are intended for the select options since we use empty string for 'All' meaning
     */
    public void clearAllFields() {
        facilityName = "";
        activeType = "";
        facilityClassification = "";
    }
}
