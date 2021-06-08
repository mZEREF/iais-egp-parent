package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * @author zixian
 * @date 2021/6/7 15:17
 * @description
 */
@Delegator("feAckPrintViewDelegator")
@Slf4j
public class FeAckPrintViewDelegator {

    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("ack print view doStart start ..."));
        String appType = ParamUtil.getString(bpc.request, "appType");
        String menuRfc = ParamUtil.getString(bpc.request, "menuRfc");
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO);


        StringBuilder smallTitle = new StringBuilder();
        if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
            smallTitle.append("<h3 id=\"newSvc\">You are applying for ");
            if(!IaisCommonUtils.isEmpty(hcsaServiceDtoList)){
                int count = 0;
                for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtoList){
                    smallTitle.append("<strong>")
                            .append(hcsaServiceDto.getSvcName())
                            .append("</strong>");
                    if(count != hcsaServiceDtoList.size()-1){
                        smallTitle.append(" | ");
                    }
                    count ++;
                }
            }
            smallTitle.append("</h3>");
            ParamUtil.setRequestAttr(bpc.request,"title", "New Licence Application");
            ParamUtil.setRequestAttr(bpc.request, "smallTitle", smallTitle);
        } else if(StringUtil.isEmpty(menuRfc) && ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            smallTitle.append("You are amending the ");
            if(hcsaServiceDtoList != null && hcsaServiceDtoList.size() > 0){
                smallTitle.append("<strong>")
                        .append(hcsaServiceDtoList.get(0).getSvcName())
                        .append(" licence (Licence No. ")
                        .append(appSubmissionDto.getLicenceNo())
                        .append("</strong>)");
            }
            smallTitle.append("</p>");

            ParamUtil.setRequestAttr(bpc.request,"title", "Amendment");
            ParamUtil.setRequestAttr(bpc.request, "smallTitle", smallTitle);
        } else if(!StringUtil.isEmpty(menuRfc) && ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){


        } else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){

        }
        log.debug(StringUtil.changeForLog("ack print view doStart end ..."));
    }

    public void prepareData(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("ack print view prepare data start ..."));

        log.debug(StringUtil.changeForLog("ack print view prepare data end ..."));
    }
}
