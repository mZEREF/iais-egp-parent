package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.WithdrawnClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.AppSubmitWithdrawnDto;
import sg.gov.moh.iais.egp.bsb.dto.withdrawn.PrimaryDocDto;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @author : tangtang
 */
@Slf4j
@Delegator("bsbWithDrawnAppDelegator")
public class WithdrawnAppDelegator {
    private static final String MODULE_NAME = "Withdrawn Application";
    private static final String ACTION_TYPE_NEXT = "doNext";
    private static final String ACTION_TYPE_PREPARE = "prepare";
    private static final String ACTION_TYPE = "action_type";
    private static final String WITHDRAWN_APP_DTO = "withdrawnDto";
    private static final String PARAM_REASON = "reason";
    private static final String PARAM_REMARKS = "remarks";

    private final WithdrawnClient withdrawnClient;

    public WithdrawnAppDelegator(WithdrawnClient withdrawnClient) {
        this.withdrawnClient = withdrawnClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(MODULE_NAME, MODULE_NAME);
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, null);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);
        if (StringUtils.isEmpty(dto.getAppId())) {
            String maskedApplicationId = request.getParameter("withdrawnAppId");
            String applicationId = MaskUtil.unMaskValue("id", maskedApplicationId);
            if (maskedApplicationId == null || applicationId == null || maskedApplicationId.equals(applicationId)) {
                throw new IaisRuntimeException("Invalid Application ID");
            }
            ResponseDto<AppSubmitWithdrawnDto> responseDto = withdrawnClient.getWithdrawnDataByApplicationId(applicationId);
            if (responseDto.ok()) {
                dto = responseDto.getEntity();
            } else {
                log.warn("get withdrawn API doesn't return ok, the response is {}", responseDto);
                ParamUtil.setRequestAttr(request, WITHDRAWN_APP_DTO, new AppSubmitWithdrawnDto());
            }
        }
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
    }

    public void preConfirm(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String reason = ParamUtil.getRequestString(request, PARAM_REASON);
        String remarks = ParamUtil.getRequestString(request, PARAM_REMARKS);
        AppSubmitWithdrawnDto dto = getWithdrawnDto(request);
        dto.setReason(reason);
        dto.setRemarks(remarks);
        //fill doc
        PrimaryDocDto primaryDocDto = new PrimaryDocDto();
        primaryDocDto.reqObjMapping(mulReq, request, "withdrawn");
        dto.setPrimaryDocDto(primaryDocDto);
        dto.setDocType("withdrawn");
        //joint repoId exist
        String newRepoId = String.join(",", primaryDocDto.getNewDocMap().keySet());
        dto.setRepoIdNewString(newRepoId);
        //set newDocFiles
        dto.setNewDocInfos(primaryDocDto.getNewDocTypeList());
        //set need Validation value
        dto.setDocMetas(primaryDocDto.doValidation());

        //validation
        String actionType = "";
        ValidationResultDto validationResultDto = withdrawnClient.validateWithdrawnDto(dto);
        if (!validationResultDto.isPass()){
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_PREPARE;
        }else {
            actionType = ACTION_TYPE_NEXT;
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
        ParamUtil.setSessionAttr(request, WITHDRAWN_APP_DTO, dto);
    }

    private AppSubmitWithdrawnDto getWithdrawnDto(HttpServletRequest request) {
        AppSubmitWithdrawnDto auditDto = (AppSubmitWithdrawnDto) ParamUtil.getSessionAttr(request, WITHDRAWN_APP_DTO);
        return auditDto == null ? getDefaultWithdrawnDto() : auditDto;
    }

    private AppSubmitWithdrawnDto getDefaultWithdrawnDto() {
        return new AppSubmitWithdrawnDto();
    }
}
