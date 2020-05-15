package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.SystemParameterService;
import com.ecquaria.cloud.moh.iais.service.client.ConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.IntranetUserClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class SystemParameterServiceImpl implements SystemParameterService {

    @Autowired
    private SystemClient systemClient;

    @Autowired
    private ConfigClient configClient;

    @Autowired
    private IntranetUserClient userClient;

    @Autowired
    private EicGatewayClient eicGatewayClient;

    @Autowired
    private EicRequestTrackingHelper trackingHelper;

    @Value("${iais.system.paging.size}")
    private String val;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;

    @Override
    @SearchTrack(catalog = "systemAdmin",key = "queryMessage")
    public SearchResult<SystemParameterQueryDto> doQuery(SearchParam param) {
        return systemClient.doQuery(param).getEntity();
    }

    @Override
    public void saveSystemParameter(SystemParameterDto dto) {
        log.info("test val" + val);
        log.info("save system parameter start....");
        dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        SystemParameterDto postUpdate = systemClient.saveSystemParameter(dto).getEntity();

        EicRequestTrackingDto postSaveTrack = trackingHelper.clientSaveEicRequestTracking(EicClientConstant.SYSTEM_ADMIN_CLIENT, SystemParameterServiceImpl.class.getName(),
                "callEicCreateSystemParameter", currentApp + "-" + currentDomain,
                SystemParameterDto.class.getName(), JsonUtil.parseToJson(postUpdate));

        try {
            FeignResponseEntity<EicRequestTrackingDto> fetchResult = trackingHelper.getEicClient().getPendingRecordByReferenceNumber(postSaveTrack.getRefNo());
            if (HttpStatus.SC_OK == fetchResult.getStatusCode()) {
                EicRequestTrackingDto entity = fetchResult.getEntity();
                if (AppConsts.EIC_STATUS_PENDING_PROCESSING.equals(entity.getStatus())){
                    callEicCreateSystemParameter(postUpdate);
                    entity.setProcessNum(1);
                    Date now = new Date();
                    entity.setFirstActionAt(now);
                    entity.setLastActionAt(now);
                    entity.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                    trackingHelper.getOrgTrackingClient().saveEicTrack(entity);
                }
            }

        }catch (Exception e){
            log.error(StringUtil.changeForLog("encounter failure when sync parameter to fe " + e.getMessage()));
        }

        try {
            //refresh spring config
            configClient.refreshConfig();
        }catch (Exception e){
            log.error(StringUtil.changeForLog("encounter failure when refresh spring config  " + e.getMessage()), e);
        }

        log.info("save system parameter end....");
    }

    private void callEicCreateSystemParameter(SystemParameterDto systemParameterDto){
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        eicGatewayClient.saveSystemParameterFe(systemParameterDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }


    @Override
    public SystemParameterDto getParameterByPid(String pid) {
       return systemClient.getParameterByRowguid(pid).getEntity();
    }

    @Override
    public OrgUserDto retrieveOrgUserAccountById(String userId) {
        return userClient.findIntranetUserById(userId).getEntity();
    }
}
