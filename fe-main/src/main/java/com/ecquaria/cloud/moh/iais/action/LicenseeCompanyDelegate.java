package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.acra.AcraConsts;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.myinfo.MyInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.UserConstants;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.model.MyinfoUtil;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import com.ecquaria.cloud.moh.iais.validation.SoloEditValidator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

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
    private  OrgUserManageService orgUserManageService;
    @Autowired
    private MyInfoAjax myInfoAjax;

    @Autowired
    private LicenceInboxClient licenceClient;
    @Autowired
    private SoloEditValidator soloEditValidator;

    private static final String SOLO_lOGIN_NAME = "solo_login_name";
    private static final String LICENSEE_COMPANYFLAG = "licenseeCompanyflag";
    private static final String LIC_COMMON = "common";
    private static final String LIC_OFFICE_TEL_NO = "officeTelNo";
    private static final String LIC_OFFICE_EMAIL = "officeEmail";
    private static final String LIC_LICENSEE = "licensee";
    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void start(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request,SOLO_lOGIN_NAME,null);
        myInfoAjax.setVerifyTakenAndAuthoriseApiUrl(bpc.request,"MohLicenseeCompanyDetail/Prepare");
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
        String flag = ParamUtil.getString(bpc.request,LICENSEE_COMPANYFLAG);
        if(StringUtil.isEmpty(flag)){
            flag = LIC_COMMON;
        }
        //if pop html,cant add audit
        if(!"pop".equals(flag)){
            AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INTERNAL_INBOX, AuditTrailConsts.FUNCTION_LICENSEE_COMPANY);
        }

        log.debug("****preparePage--Process ****");
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        LicenseeDto licenseeDto = (LicenseeDto) ParamUtil.getSessionAttr(bpc.request, MyinfoUtil.SOLO_DTO_SEESION);
        if (licenseeDto == null) {
            licenseeDto = orgUserManageService.getLicenseeById(AccessUtil.getLoginUser(bpc.request).getLicenseeId());
            ParamUtil.setSessionAttr(bpc.request, MyinfoUtil.SOLO_DTO_SEESION, licenseeDto);
        }
        if(AppConsts.YES .equalsIgnoreCase( (String) ParamUtil.getSessionAttr(bpc.request,MyinfoUtil.MYINFO_TRANSFER_CALL_BACK))){
            MyInfoDto myInfoDto = myInfoAjax.getMyInfoData(bpc.request);
            setLicByMyInfo(bpc.request,myInfoDto,licenseeDto);
        }else {
            String curdType = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
            if("refresh".equals(curdType)){
                if(OrganizationConstants.LICENSEE_TYPE_CORPPASS.equals(licenseeDto.getLicenseeType())) {
                    String organizationId = loginContext.getOrgId();
                    String officeTelNo = ParamUtil.getString(bpc.request, LIC_OFFICE_TEL_NO);
                    String officeEmail = ParamUtil.getString(bpc.request, LIC_OFFICE_EMAIL);
                    //Do validation
                    Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
                    if (StringUtil.isEmpty(officeTelNo)) {
                        errMap.put(LIC_OFFICE_TEL_NO, MessageUtil.getMessageDesc("GENERAL_ERR0006"));
                    } else if (!CommonValidator.isTelephoneNo(officeTelNo)) {
                        errMap.put(LIC_OFFICE_TEL_NO, MessageUtil.getMessageDesc("GENERAL_ERR0015"));
                    }
                    if (StringUtil.isEmpty(officeEmail)) {
                        errMap.put(LIC_OFFICE_EMAIL, MessageUtil.getMessageDesc("GENERAL_ERR0006"));
                    } else if (!ValidationUtils.isEmail(officeEmail)) {
                        errMap.put(LIC_OFFICE_EMAIL, MessageUtil.getMessageDesc("GENERAL_ERR0014"));
                    }
                    //Update info
                    if (IaisCommonUtils.isEmpty(errMap)) {
                        OrganizationDto organizationDto = orgUserManageService.getOrganizationById(organizationId);
                        orgUserManageService.updateCompLicensee(loginContext.getOrgId(), officeTelNo, officeEmail);
                        orgUserManageService.refreshLicensee(organizationDto.getUenNo()); // EDH
                    } else {
                        WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
                        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG,
                                WebValidationHelper.generateJsonStr(errMap));
                    }
                }else{
                    String actionStep = ParamUtil.getString(bpc.request,"saveDataSolo");
                    if("saveDataSolo".equalsIgnoreCase(actionStep)){
                        setSoloPageDto(bpc.request,licenseeDto);
                    }else if(MyinfoUtil.CLEAR_MYINFO_ACTION.equalsIgnoreCase(actionStep)){
                        clearInfo(bpc.request);
                    } else {
                        MyInfoDto myInfoDto = myInfoAjax.getMyInfo(loginContext.getNricNum(),bpc.request);
                        if(myInfoDto != null){
                            if(!myInfoDto.isServiceDown()){
                                setLicByMyInfo(bpc.request,myInfoDto,licenseeDto);
                            }else {
                                ParamUtil.setRequestAttr(bpc.request,UserConstants.MY_INFO_SERVICE_OPEN_FLAG, IaisEGPConstant.YES);
                            }
                        }else {
                            log.info("------- Illegal operation get Myinfo ---------");
                        }
                    }
                }
            }
        }
        String type = ParamUtil.getString(bpc.request,"licenseView");
        if(type != null && !StringUtils.isEmpty(type) ){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,type);
        }else if(OrganizationConstants.LICENSEE_TYPE_CORPPASS.equals(licenseeDto.getLicenseeType())){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,"Company");
        }else{
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,"Solo");
        }
    }


    public void clearInfo(HttpServletRequest request){
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        LicenseeDto licenseeDto = orgUserManageService.getLicenseeById(loginContext.getLicenseeId());
        if(licenseeDto != null){
            //sonar
            getLicDataUserIdForSolo(request,loginContext,licenseeDto);
            licenseeDto.setMobileNo(null);
            licenseeDto.setAddrType(null);
            licenseeDto.setFloorNo(null);
            licenseeDto.setPostalCode(null);
            licenseeDto.setUnitNo(null);
            licenseeDto.setBlkNo(null);
            licenseeDto.setBuildingName(null);
            licenseeDto.setStreetName(null);
            licenseeDto.setMobileNo(null);
            licenseeDto.setEmilAddr(null);
            licenseeDto.setFromMyInfo(0);
            ParamUtil.setRequestAttr(request, MyinfoUtil.IS_LOAD_MYINFO_DATA,AppConsts.NO);
        }
        ParamUtil.setSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION,licenseeDto);
        ParamUtil.setSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION_ACTION,AppConsts.NO);
    }

    public void company(BaseProcessClass bpc) {
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<LicenseeDto> licenseesDto = orgUserManageService.getLicenseeByOrgId(loginContext.getOrgId());
        LicenseeDto licenseeDto = licenseesDto.get(0);
        String curdType = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if("refresh".equals(curdType)){
            licenseeDto.setOfficeTelNo(ParamUtil.getString(bpc.request, LIC_OFFICE_TEL_NO));
            licenseeDto.setEmilAddr(ParamUtil.getString(bpc.request, LIC_OFFICE_EMAIL));
        }
        licenseeDto.setUenNo(loginContext.getUenNo());
        List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDto = orgUserManageService.getPersonById(loginContext.getLicenseeId());
        ParamUtil.setRequestAttr(bpc.request,LIC_LICENSEE,licenseeDto);
        ParamUtil.setRequestAttr(bpc.request,"person",licenseeKeyApptPersonDto);
        // sub licensees (licensee details)
        List<SubLicenseeDto> subLicenseeDtoList = licenceClient.getIndividualSubLicensees(loginContext.getOrgId()).getEntity();
        ParamUtil.setRequestAttr(bpc.request,"subLicenseeDtoList",subLicenseeDtoList);
    }

    public void solo(BaseProcessClass bpc) {
        log.debug("***preparePage Process ***");
        HttpServletRequest request =bpc.request;
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String saveSoleAction = (String) ParamUtil.getSessionAttr(bpc.request,MyinfoUtil.SOLO_DTO_SEESION_ACTION);
        if(AppConsts.YES.equalsIgnoreCase(saveSoleAction) || AppConsts.NO.equalsIgnoreCase(saveSoleAction)){
            LicenseeDto licenseeDto = (LicenseeDto) ParamUtil.getSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION);
            if(AppConsts.YES.equalsIgnoreCase(saveSoleAction)){
                Map<String,String> errorMap = soloEditValidator.validate(request);
                if(!errorMap.isEmpty()){
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                }else {
                    FeUserDto feUserDto = orgUserManageService.getUserAccount(loginContext.getUserId());
                    MyInfoDto myInfoDto = new MyInfoDto();
                    myInfoDto.setUserName(feUserDto.getDisplayName());
                    myInfoDto.setMobileNo(licenseeDto.getMobileNo());
                    myInfoDto.setEmail(licenseeDto.getEmilAddr());
                    myInfoDto.setFloor(licenseeDto.getFloorNo());
                    myInfoDto.setPostalCode(licenseeDto.getPostalCode());
                    myInfoDto.setUnitNo(licenseeDto.getUnitNo());
                    myInfoDto.setBlockNo(licenseeDto.getBlkNo());
                    myInfoDto.setBuildingName(licenseeDto.getBuildingName());
                    myInfoDto.setStreetName(licenseeDto.getStreetName());
                    feUserDto.setFromMyInfo(licenseeDto.getFromMyInfo());
                    orgUserManageService.saveMyinfoDataByFeUserDtoAndLicenseeDto(licenseeDto,feUserDto,myInfoDto,true);
                    ParamUtil.setSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION,null);
                }
            }
            ParamUtil.setRequestAttr(request,LIC_LICENSEE,licenseeDto);
        }else {
            LicenseeDto licenseeDto = orgUserManageService.getLicenseeById(loginContext.getLicenseeId());
            getLicDataUserIdForSolo(request,loginContext,licenseeDto);
            ParamUtil.setRequestAttr(request,LIC_LICENSEE,licenseeDto);
        }
        ParamUtil.setSessionAttr(bpc.request,MyinfoUtil.SOLO_DTO_SEESION_ACTION,null);
    }

     private String getLicDataUserIdForSolo(HttpServletRequest request,LoginContext loginContext, LicenseeDto licenseeDto){
         if(licenseeDto.getLicenseeIndividualDto() != null){
             //function change with wangyu
             FeUserDto feUserDto =   orgUserManageService.getFeUserAccountByNricAndType(licenseeDto.getLicenseeIndividualDto().getIdNo(), licenseeDto.getLicenseeIndividualDto().getIdType(), null);
             licenseeDto.setMobileNo(feUserDto.getMobileNo());
             if( loginContext.getNricNum().equalsIgnoreCase(licenseeDto.getLicenseeIndividualDto().getIdNo())){
                 //no need SOLO_lOGIN_NAME,can get login user get
                 ParamUtil.setSessionAttr(request,SOLO_lOGIN_NAME,null);
                 licenseeDto.setFromMyInfo(feUserDto.getFromMyInfo());
                 ParamUtil.setRequestAttr(request, MyinfoUtil.IS_LOAD_MYINFO_DATA,String.valueOf(feUserDto.getFromMyInfo()));
                 ParamUtil.setSessionAttr(request, UserConstants.SESSION_USER_DTO,feUserDto);
             }else {
                 ParamUtil.setSessionAttr(request,SOLO_lOGIN_NAME,feUserDto.getDisplayName());
             }
             return licenseeDto.getLicenseeIndividualDto().getIdNo();
         }
         log.info(StringUtil.changeForLog("----- loginId is " + loginContext.getLoginId() + " no solo id "));
         return "";
     }

     private void setSoloPageDto(HttpServletRequest request, LicenseeDto licenseeDto){
         licenseeDto.setAddrType(ParamUtil.getString(request,"addrType"));
         licenseeDto.setMobileNo(ParamUtil.getString(request,"telephoneNo"));
         licenseeDto.setEmilAddr(ParamUtil.getString(request,"emailAddr"));
         if(AppConsts.YES.equalsIgnoreCase(ParamUtil.getString(request,"loadMyInfoData"))){
             licenseeDto.setFromMyInfo(1);
             ParamUtil.setRequestAttr(request,MyinfoUtil.IS_LOAD_MYINFO_DATA,AppConsts.YES);
             ParamUtil.setSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION,licenseeDto);
             ParamUtil.setSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION_ACTION,AppConsts.YES);
             return;
         }else {
             licenseeDto.setFromMyInfo(0);
         }
        licenseeDto.setPostalCode(ParamUtil.getString(request,"postalCode"));
        licenseeDto.setBlkNo(ParamUtil.getString(request,"blkNo"));
        licenseeDto.setFloorNo(ParamUtil.getString(request,"floorNo"));
        licenseeDto.setUnitNo(ParamUtil.getString(request,"unitNo"));
        licenseeDto.setStreetName(ParamUtil.getString(request,"streetName"));
        licenseeDto.setBuildingName(ParamUtil.getString(request,"buildingName"));
        licenseeDto.setStreetName(ParamUtil.getString(request,"streetName"));
        licenseeDto.setMobileNo(ParamUtil.getString(request,"telephoneNo"));
        licenseeDto.setEmilAddr(ParamUtil.getString(request,"emailAddr"));
        ParamUtil.setRequestAttr(request,MyinfoUtil.IS_LOAD_MYINFO_DATA,String.valueOf(licenseeDto.getFromMyInfo()));
        ParamUtil.setSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION_ACTION,AppConsts.YES);
        ParamUtil.setSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION,licenseeDto);
    }
    private void setLicByMyInfo(HttpServletRequest request, MyInfoDto myInfoDto,LicenseeDto licenseeDto){
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        FeUserDto feUserDto = orgUserManageService.getUserAccount(loginContext.getUserId());
        licenseeDto.setMobileNo(feUserDto.getMobileNo());
        if(myInfoDto != null){
            licenseeDto.setAddrType(myInfoDto.getAddrType());
            String addrType = ParamUtil.getString(request,"addrType");
            if(StringUtil.isNotEmpty(addrType)){
                myInfoDto.setAddrType(addrType);
            }
            licenseeDto.setName(myInfoDto.getUserName());
            licenseeDto.setFloorNo(myInfoDto.getFloor());
            licenseeDto.setPostalCode(myInfoDto.getPostalCode());
            licenseeDto.setUnitNo(myInfoDto.getUnitNo());
            licenseeDto.setBlkNo(myInfoDto.getBlockNo());
            licenseeDto.setBuildingName(myInfoDto.getBuildingName());
            licenseeDto.setStreetName(myInfoDto.getStreetName());
            licenseeDto.setMobileNo(myInfoDto.getMobileNo());
            licenseeDto.setEmilAddr(myInfoDto.getEmail());
            licenseeDto.setFromMyInfo(1);
            ParamUtil.setRequestAttr(request, MyinfoUtil.IS_LOAD_MYINFO_DATA, AppConsts.YES);
        }else {
            ParamUtil.setRequestAttr(request,UserConstants.MY_INFO_SERVICE_OPEN_FLAG, IaisEGPConstant.YES);
        }
        ParamUtil.setSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION,licenseeDto);
        ParamUtil.setSessionAttr(request,MyinfoUtil.SOLO_DTO_SEESION_ACTION,AppConsts.NO);
    }
    public void licensee(BaseProcessClass bpc) {
        log.debug("**preparePage Process **");
        String flag = ParamUtil.getString(bpc.request,LICENSEE_COMPANYFLAG);
        if(StringUtil.isEmpty(flag)){
            flag = LIC_COMMON;
        }
        ParamUtil.setRequestAttr(bpc.request,LICENSEE_COMPANYFLAG,flag);
        company(bpc);
    }

    public void authorised(BaseProcessClass bpc) {
        log.debug("*preparePage Process ****");
        String flag = ParamUtil.getString(bpc.request,LICENSEE_COMPANYFLAG);
        if(StringUtil.isEmpty(flag)){
            flag = LIC_COMMON;
        }
        ParamUtil.setRequestAttr(bpc.request,"flag",flag);
        String id = (String)ParamUtil.getSessionAttr(bpc.request,"licenseeId");
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<FeUserDto> feUserDtos = orgUserManageService.getAccountByOrgId(loginContext.getOrgId());
        if(IaisCommonUtils.isNotEmpty(feUserDtos)){
            for (FeUserDto item:feUserDtos
                 ) {
                if(item.getId().equals(id)){
                    String nric = item.getIdentityNo() + " (NRIC)";
                    item.setDesignation(MasterCodeUtil.getCodeDesc(item.getDesignation()));
                    ParamUtil.setRequestAttr(bpc.request,"nric",nric);
                    ParamUtil.setRequestAttr(bpc.request,"feuser",item);
                }
            }
        }
    }

    public void medAlert(BaseProcessClass bpc) {
        log.debug("****preparePage Process *");
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        LicenseeDto licenseeDto = orgUserManageService.getLicenseeById(loginContext.getLicenseeId());
        licenseeDto.setAddrType(AcraConsts.getAddressTypeD().get(licenseeDto.getAddrType()));
        ParamUtil.setRequestAttr(bpc.request,LIC_LICENSEE,licenseeDto);
    }

    public void backToMenu(BaseProcessClass bpc){
        try {
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohServiceFeMenu/backAppBefore");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }catch (Exception e){
            log.info(e.getMessage());
        }
    }

}
