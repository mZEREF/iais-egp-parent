package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.HashMap;
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
        ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssDto", null);
        ParamUtil.setSessionAttr(bpc.request, "SupTaskSearchParam", null);
        ParamUtil.setSessionAttr(bpc.request, "SupTaskSearchResult", null);
    }

    /**
     * StartStep: inspectionSupSearchPre
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchPre start ...."));
        SearchParam searchParam = (SearchParam)ParamUtil.getSessionAttr(bpc.request, "SupTaskSearchParam");
        SearchResult<InspecTaskCreAndAssDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, "SupTaskSearchResult");
        if(searchParam == null){
            searchParam = new SearchParam(InspectionCommonPoolQueryDto.class.getName());
            searchParam.setPageSize(10);
            searchParam.setPageNo(1);
            searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
        }
        List<SelectOption> appTypeOption = inspectionService.getAppTypeOption();
        List<SelectOption> appStatusOption = inspectionService.getAppStatusOption();

        ParamUtil.setSessionAttr(bpc.request, "SupTaskSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "SupTaskSearchResult", searchResult);
        ParamUtil.setSessionAttr(bpc.request, "appTypeOption", (Serializable) appTypeOption);
        ParamUtil.setSessionAttr(bpc.request, "appStatusOption", (Serializable) appStatusOption);
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
        SearchParam searchParam = (SearchParam)ParamUtil.getSessionAttr(bpc.request, "SupTaskSearchParam");
        String application_no = ParamUtil.getRequestString(bpc.request, "application_no");
        String application_type = ParamUtil.getRequestString(bpc.request, "application_type");
        String application_status = ParamUtil.getRequestString(bpc.request, "application_status");
        String hci_code = ParamUtil.getRequestString(bpc.request, "hci_code");
        String hci_name = ParamUtil.getRequestString(bpc.request, "hci_name");
        String hci_address = ParamUtil.getRequestString(bpc.request, "hci_address");
        String inspectorValue = ParamUtil.getRequestString(bpc.request, "inspector");
        String[] appNoStrs=inspectorValue.split(",");
        List<TaskDto> commPools = inspectionService.getCommPoolByGroupWordId("A03EDD16-F90C-EA11-BE7D-000C29F371DC");
        setMapTaskId(bpc, commPools);
        List<InspectionTaskPoolListDto> inspectionTaskPoolListDtoList = inspectionService.getPoolListByTaskDto(commPools);
        String[] applicationNo_list = inspectionService.getApplicationNoListByPool(inspectionTaskPoolListDtoList);
        if(applicationNo_list == null || applicationNo_list.length == 0){
            applicationNo_list = new String[]{SystemParameterConstants.PARAM_FALSE};
        }
        String applicationStr = SqlHelper.constructInCondition("T1.APPLICATION_NO",applicationNo_list.length);
        log.debug(StringUtil.changeForLog("applicationStr: ...."+applicationStr));
        searchParam.addParam("applicationNo_list",applicationStr);
        for (int i = 0 ; i<applicationNo_list.length; i++ ) {
            searchParam.addFilter("T1.APPLICATION_NO"+i,applicationNo_list[i]);
        }
        if(!StringUtil.isEmpty(application_no)){
            searchParam.addFilter("application_no",application_no,true);
        }
        if(appNoStrs != null && appNoStrs.length > 0) {
            String appNoStr = SqlHelper.constructInCondition("T5.APPLICATION_NO",appNoStrs.length);
            searchParam.addParam("appNo_list",appNoStr);
            for (int i = 0; i < appNoStrs.length; i++) {
                searchParam.addFilter("T5.APPLICATION_NO"+i,appNoStrs[i]);
            }
        }
        if(!StringUtil.isEmpty(application_type)){
            searchParam.addFilter("application_type",application_type,true);
        }
        if(!StringUtil.isEmpty(application_status)){
            searchParam.addFilter("application_status",application_status,true);
        }
        if(!StringUtil.isEmpty(hci_code)){
            searchParam.addFilter("hci_code",hci_code,true);
        }
        if(!StringUtil.isEmpty(hci_name)){
            searchParam.addFilter("hci_name",hci_name,true);
        }
        if(!StringUtil.isEmpty(hci_address)){
            searchParam.addFilter("blk_no",hci_address,true);
            searchParam.addFilter("floor_no",hci_address,true);
            searchParam.addFilter("unit_no",hci_address,true);
            searchParam.addFilter("street_name",hci_address,true);
            searchParam.addFilter("building_name",hci_address,true);
            searchParam.addFilter("postal_code",hci_address,true);
        }
        ParamUtil.setSessionAttr(bpc.request, "commPools", (Serializable) commPools);
        ParamUtil.setSessionAttr(bpc.request, "SupTaskSearchParam", searchParam);
    }

    private void setMapTaskId(BaseProcessClass bpc, List<TaskDto> commPools) {
        Map<String, String> appNoTaskIdMap = new HashMap<>();
        if(commPools != null || commPools.size() > 0){
            for(TaskDto td:commPools){
                appNoTaskIdMap.put(td.getRefNo(), td.getId());
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "appNoTaskIdMap", (Serializable) appNoTaskIdMap);
    }

    /**
     * StartStep: inspectionSupSearchSort
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchSort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchSort start ...."));
        SearchParam searchParam = (SearchParam)ParamUtil.getSessionAttr(bpc.request, "SupTaskSearchParam");
        CrudHelper.doSorting(searchParam,  bpc.request);
    }

    /**
     * StartStep: inspectionSupSearchPage
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchPage start ...."));
        SearchParam searchParam = (SearchParam)ParamUtil.getSessionAttr(bpc.request, "SupTaskSearchParam");
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
        SearchParam searchParam = (SearchParam)ParamUtil.getSessionAttr(bpc.request, "SupTaskSearchParam");
        QueryHelp.setMainSql("inspectionQuery", "assignInspectorSupper",searchParam);
        //SResult
        ParamUtil.setSessionAttr(bpc.request, "SupTaskSearchParam", searchParam);
    }

    /**
     * StartStep: inspectionSupSearchAssign
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchAssign(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchAssign start ...."));

    }

    /**
     * StartStep: inspectionSupSearchValidate
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchValidate(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchValidate start ...."));

    }

    /**
     * StartStep: inspectionSupSearchQuery2
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchQuery2(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchQuery2 start ...."));

    }

    /**
     * StartStep: inspectionSupSearchConfirm
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchConfirm(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchConfirm start ...."));

    }

    /**
     * StartStep: InspectionInboxSearchQuery
     *
     * @param bpc
     * @throws
     */
    public void inspectionSupSearchSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionSupSearchSuccess start ...."));

    }
}
