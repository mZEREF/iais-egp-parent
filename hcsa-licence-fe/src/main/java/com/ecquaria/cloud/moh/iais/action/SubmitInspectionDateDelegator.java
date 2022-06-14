package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.SubmitInspectionDate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

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
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_MAIN_FUNCTION, AuditTrailConsts.FUNCTION_SUBMIT_PRE_INSPECT_DATE);
    }


    /**
     * @AutoStep: preLoad
     * @param:
     * @return:
     * @author: yichen
     */
    public void preLoad(BaseProcessClass bpc){
        HttpServletRequest servletRequest = bpc.request;

        ParamUtil.setRequestAttr(servletRequest, HcsaAppConst.DASHBOARDTITLE,"Indicate Preferred Inspection Date");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "AppSubmissionDto");
        if(appSubmissionDto == null){
            return;
        }

        String groupId = appSubmissionDto.getAppGrpId();

        if(StringUtils.isEmpty(groupId)){
            log.info(StringUtil.changeForLog("submit inspection date can not find app group id" + groupId));
            return;
        }

        ApplicationGroupDto group = submitInspectionDate.getApplicationGroupByGroupId(groupId);
        if (group == null){
            log.info("submit inspection date can not find app group ");
            return;
        }

/*        List<ApplicationDto> applicationList = submitInspectionDate.listApplicationByGroupId(groupId);
        Date submitDate = group.getSubmitDt();

        List<HcsaServicePrefInspPeriodDto> afterAppPeriodList = submitInspectionDate.getPrefInspPeriodList(applicationList);
        Date afterApp = submitInspectionDate.getBlockPeriodByAfterApp(submitDate, afterAppPeriodList);

        applicationList = applicationList.stream().filter(i -> !StringUtils.isEmpty(i.getOriginLicenceId())).collect(Collectors.toList());
        List<HcsaServicePrefInspPeriodDto> beforeLicencePeriodList = submitInspectionDate.getPrefInspPeriodList(applicationList);

        Date licenceExpire = submitInspectionDate.getBlockPeriodByBeforeLicenceExpire(submitDate, beforeLicencePeriodList);

        if (afterApp != null){
            ParamUtil.setSessionAttr(servletRequest, "inspStartDate" , IaisEGPHelper.tomorrow(afterApp));
            ParamUtil.setSessionAttr(servletRequest, "inspStartDateDefault" , afterApp);
        }

        if (licenceExpire != null){
            ParamUtil.setSessionAttr(servletRequest, "inspEndDate" , IaisEGPHelper.yesterday(licenceExpire));
            ParamUtil.setSessionAttr(servletRequest, "inspEndDateDefault" , licenceExpire);
        }*/
    }

    /**
     * @AutoStep: validate
     * @param:
     * @return:
     * @author: yichen
     */
    public void validate(BaseProcessClass bpc){
        String startDate = ParamUtil.getString(bpc.request, "inspStartDate");
        String endDate = ParamUtil.getString(bpc.request, "inspEndDate");
        ParamUtil.setRequestAttr(bpc.request, "inspStartDate", startDate);
        ParamUtil.setRequestAttr(bpc.request, "inspEndDate", endDate);

        if (StringUtil.isEmpty(startDate) || StringUtil.isEmpty(endDate)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("dateError", "UC_INSTA004_ERR010"));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        Date sDate = IaisEGPHelper.parseToDate(startDate, AppConsts.DEFAULT_DATE_FORMAT);
        Date eDate = IaisEGPHelper.parseToDate(endDate, AppConsts.DEFAULT_DATE_FORMAT);

        Date currentDate = new Date();
        if (!IaisEGPHelper.isAfterDate(currentDate, sDate)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("dateError", "UC_INSTA004_ERR007"));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        if (sDate != null && eDate != null){
            boolean isAfterDate = IaisEGPHelper.isAfterDateSecond(sDate, eDate);
            if (!isAfterDate){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("inspStartDate", "UC_INSP_ACK019"));
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
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "AppSubmissionDto");
        String groupId = appSubmissionDto.getAppGrpId();

        Date sDate = (Date) ParamUtil.getRequestAttr(bpc.request, "inspStartDate");
        Date eDate =  (Date)  ParamUtil.getRequestAttr(bpc.request,"inspEndDate");

        submitInspectionDate.submitInspStartDateAndEndDate(groupId, sDate, eDate);
        ParamUtil.setRequestAttr(bpc.request, "ackMsg", MessageUtil.getMessageDesc("NEW_ACK014"));

    }
}
