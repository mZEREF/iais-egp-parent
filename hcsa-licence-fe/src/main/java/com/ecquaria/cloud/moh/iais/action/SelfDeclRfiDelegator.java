package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.SelfDeclRfiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: yichen
 * @description:
 * @date:2020/3/24
 **/

@Delegator(value = "selfDeclRfiDelegator")
@Slf4j
public class SelfDeclRfiDelegator {

    @Autowired
    private AppSubmissionService appSubmissionService;

    @Autowired
    private SelfDeclRfiService selfDeclRfiService;

    /**
     * AutoStep: startStep
     *
     * @param bpc
     * @throws
     */
    public void startStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("Self decl Rfi", "Self decl Rfi");
    }


    /**
     * AutoStep: preLoad
     *
     * @param bpc
     * @throws
     */
    public void preLoad(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        /*String groupId = (String) ParamUtil.getSessionAttr(request, "appGroupId");

        if(groupId == null){
            log.info("self decl rfi can not find group id");
            return;
        }*/

        String groupId = "2EF13B4E-626E-EA11-BE7A-000C29D29DB0";


        selfDeclRfiService.getSelfDeclRfiData(groupId);


    }


    /**
     * AutoStep: switchAction
     *
     * @param bpc
     * @throws
     */
    public void switchAction(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;



    }



}
