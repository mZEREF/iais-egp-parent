package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.service.client.IaisSystemClient;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author yichen
 * @Date:2020/12/30
 */

@Slf4j
public class SystemParamHelper {
    private SystemParamHelper(){throw new IllegalStateException("Util class");}

    /**
     * use for <iais:select></iais:select> tag
     * @param propertiesKey
     * @return
     */
    public static Object getConfigValueByKey(String propertiesKey){
        IaisSystemClient isc = SpringContextHelper.getContext().getBean(IaisSystemClient.class);
        List<SystemParameterDto> spList;
        if (isc != null && (spList = isc.receiveAllSystemParam().getEntity()) != null){
            for (SystemParameterDto sp : spList){
                if (propertiesKey.equals(sp.getPropertiesKey())){
                    return sp.getValue();
                }
            }
        }

        return null;
    }
}
