package sg.gov.moh.iais.egp.bsb.dto.register.afc;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.common.annotation.RfcAttributeDesc;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *@author YiMing
 * @version 2021/10/15 14:16
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"name", "available", "validated", "dependNodes", "validationResultDto"})
public class CertifyingTeamDto extends ValidatableNodeValue {
    @Data
    @NoArgsConstructor
    public static class CertifierTeamMember implements Serializable {
        private String memberEntityId;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.name")
        private String memberName;

        private String salutation;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.idType")
        private String idType;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.idNo")
        private String idNo;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.birthDate")
        private String birthDate;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.sex")
        private String sex;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.nationality")
        private String nationality;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.telNo")
        private String telNo;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.jobDesignation")
        private String jobDesignation;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.leadCertifier")
        private String leadCertifier;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.expertiseArea")
        private String expertiseArea;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.certBSL3Exp")
        private String certBSL3Exp;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.commBSL34Exp")
        private String commBSL34Exp;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.otherBSL34Exp")
        private String otherBSL34Exp;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.eduBackground")
        private String eduBackground;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.proActivities")
        private String proActivities;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.proRegAndCert")
        private String proRegAndCert;

        @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.facRelatedPub")
        private String facRelatedPub;
    }

    @RfcAttributeDesc(aliasName = "iais.bsbfe.certifierTeamMember.addOrDelete")
    private final List<CertifierTeamMember> certifierTeamMemberList;

    private ValidationResultDto validationResultDto;

    public CertifyingTeamDto() {
        certifierTeamMemberList = new ArrayList<>();
        certifierTeamMemberList.add(new CertifierTeamMember());
    }

    @Override
    public boolean doValidation() {
//        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("cerRegFeignClient", "validateCertifierTeam", new Object[]{this});
//        return validationResultDto.isPass();
        return true;
    }

    public List<CertifierTeamMember> getCertifierTeamMemberList() {
        return certifierTeamMemberList;
    }

    public void clearCertifierTeamMemberList() {
        this.certifierTeamMemberList.clear();
    }


    public void addCertifierTeamMember(CertifierTeamMember certifierTeamMember){
        this.certifierTeamMemberList.add(certifierTeamMember);
    }

    @Override
    public String retrieveValidationResult() {
        if (this.validationResultDto == null) {
            throw new IllegalStateException("This DTO is not validated");
        }
        return this.validationResultDto.toErrorMsg();
    }



    //    ---------------------------- request -> object ----------------------------------------------
    private static final String SEPARATOR = "--v--";
    private static final String KEY_SECTION_IDXES = "sectionIdx";
    private static final String KEY_TEAM_MEMBER_NAME = "memberName";
    private static final String KEY_ID_TYPE = "idType";
    private static final String KEY_ID_NO = "idNo";
    private static final String KEY_BIRTH_OF_DATE = "birthDate";
    private static final String KEY_SEX = "sex";
    private static final String KEY_NATIONALITY= "nationality";
    private static final String KEY_TEL_NO = "telNo";
    private static final String KEY_JOB_DESIGNATION = "jobDesignation";
    private static final String KEY_LEADER_CERTIFIER = "leadCertifier";
    private static final String KEY_AREA_OF_EXPERTISE = "expertiseArea";
    private static final String KEY_CERTIFIER_BSL_3_EXPERIENCE = "certBSL3Exp";
    private static final String KEY_COMMON_BSL_3_4_EXPERIENCE= "commBSL34Exp";
    private static final String KEY_OTHER_BSL_3_4_EXPERIENCE = "otherBSL34Exp";
    private static final String KEY_EDUCATION_BACKGROUND = "eduBackground";
    private static final String KEY_PRO_ACTIVITIES = "proActivities";
    private static final String KEY_PRO_REG_AND_CERTIFIER = "proRegAndCert";
    private static final String KEY_FAC_RELATED_PUB = "facRelatedPub";


    public void reqObjMapping(HttpServletRequest request) {
//        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
//        clearCertifierTeamMemberList();
//        String[] idxArr = idxes.trim().split(" +");
//        for (String idx : idxArr) {
//            CertifierTeamMember cerTeamMember = new CertifierTeamMember();
//            cerTeamMember.setMemberName(ParamUtil.getString(request, KEY_TEAM_MEMBER_NAME+ SEPARATOR + idx));
//            cerTeamMember.setIdType(ParamUtil.getString(request, KEY_ID_TYPE+ SEPARATOR + idx));
//            cerTeamMember.setIdNo(ParamUtil.getString(request, KEY_ID_NO+ SEPARATOR + idx));
//            cerTeamMember.setBirthDate(ParamUtil.getString(request, KEY_BIRTH_OF_DATE+ SEPARATOR + idx));
//            cerTeamMember.setSex(ParamUtil.getString(request, KEY_SEX+ SEPARATOR + idx));
//            cerTeamMember.setNationality(ParamUtil.getString(request, KEY_NATIONALITY+ SEPARATOR + idx));
//            cerTeamMember.setTelNo(ParamUtil.getString(request, KEY_TEL_NO+ SEPARATOR + idx));
//            cerTeamMember.setJobDesignation(ParamUtil.getString(request, KEY_JOB_DESIGNATION+ SEPARATOR + idx));
//            cerTeamMember.setLeadCertifier(ParamUtil.getString(request, KEY_LEADER_CERTIFIER+ SEPARATOR + idx));
//            cerTeamMember.setExpertiseArea(ParamUtil.getString(request, KEY_AREA_OF_EXPERTISE+ SEPARATOR + idx));
//            cerTeamMember.setCertBSL3Exp(ParamUtil.getString(request, KEY_CERTIFIER_BSL_3_EXPERIENCE+ SEPARATOR + idx));
//            cerTeamMember.setCommBSL34Exp(ParamUtil.getString(request, KEY_COMMON_BSL_3_4_EXPERIENCE+ SEPARATOR + idx));
//            cerTeamMember.setOtherBSL34Exp(ParamUtil.getString(request, KEY_OTHER_BSL_3_4_EXPERIENCE+ SEPARATOR + idx));
//            cerTeamMember.setEduBackground(ParamUtil.getString(request, KEY_EDUCATION_BACKGROUND+ SEPARATOR + idx));
//            cerTeamMember.setProActivities(ParamUtil.getString(request, KEY_PRO_ACTIVITIES+ SEPARATOR + idx));
//            cerTeamMember.setProRegAndCert(ParamUtil.getString(request, KEY_PRO_REG_AND_CERTIFIER+ SEPARATOR + idx));
//            cerTeamMember.setFacRelatedPub(ParamUtil.getString(request, KEY_FAC_RELATED_PUB+ SEPARATOR + idx));
//            addCertifierTeamMember(cerTeamMember);
//        }
    }
}
