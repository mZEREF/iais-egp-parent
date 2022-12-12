package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * RequestForInformationClientFallback
 *
 * @author junyu
 * @date 2019/12/18
 */
@Component
public class ResponseForInformationClientFallback implements ResponseForInformationClient {



    @Override
    public FeignResponseEntity<List<LicPremisesReqForInfoDto>> searchLicPreRfiBylicenseeId(String licenseeId){
        return IaisEGPHelper.getFeignResponseEntity("searchLicPreRfiBylicenseeId",licenseeId);
    }

    @Override
    public FeignResponseEntity<LicPremisesReqForInfoDto> getLicPreReqForInfo(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getLicPreReqForInfo",id);
    }

    @Override
    public FeignResponseEntity<LicPremisesReqForInfoDto> acceptLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        return IaisEGPHelper.getFeignResponseEntity("acceptLicPremisesReqForInfo",licPremisesReqForInfoDto);
    }



    @Override
    public FeignResponseEntity<LicPremisesReqForInfoDto> createLicPremisesReqForInfoFe(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        return IaisEGPHelper.getFeignResponseEntity("createLicPremisesReqForInfoFe",licPremisesReqForInfoDto);
    }

}
