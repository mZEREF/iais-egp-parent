package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class FacilityProfileValidateDto implements Serializable {
    private List<FacilityProfileInfoValidateDto> infoList;
}
