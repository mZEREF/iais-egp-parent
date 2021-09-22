package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.MasterCodePair;
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
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.PublicHolidayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
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
    @Autowired
    SystemParamConfig systemParamConfig;

    /**
     * doStart
     * @param bpc
     */
    public void doStart(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG, AuditTrailConsts.FUNCTION_PUBLIC_HOLIDAY);
    }

    /**
     * initial
     * @param bpc
     */
    public void initial(BaseProcessClass bpc){
        initialSearchParam(bpc);
    }

    private void initialSearchParam(BaseProcessClass bpc){
        ParamUtil.setRequestAttr(bpc.request,"yearErr",null);
        ParamUtil.setSessionAttr(bpc.request,"year",null);
        ParamUtil.setSessionAttr(bpc.request,"phCode",null);
        ParamUtil.setSessionAttr(bpc.request,"nonWorking",null);
        ParamUtil.setSessionAttr(bpc.request,"searchStatus",null);

        SearchParam holidaySearchParam = new SearchParam(PublicHolidayQueryDto.class.getName());
        holidaySearchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
        holidaySearchParam.setPageNo(1);
        holidaySearchParam.setSort("FROM_DATE", SearchParam.DESCENDING);
        ParamUtil.setSessionAttr(bpc.request,"holidaySearchParam",holidaySearchParam);
    }

    private SearchParam getSearchParam(HttpServletRequest request){
        SearchParam holidaySearchParam = (SearchParam) ParamUtil.getSessionAttr(request, "holidaySearchParam");
        return holidaySearchParam;
    }

    private void setSearchParam(HttpServletRequest request, SearchParam searchParam){
        ParamUtil.setSessionAttr(request,"holidaySearchParam",searchParam);
    }

    /**
     * doPrepare
     * @param bpc
     */
    public void doPrepare(BaseProcessClass bpc){
        SearchParam holidaySearchParam = getSearchParam(bpc.request);
        //search
        QueryHelp.setMainSql("systemAdmin", "getHolidayList", holidaySearchParam);
        //set public holiday description sort data
        List<SelectOption> masterCodes = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PUBLIC_HOLIDAY);
        MasterCodePair masterCodePair = new MasterCodePair("PH_CODE", "PH_DESC", masterCodes);
        holidaySearchParam.setMasterCode(masterCodePair);
        //run sql
        SearchResult<PublicHolidayQueryDto> HolidaySearchResult = publicHolidayService.getHoliday(holidaySearchParam);

        ParamUtil.setRequestAttr(bpc.request,"HolidaySearchResult",HolidaySearchResult);

        statusOption(bpc);
        yearOption(bpc,false);

    }

    /**
     * doSwitch
     * @param bpc
     */
    public void doSwitch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String currentAction = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, currentAction);
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
        Calendar cal = Calendar.getInstance();
        cal.setTime(publicHolidayDto.getFromDate());
        int year = cal.get(Calendar.YEAR);
        ParamUtil.setRequestAttr(bpc.request,"year",String.valueOf(year));
        statusOption(bpc);
        yearOption(bpc,true);
    }

    public void editPrepare(BaseProcessClass bpc) {
        statusOption(bpc);
        yearOption(bpc,true);
    }

    /**
     * doEditValidation
     * @param bpc
     */
    public void doEditValidation(BaseProcessClass bpc) throws ParseException {
        String action = ParamUtil.getString(bpc.request, "action");
        if("back".equals(action)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID,IntranetUserConstant.TRUE);
        }else{
            PublicHolidayDto publicHolidayDto = new PublicHolidayDto();
            publicHolidayDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            publicHolidayDto.setPhCode(ParamUtil.getRequestString(bpc.request,"phCode"));
            String holidayId = ParamUtil.getRequestString(bpc.request,"holidayId");
            publicHolidayDto.setId(holidayId);
            Date fromDate = Formatter.parseDate(ParamUtil.getString(bpc.request, "sub_date"));
            publicHolidayDto.setFromDate(fromDate);
            publicHolidayDto.setStatus(ParamUtil.getString(bpc.request, "status"));

            ParamUtil.setSessionAttr(bpc.request,"holiday",publicHolidayDto);
            ValidationResult validationResult = WebValidationHelper.validateProperty(publicHolidayDto,"create");
            if(validationResult != null && validationResult.isHasErrors()){
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID,IntranetUserConstant.FALSE);

            }else{
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fromDate);
                calendar.add(Calendar.HOUR,23);
                calendar.add(Calendar.MINUTE,59);
                calendar.add(Calendar.SECOND,59);
                Date toDate = calendar.getTime();
                publicHolidayDto.setToDate(toDate);
                publicHolidayDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                publicHolidayService.updateHoliday(publicHolidayDto);
                ParamUtil.setSessionAttr(bpc.request,"holiday",null);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID,IntranetUserConstant.TRUE);
                ParamUtil.setSessionAttr(bpc.request,"year",null);
            }
        }
    }

    /**
     * doUpload
     * @param bpc
     */
    public void doUpload(BaseProcessClass bpc) throws IOException ,ParseException{
        //setfile
        List<String> list = IaisCommonUtils.genNewArrayList();
        List<PublicHolidayDto> publicHolidayDtos = IaisCommonUtils.genNewArrayList();
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        MultipartFile file = mulReq.getFile("selectedFile");
        String filename = file.getOriginalFilename();
        char fileIndex = ".".charAt(0);
        String suffix = filename.substring(filename.lastIndexOf(fileIndex) + 1);
        long size = file.getSize()/1024;
        if(size>5*1024){
            Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
            errMap.put("selectedFile", MessageUtil.getMessageDesc(MessageUtil.replaceMessage("GENERAL_ERR0019","sizeMax","5")));
            ParamUtil.setRequestAttr(bpc.request,"filename", filename);
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        }else if("ics".equals(suffix)){
            try {
                InputStreamReader inputReader = new InputStreamReader(file.getInputStream());
                BufferedReader bf = new BufferedReader(inputReader);
                String str;
                while ((str = bf.readLine()) != null) {
                    list.add(str);
                }
                bf.close();
                inputReader.close();
                int count = 5;
                while (count < list.size() ){
                    String[] begin = list.get(count).split(":");
                    if("BEGIN".equals(begin[0])){
                        PublicHolidayDto publicHolidayDto = new PublicHolidayDto();
                        count ++;
                        String[] start = list.get(count).split(":");
                        count ++;
                        String[] end = list.get(count).split(":");
                        count = count + 6;
                        String[] name = list.get(count).split(":");
                        String phcode = getPublicCode(name[1],publicHolidayDtos);
                        publicHolidayDto.setPhCode(phcode);
                        if(StringUtil.isEmpty(publicHolidayDto.getPhCode())){
                            continue;
                        }
                        publicHolidayDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                        publicHolidayDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                        count ++;
                        int startInt = Integer.parseInt(start[1]);
                        int endInt = Integer.parseInt(end[1]);
                        String nextDay = start[1];
                        while (Integer.parseInt(nextDay) < endInt){
                            DateFormat dft = new SimpleDateFormat("yyyyMMdd");
                            try {
                                Date temp = dft.parse(String.valueOf(startInt));
                                Calendar cld = Calendar.getInstance();
                                cld.setTime(temp);
                                cld.add(Calendar.DATE, 1);
                                temp = cld.getTime();
                                nextDay = dft.format(temp);
                                publicHolidayDto.setFromDate(Formatter.parseDateTime(String.valueOf(startInt), "yyyyMMdd"));

                                Calendar calendar = Calendar.getInstance();
                                calendar.setTime(Formatter.parseDateTime(String.valueOf(startInt), "yyyyMMdd"));
                                calendar.add(Calendar.HOUR,23);
                                calendar.add(Calendar.MINUTE,59);
                                calendar.add(Calendar.SECOND,59);
                                Date todate = calendar.getTime();
                                publicHolidayDto.setToDate(todate);
                                publicHolidayDtos.add(publicHolidayDto);
                                startInt = Integer.parseInt(nextDay);
                            } catch (ParseException e) {
                                log.error(e.getMessage(), e);
                            }
                        }
                    }
                    count ++;
                }
                //set Duplicate date
                List<PublicHolidayDto> duplicateDate = IaisCommonUtils.genNewArrayList();
                //filter holidays(To prevent the repeat)
                List<PublicHolidayDto> allActivePubHolDays = publicHolidayService.getAllActivePubHoliDay();
                publicHolidayDtos = publicHolidayService.filterPreventDays(publicHolidayDtos, allActivePubHolDays, duplicateDate);
                if(!IaisCommonUtils.isEmpty(publicHolidayDtos)) {
                    publicHolidayService.createHolidays(publicHolidayDtos);
                }
                //set Duplicate date message
                if(!IaisCommonUtils.isEmpty(duplicateDate)) {
                    List<String> duplicateDateStrList = publicHolidayService.getDuplicateDateStr(duplicateDate);
                    if(!IaisCommonUtils.isEmpty(duplicateDateStrList)){
                        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
                        errMap.put("selectedFile", MessageUtil.getMessageDesc("OAPPT_ERR016"));
                        ParamUtil.setRequestAttr(bpc.request,"duplicateDateStrList", duplicateDateStrList);
                        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
                    }
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
                errMap.put("selectedFile", MessageUtil.getMessageDesc("OAPPT_ERR009"));
                ParamUtil.setRequestAttr(bpc.request,"filename", filename);
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            }
        }else{
            Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
            errMap.put("selectedFile", MessageUtil.getMessageDesc("OAPPT_ERR010"));
            ParamUtil.setRequestAttr(bpc.request,"filename", filename);
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        }

    }

    private String getPublicCode(String text,List<PublicHolidayDto> publicHolidayDtos){
        if("Chinese New Year".equals(text)){
            for (PublicHolidayDto item:publicHolidayDtos
                 ) {
                if("PUHD012".equals(item.getPhCode())){
                    return "PUHD002";
                }
            }
            return "PUHD012";
        }else{
            List<SelectOption> list = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PUBLIC_HOLIDAY);
            Map<String, String> phCodeMap = IaisCommonUtils.genNewHashMap();
            for (SelectOption item:list
            ) {
                phCodeMap.put(item.getText(),item.getValue());
            }
            return phCodeMap.get(text);
        }

    }

    /**
     * doCreate
     * @param bpc
     */
    public void doCreate(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request,"holiday",null);
        ParamUtil.setSessionAttr(bpc.request,"phCode",null);
        statusOption(bpc);
        yearOption(bpc,true);
    }

    public void createPerpare(BaseProcessClass bpc) {
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
        String year = ParamUtil.getString(bpc.request,"year");
        String action = ParamUtil.getString(bpc.request,"crud_action_type");

        if(StringUtil.isEmpty(year) && "search".equals(action)){
            String yearErr = MessageUtil.replaceMessage("GENERAL_ERR0006","Year","field");
            initialSearchParam(bpc);
            ParamUtil.setRequestAttr(bpc.request,"yearErr",yearErr);
        }else{
            ParamUtil.setRequestAttr(bpc.request,"yearErr",null);

            SearchParam holidaySearchParam = getSearchParam(bpc.request);
            String phCode = ParamUtil.getString(bpc.request,"phCode");
            String nonWorking = ParamUtil.getString(bpc.request,"nonWorking");
            String status = ParamUtil.getString(bpc.request,"searchStatus");
            if(!StringUtil.isEmpty(phCode)){
                holidaySearchParam.addFilter("phCode", phCode,true);
                ParamUtil.setSessionAttr(bpc.request,"phCode",phCode);
            }else{
                holidaySearchParam.removeFilter("phCode");
                ParamUtil.setSessionAttr(bpc.request,"phCode",null);
            }
            if(!StringUtil.isEmpty(year)){
                holidaySearchParam.addFilter("year",  year ,true);
                ParamUtil.setSessionAttr(bpc.request,"year",year);
            }else{
                holidaySearchParam.removeFilter("year");
                ParamUtil.setSessionAttr(bpc.request,"year",null);
            }
            if(!StringUtil.isEmpty(nonWorking)){
                try {
                    Date work = Formatter.parseDate(nonWorking);
                    String workString = Formatter.formatDateTime(work,"yyyy-MM-dd");
                    holidaySearchParam.addFilter("nonWorking", workString,true);
                    ParamUtil.setSessionAttr(bpc.request,"nonWorking",nonWorking);
                }catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }else{
                holidaySearchParam.removeFilter("nonWorking");
                ParamUtil.setSessionAttr(bpc.request,"nonWorking",null);
            }
            if(!StringUtil.isEmpty(status)){
                holidaySearchParam.addFilter("status",  status,true);
                ParamUtil.setSessionAttr(bpc.request,"searchStatus",status);
            }else{
                holidaySearchParam.removeFilter("searchStatus");
                ParamUtil.setSessionAttr(bpc.request,"searchStatus",null);
            }
            setSearchParam(bpc.request,holidaySearchParam);
        }
    }

    public void searchPage(BaseProcessClass bpc){
        SearchParam holidaySearchParam = getSearchParam(bpc.request);
        CrudHelper.doPaging(holidaySearchParam,bpc.request);
        setSearchParam(bpc.request,holidaySearchParam);
    }


    /**
     * AutoStep: sortRecords
     * @param bpc
     * @throws IllegalAccessException
     */
    public void sortRecords(BaseProcessClass bpc){
        SearchParam holidaySearchParam = getSearchParam(bpc.request);
        CrudHelper.doSorting(holidaySearchParam,bpc.request);
        System.out.println("111");
        setSearchParam(bpc.request,holidaySearchParam);
    }



    /**
     * doEditValidation
     * @param bpc
     */
    public void doCreateValidation(BaseProcessClass bpc) throws ParseException {
        String action = ParamUtil.getString(bpc.request, "action");
        if("back".equals(action)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID,IntranetUserConstant.TRUE);
        }else{
            PublicHolidayDto publicHolidayDto = new PublicHolidayDto();
            publicHolidayDto.setPhCode(ParamUtil.getRequestString(bpc.request,"phCode"));
            Date fromDate = Formatter.parseDate(ParamUtil.getString(bpc.request, "sub_date"));
            publicHolidayDto.setFromDate(fromDate);
            publicHolidayDto.setStatus(ParamUtil.getString(bpc.request, "status"));
            ParamUtil.setSessionAttr(bpc.request,"holiday",publicHolidayDto);
            ValidationResult validationResult = WebValidationHelper.validateProperty(publicHolidayDto,"edit");
            if(validationResult != null && validationResult.isHasErrors()){
                Map<String,String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ISVALID,IntranetUserConstant.FALSE);
            }else{
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(fromDate);
                calendar.add(Calendar.HOUR,23);
                calendar.add(Calendar.MINUTE,59);
                calendar.add(Calendar.SECOND,59);
                Date toDate = calendar.getTime();
                publicHolidayDto.setToDate(toDate);
                publicHolidayDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                publicHolidayService.createHoliday(publicHolidayDto);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID,IntranetUserConstant.TRUE);
                ParamUtil.setSessionAttr(bpc.request,"year",null);
            }
        }
    }


    private void yearOption(BaseProcessClass bpc,boolean isBig){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int count = cal.get(Calendar.YEAR);
        List<SelectOption> selectOptionList = IaisCommonUtils.genNewArrayList();
        if(isBig){
            count = count + 5;
            for (;year <= count ;year ++) {
                selectOptionList.add(new SelectOption(Integer.toString(year), Integer.toString(year)));
            }
        }else{
            List<String> yearList = publicHolidayService.getAllYearList();
            int max;
            int min;
            if(!IaisCommonUtils.isEmpty(yearList)) {
                max = Integer.parseInt(Collections.max(yearList));
                min = Integer.parseInt(Collections.min(yearList));
            } else {
                Calendar calendar = Calendar.getInstance();
                max = calendar.get(Calendar.YEAR);
                min = calendar.get(Calendar.YEAR);
            }
            count = count + 2;
            if(max > count){
                count = max;
            }
            year = year - 5;
            if(min < year){
                year = min;
            }
            for (;count >= year;count --) {
                selectOptionList.add(new SelectOption(Integer.toString(count), Integer.toString(count)));
            }

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
