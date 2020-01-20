package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppPremInsDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppInGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.InspectionMainAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusMainClient;
import com.ecquaria.cloud.moh.iais.service.client.BelicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: guyin
 * @Program: iais-egp
 * @Create: 2019-11-30 13:39
 **/
@Slf4j
@Controller
@RequestMapping("/backend")
public class BackendAjaxController {

    @Autowired
    private InspectionMainService inspectionService;

    @Autowired
    private InspectionMainAssignTaskService inspectionAssignTaskService;

    @Autowired
    private AppInspectionStatusMainClient appInspectionStatusClient;

    @Autowired
    private BelicationClient belicationClient;

    @Autowired
    private HcsaConfigMainClient hcsaConfigClient;

    @Autowired
    private InspectionTaskMainClient inspectionTaskMainClient;

    @Autowired
    private OrganizationMainClient organizationMainClient;

    @RequestMapping(value = "appGroup.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> appGroup(HttpServletRequest request, HttpServletResponse response) {
        String groupNo = request.getParameter("groupno");
        SearchParam searchParamAjax = (SearchParam) ParamUtil.getSessionAttr(request, "searchParamAjax");
        Map<String, String> appNoUrl = (Map<String, String>) ParamUtil.getSessionAttr(request, "appNoUrl");
        Map<String, String> taskList = (Map<String, String>) ParamUtil.getSessionAttr(request, "taskList");
        String hastaskList = (String)ParamUtil.getSessionAttr(request, "hastaskList");
        Map<String, TaskDto> taskMap = (Map<String, TaskDto>) ParamUtil.getSessionAttr(request, "taskMap");

        Map<String, Object> map = new HashMap();
        if (groupNo != null) {
            searchParamAjax.addFilter("groupNo", groupNo, true);

            QueryHelp.setMainSql("inspectionQuery", "AppByGroupAjax", searchParamAjax);
            SearchResult<InspectionAppInGroupQueryDto> ajaxResult = inspectionService.searchInspectionBeAppGroupAjax(searchParamAjax);
            Map<String,String> serviceNameMap = new HashMap<>();
            for (InspectionAppInGroupQueryDto item:ajaxResult.getRows()) {
                HcsaServiceDto hcsaServiceDto = inspectionAssignTaskService.getHcsaServiceDtoByServiceId(item.getServiceId());
                serviceNameMap.put(hcsaServiceDto.getId(),hcsaServiceDto.getSvcName());
                item.setStatus(MasterCodeUtil.getCodeDesc(item.getStatus()));
                if(item.getHciCode()==null){
                    item.setHciCode("N/A");
                }

                //todo Kpi Colour
                /*TaskDto taskDto = taskMap.get(item.getRefNo());
                String timeLimitWarningColour = getTimeLimitWarningColourByTask(item, hcsaServiceDto, taskDto);*/
                item.setTimeLimitWarning(HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK);
            }
            map.put("serviceName", serviceNameMap);
            map.put("appNoUrl", appNoUrl);
            map.put("taskList", taskList);
            map.put("hastaskList", hastaskList);
            map.put("ajaxResult", ajaxResult);
            map.put("result", "Success");
        } else {
            map.put("result", "Fail");
        }
        return map;
    }

    private String getTimeLimitWarningColourByTask(InspectionAppInGroupQueryDto inspectionAppInGroupQueryDto, HcsaServiceDto hcsaServiceDto, TaskDto taskDto) {
        String colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
        ApplicationViewDto applicationViewDto = belicationClient.getAppViewByCorrelationId(inspectionAppInGroupQueryDto.getRefNo()).getEntity();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        HcsaSvcKpiDto hcsaSvcKpiDto = hcsaConfigClient.searchKpiResult(hcsaServiceDto.getSvcCode(), applicationDto.getApplicationType()).getEntity();
        int days = 0;
        if(hcsaSvcKpiDto != null){
            if(taskDto.getTaskKey().equals(HcsaConsts.ROUTING_STAGE_INS)) {
                String subStage = getSubStageByInspectionStatus(inspectionAppInGroupQueryDto);
                Map<String, Integer> kpiMap = hcsaSvcKpiDto.getStageIdKpi();
                if(kpiMap != null) {
                    int kpi = kpiMap.get(taskDto.getTaskKey());
                    Map<Integer, Integer> workAndNonMap = getWorkingDaysBySubStage(subStage, taskDto);
                    if(workAndNonMap != null){
                        for(Map.Entry<Integer, Integer> map:workAndNonMap.entrySet()){
                            days = map.getKey();
                        }
                    }
                    if (days < hcsaSvcKpiDto.getRemThreshold()) {
                        colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
                    } else if (hcsaSvcKpiDto.getRemThreshold() <= days && days <= kpi) {
                        colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_AMBER;
                    } else if (days > kpi) {
                        colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_RED;
                    }
                }
            } else {
                Map<String, Integer> kpiMap = hcsaSvcKpiDto.getStageIdKpi();
                if(kpiMap != null) {
                    Map<Integer, Integer> workAndNonMap = new HashMap();
                    int kpi = kpiMap.get(taskDto.getTaskKey());
                    Date startDate = taskDto.getDateAssigned();
                    Date completeDate;
                    if(taskDto.getSlaDateCompleted() != null){
                        completeDate = taskDto.getSlaDateCompleted();
                    } else {
                        completeDate = new Date();
                    }
                    int allWorkDays = 0;
                    int allHolidays = 0;
                    Map<Integer, Integer> workAndNonMapS = MiscUtil.getActualWorkingDays(startDate, completeDate, taskDto.getWkGrpId());
                    for(Map.Entry<Integer, Integer> map:workAndNonMapS.entrySet()){
                        allWorkDays = allWorkDays + map.getKey();
                        allHolidays = allHolidays + map.getValue();
                    }
                    workAndNonMap.put(allWorkDays, allHolidays);
                    if(workAndNonMap != null){
                        for(Map.Entry<Integer, Integer> map:workAndNonMap.entrySet()){
                            days = map.getKey();
                        }
                    }
                    if (days < hcsaSvcKpiDto.getRemThreshold()) {
                        colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
                    } else if (hcsaSvcKpiDto.getRemThreshold() <= days && days <= kpi) {
                        colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_AMBER;
                    } else if (days > kpi) {
                        colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_RED;
                    }
                }
            }
        }
        return colour;
    }

    private Map<Integer, Integer> getWorkingDaysBySubStage(String subStage, TaskDto taskDto) {
        Map<Integer, Integer> workAndNonMap = new HashMap();
        List<String> processUrls = new ArrayList<>();
        if(StringUtil.isEmpty(subStage)){
            return null;
        }
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto.setWrkGrpId(taskDto.getWkGrpId());
        appPremisesRoutingHistoryDto.setRoleId(taskDto.getRoleId());
        appPremisesRoutingHistoryDto.setAppPremCorreId(taskDto.getRefNo());
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = inspectionTaskMainClient.getHistoryForKpi(appPremisesRoutingHistoryDto).getEntity();
        List<TaskDto> taskDtoList = new ArrayList<>();
        int allWorkDays = 0;
        int allHolidays = 0;
        if(subStage.equals(HcsaConsts.ROUTING_STAGE_INP)){
            AppPremInsDraftDto appPremInsDraftDto = inspectionTaskMainClient.getAppPremInsDraftDtoByAppPreCorrId(taskDto.getRefNo()).getEntity();
            Date startDate = appPremInsDraftDto.getClockin();
            Date completeDate;
            for(AppPremisesRoutingHistoryDto aprhDto : appPremisesRoutingHistoryDtos) {
                TaskDto tDto = new TaskDto();
                InspecTaskCreAndAssDto inspecTaskCreAndAssDto = new InspecTaskCreAndAssDto();
                tDto.setRefNo(taskDto.getRefNo());
                tDto.setRoleId(aprhDto.getRoleId());
                tDto.setWkGrpId(taskDto.getWkGrpId());
                tDto.setTaskKey(taskDto.getTaskKey());
                inspecTaskCreAndAssDto.setTaskDto(tDto);
                inspecTaskCreAndAssDto.setProcessUrls(processUrls);
                List<TaskDto> taskDtos = organizationMainClient.getInsKpiTask(inspecTaskCreAndAssDto).getEntity();
                if(!IaisCommonUtils.isEmpty(taskDtos)){
                    for(TaskDto taskDtoSingle : taskDtos){
                        taskDtoList.add(taskDtoSingle);
                    }
                }
            }
            for(TaskDto td : taskDtoList){
                if(td.getSlaDateCompleted() == null){
                    completeDate = new Date();
                } else {
                    completeDate = td.getSlaDateCompleted();
                }
                Map<Integer, Integer> workAndNonMapS = MiscUtil.getActualWorkingDays(startDate, completeDate, td.getWkGrpId());
                for(Map.Entry<Integer, Integer> map:workAndNonMapS.entrySet()){
                    allWorkDays = allWorkDays + map.getKey();
                    allHolidays = allHolidays + map.getValue();
                }
            }
            workAndNonMap.put(allWorkDays, allHolidays);
        } else {
            for(AppPremisesRoutingHistoryDto aprhDto : appPremisesRoutingHistoryDtos) {
                TaskDto tDto = new TaskDto();
                InspecTaskCreAndAssDto inspecTaskCreAndAssDto = new InspecTaskCreAndAssDto();
                tDto.setRefNo(taskDto.getRefNo());
                tDto.setRoleId(aprhDto.getRoleId());
                tDto.setWkGrpId(taskDto.getWkGrpId());
                tDto.setTaskKey(taskDto.getTaskKey());
                inspecTaskCreAndAssDto.setTaskDto(tDto);
                inspecTaskCreAndAssDto.setProcessUrls(processUrls);
                List<TaskDto> taskDtos = organizationMainClient.getInsKpiTask(inspecTaskCreAndAssDto).getEntity();
                if(!IaisCommonUtils.isEmpty(taskDtos)){
                    for(TaskDto taskDtoSingle : taskDtos){
                        taskDtoList.add(taskDtoSingle);
                    }
                }
            }
            workAndNonMap = getActualWorkingDays(taskDtoList, allWorkDays, allHolidays);
        }
        return workAndNonMap;
    }

    private Map<Integer, Integer> getActualWorkingDays(List<TaskDto> taskDtoList, int allWorkDays, int allHolidays) {
        Map<Integer, Integer> workAndNonMap = new HashMap();
        for(TaskDto td : taskDtoList){
            Date startDate = td.getDateAssigned();
            Date completeDate;
            if(td.getSlaDateCompleted() == null){
                completeDate = new Date();
            } else {
                completeDate = td.getSlaDateCompleted();
            }
            Map<Integer, Integer> workAndNonMapS = MiscUtil.getActualWorkingDays(startDate, completeDate, td.getWkGrpId());
            for(Map.Entry<Integer, Integer> map:workAndNonMapS.entrySet()){
                allWorkDays = allWorkDays + map.getKey();
                allHolidays = allHolidays + map.getValue();
            }
        }
        workAndNonMap.put(allWorkDays, allHolidays);
        return workAndNonMap;
    }

    private String getSubStageByInspectionStatus(InspectionAppInGroupQueryDto inspectionAppInGroupQueryDto) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(inspectionAppInGroupQueryDto.getRefNo()).getEntity();
        String subStage = "";
        if(appInspectionStatusDto != null) {
            String status = appInspectionStatusDto.getStatus();
            if (status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_PRE)) {
                subStage = HcsaConsts.ROUTING_STAGE_PRE;
            } else if (status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_CHECKLIST_VERIFY)) {
                subStage = HcsaConsts.ROUTING_STAGE_INP;
            } else if (status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT) ||
                    status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_AO1_RESULT) ||
                    status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_EMAIL_VERIFY) ||
                    status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_AO1_EMAIL_VERIFY) ||
                    status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_REVIEW_CHECKLIST_EMAIL) ||
                    status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_NC_RECTIFICATION_EMAIL) ||
                    status.equals(InspectionConstants.INSPECTION_STATUS_PENDING_REVIEW_RECTIFICATION_NC)) {
                subStage = HcsaConsts.ROUTING_STAGE_POT;
            }
        }
        return subStage;
    }

    @RequestMapping(value = "setCurrentRole.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> setCurrentRole(HttpServletRequest request, HttpServletResponse response) {
        String curRole = request.getParameter("curRole");
        LoginContext loginContext = new LoginContext();
        loginContext.setCurRoleId(curRole);
        Map<String, String> res = new HashMap<>();
        res.put("role",curRole);
        return res;
    }
}