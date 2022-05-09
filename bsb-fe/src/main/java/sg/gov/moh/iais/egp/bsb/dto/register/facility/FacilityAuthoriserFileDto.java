package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;

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


    public void value2MasterCode() {
        this.salutation = MasterCodeHolder.SALUTATION.value2Code(this.salutation);
        this.idType = MasterCodeHolder.ID_TYPE.value2Code(this.idType);
        this.nationality = MasterCodeHolder.NATIONALITY.value2Code(this.nationality);
        this.employee = MasterCodeConstants.readUpperCaseYesNo(this.employee);
    }

    public void code2Value() {
        this.salutation = MasterCodeHolder.SALUTATION.code2Value(this.salutation);
        this.idType = MasterCodeHolder.ID_TYPE.code2Value(this.idType);
        this.nationality = MasterCodeHolder.NATIONALITY.code2Value(this.nationality);
        this.employee = MasterCodeConstants.displayYesNo(this.employee);
    }


    public static List<FacilityAuthoriserDto.FacilityAuthorisedPersonnel> toProcessingDtoList(List<FacilityAuthoriserFileDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>(0);
        }
        JMapper<FacilityAuthoriserDto.FacilityAuthorisedPersonnel, FacilityAuthoriserFileDto> jMapper = new JMapper<>(FacilityAuthoriserDto.FacilityAuthorisedPersonnel.class, FacilityAuthoriserFileDto.class);
        List<FacilityAuthoriserDto.FacilityAuthorisedPersonnel> list = new ArrayList<>(dtoList.size());
        for (FacilityAuthoriserFileDto fileDto : dtoList) {
            fileDto.value2MasterCode();
            FacilityAuthoriserDto.FacilityAuthorisedPersonnel dto = jMapper.getDestination(fileDto);
            list.add(dto);
        }
        return list;
    }

    public static List<FacilityAuthoriserFileDto> toDisplayDtoList(List<FacilityAuthoriserDto.FacilityAuthorisedPersonnel> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>(0);
        }
        JMapper<FacilityAuthoriserFileDto, FacilityAuthoriserDto.FacilityAuthorisedPersonnel> jMapper = new JMapper<>(FacilityAuthoriserFileDto.class, FacilityAuthoriserDto.FacilityAuthorisedPersonnel.class);
        List<FacilityAuthoriserFileDto> list = new ArrayList<>(dtoList.size());
        for (FacilityAuthoriserDto.FacilityAuthorisedPersonnel personnel : dtoList) {
            FacilityAuthoriserFileDto dto = jMapper.getDestination(personnel);
            dto.code2Value();
            list.add(dto);
        }
        return list;
    }
}