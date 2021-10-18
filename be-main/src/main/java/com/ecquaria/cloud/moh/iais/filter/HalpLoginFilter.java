package com.ecquaria.cloud.moh.iais.filter;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.action.BackendLoginDelegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloudfeign.FeignException;
import ecq.commons.exception.BaseException;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * LoginFilter
 *
 * @author Jinhua
 * @date 2020/10/28 12:06
 */
@Slf4j
public class HalpLoginFilter {

    public void doAdlogin(HttpServletRequest request, String userIdStr) {
        BackendLoginDelegator blDelegate = SpringContextHelper.getContext().getBean(BackendLoginDelegator.class);

        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request,
                AppConsts.SESSION_ATTR_LOGIN_USER);
        if (loginContext == null) {
            if (!StringUtil.isEmpty(userIdStr)) {
                String userId = userIdStr.substring(userIdStr.lastIndexOf('\\') + 1);
                try {
                    blDelegate.doLogin(userId, request);
                } catch (FeignException | BaseException e) {
                    log.error(e.getMessage(), e);
                    throw new IaisRuntimeException(e);
                }
            }
        }
    }
}
