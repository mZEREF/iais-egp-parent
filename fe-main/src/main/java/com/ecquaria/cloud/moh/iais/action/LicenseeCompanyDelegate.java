package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.OrgUserManageService;
import lombok.extern.slf4j.Slf4j;
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
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,MasterCodeUtil.getCodeDesc(licenseeDto.getLicenseeType()));
    }

    public void company(BaseProcessClass bpc) {
        LoginContext loginContext= (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        LicenseeDto licenseeDto = orgUserManageService.getLicenseeById(loginContext.getLicenseeId());
        licenseeDto.setLicenseeType(MasterCodeUtil.getCodeDesc(licenseeDto.getLicenseeType()));
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


}
