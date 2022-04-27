package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import sg.gov.moh.iais.egp.bsb.dto.chklst.ChklstItemAnswerDto;

import java.util.ArrayList;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InspectionChecklistDto extends BaseEntityDto {

    private static final long serialVersionUID = 1L;

    private String id;
    private String applicationId;
    private String chkLstConfigId;
    private String userId;
    private ArrayList<ChklstItemAnswerDto> answer;
    private Integer version;
    private String status;

}
