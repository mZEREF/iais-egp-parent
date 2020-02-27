package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterQueryDto;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.SystemParameterService;
import com.ecquaria.cloud.moh.iais.service.client.ConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SystemParameterServiceImpl implements SystemParameterService {

    @Autowired
    private SystemClient systemClient;

    @Autowired
    private ConfigClient configClient;

    @Autowired
    private EicGatewayClient eicGatewayClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    @SearchTrack(catalog = "systemAdmin",key = "queryMessage")
    public SearchResult<SystemParameterQueryDto> doQuery(SearchParam param) {
        return systemClient.doQuery(param).getEntity();
    }

    @Override
    public void saveSystemParameter(SystemParameterDto dto) {
        log.info("save system parameter start....");
        try {
            dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            systemClient.saveSystemParameter(dto).getEntity();
        }catch (Exception e){
            log.error(e.getMessage());
        }

        try {
            //refresh spring config
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            int statusCode = eicGatewayClient.saveSystemParameterFe(dto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getStatusCode();
            configClient.refreshConfig();
        }catch (Exception e){
            log.error(e.getMessage());
        }

        log.info("save system parameter end....");
    }

    @Override
    public SystemParameterDto getParameterByPid(String pid) {
       return systemClient.getParameterByRowguid(pid).getEntity();
    }
}
