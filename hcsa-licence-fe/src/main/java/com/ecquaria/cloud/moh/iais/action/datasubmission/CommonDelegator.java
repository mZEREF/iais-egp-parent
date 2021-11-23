package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationProperty;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;

/**
 * CommonDelegator
 *
 * @author suocheng
 * @date 10/20/2021
 */
@Slf4j
public abstract class CommonDelegator {

    protected static final String ACTION_TYPE_PAGE = "page";
    protected static final String ACTION_TYPE_RETURN = "return";
    protected static final String ACTION_TYPE_CONFIRM = "confirm";
    protected static final String ACTION_TYPE_DRAFT = "draft";
    protected static final String ACTION_TYPE_SUBMISSION = "submission";
    private final static String  CRUD_ACTION_VALUE_AR_STAGE      = "crud_action_value_ar_stage";
    private final static String  CRUD_ACTION_VALUE_VALIATE_DONOR = "crud_action_value_valiate_donor";
    private final static String  CRUD_ACTION_VALUE_AR_STAGE_ACTION_AGE      = "crud_action_value_action_age";
    protected final static String  DONOR_SOURSE_OTHERS             = "Others";
    protected final static String  DONOR_SAMPLE_DROP_DOWN          = "donorSampleDropDown";
    protected final static String  DONOR_SOURSE_DROP_DOWN          = "donorSourseDropDown";

    @Autowired
    private ArDataSubmissionService arDataSubmissionService;

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Start -----"));
        start(bpc);
    }

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void start(BaseProcessClass bpc) {}

    /**
     * StartStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public void doPrepareSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Prepare Switch -----"));
        ArSuperDataSubmissionDto currentArDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        ParamUtil.setRequestAttr(bpc.request, "title", DataSubmissionHelper.getMainTitle(currentArDataSubmission));
        String stage = Optional.ofNullable(currentArDataSubmission.getSelectionDto())
                .map(CycleStageSelectionDto::getStage)
                .orElse("Cycle Stages");
        stage = MasterCodeUtil.getCodeDesc(stage);
        ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>" + stage + "</strong>");
        String actionType = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.info(StringUtil.changeForLog("----- Action Type: " + actionType + " -----"));
        if (StringUtil.isEmpty(actionType)) {
            actionType = ACTION_TYPE_PAGE;
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        }
        prepareSwitch(bpc);
    }

    /**
     * StartStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public abstract void prepareSwitch(BaseProcessClass bpc);

    /**
     * StartStep: Return
     *
     * @param bpc
     * @throws
     */
    public void doReturn(BaseProcessClass bpc) throws IOException {
        returnStep(bpc);
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if (arSuperDataSubmission != null && !DataSubmissionConsts.DS_APP_TYPE_NEW.equals(arSuperDataSubmission.getAppType())) {
            StringBuilder url = new StringBuilder();
            url.append(InboxConst.URL_HTTPS)
                    .append(bpc.request.getServerName())
                    .append(InboxConst.URL_MAIN_WEB_MODULE + "MohInternetInbox");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }
    }

    /**
     * StartStep: Return
     *
     * @param bpc
     * @throws
     */
    public void returnStep(BaseProcessClass bpc) {}

    /**
     * StartStep: PreparePage
     *
     * @param bpc
     * @throws
     */
    public void doPreparePage(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Prepare Page -----"));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE,
                DataSubmissionConstant.PAGE_STAGE_PAGE);
        preparePage(bpc);
    }

    /**
     * StartStep: PreparePage
     *
     * @param bpc
     * @throws
     */
    public void preparePage(BaseProcessClass bpc) {}

    /**
     * StartStep: PrepareConfim
     *
     * @param bpc
     * @throws
     */
    public void doPrepareConfim(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Prepare Confirm Page -----"));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_PREVIEW);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ART);
        prepareConfim(bpc);
    }

    /**
     * StartStep: PrepareConfim
     *
     * @param bpc
     * @throws
     */
    public void prepareConfim(BaseProcessClass bpc) {}

    ;

    /**
     * StartStep: Draft
     *
     * @param bpc
     * @throws
     */
    public void doDraft(BaseProcessClass bpc) {
        String currentStage = (String) ParamUtil.getRequestAttr(bpc.request, "currentStage");
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, currentStage);
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if (arSuperDataSubmission != null) {
            arSuperDataSubmission.setDraftNo(
                    arDataSubmissionService.getDraftNo(DataSubmissionConsts.DS_AR, arSuperDataSubmission.getDraftNo()));
            arSuperDataSubmission = arDataSubmissionService.saveDataSubmissionDraft(arSuperDataSubmission);
            DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, bpc.request);
            ParamUtil.setRequestAttr(bpc.request, "saveDraftSuccess", "success");
        } else {
            log.info(StringUtil.changeForLog("The arSuperDataSubmission is null"));
        }
        draft(bpc);
    }

    /**
     * StartStep: Draft
     *
     * @param bpc
     * @throws
     */
    public void draft(BaseProcessClass bpc) {}

    /**
     * StartStep: Submission
     *
     * @param bpc
     * @throws
     */
    public void doSubmission(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Do Submission -----"));
        submission(bpc);
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DataSubmissionDto dataSubmissionDto = arSuperDataSubmission.getDataSubmissionDto();
        CycleDto cycle = arSuperDataSubmission.getCycleDto();
        String cycleType = cycle.getCycleType();
        if (StringUtil.isEmpty(dataSubmissionDto.getSubmissionNo())) {
            String submissionNo = arDataSubmissionService.getSubmissionNo(arSuperDataSubmission.getSelectionDto(),
                    DataSubmissionConsts.DS_AR);
            dataSubmissionDto.setSubmissionNo(submissionNo);
        }
        if (DataSubmissionConsts.DS_APP_TYPE_RFC.equals(dataSubmissionDto.getAppType())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_AMENDED);
        } else if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
        }
        String stage = dataSubmissionDto.getCycleStage();
        String status = cycle.getStatus();
        if (DataSubmissionConsts.DS_CYCLE_AR.equals(cycleType)) {
            if (DataSubmissionConsts.AR_STAGE_END_CYCLE.equals(stage)) {
                status = DataSubmissionConsts.DS_STATUS_COMPLETED_END_CYCEL;
            } else if (DataSubmissionConsts.AR_STAGE_OUTCOME_OF_EMBRYO_TRANSFERED.equals(stage)) {
                EmbryoTransferredOutcomeStageDto outcomeStageDto = arSuperDataSubmission.getEmbryoTransferredOutcomeStageDto();
                if (StringUtil.isIn(outcomeStageDto, new String[]{
                        DataSubmissionConsts.OUTCOME_OF_EMBRYO_TRANSFERRED_NO_PREGNANCY_DETECTED,
                        DataSubmissionConsts.OUTCOME_OF_EMBRYO_TRANSFERRED_UNKNOWN})) {
                    status = DataSubmissionConsts.DS_STATUS_OET_NO_PREGNACY_UNKNOWN;
                }
            } else if (DataSubmissionConsts.AR_STAGE_OUTCOME_OF_PREGNANCY.equals(stage)) {
                status = DataSubmissionConsts.DS_STATUS_COMPLETED_OUTCOME_OF_PREGNANCY;
            }
        } else if (DataSubmissionConsts.DS_CYCLE_EFO.equals(cycleType)) {
            if (DataSubmissionConsts.AR_STAGE_FREEZING.equals(stage)) {
                status = DataSubmissionConsts.DS_STATUS_COMPLETE_FREEZING;
            }
        } else if (DataSubmissionConsts.DS_CYCLE_IUI.equals(cycleType)) {
            if (DataSubmissionConsts.AR_STAGE_OUTCOME.equals(stage)) {
                OutcomeStageDto outcomeStageDto = arSuperDataSubmission.getOutcomeStageDto();
                if (!outcomeStageDto.getPregnancyDetected()) {
                    status = DataSubmissionConsts.DS_STATUS_OUTCOME_NO_DETECTED;
                }
            }
        } else if (DataSubmissionConsts.DS_CYCLE_NON.equals(cycleType)) {
            status = DataSubmissionConsts.DS_STATUS_COMPLETED;
        }
        if (StringUtil.isEmpty(status)) {
            status = DataSubmissionConsts.DS_STATUS_ACTIVE;
        }
        cycle.setStatus(status);
        arSuperDataSubmission.setCycleDto(cycle);
        log.info(StringUtil.changeForLog("-----Cycle Type: " + cycleType + " - Stage : " + stage
                + " - Status: " + status + " -----"));

        LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
        if (loginContext != null) {
            dataSubmissionDto.setSubmitBy(loginContext.getUserId());
            dataSubmissionDto.setSubmitDt(new Date());
        }
        arSuperDataSubmission = arDataSubmissionService.saveArSuperDataSubmissionDto(arSuperDataSubmission);
        try {
            arSuperDataSubmission = arDataSubmissionService.saveArSuperDataSubmissionDtoToBE(arSuperDataSubmission);
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Eic saveArSuperDataSubmissionDtoToBE failed ===>" + e.getMessage()), e);
        }
        if (!StringUtil.isEmpty(arSuperDataSubmission.getDraftId())) {
            arDataSubmissionService.updateDataSubmissionDraftStatus(arSuperDataSubmission.getDraftId(),
                    DataSubmissionConsts.DS_STATUS_INACTIVE);
        }
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmission);
        ParamUtil.setRequestAttr(bpc.request, "emailAddress", DataSubmissionHelper.getLicenseeEmailAddrs(bpc.request));
        ParamUtil.setRequestAttr(bpc.request, "submittedBy", DataSubmissionHelper.getLoginContext(bpc.request).getUserName());
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, DataSubmissionConstant.PAGE_STAGE_ACK);
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.PRINT_FLAG, DataSubmissionConstant.PRINT_FLAG_ACKART);
    }

    /**
     * StartStep: Submission
     *
     * @param bpc
     * @throws
     */
    public void submission(BaseProcessClass bpc) {}

    /**
     * StartStep: PageAction
     *
     * @param bpc
     * @throws
     */
    public void doPageAction(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Page Action -----"));
        // for draft back
        ParamUtil.setRequestAttr(bpc.request, "currentStage", ACTION_TYPE_PAGE);
        String actionType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        pageAction(bpc);
    }

    /**
     * StartStep: PageAction
     *
     * @param bpc
     * @throws
     */
    public abstract void pageAction(BaseProcessClass bpc);

    /**
     * StartStep: PageConfirmAction
     *
     * @param bpc
     * @throws
     */
    public void doPageConfirmAction(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Confirm Action -----"));
        // for draft back
        ParamUtil.setRequestAttr(bpc.request, "currentStage", ACTION_TYPE_CONFIRM);
        String crud_action_type = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crud_action_type);
        // declaration
        String declaration = ParamUtil.getString(bpc.request, "declaration");
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DataSubmissionDto dataSubmissionDto = arSuperDataSubmission.getDataSubmissionDto();
        dataSubmissionDto.setDeclaration(declaration);
        DataSubmissionHelper.setCurrentArDataSubmission(arSuperDataSubmission, bpc.request);
        // others
        pageConfirmAction(bpc);
    }

    /**
     * StartStep: PageConfirmAction
     *
     * @param bpc
     * @throws
     */
    public void pageConfirmAction(BaseProcessClass bpc) {
        // validation
        String declaration = ParamUtil.getString(bpc.request, "declaration");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
        String actionType = ParamUtil.getString(bpc.request, DataSubmissionConstant.CRUD_TYPE);
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        if (DataSubmissionConsts.DS_APP_TYPE_NEW.equals(arSuperDataSubmission.getAppType())
                && ACTION_TYPE_SUBMISSION.equals(actionType) && StringUtil.isEmpty(declaration)) {
            errorMap.put("declaration", "GENERAL_ERR0006");
        }
        if (!errorMap.isEmpty()) {
            log.error("------No checked for declaration-----");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, ACTION_TYPE_CONFIRM);
        }
    }

    public final boolean validatePageData(HttpServletRequest request, Object obj, String property, String passCrudActionType,
            String failedCrudActionType, List validationDtos, Map<Object, ValidationProperty> validationPropertyList) {
        ValidationResult validationResult = WebValidationHelper.validateProperty(obj, property);
        String prefix = "";
        String suffix = "";
        if (IaisCommonUtils.isNotEmpty(validationPropertyList)) {
            ValidationProperty validationProperty = validationPropertyList.get(obj);
            if (validationProperty != null) {
                prefix = validationProperty.getPrefix();
                suffix = validationProperty.getSuffix();
            }
        }
        Map<String, String> errorMap = validationResult.retrieveAll(prefix, suffix);
        if (!IaisCommonUtils.isEmpty(validationDtos)) {
            for (int i = 0; i < validationDtos.size(); i++) {
                if (IaisCommonUtils.isNotEmpty(validationPropertyList)) {
                    ValidationProperty validationProperty = validationPropertyList.get(validationDtos.get(i));
                    if (validationProperty != null) {
                        prefix = validationProperty.getPrefix();
                        suffix = validationProperty.getSuffix();
                    }
                }
                Map<String, String> errorMap1 = WebValidationHelper.validateProperty(validationDtos.get(i), property).retrieveAll(
                        prefix, suffix);
                if (!errorMap1.isEmpty()) {
                    errorMap.putAll(errorMap1);
                }
            }
        }
        if (!errorMap.isEmpty()) {
            log.info(StringUtil.changeForLog("----- Error Massage: " + errorMap + " -----"));
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, failedCrudActionType);
            return false;
        } else if (StringUtil.isNotEmpty(passCrudActionType)) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, passCrudActionType);
        }
        return true;
    }

    public final boolean validatePageData(HttpServletRequest request, Object obj, String property, String... actionType) {
        return needValidate(request, actionType) ? validatePageData(request, obj, property, ACTION_TYPE_CONFIRM, ACTION_TYPE_PAGE,
                null, null) : true;
    }

    public final boolean validatePageData(HttpServletRequest request, Object obj, String property, List validationDtos,
            String... actionType) {
        return needValidate(request, actionType) ? validatePageData(request, obj, property, ACTION_TYPE_CONFIRM, ACTION_TYPE_PAGE,
                validationDtos, null) : true;
    }

    public final boolean validatePageDataHaveValidationProperty(HttpServletRequest request, Object obj, String property,
            ValidationProperty validationProperty, String... actionType) {
        Map<Object, ValidationProperty> validationPropertyList = IaisCommonUtils.genNewHashMap();
        validationPropertyList.put(obj, validationProperty);
        return needValidate(request, actionType) ? validatePageData(request, obj, property, ACTION_TYPE_CONFIRM, ACTION_TYPE_PAGE,
                null, validationPropertyList) : true;
    }

    public final boolean validatePageDataHaveValidationProperty(HttpServletRequest request, Object obj, String property,
            List validationDtos, Map<Object, ValidationProperty> validationPropertyList, String... actionType) {
        return needValidate(request, actionType) ? validatePageData(request, obj, property, ACTION_TYPE_CONFIRM, ACTION_TYPE_PAGE,
                validationDtos, validationPropertyList) : true;
    }

    private boolean needValidate(HttpServletRequest request, String... actionType) {
        return StringUtil.isIn(ParamUtil.getString(request, DataSubmissionConstant.CRUD_TYPE), actionType);
    }

    protected void actionArDonorDtos(HttpServletRequest request,List<DonorDto> arDonorDtos){
        int actionArDonor = ParamUtil.getInt(request,CRUD_ACTION_VALUE_AR_STAGE);
        //actionArDonor default =-3;
        if(actionArDonor == -1){
            //add
            for (int i = 0; i < arDonorDtos.size(); i++) {
                arDonorDtos.get(i).setArDonorIndex(i);
            }
            arDonorDtos.add(getInitArDonorDto(arDonorDtos.size()));
        }else if(actionArDonor >-1 ){
            //delete
            arDonorDtos.remove(actionArDonor);
            for (int i = 0; i < arDonorDtos.size(); i++) {
                arDonorDtos.get(i).setArDonorIndex(i);
            }
        }else if(actionArDonor == -3){
            arDonorDtos.clear();
        }else if(actionArDonor == -4){
            arDonorDtos.clear();
            arDonorDtos.add(getInitArDonorDto(0));
        }else if(actionArDonor == -5){
            //clearOtherAge
            clearDonorAgesSelect(ParamUtil.getInt(request,CRUD_ACTION_VALUE_AR_STAGE_ACTION_AGE),arDonorDtos);
        }
    }

    private void clearDonorAgesSelect(int actionArDonor,List<DonorDto> arDonorDtos){
        if( actionArDonor != -1){
            DonorDto donorDto = arDonorDtos.get(actionArDonor);
            if(StringUtil.isNotEmpty(donorDto.getSameDonorSampleIndexs())){
                String[] indexs = donorDto.getSameDonorSampleIndexs().split(",");
                boolean clearOwn = false;
                for (String index:indexs) {
                    int indexInt = Integer.parseInt(index);
                    if( indexInt != actionArDonor){
                        DonorDto donorDtoEff = arDonorDtos.get(indexInt);
                        if(donorDto.getSameDonorSampleIndexs().equalsIgnoreCase(donorDtoEff.getSameDonorSampleIndexs())){
                            if(StringUtil.isNotEmpty(donorDto.getAge())){
                                reSetAgeList(donorDtoEff,donorDto.getAge(),DataSubmissionConsts.DONOR_AGE_STATUS_USED);
                                if(StringUtil.isNotEmpty(donorDto.getAgeId()) && !donorDto.getAgeId().equalsIgnoreCase(donorDto.getAge())){
                                    reSetAgeList(donorDtoEff,donorDto.getAgeId(),DataSubmissionConsts.DONOR_AGE_STATUS_ACTIVE);
                                    if(!clearOwn){
                                        clearOwn = true;
                                        reSetAgeList(donorDto,donorDto.getAgeId(),DataSubmissionConsts.DONOR_AGE_STATUS_ACTIVE);
                                    }
                                }
                                setAgeList(donorDtoEff);
                                setAgeList(donorDto);
                            }else if(!clearOwn && StringUtil.isNotEmpty(donorDto.getAgeId())){
                                clearOwn = true;
                                reSetAgeList(donorDto,donorDto.getAgeId(),DataSubmissionConsts.DONOR_AGE_STATUS_ACTIVE);
                                setAgeList(donorDto);
                            }

                        }else {
                            clearDonorAges(donorDtoEff);
                        }
                    }
                }
            }
        }
    }

    private void reSetAgeList(DonorDto arDonorDto,String ageId,String status){
        arDonorDto.getDonorSampleAgeDtos().stream().forEach(age->{
            if(ageId.equalsIgnoreCase(age.getId())){
                age.setStatus(status);
            }
        });

    }

    private void setAgeList(DonorDto arDonorDto){
        if(IaisCommonUtils.isNotEmpty(arDonorDto.getAgeList())){
            arDonorDto.getAgeList().clear();
        }
        List<SelectOption> ageList = IaisCommonUtils.genNewArrayList();
        arDonorDto.getDonorSampleAgeDtos().stream().forEach(obj->{
           if(DataSubmissionConsts.DONOR_AGE_STATUS_ACTIVE.equalsIgnoreCase(obj.getStatus())){
               ageList.add(new SelectOption(obj.getId(),String.valueOf(obj.getAge())));
           }
        });
        arDonorDto.setAgeList(ageList);
    }


    private DonorDto getInitArDonorDto(int arDonorIndex){
        DonorDto arDonorDto = new DonorDto();
        arDonorDto.setDirectedDonation(true);
        arDonorDto.setArDonorIndex(arDonorIndex);
        return arDonorDto;
    }

    protected void valiateDonorDtos(HttpServletRequest request,List<DonorDto> arDonorDtos){
        int valiateArDonor = ParamUtil.getInt(request,CRUD_ACTION_VALUE_VALIATE_DONOR);
        if(valiateArDonor >-1){
            DonorDto arDonorDto = arDonorDtos.get(valiateArDonor);
            DonorSampleDto donorSampleDto = arDataSubmissionService.getDonorSampleDto(arDonorDto.getIdType(),arDonorDto.getIdNumber(),
                    arDonorDto.getDonorSampleCode(),arDonorDto.getSource(),arDonorDto.getOtherSource());
            if(donorSampleDto == null){
                Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(2);
                errorMap.put("field1","The corresponding donor");
                errorMap.put("field2","the AR centre");
                String dsErr =  MessageUtil.getMessageDesc("DS_ERR012",errorMap).trim();
                errorMap.clear();
                errorMap.put("validateDonor" +(arDonorDto.isDirectedDonation() ? "Yes" : "No") +arDonorDto.getArDonorIndex(),dsErr);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            }else {
                arDonorDto.setRelation(donorSampleDto.getDonorRelation());
                List<DonorSampleAgeDto> ages = donorSampleDto.getDonorSampleAgeDtos();
                arDonorDto.setDonorSampleAgeDtos(ages);
                arDonorDto.setDonorSampleId(donorSampleDto.getId());
                if(IaisCommonUtils.isNotEmpty(ages)){
                    getDonorSampleAgeDtos(arDonorDtos,ages);
                    if(IaisCommonUtils.isNotEmpty(ages)){
                        arDonorDto.setResetDonor(AppConsts.NO);
                        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList( ages.size());
                        ages.stream().forEach(
                                obj-> selectOptions.add(new SelectOption(obj.getId(),String.valueOf(obj.getAge())))
                        );
                        arDonorDto.setAgeList(selectOptions);
                    }
                }
            }
        }
    }

    private  List<DonorSampleAgeDto> getDonorSampleAgeDtos(List<DonorDto> arDonorDtos,List<DonorSampleAgeDto> ages) {
        StringBuilder stringBuilder = new StringBuilder();
        Map<String,String> stringIntegerMap = IaisCommonUtils.genNewHashMap();
        arDonorDtos.stream().forEach( arDonorDto-> {
            if(IaisCommonUtils.isNotEmpty(arDonorDto.getDonorSampleAgeDtos()) && StringUtil.isNotEmpty(arDonorDto.getDonorSampleId())){
                String value = stringIntegerMap.get(arDonorDto.getDonorSampleId());
                 if(StringUtil.isNotEmpty(value)){
                     stringIntegerMap.put(arDonorDto.getDonorSampleId(),value+arDonorDto.getArDonorIndex()+",");
                 }else {
                     stringIntegerMap.put(arDonorDto.getDonorSampleId(),arDonorDto.getArDonorIndex()+",");
                 }
                arDonorDto.getDonorSampleAgeDtos().stream().forEach( age -> {
                    if(DataSubmissionConsts.DONOR_AGE_STATUS_USED.equalsIgnoreCase(age.getStatus())){
                        stringBuilder.append(age).append(",");
                    }
                });
            }
        });

        if(IaisCommonUtils.isNotEmpty(stringIntegerMap)){
            arDonorDtos.stream().forEach( arDonorDto-> arDonorDto.setSameDonorSampleIndexs( stringIntegerMap.get(arDonorDto.getDonorSampleId())));
        }

        String values = stringBuilder.toString();
        if(StringUtil.isNotEmpty(values)){
            Iterator<DonorSampleAgeDto> iterator =  ages.listIterator();
            while (iterator.hasNext()){
                DonorSampleAgeDto donorSampleAgeDto = iterator.next();
                if(values.contains(donorSampleAgeDto.getId())){
                    iterator.remove();
                }
            }
        }
       return ages;
    }
    protected void clearNoClearDataForDrDonorDto(DonorDto arDonorDto,HttpServletRequest request){
        if(arDonorDto.isDirectedDonation()){
            arDonorDto.setDonorSampleCode(null);
            arDonorDto.setSource(null);
            arDonorDto.setOtherSource(null);
            arDonorDto.setIdType(StringUtil.getNonNull(arDonorDto.getIdType()));
            arDonorDto.setIdNumber(StringUtil.getNonNull(arDonorDto.getIdNumber()));
        }else {
            arDonorDto.setPleaseIndicate(null);
            arDonorDto.setPleaseIndicateValues(null);
            arDonorDto.setIdType(ParamUtil.getString(request,"idTypeSample"+arDonorDto.getArDonorIndex()));
            arDonorDto.setSource(StringUtil.getNonNull(arDonorDto.getSource()));
            arDonorDto.setOtherSource(StringUtil.getNonNull(arDonorDto.getOtherSource()));
            arDonorDto.setDonorSampleCode(StringUtil.getNonNull(arDonorDto.getDonorSampleCode()));
        }

        if(!AppConsts.NO.equalsIgnoreCase(arDonorDto.getResetDonor())){
            clearDonorAges(arDonorDto);
        }
    }

    private void clearDonorAges(DonorDto arDonorDto){
        arDonorDto.setAgeId(null);
        arDonorDto.setDonorSampleId(null);
        arDonorDto.setAgeList(null);
        arDonorDto.setAge(null);
        arDonorDto.setRelation(null);
        arDonorDto.setDonorSampleAgeDtos(null);
        arDonorDto.setSameDonorSampleIndexs(null);
    }

     protected void setEmptyDataForNullDrDonorDto(DonorDto arDonorDto){
        arDonorDto.setIdType(StringUtil.getStringEmptyToNull( arDonorDto.getIdType()));
        if(arDonorDto.isDirectedDonation()){
            arDonorDto.setIdNumber(StringUtil.getStringEmptyToNull( arDonorDto.getIdNumber()));
        }else {
            arDonorDto.setSource(StringUtil.getStringEmptyToNull(arDonorDto.getSource()));
            arDonorDto.setOtherSource(StringUtil.getStringEmptyToNull(arDonorDto.getOtherSource()));
            arDonorDto.setDonorSampleCode(StringUtil.getStringEmptyToNull(arDonorDto.getDonorSampleCode()));
        }

        if( StringUtil.isNotEmpty(arDonorDto.getAge())){
            arDonorDto.getDonorSampleAgeDtos().stream().forEach( donorSampleAgeDto -> {
                        if(donorSampleAgeDto.getId().equalsIgnoreCase( arDonorDto.getAge())){
                            arDonorDto.setAgeId(arDonorDto.getAge());
                            donorSampleAgeDto.setStatus(DataSubmissionConsts.DONOR_AGE_STATUS_USED);
                        }else {
                            donorSampleAgeDto.setStatus(DataSubmissionConsts.DONOR_AGE_STATUS_INACTIVE);
                        }
                    }
            );
        }else {
            arDonorDto.setAgeId(null);
        }
    }

    protected Map<Object,ValidationProperty> getByArCycleStageDto(List<DonorDto> donorDtos){
        Map<Object,ValidationProperty> validationPropertyList = IaisCommonUtils.genNewHashMap();
        if(IaisCommonUtils.isNotEmpty(donorDtos)){
            donorDtos.forEach( arDonorDto -> {
                        validationPropertyList.put(arDonorDto,new ValidationProperty(arDonorDto.getArDonorIndex(),null,String.valueOf(arDonorDto.getArDonorIndex())));
                    }
            );
        }
        return validationPropertyList;
    }

    protected void setDonorDtos(HttpServletRequest request, List<DonorDto> arDonorDtos,boolean needPleaseIndicate){
        if(IaisCommonUtils.isNotEmpty(arDonorDtos)){
            arDonorDtos.forEach(arDonorDto -> {
                String arDonorIndex = String.valueOf(arDonorDto.getArDonorIndex());
                ControllerHelper.get(request,arDonorDto,arDonorIndex);
                if(needPleaseIndicate){
                arDonorDto.setPleaseIndicate(ParamUtil.getStringsToString(request,"pleaseIndicate"+arDonorIndex));
                arDonorDto.setPleaseIndicateValues(ParamUtil.getListStrings(request,"pleaseIndicate"+arDonorIndex));
                }
                clearNoClearDataForDrDonorDto(arDonorDto,request);
                if(needPleaseIndicate){
                    setArDonorDtoByPleaseIndicate(arDonorDto);
                }
            });
        }
    }

    private void setArDonorDtoByPleaseIndicate(DonorDto arDonorDto){
        if(StringUtil.isNotEmpty(arDonorDto.getPleaseIndicate())){
            String pleaseIndicate = arDonorDto.getPleaseIndicate();
            arDonorDto.setDonorIndicateEmbryo(pleaseIndicate.contains(DataSubmissionConsts.AR_DONOR_USED_TYPE_DONORS_EMBRYO_USED));
            arDonorDto.setDonorIndicateFresh(pleaseIndicate.contains(DataSubmissionConsts.AR_DONOR_USED_TYPE_DONORS_FRESH_OOCYTE));
            arDonorDto.setDonorIndicateFrozen(pleaseIndicate.contains(DataSubmissionConsts.AR_DONOR_USED_TYPE_DONORS_FROZEN_OOCYTE_USED));
            arDonorDto.setDonorIndicateSperm(pleaseIndicate.contains(DataSubmissionConsts.AR_DONOR_USED_TYPE_DONORS_SPERMS_USED));
        }
    }

    //TODO from ar center
    protected List<SelectOption> getSourseList(HttpServletRequest request){
        List<SelectOption> selectOptions  = DataSubmissionHelper.genPremisesOptions((Map<String, PremisesDto>) ParamUtil.getSessionAttr(request,DataSubmissionConstant.AR_PREMISES_MAP));
        selectOptions.add(new SelectOption(DataSubmissionConsts.AR_SOURCE_OTHER,DONOR_SOURSE_OTHERS));
        return selectOptions;
    }

    protected List<SelectOption> getSampleDropDown(){
        List<SelectOption> selectOptions  = IaisCommonUtils.genNewArrayList(4);
        selectOptions.add(new SelectOption(DataSubmissionConsts.AR_ID_TYPE_CODE,"Code"));
        MasterCodeUtil.retrieveByCategory(MasterCodeUtil.CATE_ID_DS_ID_TYPE).stream().forEach(
                obj -> selectOptions.add(new SelectOption(obj.getCode(),obj.getCodeValue()))
        );
        return selectOptions;
    }
}
