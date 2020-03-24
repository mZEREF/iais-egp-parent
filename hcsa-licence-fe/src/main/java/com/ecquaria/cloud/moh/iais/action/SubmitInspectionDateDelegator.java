package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.SubmitInspectionDate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: yichen
 * @description:
 * @date:2020/3/23
 **/
@Delegator(value = "submitInspectionDateDelegator")
@Slf4j
public class SubmitInspectionDateDelegator {

    @Autowired
    private SubmitInspectionDate submitInspectionDate;

    /**
     * @AutoStep: startStep
     * @param:
     * @return:
     * @author: yichen
     */
    public void startStep(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>SubmitInspectionDateDelegator");
        AuditTrailHelper.auditFunction("Submit Inspection Date", "Submit Inspection Date");
    }


    /**
     * @AutoStep: preLoad
     * @param:
     * @return:
     * @author: yichen
     */
    public void preLoad(BaseProcessClass bpc){
        HttpServletRequest servletRequest = bpc.request;

        String groupId = ParamUtil.getMaskedString(servletRequest, "appGroupId");
        if(StringUtils.isEmpty(groupId)){
            log.info("submit inspection date can not find app group id" + groupId);
            return;
        }

        ApplicationGroupDto group = submitInspectionDate.getApplicationGroupByGroupId(groupId);
        if (group == null){
            log.info("submit inspection date can not find app group ");
            return;
        }

        List<ApplicationDto> applicationList = submitInspectionDate.listApplicationByGroupId(groupId);
        Date submitDate = group.getSubmitDt();

        List<HcsaServicePrefInspPeriodDto> afterAppPeriodList = submitInspectionDate.getPrefInspPeriodList(applicationList);
        Date afterApp = submitInspectionDate.getBlockPeriodByAfterApp(submitDate, afterAppPeriodList);

        applicationList = applicationList.stream().filter(i -> !StringUtils.isEmpty(i.getLicenceId())).collect(Collectors.toList());
        List<HcsaServicePrefInspPeriodDto> beforeLicencePeriodList = submitInspectionDate.getPrefInspPeriodList(applicationList);

        Date licenceExpire = submitInspectionDate.getBlockPeriodByBeforeLicenceExpire(submitDate, beforeLicencePeriodList);

        ParamUtil.setSessionAttr(servletRequest, "inspStartDate" , afterApp);
        ParamUtil.setSessionAttr(servletRequest, "inspEndDate" , licenceExpire);
    }





    /**
     * @AutoStep: validate
     * @param:
     * @return:
     * @author: yichen
     */
    public void validate(BaseProcessClass bpc){
        Date afterApp = (Date) ParamUtil.getSessionAttr(bpc.request, "inspStartDate" );
        Date licenceExpire = (Date) ParamUtil.getSessionAttr(bpc.request, "inspEndDate");

        String startDate = ParamUtil.getString(bpc.request, "inspStartDate");
        String endDate = ParamUtil.getString(bpc.request, "inspEndDate");

        if (StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("dateError", "Please input inspection start date and end date."));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        Date sDate = IaisEGPHelper.parseToDate(startDate, "dd/MM/yyyy");
        Date eDate = IaisEGPHelper.parseToDate(endDate, "dd/MM/yyyy");

        // applicant submit start date and end date that it need in a period
        if (afterApp != null){
            boolean isAfterDate = IaisEGPHelper.isAfterDate(afterApp, sDate);
            if (!isAfterDate){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("inspStartDate", "Inspection start date cannot be earlier than " + IaisEGPHelper.parseToString(afterApp, "dd/MM/yyyy")));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                return;
            }
        }

        if (licenceExpire != null){
            boolean isAfterDate = IaisEGPHelper.isAfterDate(eDate, licenceExpire);
            if (!isAfterDate){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("inspEndDate", "Inspection end date cannot be later than " + IaisEGPHelper.parseToString(licenceExpire, "dd/MM/yyyy")));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                return;
            }
        }

        ParamUtil.setRequestAttr(bpc.request, "inspStartDate", sDate);
        ParamUtil.setRequestAttr(bpc.request, "inspEndDate", eDate);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
    }


    /**
     * @AutoStep: submit
     * @param:
     * @return:
     * @author: yichen
     */
    public void submit(BaseProcessClass bpc){
        // String groupId = (String) ParamUtil.getSessionAttr(servletRequest, "appGroupId");
        String groupId = "8FA5F758-801C-EA11-BE78-000C29D29DB0";

        Date sDate = (Date) ParamUtil.getRequestAttr(bpc.request, "inspStartDate");
        Date eDate =  (Date)  ParamUtil.getRequestAttr(bpc.request,"inspEndDate");

        submitInspectionDate.submitInspStartDateAndEndDate(groupId, sDate, eDate);

        ParamUtil.setRequestAttr(bpc.request, "ackMsg", "You have successfully submitted the inspection time.");

    }
}
