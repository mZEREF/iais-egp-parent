package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Process: MohSystemPoolAssign
 *
 * @author Shicheng
 * @date 2020/8/26 14:17
 **/
@Delegator("systemSearchAssignPool")
@Slf4j
public class SystemSearchAssignPoolDelegator {

    @Autowired
    private InspectionPreTaskService inspectionPreTaskService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AdhocChecklistService adhocChecklistService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private SystemSearchAssignPoolDelegator(InspectionPreTaskService inspectionPreTaskService, TaskService taskService, AdhocChecklistService adhocChecklistService,
                                            ApplicationViewService applicationViewService){
        this.inspectionPreTaskService = inspectionPreTaskService;
        this.taskService = taskService;
        this.adhocChecklistService = adhocChecklistService;
        this.applicationViewService = applicationViewService;
    }

    /**
     * StartStep: systemPoolAssignStart
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignStart start ...."));
    }

    /**
     * StartStep: systemPoolAssignInit
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignInit start ...."));
    }

    /**
     * StartStep: systemPoolAssignPre
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignPre start ...."));
    }

    /**
     * StartStep: systemPoolAssignStep
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignStep start ...."));
    }

    /**
     * StartStep: systemPoolAssignDoSearch
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignDoSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignDoSearch start ...."));
    }

    /**
     * StartStep: systemPoolAssignSort
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignSort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignSort start ...."));
    }

    /**
     * StartStep: systemPoolAssignPage
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignPage start ...."));
    }

    /**
     * StartStep: systemPoolAssignQuery
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignQuery(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignQuery start ...."));
    }

    /**
     * StartStep: systemPoolAssignAssign
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignAssign(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignAssign start ...."));
    }

    /**
     * StartStep: systemPoolAssignVali
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignVali start ...."));
    }

    /**
     * StartStep: systemPoolAssignConfirm
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignConfirm(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignConfirm start ...."));
    }

    /**
     * StartStep: systemPoolAssignSuccess
     *
     * @param bpc
     * @throws
     */
    public void systemPoolAssignSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the systemPoolAssignSuccess start ...."));
    }
}
