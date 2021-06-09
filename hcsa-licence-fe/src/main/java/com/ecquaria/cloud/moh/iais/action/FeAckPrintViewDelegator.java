package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
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
        String action = ParamUtil.getString(bpc.request, "action");
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO);


        StringBuilder smallTitle = new StringBuilder();
        String title = null;
        if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
            smallTitle.append("You are applying for ");
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
            title = "New Licence Application";
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
            title = "Amendment";
        } else if(!StringUtil.isEmpty(menuRfc) && ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            bpc.request.setAttribute("menuRfc",menuRfc);
            ParamUtil.setSessionAttr(bpc.request, "createDate", new Date());
        } else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){

        } else if("retrigger".equals(action)){
            title = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationConstant.ACK_TITLE);
            smallTitle = (StringBuilder) ParamUtil.getSessionAttr(bpc.request, NewApplicationConstant.ACK_SMALL_TITLE);
        }
        ParamUtil.setRequestAttr(bpc.request,NewApplicationConstant.ACK_TITLE, title);
        ParamUtil.setRequestAttr(bpc.request, NewApplicationConstant.ACK_SMALL_TITLE, smallTitle.toString());

        log.debug(StringUtil.changeForLog("ack print view doStart end ..."));
    }

    public void prepareData(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("ack print view prepare data start ..."));

        log.debug(StringUtil.changeForLog("ack print view prepare data end ..."));
    }
}
