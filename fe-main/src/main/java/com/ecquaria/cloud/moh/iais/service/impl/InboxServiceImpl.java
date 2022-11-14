package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxMsgMaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageSearchDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HalpSearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import com.ecquaria.cloud.moh.iais.service.client.AppInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailMainClient;
import com.ecquaria.cloud.moh.iais.service.client.ConfigInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayFeMainClient;
import com.ecquaria.cloud.moh.iais.service.client.FeUserClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InboxClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InboxServiceImpl implements InboxService {

    @Autowired
    private ConfigInboxClient configInboxClient;

    @Autowired
    private AppInboxClient appInboxClient;

    @Autowired
    private InboxClient inboxClient;

    @Autowired
    private LicenceInboxClient licenceInboxClient;

    @Autowired
    private FeUserClient feUserClient;

    @Autowired
    private AuditTrailMainClient auditTrailMainClient;

    @Autowired
    private EicGatewayFeMainClient eicGatewayFeMainClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Override
    public String getServiceNameById(String serviceId) {
        return configInboxClient.getServiceNameById(serviceId).getEntity();
    }

    @Override
    public InterInboxUserDto getUserInfoByUserId(String userId) {
        InterInboxUserDto interInboxUserDto = feUserClient.findUserInfoByUserId(userId).getEntity();
        List<String> appGrpIdList = appInboxClient.getAppGrpIdsByLicenseeIs(interInboxUserDto.getLicenseeId()).getEntity();
        interInboxUserDto.setAppGrpIds(appGrpIdList);
        return interInboxUserDto;
    }

    @Override
    @SearchTrack(catalog = "interInboxQuery", key = "assessmentWithdrawAppQuery")
    public SearchResult<InboxAppQueryDto> appDoQuery(SearchParam searchParam) {
        SearchResult<InboxAppQueryDto> inboxAppQueryDtoSearchResult = appInboxClient.searchResultFromApp(searchParam).getEntity();
        List<InboxAppQueryDto> inboxAppQueryDtoList = inboxAppQueryDtoSearchResult.getRows();
        for (InboxAppQueryDto inboxAppQueryDto : inboxAppQueryDtoList) {
            if (ApplicationConsts.APPLICATION_STATUS_DRAFT.equals(
                    inboxAppQueryDto.getStatus()) || ApplicationConsts.APPLICATION_STATUS_DRAFT_PENDING.equals(
                    inboxAppQueryDto.getStatus())) {
                ApplicationDraftDto applicationDraftDto = appInboxClient.getDraftInfo(inboxAppQueryDto.getId()).getEntity();
                inboxAppQueryDto.setServiceId(HalpSearchResultHelper.splitServiceName(applicationDraftDto.getServiceCode()));
            } else {
                if (!inboxAppQueryDto.getServiceId().isEmpty()) {
                    inboxAppQueryDto.setServiceId(getServiceNameById(inboxAppQueryDto.getServiceId()));
                } else {
                    inboxAppQueryDto.setServiceId("N/A");
                }
            }
        }
        return inboxAppQueryDtoSearchResult;
    }

    @Override
    @SearchTrack(catalog = "interInboxQuery", key = "inboxQuery")
    public SearchResult<InboxQueryDto> inboxDoQuery(SearchParam searchParam) {
        SearchResult<InboxQueryDto> inboxQueryDtoSearchResult = inboxClient.searchInbox(searchParam).getEntity();
        List<InboxQueryDto> inboxAppQueryDtoListRows = inboxQueryDtoSearchResult.getRows();
        for (InboxQueryDto inboxQueryDto : inboxAppQueryDtoListRows) {
            inboxQueryDto.setServiceCodes(HalpSearchResultHelper.splitServiceName(inboxQueryDto.getServiceCodes()));
        }
        return inboxQueryDtoSearchResult;
    }

    @Override
    public SearchResult<InboxLicenceQueryDto> licenceDoQuery(SearchParam searchParam) {
        return licenceInboxClient.searchResultFromLicence(searchParam).getEntity();
    }

    @Override
    public Integer licActiveStatusNum(InterMessageSearchDto interMessageSearchDto) {
        return licenceInboxClient.getLicActiveStatusNum(interMessageSearchDto).getEntity();
    }

    @Override
    public Integer appDraftNum(InterMessageSearchDto interMessageSearchDto) {
        return appInboxClient.getAppDraftNum(interMessageSearchDto).getEntity();
    }

    @Override
    public Integer unreadAndUnresponseNum(InterMessageSearchDto interMessageSearchDto) {
        return inboxClient.searchUnreadAndUnresponseNum(interMessageSearchDto).getEntity();
    }

    @Override
    public void updateDraftStatus(String draftNo, String status) {
        appInboxClient.updateDraftStatus(draftNo, status).getEntity();
    }

    @Override
    public boolean updateMsgStatus(String[] msgId) {
        return inboxClient.updateMsgStatusToArchive(msgId).getEntity();
    }

    @Override
    public Boolean canRecallApplication(RecallApplicationDto recallApplicationDto) {
        List<String> refNoList = IaisCommonUtils.genNewArrayList();
        String appId = recallApplicationDto.getAppId();
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtoList = appInboxClient.listAppPremisesCorrelation(appId).getEntity();
        for (AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtoList
        ) {
            refNoList.add(appPremisesCorrelationDto.getId());
        }
        recallApplicationDto.setRefNo(refNoList);
        try {
            recallApplicationDto = eicGatewayFeMainClient.callEicWithTrackForOrg(recallApplicationDto,
                    eicGatewayFeMainClient::recallAppChangeTask,
                    "recallAppChangeTask").getEntity();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return recallApplicationDto.getResult();
    }

    @Override
    public List<RecallApplicationDto> canRecallApplications(List<RecallApplicationDto> recallApplicationDtos) {
        List<RecallApplicationDto> recallApplicationDtoList = IaisCommonUtils.genNewArrayList();
        for (RecallApplicationDto h : recallApplicationDtos
        ) {
            String appId = h.getAppId();
            List<String> refNoList = IaisCommonUtils.genNewArrayList();
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtoList = appInboxClient.listAppPremisesCorrelation(
                    appId).getEntity();
            for (AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtoList
            ) {
                refNoList.add(appPremisesCorrelationDto.getId());
            }
            h.setRefNo(refNoList);
        }
        try {
            recallApplicationDtoList = eicGatewayFeMainClient.callEicWithTrackForOrg(recallApplicationDtos,
                    eicGatewayFeMainClient::recallAppTasks,
                    this.getClass(), "recallAppTasksEic").getEntity();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return recallApplicationDtoList;
    }

    @Override
    public void recallAppTasksEic(String jsonData) {
        if (StringUtils.isEmpty(jsonData)) {
            return;
        }
        List<RecallApplicationDto> recallApplicationDtos = JsonUtil.parseToList(jsonData, RecallApplicationDto.class);
        eicGatewayFeMainClient.recallAppTasks(recallApplicationDtos);
    }

    @Override
    public RecallApplicationDto recallApplication(RecallApplicationDto recallApplicationDto) {
        boolean result = false;
        ApplicationDto applicationDto = appInboxClient.getApplicarionById(recallApplicationDto.getAppId()).getEntity();
        if (applicationDto != null) {
            ApplicationGroupDto applicationGroupDto = appInboxClient.getApplicationGroup(applicationDto.getAppGrpId()).getEntity();
            if (ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED.equals(applicationGroupDto.getStatus())) {
                result = true;
                recallApplicationDto.setResult(result);
                recallApplicationDto.setMessage("RECALLMSG002");
                appInboxClient.updateFeAppStatus(recallApplicationDto.getAppId(), ApplicationConsts.APPLICATION_STATUS_RECALLED);
                return recallApplicationDto;
            }
        }
        List<String> refNoList = IaisCommonUtils.genNewArrayList();
        String appId = recallApplicationDto.getAppId();
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtoList = appInboxClient.listAppPremisesCorrelation(appId).getEntity();
        for (AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtoList
        ) {
            refNoList.add(appPremisesCorrelationDto.getId());
        }
        recallApplicationDto.setRefNo(refNoList);
        try {
            recallApplicationDto = eicGatewayFeMainClient.callEicWithTrackForOrg(recallApplicationDto,
                    eicGatewayFeMainClient::recallAppChangeTask,
                    "recallAppChangeTask").getEntity();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (recallApplicationDto.getResult()) {
            try {
                result = eicGatewayFeMainClient.callEicWithTrackForApp(recallApplicationDto,
                        eicGatewayFeMainClient::updateApplicationStatus, "updateApplicationStatus").getEntity();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        if (result || "RECALLMSG002".equals(recallApplicationDto.getMessage()) || "RECALLMSG003".equals(
                recallApplicationDto.getMessage())) {
            appInboxClient.updateFeAppStatus(recallApplicationDto.getAppId(), ApplicationConsts.APPLICATION_STATUS_RECALLED);
        }
        return recallApplicationDto;
    }

    @Override
    public Map<String, String> checkRenewalStatus(String licenceId) {
        log.info(StringUtil.changeForLog("----------checkRenewalStatus licenceId : " + licenceId));
        LicenceDto licenceDto = licenceInboxClient.getLicDtoById(licenceId).getEntity();
        return checkRenewalStatus(licenceDto);
    }

    @Override
    public Map<String, String> checkRenewalStatus(LicenceDto licenceDto) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String errorMsgEleven = MessageUtil.getMessageDesc("INBOX_ACK011");
        if (licenceDto == null) {
            errorMap.put("errorMessage2", errorMsgEleven);
            return errorMap;
        }
        String licenceId = licenceDto.getId();
        String licenceStatus = licenceDto.getStatus();
        if (!ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceStatus)) {
            if (!(IaisEGPHelper.isActiveMigrated() && licenceDto.getMigrated() != 0
                    && ApplicationConsts.LICENCE_STATUS_APPROVED.equals(licenceStatus))) {
                errorMap.put("errorMessage2", errorMsgEleven);
                return errorMap;
            }
        }
        boolean hasError = checkAppsOnLic(licenceId, errorMap);
        if (hasError) {
            log.info("An error in checking apps on licence.");
        } else {
            // 76035 - Verify whether the new licence is generated
            LicenceDto entity = licenceInboxClient.getRootLicenceDtoByOrgId(licenceId).getEntity();
            if (entity != null) {
                boolean isRenewApp = false;
                List<ApplicationDto> apps = appInboxClient.getAppByLicIdAndExcludeNew(entity.getId()).getEntity();
                if (IaisCommonUtils.isNotEmpty(apps)) {
                    log.info(StringUtil.changeForLog("----------checkRenewalStatus json : " + JsonUtil.parseToJson(apps)));
                    for (ApplicationDto a : apps) {
                        if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(a.getApplicationType())
                                && !ApplicationConsts.APPLICATION_STATUS_REJECTED.equalsIgnoreCase(a.getStatus())
                                && !ApplicationConsts.APPLICATION_STATUS_ROLL_BACK.equals(a.getStatus())
                                && !ApplicationConsts.APPLICATION_STATUS_DELETED.equals(a.getStatus())) {
                            isRenewApp = true;
                            break;
                        }
                    }
                }
                if (isRenewApp) {
                    // INBOX_ACK013 - This licence has already been renewed.
                    String errorMsg = MessageUtil.getMessageDesc("INBOX_ACK013");
                    errorMap.put("errorMessage2", errorMsg);
                } else {
                    errorMap.put("errorMessage", licenceDto.getLicenceNo());
                }
            }
        }
        //check expiry date
        Date expiryDate = licenceDto.getExpiryDate();
        Date nowDate = new Date();

        //expiryDate
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(expiryDate);

        //licence expiry date  day - 6 months
        endCalendar.add(Calendar.MONTH, -6);

        Date firstStartRenewTime = endCalendar.getTime();
        if (!(nowDate.after(firstStartRenewTime) && nowDate.before(expiryDate))) {
            errorMap.put("errorMessage", licenceDto.getLicenceNo());
        }
        log.info(StringUtil.changeForLog(" ----------checkRenewalStatus errorMap :" + JsonUtil.parseToJson(errorMap)));
        return errorMap;
    }

    @Override
    public boolean checkRenewalStatus(List<String> licenceIds, HttpServletRequest request) {
        if (IaisCommonUtils.isNotEmpty(licenceIds)) {
            return true;
        }
        if (licenceIds.size() == 1 && !checkRenewDraft(licenceIds.get(0), request)) {
            return false;
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        StringJoiner joiner = new StringJoiner(", ");
        boolean result = true;
        for (String licId : licenceIds) {
            errorMap.putAll(checkRenewalStatus(licId));
            if (!(errorMap.isEmpty())) {
                String licenseNo = errorMap.get("errorMessage");
                if (!StringUtil.isEmpty(licenseNo)) {
                    joiner.add(licenseNo);
                }
                result = false;
            }
        }
        if (!result) {
            String errorMessage = joiner.toString();
            if (StringUtil.isEmpty(errorMessage)) {
                errorMessage = errorMap.get("errorMessage2");
                if (StringUtil.isEmpty(errorMessage)) {
                    errorMessage = MessageUtil.getMessageDesc("RFC_ERR011");
                }
            } else {
                errorMessage = MessageUtil.getMessageDesc("INBOX_ACK015") + errorMessage;
            }
            ParamUtil.setRequestAttr(request, "licIsRenewed", Boolean.TRUE);
            ParamUtil.setRequestAttr(request, InboxConst.LIC_ACTION_ERR_MSG, errorMessage);
            return false;
        }
        boolean toRenewal = true;
        // check bundle
        String bundle = request.getParameter("bundle");
        if ("yes".equals(bundle)) {
            List<LicenceDto> bundleLicenceDtos = (List<LicenceDto>) request.getSession().getAttribute("bundleLicenceDtos");
            if (bundleLicenceDtos != null) {
                for (LicenceDto v : bundleLicenceDtos) {
                    licenceIds.add(v.getId());
                }
            }
        } else {
            StringJoiner data = new StringJoiner(", ");
            joiner = new StringJoiner(", ");
            List<HcsaServiceDto> entity = HcsaServiceCacheHelper.receiveAllHcsaService();
            Map<String, HcsaServiceDto> svcMap = entity.stream()
                    .collect(Collectors.toMap(HcsaServiceDto::getSvcName, Function.identity(), (u, v) -> v));
            List<LicenceDto> dtoList = IaisCommonUtils.genNewArrayList();
            List<LicenceDto> licences = getAllBundleLicences(licenceIds);
            if (!licences.isEmpty()) {
                for (LicenceDto licenceDto : licences) {
                    if (svcMap.get(licenceDto.getSvcName()) == null) {
                        continue;
                    }
                    if (!licenceIds.contains(licenceDto.getId())) {
                        Map<String, String> map = checkRenewalStatus(licenceDto);
                        if (!map.isEmpty()) {
                            joiner.add(licenceDto.getLicenceNo());
                        }
                        data.add(licenceDto.getLicenceNo());
                        dtoList.add(licenceDto);
                    }
                }

            }

            if (joiner.length() != 0) {
                //INBOX_ERR013 - The following bundled licence(s) is/are not eligible for {action}: {data}.
                Map<String, String> param = new HashMap<>(2);
                param.put("action", "renewal");
                param.put("data", joiner.toString());
                ParamUtil.setRequestAttr(request, InboxConst.LIC_ACTION_ERR_MSG,
                        MessageUtil.getMessageDesc("INBOX_ERR013", param));
                ParamUtil.setRequestAttr(request, "licIsRenewed", Boolean.TRUE);
                ParamUtil.setSessionAttr(request, "bundleLicenceDtos", null);
                toRenewal = false;
            } else if (!dtoList.isEmpty()) {
                //INBOX_ACK026 - This following licence(s) is/are bundled with selected licence(s). Would you like to renew them
                // as well: {data}.
                Map<String, String> param = new HashMap<>(2);
                param.put("data", data.toString());
                ParamUtil.setRequestAttr(request, "draftByLicAppId",
                        MessageUtil.getMessageDesc("INBOX_ACK026", param));
                ParamUtil.setRequestAttr(request, "isBundleShow", "1");
                ParamUtil.setSessionAttr(request, "bundleLicenceDtos", (Serializable) dtoList);
                toRenewal = false;
            }
        }

        return toRenewal;
    }

    private boolean checkRenewDraft(String licId, HttpServletRequest request) {
        List<ApplicationSubDraftDto> draftByLicAppId = getDraftByLicAppId(licId);
        String isNeedDelete = ParamUtil.getString(request, "isNeedDelete");
        if (!draftByLicAppId.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId) {
                stringBuilder.append(applicationSubDraftDto.getDraftNo()).append(' ');
            }
            if ("delete".equals(isNeedDelete)) {
                for (ApplicationSubDraftDto applicationSubDraftDto : draftByLicAppId) {
                    deleteDraftByNo(applicationSubDraftDto.getDraftNo());
                }
            } else {
                String ack030 = MessageUtil.getMessageDesc("GENERAL_ACK030");
                String replace = ack030.replace("{draft application no}", stringBuilder.toString());
                ParamUtil.setRequestAttr(request, "draftByLicAppId", replace);
                ParamUtil.setRequestAttr(request, "isRenewShow", "1");
//                ParamUtil.setSessionAttr(request, "licence_err_list", IaisCommonUtils.genNewArrayListWithData(licId));
                return false;
            }
        }
        List<ApplicationSubDraftDto> applicationSubDraftDtos = getDraftByLicAppIdAndStatus(licId,
                ApplicationConsts.DRAFT_STATUS_PENDING_PAYMENT);
        if (!IaisCommonUtils.isEmpty(applicationSubDraftDtos)) {
            ParamUtil.setRequestAttr(request, InboxConst.LIC_ACTION_ERR_MSG, MessageUtil.getMessageDesc("NEW_ERR0023"));
            return false;
        }
        return true;
    }

    @Override
    public void updateMsgStatusTo(String msgId, String msgStatus) {
        inboxClient.updateMsgStatusTo(msgId, msgStatus);
    }

    @Override
    public Boolean checkEligibility(String appId) {
        return appInboxClient.isAppealEligibility(appId).getEntity();
    }

    @Override
    public List<InboxMsgMaskDto> getInboxMaskEntity(String msgId) {
        return inboxClient.getInboxMsgMask(msgId).getEntity();
    }

    @Override
    public List<PremisesDto> getPremisesByLicId(String licenceId) {
        return licenceInboxClient.getPremisesDto(licenceId).getEntity();
    }

    @Override
    public AuditTrailDto getLastLoginInfo(String loginUserId, String sessionId) {
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        AuditTrailDto loginDto = auditTrailMainClient.getLastLoginInfo(loginUserId, sessionId).getEntity();
        if (loginDto != null) {
            auditTrailDto.setActionTime(loginDto.getActionTime());
            AuditTrailDto actDto = auditTrailMainClient.getLastAction(loginDto.getSessionId()).getEntity();
            if (actDto != null) {
                auditTrailDto.setModule(actDto.getModule());
                auditTrailDto.setFunctionName(actDto.getFunctionName());
                auditTrailDto.setLicenseNum(actDto.getLicenseNum());
                auditTrailDto.setApplicationNum(actDto.getApplicationNum());
            }
        }

        return auditTrailDto;
    }

    @Override
    public Map<String, String> checkRfcStatus(String licenceId) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        LicenceDto licenceDto = licenceInboxClient.getLicDtoById(licenceId).getEntity();
        boolean isActive = false;
        if (licenceDto != null) {
            if (ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus())) {
                isActive = true;
            }
            if (IaisEGPHelper.isActiveMigrated() && ApplicationConsts.LICENCE_STATUS_APPROVED.equals(
                    licenceDto.getStatus()) && licenceDto.getMigrated() != 0) {
                isActive = true;
            }
        }
        if (isActive) {
            boolean hasError = checkAppsOnLic(licenceId, errorMap);
            if (hasError) {
                log.info("An error in checking apps on licence.");
            }
            List<LicenceDto> licenceDtos = licenceInboxClient.getLicenceDtosByLicenseeId(licenceDto.getLicenseeId()).getEntity();
            for (LicenceDto licenceDto1 : licenceDtos) {
                if (ApplicationConsts.LICENCE_STATUS_APPROVED.equals(licenceDto1.getStatus()) &&
                        StringUtil.isNotEmpty(licenceDto.getSvcName()) &&
                        licenceDto.getSvcName().equals(licenceDto1.getSvcName()) &&
                        StringUtil.isNotEmpty(licenceDto1.getOriginLicenceId()) &&
                        licenceDto1.getOriginLicenceId().equals(licenceDto.getId())) {
                    errorMap.put("errorMessage", MessageUtil.getMessageDesc("INBOX_ACK025"));
                }
            }
        } else {
            errorMap.put("errorMessage", MessageUtil.getMessageDesc("INBOX_ACK011"));
        }
        return errorMap;
    }

    private boolean checkAppsOnLic(String licenceId, Map<String, String> errorMap) {
        List<ApplicationDto> apps = appInboxClient.getAppByLicIdAndExcludeNew(licenceId).getEntity();
        boolean hasError = false;
        if (!IaisCommonUtils.isEmpty(apps)) {
            List<String> finalStatusList = IaisCommonUtils.getAppFinalStatus();
            List<String> appGrpIds = IaisCommonUtils.genNewArrayList();
            for (ApplicationDto app : apps) {
                String status = app.getStatus();
                // 128600 - exclude the approved status for Audit / Post inspection
                boolean isValid = finalStatusList.contains(status) || ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(status)
                        && StringUtil.isIn(app.getApplicationType(), new String[]{ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION,
                        ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK});
                if (!isValid) {
                    String message = MessageUtil.getMessageDesc("RFC_ERR011");
                    errorMap.put("errorMessage", message);
                    hasError = true;
                }
                if (ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED.equals(status)
                        && ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(app.getApplicationType())) {
                    // INBOX_ACK025 - This licence has already been amended, please wait for the newly generated licence to be active before proceeding with amendment
                    errorMap.put("errorMessage", MessageUtil.getMessageDesc("INBOX_ACK025"));
                    hasError = true;
                }
                if (ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT.equals(status)) {
                    appGrpIds.add(app.getAppGrpId());
                }
            }
            if (!hasError && !appGrpIds.isEmpty()) {
                List<ApplicationGroupDto> appGrpDtos = appInboxClient.getApplicationGroupsByIds(appGrpIds).getEntity();
                if (!IaisCommonUtils.isEmpty(appGrpDtos)) {
                    boolean matched = appGrpDtos.stream()
                            .anyMatch(dto -> ApplicationConsts.APPLICATION_GROUP_STATUS_PENDING_PAYMENT.equals(dto.getStatus()));
                    if (matched) {
                        // GENERAL_ERR0062 - There is a related pending payment application.
                        errorMap.put("errorMessage", MessageUtil.getMessageDesc("GENERAL_ERR0062"));
                        hasError = true;
                    }
                }
            }
        }
        // appeal
        if (!hasError) {
            Boolean entity = appInboxClient.isLiscenceAppealOrCessation(licenceId).getEntity();
            if (!entity) {
                errorMap.put("errorMessage", MessageUtil.getMessageDesc("INBOX_ACK010"));
                hasError = true;
            }
        }
        return hasError;
    }

    @Override
    public Map<String, Boolean> listResultCeased(List<String> licIds) {
        Map<String, Boolean> map = IaisCommonUtils.genNewHashMap();
        for (String licId : licIds) {
            LicenceDto licenceDto = licenceInboxClient.getLicDtoById(licId).getEntity();
            if (licenceDto == null) {
                map.put(licId, Boolean.FALSE);
                return map;
            } else {
                if (!ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus())) {
                    if (!(IaisEGPHelper.isActiveMigrated() && ApplicationConsts.LICENCE_STATUS_APPROVED.equals(
                            licenceDto.getStatus()) && licenceDto.getMigrated() != 0)) {
                        map.put(licId, Boolean.FALSE);
                        return map;
                    }
                }
            }
        }
        return appInboxClient.listCanCeased(licIds).getEntity();
    }

    @Override
    public Map<String, String> appealIsApprove(String licenceId, String type) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        List<String> endStatusList = IaisCommonUtils.getAppFinalStatus();
        endStatusList.add("APST005");
        if ("licence".equals(type)) {
            List<ApplicationDto> apps = appInboxClient.getAppByLicIdAndExcludeNew(licenceId).getEntity();
            if (!IaisCommonUtils.isEmpty(apps)) {
                for (ApplicationDto applicationDto : apps) {
                    if (!endStatusList.contains(applicationDto.getStatus())) {
                        errorMap.put("errorMessage", MessageUtil.getMessageDesc("RFC_ERR011"));
                        break;
                    }
                }
            }
            return errorMap;

        } else if ("application".equals(type)) {
            ApplicationDto applicationDto = appInboxClient.getApplicarionById(licenceId).getEntity();
            if (!endStatusList.contains(applicationDto.getStatus())) {
                errorMap.put("errorMessage", MessageUtil.getMessageDesc("RFC_ERR011"));
            }
        }
        return errorMap;
    }

    @Override
    public List<ApplicationSubDraftDto> getDraftByLicAppId(String licAppId) {

        return appInboxClient.getDraftByLicAppId(licAppId).getEntity();
    }

    @Override
    public ApplicationDraftDto getDraftByAppNo(String appNo) {
        return appInboxClient.getDraftInfoByAppNo(appNo).getEntity();
    }

    @Override
    public ApplicationGroupDto getAppGroupByGroupId(String appGroupId) {
        return appInboxClient.getApplicationGroup(appGroupId).getEntity();
    }

    @Override
    public void deleteDraftByNo(String draftNo) {
        log.info(StringUtil.changeForLog("delete draft start ..."));
        appInboxClient.deleteDraftByNo(draftNo);
    }

    @Getter
    @Setter
    class InnerLicenceViewData {

        String value;
        List<String> innerLicenceViewDatas;

    }

    private String getHcsaServiceSubTypeDisplayName(List<HcsaServiceSubTypeDto> hcsaServiceSubTypeDtos, String id) {
        String result = "";
        for (HcsaServiceSubTypeDto hcsaServiceSubTypeDto : hcsaServiceSubTypeDtos) {
            if (hcsaServiceSubTypeDto.getId().equals(id)) {
                result = hcsaServiceSubTypeDto.getSubtypeName();
                break;
            }
        }
        return result;
    }

    private String getHcsaServiceDtoDisplayName(List<HcsaServiceDto> hcsaServiceDtos, String code) {
        String result = "";
        for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
            if (hcsaServiceDto.getSvcCode().equals(code)) {
                result = hcsaServiceDto.getSvcName();
                break;
            }
        }
        return result;
    }

    private boolean isLever0(List<HcsaSvcSpecifiedCorrelationDto> hcsaSvcSpecifiedCorrelationDtos, String svcCode){
         boolean result = false;
         if(IaisCommonUtils.isNotEmpty(hcsaSvcSpecifiedCorrelationDtos) && StringUtil.isNotEmpty(svcCode)){
             for(HcsaSvcSpecifiedCorrelationDto hcsaSvcSpecifiedCorrelationDto : hcsaSvcSpecifiedCorrelationDtos){
                 if(svcCode.equals(hcsaSvcSpecifiedCorrelationDto.getSpecifiedSvcId())){
                     result = true;
                     break;
                 }
             }
         }
         return result;
    }

    private List<InnerLicenceViewData> tidyInnerLicenceViewData(List<LicPremisesScopeDto> licPremisesScopeDtos,
            List<LicPremSubSvcRelDto> licPremSubSvcRelDtos,List<HcsaSvcSpecifiedCorrelationDto> hcsaSvcSpecifiedCorrelationDtos ) {
        List<InnerLicenceViewData> result = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isNotEmpty(licPremisesScopeDtos)) {
            List<String> ids = IaisCommonUtils.genNewArrayList();
            for (LicPremisesScopeDto licPremisesScopeDto : licPremisesScopeDtos) {
                ids.add(licPremisesScopeDto.getSubTypeId());
            }
            List<HcsaServiceSubTypeDto> hcsaServiceSubTypeDtos = configInboxClient.getHcsaServiceSubTypeDtosByIds(ids).getEntity();
            for (LicPremisesScopeDto licPremisesScopeDto : licPremisesScopeDtos) {
                String subTypeDisplayName =  getHcsaServiceSubTypeDisplayName(hcsaServiceSubTypeDtos, licPremisesScopeDto.getSubTypeId());
                if(StringUtil.isNotEmpty(subTypeDisplayName)){
                    InnerLicenceViewData innerLicenceViewData = new InnerLicenceViewData();
                    innerLicenceViewData.setValue(subTypeDisplayName );
                    result.add(innerLicenceViewData);
                }
            }
        }
        if (IaisCommonUtils.isNotEmpty(licPremSubSvcRelDtos)) {
            List<String> svcCodes = IaisCommonUtils.genNewArrayList();
            for (LicPremSubSvcRelDto licPremSubSvcRelDto : licPremSubSvcRelDtos) {
                svcCodes.add(licPremSubSvcRelDto.getSvcCode());
            }
            List<HcsaServiceDto> hcsaServiceDtos = configInboxClient.getHcsaServiceDtoByCode(svcCodes).getEntity();
            InnerLicenceViewData innerLicenceViewData;
            List<String> innerLicenceViewDataList = IaisCommonUtils.genNewArrayList();
            for (LicPremSubSvcRelDto licPremSubSvcRelDto : licPremSubSvcRelDtos) {
                if (isLever0(hcsaSvcSpecifiedCorrelationDtos,licPremSubSvcRelDto.getSvcCode())) {
                    innerLicenceViewData = new InnerLicenceViewData();
                    innerLicenceViewData.setValue(getHcsaServiceDtoDisplayName(hcsaServiceDtos, licPremSubSvcRelDto.getSvcCode()));
                    innerLicenceViewDataList = IaisCommonUtils.genNewArrayList();
                    innerLicenceViewData.setInnerLicenceViewDatas(innerLicenceViewDataList);
                    result.add(innerLicenceViewData);
                } else {
                    innerLicenceViewDataList.add(licPremSubSvcRelDto.getSvcCode());
                }
            }
        }
        return result;
    }

    @Override
    public LicenceViewDto getLicenceViewDtoByLicenceId(String licenceId) {
        LicenceViewDto licenceViewDto = licenceInboxClient.getAllStatusLicenceByLicenceId(licenceId).getEntity();
        List<LicPremisesScopeDto> licPremisesScopeDtos = licenceViewDto.getLicPremisesScopeDtos();
        List<LicPremSubSvcRelDto> licPremSubSvcRelDtos = licenceViewDto.getLicPremSubSvcRelDtos();
        List<HcsaSvcSpecifiedCorrelationDto> hcsaSvcSpecifiedCorrelationDtos = hcsaConfigClient.getHcsaSvcSpecifiedCorrelationDtos(licenceViewDto.getLicenceDto().getSvcName(),licenceViewDto.getPremisesType()).getEntity();
        List<InnerLicenceViewData> innerLicenceViewDataList = tidyInnerLicenceViewData(licPremisesScopeDtos, licPremSubSvcRelDtos,hcsaSvcSpecifiedCorrelationDtos);
        List<String> disciplinesSpecifieds = IaisCommonUtils.genNewArrayList();
        if (IaisCommonUtils.isNotEmpty(innerLicenceViewDataList)) {
            StringBuilder str = new StringBuilder();
            int eachPage = 14;
            for (int i = 0; i < innerLicenceViewDataList.size(); i++) {
                int d = (i + 1) % eachPage;
                str.append("<li>").append(innerLicenceViewDataList.get(i).getValue());
                List<String> innerLicenceViewDatas = innerLicenceViewDataList.get(i).getInnerLicenceViewDatas();
                if (IaisCommonUtils.isNotEmpty(innerLicenceViewDatas)) {
                    str.append("<br></br>");
                    for (int j = 0; j < innerLicenceViewDatas.size(); j++) {
                        str.append("- ").append(innerLicenceViewDatas.get(j));
                        if (j != innerLicenceViewDatas.size() - 1) {
                            str.append("<br></br>");
                        }
                    }
                }
                if (d == 0) {
                    str.append("</li>");
                    disciplinesSpecifieds.add(str.toString());
                    str = new StringBuilder();
                } else if (i == innerLicenceViewDataList.size() - 1) {
                    str.append("</li>");
                    disciplinesSpecifieds.add(str.toString());
                } else {
                    str.append("</li>");
                }
            }
        }
        licenceViewDto.setDisciplinesSpecifieds(disciplinesSpecifieds);
        return licenceViewDto;
    }

    @Override
    public LicenseeDto getLicenseeDtoBylicenseeId(String licenseeId) {
        return feUserClient.getLicenseeById(licenseeId).getEntity();
    }

    @Override
    public List<ApplicationSubDraftDto> getDraftByLicAppIdAndStatus(String licAppId, String status) {
        return appInboxClient.getDraftByLicAppIdAndStatus(licAppId, status).getEntity();
    }

    @Override
    public Map<String, Boolean> getMapCanInsp() {
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = null;
        try {
            hcsaSvcRoutingStageDtos = eicGatewayFeMainClient.getHcsaSvcRoutingStageDtoByStageId(
                    HcsaConsts.ROUTING_STAGE_INS).getEntity();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        Map<String, Boolean> map = IaisCommonUtils.genNewHashMap();
        if (IaisCommonUtils.isNotEmpty(hcsaSvcRoutingStageDtos)) {
            List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getActiveServices().getEntity();
            if (IaisCommonUtils.isNotEmpty(hcsaServiceDtos)) {
                for (HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtos) {
                    for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
                        if (hcsaServiceDto.getId().equalsIgnoreCase(hcsaSvcRoutingStageDto.getServiceId())) {
                            String key = hcsaSvcRoutingStageDto.getAppType() + "_" + hcsaServiceDto.getSvcCode();
                            map.putIfAbsent(key, Boolean.TRUE);
                            break;
                        }
                    }
                }
            }
        }
        return map;
    }

    @Override
    public Integer dssDraftNum(InterMessageSearchDto interMessageSearchDto) {
        if (StringUtil.isEmpty(interMessageSearchDto.getLicenseeId()) || IaisCommonUtils.isEmpty(
                interMessageSearchDto.getServiceCodes())) {
            return 0;
        }
        return licenceInboxClient.dssDraftNum(interMessageSearchDto).getEntity();
    }

    @Override
    public List<LicenceDto> getAllBundleLicences(List<String> licIds) {
        log.info(StringUtil.changeForLog("Lic Ids: " + licIds));
        if (IaisCommonUtils.isEmpty(licIds)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return licenceInboxClient.getAllBundleLicences(licIds).getEntity();
    }

}