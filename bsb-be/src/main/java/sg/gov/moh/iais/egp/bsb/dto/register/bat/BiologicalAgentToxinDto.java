package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class BiologicalAgentToxinDto extends BATInfoPageDto {
    private String activityEntityId;
    private String activityType;
}
