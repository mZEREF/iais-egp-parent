package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessmentConfig;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import com.ecquaria.cloud.moh.iais.constant.SelfAssessmentConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.SelfAssessmentService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: yichen
 * @Description:
 * @Date:2020/5/6
 **/

@Delegator(value = "selfAssessmentDelegator")
@Slf4j
public class SelfAssessmentDelegator {

    @Autowired
    private SelfAssessmentService selfAssessmentService;

    /**
     * @param bpc
     * @Decription startStep
     */
    public void startStep(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String action = ParamUtil.getString(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_ACTION);
        if (SelfAssessmentConstant.SELF_ASSESSMENT_RFI_ACTION.equals(action)){
            String corrId = ParamUtil.getMaskedString(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_RFI_CORR_ID);
            ParamUtil.setSessionAttr(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_RFI_CORR_ID, corrId);
        }

        AuditTrailHelper.auditFunction("Application", "Self assessment");
        ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_HAS_SUBMITTED_FLAG, null);
        ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_HAS_SUBMITTED_ERROR_MSG, null);
        ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_MAP, null);
        ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR, null);
        ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION, null);
        ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR, null);
        ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_CAN_EDIT_ANSWER_FLAG, null);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_ACTION, null);

    }

    /**
     * @param bpc
     * @Decription preLoad
     */
    public void preLoad(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE,"Self-assessment listing");
        String action = ParamUtil.getString(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_ACTION);
        if (StringUtil.isEmpty(action)){
            action = (String) ParamUtil.getScopeAttr(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_ACTION);
        }

        ParamUtil.setSessionAttr(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_ACTION, action);

        List<SelfAssessment> selfAssessmentList;
        boolean hasSubmitted;
        if (SelfAssessmentConstant.SELF_ASSESSMENT_RFI_ACTION.equals(action)){
            String corrId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_RFI_CORR_ID);
            if (StringUtil.isEmpty(corrId)) {
                log.debug("the corrId id is null");
                return;
            }

            hasSubmitted = selfAssessmentService.hasSubmittedSelfAssMtRfiByCorrId(corrId).booleanValue();
            if (hasSubmitted) {
                ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_HAS_SUBMITTED_FLAG, "Y");
                ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_HAS_SUBMITTED_ERROR_MSG, MessageUtil.getMessageDesc("ACK026"));
            }

            selfAssessmentList = selfAssessmentService.receiveSelfAssessmentRfiByCorrId(corrId);
        }else {
            String groupId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationConstant.SESSION_PARAM_APPLICATION_GROUP_ID);

            if (StringUtil.isEmpty(groupId)) {
                log.debug("the group id is null");
                return;
            }

            hasSubmitted = selfAssessmentService.hasSubmittedSelfAssMtByGroupId(groupId).booleanValue();
            if (hasSubmitted) {
                ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_HAS_SUBMITTED_FLAG, "Y");
                ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_HAS_SUBMITTED_ERROR_MSG, MessageUtil.getMessageDesc("ACK026"));
                selfAssessmentList = selfAssessmentService.receiveSubmittedSelfAssessmentDataByGroupId(groupId);
            } else {
                selfAssessmentList = selfAssessmentService.receiveSelfAssessmentByGroupId(groupId);
            }
        }

        Map<String, Integer> detailIndexMap = IaisCommonUtils.genNewHashMap();
        int index = 0;
        for (SelfAssessment s : selfAssessmentList) {
            detailIndexMap.put(s.getCorrId(), index++);
        }

        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_MAP, (Serializable) detailIndexMap);
        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR, (Serializable) selfAssessmentList);
    }

    public void setAnswerWithQuestion(HttpServletRequest request, List<PremCheckItem> tab) {
        for (PremCheckItem item : tab) {
            String answer = ParamUtil.getString(request, item.getAnswerKey());
            if (answer != null) {
                item.setAnswer(answer);
            }
        }
    }

    private void loadSelfAssessmentDetail(HttpServletRequest request) {
        int index = detailRange(request).intValue();
        List<SelfAssessment> selfAssessmentList = (List<SelfAssessment>) ParamUtil.getSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR);
        if (index != -1) {
            ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR, selfAssessmentList.get(index));
            String tabIndex = selfAssessmentList.get(index).getSelfAssessmentConfig().get(0).getConfigId();
            ParamUtil.setRequestAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION, tabIndex);
            ParamUtil.setRequestAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_LAST_TAB_INDEX_POSTION, tabIndex);
        }
    }

    /**
     * @param bpc
     * @Decription switchNextStep
     */
    public void switchNextStep(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String prevTabIndex = ParamUtil.getMaskedString(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_LAST_TAB_INDEX_POSTION);
        String tabIndex = ParamUtil.getMaskedString(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION);
        SelfAssessment selfAssessmentDetail = (SelfAssessment) ParamUtil.getSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR);

        List<PremCheckItem> lastTabQuestion = getTabQuestionByServiceId(selfAssessmentDetail, prevTabIndex);
        setAnswerWithQuestion(request, lastTabQuestion);

        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR, selfAssessmentDetail);
        ParamUtil.setRequestAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION, tabIndex);
        ParamUtil.setRequestAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_LAST_TAB_INDEX_POSTION, tabIndex);
    }

    /**
     * @param bpc
     * @Decription switchAction
     */
    public void switchAction(BaseProcessClass bpc) {

    }

    /**
     * @param bpc
     * @Decription viewSelfAssessment
     */
    public void viewSelfAssessment(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_CAN_EDIT_ANSWER_FLAG, "true");
        loadSelfAssessmentDetail(bpc.request);
    }

    /**
     * @param bpc
     * @Decription submitAllSelfAssessment
     */
    public void submitAllSelfAssessment(BaseProcessClass bpc) {
        List<SelfAssessment> selfAssessmentList = (List<SelfAssessment>) ParamUtil.getSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR);

        if (selfAssessmentList != null) {
            HashMap<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            for (int i = 0; i < selfAssessmentList.size(); i++) {
                boolean fullPower = hasFillUpAnswer(selfAssessmentList.get(i)).booleanValue();
                if (!fullPower) {
                    //confirm with mingde , no error msg will be displayed
                    errorMap.put("noFillUpItemError" + i++, "X");
                }
            }

            if (!errorMap.isEmpty()) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            } else {
                boolean successSubmit = selfAssessmentService.saveAllSelfAssessment(selfAssessmentList).booleanValue();
                if (successSubmit) {
                    String action = (String) ParamUtil.getScopeAttr(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_ACTION);
                    if (SelfAssessmentConstant.SELF_ASSESSMENT_RFI_ACTION.equals(action)){
                        String corrId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_RFI_CORR_ID);
                        selfAssessmentService.changePendingSelfAssMtStatus(corrId, false);
                        ParamUtil.setRequestAttr(bpc.request, "redirectFlag", "Y");
                    }else {
                        String groupId = (String) ParamUtil.getScopeAttr(bpc.request, NewApplicationConstant.SESSION_PARAM_APPLICATION_GROUP_ID);
                        selfAssessmentService.changePendingSelfAssMtStatus(groupId, true);
                    }
                    ParamUtil.setRequestAttr(bpc.request, "ackMsg", MessageUtil.getMessageDesc("ACK025"));
                }
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }
        }
    }

    /**
     * @param bpc
     * @Decription loadChecklist
     */
    public void loadChecklist(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_CAN_EDIT_ANSWER_FLAG, null);
        loadSelfAssessmentDetail(bpc.request);
    }

    public Integer detailRange(HttpServletRequest request) {
        String selfAssessmentCorrId = ParamUtil.getMaskedString(request, SelfAssessmentConstant.SELF_ASSESSMENT_EACH_DETAIL_CORR_ID);
        Map<String, Integer> detailIndexMap = (Map<String, Integer>) ParamUtil.getSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_MAP);
        int index = detailIndexMap != null
                && detailIndexMap.containsKey(selfAssessmentCorrId)
                ? detailIndexMap.get(selfAssessmentCorrId) : -1;
        return index;
    }

    /**
     * @param bpc
     * @Decription clearAnswer
     */
    public void clearAnswer(BaseProcessClass bpc) {
        SelfAssessment selfAssessmentDetail = (SelfAssessment) ParamUtil.getSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR);
        String tabIndex = ParamUtil.getMaskedString(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION);

        List<PremCheckItem> currentTabQuestion = getTabQuestionByServiceId(selfAssessmentDetail, tabIndex);
        for (PremCheckItem item : currentTabQuestion) {
            item.setAnswer(null);
        }

        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR, selfAssessmentDetail);
        ParamUtil.setRequestAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION, tabIndex);
        ParamUtil.setRequestAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_LAST_TAB_INDEX_POSTION, tabIndex);
    }

    private Boolean hasFillUpAnswer(SelfAssessment selfAssessmentDetail) {
        boolean fullPower = true;
        List<SelfAssessmentConfig> selfAssessmentConfig = selfAssessmentDetail.getSelfAssessmentConfig();

        for (SelfAssessmentConfig s : selfAssessmentConfig) {
            List<PremCheckItem> premCheckItemList = s.getQuestion();
            for (PremCheckItem item : premCheckItemList) {
                if (StringUtils.isEmpty(item.getAnswer())) {
                    fullPower = false;
                }
            }
        }
        return fullPower;
    }

    /**
     * @param bpc
     * @Decription draftItem
     */
    public void draftItem(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String tabIndex = ParamUtil.getMaskedString(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION);
        ParamUtil.setRequestAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION, tabIndex);
        ParamUtil.setRequestAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_LAST_TAB_INDEX_POSTION, tabIndex);

        SelfAssessment selfAssessmentDetail = (SelfAssessment) ParamUtil.getSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR);

        if (selfAssessmentDetail != null) {
            List<PremCheckItem> lastTabQuestion = getTabQuestionByServiceId(selfAssessmentDetail, tabIndex);
            setAnswerWithQuestion(request, lastTabQuestion);

            boolean fullPower = hasFillUpAnswer(selfAssessmentDetail).booleanValue();
            if (!fullPower) {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("noFillUpItemError", "SELF_CHECKLIST_ERROR0001"));
            } else {
                selfAssessmentDetail.setCanEdit(false);

                List<SelfAssessment> selfAssessmentList = (List<SelfAssessment>) ParamUtil.getSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR);
                int index = detailRange(request).intValue();
                if (index != -1) {
                    selfAssessmentList.set(index, selfAssessmentDetail);
                }

                ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR, (Serializable) selfAssessmentList);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }
        }
    }

    private List<PremCheckItem> getTabQuestionByServiceId(SelfAssessment selfAssessmentDetail, String tabIndex) {
        List<PremCheckItem> list = IaisCommonUtils.genNewArrayList();
        if (selfAssessmentDetail != null && selfAssessmentDetail.getSelfAssessmentConfig() != null) {
            List<SelfAssessmentConfig> selfAssessmentConfigList = selfAssessmentDetail.getSelfAssessmentConfig();

            selfAssessmentConfigList = selfAssessmentConfigList.stream().filter(i -> i.getConfigId().equals(tabIndex)).collect(Collectors.toList());

            //uncheck
            list = selfAssessmentConfigList.get(0).getQuestion();
        }

        return list;
    }


}
