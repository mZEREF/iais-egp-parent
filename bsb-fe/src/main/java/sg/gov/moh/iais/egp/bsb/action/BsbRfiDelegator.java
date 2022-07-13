package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.ApplicationRfiIndicatorDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDisplayDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_REQUEST_FOR_INFORMATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_REQUEST_FOR_INFORMATION;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_ACTION_TYPE_PREPARE_RFI;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_ACTION_TYPE_PRE_ACKNOWLEDGE;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_APPLICATION_RFI_INDICATOR_DTO_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CRUD_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_DISPLAY_DTO;

/**
 * RFI can be dealt with in three cases.
 * 1. Process is simple with New and RFI, RFI judgment is added to the same process and page.
 * (eg: BsbSubmitSelfAssessmentDelegator, BsbRectifiesNonComplianceDelegator, InspectionFollowUpItemsDelegator)
 * 2. Process is complex with New and RFI, RFI has a separate process and page.
 * (eg: RfiFacilityRegistrationDelegator, RfiApprovalBatAndActivityDelegator)
 * 3. Process only has RFI and no New, RFI has a separate process and page.
 * (eg: BsbRfiCommentInspectionReportDelegator)
 */
@Slf4j
@Delegator(value = "bsbRfiDelegator")
public class BsbRfiDelegator {
    private final RfiClient rfiClient;

    @Autowired
    public BsbRfiDelegator(RfiClient rfiClient) {
        this.rfiClient = rfiClient;
    }

    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_RFI_DISPLAY_DTO);
        AuditTrailHelper.auditFunction(MODULE_REQUEST_FOR_INFORMATION, FUNCTION_REQUEST_FOR_INFORMATION);
    }

    public void init(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter(KEY_APP_ID);
        boolean failRetrieveRfiData = true;
        if (StringUtils.hasLength(maskedAppId)) {
            if (log.isInfoEnabled()) {
                log.info("masked application ID: {}", org.apache.commons.lang.StringUtils.normalizeSpace(maskedAppId));
            }
            String appId = MaskUtil.unMaskValue(KEY_RFI_APP_ID, maskedAppId);
            if (appId != null) {
                ResponseDto<RfiDisplayDto> resultDto = rfiClient.getProcessingRfiByApplicationId(appId);
                if (resultDto.ok()) {
                    failRetrieveRfiData = false;
                    RfiDisplayDto rfiDisplayDto = resultDto.getEntity();
                    ParamUtil.setSessionAttr(request, KEY_RFI_DISPLAY_DTO, rfiDisplayDto);
                }
            }
        }
        if (failRetrieveRfiData) {
            throw new IaisRuntimeException("Fail to retrieve rfi data");
        }
    }

    public void prepareRfi(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        boolean allCompleted = true;
        RfiDisplayDto rfiDisplayDto = (RfiDisplayDto) ParamUtil.getSessionAttr(request, KEY_RFI_DISPLAY_DTO);
        List<ApplicationRfiIndicatorDto> applicationRfiIndicatorDtoList = rfiDisplayDto.getApplicationRfiIndicatorDtoList();
        for (ApplicationRfiIndicatorDto applicationRfiIndicatorDto : applicationRfiIndicatorDtoList) {
            boolean status = applicationRfiIndicatorDto.isStatus();
            if (!status) {
                allCompleted = false;
                break;
            }
        }
        if (allCompleted) {
            ParamUtil.setRequestAttr(request, "completedAllRfi", "true");
        } else {
            ParamUtil.setRequestAttr(request, "completedAllRfi", "false");
        }
        ParamUtil.setRequestAttr(request, KEY_APPLICATION_RFI_INDICATOR_DTO_LIST, applicationRfiIndicatorDtoList);
    }

    public void doRfi(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        boolean allCompleted = true;
        RfiDisplayDto rfiDisplayDto = (RfiDisplayDto) ParamUtil.getSessionAttr(request, KEY_RFI_DISPLAY_DTO);
        List<ApplicationRfiIndicatorDto> applicationRfiIndicatorDtoList = rfiDisplayDto.getApplicationRfiIndicatorDtoList();
        for (ApplicationRfiIndicatorDto applicationRfiIndicatorDto : applicationRfiIndicatorDtoList) {
            boolean status = applicationRfiIndicatorDto.isStatus();
            if (!status) {
                allCompleted = false;
                break;
            }
        }
        if (allCompleted) {
            rfiClient.saveRfi(rfiDisplayDto.getId());
            ParamUtil.setRequestAttr(request, KEY_CRUD_ACTION_TYPE, KEY_ACTION_TYPE_PRE_ACKNOWLEDGE);
        } else {
            ParamUtil.setRequestAttr(request, KEY_CRUD_ACTION_TYPE, KEY_ACTION_TYPE_PREPARE_RFI);
        }
    }

    public void preAcknowledge(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        RfiDisplayDto rfiDisplayDto = (RfiDisplayDto) ParamUtil.getSessionAttr(request, KEY_RFI_DISPLAY_DTO);
        List<ApplicationRfiIndicatorDto> applicationRfiIndicatorDtoList = rfiDisplayDto.getApplicationRfiIndicatorDtoList();
        ParamUtil.setRequestAttr(request, KEY_APPLICATION_RFI_INDICATOR_DTO_LIST, applicationRfiIndicatorDtoList);
    }

}
