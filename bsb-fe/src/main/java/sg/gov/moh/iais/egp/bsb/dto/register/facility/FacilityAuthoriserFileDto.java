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


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap
public class FacilityAuthoriserFileDto implements Serializable {
    @JsonProperty("Salutation")
    @JsonAlias({"AUTHORISED_PERSONNEL_SALUTATION", "salutation"})
    private String salutation;

    @JsonProperty("Name")
    @JsonAlias({"AUTHORISED_PERSONNEL_NAME", "name"})
    private String name;

    @JsonProperty("Nationality")
    @JsonAlias({"AUTHORISED_PERSONNEL_NATIONALITY", "nationality"})
    private String nationality;

    @JsonProperty("ID Type")
    @JsonAlias({"AUTHORISED_PERSONNEL_ID_TYPE", "idType"})
    private String idType;

    @JsonProperty("ID Number")
    @JsonAlias({"AUTHORISED_PERSONNEL_ID_NUMBER", "idNumber"})
    private String idNumber;

    @JsonProperty("Designation")
    @JsonAlias({"AUTHORISED_PERSONNEL_DESIGNATION", "designation"})
    private String designation;

    @JsonProperty("Contact No.")
    @JsonAlias({"AUTHORISED_PERSONNEL_CONTACT_NO", "contactNo"})
    private String contactNo;

    @JsonProperty("Email")
    @JsonAlias({"AUTHORISED_PERSONNEL_EMAIL", "email"})
    private String email;

    @JsonProperty("Employment Start Date")
    @JsonAlias({"AUTHORISED_PERSONNEL_EMPLOYMENT_START_DATE", "employmentStartDt"})
    private String employmentStartDt;

    @JsonProperty("Area of Work")
    @JsonAlias({"AUTHORISED_PERSONNEL_AREA_OF_WORK", "workArea"})
    private String workArea;

    @JsonProperty("Security Clearance Date")
    @JsonAlias({"AUTHORISED_PERSONNEL_SECURITY_CLEARANCE_DATE", "securityClearanceDt"})
    private String securityClearanceDt;

    @JsonProperty("Is This Person an Employee of This Company")
    @JsonAlias({"AUTHORISED_PERSONNEL_IS_PERSON_EMPLOYEE", "employee"})
    private String employee;

    @JsonProperty("Company Name")
    @JsonAlias({"AUTHORISED_PERSONNEL_COMPANY_NAME", "externalCompName"})
    private String externalCompName;


    public static List<FacilityAuthoriserDto.FacilityAuthorisedPersonnel> toProcessingDtoList(List<FacilityAuthoriserFileDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>(0);
        }
        MasterCodeConverter salutationConverter = MasterCodeHolder.SALUTATION.converter();
        MasterCodeConverter idTypeConverter = MasterCodeHolder.ID_TYPE.converter();
        MasterCodeConverter nationalityConverter = MasterCodeHolder.NATIONALITY.converter();

        JMapper<FacilityAuthoriserDto.FacilityAuthorisedPersonnel, FacilityAuthoriserFileDto> jMapper = new JMapper<>(FacilityAuthoriserDto.FacilityAuthorisedPersonnel.class, FacilityAuthoriserFileDto.class);
        List<FacilityAuthoriserDto.FacilityAuthorisedPersonnel> list = new ArrayList<>(dtoList.size());
        for (FacilityAuthoriserFileDto fileDto : dtoList) {
            fileDto.salutation = salutationConverter.value2Code(fileDto.salutation);
            fileDto.idType = idTypeConverter.value2Code(fileDto.getIdType());
            fileDto.nationality = nationalityConverter.value2Code(fileDto.getNationality());
            fileDto.employee = MasterCodeConstants.readUpperCaseYesNo(fileDto.employee);
            FacilityAuthoriserDto.FacilityAuthorisedPersonnel dto = jMapper.getDestination(fileDto);
            list.add(dto);
        }
        return list;
    }

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