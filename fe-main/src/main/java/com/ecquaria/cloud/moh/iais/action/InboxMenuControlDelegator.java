package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/****
 *
 *   @date 1/4/2020
 *   @author zixian
 */
@Slf4j
@Delegator("inboxMenuControlDelegator")
public class InboxMenuControlDelegator {


    /**
     *
     * @param bpc
     * @Decription doStart
     */
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doStart start ...."));



        log.debug(StringUtil.changeForLog("the do doStart end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription prepareJump
     */
    public void prepareJump(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareJump start ...."));
        String crudActionTypeMenu  = (String) ParamUtil.getRequestAttr(bpc.request, RfcConst.CRUD_ACTION_TYPE_MENU);


        log.debug(StringUtil.changeForLog("the do prepareJump end ...."));
    }


    /**
     *
     * @param bpc
     * @Decription doJump
     */
    public void doJump(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doJump start ...."));



        log.debug(StringUtil.changeForLog("the do doJump end ...."));
    }


}
