package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Shicheng
 * @date 2019/12/18 14:22
 **/
@Delegator("inspectionRectificationProDelegator")
@Slf4j
public class InspectionRectificationProDelegator {

    @Autowired
    private InspectionRectificationProService inspectionRectificationProService;

    @Autowired
    private InspectionRectificationProDelegator(InspectionRectificationProService inspectionRectificationProService){
        this.inspectionRectificationProService = inspectionRectificationProService;
    }

    /**
     * StartStep: inspectorProRectificationStart
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationStart start ...."));
        AuditTrailHelper.auditFunction("Inspection Rectification Process", "Inspector Processing Rectification");
    }

    /**
     * StartStep: inspectorProRectificationInit
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationInit start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request,"inspectionTaskPoolListDto", null);
    }

    /**
     * StartStep: inspectorProRectificationPre
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationPre start ...."));
    }

    /**
     * StartStep: inspectorProRectificationValid
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationValid(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationValid start ...."));
    }

    /**
     * StartStep: inspectorProRectificationReq
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationReq(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationReq start ...."));
    }

    /**
     * StartStep: inspectorProRectificationAcc
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationAcc(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationAcc start ...."));
    }

    /**
     * StartStep: inspectorProRectificationAccCond
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationAccCond(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationAccCond start ...."));
    }
}
