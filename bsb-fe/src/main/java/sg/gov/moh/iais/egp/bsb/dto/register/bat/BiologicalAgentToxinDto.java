package sg.gov.moh.iais.egp.bsb.dto.register.bat;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;
import sg.gov.moh.iais.egp.common.annotation.RfcAttributeDesc;

import javax.servlet.http.HttpServletRequest;
import java.util.*;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class BiologicalAgentToxinDto extends ValidatableNodeValue {
    private String activityEntityId;
    private String activityType;

    @RfcAttributeDesc(aliasName = "iais.bsbfe.facBat.addOrDelete")
    private List<BATInfo> batInfos;


    @JsonIgnore
    private ValidationResultDto validationResultDto;


    public BiologicalAgentToxinDto() {
        batInfos = new ArrayList<>();
        BATInfo batInfo = new BATInfo();
        batInfos.add(batInfo);
    }

    public BiologicalAgentToxinDto(String activityType) {
        this();
        this.activityType = activityType;
    }


    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityBiologicalAgentToxin", new Object[]{this});
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



    public String getActivityType() {
        return activityType;
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public String getActivityEntityId() {
        return activityEntityId;
    }

    public void setActivityEntityId(String activityEntityId) {
        this.activityEntityId = activityEntityId;
    }

    public List<BATInfo> getBatInfos() {
        return new ArrayList<>(batInfos);
    }

    public void clearBatInfos() {
        this.batInfos.clear();
    }

    public void addBatInfo(BATInfo info) {
        this.batInfos.add(info);
    }

    public void setBatInfos(List<BATInfo> batInfos) {
        this.batInfos = new ArrayList<>(batInfos);
    }



//    ---------------------------- request -> object ----------------------------------------------

    private static final String KEY_SECTION_IDXES                             = "sectionIdx";


    public void reqObjMapping(HttpServletRequest request) {
        clearBatInfos();
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
            BATInfo info = new BATInfo();
            info.reqObjMapping(request,idx);
            addBatInfo(info);
        }
    }
}
