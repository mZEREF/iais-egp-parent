package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.QueryHelperResultDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.kafka.model.Submission;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * @author Wenkang
 * @date 2020/3/20 15:12
 */
public class EventBeMainClientCallback implements EventBeMainClient{

    @Override
    public FeignResponseEntity<QueryHelperResultDto> doQuery(String sql) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
