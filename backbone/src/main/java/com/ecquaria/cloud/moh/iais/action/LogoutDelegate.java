package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.web.logging.util.AuditLogUtil;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.audit.SOPAuditLog;
import sop.audit.SOPAuditLogConstants;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.Cookie;
import java.util.List;

/**
 * LogoutDelegate
 *
 * @author Jinhua
 * @date 2020/3/11 9:55
 */
@Slf4j
@Delegator("iaisLogoutDelegate")
public class LogoutDelegate {
    @Autowired
    private SubmissionClient client;

    public void logout(BaseProcessClass bpc) {
        if (bpc.request != null) {
            sop.rbac.user.UserIdentifier userIden = new  sop.rbac.user.UserIdentifier();
            sop.iwe.SessionManager session_mgmt = sop.iwe.SessionManager.getInstance(bpc.request);
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);

            String userdomain = session_mgmt.getCurrentUserDomain();
            String userid=session_mgmt.getCurrentUserID();

            //Add audit trail
            AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
            List<AuditTrailDto> dtoList = IaisCommonUtils.genNewArrayList();
            dtoList.add(auditTrailDto);
            auditTrailDto.setOperation(AuditTrailConsts.OPERATION_LOGOUT);

            if (loginContext != null){
                String curDomain = loginContext.getUserDomain();
                auditTrailDto.setOperationType(AppConsts.USER_DOMAIN_INTERNET.equalsIgnoreCase(curDomain) ?
                        AuditTrailConsts.OPERATION_TYPE_INTERNET : AuditTrailConsts.OPERATION_TYPE_INTRANET);
            }

            if(!StringUtil.isEmpty(userid) && !StringUtil.isEmpty(userdomain)){
                userIden.setUserDomain(userdomain);
                userIden.setId(userid);
                String event = SOPAuditLogConstants.getLogEvent(
                        SOPAuditLogConstants.KEY_LOGOUT,
                        new String[] { userIden.getUserDomain() });

                String eventData = SOPAuditLogConstants.getLogEventData(
                        SOPAuditLogConstants.KEY_LOGOUT, new String[] { userIden.getUserDomain(),userIden.getId() });

                SOPAuditLog.log(userIden, event, eventData,
                        SOPAuditLogConstants.MODULE_LOGOUT);
            }

            bpc.request.getSession().invalidate();
            Cookie[] cookies = bpc.request.getCookies();
            if(cookies != null) {
                Cookie cookie = bpc.request.getCookies()[0];
                cookie.setMaxAge(0);
            }

            try {
                AuditLogUtil.callWithEventDriven(dtoList, client);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
