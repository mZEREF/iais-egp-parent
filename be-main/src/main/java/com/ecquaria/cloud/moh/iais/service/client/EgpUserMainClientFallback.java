package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.role.Role;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2019/12/31 9:27
 */
public class EgpUserMainClientFallback implements EgpUserMainClient {

    @Override
    public FeignResponseEntity<List<Role>> search(Map<String, String> map) {
        return IaisEGPHelper.getFeignResponseEntity("search",map);
    }
}
