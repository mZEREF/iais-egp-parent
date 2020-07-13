package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/5/19 15:32
 **/
public class OnlineApptEicClientFallback implements OnlineApptEicClient {
    @Override
    public FeignResponseEntity<List<EicRequestTrackingDto>> getPendingRecords(String moduleName) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<Map<String, String>> updateStatus(List<EicRequestTrackingDto> dtoList) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<EicRequestTrackingDto> getPendingRecordByReferenceNumber(String referenceNumber) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<Map<String, String>> saveEicTrack(EicRequestTrackingDto eicRequestTrackingDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
