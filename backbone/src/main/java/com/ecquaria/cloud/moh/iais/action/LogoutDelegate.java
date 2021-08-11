package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * LogoutDelegate
 *
 * @author Jinhua
 * @date 2020/3/11 9:55
 */
@Slf4j
@Delegator("iaisLogoutDelegate")
public class LogoutDelegate {

    public void logout(BaseProcessClass bpc) {
        IaisEGPHelper.doLogout(bpc.request);
    }
}
