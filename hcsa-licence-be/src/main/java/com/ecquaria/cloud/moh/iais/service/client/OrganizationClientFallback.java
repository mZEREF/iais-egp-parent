package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/12/4 15:13
 */
public class OrganizationClientFallback {
    FeignResponseEntity<List<OrgUserDto>> retrieveOrgUserAccount(List<String> ids){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
