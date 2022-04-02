package sg.gov.moh.iais.egp.bsb.dto.register.facility;

import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationAnswerDto;
import sg.gov.moh.iais.egp.bsb.dto.declaration.DeclarationItemMainInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class OtherApplicationInfoDto extends ValidatableNodeValue {
    private String declarationId;

    @JsonIgnore
    private List<DeclarationItemMainInfo> declarationConfig;

    /* The key is item ID */
    private final Map<String, String> answerMap;

    public OtherApplicationInfoDto() {
        answerMap = new HashMap<>();
    }

    @JsonIgnore
    private ValidationResultDto validationResultDto;


    @Override
    public boolean doValidation() {
        DeclarationAnswerDto answerDto = new DeclarationAnswerDto(this.declarationId, this.answerMap);
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod("facRegFeignClient", "validateFacilityOtherAppInfo", new Object[]{answerDto});
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

    public ValidationResultDto getValidationResultDto() {
        return validationResultDto;
    }

    public String getDeclarationId() {
        return declarationId;
    }

    public void setDeclarationId(String declarationId) {
        this.declarationId = declarationId;
    }

    public List<DeclarationItemMainInfo> getDeclarationConfig() {
        return declarationConfig;
    }

    public void setDeclarationConfig(List<DeclarationItemMainInfo> declarationConfig) {
        this.declarationConfig = declarationConfig;
    }

    public boolean isConfigNotLoaded() {
        return declarationConfig == null;
    }

    public Map<String, String> getAnswerMap() {
        return answerMap;
    }

    public void setAnswerMap(Map<String, String> answerMap) {
        this.answerMap.clear();
        if (answerMap != null) {
            this.answerMap.putAll(answerMap);
        }
    }

    public void addAnswerItem(String id, String value) {
        this.answerMap.put(id, value);
    }

    public void clearAnswerMap() {
        this.answerMap.clear();
    }

    //    ---------------------------- request -> object ----------------------------------------------
    public void reqObjMapping(HttpServletRequest request) {
        clearAnswerMap();
        Enumeration<String> nameIterator =  request.getParameterNames();
        while (nameIterator.hasMoreElements()) {
            String paramName = nameIterator.nextElement();
            if (paramName.startsWith("MID")) {
                String maskedId = paramName.substring(3);
                String id = MaskUtil.unMaskValue("declaration", maskedId);
                addAnswerItem(id, request.getParameter(paramName));
            }
        }
    }
}
