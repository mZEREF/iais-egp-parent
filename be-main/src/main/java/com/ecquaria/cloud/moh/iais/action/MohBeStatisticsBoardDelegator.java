package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.BeDashboardConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashAllActionAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashAllGrpAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashStageCircleKpiDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.InspectionMainService;
import com.ecquaria.cloud.moh.iais.service.MohHcsaBeDashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @author Shicheng
 * @date 2021/6/28 14:19
 **/
@Delegator(value = "mohBeStatisticsBoardDelegator")
@Slf4j
public class MohBeStatisticsBoardDelegator {

    @Autowired
    private MohHcsaBeDashboardService mohHcsaBeDashboardService;

    @Autowired
    private InspectionMainService inspectionService;

    /**
     * StartStep: beStatisticsBoardStart
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardStart(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardStart start ...."));
        ParamUtil.setSessionAttr(bpc.request, "dashActionValue", null);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", null);
        String backFlag = ParamUtil.getRequestString(bpc.request, "dashProcessBack");
        if(!AppConsts.YES.equals(backFlag)) {
            ParamUtil.setSessionAttr(bpc.request, "dashSwitchActionValue", null);
            ParamUtil.setSessionAttr(bpc.request, "appTypeOption", null);
            ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
            ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", null);
            ParamUtil.setSessionAttr(bpc.request, "dashSearchResult", null);
            ParamUtil.setSessionAttr(bpc.request, "dashWorkGroupIds", null);
            ParamUtil.setSessionAttr(bpc.request, "groupRoleFieldDto", null);
            ParamUtil.setSessionAttr(bpc.request, "beDashRoleIds", null);
            ParamUtil.setSessionAttr(bpc.request, "dashRoleCheckDto", null);
            ParamUtil.setSessionAttr(bpc.request, "dashAppStatus", null);
            ParamUtil.setSessionAttr(bpc.request, "application_status", null);
            ParamUtil.setSessionAttr(bpc.request, "dashFilterAppNo", null);
        } else {
            ParamUtil.setRequestAttr(bpc.request, "dashProcessBackFlag", backFlag);
        }
        ParamUtil.setSessionAttr(bpc.request, "inspecTaskCreAndAssDto", null);
        ParamUtil.setSessionAttr(bpc.request, "dashAsoCircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashPsoCircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashPreInspCircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashInspCircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashPostInspCircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashAo1CircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashAo2CircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashAo3CircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashOverAllCircleKpi", null);
        ParamUtil.setSessionAttr(bpc.request, "dashSvcCheckList", null);
        ParamUtil.setSessionAttr(bpc.request, "dashAppTypeCheckList", null);
    }

    /**
     * StartStep: beStatisticsBoardInit
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardInit(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardInit start ...."));
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INTRANET_DASHBOARD, AuditTrailConsts.FUNCTION_INTRANET_STATISTICS_BOARD);
    }

    /**
     * StartStep: beStatisticsBoardPre
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardPre(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardPre start ...."));
        String switchAction = ParamUtil.getRequestString(bpc.request, "switchAction");
        if(StringUtil.isEmpty(switchAction)) {
            switchAction = BeDashboardConstant.SWITCH_ACTION_SYSTEM_ALL;
        }
        String applicationNo = ParamUtil.getRequestString(bpc.request, "applicationNo");
        String[] services = ParamUtil.getStrings(bpc.request,"svcLic");
        String[] appTypes = ParamUtil.getStrings(bpc.request,"appType");
        //search system dashboard stage show
        SearchParam searchParam = new SearchParam(DashAllActionAppQueryDto.class.getName());
        searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
        //set filter
        searchParam = mohHcsaBeDashboardService.setStatisticsDashFilter(searchParam, services, appTypes, applicationNo);
        QueryHelp.setMainSql("intraDashboardQuery", "dashSystemOverAll", searchParam);
        SearchResult<DashAllActionAppQueryDto> searchResult = mohHcsaBeDashboardService.getDashAllActionResult(searchParam);
        //get Dashboard Circle Kpi Show Dto
        List<DashStageCircleKpiDto> dashStageCircleKpiDtos = mohHcsaBeDashboardService.getDashStageCircleKpiShow(searchResult);
        //set Dashboard Circle Kpi Show Session
        setDashCircleKpiShowSession(bpc.request, dashStageCircleKpiDtos);
        //get appType option
        List<SelectOption> appTypeOption = inspectionService.getAppTypeOption();
        //get service option
        List<SelectOption> serviceOption = mohHcsaBeDashboardService.getHashServiceOption();
        //set session
        setSessionBySvcAppTypeFilter(bpc.request, services, appTypes, applicationNo);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", (Serializable) appTypeOption);
        ParamUtil.setSessionAttr(bpc.request, "dashServiceOption", (Serializable) serviceOption);
        ParamUtil.setSessionAttr(bpc.request, "dashSwitchActionValue", switchAction);
        ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", null);
        ParamUtil.setSessionAttr(bpc.request, "dashSearchResult", null);
    }

    private void setDashCircleKpiShowSession(HttpServletRequest request, List<DashStageCircleKpiDto> dashStageCircleKpiDtos) {
        if(!IaisCommonUtils.isEmpty(dashStageCircleKpiDtos)) {
            for(DashStageCircleKpiDto dashStageCircleKpiDto : dashStageCircleKpiDtos) {
                if(dashStageCircleKpiDto != null) {
                    String jsonData = JsonUtil.parseToJson(dashStageCircleKpiDto);
                    String stageId = dashStageCircleKpiDto.getStageId();
                    if(HcsaConsts.ROUTING_STAGE_ASO.equals(stageId)) {
                        ParamUtil.setSessionAttr(request, "dashAsoCircleKpi", jsonData);
                    } else if(HcsaConsts.ROUTING_STAGE_PSO.equals(stageId)) {
                        ParamUtil.setSessionAttr(request, "dashPsoCircleKpi", jsonData);
                    } else if(HcsaConsts.ROUTING_STAGE_PRE.equals(stageId)) {
                        ParamUtil.setSessionAttr(request, "dashPreInspCircleKpi", jsonData);
                    } else if(HcsaConsts.ROUTING_STAGE_INP.equals(stageId)) {
                        ParamUtil.setSessionAttr(request, "dashInspCircleKpi", jsonData);
                    } else if(HcsaConsts.ROUTING_STAGE_POT.equals(stageId)) {
                        ParamUtil.setSessionAttr(request, "dashPostInspCircleKpi", jsonData);
                    } else if(HcsaConsts.ROUTING_STAGE_AO1.equals(stageId)) {
                        ParamUtil.setSessionAttr(request, "dashAo1CircleKpi", jsonData);
                    } else if(HcsaConsts.ROUTING_STAGE_AO2.equals(stageId)) {
                        ParamUtil.setSessionAttr(request, "dashAo2CircleKpi", jsonData);
                    } else if(HcsaConsts.ROUTING_STAGE_AO3.equals(stageId)) {
                        ParamUtil.setSessionAttr(request, "dashAo3CircleKpi", jsonData);
                    } else {
                        ParamUtil.setSessionAttr(request, "dashOverAllCircleKpi", jsonData);
                    }
                }
            }
        }
    }

    private void setSessionBySvcAppTypeFilter(HttpServletRequest request, String[] services, String[] appTypes, String applicationNo) {
        if(services != null && services.length > 0) {
            List<String> serviceList = Arrays.asList(services);
            ParamUtil.setSessionAttr(request, "dashSvcCheckList", (Serializable) serviceList);
        } else {
            ParamUtil.setSessionAttr(request, "dashSvcCheckList", null);
        }
        if(appTypes != null && appTypes.length > 0) {
            List<String> appTypeList = Arrays.asList(appTypes);
            ParamUtil.setSessionAttr(request, "dashAppTypeCheckList", (Serializable) appTypeList);
        } else {
            ParamUtil.setSessionAttr(request, "dashAppTypeCheckList", null);
        }
        if(!StringUtil.isEmpty(applicationNo)){
            ParamUtil.setSessionAttr(request, "dashFilterAppNo", applicationNo);
        } else {
            ParamUtil.setSessionAttr(request, "dashFilterAppNo", null);
        }
    }

    /**
     * StartStep: beStatisticsBoardStep
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardStep(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardStep start ...."));
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        ParamUtil.setRequestAttr(bpc.request, "dashActionValue", actionValue);
    }

    /**
     * StartStep: beStatisticsBoardSearch
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardSearch(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardSearch start ...."));
        List<SelectOption> serviceOption = (List<SelectOption>)ParamUtil.getSessionAttr(bpc.request, "dashServiceOption");
        String dashSysStageVal = ParamUtil.getRequestString(bpc.request, "dashSysStageVal");
        String applicationNo = ParamUtil.getRequestString(bpc.request, "applicationNo");
        String[] services = ParamUtil.getStrings(bpc.request,"svcLic");
        String[] appTypes = ParamUtil.getStrings(bpc.request,"appType");
        //create count's searchParam
        SearchParam searchCountParam = new SearchParam(DashAllActionAppQueryDto.class.getName());
        searchCountParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
        //create detail's SearchParam
        SearchParam searchParam = getSearchParam(bpc, true, DashAllGrpAppQueryDto.class.getName());
        //set stageId Filter
        String stageId = mohHcsaBeDashboardService.getStageIdByJspClickVal(dashSysStageVal);
        if(!StringUtil.isEmpty(stageId)) {
            searchCountParam.addFilter("stage_id", stageId, true);
            searchParam.addFilter("stage_id", stageId, true);
        }
        //set filter
        searchCountParam = mohHcsaBeDashboardService.setStatisticsDashFilter(searchCountParam, services, appTypes, applicationNo);
        searchParam = mohHcsaBeDashboardService.setStatisticsDashFilter(searchParam, services, appTypes, applicationNo);
        //get result
        QueryHelp.setMainSql("intraDashboardQuery", "dashSystemOverAll", searchCountParam);
        SearchResult<DashAllActionAppQueryDto> searchCountResult = mohHcsaBeDashboardService.getDashAllActionResult(searchCountParam);
        QueryHelp.setMainSql("intraDashboardQuery", "dashSystemDetail", searchParam);
        SearchResult<DashAllGrpAppQueryDto> searchResult = mohHcsaBeDashboardService.getDashSysGrpDetailQueryResult(searchParam);
        searchResult = mohHcsaBeDashboardService.getDashSysGrpDetailOtherData(searchResult);
        //get Dashboard Circle Kpi Show Dto
        List<DashStageCircleKpiDto> dashStageCircleKpiDtos = mohHcsaBeDashboardService.getDashStageSvcKpiShow(searchCountResult, serviceOption);
        //set Dashboard Circle Kpi Show Session
        setDashStageKpiShowSession(bpc.request, dashStageCircleKpiDtos, serviceOption);
        //set session
        setSessionBySvcAppTypeFilter(bpc.request, services, appTypes, applicationNo);
        ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "dashSearchResult", searchResult);
        ParamUtil.setSessionAttr(bpc.request, "dashSysStageValReq", dashSysStageVal);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false, null);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc, boolean isNew, String className){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "dashSearchParam");
        int pageSize = SystemParamUtil.getDefaultPageSize();
        if(searchParam == null || isNew){
            searchParam = new SearchParam(className);
            searchParam.setPageSize(pageSize);
            searchParam.setPageNo(1);
            searchParam.setSort("SUBMIT_DT", SearchParam.ASCENDING);
        }
        return searchParam;
    }

    /**
     * StartStep: beStatisticsBoardPage
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardPage(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardPage start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam, bpc.request);
        List<SelectOption> serviceOption = (List<SelectOption>)ParamUtil.getSessionAttr(bpc.request, "dashServiceOption");
        String dashSysStageVal = ParamUtil.getRequestString(bpc.request, "dashSysStageVal");
        String applicationNo = ParamUtil.getRequestString(bpc.request, "applicationNo");
        String[] services = ParamUtil.getStrings(bpc.request,"svcLic");
        String[] appTypes = ParamUtil.getStrings(bpc.request,"appType");
        //create count's searchParam
        SearchParam searchCountParam = new SearchParam(DashAllActionAppQueryDto.class.getName());
        searchCountParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
        //set stageId Filter
        String stageId = mohHcsaBeDashboardService.getStageIdByJspClickVal(dashSysStageVal);
        if(!StringUtil.isEmpty(stageId)) {
            searchCountParam.addFilter("stage_id", stageId, true);
            searchParam.addFilter("stage_id", stageId, true);
        }
        //set filter
        searchCountParam = mohHcsaBeDashboardService.setStatisticsDashFilter(searchCountParam, services, appTypes, applicationNo);
        searchParam = mohHcsaBeDashboardService.setStatisticsDashFilter(searchParam, services, appTypes, applicationNo);
        //get result
        QueryHelp.setMainSql("intraDashboardQuery", "dashSystemOverAll", searchCountParam);
        SearchResult<DashAllActionAppQueryDto> searchCountResult = mohHcsaBeDashboardService.getDashAllActionResult(searchCountParam);
        QueryHelp.setMainSql("intraDashboardQuery", "dashSystemDetail", searchParam);
        SearchResult<DashAllGrpAppQueryDto> searchResult = mohHcsaBeDashboardService.getDashSysGrpDetailQueryResult(searchParam);
        searchResult = mohHcsaBeDashboardService.getDashSysGrpDetailOtherData(searchResult);
        //get Dashboard Circle Kpi Show Dto
        List<DashStageCircleKpiDto> dashStageCircleKpiDtos = mohHcsaBeDashboardService.getDashStageSvcKpiShow(searchCountResult, serviceOption);
        //set Dashboard Circle Kpi Show Session
        setDashStageKpiShowSession(bpc.request, dashStageCircleKpiDtos, serviceOption);
        //set session
        setSessionBySvcAppTypeFilter(bpc.request, services, appTypes, applicationNo);
        ParamUtil.setSessionAttr(bpc.request, "dashSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "dashSearchResult", searchResult);
        ParamUtil.setSessionAttr(bpc.request, "dashSysStageValReq", dashSysStageVal);
    }

    private void setDashStageKpiShowSession(HttpServletRequest request, List<DashStageCircleKpiDto> dashStageCircleKpiDtos, List<SelectOption> serviceOption) {
        if(!IaisCommonUtils.isEmpty(serviceOption) && !IaisCommonUtils.isEmpty(dashStageCircleKpiDtos)) {
            for(SelectOption selectOption : serviceOption) {
                if(selectOption != null) {
                    String svcCodeOpVal = selectOption.getValue();
                    for(DashStageCircleKpiDto dashStageCircleKpiDto : dashStageCircleKpiDtos) {
                        String svcCode = dashStageCircleKpiDto.getSvcCode();
                        String jsonData = JsonUtil.parseToJson(dashStageCircleKpiDto);
                        if(!StringUtil.isEmpty(svcCodeOpVal) && svcCodeOpVal.equals(svcCode)) {
                            StringBuilder stringBuilder = new StringBuilder("dash");
                            stringBuilder.append(svcCode);
                            stringBuilder.append("CircleKpi");
                            ParamUtil.setSessionAttr(request, stringBuilder.toString(), jsonData);
                        }
                        if(StringUtil.isEmpty(svcCode)) {
                            ParamUtil.setSessionAttr(request, "dashAllSvcCircleKpi", jsonData);
                        }
                    }
                }
            }
        }
    }

    /**
     * StartStep: beStatisticsBoardSort
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardSort(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardSort start ...."));

    }

    /**
     * StartStep: beStatisticsBoardQuery
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardQuery(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardQuery start ...."));

    }

    /**
     * StartStep: beStatisticsBoardDetail
     *
     * @param bpc
     * @throws
     */
    public void beStatisticsBoardDetail(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beStatisticsBoardDetail start ...."));

    }
}
