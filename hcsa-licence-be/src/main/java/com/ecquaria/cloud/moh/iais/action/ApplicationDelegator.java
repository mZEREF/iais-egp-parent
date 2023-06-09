package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import static com.ecquaria.cloud.moh.iais.constant.HcsaAppConst.APPSUBMISSIONDTO;

/**
 * Process: MohApplication
 *
 * @Auther chenlei on 5/9/2022.
 */
@Slf4j
@Delegator("applicationDelegator")
public class ApplicationDelegator extends AppCommDelegator {

    @Autowired
    private ApplicationService applicationService;
    private static final String ILLEGAL_METHOD = "Illegal method";

    /**
     * Check Data For Edit App
     *
     * @param check   {@link HcsaAppConst#CHECKED_ALL}: do all check; {@link HcsaAppConst#CHECKED_BTN_SHOW}: check for
     *                showing "Edit Application" button
     * @param request
     * @return
     */
    public boolean checkData(int check, HttpServletRequest request) {
        boolean isValid = true;
        if (check == HcsaAppConst.CHECKED_ALL) {
            if (ParamUtil.getRequestAttr(request, IaisEGPConstant.CRUD_TYPE) != null) {
                return isValid;
            } else {
                Object checked = ParamUtil.getRequestAttr(request, HcsaAppConst.CHECKED);
                if (StringUtil.isDigit(checked) && check == Integer.parseInt(checked.toString())) {
                    return isValid;
                }
            }
        }
        String curRoleId = null;
        if (check == HcsaAppConst.CHECKED_BTN_SHOW || check == HcsaAppConst.CHECKED_ALL) {
            String invalidRole = (String) ParamUtil.getRequestAttr(request, HcsaAppConst.ERROR_TYPE);
            if (HcsaAppConst.ERROR_ROLE.equals(invalidRole)) {
                isValid = false;
                ParamUtil.setRequestAttr(request, HcsaAppConst.ERROR_TYPE, HcsaAppConst.ERROR_ROLE);
            } else {
                LoginContext loginContext = ApplicationHelper.getLoginContext(request);
                if (loginContext == null) {
                    isValid = false;
                    ParamUtil.setRequestAttr(request, HcsaAppConst.ERROR_TYPE, HcsaAppConst.ERROR_ROLE);
                } else {
                    curRoleId = loginContext.getCurRoleId();
                }
            }
        }
        String appType = null;
        String appGrpNo = null;
        if (isValid) {
            ApplicationViewDto applicationViewDto = (ApplicationViewDto) request.getSession().getAttribute("applicationViewDto");
            if (applicationViewDto == null) {
                isValid = false;
                ParamUtil.setRequestAttr(request, HcsaAppConst.ERROR_TYPE, HcsaAppConst.ERROR_ROLE);
            } else {
                // licensee trasfer application
                ApplicationGroupDto applicationGroupDto = applicationViewDto.getApplicationGroupDto();
                // transfer licence
                if (HcsaAppConst.CHECKED_BTN_SHOW == check && !StringUtil.isEmpty(applicationGroupDto.getNewLicenseeId())) {
                    isValid = false;
                }
                // check current application status
                ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
                if (IaisCommonUtils.getNonDoBeEditStatus().contains(applicationDto.getStatus())) {
                    isValid = false;
                    if (check == HcsaAppConst.CHECKED_ALL) {
                        ParamUtil.setRequestAttr(request, HcsaAppConst.ERROR_APP, MessageUtil.replaceMessage("GENERAL_ERR0061",
                                "edited", "action"));
                    }
                }
                appType = applicationDto.getApplicationType();
                appGrpNo = applicationGroupDto.getGroupNo();
            }
        }
        if (isValid) {
            Map<String, String> map = applicationService.checkDataForEditApp(check, curRoleId, appType, appGrpNo);
            String error = map.get(HcsaAppConst.ERROR_TYPE);
            if (StringUtil.isNotEmpty(error)) {
                isValid = false;
                ParamUtil.setRequestAttr(request, HcsaAppConst.ERROR_TYPE, error);
            }
            String appError = map.get(HcsaAppConst.ERROR_APP);
            if (StringUtil.isNotEmpty(appError)) {
                isValid = false;
                if (check == HcsaAppConst.CHECKED_ALL) {
                    ParamUtil.setRequestAttr(request, HcsaAppConst.ERROR_APP, appError);
                }
            }
        }
        if (!isValid && check == HcsaAppConst.CHECKED_ALL) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, HcsaAppConst.ACTION_JUMP);
        }
        ParamUtil.setRequestAttr(request, HcsaAppConst.CHECKED, check);
        log.info(StringUtil.changeForLog("Check[ " + check + " ] - isValid: " + isValid));
        return isValid;
    }

    @Override
    protected void requestForInformationLoading(HttpServletRequest request, String appNo) {
        log.info(StringUtil.changeForLog("the do requestForInformationLoading start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(request, "applicationViewDto");
        if (applicationViewDto == null) {
            return;
        }
        String newAppNo;
        newAppNo = applicationViewDto.getApplicationDto().getApplicationNo();
        log.info(StringUtil.changeForLog("AppNo: " + newAppNo));
        if (StringUtil.isEmpty(newAppNo)) {
            return;
        }
        AppSubmissionDto appSubmissionDto = appCommService.getAppSubmissionDtoByAppNo(newAppNo);
        appSubmissionDto.setAmountStr("N/A");
        String appType = appSubmissionDto.getAppType();
        boolean isNew = ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
        boolean isRenewalOrRfc = ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)
                || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType);
        loadingRfiGrpServiceConfig(appSubmissionDto, request);
        svcRelatedInfoRFI(appSubmissionDto, newAppNo);
        appSubmissionDto.setRfiAppNo(newAppNo);
        //clear svcDoc id
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                if (!IaisCommonUtils.isEmpty(appSvcDocDtos)) {
                    for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                        appSvcDocDto.setId(null);
                    }
                    appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                }
            }
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
        }
        appSubmissionDto.setNeedEditController(true);
        // App Edit Selecti Dto (RFI)
        AppEditSelectDto appEditSelectDto;
        if (isRenewalOrRfc || isNew) {
            appEditSelectDto = ApplicationHelper.createAppEditSelectDto(true);
            appEditSelectDto.setLicenseeEdit(ApplicationHelper.canLicenseeEdit(appSubmissionDto.getSubLicenseeDto(), appType,
                    true, true));
        } else {
            appEditSelectDto = applicationViewDto.getAppEditSelectDto();
        }
        appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        log.info(StringUtil.changeForLog("App Edit Selecti Dto (RFI): " + JsonUtil.parseToJson(appEditSelectDto)));
        if (isRenewalOrRfc && StringUtil.isEmpty(appSubmissionDto.getLicenceNo())) {
            // set the required information
            String licenceId = appSubmissionDto.getLicenceId();
            LicenceDto licenceById = licCommService.getActiveLicenceById(licenceId);
            if (licenceById != null) {
                appSubmissionDto.setLicenceNo(licenceById.getLicenceNo());
            } else {
                log.warn(StringUtil.changeForLog("##### No Active Licence for this ID: " + licenceId));
            }
        }
        // BE init
        appSubmissionDto.setUserAgreement(true);
        // Tab tooltip

        //control premises edit
        handlePremises(appSubmissionDto, newAppNo);
        ParamUtil.setSessionAttr(request, APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setSessionAttr(request, HcsaAppConst.REQUESTINFORMATIONCONFIG, "test");
        log.info(StringUtil.changeForLog("the do requestForInformationLoading end ...."));
    }

    /**
     * StartStep: Prepare
     *
     * @param bpc
     * @throws
     */
    @Override
    public void prepare(BaseProcessClass bpc) {
        checkData(HcsaAppConst.CHECKED_ALL, bpc.request);
        super.prepare(bpc);
    }

    /**
     * StartStep: Prepare
     *
     * @param bpc
     * @throws
     */
    @Override
    public void preparePremises(BaseProcessClass bpc) {
        super.preparePremises(bpc);
    }

    /**
     * StartStep: Prepare
     *
     * @param bpc
     * @throws
     */
    @Override
    public void doPremises(BaseProcessClass bpc) {
        super.doPremises(bpc);
    }

    /**
     * StartStep: PrepareForms
     *
     * @param bpc
     * @throws
     */
    @Override
    public void prepareForms(BaseProcessClass bpc) {
        super.prepareForms(bpc);
    }

    /**
     * StartStep: DoForms
     *
     * @param bpc
     * @throws
     */
    @Override
    public void doForms(BaseProcessClass bpc) {
        super.doForms(bpc);
    }

    /**
     * StartStep: PreparePreview
     *
     * @param bpc
     * @throws
     */
    @Override
    public void preparePreview(BaseProcessClass bpc) {
        super.preparePreview(bpc);
    }

    /**
     * StartStep: DoPreview
     *
     * @param bpc
     * @throws
     */
    @Override
    public void doPreview(BaseProcessClass bpc) {
        super.doPreview(bpc);
    }

    /**
     * StartStep: ControlSwitch
     *
     * @param bpc
     * @throws
     */
    @Override
    public void controlSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do controlSwitch start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String crudType = "loading";
        String crudActionValue = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.ISVALID);
        if (StringUtil.isEmpty(crudActionValue)) {
            crudActionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            if (StringUtil.isEmpty(crudActionValue)) {
                crudActionValue = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            }
        }
        if ("ack".equals(crudActionValue)) {
            crudType = crudActionValue;
        } else if ("doSubmit".equals(crudActionValue)) {
            crudType = HcsaAppConst.ACTION_RFI;
        } else if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            // 72106
            String action = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
            if (!StringUtil.isIn(action, new String[]{HcsaAppConst.ACTION_LICENSEE, HcsaAppConst.ACTION_PREMISES,
                    HcsaAppConst.ACTION_JUMP})) {
                AppGrpPremisesDto premisse = IaisCommonUtils.isNotEmpty(appSubmissionDto.getAppGrpPremisesDtoList()) ?
                        appSubmissionDto.getAppGrpPremisesDtoList().get(0) : null;
                if (premisse == null || !premisse.isFilled()) {
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, HcsaAppConst.ACTION_PREMISES);
                }
            }
        }
        log.info(StringUtil.changeForLog("Action Type: " + crudType));
        ParamUtil.setRequestAttr(bpc.request, "Switch2", crudType);
        log.info(StringUtil.changeForLog("the do controlSwitch end ...."));
    }

    /**
     * StartStep: DoRequestInformationSubmit
     *
     * @param bpc
     * @throws Exception
     */
    @Override
    public void prepareJump(BaseProcessClass bpc) throws Exception {
        String invalidRole = (String) ParamUtil.getRequestAttr(bpc.request, HcsaAppConst.ERROR_TYPE);
        StringBuilder url = new StringBuilder();
        if (HcsaAppConst.ERROR_ROLE.equals(invalidRole)) {
            url.append(InboxConst.URL_HTTPS)
                    .append(bpc.request.getServerName())
                    .append("main-web");
        } else {
            url.append(InboxConst.URL_HTTPS)
                    .append(bpc.request.getServerName());
            LoginContext loginContext = ApplicationHelper.getLoginContext(bpc.request);
            if (RoleConsts.USER_ROLE_INSPECTIOR.equals(loginContext.getCurRoleId())
                    || RoleConsts.USER_ROLE_AO1.equals(loginContext.getCurRoleId())
                    || RoleConsts.USER_ROLE_INSPECTION_LEAD.equals(loginContext.getCurRoleId())) {
                TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
                if (taskDto != null) {
                    url.append(taskDto.getProcessUrl()).append("?taskId=").append(MaskUtil.maskValue("taskId",taskDto.getId()));
                    url.append("&backFromEdit=Y");
                } else {
                    url.append("/hcsa-licence-web/eservice/INTRANET/MohInspectionPreInspector/InspectionPreInspectorPre");
                }
            } else {
                url.append("/hcsa-licence-web/eservice/INTRANET/ApplicationView/prepareData");
            }
            String appError = (String) ParamUtil.getRequestAttr(bpc.request, HcsaAppConst.ERROR_APP);
            if (StringUtil.isNotEmpty(appError)) {
                if (url.indexOf("?") > 0) {
                    url.append('&');
                } else {
                    url.append('?');
                }
                url.append(HcsaAppConst.ERROR_APP).append('=')
                        .append(StringUtil.obscured(appError));
            }
        }
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
    }

    /**
     * StartStep: DoRequestInformationSubmit
     *
     * @param bpc
     * @throws Exception
     */
    @Override
    public void doRequestInformationSubmit(BaseProcessClass bpc) throws Exception {
        super.doRequestInformationSubmit(bpc);
    }

    @Override
    protected Map<String, String> checkNextStatusOnRfi(String appGrpNo, String appNo) {
        log.info(StringUtil.changeForLog("App Grp No: " + appGrpNo + " - App No: " + appNo));
        Map<String, String> map = applicationService.checkApplicationByAppGrpNo(appGrpNo);
        map.put(HcsaAppConst.STATUS_GRP, ApplicationConsts.APPLICATION_GROUP_STATUS_PEND_TO_FE);
        map.put(HcsaAppConst.STATUS_APP, map.get(appNo));
        log.info(StringUtil.changeForLog("NextStatusOnRfi: " + map));
        return map;
    }

    @Override
    protected AppSubmissionDto submitRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto,
            String appType, HttpServletRequest request) {
        log.info("----Submit Request In formation-----");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(request, "applicationViewDto");
        if (applicationViewDto != null) {
            appSubmissionRequestInformationDto.getAppSubmissionDto().setChangeSelectDto(applicationViewDto.getAppEditSelectDto());
        }
        appSubmissionRequestInformationDto.setBackend(true);
        return applicationService.submitRequestInformation(appSubmissionRequestInformationDto, appType);
    }

    @Override
    public void prepareAckPage(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareAckPage start ...."));
        // "You have successfully submitted application."
        ParamUtil.setRequestAttr(bpc.request, "ackMsg", MessageUtil.replaceMessage("GENERAL_ERR0058",
                "application", "data"));
        log.info(StringUtil.changeForLog("the do prepareAckPage end ...."));
    }

    @Override
    protected List<AppSubmissionDto> submitRequestForChange(List<AppSubmissionDto> appSubmissionDtoList, String eventRefNo,
            BaseProcessClass bpc) {
        throw new IaisRuntimeException(ILLEGAL_METHOD);
    }

    @Override
    protected void sendRfcSubmittedEmail(List<AppSubmissionDto> appSubmissionDtos, String pmtMethod) {
        throw new IaisRuntimeException(ILLEGAL_METHOD);
    }

    @Override
    protected AppSubmissionDto submit(AppSubmissionDto appSubmissionDto) {
        throw new IaisRuntimeException(ILLEGAL_METHOD);
    }

    @Override
    protected FeeDto getNewAppAmount(AppSubmissionDto appSubmissionDto, boolean charity) {
        throw new IaisRuntimeException(ILLEGAL_METHOD);
    }

}
