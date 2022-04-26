package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.ArrayList;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.chklst.ChklstItemAnswerDto;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InspectionChecklistDto extends BaseEntityDto {
    private String id;
    private String applicationId;
    private String chkLstConfigId;
    private ArrayList<ChklstItemAnswerDto> answer;
    private Integer version;
    private String status;
}
