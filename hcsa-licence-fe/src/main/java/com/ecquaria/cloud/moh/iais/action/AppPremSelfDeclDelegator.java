package com.ecquaria.cloud.moh.iais.action;

/*
 *author: yichen
 *date time:11/20/2019 10:12 AM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremSelfDeclService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Delegator(value = "appPremSelfDeclDelegator")
@Slf4j
public class AppPremSelfDeclDelegator {

    private final AppPremSelfDeclService appPremSelfDesc;

    @Autowired
    public AppPremSelfDeclDelegator(AppPremSelfDeclService appPremSelfDesc){
        this.appPremSelfDesc = appPremSelfDesc;
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

        List<SelfDecl> selfDeclList = (List<SelfDecl>) ParamUtil.getSessionAttr(request, "selfDeclQueryAttr");
        if (selfDeclList == null){
            List<SelfDecl> selfDeclByGroupId = appPremSelfDesc.getSelfDeclByGroupId("1C629C17-CB72-4892-8F31-87F6759C791A");
            ParamUtil.setSessionAttr(request, "selfDeclQueryAttr", (Serializable) selfDeclByGroupId);
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

        List<SelfDecl> selfDeclByGroupId = (List<SelfDecl>) ParamUtil.getSessionAttr(request, "selfDeclQueryAttr");

        for (SelfDecl selfDecl : selfDeclByGroupId){
            if (currentPage == null && selfDecl.isCommon()){
                Map<String, List<PremCheckItem>> premAnswerMap = selfDecl.getPremAnswerMap();
                 setAnswer(request, premAnswerMap);
            }else if (currentPage != null && currentPage.equals(selfDecl.getSvcId())){
                Map<String, List<PremCheckItem>> premAnswerMap = selfDecl.getPremAnswerMap();
                setAnswer(request, premAnswerMap);
            }

        }
        ParamUtil.setSessionAttr(request, "selfDeclQueryAttr", (Serializable) selfDeclByGroupId);
    }

    private void setAnswer(HttpServletRequest request, Map<String, List<PremCheckItem>> premAnswerMap){
        for (Map.Entry<String,  List<PremCheckItem>> entry : premAnswerMap.entrySet()){
            List<PremCheckItem> premCheckItemList = entry.getValue();
            for (PremCheckItem item : premCheckItemList){
                String answer = ParamUtil.getString(request, item.getAnswerKey());
                item.setAnswer(answer);
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
        List<SelfDecl> selfDeclList = (List<SelfDecl>) ParamUtil.getSessionAttr(request, "selfDeclQueryAttr");

        boolean hasWriteAnswer = hasWtriteAnswer(selfDeclList).booleanValue();
        if (!hasWriteAnswer){
            Map<String, String> errorMap = new HashMap<>(1);
            errorMap.put("premItemAnswer", "Please fill in the necessary answers.");
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        }else {
            appPremSelfDesc.saveSelfDecl(selfDeclList);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
    }

    // Lamda is not recommended
    private Boolean hasWtriteAnswer(List<SelfDecl> selfDeclList){
        boolean fullPower = true;
        if (selfDeclList != null){
            for (SelfDecl selfDecl : selfDeclList){
                Map<String, List<PremCheckItem>> listMap = selfDecl.getPremAnswerMap();
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
