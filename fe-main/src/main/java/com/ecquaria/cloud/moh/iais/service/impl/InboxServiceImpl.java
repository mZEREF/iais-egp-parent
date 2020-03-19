package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

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
            String draftNo = appInboxClient.getDraftNumber(recallApplicationDto.getAppNo()).getEntity();
            appInboxClient.updateDraftStatus(draftNo,ACTIVE).getEntity();
        }
        return result;
    }
}