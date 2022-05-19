package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants;
import sg.gov.moh.iais.egp.bsb.dto.entity.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author : LiRan
 * @date : 2022/3/17
 */

@Data
@EqualsAndHashCode(callSuper = false)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FacAuthorisedDto extends ValidatableNodeValue {
    private Map<String, FacilityAuthoriserDto> facilityAuthorisedMap;
    private List<String> authPersonnelIds;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public FacAuthorisedDto() {
        this.facilityAuthorisedMap = new HashMap<>();
        authPersonnelIds = new ArrayList<>();
    }

    public void clearFacilityAuthorisedMap(){
        this.facilityAuthorisedMap.clear();
    }


    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod(ApprovalBatAndActivityConstants.FEIGN_CLIENT, "validateFacilityAuthoriser", new Object[]{this.authPersonnelIds});
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

    private static final String SEPARATOR                                     = "--v--";
    private static final String KEY_SECTION_IDXES                             = "sectionIdx";
    private static final String KEY_FACILITY_AUTHORISED_PERSONNEL             = "personnel";
    public static final String KEY_USER_ID_FACILITY_AUTH_MAP                  = "facilityAuthIdMap";

    public void reqObjMapping(HttpServletRequest request) {
        clearFacilityAuthorisedMap();
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        Map<String, FacilityAuthoriserDto> facilityAuthDtoMap = (Map<String, FacilityAuthoriserDto>) ParamUtil.getSessionAttr(request,KEY_USER_ID_FACILITY_AUTH_MAP);
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
         String personnelId = ParamUtil.getString(request,KEY_FACILITY_AUTHORISED_PERSONNEL+SEPARATOR+idx);
         if(StringUtils.hasLength(personnelId)){
            FacilityAuthoriserDto facilityAuthoriserDto = facilityAuthDtoMap.get(personnelId);
            this.authPersonnelIds.add(facilityAuthoriserDto.getId());
            this.facilityAuthorisedMap.put(personnelId,facilityAuthoriserDto);
         }
        }
    }
}
