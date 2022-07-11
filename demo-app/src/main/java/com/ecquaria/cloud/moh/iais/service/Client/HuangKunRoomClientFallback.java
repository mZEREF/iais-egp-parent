package com.ecquaria.cloud.moh.iais.service.Client;

import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;

/**
 * @ClassName: RoomClientFallback
 * @author: haungkun
 * @date: 2022/7/8 17:07
 */
@Slf4j
public class HuangKunRoomClientFallback {

    private <T> FeignResponseEntity<T> getFeignResponseEntity(Object... objs) {
        log.warn(StringUtil.changeForLog("Params: " + Arrays.toString(objs)));
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

}
