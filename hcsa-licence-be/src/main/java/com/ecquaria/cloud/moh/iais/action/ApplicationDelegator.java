package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.RfcHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
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

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the do Start start ...."));
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_TYPE, null);
        if (!checkData(bpc.request)) {
            return;
        }
        HcsaServiceCacheHelper.flushServiceMapping();
        DealSessionUtil.clearSession(bpc.request);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_NEW_APPLICATION, AuditTrailConsts.FUNCTION_NEW_APPLICATION);
        //for rfi loading
        requestForInformationLoading(bpc.request, null);
        //for loading Service Config
        boolean flag = loadingServiceConfig(bpc);
        log.info(StringUtil.changeForLog("The loadingServiceConfig -->:" + flag));
        if (flag) {
            //init session and data reomve function to DealSessionUtil
            DealSessionUtil.initSession(bpc);
        }
        //bpc.request.getSession().setAttribute("RFC_ERR004", MessageUtil.getMessageDesc("RFC_ERR004"));
        // app type and licence id
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if (appSubmissionDto != null && appSubmissionDto.getAppSvcRelatedInfoDtoList() != null) {
            for (AppSvcRelatedInfoDto dto : appSubmissionDto.getAppSvcRelatedInfoDtoList()) {
                if (StringUtil.isEmpty(dto.getApplicationType())) {
                    dto.setApplicationType(appSubmissionDto.getAppType());
                }
                if (StringUtil.isEmpty(dto.getLicenceId())) {
                    dto.setLicenceId(appSubmissionDto.getLicenceId());
                }
            }
        }
        log.info(StringUtil.changeForLog("the do Start end ...."));
    }

    private boolean checkData(HttpServletRequest request) {
        boolean isValid = true;
        if (ParamUtil.getRequestAttr(request, IaisEGPConstant.CRUD_TYPE) != null) {
            return isValid;
        }
        String invalidRole = (String) ParamUtil.getRequestAttr(request, HcsaAppConst.ERROR_TYPE);
        if (HcsaAppConst.ERROR_ROLE.equals(invalidRole)) {
            isValid = false;
        } else {
            LoginContext loginContext = ApplicationHelper.getLoginContext(request);
            if (loginContext == null || !StringUtil.isIn(loginContext.getCurRoleId(), new String[]{
                    RoleConsts.USER_ROLE_ASO,
                    RoleConsts.USER_ROLE_PSO,
                    RoleConsts.USER_ROLE_INSPECTIOR})) {
                ParamUtil.setRequestAttr(request, HcsaAppConst.ERROR_TYPE, HcsaAppConst.ERROR_ROLE);
                isValid = false;
            }
        }
        if (isValid) {
            ApplicationViewDto applicationViewDto = (ApplicationViewDto) request.getSession().getAttribute("applicationViewDto");
            if (applicationViewDto == null) {
                ParamUtil.setRequestAttr(request, HcsaAppConst.ERROR_TYPE, HcsaAppConst.ERROR_ROLE);
                isValid = false;
            } else {
                Map<String, String> checkMap = checkNextStatusOnRfi(applicationViewDto.getApplicationGroupDto().getGroupNo(),
                        applicationViewDto.getApplicationDto().getApplicationNo());
                String appError = checkMap.get(HcsaAppConst.ERROR_APP);
                if (!StringUtil.isEmpty(appError)) {
                    ParamUtil.setRequestAttr(request, HcsaAppConst.ERROR_APP, appError);
                    isValid = false;
                }
            }
        }
        if (!isValid) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, HcsaAppConst.ACTION_JUMP);
        }
        log.info(StringUtil.changeForLog("Check Roles - isValid: " + isValid));
        return isValid;
    }

    @Override
    protected void requestForInformationLoading(HttpServletRequest request, String appNo) {
        log.info(StringUtil.changeForLog("the do requestForInformationLoading start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(request, "applicationViewDto");
        if (applicationViewDto == null) {
            return;
        }
        appNo = applicationViewDto.getApplicationDto().getApplicationNo();
        log.info(StringUtil.changeForLog("AppNo: " + appNo));
        if (StringUtil.isEmpty(appNo)) {
            return;
        }
        AppSubmissionDto appSubmissionDto = appCommService.getAppSubmissionDtoByAppNo(appNo);
        appSubmissionDto.setAmountStr("N/A");
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(
                appSubmissionDto.getAppType()) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(
                appSubmissionDto.getAppType())) {
            RfcHelper.svcDocToPresmise(appSubmissionDto);
        }
        svcRelatedInfoRFI(appSubmissionDto, appNo);
        //set max file index into session
        ApplicationHelper.reSetMaxFileIndex(appSubmissionDto.getMaxFileIndex());
        appSubmissionDto.setRfiAppNo(appNo);
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
        String appType = appSubmissionDto.getAppType();
        boolean isRenewalOrRfc = ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(
                appType) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType);
        appSubmissionDto.setNeedEditController(true);
        AppEditSelectDto appEditSelectDto = applicationViewDto.getAppEditSelectDto();
        if (appEditSelectDto == null) {
            appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
        }
        if (appEditSelectDto == null) {
            appEditSelectDto = new AppEditSelectDto();
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        }
        if (isRenewalOrRfc) {
            // set the required information
            String licenceId = appSubmissionDto.getLicenceId();
            LicenceDto licenceById = licCommService.getActiveLicenceById(licenceId);
            if (licenceById != null) {
                appSubmissionDto.setLicenceNo(licenceById.getLicenceNo());
            } else {
                log.warn(StringUtil.changeForLog("##### No Active Licence for this ID: " + licenceId));
            }
        }
        ParamUtil.setSessionAttr(request, APPSUBMISSIONDTO, appSubmissionDto);
        HashMap<String, String> coMap = (HashMap<String, String>) ParamUtil.getSessionAttr(request, HcsaAppConst.CO_MAP);
        coMap.put(HcsaAppConst.SECTION_LICENSEE, HcsaAppConst.SECTION_LICENSEE);
        coMap.put(HcsaAppConst.SECTION_PREMISES, HcsaAppConst.SECTION_PREMISES);
        coMap.put(HcsaAppConst.SECTION_DOCUMENT, HcsaAppConst.SECTION_PREMISES);
        coMap.put(HcsaAppConst.SECTION_SVCINFO, HcsaAppConst.SECTION_PREMISES);
        coMap.put(HcsaAppConst.SECTION_PREVIEW, HcsaAppConst.SECTION_PREVIEW);
        ParamUtil.setSessionAttr(request, HcsaAppConst.CO_MAP, coMap);
        //control premises edit
        handlePremises(appSubmissionDto, appNo);
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
        checkData(bpc.request);
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
     * StartStep: PrepareDocuments
     *
     * @param bpc
     * @throws
     */
    @Override
    public void prepareDocuments(BaseProcessClass bpc) {
        super.prepareDocuments(bpc);
    }

    /**
     * StartStep: DoDocument
     *
     * @param bpc
     * @throws
     */
    @Override
    public void doDocument(BaseProcessClass bpc) throws IOException {
        super.doDocument(bpc);
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
    public void controlSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do controlSwitch start ...."));
        AppSubmissionDto appSubmissionDto = ApplicationHelper.getAppSubmissionDto(bpc.request);
        String crudType = "loading";
        String crudActionValue = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.ISVALID);
        if (StringUtil.isEmpty(crudActionValue)) {
            crudActionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            if (StringUtil.isEmpty(crudActionValue)) {
                crudActionValue = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
            }
        }
        /*boolean isRfi = ApplicationHelper.checkIsRfi(bpc.request);
        if ((ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) && !isRfi) {
            String crud_action_additional = ParamUtil.getString(bpc.request, "crud_action_additional");
            if ("rfcSaveDraft".equals(crud_action_additional)) {
                crudActionValue = "saveDraft";
            }
        }*/
        if ("ack".equals(crudActionValue)) {
            crudType = crudActionValue;
        } else if ("doSubmit".equals(crudActionValue)) {
            crudType = HcsaAppConst.ACTION_RFI;
       /* } else if ("back".equals(crudActionValue)) {
            String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
            if (StringUtil.isEmpty(action)) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, HcsaAppConst.ACTION_JUMP);
            }*/
        } else if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
            // 72106
            String action = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
            if (!StringUtil.isIn(action, new String[]{HcsaAppConst.ACTION_LICENSEE, HcsaAppConst.ACTION_PREMISES,
                    HcsaAppConst.ACTION_JUMP})) {
                AppGrpPremisesDto premisse = appSubmissionDto.getAppGrpPremisesDtoList() != null
                        && appSubmissionDto.getAppGrpPremisesDtoList().size() > 0 ?
                        appSubmissionDto.getAppGrpPremisesDtoList().get(0) : null;
                if (premisse == null || !premisse.isFilled()) {
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, HcsaAppConst.ACTION_PREMISES);
                }
            }
        }
        log.info(StringUtil.changeForLog("Action Type: " + crudType));
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_TYPE, crudType);
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
            super.prepareJump(bpc);
            url.append(InboxConst.URL_HTTPS)
                    .append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTRANET/ApplicationView/prepareData");
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
        map.put(HcsaAppConst.MAP_KEY_STATUS, ApplicationConsts.APPLICATION_GROUP_STATUS_PEND_TO_FE);
        log.info(StringUtil.changeForLog("NextStatusOnRfi: " + map));
        return map;
    }

    @Override
    protected AppSubmissionDto submitRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto,
            String appType) {
        //return applicationService.submitRequestInformation(appSubmissionRequestInformationDto, appType);
        return appSubmissionRequestInformationDto.getAppSubmissionDto();
    }

    @Override
    public void prepareAckPage(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareAckPage start ...."));
        // TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        // ParamUtil.setRequestAttr(bpc.request, "taskId", MaskUtil.maskValue("taskId", taskDto.getId()));
        ParamUtil.setRequestAttr(bpc.request, "ackMsg", "You have successfully submitted application.");
        log.info(StringUtil.changeForLog("the do prepareAckPage end ...."));
    }

    @Override
    protected List<AppSubmissionDto> submitRequestForChange(List<AppSubmissionDto> appSubmissionDtoList, boolean isAutoRfc) {
        throw new IaisRuntimeException("Illegal method");
    }

    @Override
    protected void sendRfcSubmittedEmail(List<AppSubmissionDto> appSubmissionDtos, String pmtMethod) throws Exception {
        throw new IaisRuntimeException("Illegal method");
    }

    @Override
    protected AppSubmissionDto submit(AppSubmissionDto appSubmissionDto) {
        throw new IaisRuntimeException("Illegal method");
    }

    @Override
    protected FeeDto getNewAppAmount(AppSubmissionDto appSubmissionDto, boolean charity) {
        throw new IaisRuntimeException("Illegal method");
    }

}
