package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeAdminDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeAdminQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

public class FeAdminClientFallback implements FeAdminClient{

    @Override
    public FeignResponseEntity<SearchResult<FeAdminQueryDto>> getFeAdminList(SearchParam searchParam) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<OrganizationDto> getOrganizationById(String id) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<FeAdminDto> addAdminAccount(FeAdminDto feAdminDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<String> ChangeActiveStatus(String id, String targetStatus) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
