package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessmentConfig;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.SelfAssessmentConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
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
     *
     * @param bpc
     * @Decription startStep
     */
    public void startStep(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("Hcsa Application", "Self desc");

        ParamUtil.setSessionAttr(bpc.request, "selfAssessmentMap", null);
        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request,SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION, null);
    }

    /**
     *
     * @param bpc
     * @Decription preLoad
     */
    public void preLoad(BaseProcessClass bpc) {
        String groupId = "99E1370B-CC90-EA11-BE82-000C29F371DC";
        String action = "rfi";

        if (StringUtils.isEmpty(groupId)){
            log.info("can not find group id");
            return;
        }

        List<SelfAssessment> selfAssessmentList = (List<SelfAssessment>) ParamUtil.getSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR);
        log.info(StringUtil.changeForLog("assign to self decl group id ==>>>>> " + groupId));

        if(IaisCommonUtils.isEmpty(selfAssessmentList)){
            }else {
                if ("rfi".equals(action)){
                    selfAssessmentList = selfAssessmentService.receiveSelfAssessmentRfiByCorrId("D0490B54-2C90-EA11-BE7A-000C29D29DB0");
                }else {
                    boolean hasSubmitted = selfAssessmentService.hasSubmittedSelfAssessment("43FAE68F-2990-EA11-BE7A-000C29D29DB0");
                    if (hasSubmitted) {
                        ParamUtil.setSessionAttr(bpc.request, "hasSubmitted", "Y");
                        ParamUtil.setSessionAttr(bpc.request, "hasSubmittedMsg", "You have submitted self decl. Please do not submit again");
                        selfAssessmentList = selfAssessmentService.receiveSubmittedSelfAssessmentDataByGroupId("43FAE68F-2990-EA11-BE7A-000C29D29DB0");
                    }else {
                        selfAssessmentList = selfAssessmentService.receiveSelfAssessmentByGroupId("43FAE68F-2990-EA11-BE7A-000C29D29DB0");
                    }
                }

        }

        Map<String, Integer> detailIndexMap = IaisCommonUtils.genNewHashMap();
        int index = 0;
        for (SelfAssessment s : selfAssessmentList){
            detailIndexMap.put(s.getCorrId(), index++);
        }

        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_MAP, (Serializable) detailIndexMap);
        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR, (Serializable) selfAssessmentList);
    }

    public void setAnswerWithQuestion(HttpServletRequest request, List<PremCheckItem> tab){
        for (PremCheckItem item : tab){
            String answer = ParamUtil.getString(request, item.getAnswerKey());
            if (answer != null){
                item.setAnswer(answer);
            }
        }
    }

    private void loadSelfAssessmentDetail(HttpServletRequest request){
        int index = getDetailIndexInSessionList(request).intValue();
        if (index > -1){
            List<SelfAssessment> selfAssessmentList = (List<SelfAssessment>) ParamUtil.getSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR);
            ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR, selfAssessmentList.get(index));
            String tabIndex = selfAssessmentList.get(index).getSelfAssessmentConfig().get(0).getConfigId();
            ParamUtil.setRequestAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION, tabIndex);
            ParamUtil.setRequestAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_LAST_TAB_INDEX_POSTION, tabIndex);
        }
    }

    /**
     *
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
        ParamUtil.setRequestAttr(bpc.request,SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION, tabIndex);
        ParamUtil.setRequestAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_LAST_TAB_INDEX_POSTION, tabIndex);
    }

    /**
     *
     * @param bpc
     * @Decription switchAction
     */
    public void switchAction(BaseProcessClass bpc) {

    }

    /**
     *
     * @param bpc
     * @Decription viewSelfAssessment
     */
    public void viewSelfAssessment(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_CAN_EDIT_ANSWER_FLAG, "true");
        loadSelfAssessmentDetail(bpc.request);
    }

    /**
     *
     * @param bpc
     * @Decription submitAllSelfAssessment
     */
    public void submitAllSelfAssessment(BaseProcessClass bpc) {
        List<SelfAssessment> selfAssessmentList = (List<SelfAssessment>) ParamUtil.getSessionAttr(bpc.request, "selfAssessmentQueryAttr");

        if (selfAssessmentList != null){
            HashMap<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            for (int i = 0; i < selfAssessmentList.size(); i++){
                boolean fullPower = hasFillUpAnswer(selfAssessmentList.get(i)).booleanValue();
                if (!fullPower){
                    errorMap.put("noFillUpItemError" + i++, "X");
                }
            }

            if (!errorMap.isEmpty()){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            }else {
                boolean successSubmit = selfAssessmentService.saveAllSelfAssessment(selfAssessmentList).booleanValue();
                if (successSubmit){
                    String groupId = "43FAE68F-2990-EA11-BE7A-000C29D29DB0";
                    selfAssessmentService.changePendingSelfAssMtStatus(groupId);
                }
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }
        }
    }

    /**
     *
     * @param bpc
     * @Decription loadChecklist
     */
    public void loadChecklist(BaseProcessClass bpc) {
        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_CAN_EDIT_ANSWER_FLAG, null);
        loadSelfAssessmentDetail(bpc.request);
    }



    public Integer getDetailIndexInSessionList(HttpServletRequest request){
        String selfAssessmentCorrId = ParamUtil.getMaskedString(request, SelfAssessmentConstant.SELF_ASSESSMENT_CORR_ID);
        Map<String, Integer> detailIndexMap = (Map<String, Integer>) ParamUtil.getSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_MAP);
        int index = detailIndexMap != null && detailIndexMap.containsKey(selfAssessmentCorrId) ? detailIndexMap.get(selfAssessmentCorrId) : -1;
        return index;
    }

    /**
     *
     * @param bpc
     * @Decription clearAnswer
     */
    public void clearAnswer(BaseProcessClass bpc) {
        SelfAssessment selfAssessmentDetail = (SelfAssessment) ParamUtil.getSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR);
        String tabIndex = ParamUtil.getMaskedString(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION);

        List<PremCheckItem> currentTabQuestion = getTabQuestionByServiceId(selfAssessmentDetail, tabIndex);
        for (PremCheckItem item : currentTabQuestion){
            item.setAnswer(null);
        }

        ParamUtil.setSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR, selfAssessmentDetail);
        ParamUtil.setRequestAttr(bpc.request,SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION, tabIndex);
        ParamUtil.setRequestAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_LAST_TAB_INDEX_POSTION, tabIndex);
    }

    private Boolean hasFillUpAnswer(SelfAssessment selfAssessmentDetail){
        boolean fullPower = true;
        List<SelfAssessmentConfig> selfAssessmentConfig = selfAssessmentDetail.getSelfAssessmentConfig();

        for (SelfAssessmentConfig s : selfAssessmentConfig){
            List<PremCheckItem> premCheckItemList = s.getQuestion();
            for (PremCheckItem item : premCheckItemList){
                if (StringUtils.isEmpty(item.getAnswer())){
                    fullPower = false;
                }
            }
        }
        return fullPower;
    }

    /**
     *
     * @param bpc
     * @Decription draftItem
     */
    public void draftItem(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String tabIndex = ParamUtil.getMaskedString(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION);
        ParamUtil.setRequestAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_TAB_INDEX_POSTION, tabIndex);
        ParamUtil.setRequestAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_LAST_TAB_INDEX_POSTION, tabIndex);

        SelfAssessment selfAssessmentDetail = (SelfAssessment) ParamUtil.getSessionAttr(bpc.request, SelfAssessmentConstant.SELF_ASSESSMENT_DETAIL_ATTR);

        if (selfAssessmentDetail != null){
            List<PremCheckItem> lastTabQuestion = getTabQuestionByServiceId(selfAssessmentDetail, tabIndex);
            setAnswerWithQuestion(request, lastTabQuestion);

            boolean fullPower = hasFillUpAnswer(selfAssessmentDetail).booleanValue();
            if (!fullPower){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("noFillUpItemError", "SELF_CHECKLIST_ERROR0001"));
            }else {
                selfAssessmentDetail.setCanEdit(false);

                List<SelfAssessment> selfAssessmentList = (List<SelfAssessment>) ParamUtil.getSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR);
                int index = getDetailIndexInSessionList(request).intValue();
                if (index > -1){
                    selfAssessmentList.set(index, selfAssessmentDetail);
                }

                ParamUtil.setSessionAttr(request, SelfAssessmentConstant.SELF_ASSESSMENT_QUERY_ATTR, (Serializable) selfAssessmentList);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }
        }
    }

    private List<PremCheckItem> getTabQuestionByServiceId(SelfAssessment selfAssessmentDetail, String tabIndex){
        List<PremCheckItem> list = IaisCommonUtils.genNewArrayList();
        if (selfAssessmentDetail != null && selfAssessmentDetail.getSelfAssessmentConfig() != null) {
            List<SelfAssessmentConfig> selfAssessmentConfigList = selfAssessmentDetail.getSelfAssessmentConfig();


            selfAssessmentConfigList = selfAssessmentConfigList.stream().filter(i -> i.getConfigId().equals(tabIndex)).collect(Collectors.toList());
           /* if (StringUtils.isEmpty(tabIndex)){

            }else {
                selfAssessmentConfigList = selfAssessmentConfigList.stream().filter(i -> i.getConfigId() != null &&
                        i.getConfigId().equals(tabIndex) && !i.isCommon()).collect(Collectors.toList());
            }*/

            list = selfAssessmentConfigList.get(0).getQuestion();
        }

        return list;
    }


}
