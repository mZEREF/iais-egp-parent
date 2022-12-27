package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.EgpUserRoleDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

/**
 * @author yichen
 * @Date:2020/8/20
 */

public class FEMainRbacClientFallback implements FEMainRbacClient {

    @Override
    public FeignResponseEntity<Boolean> deleteUerRoleIds(String var1, String var2, String... var3) {
        return IaisEGPHelper.getFeignResponseEntity("deleteUerRoleIds", var1);
    }

    @Override
    public FeignResponseEntity<String> createUerRoleIds(EgpUserRoleDto var1) {
        return IaisEGPHelper.getFeignResponseEntity("createUerRoleIds", var1);
    }

}
