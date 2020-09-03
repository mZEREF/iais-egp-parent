package com.ecquaria.cloud.moh.iais.util;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;

/**
 * EicUtil
 *
 * @author suocheng
 * @date 9/3/2020
 */
@Slf4j
public final class EicUtil {
    public static <T> T getObjectApp(AppEicRequestTrackingDto appEicRequestTrackingDto, Class<T> cls){
        T result = null;
        if(appEicRequestTrackingDto!=null){
            ObjectMapper mapper = new ObjectMapper();
            try {
                result = mapper.readValue(appEicRequestTrackingDto.getDtoObj(), cls);
            } catch (IOException e) {
                log.error(StringUtil.changeForLog(e.getMessage()),e);
            }
        }
        return  result;
    }
}
