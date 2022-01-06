package sg.gov.moh.iais.egp.bsb.dto.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InspectionFindingDto extends BaseEntityDto {
    private String id;
    private String applicationId;
    private String configId;
    private String sectionId;
    private String itemId;
    private String findingType;
    private String remarks;
    private LocalDate deadline;
    private String status;
}
