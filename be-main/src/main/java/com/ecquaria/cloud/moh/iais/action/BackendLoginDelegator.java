package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.iwe.SessionManager;
import sop.rbac.user.User;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    OrganizationMainClient organizationMainClient;

    public void Start(BaseProcessClass bpc){

    }
    public void preLogin(BaseProcessClass bpc){

    }
    public void doLogin(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;
        HttpServletResponse response=bpc.response;
        String userId= ParamUtil.getString(request,"entityId");
        try {
            OrgUserDto orgUserDto=organizationMainClient.retrieveOneOrgUserAccount(userId).getEntity();
            ParamUtil.setSessionAttr(request,"orgUserDto",orgUserDto);
        }catch (Exception e){
            log.info(e.getMessage());
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
        user.setUserDomain(orgUserDto.getUserDomain());
        user.setPassword("$2a$12$BaTEVyvwaRuop2SdFoK5jOZvK8tnycxVNx1MYVGjbd1vPEQLcaK4K");
        user.setId(orgUserDto.getUserId());
        //user.setId(orgUserDto.getUserDomain());

        SessionManager.getInstance(bpc.request).imitateLogin(user, true, true);
        SessionManager.getInstance(bpc.request).initSopLoginInfo(bpc.request);
        AccessUtil.initLoginUserInfo(bpc.request);




    }

    public void validRole(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;

        OrgUserDto orgUserDto= (OrgUserDto) ParamUtil.getSessionAttr(request,"orgUserDto");

        if (orgUserDto.getUserRoles().size()!=1) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
        }else {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");

        }
    }

    public void preRole(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;

        OrgUserDto orgUserDto= (OrgUserDto) ParamUtil.getSessionAttr(request,"orgUserDto");
        List<SelectOption> selectOptions= IaisCommonUtils.genNewArrayList();
        for (String userRole:orgUserDto.getUserRoles()
        ) {
            SelectOption selectOption=new SelectOption();
            selectOption.setText(userRole);
            selectOption.setValue(userRole);
            selectOptions.add(selectOption);
        }
        ParamUtil.setRequestAttr(request,"roleTypeOption", selectOptions);

    }

    public void doRole(BaseProcessClass bpc){
        String role=ParamUtil.getString(bpc.request,"decisionRole");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        loginContext.setCurRoleId(role);
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER,loginContext);

    }



    private Map<String, String> validate(HttpServletRequest request) {
        OrgUserDto orgUserDto= (OrgUserDto) ParamUtil.getSessionAttr(request,"orgUserDto");
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();

        if(orgUserDto==null){
            errMap.put("login","Please key in a valid userId");
        }
        String role=ParamUtil.getString(request,"decisionRole");
        if(StringUtil.isEmpty(role)){
            if ("Select".equals(role)){
                errMap.put("role","ERR0010");
            }
        }
        return errMap;
    }
}
