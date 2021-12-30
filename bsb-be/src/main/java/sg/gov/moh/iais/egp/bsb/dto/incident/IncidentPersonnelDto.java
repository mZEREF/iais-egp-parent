package sg.gov.moh.iais.egp.bsb.dto.incident;


import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.CommiteeDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.FacilityAdminDto;
import sg.gov.moh.iais.egp.bsb.dto.incident.entity.PersonnelAuthorisedDto;

import java.util.List;


/**
 * @author YiMing
 * @version 2021/12/27 17:34
 **/
@Data
public class IncidentPersonnelDto {
    private List<FacilityAdminDto> facilityAdminList;
    private List<CommiteeDto> commiteeDtoList;
    private List<PersonnelAuthorisedDto> personnelAuthorisedList;

}
