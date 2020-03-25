package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private AppSubmissionService appSubmissionService;

    @Autowired
    private ServiceConfigService serviceConfigService;

    /**
     *
     * @param bpc
     * @Decription doStart
     */
    public void doStart(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do doStart start ...."));
        ParamUtil.setSessionAttr(bpc.request, RfcConst.LICENCEID, null);
        ParamUtil.setSessionAttr(bpc.request,"SvcName",null);
        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, null);
        ParamUtil.setSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO,null);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.DODRAFTCONFIG,null);

        init(bpc);

        log.debug(StringUtil.changeForLog("the do doStart start ...."));
    }

    /**
     *
     * @param bpc
     * @Decription prepare
     */
    public void prepare(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepare start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO);
        ParamUtil.setRequestAttr(bpc.request,RfcConst.APPSUBMISSIONDTO,appSubmissionDto);
        log.debug(StringUtil.changeForLog("the do prepare end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription prepareDraft
     */
    public void prepareDraft(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareDraft start ...."));
        ParamUtil.setSessionAttr(bpc.request, RfcConst.DODRAFTCONFIG,null);
        loadingDraft(bpc);

        log.debug(StringUtil.changeForLog("the do prepareDraft end ...."));
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
        if(StringUtil.isEmpty(amendType)){
            flag = false;
            ParamUtil.setRequestAttr(bpc.request, "ErrorMsg", "Please select a type of amendment");
        }
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
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "doAmend");
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
     * @Decription prepareFirstView
     */
    public void prepareFirstView(BaseProcessClass bpc)  {
        log.debug(StringUtil.changeForLog("the do prepareFirstView start ...."));
//        ParamUtil.setRequestAttr(bpc.request,"FirstView",AppConsts.TRUE);
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO);


        if(appSubmissionDto != null){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                String svcId = (String) ParamUtil.getSessionAttr(bpc.request,"SvcId");
                ParamUtil.setRequestAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDtos.get(0));
                Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap= NewApplicationHelper.getDisciplineAllocationDtoList(appSubmissionDto,svcId);
                ParamUtil.setRequestAttr(bpc.request, "reloadDisciplineAllocationMap", (Serializable) reloadDisciplineAllocationMap);
                //PO/DPO
                List<AppSvcPrincipalOfficersDto> principalOfficersDtos = IaisCommonUtils.genNewArrayList();
                List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
                if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos.get(0).getAppSvcPrincipalOfficersDtoList())){
                    for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto:appSvcRelatedInfoDtos.get(0).getAppSvcPrincipalOfficersDtoList()){
                        if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(appSvcPrincipalOfficersDto.getPsnType())){
                            principalOfficersDtos.add(appSvcPrincipalOfficersDto);
                        }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())){
                            deputyPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                        }
                    }
                }
                ParamUtil.setRequestAttr(bpc.request, "ReloadPrincipalOfficers", principalOfficersDtos);
                ParamUtil.setRequestAttr(bpc.request, "ReloadDeputyPrincipalOfficers", deputyPrincipalOfficersDtos);

            }
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            appEditSelectDto.setPremisesEdit(true);
            appEditSelectDto.setDocEdit(true);
            appEditSelectDto.setServiceEdit(true);
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        }
        ParamUtil.setSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO,appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request,RfcConst.APPSUBMISSIONDTO,appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request,RfcConst.FIRSTVIEW,AppConsts.TRUE);

        log.debug(StringUtil.changeForLog("the do prepareFirstView end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription doFirstView
     */
    public void doFirstView(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doFirstView start ...."));
        String switchValue = ParamUtil.getString(bpc.request,RfcConst.SWITCH_VALUE);
        if("back".equals(switchValue)){
            ParamUtil.setRequestAttr(bpc.request,RfcConst.SWITCH,"prepare");
            return;
        }
        String editValue = ParamUtil.getString(bpc.request,"EditValue");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO);
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto()==null? new AppEditSelectDto():appSubmissionDto.getAppEditSelectDto();
        String switchVal = "prepareFirstView";
        if(!StringUtil.isEmpty(editValue)){
            switchVal = "doEdit";
            if(RfcConst.EDIT_PREMISES.equals(editValue)){
                appEditSelectDto.setPremisesEdit(true);
                ParamUtil.setRequestAttr(bpc.request,RfcConst.RFC_CURRENT_EDIT,RfcConst.EDIT_PREMISES);
            }else if(RfcConst.EDIT_PRIMARY_DOC.equals(editValue)){
                appEditSelectDto.setDocEdit(true);
                ParamUtil.setRequestAttr(bpc.request,RfcConst.RFC_CURRENT_EDIT,RfcConst.EDIT_PRIMARY_DOC);
            }else if(RfcConst.EDIT_SERVICE.equals(editValue)){
                appEditSelectDto.setServiceEdit(true);
                ParamUtil.setRequestAttr(bpc.request,RfcConst.RFC_CURRENT_EDIT,RfcConst.EDIT_SERVICE);
            }
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDto.setClickEditPage(null);
            ParamUtil.setRequestAttr(bpc.request,RfcConst.APPSUBMISSIONDTORFCATTR,appSubmissionDto);
            ParamUtil.setRequestAttr(bpc.request,"appType",ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        }
        ParamUtil.setRequestAttr(bpc.request,RfcConst.SWITCH,switchVal);
        log.debug(StringUtil.changeForLog("the do doFirstView end ...."));
    }

    /**
     *
     * @param bpc
     * @Decription doBack
     */
    public void doBack(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doBack start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO);
        ParamUtil.setSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO,appSubmissionDto);

        log.debug(StringUtil.changeForLog("the do doBack end ...."));
    }


    /**
     * @param bpc
     * @Decription prepareTranfer
     */
    public void prepareTranfer(BaseProcessClass bpc) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO);
        String serviceName = (String)ParamUtil.getSessionAttr(bpc.request,"SvcName");
        appSubmissionDto.setServiceName(serviceName);
        ParamUtil.setRequestAttr(bpc.request, "prepareTranfer", appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, "AppSubmissionDto", appSubmissionDto);
    }



    private boolean canTransfer(List<LicenseeKeyApptPersonDto> oldLicenseeKeyApptPersonDtos,
                                List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDtoList){
        boolean canTransfer = true;
        if(IaisCommonUtils.isEmpty(oldLicenseeKeyApptPersonDtos)||IaisCommonUtils.isEmpty(licenseeKeyApptPersonDtoList)){
            canTransfer = false;
        }else{
            int existCount = 0;
            for(LicenseeKeyApptPersonDto newLicenseeKeyApptPersonDto : licenseeKeyApptPersonDtoList){
              for(LicenseeKeyApptPersonDto oldLicenseeKeyApptPersonDto1 : oldLicenseeKeyApptPersonDtos){
                if(newLicenseeKeyApptPersonDto.getIdType().equals(oldLicenseeKeyApptPersonDto1.getIdType())
                        && newLicenseeKeyApptPersonDto.getIdNo().equals(oldLicenseeKeyApptPersonDto1.getIdNo())){
                    existCount = existCount+1;
                    break;
                }
              }
            }
            log.info(StringUtil.changeForLog("The  existCount  is -->:"+existCount));
            log.info(StringUtil.changeForLog("The  licenseeKeyApptPersonDtoList.size()  is -->:"+licenseeKeyApptPersonDtoList.size()));
            log.info(StringUtil.changeForLog("The  existCount/licenseeKeyApptPersonDtoList.size() is -->:"+existCount/licenseeKeyApptPersonDtoList.size()));
            if((licenseeKeyApptPersonDtoList.size()-existCount)/licenseeKeyApptPersonDtoList.size()>0.5){
                canTransfer = false;
            }else{
                log.info(StringUtil.changeForLog("can transfer"));
            }
        }

        return canTransfer;

    }

    private FeeDto getTransferFee(){
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        amendmentFeeDto.setChangeInHCIName(false);
        amendmentFeeDto.setChangeInLicensee(true);
        amendmentFeeDto.setChangeInLocation(false);
        FeeDto feeDto = appSubmissionService.getGroupAmendAmount(amendmentFeeDto);
        return feeDto;
    }

    private boolean isSelect(String[] selectCheakboxs,String premisesIndexNo){
        boolean isSelect = false;
       if(!StringUtil.isEmpty(premisesIndexNo) && selectCheakboxs != null){
          for(String select : selectCheakboxs){
              if(premisesIndexNo.equals(select)){
                  isSelect = true;
                  break;
              }
          }
       }
       return isSelect;
    }

    private Map<String,String> doValidateEmpty(String uen,String[] selectCheakboxs){
        Map<String,String> error = IaisCommonUtils.genNewHashMap();
        if(selectCheakboxs == null || selectCheakboxs.length == 0){
            error.put("premisesError","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(uen)){
            error.put("uenError","UC_CHKLMD001_ERR001");
        }
        return error;
    }
    private Map<String,String> doValidateLojic(String uen,Map<String,String> error,LicenceDto licenceDto,LicenseeDto licenseeDto){
        if(licenceDto==null){
            error.put("uenError","Licence Error!!!");
        }else{
            if(licenseeDto == null){
                error.put("uenError","uen error !!!");
            }else{
                List<LicenseeKeyApptPersonDto> oldLicenseeKeyApptPersonDtos = requestForChangeService.
                        getLicenseeKeyApptPersonDtoListByLicenseeId(licenceDto.getLicenseeId());
                List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDtoList = requestForChangeService.getLicenseeKeyApptPersonDtoListByUen(uen);
                boolean canTransfer = canTransfer(oldLicenseeKeyApptPersonDtos,licenseeKeyApptPersonDtoList);
                if(canTransfer){
                    if(licenceDto.getLicenseeId().equals(licenseeDto.getId())){
                        log.error(StringUtil.changeForLog("This Uen can not get the licensee -->:"+uen));
                        error.put("uenError","can not transfer to self");
                    }
                }else{
                    error.put("uenError","can not transfer");
                }
            }
        }
        return error;
    }
    /**
     * @param bpc
     * @Decription compareChangePercentage
     */
    public void compareChangePercentage(BaseProcessClass bpc) throws CloneNotSupportedException,IOException {
        log.info(StringUtil.changeForLog("The compareChangePercentage start ..."));
        String licenceId = (String) ParamUtil.getSessionAttr(bpc.request, RfcConst.LICENCEID);
        String uen = ParamUtil.getString(bpc.request, "UEN");
        String[] selectCheakboxs = ParamUtil.getStrings(bpc.request, "premisesInput");
        log.info(StringUtil.changeForLog("The compareChangePercentage licenceId is -->:"+licenceId));
        log.info(StringUtil.changeForLog("The compareChangePercentage uen is -->:"+uen));
        Map<String,String> error = doValidateEmpty(uen,selectCheakboxs);
        if(error.isEmpty()){
            LicenceDto licenceDto = requestForChangeService.getLicenceDtoByLicenceId(licenceId);
            LicenseeDto licenseeDto = requestForChangeService.getLicenseeByUenNo(uen);
            doValidateLojic(uen,error,licenceDto,licenseeDto);
            if(error.isEmpty()){
                String newLicenseeId = licenseeDto.getId();
                log.info(StringUtil.changeForLog("The newLicenseeId is -->:"+newLicenseeId));
                AppSubmissionDto appSubmissionDto = requestForChangeService.getAppSubmissionDtoByLicenceId(licenceId);
                appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                appSubmissionDto.setLicenseeId(newLicenseeId);
                appSubmissionDto.setAutoRfc(true);
                FeeDto feeDto = getTransferFee();
                if(feeDto != null){
                    Double amount = feeDto.getTotal();
                    appSubmissionDto.setAmount(amount);
                    log.info(StringUtil.changeForLog("The selectCheakboxs.length is -->:"+selectCheakboxs.length));
                    log.info(StringUtil.changeForLog("The appSubmissionDto.getAppGrpPremisesDtoList().size() is -->:"
                            +appSubmissionDto.getAppGrpPremisesDtoList().size()));
                    if (selectCheakboxs.length == appSubmissionDto.getAppGrpPremisesDtoList().size()) {
                        // appSubmissionDto.setIsNeedNewLicNo("0");
                        for (AppGrpPremisesDto appGrpPremisesDto: appSubmissionDto.getAppGrpPremisesDtoList()) {
                            appGrpPremisesDto.setNeedNewLicNo(false);
                            appGrpPremisesDto.setGroupLicenceFlag("1");
                        }
                    } else {
                        // appSubmissionDto.setIsNeedNewLicNo("1");
                        for (AppGrpPremisesDto appGrpPremisesDto : appSubmissionDto.getAppGrpPremisesDtoList()) {
                            String premise = appGrpPremisesDto.getTranferSelect();
                            boolean isSelect  = isSelect(selectCheakboxs,premise);
                            if(isSelect){
                                appGrpPremisesDto.setNeedNewLicNo(true);
                                appGrpPremisesDto.setGroupLicenceFlag("1");
                            }else {
                                appGrpPremisesDto.setNeedNewLicNo(false);
                                appGrpPremisesDto.setGroupLicenceFlag("2");
                            }
                        }
                    }
                    String grpNo = appSubmissionService.getGroupNo(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                    log.info(StringUtil.changeForLog("The grpNo is -->:"+grpNo));
                    appSubmissionDto.setAppGrpNo(grpNo);
                    List<String> serviceNames = IaisCommonUtils.genNewArrayList();
                    for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSubmissionDto.getAppSvcRelatedInfoDtoList()) {
                        serviceNames.add(appSvcRelatedInfoDto.getServiceName());
                    }
                    List<HcsaServiceDto> hcsaServiceDtos = serviceConfigService.getHcsaServiceByNames(serviceNames);
                    ParamUtil.setRequestAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, hcsaServiceDtos);
                    NewApplicationHelper.setSubmissionDtoSvcData(bpc.request, appSubmissionDto);
                    appSubmissionService.setRiskToDto(appSubmissionDto);
                    AppSubmissionDto tranferSub = requestForChangeService.submitChange(appSubmissionDto);
                    ParamUtil.setSessionAttr(bpc.request, "app-rfc-tranfer", tranferSub);
                    StringBuffer url = new StringBuffer();
                    url.append("https://").append(bpc.request.getServerName())
                            .append("/hcsa-licence-web/eservice/INTERNET/MohNewApplication/PreparePayment");
                    String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
                    bpc.response.sendRedirect(tokenUrl);
                }
            }
        }
        if(!error.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"errorMsg" , WebValidationHelper.generateJsonStr(error));
            ParamUtil.setRequestAttr(bpc.request,"UEN",uen);
            log.info(StringUtil.changeForLog("The selectCheakboxs.toString() is -->:"+ArrayUtils.toString(selectCheakboxs)));
            ParamUtil.setRequestAttr(bpc.request,"selectCheakboxs",ArrayUtils.toString(selectCheakboxs));
        }
        log.info(StringUtil.changeForLog("The compareChangePercentage end ..."));
    }


    /**
     * @param bpc
     * @Decription submitPayment
     */
    public void submitPayment(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do submitPayment start ...."));
        AppSubmissionDto appSubmissionDto=(AppSubmissionDto) ParamUtil.getRequestAttr(bpc.request, "tranferPayment");

        ParamUtil.setRequestAttr(bpc.request, "tranferPayment", appSubmissionDto);
        log.debug(StringUtil.changeForLog("the do submitPayment end ...."));

    }

    private boolean iftranfer(String UNID,String licenceId,String pageType){
        return true;
    }

    private void init(BaseProcessClass bpc) throws CloneNotSupportedException {
        String licenceId = ParamUtil.getString(bpc.request, "licenceId");
        ParamUtil.setSessionAttr(bpc.request, RfcConst.LICENCEID, licenceId);

        //load data
        if(!StringUtil.isEmpty(licenceId)){
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByLicenceId(licenceId);
            if(appSubmissionDto == null || IaisCommonUtils.isEmpty(appSubmissionDto.getAppGrpPremisesDtoList()) ||
                    IaisCommonUtils.isEmpty(appSubmissionDto.getAppSvcRelatedInfoDtoList())){
                log.info("appSubmissionDto incomplete , licenceId:"+licenceId);
            }else{
                appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                String svcName = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName();
                List<String> svcNames = IaisCommonUtils.genNewArrayList();
                svcNames.add(svcName);
                List<HcsaServiceDto> hcsaServiceDtoList  = serviceConfigService.getHcsaServiceByNames(svcNames);
                ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
                //set svcInfo
                NewApplicationHelper.setSubmissionDtoSvcData(bpc.request,appSubmissionDto);
                //set laboratory disciplines info
                if(!IaisCommonUtils.isEmpty(hcsaServiceDtoList)){
                    List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos= serviceConfigService.loadLaboratoryDisciplines(hcsaServiceDtoList.get(0).getId());
                    NewApplicationHelper.setLaboratoryDisciplinesInfo(appSubmissionDto,hcsaSvcSubtypeOrSubsumedDtos);
                    ParamUtil.setSessionAttr(bpc.request, "SvcId",hcsaServiceDtoList.get(0).getId());
                }

                ParamUtil.setSessionAttr(bpc.request,"SvcName",svcName);
                ParamUtil.setSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO,appSubmissionDto);
            }
        }
    }

    private void loadingDraft(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do loadingDraft start ...."));
        String draftNo = ParamUtil.getString(bpc.request, "DraftNumber");
        //draftNo = "DQ2003030005426";
        String action = "doAmend";
        if(!StringUtil.isEmpty(draftNo)){
            log.info(StringUtil.changeForLog("draftNo is not empty"));
            AppSubmissionDto appSubmissionDto = serviceConfigService.getAppSubmissionDtoDraft(draftNo);
            if(appSubmissionDto.getAppGrpPremisesDtoList() != null && appSubmissionDto.getAppGrpPremisesDtoList().size() >0){
                ParamUtil.setSessionAttr(bpc.request, RfcConst.RFCAPPSUBMISSIONDTO, appSubmissionDto);
            }else{
                ParamUtil.setSessionAttr(bpc.request, RfcConst.RFCAPPSUBMISSIONDTO, null);
            }
            ParamUtil.setSessionAttr(bpc.request, RfcConst.DODRAFTCONFIG,"test");
        }else{
            action = "error";
            ParamUtil.setRequestAttr(bpc.request, RfcConst.ACKMESSAGE,"error !!!");
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, action);
        log.info(StringUtil.changeForLog("the do loadingDraft end ...."));
    }
}
