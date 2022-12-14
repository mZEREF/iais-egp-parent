package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

/**
 * @author Wenkang
 * @date 2019/11/28 10:28
 */
public class SystemClientBeLicFallback {

    public FeignResponseEntity<InspectionEmailTemplateDto> loadingEmailTemplate(String id ){
        return IaisEGPHelper.getFeignResponseEntity("loadingEmailTemplate",id);
    }
}
