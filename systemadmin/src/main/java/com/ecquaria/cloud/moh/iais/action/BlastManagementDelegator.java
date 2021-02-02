package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.AttachmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementListDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListWebDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.EmailAuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.EmailAuditTrailEmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.EmailAuditTrailSMSDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.BlastManagementListService;
import com.ecquaria.cloud.moh.iais.service.DistributionListService;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
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

@Delegator(value = "BlastManagementDelegator")
@Slf4j
public class BlastManagementDelegator {


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
    private SystemParamConfig systemParamConfig;
    @Autowired
    OrganizationClient organizationClient;
    @Autowired
    DistributionListService distributionListService;

    public void start(BaseProcessClass bpc){

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_BLAST_NOTIFICATIONS, AuditTrailConsts.FUNCTION_BLAST_MANAGEMENT);

        SearchParam searchParam = getSearchParam(bpc.request,true);
        ParamUtil.setSessionAttr(bpc.request, "blastmanagementSearchParam", searchParam);
        int configFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setSessionAttr(bpc.request,"configFileSize",configFileSize);
    }
    /**
     * doPrepare
     * @param bpc
     */
    public void prepare(BaseProcessClass bpc){
        SearchParam searchParam = getSearchParam(bpc.request,false);

        CrudHelper.doPaging(searchParam,bpc.request);
        QueryHelp.setMainSql("systemAdmin", "queryBlastManagementList",searchParam);
        SearchResult<BlastManagementListDto> searchResult = blastManagementListService.blastList(searchParam);
        Map<String, String> userNameList = IaisCommonUtils.genNewHashMap();
        List<String> ids = IaisCommonUtils.genNewArrayList();
        for (BlastManagementListDto item:searchResult.getRows()
        ) {
            ids.add(item.getCreateBy());
        }
        List<OrgUserDto> actionByRealNameList=blastManagementListService.retrieveOrgUserAccount(ids);
        for (OrgUserDto item:actionByRealNameList
        ) {
            userNameList.put(item.getId(),item.getDisplayName());
        }
        for (BlastManagementListDto item:searchResult.getRows()
             ) {
            if(item.getSchedule() != null){
                item.setSchedule(getDate(item.getSchedule()));
            }
            if(item.getCreateDt() != null){
                item.setCreateDt(getDate(item.getCreateDt()));
            }
            if(item.getActual() != null){
                item.setActual(getDate(item.getActual()));
            }
            String name = userNameList.get(item.getCreateBy());
            item.setCreateBy(name);
            if(StringUtil.isEmpty(item.getDocName())){
                item.setDocName("No");
            }else{
                item.setDocName("Yes");
            }
        }

        getDistribution(bpc,(String)searchParam.getFilters().get("mode"));
        setModeSelection(bpc);
        ParamUtil.setRequestAttr(bpc.request,"blastSearchResult",searchResult);
        ParamUtil.setRequestAttr(bpc.request,"blastSearchParam",searchParam);

    }

    private SearchParam getSearchParam(HttpServletRequest request, boolean neednew){
        SearchParam searchParamGroup = (SearchParam) ParamUtil.getSessionAttr(request, "blastmanagementSearchParam");
        if(neednew){
            searchParamGroup = new SearchParam(BlastManagementListDto.class.getName());
            searchParamGroup.setPageSize(SystemParamUtil.getDefaultPageSize());
            searchParamGroup.setPageNo(1);
            searchParamGroup.setSort("SCHEDULE_SEND_DATE", SearchParam.DESCENDING);
        }
        return searchParamGroup;

    }

    /**
     * create
     * @param bpc
     */
    public void create(BaseProcessClass bpc){
        BlastManagementDto blastManagementDto = new BlastManagementDto();
        blastManagementDto.setMessageId(blastManagementListService.getMessageId());
        ParamUtil.setSessionAttr(bpc.request,"blastManagementDto",blastManagementDto);
    }

    /**
     * edit
     * @param bpc
     */
    public void edit(BaseProcessClass bpc){
        String id =  ParamUtil.getMaskedString(bpc.request, "editBlast");
        if(id == null ){
            id = (String) ParamUtil.getSessionAttr(bpc.request,"editBlastId");
        }
        ParamUtil.setSessionAttr(bpc.request,"editBlastId",id);
        BlastManagementDto blastManagementDto = blastManagementListService.getBlastById(id);
        Calendar cal = Calendar.getInstance();
        cal.setTime(blastManagementDto.getSchedule());
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        blastManagementDto.setMM(String.valueOf(minute));
        blastManagementDto.setHH(String.valueOf(hour));
        ParamUtil.setSessionAttr(bpc.request,"blastManagementDto",blastManagementDto);
    }

    public void createBeforeFill(BaseProcessClass bpc){
        BlastManagementDto blastManagementDto = (BlastManagementDto)ParamUtil.getSessionAttr(bpc.request,"blastManagementDto");
        String schedule = Formatter.formatDate(blastManagementDto.getSchedule());
        ParamUtil.setRequestAttr(bpc.request,"edit",blastManagementDto);
        ParamUtil.setRequestAttr(bpc.request,"schedule",schedule);
        setStatusSelection(bpc);
        setModeSelection(bpc);
        setTimeSelection(bpc);
    }

    public void editBeforeFill(BaseProcessClass bpc){
        BlastManagementDto blastManagementDto = (BlastManagementDto)ParamUtil.getSessionAttr(bpc.request,"blastManagementDto");
        String schedule = Formatter.formatDate(blastManagementDto.getSchedule());
        ParamUtil.setRequestAttr(bpc.request,"edit",blastManagementDto);
        ParamUtil.setRequestAttr(bpc.request,"schedule",schedule);
        setStatusSelection(bpc);
        setModeSelection(bpc);
        setTimeSelection(bpc);
    }

    /**
     * delete
     * @param bpc
     */
    public void delete(BaseProcessClass bpc){
        String[] checkboxlist =  ParamUtil.getMaskedStrings(bpc.request, "editBlast");
        if(checkboxlist != null && checkboxlist.length > 0){
            List<String> list = IaisCommonUtils.genNewArrayList();
            for (String item:checkboxlist
                 ) {
                if(!StringUtil.isEmpty(item)){
                    list.add(item);
                }
            }
            blastManagementListService.deleteBlastList(list);
        }

    }

    /**
     * search
     * @param bpc
     */
    public void search(BaseProcessClass bpc) throws ParseException {
        SearchParam searchParam = getSearchParam(bpc.request,true);
        String descriptionSwitch = ParamUtil.getRequestString(bpc.request,"descriptionSwitch");
        String msgName = ParamUtil.getRequestString(bpc.request,"msgName");
        String start = ParamUtil.getRequestString(bpc.request,"start");
        String end = ParamUtil.getRequestString(bpc.request,"end");
        Date startDate = Formatter.parseDate(start);
        Date endDate = Formatter.parseDate(end);

        String mode = ParamUtil.getRequestString(bpc.request,"modeDelivery");
        String distribution = ParamUtil.getRequestString(bpc.request,"distributionList");
        if(start != null && end !=null && startDate.after(endDate)){
            Map<String,String> err = IaisCommonUtils.genNewHashMap();
            err.put("errDate",MessageUtil.getMessageDesc("EMM_ERR012"));
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(err));
        }else{
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
            if(!StringUtil.isEmpty(mode)){
                searchParam.addFilter("mode",  mode,true);
            }
            if(!StringUtil.isEmpty(distribution)){
                searchParam.addFilter("distribution",  distribution,true);
            }
            if(!StringUtil.isEmpty(end)){
                String enddate = end + " 23:59:59";
                searchParam.addFilter("end",  enddate,true);
            }
            ParamUtil.setRequestAttr(bpc.request,"distributionList",distribution);
        }
        ParamUtil.setSessionAttr(bpc.request, "blastmanagementSearchParam", searchParam);
        ParamUtil.setRequestAttr(bpc.request,"descriptionSwitch",descriptionSwitch);
        ParamUtil.setRequestAttr(bpc.request,"msgName",msgName);
        ParamUtil.setRequestAttr(bpc.request,"start",start);
        ParamUtil.setRequestAttr(bpc.request,"end",end);
        ParamUtil.setRequestAttr(bpc.request,"modeDelivery",mode);
    }

    public void switchBackFill(BaseProcessClass bpc){
        BlastManagementDto blastManagementDto = (BlastManagementDto)ParamUtil.getSessionAttr(bpc.request,"blastManagementDto");
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
        ParamUtil.setSessionAttr(bpc.request,"BlastManagementStep","fillMessage");
        BlastManagementDto blastManagementDto = (BlastManagementDto)ParamUtil.getSessionAttr(bpc.request,"blastManagementDto");
        String name = ParamUtil.getString(bpc.request, "msgName");
        String mode = ParamUtil.getString(bpc.request, "mode");
        String date = ParamUtil.getString(bpc.request, "date");
        String msgId = ParamUtil.getString(bpc.request, "msgId");
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
        blastManagementDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        SimpleDateFormat newformat =  new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);

        if(!StringUtil.isEmpty(date)){
            try {
                Date schedule = new Date();
                schedule = newformat.parse(date);
                long time = schedule.getTime() + Long.parseLong(HH) * 60 * 60 * 1000 + Long.parseLong(MM) * 60 * 1000;
                schedule.setTime(time);
                blastManagementDto.setSchedule(schedule);
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
        }else{
            blastManagementDto.setSchedule(null);
        }

        ParamUtil.setRequestAttr(bpc.request,"edit",blastManagementDto);
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
            if(mode.equals(SMS)){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, SMS);
            }else{
                String fileName = "";
                StringBuilder stringBuilder = new StringBuilder();
                if(blastManagementDto.getAttachmentDtos() != null && blastManagementDto.getAttachmentDtos().size() > 0){
                    for (AttachmentDto item: blastManagementDto.getAttachmentDtos()
                    ) {
                        stringBuilder.append(item.getDocName()).append(',');
                    }
                    fileName = stringBuilder.substring(0,stringBuilder.length()-1);
                }
                if(StringUtil.isEmpty(fileName)){
                    ParamUtil.setRequestAttr(bpc.request, "fileName", "");
                }else{
                    ParamUtil.setRequestAttr(bpc.request, "fileName", fileName);
                }

                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, EMAIL);
            }
        }
        ParamUtil.setSessionAttr(bpc.request,"blastManagementDto",blastManagementDto);
    }

    public void fillMessageSuccess(BaseProcessClass bpc){

    }
    /**
     * writeMessage
     * @param bpc
     */
    public void writeMessage(BaseProcessClass bpc) throws Exception {
        ParamUtil.setSessionAttr(bpc.request,"BlastManagementStep","writeMessage");
        BlastManagementDto blastManagementDto = (BlastManagementDto)ParamUtil.getSessionAttr(bpc.request,"blastManagementDto");
        HttpServletRequest request = bpc.request;
        //setfile
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        List<MultipartFile> files = mulReq.getFiles("selectFile");
        String subject = mulReq.getParameter("subject");
        String messageContent = mulReq.getParameter( "messageContent");
        String content = messageContent.replaceAll("\\&[a-zA-Z]{1,10};", "").replaceAll("<[^>]*>", "").replaceAll("\\r", "").replaceAll("\\n", "");
        blastManagementDto.setSubject(subject);
        blastManagementDto.setMsgContent(messageContent);
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        if(blastManagementDto.getAttachmentDtos() != null && blastManagementDto.getAttachmentDtos().size() > 0){
            for (AttachmentDto item:blastManagementDto.getAttachmentDtos()
                 ) {
                if(item.getDocName().length() > 100){
                    errMap.put("fileUploadError", "GENERAL_ERR0022");
                    break;
                }
            }
        }
        if(StringUtil.isEmpty(content)){
            errMap.put("content", MessageUtil.replaceMessage("GENERAL_ERR0006","Content","field"));
        }
        if(StringUtil.isEmpty(subject)){
            errMap.put("subject",MessageUtil.replaceMessage("GENERAL_ERR0006","Subject","field"));
        }
         if(errMap.size() > 0){
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, AppConsts.FALSE);
        }else{
            ParamUtil.setRequestAttr(bpc.request,"edit",blastManagementDto);
            ValidationResult validationResult =WebValidationHelper.validateProperty(blastManagementDto, "page2");
            if(validationResult != null && validationResult.isHasErrors()) {
                Map<String, String> err = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(err));
                ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, AppConsts.FALSE);
            }else{
                getDistribution(bpc,blastManagementDto.getMode());
                String emailAddress = StringUtils.join(blastManagementDto.getEmailAddress(),"\n");
                ParamUtil.setRequestAttr(bpc.request, "emailAddress", emailAddress);
                ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.TRUE);
            }
            ParamUtil.setSessionAttr(bpc.request,"blastManagementDto",blastManagementDto);

        }

    }

    public void writeMessageSuccess(BaseProcessClass bpc){
        BlastManagementDto blastManagementDto = (BlastManagementDto)ParamUtil.getSessionAttr(bpc.request,"blastManagementDto");
        String fileName = "";
        StringBuilder stringBuilder = new StringBuilder();
        if(blastManagementDto.getAttachmentDtos() != null && blastManagementDto.getAttachmentDtos().size() > 0){
            for (AttachmentDto item: blastManagementDto.getAttachmentDtos()
            ) {
                stringBuilder.append(item.getDocName()).append(',');
            }
            fileName = stringBuilder.substring(0,stringBuilder.length()-1);
        }
        if(StringUtil.isEmpty(fileName)){
            ParamUtil.setRequestAttr(bpc.request, "fileName", "");
        }else{
            ParamUtil.setRequestAttr(bpc.request, "fileName", fileName);
        }
        ParamUtil.setRequestAttr(bpc.request,"edit",blastManagementDto);
    }
    /**
     * selectRecipients
     * @param bpc
     */
    public void selectRecipients(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request,"BlastManagementStep","selectRecipients");
        BlastManagementDto blastManagementDto = (BlastManagementDto)ParamUtil.getSessionAttr(bpc.request,"blastManagementDto");
        String distribution = ParamUtil.getString(bpc.request, "distribution");
        if(distribution == null){
            getDistribution(bpc,blastManagementDto.getMode());
            Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
            errMap.put("distribution",MessageUtil.replaceMessage("GENERAL_ERR0006","Use a distribution list","field"));
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ISVALID, AppConsts.FALSE);

        }else{
            blastManagementDto.setDistributionId(distribution);
            ParamUtil.setRequestAttr(bpc.request,"edit",blastManagementDto);
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
        ParamUtil.setSessionAttr(bpc.request,"blastManagementDto",blastManagementDto);
    }



    @GetMapping(value = "/file-repo")
    public @ResponseBody
    void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        File file = null;
        SearchParam searchParam = getSearchParam(request,false);

        QueryHelp.setMainSql("systemAdmin", "queryBlastManagementList",searchParam);
        SearchResult<BlastManagementListDto> searchResultRowCount = blastManagementListService.blastList(searchParam);
        int row = searchResultRowCount.getRowCount();
        searchParam.setPageNo(1);
        searchParam.setPageSize(row);
        SearchResult<BlastManagementListDto> searchResult = blastManagementListService.blastList(searchParam);
        if (!searchResult.getRows().isEmpty()){
            //master code to description
            List<BlastManagementListDto> blastManagementListDtos = searchResult.getRows();
            List<String> ids = IaisCommonUtils.genNewArrayList();

            for (BlastManagementListDto item:blastManagementListDtos
                 ) {
                ids.add(item.getCreateBy());
            }
            Map<String, String> userNameList = IaisCommonUtils.genNewHashMap();
            List<OrgUserDto> actionByRealNameList=blastManagementListService.retrieveOrgUserAccount(ids);
            for (OrgUserDto item:actionByRealNameList
                 ) {
                userNameList.put(item.getId(),item.getDisplayName());
            }
            for (BlastManagementListDto item:blastManagementListDtos
            ) {
                item.setStatus(MasterCodeUtil.getCodeDesc(item.getStatus()));
                item.setCreateBy(userNameList.get(item.getCreateBy()));
                if(item.getSchedule() != null){
                    item.setSchedule(getDate(item.getSchedule()));
                }
                if(item.getActual() != null){
                    item.setActual(getDate(item.getActual()));
                }
                if(item.getCreateDt() != null){
                    item.setCreateDt(getDate(item.getCreateDt()));
                }
                if(StringUtil.isEmpty(item.getDocName())){
                    item.setDocName("No");
                }else{
                    item.setDocName("Yes");
                }
            }

            try {
                file = ExcelWriter.writerToExcel(searchResult.getRows(), BlastManagementListDto.class, "Blast_Management_Upload_Template");
            } catch (Exception e) {
                log.error("=======>fileDownload error >>>>>", e);
            }

        }else{
            BlastManagementListDto blastManagementListDto = new BlastManagementListDto();
            List<BlastManagementListDto> blastManagementListDtos = IaisCommonUtils.genNewArrayList();
            blastManagementListDto.setCreateDt("");

            try {
                file = ExcelWriter.writerToExcel(blastManagementListDtos, BlastManagementListDto.class, "Blast_Management_Upload_Template");
            } catch (Exception e) {
                log.error("=======>fileDownload error >>>>>", e);
            }

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

    @GetMapping(value = "/audit-repo")
    public @ResponseBody
    void auditfileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String msgid =  ParamUtil.getString(request, "editBlast");
        String mode =  ParamUtil.getString(request, "mode");
        SearchParam auditSearchParam = new SearchParam(EmailAuditTrailDto.class.getName());
        auditSearchParam.setSort("sent_time", SearchParam.ASCENDING);
        auditSearchParam.addFilter("REQUEST_REF_NO", msgid,true);
        CrudHelper.doPaging(auditSearchParam,request);
        QueryHelp.setMainSql("systemAdmin", "audit",auditSearchParam);
        SearchResult<EmailAuditTrailDto> searchResult = blastManagementListService.auditList(auditSearchParam);
        File file = null;
        String createby =  ParamUtil.getString(request, "createby");
        String createDt =  ParamUtil.getString(request, "createDt");
        try {
            if("SMS".equals(mode)){
                List<EmailAuditTrailSMSDto> smsDtos = IaisCommonUtils.genNewArrayList();
                for (EmailAuditTrailDto item:searchResult.getRows()
                     ) {
                    EmailAuditTrailSMSDto emailAuditTrailSMSDto = new EmailAuditTrailSMSDto();
                    emailAuditTrailSMSDto.setRecipient(item.getRecipient());
                    emailAuditTrailSMSDto.setSubject(item.getSubject());
                    emailAuditTrailSMSDto.setContent(item.getContent());
                    emailAuditTrailSMSDto.setNumberAttempts(item.getNumberAttempts());
                    emailAuditTrailSMSDto.setLogMsg(item.getLogMsg());
                    emailAuditTrailSMSDto.setCreateBy(createby);
                    emailAuditTrailSMSDto.setCreateDt(createDt);
                    String date = Formatter.formatDate(item.getSentTime());
                    emailAuditTrailSMSDto.setSentTime(date);
                    smsDtos.add(emailAuditTrailSMSDto);
                }
                if (!smsDtos.isEmpty()){
                    try {
                        file = ExcelWriter.writerToExcel(smsDtos, EmailAuditTrailSMSDto.class, "audit");
                    } catch (Exception e) {
                        log.error("=======>fileDownload error >>>>>", e);
                    }

                }else{
                    List<EmailAuditTrailSMSDto> EmailAuditTrailSMSDtos = IaisCommonUtils.genNewArrayList();
                    try {
                        file = ExcelWriter.writerToExcel(EmailAuditTrailSMSDtos, EmailAuditTrailSMSDto.class, "audit");
                    } catch (Exception e) {
                        log.error("=======>fileDownload error >>>>>", e);
                    }
                }
            }else{
                List<EmailAuditTrailEmailDto> emailDtos = IaisCommonUtils.genNewArrayList();
                for (EmailAuditTrailDto item:searchResult.getRows()
                ) {
                    EmailAuditTrailEmailDto emailAuditTrailEmailDto = new EmailAuditTrailEmailDto();
                    emailAuditTrailEmailDto.setRecipient(item.getRecipient());
                    emailAuditTrailEmailDto.setSubject(item.getSubject());
                    emailAuditTrailEmailDto.setContent(item.getContent());
                    emailAuditTrailEmailDto.setNumberAttempts(item.getNumberAttempts());
                    emailAuditTrailEmailDto.setLogMsg(item.getLogMsg());
                    emailAuditTrailEmailDto.setCreateBy(createby);
                    emailAuditTrailEmailDto.setCreateDt(createDt);
                    String date = Formatter.formatDate(item.getSentTime());
                    emailAuditTrailEmailDto.setSentTime(date);
                    emailDtos.add(emailAuditTrailEmailDto);
                }
                if (!emailDtos.isEmpty()){
                    try {
                        file = ExcelWriter.writerToExcel(emailDtos, EmailAuditTrailEmailDto.class, "audit");
                    } catch (Exception e) {
                        log.error("=======>fileDownload error >>>>>", e);
                    }

                }else{
                    List<EmailAuditTrailEmailDto> emailAuditTrailEmailDtos = IaisCommonUtils.genNewArrayList();
                    try {
                        file = ExcelWriter.writerToExcel(emailAuditTrailEmailDtos, EmailAuditTrailEmailDto.class, "audit");
                    } catch (Exception e) {
                        log.error("=======>fileDownload error >>>>>", e);
                    }
                }
            }
        }catch (Exception e){
            log.info(e.getMessage(),e);
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

    private String getDate(String date){
        char indexChar = ".".charAt(0);
        int index=date.lastIndexOf(indexChar);
        return date.substring(0,index);
    }

    private void getDistribution(BaseProcessClass bpc,String mode){
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        if(mode !=null){
            List<DistributionListWebDto> distributionListDtos = distributionListService.getDistributionList(mode);
            for (DistributionListWebDto item :distributionListDtos
            ) {
                selectOptions.add(new SelectOption(item.getId(),item.getDisname()));
            }
            doSortSelOption(selectOptions);
        }
        ParamUtil.setRequestAttr(bpc.request, "distribution",  (Serializable) selectOptions);
    }

    public static void doSortSelOption(List<SelectOption> selectOptions){
        Collections.sort(selectOptions,(s1, s2)->(s1.getText().compareTo(s2.getText())));
    }

    public void writeSms(BaseProcessClass bpc){
        BlastManagementDto blastManagementDto = (BlastManagementDto)ParamUtil.getSessionAttr(bpc.request,"blastManagementDto");
        getDistribution(bpc,blastManagementDto.getMode());
        ParamUtil.setRequestAttr(bpc.request,"edit",blastManagementDto);
    }

    public void saveSms(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request,"BlastManagementStep","saveSms");
        BlastManagementDto blastManagementDto = (BlastManagementDto)ParamUtil.getSessionAttr(bpc.request,"blastManagementDto");
        String action = ParamUtil.getString(bpc.request, "action");
        String subject = ParamUtil.getString(bpc.request, "subject");
        String messageContent = ParamUtil.getString(bpc.request, "messageContent");
        blastManagementDto.setSubject(subject);
        blastManagementDto.setMsgContent(messageContent);
        String distribution = ParamUtil.getString(bpc.request, "distribution");
        blastManagementDto.setDistributionId(distribution);
        if(!action.equals(BACK)){
            ParamUtil.setRequestAttr(bpc.request, "edit", blastManagementDto);
            ValidationResult validationResult =WebValidationHelper.validateProperty(blastManagementDto, "page2");
            if(validationResult != null && validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "invalid");
            }else{
                blastManagementListService.saveBlast(blastManagementDto);
                ParamUtil.setSessionAttr(bpc.request,"blastManagementDto",null);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "save");
            }
        }else{
            ParamUtil.setSessionAttr(bpc.request,"blastManagementDto",blastManagementDto);
            if(blastManagementDto.getId() == null){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "createBack");
            }else{
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "editBack");
            }
        }


    }

    public void auditTrial(BaseProcessClass bpc) throws ParseException {
        String msgid =  ParamUtil.getString(bpc.request, "msgId");
        String mode =  ParamUtil.getString(bpc.request, "mode");
        String createby =  ParamUtil.getString(bpc.request, "createby");
        String createDt =  ParamUtil.getString(bpc.request, "createDt");

        SearchParam auditSearchParam = new SearchParam(EmailAuditTrailDto.class.getName());
        auditSearchParam.setSort("sent_time", SearchParam.ASCENDING);
        auditSearchParam.addFilter("REQUEST_REF_NO", msgid,true);
        CrudHelper.doPaging(auditSearchParam,bpc.request);
        QueryHelp.setMainSql("systemAdmin", "audit",auditSearchParam);
        SearchResult<EmailAuditTrailDto> searchResult = blastManagementListService.auditList(auditSearchParam);
//        String errMsg = null;
//        if(searchResult.getRowCount() == 0){
//            if(!StringUtil.isEmpty(blastManagementDto.getActual())){
//                List<String> roleEmail = IaisCommonUtils.genNewArrayList();
//                if("LICENSEE".equals(blastManagementDto.getRecipientsRole())) {
//                    List<String> licenseeIds = blastManagementListService.getLicenseeIds(HcsaServiceCacheHelper.getServiceByCode(blastManagementDto.getService()).getSvcName());
//                    roleEmail = organizationClient.getEmailInLicenseeIds(licenseeIds).getEntity();
//                }else{
//                    roleEmail = blastManagementListService.getEmailByRole(blastManagementDto.getRecipientsRole(), HcsaServiceCacheHelper.getServiceByCode(blastManagementDto.getService()).getSvcName());
//                }
//                if(roleEmail.size() > 0){
//
//                }else{
//
//                }
//
//            }else{
//                errMsg = MessageUtil.getMessageDesc("GENERAL_ACK018");
//            }
//        }else{
//            errMsg = null;
//        }
        //
        ParamUtil.setRequestAttr(bpc.request,"searchResult",searchResult);
        ParamUtil.setRequestAttr(bpc.request,"auditSearchParam",auditSearchParam);
        ParamUtil.setRequestAttr(bpc.request,"mode",mode);
        ParamUtil.setRequestAttr(bpc.request,"createDt",createDt);
        ParamUtil.setRequestAttr(bpc.request,"createby",createby);
        ParamUtil.setRequestAttr(bpc.request,"editBlast",msgid);
    }

    public void preview(BaseProcessClass bpc){
        String id =  ParamUtil.getRequestString(bpc.request, "editBlast");
        BlastManagementDto blastManagementDto = blastManagementListService.getBlastById(id);
        Calendar cal = Calendar.getInstance();
        cal.setTime(blastManagementDto.getSchedule());
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        int minute = cal.get(Calendar.MINUTE);
        blastManagementDto.setMM(String.valueOf(minute));
        blastManagementDto.setHH(String.valueOf(hour));

        if(EMAIL.equals(blastManagementDto.getMode())) {
            String fileName = "";
            StringBuilder stringBuilder = new StringBuilder();
            if (blastManagementDto.getAttachmentDtos() != null && blastManagementDto.getAttachmentDtos().size() > 0) {
                for (AttachmentDto item : blastManagementDto.getAttachmentDtos()
                ) {
                    stringBuilder.append(item.getDocName()).append(',');
                }
                fileName = stringBuilder.substring(0, stringBuilder.length() - 1);
            }
            if (StringUtil.isEmpty(fileName)) {
                ParamUtil.setRequestAttr(bpc.request, "fileName", "N/A");
            } else {
                ParamUtil.setRequestAttr(bpc.request, "fileName", fileName);
            }
        }
        ParamUtil.setRequestAttr(bpc.request,"edit",blastManagementDto);
    }

    private void setModeSelection(BaseProcessClass bpc){
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        selectOptions.add(new SelectOption(EMAIL,EMAIL));
        selectOptions.add(new SelectOption(SMS,SMS));
        ParamUtil.setSessionAttr(bpc.request, "mode",  (Serializable) selectOptions);
    }

    private void setStatusSelection(BaseProcessClass bpc){
        List<SelectOption> selectOptionArrayList = IaisCommonUtils.genNewArrayList();
        selectOptionArrayList.add(new SelectOption(AppConsts.COMMON_STATUS_ACTIVE,"Active"));
        selectOptionArrayList.add(new SelectOption(AppConsts.COMMON_STATUS_IACTIVE,"Inactive"));
        ParamUtil.setRequestAttr(bpc.request, "status",  selectOptionArrayList);
    }

    private void setTimeSelection(BaseProcessClass bpc){
        List<SelectOption> timeHourList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i< 24;i++){
            timeHourList.add(new SelectOption(String.valueOf(i), i<10?"0"+String.valueOf(i):String.valueOf(i)));
        }
        List<SelectOption> timeMinList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i< 60;i++){
            timeMinList.add(new SelectOption(String.valueOf(i), i<10?"0"+String.valueOf(i):String.valueOf(i)));
        }
        ParamUtil.setRequestAttr(bpc.request, "HHselect",  timeHourList);
        ParamUtil.setRequestAttr(bpc.request, "MMselect",  timeMinList);
    }

}
