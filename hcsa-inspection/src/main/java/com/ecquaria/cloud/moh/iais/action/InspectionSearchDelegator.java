package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

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
     * StartStep: InspectionPreInspectorStart
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the InspectionPreInspectorStart start ...."));
    }

    /**
     * StartStep: InspectionPreInspectorInit
     *
     * @param bpc
     * @throws
     */
    public void inspectionPreInspectorInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the InspectionPreInspectorInit start ...."));
    }

    /**
     * StartStep: InspectionInboxSearchPre
     *
     * @param bpc
     * @throws
     */
    public void inspectionInboxSearchPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the InspectionInboxSearchPre start ...."));
    }

    /**
     * StartStep: InspectionInboxSearchStep1
     *
     * @param bpc
     * @throws
     */
    public void inspectionInboxSearchStep1(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the InspectionInboxSearchStep1 start ...."));

    }

    /**
     * StartStep: InspectionInboxSearchDoSearch
     *
     * @param bpc
     * @throws
     */
    public void inspectionInboxSearchDoSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the InspectionInboxSearchDoSearch start ...."));
    }

    /**
     * StartStep: InspectionInboxSearchPageStep
     *
     * @param bpc
     * @throws
     */
    public void inspectionInboxSearchPageStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the InspectionInboxSearchPageStep start ...."));

    }

    /**
     * StartStep: InspectionInboxSearchSort
     *
     * @param bpc
     * @throws
     */
    public void inspectionInboxSearchSort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the InspectionInboxSearchSort start ...."));

    }

    /**
     * StartStep: InspectionInboxSearchQuery
     *
     * @param bpc
     * @throws
     */
    public void inspectionInboxSearchQuery(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the InspectionInboxSearchQuery start ...."));

    }
}
