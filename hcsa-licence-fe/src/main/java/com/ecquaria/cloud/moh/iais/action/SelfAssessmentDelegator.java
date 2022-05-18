package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessmentConfig;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
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
import java.util.LinkedHashMap;
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
     * Refer from InterInboxDelegator.doSelfAssMt
     *
     * @AutoStep: startStep
     * @author: yichen
     */
    public void startStep(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        // action: inbox - (appSelfFlag: 0);  other: rfi
        String action = ParamUtil.getString(bpc.request, HcsaAppConst.SESSION_SELF_DECL_ACTION);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.SESSION_SELF_DECL_ACTION, action);

        // From inbox action
        String groupId;
        try {
            groupId = ParamUtil.getMaskedString(bpc.request, HcsaAppConst.SESSION_PARAM_APPLICATION_GROUP_ID);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            groupId = ParamUtil.getString(bpc.request, HcsaAppConst.SESSION_PARAM_APPLICATION_GROUP_ID);
        }
        log.info("SelfAssessmentDelegator [startStep] group id  {}", groupId);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.SESSION_PARAM_APPLICATION_GROUP_ID, groupId);

        String appNo;
        try {
            appNo = ParamUtil.getMaskedString(bpc.request, HcsaAppConst.SESSION_SELF_DECL_APPLICATION_NUMBER);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            appNo = ParamUtil.getString(bpc.request, HcsaAppConst.SESSION_SELF_DECL_APPLICATION_NUMBER);
        }

        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.SESSION_SELF_DECL_APPLICATION_NUMBER, appNo);
        if (StringUtils.isNotEmpty(appNo)){
            log.info("SelfAssessmentDelegator [startStep] when self ass rfi , the application number is  {}", appNo);
            AppPremisesCorrelationDto correlation = selfAssessmentService.getCorrelationByAppNo(appNo);
            if (Optional.ofNullable(correlation).isPresent()){
                ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.SESSION_SELF_DECL_RFI_CORR_ID, correlation.getId());
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
        String action = (String) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.SESSION_SELF_DECL_ACTION);
        log.info("SelfAssessmentDelegator [preLoad] action {}", action);
        if ("new".equalsIgnoreCase(action)){
            ParamUtil.setSessionAttr(bpc.request, REDIRECT_TO_MAIN_FLAG, "N");
        }else {
            ParamUtil.setSessionAttr(bpc.request, REDIRECT_TO_MAIN_FLAG, "Y");
        }

        String ack026 = MessageUtil.getMessageDesc("NEW_ACK026");
        List<SelfAssessment> selfAssessmentList;
        boolean hasSubmitted;
        if (SelfAssessmentConstant.SELF_ASSESSMENT_RFI_ACTION.equals(action)){
            String corrId = (String) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.SESSION_SELF_DECL_RFI_CORR_ID);
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
            String groupId = (String) ParamUtil.getSessionAttr(bpc.request, HcsaAppConst.SESSION_PARAM_APPLICATION_GROUP_ID);
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

        LinkedHashMap<String, SelfAssessment> correlationMap = new LinkedHashMap<>();
        for (SelfAssessment s : selfAssessmentList) {
            correlationMap.put(s.getCorrId(), s);
        }

        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_MAP, correlationMap);
    }

    private void loadSelfAssessment(HttpServletRequest request) {
        SelfAssessment selfAssessment = getCurrentSelfAssessment(request);
        if (selfAssessment == null ){
            return;
        }
        List<SelfAssessmentConfig> selfAssessmentConfig = selfAssessment.getSelfAssessmentConfig();
        if(IaisCommonUtils.isEmpty(selfAssessmentConfig)){
            return;
        }
        ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR, selfAssessment);
        // look <input type="hidden" name="tagIndex" value="<iais:mask name="tagIndex" value="${tagIndex}"/>">
        String idx = selfAssessmentConfig.get(0).getConfigId();
        ParamUtil.setRequestAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION, idx);
        ParamUtil.setRequestAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_LAST_TAB_INDEX_POSTION, idx);
    }

    /**
     * @AutoStep: switchNextStep
     * @author: yichen
     */
    public void switchNextStep(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String tagIndex = ParamUtil.getMaskedString(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION);
        SelfAssessment selfAssessment = (SelfAssessment) ParamUtil.getSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR);
        log.info("SelfAssessmentDelegator [switchNextStep] selfAssessment INFO  ..{}..", JsonUtil.parseToJson(selfAssessment));
        doAnswerAction(request, selfAssessment, false);
        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR, selfAssessment);
        ParamUtil.setRequestAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION, tagIndex);
        ParamUtil.setRequestAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_LAST_TAB_INDEX_POSTION, tagIndex);
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
     * @AutoStep: printChecklist
     * @author: yichen
     */
    public void printChecklist(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_CAN_EDIT_ANSWER_FLAG, "true");
        loadSelfAssessment(bpc.request);
    }

    /**
     * @AutoStep: submitAllSelfAssessment
     * @author: yichen
     */
    public void submitAllSelfAssessment(BaseProcessClass bpc) {
        LinkedHashMap<String, SelfAssessment> correlationMap = (LinkedHashMap<String, SelfAssessment>) ParamUtil.getSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_MAP);
        if (Optional.ofNullable(correlationMap).isPresent()) {
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            StringBuilder errorIndexStr = new StringBuilder();
            List<SelfAssessment> list = IaisCommonUtils.genNewArrayList();
            int i = 1;
            for (Map.Entry<String, SelfAssessment> entry : correlationMap.entrySet()){
                SelfAssessment selfAssessment = entry.getValue();
                if (selfAssessment.isCanEdit()){
                    errorIndexStr.append(i).append('/');
                }
                i++;
                list.add(selfAssessment);
            }

            if (errorIndexStr.length() > 0) {
                errorMap.put("noFillUpItemError", MessageUtil.replaceMessage("NEW_ERR0022",errorIndexStr.substring(0, errorIndexStr.length() -1),"errorIndex"));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                WebValidationHelper.saveAuditTrailForNoUseResult(correlationMap, errorMap);
            } else {
                log.info("SelfAssessmentDelegator [submitAllSelfAssessment] START {}", JsonUtil.parseToJson(list));
                selfAssessmentService.saveAllSelfAssessment(list);
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

    private SelfAssessment getCurrentSelfAssessment(HttpServletRequest request) {
        String corrId = ParamUtil.getMaskedString(request, SelfAssessmentConstant.SELF_ASSESSMENT_EACH_DETAIL_CORR_ID);
        LinkedHashMap<String, SelfAssessment> correlationMap = (LinkedHashMap<String, SelfAssessment>) ParamUtil.getSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_MAP);
        return correlationMap.get(corrId);
    }

    /**
     * @AutoStep: clearAnswer
     * @author: yichen
     */
    public void clearAnswer(BaseProcessClass bpc) {
        SelfAssessment selfAssessment = (SelfAssessment) ParamUtil.getSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR);
        String tagIndex = ParamUtil.getMaskedString(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION);
        doAnswerAction(bpc.request, selfAssessment, true);
        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR, selfAssessment);
        ParamUtil.setRequestAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION, tagIndex);
        ParamUtil.setRequestAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_LAST_TAB_INDEX_POSTION, tagIndex);
    }

    private void doAnswerAction(HttpServletRequest request, SelfAssessment detail, boolean isClear) {
        if (detail == null) return;

        log.info("doAnswerAction =====>>>>>>>>>self assessment {}, action {}", JsonUtil.parseToJson(detail), isClear);
        //clear count
        detail.setCompleteCount(0);
        int answerCount = 0;
        int count = 0;
        List<SelfAssessmentConfig> list = detail.getSelfAssessmentConfig();
        for (SelfAssessmentConfig s : list) {
            for (Map.Entry<String, List<PremCheckItem>> entry : s.getSqMap().entrySet()){
                List<PremCheckItem> answerList = entry.getValue();
                int size = 0;
                for (PremCheckItem item : answerList) {
                    if (isClear){
                        item.setAnswer(null);
                    }else {
                        String answer = ParamUtil.getString(request, item.getAnswerKey());
                        if (StringUtil.isNotEmpty(answer)) {
                            item.setAnswer(answer);
                        }
                    }

                    size++;
                    if (StringUtil.isNotEmpty(item.getAnswer())){
                        count++;
                    }
                }
                answerCount += size;
            }
        }
        detail.setCompleteCount(count);
        detail.setAnswerCount(answerCount);
        log.info("doAnswerAction =====>>>>>>>>>Complete Count {}", detail.getCompleteCount());
    }

    /**
     * @AutoStep: draftItem
     * @author: yichen
     */
    public void draftItem(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SelfAssessment mt = (SelfAssessment) ParamUtil.getSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR);
        if (Optional.ofNullable(mt).isPresent()) {
            doAnswerAction(request, mt, false);
            if (mt.isCompletedAnswer()) {
                mt.setCanEdit(false);
                LinkedHashMap<String, SelfAssessment> correlationMap = (LinkedHashMap<String, SelfAssessment>) ParamUtil.getSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_MAP);
                correlationMap.put(mt.getCorrId(), mt);
                ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_MAP, correlationMap);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            } else {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("noFillUpItemError", "UC_INSTA004_ERR008"));
                String tagIndex = ParamUtil.getMaskedString(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION);
                ParamUtil.setRequestAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION, tagIndex);
            }
            ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR, mt);
        }
    }

    private void responseMsg(HttpServletRequest request){
        String messageId = (String) ParamUtil.getSessionAttr(request, AppConsts.SESSION_INTER_INBOX_MESSAGE_ID);
        if (StringUtil.isNotEmpty(messageId)) {
            inspecUserRecUploadService.updateMessageStatus(messageId, MessageConstants.MESSAGE_STATUS_RESPONSE);
        }
    }
}
