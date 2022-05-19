package sg.gov.moh.iais.egp.bsb.dto.register.approval;

import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import sg.gov.moh.iais.egp.bsb.common.node.simple.ValidatableNodeValue;
import sg.gov.moh.iais.egp.bsb.constant.module.ApprovalBatAndActivityConstants;
import sg.gov.moh.iais.egp.bsb.dto.register.bat.ProcModeLSPDetails;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.SpringReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author : LiRan
 * @date : 2022/3/17
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApprovalToLargeDto extends ValidatableNodeValue {

    @Data
    @NoArgsConstructor
    public static class BATInfo implements Serializable {
        private String schedule;
        private String batName;
        private String estimatedMaximumVolume;
        private String methodOrSystem;

        private ProcModeLSPDetails lspDetails;
    }

    private List<BATInfo> batInfos;

    @JsonIgnore
    private ValidationResultDto validationResultDto;

    public ApprovalToLargeDto() {
        batInfos = new ArrayList<>();
    }


    @Override
    public boolean doValidation() {
        this.validationResultDto = (ValidationResultDto) SpringReflectionUtils.invokeBeanMethod(ApprovalBatAndActivityConstants.FEIGN_CLIENT, "validateApprovalToLargeDto", new Object[]{this});
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

    // ---------------------------- request -> object ----------------------------------------------

    private static final String SEPARATOR                                     = "--v--";
    private static final String KEY_SECTION_IDXES                             = "sectionIdx";
    private static final String KEY_PREFIX_SCHEDULE                           = "schedule";
    private static final String KEY_PREFIX_BAT_NAME                           = "batName";
    private static final String KEY_PREFIX_ESTIMATED_MAXIMUM_VOLUME           = "estimatedMaximumVolume";
    private static final String KEY_PREFIX_METHOD_OR_SYSTEM                   = "methodOrSystem";

    public void reqObjMapping(HttpServletRequest request) {
        clearBatInfos();
        String idxes = ParamUtil.getString(request, KEY_SECTION_IDXES);
        String[] idxArr = idxes.trim().split(" +");
        for (String idx : idxArr) {
            BATInfo info = new BATInfo();
            info.setSchedule(ParamUtil.getString(request, KEY_PREFIX_SCHEDULE + SEPARATOR +idx));
            info.setBatName(ParamUtil.getString(request, KEY_PREFIX_BAT_NAME + SEPARATOR +idx));
            info.setEstimatedMaximumVolume(ParamUtil.getString(request, KEY_PREFIX_ESTIMATED_MAXIMUM_VOLUME + SEPARATOR +idx));
            info.setMethodOrSystem(ParamUtil.getString(request, KEY_PREFIX_METHOD_OR_SYSTEM + SEPARATOR +idx));
            ProcModeLSPDetails details = new ProcModeLSPDetails();
            details.reqObjMapping(request,idx);
            info.setLspDetails(details);
            addBatInfo(info);
        }
    }
}
