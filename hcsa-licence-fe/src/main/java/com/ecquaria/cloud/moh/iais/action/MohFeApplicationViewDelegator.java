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

    private LoginContext loginContext = null;

    public void feApplicationViewStart(BaseProcessClass bpc){
//        loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request,AppConsts.SESSION_ATTR_LOGIN_USER);
        String appNo = ParamUtil.getMaskedString(bpc.request, "appNo");
    }

    public void feApplicationViewPrepareData(BaseProcessClass bpc) throws Exception {
//        NewApplicationDelegator newApplicationDelegator = new NewApplicationDelegator();
        NewApplicationDelegator newApplicationDelegator = SpringContextHelper.getContext().getBean(NewApplicationDelegator.class);
        newApplicationDelegator.inboxToPreview(bpc);
    }
}
