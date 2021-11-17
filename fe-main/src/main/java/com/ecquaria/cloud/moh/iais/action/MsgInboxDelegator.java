package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * FeAdminManageDelegate
 *
 * @author wangyu
 * @date 2021/11/18
 */
@Delegator("mohMsgInboxDelegator")
@Slf4j
public class MsgInboxDelegator {

    /**
     * StartStep: start
     *
     * @param bpc
     * @throws
     */
    public void start(BaseProcessClass bpc){
       log.info(StringUtil.changeForLog("----------start url from "+ bpc.request.getRequestURI()+"---------------------"));
    }

    /**
     * AutoStep: prepare
     *
     * @param bpc
     * @throws
     */
    public void prepare(BaseProcessClass bpc) {
        log.info("-----------mohMsgInboxDelegator prepare----------");
        IaisEGPHelper.redirectUrl(bpc.response,bpc.request, "MohInternetInbox", InboxConst.URL_MAIN_WEB_MODULE,null);
    }



}
