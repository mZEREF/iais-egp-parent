package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskScoreDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2019/12/4 15:28
 */
public class HcsaConfigMainClientFallback implements HcsaConfigMainClient{


    @Override
    public FeignResponseEntity<List<HcsaSvcDocConfigDto>> listSvcDocConfig(List<String> docId) {
        return IaisEGPHelper.getFeignResponseEntity("listSvcDocConfig",docId);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcRoutingStageDto>> getStageName(String serviceId, String stageId) {
        return IaisEGPHelper.getFeignResponseEntity("getStageName",serviceId,stageId);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> getHcsaService(List<String> serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaService",serviceId);
    }

    @Override
    public FeignResponseEntity<HcsaServiceSubTypeDto> getHcsaServiceSubTypeById(String subTypeId) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaServiceSubTypeById",subTypeId);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcRoutingStageDto>> stagelist() {
        return IaisEGPHelper.getFeignResponseEntity("stagelist");
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcStageWorkloadDto>> listHcsaSvcStageWorkloadEntity(String code) {
        return IaisEGPHelper.getFeignResponseEntity("listHcsaSvcStageWorkloadEntity",code);
    }

    @Override
    public FeignResponseEntity<Void> saveStage(Map<String , List<HcsaSvcSpecificStageWorkloadDto>> map) {

        return IaisEGPHelper.getFeignResponseEntity("saveStage",map);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcSpecificStageWorkloadDto>> calculateWorkload(String serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("calculateWorkload",serviceId);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcSpePremisesTypeDto>> applicationPremisesByIds(List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("applicationPremisesByIds",hcsaSvcSpecificStageWorkloadDtoList);
    }

    @Override
    public FeignResponseEntity<HcsaSvcKpiDto> searchKpiResult(String serviceCode, String module) {
        return IaisEGPHelper.getFeignResponseEntity("searchKpiResult",serviceCode,module);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> returnFee(List<ApplicationDto> applicationDtos) {
        return IaisEGPHelper.getFeignResponseEntity("returnFee",applicationDtos);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcRoutingStageDto>> getStageName(String serviceId, String stageId, String type) {
        return IaisEGPHelper.getFeignResponseEntity("getStageName",serviceId,stageId,type);
    }

    @Override
    public FeignResponseEntity<HcsaSvcRoutingStageDto> getHcsaSvcRoutingStageDto(HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaSvcRoutingStageDto",hcsaSvcRoutingStageDto);
    }

    @Override
    public FeignResponseEntity<HcsaRiskScoreDto> getHcsaRiskScoreDtoByHcsaRiskScoreDto(HcsaRiskScoreDto hcsaRiskScoreDto) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaRiskScoreDtoByHcsaRiskScoreDto",hcsaRiskScoreDto);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> getActiveServices() {
        return IaisEGPHelper.getFeignResponseEntity("getActiveServices");
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcKpiDto>> retrieveForDashboard() {
        return IaisEGPHelper.getFeignResponseEntity("retrieveForDashboard");
    }
}
