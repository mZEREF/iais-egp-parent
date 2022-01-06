package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.List;

/**
 * ArWithdrawalDelegator
 *
 * @author junyu
 * @date 2022/1/5
 */
@Slf4j
@Delegator("arWithdrawalDelegator")
public class ArWithdrawalDelegator {


    public void start(BaseProcessClass bpc)  {
        List<String> submissionNos = (List<String>) ParamUtil.getSessionAttr(bpc.request,"submissionWithdrawalNos");
        ParamUtil.setSessionAttr(bpc.request, "addWithdrawnDtoList", (Serializable) submissionNos);
        ParamUtil.setSessionAttr(bpc.request,"submissionWithdrawalNos",null);
        ParamUtil.setSessionAttr(bpc.request, "isDoView", 'N');

    }

    public void prepareDate(BaseProcessClass bpc)  {

    }

    public void withdrawalStep(BaseProcessClass bpc)  {

    }

    public void saveDate(BaseProcessClass bpc)  {

    }

    public void printStep(BaseProcessClass bpc)  {

    }
}
