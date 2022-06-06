package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
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
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelReader;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.google.common.collect.Maps;
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
import sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants;
import sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants;
import sg.gov.moh.iais.egp.bsb.dto.ProcessHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.BsbAdCheckListShowDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.BsbAdhocNcCheckItemDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.BsbAnswerForDifDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.ChklstItemAnswerDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.InsChklItemExcelDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.InspectionCheckQuestionDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.InspectionFDtosDto;
import sg.gov.moh.iais.egp.bsb.dto.chklst.InspectionFillCheckListDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocChecklistConfigDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AdhocChecklistItemDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionChecklistDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.file.DocDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.file.NewDocInfo;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsProcessDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.InsSubmitFindingDataDto;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.FacilityDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.mohprocessingdisplay.SubmissionDetailsInfo;
import sg.gov.moh.iais.egp.bsb.dto.task.TaskAssignDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.InspectionService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sg.gov.moh.iais.egp.bsb.validation.CheckListCommonValidate;
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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.module.AppViewConstants.MODULE_VIEW_NEW_FACILITY;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_ADHOC_CHECKLIST_LIST_ATTR;
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

    private static final String INSPECTION_USERS = "inspectorsParticipant";
    private static final String ADHOC_DTO = "adchklDto";
    private static final String INSPECTION_USER_FINISH = "inspectorUserFinishChecklistId";

    private static final String NOW_TAB_IN = "nowTabIn";
    private static final String NOW_COM_TAB_IN = "nowComTabIn";
    private static final String TAB_COMBINED = "Combined";
    private static final String CHK_LIST = "chkList";
    private static final String CHECK_LIST_DATA = "checklistData";
    private static final String GENERAL_ERR0006 = "GENERAL_ERR0006";

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
        session.removeAttribute(SERLISTDTO);
        session.removeAttribute(KEY_ADHOC_CHECKLIST_LIST_ATTR);
        session.removeAttribute(ADHOC_DTO);

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


        //view application
        ParamUtil.setRequestAttr(request, AppViewConstants.MASK_PARAM_APP_VIEW_MODULE_TYPE, MODULE_VIEW_NEW_FACILITY);
    }

    private void setCheckListUnFinishedTask(HttpServletRequest request,String appId){
        //get  commonDto draft
        List<String> ids=IaisCommonUtils.genNewArrayList();
        List<TaskAssignDto> taskAssignDtoList=inspectionClient.getOfficerTaskList("/bsb-web/eservice/INTRANET/MohBsbInspectionDO",appId).getBody();
        assert taskAssignDtoList != null;
        for (TaskAssignDto ta:taskAssignDtoList) {
            ids.add(ta.getUserId());
        }

        List<OrgUserDto> orgUserDtoUsers = organizationClient.retrieveOrgUserAccount(ids).getEntity();
        List<InspectionFillCheckListDto> cDtoList =  inspectionService.getServiceChkDtoListByAppPremId(appId,"service",false);
        BsbAdCheckListShowDto adhocConf = inspectionService.getAdhocCheckListDto(appId, orgUserDtoUsers);
        InspectionFDtosDto inspectionFDtosDto=new InspectionFDtosDto();

        inspectionService.getInspectionFillCheckListDtoByInspectionFillCheckListDto(cDtoList.get(0),orgUserDtoUsers);
        inspectionService.getInspectionFillCheckListDtoForShow(cDtoList.get(0));
        inspectionFDtosDto.setFdtoList(cDtoList);
        List<InspectionChecklistDto> inspectionChecklistDtoList = IaisCommonUtils.genNewArrayList();
        for (OrgUserDto officer:orgUserDtoUsers) {
            InspectionChecklistDto inspectionChecklistDto=inspectionClient.getChkListDraft(officer.getId(),appId).getBody();

            if(inspectionChecklistDto!=null){
                InspectionChecklistDto combinedChecklistDto=inspectionClient.getCombinedChkList(appId).getBody();

                List<ChklstItemAnswerDto> answerDtos = inspectionChecklistDto.getAnswer();
                inspectionChecklistDtoList.add(inspectionChecklistDto);
                InspectionFillCheckListDto comDto = inspectionFDtosDto.getFdtoList().get(0);
                if(comDto != null && comDto.getCheckList()!=null&& !comDto.getCheckList().isEmpty()){
                    List<InspectionCheckQuestionDto> checkList = comDto.getCheckList();
                    for (InspectionCheckQuestionDto inspectionCheckQuestionDto : checkList) {
                        if (IaisCommonUtils.isNotEmpty(answerDtos)) {
                            Map<String, BsbAnswerForDifDto> answerForDifDtoMaps = Maps.newHashMapWithExpectedSize(orgUserDtoUsers.size());
                            for (BsbAnswerForDifDto answerForDifDto: inspectionCheckQuestionDto.getAnswerForDifDtos()) {
                                for (ChklstItemAnswerDto answer: answerDtos) {
                                    if (answer.getItemId() != null && answer.getItemId().equals(inspectionCheckQuestionDto.getItemId())
                                            && inspectionChecklistDto.getUserId().equals(answerForDifDto.getSubmitId())) {
                                        if(combinedChecklistDto!=null){
                                            List<ChklstItemAnswerDto> answerDtoCombineds=combinedChecklistDto.getAnswer();
                                            for (ChklstItemAnswerDto combined : answerDtoCombineds) {
                                                if(combined.equals(answer)){
                                                    inspectionCheckQuestionDto.setSameAnswer(false);
                                                    inspectionCheckQuestionDto.setDeconflict(officer.getId());
                                                }
                                            }
                                        }
                                        answerForDifDto.setAnswer(answer.getAnswer());
                                        if (answer.getRectified().equals(Boolean.TRUE)) {
                                            answerForDifDto.setIsRec("1");
                                        } else {
                                            answerForDifDto.setIsRec("0");
                                        }
                                        answerForDifDto.setNcs(answer.getFindings());
                                        answerForDifDto.setRemark(answer.getActionRequired());
                                        answerForDifDto.setSameAnswer(false);
                                        answerForDifDto.setFollowupItem(answer.getFollowupItem());
                                        answerForDifDto.setFollowupAction(answer.getFollowupAction());
                                        answerForDifDto.setObserveFollowup(answer.getObserveFollowup());
                                        answerForDifDto.setDueDate(answer.getDueDate());
                                        answerForDifDtoMaps.put(officer.getId(),answerForDifDto);
                                    }
                                }
                            }

                            inspectionCheckQuestionDto.getAnswerForDifDtoMaps().putAll(answerForDifDtoMaps);
                        }
                    }
                }
                // Set adhoc answers
                if (adhocConf != null && !IaisCommonUtils.isEmpty(adhocConf.getAdItemList())) {
                    for(BsbAdhocNcCheckItemDto inspectionCheckQuestionDto : adhocConf.getAdItemList()){
                        if (IaisCommonUtils.isNotEmpty(answerDtos)) {
                            Map<String, BsbAnswerForDifDto> answerForDifDtoMaps = Maps.newHashMapWithExpectedSize(orgUserDtoUsers.size());
                            for (ChklstItemAnswerDto answer : answerDtos) {
                                for (BsbAnswerForDifDto answerForDifDto : inspectionCheckQuestionDto.getAdhocAnswerForDifDtos()) {
                                    if (answer.getItemId() != null && answer.getItemId().equals(inspectionCheckQuestionDto.getId())
                                                && inspectionChecklistDto.getUserId().equals(answerForDifDto.getSubmitId())) {
                                        if (combinedChecklistDto != null) {
                                            List<ChklstItemAnswerDto> answerDtoCombineds=combinedChecklistDto.getAnswer();
                                            for (ChklstItemAnswerDto combined : answerDtoCombineds) {
                                                if (combined.equals(answer)) {
                                                    inspectionCheckQuestionDto.setSameAnswer(false);
                                                    inspectionCheckQuestionDto.setDeconflict(officer.getId());
                                                }
                                            }
                                        }
                                        answerForDifDto.setAnswer(answer.getAnswer());
                                        if (answer.getRectified().equals(Boolean.TRUE)) {
                                            answerForDifDto.setIsRec("1");
                                        } else {
                                            answerForDifDto.setIsRec("0");
                                        }
                                        answerForDifDto.setNcs(answer.getFindings());
                                        answerForDifDto.setRemark(answer.getActionRequired());
                                        answerForDifDto.setSameAnswer(false);
                                        answerForDifDto.setFollowupItem(answer.getFollowupItem());
                                        answerForDifDto.setFollowupAction(answer.getFollowupAction());
                                        answerForDifDto.setObserveFollowup(answer.getObserveFollowup());
                                        answerForDifDto.setDueDate(answer.getDueDate());
                                        answerForDifDtoMaps.put(officer.getId(),answerForDifDto);
                                    }
                                }
                            }
                            inspectionCheckQuestionDto.getAnswerForDifDtoMaps().putAll(answerForDifDtoMaps);
                        }
                    }
                }
            }
        }

        ParamUtil.setSessionAttr(request, KEY_INS_CHECKLIST_DTO, (Serializable) inspectionChecklistDtoList);
        ParamUtil.setSessionAttr(request, INSPECTION_USER_FINISH, IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        ParamUtil.setSessionAttr(request,SERLISTDTO,inspectionFDtosDto);
        ParamUtil.setSessionAttr(request,INSPECTION_USERS, (Serializable) orgUserDtoUsers);
        ParamUtil.setSessionAttr(request,ADHOC_DTO, adhocConf);
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
        ParamUtil.setRequestAttr(bpc.request, NOW_TAB_IN,  TAB_COMBINED);
        String appId = (String) ParamUtil.getSessionAttr(bpc.request, KEY_APP_ID);
        setCheckListUnFinishedTask(bpc.request,appId);
        // do nothing now
    }

    public void changeTab(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        log.info("----------changeTab --");
        String nowTabIn = ParamUtil.getString(request,NOW_TAB_IN);
        ParamUtil.setRequestAttr(request, NOW_TAB_IN,  nowTabIn);
        String nowComTabIn = ParamUtil.getString(request,NOW_COM_TAB_IN);
        ParamUtil.setRequestAttr(request, NOW_COM_TAB_IN,  nowComTabIn);
        changeDataForCheckList(request);
    }
    public void changeDataForCheckList(HttpServletRequest request){
        getCommonCheckListMoreData(request);
        getAdhocCheckListMoreData(request);

        setRate(request);
    }

    public void getCommonCheckListMoreData(HttpServletRequest request){
        InspectionFDtosDto serListDto = (InspectionFDtosDto) ParamUtil.getSessionAttr(request,SERLISTDTO);
        InspectionFillCheckListDto comDto = serListDto.getFdtoList().get(0);
        String userId = (String)ParamUtil.getSessionAttr(request, INSPECTION_USER_FINISH);
        if(comDto != null && comDto.getCheckList()!=null&& !comDto.getCheckList().isEmpty()){
            List<InspectionCheckQuestionDto> checkList = comDto.getCheckList();
            for(InspectionCheckQuestionDto inspectionCheckQuestionDto : checkList){
                String prefix = inspectionCheckQuestionDto.getSectionId()+inspectionCheckQuestionDto.getItemId();

                List<BsbAnswerForDifDto> answerForDifDtos = inspectionCheckQuestionDto.getAnswerForDifDtos();
                int index = 0;
                for(BsbAnswerForDifDto answerForDifDto : answerForDifDtos){
                    if(userId.equalsIgnoreCase(answerForDifDto.getSubmitId())){
                        String answer =  ParamUtil.getString(request,prefix +"comradIns" + index);
                        String remark = ParamUtil.getString(request,prefix +"comremarkIns" + index);
                        String raf =  ParamUtil.getString(request,prefix +"comrecIns" + index);
                        String ncs  = ParamUtil.getString(request,prefix +"comFindNcsIns" + index);
                        answerForDifDto.setNcs(ncs);
                        answerForDifDto.setAnswer(answer);
                        answerForDifDto.setRemark(remark);
                        if("NO".equalsIgnoreCase(answer) && "rec".equalsIgnoreCase(raf)){
                            answerForDifDto.setIsRec("1");
                        }else {
                            answerForDifDto.setIsRec("0");
                        }
                        String  follItem=  ParamUtil.getString(request,prefix +"comradFull" + index);
                        answerForDifDto.setFollowupItem(follItem);
                        if("YES".equalsIgnoreCase(follItem)){
                            String observeFoll =  ParamUtil.getString(request,prefix +"comObserveFoll" + index);
                            answerForDifDto.setObserveFollowup(observeFoll);
                            String follAction =  ParamUtil.getString(request,prefix +"comFollAction" + index);
                            answerForDifDto.setFollowupAction(follAction);
                            String dueDate =  ParamUtil.getString(request,prefix +"comDueDate" + index);
                            answerForDifDto.setDueDate(dueDate);
                        }else {
                            answerForDifDto.setObserveFollowup(null);
                            answerForDifDto.setFollowupAction(null);
                            answerForDifDto.setDueDate(null);
                        }
                        inspectionService.getInspectionCheckQuestionDtoByAnswerForDifDto(inspectionCheckQuestionDto, answerForDifDto);
                        break;
                    }
                    index++;
                }
            }
            ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        }
    }

    public void getAdhocCheckListMoreData(HttpServletRequest request) {
        BsbAdCheckListShowDto adhocConf = (BsbAdCheckListShowDto) ParamUtil.getSessionAttr(request,ADHOC_DTO);
        if (adhocConf == null) {
            return;
        }
        String userId = (String)ParamUtil.getSessionAttr(request, INSPECTION_USER_FINISH);
        if (adhocConf.getAdItemList() != null && !adhocConf.getAdItemList().isEmpty()) {
            List<BsbAdhocNcCheckItemDto> checkList = adhocConf.getAdItemList();
            for(BsbAdhocNcCheckItemDto inspectionCheckQuestionDto : checkList){
                String prefix = inspectionCheckQuestionDto.getId() + "adhoc";

                List<BsbAnswerForDifDto> answerForDifDtos = inspectionCheckQuestionDto.getAdhocAnswerForDifDtos();
                int index = 0;
                for (BsbAnswerForDifDto answerForDifDto : answerForDifDtos) {
                    if(userId.equalsIgnoreCase(answerForDifDto.getSubmitId())){
                        String answer =  ParamUtil.getString(request,prefix +"radIns" + index);
                        String remark = ParamUtil.getString(request,prefix +"remarkIns" + index);
                        String raf =  ParamUtil.getString(request,prefix +"recIns" + index);
                        String ncs  = ParamUtil.getString(request,prefix +"FindNcsIns" + index);
                        answerForDifDto.setNcs(ncs);
                        answerForDifDto.setAnswer(answer);
                        answerForDifDto.setRemark(remark);
                        if("NO".equalsIgnoreCase(answer) && "rec".equalsIgnoreCase(raf)){
                            answerForDifDto.setIsRec("1");
                        }else {
                            answerForDifDto.setIsRec("0");
                        }
                        String  follItem=  ParamUtil.getString(request,prefix +"radFull" + index);
                        answerForDifDto.setFollowupItem(follItem);
                        if("YES".equalsIgnoreCase(follItem)){
                            String observeFoll =  ParamUtil.getString(request,prefix +"ObserveFoll" + index);
                            answerForDifDto.setObserveFollowup(observeFoll);
                            String follAction =  ParamUtil.getString(request,prefix +"FollAction" + index);
                            answerForDifDto.setFollowupAction(follAction);
                            String dueDate =  ParamUtil.getString(request,prefix +"DueDate" + index);
                            answerForDifDto.setDueDate(dueDate);
                        }else {
                            answerForDifDto.setObserveFollowup(null);
                            answerForDifDto.setFollowupAction(null);
                            answerForDifDto.setDueDate(null);
                        }
                        inspectionCheckQuestionDto.setRemark(answerForDifDto.getRemark());
                        inspectionCheckQuestionDto.setAnswer(answerForDifDto.getAnswer());
                        inspectionCheckQuestionDto.setRectified("1".equalsIgnoreCase(answerForDifDto.getIsRec()));
                        inspectionCheckQuestionDto.setNcs(answerForDifDto.getNcs());
                        inspectionCheckQuestionDto.setFollowupAction(answerForDifDto.getFollowupAction());
                        inspectionCheckQuestionDto.setFollowupItem(answerForDifDto.getFollowupItem());
                        inspectionCheckQuestionDto.setObserveFollowup(answerForDifDto.getObserveFollowup());
                        inspectionCheckQuestionDto.setDueDate(answerForDifDto.getDueDate());
                        break;
                    }
                    index++;
                }
            }
            ParamUtil.setSessionAttr(request,ADHOC_DTO,adhocConf);
        }
    }

    public void setRate(HttpServletRequest request){
        InspectionFDtosDto serListDto = (InspectionFDtosDto) ParamUtil.getSessionAttr(request,SERLISTDTO);
        BsbAdCheckListShowDto adchklDto = (BsbAdCheckListShowDto) ParamUtil.getSessionAttr(request,ADHOC_DTO);
        inspectionService.getRateOfCheckList(serListDto,adchklDto);
        ParamUtil.setSessionAttr(request,ADHOC_DTO,adchklDto);
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
    }


    public void saveDraft(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ArrayList<ChklstItemAnswerDto> answerDtos = IaisCommonUtils.genNewArrayList();
        getCommonCheckListMoreData(request);
        getAdhocCheckListMoreData(request);
        InspectionFDtosDto serListDto = (InspectionFDtosDto) ParamUtil.getSessionAttr(request,SERLISTDTO);
        BsbAdCheckListShowDto adhocConf = (BsbAdCheckListShowDto) ParamUtil.getSessionAttr(request,ADHOC_DTO);
        InspectionFillCheckListDto comDto = serListDto.getFdtoList().get(0);
        String userId = (String)ParamUtil.getSessionAttr(request, INSPECTION_USER_FINISH);
        Map<String, String> errMap =IaisCommonUtils.genNewHashMap();
        if(comDto != null && comDto.getCheckList()!=null && !comDto.getCheckList().isEmpty()){
            List<InspectionCheckQuestionDto> checkList = comDto.getCheckList();
            for(InspectionCheckQuestionDto inspectionCheckQuestionDto : checkList){
                List<BsbAnswerForDifDto> answerForDifDtos = inspectionCheckQuestionDto.getAnswerForDifDtos();
                for(BsbAnswerForDifDto answerForDifDto : answerForDifDtos){
                    if(userId.equalsIgnoreCase(answerForDifDto.getSubmitId())){
                        answerDtos.add(transferDifAnswsertoChkAnswer(answerForDifDto,
                                inspectionCheckQuestionDto.getConfigId(), inspectionCheckQuestionDto.getItemId(),
                                inspectionCheckQuestionDto.getSectionId()));
                        break;
                    }
                }
            }
        }
        if (adhocConf != null && !IaisCommonUtils.isEmpty(adhocConf.getAdItemList())) {
            BsbAdhocNcCheckItemDto itemDto = adhocConf.getAdItemList().get(0);
            for(BsbAnswerForDifDto answerForDifDto : itemDto.getAdhocAnswerForDifDtos()){
                if(userId.equalsIgnoreCase(answerForDifDto.getSubmitId())){
                    answerDtos.add(transferDifAnswsertoChkAnswer(answerForDifDto, itemDto.getAdhocChecklistConfigId(),
                            itemDto.getId(), ChecklistConstants.ADHOC_SECTION_ID));
                    break;
                }
            }
        }
        if(!IaisCommonUtils.isEmpty(answerDtos)){
            for(ChklstItemAnswerDto temp: answerDtos){
                CheckListCommonValidate.verifyQuestionDto(temp.getAnswer(),temp.getActionRequired(),temp.getFindings(),temp.getFollowupItem(),temp.getObserveFollowup(),temp.getFollowupAction(),temp.getDueDate(), true,StringUtil.getNonNull(temp.getSectionId())+temp.getItemId()+userId,errMap);
            }
        }
        ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);

        InspectionChecklistDto checklistDto= null;

        List<InspectionChecklistDto> checklistDtoList= (List<InspectionChecklistDto>) ParamUtil.getSessionAttr(request,KEY_INS_CHECKLIST_DTO);
        if (checklistDtoList != null) {
            for (InspectionChecklistDto cklDto : checklistDtoList) {
                if (cklDto.getUserId().equals(userId)) {
                    checklistDto = cklDto;
                    checklistDto.setAnswer(answerDtos);
                }
            }
        }
        if (checklistDto == null) {
            checklistDto = new InspectionChecklistDto();
            checklistDto.setApplicationId(serListDto.getFdtoList().get(0).getCheckList().get(0).getAppPreCorreId());
            checklistDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            checklistDto.setUserId(userId);
            checklistDto.setVersion(1);
            checklistDto.setChkLstConfigId(serListDto.getFdtoList().get(0).getConfigId());
            checklistDto.setAnswer(answerDtos);
        }
        ParamUtil.setRequestAttr(request, NOW_TAB_IN,  TAB_COMBINED);
        if (!errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            serListDto.setCheckListTab(CHK_LIST);
            ParamUtil.setRequestAttr(request, NOW_TAB_IN,  userId);
            String nowComTabIn = ParamUtil.getString(request,NOW_COM_TAB_IN);
            ParamUtil.setRequestAttr(request, NOW_COM_TAB_IN,  nowComTabIn);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        } else {
            serListDto.setCheckListTab(CHK_LIST);

            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            inspectionClient.saveChkListDraft(checklistDto);
        }
        ParamUtil.setSessionAttr(request, SERLISTDTO, serListDto);
    }

    public void validateAndSaveChecklist(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;


        InspectionFDtosDto serListDto = (InspectionFDtosDto) ParamUtil.getSessionAttr(request,SERLISTDTO);
        BsbAdCheckListShowDto adhocConf = (BsbAdCheckListShowDto) ParamUtil.getSessionAttr(request,ADHOC_DTO);
        InspectionFillCheckListDto comDto = serListDto.getFdtoList().get(0);
        String userId = (String)ParamUtil.getSessionAttr(request, INSPECTION_USER_FINISH);
        String doSubmitAction = ParamUtil.getString(request,"doSubmitAction");
        Map<String, String> errMap =IaisCommonUtils.genNewHashMap();
        Map<String, String> errMapCombined =IaisCommonUtils.genNewHashMap();
        ArrayList<ChklstItemAnswerDto> answerDtoList=IaisCommonUtils.genNewArrayList();
        if(comDto != null && comDto.getCheckList() != null && !comDto.getCheckList().isEmpty()){
            List<InspectionCheckQuestionDto> checkList = comDto.getCheckList();

            if(!IaisCommonUtils.isEmpty(checkList)){

                for(InspectionCheckQuestionDto temp:checkList){

                    String deconflict=ParamUtil.getRequestString(request,StringUtil.getNonNull(temp.getSectionNameShow())+temp.getItemId()+"Deconflict");
                    if(deconflict!=null){
                        temp.setSameAnswer(false);
                        temp.setDeconflict(deconflict);
                        BsbAnswerForDifDto answerForDifDto=temp.getAnswerForDifDtoMaps().get(deconflict);
                        ChklstItemAnswerDto answerDto=new ChklstItemAnswerDto();
                        answerDto.setAnswer(answerForDifDto.getAnswer());
                        answerDto.setFindings(answerForDifDto.getNcs());
                        answerDto.setActionRequired(answerForDifDto.getRemark());
                        answerDto.setConfigId(temp.getConfigId());
                        answerDto.setItemId(temp.getItemId());
                        answerDto.setSectionId(temp.getSectionId());
                        answerDto.setFollowupItem(answerForDifDto.getFollowupItem());
                        answerDto.setObserveFollowup(answerForDifDto.getObserveFollowup());
                        answerDto.setFollowupAction(answerForDifDto.getFollowupAction());
                        answerDto.setDueDate(answerForDifDto.getDueDate());
                        answerDto.setRectified(answerForDifDto.getIsRec().equals("1"));
                        answerDtoList.add(answerDto);
                    }else {
                        errMapCombined.put(StringUtil.getNonNull(temp.getSectionNameShow())+temp.getItemId()+"com",MessageUtil.getMessageDesc(GENERAL_ERR0006));
                    }
                }

            }
        }
        if(adhocConf!=null&&IaisCommonUtils.isNotEmpty(adhocConf.getAdItemList())){
            for (BsbAdhocNcCheckItemDto adItem : adhocConf.getAdItemList()) {
                String adhocDeconflict=ParamUtil.getRequestString(request,adItem.getId()+"adhocDeconflict");
                if(adhocDeconflict!=null){
                    adItem.setSameAnswer(false);
                    adItem.setDeconflict(adhocDeconflict);
                    BsbAnswerForDifDto answerForDifDto=adItem.getAnswerForDifDtoMaps().get(adhocDeconflict);
                    ChklstItemAnswerDto answerDto=new ChklstItemAnswerDto();
                    answerDto.setAnswer(answerForDifDto.getAnswer());
                    answerDto.setFindings(answerForDifDto.getNcs());
                    answerDto.setActionRequired(answerForDifDto.getRemark());
                    answerDto.setConfigId(adItem.getAdhocChecklistConfigId());
                    answerDto.setItemId(adItem.getId());
                    answerDto.setSectionId(adItem.getSectionId());
                    answerDto.setFollowupItem(answerForDifDto.getFollowupItem());
                    answerDto.setObserveFollowup(answerForDifDto.getObserveFollowup());
                    answerDto.setFollowupAction(answerForDifDto.getFollowupAction());
                    answerDto.setDueDate(answerForDifDto.getDueDate());
                    answerDto.setRectified(answerForDifDto.getIsRec().equals("1"));
                    answerDtoList.add(answerDto);
                }else {
                    errMapCombined.put(adItem.getId()+"adhoc",MessageUtil.getMessageDesc(GENERAL_ERR0006));

                }


            }
        }
        if(!IaisCommonUtils.isEmpty(answerDtoList)){
            for(ChklstItemAnswerDto temp: answerDtoList){
                CheckListCommonValidate.verifyQuestionDto(temp.getAnswer(),temp.getActionRequired(),temp.getFindings(),temp.getFollowupItem(),temp.getObserveFollowup(),temp.getFollowupAction(),temp.getDueDate(), true,StringUtil.getNonNull(temp.getSectionId())+temp.getItemId()+userId,errMap);
            }
        }

        ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        InspectionChecklistDto inspectionChecklistDto=new InspectionChecklistDto();
        inspectionChecklistDto.setApplicationId(appId);
        inspectionChecklistDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        inspectionChecklistDto.setUserId(userId);
        inspectionChecklistDto.setVersion(1);
        inspectionChecklistDto.setChkLstConfigId(comDto.getConfigId());
        inspectionChecklistDto.setAnswer( answerDtoList);


        ParamUtil.setRequestAttr(request, NOW_TAB_IN,  TAB_COMBINED);
        if("next".equalsIgnoreCase(doSubmitAction)) {

            if (!errMap.isEmpty()) {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                serListDto.setCheckListTab(CHK_LIST);
                ParamUtil.setRequestAttr(request, NOW_TAB_IN,  userId);
                String nowComTabIn = ParamUtil.getString(request,NOW_COM_TAB_IN);
                ParamUtil.setRequestAttr(request, NOW_COM_TAB_IN,  nowComTabIn);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            } else {
                serListDto.setCheckListTab(CHK_LIST);
                if(errMapCombined.isEmpty()){
                    ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
                    inspectionClient.saveCombinedChkList(inspectionChecklistDto);
                }else {
                    ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                }
            }
        }else {
            serListDto.setCheckListTab(CHK_LIST);

            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }

        ParamUtil.setSessionAttr(request, SERLISTDTO, serListDto);
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
        ValidationResultDto validationResultDto = inspectionClient.validateActualInspectionCombineCheckList(appId);
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
        InspectionInfoDto inspectionInfoDto = inspectionClient.getInspectionInfoDto(appId).getBody();
        String chkLstConfigId;
        if (inspectionInfoDto != null) {
            chkLstConfigId = inspectionInfoDto.getCheckListConfigId();
        } else {
            ChecklistConfigDto configDto = inspectionClient.getMaxVersionChecklistConfig(appId, HcsaChecklistConstants.INSPECTION);
            chkLstConfigId = configDto.getId();
        }
        checklistDto.setChkLstConfigId(chkLstConfigId);
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
            errorMap.put(CHECK_LIST_DATA, MessageUtil.getMessageDesc(GENERAL_ERR0006));
        } else if (fileInfo.getSize() == 0) {
            errorMap.put(CHECK_LIST_DATA, "Could not parse file content.");
        }  else if (!FileUtils.isExcel(fileInfo.getFilename())) {
            errorMap.put(CHECK_LIST_DATA, MessageUtil.replaceMessage("GENERAL_ERR0018", "XLSX", "fileType"));
        } else {
            Map<String, List<ChklstItemAnswerDto>> result = transformToChklstItemAnswerDtos(fileInfo);
            List<ChklstItemAnswerDto> bsbData = result.get(InspectionConstants.SHEET_NAME_BSB);
            AdhocChecklistConfigDto adhocConfig = inspectionClient.getAdhocChecklistConfigDaoByAppid(checklistDto.getApplicationId()).getBody();
            Boolean isValid = validateChklItemExcelDto(bsbData, SHEET_NAME_BSB, checklistDto.getChkLstConfigId(),
                    adhocConfig == null ? null : adhocConfig.getId(), errorMsgs);
            if (isValid != null && isValid) {
                answerDtos.addAll(bsbData);
            }
            if (isValid == null) {
                errorMap.put(CHECK_LIST_DATA, "Could not parse file content. Please download new template to do this.");
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
        ParamUtil.setRequestAttr(bpc.request, "ackMsg", MessageUtil.replaceMessage("GENERAL_ERR0058",
                "Inspection Checklist", "data"));
    }

    /**
     * Step: preListAdhoc
     *
     * @param bpc
     */
    public void preListAdhoc(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AdhocChecklistConfigDto adhocChecklistConfigDto = (AdhocChecklistConfigDto) ParamUtil.getSessionAttr(request, KEY_ADHOC_CHECKLIST_LIST_ATTR);
        if (adhocChecklistConfigDto == null) {
            String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
            if (StringUtil.isNotEmpty(appId)) {
                adhocChecklistConfigDto = inspectionClient.getAdhocChecklistConfigDaoByAppid(appId).getBody();
                ParamUtil.setSessionAttr(request, KEY_ADHOC_CHECKLIST_LIST_ATTR, adhocChecklistConfigDto);
            } else {
                log.info("-----------application id is null-------");
            }
        }
    }

    /**
     * Step: addAdhoc
     *
     * @param bpc
     */
    public void addAdhoc(BaseProcessClass bpc) {
        //do nothing now
    }

    /**
     * Step: saveAdhoc
     *
     * @param bpc
     */
    public void saveAdhoc(BaseProcessClass bpc) {
        // save move to adhocChecklistDelegator
        bpc.getSession().removeAttribute(KEY_ADHOC_CHECKLIST_LIST_ATTR);
    }

    private Boolean validateChklItemExcelDto(List<ChklstItemAnswerDto> data, String sheetName, String chkLstConfigId, String adhocConfId,
                                             List<FileErrorMsg> errorMsgs) {
        if (data == null || data.isEmpty()) {
            log.info("No data found!");
            return null;
        }
        Optional<ChklstItemAnswerDto> optional = data.stream()
                .filter(dto -> (!Objects.equals(chkLstConfigId, dto.getConfigId()) && !Objects.equals(adhocConfId, dto.getConfigId()))
                        || !ExcelValidatorHelper.isValidUuid(dto.getSectionId())
                        || !ExcelValidatorHelper.isValidUuid(dto.getItemId()))
                .findAny();
        if (optional.isPresent()) {
            log.info(StringUtil.changeForLog("Wrong Data: " + JsonUtil.parseToJson(optional.get()) + " | " + chkLstConfigId));
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
            List<ExcelSheetDto> excelSheetDtos = getExcelSheetDtos(null, new ChecklistConfigDto(), null, null, false);
            Map<String, List<InsChklItemExcelDto>> data = ExcelReader.readerToBeans(file, excelSheetDtos);
            if (!data.isEmpty()) {
                for (Map.Entry<String, List<InsChklItemExcelDto>> entry : data.entrySet()) {
                    List<ChklstItemAnswerDto> collect = entry.getValue().stream()
                            .filter(dto -> !StringUtils.isEmpty(dto.getSnNo()) && !StringUtils.isEmpty(dto.getChecklistItem()))
                            .map(dto -> {
                                ChklstItemAnswerDto answerDto = MiscUtil.transferEntityDto(dto, ChklstItemAnswerDto.class);
                                String itemKey = dto.getItemKey();
                                if (StringUtils.isEmpty(itemKey)) {
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

    /**
     * Download / export Template
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @GetMapping(value = "/inspection/checklist/exporting-template")
    public void exportTemplate(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        ChecklistConfigDto configDto;
        InspectionInfoDto inspectionInfoDto = inspectionClient.getInspectionInfoDto(appId).getBody();
        if (inspectionInfoDto != null) {
            configDto = inspectionClient.getChecklistConfigById(inspectionInfoDto.getCheckListConfigId());
        } else {
            configDto = inspectionClient.getMaxVersionChecklistConfig(appId, HcsaChecklistConstants.INSPECTION);
        }
        //Add Adhoc check list -- Jinhua
        AdhocChecklistConfigDto adhocConfig = inspectionClient.getAdhocChecklistConfigDaoByAppid(appId).getBody();

        exportExcel(null, configDto, adhocConfig, null, "Inspection_Checklist_Template", response);
    }

    /**
     * Download / export Data
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @ResponseBody
    @GetMapping(value = "/inspection/checklist/exporting-data")
    public void exportData(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String appId = (String) ParamUtil.getSessionAttr(request, KEY_APP_ID);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        InspectionChecklistDto checklistDto = inspectionClient.getChkListDraft(loginContext.getUserId(), appId).getBody();
        Map<String, ChklstItemAnswerDto> answerMap = null;
        if (checklistDto != null) {
            List<ChklstItemAnswerDto> answerDtoList = checklistDto.getAnswer();
            if (answerDtoList != null) {
                answerMap = answerDtoList.stream()
                        .collect(Collectors.toMap(t -> new StringBuilder()
                                .append(t.getConfigId())
                                .append(KEY_SEPARATOR)
                                .append(t.getSectionId())
                                .append(KEY_SEPARATOR)
                                .append(t.getItemId())
                                .toString(), Function.identity()));
            }
        }
        ChecklistConfigDto configDto;
        InspectionInfoDto inspectionInfoDto = inspectionClient.getInspectionInfoDto(appId).getBody();
        if (inspectionInfoDto != null) {
            configDto = inspectionClient.getChecklistConfigById(inspectionInfoDto.getCheckListConfigId());
        } else {
            configDto = inspectionClient.getMaxVersionChecklistConfig(appId, HcsaChecklistConstants.INSPECTION);
        }
        //Add Adhoc check list -- Jinhua
        AdhocChecklistConfigDto adhocConfig = inspectionClient.getAdhocChecklistConfigDaoByAppid(appId).getBody();

        exportExcel(null, configDto, adhocConfig, answerMap, "Inspection_Checklist", response);
    }

    private void exportExcel(ChecklistConfigDto commonConfigDto, ChecklistConfigDto configDto, AdhocChecklistConfigDto adhocConfig,
            Map<String, ChklstItemAnswerDto> answerMap, String filename, HttpServletResponse response) throws Exception {
        try {
            File configInfoTemplate = ResourceUtils.getFile("classpath:template/Inspection_Checklist_Template.xlsx");
            List<ExcelSheetDto> excelSheetDtos = getExcelSheetDtos(commonConfigDto, configDto, adhocConfig, answerMap, true);
            File inputFile = ExcelWriter.writerToExcel(excelSheetDtos, configInfoTemplate, filename);
            FileUtils.writeFileResponseContent(response, inputFile);
            FileUtils.deleteTempFile(inputFile);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog(e.getMessage()), e);
            throw e;
        }
    }

    private List<ExcelSheetDto> getExcelSheetDtos(ChecklistConfigDto commonConfigDto, ChecklistConfigDto configDto,
          AdhocChecklistConfigDto adhocConfig, Map<String, ChklstItemAnswerDto> answerMap, boolean withData) {
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
                if (adhocConfig != null) {
                    data.addAll(getAdhocChklItemExcelDtos(adhocConfig, configDto.getSectionDtos().size(), answerMap));
                }
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
        excelSheetDto.setDefaultRowHeight((short) 600);
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
                excelDto.setSection(sectionDto.getSection());
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

    private List<InsChklItemExcelDto> getAdhocChklItemExcelDtos(AdhocChecklistConfigDto adhocConfig, int sectionSn,
                                                    Map<String, ChklstItemAnswerDto> answerMap) {
        if (adhocConfig == null || IaisCommonUtils.isEmpty(adhocConfig.getAdhocChecklistItemList())) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<InsChklItemExcelDto> result = IaisCommonUtils.genNewArrayList();
        List<AdhocChecklistItemDto> checklistItemDtos = adhocConfig.getAdhocChecklistItemList();
        for (int j = 0, n = checklistItemDtos.size(); j < n; j++) {
            AdhocChecklistItemDto itemDto = checklistItemDtos.get(j);
            InsChklItemExcelDto excelDto = new InsChklItemExcelDto();
            excelDto.setSnNo((sectionSn + 1) + "." + (j + 1));
            excelDto.setChecklistItem(itemDto.getQuestion());
            excelDto.setSection("Adhoc");
            String itemKey = new StringBuilder()
                    .append(adhocConfig.getId())
                    .append(KEY_SEPARATOR)
                    .append(ChecklistConstants.ADHOC_SECTION_ID)
                    .append(KEY_SEPARATOR)
                    .append(itemDto.getId())
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
        return result;
    }

    private static Map<Integer, Integer> widthMap;

    public static Map<Integer, Integer> getWidthMap() {
        if (widthMap != null) {
            return widthMap;
        }
        widthMap = IaisCommonUtils.genNewHashMap(5);
        widthMap.put(0, 9);
        widthMap.put(1, 18);
        widthMap.put(2, 25);
        widthMap.put(3, 12);
        widthMap.put(4, 25);
        widthMap.put(5, 25);
        widthMap.put(6, 10);
        widthMap.put(7, 12);
        widthMap.put(8, 25);
        widthMap.put(9, 25);
        widthMap.put(10, 15);
        return widthMap;
    }

    private ChklstItemAnswerDto transferDifAnswsertoChkAnswer(BsbAnswerForDifDto answerForDifDto,
                                              String configId, String itemId, String sectionId) {
        ChklstItemAnswerDto answerDto=new ChklstItemAnswerDto();
        answerDto.setAnswer(answerForDifDto.getAnswer());
        answerDto.setFindings(answerForDifDto.getNcs());
        answerDto.setActionRequired(answerForDifDto.getRemark());
        answerDto.setConfigId(configId);
        answerDto.setItemId(itemId);
        answerDto.setSectionId(sectionId);
        answerDto.setFollowupItem(answerForDifDto.getFollowupItem());
        answerDto.setObserveFollowup(answerForDifDto.getObserveFollowup());
        answerDto.setFollowupAction(answerForDifDto.getFollowupAction());
        answerDto.setDueDate(answerForDifDto.getDueDate());
        answerDto.setRectified(answerForDifDto.getIsRec().equals("1"));

        return answerDto;
    }
}
