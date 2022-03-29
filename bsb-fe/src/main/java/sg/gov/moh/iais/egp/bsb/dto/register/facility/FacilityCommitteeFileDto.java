package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;

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
    @JsonAlias("salutation")
    private String salutation;

    @JsonProperty("Name")
    @JsonAlias("name")
    private String name;

    @JsonProperty("Nationality")
    @JsonAlias("nationality")
    private String nationality;

    @JsonProperty("ID Type")
    @JsonAlias("idType")
    private String idType;

    @JsonProperty("ID Number")
    @JsonAlias("idNumber")
    private String idNumber;

    @JsonProperty("Designation")
    @JsonAlias("designation")
    private String designation;

    @JsonProperty("Contact No.")
    @JsonAlias("contactNo")
    private String contactNo;

    @JsonProperty("Email")
    @JsonAlias("email")
    private String email;

    @JsonProperty("Employment Start Date")
    @JsonAlias("employmentStartDt")
    private String employmentStartDt;

    @JsonProperty("Area of Expertise")
    @JsonAlias("expertiseArea")
    private String expertiseArea;

    @JsonProperty("Role")
    @JsonAlias("role")
    private String role;

    @JsonProperty("Is This Person an Employee of This Company")
    @JsonAlias("employee")
    private String employee;

    @JsonProperty("Company Name")
    @JsonAlias("externalCompName")
    private String externalCompName;


    public void value2MasterCode() {
        this.salutation = MasterCodeHolder.SALUTATION.value2Code(this.salutation);
        this.nationality = MasterCodeHolder.NATIONALITY.value2Code(this.nationality);
        this.idType = MasterCodeHolder.ID_TYPE.value2Code(this.idType);
    }

    public void code2Value() {
        this.salutation = MasterCodeHolder.SALUTATION.code2Value(this.salutation);
        this.nationality = MasterCodeHolder.NATIONALITY.code2Value(this.nationality);
        this.idType = MasterCodeHolder.ID_TYPE.code2Value(this.idType);
    }



    /** Convert DTOs for file to DTOs for processing, convert necessary fields to master codes. */
    public static List<FacilityCommitteeDto.BioSafetyCommitteePersonnel> toProcessingDtoList(List<FacilityCommitteeFileDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>(0);
        }
        JMapper<FacilityCommitteeDto.BioSafetyCommitteePersonnel, FacilityCommitteeFileDto> jMapper = new JMapper<>(FacilityCommitteeDto.BioSafetyCommitteePersonnel.class, FacilityCommitteeFileDto.class);
        List<FacilityCommitteeDto.BioSafetyCommitteePersonnel> list = new ArrayList<>(dtoList.size());
        for (FacilityCommitteeFileDto fileDto : dtoList) {
            fileDto.value2MasterCode();
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
        JMapper<FacilityCommitteeFileDto, FacilityCommitteeDto.BioSafetyCommitteePersonnel> jMapper = new JMapper<>(FacilityCommitteeFileDto.class, FacilityCommitteeDto.BioSafetyCommitteePersonnel.class);
        List<FacilityCommitteeFileDto> list = new ArrayList<>(dtoList.size());
        for (FacilityCommitteeDto.BioSafetyCommitteePersonnel personnel : dtoList) {
            FacilityCommitteeFileDto dto = jMapper.getDestination(personnel);
            dto.code2Value();
            list.add(dto);
        }
        return list;
    }
}
