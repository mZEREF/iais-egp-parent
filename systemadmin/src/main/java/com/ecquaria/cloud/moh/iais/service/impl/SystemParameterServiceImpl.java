package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterQueryDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.SystemParameterService;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.IntranetUserClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;

@Service
@Slf4j
public class SystemParameterServiceImpl implements SystemParameterService {
    private final static HashMap<String, Long> propertiesBitIndex =  new HashMap<>();
    private final static String SYSTEM_PARAM_EDIT_OFFSET = "cache_system_param_edit_offset";

    @Autowired
    private SystemClient systemClient;

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

    @Autowired
    private RedisCacheHelper redisCacheHelper;

    @Override
    @SearchTrack(catalog = "systemAdmin",key = "querySystemParam")
    public SearchResult<SystemParameterQueryDto> doQuery(SearchParam param) {
        initPropertyKeyOffset();
        return systemClient.doQuery(param).getEntity();
    }

    @Override
    public void saveSystemParameter(SystemParameterDto dto) {
        log.info(StringUtil.changeForLog("test val" + val));
        log.info("save system parameter start....");
        dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        dto.setModifiedAt(new Date());
        SystemParameterDto postUpdate = systemClient.saveSystemParameter(dto).getEntity();
        if (postUpdate != null){
            log.debug(StringUtil.changeForLog("go to update fe param =========>>>>>>>>>>>>>>>>>" + JsonUtil.parseToJson(postUpdate)));
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
                log.error(StringUtil.changeForLog("encounter failure when sync parameter to fe " + e.getMessage()), e);
            }
        }


        log.info("save system parameter end....");
    }

    public void callEicCreateSystemParameter(SystemParameterDto systemParameterDto){
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        systemParameterDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
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

    @Override
    public void initPropertyKeyOffset() {
        if(IaisCommonUtils.isEmpty(propertiesBitIndex)){
            Long index = 0L;
            Class clz = SystemParamConfig.class;
            Field[] fields = clz.getDeclaredFields();
            for (Field f : fields){
                Value value = f.getAnnotation(Value.class);
                if (value != null){
                    String propertyKey = value.value();
                    if (StringUtil.isNotEmpty(propertyKey)){
                        propertyKey = propertyKey.replace("${", "").replace("}", "");
                        log.debug(StringUtil.changeForLog("offset PropertyKey" + propertyKey));
                        propertiesBitIndex.put(propertyKey, index++);
                    }
                }
            }
        }
    }

    @Override
    public boolean getPropertyOffsetStatus(String propertyKey) {
        if (propertyKey == null || StringUtil.isEmpty(propertyKey)){
            throw new NullPointerException();
        }

        long offset = propertiesBitIndex.get(propertyKey);
        return redisCacheHelper.getBitValue(SYSTEM_PARAM_EDIT_OFFSET, offset);
    }

    @Override
    public void setPropertyOffset(String propertyKey, boolean flag) {
        if (propertyKey == null || StringUtil.isEmpty(propertyKey)){
            throw new NullPointerException();
        }

        long offset = propertiesBitIndex.get(propertyKey);
        //cache 5 minute
        redisCacheHelper.setBit(SYSTEM_PARAM_EDIT_OFFSET, offset, flag, 300L);
    }
}
