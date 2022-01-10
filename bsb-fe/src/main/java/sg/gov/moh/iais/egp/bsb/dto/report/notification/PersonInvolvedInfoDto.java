package sg.gov.moh.iais.egp.bsb.dto.report.notification;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author YiMing
 * @version 2021/12/2 13:30
 **/
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonInvolvedInfoDto extends ValidatableNodeValue {

    @Data
    @NoArgsConstructor
    public static class PersonInvolved implements Serializable {
        private String involvedPersonEntityId;
        private String name;
        private String gender;
        private String telNo;
        private String designation;
        private String personnelInjured;
        private String personnelInvolvement;
        private String involvementDesc;
        private String medicalPerson;
        private String practitionerName;
        private String hospitalName;
        private String medicalDesc;
        private String medicalFollowup;
    }

    private List<PersonInvolved> incidentPersons;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public PersonInvolvedInfoDto() {
        incidentPersons = new ArrayList<>();
        incidentPersons.add(new PersonInvolved());
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("incidentFeignClient", "validatePersonInvolvedInfo", new Object[]{this});
        return validationResultDto.isPass();
    }

    @Override
    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }

    @Override
    public void clearValidationResult() {
        this.validationResultDto = null;
    }

    public List<PersonInvolved> getIncidentPersons() {
        return new ArrayList<>(incidentPersons);
    }

    public void setIncidentPersons(List<PersonInvolved> personInvolvedList) {
        this.incidentPersons = new ArrayList<>(personInvolvedList);
    }

    public void clearIncidentPersons(){
        incidentPersons.clear();
    }

    public void addIncidentPersons(PersonInvolved personInvolved){
        incidentPersons.add(personInvolved);
    }


    private static final String SEPARATOR = "--v--";
    private static final String KEY_SECTION_IDXES = "sectionIdx";
    private static final String KEY_INVOLVED_PERSON_NAME = "name";
    private static final String KEY_INVOLVED_PERSON_GENDER = "gender";
    private static final String KEY_INVOLVED_PERSON_TEL_NO = "telNo";
    private static final String KEY_INVOLVED_PERSON_DESIGNATION = "designation";
    private static final String KEY_PERSONNEL_INJURED = "personnelInjured";
    private static final String KEY_PERSONNEL_INVOLVEMENT = "personnelInvolvement";
    private static final String KEY_INVOLVEMENT_DESCRIPTION = "involvementDesc";
    private static final String KEY_PERSON_SENT_FOR_MEDICAL = "medicalPerson";
    private static final String KEY_MEDICAL_PRACTITIONER_NAME = "practitionerName";
    private static final String KEY_HOSPITAL_NAME = "hospitalName";
    private static final String KEY_MEDICAL_DESCRIPTION = "medicalDesc";
    private static final String KEY_MEDICAL_FOLLOW_UP = "medicalFollowup";
    public void reqObjMapping(HttpServletRequest request){
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        clearIncidentPersons();
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
            PersonInvolved personInvolved = new PersonInvolved();
            personInvolved.setName(ParamUtil.getString(request,KEY_INVOLVED_PERSON_NAME+SEPARATOR+idx));
            personInvolved.setGender(ParamUtil.getString(request,KEY_INVOLVED_PERSON_GENDER+SEPARATOR+idx));
            personInvolved.setDesignation(ParamUtil.getString(request,KEY_INVOLVED_PERSON_DESIGNATION+SEPARATOR+idx));
            personInvolved.setTelNo(ParamUtil.getString(request,KEY_INVOLVED_PERSON_TEL_NO+SEPARATOR+idx));
            personInvolved.setPersonnelInjured(ParamUtil.getString(request,KEY_PERSONNEL_INJURED+SEPARATOR+idx));
            personInvolved.setPersonnelInvolvement(ParamUtil.getString(request,KEY_PERSONNEL_INVOLVEMENT+SEPARATOR+idx));
            personInvolved.setInvolvementDesc(ParamUtil.getString(request,KEY_INVOLVEMENT_DESCRIPTION+SEPARATOR+idx));
            personInvolved.setMedicalPerson(ParamUtil.getString(request,KEY_PERSON_SENT_FOR_MEDICAL+SEPARATOR+idx));
            personInvolved.setPractitionerName(ParamUtil.getString(request,KEY_MEDICAL_PRACTITIONER_NAME+SEPARATOR+idx));
            personInvolved.setHospitalName(ParamUtil.getString(request,KEY_HOSPITAL_NAME+SEPARATOR+idx));
            personInvolved.setMedicalDesc(ParamUtil.getString(request,KEY_MEDICAL_DESCRIPTION+SEPARATOR+idx));
            personInvolved.setMedicalFollowup(ParamUtil.getString(request,KEY_MEDICAL_FOLLOW_UP+SEPARATOR+idx));
            addIncidentPersons(personInvolved);
        }
    }

}
