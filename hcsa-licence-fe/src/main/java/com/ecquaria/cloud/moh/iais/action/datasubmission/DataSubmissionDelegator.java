package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
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


}
