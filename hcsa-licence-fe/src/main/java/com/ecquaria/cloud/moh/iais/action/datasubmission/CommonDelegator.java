package com.ecquaria.cloud.moh.iais.action.datasubmission;

import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * CommonDelegator
 *
 * @author suocheng
 * @date 10/20/2021
 */
@Slf4j
public abstract  class CommonDelegator {
    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
        start(bpc);
    }
    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public abstract void  start(BaseProcessClass bpc);

    /**
     * StartStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public void doPrepareSwitch(BaseProcessClass bpc) {
        prepareSwitch(bpc);
    }
    /**
     * StartStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public abstract void  prepareSwitch(BaseProcessClass bpc);

    /**
     * StartStep: Return
     *
     * @param bpc
     * @throws
     */
    public void doReturn(BaseProcessClass bpc) {
        returnStep(bpc);
    }
    /**
     * StartStep: Return
     *
     * @param bpc
     * @throws
     */
    public abstract void  returnStep(BaseProcessClass bpc);

    /**
     * StartStep: PreparePage
     *
     * @param bpc
     * @throws
     */
    public void doPreparePage(BaseProcessClass bpc) {
        preparePage(bpc);
    }
    /**
     * StartStep: PreparePage
     *
     * @param bpc
     * @throws
     */
    public abstract void  preparePage(BaseProcessClass bpc);

    /**
     * StartStep: PrepareConfim
     *
     * @param bpc
     * @throws
     */
    public void doPrepareConfim(BaseProcessClass bpc) {
        prepareConfim(bpc);
    }
    /**
     * StartStep: PrepareConfim
     *
     * @param bpc
     * @throws
     */
    public abstract void  prepareConfim(BaseProcessClass bpc);

    /**
     * StartStep: Draft
     *
     * @param bpc
     * @throws
     */
    public void doDraft(BaseProcessClass bpc) {
        String currentStage = (String)bpc.request.getAttribute("currentStage");
        bpc.request.setAttribute("crud_action_type",currentStage);
        draft(bpc);
    }
    /**
     * StartStep: Draft
     *
     * @param bpc
     * @throws
     */
    public abstract void  draft(BaseProcessClass bpc);

    /**
     * StartStep: Submission
     *
     * @param bpc
     * @throws
     */
    public void doSubmission(BaseProcessClass bpc) {
        submission(bpc);
    }
    /**
     * StartStep: Submission
     *
     * @param bpc
     * @throws
     */
    public abstract void  submission(BaseProcessClass bpc);

    /**
     * StartStep: PageAction
     *
     * @param bpc
     * @throws
     */
    public void doPageAction(BaseProcessClass bpc) {
        bpc.request.setAttribute("currentStage","page");
        String crud_action_type = (String)bpc.request.getAttribute("crud_type");
        bpc.request.setAttribute("crud_action_type",crud_action_type);
        pageAction(bpc);
    }
    /**
     * StartStep: PageAction
     *
     * @param bpc
     * @throws
     */
    public abstract void  pageAction(BaseProcessClass bpc);

    /**
     * StartStep: PageConfirmAction
     *
     * @param bpc
     * @throws
     */
    public void doPageConfirmAction(BaseProcessClass bpc) {
        bpc.request.setAttribute("currentStage","confirm");
        String crud_action_type = (String)bpc.request.getAttribute("crud_type");
        bpc.request.setAttribute("crud_action_type",crud_action_type);
        pageConfirmAction(bpc);
    }
    /**
     * StartStep: PageConfirmAction
     *
     * @param bpc
     * @throws
     */
    public abstract void  pageConfirmAction(BaseProcessClass bpc);

}
