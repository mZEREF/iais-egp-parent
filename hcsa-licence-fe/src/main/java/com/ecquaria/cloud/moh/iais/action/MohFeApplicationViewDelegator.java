package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

@Delegator("mohFeApplicationView")
@Slf4j
public class MohFeApplicationViewDelegator {

   // private LoginContext loginContext = null;

    public void feApplicationViewStart(BaseProcessClass bpc){
//        loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String appNo = ParamUtil.getMaskedString(bpc.request, "appNo");
    }

    public void feApplicationViewPrepareData(BaseProcessClass bpc) {
//        NewApplicationDelegator newApplicationDelegator = new NewApplicationDelegator();
        String page = ParamUtil.getRequestString(bpc.request,"app_type");
        if ("ar".equals(page)){
            ParamUtil.setRequestAttr(bpc.request,"app_action_type","toAppeal");
        }else{
            ParamUtil.setRequestAttr(bpc.request,"app_action_type","");
        }
    }

    public void toApplicationStep(BaseProcessClass bpc) throws Exception {
        NewApplicationDelegator newApplicationDelegator = SpringContextHelper.getContext().getBean(NewApplicationDelegator.class);
        newApplicationDelegator.inboxToPreview(bpc);
        ParamUtil.setSessionAttr(bpc.request, "isPopApplicationView", Boolean.TRUE);
        ParamUtil.setRequestAttr(bpc.request, "cessationForm", "Application Details");
    }

    public void toAppealStep(BaseProcessClass bpc){
        AppealDelegator appealDelegator = SpringContextHelper.getContext().getBean(AppealDelegator.class);
        appealDelegator.inbox(bpc);
        ParamUtil.setSessionAttr(bpc.request, "isPopApplicationView", Boolean.TRUE);
    }

}
