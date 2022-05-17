package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbAppointmentClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.appointment.InspectionDateDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;

@Delegator(value = "bsbSubmitInspectionDateDelegator")
@Slf4j
public class BsbSubmitInspectionDateDelegator {
    public static final String MASK_PARAM_APP_ID = "indicateInsDateAppId";

    public static final String PARAM_INSPECTION_DATE_DTO = "inspectionDateDto";
    public static final String PARAM_SPECIFY_START_DATE = "specifyStartDate";
    public static final String PARAM_SPECIFY_END_DATE = "specifyEndDate";
    public static final String PARAM_BACK_URL = "backUrl";
    public static final String PARAM_INBOX_URL = "/bsb-fe/eservice/INTERNET/MohBSBInboxMsg";
    public static final String PARAM_ACK_MSG = "ackMsg";
    public static final String PARAM_SUCCESS_MSG = "Your submission was successful.";

    private final BsbAppointmentClient bsbAppointmentClient;

    @Autowired
    public BsbSubmitInspectionDateDelegator(BsbAppointmentClient bsbAppointmentClient) {
        this.bsbAppointmentClient = bsbAppointmentClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        // clear sessions
        HttpSession session = request.getSession();
        session.removeAttribute(PARAM_INSPECTION_DATE_DTO);
        // set audit trail info (We can set appNo here, may be added in future)
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_MAIN_FUNCTION, AuditTrailConsts.FUNCTION_DEFINE_PREF_DATE_RANGE);
    }

    public void prepareData(BaseProcessClass bpc) {
        log.info("start prepareData ......");
        HttpServletRequest request = bpc.request;
        InspectionDateDto dto = getInspectionDateDto(request);
        if (StringUtils.isEmpty(dto.getAppId())) {
            // get app ID from request parameter
            String maskedAppId = ParamUtil.getString(request, KEY_APP_ID);
            String appId = MaskUtil.unMaskValue(MASK_PARAM_APP_ID, maskedAppId);
            if (appId == null || appId.equals(maskedAppId)) {
                throw new IllegalArgumentException("Invalid masked app ID:" + LogUtil.escapeCrlf(maskedAppId));
            }
            dto.setAppId(appId);
        }
        ParamUtil.setSessionAttr(request, PARAM_INSPECTION_DATE_DTO, dto);
        ParamUtil.setRequestAttr(request,PARAM_BACK_URL,PARAM_INBOX_URL);
    }

    public void validateDate(BaseProcessClass bpc) {
        log.info("start validate ......");
        HttpServletRequest request = bpc.request;
        String startDate = ParamUtil.getString(request, PARAM_SPECIFY_START_DATE);
        String endDate = ParamUtil.getString(request, PARAM_SPECIFY_END_DATE);

        InspectionDateDto dto = getInspectionDateDto(request);
        dto.setModule("specifyInsDate");
        dto.setSpecifyStartDate(startDate);
        dto.setSpecifyEndDate(endDate);

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
        bsbAppointmentClient.saveInsDate(dto);
        ParamUtil.setRequestAttr(request,PARAM_BACK_URL,PARAM_INBOX_URL);
        ParamUtil.setRequestAttr(request,PARAM_ACK_MSG,PARAM_SUCCESS_MSG);
    }

    private InspectionDateDto getInspectionDateDto(HttpServletRequest request) {
        InspectionDateDto dto = (InspectionDateDto) ParamUtil.getSessionAttr(request, PARAM_INSPECTION_DATE_DTO);
        return dto == null ? getDefaultDto() : dto;
    }

    private InspectionDateDto getDefaultDto() {
        return new InspectionDateDto();
    }
}
