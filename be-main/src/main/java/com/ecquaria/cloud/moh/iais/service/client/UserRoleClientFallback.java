package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * @author guyin
 * @date 2020/1/22 15:57
 */
public class UserRoleClientFallback implements UserRoleClient {

    @Override
    public FeignResponseEntity<Void> setAvailable(List<String> user) {
        return IaisEGPHelper.getFeignResponseEntity("setAvailable",user);
    }

    @Override
    public FeignResponseEntity<String> getAvailable(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getAvailable",id);
    }
}
