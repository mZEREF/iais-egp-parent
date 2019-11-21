package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.InspectionServie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * Process: MohInspectionAllotTaskInspector
 *
 * @author Shicheng
 * @date 2019/11/14 18:01
 **/
@Delegator("inspectionDelegator")
@Slf4j
public class InspectionDelegator {

    @Autowired
    private InspectionServie inspectionServie;

    @Autowired
    private InspectionDelegator(InspectionServie inspectionServie){
        this.inspectionServie = inspectionServie;
    }

    /**
     * StartStep: InspectionPreInspectorStart
     *
     * @param bpc
     * @throws
     */
    public void startStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the startStep start ...."));
    }

    /**
     * StartStep: InspectionPreInspectorInit
     *
     * @param bpc
     * @throws
     */
    public void initStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the initStep start ...."));
    }

    /**
     * StartStep: InspectionPreInspectorInit
     *
     * @param bpc
     * @throws
     */
    public void perpareStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the initStep start ...."));
    }
}
