package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.MohUenDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

/**
 * UenManagementClientFallback
 *
 * @author junyu
 * @date 2020/1/22
 */
public class UenManagementClientFallback {
    public FeignResponseEntity<MohUenDto> getMohUenById(String uenNo){
        return IaisEGPHelper.getFeignResponseEntity("getMohUenById",uenNo);
    }


    public FeignResponseEntity<MohUenDto>  generatesMohIssuedUen(MohUenDto mohUenDto){
        return IaisEGPHelper.getFeignResponseEntity("generatesMohIssuedUen",mohUenDto);
    }

}
