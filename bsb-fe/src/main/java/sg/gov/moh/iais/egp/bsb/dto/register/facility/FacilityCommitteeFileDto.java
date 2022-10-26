package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sg.gov.moh.iais.egp.bsb.util.mastercode.convert.MasterCodeConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A DTO used to read CSV & EXCEL file data.
 * All fields in this DTO MUST be the same as the original data DTO.
 * <p>
 * The JSON property of fields are the header name in the EXCEL file. So when we parse the CSV to generate
 * a JSON, it can be read into this DTO.
 * Then we convert this DTO to the DTO in the flow Node with JMapper.
 * <p>
 * This DTO has another usage. When we call validation for the counterpart DTO in Node, it will return an error map.
 * We use this class to convert key to the header name which is expected in the error list for user view.
 * So the 'Include.NON_NULL' feature must be enabled.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap
public class FacilityCommitteeFileDto implements Serializable {
    @JsonProperty("Salutation")
    @JsonAlias({"BC_SALUTATION", "salutation"})
    private String salutation;

    @JsonProperty("Name")
    @JsonAlias({"BC_NAME", "name"})
    private String name;

    @JsonProperty("Nationality")
    @JsonAlias({"BC_NATIONALITY", "nationality"})
    private String nationality;

    @JsonProperty("ID Type")
    @JsonAlias({"BC_ID_TYPE", "idType"})
    private String idType;

    @JsonProperty("ID Number")
    @JsonAlias({"BC_ID_NUMBER", "idNumber"})
    private String idNumber;

    @JsonProperty("Designation")
    @JsonAlias({"BC_DESIGNATION", "designation"})
    private String designation;

    @JsonProperty("Contact No.")
    @JsonAlias({"BC_CONTACT_NO", "contactNo"})
    private String contactNo;

    @JsonProperty("Email")
    @JsonAlias({"BC_EMAIL", "email"})
    private String email;

    @JsonProperty("Employment Start Date")
    @JsonAlias({"BC_EMPLOYMENT_START_DATE", "employmentStartDt"})
    private String employmentStartDt;

    @JsonProperty("Area of Work")
    @JsonAlias({"BC_AREA_OF_WORK", "workArea"})
    private String workArea;

    @JsonProperty("Role under Sixth Schedule")
    @JsonAlias({"BC_ROLE_UNDER_SIXTH_SCHEDULE", "role"})
    private String role;

    @JsonProperty("Is This Person an Employee of This Company")
    @JsonAlias({"BC_IS_PERSON_EMPLOYEE", "employee"})
    private String employee;

    @JsonProperty("Company Name")
    @JsonAlias({"BC_COMPANY_NAME", "externalCompName"})
    private String externalCompName;


    /** Convert DTOs for file to DTOs for processing, convert necessary fields to master codes. */
    public static List<FacilityCommitteeDto.BioSafetyCommitteePersonnel> toProcessingDtoList(List<FacilityCommitteeFileDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>(0);
        }
        MasterCodeConverter salutationConverter = MasterCodeHolder.SALUTATION.converter();
        MasterCodeConverter idTypeConverter = MasterCodeHolder.ID_TYPE.converter();
        MasterCodeConverter nationalityConverter = MasterCodeHolder.NATIONALITY.converter();
        MasterCodeConverter roleConverter = MasterCodeHolder.ROLE_UNDER_SIXTH_SCHEDULE.converter();

        JMapper<FacilityCommitteeDto.BioSafetyCommitteePersonnel, FacilityCommitteeFileDto> jMapper = new JMapper<>(FacilityCommitteeDto.BioSafetyCommitteePersonnel.class, FacilityCommitteeFileDto.class);
        List<FacilityCommitteeDto.BioSafetyCommitteePersonnel> list = new ArrayList<>(dtoList.size());
        for (FacilityCommitteeFileDto fileDto : dtoList) {
            fileDto.salutation = salutationConverter.value2Code(fileDto.salutation);
            fileDto.idType = idTypeConverter.value2Code(fileDto.getIdType());
            fileDto.nationality = nationalityConverter.value2Code(fileDto.getNationality());
            fileDto.role = roleConverter.desc2Code(fileDto.getRole());
            fileDto.employee = MasterCodeConstants.readUpperCaseYesNo(fileDto.employee);
            FacilityCommitteeDto.BioSafetyCommitteePersonnel dto = jMapper.getDestination(fileDto);
            list.add(dto);
        }
        return list;
    }

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
