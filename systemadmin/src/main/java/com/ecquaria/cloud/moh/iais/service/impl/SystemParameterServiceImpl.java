package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterQueryDto;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.SystemParameterService;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.IntranetUserClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class SystemParameterServiceImpl implements SystemParameterService {

    private final static String SYSTEM_PARAM_EDIT_OFFSET = "cache_system_param_edit_offset";

    private static final ConcurrentHashMap<String, Long> propertiesBitIndex =  new ConcurrentHashMap(10);

    @Autowired
    private SystemClient systemClient;

    @Autowired
    private IntranetUserClient userClient;

    @Autowired
    private EicGatewayClient eicGatewayClient;

    @Value("${iais.system.paging.size}")
    private String val;

    @Autowired
    private RedisCacheHelper redisCacheHelper;

    static {
        Long index = 0L;
        Class clz = SystemParamConfig.class;
        Field[] fields = clz.getDeclaredFields();
        for (Field f : fields){
            Value value = f.getAnnotation(Value.class);
            if (value != null){
                String propertyKey = value.value();
                if (StringUtil.isNotEmpty(propertyKey)){
                    propertyKey = propertyKey.replace("${", "").replace("}", "");
                    // log.debug(StringUtil.changeForLog("offset PropertyKey" + propertyKey));
                    propertiesBitIndex.put(propertyKey, index++);
                }
            }
        }
    }

    @Override
    @SearchTrack(catalog = "systemAdmin",key = "querySystemParam")
    public SearchResult<SystemParameterQueryDto> doQuery(SearchParam param) {
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
            eicGatewayClient.callEicWithTrack(postUpdate, eicGatewayClient::saveSystemParameterFe, EicGatewayClient.class,
                    "saveSystemParameterFe");
        }

        log.info("save system parameter end....");
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
    public boolean getPropertyOffsetStatus(String propertyKey) {
        if (propertyKey == null || StringUtil.isEmpty(propertyKey)){
            throw new NullPointerException();
        }

        try {
            long offset = propertiesBitIndex.get(propertyKey);
            return redisCacheHelper.getBitValue(SYSTEM_PARAM_EDIT_OFFSET, offset);
        }catch (NullPointerException e){
            log.error("don't have init this properties..");
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void setPropertyOffset(String propertyKey, boolean flag) {
        if (propertyKey == null || StringUtil.isEmpty(propertyKey)){
            throw new NullPointerException();
        }

        try {
            long offset = propertiesBitIndex.get(propertyKey);
            //cache 5 minute
            redisCacheHelper.setBit(SYSTEM_PARAM_EDIT_OFFSET, offset, flag, 300L);
        }catch (NullPointerException e){
            log.error("don't have init this properties..");
            log.error(e.getMessage(), e);
        }
    }

}
