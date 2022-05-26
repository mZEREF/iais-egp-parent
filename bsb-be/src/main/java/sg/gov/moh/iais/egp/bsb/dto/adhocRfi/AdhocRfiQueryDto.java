package sg.gov.moh.iais.egp.bsb.dto.adhocrfi;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.PagingAndSortingDto;

import java.io.Serializable;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AdhocRfiQueryDto extends PagingAndSortingDto implements Serializable {
    private String approvalId;
    private String approvalNo;
}
