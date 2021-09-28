package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.client.ComFileRepoClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sg.gov.moh.iais.egp.bsb.client.ApprovalApplicationClient;
import sg.gov.moh.iais.egp.bsb.client.DocClient;
import sg.gov.moh.iais.egp.bsb.constant.ApprovalApplicationConstants;
import sg.gov.moh.iais.egp.bsb.dto.approval.ApprovalApplicationDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.DocConfigDto;
import sg.gov.moh.iais.egp.bsb.entity.Biological;
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sg.gov.moh.iais.egp.bsb.entity.FacilityDoc;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * @author : LiRan
 * @date : 2021/8/11
 */
@Delegator
@Slf4j
public class NewApprovalDelegator {

    public static final String TASK_LIST = "taskList";

    private static final String DOC_TYPE_1 = "Approval/Endorsement: Biosafety Committee";
    private static final String DOC_TYPE_2 = "Risk Assessment";
    private static final String DOC_TYPE_3 = "Standard Operating Procedure (SOP)";
    private static final String DOC_TYPE_4 = "GMAC Endorsement";
    private static final String DOC_TYPE_5 = "Emergency Response Plan";
    private static final String DOC_TYPE_6 = "Approval Document from MOH";
    private static final String DOC_TYPE_7 = "Special Approval to Handle";
    private static final String DOC_TYPE_8 = "Others";

    public static final String PRIMARY_DOC_CONFIG = "primaryDocConfig";

    @Autowired
    private ApprovalApplicationClient approvalApplicationClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private ComFileRepoClient comFileRepoClient;

    @Autowired
    private DocClient docClient;

    public void doStart(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(ApprovalApplicationConstants.MODULE_SYSTEM_CONFIG,
                ApprovalApplicationConstants.FUNCTION_ERROR_MESSAGES_MANAGEMENT);
        HttpServletRequest request = bpc.request;
        String taskList = ParamUtil.getString(request, TASK_LIST);
        ParamUtil.setSessionAttr(request, TASK_LIST, taskList);
//        ParamUtil.setSessionAttr(request, TASK_LIST, ApprovalApplicationConstants.APPROVAL_TYPE_2);
//        ParamUtil.setSessionAttr(request, TASK_LIST, ApprovalApplicationConstants.APPROVAL_TYPE_3);
        IaisEGPHelper.clearSessionAttr(request, ApprovalApplicationConstants.class);
    }

    public void prepareCompanyInfo(BaseProcessClass bpc) {
        //TODO prepare company info
    }

    public void prepare(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String action = ParamUtil.getRequestString(request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_PAGE);
        Object errorMap = ParamUtil.getRequestAttr(request,ApprovalApplicationConstants.ERRORMSG);
        ParamUtil.setRequestAttr(request, ApprovalApplicationConstants.ERRORMSG, errorMap);
        if (StringUtil.isEmpty(action)) {
            action = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_1;
        }
        List<SelectOption> biologicalSchedule1 = getSelectOptionList(ApprovalApplicationConstants.SCHEDULE_1);
        List<SelectOption> biologicalSchedule2 = getSelectOptionList(ApprovalApplicationConstants.SCHEDULE_2);
        List<SelectOption> biologicalSchedule3 = getSelectOptionList(ApprovalApplicationConstants.SCHEDULE_3);
        List<SelectOption> biologicalSchedule4 = getSelectOptionList(ApprovalApplicationConstants.SCHEDULE_4);
        List<SelectOption> biologicalSchedule5 = getSelectOptionList(ApprovalApplicationConstants.SCHEDULE_5);
        List<SelectOption> biologicalSchedule6 = getSelectOptionList(ApprovalApplicationConstants.SCHEDULE_6);
        ParamUtil.setSessionAttr(request, ApprovalApplicationConstants.BIOLOGICAL_SCHEDULE_1, (Serializable) biologicalSchedule1);
        ParamUtil.setSessionAttr(request, ApprovalApplicationConstants.BIOLOGICAL_SCHEDULE_2, (Serializable) biologicalSchedule2);
        ParamUtil.setSessionAttr(request, ApprovalApplicationConstants.BIOLOGICAL_SCHEDULE_3, (Serializable) biologicalSchedule3);
        ParamUtil.setSessionAttr(request, ApprovalApplicationConstants.BIOLOGICAL_SCHEDULE_4, (Serializable) biologicalSchedule4);
        ParamUtil.setSessionAttr(request, ApprovalApplicationConstants.BIOLOGICAL_SCHEDULE_5, (Serializable) biologicalSchedule5);
        ParamUtil.setSessionAttr(request, ApprovalApplicationConstants.BIOLOGICAL_SCHEDULE_6, (Serializable) biologicalSchedule6);
        ParamUtil.setRequestAttr(request, ApprovalApplicationConstants.CRUD_ACTION_TYPE_VALUE, action);
        log.info(StringUtil.changeForLog("prepare(action):"+action));
    }

    public void prepareForms(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        Object errorMap = ParamUtil.getRequestAttr(request,ApprovalApplicationConstants.ERRORMSG);
        ParamUtil.setRequestAttr(request, ApprovalApplicationConstants.ERRORMSG, errorMap);
        List<Facility> facilityList = approvalApplicationClient.getAllFacility().getEntity();
        List<SelectOption> facilityNameList =  new ArrayList<>();
        if (facilityList != null){
            for (Facility dto : facilityList) {
                facilityNameList.add(new SelectOption(dto.getId(),dto.getFacilityName()));
            }
        }
        ParamUtil.setRequestAttr(request, ApprovalApplicationConstants.FACILITY_NAME_SELECT, facilityNameList);
    }

    public void prepareDocuments(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        log.info(StringUtil.changeForLog("the do prepareDocuments start ...."));
        //set docConfig on different processType,The data here is simulated
        String task = (String)ParamUtil.getSessionAttr(request,TASK_LIST);
        List<DocConfigDto> docConfigDtoList = new ArrayList<>();
        docConfigDtoList.add(new DocConfigDto(DOC_TYPE_1,true,task,"1",false));
        docConfigDtoList.add(new DocConfigDto(DOC_TYPE_2,true,task,"2",false));
        if (task.equals(ApprovalApplicationConstants.APPROVAL_TYPE_1)){
            docConfigDtoList.add(new DocConfigDto(DOC_TYPE_3,true,task,"3",false));
            docConfigDtoList.add(new DocConfigDto(DOC_TYPE_4,true,task,"4",false));
        }else if (task.equals(ApprovalApplicationConstants.APPROVAL_TYPE_2)){
            docConfigDtoList.add(new DocConfigDto(DOC_TYPE_3,true,task,"3",false));
            docConfigDtoList.add(new DocConfigDto(DOC_TYPE_5,true,task,"5",false));
        }else if (task.equals(ApprovalApplicationConstants.APPROVAL_TYPE_3)){
            docConfigDtoList.add(new DocConfigDto(DOC_TYPE_6,true,task,"6",false));
            docConfigDtoList.add(new DocConfigDto(DOC_TYPE_5,true,task,"5",false));
            docConfigDtoList.add(new DocConfigDto(DOC_TYPE_7,true,task,"7",false));
        }
        docConfigDtoList.add(new DocConfigDto(DOC_TYPE_8,false,task,"8",true));
        ParamUtil.setSessionAttr(request, PRIMARY_DOC_CONFIG, (Serializable) docConfigDtoList);

        //set sysFileSize and sysFileType
        int sysFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setRequestAttr(request, "sysFileSize", sysFileSize);
        String sysFileType = systemParamConfig.getUploadFileType();
        String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
        StringBuilder fileTypeStr = new StringBuilder();
        if (sysFileTypeArr != null) {
            int i = 0;
            int fileTypeLength = sysFileTypeArr.length;
            for (String fileType : sysFileTypeArr) {
                fileTypeStr.append(' ').append(fileType);
                if (fileTypeLength > 1 && i < fileTypeLength - 2) {
                    fileTypeStr.append(',');
                }
                if (fileTypeLength > 1 && i == sysFileTypeArr.length - 2) {
                    fileTypeStr.append(" and");
                }
                if (i == fileTypeLength - 1) {
                    fileTypeStr.append('.');
                }
                i++;
            }
        }
        ParamUtil.setRequestAttr(request, "sysFileType", fileTypeStr.toString());
        log.info(StringUtil.changeForLog("the do prepareDocuments end ...."));
    }

    public void preparePreview(BaseProcessClass bpc) {
    }

    public void doDocuments(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApprovalApplicationDto approvalApplicationDto = (ApprovalApplicationDto)ParamUtil.getSessionAttr(request, ApprovalApplicationConstants.APPROVAL_APPLICATION_DTO_ATTR);
        //set crudActionType and crudActionTypeFormPage on different actionType
        String crudActionType = "";
        String crudActionTypeFormPage = "";
        String actionType = ParamUtil.getString(request,ApprovalApplicationConstants.ACTIONTYPE);
        if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_1)){
            crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
            crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_1;
        }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_2)){
            crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
            crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_3;
        }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_3)){
            crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
            crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_1;
        }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_4)){
            crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
            crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_2;
        }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_5)){
            crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
            crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_3;
        }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_6)){
            crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_2;
            crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_2;
        }
        ParamUtil.setRequestAttr(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE,crudActionTypeFormPage);
        ParamUtil.setRequestAttr(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE,crudActionType);

        List<DocConfigDto> docConfigDtoList = (List<DocConfigDto>) request.getSession().getAttribute(PRIMARY_DOC_CONFIG);
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        Map<String, File> fileMap = new HashMap<>();
        for(int i =0;i<docConfigDtoList.size();i++){
            String docKey = i+"primaryDoc";
            String configKey = "primaryDoc"+i;
            fileMap = (Map<String, File>) ParamUtil.getSessionAttr(mulReq,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey);
            if (fileMap != null){
                List<File> fileList = new ArrayList<>(fileMap.values());
                docConfigDtoList.get(i).setIsValid(true);
                ParamUtil.setSessionAttr(request,configKey,(Serializable) fileList);
            }
        }
    }

    public void doPreview(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        //set crudActionType and crudActionTypeFormPage on different actionType
        String crudActionType = "";
        String crudActionTypeFormPage = "";
        String actionType = ParamUtil.getString(request,ApprovalApplicationConstants.ACTIONTYPE);
        if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_1)){
            crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
            crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_2;
        }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_2)){
            crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
            crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_1;
        }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_3)){
            crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
            crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_1;
        }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_4)){
            crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
            crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_2;
        }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_5)){
            crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
            crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_3;
        }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_6)){
            crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_2;
            crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_3;
        }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_7)){
            crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_3;
            crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_4;
        }
        ParamUtil.setRequestAttr(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE,crudActionTypeFormPage);
        ParamUtil.setRequestAttr(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE,crudActionType);
    }

    public void doForms(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        ApprovalApplicationDto approvalApplicationDto = getDtoByForm(bpc);
        ParamUtil.setSessionAttr(request,ApprovalApplicationConstants.APPROVAL_APPLICATION_DTO_ATTR, approvalApplicationDto);
        //set validateStatus on different conditions
        String task = (String)ParamUtil.getSessionAttr(request,TASK_LIST);
        String validateStatus = "";
        String crudActionType = "";
        String crudActionTypeFormPage = "";
        //judge natureOfTheSampleList if has "others"
        Boolean flag = true;
        String actionType = ParamUtil.getString(request,ApprovalApplicationConstants.ACTIONTYPE);
        if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_6)){
            validateStatus = ApprovalApplicationConstants.VALIDATE_STATUS_11;
        }else{
            if (task.equals(ApprovalApplicationConstants.APPROVAL_TYPE_1)){
                List<String> natureOfTheSampleList = approvalApplicationDto.getNatureOfTheSampleList();
                if (natureOfTheSampleList != null){
                    for (int i = 0; i < natureOfTheSampleList.size(); i++) {
                        if(natureOfTheSampleList.get(i).equals(ApprovalApplicationConstants.NATURE_OF_THE_SAMPLE_7)) {
                            flag = false;
                        }
                    }
                }
                if (flag){
                    if (approvalApplicationDto.getProcurementMode() == null || approvalApplicationDto.getProcurementMode() == ""){
                        validateStatus = ApprovalApplicationConstants.VALIDATE_STATUS_8;
                    }else if (approvalApplicationDto.getProcurementMode().equals(ApprovalApplicationConstants.MODE_OF_PROCUREMENT_1)){
                        validateStatus = ApprovalApplicationConstants.VALIDATE_STATUS_1;
                    }else if (approvalApplicationDto.getProcurementMode().equals(ApprovalApplicationConstants.MODE_OF_PROCUREMENT_2)){
                        validateStatus = ApprovalApplicationConstants.VALIDATE_STATUS_2;
                    }
                }else{
                    if (approvalApplicationDto.getProcurementMode() == null || approvalApplicationDto.getProcurementMode() == ""){
                        validateStatus = ApprovalApplicationConstants.VALIDATE_STATUS_9;
                    }else if (approvalApplicationDto.getProcurementMode().equals(ApprovalApplicationConstants.MODE_OF_PROCUREMENT_1)){
                        validateStatus = ApprovalApplicationConstants.VALIDATE_STATUS_3;
                    }else if (approvalApplicationDto.getProcurementMode().equals(ApprovalApplicationConstants.MODE_OF_PROCUREMENT_2)){
                        validateStatus = ApprovalApplicationConstants.VALIDATE_STATUS_4;
                    }
                }
            }else if (task.equals(ApprovalApplicationConstants.APPROVAL_TYPE_2)){
                if (approvalApplicationDto.getProcurementMode() == null || approvalApplicationDto.getProcurementMode() == ""){
                    validateStatus = ApprovalApplicationConstants.VALIDATE_STATUS_10;
                }else if (approvalApplicationDto.getProcurementMode().equals(ApprovalApplicationConstants.MODE_OF_PROCUREMENT_1)){
                    validateStatus = ApprovalApplicationConstants.VALIDATE_STATUS_5;
                }else if (approvalApplicationDto.getProcurementMode().equals(ApprovalApplicationConstants.MODE_OF_PROCUREMENT_2)){
                    validateStatus = ApprovalApplicationConstants.VALIDATE_STATUS_6;
                }
            }else if (task.equals(ApprovalApplicationConstants.APPROVAL_TYPE_3)){
                validateStatus = ApprovalApplicationConstants.VALIDATE_STATUS_7;
            }
        }
        ValidationResult vResult = WebValidationHelper.validateProperty(approvalApplicationDto,validateStatus);
        //back
        if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_1)) {
            crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
            crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_4;
        }else{
            //validate
            if(vResult != null && vResult.isHasErrors()){
                Map<String,String> errorMap = vResult.retrieveAll();
                ParamUtil.setRequestAttr(request, ApprovalApplicationConstants.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
                crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_1;
            }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_2)){
                crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
                crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_2;
            }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_3)){
                crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
                crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_1;
            }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_4)){
                crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
                crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_2;
            }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_5)){
                crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_1;
                crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_3;
            }else if (actionType.equals(ApprovalApplicationConstants.ACTIONTYPE_6)){
                crudActionType = ApprovalApplicationConstants.CRUD_ACTION_TYPE_2;
                crudActionTypeFormPage = ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE_1;
            }
        }
        ParamUtil.setRequestAttr(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE,crudActionTypeFormPage);
        ParamUtil.setRequestAttr(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE,crudActionType);

    }

    public void controlSwitch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String crudActionTypeFormPage = (String) ParamUtil.getRequestAttr(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE);
        ParamUtil.setRequestAttr(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE,crudActionTypeFormPage);
    }

    public void doSaveDraft(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApprovalApplicationDto approvalApplicationDto = (ApprovalApplicationDto)ParamUtil.getSessionAttr(request, ApprovalApplicationConstants.APPROVAL_APPLICATION_DTO_ATTR);
        approvalApplicationDto.setStatus(ApprovalApplicationConstants.APP_STATUS_11);
        approvalApplicationClient.saveApproval(approvalApplicationDto);
    }

    public void doSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApprovalApplicationDto approvalApplicationDto = (ApprovalApplicationDto)ParamUtil.getSessionAttr(request, ApprovalApplicationConstants.APPROVAL_APPLICATION_DTO_ATTR);
        approvalApplicationDto.setStatus(ApprovalApplicationConstants.APP_STATUS_1);
        approvalApplicationClient.saveApproval(approvalApplicationDto);
        String crudActionTypeFormPage = (String) ParamUtil.getRequestAttr(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE);
        ParamUtil.setRequestAttr(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE,crudActionTypeFormPage);
        /*//upload documents
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        Facility facility = new Facility();
        facility.setId(approvalApplicationDto.getFacilityId());
        List<DocConfigDto> docConfigDtoList = (List<DocConfigDto>) request.getSession().getAttribute(PRIMARY_DOC_CONFIG);
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        Map<String, File> fileMap = new HashMap<>();
        for(int i =0;i<docConfigDtoList.size();i++){
            String docKey = i+"primaryDoc";
            fileMap = (Map<String, File>) ParamUtil.getSessionAttr(mulReq,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey);
            List<File> fileList = new ArrayList<>(fileMap.values());
            List<String> fileRepoIdList = comFileRepoClient.saveFileRepo(fileList);
            if (fileMap != null){
                int finalI = i;
                fileMap.forEach((k, v)->{
                    int index = k.indexOf(docKey);
                    String seqNumStr = k.substring(index+docKey.length());
                    int seqNum = -1;
                    try{
                        seqNum = Integer.parseInt(seqNumStr);
                    }catch (Exception e){
                        log.error(StringUtil.changeForLog("doc seq num can not parse to int"));
                    }
                    FacilityDoc facilityDoc = new FacilityDoc();
                    if(v != null){
                        long size = v.length() / 1024;
                        facilityDoc.setFacility(facility);
                        facilityDoc.setName(v.getName());
                        facilityDoc.setSize(size);
                        *//*facilityDoc.setFileRepoId();*//*
                        facilityDoc.setSubmitAt(new Date());
                        facilityDoc.setSubmitBy(loginContext.getUserName());
                        facilityDoc.setSeqNum(seqNum);
                        facilityDoc.setDocType(docConfigDtoList.get(finalI).getDocType());
                    }
                });
            }

        }*/
    }

    private ApprovalApplicationDto getDtoByForm(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String task = (String)ParamUtil.getSessionAttr(request,TASK_LIST);
        String facilityId = ParamUtil.getString(request, ApprovalApplicationConstants.FACILITY_ID);
        String facilityName = ParamUtil.getString(request, ApprovalApplicationConstants.FACILITY_NAME);
        String schedule = ParamUtil.getString(request, ApprovalApplicationConstants.SCHEDULE);
        String estimatedMaximumVolume = ParamUtil.getString(request, ApprovalApplicationConstants.ESTIMATED_MAXIMUM_VOLUME);
        String methodOrSystemUsedForLargeScaleProduction = ParamUtil.getString(request, ApprovalApplicationConstants.METHOD_OR_SYSTEM_USER_FOR_LARGE_SCALE_PRODUCTION);
        List<String> listOfAgentsOrToxinsList = stringArrayToList(ParamUtil.getStrings(request, ApprovalApplicationConstants.LIST_OF_AGENTS_OR_TOXINS));
        //preview biological info
        List<String> biologicalNameList = new ArrayList<>();
        if (listOfAgentsOrToxinsList != null){
            for (int i = 0; i < listOfAgentsOrToxinsList.size(); i++) {
                biologicalNameList.add(approvalApplicationClient.getBiologicalById(listOfAgentsOrToxinsList.get(i)).getEntity().getName());
            }
        }
        String biologicalName = listToString(biologicalNameList);
        List<String> natureOfTheSampleList = stringArrayToList(ParamUtil.getStrings(request, ApprovalApplicationConstants.NATURE_OF_THE_SAMPLE));
        String sampleNature = listToString(natureOfTheSampleList);
        String others = ParamUtil.getString(request, ApprovalApplicationConstants.OTHERS);
        String modeOfProcurement = ParamUtil.getString(request, ApprovalApplicationConstants.MODE_OF_PROCUREMENT);
        String transferFromFacilityName = ParamUtil.getString(request, ApprovalApplicationConstants.TRANSFER_FROM_FACILITY_NAME);
        String expectedDateOfTransfer = ParamUtil.getString(request, ApprovalApplicationConstants.EXPECTED_DATE_OF_TRANSFER);
        String contactPersonFromTransferringFacility = ParamUtil.getString(request, ApprovalApplicationConstants.CONTACT_PERSON_FROM_TRANSFERRING_FACILITY);
        String contactNoOfContactPersonFromTransferringFacility = ParamUtil.getString(request, ApprovalApplicationConstants.CONTACT_NO_OF_CONTACT_PERSON_FROM_TRANSFERRING_FACILITY);
        String emailAddressOfContactPersonFromTransferringFacility = ParamUtil.getString(request, ApprovalApplicationConstants.EMAIL_ADDRESS_OF_CONTACT_PERSON_FROM_TRANSFERRING_FACILITY);
        String overseasFacilityName = ParamUtil.getString(request, ApprovalApplicationConstants.OVERSEAS_FACILITY_NAME);
        String expectedDateOfImport = ParamUtil.getString(request, ApprovalApplicationConstants.EXPECTED_DATE_OF_IMPORT);
        String contactPersonFromSourceFacility = ParamUtil.getString(request, ApprovalApplicationConstants.CONTACT_PERSON_FROM_SOURCE_FACILITY);
        String emailAddressOfContactPersonFromSourceFacility = ParamUtil.getString(request, ApprovalApplicationConstants.EMAIL_ADDRESS_OF_CONTACT_PERSON_FROM_SOURCE_FACILITY);
        String facilityAddress1 = ParamUtil.getString(request, ApprovalApplicationConstants.FACILITY_ADDRESS_1);
        String facilityAddress2 = ParamUtil.getString(request, ApprovalApplicationConstants.FACILITY_ADDRESS_2);
        String facilityAddress3 = ParamUtil.getString(request, ApprovalApplicationConstants.FACILITY_ADDRESS_3);
        String country = ParamUtil.getString(request, ApprovalApplicationConstants.COUNTRY);
        String city = ParamUtil.getString(request, ApprovalApplicationConstants.CITY);
        String state = ParamUtil.getString(request, ApprovalApplicationConstants.STATE);
        String postalCode = ParamUtil.getString(request, ApprovalApplicationConstants.POSTAL_CODE);
        String nameOfCourierServiceProvider = ParamUtil.getString(request, ApprovalApplicationConstants.NAME_OF_COURIER_SERVICE_PROVIDER);
        String nameOfProject = ParamUtil.getString(request, ApprovalApplicationConstants.NAME_OF_PROJECT);
        String nameOfPrincipalInvestigator = ParamUtil.getString(request, ApprovalApplicationConstants.NAME_OF_PRINCIPAL_INVESTIGATOR);
        String intendedWorkActivity = ParamUtil.getString(request, ApprovalApplicationConstants.INTENDED_WORK_ACTIVITY);
        String startDate = ParamUtil.getString(request, ApprovalApplicationConstants.START_DATE);
        String endDate = ParamUtil.getString(request, ApprovalApplicationConstants.END_DATE);
        String remarks = ParamUtil.getString(request, ApprovalApplicationConstants.REMARKS);
        String checkbox1 = ParamUtil.getString(request,ApprovalApplicationConstants.CHECKBOX_1);
        String checkbox2 = ParamUtil.getString(request,ApprovalApplicationConstants.CHECKBOX_2);
        String appType = ApprovalApplicationConstants.APP_TYPE_1;
        Date applicationDt = new Date();
        String processType = "";
        if (task.equals(ApprovalApplicationConstants.APPROVAL_TYPE_1)){
            processType = ApprovalApplicationConstants.PROCESS_TYPE_2;
        }else if (task.equals(ApprovalApplicationConstants.APPROVAL_TYPE_2)){
            processType = ApprovalApplicationConstants.PROCESS_TYPE_3;
        }else if (task.equals(ApprovalApplicationConstants.APPROVAL_TYPE_3)){
            processType = ApprovalApplicationConstants.PROCESS_TYPE_4;
        }
        ApprovalApplicationDto approvalApplicationDto = new ApprovalApplicationDto();
        approvalApplicationDto.setApprovalType(task);
        approvalApplicationDto.setFacilityId(facilityId);
        approvalApplicationDto.setFacilityName(facilityName);
        approvalApplicationDto.setNatureOfTheSampleList(natureOfTheSampleList);
        approvalApplicationDto.setSampleNature(sampleNature);
        approvalApplicationDto.setSampleNatureOth(others);
        approvalApplicationDto.setProdMaxVolumeLitres(estimatedMaximumVolume);
        approvalApplicationDto.setLspMethod(methodOrSystemUsedForLargeScaleProduction);
        approvalApplicationDto.setPrjName(nameOfProject);
        approvalApplicationDto.setPrincipalInvestigatorName(nameOfPrincipalInvestigator);
        approvalApplicationDto.setWorkActivityIntended(intendedWorkActivity);
        approvalApplicationDto.setStartDate(Formatter.parseDate(startDate));
        approvalApplicationDto.setEndDate(Formatter.parseDate(endDate));
        approvalApplicationDto.setSchedule(schedule);
        approvalApplicationDto.setBiologicalIdList(listOfAgentsOrToxinsList);
        approvalApplicationDto.setBiologicalName(biologicalName);
        approvalApplicationDto.setProcurementMode(modeOfProcurement);
        approvalApplicationDto.setFacilityNameOfTransfer(transferFromFacilityName);
        approvalApplicationDto.setExpectedDateOfTransfer(Formatter.parseDate(expectedDateOfTransfer));
        approvalApplicationDto.setContactPersonNameOfTransfer(contactPersonFromTransferringFacility);
        approvalApplicationDto.setImpCtcPersonNo(contactNoOfContactPersonFromTransferringFacility);
        approvalApplicationDto.setContactPersonEmailOfTransfer(emailAddressOfContactPersonFromTransferringFacility);
        approvalApplicationDto.setFacilityNameOfImport(overseasFacilityName);
        approvalApplicationDto.setExpectedDateOfImport(Formatter.parseDate(expectedDateOfImport));
        approvalApplicationDto.setContactPersonNameOfImport(contactPersonFromSourceFacility);
        approvalApplicationDto.setContactPersonEmailOfImport(emailAddressOfContactPersonFromSourceFacility);
        approvalApplicationDto.setTransferFacAddr1(facilityAddress1);
        approvalApplicationDto.setTransferFacAddr2(facilityAddress2);
        approvalApplicationDto.setTransferFacAddr3(facilityAddress3);
        approvalApplicationDto.setTransferCountry(country);
        approvalApplicationDto.setTransferCity(city);
        approvalApplicationDto.setTransferState(state);
        approvalApplicationDto.setTransferPostalCode(postalCode);
        approvalApplicationDto.setCourierServiceProviderName(nameOfCourierServiceProvider);
        approvalApplicationDto.setRemarks(remarks);
        approvalApplicationDto.setCheckbox1(checkbox1);
        approvalApplicationDto.setCheckbox2(checkbox2);
        approvalApplicationDto.setAppType(appType);
        approvalApplicationDto.setProcessType(processType);
        approvalApplicationDto.setApplicationDt(applicationDt);
        return approvalApplicationDto;
    }

    private List<String> stringArrayToList(String[] strings){
        List<String> list = null;
        if (!StringUtil.isEmpty(strings)){
            list = Arrays.asList(strings);
        }
        return list;
    }

    private String listToString(List<String> list){
        StringBuilder stringBuilder = new StringBuilder();
        if (list != null && list.size() > 0){
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(list.get(i));
                if (i < list.size()-1){
                    stringBuilder.append(",");
                }
            }
        }
        return stringBuilder.toString();
    }

    private List<SelectOption> getSelectOptionList(String schedule){
        List<Biological> biological = approvalApplicationClient.getBiologicalBySchedule(schedule).getEntity();
        List<SelectOption> biologicalSchedule =  IaisCommonUtils.genNewArrayList();
        if (biological != null && biological.size() > 0){
            for (Biological dto : biological) {
                biologicalSchedule.add(new SelectOption(dto.getId(),dto.getName()));
            }
        }
        return biologicalSchedule;
    }


}

