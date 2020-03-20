package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
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

    @RequestMapping(value = "common.do", method = RequestMethod.POST)

    public @ResponseBody Map<String, Object> appGroup(HttpServletRequest request) {
        String groupNo = request.getParameter("groupNo");
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
                }
            }
            map.put("result", "Success");
            map.put("ajaxResult", ajaxResult);
        } else {
            map.put("result", "Fail");
        }
        return map;
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