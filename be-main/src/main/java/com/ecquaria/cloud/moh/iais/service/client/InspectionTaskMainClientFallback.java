package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppStageSlaTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppPremInsDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppInGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashAssignMeAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashAssignMeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashComPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashWorkTeamAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashWorkTeamQueryDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;

/**
 * @author Shicheng
 * @date 2019/11/26 10:34
 **/
public class InspectionTaskMainClientFallback implements InspectionTaskMainClient {
    @Override
    public FeignResponseEntity<ApplicationDto> getApplicationDtoByAppNo(String applicationNo){
        return IaisEGPHelper.getFeignResponseEntity("getApplicationDtoByAppNo",applicationNo);
    }

    @Override
    public FeignResponseEntity<ApplicationGroupDto> getApplicationGroupDtoByAppGroId(String appGroupId){
        return IaisEGPHelper.getFeignResponseEntity("getApplicationGroupDtoByAppGroId",appGroupId);
    }

    @Override
    public FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesDtoByAppGroId(String applicationId){
        return IaisEGPHelper.getFeignResponseEntity("getAppGrpPremisesDtoByAppGroId",applicationId);
    }

    @Override
    public FeignResponseEntity<SearchResult<InspectionCommonPoolQueryDto>> searchInspectionPool(SearchParam searchParam){
        return IaisEGPHelper.getFeignResponseEntity("searchInspectionPool",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<InspectionSubPoolQueryDto>> searchInspectionSupPool(SearchParam searchParam){
        return IaisEGPHelper.getFeignResponseEntity("searchInspectionSupPool",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<InspectionAppGroupQueryDto>> searchInspectionBeAppGroup(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchInspectionBeAppGroup",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<InspectionAppInGroupQueryDto>> searchInspectionBeAppGroupAjax(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchInspectionBeAppGroupAjax",searchParam);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> getHistoryForKpi(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto) {
        return IaisEGPHelper.getFeignResponseEntity("getHistoryForKpi",appPremisesRoutingHistoryDto);
    }

    @Override
    public FeignResponseEntity<AppPremInsDraftDto> getAppPremInsDraftDtoByAppPreCorrId(String appPreCorrId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremInsDraftDtoByAppPreCorrId",appPreCorrId);
    }

    @Override
    public FeignResponseEntity<ApplicationDto> getApplicationByCorreId(String appCorreId) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationByCorreId",appCorreId);
    }

    @Override
    public FeignResponseEntity<AppStageSlaTrackingDto> getSlaTrackByAppNoStageId(String appNo, String stageId) {
        return IaisEGPHelper.getFeignResponseEntity("getSlaTrackByAppNoStageId",appNo);
    }

    @Override
    public FeignResponseEntity<AppPremisesRecommendationDto> getAppPremRecordByIdAndType(String appPremId, String recomType) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremRecordByIdAndType",appPremId);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashComPoolQueryDto>> searchDashComPoolResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashComPoolResult",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashComPoolAjaxQueryDto>> searchDashComPoolDropResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashComPoolDropResult",searchParam);
    }

    @Override
    public FeignResponseEntity<List<AppIntranetDocDto>> getAppIntranetDocListByPremIdAndStatus(String premId, String status) {
        return IaisEGPHelper.getFeignResponseEntity("getAppIntranetDocListByPremIdAndStatus",premId);
    }

    @Override
    public FeignResponseEntity<List<AppPremiseMiscDto>> getAppPremiseMiscDtoListByAppId(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremiseMiscDtoListByAppId",appId);
    }

    @Override
    public FeignResponseEntity<AppSvcPrincipalOfficersDto> getApplicationCgoByAppId(String applicationId, String psnType) {
        return IaisEGPHelper.getFeignResponseEntity("getApplicationCgoByAppId",applicationId);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesSpecialDocDto>> getAppPremisesSpecialDocByPremId(String premId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesSpecialDocByPremId",premId);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesCorrelationDto>> getAppPremisesCorrelationsByAppId(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesCorrelationsByAppId",appId);
    }

    @Override
    public FeignResponseEntity<AppInsRepDto> appGrpPremises(String appPremcorrId) {
        return IaisEGPHelper.getFeignResponseEntity("appGrpPremises",appPremcorrId);
    }

    @Override
    public FeignResponseEntity<AppPremisesRecommendationDto> saveAppRecom(AppPremisesRecommendationDto appPremisesRecommendationDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveAppRecom",appPremisesRecommendationDto);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashAssignMeQueryDto>> searchDashAssignMeResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashAssignMeResult",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashAssignMeAjaxQueryDto>> searchDashAssignMeAjaxResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashAssignMeAjaxResult",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashWorkTeamQueryDto>> searchDashWorkTeamResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashWorkTeamResult",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DashWorkTeamAjaxQueryDto>> searchDashWorkTeamDropResult(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDashWorkTeamDropResult",searchParam);
    }

    @Override
    public FeignResponseEntity<Map<String, AppGrpPremisesDto>> getGroupAppsByNos(List<String> appGropIds) {
        return IaisEGPHelper.getFeignResponseEntity("getGroupAppsByNos",appGropIds);
    }

    @Override
    public FeignResponseEntity<List<AppStageSlaTrackingDto>> getSlaTrackByAppNoStageIds(Map<String, String> params) {
        return IaisEGPHelper.getFeignResponseEntity("getSlaTrackByAppNoStageIds",params);
    }
}
