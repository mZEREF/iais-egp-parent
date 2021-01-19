package com.ecquaria.cloud.moh.iais.service.client;

/*
 *author: guyin
 *date time:2/6/2019 10:09 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListWebDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

public class DistributionListClientFallback implements DistributionListClient{

    @Override
    public FeignResponseEntity<SearchResult<DistributionListDto>> getDistributionList(SearchParam searchParam) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<DistributionListWebDto>> getDistributionListNoParam(String mode) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }


    @Override
    public FeignResponseEntity<DistributionListWebDto> saveDistributionList(DistributionListWebDto distributionListDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<Void> deleteDistributionList(List<String> list) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<DistributionListWebDto> getDistributionListById(String id) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }


}
