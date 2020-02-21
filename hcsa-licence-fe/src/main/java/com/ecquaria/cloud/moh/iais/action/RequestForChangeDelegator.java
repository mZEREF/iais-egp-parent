package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
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

/****
 *
 *   @date 1/5/2020
 *   @author zixian
 */
@Slf4j
@Delegator("requestForChangeDelegator")
public class RequestForChangeDelegator {

    @Autowired
    RequestForChangeService requestForChangeService;

    /**
     *
     * @param bpc
     * @Decription doStart
     */
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doStart start ...."));
        String licenceId = ParamUtil.getString(bpc.request, "licenceId");
        ParamUtil.setSessionAttr(bpc.request, RfcConst.LICENCEID, licenceId);

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
        String licenceId = (String) ParamUtil.getSessionAttr(bpc.request, RfcConst.LICENCEID);
        LicenceDto licenceDto = requestForChangeService.getLicenceDtoByLicenceId(licenceId);
        String UNID=ParamUtil.getString(bpc.request, "UNID");

        if(licenceDto != null && UNID==null) {
            String status = licenceDto.getStatus();
            if (!ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(status)) {
                ParamUtil.setRequestAttr(bpc.request, "ErrorMsg", "licence status is not active");
                flag = false;
            }
        }
        if(flag){
            if(AppConsts.NO.equals(amendType)){
                //....
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "doTranfer");
                flag = true;
            }else if(AppConsts.YES.equals(amendType)){
                String [] amendLicenceType = ParamUtil.getStrings(bpc.request, "amend-licence-type");
                if(amendLicenceType != null && amendLicenceType.length > 0){
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "doAmend");
                }else{
                    flag = false;
                }
            }
        }
        if(!flag){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prepare");
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
        /*//String licenceId = "B99F41F3-5D1E-EA11-BE7D-000C29F371DC";
         String licenceId = (String) ParamUtil.getSessionAttr(bpc.request, RfcConst.LICENCEID);
        LicenceDto licenceDto = new LicenceDto();
        licenceDto.setId(licenceId);
        licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_REQUEST_FOR_CHANGE);
        //update lic status
        //requestForChangeService.upDateLicStatus(licenceDto);*/
        
        log.debug(StringUtil.changeForLog("the do prepareAmend end ...."));
    }

    /**
     * @param bpc
     * @Decription compareChangePercentage
     */
    public void compareChangePercentage(BaseProcessClass bpc) {
        String pageType =ParamUtil.getString(bpc.request, "keyType");
        String licenseNo=ParamUtil.getString(bpc.request, "licenceNo");
        String licenceId= (String) ParamUtil.getSessionAttr(bpc.request, RfcConst.LICENCEID);
        //String licenseNo="L/20CLB0156/CLB/001/201";
        LicenceDto licenceDto=requestForChangeService.getLicenceDtoByLicenceId(licenceId);
        String UNID=ParamUtil.getString(bpc.request, "UNID");
        String newLicenseeId=null;
        List<String> uenMemberIds=new ArrayList<>();
        if(("UEN").equals(pageType)){
            List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDtoList=requestForChangeService.getLicenseeKeyApptPersonDtoListByUen(UNID);
            if(licenseeKeyApptPersonDtoList!=null&&licenseeKeyApptPersonDtoList.size()>0){
                for (LicenseeKeyApptPersonDto e:licenseeKeyApptPersonDtoList
                        ) {
                    uenMemberIds.add(e.getId());
                    newLicenseeId=e.getLicenseeId();

                }
            }
            //uen

            List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDtoListFromLicenseeId=requestForChangeService.getLicenseeKeyApptPersonDtoListByLicenseeId(licenceDto.getLicenseeId());
            List<String> oldMemberIds=new ArrayList<>();
            if(licenseeKeyApptPersonDtoListFromLicenseeId!=null&&licenseeKeyApptPersonDtoListFromLicenseeId.size()>0) {
                for (LicenseeKeyApptPersonDto e:licenseeKeyApptPersonDtoListFromLicenseeId
                ) {
                    oldMemberIds.add(e.getId());
                }
            }
            int count = 0;
            for(int i=0;i<=oldMemberIds.size();i++){
                for(int j=0;j<=uenMemberIds.size();j++){
                    if(oldMemberIds.get(i).equals(uenMemberIds.get(j))){
                        count++;
                    }
                }
            }
            boolean result=false;
            if(count/oldMemberIds.size()<0.5){
                result=true;
            }
            if(result){
                licenceDto.setLicenseeId(newLicenseeId);
                requestForChangeService.saveLicence(licenceDto);
            }
        }else {
            String nric=UNID;
            LicenseeIndividualDto licenseeIndividualDt=requestForChangeService.getLicIndByNRIC(nric);
            licenceDto.setLicenseeId(licenseeIndividualDt.getId());
            requestForChangeService.saveLicence(licenceDto);
        }

    }
}
