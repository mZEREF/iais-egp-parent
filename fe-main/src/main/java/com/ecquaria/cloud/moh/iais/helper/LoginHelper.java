package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.web.logging.util.AuditLogUtil;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
public final class LoginHelper {
    public static final String INBOX_URL = "/main-web/eservice/INTERNET/MohInternetInbox";
    public static final String MAIN_WEB_URL = "/main-web/";
    public static final String CORPPASS_URL = "/main-web/eservice/INTERNET/FE_Landing/1/croppass";

    public static void initUserInfo(HttpServletRequest request, HttpServletResponse response, User user, int loginType){
        user.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
        SessionManager.getInstance(request).imitateLogin(user, true, true);
        SessionManager.getInstance(request).initSopLoginInfo(request);

        AccessUtil.initLoginUserInfo(request);
        List<AuditTrailDto> trailDtoList = IaisCommonUtils.genNewArrayList(1);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTERNET);
        auditTrailDto.setOperation(AuditTrailConsts.OPERATION_LOGIN);
        auditTrailDto.setLoginType(loginType);
        auditTrailDto.setNricNumber(user.getIdentityNo());
        IaisEGPHelper.setAuditLoginUserInfo(auditTrailDto);
        trailDtoList.add(auditTrailDto);
        SubmissionClient client = SpringContextHelper.getContext().getBean(SubmissionClient.class);
        try {
            String eventRefNo = String.valueOf(System.currentTimeMillis());
            log.info(StringUtil.changeForLog("call event bus for login , the event ref number is " + eventRefNo));
            AuditLogUtil.callWithEventDriven(trailDtoList, client, eventRefNo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    public static boolean isTestMode(HttpServletRequest request){
        String openTestMode = (String) ParamUtil.getSessionAttr(request, "openTestMode");
        return !StringUtil.isEmpty(openTestMode) && "Y".equals(openTestMode) ? true : false;
    }

    private LoginHelper() {
        throw new IllegalStateException("Utility class");
    }

    public static void insertAuditTrail(String identityNo){
        insertAuditTrail(null, identityNo);
    }

    public static void insertAuditTrail(String uen, String identityNo){
        ApplicationContext context = SpringContextHelper.getContext();
        if (context == null){
            return;
        }

        SubmissionClient client = context.getBean(SubmissionClient.class);
        if (client == null){
            return;
        }

        log.info("insertAuditTrail.........fe");
        List<AuditTrailDto> adList = IaisCommonUtils.genNewArrayList(1);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        auditTrailDto.setMohUserId(identityNo);
        auditTrailDto.setUenId(uen);
        auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTERNET);
        auditTrailDto.setOperation(AuditTrailConsts.OPERATION_LOGIN_FAIL);
        adList.add(auditTrailDto);
        try {
            AuditLogUtil.callWithEventDriven(adList, client);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
