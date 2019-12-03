package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * ComSystemAdminClientFallback
 *
 * @author Jinhua
 * @date 2019/12/3 9:58
 */
public class ComSystemAdminClientFallback {
    public FeignResponseEntity<OrgUserDto> retrieveOrgUserAccount(String userId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
