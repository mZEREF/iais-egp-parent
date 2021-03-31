package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschedulingOfficerDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschedulingOfficerQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.mask.MaskAttackException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import com.ecquaria.cloud.moh.iais.service.OfficersReSchedulingService;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
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
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION,  AuditTrailConsts.FUNCTION_RESCHEDULE);
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
        ParamUtil.setSessionAttr(bpc.request, "workGroupOption", null);
        ParamUtil.setSessionAttr(bpc.request, "reschedulingOfficerDto", null);
        ParamUtil.setSessionAttr(bpc.request, "inspReSchSearchResult", null);
        ParamUtil.setSessionAttr(bpc.request, "inspReSchSearchParam", null);
        ParamUtil.setSessionAttr(bpc.request, "workGroupNos", null);
        ParamUtil.setSessionAttr(bpc.request, "applicationDto", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectorOption1", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectorOption2", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectorOption3", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectorOption4", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectorOption5", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectorOption6", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectorOption7", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectorOption8", null);
    }

    /**
     * StartStep: mohOfficerReSchedulingPer
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingPer(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingPer start ...."));
        ReschedulingOfficerDto reschedulingOfficerDto = (ReschedulingOfficerDto)ParamUtil.getSessionAttr(bpc.request, "reschedulingOfficerDto");
        SearchParam searchParam = getSearchParam(bpc);
        SearchResult<ReschedulingOfficerQueryDto> searchResult = (SearchResult) ParamUtil.getSessionAttr(bpc.request, "inspReSchSearchResult");
        if(searchResult == null){
            LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
            reschedulingOfficerDto = new ReschedulingOfficerDto();
            //get all inspection work group
            List<SelectOption> workGroupOption = officersReSchedulingService.getInspWorkGroupByLogin(loginContext, reschedulingOfficerDto);
            if(!IaisCommonUtils.isEmpty(workGroupOption)) {
                //get a work group to show
                reschedulingOfficerDto.setWorkGroupCheck(workGroupOption.get(0).getValue());
            }
            //get all inspector by work group list
            List<String> workGroupNos = officersReSchedulingService.allInspectorFromGroupList(reschedulingOfficerDto, workGroupOption);
            //set session inspector options
            Map<String, List<SelectOption>> inspectorByGroup = reschedulingOfficerDto.getInspectorByGroup();
            if(inspectorByGroup != null){
                for(Map.Entry<String, List<SelectOption>> map : inspectorByGroup.entrySet()){
                    String groupNo = map.getKey();
                    List<SelectOption> inspectorOption = map.getValue();
                    ParamUtil.setSessionAttr(bpc.request, "inspectorOption" + groupNo, (Serializable) inspectorOption);
                }
            }
            //get Work Group Check and it's inspector, get appNo by inspector and some Filter conditions
            List<String> appNoList = officersReSchedulingService.getAppNoByInspectorAndConditions(reschedulingOfficerDto);
            searchParam = getSearchParamByFilter(searchParam, appNoList);
            QueryHelp.setMainSql("inspectionQuery", "reschedulingSearch",searchParam);
            searchResult = officersReSchedulingService.getOfficersSearch(searchParam);
            searchResult = officersReSchedulingService.setInspectorsAndServices(searchResult, reschedulingOfficerDto);
            ParamUtil.setSessionAttr(bpc.request, "workGroupOption", (Serializable) workGroupOption);
            ParamUtil.setSessionAttr(bpc.request, "reschedulingOfficerDto", reschedulingOfficerDto);
            ParamUtil.setSessionAttr(bpc.request, "workGroupNos", (Serializable) workGroupNos);
        }
        ParamUtil.setSessionAttr(bpc.request, "inspReSchSearchResult", searchResult);
        ParamUtil.setSessionAttr(bpc.request, "inspReSchSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "reschedulingOfficerDto", reschedulingOfficerDto);
    }

    private SearchParam getSearchParamByFilter(SearchParam searchParam, List<String> appNoList) {
        appNoList = removeNull(appNoList);
        if(!IaisCommonUtils.isEmpty(appNoList)) {
            StringBuilder sb = new StringBuilder("(");
            for (int i = 0; i < appNoList.size(); i++) {
                sb.append(":appNo")
                        .append(i)
                        .append(',');
            }
            String inSql = sb.substring(0, sb.length() - 1) + ")";
            searchParam.addParam("application_no", inSql);
            for (int i = 0; i < appNoList.size(); i++) {
                searchParam.addFilter("appNo" + i, appNoList.get(i));
            }
        } else {
            searchParam.addFilter("appNo", "0",true);
        }
        return searchParam;
    }

    private List<String> removeNull(List<String> appNoList) {
        for (int i = 0; i < appNoList.size(); i++) {
            if(StringUtil.isEmpty(appNoList.get(i))){
                appNoList.remove(i);
                i--;
            }
        }
        return appNoList;
    }

    /**
     * StartStep: mohOfficerReSchedulingStep
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingStep start ...."));
        ReschedulingOfficerDto reschedulingOfficerDto = (ReschedulingOfficerDto)ParamUtil.getSessionAttr(bpc.request, "reschedulingOfficerDto");
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        if("assign".equals(actionValue) || "audit".equals(actionValue)) {
            String applicationNo = "";
            try{
                applicationNo = ParamUtil.getMaskedString(bpc.request, "applicationNo");
            }catch (MaskAttackException e){
                log.error(e.getMessage(), e);
                try{
                    bpc.response.sendRedirect("https://"+bpc.request.getServerName()+"/hcsa-licence-web/CsrfErrorPage.jsp");
                } catch (IOException ioe){
                    log.error(ioe.getMessage(), ioe);
                    return;
                }
            }
            reschedulingOfficerDto.setAssignNo(applicationNo);
            reschedulingOfficerDto.setNewInspDates(null);
        }
        ParamUtil.setSessionAttr(bpc.request, "reschedulingOfficerDto", reschedulingOfficerDto);
    }

    /**
     * StartStep: mohOfficerReSchedulingSearch
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingSearch start ...."));
        SearchParam searchParam = getSearchParam(bpc, true);
        ReschedulingOfficerDto reschedulingOfficerDto = (ReschedulingOfficerDto)ParamUtil.getSessionAttr(bpc.request, "reschedulingOfficerDto");
        String workGroupCheck = ParamUtil.getRequestString(bpc.request, "reSchInspWorkGroup");
        String inspectorCheck = ParamUtil.getRequestString(bpc.request, "inspectorName" + workGroupCheck);
        List<String> appNoList = officersReSchedulingService.appNoListByGroupAndUserCheck(reschedulingOfficerDto, workGroupCheck, inspectorCheck);
        searchParam = getSearchParamByFilter(searchParam, appNoList);
        reschedulingOfficerDto.setInspectorCheck(inspectorCheck);
        reschedulingOfficerDto.setWorkGroupCheck(workGroupCheck);
        ParamUtil.setSessionAttr(bpc.request, "inspReSchSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "reschedulingOfficerDto", reschedulingOfficerDto);
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
        CrudHelper.doPaging(searchParam, bpc.request);
    }

    /**
     * StartStep: mohOfficerReSchedulingQuery
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingQuery(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingQuery start ...."));
        SearchParam searchParam = getSearchParam(bpc);
        ReschedulingOfficerDto reschedulingOfficerDto = (ReschedulingOfficerDto)ParamUtil.getSessionAttr(bpc.request, "reschedulingOfficerDto");
        QueryHelp.setMainSql("inspectionQuery", "reschedulingSearch", searchParam);
        SearchResult<ReschedulingOfficerQueryDto> searchResult = officersReSchedulingService.getOfficersSearch(searchParam);
        searchResult = officersReSchedulingService.setInspectorsAndServices(searchResult, reschedulingOfficerDto);

        ParamUtil.setSessionAttr(bpc.request, "inspReSchSearchResult", searchResult);
        ParamUtil.setSessionAttr(bpc.request, "inspReSchSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "reschedulingOfficerDto", reschedulingOfficerDto);
    }

    /**
     * StartStep: mohOfficerReSchedulingInsp
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingInsp(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingInsp start ...."));
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        ReschedulingOfficerDto reschedulingOfficerDto = (ReschedulingOfficerDto)ParamUtil.getSessionAttr(bpc.request, "reschedulingOfficerDto");
        //get inspector and new date
        reschedulingOfficerDto.setCurUserId(loginContext.getUserId());
        List<ApptAppInfoShowDto> apptReSchAppInfoShowDtos = officersReSchedulingService.getReScheduleNewDateInfo(reschedulingOfficerDto);
        ParamUtil.setSessionAttr(bpc.request, "reschedulingOfficerDto", reschedulingOfficerDto);
        ParamUtil.setSessionAttr(bpc.request, "apptReSchAppInfoShowDtos", (Serializable) apptReSchAppInfoShowDtos);
    }

    /**
     * StartStep: mohOfficerReSchedInspDo
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedInspDo(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedInspDo start ...."));
        ReschedulingOfficerDto reschedulingOfficerDto = (ReschedulingOfficerDto)ParamUtil.getSessionAttr(bpc.request, "reschedulingOfficerDto");
        List<ApptAppInfoShowDto> apptReSchAppInfoShowDtos = (List<ApptAppInfoShowDto>)ParamUtil.getSessionAttr(bpc.request, "apptReSchAppInfoShowDtos");
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        if(InspectionConstants.SWITCH_ACTION_YES.equals(actionValue)) {
            officersReSchedulingService.sendEmailToApplicant(reschedulingOfficerDto);
            ParamUtil.setRequestAttr(bpc.request, "reScheduleFail", "reScheduleSuccess");
        } else if (InspectionConstants.SWITCH_ACTION_SUCCESS.equals(actionValue)) {
            String appStatusFlag = officersReSchedulingService.changeInspectorAndDate(reschedulingOfficerDto, apptReSchAppInfoShowDtos);
            ParamUtil.setRequestAttr(bpc.request, "appStatusFlag", appStatusFlag);
            ParamUtil.setRequestAttr(bpc.request, "reScheduleSuccess", "reScheduleSuccess");
        } else if (InspectionConstants.SWITCH_ACTION_AGAIN.equals(actionValue)) {
            reschedulingOfficerDto.setNewInspDates(null);
        } else {
            apptReSchAppInfoShowDtos = null;
        }
        ParamUtil.setSessionAttr(bpc.request, "reschedulingOfficerDto", reschedulingOfficerDto);
        ParamUtil.setSessionAttr(bpc.request, "apptReSchAppInfoShowDtos", (Serializable) apptReSchAppInfoShowDtos);
    }

    /**
     * StartStep: mohOfficerReSchedInspAgain
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedInspAgain(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedInspAgain start ...."));
        ReschedulingOfficerDto reschedulingOfficerDto = (ReschedulingOfficerDto)ParamUtil.getSessionAttr(bpc.request, "reschedulingOfficerDto");
        List<ApptAppInfoShowDto> apptReSchAppInfoShowDtos = (List<ApptAppInfoShowDto>)ParamUtil.getSessionAttr(bpc.request, "apptReSchAppInfoShowDtos");
        apptReSchAppInfoShowDtos = officersReSchedulingService.setInfoByDateAndUserIdToSave(apptReSchAppInfoShowDtos, reschedulingOfficerDto);
        ParamUtil.setSessionAttr(bpc.request, "reschedulingOfficerDto", reschedulingOfficerDto);
        ParamUtil.setSessionAttr(bpc.request, "apptReSchAppInfoShowDtos", (Serializable) apptReSchAppInfoShowDtos);
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
        ReschedulingOfficerDto reschedulingOfficerDto = (ReschedulingOfficerDto)ParamUtil.getSessionAttr(bpc.request, "reschedulingOfficerDto");
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        String specificStartDate = ParamUtil.getDate(bpc.request, "specificStartDate");
        String specificEndDate = ParamUtil.getDate(bpc.request, "specificEndDate");
        String startHours = ParamUtil.getRequestString(bpc.request, "startHours");
        String endHours = ParamUtil.getRequestString(bpc.request, "endHours");
        List<SelectOption> hoursOption = (List<SelectOption>)ParamUtil.getSessionAttr(bpc.request, "hoursOption");
        List<SelectOption> endHoursOption = (List<SelectOption>)ParamUtil.getSessionAttr(bpc.request, "endHoursOption");

        //combination date
        Date startDate = getSpecificDate(specificStartDate, hoursOption, startHours);
        Date endDate = getSpecificDate(specificEndDate, endHoursOption, endHours);
        //set date do first validate not null
        if(containValueInList(startHours, hoursOption)){
            reschedulingOfficerDto.setStartHours(startHours);
        } else {
            reschedulingOfficerDto.setStartHours(null);
        }
        if(containValueInList(endHours, endHoursOption)){
            reschedulingOfficerDto.setEndHours(endHours);
        } else {
            reschedulingOfficerDto.setEndHours(null);
        }
        if(startDate != null){
            reschedulingOfficerDto.setSpecificStartDate(startDate);
        }
        if(endDate != null){
            reschedulingOfficerDto.setSpecificEndDate(endDate);
        }
        if(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue)) {
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        } else {
            ValidationResult validationResult = WebValidationHelper.validateProperty(reschedulingOfficerDto, "reschedule");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
            } else {
                //second validate
                Map<String, String> errMap = null;
                if(startDate != null && endDate != null && endDate.before(startDate)){
                    errMap = IaisCommonUtils.genNewHashMap();
                    errMap.put("specificDate", "UC_INSP_ERR0007");
                } else {
                    AppointmentDto appointmentDto = officersReSchedulingService.getInspDateValidateData(reschedulingOfficerDto);
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
                    reschedulingOfficerDto.setAppointmentDto(appointmentDto);
                }
                if(errMap != null) {
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                    ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
                } else {
                    ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.TRUE);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "reschedulingOfficerDto", reschedulingOfficerDto);
    }

    private boolean containValueInList(String str, List<SelectOption> optionList) {
        if(!StringUtil.isEmpty(str) && !IaisCommonUtils.isEmpty(optionList)){
            for(SelectOption so : optionList){
                if(str.equals(so.getValue())){
                    return true;
                }
            }
        }
        return false;
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
     * StartStep: mohOfficerReSchedulingAuDo
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingAuDo(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingAuDo start ...."));
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        ReschedulingOfficerDto reschedulingOfficerDto = (ReschedulingOfficerDto)ParamUtil.getSessionAttr(bpc.request, "reschedulingOfficerDto");
        reschedulingOfficerDto.setCurUserId(loginContext.getUserId());
        String applicationNo = reschedulingOfficerDto.getAssignNo();
        ApplicationDto applicationDto = officersReSchedulingService.getApplicationByAppNo(applicationNo);
        officersReSchedulingService.reScheduleRoutingAudit(reschedulingOfficerDto);
        ParamUtil.setSessionAttr(bpc.request, "reschedulingOfficerDto", reschedulingOfficerDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationDto", applicationDto);
    }

    /**
     * StartStep: mohOfficerReSchedulingSuccess
     *
     * @param bpc
     * @throws
     */
    public void mohOfficerReSchedulingSuccess(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the mohOfficerReSchedulingSuccess start ...."));
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        ReschedulingOfficerDto reschedulingOfficerDto = (ReschedulingOfficerDto)ParamUtil.getSessionAttr(bpc.request, "reschedulingOfficerDto");
        reschedulingOfficerDto.setCurUserId(loginContext.getUserId());
        String applicationNo = reschedulingOfficerDto.getAssignNo();
        ApplicationDto applicationDto = officersReSchedulingService.getApplicationByAppNo(applicationNo);
        ParamUtil.setSessionAttr(bpc.request, "reschedulingOfficerDto", reschedulingOfficerDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationDto", applicationDto);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false);
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, "inspReSchSearchParam");
        int pageSize = SystemParamUtil.getDefaultPageSize();
        if(searchParam == null || isNew){
            searchParam = new SearchParam(ReschedulingOfficerQueryDto.class.getName());
            searchParam.setPageSize(pageSize);
            searchParam.setPageNo(1);
            searchParam.setSort("ID", SearchParam.ASCENDING);
        }
        return searchParam;
    }
}
