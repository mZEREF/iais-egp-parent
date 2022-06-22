package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.dto.ComplianceDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * DataSubmissionDelegator
 *
 * @author suocheng
 * @date 10/20/2021
 */
@Delegator("dataSubmissionDelegator")
@Slf4j
public class DataSubmissionDelegator {
    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_DATA_SUBMISSION, AuditTrailConsts.MODULE_SELECT_DATA_SUBMISSION);
    }

    /**
     * StartStep: PrepareDataSubmissionSelect
     *
     * @param bpc
     * @throws
     */
    public void doPrepareDataSubmissionSelect(BaseProcessClass bpc) {


    }

    /**
     * StartStep: PrepareCompliance
     *
     * @param bpc
     * @throws
     */
    public void prepareCompliance(BaseProcessClass bpc) {
        bpc.request.setAttribute("title","New Data Submission");
        String crud_action_type_ds = bpc.request.getParameter(DataSubmissionConstant.CRUD_TYPE);
        if(StringUtil.isEmpty(crud_action_type_ds) || "back".equals(crud_action_type_ds)){
            crud_action_type_ds = (String)ParamUtil.getSessionAttr(bpc.request,"DsModleSelect");
        }
        ParamUtil.setSessionAttr(bpc.request,"DsModleSelect",crud_action_type_ds);
        ComplianceDto  complianceDto = new ComplianceDto();
        complianceDto.setSubmissionType(crud_action_type_ds);
        switch (crud_action_type_ds){
            case "AR" :
                complianceDto.setMins("30");
                complianceDto.setSubmissionTypeDisplay("Assisted Reproduction");
                break;
            case "DP" :
                complianceDto.setMins("5");
                complianceDto.setSubmissionTypeDisplay("Drug Practices");
                break;
            case "LDT" :
                complianceDto.setMins("5");
                complianceDto.setSubmissionTypeDisplay("Laboratory Developed Test");
                break;
            case "TP" :
                complianceDto.setMins("15");
                complianceDto.setSubmissionTypeDisplay("Termination of Pregnancy");
                break;
            case "VS" :
                complianceDto.setMins("10");
                complianceDto.setSubmissionTypeDisplay("Voluntary Sterilisation");
                break;
        }
        bpc.request.setAttribute("complianceDto",complianceDto);
    }
    /**
     * StartStep: PrepareDataSubmission
     *
     * @param bpc
     * @throws
     */
    public void doPrepareDataSubmission(BaseProcessClass bpc) {
        String crud_action_type_ds = bpc.request.getParameter(DataSubmissionConstant.CRUD_TYPE);
        bpc.request.setAttribute(DataSubmissionConstant.CRUD_ACTION_TYPE_DS,crud_action_type_ds);
    }
    /**
     * StartStep: PrepeareAR
     *
     * @param bpc
     * @throws
     */
    public void doPrepeareAR(BaseProcessClass bpc) {

    }
    /**
     * StartStep: PrepeareLDT
     *
     * @param bpc
     * @throws
     */
    public void doPrepeareLDT(BaseProcessClass bpc) {

    }
    /**
     * StartStep: PrepeareVS
     *
     * @param bpc
     * @throws
     */
    public void doPrepeareVS(BaseProcessClass bpc) {

    }
    /**
     * StartStep: PrepeareDP
     *
     * @param bpc
     * @throws
     */
    public void doPrepeareDP(BaseProcessClass bpc) {

    }
    /**
     * StartStep: PrepeareTP
     *
     * @param bpc
     * @throws
     */
    public void doPrepeareTP(BaseProcessClass bpc) {

    }
    /**
     * StartStep: Back
     *
     * @param bpc
     * @throws
     */
    public void back(BaseProcessClass bpc) {

    }

}
