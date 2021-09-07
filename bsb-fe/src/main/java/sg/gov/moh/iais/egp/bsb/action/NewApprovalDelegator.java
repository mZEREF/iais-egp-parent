package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sg.gov.moh.iais.egp.bsb.client.ApprovalApplicationClient;
import sg.gov.moh.iais.egp.bsb.client.service.AppSubmissionService;
import sg.gov.moh.iais.egp.bsb.client.service.ServiceConfigService;
import sg.gov.moh.iais.egp.bsb.constant.ApprovalApplicationConstants;
import sg.gov.moh.iais.egp.bsb.constant.RfcConst;
import sg.gov.moh.iais.egp.bsb.dto.approval.ApprovalApplicationDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.BiologicalQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.approval.FacilityQueryDto;
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sg.gov.moh.iais.egp.bsb.util.SingeFileUtil;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
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

    @Autowired
    private  ApprovalApplicationClient approvalApplicationClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    //
    public static final String CURRENTSERVICEID = "currentServiceId";
    public static final String APPSUBMISSIONDTO = "AppSubmissionDto";
    public static final String OLDAPPSUBMISSIONDTO = "oldAppSubmissionDto";
    public static final String RELOADAPPGRPPRIMARYDOCMAP = "reloadAppGrpPrimaryDocMap";
    public static final String APPGRPPRIMARYDOCERRMSGMAP = "appGrpPrimaryDocErrMsgMap";
    public static final String PRIMARY_DOC_CONFIG = "primaryDocConfig";

    public static final String REQUESTINFORMATIONCONFIG = "requestInformationConfig";

    //page name
    public static final String APPLICATION_PAGE_NAME_PRIMARY = "APPPN02";
    //isClickEdit
    public static final String IS_EDIT = "isEdit";

    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private AppSubmissionService appSubmissionService;

    public void doStart(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG,
                AuditTrailConsts.FUNCTION_ERROR_MESSAGES_MANAGEMENT);
        HttpServletRequest request = bpc.request;
        String task = ParamUtil.getString(request,TASK_LIST);
        ParamUtil.setSessionAttr(request, TASK_LIST, task);
        log.info("task is {}", task);
        IaisEGPHelper.clearSessionAttr(request, ApprovalApplicationConstants.class);
    }

    public void prepare(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String action = (String) ParamUtil.getRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_PAGE);
        if (StringUtil.isEmpty(action)) {
            action = "PrepareForms";
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
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, action);
        log.info(StringUtil.changeForLog("prepare(action):"+action));
    }

    public void prepareDocuments(BaseProcessClass bpc) {
//        HttpServletRequest request = bpc.request;
//        int sysFileSize = systemParamConfig.getUploadFileLimit();
//        ParamUtil.setRequestAttr(request, "sysFileSize", sysFileSize);
//        String sysFileType = systemParamConfig.getUploadFileType();
//        String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
//        StringBuilder fileTypeStr = new StringBuilder();
//        if (sysFileTypeArr != null) {
//            int i = 0;
//            int fileTypeLength = sysFileTypeArr.length;
//            for (String fileType : sysFileTypeArr) {
//                fileTypeStr.append(' ').append(fileType);
//                if (fileTypeLength > 1 && i < fileTypeLength - 2) {
//                    fileTypeStr.append(',');
//                }
//                if (fileTypeLength > 1 && i == sysFileTypeArr.length - 2) {
//                    fileTypeStr.append(" and");
//                }
//                if (i == fileTypeLength - 1) {
//                    fileTypeStr.append('.');
//                }
//                i++;
//            }
//        }
//        ParamUtil.setRequestAttr(request, "sysFileType", fileTypeStr.toString());
        log.info(StringUtil.changeForLog("the do prepareDocuments start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<HcsaSvcDocConfigDto> hcsaSvcDocDtos;
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        AppSubmissionDto oldAppSubDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = IaisCommonUtils.genNewArrayList();
        if(oldAppSubDto != null){
            appGrpPrimaryDocDtos = oldAppSubDto.getAppGrpPrimaryDocDtos();
        }
        if(isRfi && appGrpPrimaryDocDtos != null && appGrpPrimaryDocDtos.size() > 0){
            hcsaSvcDocDtos = serviceConfigService.getPrimaryDocConfigById(appGrpPrimaryDocDtos.get(0).getSvcComDocId());
        }else{
            hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(null);
        }
        ParamUtil.setSessionAttr(bpc.request,PRIMARY_DOC_CONFIG, (Serializable) hcsaSvcDocDtos);

        //reload page
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
        Map<String, List<AppGrpPrimaryDocDto>> reloadDocMap = IaisCommonUtils.genNewHashMap();
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtoList)){
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtoList){
                if(StringUtil.isEmpty(appGrpPrimaryDocDto.getFileRepoId()) && StringUtil.isEmpty(appGrpPrimaryDocDto.getDocName())){
                    continue;
                }
                String reloadDocMapKey;
                if(StringUtil.isEmpty(appGrpPrimaryDocDto.getPremisessName())){
                    reloadDocMapKey = appGrpPrimaryDocDto.getSvcComDocId();
                }else{
                    reloadDocMapKey = appGrpPrimaryDocDto.getPremisessName() + appGrpPrimaryDocDto.getSvcComDocId();
                }

                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos1 = reloadDocMap.get(reloadDocMapKey);
                if(IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos1)){
                    appGrpPrimaryDocDtos1 = IaisCommonUtils.genNewArrayList();
                }
                appGrpPrimaryDocDtos1.add(appGrpPrimaryDocDto);
                reloadDocMap.put(reloadDocMapKey,appGrpPrimaryDocDtos1);
            }
            //do sort

            reloadDocMap.forEach((k,v)->{
                Collections.sort(v,(s1,s2)->(
                        s1.getSeqNum().compareTo(s2.getSeqNum())
                ));
            });
        }
        ParamUtil.setSessionAttr(bpc.request,"docReloadMap", (Serializable) reloadDocMap);

        int sysFileSize = systemParamConfig.getUploadFileLimit();
        ParamUtil.setRequestAttr(bpc.request, "sysFileSize", sysFileSize);
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
        ParamUtil.setRequestAttr(bpc.request, "sysFileType", fileTypeStr.toString());
        log.info(StringUtil.changeForLog("the do prepareDocuments end ...."));
    }

    public void preparePreview(BaseProcessClass bpc) {
    }
    public void prepareJump(BaseProcessClass bpc) {
    }
    public void doDocuments(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doDocument start ...."));
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
        String crud_action_additional = mulReq.getParameter("crud_action_additional");

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);
        ParamUtil.setRequestAttr(bpc.request, crud_action_additional, crud_action_additional);
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        Map<String, CommonsMultipartFile> commonsMultipartFileMap = IaisCommonUtils.genNewHashMap();
        List<AppGrpPrimaryDocDto> newAppGrpPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        AppSubmissionDto oldSubmissionDto = NewApplicationHelper.getOldSubmissionDto(bpc.request,appSubmissionDto.getAppType());
        List<AppGrpPrimaryDocDto> oldPrimaryDocDtoList = oldSubmissionDto.getAppGrpPrimaryDocDtos();
        String action = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
            if (RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)) {
                //clear
                ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, null);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "jump");
                return;
            }
        }

        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request, REQUESTINFORMATIONCONFIG);
        boolean isRfi = false;
        if (requestInformationConfig != null) {
            isRfi = true;
        }
        String isEdit = ParamUtil.getString(mulReq, IS_EDIT);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SUPPORTING_DOCUMENT, isEdit, isRfi);

        //premIndexNo+configId+seqnum
        Map<String, File> saveFileMap = IaisCommonUtils.genNewHashMap();
        if (isGetDataFromPage) {
            List<AppGrpPremisesDto> appGrpPremisesList = appSubmissionDto.getAppGrpPremisesDtoList();
            if (appSubmissionDto.isNeedEditController()) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                clickEditPages.add(NewApplicationDelegator.APPLICATION_PAGE_NAME_PRIMARY);
                appSubmissionDto.setClickEditPage(clickEditPages);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                appEditSelectDto.setDpoEdit(true);
                appSubmissionDto.setChangeSelectDto(appEditSelectDto);
            }
            List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request,PRIMARY_DOC_CONFIG);
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
            for(int i =0;i<hcsaSvcDocConfigDtos.size();i++){
                HcsaSvcDocConfigDto hcsaSvcDocConfigDto = hcsaSvcDocConfigDtos.get(i);
                String dupForPrem = hcsaSvcDocConfigDto.getDupForPrem();
                if("0".equals(dupForPrem)){
                    String docKey = i+"primaryDoc";
                    Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(mulReq,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey);
                    genPrimaryDoc(fileMap,docKey,hcsaSvcDocConfigDto,saveFileMap,appGrpPrimaryDocDtos,newAppGrpPrimaryDocDtoList,"","",isRfi,oldPrimaryDocDtoList,oldSubmissionDto.getAppGrpId(),oldSubmissionDto.getRfiAppNo(),appSubmissionDto.getAppType(),dupForPrem);
                    ParamUtil.setSessionAttr(bpc.request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey, (Serializable) fileMap);
                }else if("1".equals(dupForPrem)){
                    for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesList){
                        String docKey = i+"primaryDoc" + appGrpPremisesDto.getPremisesIndexNo();
                        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(mulReq,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey);
                        genPrimaryDoc(fileMap,docKey,hcsaSvcDocConfigDto,saveFileMap,appGrpPrimaryDocDtos,newAppGrpPrimaryDocDtoList,appGrpPremisesDto.getPremisesIndexNo(),appGrpPremisesDto.getPremisesType(),isRfi,oldPrimaryDocDtoList,oldSubmissionDto.getAppGrpId(),oldSubmissionDto.getRfiAppNo(),appSubmissionDto.getAppType(),dupForPrem);
                        ParamUtil.setSessionAttr(bpc.request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey, (Serializable) fileMap);
                    }
                }
            }
            //set value into AppSubmissionDto
            appSubmissionDto.setAppGrpPrimaryDocDtos(newAppGrpPrimaryDocDtoList);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        }

        String crud_action_values = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if ("next".equals(crud_action_values)) {
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionService.documentValid(bpc.request, errorMap,true);
            doIsCommom(bpc.request, errorMap);
            HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
            if (errorMap.isEmpty()) {
                coMap.put("document", "document");
            } else {
                coMap.put("document", "");
            }
            saveFileAndSetFileId(appGrpPrimaryDocDtos,saveFileMap);
            appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);
            ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);

            bpc.request.getSession().setAttribute("coMap", coMap);
        }else{
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionService.documentValid(bpc.request, errorMap,true);
            doIsCommom(bpc.request, errorMap);
            saveFileAndSetFileId(appGrpPrimaryDocDtos,saveFileMap);
            appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);
            ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
            errorMap = IaisCommonUtils.genNewHashMap();
        }
        if (errorMap.size() > 0) {
            //set audit
            bpc.request.setAttribute("errormapIs", "error");
            NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setSessionAttr(bpc.request, APPGRPPRIMARYDOCERRMSGMAP, (Serializable) errorMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "documents");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, "documents");
            return;
        }

        log.info(StringUtil.changeForLog("the do doDocument end ...."));
    }
    public void doPreview(BaseProcessClass bpc) {
    }

    public void prepareForms(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String task = (String)ParamUtil.getSessionAttr(request,TASK_LIST);
        List<FacilityQueryDto> facilityByApprovalStatus = approvalApplicationClient.getFacilityByApprovalType(task).getEntity();
        List<SelectOption> facilityNameList =  new ArrayList<>();
        for (FacilityQueryDto dto : facilityByApprovalStatus) {
            facilityNameList.add(new SelectOption(dto.getId(),dto.getFacilityName()));
        }
        ParamUtil.setRequestAttr(request, ApprovalApplicationConstants.FACILITY_NAME_SELECT, facilityNameList);
    }

    public void doForms(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        ApprovalApplicationDto approvalApplicationDto = getDtoByForm(bpc);
        ParamUtil.setSessionAttr(request,ApprovalApplicationConstants.APPROVAL_APPLICATION_DTO_ATTR, approvalApplicationDto);
        ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
    }

    public void controlSwitch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String crud_action_type_form_page = ParamUtil.getString(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE);
        String crud_action_type = ParamUtil.getString(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE);
        ParamUtil.setRequestAttr(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE_FROM_PAGE,crud_action_type_form_page);
        ParamUtil.setRequestAttr(request,ApprovalApplicationConstants.CRUD_ACTION_TYPE,crud_action_type);
    }

    public void doSaveDraft(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApprovalApplicationDto approvalApplicationDto = (ApprovalApplicationDto)ParamUtil.getSessionAttr(request, ApprovalApplicationConstants.APPROVAL_APPLICATION_DTO_ATTR);
        String status = ApprovalApplicationConstants.APP_STATUS_11;
        approvalApplicationDto.setStatus(status);
        approvalApplicationClient.saveApproval(approvalApplicationDto);
    }

    public void doSubmit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApprovalApplicationDto approvalApplicationDto = (ApprovalApplicationDto)ParamUtil.getSessionAttr(request, ApprovalApplicationConstants.APPROVAL_APPLICATION_DTO_ATTR);
        String status = ApprovalApplicationConstants.APP_STATUS_1;
        approvalApplicationDto.setStatus(status);
        approvalApplicationClient.saveApproval(approvalApplicationDto);
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
        Facility facility = new Facility();
        facility.setId(facilityId);
        facility.setFacilityName(facilityName);
        approvalApplicationDto.setFacility(facility);
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
        approvalApplicationDto.setProcurementMode(modeOfProcurement);
        approvalApplicationDto.setBiologicalName(biologicalName);
        if (modeOfProcurement != null){
            if (modeOfProcurement.equals(ApprovalApplicationConstants.MODE_OF_PROCUREMENT_1)){
                approvalApplicationDto.setFacTransferForm(transferFromFacilityName);
                approvalApplicationDto.setTransferExpectedDate(Formatter.parseDate(expectedDateOfTransfer));
                approvalApplicationDto.setImpCtcPersonName(contactPersonFromTransferringFacility);
                approvalApplicationDto.setImpCtcPersonNo(contactNoOfContactPersonFromTransferringFacility);
                approvalApplicationDto.setImpCtcPersonEmail(emailAddressOfContactPersonFromTransferringFacility);
            }else if(modeOfProcurement.equals(ApprovalApplicationConstants.MODE_OF_PROCUREMENT_2)){
                approvalApplicationDto.setFacTransferForm(overseasFacilityName);
                approvalApplicationDto.setTransferExpectedDate(Formatter.parseDate(expectedDateOfImport));
                approvalApplicationDto.setImpCtcPersonName(contactPersonFromSourceFacility);
                approvalApplicationDto.setImpCtcPersonEmail(emailAddressOfContactPersonFromSourceFacility);
            }
        }
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
        List<BiologicalQueryDto> biological = approvalApplicationClient.getBiologicalBySchedule(schedule).getEntity();
        List<SelectOption> biologicalSchedule =  IaisCommonUtils.genNewArrayList();
        if (biological != null && biological.size() > 0){
            for (BiologicalQueryDto dto : biological) {
                biologicalSchedule.add(new SelectOption(dto.getId(),dto.getName()));
            }
        }
        return biologicalSchedule;
    }

    //
    private static AppSubmissionDto getAppSubmissionDto(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        if (appSubmissionDto == null) {
            log.info(StringUtil.changeForLog("appSubmissionDto is empty "));
            appSubmissionDto = new AppSubmissionDto();
            ParamUtil.setSessionAttr(request, APPSUBMISSIONDTO, appSubmissionDto);
        }
        return appSubmissionDto;
    }

    private void genPrimaryDoc(Map<String, File> fileMap,String docKey,HcsaSvcDocConfigDto hcsaSvcDocConfigDto,
                               Map<String,File> saveFileMap,List<AppGrpPrimaryDocDto> currDocDtoList,List<AppGrpPrimaryDocDto> newAppGrpPrimaryDocDtoList,
                               String premVal,String premType,boolean isRfi,List<AppGrpPrimaryDocDto> oldPrimaryDotList,String appGrpId,String appNo,String appType,String dupForPrem){
        if(fileMap != null){
            fileMap.forEach((k,v)->{
                int index = k.indexOf(docKey);
                String seqNumStr = k.substring(index+docKey.length());
                int seqNum = -1;
                try{
                    seqNum = Integer.parseInt(seqNumStr);
                }catch (Exception e){
                    log.error(StringUtil.changeForLog("doc seq num can not parse to int"));
                }
                AppGrpPrimaryDocDto primaryDocDto = new AppGrpPrimaryDocDto();
                if(v != null){
                    primaryDocDto.setSvcComDocId(hcsaSvcDocConfigDto.getId());
                    primaryDocDto.setSvcComDocName(hcsaSvcDocConfigDto.getDocTitle());
                    primaryDocDto.setDocName(v.getName());
                    primaryDocDto.setRealDocSize(v.length());
                    long size = v.length() / 1024;
                    primaryDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                    String md5Code = SingeFileUtil.getInstance().getFileMd5(v);
                    primaryDocDto.setMd5Code(md5Code);
                    //if  common ==> set null
                    primaryDocDto.setPremisessName(premVal);
                    primaryDocDto.setPremisessType(premType);
                    primaryDocDto.setSeqNum(seqNum);
                    primaryDocDto.setVersion(getAppGrpPrimaryDocVersion(hcsaSvcDocConfigDto.getId(),oldPrimaryDotList,isRfi,md5Code,appGrpId,appNo,appType,seqNum,dupForPrem));
                    saveFileMap.put(premVal+hcsaSvcDocConfigDto.getId()+seqNum,v);
                }else{
                    primaryDocDto = getAppGrpPrimaryDocByConfigIdAndSeqNum(currDocDtoList,hcsaSvcDocConfigDto.getId(),seqNum,premVal,premType);
                }
                //the data is retrieved from the DTO a second time
                fileMap.put(k,null);
                if(primaryDocDto != null){
                    newAppGrpPrimaryDocDtoList.add(primaryDocDto);
                }
            });
        }
    }

    private void doIsCommom(HttpServletRequest request, Map<String, String> errorMap) {

        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
        String err006 = MessageUtil.replaceMessage("GENERAL_ERR0006", "Document", "field");
        List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>) request.getSession().getAttribute(PRIMARY_DOC_CONFIG);
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        if(!IaisCommonUtils.isEmpty(commonHcsaSvcDocConfigList) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            int i = 0;
            String suffix = "Error";
            for (HcsaSvcDocConfigDto comm : commonHcsaSvcDocConfigList) {
                String errKey = i+"primaryDoc";
                Boolean isMandatory = comm.getIsMandatory();
                String dupForPrem = comm.getDupForPrem();
                String configId = comm.getId();
                i++;
                if(!isMandatory){
                    continue;
                }
                if(IaisCommonUtils.isEmpty(appGrpPrimaryDocDtoList)){
                    appGrpPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
                }
                if("0".equals(dupForPrem)){
                    AppGrpPrimaryDocDto appGrpPrimaryDocDto =getAppGrpPrimaryDocByConfigIdAndPremIndex(appGrpPrimaryDocDtoList,configId,"");
                    if(appGrpPrimaryDocDto == null){
                        errorMap.put(errKey+suffix, err006);
                    }
                }else if("1".equals(dupForPrem)){
                    for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                        String premIndex = appGrpPremisesDto.getPremisesIndexNo();
                        String currErrKey = errKey + premIndex +suffix;
                        AppGrpPrimaryDocDto appGrpPrimaryDocDto =getAppGrpPrimaryDocByConfigIdAndPremIndex(appGrpPrimaryDocDtoList,configId,premIndex);
                        if(appGrpPrimaryDocDto == null){
                            errorMap.put(currErrKey, err006);
                        }
                    }
                }
            }
        }
    }

    private void saveFileAndSetFileId(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList,Map<String,File> saveFileMap){
        Map<String,File> passValidateFileMap = IaisCommonUtils.genNewHashMap();
        for (AppGrpPrimaryDocDto primaryDocDto : appGrpPrimaryDocDtoList) {
            if(primaryDocDto.isPassValidate()){
                String premIndex = "";
                if(!StringUtil.isEmpty(primaryDocDto.getPremisessName())){
                    premIndex = primaryDocDto.getPremisessName();
                }
                String fileMapKey = premIndex + primaryDocDto.getSvcComDocId() + primaryDocDto.getSeqNum();
                File file = saveFileMap.get(fileMapKey);
                if(file != null){
                    passValidateFileMap.put(fileMapKey,file);
                }
            }
        }
        if(passValidateFileMap.size() > 0){
            List<File> fileList = new ArrayList<>(passValidateFileMap.values());
            List<String> fileRepoIdList = appSubmissionService.saveFileList(fileList);
            int i = 0;
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtoList){
                String premIndexNo = appGrpPrimaryDocDto.getPremisessName();
                if(StringUtil.isEmpty(premIndexNo)){
                    premIndexNo = "";
                }
                String saveFileMapKey = premIndexNo+appGrpPrimaryDocDto.getSvcComDocId()+appGrpPrimaryDocDto.getSeqNum();
                File file = saveFileMap.get(saveFileMapKey);
                if(file != null){
                    appGrpPrimaryDocDto.setFileRepoId(fileRepoIdList.get(i));
                    i++;
                }
            }
        }
    }

    private Integer getAppGrpPrimaryDocVersion(String configDocId,List<AppGrpPrimaryDocDto> oldDocs,boolean isRfi,String md5Code,String appGrpId,String appNo,String appType,int seqNum,String dupForPrem){
        log.info(StringUtil.changeForLog("AppGrpPrimaryDocVersion start..."));
        Integer version = 1;
        if(StringUtil.isEmpty(configDocId) || IaisCommonUtils.isEmpty(oldDocs) || StringUtil.isEmpty(md5Code)){
            return version;
        }
        log.info(StringUtil.changeForLog("isRfi:"+isRfi));
        log.info(StringUtil.changeForLog("appType:"+appType));
        log.info(StringUtil.changeForLog("seqNum:"+seqNum));
        if(isRfi){
            log.info(StringUtil.changeForLog("rfi appNo:"+appNo));
            boolean canFound = false;
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:oldDocs){
                Integer oldVersion = appGrpPrimaryDocDto.getVersion();
                if(configDocId.equals(appGrpPrimaryDocDto.getSvcComDocId()) && seqNum == appGrpPrimaryDocDto.getSeqNum()){
                    canFound = true;
                    if(MessageDigest.isEqual(md5Code.getBytes(StandardCharsets.UTF_8),
                            appGrpPrimaryDocDto.getMd5Code().getBytes(StandardCharsets.UTF_8))){
                        if(!StringUtil.isEmpty(oldVersion)){
                            version = oldVersion;
                        }
                    }else{
                        version = getVersion(appGrpId,configDocId,appNo,appType,dupForPrem,seqNum);
                    }
                    break;
                }
            }
            if(!canFound){
                //last doc is null new rfi not use app no
                version = getVersion(appGrpId,configDocId,appNo,appType,dupForPrem,seqNum);
            }
        }
        log.info(StringUtil.changeForLog("AppGrpPrimaryDocVersion end..."));
        return version;
    }

    private static AppGrpPrimaryDocDto getAppGrpPrimaryDocByConfigIdAndSeqNum(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,String configId,int seqNum,String premVal,String premType){
        log.debug(StringUtil.changeForLog("getAppGrpPrimaryDocByConfigIdAndSeqNum start..."));
        AppGrpPrimaryDocDto appGrpPrimaryDocDto = null;
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
            log.debug("configId is {}", configId);
            log.debug("seqNum is {}", seqNum);
            log.debug("premIndex is {}", premVal);
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto1:appGrpPrimaryDocDtos){
                String currPremVal = "";
                String currPremType = "";
                if(!StringUtil.isEmpty(appGrpPrimaryDocDto1.getPremisessName())){
                    currPremVal = appGrpPrimaryDocDto1.getPremisessName();
                }
                if(!StringUtil.isEmpty(appGrpPrimaryDocDto1.getPremisessType())){
                    currPremType = appGrpPrimaryDocDto1.getPremisessType();
                }
                if(!StringUtil.isEmpty(appGrpPrimaryDocDto1.getFileRepoId())
                        && configId.equals(appGrpPrimaryDocDto1.getSvcComDocId())
                        && seqNum == appGrpPrimaryDocDto1.getSeqNum()
                        && premVal.equals(currPremVal)
                        && premType.equals(currPremType)){
                    appGrpPrimaryDocDto = (AppGrpPrimaryDocDto) CopyUtil.copyMutableObject(appGrpPrimaryDocDto1);
                    break;
                }
            }
        }
        return appGrpPrimaryDocDto;
    }

    private AppGrpPrimaryDocDto getAppGrpPrimaryDocByConfigIdAndPremIndex(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,String config,String premIndex){
        AppGrpPrimaryDocDto appGrpPrimaryDocDto = null;
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto1:appGrpPrimaryDocDtos){
                String currPremVal = appGrpPrimaryDocDto1.getPremisessName();
                if(StringUtil.isEmpty(currPremVal)){
                    currPremVal = "";
                }
                if(config.equals(appGrpPrimaryDocDto1.getSvcComDocId()) && premIndex.equals(currPremVal)){
                    appGrpPrimaryDocDto = appGrpPrimaryDocDto1;
                    break;
                }
            }
        }
        return appGrpPrimaryDocDto;
    }

    private Integer getVersion(String appGrpId,String configDocId,String appNo,String appType,String dupForPrem,int seqNum){
        Integer version = 1;

        //common doc
        if("0".equals(dupForPrem)){
            if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                AppGrpPrimaryDocDto maxVersionDocDto = appSubmissionService.getMaxVersionPrimaryComDoc(appGrpId,configDocId,String.valueOf(seqNum));
                Integer maxVersion = maxVersionDocDto.getVersion();
                String fileRepoId = maxVersionDocDto.getFileRepoId();
                if(!StringUtil.isEmpty(maxVersion) &&  !StringUtil.isEmpty(fileRepoId)){
                    version = maxVersionDocDto.getVersion() + 1;
                }
            }else{
                AppSvcDocDto maxVersionDocDto = appSubmissionService.getMaxVersionSvcComDoc(appGrpId,configDocId,String.valueOf(seqNum));
                if(!StringUtil.isEmpty(maxVersionDocDto.getVersion()) && !StringUtil.isEmpty(maxVersionDocDto.getFileRepoId())){
                    version = maxVersionDocDto.getVersion() + 1;
                }
            }

        }else if("1".equals(dupForPrem)){
            if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                AppGrpPrimaryDocDto maxVersionDocDto = appSubmissionService.getMaxVersionPrimarySpecDoc(appGrpId,configDocId,appNo,String.valueOf(seqNum));
                if(!StringUtil.isEmpty(maxVersionDocDto.getVersion()) && !StringUtil.isEmpty(maxVersionDocDto.getFileRepoId())){
                    version = maxVersionDocDto.getVersion() + 1;
                }
            }else{
                AppSvcDocDto searchDto = new AppSvcDocDto();
                searchDto.setAppGrpId(appGrpId);
                searchDto.setSvcDocId(configDocId);
                searchDto.setSeqNum(seqNum);
                AppSvcDocDto maxVersionDocDto = appSubmissionService.getMaxVersionSvcSpecDoc(searchDto,appNo);
                if(!StringUtil.isEmpty(maxVersionDocDto.getVersion()) && !StringUtil.isEmpty(maxVersionDocDto.getFileRepoId())){
                    version = maxVersionDocDto.getVersion() + 1;
                }
            }
        }
        return version;
    }
}

