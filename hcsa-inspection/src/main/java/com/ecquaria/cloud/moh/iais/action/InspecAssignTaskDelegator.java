package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.List;

/**
 * @Process: MohInspectionAllotTaskInspector
 *
 * @author Shicheng
 * @date 2019/11/22 10:16
 **/
@Delegator("inspecAssignTaskDelegator")
@Slf4j
public class InspecAssignTaskDelegator {
    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;

    @Autowired
    private InspecAssignTaskDelegator(InspectionAssignTaskService inspectionAssignTaskService){
        this.inspectionAssignTaskService = inspectionAssignTaskService;
    }

    /**
     * StartStep: inspectionAllotTaskInspectorStart
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorStart start ...."));
    }

    /**
     * StartStep: inspectionAllotTaskInspectorInit
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorInit start ...."));
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDtoList", null);
        ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssQueryDto", null);
    }

    /**
     * StartStep: inspectionAllotTaskInspectorPre
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorPre start ...."));
        List<TaskDto> commPools = inspectionAssignTaskService.getCommPoolByGroupWordId("BF3B0634-F80C-EA11-BE7D-000C29F371DC");
        List<InspectionTaskPoolListDto> inspectionTaskPoolListDtoList = inspectionAssignTaskService.getPoolListByTaskDto(commPools);
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDtoList", (Serializable) inspectionTaskPoolListDtoList);
    }

    /**
     * StartStep: inspectionAllotTaskInspectorStep1
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorStep1(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorStep1 start ...."));
    }

    /**
     * StartStep: inspectionAllotTaskInspectorAssign
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorAssign(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorAssign start ...."));
        String applicationNo = ParamUtil.getMaskedString(bpc.request,"crud_action_value");
        InspecTaskCreAndAssQueryDto inspecTaskCreAndAssQueryDto = inspectionAssignTaskService.getInspecTaskCreAndAssQueryDto(applicationNo);
        ParamUtil.setSessionAttr(bpc.request,"inspecTaskCreAndAssQueryDto", inspecTaskCreAndAssQueryDto);
    }

    /**
     * StartStep: inspectionAllotTaskInspectorAction
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorAction(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorAction start ...."));

    }

    /**
     * StartStep: inspectionAllotTaskInspectorQuery
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorQuery(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorQuery start ...."));
    }

    /**
     * StartStep: inspectionAllotTaskInspectorSuccess
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorSuccess start ...."));
    }

    /**
     * StartStep: inspectionAllotTaskInspectorConfirm
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorConfirm(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorConfirm start ...."));

    }

    /**
     * StartStep: inspectionAllotTaskInspectorSearch
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorSearch start ...."));

    }

    /**
     * StartStep: inspectionAllotTaskInspectorSort
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorSort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorSort start ...."));

    }

    /**
     * StartStep: inspectionAllotTaskInspectorPage
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorPage start ...."));

    }

    /**
     * StartStep: inspectionAllotTaskInspectorQuery2
     *
     * @param bpc
     * @throws
     */
    public void inspectionAllotTaskInspectorQuery2(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectionAllotTaskInspectorQuery2 start ...."));

    }
}
