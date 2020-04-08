package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * @Author: yichen
 * @Description:
 * @Date:2020/4/8
 **/

public class FeMainEgpUserClientFallBack implements FeMainEgpUserClient {

    @Override
    public FeignResponseEntity<ClientUser> createClientUser(ClientUser var1) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
