package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Process: MohInspectionInboxSearch
 *
 * @author Shicheng
 * @date 2019/11/14 18:01
 **/
@Delegator("inspectionSearchDelegator")
@Slf4j
public class InspectionSearchDelegator {

    @Autowired
    private InspectionService inspectionService;

    @Autowired
    private InspectionSearchDelegator(InspectionService inspectionService){
        this.inspectionService = inspectionService;
    }

    /**
     * StartStep: inspectionSupSearchStart
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchStart start ...."));
        AuditTrailHelper.auditFunction("Inspection Sup Assign", "Sup Assign Task");
    }

    /**
     * StartStep: inspectionSupSearchInit
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchInit start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDto", null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", null);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", null);
        ParamUtil.setSessionAttr(bpc.request, "workGroupIds", null);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", null);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectorOption", null);
        ParamUtil.setSessionAttr(bpc.request, "commPools", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectorValue", null);
    }

    /**
     * StartStep: inspectionSupSearchPre
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchPre start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        SearchResult<InspectionSubPoolQueryDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, "supTaskSearchResult");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<String> workGroupIds = inspectionService.getWorkGroupIdsByLogin(loginContext);
        List<SelectOption> appTypeOption = inspectionService.getAppTypeOption();
        List<SelectOption> appStatusOption = inspectionService.getAppStatusOption();
        //get Inspector Option
        List<SelectOption> inspectorOption = inspectionService.getInspectorOptionByLogin(loginContext, workGroupIds);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", (Serializable) appTypeOption);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
        ParamUtil.setSessionAttr(bpc.request, "inspectorOption", (Serializable) inspectorOption);
        ParamUtil.setSessionAttr(bpc.request, "workGroupIds", (Serializable) workGroupIds);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "supTaskSearchParam");
        if(searchParam == null || isNew){
            searchParam = new SearchParam(InspectionSubPoolQueryDto.class.getName());
            searchParam.setPageSize(10);
            searchParam.setPageNo(1);
            searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
        }
        return searchParam;
    }

    /**
     * StartStep: inspectionSupSearchStartStep1
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchStartStep1(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchStartStep1 start ...."));

    }

    /**
     * StartStep: inspectionSupSearchDoSearch
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchDoSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchDoSearch start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        List<String> workGroupIds = (List<String>)ParamUtil.getSessionAttr(bpc.request, "workGroupIds");
        String application_no = ParamUtil.getRequestString(bpc.request, "application_no");
        String application_type = ParamUtil.getRequestString(bpc.request, "application_type");
        String application_status = ParamUtil.getRequestString(bpc.request, "application_status");
        String hci_code = ParamUtil.getRequestString(bpc.request, "hci_code");
        String hci_name = ParamUtil.getRequestString(bpc.request, "hci_name");
        String hci_address = ParamUtil.getRequestString(bpc.request, "hci_address");
        String inspectorValue = ParamUtil.getMaskedString(bpc.request, "inspector_name");

        List<TaskDto> commPools = getCommPoolByGroupWordId(workGroupIds);
        List<String> appCorrId_list = inspectionService.getApplicationNoListByPool(commPools);
        StringBuilder sb = new StringBuilder("(");
        for(int i = 0; i < appCorrId_list.size(); i++){
            sb.append(":appCorrId" + i).append(",");
        }
        String inSql = sb.substring(0, sb.length() - 1) + ")";
        searchParam.addParam("appCorrId_list", inSql);
        for(int i = 0; i < appCorrId_list.size(); i++){
            searchParam.addFilter("appCorrId" + i, appCorrId_list.get(i));
        }
        if(!StringUtil.isEmpty(application_no)){
            searchParam.addFilter("application_no", application_no,true);
        }
        String[] appCorIdStrs;
        if(!(StringUtil.isEmpty(inspectorValue))) {
            appCorIdStrs = inspectorValue.split(",");
            String appNoStr = SqlHelper.constructInCondition("T5.ID", appCorIdStrs.length);
            searchParam.addParam("appCorId_list", appNoStr);
            for (int i = 0; i < appCorIdStrs.length; i++) {
                searchParam.addFilter("T5.ID" + i, appCorIdStrs[i]);
            }
        }
        if(!StringUtil.isEmpty(application_type)){
            searchParam.addFilter("application_type", application_type,true);
        }
        if(!StringUtil.isEmpty(application_status)){
            searchParam.addFilter("application_status", application_status,true);
        }
        if(!StringUtil.isEmpty(hci_code)){
            searchParam.addFilter("hci_code", hci_code,true);
        }
        if(!StringUtil.isEmpty(hci_name)){
            searchParam.addFilter("hci_name", hci_name,true);
        }
        if(!StringUtil.isEmpty(hci_address)){
            searchParam.addFilter("blk_no", hci_address,true);
            searchParam.addFilter("floor_no", hci_address,true);
            searchParam.addFilter("unit_no", hci_address,true);
            searchParam.addFilter("street_name", hci_address,true);
            searchParam.addFilter("building_name", hci_address,true);
            searchParam.addFilter("postal_code", hci_address,true);
        }
        ParamUtil.setSessionAttr(bpc.request, "commPools", (Serializable) commPools);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "inspectorValue", inspectorValue);
    }

    private List<TaskDto> getCommPoolByGroupWordId(List<String> workGroupIds) {
        List<TaskDto> taskDtoList = new ArrayList<>();
        if(IaisCommonUtils.isEmpty(workGroupIds)){
            return null;
        }
        for(String workGrpId:workGroupIds){
            for(TaskDto tDto:inspectionService.getSupervisorPoolByGroupWordId(workGrpId)){
                taskDtoList.add(tDto);
            }
        }
        return taskDtoList;
    }

    /**
     * StartStep: inspectionSupSearchSort
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchSort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchSort start ...."));
    }

    /**
     * StartStep: inspectionSupSearchPage
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchPage start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * StartStep: inspectionSupSearchQuery1
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchQuery1(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchQuery1 start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        List<TaskDto> commPools =(List<TaskDto>)ParamUtil.getSessionAttr(bpc.request, "commPools");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        QueryHelp.setMainSql("inspectionQuery", "assignInspectorSupper",searchParam);
        SearchResult<InspectionSubPoolQueryDto> searchResult = inspectionService.getSupPoolByParam(searchParam);
        SearchResult<InspectionTaskPoolListDto> searchResult2 = inspectionService.getOtherDataForSr(searchResult, commPools, loginContext);
        if(!(loginContext.getCurRoleId().equals(RoleConsts.USER_ROLE_INSPECTION_LEAD))){
            searchParam = null;
            searchResult2 = null;
        }
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult2);
    }

    /**
     * StartStep: inspectionSupSearchAssign
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchAssign(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchAssign start ...."));
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        SearchResult<InspectionTaskPoolListDto> searchResult = (SearchResult<InspectionTaskPoolListDto>)ParamUtil.getSessionAttr(bpc.request, "supTaskSearchResult");
        String taskId = ParamUtil.getMaskedString(bpc.request, "taskId");
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        if(!(StringUtil.isEmpty(taskId))){
            inspectionTaskPoolListDto = new InspectionTaskPoolListDto();
            for(InspectionTaskPoolListDto iDto:searchResult.getRows()){
                if(taskId.equals(iDto.getTaskId())){
                    inspectionTaskPoolListDto = iDto;
                }
            }
            if(StringUtil.isEmpty(inspectionTaskPoolListDto.getInspector())){
                if(loginContext.getRoleIds().contains(RoleConsts.USER_ROLE_INSPECTIOR)){
                    inspectionTaskPoolListDto.setInspectorFlag(AppConsts.TRUE);
                } else {
                    inspectionTaskPoolListDto.setInspectorFlag(AppConsts.FALSE);
                }
            } else {
                inspectionTaskPoolListDto.setInspectorFlag(AppConsts.TRUE);
            }
            //get inspector Option
            inspectionTaskPoolListDto = inspectionService.inputInspectorOption(inspectionTaskPoolListDto, loginContext);
        }

        ParamUtil.setSessionAttr(bpc.request, "inspectionTaskPoolListDto", inspectionTaskPoolListDto);
        ParamUtil.setSessionAttr(bpc.request, "supTaskSearchResult", searchResult);
    }

    /**
     * StartStep: inspectionSupSearchValidate
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchValidate(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchValidate start ...."));
        InspectionTaskPoolListDto inspectionTaskPoolListDto = getValueFromPage(bpc);
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        if(!(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue))){
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionTaskPoolListDto,"create");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
            }
        }else{
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDto", inspectionTaskPoolListDto);
    }

    public InspectionTaskPoolListDto getValueFromPage(BaseProcessClass bpc) {
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        String[] nameValue = ParamUtil.getStrings(bpc.request,"inspectorCheck");
        if(nameValue == null || nameValue.length < 0) {
            inspectionTaskPoolListDto.setInspectorCheck(null);
        } else {
            List<SelectOption> inspectorCheckList = inspectionService.getCheckInspector(nameValue, inspectionTaskPoolListDto);
            inspectionTaskPoolListDto.setInspectorCheck(inspectorCheckList);
        }
        return inspectionTaskPoolListDto;
    }

    /**
     * StartStep: inspectionSupSearchQuery2
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchQuery2(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchQuery2 start ...."));
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDto", inspectionTaskPoolListDto);
    }

    /**
     * StartStep: inspectionSupSearchConfirm
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchConfirm(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchConfirm start ...."));
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDto", inspectionTaskPoolListDto);
    }

    /**
     * StartStep: InspectionInboxSearchQuery
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchSuccess start ...."));
        InspectionTaskPoolListDto inspectionTaskPoolListDto = (InspectionTaskPoolListDto)ParamUtil.getSessionAttr(bpc.request, "inspectionTaskPoolListDto");
        List<TaskDto> commPools =(List<TaskDto>)ParamUtil.getSessionAttr(bpc.request, "commPools");
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        inspectionService.routingTaskByPool(inspectionTaskPoolListDto, commPools, internalRemarks);
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDto", inspectionTaskPoolListDto);
    }
}
