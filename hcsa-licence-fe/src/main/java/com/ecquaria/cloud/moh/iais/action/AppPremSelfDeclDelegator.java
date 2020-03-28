package com.ecquaria.cloud.moh.iais.action;

/*
 *author: yichen
 *date time:11/20/2019 10:12 AM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclaration;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremSelfDeclService;
import com.ecquaria.cloud.moh.iais.service.SelfDeclRfiService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Delegator(value = "appPremSelfDeclDelegator")
@Slf4j
public class AppPremSelfDeclDelegator {
    private static final String GROUP_ID_ATTR = "groupId";
    private static final String SELF_DECL_ACTION = "selfDeclAction";

    private final AppPremSelfDeclService appPremSelfDesc;
    private final SelfDeclRfiService selfDeclRfiService;

    @Autowired
    public AppPremSelfDeclDelegator(AppPremSelfDeclService appPremSelfDesc, SelfDeclRfiService selfDeclRfiService){
        this.appPremSelfDesc = appPremSelfDesc;
        this.selfDeclRfiService = selfDeclRfiService;
    }

    /**
     * AutoStep: startStep
     *
     * @param bpc
     * @throws
     */
    public void startStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("Hcsa Application", "Self desc");

        ParamUtil.setSessionAttr(request, "selfDeclQueryAttr", null);

    }

    /**
     * AutoStep: initData
     *
     * @param bpc
     * @throws
     */
    public void initData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        /*String groupId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationConstant.SESSION_PARAM_APPLICATION_GROUP_ID);
        String action = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationConstant.SESSION_SELF_DECL_ACTION);

        if (StringUtil.isEmpty(groupId) || StringUtils.isEmpty(action)){
            log.info("can not find group id");
            return;
        }*/

        String action = "rfi";
        String groupId = "E83380FA-2668-EA11-BE79-000C29D29DB0";

        /*if ("new".equals(action)){
            boolean isSubmitted = appPremSelfDesc.hasSelfDeclRecord(groupId);
            if (isSubmitted){
                ParamUtil.setRequestAttr(request, "isSubmitted", "You have submitted self decl. Please do not submit again.");
                ParamUtil.setRequestAttr(request, "selfDeclQueryAttr", null);
                return;
            }
        }*/

        ParamUtil.setSessionAttr(request, "currentSelfDeclGroupId", groupId);
        List<SelfDeclaration> selfDeclList = (List<SelfDeclaration>) ParamUtil.getSessionAttr(request, "selfDeclQueryAttr");
        log.info("assign to self decl group id ==>>>>> " + groupId);

        if (selfDeclList == null) {
            if ("rfi".equals(action)){
                selfDeclList= selfDeclRfiService.getSelfDeclRfiData(groupId);
            }else if ("new".equals(action)){
                selfDeclList = appPremSelfDesc.getSelfDeclByGroupId(groupId);
            }
            ParamUtil.setSessionAttr(request, "selfDeclQueryAttr", (Serializable) selfDeclList);
        }
    }

    /**
     * AutoStep: doSave
     *
     * @param bpc
     * @throws
     */
    public void doSave(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        String currentPage = ParamUtil.getString(request, "tabIndex");

        List<SelfDeclaration> selfDeclByGroupId = (List<SelfDeclaration>) ParamUtil.getSessionAttr(request, "selfDeclQueryAttr");

        for (SelfDeclaration selfDecl : selfDeclByGroupId){
            if (currentPage == null && selfDecl.isCommon()){
                LinkedHashMap<String, List<PremCheckItem>> eachPremQuestion = selfDecl.getEachPremQuestion();
                setAnswer(request, eachPremQuestion);
            }else if (currentPage != null && currentPage.equals(selfDecl.getSvcId())){
                LinkedHashMap<String, List<PremCheckItem>> eachPremQuestion = selfDecl.getEachPremQuestion();
                setAnswer(request, eachPremQuestion);
            }

        }
        ParamUtil.setSessionAttr(request, "selfDeclQueryAttr", (Serializable) selfDeclByGroupId);
    }

    private void setAnswer(HttpServletRequest request, LinkedHashMap<String, List<PremCheckItem>> eachPremQuestion){
        for (Map.Entry<String,  List<PremCheckItem>> entry : eachPremQuestion.entrySet()){
            List<PremCheckItem> premCheckItemList = entry.getValue();
            for (PremCheckItem item : premCheckItemList){
                String answer = ParamUtil.getString(request, item.getAnswerKey());
                if (answer != null){
                    item.setAnswer(answer);
                }
                ParamUtil.setSessionAttr(request, item.getAnswerKey(), answer);
            }
        }
    }

    /**
     * AutoStep: saveCommonData
     *
     * @param bpc
     * @throws
     */
    public void saveCommonData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String tabIndex = ParamUtil.getString(request, "tabIndex");
        ParamUtil.setRequestAttr(request, "tabIndex", tabIndex);

    }

    /**
     * AutoStep: validate
     *
     * @param bpc
     * @throws
     */
    public void validate(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        String action = ParamUtil.getString(request, "crud_action_type");

        ParamUtil.setRequestAttr(bpc.request,"crud_action_type", action);
    }

    /**
     * AutoStep: submitSelfDesc
     *
     * @param bpc
     * @throws
     */
    public void submitSelfDesc(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        List<SelfDeclaration> selfDeclList = (List<SelfDeclaration>) ParamUtil.getSessionAttr(request, "selfDeclQueryAttr");

        //Once transaction
        boolean hasWriteAnswer = hasWriteAnswer(selfDeclList).booleanValue();
        if (!hasWriteAnswer){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("premItemAnswer", "Please fill in the necessary answers."));
            return;
        }

        String json = JsonUtil.parseToJson(selfDeclList);
        appPremSelfDesc.saveSelfDecl(selfDeclList);
        ParamUtil.setRequestAttr(request,"ackMsg", "You have successfully submitted self-assessment");
        ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
    }

    private Boolean hasWriteAnswer(List<SelfDeclaration> selfDeclList){
        boolean fullPower = true;
        if (selfDeclList != null){
            for (SelfDeclaration selfDecl : selfDeclList){
                LinkedHashMap<String, List<PremCheckItem>> listMap = selfDecl.getEachPremQuestion();
                for(Map.Entry<String, List<PremCheckItem>> entry : listMap.entrySet()){
                    List<PremCheckItem> premCheckItemList = entry.getValue();
                    for (PremCheckItem item : premCheckItemList){
                        if (StringUtils.isEmpty(item.getAnswer())){
                            fullPower = false;
                        }
                    }
                }
            }
        }

        return fullPower;
    }

    /**
     * AutoStep: switchNextStep
     *
     * @param bpc
     * @throws
     */
    public void switchNextStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        String action = ParamUtil.getString(request, "crud_action_type");

        ParamUtil.setRequestAttr(bpc.request,"crud_action_type", action);
    }
}
