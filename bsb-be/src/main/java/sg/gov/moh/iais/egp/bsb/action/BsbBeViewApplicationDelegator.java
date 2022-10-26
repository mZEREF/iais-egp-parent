package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.AppViewClient;
import sg.gov.moh.iais.egp.bsb.client.ApplicationClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.info.common.AppBasicInfo;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityAuthoriserFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityCommitteeFileDto;
import sg.gov.moh.iais.egp.bsb.dto.register.facility.FacilityRegisterDto;
import sg.gov.moh.iais.egp.bsb.service.AppViewService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.FUNCTION_VIEW_APPLICATION;
import static com.ecquaria.cloud.moh.iais.common.constant.BsbAuditTrailConstants.MODULE_VIEW_APPLICATION;
import static sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants.PROCESS_TYPE_RENEW_DEFER;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_ACTION_EXPAND_FILE;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_DATA_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_FACILITY_REGISTRATION_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_INDEED_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.KEY_IS_RFC;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_FAC_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.PARAM_NAME_TASK_ID;


@Slf4j
@RequiredArgsConstructor
@Delegator("bsbBeViewApplicationDelegator")
public class BsbBeViewApplicationDelegator {

    private final AppViewService appViewService;
    private final AppViewClient appViewClient;
    private final ApplicationClient applicationClient;
    private static final String PARAM_VIEW_TYPE = "viewType";

    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(MODULE_VIEW_APPLICATION, FUNCTION_VIEW_APPLICATION);
        bpc.request.getSession().removeAttribute(PARAM_VIEW_TYPE);
    }

    public void prepare(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String taskId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_TASK_ID);
        String appId = (String) ParamUtil.getSessionAttr(request, PARAM_NAME_APP_ID);
        String viewType = (String) ParamUtil.getSessionAttr(request, PARAM_VIEW_TYPE);
        String facId = ParamUtil.getString(request, PARAM_NAME_FAC_ID);
        if (!StringUtils.hasLength(viewType)) {
            viewType = request.getParameter(PARAM_VIEW_TYPE);
            ParamUtil.setSessionAttr(request, PARAM_VIEW_TYPE, viewType);
        }
        if (StringUtils.hasLength(facId)) {
            // view facility
            facId = MaskUtil.unMaskValue(PARAM_NAME_FAC_ID, facId);
            appViewService.prepareViewFacility(request, facId);
        } else {
            AppBasicInfo appBasicInfo = applicationClient.getAppBasicInfoById(appId);
            boolean isRfc = MasterCodeConstants.APP_TYPE_RFC.equals(appBasicInfo.getAppType());
            ParamUtil.setRequestAttr(request, KEY_IS_RFC, isRfc ? Boolean.TRUE : Boolean.FALSE);
            ParamUtil.setRequestAttr(request, "appBasicInfo", appBasicInfo);
            String processType = appBasicInfo.getProcessType();
            String appType = appBasicInfo.getAppType();
            if ("view".equals(viewType)) {
                if (appViewClient.hasCompletedRfi(appId, taskId)) {
                    // view rfi compare application
                    appViewService.retrieveHasRfiDataByProcessType(request, appId, taskId, processType);
                } else if (MasterCodeConstants.APP_TYPE_RFC.equals(appType) || (MasterCodeConstants.APP_TYPE_RENEW.equals(appType) && !PROCESS_TYPE_RENEW_DEFER.equals(processType))) {
                    appViewService.retrieveRfcDataByProcessType(request, appId, processType);
                } else {
                    // view single application
                    appViewService.retrieveDataByProcessType(request, appId, processType, isRfc);
                }
            } else if ("rfi".equals(viewType)) {
                // view rfi select section application
                appViewService.retrieveDoRfiDataByProcessType(request, appId, processType, isRfc);
            }
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
