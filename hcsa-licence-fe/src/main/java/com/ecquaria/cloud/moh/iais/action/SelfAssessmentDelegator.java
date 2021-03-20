package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import com.ecquaria.cloud.moh.iais.constant.SelfAssessmentConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.InspecUserRecUploadService;
import com.ecquaria.cloud.moh.iais.service.SelfAssessmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @Author: yichen
 * @Description:
 * @Date:2020/5/6
 **/

@Delegator(value = "selfAssessmentDelegator")
@Slf4j
public class SelfAssessmentDelegator {

    private final static String REDIRECT_TO_MAIN_FLAG = "redirectFlag";
    
    @Autowired
    private SelfAssessmentService selfAssessmentService;

    @Autowired
    private InspecUserRecUploadService inspecUserRecUploadService;

    @Autowired
    private AppSubmissionService appSubmissionService;

    /**
     * @AutoStep: startStep
     * @author: yichen
     */
    public void startStep(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String action = ParamUtil.getString(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_ACTION);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_ACTION, action);

        String groupId;
        try {
            groupId = ParamUtil.getMaskedString(bpc.request, NewApplicationConstant.SESSION_PARAM_APPLICATION_GROUP_ID);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            groupId = ParamUtil.getString(bpc.request, NewApplicationConstant.SESSION_PARAM_APPLICATION_GROUP_ID);
        }
        log.info("SelfAssessmentDelegator [startStep] group id  {}", groupId);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationConstant.SESSION_PARAM_APPLICATION_GROUP_ID, groupId);

        String appNo;
        try {
            appNo = ParamUtil.getMaskedString(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_APPLICATION_NUMBER);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            appNo = ParamUtil.getString(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_APPLICATION_NUMBER);
        }

        ParamUtil.setSessionAttr(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_APPLICATION_NUMBER, appNo);
        if (StringUtils.isNotEmpty(appNo)){
            log.info("SelfAssessmentDelegator [startStep] when self ass rfi , the application number is  {}", appNo);
            AppPremisesCorrelationDto correlation = selfAssessmentService.getCorrelationByAppNo(appNo);
            if (Optional.ofNullable(correlation).isPresent()){
                ParamUtil.setSessionAttr(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_RFI_CORR_ID, correlation.getId());
            }
        }

        String messageId = (String) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        if (StringUtil.isNotEmpty(messageId)){
            log.info("SelfAssessmentDelegator [startStep] message id  {}", messageId);
            InterMessageDto interMessageDto = appSubmissionService.getInterMessageById(messageId);
            ParamUtil.setSessionAttr(request,"msg_action_id",messageId);
            ParamUtil.setSessionAttr(request,"msg_action_type",interMessageDto.getMessageType());
            ParamUtil.setSessionAttr(request,"IAIS_MSG_CONTENT",interMessageDto.getMsgContent());
        }

        AuditTrailHelper.auditFunctionWithAppNo(AuditTrailConsts.MODULE_MAIN_FUNCTION, AuditTrailConsts.FUNCTION_SELF_ASSESSMENT, appNo);
        ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_HAS_SUBMITTED_FLAG, null);
        ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_HAS_SUBMITTED_ERROR_MSG, null);
        ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_MAP, null);
        ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR, null);
        ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION, null);
        ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR, null);
        ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_CAN_EDIT_ANSWER_FLAG, null);
        ParamUtil.setSessionAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,"Self-assessment listing");
    }

    /**
     * @AutoStep: startStep
     * @author: yichen
     */
    public void preLoad(BaseProcessClass bpc) {
        String action = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_ACTION);
        log.info("SelfAssessmentDelegator [preLoad] action {}", action);
        if ("new".equals(action)){
            ParamUtil.setSessionAttr(bpc.request, REDIRECT_TO_MAIN_FLAG, "N");
        }else {
            ParamUtil.setSessionAttr(bpc.request, REDIRECT_TO_MAIN_FLAG, "Y");
        }

        String ack026 = MessageUtil.getMessageDesc("NEW_ACK026");
        List<SelfAssessment> selfAssessmentList;
        boolean hasSubmitted;
        if (SelfAssessmentConstant.SELF_ASSESSMENT_RFI_ACTION.equals(action)){
            String corrId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_RFI_CORR_ID);
            if (StringUtil.isEmpty(corrId)) {
                log.info("the corrId id is null");
                return;
            }

            hasSubmitted = selfAssessmentService.hasSubmittedSelfAssMtRfiByCorrId(corrId).booleanValue();
            if (hasSubmitted) {
                ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_HAS_SUBMITTED_FLAG, "Y");
                ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_HAS_SUBMITTED_ERROR_MSG, ack026);
                responseMsg(bpc.request);
            }
            selfAssessmentList = selfAssessmentService.receiveSelfAssessmentRfiByCorrId(corrId);
            log.info("SelfAssessmentDelegator [preLoad] corrId {} , submit flag {}", corrId, hasSubmitted);
        }else {
            String groupId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationConstant.SESSION_PARAM_APPLICATION_GROUP_ID);
            if (StringUtil.isEmpty(groupId)) {
                log.info("the group id is null");
                return;
            }

            hasSubmitted = selfAssessmentService.hasSubmittedSelfAssMtByGroupId(groupId).booleanValue();
            if (hasSubmitted) {
                ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_HAS_SUBMITTED_FLAG, "Y");
                ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_HAS_SUBMITTED_ERROR_MSG, ack026);
                selfAssessmentList = selfAssessmentService.receiveSubmittedSelfAssessmentDataByGroupId(groupId);
                responseMsg(bpc.request);
            } else {
                selfAssessmentList = selfAssessmentService.receiveSelfAssessmentByGroupId(groupId);
            }
            log.info("SelfAssessmentDelegator [preLoad] groupId {} , submit flag {}", groupId, hasSubmitted);
        }

        Map<String, Integer> correlationMap = IaisCommonUtils.genNewHashMap();
        int index = 0;
        for (SelfAssessment s : selfAssessmentList) {
            correlationMap.put(s.getCorrId(), index++);
        }

        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_MAP, (Serializable) correlationMap);
        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR, (Serializable) selfAssessmentList);
    }

    private void loadSelfAssessment(HttpServletRequest request) {
        int index = getCurrentSelfAssessmentIndexInList(request).intValue();
        List<SelfAssessment> selfAssessmentList = (List<SelfAssessment>) ParamUtil.getSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR);
        if (index >= 0 && IaisCommonUtils.isNotEmpty(selfAssessmentList)) {
            ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR, selfAssessmentList.get(index));
        }
    }

    /**
     * @AutoStep: switchNextStep
     * @author: yichen
     */
    public void switchNextStep(BaseProcessClass bpc) {
    }

    /**
     * @AutoStep: switchAction
     * @author: yichen
     */
    public void switchAction(BaseProcessClass bpc) {

    }

    /**
     * @AutoStep: viewSelfAssessment
     * @author: yichen
     */
    public void viewSelfAssessment(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_CAN_EDIT_ANSWER_FLAG, "true");
        loadSelfAssessment(bpc.request);
    }

    /**
     * @AutoStep: submitAllSelfAssessment
     * @author: yichen
     */
    public void submitAllSelfAssessment(BaseProcessClass bpc) {
        List<SelfAssessment> selfAssessmentList = (List<SelfAssessment>) ParamUtil.getSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR);
        if (Optional.ofNullable(selfAssessmentList).isPresent()) {
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            StringBuilder errorIndexStr = new StringBuilder();
            for (int i = 0; i < selfAssessmentList.size(); i++) {
                SelfAssessment mt = selfAssessmentList.get(i);
                if (mt.isCanEdit()){
                    errorIndexStr.append(i + 1).append("/");
                }
            }

            if (errorIndexStr.length() > 0) {
                errorMap.put("noFillUpItemError", MessageUtil.replaceMessage("NEW_ERR0022",errorIndexStr.substring(0, errorIndexStr.length() -1),"errorIndex"));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                WebValidationHelper.saveAuditTrailForNoUseResult(selfAssessmentList, errorMap);
            } else {
                log.info("SelfAssessmentDelegator [submitAllSelfAssessment] START {}", JsonUtil.parseToJson(selfAssessmentList));
                selfAssessmentService.saveAllSelfAssessment(selfAssessmentList);
                log.info("SelfAssessmentDelegator [submitAllSelfAssessment] END");
                responseMsg(bpc.request);
                ParamUtil.setRequestAttr(bpc.request, "ackMsg", MessageUtil.getMessageDesc("GENERAL_ERR0038"));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }
        }
    }

    /**
     * @AutoStep: loadChecklist
     * @author: yichen
     */
    public void loadChecklist(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_CAN_EDIT_ANSWER_FLAG, null);
        loadSelfAssessment(bpc.request);
    }

    private Integer getCurrentSelfAssessmentIndexInList(HttpServletRequest request) {
        String selfAssessmentCorrId = ParamUtil.getMaskedString(request, SelfAssessmentConstant.SELF_ASSESSMENT_EACH_DETAIL_CORR_ID);
        Map<String, Integer> correlationMap = (Map<String, Integer>) ParamUtil.getSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_MAP);
        return Optional.ofNullable(correlationMap).map(item -> item.get(selfAssessmentCorrId)).orElseGet(() -> -1);
    }

    /**
     * @AutoStep: clearAnswer
     * @author: yichen
     */
    public void clearAnswer(BaseProcessClass bpc) {
        SelfAssessment selfAssessment = (SelfAssessment) ParamUtil.getSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR);
        selfAssessmentService.doAnswerAction(bpc.request, selfAssessment, true);
        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR, selfAssessment);
    }

    /**
     * @AutoStep: draftItem
     * @author: yichen
     */
    public void draftItem(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SelfAssessment mt = (SelfAssessment) ParamUtil.getSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR);
        if (Optional.ofNullable(mt).isPresent()) {
            selfAssessmentService.doAnswerAction(request, mt, false);
            if (mt.isCompletedAnswer()) {
                mt.setCanEdit(false);
                List<SelfAssessment> list = (List<SelfAssessment>) ParamUtil.getSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR);
                int index = getCurrentSelfAssessmentIndexInList(request);
                if (index >= 0) {
                    list.set(index, mt);
                }

                ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR, (Serializable) list);
                ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR, mt);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            } else {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("noFillUpItemError", "UC_INSTA004_ERR008"));
            }
        }
    }

    private void responseMsg(HttpServletRequest request){
        String messageId = (String) ParamUtil.getSessionAttr(request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        if (StringUtil.isNotEmpty(messageId)) {
            inspecUserRecUploadService.updateMessageStatus(messageId, MessageConstants.MESSAGE_STATUS_RESPONSE);
        }
    }
}
