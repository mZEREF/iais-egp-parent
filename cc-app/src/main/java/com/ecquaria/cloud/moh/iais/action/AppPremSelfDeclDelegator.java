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
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremSelfDeclService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
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

    private void loadTabOption(HttpServletRequest request){

    }



    private void loadSearchResult(HttpServletRequest request, String configId){

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

        String tabIndex = ParamUtil.getString(request, "tabIndex");
        ParamUtil.setRequestAttr(request, "tabIndex", tabIndex);
    }

    /**
     * AutoStep: initCommonData
     *
     * @param bpc
     * @throws
     */
    public void initCommonData(BaseProcessClass bpc){
    }

    /**
     * AutoStep: initServiceData
     *
     * @param bpc
     * @throws
     */
    public void initServiceData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
    }


    /**
     * AutoStep: saveServiceData
     *
     * @param bpc
     * @throws
     */
    public void saveServiceData(BaseProcessClass bpc){

    }

    /**
     * AutoStep: saveCommonData
     *
     * @param bpc
     * @throws
     */
    public void saveCommonData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        List<SelfDecl> selfDeclByGroupId = (List<SelfDecl>) ParamUtil.getSessionAttr(request, "selfDeclQueryAttr");

        for (SelfDecl selfDecl : selfDeclByGroupId){
            Map<String, List<PremCheckItem>> premAnswerMap = selfDecl.getPremAnswerMap();
            for (Map.Entry<String,  List<PremCheckItem>> entry : premAnswerMap.entrySet()){
                List<PremCheckItem> premCheckItemList = entry.getValue();
                for (PremCheckItem item : premCheckItemList){
                    String answer = ParamUtil.getString(request, item.getAnswerKey());
                    item.setAnswer(answer);
                    ParamUtil.setSessionAttr(request, item.getAnswerKey(), answer);
                }
            }
        }
        ParamUtil.setSessionAttr(request, "selfDeclQueryAttr", (Serializable) selfDeclByGroupId);
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

        appPremSelfDesc.saveSelfDecl(selfDeclList);

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
