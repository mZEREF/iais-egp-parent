package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesUpdateEmailDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * EmailViewDelegator
 *
 * @author junyu
 * @date 2022/7/21
 */
@Delegator("emailViewDelegator")
@Slf4j
public class EmailViewDelegator {
    public void start(BaseProcessClass bpc){


    }

    public void perInfo(BaseProcessClass bpc){
        AppPremisesUpdateEmailDto emailDto= (AppPremisesUpdateEmailDto) ParamUtil.getSessionAttr(bpc.request,"appPremisesUpdateEmailDto");
        String subject = ParamUtil.getString(bpc.request, "subject");
        String mailContent = ParamUtil.getString(bpc.request, "mailContent");
        emailDto.setSubject(subject);
        emailDto.setMailContent(mailContent);

        ParamUtil.setSessionAttr(bpc.request,"appPremisesUpdateEmailDto",emailDto);

    }
}
