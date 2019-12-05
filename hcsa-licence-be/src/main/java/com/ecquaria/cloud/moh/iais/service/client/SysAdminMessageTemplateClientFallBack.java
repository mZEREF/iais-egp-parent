package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * SysAdminMessageTemplateClientFallBack
 *
 * @author junyu
 * @date 2019/12/4
 */
public class SysAdminMessageTemplateClientFallBack {
    public FeignResponseEntity<InspectionEmailTemplateDto> loadingEmailTemplate(String id ){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
