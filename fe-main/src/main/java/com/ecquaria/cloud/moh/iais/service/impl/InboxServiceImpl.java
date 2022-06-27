package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.*;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxMsgMaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageSearchDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HalpStringUtils;
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
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        return  interInboxUserDto;
    }

    @Override
    @SearchTrack(catalog = "interInboxQuery", key = "assessmentWithdrawAppQuery")
    public SearchResult<InboxAppQueryDto> appDoQuery(SearchParam searchParam) {
        SearchResult<InboxAppQueryDto> inboxAppQueryDtoSearchResult = appInboxClient.searchResultFromApp(searchParam).getEntity();
        List<InboxAppQueryDto> inboxAppQueryDtoList = inboxAppQueryDtoSearchResult.getRows();
        for (InboxAppQueryDto inboxAppQueryDto:inboxAppQueryDtoList) {
            if (ApplicationConsts.APPLICATION_STATUS_DRAFT.equals(inboxAppQueryDto.getStatus()) || ApplicationConsts.APPLICATION_STATUS_DRAFT_PENDING.equals(inboxAppQueryDto.getStatus())){
                ApplicationDraftDto applicationDraftDto = appInboxClient.getDraftInfo(inboxAppQueryDto.getId()).getEntity();
                inboxAppQueryDto.setServiceId(HalpStringUtils.splitServiceName(applicationDraftDto.getServiceCode()));
            }else{
                if(!inboxAppQueryDto.getServiceId().isEmpty()){
                    inboxAppQueryDto.setServiceId(getServiceNameById(inboxAppQueryDto.getServiceId()));
                }else{
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
        for (InboxQueryDto inboxQueryDto:inboxAppQueryDtoListRows) {
            inboxQueryDto.setServiceCodes(HalpStringUtils.splitServiceName(inboxQueryDto.getServiceCodes()));
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
        appInboxClient.updateDraftStatus(draftNo,status).getEntity();
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
        for (AppPremisesCorrelationDto appPremisesCorrelationDto:appPremisesCorrelationDtoList
                ) {
            refNoList.add(appPremisesCorrelationDto.getId());
        }
        recallApplicationDto.setRefNo(refNoList);
        try {
            recallApplicationDto = eicGatewayFeMainClient.callEicWithTrackForOrg(recallApplicationDto, eicGatewayFeMainClient::recallAppChangeTask,
                    "recallAppChangeTask").getEntity();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return recallApplicationDto.getResult();
    }

    @Override
    public List<RecallApplicationDto> canRecallApplications(List<RecallApplicationDto> recallApplicationDtos) {
        List<RecallApplicationDto> recallApplicationDtoList = IaisCommonUtils.genNewArrayList();
        for (RecallApplicationDto h:recallApplicationDtos
             ) {
            String appId = h.getAppId();
            List<String> refNoList = IaisCommonUtils.genNewArrayList();
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtoList = appInboxClient.listAppPremisesCorrelation(appId).getEntity();
            for (AppPremisesCorrelationDto appPremisesCorrelationDto:appPremisesCorrelationDtoList
                    ) {
                refNoList.add(appPremisesCorrelationDto.getId());
            }
            h.setRefNo(refNoList);
        }
        try {
            recallApplicationDtoList = eicGatewayFeMainClient.callEicWithTrackForOrg(recallApplicationDtos, eicGatewayFeMainClient::recallAppTasks,
                    this.getClass(), "recallAppTasksEic").getEntity();
        }catch (Exception e){
            log.error(e.getMessage(),e);
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
        if (applicationDto != null){
            ApplicationGroupDto applicationGroupDto = appInboxClient.getApplicationGroup(applicationDto.getAppGrpId()).getEntity();
            if (ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED.equals(applicationGroupDto.getStatus())){
                result = true;
                recallApplicationDto.setResult(result);
                recallApplicationDto.setMessage("RECALLMSG002");
                appInboxClient.updateFeAppStatus(recallApplicationDto.getAppId(),ApplicationConsts.APPLICATION_STATUS_RECALLED);
                return recallApplicationDto;
            }
        }
        List<String> refNoList = IaisCommonUtils.genNewArrayList();
        String appId = recallApplicationDto.getAppId();
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtoList = appInboxClient.listAppPremisesCorrelation(appId).getEntity();
        for (AppPremisesCorrelationDto appPremisesCorrelationDto:appPremisesCorrelationDtoList
             ) {
            refNoList.add(appPremisesCorrelationDto.getId());
        }
        recallApplicationDto.setRefNo(refNoList);
        try {
            recallApplicationDto = eicGatewayFeMainClient.callEicWithTrackForOrg(recallApplicationDto, eicGatewayFeMainClient::recallAppChangeTask,
                    "recallAppChangeTask").getEntity();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        if (recallApplicationDto.getResult()){
            try {
                result = eicGatewayFeMainClient.callEicWithTrackForApp(recallApplicationDto,
                        eicGatewayFeMainClient::updateApplicationStatus, "updateApplicationStatus").getEntity();
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }
        if(result || "RECALLMSG002".equals(recallApplicationDto.getMessage()) || "RECALLMSG003".equals(recallApplicationDto.getMessage())){
            appInboxClient.updateFeAppStatus(recallApplicationDto.getAppId(),ApplicationConsts.APPLICATION_STATUS_RECALLED);
        }
        return recallApplicationDto;
    }

    @Override
    public Map<String,String> checkRenewalStatus(String licenceId) {
        log.info(StringUtil.changeForLog("----------checkRenewalStatus licenceId : " + licenceId));
        LicenceDto licenceDto = licenceInboxClient.getLicDtoById(licenceId).getEntity();
        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
        String errorMsgEleven = MessageUtil.getMessageDesc("INBOX_ACK011");
        if(licenceDto != null){
            String licenceStatus = licenceDto.getStatus();
            if(!ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceStatus)){
                if(!(IaisEGPHelper.isActiveMigrated()&&ApplicationConsts.LICENCE_STATUS_APPROVED.equals(licenceStatus)&&licenceDto.getMigrated()!=0)){
                    errorMap.put("errorMessage2",errorMsgEleven);
                }
            }
        }else{
            errorMap.put("errorMessage2",errorMsgEleven);
            return errorMap;
        }
        List<ApplicationDto> apps = appInboxClient.getAppByLicIdAndExcludeNew(licenceId).getEntity();
        List<String> finalStatusList = IaisCommonUtils.getAppFinalStatus();
        boolean hasError = false;
        if(!IaisCommonUtils.isEmpty(apps)) {
            List<String> appGrpIds = IaisCommonUtils.genNewArrayList();
            for (ApplicationDto app : apps) {
                String status = app.getStatus();
                if (!finalStatusList.contains(status)) {
                    errorMap.put("errorMessage1", "This application is performing the renew process");
                    hasError = true;
                }
                // 81903
                if (ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED.equals(status)
                        && ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(app.getApplicationType())) {
                    errorMap.put("errorMessage2", MessageUtil.getMessageDesc("INBOX_ACK013"));
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
                        errorMap.put("errorMessage2", MessageUtil.getMessageDesc("GENERAL_ERR0062"));
                    }
                }
            }
        }
        //appeal
        Boolean appealFlag = appInboxClient.isLiscenceAppealOrCessation(licenceId).getEntity();
        if(!appealFlag){
            String errorMsg = MessageUtil.getMessageDesc("INBOX_ACK010");
            errorMap.put("errorMessage2",errorMsg);
        }
        //Verify whether the new licence is generated
        LicenceDto entity = licenceInboxClient.getRootLicenceDtoByOrgId(licenceId).getEntity();
        if(entity != null){
            boolean isRenewApp = false;
            apps = appInboxClient.getAppByLicIdAndExcludeNew(entity.getId()).getEntity();
            if(IaisCommonUtils.isNotEmpty(apps)){
                log.info(StringUtil.changeForLog("----------checkRenewalStatus json : " + JsonUtil.parseToJson(apps)));
                for (ApplicationDto a : apps) {
                    if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(a.getApplicationType())
                            && !ApplicationConsts.APPLICATION_STATUS_REJECTED.equalsIgnoreCase(a.getStatus())
                            && !ApplicationConsts.APPLICATION_STATUS_ROLL_BACK.equals(a.getStatus())
                            && !ApplicationConsts.APPLICATION_STATUS_DELETED.equals(a.getStatus())){
                        isRenewApp = true;
                        break;
                    }
                }
            }
            if(isRenewApp){
                String errorMsg = MessageUtil.getMessageDesc("INBOX_ACK013");
                errorMap.put("errorMessage2",errorMsg);
            }else {
                errorMap.put("errorMessage",licenceDto.getLicenceNo());
            }
        }
        //check expiry date
        Date expiryDate = licenceDto.getExpiryDate();
        Date nowDate = new Date();

        //expiryDate
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(expiryDate);

        //licence expiry date  day - 6 months
        endCalendar.add(Calendar.MONTH,-6);

        Date firstStartRenewTime = endCalendar.getTime();
        if(!(nowDate.after(firstStartRenewTime) && nowDate.before(expiryDate))){
            errorMap.put("errorMessage",licenceDto.getLicenceNo());
        }

//        int daysBetween = MiscUtil.getDaysBetween(startCalendar,endCalendar);
//        if(nowDate.before(expiryDate) && daysBetween > 180){
//            errorMap.put("errorMessage",licenceDto.getLicenceNo());
//        }
        log.info(StringUtil.changeForLog(" ----------checkRenewalStatus errorMap :" + JsonUtil.parseToJson(errorMap)));
        return errorMap;
    }

    @Override
    public void updateMsgStatusTo(String msgId,String msgStatus) {
        inboxClient.updateMsgStatusTo(msgId,msgStatus);
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
        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
        LicenceDto licenceDto = licenceInboxClient.getLicDtoById(licenceId).getEntity();
        boolean isActive = false;
        if(licenceDto != null ){
            if( ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus())){
                isActive=true;
            }
            if(IaisEGPHelper.isActiveMigrated() &&ApplicationConsts.LICENCE_STATUS_APPROVED.equals(licenceDto.getStatus())&&licenceDto.getMigrated()!=0){
                isActive=true;
            }
        }
        if(isActive){
            List<ApplicationDto> apps = appInboxClient.getAppByLicIdAndExcludeNew(licenceId).getEntity();
            List<String> finalStatusList = IaisCommonUtils.getAppFinalStatus();
            boolean hasError = false;
            if (!IaisCommonUtils.isEmpty(apps)) {
                List<String> appGrpIds = IaisCommonUtils.genNewArrayList();
                for (ApplicationDto app : apps) {
                    String status = app.getStatus();
                    if(!finalStatusList.contains(status)){
                        String message = MessageUtil.getMessageDesc("RFC_ERR011");
                        errorMap.put("errorMessage",message);
                        hasError = true;
                    }
                    if (ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED.equals(status)
                            && ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(app.getApplicationType())) {
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
                        }
                    }
                }
            }
            Boolean entity = appInboxClient.isLiscenceAppealOrCessation(licenceId).getEntity();
            if(!entity){
                errorMap.put("errorMessage",MessageUtil.getMessageDesc("INBOX_ACK010"));
            }
            List<LicenceDto> licenceDtos = licenceInboxClient.getLicenceDtosByLicenseeId(licenceDto.getLicenseeId()).getEntity();
            for (LicenceDto licenceDto1 : licenceDtos){
                if (ApplicationConsts.LICENCE_STATUS_APPROVED.equals(licenceDto1.getStatus()) &&
                        StringUtil.isNotEmpty(licenceDto.getSvcName()) &&
                        licenceDto.getSvcName().equals(licenceDto1.getSvcName()) &&
                        StringUtil.isNotEmpty(licenceDto1.getOriginLicenceId()) &&
                        licenceDto1.getOriginLicenceId().equals(licenceDto.getId())){
                    errorMap.put("errorMessage",MessageUtil.getMessageDesc("INBOX_ACK025"));
                }
            }
        }else{
            errorMap.put("errorMessage",MessageUtil.getMessageDesc("INBOX_ACK011"));
        }
        return errorMap;
    }

    @Override
    public Map<String, Boolean> listResultCeased(List<String> licIds) {
        Map<String, Boolean> map = IaisCommonUtils.genNewHashMap();
        for(String licId : licIds){
            LicenceDto licenceDto = licenceInboxClient.getLicDtoById(licId).getEntity();
            if(licenceDto==null){
                map.put(licId,Boolean.FALSE);
                return map;
            }else {
                if( !ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus())){
                    if(!(IaisEGPHelper.isActiveMigrated() &&ApplicationConsts.LICENCE_STATUS_APPROVED.equals(licenceDto.getStatus())&&licenceDto.getMigrated()!=0)){
                        map.put(licId,Boolean.FALSE);
                        return map;
                    }
                }
            }
        }
        return appInboxClient.listCanCeased(licIds).getEntity();
    }

    @Override
    public Map<String, String> appealIsApprove(String licenceId, String type) {
        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
        List<String> endStatusList = IaisCommonUtils.getAppFinalStatus();
        endStatusList.add("APST005");
        if("licence".equals(type)){
            List<ApplicationDto> apps = appInboxClient.getAppByLicIdAndExcludeNew(licenceId).getEntity();
            if(!IaisCommonUtils.isEmpty(apps)){
                for(ApplicationDto applicationDto:apps){
                    if(!endStatusList.contains(applicationDto.getStatus())){
                        errorMap.put("errorMessage", MessageUtil.getMessageDesc("RFC_ERR011"));
                        break;
                    }
                }
            }
            return errorMap;

        }else if("application".equals(type)){
            ApplicationDto applicationDto = appInboxClient.getApplicarionById(licenceId).getEntity();
            if(!endStatusList.contains(applicationDto.getStatus())){
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

    @Override
    public LicenceViewDto getLicenceViewDtoByLicenceId(String licenceId) {
        LicenceViewDto licenceViewDto =  licenceInboxClient.getAllStatusLicenceByLicenceId(licenceId).getEntity();
//        if(licenceViewDto!=null){
//            LicenceDto licenceDto = licenceViewDto.getLicenceDto();
//            String licenseeId = licenceDto.getLicenseeId();
//            LicenseeDto licenseeDto = this.getLicenseeDtoBylicenseeId(licenseeId);
//            licenceViewDto.setLicenseeDto(licenseeDto);
//        }
        return licenceViewDto;
    }

    @Override
    public LicenseeDto getLicenseeDtoBylicenseeId(String licenseeId) {
        return feUserClient.getLicenseeById(licenseeId).getEntity();
    }

    @Override
    public List<ApplicationSubDraftDto> getDraftByLicAppIdAndStatus(String licAppId, String status) {
        return appInboxClient.getDraftByLicAppIdAndStatus(licAppId,status).getEntity();
    }

    @Override
    public Map<String, Boolean> getMapCanInsp() {
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = null;
        try {
            hcsaSvcRoutingStageDtos = eicGatewayFeMainClient.getHcsaSvcRoutingStageDtoByStageId(HcsaConsts.ROUTING_STAGE_INS).getEntity();
        }catch (Exception e){
         log.error(e.getMessage(),e);
        }
        Map<String, Boolean> map = IaisCommonUtils.genNewHashMap();
        if(IaisCommonUtils.isNotEmpty(hcsaSvcRoutingStageDtos)){
            List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getActiveServices().getEntity();
            if(IaisCommonUtils.isNotEmpty(hcsaServiceDtos)){
                for(HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtos){
                    for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
                        if(hcsaServiceDto.getId().equalsIgnoreCase(hcsaSvcRoutingStageDto.getServiceId())){
                            String key = hcsaSvcRoutingStageDto.getAppType() +"_"+hcsaServiceDto.getSvcCode();
                            if(map.get(key) == null){
                                map.put(key,Boolean.TRUE);
                            }
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
        if(StringUtil.isEmpty(interMessageSearchDto.getLicenseeId()) || IaisCommonUtils.isEmpty(interMessageSearchDto.getServiceCodes())){
            return 0;
        }
        return licenceInboxClient.dssDraftNum(interMessageSearchDto).getEntity();
    }
}