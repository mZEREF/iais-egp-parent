package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sg.gov.moh.iais.egp.bsb.util.mastercode.convert.MasterCodeConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JGlobalMap
public class FacilityAuthoriserFileDto implements Serializable {
    private String salutation;
    private String name;
    private String nationality;
    private String idType;
    private String idNumber;
    private String designation;
    private String contactNo;
    private String email;
    private String employmentStartDt;
    private String workArea;
    private String securityClearanceDt;
    private String employee;
    private String externalCompName;

    public static List<FacilityAuthoriserFileDto> toDisplayDtoList(List<FacilityAuthoriserDto.FacilityAuthorisedPersonnel> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>(0);
        }
        MasterCodeConverter salutationConverter = MasterCodeHolder.SALUTATION.converter();
        MasterCodeConverter idTypeConverter = MasterCodeHolder.ID_TYPE.converter();
        MasterCodeConverter nationalityConverter = MasterCodeHolder.NATIONALITY.converter();

        JMapper<FacilityAuthoriserFileDto, FacilityAuthoriserDto.FacilityAuthorisedPersonnel> jMapper = new JMapper<>(FacilityAuthoriserFileDto.class, FacilityAuthoriserDto.FacilityAuthorisedPersonnel.class);
        List<FacilityAuthoriserFileDto> list = new ArrayList<>(dtoList.size());
        for (FacilityAuthoriserDto.FacilityAuthorisedPersonnel personnel : dtoList) {
            FacilityAuthoriserFileDto dto = jMapper.getDestination(personnel);
            dto.salutation = salutationConverter.code2Value(dto.salutation);
            dto.idType = idTypeConverter.code2Value(dto.idType);
            dto.nationality = nationalityConverter.code2Value(dto.nationality);
            dto.employee = MasterCodeConstants.displayYesNo(dto.employee);
            list.add(dto);
        }
        return list;
    }
}
