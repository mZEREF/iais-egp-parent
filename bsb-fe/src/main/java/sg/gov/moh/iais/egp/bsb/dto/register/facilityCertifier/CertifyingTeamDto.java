package sg.gov.moh.iais.egp.bsb.dto.register.facilityCertifier;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.node.Node;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.FacCertifierRegisterConstants.NODE_NAME_ORG_CERTIFYING_TEAM;


/**
 * AUTHOR: YiMing
 * DATE:2021/9/26 15:13
 * DESCRIPTION: TODO
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties({"name", "available", "validated", "dependNodes", "validationResultDto"})
public class CertifyingTeamDto extends Node {
    @Data
    @NoArgsConstructor
    public static class CertifierTeamMember implements Serializable {
        private String memberName;
        private String idType;
        private String idNo;
        private String birthDate;
        private String sex;
        private String nationality;
        private String telNo;
        private String jobDesignation;
        private String leadCertifier;
        private String expertiseArea;
        private String certBSL3Exp;
        private String commBSL34Exp;
        private String otherBSL34Exp;
        private String eduBackground;
        private String proActivities;
        private String proRegAndCert;
        private String facRelatedPub;
    }

    private final List<CertifierTeamMember> certifierTeamMemberList;
    private ValidationResultDto validationResultDto;

    public CertifyingTeamDto(String name, Node[] dependNodes) {
        super(name, dependNodes);
        certifierTeamMemberList = new ArrayList<>();
        certifierTeamMemberList.add(new CertifierTeamMember());
    }

    public static CertifyingTeamDto getInstance(Node[] dependNodes) {
        return new CertifyingTeamDto(NODE_NAME_ORG_CERTIFYING_TEAM, dependNodes);
    }
    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityProfile", new Object[]{this});
        return validationResultDto.isPass();
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

    @Override
    public void needValidation() {
        super.needValidation();
        this.validationResultDto = null;
    }


    //    ---------------------------- request -> object ----------------------------------------------
    private static final String SEPARATOR = "--v--";
    private static final String KEY_SECTION_AMT = "sectionAmt";
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
        int amt = ParamUtil.getInt(request, KEY_SECTION_AMT);
        clearCertifierTeamMemberList();
        for (int i = 0; i < amt; i++) {
            CertifierTeamMember cerTeamMember = new CertifierTeamMember();
            cerTeamMember.setMemberName(ParamUtil.getString(request, KEY_TEAM_MEMBER_NAME+ SEPARATOR + i));
            cerTeamMember.setIdType(ParamUtil.getString(request, KEY_ID_TYPE+ SEPARATOR + i));
            cerTeamMember.setIdNo(ParamUtil.getString(request, KEY_ID_NO+ SEPARATOR + i));
            cerTeamMember.setBirthDate(ParamUtil.getString(request, KEY_BIRTH_OF_DATE+ SEPARATOR + i));
            cerTeamMember.setSex(ParamUtil.getString(request, KEY_SEX+ SEPARATOR + i));
            cerTeamMember.setNationality(ParamUtil.getString(request, KEY_NATIONALITY+ SEPARATOR + i));
            cerTeamMember.setTelNo(ParamUtil.getString(request, KEY_TEL_NO+ SEPARATOR + i));
            cerTeamMember.setJobDesignation(ParamUtil.getString(request, KEY_JOB_DESIGNATION+ SEPARATOR + i));
            cerTeamMember.setLeadCertifier(ParamUtil.getString(request, KEY_LEADER_CERTIFIER+ SEPARATOR + i));
            cerTeamMember.setExpertiseArea(ParamUtil.getString(request, KEY_AREA_OF_EXPERTISE + i));
            cerTeamMember.setCertBSL3Exp(ParamUtil.getString(request, KEY_CERTIFIER_BSL_3_EXPERIENCE+ SEPARATOR + i));
            cerTeamMember.setCommBSL34Exp(ParamUtil.getString(request, KEY_COMMON_BSL_3_4_EXPERIENCE+ SEPARATOR + i));
            cerTeamMember.setOtherBSL34Exp(ParamUtil.getString(request, KEY_OTHER_BSL_3_4_EXPERIENCE+ SEPARATOR + i));
            cerTeamMember.setEduBackground(ParamUtil.getString(request, KEY_EDUCATION_BACKGROUND+ SEPARATOR + i));
            cerTeamMember.setProActivities(ParamUtil.getString(request, KEY_PRO_ACTIVITIES+ SEPARATOR + i));
            cerTeamMember.setProRegAndCert(ParamUtil.getString(request, KEY_PRO_REG_AND_CERTIFIER+ SEPARATOR + i));
            cerTeamMember.setFacRelatedPub(ParamUtil.getString(request, KEY_FAC_RELATED_PUB+ SEPARATOR + i));
            addCertifierTeamMember(cerTeamMember);
        }
    }
}
