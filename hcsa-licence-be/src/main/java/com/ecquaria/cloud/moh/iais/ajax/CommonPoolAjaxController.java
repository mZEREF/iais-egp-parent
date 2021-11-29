package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeView;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.SystemSearchAssignPoolService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
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

    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;

    @Autowired
    private SystemSearchAssignPoolService systemSearchAssignPoolService;

    @RequestMapping(value = "common.do", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> appGroup(HttpServletRequest request) {
        String groupNo = MaskUtil.unMaskValue("appGroupNo", request.getParameter("groupNo"));
        HcsaTaskAssignDto hcsaTaskAssignDto = (HcsaTaskAssignDto)ParamUtil.getSessionAttr(request, "hcsaTaskAssignDto");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String hci_address = (String)ParamUtil.getSessionAttr(request, "comPoolHciAddress");
        SearchParam searchParamGroup = (SearchParam) ParamUtil.getSessionAttr(request, "cPoolSearchParam");
        //get task by user workGroupId
        List<TaskDto> commPools = inspectionAssignTaskService.getCommPoolByGroupWordId(loginContext);
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        if(!StringUtil.isEmpty(groupNo)){
            //create new searchParam
            SearchParam searchParam = new SearchParam(ComPoolAjaxQueryDto.class.getName());
            searchParam.setPageSize(10);
            searchParam.setPageNo(1);
            searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
            //appCorrIdTaskIdMap
            setMapTaskId(request, commPools);
            //set filter
            List<String> appCorrId_list = inspectionAssignTaskService.getAppCorrIdListByPool(commPools);
            //filter appCorrId_list
            String appPremCorrId = SqlHelper.constructInCondition("T2.ID", appCorrId_list.size());
            searchParam.addParam("appCorrId_list", appPremCorrId);
            for(int i = 0; i < appCorrId_list.size(); i++){
                searchParam.addFilter("T2.ID" + i, appCorrId_list.get(i));
            }
            //other filter
            searchParam = setFilterByAppGrpParamAndNo(searchParamGroup, groupNo, searchParam, hcsaTaskAssignDto, null,
                    "T5.APP_PREM_ID", "appPremId_list", hci_address);
            //get search result
            QueryHelp.setMainSql("inspectionQuery", "commonPoolAjax", searchParam);
            SearchResult<ComPoolAjaxQueryDto> ajaxResult = inspectionAssignTaskService.getAjaxResultByParam(searchParam);
            //set other data
            List<ComPoolAjaxQueryDto> comPoolAjaxQueryDtos = ajaxResult.getRows();
            if(!IaisCommonUtils.isEmpty(comPoolAjaxQueryDtos)){
                for(ComPoolAjaxQueryDto comPoolAjaxQueryDto : comPoolAjaxQueryDtos){
                    //get hciName / address
                    AppGrpPremisesDto appGrpPremisesDto = inspectionAssignTaskService.getAppGrpPremisesDtoByAppGroId(comPoolAjaxQueryDto.getId());
                    String address = inspectionAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                    if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                        comPoolAjaxQueryDto.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                    } else {
                        comPoolAjaxQueryDto.setHciAddress(StringUtil.viewHtml(address));
                    }
                    //app status
                    comPoolAjaxQueryDto.setAppStatus(MasterCodeUtil.getCodeDesc(comPoolAjaxQueryDto.getAppStatus()));
                    //service
                    HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(comPoolAjaxQueryDto.getServiceId()).getEntity();;
                    comPoolAjaxQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                    comPoolAjaxQueryDto.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getHciCode()));
                    String maskId = MaskUtil.maskValue("appCorrelationId", comPoolAjaxQueryDto.getId());
                    comPoolAjaxQueryDto.setMaskId(maskId);
                    //application
                    ApplicationDto applicationDto = applicationClient.getAppByNo(comPoolAjaxQueryDto.getApplicationNo()).getEntity();
                    //get license date
                    if(StringUtil.isEmpty(applicationDto.getOriginLicenceId())){
                        comPoolAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    } else {
                        LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(applicationDto.getOriginLicenceId()).getEntity();
                        Date licExpiryDate = licenceDto.getExpiryDate();
                        if(licExpiryDate != null) {
                            comPoolAjaxQueryDto.setLicenceExpiryDate(licExpiryDate);
                            String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                            comPoolAjaxQueryDto.setLicenceExpiryDateStr(licExpiryDateStr);
                        } else {
                            comPoolAjaxQueryDto.setLicenceExpiryDate(null);
                            comPoolAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                        }
                    }
                }
            }
            //set js key and value for sub show
            map.put("result", "Success");
            map.put("ajaxResult", ajaxResult);
        } else {
            map.put("result", "Fail");
        }
        return map;
    }

    private SearchParam setFilterByAppGrpParamAndNo(SearchParam searchParamGroup, String groupNo, SearchParam searchParam, HcsaTaskAssignDto hcsaTaskAssignDto,
                                                    String appStatusKey, String fieldName, String filterName, String hci_address) {
        //filter app Grp No
        if(!StringUtil.isEmpty(groupNo)) {
            searchParam.addFilter("groupNo", groupNo, true);
        }

        if(searchParamGroup != null) {
            Map<String, Object> filters = searchParamGroup.getFilters();
            if(filters != null) {
                //get Group filter value
                String application_no = (String)filters.get("application_no");
                String application_type = (String)filters.get("application_type");
                String hci_code = (String)filters.get("hci_code");
                String hci_name = (String)filters.get("hci_name");
                String application_status = (String)filters.get("application_status");

                //set filter value
                if(!StringUtil.isEmpty(application_no)) {
                    searchParam.addFilter("application_no", application_no, true);
                }
                if(!StringUtil.isEmpty(application_type)) {
                    searchParam.addFilter("application_type", application_type, true);
                }
                if(!StringUtil.isEmpty(application_status)) {
                    List<String> appStatus = getSearchAppStatus(application_status);
                    String appStatusStr = SqlHelper.constructInCondition(appStatusKey, appStatus.size());
                    searchParam.addParam("application_status", appStatusStr);
                    for(int i = 0; i < appStatus.size(); i++) {
                        searchParam.addFilter(appStatusKey + i, appStatus.get(i));
                    }
                }
                if(!StringUtil.isEmpty(hci_code)) {
                    searchParam.addFilter("hci_code", hci_code, true);
                }
                if(!StringUtil.isEmpty(hci_name)) {
                    searchParam.addFilter("hci_name", hci_name, true);
                }
                if(!StringUtil.isEmpty(hci_address)) {
                    searchParam = inspectionAssignTaskService.setAppPremisesIdsByUnitNos(searchParam, hci_address, hcsaTaskAssignDto,
                            fieldName, filterName);
                }
            }
        }
        return searchParam;
    }

    public List<String> getSearchAppStatus(String application_status) {
        List<String> appStatus = IaisCommonUtils.genNewArrayList();
        List<MasterCodeView> masterCodeViews = MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_APP_STATUS);
        String codeValue = MasterCodeUtil.getCodeDesc(application_status);
        for (MasterCodeView masterCodeView : masterCodeViews) {
            if(masterCodeView != null && codeValue.equals(masterCodeView.getCodeValue())){
                appStatus.add(masterCodeView.getCode());
            }
        }
        return appStatus;
    }

    @RequestMapping(value = "supervisor.do", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> supervisorPool(HttpServletRequest request) {
        Map<String, Object> jsonMap = IaisCommonUtils.genNewHashMap();
        //get session data
        String appGroupId = MaskUtil.unMaskValue("appGroupId", request.getParameter("groupId"));
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto) ParamUtil.getSessionAttr(request, "groupRoleFieldDto");
        List<String> workGroupIds = (List<String>) ParamUtil.getSessionAttr(request, "workGroupIds");
        Map<String, SuperPoolTaskQueryDto> assignMap = (Map<String, SuperPoolTaskQueryDto>) ParamUtil.getSessionAttr(request, "assignMap");
        String userId = (String) ParamUtil.getSessionAttr(request, "memberId");
        String commonPoolStatus = (String) ParamUtil.getSessionAttr(request, "commonPoolStatus");
        HcsaTaskAssignDto hcsaTaskAssignDto = (HcsaTaskAssignDto)ParamUtil.getSessionAttr(request, "hcsaTaskAssignDto");
        SearchParam searchParamGroup = (SearchParam) ParamUtil.getSessionAttr(request, "supTaskSearchParam");
        String hci_address = (String)ParamUtil.getSessionAttr(request, "superPoolHciAddress");
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
            //set filter common
            searchParam = setFilterByAppGrpParamAndNo(searchParamGroup, null, searchParam, hcsaTaskAssignDto, "T3.STATUS",
                    "T4.ID","appPremId_list", hci_address);

            if (!IaisCommonUtils.isEmpty(appCorrId_list)) {
                String appPremCorrId = SqlHelper.constructInCondition("T1.REF_NO", appCorrId_list.size());
                searchParam.addParam("appCorrId_list", appPremCorrId);
                for (int i = 0; i < appCorrId_list.size(); i++) {
                    searchParam.addFilter("T1.REF_NO" + i, appCorrId_list.get(i));
                }
            }

            StringBuilder sb2 = new StringBuilder("(");
            for (int i = 0; i < status.size(); i++) {
                sb2.append(":tStatus").append(i).append(',');
            }
            String inSq2 = sb2.substring(0, sb2.length() - 1) + ")";
            searchParam.addParam("taskStatus", inSq2);
            for (int i = 0; i < status.size(); i++) {
                searchParam.addFilter("tStatus" + i, status.get(i));
            }

            StringBuilder sb3 = new StringBuilder("(");
            for (int i = 0; i < workGroupIds.size(); i++) {
                sb3.append(":workId").append(i).append(',');
            }
            String inSq3 = sb3.substring(0, sb3.length() - 1) + ")";
            searchParam.addParam("workGroupId", inSq3);
            for (int i = 0; i < workGroupIds.size(); i++) {
                searchParam.addFilter("workId" + i, workGroupIds.get(i));
            }
            //user's task and common
            if(!StringUtil.isEmpty(userId)){
                searchParam.addFilter("userId", userId,true);
            } else if(!StringUtil.isEmpty(commonPoolStatus)){
                searchParam.addFilter("commonPoolStatus", commonPoolStatus,true);
            }
            //do search
            QueryHelp.setMainSql("inspectionQuery", "supervisorPoolDropdown", searchParam);
            SearchResult<SuperPoolTaskQueryDto> searchResult = inspectionService.getSupPoolSecondByParam(searchParam);
            searchResult = inspectionService.getSecondSearchOtherData(searchResult, hcsaTaskAssignDto);
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

    @RequestMapping(value = "system.do", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> systemPool(HttpServletRequest request) {
        Map<String, Object> jsonMap = IaisCommonUtils.genNewHashMap();
        //get session data
        String appGroupId = MaskUtil.unMaskValue("appGroupId", request.getParameter("groupId"));
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        Map<String, SuperPoolTaskQueryDto> assignMap = (Map<String, SuperPoolTaskQueryDto>) ParamUtil.getSessionAttr(request, "assignMap");
        HcsaTaskAssignDto hcsaTaskAssignDto = (HcsaTaskAssignDto)ParamUtil.getSessionAttr(request, "hcsaTaskAssignDto");
        SearchParam searchParamGroup = (SearchParam) ParamUtil.getSessionAttr(request, "systemSearchParam");
        String hci_address = (String)ParamUtil.getSessionAttr(request, "systemPoolHciAddress");
        if(!StringUtil.isEmpty(appGroupId)) {
            //get userId
            String userId = loginContext.getUserId();
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
            //set filter common
            searchParam = setFilterByAppGrpParamAndNo(searchParamGroup, null, searchParam, hcsaTaskAssignDto, "T3.STATUS",
                    "T4.ID","appPremId_list", hci_address);

            //set filters
            if (!IaisCommonUtils.isEmpty(appCorrId_list)) {
                String appPremCorrId = SqlHelper.constructInCondition("T1.REF_NO", appCorrId_list.size());
                searchParam.addParam("appCorrId_list", appPremCorrId);
                for (int i = 0; i < appCorrId_list.size(); i++) {
                    searchParam.addFilter("T1.REF_NO" + i, appCorrId_list.get(i));
                }
            }
            StringBuilder sb2 = new StringBuilder("(");
            for (int i = 0; i < status.size(); i++) {
                sb2.append(":tStatus").append(i).append(',');
            }
            String inSq2 = sb2.substring(0, sb2.length() - 1) + ")";
            searchParam.addParam("taskStatus", inSq2);
            for (int i = 0; i < status.size(); i++) {
                searchParam.addFilter("tStatus" + i, status.get(i));
            }
            if(!StringUtil.isEmpty(userId)){
                searchParam.addFilter("userId", userId,true);
            }
            //do search
            QueryHelp.setMainSql("inspectionQuery", "systemPoolDropdown", searchParam);
            SearchResult<SuperPoolTaskQueryDto> searchResult = systemSearchAssignPoolService.getSystemPoolSecondByParam(searchParam);
            searchResult = inspectionService.getSecondSearchOtherData(searchResult, hcsaTaskAssignDto);
            jsonMap.put("ajaxResult", searchResult);
            jsonMap.put("result", "Success");
            assignMap = setTaskIdAndDataMap(assignMap, searchResult);
        } else {
            jsonMap.put("result", "Fail");
        }
        ParamUtil.setSessionAttr(request, "systemAssignMap", (Serializable) assignMap);
        //get other data
        return jsonMap;
    }

    @RequestMapping(value = "reassign.do", method = RequestMethod.POST)
    public @ResponseBody Map<String, Object> reassignTask(HttpServletRequest request) {
        Map<String, Object> jsonMap = IaisCommonUtils.genNewHashMap();
        //get session data
        String appGroupId = MaskUtil.unMaskValue("appGroupId", request.getParameter("groupId"));
        GroupRoleFieldDto groupRoleFieldDto = (GroupRoleFieldDto) ParamUtil.getSessionAttr(request, "groupRoleFieldDto");
        List<String> workGroupIds = (List<String>) ParamUtil.getSessionAttr(request, "workGroupIds");
        Map<String, SuperPoolTaskQueryDto> assignMap = (Map<String, SuperPoolTaskQueryDto>) ParamUtil.getSessionAttr(request, "assignMap");
        String userId = (String) ParamUtil.getSessionAttr(request, "memberId");
        String commonPoolStatus = (String) ParamUtil.getSessionAttr(request, "commonPoolStatus");
        HcsaTaskAssignDto hcsaTaskAssignDto = (HcsaTaskAssignDto)ParamUtil.getSessionAttr(request, "hcsaTaskAssignDto");
        SearchParam searchParamGroup = (SearchParam) ParamUtil.getSessionAttr(request, "supTaskSearchParam");
        String hci_address = (String)ParamUtil.getSessionAttr(request, "reAssignPoolHciAddress");
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
            //set filter common
            searchParam = setFilterByAppGrpParamAndNo(searchParamGroup, null, searchParam, hcsaTaskAssignDto, "T3.STATUS",
                    "T4.ID","appPremId_list", hci_address);

            //set filters
            if (!IaisCommonUtils.isEmpty(appCorrId_list)) {
                String appPremCorrId = SqlHelper.constructInCondition("T1.REF_NO", appCorrId_list.size());
                searchParam.addParam("appCorrId_list", appPremCorrId);
                for (int i = 0; i < appCorrId_list.size(); i++) {
                    searchParam.addFilter("T1.REF_NO" + i, appCorrId_list.get(i));
                }
            }

            StringBuilder sb2 = new StringBuilder("(");
            for (int i = 0; i < status.size(); i++) {
                sb2.append(":tStatus").append(i).append(',');
            }
            String inSq2 = sb2.substring(0, sb2.length() - 1) + ")";
            searchParam.addParam("taskStatus", inSq2);
            for (int i = 0; i < status.size(); i++) {
                searchParam.addFilter("tStatus" + i, status.get(i));
            }

            StringBuilder sb3 = new StringBuilder("(");
            for (int i = 0; i < workGroupIds.size(); i++) {
                sb3.append(":workId").append(i).append(',');
            }
            String inSq3 = sb3.substring(0, sb3.length() - 1) + ")";
            searchParam.addParam("workGroupId", inSq3);
            for (int i = 0; i < workGroupIds.size(); i++) {
                searchParam.addFilter("workId" + i, workGroupIds.get(i));
            }

            //user's pool or common pool
            if(!StringUtil.isEmpty(userId)){
                searchParam.addFilter("userId", userId,true);
            } else if(!StringUtil.isEmpty(commonPoolStatus)){
                searchParam.addFilter("commonPoolStatus", commonPoolStatus,true);
            }
            //do search
            QueryHelp.setMainSql("inspectionQuery", "supervisorPoolDropdown", searchParam);
            SearchResult<SuperPoolTaskQueryDto> searchResult = inspectionService.getSupPoolSecondByParam(searchParam);
            searchResult = inspectionService.getSecondSearchOtherData(searchResult, hcsaTaskAssignDto);
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