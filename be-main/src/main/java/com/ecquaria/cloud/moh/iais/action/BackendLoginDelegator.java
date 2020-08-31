package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.jwt.JwtEncoder;
import com.ecquaria.cloud.moh.iais.common.jwt.JwtVerify;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import com.ecquaria.cloud.moh.iais.web.logging.util.AuditLogUtil;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.iwe.SessionManager;
import sop.rbac.user.User;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;

/**
 * BackendLoginDelegator
 *
 * @author junyu
 * @date 2020/4/7
 */
@Slf4j
@Delegator("backendLoginDelegator")
public class BackendLoginDelegator {
    @Autowired
    private OrganizationMainClient organizationMainClient;
    @Autowired
    private SubmissionClient submissionClient;
    @Value("${halp.fakelogin.flag}")
    private boolean fakeLogin;
    @Value("${jwt.login.base64encodedpub}")
    private String base64encodedPub;

    private String base64encodedPriv = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDGzkPTJxV5tmS2LQbUSoueCXjvrmaM3yxFOGra5/GU9MBSgQYP7yCXgJNrsT8s7ZdroK4O/fTfNLRsbKEY2Qt2a1D+ZHH0FACs2h8j/eeN1WMeHGuvEAGhbtvVXn3TxZ4XhylNBecuAzAbzmstebfcpx+oT4ycClNzPS5GdT1PcjTUgbb4eJaKatgbzJJBL8m3QFZYnVaqFpqeQ2CbCG0nxtzJwBMAXpLIF0NvGTymoGY98mZpTeueu/EOSxBWyMSzO3DYBhw9neTFkDcWCL+44KUzC2DdkrgunrNVx4m9KxyDWjhpEhK4Hr7okfm7Xo8ntuerhQMKv9QaCdpg3CErAgMBAAECggEACnpIfNJsgVOpjyhkWb/sB9I7+3XXlckVTjig+RSMWOtlT3PZW/GgaBuwqVZYHAgRmOyI/+VGiJUAhU5cVzDpN76EMEQe9VwwhOuiBIWXNJRwet/IlRtk2ps7Hs2yF/0sTdUjyhlri7NDT9PbNjRaClkMhBRdNsQcjdBFphT7vDoS13aJ0vFiG1pW8NIyzGjoKv96ulWBU5EgIumQkXvyHYf7mSj67DUQ33Jc2uI67KpMk7lh9sLCfZs5RPJNyTnE0snhRCKj9rjMWelFGoSvMIF8Vmv1oamkbt/WGeX2rcltvFUT2d1PYYBmQhHJEwKm0b3FobUBmQ0Jdpb1kcNQoQKBgQDv/cYNdctSlD+Q7s8k+MmmRhg7shpSJix9QDZuiFnveoqspDInlccewI4DyZrYFvxzaMPktXQRCuPQaIeYAI/OwM7QG8M47hx5CLMHmNADNOJ98Tb2QiQTEYCkBAs84yHZFQmumfVIBqKnH9cx9noBU+gZMkVrGQR3eqfXszlMWQKBgQDUES7ki8cg7IAxvnijMeOmVvIE37Z63+8tOcFigTncSek92QXVr6tif/Vv4NrnrofXHHw0UFUXeXrSz+TEtk+jCvqhxZtgUawuUPpP99OpmgeT62MpaYf8Yp6BoJdY4IzuNtCZTcQ0LPrZyZ2kSD+VD8x7NGurp7dSRh1dyeIZIwKBgCz/aCMd8wGIymJiZHSKMUT7349R1Z2RisXxREN1TWeZkmbMYpsJekxJzlbndiEOLkd1XUWEwD5xzEVHVCUr9crOW/ipO/Fws+X9u5OxE0+GmvECSUvjGuB4Z6ZZG8JO1Anf5/DU7SiUFetADRlRHY8iLGKNCncKOcwtM0s+T4ABAoGBAIJMKH6+L1RYW4+00xSaU6BhbYR3G/y1WJR2Fw76DV3/f7LDhGU98VStAFWEV5vgTZBq7RmLfizVNBURLSN8TMQFu+bnrkbpB5zRnmrB/6gGfr9w8lWAhNAam/9Td4phNdYrkdGPyYyxUlO8IALPbVmeAmp00+79NpLzh5mXsDb3AoGBAOJVuzBt+zx/JLrcyi1zpftZHaJtod2GtDvxmVqm8DeSdXT1Bgs3CQwejpZQieC+OKGUqWIKP72s6DvPLc821D6TXHt6lBYtzTDkyfvrHY4TzW4nwpfuhLMM6BUVHK1HbffybS4jb47F7Q/7jCqxzflmc4UT/9XTOu1hXATZ6lL6\r\n";

    public void start(BaseProcessClass bpc){
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);

        if(loginContext!=null){
            if(loginContext.getUserDomain().equals(IntranetUserConstant.DOMAIN_INTRANET)){
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
            }else {
                ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            }
        }else {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");

        }
    }

    public void preLogin(BaseProcessClass bpc){

    }

    public void doLogin(BaseProcessClass bpc) throws InvalidKeySpecException, NoSuchAlgorithmException {
        HttpServletRequest request=bpc.request;
        JwtVerify verifier = new JwtVerify();
        String jwtt = null;
        if (fakeLogin) {
            jwtt = (String) request.getAttribute("encryptJwtt");
        } else {
            jwtt = request.getHeader("authToken");
        }
        Jws<Claims> claimsFromToken = verifier.parseVerifyJWT(jwtt, base64encodedPub + "\r\n");
        Claims claims = claimsFromToken.getBody();
        String userId = (String) claims.get("userid");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        OrgUserDto orgUserDto = null;
        try {
            orgUserDto = organizationMainClient.retrieveOneOrgUserAccount(userId).getEntity();
            ParamUtil.setSessionAttr(request,"orgUserDto",orgUserDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        errorMap = validate(request, userId);
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            return;
        }

        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");

        User user = new User();
        assert orgUserDto != null;
        user.setDisplayName(orgUserDto.getDisplayName());
        user.setMobileNo(orgUserDto.getMobileNo());
        user.setEmail(orgUserDto.getEmail());
        user.setUserDomain(AppConsts.HALP_EGP_DOMAIN);
        user.setPassword("$2a$12$BaTEVyvwaRuop2SdFoK5jOZvK8tnycxVNx1MYVGjbd1vPEQLcaK4K");
        user.setId(orgUserDto.getUserId());

        SessionManager.getInstance(bpc.request).imitateLogin(user, true, true);
        SessionManager.getInstance(bpc.request).initSopLoginInfo(bpc.request);
        AccessUtil.initLoginUserInfo(bpc.request);
        List<AuditTrailDto> trailDtoList = IaisCommonUtils.genNewArrayList(1);
        AuditTrailDto auditTrailDto = new AuditTrailDto();
        auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTRANET);
        auditTrailDto.setOperation(AuditTrailConsts.OPERATION_LOGIN);
        IaisEGPHelper.setAuditLoginUserInfo(auditTrailDto);
        trailDtoList.add(auditTrailDto);
        try {
            String eventRefNo = String.valueOf(System.currentTimeMillis());
            log.info(StringUtil.changeForLog("be call event bus for login , the event ref number is " + eventRefNo));
            AuditLogUtil.callWithEventDriven(trailDtoList, submissionClient, eventRefNo);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Map<String, String> validate(HttpServletRequest request, String userId) {
        OrgUserDto orgUserDto= (OrgUserDto) ParamUtil.getSessionAttr(request,"orgUserDto");
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();

        if (orgUserDto==null) {
            // Add Audit Trail -- Start
            List<AuditTrailDto> adList = IaisCommonUtils.genNewArrayList(1);
            AuditTrailDto auditTrailDto = new AuditTrailDto();
            auditTrailDto.setMohUserId(userId);
            auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTRANET);
            auditTrailDto.setOperation(AuditTrailConsts.OPERATION_LOGIN_FAIL);
            adList.add(auditTrailDto);
            try {
                AuditLogUtil.callWithEventDriven(adList, submissionClient);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            // End Audit Trail -- End
            errMap.put("login","LOGIN_ERR001");
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
