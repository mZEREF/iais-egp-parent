package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.GiroPaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.GiroPaymentSendGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.GiroPaymentXmlDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * @author weilu
 * @date 2020/1/14 17:07
 */
public class AppPaymentStatusClientFallback implements AppPaymentStatusClient {


    @Override
    public FeignResponseEntity<List<PaymentDto>> getPaymentDtosByReqRefNos(String sysClientId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<GiroPaymentXmlDto> updateGiroPaymentXmlDto(GiroPaymentXmlDto giroPaymentXmlDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<GiroPaymentXmlDto>> getGiroPaymentDtosByStatusAndXmlType(String status, String xmlType) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<GiroPaymentDto>> getGiroPaymentDtosByPmtStatusAndAppGroupNo(String pmtStatus, String appGroupNo,String sysClientId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<GiroPaymentXmlDto>> updateGiroPaymentXmlDtos(List<GiroPaymentXmlDto> giroPaymentXmlDtos) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<GiroPaymentDto> updateGiroPaymentDto(GiroPaymentDto giroPaymentDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<GiroPaymentDto>> updateGiroPaymentDtos(List<GiroPaymentDto> giroPaymentDtos) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<GiroPaymentXmlDto>> getGiroPaymentDtosByStatusAndXmlType(String tag, String status, String xmlType) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<GiroPaymentXmlDto> createNewGiroPaymentXmlDto(GiroPaymentXmlDto giroPaymentXmlDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<GiroPaymentSendGroupDto> createGiroPaymentSendGroupDto(GiroPaymentSendGroupDto giroPaymentSendGroupDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<GiroPaymentXmlDto> updateGiroAckByGiroPaymentXmlDto(GiroPaymentXmlDto giroPaymentXmlDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }


}
