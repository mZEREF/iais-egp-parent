package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.QueryHelperResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.PostInsGroupDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class LicenceInFallback implements LicenceClient {


    @Override
    public FeignResponseEntity<LicenceDto> getLicenceByAppId(String appId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<LicenceViewDto> getLicenceViewDtoByLicPremCorrId(String licPremCorrId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<LicenceDto> getLicBylicId(String licenceId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<QueryHelperResultDto> doQuery(String sql) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<Void> doDeleteBySql(String sql) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<String>> getActSpecIdByActBaseId(String licId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> getLicenceDtosByLicenseeId(String licenseeId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }


    @Override
    public FeignResponseEntity<PostInsGroupDto> getPostInsGroupDto(String licId, String corrId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<PostInsGroupDto> savePostInsGroupDto(PostInsGroupDto postInsGroupDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
