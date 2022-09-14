package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.action.ApplicationDelegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import javax.servlet.http.HttpServletRequest;

/**
 * InspectionHelper
 *
 * @author Jinhua
 * @date 2022/9/7 16:20
 */
public final class InspectionHelper {

    public static void checkForEditingApplication(HttpServletRequest request) {
        // check from editing application
        String appError = ParamUtil.getString(request, HcsaAppConst.ERROR_APP);
        if (StringUtil.isNotEmpty(appError)) {
            ParamUtil.setRequestAttr(request, HcsaAppConst.ERROR_APP, StringUtil.clarify(appError));
        }
        // show edit application
        boolean showBtn = true;
        ParamUtil.setRequestAttr(request, HcsaAppConst.SHOW_EDIT_BTN, showBtn
                && SpringHelper.getBean(ApplicationDelegator.class).checkData(HcsaAppConst.CHECKED_BTN_SHOW, request));
    }

    private InspectionHelper() {
    }
}
