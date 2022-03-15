package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sg.gov.moh.iais.egp.common.annotation.RfcAttributeDesc;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacilitySelectionDto extends ValidatableNodeValue {
    private String draftAppNo;
    private String facClassification;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facProfile.type")
    private List<String> activityTypes;

    @JsonIgnore
    private ValidationResultDto validationResultDto;


    public FacilitySelectionDto() {
        activityTypes = new ArrayList<>();
    }

    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilitySelection", new Object[]{this});
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



    public void addActivityType(String activityType) {
        activityTypes.add(activityType);
    }

    public void addActivityTypes(String[] activityTypes) {
        if (activityTypes != null && activityTypes.length > 0) {
            this.activityTypes.addAll(Arrays.asList(activityTypes));
        }
    }

    public void replaceActivityTypes(String[] activityTypes) {
        clearActivityTypes();
        addActivityTypes(activityTypes);
    }

    public void clearActivityTypes() {
        this.activityTypes.clear();
    }

    public String getDraftAppNo() {
        return draftAppNo;
    }

    public void setDraftAppNo(String draftAppNo) {
        this.draftAppNo = draftAppNo;
    }

    public String getFacClassification() {
        return facClassification;
    }

    public void setFacClassification(String facClassification) {
        this.facClassification = facClassification;
    }

    public List<String> getActivityTypes() {
        return new ArrayList<>(activityTypes);
    }

    public void setActivityTypes(List<String> activityTypes) {
        this.activityTypes = new ArrayList<>(activityTypes);
    }

    //    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_SELECTION_CLASSIFICATION = "facClassification";
    private static final String KEY_FAC_AVTVI_TYPE_BLS3 = "bsl3ActviTypes";
    private static final String KEY_FAC_AVTVI_TYPE_BLS4 = "bsl4ActviTypes";
    private static final String KEY_FAC_AVTVI_TYPE_UF = "ufActviTypes";
    private static final String KEY_FAC_AVTVI_TYPE_LSPF = "lspfActviTypes";
    private static final String KEY_FAC_AVTVI_TYPE_RF = "rfActviTypes";

    public void reqObjMapping(HttpServletRequest request) {
        String newFacClassification = ParamUtil.getString(request, KEY_SELECTION_CLASSIFICATION);
        this.setFacClassification(newFacClassification);
        String[] facActivityTypes = null;
        if (newFacClassification != null) {
            switch (newFacClassification) {
                case MasterCodeConstants.FAC_CLASSIFICATION_BSL3:
                    facActivityTypes = ParamUtil.getStrings(request, KEY_FAC_AVTVI_TYPE_BLS3);
                    break;
                case MasterCodeConstants.FAC_CLASSIFICATION_BSL4:
                    facActivityTypes = ParamUtil.getStrings(request, KEY_FAC_AVTVI_TYPE_BLS4);
                    break;
                case MasterCodeConstants.FAC_CLASSIFICATION_UF:
                    facActivityTypes = ParamUtil.getStrings(request, KEY_FAC_AVTVI_TYPE_UF);
                    break;
                case MasterCodeConstants.FAC_CLASSIFICATION_LSPF:
                    facActivityTypes = ParamUtil.getStrings(request, KEY_FAC_AVTVI_TYPE_LSPF);
                    break;
                case MasterCodeConstants.FAC_CLASSIFICATION_RF:
                    facActivityTypes = ParamUtil.getStrings(request, KEY_FAC_AVTVI_TYPE_RF);
                    break;
                default:
                    break;
            }
        }
        this.replaceActivityTypes(facActivityTypes);
    }
}
