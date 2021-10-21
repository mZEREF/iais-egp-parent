package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * ARDataSubmissionDelegator
 *
 * @author suocheng
 * @date 10/20/2021
 */
@Delegator("arDataSubmissionDelegator")
@Slf4j
public class ArDataSubmissionDelegator {
    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {

    }
    /**
     * StartStep: PrepareARSubmission
     *
     * @param bpc
     * @throws
     */
    public void doPrepareARSubmission(BaseProcessClass bpc) {
        String crud_action_type_ds = bpc.request.getParameter(DataSubmissionConstant.CRUD_TYPE);
        bpc.request.setAttribute(DataSubmissionConstant.CRUD_ACTION_TYPE_AR,crud_action_type_ds);
    }
    /**
     * StartStep: PreparePIM
     *
     * @param bpc
     * @throws
     */
    public void doPreparePIM(BaseProcessClass bpc) {

    }
    /**
     * StartStep: PreparePIF
     *
     * @param bpc
     * @throws
     */
    public void doPreparePIF(BaseProcessClass bpc) {

    }
    /**
     * StartStep: PrepareCSM
     *
     * @param bpc
     * @throws
     */
    public void doPrepareCSM(BaseProcessClass bpc) {

    }
    /**
     * StartStep: PrepareCSF
     *
     * @param bpc
     * @throws
     */
    public void doPrepareCSF(BaseProcessClass bpc) {

    }
    /**
     * StartStep: PrepareDS
     *
     * @param bpc
     * @throws
     */
    public void doPrepareDS(BaseProcessClass bpc) {

    }
    /**
     * StartStep: Back
     *
     * @param bpc
     * @throws
     */
    public void doBack(BaseProcessClass bpc) {

    }


}
