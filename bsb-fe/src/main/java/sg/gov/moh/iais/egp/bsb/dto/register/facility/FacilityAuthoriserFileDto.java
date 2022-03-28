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


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap
public class FacilityAuthoriserFileDto implements Serializable {
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

    @JsonProperty("Employment Period")
    @JsonAlias("employmentPeriod")
    private String employmentPeriod;

    @JsonProperty("Work Area")
    @JsonAlias("workArea")
    private String workArea;

    @JsonProperty("Security Clearance Date")
    @JsonAlias("securityClearanceDt")
    private String securityClearanceDt;



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