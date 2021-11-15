package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.List;

/**
 * @author Shicheng
 * @date 2021/11/15 10:59
 **/
@Delegator("iuiCycleStageDelegator")
@Slf4j
public class IuiCycleStageDelegator extends CommonDelegator {

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    @Override
    public void start(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, "sourceOfSemenOption", null);
        ParamUtil.setSessionAttr(bpc.request, "curMarrChildNumOption", null);
        ParamUtil.setSessionAttr(bpc.request, "prevMarrChildNumOption", null);
    }

    @Override
    public void prepareSwitch(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Cycle Stages</strong>");
        List<SelectOption> sourceOfSemenOption = arDataSubmissionService.getSourceOfSemenOption();
        List<SelectOption> curMarrChildNumOption = arDataSubmissionService.getChildNumOption();
        List<SelectOption> prevMarrChildNumOption = arDataSubmissionService.getChildNumOption();
        ParamUtil.setSessionAttr(bpc.request, "sourceOfSemenOption", (Serializable) sourceOfSemenOption);
        ParamUtil.setSessionAttr(bpc.request, "curMarrChildNumOption", (Serializable) curMarrChildNumOption);
        ParamUtil.setSessionAttr(bpc.request, "prevMarrChildNumOption", (Serializable) prevMarrChildNumOption);
        String actionType = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.info(StringUtil.changeForLog("----- Action Type: " + actionType + " -----"));
        if (StringUtil.isEmpty(actionType)) {
            actionType = ACTION_TYPE_PAGE;
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        }
    }

    @Override
    public void pageAction(BaseProcessClass bpc) {

    }
}
