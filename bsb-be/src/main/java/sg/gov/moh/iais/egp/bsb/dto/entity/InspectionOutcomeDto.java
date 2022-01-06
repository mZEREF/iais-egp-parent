package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InspectionOutcomeDto extends BaseEntityDto {
    private String id;
    private String applicationId;
    private String status;
    private String deficiency;
    private String followUpRequired;
    private String outcome;
    private String remarks;
}
