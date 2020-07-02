package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import com.ecquaria.cloud.moh.iais.service.OfficersReSchedulingService;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Process: MohOfficerReScheduling
 *
 * @author Shicheng
 * @date 2020/6/24 17:48
 **/
@Delegator("mohOfficerReSchedulingDelegator")
@Slf4j
public class OfficersReSchedulingDelegator {

    @Autowired
    private OfficersReSchedulingService officersReSchedulingService;

    @Autowired
    private AppointmentClient appointmentClient;

    @Autowired
    private ApptInspectionDateService apptInspectionDateService;

    @Autowired
    private OfficersReSchedulingDelegator(OfficersReSchedulingService officersReSchedulingService, ApptInspectionDateService apptInspectionDateService){
        this.officersReSchedulingService = officersReSchedulingService;
        this.apptInspectionDateService = apptInspectionDateService;
    }

    /**
     * StartStep: mohOfficerReSchedulingStart
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingStart start ...."));
        AuditTrailHelper.auditFunction("Moh Officer Rescheduling", "Moh Officer Rescheduling");
    }

    /**
     * StartStep: mohOfficerReSchedulingInit
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "hoursOption", null);
        ParamUtil.setSessionAttr(bpc.request, "endHoursOption", null);
    }

    /**
     * StartStep: mohOfficerReSchedulingPer
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingPer(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingPer start ...."));
        SearchParam searchParam = getSearchParam(bpc);
    }

    /**
     * StartStep: mohOfficerReSchedulingStep
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingStep start ...."));
    }

    /**
     * StartStep: mohOfficerReSchedulingSearch
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingSearch start ...."));

    }

    /**
     * StartStep: mohOfficerReSchedulingSort
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingSort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingSort start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doSorting(searchParam, bpc.request);
    }

    /**
     * StartStep: mohOfficerReSchedulingPage
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingPage start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * StartStep: mohOfficerReSchedulingQuery
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingQuery(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingQuery start ...."));
    }

    /**
     * StartStep: mohOfficerReSchedulingInsp
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingInsp(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingInsp start ...."));
    }

    /**
     * StartStep: mohOfficerReSchedulingAudit
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingAudit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingAudit start ...."));
        List<SelectOption> hours = apptInspectionDateService.getInspectionDateHours();
        List<SelectOption> endHours = apptInspectionDateService.getInspectionDateEndHours();
        ParamUtil.setSessionAttr(bpc.request, "hoursOption", (Serializable) hours);
        ParamUtil.setSessionAttr(bpc.request, "endHoursOption", (Serializable) endHours);
    }

    /**
     * StartStep: mohOfficerReSchedulingVali
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingVali start ...."));
        String specificStartDate = ParamUtil.getDate(bpc.request, "specificStartDate");
        String specificEndDate = ParamUtil.getDate(bpc.request, "specificEndDate");
        String startHours = ParamUtil.getRequestString(bpc.request, "startHours");
        String endHours = ParamUtil.getRequestString(bpc.request, "endHours");
        List<SelectOption> hoursOption = (List<SelectOption>)ParamUtil.getSessionAttr(bpc.request, "hoursOption");
        List<SelectOption> endHoursOption = (List<SelectOption>)ParamUtil.getSessionAttr(bpc.request, "endHoursOption");
        AppointmentDto appointmentDto = new AppointmentDto();
        Date startDate = getSpecificDate(specificStartDate, hoursOption, startHours);
        Date endDate = getSpecificDate(specificEndDate, endHoursOption, endHours);
        Map<String, String> errMap = null;
        if(endDate.before(startDate)){
            errMap = IaisCommonUtils.genNewHashMap();
            errMap.put("specificDate", "UC_INSP_ERR0007");
        } else {
            //todo get other info
            if (startDate != null) {
                appointmentDto.setStartDate(Formatter.formatDateTime(startDate, AppConsts.DEFAULT_DATE_TIME_FORMAT));
            }
            if (endDate != null) {
                appointmentDto.setEndDate(Formatter.formatDateTime(endDate, AppConsts.DEFAULT_DATE_TIME_FORMAT));
            }
            try {
                appointmentClient.validateUserCalendar(appointmentDto).getStatusCode();
            } catch (Exception e) {
                errMap = IaisCommonUtils.genNewHashMap();
                errMap.put("specificDate", "UC_INSP_ERR0007");
            }
        }
        if(errMap != null) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
        } else {
            ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.TRUE);
        }
    }

    private Date getSpecificDate(String specificDate1, List<SelectOption> hoursOption, String hours) {
        if(specificDate1 != null) {
            Date specificDate = null;
            SimpleDateFormat sdf = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);
            Date sub_date1 = null;
            try {
                sub_date1 = sdf.parse(specificDate1);
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            StringBuilder subDate = new StringBuilder();
            subDate.append(sdf2.format(sub_date1)) ;
            if(!StringUtil.isEmpty(hours)) {
                for(SelectOption so : hoursOption){
                    if(hours.equals(so.getValue())){
                        subDate.append(' ').append(so.getText()).append(":00");
                    }
                }
            } else {
                subDate .append(' ').append(":00:00");
            }
            try {
                specificDate = sdf3.parse(subDate.toString());
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
            return specificDate;
        }
        return null;
    }

    /**
     * StartStep: mohOfficerReSchedulingSuccess
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingSuccess start ...."));
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "inspReSchSearchParam");
        if(searchParam == null || isNew){
            //todo query dto
            searchParam = new SearchParam(InspectionCommonPoolQueryDto.class.getName());
            searchParam.setPageSize(10);
            searchParam.setPageNo(1);
            //todo query dto
            searchParam.setSort("GROUP_NO", SearchParam.ASCENDING);
        }
        return searchParam;
    }
}
