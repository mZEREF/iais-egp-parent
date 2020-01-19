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
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremSelfDeclService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Delegator(value = "appPremSelfDeclDelegator")
@Slf4j
public class AppPremSelfDeclDelegator {

    private static final String INSPECTION_START_PERIOD = "inspStartDate";
    private static final String INSPECTION_END_PERIOD = "inspEndDate";

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
        ParamUtil.setSessionAttr(request, INSPECTION_START_PERIOD, null);
        ParamUtil.setSessionAttr(request, INSPECTION_END_PERIOD, null);

    }

    /**
     * AutoStep: initData
     *
     * @param bpc
     * @throws
     */
    public void initData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        String groupId = (String) ParamUtil.getScopeAttr(request, "groupId");
        ParamUtil.setSessionAttr(request, "currentSelfDeclGroupId", groupId);

        log.info("assign to self decl group id ==>>>>> " + groupId);

        if (groupId == null){
            return;
        }

        List<SelfDecl> selfDeclList = (List<SelfDecl>) ParamUtil.getSessionAttr(request, "selfDeclQueryAttr");
        if (selfDeclList == null){
            List<SelfDecl> selfDeclByGroupId = appPremSelfDesc.getSelfDeclByGroupId(groupId);
            ParamUtil.setSessionAttr(request, "selfDeclQueryAttr", (Serializable) selfDeclByGroupId);

            Date date = appPremSelfDesc.getBlockPeriodByAfterApp(groupId, selfDeclByGroupId);
            ParamUtil.setSessionAttr(request, INSPECTION_START_PERIOD, IaisEGPHelper.parseToString(date, "dd/MM/yyyy"));
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

        String inspStartDate = ParamUtil.getString(request, "inspStartDate");
        String inspEndDate = ParamUtil.getString(request, "inspEndDate");

        ParamUtil.setSessionAttr(request, INSPECTION_START_PERIOD, inspStartDate);
        ParamUtil.setSessionAttr(request, INSPECTION_END_PERIOD, inspEndDate);

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

        //Once transaction

        String inspStartDate = ParamUtil.getString(request, "inspStartDate");
        String inspEndDate = ParamUtil.getString(request, "inspEndDate");

        Map<String, String> errorMap = new HashMap<>(4);
        if (StringUtils.isEmpty(inspStartDate)){
            errorMap.put("inspStartDate", "CHKL_ERR001");
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            return;
        }

        if (StringUtils.isEmpty(inspEndDate)){
            errorMap.put("inspEndDate", "CHKL_ERR001");
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            return;
        }

        boolean hasWriteAnswer = hasWtriteAnswer(selfDeclList).booleanValue();
        if (!hasWriteAnswer){
            errorMap.put("premItemAnswer", "Please fill in the necessary answers.");
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            return;
        }

        Date startDate = IaisEGPHelper.parseToDate(inspStartDate, "dd/MM/yyyy");
        Date endDate = IaisEGPHelper.parseToDate(inspEndDate, "dd/MM/yyyy");

        if (endDate.compareTo(startDate) < 0){
            errorMap.put("inspectionDateErr", "CHKL_ERR002");
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            return;
        }

        if (errorMap != null && errorMap.isEmpty()){
            String groupId = (String) ParamUtil.getSessionAttr(request, "currentSelfDeclGroupId");
            appPremSelfDesc.saveSelfDeclAndInspectionDate(selfDeclList, groupId, startDate, endDate);
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
