package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.client.rbac.ClientUser;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import sop.rbac.user.UserIdentifier;

/**
 * @author weilu
 * @date 2019/12/31 9:27
 */
public class EgpUserClientFallback implements EgpUserClient {
    @Override
    public FeignResponseEntity<ClientUser> createClientUser(ClientUser var1) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<ClientUser> updateClientUser(ClientUser var1) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<Boolean> deleteUser(String var1, String var2) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<ClientUser> getUserByIdentifier(String var1, String var2) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<Boolean> validatepassword(String var1, UserIdentifier var2) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
