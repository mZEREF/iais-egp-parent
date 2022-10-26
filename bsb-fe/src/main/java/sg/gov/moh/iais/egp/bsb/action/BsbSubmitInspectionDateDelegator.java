package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbAppointmentClient;
import sg.gov.moh.iais.egp.bsb.client.RfiClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.appointment.InspectionDateDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_DEFINE_PREF_DATE_RANGE;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_INSPECTION;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_CONFIRM_RFI_Y;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_MASKED_RFT_DATA_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFI_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.RfiConstants.KEY_RFT_DATA_ID;

@Slf4j
@RequiredArgsConstructor
@Delegator(value = "bsbSubmitInspectionDateDelegator")
public class BsbSubmitInspectionDateDelegator {
    public static final String MASK_PARAM_APP_ID = "indicateInsDateAppId";

    public static final String PARAM_INSPECTION_DATE_DTO = "inspectionDateDto";
    public static final String PARAM_SPECIFY_START_DATE = "specifyStartDt";
    public static final String PARAM_SPECIFY_END_DATE = "specifyEndDt";
    public static final String PARAM_BACK_URL = "backUrl";
    public static final String PARAM_INBOX_URL = "/bsb-web/eservice/INTERNET/MohBSBInboxMsg";
    public static final String KEY_ACK_MSG = "ackMsg";
    public static final String KEY_TITLE = "title";
    public static final String TITLE_INDICATE_PREFERRED_INSPECTION_DT = "Indicate Preferred Inspection Date";
    public static final String TITLE_RESPOND_RFI_AND_INDICATE_PREFERRED_INSPECTION_DT = "Respond to Request for Information and Indicate Preferred Inspection Date";
    public static final String PARAM_SUCCESS_MSG = "You have successfully submitted the preferred inspection date.";

    private final BsbAppointmentClient bsbAppointmentClient;
    private final RfiClient rfiClient;

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        // clear sessions
        HttpSession session = request.getSession();
        session.removeAttribute(PARAM_INSPECTION_DATE_DTO);
        session.removeAttribute(KEY_TITLE);
        session.removeAttribute(KEY_RFT_DATA_ID);
        session.removeAttribute(KEY_CONFIRM_RFI);
        session.removeAttribute(KEY_APP_ID);
        // set audit trail info (We can set appNo here, may be added in future)
        AuditTrailHelper.auditFunction(MODULE_INSPECTION, FUNCTION_DEFINE_PREF_DATE_RANGE);
    }

    public void prepareData(BaseProcessClass bpc) {
        log.info("start prepareData ......");
        HttpServletRequest request = bpc.request;
        InspectionDateDto dto = getInspectionDateDto(request);
        String maskedAppId = ParamUtil.getString(request, KEY_APP_ID);
        if (StringUtils.hasLength(maskedAppId)) {
            String appId = MaskUtil.unMaskValue(MASK_PARAM_APP_ID, maskedAppId);
            if (appId == null || appId.equals(maskedAppId)) {
                throw new IllegalArgumentException("Invalid masked app ID:" + LogUtil.escapeCrlf(maskedAppId));
            }
            dto.setAppId(appId);
        }
        ParamUtil.setSessionAttr(request, PARAM_INSPECTION_DATE_DTO, dto);
        ParamUtil.setRequestAttr(request, PARAM_BACK_URL, PARAM_INBOX_URL);
        // todo: JUDGE STATUS
        ParamUtil.setSessionAttr(request, KEY_TITLE, TITLE_INDICATE_PREFERRED_INSPECTION_DT);

        // get app id
        String maskedRfiAppId = ParamUtil.getString(request, KEY_RFI_APP_ID);
        String maskedRfiDataId = ParamUtil.getString(request, KEY_MASKED_RFT_DATA_ID);
        if (StringUtils.hasLength(maskedRfiAppId) && StringUtils.hasLength(maskedRfiDataId)) {
            String appId = MaskHelper.unmask(KEY_RFI_APP_ID, maskedRfiAppId);
            String rfiDataId = MaskHelper.unmask(KEY_MASKED_RFT_DATA_ID, maskedRfiDataId);
            ParamUtil.setSessionAttr(request, KEY_APP_ID, appId);
            ParamUtil.setSessionAttr(request, KEY_RFT_DATA_ID, rfiDataId);
            // rfi inspection self-assessment, nc, follow-up need
            ParamUtil.setSessionAttr(request, KEY_CONFIRM_RFI, KEY_CONFIRM_RFI_Y);
            dto.setAppId(appId);
        }
    }

    public void validateDate(BaseProcessClass bpc) {
        log.info("start validate ......");
        HttpServletRequest request = bpc.request;
        String startDate = ParamUtil.getString(request, PARAM_SPECIFY_START_DATE);
        String endDate = ParamUtil.getString(request, PARAM_SPECIFY_END_DATE);

        InspectionDateDto dto = getInspectionDateDto(request);
        dto.setSpecifyStartDt(startDate);
        dto.setSpecifyEndDt(endDate);

        String actionType;
        ValidationResultDto validationResultDto = bsbAppointmentClient.validateAppointmentData(dto);

        if (!validationResultDto.isPass()) {
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = "prepare";
        } else {
            actionType = "next";
        }
        ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, actionType);
        ParamUtil.setSessionAttr(request, PARAM_INSPECTION_DATE_DTO, dto);
    }

    public void saveInspectionDate(BaseProcessClass bpc) {
        log.info("start save form data ......");
        HttpServletRequest request = bpc.request;
        InspectionDateDto dto = getInspectionDateDto(request);
        String confirmRfi = (String) ParamUtil.getSessionAttr(request, KEY_CONFIRM_RFI);
        if (confirmRfi != null && confirmRfi.equals(KEY_CONFIRM_RFI_Y)) {
            String rfiDataId = (String) ParamUtil.getSessionAttr(request, KEY_RFT_DATA_ID);
            rfiClient.saveInsDate(dto, rfiDataId);
        } else {
            bsbAppointmentClient.saveInsDate(dto);
        }
        ParamUtil.setRequestAttr(request, PARAM_BACK_URL, PARAM_INBOX_URL);
        ParamUtil.setRequestAttr(request, KEY_ACK_MSG, PARAM_SUCCESS_MSG);
    }

    private InspectionDateDto getInspectionDateDto(HttpServletRequest request) {
        InspectionDateDto dto = (InspectionDateDto) ParamUtil.getSessionAttr(request, PARAM_INSPECTION_DATE_DTO);
        return dto == null ? new InspectionDateDto() : dto;
    }
}
