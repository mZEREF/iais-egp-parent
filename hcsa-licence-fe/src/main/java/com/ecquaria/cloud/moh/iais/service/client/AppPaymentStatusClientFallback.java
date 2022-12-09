package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.grio.xml.GiroPaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.grio.xml.GiroPaymentSendGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.grio.xml.GiroPaymentXmlDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/1/14 17:07
 */
public class AppPaymentStatusClientFallback implements AppPaymentStatusClient {


    @Override
    public FeignResponseEntity<List<PaymentDto>> getPaymentDtosByReqRefNos(String sysClientId) {
        return IaisEGPHelper.getFeignResponseEntity("getPaymentDtosByReqRefNos",sysClientId);
    }

    @Override
    public FeignResponseEntity<GiroPaymentXmlDto> updateGiroDataXmlDto(GiroPaymentXmlDto giroPaymentXmlDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateGiroDataXmlDto",giroPaymentXmlDto);
    }

    @Override
    public FeignResponseEntity<List<GiroPaymentXmlDto>> getGiroPaymentDtosByStatusAndXmlType(String status, String xmlType) {
        return IaisEGPHelper.getFeignResponseEntity("getGiroPaymentDtosByStatusAndXmlType",status,xmlType);
    }

    @Override
    public FeignResponseEntity<List<GiroPaymentDto>> getGiroPaymentDtosByPmtStatusAndAppGroupNo(String pmtStatus, String appGroupNo,String sysClientId) {
        return IaisEGPHelper.getFeignResponseEntity("getGiroPaymentDtosByPmtStatusAndAppGroupNo",pmtStatus,appGroupNo,sysClientId);
    }

    @Override
    public FeignResponseEntity<List<GiroPaymentXmlDto>> updateGiroPaymentXmlDtos(List<GiroPaymentXmlDto> giroPaymentXmlDtos) {
        return IaisEGPHelper.getFeignResponseEntity("updateGiroPaymentXmlDtos",giroPaymentXmlDtos);
    }

    @Override
    public FeignResponseEntity<GiroPaymentDto> updateGiroPaymentDto(GiroPaymentDto giroPaymentDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateGiroPaymentDto",giroPaymentDto);
    }

    @Override
    public FeignResponseEntity<List<GiroPaymentDto>> updateGiroPaymentDtos(List<GiroPaymentDto> giroPaymentDtos) {
        return IaisEGPHelper.getFeignResponseEntity("updateGiroPaymentDtos",giroPaymentDtos);
    }

    @Override
    public FeignResponseEntity<List<GiroPaymentXmlDto>> getGiroPaymentDtosByStatusAndXmlType(String tag, String status, String xmlType) {
        return IaisEGPHelper.getFeignResponseEntity("getGiroPaymentDtosByStatusAndXmlType",tag,status,xmlType);
    }

    @Override
    public FeignResponseEntity<GiroPaymentXmlDto> createNewGiroPaymentXmlDto(GiroPaymentXmlDto giroPaymentXmlDto) {
        return IaisEGPHelper.getFeignResponseEntity("createNewGiroPaymentXmlDto",giroPaymentXmlDto);
    }

    @Override
    public FeignResponseEntity<GiroPaymentSendGroupDto> createGiroPaymentSendGroupDto(GiroPaymentSendGroupDto giroPaymentSendGroupDto) {
        return IaisEGPHelper.getFeignResponseEntity("createGiroPaymentSendGroupDto",giroPaymentSendGroupDto);
    }

    @Override
    public FeignResponseEntity<GiroPaymentXmlDto> updateGiroAckByGiroPaymentXmlDto(GiroPaymentXmlDto giroPaymentXmlDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateGiroAckByGiroPaymentXmlDto",giroPaymentXmlDto);
    }

    @Override
    public FeignResponseEntity<Boolean> checkOldGiroDataToNewData() {
        return IaisEGPHelper.getFeignResponseEntity("checkOldGiroDataToNewData");
    }

    @Override
    public FeignResponseEntity<List<String>> getGroupNosFromOldGiro() {
        return IaisEGPHelper.getFeignResponseEntity("getGroupNosFromOldGiro");
    }

    @Override
    public FeignResponseEntity<String> changeOldGiroDataToNewData(Map<String,List<String>> groupNoGiroAccMap) {
        return IaisEGPHelper.getFeignResponseEntity("changeOldGiroDataToNewData",groupNoGiroAccMap);
    }


}
