package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.AppViewClient;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_ACTION_EXPAND_FILE;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_DATA_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_FACILITY_REGISTRATION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_INDEED_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.MASK_PARAM_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_TASK_TYPE;


@Slf4j
@Delegator("bsbBeViewFacilityDelegator")
public class BsbBeViewFacilityDelegator {

    private final AppViewService appViewService;
    private final AppViewClient appViewClient;

    @Autowired
    public BsbBeViewFacilityDelegator(AppViewService appViewService, AppViewClient appViewClient) {
        this.appViewService = appViewService;
        this.appViewClient = appViewClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(MASK_PARAM_APP_ID);
        session.removeAttribute(KEY_FACILITY_REGISTRATION_DTO);
        session.removeAttribute(KEY_TASK_TYPE);
        AuditTrailHelper.auditFunction("View Application", "Facility Registration");
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskedAppId = request.getParameter(MASK_PARAM_APP_ID);
        String appId = MaskUtil.unMaskValue(MASK_PARAM_APP_ID, maskedAppId);
        if (maskedAppId == null || appId == null || maskedAppId.equals(appId)) {
            throw new IaisRuntimeException("Invalid App ID");
        }
        ParamUtil.setSessionAttr(request, MASK_PARAM_APP_ID, appId);

        String taskType = request.getParameter(KEY_TASK_TYPE);
        if (taskType != null && !taskType.equals("")) {
            ParamUtil.setSessionAttr(request, KEY_TASK_TYPE, taskType);
        }
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, MASK_PARAM_APP_ID);
        String taskType = (String) ParamUtil.getSessionAttr(request, KEY_TASK_TYPE);
        if (taskType != null) {
            // need judge has rfi
            if (appViewClient.hasCompletedRfi(appId, taskType)) {
                FacilityRegisterDto oldFacRegDto = appViewClient.getOldFacilityRegistrationData(appId).getEntity();
                appViewService.retrieveRfiFacReg(request, appId, oldFacRegDto);
            } else {
                appViewService.retrieveFacReg(request, appId);
            }
        } else {
            appViewService.retrieveFacReg(request, appId);
        }
    }

    public void handle(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_TYPE);
        if (KEY_ACTION_EXPAND_FILE.equals(actionType)) {
            String actionValue = ParamUtil.getString(request, ModuleCommonConstants.KEY_ACTION_VALUE);
            if ("bsbCommittee".equals(actionValue)) {
                ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, "committee");
            } else if ("facAuth".equals(actionValue)) {
                ParamUtil.setRequestAttr(request, KEY_INDEED_ACTION_TYPE, "authorizer");
            } else {
                throw new IaisRuntimeException("Invalid action value");
            }
        } else {
            throw new IaisRuntimeException("Invalid action type");
        }
    }

    public void preCommittee(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityRegisterDto facilityRegisterDto = (FacilityRegisterDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_REGISTRATION_DTO);
        FacilityCommitteeDto facCommitteeDto = facilityRegisterDto.getFacilityCommitteeDto();
        List<FacilityCommitteeFileDto> dataList = facCommitteeDto.getDataListForDisplay();
        ParamUtil.setRequestAttr(request, KEY_DATA_LIST, dataList);
    }

    public void preAuthorizer(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        FacilityRegisterDto facilityRegisterDto = (FacilityRegisterDto) ParamUtil.getSessionAttr(request, KEY_FACILITY_REGISTRATION_DTO);
        FacilityAuthoriserDto facAuthDto = facilityRegisterDto.getFacilityAuthoriserDto();
        List<FacilityAuthoriserFileDto> dataList = facAuthDto.getDataListForDisplay();
        ParamUtil.setRequestAttr(request, KEY_DATA_LIST, dataList);
    }
}
