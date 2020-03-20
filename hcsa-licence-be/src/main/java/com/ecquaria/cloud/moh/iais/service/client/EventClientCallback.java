package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.kafka.model.Submission;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Wenkang
 * @date 2020/3/20 15:12
 */
public class EventClientCallback {
    public FeignResponseEntity<List<Submission>> getSubmission(String submissionId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
