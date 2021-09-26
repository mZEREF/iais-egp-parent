package sg.gov.moh.iais.egp.bsb.dto.revocation;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;

import java.io.Serializable;

/**
 * @author Zhu Tangtang
 * @date 2021/9/26 16:21
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FacilityQueryDto extends PagingAndSortingDto implements Serializable {
    private String facilityName;

    private String activeType;

    private String facilityClassification;


    public void clearAllFields() {
        facilityName = "";
        activeType = "";
        facilityClassification = "";
    }
}
