package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * licenseeCompanyDelegate
 *
 * @author guyin
 * @date 2019/10/18 15:07
 */
@Delegator("licenseeCompanyDelegate")
@Slf4j
public class LicenseeCompanyDelegate {

    @Autowired
    OrgUserManageService orgUserManageService;
    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction("LicenseeSoloDelegate", "LicenseeSoloDelegate");
        log.debug("****doStart Process ****");
    }

    /**
     * AutoStep: preparePage
     *
     * @param bpc
     * @throws
     */
    public void prepare(BaseProcessClass bpc) {
        log.debug("****preparePage Process ****");
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        LicenseeDto licenseeDto = orgUserManageService.getLicenseeById(loginContext.getLicenseeId());

        if(ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE).equals("refresh")){
            String organizationId = loginContext.getOrgId();
            OrganizationDto organizationDto = orgUserManageService.getOrganizationById(organizationId);
            orgUserManageService.createCropUser(organizationDto);
        }
        String type = ParamUtil.getString(bpc.request,"licenseView");
        if(type != null && !StringUtils.isEmpty(type) ){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,type);
        }else if("Company".equals(MasterCodeUtil.getCodeDesc(licenseeDto.getLicenseeType()))){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,"Company");
        }else{
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,"Solo");
        }
    }

    public void company(BaseProcessClass bpc) {
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<LicenseeDto> licenseeDto = orgUserManageService.getLicenseeByOrgId(loginContext.getOrgId());

        List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDto = orgUserManageService.getPersonById(loginContext.getLicenseeId());
        ParamUtil.setRequestAttr(bpc.request,"licensee",licenseeDto);
        ParamUtil.setRequestAttr(bpc.request,"person",licenseeKeyApptPersonDto);

    }

    public void solo(BaseProcessClass bpc) {
        log.debug("****preparePage Process ****");
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        LicenseeDto licenseeDto = orgUserManageService.getLicenseeById(loginContext.getLicenseeId());
        ParamUtil.setRequestAttr(bpc.request,"licensee",licenseeDto);
    }

    public void licensee(BaseProcessClass bpc) {
        log.debug("****preparePage Process ****");
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        LicenseeDto licenseeDto = orgUserManageService.getLicenseeById(loginContext.getLicenseeId());
        List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDto = orgUserManageService.getPersonById(loginContext.getLicenseeId());
        ParamUtil.setRequestAttr(bpc.request,"person",licenseeKeyApptPersonDto);
        ParamUtil.setRequestAttr(bpc.request,"licensee",licenseeDto);

    }

    public void authorised(BaseProcessClass bpc) {
        log.debug("****preparePage Process ****");
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDto = orgUserManageService.getPersonById(loginContext.getLicenseeId());
        if(licenseeKeyApptPersonDto!= null && licenseeKeyApptPersonDto.size() > 0){
            LicenseeKeyApptPersonDto licenseeper = licenseeKeyApptPersonDto.get(0);
            String nric = "XXXX"+licenseeper.getIdNo().substring(3) + "(NRIC)";
            ParamUtil.setRequestAttr(bpc.request,"nric",nric);
            ParamUtil.setRequestAttr(bpc.request,"licensee",licenseeper);
        }


    }

    public void medAlert(BaseProcessClass bpc) {
        log.debug("****preparePage Process ****");
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        LicenseeDto licenseeDto = orgUserManageService.getLicenseeById(loginContext.getLicenseeId());
        ParamUtil.setRequestAttr(bpc.request,"licensee",licenseeDto);
    }

    public void backToMenu(BaseProcessClass bpc){
        try {
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohServiceFeMenu/backAppBefore");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }

}
