package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxMsgMaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import com.ecquaria.cloud.moh.iais.service.client.AppInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailMainClient;
import com.ecquaria.cloud.moh.iais.service.client.ConfigInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayFeMainClient;
import com.ecquaria.cloud.moh.iais.service.client.FeUserClient;
import com.ecquaria.cloud.moh.iais.service.client.InboxClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    private static final String ACTIVE ="CMSTAT001";

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
    public SearchResult<InboxAppQueryDto> appDoQuery(SearchParam searchParam) {
        return appInboxClient.searchResultFromApp(searchParam).getEntity();
    }

    @Override
    public SearchResult<InboxQueryDto> inboxDoQuery(SearchParam searchParam) {
        return inboxClient.searchInbox(searchParam).getEntity();
    }

    @Override
    public SearchResult<InboxLicenceQueryDto> licenceDoQuery(SearchParam searchParam) {
        return licenceInboxClient.searchResultFromLicence(searchParam).getEntity();
    }

    @Override
    public Integer licActiveStatusNum(String licenseeId) {
        return licenceInboxClient.getLicActiveStatusNum(licenseeId).getEntity();
    }

    @Override
    public Integer appDraftNum(String licenseeId) {
        return appInboxClient.getAppDraftNum(licenseeId).getEntity();
    }

    @Override
    public Integer unreadAndUnresponseNum(String userId) {
        return inboxClient.searchUnreadAndUnresponseNum(userId).getEntity();
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
    public Boolean recallApplication(RecallApplicationDto recallApplicationDto) {
        boolean result = false;
        List<String> refNoList = IaisCommonUtils.genNewArrayList();
        String appId = recallApplicationDto.getAppId();
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtoList = appInboxClient.listAppPremisesCorrelation(appId).getEntity();
        for (AppPremisesCorrelationDto appPremisesCorrelationDto:appPremisesCorrelationDtoList
             ) {
            refNoList.add(appPremisesCorrelationDto.getId());
        }
        recallApplicationDto.setRefNo(refNoList);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        try {
            recallApplicationDto = eicGatewayFeMainClient.recallAppChangeTask(recallApplicationDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return result;
        }
        if (recallApplicationDto.getResult()){
            try {
                result = eicGatewayFeMainClient.updateApplicationStatus(recallApplicationDto, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization()).getEntity();
            }catch (Exception e){
                log.error(e.getMessage(),e);
                return result;
            }
        }
        if(result){
            appInboxClient.updateFeAppStatus(recallApplicationDto.getAppId(),ApplicationConsts.APPLICATION_STATUS_RECALLED);
            String draftNo = appInboxClient.getDraftNumber(recallApplicationDto.getAppNo()).getEntity();
            appInboxClient.updateDraftStatus(draftNo,ACTIVE).getEntity();
        }
        return result;
    }

    @Override
    public Map<String,String> checkRenewalStatus(String licenceId) {
        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
        boolean flag = true;
        List<ApplicationDto> apps = appInboxClient.getAppByLicIdAndExcludeNew(licenceId).getEntity();
        for(ApplicationDto app : apps){
            if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(app.getApplicationType())
                    && !(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT.equals(app.getStatus()))
                    && !(ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(app.getStatus()))){
                flag = false;
                errorMap.put("errorMessage1","This application is performing the renew process");
            }
        }
        //check expiry date
        LicenceDto licenceDto = licenceInboxClient.getLicBylicId(licenceId).getEntity();
        Date expiryDate = licenceDto.getExpiryDate();
        Date nowDate = new Date();

        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(nowDate);

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(expiryDate);
        int daysBetween = MiscUtil.getDaysBetween(startCalendar,endCalendar);
        if(nowDate.before(expiryDate) && daysBetween > 180){
            flag = flag && false;
            errorMap.put("errorMessage",licenceDto.getLicenceNo());
        }
        return errorMap;
    }

    @Override
    public void updateMsgStatusToRead(String msgId) {
        inboxClient.updateMsgStatusToRead(msgId);
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
    public AuditTrailDto getLastLoginInfo(String loginUserId) {
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        AuditTrailDto loginDto = auditTrailMainClient.getLastLoginInfo(loginUserId).getEntity();
        if (loginDto != null) {
            auditTrailDto.setActionTime(loginDto.getActionTime());
            String sessionId = loginDto.getSessionId();
            AuditTrailDto actDto = auditTrailMainClient.getLastAction(sessionId).getEntity();
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
        List<ApplicationDto> apps = appInboxClient.getAppByLicIdAndExcludeNew(licenceId).getEntity();
        if(!IaisCommonUtils.isEmpty(apps)){
            for(ApplicationDto applicationDto:apps){
                if(!ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED.equals(applicationDto.getStatus()) &&
                        !ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(applicationDto.getStatus())){
                    errorMap.put("errorMessage","There is already a pending application for this licence");
                    break;
                }
            }
        }
        return errorMap;
    }
}