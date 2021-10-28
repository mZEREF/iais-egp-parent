package com.ecquaria.cloud.moh.iais.action.datasubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationProperty;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    public abstract void start(BaseProcessClass bpc);

    /**
     * StartStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public void doPrepareSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Prepare Switch -----"));
        ParamUtil.setRequestAttr(bpc.request, "title", "New Assisted Reproduction Submission");
        //ParamUtil.setRequestAttr(bpc.request, "smallTitle", "You are submitting for <strong>Patient Information</strong>");
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
    public void doReturn(BaseProcessClass bpc) {
        returnStep(bpc);
    }

    /**
     * StartStep: Return
     *
     * @param bpc
     * @throws
     */
    public abstract void returnStep(BaseProcessClass bpc);

    /**
     * StartStep: PreparePage
     *
     * @param bpc
     * @throws
     */
    public void doPreparePage(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Prepare Page -----"));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, ACTION_TYPE_PAGE);
        preparePage(bpc);
    }

    /**
     * StartStep: PreparePage
     *
     * @param bpc
     * @throws
     */
    public abstract void preparePage(BaseProcessClass bpc);

    /**
     * StartStep: PrepareConfim
     *
     * @param bpc
     * @throws
     */
    public void doPrepareConfim(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("-----" + this.getClass().getSimpleName() + " Prepare Confirm Page -----"));
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, "preview");
        prepareConfim(bpc);
    }

    /**
     * StartStep: PrepareConfim
     *
     * @param bpc
     * @throws
     */
    public abstract void prepareConfim(BaseProcessClass bpc);

    /**
     * StartStep: Draft
     *
     * @param bpc
     * @throws
     */
    public void doDraft(BaseProcessClass bpc) {
        String currentStage = (String) ParamUtil.getRequestAttr(bpc.request, "currentStage");
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, currentStage);
        draft(bpc);
    }

    /**
     * StartStep: Draft
     *
     * @param bpc
     * @throws
     */
    public abstract void draft(BaseProcessClass bpc);

    /**
     * StartStep: Submission
     *
     * @param bpc
     * @throws
     */
    public void doSubmission(BaseProcessClass bpc) {
        ParamUtil.setRequestAttr(bpc.request, DataSubmissionConstant.CURRENT_PAGE_STAGE, "ack");
        submission(bpc);
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(bpc.request);
        DataSubmissionDto dataSubmissionDto = arSuperDataSubmission.getCurrentDataSubmissionDto();
        dataSubmissionDto.setSubmissionNo(arDataSubmissionService.getSubmissionNo(dataSubmissionDto.getSubmissionType(),
                dataSubmissionDto.getCycleStage(), arSuperDataSubmission.getLastDataSubmissionDto()));
        if (StringUtil.isEmpty(dataSubmissionDto.getStatus())) {
            dataSubmissionDto.setStatus(DataSubmissionConsts.DS_STATUS_COMPLETED);
        }
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(bpc.request);
        if (loginContext != null) {
            dataSubmissionDto.setSubmitBy(loginContext.getUserId());
            dataSubmissionDto.setSubmitDt(new Date());
        }
        dataSubmissionDto.setVersion(1);
        arSuperDataSubmission = arDataSubmissionService.saveArSuperDataSubmissionDto(arSuperDataSubmission);
        ParamUtil.setSessionAttr(bpc.request, DataSubmissionConstant.AR_DATA_SUBMISSION, arSuperDataSubmission);
    }

    /**
     * StartStep: Submission
     *
     * @param bpc
     * @throws
     */
    public abstract void submission(BaseProcessClass bpc);

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
        pageConfirmAction(bpc);
    }

    /**
     * StartStep: PageConfirmAction
     *
     * @param bpc
     * @throws
     */
    public abstract void pageConfirmAction(BaseProcessClass bpc);

    public  final boolean validatePageData(HttpServletRequest request, Object obj, String property, String passCrudActionType, String failedCrudActionType, List validationDtos, Map<Object,ValidationProperty> validationPropertyList){
        ValidationResult validationResult = WebValidationHelper.validateProperty(obj, property);
        String  prefix = "";
        String  suffix = "";
        if(IaisCommonUtils.isNotEmpty(validationPropertyList)){
            ValidationProperty validationProperty = validationPropertyList.get(obj);
            if( validationProperty !=null){
                prefix = validationProperty.getPrefix();
                suffix = validationProperty.getSuffix();
            }
        }
        Map<String, String> errorMap = validationResult.retrieveAll( prefix,suffix);
        if( !IaisCommonUtils.isEmpty(validationDtos)){
            for (int i = 0; i < validationDtos.size(); i++) {
                if(IaisCommonUtils.isNotEmpty(validationPropertyList)){
                    ValidationProperty validationProperty = validationPropertyList.get(validationDtos.get(i));
                    if( validationProperty !=null){
                    prefix = validationProperty.getPrefix();
                    suffix = validationProperty.getSuffix();
                    }
                }
                Map<String, String> errorMap1 = WebValidationHelper.validateProperty( validationDtos.get(i), property).retrieveAll(prefix,suffix);
                if(!errorMap1.isEmpty()){
                    errorMap.putAll(errorMap1);
                }
            }
        }
        if (!errorMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,failedCrudActionType);
            return false;
        }else if(StringUtil.isNotEmpty(passCrudActionType)){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE,passCrudActionType);
        }
        return true;
    }

    public  final boolean validatePageData(HttpServletRequest request,Object obj){
        return validatePageData(request,obj,"save");
    }

    public  final boolean validatePageData(HttpServletRequest request,Object obj,List validationDtos){
        return validatePageData(request,obj,"save",ACTION_TYPE_CONFIRM,ACTION_TYPE_PAGE,validationDtos,null);
    }

    public  final boolean validatePageData(HttpServletRequest request,Object obj,String property){
        return validatePageData(request,obj,property,ACTION_TYPE_CONFIRM,ACTION_TYPE_PAGE,null,null);
    }

    public  final boolean validatePageData(HttpServletRequest request,Object obj,String property,List validationDtos){
        return validatePageData(request,obj,property,ACTION_TYPE_CONFIRM,ACTION_TYPE_PAGE,validationDtos,null);
    }

    public  final boolean validatePageDataHaveValidationProperty(HttpServletRequest request,Object obj,List validationDtos, Map<Object,ValidationProperty> validationPropertyList){
        return validatePageDataHaveValidationProperty(request,obj,"save",validationDtos,validationPropertyList);
    }

    public  final boolean validatePageDataHaveValidationProperty(HttpServletRequest request,Object obj,ValidationProperty validationProperty){
        return validatePageDataHaveValidationProperty(request,obj,"save",validationProperty);
    }

    public  final boolean validatePageDataHaveValidationProperty(HttpServletRequest request,Object obj,String property,ValidationProperty validationProperty){
        Map<Object,ValidationProperty> validationPropertyList = IaisCommonUtils.genNewHashMap();
        validationPropertyList.put(obj,validationProperty);
        return validatePageData(request,obj,property,ACTION_TYPE_CONFIRM,ACTION_TYPE_PAGE,null,validationPropertyList);
    }

    public  final boolean validatePageDataHaveValidationProperty(HttpServletRequest request,Object obj,String property,List validationDtos, Map<Object,ValidationProperty> validationPropertyList){
        return validatePageData(request,obj,property,ACTION_TYPE_CONFIRM,ACTION_TYPE_PAGE,validationDtos,validationPropertyList);
    }
}
