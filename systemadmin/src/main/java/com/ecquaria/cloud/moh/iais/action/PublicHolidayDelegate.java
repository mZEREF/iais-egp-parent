package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.impl.PublicHolidayServiceImpl;
import java.text.ParseException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;


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
    PublicHolidayServiceImpl publicHolidayService;

    private SearchParam holidaySearchParam = new SearchParam(PublicHolidayQueryDto.class.getName());
    /**
     * doStart
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction("public holiday", "public holiday");
        holidaySearchParam.setPageSize(10);
        holidaySearchParam.setPageNo(1);
        holidaySearchParam.setSort("ID", SearchParam.ASCENDING);
        ParamUtil.setRequestAttr(bpc.request,"descriptionSwitch",null);
        ParamUtil.setRequestAttr(bpc.request,"yearSwitch",null);
    }

    /**
     * doPrepare
     * @param bpc
     */
    public void doPrepare(BaseProcessClass bpc){
        QueryHelp.setMainSql("systemAdmin", "getHolidayList", holidaySearchParam);
        List<PublicHolidayQueryDto> publicHolidayDtoList = publicHolidayService.getHoliday(holidaySearchParam).getRows();

        Calendar cal = Calendar.getInstance();

        String []arr = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        List<Map<String,String>> holidayList = IaisCommonUtils.genNewArrayList();
        for (PublicHolidayQueryDto item: publicHolidayDtoList
             ) {
            Map<String, String> holidayMap = new HashMap<String,String>();
            if(item.getFromDate().equals(item.getToDate())){
                cal.setTime(item.getFromDate());
                holidayMap.put("week",arr[cal.get(Calendar.DAY_OF_WEEK)-1]);
                holidayMap.put("date",Formatter.formatDate(item.getFromDate()));
            }else{
                cal.setTime(item.getFromDate());
                String fromweek = arr[cal.get(Calendar.DAY_OF_WEEK)-1];
                cal.setTime(item.getToDate());
                String toweek = arr[cal.get(Calendar.DAY_OF_WEEK)-1];
                holidayMap.put("week",fromweek + "-" + toweek);
                holidayMap.put("date",Formatter.formatDate(item.getFromDate()) + "-" + Formatter.formatDate(item.getToDate()));
            }
            holidayMap.put("description",item.getDescription());
            holidayMap.put("id",item.getId());
            holidayMap.put("sub_date",Formatter.formatDate(item.getFromDate()));
            holidayMap.put("to_date",Formatter.formatDate(item.getToDate()));
            holidayList.add(holidayMap);
        }
        ParamUtil.setRequestAttr(bpc.request,"holidayList",holidayList);

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
        ParamUtil.setRequestAttr(bpc.request,"description",ParamUtil.getRequestString(bpc.request,"des"));
        ParamUtil.setRequestAttr(bpc.request,"holidayId",ParamUtil.getRequestString(bpc.request,"holidayId"));
        ParamUtil.setRequestAttr(bpc.request,"sub_date",Formatter.parseDate(ParamUtil.getString(bpc.request, "sub_date")));
        ParamUtil.setRequestAttr(bpc.request,"to_date",Formatter.parseDate(ParamUtil.getString(bpc.request, "to_date")));
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
        publicHolidayDto.setFromDate(Formatter.parseDate(ParamUtil.getString(bpc.request, "sub_date")));
        publicHolidayDto.setToDate(Formatter.parseDate(ParamUtil.getString(bpc.request, "to_date")));
        publicHolidayDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
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

    }

    /**
     * doDelete
     * @param bpc
     */
    public void doDelete(BaseProcessClass bpc){
        String id = ParamUtil.getRequestString(bpc.request,"holidayId");
        publicHolidayService.deleteHoliday(id);
    }

    /**
     * doSearch
     * @param bpc
     */
    public void doSearch(BaseProcessClass bpc){
        String descriptionSwitch = ParamUtil.getRequestString(bpc.request,"descriptionSwitch");
        String yearSwitch = ParamUtil.getRequestString(bpc.request,"yearSwitch");
        holidaySearchParam.getParams().clear();
        holidaySearchParam.getFilters().clear();
        holidaySearchParam.setPageNo(1);
        if(!StringUtil.isEmpty(descriptionSwitch)){
            holidaySearchParam.addFilter("description", "%" + descriptionSwitch + "%",true);
        }
        if(!StringUtil.isEmpty(yearSwitch)){
            holidaySearchParam.addFilter("year", "%" + yearSwitch + "%",true);
        }

        ParamUtil.setRequestAttr(bpc.request,"descriptionSwitch",descriptionSwitch);
        ParamUtil.setRequestAttr(bpc.request,"yearSwitch",yearSwitch);
    }

    /**
     * doEditValidation
     * @param bpc
     */
    public void doCreateValidation(BaseProcessClass bpc) throws ParseException {
        PublicHolidayDto publicHolidayDto = new PublicHolidayDto();
        publicHolidayDto.setDescription(ParamUtil.getRequestString(bpc.request,"Description"));
        publicHolidayDto.setFromDate(Formatter.parseDate(ParamUtil.getString(bpc.request, "sub_date")));
        publicHolidayDto.setToDate(Formatter.parseDate(ParamUtil.getString(bpc.request, "to_date")));
        publicHolidayDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);

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


}
