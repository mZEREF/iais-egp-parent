package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AnswerForDifDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.ExcelSheetDto;
import com.ecquaria.cloud.moh.iais.dto.FileErrorMsg;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelReader;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.egp.bsb.client.InspectionClient;
import sg.gov.moh.iais.egp.bsb.client.InternalDocClient;
import sg.gov.moh.iais.egp.bsb.client.OrganizationClient;
import sg.gov.moh.iais.egp.bsb.constant.ChecklistConstants;
import sg.gov.moh.iais.egp.bsb.constant.DocConstants;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.ChklstItemAnswerDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.InsChklItemExcelDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionChecklistDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsSubmitFindingDataDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APP_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_INS_CHECKLIST_DTO;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_RESULT_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ROUTE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_SEPARATOR;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_TASK_ID;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.SHEET_NAME_BSB;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.SHEET_NAME_COMMON;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_ACTIVE;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.TAB_PROCESSING;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_TYPE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_FACILITY_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ROUTING_HISTORY_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_SUBMISSION_DETAILS_INFO;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_VALIDATION_ERRORS;


@Slf4j
@Delegator("bsbInspectionDO")
public class InspectionDODelegator {
    private final InspectionClient inspectionClient;
    private final InternalDocClient internalDocClient;
    private final OrganizationClient organizationClient;
    private final InspectionService inspectionService;
    private static final String SERLISTDTO="serListDto";

    private static final String INSPECTION_ADHOC_CHECKLIST_LIST_ATTR  = "inspection_adhoc_checklist_list_attr";
    private static final String INSPECTION_USERS = "inspectorsParticipant";
    private static final String INSPECTION_USER_FINISH = "inspectorUserFinishChecklistId";
    private static final String BEFORE_FINISH_CHECK_LIST = "inspectionNcCheckListDelegator_before_finish_check_list";
    private static final String MOBILE_REMARK_GROUP = "mobile_remark_group";
    @Autowired
    public InspectionDODelegator(InspectionClient inspectionClient, InternalDocClient internalDocClient, OrganizationClient organizationClient, InspectionService inspectionService) {
        this.inspectionClient = inspectionClient;
        this.internalDocClient = internalDocClient;
        this.organizationClient = organizationClient;
        this.inspectionService = inspectionService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MaskHelper.taskProcessUnmask(request, KEY_APP_ID, KEY_TASK_ID);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_INSPECTION_CHECKLIST);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        HttpSession session = request.getSession();
        session.removeAttribute(KEY_SUBMISSION_DETAILS_INFO);
        session.removeAttribute(KEY_FACILITY_DETAILS_INFO);
        session.removeAttribute(KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST);
        session.removeAttribute(KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST);
        session.removeAttribute(KEY_ROUTING_HISTORY_LIST);
        session.removeAttribute(KEY_INS_CHECKLIST_DTO);


        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);

        InsSubmitFindingDataDto initDataDto = inspectionClient.getInitInsFindingData(appId);

        // facility info and inspection info
        SubmissionDetailsInfo infoDto = initDataDto.getSubmissionDetailsInfo();
        ParamUtil.setSessionAttr(request, KEY_SUBMISSION_DETAILS_INFO, infoDto);


        FacilityDetailsInfo detailsInfo = initDataDto.getFacilityDetailsInfo();
        ParamUtil.setSessionAttr(request, KEY_FACILITY_DETAILS_INFO, detailsInfo);

        List<DocDisplayDto> supportDocList = initDataDto.getSupportDocDisplayDtoList();
        ParamUtil.setSessionAttr(request,KEY_TAB_DOCUMENT_SUPPORT_DOC_LIST,new ArrayList<>(supportDocList));

        List<ProcessHistoryDto> processHistoryDtoList = initDataDto.getProcessHistoryDtoList();
        ParamUtil.setSessionAttr(request,KEY_ROUTING_HISTORY_LIST,new ArrayList<>(processHistoryDtoList));

        setCheckListUnFinishedTask(request,appId);

    }

    private void setCheckListUnFinishedTask(HttpServletRequest request,String appId){


        //get  commonDto draft
        List<String> ids=IaisCommonUtils.genNewArrayList();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        ids.add(loginContext.getUserId());
        List<OrgUserDto> orgUserDtoUsers = organizationClient.retrieveOrgUserAccount(ids).getEntity();
        List<InspectionFillCheckListDto> cDtoList =  inspectionService.getServiceChkDtoListByAppPremId(appId,"service",false);
        InspectionFDtosDto inspectionFDtosDto=new InspectionFDtosDto();
        inspectionFDtosDto.setFdtoList(cDtoList);

        inspectionService.getInspectionFillCheckListDtoByInspectionFillCheckListDto(cDtoList.get(0),orgUserDtoUsers);
        ParamUtil.setSessionAttr(request, INSPECTION_USER_FINISH, IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        ParamUtil.setSessionAttr(request,SERLISTDTO,inspectionFDtosDto);
        ParamUtil.setSessionAttr(request,INSPECTION_USERS, (Serializable) orgUserDtoUsers);
        InspectionChecklistDto inspectionChecklistDto=inspectionClient.getCombinedChkList(appId).getBody();
        if(inspectionChecklistDto==null){
            ChecklistConfigDto dto = inspectionClient.getMaxVersionChecklistConfig(appId, HcsaChecklistConstants.INSPECTION);
            inspectionChecklistDto=new InspectionChecklistDto();
            inspectionChecklistDto.setApplicationId(appId);
            inspectionChecklistDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            inspectionChecklistDto.setUserId(loginContext.getLoginId());
            inspectionChecklistDto.setVersion(1);
            inspectionChecklistDto.setChkLstConfigId(dto.getId());

        }
        ParamUtil.setSessionAttr(request, KEY_INS_CHECKLIST_DTO, inspectionChecklistDto);

    }

    public void pre(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        List<DocDisplayDto> internalDocDisplayDto = internalDocClient.getInternalDocForDisplay(appId);
        ParamUtil.setRequestAttr(request, KEY_TAB_DOCUMENT_INTERNAL_DOC_LIST, internalDocDisplayDto);
    }

    public void bindAction(BaseProcessClass bpc) {
        // do nothing now
    }

    public void preChecklist(BaseProcessClass bpc) {
        // do nothing now
    }

    public void changeTab(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        log.info("----------changeTab --");
        String nowTabIn = ParamUtil.getString(request,"nowTabIn");
        ParamUtil.setRequestAttr(request, "nowTabIn",  nowTabIn);
        String nowComTabIn = ParamUtil.getString(request,"nowComTabIn");
        ParamUtil.setRequestAttr(request, "nowComTabIn",  nowComTabIn);
        changeDataForCheckList(request);
    }
    public void changeDataForCheckList(HttpServletRequest request){
        getCommonCheckListMoreData(request);


        setRate(request);
    }

    public void getCommonCheckListMoreData(HttpServletRequest request){
        InspectionFDtosDto serListDto = (InspectionFDtosDto) ParamUtil.getSessionAttr(request,SERLISTDTO);
        InspectionFillCheckListDto comDto = serListDto.getFdtoList().get(0);
        String userId = (String)ParamUtil.getSessionAttr(request, INSPECTION_USER_FINISH);
        if(comDto != null && comDto.getCheckList()!=null&& !comDto.getCheckList().isEmpty()){
            List<InspectionCheckQuestionDto> checkList = comDto.getCheckList();
            for(InspectionCheckQuestionDto inspectionCheckQuestionDto : checkList){
                String prefix = inspectionCheckQuestionDto.getSectionNameShow()+inspectionCheckQuestionDto.getItemId();

                List<AnswerForDifDto> answerForDifDtos = inspectionCheckQuestionDto.getAnswerForDifDtos();
                int index = 0;
                for(AnswerForDifDto answerForDifDto : answerForDifDtos){
                    if(userId.equalsIgnoreCase(answerForDifDto.getSubmitId())){
                        String answer =  ParamUtil.getString(request,prefix +"comradIns" + index);
                        String remark = ParamUtil.getString(request,prefix +"comremarkIns" + index);
                        String raf =  ParamUtil.getString(request,prefix +"comrecIns" + index);
                        String ncs  = ParamUtil.getString(request,prefix +"comFindNcsIns" + index);
                        answerForDifDto.setNcs(ncs);
                        answerForDifDto.setAnswer(answer);
                        answerForDifDto.setRemark(remark);
                        if("No".equalsIgnoreCase(answer) && "rec".equalsIgnoreCase(raf)){
                            answerForDifDto.setIsRec("1");
                        }else {
                            answerForDifDto.setIsRec("0");
                        }
                        break;
                    }
                    index++;
                }
                inspectionService.setInspectionCheckQuestionDtoByAnswerForDifDtosAndDeconflict(inspectionCheckQuestionDto,answerForDifDtos,ParamUtil.getString(request,prefix +"Deconflict"));
            }
            ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        }
    }
    public void setRate(HttpServletRequest request){
        InspectionFDtosDto serListDto = (InspectionFDtosDto) ParamUtil.getSessionAttr(request,SERLISTDTO);

        inspectionService.getRateOfCheckList(serListDto);

        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
    }


    public void saveDraft(BaseProcessClass bpc) {
        // do nothing now
    }

    public void validateAndSaveChecklist(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArrayList<ChklstItemAnswerDto> answerDtos = IaisCommonUtils.genNewArrayList();
        getCommonCheckListMoreData(request);
        InspectionFDtosDto serListDto = (InspectionFDtosDto) ParamUtil.getSessionAttr(request,SERLISTDTO);
        InspectionFillCheckListDto comDto = serListDto.getFdtoList().get(0);
        String userId = (String)ParamUtil.getSessionAttr(request, INSPECTION_USER_FINISH);
        if(comDto != null && comDto.getCheckList()!=null&& !comDto.getCheckList().isEmpty()){
            List<InspectionCheckQuestionDto> checkList = comDto.getCheckList();
            for(InspectionCheckQuestionDto inspectionCheckQuestionDto : checkList){

                List<AnswerForDifDto> answerForDifDtos = inspectionCheckQuestionDto.getAnswerForDifDtos();
                for(AnswerForDifDto answerForDifDto : answerForDifDtos){
                    if(userId.equalsIgnoreCase(answerForDifDto.getSubmitId())){
                        ChklstItemAnswerDto answerDto=new ChklstItemAnswerDto();
                        answerDto.setAnswer(answerForDifDto.getAnswer());
                        answerDto.setFindings(answerForDifDto.getNcs());
                        answerDto.setActionRequired(answerForDifDto.getRemark());
                        answerDto.setConfigId(inspectionCheckQuestionDto.getConfigId());
                        answerDto.setItemId(inspectionCheckQuestionDto.getItemId());
                        answerDto.setSectionId(inspectionCheckQuestionDto.getSectionId());
                        answerDto.setRemarks(answerForDifDto.getRemark());
                        answerDto.setRectified(answerForDifDto.getIsRec().equals("1"));
                        answerDtos.add(answerDto);
                        break;
                    }
                }
            }
        }


        ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        InspectionChecklistDto checklistDto= (InspectionChecklistDto) ParamUtil.getSessionAttr(request,KEY_INS_CHECKLIST_DTO);


        checklistDto.setAnswer(answerDtos);
        //inspectionClient.saveCombinedChkList(checklistDto);
        // do nothing now
    }

    public void saveInsFinding(BaseProcessClass bpc) {
        // do nothing now
    }

    public void saveInsOutcome(BaseProcessClass bpc) {
        // do nothing now
    }

    public void handleSubmit(BaseProcessClass bpc) {
        log.info("Officer submit for inspection findings step");
        HttpServletRequest request = bpc.request;
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        ValidationResultDto validationResultDto = inspectionClient.validateActualInspectionFindings(appId);
        String route;
        if (validationResultDto.isPass() || (StringUtils.hasLength(actionValue) && "noValidate".equals(actionValue))) {
            String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
            inspectionClient.submitInspectionFindingChangeStatusToReport(appId, taskId);
            ParamUtil.setRequestAttr(request, KEY_RESULT_MSG, "You have successfully completed your task");
            route = "submit";
        } else {
            log.info("But not all necessary data has been submitted");
            ParamUtil.setRequestAttr(request, KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            ParamUtil.setRequestAttr(request, TAB_ACTIVE, TAB_PROCESSING);
            route = "back";
        }
        ParamUtil.setRequestAttr(request, KEY_ROUTE, route);
    }

    public void skip(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        String taskId = (String) ParamUtil.getSessionAttr(request, KEY_TASK_ID);
        inspectionClient.skipInspection(appId,taskId,new InsProcessDto(MasterCodeConstants.MOH_PROCESSING_DECISION_SKIP_INSPECTION));
    }

    /**
     * Step: PrepareUpload
     * @param bpc
     */
    public void prepareUpload(BaseProcessClass bpc) {
        log.info("----- PrepareUpload -----");
        HttpServletRequest request = bpc.request;
        InspectionChecklistDto checklistDto = new InspectionChecklistDto();
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        checklistDto.setApplicationId(appId);
        ChecklistConfigDto configDto = inspectionClient.getMaxVersionChecklistConfig(appId, HcsaChecklistConstants.INSPECTION);
        checklistDto.setChkLstConfigId(configDto.getId());
        checklistDto.setVersion(1);
        checklistDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        checklistDto.setUserId(loginContext.getUserId());
        ParamUtil.setSessionAttr(request, KEY_INS_CHECKLIST_DTO, checklistDto);
        ParamUtil.clearSession(request, InspectionConstants.SEESION_FILES_MAP_AJAX);
    }

    /**
     * Step: DoUpload
     * @param bpc
     */
    public void doUpload(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String actionType = ParamUtil.getString(request, KEY_ACTION_TYPE);
        log.info(StringUtil.changeForLog("The Action Type: " + actionType));
        if (ModuleCommonConstants.KEY_NAV_BACK.equals(actionType)) {
            ParamUtil.setSessionAttr(request, InspectionConstants.ACTION_UPLOAD_TYPE, ModuleCommonConstants.KEY_NAV_BACK);
            return;
        }
        InspectionChecklistDto checklistDto = (InspectionChecklistDto) ParamUtil.getSessionAttr(request, KEY_INS_CHECKLIST_DTO);

        ArrayList<ChklstItemAnswerDto> answerDtos = IaisCommonUtils.genNewArrayList();
        List<FileErrorMsg> errorMsgs = IaisCommonUtils.genNewArrayList();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        NewDocInfo fileInfo = getFileInfo(request);
        if (fileInfo == null) {
            errorMap.put("checklistData", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
        } else if (fileInfo.getSize() == 0) {
            errorMap.put("checklistData", "Could not parse file content.");
        }  else if (!FileUtils.isExcel(fileInfo.getFilename())) {
            errorMap.put("checklistData", MessageUtil.replaceMessage("GENERAL_ERR0018", "XLSX", "fileType"));
        } else {
            Map<String, List<ChklstItemAnswerDto>> result = transformToChklstItemAnswerDtos(fileInfo);
            List<ChklstItemAnswerDto> bsbData = result.get(InspectionConstants.SHEET_NAME_BSB);
            Boolean isValid = validateChklItemExcelDto(bsbData, SHEET_NAME_BSB, checklistDto.getChkLstConfigId(),
                    errorMsgs);
            if (isValid != null && isValid) {
                answerDtos.addAll(bsbData);
            }
            if (isValid == null) {
                errorMap.put("checklistData", "Could not parse file content. Please download new template to do this.");
                errorMsgs.clear();
            }
            if (!errorMsgs.isEmpty()) {
                Collections.sort(errorMsgs, Comparator.comparing(FileErrorMsg::getSheetName).thenComparing(FileErrorMsg::getRow)
                        .thenComparing(FileErrorMsg::getCol));
                ParamUtil.setRequestAttr(bpc.request, InspectionConstants.FILE_ITEM_ERROR_MSGS, errorMsgs);
            }
        }
        String nextType = ModuleCommonConstants.KEY_NAV_NEXT;
        if (!errorMap.isEmpty() || IaisCommonUtils.isNotEmpty(errorMsgs)) {
            if (!errorMap.isEmpty()) {
                ParamUtil.setRequestAttr(bpc.request, ValidationConstants.KEY_VALIDATION_ERRORS, JsonUtil.parseToJson(errorMap));
            }
            nextType = ModuleCommonConstants.KEY_NAV_PAGE;
        } else {
            // save
            checklistDto.setAnswer(answerDtos);
            inspectionClient.saveChkListDraft(checklistDto);
        }
        ParamUtil.setRequestAttr(bpc.request, InspectionConstants.ACTION_UPLOAD_TYPE, nextType);
        ParamUtil.setRequestAttr(bpc.request, "ackMsg", MessageUtil.getMessageDesc("GENERAL_ERR0038"));
    }

    private Boolean validateChklItemExcelDto(List<ChklstItemAnswerDto> data, String sheetName, String chkLstConfigId,
            List<FileErrorMsg> errorMsgs) {
        if (data == null || data.isEmpty()) {
            log.info("No data found!");
            return null;
        }
        String item = data.get(0).getConfigId();
        if (StringUtil.isEmpty(item) || !item.equals(chkLstConfigId)) {
            log.info(StringUtil.changeForLog("Wrong configId: " + item + " | " + chkLstConfigId));
            return null;
        }
        errorMsgs.addAll(ExcelValidatorHelper.validateExcelList(data, sheetName, ChklstItemAnswerDto::getSnNo, "file",
                InspectionConstants.START_ROW, InsChklItemExcelDto.class));
        return errorMsgs.isEmpty();
    }

    private Map<String, List<ChklstItemAnswerDto>> transformToChklstItemAnswerDtos(NewDocInfo fileInfo) {
        if (fileInfo == null) {
            return IaisCommonUtils.genNewHashMap();
        }
        Map<String, List<ChklstItemAnswerDto>> resultMap = IaisCommonUtils.genNewHashMap();
        try {
            File file = fileInfo.getFile();
            List<ExcelSheetDto> excelSheetDtos = getExcelSheetDtos(null, new ChecklistConfigDto(), null, false);
            Map<String, List<InsChklItemExcelDto>> data = ExcelReader.readerToBeans(file, excelSheetDtos);
            if (data != null && !data.isEmpty()) {
                for (Map.Entry<String, List<InsChklItemExcelDto>> entry : data.entrySet()) {
                    List<ChklstItemAnswerDto> collect = entry.getValue().stream()
                            .filter(dto -> !StringUtil.isEmpty(dto.getSnNo()))
                            .map(dto -> {
                                ChklstItemAnswerDto answerDto = MiscUtil.transferEntityDto(dto, ChklstItemAnswerDto.class);
                                String itemKey = dto.getItemKey();
                                if (StringUtil.isEmpty(itemKey)) {
                                    return new ChklstItemAnswerDto();
                                }
                                String[] keys = itemKey.split(KEY_SEPARATOR);
                                if (keys.length != 3) {
                                    return new ChklstItemAnswerDto();
                                }
                                answerDto.setConfigId(keys[0]);
                                answerDto.setSectionId(keys[1]);
                                answerDto.setItemId(keys[2]);
                                answerDto.setAnswer(ChecklistConstants.getAnswer(dto.getAnswer()));
                                answerDto.setFollowupItem(ChecklistConstants.getAnswer(dto.getFollowupItem()));
                                answerDto.setRectified(ChecklistConstants.getRectified(dto.getRectified()));
                                return answerDto;
                            }).collect(Collectors.toList());
                    resultMap.put(entry.getKey(), collect);
                }
            }

        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
        }
        return resultMap;
    }

    private NewDocInfo getFileInfo(HttpServletRequest request) {
        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(request, InspectionConstants.SEESION_FILES_MAP_AJAX);
        if (fileMap == null || fileMap.isEmpty()) {
            return null;
        }
        // only one
        Iterator<Map.Entry<String, File>> iterator = fileMap.entrySet().iterator();
        if (!iterator.hasNext()) {
            return null;
        }
        Map.Entry<String, File> next = iterator.next();
        File file = next.getValue();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        return NewDocInfo.toNewDocInfo(DocConstants.DOC_TYPE_INS_CHECKLIST, loginContext.getUserId(), file);
    }

    @ResponseBody
    @GetMapping(value = "/inspection/checklist/exporting-template")
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        ChecklistConfigDto configDto = inspectionClient.getMaxVersionChecklistConfig(appId, HcsaChecklistConstants.INSPECTION);
        exportExcel(null, configDto, null, response);
    }

    @ResponseBody
    @GetMapping(value = "/inspection/checklist/exporting-data")
    public void exportData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        InspectionChecklistDto checklistDto = inspectionClient.getChkListDraft(loginContext.getUserId(),appId).getBody();
        ChecklistConfigDto configDto;
        Map<String, ChklstItemAnswerDto> answerMap = null;
        if (checklistDto == null) {
            configDto = inspectionClient.getMaxVersionChecklistConfig(appId, HcsaChecklistConstants.INSPECTION);
        } else {
            configDto = inspectionClient.getChecklistConfigById(checklistDto.getChkLstConfigId());
            List<ChklstItemAnswerDto> answerDtoList = checklistDto.getAnswer();
            if (answerDtoList != null) {
                answerMap = answerDtoList.stream()
                        .collect(Collectors.toMap((t) -> new StringBuilder()
                                .append(t.getConfigId())
                                .append(KEY_SEPARATOR)
                                .append(t.getSectionId())
                                .append(KEY_SEPARATOR)
                                .append(t.getItemId())
                                .toString(), Function.identity()));
            }
        }
        exportExcel(null, configDto, answerMap, response);
    }

    private void exportExcel(ChecklistConfigDto commonConfigDto, ChecklistConfigDto configDto,
            Map<String, ChklstItemAnswerDto> answerMap, HttpServletResponse response) throws Exception {
        try {
            File configInfoTemplate = ResourceUtils.getFile("classpath:template/Inspection_Checklist_Template.xlsx");
            List<ExcelSheetDto> excelSheetDtos = getExcelSheetDtos(commonConfigDto, configDto, answerMap, true);
            File inputFile = ExcelWriter.writerToExcel(excelSheetDtos, configInfoTemplate, "Inspection_Checklist_Template");
            FileUtils.writeFileResponseContent(response, inputFile);
            FileUtils.deleteTempFile(inputFile);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
            throw e;
        }
    }

    private List<ExcelSheetDto> getExcelSheetDtos(ChecklistConfigDto commonConfigDto, ChecklistConfigDto configDto,
            Map<String, ChklstItemAnswerDto> answerMap, boolean withData) {
        List<ExcelSheetDto> excelSheetDtos = IaisCommonUtils.genNewArrayList();
        int sheetAt = 1;
        if (commonConfigDto != null) {
            List<InsChklItemExcelDto> data = null;
            if (withData) {
                data = getChklItemExcelDtos(commonConfigDto, answerMap);
            }
            excelSheetDtos.add(getExcelSheetDto(sheetAt++, SHEET_NAME_COMMON, data));
        }

        if (configDto != null) {
            List<InsChklItemExcelDto> data = null;
            if (withData) {
                data = getChklItemExcelDtos(configDto, answerMap);
            }
            excelSheetDtos.add(getExcelSheetDto(sheetAt++, SHEET_NAME_BSB, data));
        }
        return excelSheetDtos;
    }

    private ExcelSheetDto getExcelSheetDto(int sheetAt, String sheetName, List<InsChklItemExcelDto> data) {
        ExcelSheetDto excelSheetDto = new ExcelSheetDto();
        excelSheetDto.setSheetAt(sheetAt);
        excelSheetDto.setSheetName(sheetName);
        excelSheetDto.setBlock(true);
        excelSheetDto.setPwd(Formatter.formatDateTime(new Date(), "yyyyMMdd"));
        excelSheetDto.setStartRowIndex(InspectionConstants.START_ROW);
        excelSheetDto.setSource(data);
        excelSheetDto.setSourceClass(InsChklItemExcelDto.class);
        excelSheetDto.setDefaultRowHeight((short) 500);
        excelSheetDto.setChangeHeight(true);
        excelSheetDto.setWidthMap(getWidthMap());

        return excelSheetDto;
    }

    private List<InsChklItemExcelDto> getChklItemExcelDtos(ChecklistConfigDto configDto, Map<String, ChklstItemAnswerDto> answerMap) {
        if (configDto == null || configDto.getSectionDtos() == null) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<InsChklItemExcelDto> result = IaisCommonUtils.genNewArrayList();
        List<ChecklistSectionDto> sectionDtos = configDto.getSectionDtos();
        for (int i = 0, m = sectionDtos.size(); i < m; i++) {
            ChecklistSectionDto sectionDto = sectionDtos.get(i);
            List<ChecklistItemDto> checklistItemDtos = sectionDto.getChecklistItemDtos();
            for (int j = 0, n = checklistItemDtos.size(); j < n; j++) {
                ChecklistItemDto itemDto = checklistItemDtos.get(j);
                InsChklItemExcelDto excelDto = new InsChklItemExcelDto();
                excelDto.setSnNo((i+1) + "." + (j+1));
                excelDto.setChecklistItem(itemDto.getChecklistItem());
                String itemKey = new StringBuilder()
                        .append(configDto.getId())
                        .append(KEY_SEPARATOR)
                        .append(sectionDto.getId())
                        .append(KEY_SEPARATOR)
                        .append(itemDto.getItemId())
                        .toString();
                ChklstItemAnswerDto dto = answerMap != null ? answerMap.get(itemKey) : null;
                if (dto != null) {
                    excelDto.setAnswer(ChecklistConstants.displayAnswer(dto.getAnswer()));
                    excelDto.setFindings(dto.getFindings());
                    excelDto.setActionRequired(dto.getActionRequired());
                    excelDto.setRectified(ChecklistConstants.getRectified(dto.getRectified()));
                    excelDto.setFollowupItem(ChecklistConstants.displayAnswer(dto.getFollowupItem()));
                    excelDto.setObserveFollowup(dto.getObserveFollowup());
                    excelDto.setFollowupAction(dto.getFollowupAction());
                    excelDto.setDueDate(dto.getDueDate());
                } else {
                    excelDto.setAnswer("");
                    excelDto.setFindings("");
                    excelDto.setActionRequired("");
                    excelDto.setRectified("");
                    excelDto.setFollowupItem("");
                    excelDto.setObserveFollowup("");
                    excelDto.setFollowupAction("");
                    excelDto.setDueDate("");
                }
                excelDto.setItemKey(itemKey);
                result.add(excelDto);
            }
        }
        return result;
    }

    private static Map<Integer, Integer> widthMap;

    public static Map<Integer, Integer> getWidthMap() {
        if (widthMap != null) {
            return widthMap;
        }
        widthMap = IaisCommonUtils.genNewHashMap(5);
        widthMap.put(0, 9);
        widthMap.put(1, 25);
        widthMap.put(2, 12);
        widthMap.put(3, 25);
        widthMap.put(4, 25);
        widthMap.put(5, 9);
        widthMap.put(6, 15);
        widthMap.put(7, 25);
        widthMap.put(8, 25);
        widthMap.put(9, 12);
        return widthMap;
    }
}
