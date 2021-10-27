package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

@Delegator("fertilisationDelegator")
@Slf4j
public class FertilisationDelegator extends CommonDelegator{

    public static final String SOURCE_OF_SEMENS = "sourceOfSemens";
    public static final String AR_TECHNIQUES_USEDS = "arTechniquesUseds";
    @Override
    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,SOURCE_OF_SEMENS, (Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.SOURCE_OF_SEMEN));

        ParamUtil.setSessionAttr(request,AR_TECHNIQUES_USEDS, (Serializable) MasterCodeUtil.retrieveByCategory(MasterCodeUtil.AR_TECHNIQUES_USED));
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {

    }

    @Override
    public void returnStep(BaseProcessClass bpc) {

    }

    @Override
    public void preparePage(BaseProcessClass bpc) {

    }

    @Override
    public void prepareConfim(BaseProcessClass bpc) {

    }

    @Override
    public void draft(BaseProcessClass bpc) {

    }

    @Override
    public void submission(BaseProcessClass bpc) {

    }

    @Override
    public void pageAction(BaseProcessClass bpc) {

    }

    @Override
    public void pageConfirmAction(BaseProcessClass bpc) {

    }
}
