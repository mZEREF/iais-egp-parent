package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.system.EventCallbackTrackDto;
import com.ecquaria.cloud.submission.client.model.GetSubmissionStatusResp;
import com.ecquaria.cloud.submission.client.model.SubmitReq;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.http.HttpHeaders;

/**
 * EventBusClientClientFallBack
 *
 * @author Jinhua
 * @date 2020/9/23 14:01
 */
public class EventBusClientClientFallBack implements EventBusClient{
    @Override
    public FeignResponseEntity<Void> trackCompersation() {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<SubmitReq>> getRequestToRecover() {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<EventCallbackTrackDto> getCallbackTracking(String submissionId, String operation) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<Void> updateCallbackTracking(EventCallbackTrackDto dto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<GetSubmissionStatusResp> getSubmissionStatus(String submissionId, String operation) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
