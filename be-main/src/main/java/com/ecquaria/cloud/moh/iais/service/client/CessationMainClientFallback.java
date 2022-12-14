package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/2/7 13:25
 */
public class CessationMainClientFallback implements CessationMainClient {
    @Override
    public FeignResponseEntity<AppInsRepDto> getAppCessationDto(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity("getAppCessationDto",appNo);
    }

    @Override
    public FeignResponseEntity<List<String>> saveCessation(List<AppCessMiscDto> appCessMiscDtos) {
        return IaisEGPHelper.getFeignResponseEntity("saveCessation",appCessMiscDtos);
    }

    @Override
    public FeignResponseEntity<String> updateCessation(AppCessMiscDto appCessMiscDto) {
        return null;
    }


    @Override
    public FeignResponseEntity<AppCessMiscDto> getCessationByLicId(String type, String status, String licId) {
        return IaisEGPHelper.getFeignResponseEntity("getCessationByLicId",type,status,licId);
    }

    @Override
    public FeignResponseEntity<List<String>> getlicIdToCessation(List<String> licIds) {
        return IaisEGPHelper.getFeignResponseEntity("getlicIdToCessation",licIds);
    }

    @Override
    public FeignResponseEntity<List<String>> listHciNames() {
        return IaisEGPHelper.getFeignResponseEntity("listHciNames");
    }

    @Override
    public FeignResponseEntity<Boolean> isCeased(String licId) {
        return IaisEGPHelper.getFeignResponseEntity("isCeased",licId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationGroupDto>> listAppGrpForCess() {
        return IaisEGPHelper.getFeignResponseEntity("listAppGrpForCess");
    }

    @Override
    public FeignResponseEntity<List<AppCessMiscDto>> getAppCessMiscDtosByCorrIds(List<String> corrIds) {
        return IaisEGPHelper.getFeignResponseEntity("getAppCessMiscDtosByCorrIds",corrIds);
    }

    @Override
    public FeignResponseEntity<AppPremiseMiscDto> getAppPremiseMiscDtoByAppId(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremiseMiscDtoByAppId",appId);
    }

    @Override
    public FeignResponseEntity<List<AppPremiseMiscDto>> getAppPremiseMiscDtoListByAppId(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremiseMiscDtoListByAppId",appId);
    }

    @Override
    public FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesDtoByAppId(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppGrpPremisesDtoByAppId",appId);
    }

    @Override
    public FeignResponseEntity<Map<String,Boolean>> listCanCeased(List<String> licIds) {
        return IaisEGPHelper.getFeignResponseEntity("listCanCeased",licIds);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> prepareCessation() {
        return IaisEGPHelper.getFeignResponseEntity("prepareCessation");
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> getAppsByLicId(String licId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppsByLicId",licId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> getAppByBaseAppNo(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity("getAppByBaseAppNo",appNo);
    }


}
