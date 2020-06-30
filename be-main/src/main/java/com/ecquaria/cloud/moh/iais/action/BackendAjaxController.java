package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppStageSlaTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionAppInGroupQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryMainClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Comparator;
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
    private AppPremisesRoutingHistoryMainClient appPremisesRoutingHistoryMainClient;

    @Autowired
    private HcsaConfigMainClient hcsaConfigClient;

    @Autowired
    private InspectionTaskMainClient inspectionTaskMainClient;

    @Autowired
    private InspEmailService inspEmailService;


    @RequestMapping(value = "appGroup.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> appGroup(HttpServletRequest request, HttpServletResponse response) {
        String groupNo = request.getParameter("groupno");
        SearchParam searchParamAjax = (SearchParam) ParamUtil.getSessionAttr(request, "searchParamAjax");
        Map<String, String> appNoUrl = (Map<String, String>) ParamUtil.getSessionAttr(request, "appNoUrl");
        Map<String, String> taskList = (Map<String, String>) ParamUtil.getSessionAttr(request, "taskList");
        String hastaskList = (String)ParamUtil.getSessionAttr(request, "hastaskList");
        Map<String, TaskDto> taskMap = (Map<String, TaskDto>) ParamUtil.getSessionAttr(request, "taskMap");

        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        if (groupNo != null) {
            searchParamAjax.addFilter("groupNo", groupNo, true);

            QueryHelp.setMainSql("inspectionQuery", "AppByGroupAjax", searchParamAjax);
            SearchResult<InspectionAppInGroupQueryDto> ajaxResult = inspectionService.searchInspectionBeAppGroupAjax(searchParamAjax);
            Map<String,String> serviceNameMap = IaisCommonUtils.genNewHashMap();

            //get lastest version
            Map<String, InspectionAppInGroupQueryDto> lastestResultMap = IaisCommonUtils.genNewHashMap();
            for (InspectionAppInGroupQueryDto item:ajaxResult.getRows()
                 ) {
                if(lastestResultMap.get(item.getApplicationNo()) != null){
                    if(Integer.parseInt(lastestResultMap.get(item.getApplicationNo()).getVersion()) < Integer.parseInt(item.getVersion())){
                        lastestResultMap.remove(item.getApplicationNo());
                        lastestResultMap.put(item.getApplicationNo(), item);
                    }
                }else{
                    lastestResultMap.put(item.getApplicationNo(), item);
                }
            }
            List<InspectionAppInGroupQueryDto> lastestResultList = IaisCommonUtils.genNewArrayList();
            for (Map.Entry<String, InspectionAppInGroupQueryDto> entry : lastestResultMap.entrySet()) {
                lastestResultList.add(entry.getValue());
            }
            lastestResultList.sort(Comparator.comparing(InspectionAppInGroupQueryDto::getApplicationNo));

            for (InspectionAppInGroupQueryDto item:lastestResultList) {
                HcsaServiceDto hcsaServiceDto = inspectionAssignTaskService.getHcsaServiceDtoByServiceId(item.getServiceId());
                serviceNameMap.put(hcsaServiceDto.getId(),hcsaServiceDto.getSvcName());
                item.setStatus(MasterCodeUtil.getCodeDesc(item.getStatus()));
                if(item.getHciCode()==null){
                    item.setHciCode("N/A");
                }
                if(item.getLicenceId() != null){
                    LicenceDto licenceDto = inspEmailService.getLicBylicId(item.getLicenceId());
                    if(licenceDto != null && licenceDto.getExpiryDate() != null){
                        item.setExpiryDate(Formatter.formatDate(licenceDto.getExpiryDate()));
                    }else{
                        item.setExpiryDate("N/A");
                    }
                }else{
                    item.setExpiryDate("N/A");
                }

                //show kpi colour
                String colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
                ApplicationDto applicationDto = inspectionTaskMainClient.getApplicationByCorreId(item.getRefNo() ).getEntity();
                TaskDto taskDto = taskMap.get(item.getRefNo());
                String stage;
                if(HcsaConsts.ROUTING_STAGE_INS.equals(taskDto.getTaskKey())){
                    AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =
                            appPremisesRoutingHistoryMainClient.getAppPremisesRoutingHistorySubStage(taskDto.getRefNo(), taskDto.getTaskKey()).getEntity();
                    stage = appPremisesRoutingHistoryDto.getSubStage();
                } else {
                    stage = taskDto.getTaskKey();
                }
                HcsaSvcKpiDto hcsaSvcKpiDto = hcsaConfigClient.searchKpiResult(hcsaServiceDto.getSvcCode(), applicationDto.getApplicationType()).getEntity();
                if(hcsaSvcKpiDto != null) {
                    //get current stage worked days
                    int days = 0;
                    if(!StringUtil.isEmpty(stage)) {
                        AppStageSlaTrackingDto appStageSlaTrackingDto = inspectionTaskMainClient.getSlaTrackByAppNoStageId(applicationDto.getApplicationNo(), stage).getEntity();
                        if (appStageSlaTrackingDto != null) {
                            days = appStageSlaTrackingDto.getKpiSlaDays();
                        }
                    }
                    //get warning value
                    Map<String, Integer> kpiMap = hcsaSvcKpiDto.getStageIdKpi();
                    int kpi = 0;
                    if(!StringUtil.isEmpty(stage)) {
                        if (kpiMap != null && kpiMap.get(stage) != null) {
                            kpi = kpiMap.get(stage);
                        }
                    }
                    //get threshold value
                    int remThreshold = 0;
                    if (hcsaSvcKpiDto.getRemThreshold() != null) {
                        remThreshold = hcsaSvcKpiDto.getRemThreshold();
                    }
                    //get color
                    colour = getColorByWorkAndKpiDay(kpi, days, remThreshold);
                }
                //set colour to show jsp
                item.setTimeLimitWarning(colour);
            }
            map.put("serviceName", serviceNameMap);
            map.put("appNoUrl", appNoUrl);
            map.put("taskList", taskList);
            map.put("hastaskList", hastaskList);
            map.put("ajaxResult", lastestResultList);
            map.put("result", "Success");
        } else {
            map.put("result", "Fail");
        }
        return map;
    }

    private String getColorByWorkAndKpiDay(int kpi, int days, int remThreshold) {
        String colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
        if(remThreshold != 0) {
            if (days < remThreshold) {
                colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
            }
            if (kpi != 0) {
                if (remThreshold <= days && days <= kpi) {
                    colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_AMBER;
                } else if (days > kpi) {
                    colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_RED;
                }
            }
        }
        return colour;
    }

    @RequestMapping(value = "setCurrentRole.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, String> setCurrentRole(HttpServletRequest request, HttpServletResponse response) {
        String curRole = request.getParameter("curRole");
        LoginContext loginContext = new LoginContext();
        loginContext.setCurRoleId(curRole);
        Map<String, String> res = IaisCommonUtils.genNewHashMap();
        res.put("role",curRole);
        return res;
    }

    @RequestMapping(value = "changeTaskStatus.do", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> changeTaskStatus(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> map = new HashMap<>();
        String taskId = ParamUtil.getMaskedString(request, "taskId");
        String res = inspectionAssignTaskService.taskRead(taskId);
        map.put("res",res);
        return map;
    }

}