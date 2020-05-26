package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.PublicHolidayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;


/*
 *File Name: MessageDelegator
 *Creator: guyin
 *Creation time:2019/12/26 19:08
 *Describe:
 */

@Delegator(value = "publicHolidayDelegate")
@Slf4j
public class PublicHolidayDelegate {
    @Autowired
    PublicHolidayService publicHolidayService;

    private SearchParam holidaySearchParam ;
    /**
     * doStart
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction("public holiday", "public holiday");
        initSearchParam();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        holidaySearchParam.addFilter("year", "%" + year + "%",true);
    }

    private void initSearchParam(){
        holidaySearchParam = new SearchParam(PublicHolidayQueryDto.class.getName());
        holidaySearchParam.setPageSize(10);
        holidaySearchParam.setPageNo(1);
        holidaySearchParam.setSort("ID", SearchParam.ASCENDING);
    }

    /**
     * doPrepare
     * @param bpc
     */
    public void doPrepare(BaseProcessClass bpc){
        if(holidaySearchParam == null){
            initSearchParam();
        }
        QueryHelp.setMainSql("systemAdmin", "getHolidayList", holidaySearchParam);
        SearchResult<PublicHolidayQueryDto> HolidaySearchResult = publicHolidayService.getHoliday(holidaySearchParam);

        ParamUtil.setRequestAttr(bpc.request,"HolidaySearchResult",HolidaySearchResult);
        ParamUtil.setRequestAttr(bpc.request,"holidaySearchParam",holidaySearchParam);
        statusOption(bpc);
        yearOption(bpc,false);

    }

    /**
     * doSwitch
     * @param bpc
     */
    public void doSwitch(BaseProcessClass bpc){

    }

    /**
     * doEdit
     * @param bpc
     */
    public void doEdit(BaseProcessClass bpc) throws ParseException {
        log.debug(StringUtil.changeForLog("Public holiday edit ...."));
        String holidayId = ParamUtil.getMaskedString(bpc.request,"holidayId");
        PublicHolidayDto publicHolidayDto = publicHolidayService.getHolidayById(holidayId);
        ParamUtil.setRequestAttr(bpc.request,"holiday",publicHolidayDto);
        statusOption(bpc);
        yearOption(bpc,true);
    }

    /**
     * doEditValidation
     * @param bpc
     */
    public void doEditValidation(BaseProcessClass bpc) throws ParseException {
        PublicHolidayDto publicHolidayDto = new PublicHolidayDto();
        publicHolidayDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        publicHolidayDto.setDescription(ParamUtil.getRequestString(bpc.request,"description"));
        publicHolidayDto.setId(ParamUtil.getRequestString(bpc.request,"holidayId"));

        Date fromDate = Formatter.parseDate(ParamUtil.getString(bpc.request, "sub_date"));
        publicHolidayDto.setFromDate(fromDate);

        Date todate= new  Date();
        Calendar   calendar = Calendar.getInstance();
        calendar.setTime(fromDate);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        todate=calendar.getTime();

        publicHolidayDto.setToDate(todate);

        publicHolidayDto.setStatus(ParamUtil.getString(bpc.request, "status"));
        PublicHolidayDto resDto = publicHolidayService.updateHoliday(publicHolidayDto);
        if(resDto != null){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID,"true");
        }else{
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID,"false");
        }
    }

    /**
     * doUpload
     * @param bpc
     */
    public void doUpload(BaseProcessClass bpc){
    }

    /**
     * doCreate
     * @param bpc
     */
    public void doCreate(BaseProcessClass bpc){

        statusOption(bpc);
        yearOption(bpc,true);
    }

    /**
     * doDelete
     * @param bpc
     */
    public void doDelete(BaseProcessClass bpc){
        String[] id = ParamUtil.getMaskedStrings(bpc.request,"deleteId");
        List<String> holidayIds = IaisCommonUtils.genNewArrayList();
        for (String item:id
             ) {
            holidayIds.add(item);
        }
        publicHolidayService.deleteHoliday(holidayIds);
    }

    /**
     * doSearch
     * @param bpc
     */
    public void doSearch(BaseProcessClass bpc){
        initSearchParam();
        String description = ParamUtil.getRequestString(bpc.request,"des");
        String year = ParamUtil.getRequestString(bpc.request,"year");
        String nonWorking = ParamUtil.getRequestString(bpc.request,"nonWorking");
        String status = ParamUtil.getRequestString(bpc.request,"searchStatus");
        if(description != null && !StringUtil.isEmpty(description)){
            holidaySearchParam.addFilter("description", "%" + description + "%",true);
        }
        if(year != null && !StringUtil.isEmpty(year)){
            holidaySearchParam.addFilter("year", "%" + year + "%",true);
        }
        if(nonWorking != null && !StringUtil.isEmpty(nonWorking)){
            holidaySearchParam.addFilter("nonWorking", nonWorking,true);
        }
        if(status != null && !StringUtil.isEmpty(status)){
            holidaySearchParam.addFilter("status",  status,true);
        }
    }

    public void searchPage(BaseProcessClass bpc){
        CrudHelper.doPaging(holidaySearchParam,bpc.request);
    }

    /**
     * doEditValidation
     * @param bpc
     */
    public void doCreateValidation(BaseProcessClass bpc) throws ParseException {
        PublicHolidayDto publicHolidayDto = new PublicHolidayDto();
        publicHolidayDto.setDescription(ParamUtil.getRequestString(bpc.request,"Description"));
        Date fromDate = Formatter.parseDate(ParamUtil.getString(bpc.request, "sub_date"));
        publicHolidayDto.setFromDate(fromDate);

        Date todate= new  Date();
        Calendar   calendar = Calendar.getInstance();
        calendar.setTime(fromDate);
        calendar.add(Calendar.DAY_OF_MONTH,1);
        todate=calendar.getTime();

        publicHolidayDto.setToDate(todate);
        publicHolidayDto.setStatus(ParamUtil.getString(bpc.request, "status"));

        ValidationResult validationResult = WebValidationHelper.validateEntity(publicHolidayDto);
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID,IntranetUserConstant.FALSE);
        }else{
            publicHolidayDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            PublicHolidayDto resDto = publicHolidayService.createHoliday(publicHolidayDto);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID,IntranetUserConstant.TRUE);
        }

    }


    private void yearOption(BaseProcessClass bpc,boolean isBig){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int count = cal.get(Calendar.YEAR);
        List<SelectOption> selectOptionList = IaisCommonUtils.genNewArrayList();
        if(isBig){
            count = count + 5;
        }else{
            year = year - 5;
        }
        for (;count >= year;count --) {
            selectOptionList.add(new SelectOption(Integer.toString(count), Integer.toString(count)));
        }
        ParamUtil.setRequestAttr(bpc.request,"yearOption",selectOptionList);
    }

    private  void statusOption(BaseProcessClass bpc){
        String[] status = new String[]{
                AppConsts.COMMON_STATUS_ACTIVE,
                AppConsts.COMMON_STATUS_IACTIVE
        };
        List<SelectOption> selectOptions =  MasterCodeUtil.retrieveOptionsByCodes(status);
        ParamUtil.setRequestAttr(bpc.request,"statusOption",selectOptions);
    }

}
