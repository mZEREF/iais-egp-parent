package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.jwt.JwtEncoder;
import com.ecquaria.cloud.moh.iais.common.jwt.JwtVerify;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.security.AuthenticationConfig;
import com.ecquaria.cloud.usersession.UserSession;
import com.ecquaria.cloud.usersession.UserSessionUtil;
import com.ecquaria.cloud.usersession.client.UserSessionService;
import com.ecquaria.cloudfeign.FeignException;
import ecq.commons.exception.BaseException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sg.gov.moh.iais.egp.bsb.client.LoginClient;
import sop.iwe.SessionManager;
import sop.rbac.user.User;
import sop.webflow.process5.ProcessCacheHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class is copied from {@code com.ecquaria.cloud.moh.iais.action.BackendLoginDelegator} under be-main.
 * This class will be removed when we integrate phase one and two.
 */
@Slf4j
@Delegator("bsbBeLoginDelegator")
public class BsbBeLoginDelegator {
    @Autowired
    private LoginClient loginClient;
    @Value("${halp.fakelogin.flag}")
    private boolean fakeLogin;
    @Value("${jwt.login.base64encodedpub}")
    private String base64encodedPub;

    private final String base64encodedPriv = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDGzkPTJxV5tmS2LQbUSoueCXjvrmaM3yxFOGra5/GU9MBSgQYP7yCXgJNrsT8s7ZdroK4O/fTfNLRsbKEY2Qt2a1D+ZHH0FACs2h8j/eeN1WMeHGuvEAGhbtvVXn3TxZ4XhylNBecuAzAbzmstebfcpx+oT4ycClNzPS5GdT1PcjTUgbb4eJaKatgbzJJBL8m3QFZYnVaqFpqeQ2CbCG0nxtzJwBMAXpLIF0NvGTymoGY98mZpTeueu/EOSxBWyMSzO3DYBhw9neTFkDcWCL+44KUzC2DdkrgunrNVx4m9KxyDWjhpEhK4Hr7okfm7Xo8ntuerhQMKv9QaCdpg3CErAgMBAAECggEACnpIfNJsgVOpjyhkWb/sB9I7+3XXlckVTjig+RSMWOtlT3PZW/GgaBuwqVZYHAgRmOyI/+VGiJUAhU5cVzDpN76EMEQe9VwwhOuiBIWXNJRwet/IlRtk2ps7Hs2yF/0sTdUjyhlri7NDT9PbNjRaClkMhBRdNsQcjdBFphT7vDoS13aJ0vFiG1pW8NIyzGjoKv96ulWBU5EgIumQkXvyHYf7mSj67DUQ33Jc2uI67KpMk7lh9sLCfZs5RPJNyTnE0snhRCKj9rjMWelFGoSvMIF8Vmv1oamkbt/WGeX2rcltvFUT2d1PYYBmQhHJEwKm0b3FobUBmQ0Jdpb1kcNQoQKBgQDv/cYNdctSlD+Q7s8k+MmmRhg7shpSJix9QDZuiFnveoqspDInlccewI4DyZrYFvxzaMPktXQRCuPQaIeYAI/OwM7QG8M47hx5CLMHmNADNOJ98Tb2QiQTEYCkBAs84yHZFQmumfVIBqKnH9cx9noBU+gZMkVrGQR3eqfXszlMWQKBgQDUES7ki8cg7IAxvnijMeOmVvIE37Z63+8tOcFigTncSek92QXVr6tif/Vv4NrnrofXHHw0UFUXeXrSz+TEtk+jCvqhxZtgUawuUPpP99OpmgeT62MpaYf8Yp6BoJdY4IzuNtCZTcQ0LPrZyZ2kSD+VD8x7NGurp7dSRh1dyeIZIwKBgCz/aCMd8wGIymJiZHSKMUT7349R1Z2RisXxREN1TWeZkmbMYpsJekxJzlbndiEOLkd1XUWEwD5xzEVHVCUr9crOW/ipO/Fws+X9u5OxE0+GmvECSUvjGuB4Z6ZZG8JO1Anf5/DU7SiUFetADRlRHY8iLGKNCncKOcwtM0s+T4ABAoGBAIJMKH6+L1RYW4+00xSaU6BhbYR3G/y1WJR2Fw76DV3/f7LDhGU98VStAFWEV5vgTZBq7RmLfizVNBURLSN8TMQFu+bnrkbpB5zRnmrB/6gGfr9w8lWAhNAam/9Td4phNdYrkdGPyYyxUlO8IALPbVmeAmp00+79NpLzh5mXsDb3AoGBAOJVuzBt+zx/JLrcyi1zpftZHaJtod2GtDvxmVqm8DeSdXT1Bgs3CQwejpZQieC+OKGUqWIKP72s6DvPLc821D6TXHt6lBYtzTDkyfvrHY4TzW4nwpfuhLMM6BUVHK1HbffybS4jb47F7Q/7jCqxzflmc4UT/9XTOu1hXATZ6lL6\r\n";

    public void start(BaseProcessClass bpc){
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String sessionId = UserSessionUtil.getLoginSessionID(bpc.request.getSession());
        UserSession us = ProcessCacheHelper.getUserSessionFromCache(sessionId);
        if(loginContext!=null && us != null && "Active".equals(us.getStatus())){
            if(loginContext.getUserDomain().equals(IntranetUserConstant.DOMAIN_INTRANET)){
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
            }else {
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            }
        } else {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");

        }
    }

    public void preLogin(BaseProcessClass bpc){
        // empty
    }

    public void doLogin(BaseProcessClass bpc) throws InvalidKeySpecException, NoSuchAlgorithmException,
                        FeignException, BaseException {
        if (fakeLogin) {
            HttpServletRequest request = bpc.request;
            JwtVerify verifier = new JwtVerify();
            String jwtt = (String) request.getAttribute("encryptJwtt");
            Jws<Claims> claimsFromToken = verifier.parseVerifyJWT(jwtt, base64encodedPub + "\r\n");
            Claims claims = claimsFromToken.getBody();
            String userId = (String) claims.get("userid");

            doLogin(userId, bpc.request);
        }
    }

    public void doLogin(String userId, HttpServletRequest request) throws FeignException, BaseException {
        Map<String, String> errorMap;
        OrgUserDto orgUserDto = null;
        try {
            orgUserDto = loginClient.retrieveOneOrgUserAccount(userId).getEntity();
            ParamUtil.setSessionAttr(request,"orgUserDto",orgUserDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        errorMap = validate(request, userId);
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IntranetUserConstant.ISVALID, "N");
            return;
        }

        ParamUtil.setRequestAttr(request, IntranetUserConstant.ISVALID, "Y");

        User user = new User();
        assert orgUserDto != null;
        user.setDisplayName(orgUserDto.getDisplayName());
        user.setMobileNo(orgUserDto.getMobileNo());
        user.setEmail(orgUserDto.getEmail());
        user.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
        user.setId(orgUserDto.getUserId());

        String conInfo = AuthenticationConfig.getConcurrentUserSession();
        if (AuthenticationConfig.VALUE_CONCURRENT_USER_SESSION_CLOSE_OLD.equals(conInfo)) {
            List<UserSession> usesses = UserSessionService.getInstance()
                    .retrieveActiveSessionByUserDomainAndUserId(user.getUserDomain(), user.getId());
            if (usesses != null && !usesses.isEmpty()) {
                for (UserSession us : usesses) {
                    UserSessionUtil.killUserSession(us.getSessionId());
                }
            }
        }
        SessionManager.getInstance(request).imitateLogin(user, true, true);
        SessionManager.getInstance(request).initSopLoginInfo(request);

        AccessUtil.initLoginUserInfo(request);

        AuditTrailDto auditTrailDto = new AuditTrailDto();
        auditTrailDto.setOperation(AuditTrailConsts.OPERATION_LOGIN);
        auditTrailDto.setMohUserId(userId);
        auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTRANET);
        auditTrailDto.setLoginType(AuditTrailConsts.LOGIN_TYPE_MOH);
        auditTrailDto.setModule("Intranet Login");
        auditTrailDto.setFunctionName("Intranet Login");
        auditTrailDto.setLoginTime(new Date());
        IaisEGPHelper.setAuditLoginUserInfo(auditTrailDto);
        AuditTrailHelper.callSaveAuditTrail(auditTrailDto);
    }

    private Map<String, String> validate(HttpServletRequest request, String userId) {
        OrgUserDto orgUserDto= (OrgUserDto) ParamUtil.getSessionAttr(request,"orgUserDto");
        Map<String, String> errMap = new HashMap<>();

        if (orgUserDto==null||orgUserDto.getUserDomain().equals(AppConsts.USER_DOMAIN_INTERNET)||!orgUserDto.getStatus().equals(IntranetUserConstant.COMMON_STATUS_ACTIVE)) {
            // Add Audit Trail -- Start
            AuditTrailHelper.insertLoginFailureAuditTrail(request, null, userId, "LOGIN_ERR001");
            // End Audit Trail -- End
            errMap.put("login","LOGIN_ERR001");
        }
        if (orgUserDto != null && orgUserDto.getUserRoles().isEmpty()) {
            // Add Audit Trail -- Start
            AuditTrailHelper.insertLoginFailureAuditTrail(request, null, userId, "LOGIN_ERR002");
            // End Audit Trail -- End
            errMap.put("login", "LOGIN_ERR002");
        }
        return errMap;
    }

    public void afterSubmit(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;
        String userId= ParamUtil.getString(request,"entityId");
        JwtEncoder encoder = new JwtEncoder();
        Claims claims = Jwts.claims();
        claims.put("userid", userId);

        //Encode the token JWT Token
        String jwtt = encoder.encode(claims, base64encodedPriv);
        request.setAttribute("encryptJwtt", jwtt);
    }

    public void initLogin(BaseProcessClass bpc){
        if (fakeLogin) {
            bpc.request.setAttribute("isFake", "Y");
        } else {
            bpc.request.setAttribute("isFake", "N");
        }
    }
}
