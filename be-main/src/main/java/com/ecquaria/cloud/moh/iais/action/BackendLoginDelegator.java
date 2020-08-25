package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import com.ecquaria.cloud.moh.iais.web.logging.util.AuditLogUtil;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.iwe.SessionManager;
import sop.rbac.user.User;
import sop.webflow.rt.api.BaseProcessClass;

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
    public void doLogin(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;
        String userId= ParamUtil.getString(request,"entityId");
        try {
            OrgUserDto orgUserDto=organizationMainClient.retrieveOneOrgUserAccount(userId).getEntity();
            ParamUtil.setSessionAttr(request,"orgUserDto",orgUserDto);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        errorMap=validate(request);
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            return;
        }
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");


        OrgUserDto orgUserDto= (OrgUserDto) ParamUtil.getSessionAttr(request,"orgUserDto");

        User user = new User();
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
            AuditLogUtil.callWithEventDriven(trailDtoList, submissionClient);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private Map<String, String> validate(HttpServletRequest request) {
        OrgUserDto orgUserDto= (OrgUserDto) ParamUtil.getSessionAttr(request,"orgUserDto");
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();

        if (orgUserDto==null) {
            String userId = ParamUtil.getString(request,"entityId");
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
            errMap.put("login","Please key in a valid userId");
        }
        return errMap;
    }
    public void aferSubmit(BaseProcessClass bpc){

    }
    public void initLogin(BaseProcessClass bpc){

    }
}
