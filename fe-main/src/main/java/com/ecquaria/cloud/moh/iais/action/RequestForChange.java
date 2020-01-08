package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;

/****
 *
 *   @date 1/5/2020
 *   @author zixian
 */
@Slf4j
@Delegator("requestForChangeDelegator")
public class RequestForChange {

    @Autowired
    RequestForChangeService requestForChangeService;

    /**
     *
     * @param bpc
     * @Decription doStart
     */
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doStart start ...."));

        log.debug(StringUtil.changeForLog("the do doStart start ...."));
    }

    /**
     *
     * @param bpc
     * @Decription prepare
     */
    public void prepare(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepare start ...."));
        List<SelectOption> amendType = new ArrayList<>();
        SelectOption sp1 = new SelectOption(ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_PREMISES_INFORMATION,
                RfcConst.REQUEST_FOR_CHANGE_TYPE_PREMISES_INFORMATION_TEXT);
        SelectOption sp2 = new SelectOption(ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_MEDALERT_PERSONNEL,
                RfcConst.REQUEST_FOR_CHANGE_TYPE_MEDALERT_PERSONNEL_TEXT);
        SelectOption sp3 = new SelectOption(ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_PRINCIPAL_OFFICER,
                RfcConst.REQUEST_FOR_CHANGE_TYPE_PRINCIPAL_OFFICER_TEXT);
        SelectOption sp4 = new SelectOption(ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_DEPUTY_PRINCIPAL_OFFICER,
                RfcConst.REQUEST_FOR_CHANGE_TYPE_DEPUTY_PRINCIPAL_OFFICER_TEXT);
        SelectOption sp5 = new SelectOption(ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_RELATED_INFORMATION,
                RfcConst.REQUEST_FOR_CHANGE_TYPE_SERVICE_RELATED_INFORMATION_TEXT);
        SelectOption sp6 = new SelectOption(ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SUPPORTING_DOCUMENT,
                RfcConst.REQUEST_FOR_CHANGE_TYPE_SUPPORTING_DOCUMENT_TEXT);
        amendType.add(sp1);
        amendType.add(sp2);
        amendType.add(sp3);
        amendType.add(sp4);
        amendType.add(sp5);
        amendType.add(sp6);
        ParamUtil.setRequestAttr(bpc.request, "AmendTypeList", amendType);

        log.debug(StringUtil.changeForLog("the do prepare end ...."));
    }


    /**
     *
     * @param bpc
     * @Decription doChoose
     */
    public void doChoose(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do start doChoose ...."));
        String amendType = ParamUtil.getString(bpc.request, "amendType");
        boolean flag = true;
        if(AppConsts.NO.equals(amendType)){
            //....
            flag = false;
        }else if(AppConsts.YES.equals(amendType)){
            String [] amendLicenceType = ParamUtil.getStrings(bpc.request, "amend-licence-type");
            if(amendLicenceType != null && amendLicenceType.length > 0){
                StringJoiner joiner =new StringJoiner(";");
                for(String type:amendLicenceType){
                    joiner.add(type);
                }
                ParamUtil.setSessionAttr(bpc.request, "amendLicenceType", joiner.toString());
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "doAmend");
            }else{
                flag = false;
            }
        }
        if(!flag){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prepare");
            ParamUtil.setRequestAttr(bpc.request, "ErrorMsg", "error 1!!");
            ParamUtil.setRequestAttr(bpc.request, "AmendType", amendType);
        }

        log.debug(StringUtil.changeForLog("the do doChoose end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription jump
     */
    public void jump(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do jump start ...."));
        String crudActionTypeForm = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(StringUtil.isEmpty(crudActionTypeForm)){
            crudActionTypeForm = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM);
        }


        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, crudActionTypeForm);
        log.debug(StringUtil.changeForLog("the do jump end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription prepareAmend
     */
    public void prepareAmend(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do prepareAmend start ...."));
        String amendTypeStr = (String) ParamUtil.getSessionAttr(bpc.request, "amendLicenceType");
        //todo: 
        String licenceId = "B99F41F3-5D1E-EA11-BE7D-000C29F371DC";
        

        StringBuffer url = new StringBuffer();
        url.append("https://").append(bpc.request.getServerName())
                .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication")
                .append(" ?licenceId=")
                .append(licenceId)
                .append("&amendTypes=")
                .append(amendTypeStr);
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        bpc.response.sendRedirect(url.toString());
        log.debug(StringUtil.changeForLog("the do prepareAmend end ...."));
    }




}
