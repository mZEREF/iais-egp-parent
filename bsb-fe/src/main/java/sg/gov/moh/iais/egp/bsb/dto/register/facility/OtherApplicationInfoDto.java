package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherApplicationInfoDto extends ValidatableNodeValue {
    private String facOpDeInformedResponsibilities;
    private String facCommitteeInformedResponsibilities;
    private String bioRiskManagementDeclare;
    private String infoAuthenticatedDeclare;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public static OtherApplicationInfoDto getAllCheckedInstance() {
        OtherApplicationInfoDto dto = new OtherApplicationInfoDto();
        dto.setFacOpDeInformedResponsibilities(MasterCodeConstants.YES);
        dto.setFacCommitteeInformedResponsibilities(MasterCodeConstants.YES);
        dto.setBioRiskManagementDeclare(MasterCodeConstants.YES);
        dto.setInfoAuthenticatedDeclare(MasterCodeConstants.YES);
        return dto;
    }


    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityOtherAppInfo", new Object[]{this});
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

    public String getFacOpDeInformedResponsibilities() {
        return facOpDeInformedResponsibilities;
    }

    public void setFacOpDeInformedResponsibilities(String facOpDeInformedResponsibilities) {
        this.facOpDeInformedResponsibilities = facOpDeInformedResponsibilities;
    }

    public String getFacCommitteeInformedResponsibilities() {
        return facCommitteeInformedResponsibilities;
    }

    public void setFacCommitteeInformedResponsibilities(String facCommitteeInformedResponsibilities) {
        this.facCommitteeInformedResponsibilities = facCommitteeInformedResponsibilities;
    }

    public String getBioRiskManagementDeclare() {
        return bioRiskManagementDeclare;
    }

    public void setBioRiskManagementDeclare(String bioRiskManagementDeclare) {
        this.bioRiskManagementDeclare = bioRiskManagementDeclare;
    }

    public String getInfoAuthenticatedDeclare() {
        return infoAuthenticatedDeclare;
    }

    public void setInfoAuthenticatedDeclare(String infoAuthenticatedDeclare) {
        this.infoAuthenticatedDeclare = infoAuthenticatedDeclare;
    }


//    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_INFORM_OPERATOR = "facOpDeInformedResponsibilities";
    private static final String KEY_INFORM_COMMITTEE = "facCommitteeInformedResponsibilities";
    private static final String KEY_DECLARE_RISK_MANAGEMENT = "bioRiskManagementDeclare";
    private static final String KEY_DECLARE_TRUE = "infoAuthenticatedDeclare";

    public void reqObjMapping(HttpServletRequest request) {
        setFacOpDeInformedResponsibilities(ParamUtil.getString(request, KEY_INFORM_OPERATOR));
        setFacCommitteeInformedResponsibilities(ParamUtil.getString(request, KEY_INFORM_COMMITTEE));
        setBioRiskManagementDeclare(ParamUtil.getString(request, KEY_DECLARE_RISK_MANAGEMENT));
        setInfoAuthenticatedDeclare(ParamUtil.getString(request, KEY_DECLARE_TRUE));
    }
}
