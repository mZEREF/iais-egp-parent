package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.myinfo.MyInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
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
    @Autowired
    MyInfoAjax myInfoAjax;
    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void start(BaseProcessClass bpc){

    }

    /**
     * AutoStep: preparePage
     *
     * @param bpc
     * @throws
     */
    public void prepare(BaseProcessClass bpc) {
        String name = ParamUtil.getString(bpc.request,"name");
        String id = ParamUtil.getMaskedString(bpc.request,name);
        ParamUtil.setSessionAttr(bpc.request,"licenseeId",id);
        String flag = ParamUtil.getString(bpc.request,"licenseeCompanyflag");
        if(StringUtil.isEmpty(flag)){
            flag = "common";
        }
        //if pop html,cant add audit
        if(!"pop".equals(flag)){
            AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INTERNAL_INBOX, AuditTrailConsts.FUNCTION_LICENSEE_COMPANY);
        }

        log.debug("****preparePage Process ****");
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        LicenseeDto licenseeDto = orgUserManageService.getLicenseeById(loginContext.getLicenseeId());
        String curdType = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if("refresh".equals(curdType)){
            if("Company".equals(MasterCodeUtil.getCodeDesc(licenseeDto.getLicenseeType()))) {
                String organizationId = loginContext.getOrgId();
                OrganizationDto organizationDto = orgUserManageService.getOrganizationById(organizationId);
                orgUserManageService.refreshLicensee(organizationDto.getUenNo());
            }else{
                MyInfoDto myInfoDto = myInfoAjax.getMyInfo(loginContext.getNricNum());
                if(myInfoDto != null){
                    if(!myInfoDto.isServiceDown()){
                        FeUserDto feUserDto = orgUserManageService.getUserAccount(loginContext.getUserId());
                        if(myInfoDto != null && feUserDto != null){
                            orgUserManageService.saveMyinfoDataByFeUserDtoAndLicenseeDto(licenseeDto,feUserDto,myInfoDto,true);
                        }
                    }else {
                        ParamUtil.setRequestAttr(bpc.request,"myinfoServiceDown", "Y");
                    }
                }else {
                    log.info("------- Illegal operation get Myinfo ---------");
                }
            }
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

        List<LicenseeDto> licenseesDto = orgUserManageService.getLicenseeByOrgId(loginContext.getOrgId());
        LicenseeDto licenseeDto = licenseesDto.get(0);
        licenseeDto.setLicenseeType(MasterCodeUtil.getCodeDesc(licenseeDto.getLicenseeType()));
        OrganizationDto organizationDto= orgUserManageService.getOrganizationById(loginContext.getOrgId());
        List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDto = orgUserManageService.getPersonById(loginContext.getLicenseeId());
        ParamUtil.setRequestAttr(bpc.request,"organization",organizationDto);
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
        String flag = ParamUtil.getString(bpc.request,"licenseeCompanyflag");
        if(StringUtil.isEmpty(flag)){
            flag = "common";
        }
        ParamUtil.setRequestAttr(bpc.request,"licenseeCompanyflag",flag);
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        LicenseeDto licenseeDto = orgUserManageService.getLicenseeById(loginContext.getLicenseeId());
        List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDto = orgUserManageService.getPersonById(loginContext.getLicenseeId());
        ParamUtil.setRequestAttr(bpc.request,"person",licenseeKeyApptPersonDto);
        ParamUtil.setRequestAttr(bpc.request,"licensee",licenseeDto);

    }

    public void authorised(BaseProcessClass bpc) {
        log.debug("****preparePage Process ****");
        String flag = ParamUtil.getString(bpc.request,"licenseeCompanyflag");
        if(StringUtil.isEmpty(flag)){
            flag = "common";
        }
        ParamUtil.setRequestAttr(bpc.request,"flag",flag);
        String id = (String)ParamUtil.getSessionAttr(bpc.request,"licenseeId");
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<FeUserDto> feUserDtos = orgUserManageService.getAccountByOrgId(loginContext.getOrgId());
        if(feUserDtos!= null && feUserDtos.size() > 0){
            for (FeUserDto item:feUserDtos
                 ) {
                if(item.getId().equals(id)){
                    String nric =  ((FeUserDto) item).getIdentityNo() + " (NRIC)";
                    item.setDesignation(MasterCodeUtil.getCodeDesc(item.getDesignation()));
                    ParamUtil.setRequestAttr(bpc.request,"nric",nric);
                    ParamUtil.setRequestAttr(bpc.request,"feuser",item);
                }
            }
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
