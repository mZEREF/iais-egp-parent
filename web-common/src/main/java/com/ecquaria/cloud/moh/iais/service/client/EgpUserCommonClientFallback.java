package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.role.Role;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2019/12/31 9:27
 */
public class EgpUserCommonClientFallback implements EgpUserCommonClient {
    @Override
    public FeignResponseEntity<List<Role>> search(Map<String, String> map) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
