package com.ecquaria.cloud.moh.iais.util;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
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
    public static <T> T getObjectApp(EicRequestTrackingDto appEicRequestTrackingDto, Class<T> cls){
        log.info(StringUtil.changeForLog("The getObjectApp start ..."));
        T result = null;
        if(appEicRequestTrackingDto!=null){
            log.info(StringUtil.changeForLog("The getObjectApp appEicRequestTrackingDto.getId() is -->:"
                    +appEicRequestTrackingDto.getId()));
            ObjectMapper mapper = new ObjectMapper();
            try {
                String content = appEicRequestTrackingDto.getDtoObject();
                log.info(StringUtil.changeForLog("The getObjectApp content is -->:"+content));
                result = mapper.readValue(content, cls);
            } catch (IOException e) {
                log.debug(StringUtil.changeForLog(e.getMessage()),e);
            }
        }
        log.info(StringUtil.changeForLog("The getObjectApp end ..."));
        return  result;
    }
}
