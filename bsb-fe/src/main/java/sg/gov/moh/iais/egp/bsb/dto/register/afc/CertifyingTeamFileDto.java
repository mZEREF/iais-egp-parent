package sg.gov.moh.iais.egp.bsb.dto.register.afc;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.googlecode.jmapper.JMapper;
import com.googlecode.jmapper.annotations.JGlobalMap;
import lombok.Data;
import sg.gov.moh.iais.egp.bsb.util.mastercode.MasterCodeHolder;
import sg.gov.moh.iais.egp.bsb.util.mastercode.convert.MasterCodeConverter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JGlobalMap
public class CertifyingTeamFileDto implements Serializable {
    @JsonProperty("Salutation")
    @JsonAlias("salutation")
    private String salutation;

    @JsonProperty("Name")
    @JsonAlias("name")
    private String name;

    @JsonProperty("ID Type")
    @JsonAlias("idType")
    private String idType;

    @JsonProperty("ID Number")
    @JsonAlias("idNumber")
    private String idNumber;

    @JsonProperty("Date of Birth")
    @JsonAlias("entityBirthDate")
    private String entityBirthDate;

    @JsonProperty("Nationality")
    @JsonAlias("nationality")
    private String nationality;

    @JsonProperty("Contact No.")
    @JsonAlias("contactNo")
    private String contactNo;

    @JsonProperty("Job Designation")
    @JsonAlias("jobDesignation")
    private String jobDesignation;

    @JsonProperty("Lead Certifier")
    @JsonAlias("leadCertifier")
    private String leadCertifier;

    @JsonProperty("Role")
    @JsonAlias("role")
    private String role;

    @JsonProperty("Experience in certification of BSL-3/4 facility")
    @JsonAlias("certBSL3Exp")
    private String certBSL3Exp;

    @JsonProperty("Experience in commissioning of BSL-3/4 facility")
    @JsonAlias("commBSL34Exp")
    private String commBSL34Exp;

    @JsonProperty("Experience in other BSL-3/4 related activities")
    @JsonAlias("otherBSL34Exp")
    private String otherBSL34Exp;

    @JsonProperty("Highest level of education completed")
    @JsonAlias("highestEdu")
    private String highestEdu;

    @JsonProperty("Relevant professional registration and certificates")
    @JsonAlias("proCertifications")
    private String proCertifications;

    @JsonProperty("Other related achievements/activities")
    @JsonAlias("otherRelatedAchievement")
    private String otherRelatedAchievement;


    public void value2SpecialYN(){
        if("Yes".equals(this.leadCertifier)){
            this.leadCertifier = "Y";
        }else if("No".equals(this.leadCertifier)){
            this.leadCertifier = "N";
        }
    }


    /** Convert DTOs for file to DTOs for processing, convert necessary fields to master codes. */
    public static List<CertifyingTeamDto.CertifierTeamMember> toProcessingDtoList(List<CertifyingTeamFileDto> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>(0);
        }
        MasterCodeConverter idTypeConverter = MasterCodeHolder.ID_TYPE.converter();
        MasterCodeConverter nationalityConverter = MasterCodeHolder.NATIONALITY.converter();
        MasterCodeConverter roleConverter = MasterCodeHolder.AFC_TEAM_ROLE.converter();

        JMapper<CertifyingTeamDto.CertifierTeamMember, CertifyingTeamFileDto> jMapper = new JMapper<>(CertifyingTeamDto.CertifierTeamMember.class, CertifyingTeamFileDto.class);
        List<CertifyingTeamDto.CertifierTeamMember> list = new ArrayList<>(dtoList.size());
        for (CertifyingTeamFileDto fileDto : dtoList) {
            fileDto.value2SpecialYN();
            fileDto.idType = idTypeConverter.value2Code(fileDto.getIdType());
            fileDto.nationality = nationalityConverter.value2Code(fileDto.getNationality());
            fileDto.role = roleConverter.value2Code(fileDto.role);
            CertifyingTeamDto.CertifierTeamMember dto = jMapper.getDestination(fileDto);
            list.add(dto);
        }
        return list;
    }

    /** Convert DTOs for processing to display, convert master codes to display descriptions */
    public static List<CertifyingTeamFileDto> toDisplayDtoList(List<CertifyingTeamDto.CertifierTeamMember> dtoList) {
        if (dtoList == null || dtoList.isEmpty()) {
            return new ArrayList<>(0);
        }
        MasterCodeConverter idTypeConverter = MasterCodeHolder.ID_TYPE.converter();
        MasterCodeConverter nationalityConverter = MasterCodeHolder.NATIONALITY.converter();
        MasterCodeConverter roleConverter = MasterCodeHolder.AFC_TEAM_ROLE.converter();

        JMapper<CertifyingTeamFileDto, CertifyingTeamDto.CertifierTeamMember> jMapper = new JMapper<>(CertifyingTeamFileDto.class, CertifyingTeamDto.CertifierTeamMember.class);
        List<CertifyingTeamFileDto> list = new ArrayList<>(dtoList.size());
        for (CertifyingTeamDto.CertifierTeamMember teamMember : dtoList) {
            CertifyingTeamFileDto dto = jMapper.getDestination(teamMember);
            dto.idType = idTypeConverter.code2Value(dto.idType);
            dto.nationality = nationalityConverter.code2Value(dto.nationality);
            dto.role = roleConverter.code2Value(dto.role);
            list.add(dto);
        }
        return list;
    }
}
