package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.HcsaFeeBundleItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.AuditSystemResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.AuditSystemRiskAccpetDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GobalRiskAccpetDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GolbalRiskShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaLastInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFinanceMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskGlobalDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskGolbalExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskInspectionComplianceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskInspectionMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLeadershipMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLegislativeMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLicenceTenureDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskScoreDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.InspectionShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.LicenceTenShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskFinancialShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLeaderShipShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLegislativeShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaPrimiseWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCategoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcCateWrkgrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpeRoutingSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author Wenkang
 * @date 2019/12/4 15:28
 */
public class HcsaConfigFeClientFallback implements HcsaConfigFeClient{

    @Override
    public FeignResponseEntity<List<HcsaSvcRoutingStageDto>> getStageName(String serviceId, String stageId) {
        return IaisEGPHelper.getFeignResponseEntity("getStageName",serviceId,stageId);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcRoutingStageDto>> getStageName(String serviceId, String stageId, String type,Integer isPreIns) {
        return IaisEGPHelper.getFeignResponseEntity("getStageName",serviceId,stageId,type,isPreIns);
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
    public FeignResponseEntity<Integer> getManHour(ApptAppInfoShowDto apptAppInfoShowDto) {
        return IaisEGPHelper.getFeignResponseEntity("getManHour",apptAppInfoShowDto);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcStageWorkloadDto>> listHcsaSvcStageWorkloadEntity(String code) {
        return IaisEGPHelper.getFeignResponseEntity("listHcsaSvcStageWorkloadEntity",code);
    }

    @Override
    public FeignResponseEntity<Void> saveStage(List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("saveStage",hcsaSvcSpecificStageWorkloadDtoList);
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
    public FeignResponseEntity<List<RiskResultDto>> getRiskResult(List<RiskAcceptiionDto> riskAcceptiionDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("getRiskResult",riskAcceptiionDtoList);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> getActiveServices() {
        return IaisEGPHelper.getFeignResponseEntity("getActiveServices");
    }

    @Override
    public FeignResponseEntity<List<String>> listServiceP1Name() {
        return IaisEGPHelper.getFeignResponseEntity("listServiceP1Name");
    }

    @Override
    public FeignResponseEntity<RiskFinancialShowDto> getRiskFinShow(List<HcsaServiceDto> svcList) {
        return IaisEGPHelper.getFeignResponseEntity("getRiskFinShow",svcList);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskFinanceMatrixDto>> getFinianceRiskBySvcCode(String serviceCode) {
        return IaisEGPHelper.getFeignResponseEntity("getFinianceRiskBySvcCode",serviceCode);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcStageWorkingGroupDto>> getSvcWorkGroup(HcsaSvcStageWorkingGroupDto dto) {
        return IaisEGPHelper.getFeignResponseEntity("getSvcWorkGroup",dto);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskFinanceMatrixDto>> saveFinRiskMatrix(List<HcsaRiskFinanceMatrixDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("saveFinRiskMatrix",finDtoList);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskFinanceMatrixDto>> updateFinRiskMatrix(List<HcsaRiskFinanceMatrixDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("updateFinRiskMatrix",finDtoList);
    }

    @Override
    public FeignResponseEntity<RiskLeaderShipShowDto> getRiskLeaderShipShow(List<HcsaServiceDto> svcList) {
        return IaisEGPHelper.getFeignResponseEntity("getRiskLeaderShipShow",svcList);
    }

    @Override
    public FeignResponseEntity<List<String>> getHcsaServiceNameByType(String type) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaServiceNameByType",type);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskLeadershipMatrixDto>> getLeadershipRiskBySvcCode(String serviceCode) {
        return IaisEGPHelper.getFeignResponseEntity("getLeadershipRiskBySvcCode",serviceCode);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskLeadershipMatrixDto>> saveLeadershipRiskMatrix(List<HcsaRiskLeadershipMatrixDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("saveLeadershipRiskMatrix",finDtoList);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskLeadershipMatrixDto>> updateLeadershipRiskMatrix(List<HcsaRiskLeadershipMatrixDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("updateLeadershipRiskMatrix",finDtoList);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskLegislativeMatrixDto>> getLegislativeRiskBySvcCode(String serviceCode) {
        return IaisEGPHelper.getFeignResponseEntity("getLegislativeRiskBySvcCode",serviceCode);
    }

    @Override
    public FeignResponseEntity<RiskLegislativeShowDto> getLegislativeShow(List<HcsaServiceDto> svcList) {
        return IaisEGPHelper.getFeignResponseEntity("getLegislativeShow",svcList);
    }

    @Override
    public FeignResponseEntity<SearchResult<HcsaServicePrefInspPeriodQueryDto>> getHcsaServicePrefInspPeriodList(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaServicePrefInspPeriodList",searchParam);
    }

    @Override
    public FeignResponseEntity<HcsaServicePrefInspPeriodDto> savePrefInspPeriod(HcsaServicePrefInspPeriodDto period) {
        return IaisEGPHelper.getFeignResponseEntity("savePrefInspPeriod",period);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskLegislativeMatrixDto>> saveLegislativeRiskMatrix(List<HcsaRiskLegislativeMatrixDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("saveLegislativeRiskMatrix",finDtoList);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskLegislativeMatrixDto>> updateLegislativeRiskMatrix(List<HcsaRiskLegislativeMatrixDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("updateLegislativeRiskMatrix",finDtoList);
    }

    @Override
    public FeignResponseEntity<HcsaRiskWeightageShowDto> getWeightageShow(List<HcsaServiceDto> svcList) {
        return IaisEGPHelper.getFeignResponseEntity("getWeightageShow",svcList);
    }

    @Override
    public FeignResponseEntity<FeeDto> renewFee(List<LicenceFeeDto> dtos) {
        return IaisEGPHelper.getFeignResponseEntity("renewFee",dtos);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskWeightageDto>> getWeightageRiskBySvcCode(String serviceCode) {
        return IaisEGPHelper.getFeignResponseEntity("getWeightageRiskBySvcCode",serviceCode);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskWeightageDto>> saveWeightageMatrixList(List<HcsaRiskWeightageDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("saveWeightageMatrixList",finDtoList);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskWeightageDto>> updateWeightageMatrixList(List<HcsaRiskWeightageDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("updateWeightageMatrixList",finDtoList);
    }

    @Override
    public FeignResponseEntity<GolbalRiskShowDto> getgolbalshow(List<HcsaServiceDto> svcList) {
        return IaisEGPHelper.getFeignResponseEntity("getgolbalshow",svcList);
    }

    @Override
    public FeignResponseEntity<HcsaRiskGlobalDto> getRiskGolbalRiskMatraixBySvcCode(String serviceCode) {
        return IaisEGPHelper.getFeignResponseEntity("getRiskGolbalRiskMatraixBySvcCode",serviceCode);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskGolbalExtDto>> getRiskGolbalextDtoById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getRiskGolbalextDtoById",id);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskGlobalDto>> saveGoalbalMatrixList(List<HcsaRiskGlobalDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("saveGoalbalMatrixList",finDtoList);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskGlobalDto>> udpateGoalbalMatrixList(List<HcsaRiskGlobalDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("udpateGoalbalMatrixList",finDtoList);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskGolbalExtDto>> saveGoalbalExtMatrixList(List<HcsaRiskGolbalExtDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("saveGoalbalExtMatrixList",finDtoList);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskGolbalExtDto>> updateGoalbalExtMatrixList(List<HcsaRiskGolbalExtDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("updateGoalbalExtMatrixList",finDtoList);
    }

    @Override
    public FeignResponseEntity<HcsaRiskGlobalDto> saveGoalbalMatrix(HcsaRiskGlobalDto finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("saveGoalbalMatrix",finDtoList);
    }

    @Override
    public FeignResponseEntity<HcsaRiskGlobalDto> udpateGoalbalMatrix(HcsaRiskGlobalDto finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("udpateGoalbalMatrix",finDtoList);
    }

    @Override
    public FeignResponseEntity<InspectionShowDto> getInspectionshow(List<HcsaServiceDto> svcList) {
        return IaisEGPHelper.getFeignResponseEntity("getInspectionshow",svcList);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskInspectionMatrixDto>> saveInspectionMatrix(List<HcsaRiskInspectionMatrixDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("saveInspectionMatrix",finDtoList);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskInspectionMatrixDto>> udpateInspectionMatrix(List<HcsaRiskInspectionMatrixDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("udpateInspectionMatrix",finDtoList);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskInspectionMatrixDto>> getInspectionBySvcCode(String svcCode) {
        return IaisEGPHelper.getFeignResponseEntity("getInspectionBySvcCode",svcCode);
    }

    @Override
    public FeignResponseEntity<LicenceTenShowDto> getLicenceTenureShow(List<HcsaServiceDto> svcList) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenceTenureShow",svcList);
    }
    @Override
    public FeignResponseEntity<List<HcsaSvcSubtypeOrSubsumedDto>> listSubCorrelation(String serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("listSubCorrelation",serviceId);
    }

    @Override
    public FeignResponseEntity<List<String>> getModuleName(String serviceCode) {
        return IaisEGPHelper.getFeignResponseEntity("getModuleName",serviceCode);
    }

    @Override
    public FeignResponseEntity<HcsaSvcKpiDto> searchResult(String serviceCode, String module) {
        return IaisEGPHelper.getFeignResponseEntity("searchResult",serviceCode,module);
    }

    @Override
    public FeignResponseEntity<HcsaSvcStageWorkingGroupDto> getHcsaSvcStageWorkingGroupDto(HcsaSvcStageWorkingGroupDto dto) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaSvcStageWorkingGroupDto",dto);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskLicenceTenureDto>> savehcsaRiskLicenceTenure(List<HcsaRiskLicenceTenureDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("savehcsaRiskLicenceTenure",finDtoList);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskLicenceTenureDto>> updatehcsaRiskLicenceTenure(List<HcsaRiskLicenceTenureDto> finDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("updatehcsaRiskLicenceTenure",finDtoList);
    }

    @Override
    public FeignResponseEntity<List<HcsaRiskLicenceTenureDto>> getgetLictenureByCode(String serviceCode) {
        return IaisEGPHelper.getFeignResponseEntity("getgetLictenureByCode",serviceCode);
    }

    @Override
    public FeignResponseEntity saveKpiAndReminder(HcsaSvcKpiDto hcsaSvcKpiDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveKpiAndReminder",hcsaSvcKpiDto);
    }
    @Override
    public   FeignResponseEntity <List<HcsaSvcRoutingStageDto>> getAllHcsaSvcRoutingStage(){
        return IaisEGPHelper.getFeignResponseEntity("getAllHcsaSvcRoutingStage");
    }

    @Override
    public FeignResponseEntity<HcsaSvcRoutingStageDto> getHcsaSvcRoutingStageById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaSvcRoutingStageById",id);
    }

    @Override
    public FeignResponseEntity<HcsaLastInspectionDto> getLastAndSecRiskScore(HcsaLastInspectionDto inspDto) {
        return IaisEGPHelper.getFeignResponseEntity("getLastAndSecRiskScore",inspDto);
    }

    @Override
    public FeignResponseEntity<String> getServiceCodeByName(String svcName) {
        return IaisEGPHelper.getFeignResponseEntity("getServiceCodeByName",svcName);
    }

    @Override
    public FeignResponseEntity<SearchResult<HcsaSvcQueryDto>> searchSvcNamesParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchSvcNamesParam",searchParam);
    }

    @Override
    public FeignResponseEntity<AppointmentDto> getApptStartEndDateByService(AppointmentDto appointmentDto) {
        return IaisEGPHelper.getFeignResponseEntity("getApptStartEndDateByService",appointmentDto);
    }


    @Override
    public FeignResponseEntity<List<AuditSystemResultDto>> getAuditSystemRiskResult(List<AuditSystemRiskAccpetDto> acceptDtoList) {
        return IaisEGPHelper.getFeignResponseEntity("getAuditSystemRiskResult",acceptDtoList);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> allHcsaService() {
        return IaisEGPHelper.getFeignResponseEntity("allHcsaService");
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> getHcsaServiceDtoByCode(List<String> code) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaServiceDtoByCode",code);
    }

    @Override
    public FeignResponseEntity<Set<String>> getAppGrpPremisesTypeBySvcId(List<String> serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppGrpPremisesTypeBySvcId",serviceId);
    }

    @Override
    public FeignResponseEntity<HcsaServiceConfigDto> saveHcsaServiceConfig(HcsaServiceConfigDto hcsaServiceConfigDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveHcsaServiceConfig",hcsaServiceConfigDto);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcPersonnelDto>> getSvcPersonnelByServiceId(String serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("getSvcPersonnelByServiceId",serviceId);
    }

    @Override
    public FeignResponseEntity<Map<String,Boolean>> isExistHcsaService(HcsaServiceDto hcsaServiceDto) {
        return IaisEGPHelper.getFeignResponseEntity("isExistHcsaService",hcsaServiceDto);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcStageWorkloadDto>> getHcsaSvcSpeRoutingSchemeByServiceId(String serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaSvcSpeRoutingSchemeByServiceId",serviceId);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcStageWorkingGroupDto>> getHcsaStageWorkingGroup(String serivceId) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaStageWorkingGroup",serivceId);
    }

    @Override
    public FeignResponseEntity<HcsaServiceDto> getServiceDtoByName(String svcName) {
        return IaisEGPHelper.getFeignResponseEntity("getServiceDtoByName",svcName);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcStageWorkingGroupDto>> getHcsaSvcWorkingGroupByStages(List<String> stageIds) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaSvcWorkingGroupByStages",stageIds);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceStepSchemeDto>> getHcsaServiceStepSchemeDtoByServiceId(String serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaServiceStepSchemeDtoByServiceId",serviceId);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceCategoryDto>> getHcsaServiceCategorys() {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaServiceCategorys");
    }

    @Override
    public FeignResponseEntity<Boolean> serviceIdIsUsed(String serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("serviceIdIsUsed",serviceId);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcSpeRoutingSchemeDto>> getHcsaSvcSpeRoutingSchemeDtoByServiceId(String serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaSvcSpeRoutingSchemeDtoByServiceId",serviceId);

    }

    @Override
    public FeignResponseEntity updateService(String serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("updateService",serviceId);

    }

    @Override
    public FeignResponseEntity<List<HcsaSvcDocConfigDto>> getHcsaSvcDocConfigDto(String serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaSvcDocConfigDto",serviceId);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> getServiceVersions(String serviceCode) {
        return IaisEGPHelper.getFeignResponseEntity("getServiceVersions",serviceCode);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceStepSchemeDto>> getServiceStepsByServiceIds(List<String> serviceIds) {
        return IaisEGPHelper.getFeignResponseEntity("getServiceStepsByServiceIds",serviceIds);
    }

    @Override
    public FeignResponseEntity<HcsaSvcDocConfigDto> getHcsaSvcDocConfigDtoById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaSvcDocConfigDtoById",id);
    }

    @Override
    public FeignResponseEntity<List<AppSvcChckListDto>> getAppSvcChckListDto(List<AppSvcChckListDto> appSvcChckListDto) {
        return IaisEGPHelper.getFeignResponseEntity("getAppSvcChckListDto",appSvcChckListDto);
    }

    @Override
    public FeignResponseEntity<HcsaRiskInspectionComplianceDto> getHcsaRiskInspectionComplianceDto(HcsaRiskInspectionComplianceDto hcsaRiskInspectionComplianceDto) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaRiskInspectionComplianceDto",hcsaRiskInspectionComplianceDto);
    }

    @Override
    public FeignResponseEntity<GobalRiskAccpetDto> getGobalRiskAccpetDtoByGobalRiskAccpetDto(GobalRiskAccpetDto gobalRiskAccpetDto) {
        return IaisEGPHelper.getFeignResponseEntity("getGobalRiskAccpetDtoByGobalRiskAccpetDto",gobalRiskAccpetDto);
    }

    @Override
    public FeignResponseEntity<HcsaRiskScoreDto> getHcsaRiskScoreDtoByHcsaRiskScoreDto(HcsaRiskScoreDto hcsaRiskScoreDto) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaRiskScoreDtoByHcsaRiskScoreDto",hcsaRiskScoreDto);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> returnFee(List<ApplicationDto> applicationDtos) {
        return IaisEGPHelper.getFeignResponseEntity("returnFee",applicationDtos);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcDocConfigDto>> getHcsaSvcDocConfig(String docMapJson) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaSvcDocConfig",docMapJson);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> baseHcsaService() {
        return IaisEGPHelper.getFeignResponseEntity("baseHcsaService");
    }

    @Override
    public FeignResponseEntity<List<HcsaPrimiseWorkloadDto>> getHcsaPremisesWorkload(String type) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaPremisesWorkload",type);
    }

    @Override
    public FeignResponseEntity<Void> savePremiseWorkload(List<HcsaPrimiseWorkloadDto> hcsaPrimiseWorkloadDtos) {
        return IaisEGPHelper.getFeignResponseEntity("savePremiseWorkload",hcsaPrimiseWorkloadDtos);
    }

    @Override
    public FeignResponseEntity<List<ChecklistConfigDto>> getChecklistConfigByIds(List<String> ids) {
        return IaisEGPHelper.getFeignResponseEntity("getChecklistConfigByIds",ids);
    }

    @Override
    public FeignResponseEntity<ChecklistConfigDto> getChecklistConfigById(String configId) {
        return IaisEGPHelper.getFeignResponseEntity("getChecklistConfigById",configId);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcSpecifiedCorrelationDto>> getHcsaSvcSpecifiedCorrelationDto(String speServiceId) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaSvcSpecifiedCorrelationDto",speServiceId);
    }

    @Override
    public FeignResponseEntity<List<ApplicationDto>> needToSendTask(List<ApplicationDto> applicationDtos) {
        return IaisEGPHelper.getFeignResponseEntity("needToSendTask",applicationDtos);
    }

    @Override
    public FeignResponseEntity<List<String>> getWorkGroupIdsByStageId(String stageId) {
        return IaisEGPHelper.getFeignResponseEntity("getWorkGroupIdsByStageId",stageId);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcRoutingStageDto>> getHcsaSvcRoutingStageDtoByServiceAndType(String service, String type) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaSvcRoutingStageDtoByServiceAndType",service,type);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcCateWrkgrpCorrelationDto>> getHcsaSvcCateWrkgrpCorrelationDtoBySvcCateId(String svcCateId) {
        return IaisEGPHelper.getFeignResponseEntity("getHcsaSvcCateWrkgrpCorrelationDtoBySvcCateId",svcCateId);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> getNeedInActiveServices(String status) {
        return IaisEGPHelper.getFeignResponseEntity("getNeedInActiveServices",status);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> getNeedActiveServices(String status) {
        return IaisEGPHelper.getFeignResponseEntity("getNeedActiveServices",status);
    }

    @Override
    public FeignResponseEntity<List<HcsaServiceDto>> saveServiceList(List<HcsaServiceDto> hcsaServiceDtos) {
        return IaisEGPHelper.getFeignResponseEntity("saveServiceList",hcsaServiceDtos);
    }

    @Override
    public FeignResponseEntity<List<HcsaSvcDocConfigDto>> getPrimaryDocConfigList(String docId) {
        return IaisEGPHelper.getFeignResponseEntity("getPrimaryDocConfigList",docId);
    }

    @Override
    public FeignResponseEntity<HcsaServiceDto> getServiceDtoById(String serviceId) {
        return IaisEGPHelper.getFeignResponseEntity("getServiceDtoById",serviceId);
    }

    @Override
    public FeignResponseEntity<List<HcsaFeeBundleItemDto>> getActiveBundleDtoList() {
        return IaisEGPHelper.getFeignResponseEntity("getActiveBundleDtoList");
    }
}
