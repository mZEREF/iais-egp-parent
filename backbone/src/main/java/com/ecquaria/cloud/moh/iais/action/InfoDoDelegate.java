package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * InfoDoDelegate
 *
 * @author WANGYU
 * @date 2021/4/26
 */
@Slf4j
@Delegator("infoDo")
public class InfoDoDelegate {
    public void start(BaseProcessClass bpc) {
        log.info("----------InfoDoDelegate  start-----------");
        if(bpc.request != null){
            ParamUtil.setRequestAttr(bpc.request,"section", ParamUtil.getRequestString(bpc.request,"section"));
        }
    }
}
