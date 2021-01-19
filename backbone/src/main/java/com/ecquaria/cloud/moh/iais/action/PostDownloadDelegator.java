package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @Author: yichen
 * @Date:2020/6/5
 **/

@Slf4j
@Delegator("postDownloadDelegator")
public class PostDownloadDelegator {

    public void postDownload(BaseProcessClass bpc) {
        log.info("post download file start ###########");
        String fileName = (String) bpc.request.getAttribute("processDownloadFileName");
        log.info(StringUtil.changeForLog("current download file name " + fileName));
    }
}
