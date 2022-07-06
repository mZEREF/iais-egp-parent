package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto;

import java.io.Serializable;
import java.util.List;


@Data
public class FacAuthorisedDto implements Serializable {
    private List<FacilityAuthoriserDto> facAuthorisedDtoList;
}
