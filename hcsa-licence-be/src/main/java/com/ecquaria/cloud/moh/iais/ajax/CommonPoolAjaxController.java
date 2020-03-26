package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @Author: guyin
 * @Program: iais-egp
 * @Create: 2019-11-30 13:39
 **/
@Slf4j
@Controller
@RequestMapping("/common-pool")
public class CommonPoolAjaxController {

    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private InspectionService inspectionService;

    @RequestMapping(value = "common.do", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> appGroup(HttpServletRequest request) {
        String groupNo = MaskUtil.unMaskValue("appGroupNo", request.getParameter("groupNo"));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        //get task by user workGroupId
        List<TaskDto> commPools = inspectionAssignTaskService.getCommPoolByGroupWordId(loginContext);
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        if(!StringUtil.isEmpty(groupNo)){
            SearchParam searchParam = new SearchParam(ComPoolAjaxQueryDto.class.getName());
            searchParam.setPageSize(10);
            searchParam.setPageNo(1);
            searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
            searchParam.addFilter("groupNo", groupNo, true);
            setMapTaskId(request, commPools);
            List<String> appCorrId_list = inspectionAssignTaskService.getAppCorrIdListByPool(commPools);
            StringBuilder sb = new StringBuilder("(");
            for(int i = 0; i < appCorrId_list.size(); i++){
                sb.append(":appCorrId" + i).append(",");
            }
            String inSql = sb.substring(0, sb.length() - 1) + ")";
            searchParam.addParam("appCorrId_list", inSql);
            for(int i = 0; i < appCorrId_list.size(); i++){
                searchParam.addFilter("appCorrId" + i, appCorrId_list.get(i));
            }
            QueryHelp.setMainSql("inspectionQuery", "commonPoolAjax", searchParam);
            SearchResult<ComPoolAjaxQueryDto> ajaxResult = inspectionAssignTaskService.getAjaxResultByParam(searchParam);
            List<ComPoolAjaxQueryDto> comPoolAjaxQueryDtos = ajaxResult.getRows();
            if(!IaisCommonUtils.isEmpty(comPoolAjaxQueryDtos)){
                for(ComPoolAjaxQueryDto comPoolAjaxQueryDto : comPoolAjaxQueryDtos){
                    AppGrpPremisesDto appGrpPremisesDto = inspectionAssignTaskService.getAppGrpPremisesDtoByAppGroId(comPoolAjaxQueryDto.getId());
                    String address = inspectionAssignTaskService.getAddress(appGrpPremisesDto);
                    if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                        comPoolAjaxQueryDto.setHciAddress(appGrpPremisesDto.getHciName() + " / " + address);
                    } else {
                        comPoolAjaxQueryDto.setHciAddress(address);
                    }
                    comPoolAjaxQueryDto.setAppStatus(MasterCodeUtil.getCodeDesc(comPoolAjaxQueryDto.getAppStatus()));
                    HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(comPoolAjaxQueryDto.getServiceId()).getEntity();;
                    comPoolAjaxQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                    comPoolAjaxQueryDto.setHciCode(appGrpPremisesDto.getHciCode());
                    String maskId = MaskUtil.maskValue("appCorrelationId", comPoolAjaxQueryDto.getId());
                    comPoolAjaxQueryDto.setMaskId(maskId);
                }
            }
            map.put("result", "Success");
            map.put("ajaxResult", ajaxResult);
        } else {
            map.put("result", "Fail");
        }
        return map;
    }

    @RequestMapping(value = "supervisor.do", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> supervisorPool(HttpServletRequest request) {
        Map<String, Object> jsonMap = IaisCommonUtils.genNewHashMap();
        //get session data
        String appGroupId = MaskUtil.unMaskValue("appGroupId", request.getParameter("groupId"));
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto) ParamUtil.getSessionAttr(request, "groupRoleFieldDto");
        List<String> workGroupIds = (List<String>) ParamUtil.getSessionAttr(request, "workGroupIds");
        Map<String, SuperPoolTaskQueryDto> assignMap = (Map<String, SuperPoolTaskQueryDto>) ParamUtil.getSessionAttr(request, "assignMap");
        if(!IaisCommonUtils.isEmpty(workGroupIds) && !StringUtil.isEmpty(appGroupId)) {
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationClient.getPremCorrDtoByAppGroupId(appGroupId).getEntity();
            //filter list
            List<String> appCorrId_list = getRefNoList(appPremisesCorrelationDtos);
            List<String> status = IaisCommonUtils.genNewArrayList();
            status.add(TaskConsts.TASK_STATUS_PENDING);
            status.add(TaskConsts.TASK_STATUS_READ);
            //create searchParam
            SearchParam searchParam = new SearchParam(SuperPoolTaskQueryDto.class.getName());
            searchParam.setPageSize(10);
            searchParam.setPageNo(1);
            searchParam.setSort("REF_NO", SearchParam.ASCENDING);
            //set filters
            StringBuilder sb = new StringBuilder("(");
            if (!IaisCommonUtils.isEmpty(appCorrId_list)) {
                for (int i = 0; i < appCorrId_list.size(); i++) {
                    sb.append(":appCorrId" + i).append(",");
                }
                String inSq1 = sb.substring(0, sb.length() - 1) + ")";
                searchParam.addParam("appCorrId_list", inSq1);
                for (int i = 0; i < appCorrId_list.size(); i++) {
                    searchParam.addFilter("appCorrId" + i, appCorrId_list.get(i));
                }
            }

            StringBuilder sb2 = new StringBuilder("(");
            for (int i = 0; i < status.size(); i++) {
                sb2.append(":tStatus" + i).append(",");
            }
            String inSq2 = sb2.substring(0, sb2.length() - 1) + ")";
            searchParam.addParam("taskStatus", inSq2);
            for (int i = 0; i < status.size(); i++) {
                searchParam.addFilter("tStatus" + i, status.get(i));
            }

            StringBuilder sb3 = new StringBuilder("(");
            for (int i = 0; i < workGroupIds.size(); i++) {
                sb3.append(":workId" + i).append(",");
            }
            String inSq3 = sb3.substring(0, sb3.length() - 1) + ")";
            searchParam.addParam("workGroupId", inSq3);
            for (int i = 0; i < workGroupIds.size(); i++) {
                searchParam.addFilter("workId" + i, workGroupIds.get(i));
            }
            //do search
            QueryHelp.setMainSql("inspectionQuery", "supervisorPoolDropdown", searchParam);
            SearchResult<SuperPoolTaskQueryDto> searchResult = inspectionService.getSupPoolSecondByParam(searchParam);
            searchResult = inspectionService.getSecondSearchOtherData(searchResult);
            jsonMap.put("ajaxResult", searchResult);
            jsonMap.put("result", "Success");
            assignMap = setTaskIdAndDataMap(assignMap, searchResult);
        } else {
            jsonMap.put("result", "Fail");
        }
        ParamUtil.setSessionAttr(request, "assignMap", (Serializable) assignMap);
        jsonMap.put("memberName", groupRoleFieldDto.getGroupMemBerName());
        //get other data
        return jsonMap;
    }

    private Map<String, SuperPoolTaskQueryDto> setTaskIdAndDataMap(Map<String, SuperPoolTaskQueryDto> assignMap, SearchResult<SuperPoolTaskQueryDto> searchResult) {
        if(assignMap == null) {
            assignMap = IaisCommonUtils.genNewHashMap();
        }
        if(searchResult != null && !IaisCommonUtils.isEmpty(searchResult.getRows())){
            List<SuperPoolTaskQueryDto> superPoolTaskQueryDtos = searchResult.getRows();
            for(SuperPoolTaskQueryDto superPoolTaskQueryDto : superPoolTaskQueryDtos){
                assignMap.put(superPoolTaskQueryDto.getId(), superPoolTaskQueryDto);
            }
        }
        return assignMap;
    }

    private List<String> getRefNoList(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos) {
        List<String> refNoList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appPremisesCorrelationDtos)) {
            for (AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos) {
                refNoList.add(appPremisesCorrelationDto.getId());
            }
        }
        return refNoList;
    }

    private void setMapTaskId(HttpServletRequest request, List<TaskDto> commPools) {
        Map<String, String> appCorrIdTaskIdMap = IaisCommonUtils.genNewHashMap();
        if(!IaisCommonUtils.isEmpty(commPools)){
            for(TaskDto td:commPools){
                appCorrIdTaskIdMap.put(td.getRefNo(), td.getId());
            }
        }
        ParamUtil.setSessionAttr(request, "appCorrIdTaskIdMap", (Serializable) appCorrIdTaskIdMap);
    }
}