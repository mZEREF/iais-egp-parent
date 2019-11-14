package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.OrgUserDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.impl.OrgUserManageServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * OrgUserManageDelegate
 *
 * @author Jinhua
 * @date 2019/10/18 15:07
 */
@Delegator("orgUserManageDelegate")
@Slf4j
public class OrgUserManageDelegate {
    public static final String ORG_USER_DTO_ATTR                   = "orgUserDto";

    @Autowired
    private OrgUserManageServiceImpl orgUserManageService;

    OrgUserDto userDto = new OrgUserDto();

    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug("****doStart Process ****");
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("Front End Admin", "User Management");
        ParamUtil.setSessionAttr(request, ORG_USER_DTO_ATTR, new OrgUserDto());
    }

    /**
     * AutoStep: preparePage
     *
     * @param bpc
     * @throws
     */
    public void preparePage(BaseProcessClass bpc) {
        log.debug("****preparePage Process ****");
        //todo:preparePage
    }

    /**
     * AutoStep: validation
     *
     * @param bpc
     * @throws
     */
    public void validation(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        userDto.setUenNo(ParamUtil.getString(request,"uenNo"));
        userDto.setIdNumber(ParamUtil.getString(request,"idNumber"));
        userDto.setSalutation(ParamUtil.getString(request,"salutation"));
        userDto.setFirstName(ParamUtil.getString(request,"firstName"));
        userDto.setLastName(ParamUtil.getString(request,"lastName"));
        userDto.setEmailAddr(ParamUtil.getString(request,"emailAddr"));
        userDto.setOrgId("4B2B9D34-59EE-E911-BE76-000C294908E1");
        userDto.setUserId("userid");
        userDto.setStatus("CMSTAT001");
        userDto.setUserDomain("internet");
        userDto.setUserName(userDto.getFirstName() + " " + userDto.getLastName());
        ValidationResult validationResult =WebValidationHelper.validateEntity(userDto);
        log.debug(StringUtil.changeForLog("*******************validationResult-->:"+validationResult));
        log.debug(StringUtil.changeForLog("*******************validationResult.isHasErrors-->:"+validationResult.isHasErrors()));

        if (validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else{
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            orgUserManageService.saveOrgManage(userDto);
        }

    }

    /**
     * AutoStep: InsertDatabase
     *
     * @param bpc
     * @throws
     */
    public void insertDatabase(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("*******************insertDatabase end"));
    }


}
