package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.utils.*;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * AutoRenewalDelegator
 *
 * @author caijing
 * @date 2020/1/6
 */

@Delegator("withOutRenewalDelegator")
@Slf4j
public class WithOutRenewalDelegator {
    private static final String PAGE1 = "instructions";
    private static final String PAGE2 = "licenceReview";
    private static final String PAGE3 = "payment";
    private static final String PAGE4 = "acknowledgement";
    private static final String INSTRUCTIONS = "doInstructions";
    private static final String REVIEW = "doLicenceReview";
    private static final String PAYMENT = "doPayment";
    private static final String BACK = "back";
    private static final String ACKNOWLEDGEMENT = "doAcknowledgement";
    private static final String PAGE_SWITCH = "page_value";
    private static final String CONTROL_SWITCH = "controlSwitch";
    @Autowired
    WithOutRenewalService outRenewalService;

    @Autowired
    AppSubmissionService appSubmissionService;

    public void start(BaseProcessClass bpc){
        log.info("**** the non auto renwal  start ******");
        //init session
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO,null);

        //init page value
        //instructions
        ParamUtil.setRequestAttr(bpc.request,"page_value",PAGE1);

        //init data
        List<String> licenceIDList = (List<String>) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_LIC_ID_LIST_ATTR);
/*        licenceIDList = IaisCommonUtils.genNewArrayList();
        licenceIDList.add("B1DC1835-E161-EA11-BE7F-000C29F371DC");*/
        if (licenceIDList == null || IaisCommonUtils.isEmpty(licenceIDList)){
            log.info("can not find licence id for without renewal");
            return;
        }
        int index = 0;
        String firstSvcName = "";
        List<String> serviceNameTitleList = IaisCommonUtils.genNewArrayList();
        List<String> serviceNameList = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> appSubmissionDtoList = outRenewalService.getAppSubmissionDtos(licenceIDList);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = IaisCommonUtils.genNewArrayList();
        List<Map<String,List<AppSvcDisciplineAllocationDto>>> reloadDisciplineAllocationMapList = IaisCommonUtils.genNewArrayList();
        List<List<AppSvcPrincipalOfficersDto>> principalOfficersDtosList = IaisCommonUtils.genNewArrayList();
        List<List<AppSvcPrincipalOfficersDto>> deputyPrincipalOfficersDtosList = IaisCommonUtils.genNewArrayList();
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        for (AppSubmissionDto appSubmissionDto: appSubmissionDtoList) {
            if(!appSubmissionDto.getAppSvcRelatedInfoDtoList().isEmpty()) {
                String serviceName = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName();
                String svcId = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceId();

                Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap= NewApplicationHelper.getDisciplineAllocationDtoList(appSubmissionDto,svcId);
                reloadDisciplineAllocationMapList.add(reloadDisciplineAllocationMap);

                AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
                appSvcRelatedInfoDtoList.add(appSvcRelatedInfoDto);
                List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                List<AppSvcPrincipalOfficersDto> principalOfficersDtos = IaisCommonUtils.genNewArrayList();
                List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
                if(appSvcPrincipalOfficersDtos != null && ! appSvcPrincipalOfficersDtos.isEmpty()){
                    for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto:appSvcPrincipalOfficersDtos){
                        if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(appSvcPrincipalOfficersDto.getPsnType())){
                            principalOfficersDtos.add(appSvcPrincipalOfficersDto);
                        }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())){
                            deputyPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                        }
                    }
                }
                principalOfficersDtosList.add(principalOfficersDtos);
                deputyPrincipalOfficersDtosList.add(deputyPrincipalOfficersDtos);

                if(!StringUtil.isEmpty(serviceName)){
                    if(index ==0){
                        firstSvcName = serviceName;
                        index ++;
                        ParamUtil.setSessionAttr(bpc.request,"AppSubmissionDto", appSubmissionDto);
                    }else{
                        serviceNameTitleList.add(serviceName);
                    }
                    serviceNameList.add(serviceName);
                }
                appSubmissionDto.setServiceName(serviceName);
                appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            }
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_RENEWAL);

            String draftNumber = appSubmissionService.getDraftNo(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            String groupNumber =  appSubmissionService.getGroupNo(ApplicationConsts.APPLICATION_TYPE_RENEWAL);

            log.info("without renewal deafrt number =====>" + draftNumber);
            log.info("without renewal group number =====>" + groupNumber);

            appSubmissionDto.setDraftNo(draftNumber);
            appSubmissionDto.setAppGrpNo(groupNumber);
            appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

            try {
                NewApplicationHelper.setSubmissionDtoSvcData(bpc.request, appSubmissionDto);
            } catch (CloneNotSupportedException e) {
                log.error(e.getMessage());
            }

            appSubmissionService.setRiskToDto(appSubmissionDto);
        }

        RenewDto renewDto = new RenewDto();

        String parseToJson = JsonUtil.parseToJson(appSubmissionDtoList);
        log.info("without renewal submission json info " + parseToJson);

        renewDto.setAppSubmissionDtos(appSubmissionDtoList);
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, renewDto);
        ParamUtil.setSessionAttr(bpc.request,"serviceNameTitleList", (Serializable)serviceNameTitleList);
        //serviceNameList
        ParamUtil.setSessionAttr(bpc.request,"serviceNames", (Serializable)serviceNameList);
        ParamUtil.setSessionAttr(bpc.request,"firstSvcName", firstSvcName);
        ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfoList", (Serializable)appSvcRelatedInfoDtoList);
        ParamUtil.setSessionAttr(bpc.request, "reloadDisciplineAllocationMapList", (Serializable)reloadDisciplineAllocationMapList);
        ParamUtil.setSessionAttr(bpc.request, "ReloadPrincipalOfficersList", (Serializable)principalOfficersDtosList);
        ParamUtil.setSessionAttr(bpc.request, "deputyPrincipalOfficersDtosList", (Serializable)deputyPrincipalOfficersDtosList);



        log.info("**** the non auto renwal  end ******");
    }

    /**
     * AutoStep: prepare
     *
     * @param bpc
     * @throws
     */
    public void prepare(BaseProcessClass bpc)throws Exception{
        log.info("**** the  auto renwal  prepare start  ******");

        log.info("**** the  renwal  prepare  end ******");
    }

    public void prepareInstructions(BaseProcessClass bpc)throws Exception{

    }

    //prepareLicenceReview
    public void prepareLicenceReview(BaseProcessClass bpc)throws Exception{

    }

    //preparePayment
    public void preparePayment(BaseProcessClass bpc)throws Exception{
        RenewDto renewDto = (RenewDto)ParamUtil.getSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        if(renewDto == null){
            return;
        }
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtos){
            if(!StringUtil.isEmpty(appSubmissionDto.getAmount())){
                String amountStr = Formatter.formatterMoney(appSubmissionDto.getAmount());
                appSubmissionDto.setAmountStr(amountStr);
            }
        }

    }

    //prepareAcknowledgement
    public void prepareAcknowledgement(BaseProcessClass bpc)throws Exception{

    }

    //doInstructions
    public void doInstructions(BaseProcessClass bpc)throws Exception{
        //go page2
        ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE2);
    }

    //doLicenceReview
    public void doLicenceReview(BaseProcessClass bpc)throws Exception{
        ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE3);
    }

    //doPayment
    public void doPayment(BaseProcessClass bpc)throws Exception{

    }

    //doAcknowledgement
    public void doAcknowledgement(BaseProcessClass bpc)throws Exception{

    }

    //controlSwitch
    public void controlSwitch(BaseProcessClass bpc)throws Exception{
        String switch_value = ParamUtil.getString(bpc.request,"switch_value");
        if(INSTRUCTIONS.equals(switch_value)){
            //controlSwitch
            ParamUtil.setRequestAttr(bpc.request,CONTROL_SWITCH,switch_value);
        }else if(REVIEW.equals(switch_value)){
            ParamUtil.setRequestAttr(bpc.request,CONTROL_SWITCH,switch_value);
        }else if(PAYMENT.equals(switch_value)){
            ParamUtil.setRequestAttr(bpc.request,CONTROL_SWITCH,switch_value);
        }else if(ACKNOWLEDGEMENT.equals(switch_value)){
            ParamUtil.setRequestAttr(bpc.request,CONTROL_SWITCH,switch_value);
        }else if(PAGE1.equals(switch_value)){
            ParamUtil.setRequestAttr(bpc.request,CONTROL_SWITCH,BACK);
            ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,switch_value);
        }
    }


    /**
     * AutoStep: determineAutoRenewalEligibility
     *
     * @param bpc
     * @throws
     */
    public void determineAutoRenewalEligibility(BaseProcessClass bpc){
        log.info("**** the determineAutoRenewalEligibility  prepare start  ******");

        //todo: editvalue is not null and one licence to jump
        String editValue = ParamUtil.getString(bpc.request,"EditValue");
        if(!StringUtil.isEmpty(editValue)){
            RenewDto renewDto  = (RenewDto) ParamUtil.getSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
            if(renewDto != null){
                List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
                if(!IaisCommonUtils.isEmpty(appSubmissionDtos) && appSubmissionDtos.size() == 1){
                    ParamUtil.setRequestAttr(bpc.request,"EditValue",editValue);
                    ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,"jump");
                }
            }
        }



        log.info("**** the determineAutoRenewalEligibility  prepare  end ******");
    }


    /**
     * AutoStep: markPostInspection
     *
     * @param bpc
     * @throws
     */
    public void markPostInspection(BaseProcessClass bpc){
        log.info("**** the markPostInspection start  ******");
        AppSubmissionDto appSubmissionDto=(AppSubmissionDto)ParamUtil.getSessionAttr(bpc.request,"appSubmissionDto");
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }
        log.info("**** the markPostInspection end ******");
    }


    /**
     * AutoStep: prepareJump
     *
     * @param bpc
     * @throws
     */
    public void prepareJump(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do prepareJump start ...."));
        String editValue = (String) ParamUtil.getRequestAttr(bpc.request,"EditValue");
        RenewDto renewDto  = (RenewDto) ParamUtil.getSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        AppSubmissionDto appSubmissionDto = appSubmissionDtos.get(0);
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto()==null? new AppEditSelectDto():appSubmissionDto.getAppEditSelectDto();
        if(!StringUtil.isEmpty(editValue)){
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
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            ParamUtil.setRequestAttr(bpc.request,RfcConst.APPSUBMISSIONDTORFCATTR,appSubmissionDto);
            ParamUtil.setRequestAttr(bpc.request,"appType",ApplicationConsts.APPLICATION_TYPE_RENEWAL);
        }


        log.info(StringUtil.changeForLog("the do prepareJump end ...."));
    }

    /**
     * AutoStep: toPrepareData
     *
     * @param bpc
     * @throws
     */
    public void toPrepareData(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do toPrepareData start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO);
        RenewDto renewDto = new RenewDto();
        List<AppSubmissionDto> appSubmissionDtos = IaisCommonUtils.genNewArrayList();
        appSubmissionDtos.add(appSubmissionDto);
        renewDto.setAppSubmissionDtos(appSubmissionDtos);
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, renewDto);
        ParamUtil.setRequestAttr(bpc.request,"jumpEdit","Y");
        log.info(StringUtil.changeForLog("the do toPrepareData end ...."));
    }


    //=============================================================================
    //private method
    //=============================================================================





}
