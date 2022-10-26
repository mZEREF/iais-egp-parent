package sg.gov.moh.iais.egp.bsb.dto.inspection;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InsFindingDisplayDto implements Serializable {
    private String section;
    private String item;
    private String findingType;
    private String remarks;
    private String deadline;
}
