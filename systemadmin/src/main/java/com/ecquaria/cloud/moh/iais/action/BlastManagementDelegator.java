package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementListDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListWebDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.EmailAuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.BlastManagementListService;
import com.ecquaria.cloud.moh.iais.service.DistributionListService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 *File Name: MessageDelegator
 *Creator: guyin
 *Creation time:2019/12/26 19:08
 *Describe:
 */

@Delegator(value = "BlastManagementDelegator")
@Slf4j
public class BlastManagementDelegator {

    private SearchParam searchParam;
    private static final String BASE_SERVICE = "SVTP001";
    private static final String SPECIFIED_SERVICE = "SVTP003";
    private static final String EMAIL = "Email";
    private static final String SMS = "SMS";
    private static final String BOTH = "both";
    private static final String BACK = "back";
    private static final String FILE_UPLOAD_ERROR = "fileUploadError";
    @Autowired
    BlastManagementListService blastManagementListService;

    @Autowired
    DistributionListService distributionListService;

    private BlastManagementDto blastManagementDto = new BlastManagementDto();
    public void start(BaseProcessClass bpc){
        searchParam = new SearchParam(BlastManagementListDto.class.getName());
        searchParam.setPageSize(10);
        searchParam.setPageNo(1);
        searchParam.setSort("ID", SearchParam.ASCENDING);
        AuditTrailHelper.auditFunction("blastManagement", "BlastManagementDelegator");
        ParamUtil.setRequestAttr(bpc.request, "firstOption", "Please Select");
        ParamUtil.setRequestAttr(bpc.request, "firstValue", " ");
    }
    /**
     * doPrepare
     * @param bpc
     */
    public void prepare(BaseProcessClass bpc){
        blastManagementDto = new BlastManagementDto();
        CrudHelper.doPaging(searchParam,bpc.request);
        QueryHelp.setMainSql("systemAdmin", "queryBlastManagementList",searchParam);
        SearchResult<BlastManagementListDto> searchResult = blastManagementListService.blastList(searchParam);
        ParamUtil.setRequestAttr(bpc.request,"blastSearchResult",searchResult);
        ParamUtil.setRequestAttr(bpc.request,"blastSearchParam",searchParam);

    }

    /**
     * create
     * @param bpc
     */
    public void create(BaseProcessClass bpc){
        blastManagementDto = new BlastManagementDto();
    }

    /**
     * edit
     * @param bpc
     */
    public void edit(BaseProcessClass bpc){
        String id =  ParamUtil.getRequestString(bpc.request, "editBlast");
        if(id == null ){
            id = (String) ParamUtil.getSessionAttr(bpc.request,"editBlastId");
        }
        ParamUtil.setSessionAttr(bpc.request,"editBlastId",id);
        blastManagementDto = blastManagementListService.getBlastById(id);
        Calendar cal = Calendar.getInstance();
        cal.setTime(blastManagementDto.getSchedule());
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        blastManagementDto.setMM(String.valueOf(minute));
        blastManagementDto.setHH(String.valueOf(hour));
    }

    public void createBeforeFill(BaseProcessClass bpc){
        String schedule = Formatter.formatDate(blastManagementDto.getSchedule());
        ParamUtil.setSessionAttr(bpc.request,"edit",blastManagementDto);
        ParamUtil.setRequestAttr(bpc.request,"schedule",schedule);
        setStatusSelection(bpc,blastManagementDto.getStatus());
        setModeSelection(bpc,blastManagementDto.getMode());
    }

    public void editBeforeFill(BaseProcessClass bpc){
        String schedule = Formatter.formatDate(blastManagementDto.getSchedule());
        ParamUtil.setSessionAttr(bpc.request,"edit",blastManagementDto);
        ParamUtil.setRequestAttr(bpc.request,"schedule",schedule);
        setStatusSelection(bpc,blastManagementDto.getStatus());
        setModeSelection(bpc,blastManagementDto.getMode());
    }

    /**
     * delete
     * @param bpc
     */
    public void delete(BaseProcessClass bpc){
        String[] checkboxlist =  ParamUtil.getStrings(bpc.request, "checkboxlist");
        if(checkboxlist != null && checkboxlist.length > 0){
            List<String> list = Arrays.asList(checkboxlist);
            blastManagementListService.deleteBlastList(list);
        }

    }

    /**
     * search
     * @param bpc
     */
    public void search(BaseProcessClass bpc){
        String descriptionSwitch = ParamUtil.getRequestString(bpc.request,"descriptionSwitch");
        String msgName = ParamUtil.getRequestString(bpc.request,"msgName");
        String start = ParamUtil.getRequestString(bpc.request,"start");
        String end = ParamUtil.getRequestString(bpc.request,"end");
        searchParam.getParams().clear();
        searchParam.getFilters().clear();
        searchParam.setPageNo(1);
        if(!StringUtil.isEmpty(descriptionSwitch)){
            searchParam.addFilter("description", "%" + descriptionSwitch + "%",true);
        }
        if(!StringUtil.isEmpty(msgName)){
            searchParam.addFilter("msgName",  "%" +msgName + "%",true);
        }
        if(!StringUtil.isEmpty(start)){
            searchParam.addFilter("start",  start,true);
        }
        if(!StringUtil.isEmpty(end)){
            searchParam.addFilter("end",  end,true);
        }
        ParamUtil.setRequestAttr(bpc.request,"descriptionSwitch",descriptionSwitch);
        ParamUtil.setRequestAttr(bpc.request,"msgName",msgName);
        ParamUtil.setRequestAttr(bpc.request,"start",start);
        ParamUtil.setRequestAttr(bpc.request,"end",end);
    }

    public void switchBackFill(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String currentAction = mulReq.getParameter("action");

        if(!currentAction.equals(BACK)){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, currentAction);
        }else{
            if(blastManagementDto.getId() == null){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "createBack");
            }else{
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "editBack");
            }
        }

    }

    /**
     * save
     * @param bpc
     */
    public void save(BaseProcessClass bpc){

    }

    /**
     * fillMessage
     * @param bpc
     */
    public void fillMessage(BaseProcessClass bpc){
        String name = ParamUtil.getString(bpc.request, "name");
        String mode = ParamUtil.getString(bpc.request, "mode");
        String date = ParamUtil.getString(bpc.request, "date");
        String HH = ParamUtil.getString(bpc.request, "HH");
        String MM = ParamUtil.getString(bpc.request, "MM");
        ParamUtil.setRequestAttr(bpc.request,"hour",HH);
        ParamUtil.setRequestAttr(bpc.request,"minutes",MM);
        String status = ParamUtil.getString(bpc.request, "status");
        blastManagementDto.setMsgName(name);
        blastManagementDto.setMode(mode);
        blastManagementDto.setStatus(status);
        blastManagementDto.setHH(HH);
        blastManagementDto.setMM(MM);
        SimpleDateFormat newformat =  new SimpleDateFormat("dd/MM/yyyy");
        Date schedule = new Date();
        if(!StringUtil.isEmpty(date)){
            try {
                schedule = newformat.parse(date);
                long time = schedule.getTime();
                schedule.setTime(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        blastManagementDto.setSchedule(schedule);
        ParamUtil.setSessionAttr(bpc.request,"edit",blastManagementDto);
        ValidationResult validationResult =WebValidationHelper.validateProperty(blastManagementDto, "page1");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            if(blastManagementDto.getId() == null){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "createBack");
            }else{
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "editBack");
            }
        }else{
            if(!StringUtil.isEmpty(date)){
                try {
                    schedule = newformat.parse(date);
                    long time = schedule.getTime() + Long.parseLong(HH) * 60 * 60 * 1000 + Long.parseLong(MM) * 60 * 1000;
                    schedule.setTime(time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            blastManagementDto.setSchedule(schedule);
            if(mode.equals(SMS)){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, SMS);
            }else{
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, EMAIL);
            }
        }
    }

    public void fillMessageSuccess(BaseProcessClass bpc){

    }
    /**
     * writeMessage
     * @param bpc
     */
    public void writeMessage(BaseProcessClass bpc) throws Exception {
        HttpServletRequest request = bpc.request;
        //setfile
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        MultipartFile file = mulReq.getFile("selectedFile");
        String subject = mulReq.getParameter("subject");
        String messageContent = mulReq.getParameter( "messageContent");
        blastManagementDto.setSubject(subject);
        blastManagementDto.setMsgContent(messageContent);

        Map<String, String> errorMap = validationFile(request, file);
        if (!(errorMap != null && !errorMap.isEmpty())){
            String json = "";
            File toFile = FileUtils.multipartFileToFile(file);
            errorMap = new HashMap<>(1);
            try {
                byte[] fileToByteArray = FileUtils.readFileToByteArray(toFile);
                blastManagementDto.setFileDate(fileToByteArray);
                blastManagementDto.setDocName(file.getOriginalFilename());
                blastManagementDto.setDocSize(Long.toString(file.getSize()));
            }catch (Exception e){
                errorMap.put(FILE_UPLOAD_ERROR, MessageCodeKey.CHKL_ERR011);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                log.info(e.getMessage());
            }
        }
        ParamUtil.setSessionAttr(bpc.request,"edit",blastManagementDto);
        ValidationResult validationResult =WebValidationHelper.validateProperty(blastManagementDto, "page2");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> err = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(err));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, AppConsts.FALSE);
        }else{
            getDistribution(bpc);
            String emailAddress = StringUtils.join(blastManagementDto.getEmailAddress(),"\n");
            ParamUtil.setRequestAttr(bpc.request, "emailAddress", emailAddress);
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.TRUE);
        }


    }

    public void writeMessageSuccess(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request,"edit",blastManagementDto);
    }
    /**
     * selectRecipients
     * @param bpc
     */
    public void selectRecipients(BaseProcessClass bpc){
        String email = ParamUtil.getString(bpc.request, "email");
        String distribution = ParamUtil.getString(bpc.request, "distribution");
        List<String> emaillist = Arrays.asList(email.split("\\n"));
        if(distribution != null){
            blastManagementDto.setDistributionId(distribution);
        }
        if(emaillist != null){
            blastManagementDto.setEmailAddress(emaillist);
        }
        ParamUtil.setSessionAttr(bpc.request,"edit",blastManagementDto);
        ValidationResult validationResult =WebValidationHelper.validateProperty(blastManagementDto, "page3");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.FALSE);

        }else{
            blastManagementListService.saveBlast(blastManagementDto);
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.TRUE);
        }

    }

    private Map<String, String> validationFile(HttpServletRequest request, MultipartFile file){
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (file.getSize() <= 0){
            errorMap.put(FILE_UPLOAD_ERROR, "GENERAL_ERR0004");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return errorMap;
        }

        double size = (file.getSize() / 0x400) / (double) 0x400;

        if (Math.ceil(size) > 0x10){
            errorMap.put(FILE_UPLOAD_ERROR, "GENERAL_ERR0004");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return errorMap;
        }

        return null;
    }

    public void auditTrial(BaseProcessClass bpc){
        String id =  ParamUtil.getString(bpc.request, "editBlast");
        SearchParam auditSearchParam = new SearchParam(EmailAuditTrailDto.class.getName());
        auditSearchParam.setSort("history_id", SearchParam.ASCENDING);
//        if(!StringUtil.isEmpty(id)){
        auditSearchParam.addFilter("refNum", "6F4FAB70-D32E-EA11-BE7D-000C29F371DC",true);
//        }
        CrudHelper.doPaging(auditSearchParam,bpc.request);
        QueryHelp.setMainSql("systemAdmin", "audit",auditSearchParam);
        SearchResult<EmailAuditTrailDto> searchResult = blastManagementListService.auditList(auditSearchParam);
        ParamUtil.setRequestAttr(bpc.request,"SearchResult",searchResult);
    }

    @GetMapping(value = "/file-repo")
    public @ResponseBody
    void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = null;
        QueryHelp.setMainSql("systemAdmin", "queryBlastManagementList",searchParam);
        SearchResult<BlastManagementListDto> searchResult = blastManagementListService.blastList(searchParam);
        if (!searchResult.getRows().isEmpty()){
            //master code to description
            List<BlastManagementListDto> blastManagementListDtos = searchResult.getRows();
            file = ExcelWriter.exportExcel(blastManagementListDtos, BlastManagementListDto.class, "Blast_Management_Upload_Template");
        }

        if(file != null){
            try {
                FileUtils.writeFileResponseContent(response, file);
                FileUtils.deleteTempFile(file);
            } catch (IOException e) {
                log.debug(e.getMessage());
            }
        }

        log.debug(StringUtil.changeForLog("fileHandler end ...."));
    }

    private void getDistribution(BaseProcessClass bpc){
        List<DistributionListWebDto> distributionListDtos = distributionListService.getDistributionList(blastManagementDto.getMode());
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        if(blastManagementDto.getDistributionId() != null){
            selectOptions.add(new SelectOption(blastManagementDto.getDistributionId(),blastManagementDto.getDistributionName()));
        }else{
            selectOptions.add(new SelectOption("","Please Select"));
        }

        for (DistributionListWebDto item :distributionListDtos
        ) {
            selectOptions.add(new SelectOption(item.getId(),item.getDisname()));
        }
        ParamUtil.setRequestAttr(bpc.request, "distribution",  (Serializable) selectOptions);
    }

    public void writeSms(BaseProcessClass bpc){
        getDistribution(bpc);
        ParamUtil.setSessionAttr(bpc.request,"edit",blastManagementDto);
    }

    public void saveSms(BaseProcessClass bpc){
        String action = ParamUtil.getString(bpc.request, "action");
        String subject = ParamUtil.getString(bpc.request, "subject");
        String messageContent = ParamUtil.getString(bpc.request, "messageContent");
        blastManagementDto.setSubject(subject);
        blastManagementDto.setMsgContent(messageContent);
        String distribution = ParamUtil.getString(bpc.request, "distribution");
        if(distribution != null){
            blastManagementDto.setDistributionId(distribution);
        }
        if(!action.equals(BACK)){
            ParamUtil.setRequestAttr(bpc.request, "edit", blastManagementDto);
            ValidationResult validationResult =WebValidationHelper.validateProperty(blastManagementDto, "page2");
            if(validationResult != null && validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "invalid");
            }else{
                blastManagementListService.saveBlast(blastManagementDto);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "save");
            }
        }else{
            if(blastManagementDto.getId() == null){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "createBack");
            }else{
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "editBack");
            }
        }


    }

    private void setModeSelection(BaseProcessClass bpc,String selected){
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        if(selected != null){
            if(selected.equals(EMAIL)){
                selectOptions.add(new SelectOption(EMAIL,EMAIL));
                selectOptions.add(new SelectOption(SMS,SMS));
            }else{
                selectOptions.add(new SelectOption(SMS,SMS));
                selectOptions.add(new SelectOption(EMAIL,EMAIL));
            }
        }else{
            selectOptions.add(new SelectOption("","Please Select"));
            selectOptions.add(new SelectOption(EMAIL,EMAIL));
            selectOptions.add(new SelectOption(SMS,SMS));
        }
        ParamUtil.setSessionAttr(bpc.request, "mode",  (Serializable) selectOptions);
    }

    private void setStatusSelection(BaseProcessClass bpc,String selected){
        List<SelectOption> selectOptionArrayList = IaisCommonUtils.genNewArrayList();
        if(selected != null){
            if(selected.equals(AppConsts.COMMON_STATUS_ACTIVE)){
                selectOptionArrayList.add(new SelectOption(AppConsts.COMMON_STATUS_ACTIVE,"active"));
                selectOptionArrayList.add(new SelectOption(AppConsts.COMMON_STATUS_IACTIVE,"inactive"));
            }else{
                selectOptionArrayList.add(new SelectOption(AppConsts.COMMON_STATUS_IACTIVE,"inactive"));
                selectOptionArrayList.add(new SelectOption(AppConsts.COMMON_STATUS_ACTIVE,"active"));
            }
        }else{
            selectOptionArrayList.add(new SelectOption("","Please Select"));
            selectOptionArrayList.add(new SelectOption(AppConsts.COMMON_STATUS_ACTIVE,"active"));
            selectOptionArrayList.add(new SelectOption(AppConsts.COMMON_STATUS_IACTIVE,"inactive"));
        }
        ParamUtil.setRequestAttr(bpc.request, "status",  selectOptionArrayList);
    }

}
