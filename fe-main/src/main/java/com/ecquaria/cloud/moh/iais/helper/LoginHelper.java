package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.web.logging.util.AuditLogUtil;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import lombok.extern.slf4j.Slf4j;
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

    public static void initUserInfo(HttpServletRequest request, HttpServletResponse response, User user){
        user.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
        SessionManager.getInstance(request).imitateLogin(user, true, true);
        SessionManager.getInstance(request).initSopLoginInfo(request);

        AccessUtil.initLoginUserInfo(request);
        List<AuditTrailDto> trailDtoList = IaisCommonUtils.genNewArrayList(1);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTERNET);
        auditTrailDto.setOperation(AuditTrailConsts.OPERATION_LOGIN);
        IaisEGPHelper.setAuditLoginUserInfo(auditTrailDto);
        trailDtoList.add(auditTrailDto);
        SubmissionClient client = SpringContextHelper.getContext().getBean(SubmissionClient.class);
        try {
            AuditLogUtil.callWithEventDriven(trailDtoList, client);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private LoginHelper() {
        throw new IllegalStateException("Utility class");
    }
}
