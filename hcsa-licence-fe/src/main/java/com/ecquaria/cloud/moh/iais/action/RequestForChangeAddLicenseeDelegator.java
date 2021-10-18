package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * RequestForChangeAddLicenseeDelegator
 *
 * @author suocheng
 * @date 7/29/2021
 */
@Slf4j
@Delegator("requestForChangeAddLicenseeDelegator")
public class RequestForChangeAddLicenseeDelegator {

    @Autowired
    private AppSubmissionService appSubmissionService;

    /**
     *
     * @param bpc
     * @Decription doStart
     */
    public void doStart(BaseProcessClass bpc){
      log.info(StringUtil.changeForLog("The RequestForChangeAddLicenseeDelegator doStart start..."));
      log.info(StringUtil.changeForLog("The RequestForChangeAddLicenseeDelegator doStart  end..."));
    }

    /**
     *
     * @param bpc
     * @Decription prepare
     */
    public void prepare(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("The RequestForChangeAddLicenseeDelegator prepare start..."));
        String subLicensee = ParamUtil.getString(bpc.request, "subLicensee");
        if("new".equals(subLicensee)){
            SubLicenseeDto subLicenseeDto = null;
            if(ParamUtil.getSessionAttr(bpc.request,"subLicenseeDto")==null){
                subLicenseeDto = new SubLicenseeDto();
                subLicenseeDto.setLicenseeType(OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL);
            }else{
                subLicenseeDto = (SubLicenseeDto) ParamUtil.getSessionAttr(bpc.request,"subLicenseeDto");
            }

            ParamUtil.setRequestAttr(bpc.request,"dto",subLicenseeDto);
          ParamUtil.setRequestAttr(bpc.request,"crud_action_type_switch1","addNew");
        }else{
            ParamUtil.setRequestAttr(bpc.request,"crud_action_type_switch1","return");
        }
        log.info(StringUtil.changeForLog("The RequestForChangeAddLicenseeDelegator prepare end..."));
    }

    /**
     *
     * @param bpc
     * @Decription validate
     */
    public void validate(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("The RequestForChangeAddLicenseeDelegator validate start..."));
        SubLicenseeDto subLicenseeDto = NewApplicationDelegator.getSubLicenseeDtoDetailFromPage(bpc.request);
        subLicenseeDto.setAssignSelect(IaisEGPConstant.ASSIGN_SELECT_ADD_NEW);
        subLicenseeDto.setLicenseeType(OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        appSubmissionService.validateSubLicenseeDto(errorMap, subLicenseeDto, bpc.request);
        ParamUtil.setRequestAttr(bpc.request,"dto",subLicenseeDto);
        if (errorMap.size() > 0) {
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request,"crud_action_type_switch2","back");
        }else{
            ParamUtil.setRequestAttr(bpc.request,"crud_action_type_switch2","save");
        }
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO);
        ParamUtil.setRequestAttr(bpc.request,RfcConst.APPSUBMISSIONDTO,appSubmissionDto);
        log.info(StringUtil.changeForLog("The RequestForChangeAddLicenseeDelegator validate end..."));
    }
    /**
     *
     * @param bpc
     * @Decription addLIcensee
     */
    public void addLIcensee(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("The RequestForChangeAddLicenseeDelegator addLIcensee start..."));
        SubLicenseeDto subLicenseeDto = (SubLicenseeDto)ParamUtil.getRequestAttr(bpc.request,"dto");
        ParamUtil.setSessionAttr(bpc.request, "hasNewSubLicensee", Boolean.TRUE);
        ParamUtil.setSessionAttr(bpc.request, "subLicenseeDto", subLicenseeDto);
        ParamUtil.setRequestAttr(bpc.request,"isValidate","Y");
        log.info(StringUtil.changeForLog("The RequestForChangeAddLicenseeDelegator addLIcensee end..."));
    }
    /**
     *
     * @param bpc
     * @Decription returns
     */
    public void returns(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("The RequestForChangeAddLicenseeDelegator returns start..."));
        log.info(StringUtil.changeForLog("The RequestForChangeAddLicenseeDelegator returns isValidate is -->"+
                ParamUtil.getRequestAttr(bpc.request,"isValidate")));
        log.info(StringUtil.changeForLog("The RequestForChangeAddLicenseeDelegator returns end..."));
    }


}
