package com.ecquaria.cloud.moh.iais.action.ar;

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
    public abstract void  prepareConfim(BaseProcessClass bpc);

    /**
     * StartStep: Draft
     *
     * @param bpc
     * @throws
     */
    public void doDraft(BaseProcessClass bpc) {
        draft(bpc);
    }
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
    public abstract void  submission(BaseProcessClass bpc);

    /**
     * StartStep: PageAction
     *
     * @param bpc
     * @throws
     */
    public void doPageAction(BaseProcessClass bpc) {
        pageAction(bpc);
    }
    public abstract void  pageAction(BaseProcessClass bpc);

    /**
     * StartStep: PageConfirmAction
     *
     * @param bpc
     * @throws
     */
    public void doPageConfirmAction(BaseProcessClass bpc) {
        pageConfirmAction(bpc);
    }
    public abstract void  pageConfirmAction(BaseProcessClass bpc);

}
