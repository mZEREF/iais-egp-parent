package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author weilu
 * @date 2019/12/24 15:15
 */
@Delegator(value = "createUser")
@Slf4j
public class MohCreateUserDelegator {

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
        AuditTrailHelper.auditFunction("Create User", "Create User");
    }

    public void prepareData (BaseProcessClass bpc){
        String userDomain = ParamUtil.getRequestString(bpc.request, "userDomain");
        String userId = ParamUtil.getRequestString(bpc.request, "userId");
        String userName = ParamUtil.getRequestString(bpc.request, "userName");
        ParamUtil.setRequestAttr(bpc.request,"userDomain",userDomain);
        ParamUtil.setRequestAttr(bpc.request,"userId",userId);
        ParamUtil.setRequestAttr(bpc.request,"userName",userName);
    }
    public void preapreSwitch(BaseProcessClass bpc){

    }
    public void preapreCreate(BaseProcessClass bpc){
        String userDomain = ParamUtil.getRequestString(bpc.request, "userDomain");
        String userId = ParamUtil.getRequestString(bpc.request, "userId");
        String userName = ParamUtil.getRequestString(bpc.request, "userName");
        ParamUtil.setRequestAttr(bpc.request,"userDomain",userDomain);
        ParamUtil.setRequestAttr(bpc.request,"userId",userId);
        ParamUtil.setRequestAttr(bpc.request,"userName",userName);
    }
    public void doCreate (BaseProcessClass bpc){
        String userDomain = ParamUtil.getRequestString(bpc.request, "userDomain");
        String userId = ParamUtil.getRequestString(bpc.request, "userId");
        String userName = ParamUtil.getRequestString(bpc.request, "userName");
    }

    public void prepareEdit(BaseProcessClass bpc){

    }

    public void doEdit(BaseProcessClass bpc){

    }


    public void doDelete(BaseProcessClass bpc){

    }



    public void doSearch(BaseProcessClass bpc){

    }

    public void doSorting(BaseProcessClass bpc){

    }


}
