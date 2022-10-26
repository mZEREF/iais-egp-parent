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
public class FacilityCommitteeFileDto implements Serializable {

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
    private String role;
    private String employee;
    private String externalCompName;

    /** Convert DTOs for processing to display, convert master codes to display descriptions */
    public static List<FacilityCommitteeFileDto> toDisplayDtoList(List<FacilityCommitteeDto.BioSafetyCommitteePersonnel> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>(0);
        }
        MasterCodeConverter salutationConverter = MasterCodeHolder.SALUTATION.converter();
        MasterCodeConverter idTypeConverter = MasterCodeHolder.ID_TYPE.converter();
        MasterCodeConverter nationalityConverter = MasterCodeHolder.NATIONALITY.converter();
        MasterCodeConverter roleConverter = MasterCodeHolder.ROLE_UNDER_SIXTH_SCHEDULE.converter();

        JMapper<FacilityCommitteeFileDto, FacilityCommitteeDto.BioSafetyCommitteePersonnel> jMapper = new JMapper<>(FacilityCommitteeFileDto.class, FacilityCommitteeDto.BioSafetyCommitteePersonnel.class);
        List<FacilityCommitteeFileDto> list = new ArrayList<>(dtoList.size());
        for (FacilityCommitteeDto.BioSafetyCommitteePersonnel personnel : dtoList) {
            FacilityCommitteeFileDto dto = jMapper.getDestination(personnel);
            dto.salutation = salutationConverter.code2Value(dto.salutation);
            dto.idType = idTypeConverter.code2Value(dto.idType);
            dto.nationality = nationalityConverter.code2Value(dto.nationality);
            dto.role = roleConverter.code2Value(dto.role);
            dto.employee = MasterCodeConstants.displayYesNo(dto.employee);
            list.add(dto);
        }
        return list;
    }
}
