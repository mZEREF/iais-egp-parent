package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/2/7 13:25
 */
public class CessationClientFallback implements CessationClient {

    @Override
    public FeignResponseEntity<List<String>> saveCessation(List<AppCessMiscDto> appCessMiscDtos) {
        return IaisEGPHelper.getFeignResponseEntity("saveCessation",appCessMiscDtos);
    }


    @Override
    public FeignResponseEntity<List<String>> saveWithdrawn(List<WithdrawnDto> withdrawnDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveWithdrawn",withdrawnDto);
    }


    @Override
    public FeignResponseEntity<List<AppCessLicDto>> getCessationByLicIds(List<String> licIds) {
        return IaisEGPHelper.getFeignResponseEntity("getCessationByLicIds",licIds);
    }


    @Override
    public FeignResponseEntity<Boolean> isCeased(String licId) {
        return IaisEGPHelper.getFeignResponseEntity("isCeased",licId);
    }

    @Override
    public FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesDtoByAppId(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppGrpPremisesDtoByAppId",appId);
    }

    @Override
    public FeignResponseEntity<Map<String, Boolean>> listCanCeased(List<String> licIds) {
        return IaisEGPHelper.getFeignResponseEntity("listCanCeased",licIds);
    }

    @Override
    public FeignResponseEntity<List<AppPremiseMiscDto>> getAppPremiseMiscDtoListByAppId(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremiseMiscDtoListByAppId",appId);
    }

    @Override
    public FeignResponseEntity<AppCessMiscDto> getAppMiscDtoByAppId(String appId) {
        return null;
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> getAppByBaseAppNo(String appNo) {
        return null;
    }

    @Override
    public FeignResponseEntity<AppPremiseMiscDto> getAppPremiseMiscDtoListByCon(String corrId, String type) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremiseMiscDtoListByCon",corrId,type);
    }


}
