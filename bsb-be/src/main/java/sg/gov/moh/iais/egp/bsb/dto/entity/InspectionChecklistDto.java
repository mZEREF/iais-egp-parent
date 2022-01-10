package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InspectionChecklistDto extends BaseEntityDto {
    private String id;
    private String applicationId;
    private String chkLstConfigId;
    private String answer;
    private Integer version;
    private String status;
}
