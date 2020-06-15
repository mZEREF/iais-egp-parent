package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import com.ecquaria.cloud.moh.iais.validation.PaymentValidate;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.util.CopyUtil;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


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
    private static final String EDIT = "doEdit";
    private static final String PREFIXTITLE = "prefixTitle";
    @Autowired
    WithOutRenewalService outRenewalService;

    @Autowired
    AppSubmissionService appSubmissionService;

    @Autowired
    ServiceConfigService serviceConfigService;

    @Autowired
    NewApplicationDelegator newApplicationDelegator;

    @Autowired
    RequestForChangeService requestForChangeService;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;

    @Value("${iais.email.sender}")
    private String mailSender;

    public void start(BaseProcessClass bpc){
        log.info("**** the non auto renwal  start ******");
        String draftNo = ParamUtil.getMaskedString(bpc.request,"DraftNumber");
        //init session
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, null);
        ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO,null);
        ParamUtil.setSessionAttr(bpc.request,"totalStr",null);
        ParamUtil.setSessionAttr(bpc.request,"totalAmount",null);
        ParamUtil.setSessionAttr(bpc.request,"userAgreement",null);ParamUtil.setSessionAttr(bpc.request,PREFIXTITLE,"renewing");

        //inbox draft number
        String oldDraftNumber= ParamUtil.getMaskedString(bpc.request, "DraftNumber");
        //init page value
        //instructions
        ParamUtil.setRequestAttr(bpc.request,"page_value",PAGE1);
        String licenceId= ParamUtil.getMaskedString(bpc.request,"licenceId");

        //init data
        List<String> licenceIDList = (List<String>) ParamUtil.getSessionAttr(bpc.request, RenewalConstants.WITHOUT_RENEWAL_LIC_ID_LIST_ATTR);
        if(licenceIDList==null&&licenceId!=null){
            licenceIDList=new ArrayList<>(1);
            licenceIDList.add(licenceId);
        }
        /*        licenceIDList = IaisCommonUtils.genNewArrayList();
        licenceIDList.add("B1DC1835-E161-EA11-BE7F-000C29F371DC");*/
        if (licenceIDList == null || IaisCommonUtils.isEmpty(licenceIDList)){
            log.info("can not find licence id for without renewal");
            return;
        }
        //get licensee ID
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(bpc.request,"INTER_INBOX_USER_INFO");
        int index = 0;
        String firstSvcName = "";
        List<String> serviceNameTitleList = IaisCommonUtils.genNewArrayList();
        List<String> serviceNameList = IaisCommonUtils.genNewArrayList();
        List<AppSubmissionDto> appSubmissionDtoList = IaisCommonUtils.genNewArrayList();
        if(StringUtil.isEmpty(draftNo)){
            appSubmissionDtoList = outRenewalService.getAppSubmissionDtos(licenceIDList);
            ParamUtil.setSessionAttr(bpc.request,"backUrl","initLic");
        }else{
            appSubmissionDtoList.add(serviceConfigService.getAppSubmissionDtoDraft(draftNo));
            ParamUtil.setSessionAttr(bpc.request,"backUrl","initApp");
        }

        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = IaisCommonUtils.genNewArrayList();
        List<Map<String,List<AppSvcDisciplineAllocationDto>>> reloadDisciplineAllocationMapList = IaisCommonUtils.genNewArrayList();
        List<List<AppSvcPrincipalOfficersDto>> principalOfficersDtosList = IaisCommonUtils.genNewArrayList();
        List<List<AppSvcPrincipalOfficersDto>> deputyPrincipalOfficersDtosList = IaisCommonUtils.genNewArrayList();
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        List<String> serviceNames = IaisCommonUtils.genNewArrayList();
        for (AppSubmissionDto appSubmissionDto: appSubmissionDtoList) {
            List<AppGrpPremisesDto> appGrpPremisesDtos= appSubmissionDto.getAppGrpPremisesDtoList();
            if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                    NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                }
            }
            if(!appSubmissionDto.getAppSvcRelatedInfoDtoList().isEmpty()) {
                String serviceName = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName();
                //add to service name list
                serviceNames.add(serviceName);
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
                String svcId = hcsaServiceDto.getId();
                //set svc info(svcId,svcName,svcCode,svcCode)
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
                appSvcRelatedInfoDto.setServiceId(hcsaServiceDto.getId());
                appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                //set AppSvcRelatedInfoDtoList chkName
                List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos= serviceConfigService.loadLaboratoryDisciplines(svcId);
                NewApplicationHelper.setLaboratoryDisciplinesInfo(appSubmissionDto,hcsaSvcSubtypeOrSubsumedDtos);
                Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap= NewApplicationHelper.getDisciplineAllocationDtoList(appSubmissionDto,svcId);
                reloadDisciplineAllocationMapList.add(reloadDisciplineAllocationMap);
                //set svc step
                List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(svcId);
                appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
                //set doc info
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
                List<HcsaSvcDocConfigDto> primaryDocConfig = serviceConfigService.getAllHcsaSvcDocs(null);
                if(!StringUtil.isEmpty(svcId)){
                    List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                    List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(svcId);
                    NewApplicationHelper.setDocInfo(appGrpPrimaryDocDtos,appSvcDocDtos,primaryDocConfig,svcDocConfig);
                }
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
                if(licenceIDList.size() == 1){
                    appEditSelectDto.setPremisesEdit(true);
                    appEditSelectDto.setDocEdit(true);
                    appEditSelectDto.setServiceEdit(true);
                    ParamUtil.setSessionAttr(bpc.request,"isSingle", "Y");
                }else{
                    ParamUtil.setSessionAttr(bpc.request,"isSingle", "N");
                }
                appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            }
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_RENEWAL);
            //set licensee ID
            String licenseeId = interInboxUserDto.getLicenseeId();
            appSubmissionDto.setLicenseeId(licenseeId);
            LicenseeDto licenseeDto = organizationLienceseeClient.getLicenseeById(licenseeId).getEntity();
            String licenseeName = "-";
            if(licenseeDto!=null){
                licenseeName = licenseeDto.getName();
                ParamUtil.setSessionAttr(bpc.request,"licenseeName", licenseeName);
            }

            String groupNumber =  appSubmissionService.getGroupNo(ApplicationConsts.APPLICATION_TYPE_RENEWAL);

            log.info(StringUtil.changeForLog("without renewal group number =====>" + groupNumber));
            if(StringUtil.isEmpty(oldDraftNumber)){
                String draftNumber = appSubmissionService.getDraftNo(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
                appSubmissionDto.setDraftNo(draftNumber);
                log.info(StringUtil.changeForLog("without renewal deafrt number =====>" + draftNumber));
            }else {
                appSubmissionDto.setDraftNo(oldDraftNumber);
            }

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
        log.info(StringUtil.changeForLog("without renewal submission json info " + parseToJson));

        if(!IaisCommonUtils.isEmpty(serviceNames)) {
            List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getHcsaServiceByNames(serviceNames);
            ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
        }

        renewDto.setAppSubmissionDtos(appSubmissionDtoList);

        List<AppSubmissionDto> cloneAppsbumissionDtos = IaisCommonUtils.genNewArrayList();
        CopyUtil.copyMutableObjectList(appSubmissionDtoList, cloneAppsbumissionDtos);
        ParamUtil.setSessionAttr(bpc.request, "oldSubmissionDtos", (Serializable) cloneAppsbumissionDtos);
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, renewDto);
        ParamUtil.setSessionAttr(bpc.request,"serviceNameTitleList", (Serializable)serviceNameTitleList);
        //serviceNameList
        ParamUtil.setSessionAttr(bpc.request,"serviceNames", (Serializable)serviceNameList);
        ParamUtil.setSessionAttr(bpc.request,"firstSvcName", firstSvcName);
        ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfoList", (Serializable)appSvcRelatedInfoDtoList);
        ParamUtil.setSessionAttr(bpc.request, "reloadDisciplineAllocationMapList", (Serializable)reloadDisciplineAllocationMapList);
        ParamUtil.setSessionAttr(bpc.request, "ReloadPrincipalOfficersList", (Serializable)principalOfficersDtosList);
        ParamUtil.setSessionAttr(bpc.request, "deputyPrincipalOfficersDtosList", (Serializable)deputyPrincipalOfficersDtosList);

        //init app submit
        ParamUtil.setSessionAttr(bpc.request,"hasAppSubmit",null);
        ParamUtil.setSessionAttr(bpc.request,"txnDt",null);
        ParamUtil.setSessionAttr(bpc.request,"txnRefNo",null);

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
        ParamUtil.setRequestAttr(bpc.request,RfcConst.FIRSTVIEW,AppConsts.TRUE);
        //finish pay
        RenewDto renewDto = (RenewDto)ParamUtil.getSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        String groupId = "";
        Double amount=0.0;
        String licenseeId=null;
        if(appSubmissionDtos.size() != 0){
            groupId = appSubmissionDtos.get(0).getAppGrpId();
            amount = appSubmissionDtos.get(0).getAmount();
            licenseeId = appSubmissionDtos.get(0).getLicenseeId();

            AppSubmissionDto oldAppSubmissionDto=(AppSubmissionDto) bpc.request.getSession().getAttribute("oldAppSubmissionDto");
            if(oldAppSubmissionDto==null){
                oldAppSubmissionDto= appSubmissionDtos.get(0);
            }
            bpc.request.getSession().setAttribute("oldAppSubmissionDto",oldAppSubmissionDto);

        }
        String result = ParamUtil.getMaskedString(bpc.request,"result");
        if (!StringUtil.isEmpty(result)) {
            log.info(StringUtil.changeForLog("payment result:" + result));
            String pmtRefNo = ParamUtil.getMaskedString(bpc.request,"reqRefNo");
            if ("success".equals(result) && !StringUtil.isEmpty(pmtRefNo)) {
                String txnDt = ParamUtil.getMaskedString(bpc.request,"txnDt");
                String txnRefNo = ParamUtil.getMaskedString(bpc.request,"txnRefNo");

                ParamUtil.setSessionAttr(bpc.request,"txnDt",txnDt);
                ParamUtil.setSessionAttr(bpc.request,"txnRefNo",txnRefNo);
                List<AppSubmissionDto> otherAppSubmissionDtos=(List<AppSubmissionDto>)bpc.request.getSession().getAttribute("otherAppSubmissionDtos");
                if(otherAppSubmissionDtos!=null&&!otherAppSubmissionDtos.isEmpty()){
                    String rfcGrpId = otherAppSubmissionDtos.get(0).getAppGrpId();
                    ApplicationGroupDto rfcAppGrp = new ApplicationGroupDto();
                    rfcAppGrp.setId(rfcGrpId);
                    rfcAppGrp.setPmtRefNo(pmtRefNo);
                    rfcAppGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                    serviceConfigService.updatePaymentStatus(rfcAppGrp);
                }
                //update status
                ApplicationGroupDto appGrp = new ApplicationGroupDto();
                appGrp.setId(groupId);
                appGrp.setPmtRefNo(pmtRefNo);
                appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                serviceConfigService.updatePaymentStatus(appGrp);
                //update application status
                appSubmissionService.updateApplicationsStatus(groupId,ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
                //jump page to acknowledgement
                //send email pay success
                try {

                    sendEmail(bpc.request,groupId,"successfulOnlinePayment",licenseeId,amount,"xxxx-xxxx-xxxx");
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
                ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE4);
            }else{
                //jump page to payment
                ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE3);
            }
        }
        log.info("**** the  renwal  prepare  end ******");
    }

    public void prepareInstructions(BaseProcessClass bpc)throws Exception{
        ParamUtil.setRequestAttr(bpc.request,"hasDetail","N");
    }

    //prepareLicenceReview
    public void prepareLicenceReview(BaseProcessClass bpc)throws Exception{
        ParamUtil.setRequestAttr(bpc.request,"hasDetail","Y");
        List<AppSubmissionDto> oldSubmissionDtos = (List<AppSubmissionDto>)ParamUtil.getSessionAttr(bpc.request, "oldSubmissionDtos");
        RenewDto renewDto = (RenewDto)ParamUtil.getSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        if(!IaisCommonUtils.isEmpty(oldSubmissionDtos)&&!IaisCommonUtils.isEmpty(appSubmissionDtos)){
            List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList = oldSubmissionDtos.get(0).getAppSvcRelatedInfoDtoList();
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldSubmissionDtos.get(0).getAppGrpPremisesDtoList();
            List<AppGrpPrimaryDocDto> oldAppGrpPrimaryDocDtos = oldSubmissionDtos.get(0).getAppGrpPrimaryDocDtos();
            List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList = appSubmissionDtos.get(0).getAppSvcRelatedInfoDtoList();
            List<AppGrpPremisesDto> newAppGrpPremisesDtoList = appSubmissionDtos.get(0).getAppGrpPremisesDtoList();
            List<AppGrpPrimaryDocDto> newAppGrpPrimaryDocDtos = appSubmissionDtos.get(0).getAppGrpPrimaryDocDtos();
            if(!oldAppSvcRelatedInfoDtoList.equals(newAppSvcRelatedInfoDtoList) ||!oldAppGrpPremisesDtoList.equals(newAppGrpPremisesDtoList) ||!oldAppGrpPrimaryDocDtos.equals(newAppGrpPrimaryDocDtos)){
                ParamUtil.setRequestAttr(bpc.request,"changeRenew","Y");
            }
        }
    }

    //preparePayment
    public void preparePayment(BaseProcessClass bpc)throws Exception{
        RenewDto renewDto = (RenewDto)ParamUtil.getSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        ParamUtil.setRequestAttr(bpc.request,"hasDetail","Y");
        if(renewDto == null){
            return;
        }
        //
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setServiceEdit(true);
        appEditSelectDto.setPremisesEdit(true);
        appEditSelectDto.setDocEdit(true);
        appEditSelectDto.setPoEdit(true);
        appEditSelectDto.setDocEdit(true);
        AppFeeDetailsDto appFeeDetailsDto=new AppFeeDetailsDto();
        String hasSubmit = (String)ParamUtil.getSessionAttr(bpc.request,"hasAppSubmit");
        if("Y".equals(hasSubmit)){
            return;
        }
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        //app submit
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto)ParamUtil.getSessionAttr(bpc.request,"INTER_INBOX_USER_INFO");
        String licenseeId = null;
        if(interInboxUserDto!=null){
            licenseeId = interInboxUserDto.getLicenseeId();
        }else{
            log.error(StringUtil.changeForLog("interInboxUserDto null"));
        }
        Double total = 0d;
        Double renewTotal=0.0;
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        amendmentFeeDto.setChangeInLicensee(Boolean.FALSE);

        AppSubmissionDto oldAppSubmissionDto  =(AppSubmissionDto)bpc.request.getSession().getAttribute("oldAppSubmissionDto");
        List<AppGrpPremisesDto> oldAppSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        List<AppSubmissionDto> rfcAppSubmissionDtos=IaisCommonUtils.genNewArrayList();
        List<String> renewLicIds = IaisCommonUtils.genNewArrayList();
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtos){
            appEditSelectDto.setPremisesEdit(true);
            appEditSelectDto.setServiceEdit(false);
            appEditSelectDto.setDocEdit(false);
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDto.setChangeSelectDto(appEditSelectDto);
            boolean premiseChange;
            String appType = appSubmissionDto.getAppType();
            String appGrpNo = requestForChangeService.getApplicationGroupNumber(appType);
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
              appGrpPremisesDto.setLicenceDtos(null);
              if(StringUtil.isEmpty(appGrpPremisesDto.getOffTelNo())){
                  appGrpPremisesDto.setOffTelNo(null);
              }
              if(StringUtil.isEmpty(appGrpPremisesDto.getCertIssuedDtStr())){
                  appGrpPremisesDto.setCertIssuedDtStr(null);
              }
            }
            boolean eqGrpPremisesResult = eqGrpPremises(appGrpPremisesDtoList, oldAppSubmissionDtoAppGrpPremisesDtoList);
            premiseChange=!eqGrpPremisesResult;

            if(!premiseChange){
                if(appGrpPremisesDtoList!=null){
                    for(int i=0;i<appGrpPremisesDtoList.size();i++){
                        List<LicenceDto> attribute =(List<LicenceDto>) bpc.request.getSession().getAttribute("selectLicence"+i);
                        if(attribute!=null){
                            for(LicenceDto licenceDto : attribute){
                                AppSubmissionDto appSubmissionDtoByLicenceId = requestForChangeService.getAppSubmissionDtoByLicenceId(licenceDto.getId());
                                appSubmissionService.transform(appSubmissionDtoByLicenceId,licenseeId);
                                boolean groupLic = appSubmissionDtoByLicenceId.isGroupLic();
                                String address = appGrpPremisesDtoList.get(i).getAddress();
                                boolean equals;
                                if(groupLic){
                                    AppGrpPremisesDto oldAppGrpPremisesDto = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(i);
                                    boolean b = NewApplicationDelegator.compareHciName(oldAppGrpPremisesDto, appGrpPremisesDtoList.get(i));
                                    amendmentFeeDto.setChangeInHCIName(!b);
                                    String olAddress = oldAppGrpPremisesDto.getAddress();
                                    equals = olAddress.equals(address);
                                }else {
                                    String oldAddress = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0).getAddress();
                                    equals = oldAddress.equals(address);
                                    boolean b = NewApplicationDelegator.compareHciName(appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList().get(0), appSubmissionDto.getAppGrpPremisesDtoList().get(i));
                                    amendmentFeeDto.setChangeInHCIName(!b);
                                }
                                amendmentFeeDto.setChangeInLocation(!equals);
                                FeeDto feeDto = appSubmissionService.getGroupAmendAmount(amendmentFeeDto);
                                Double rfcTotal = feeDto.getTotal();
                                total+=rfcTotal;
                                if(equals){
                                    List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList();
                                    if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                                        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                                            appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
                                        }
                                    }
                                }
                                appSubmissionDtoByLicenceId.setGroupLic(groupLic);
                                appSubmissionDtoByLicenceId.setPartPremise(groupLic);
                                appSubmissionDtoByLicenceId.setAmount(rfcTotal);
                                AppGrpPremisesDto appGrpPremisesDto=appGrpPremisesDtoList.get(i);
                                List<AppGrpPremisesDto> appGrpPremisesDtos=new ArrayList<>(1);
                                appGrpPremisesDtos.add(appGrpPremisesDto);
                                if(groupLic){
                                    appGrpPremisesDtos.get(0).setGroupLicenceFlag(licenceDto.getId());
                                }
                                appSubmissionDtoByLicenceId.setAppGrpPremisesDtoList(appGrpPremisesDtos);
                                appSubmissionDtoByLicenceId.setAppGrpNo(appGrpNo);
                                appSubmissionDtoByLicenceId.setIsNeedNewLicNo(AppConsts.YES);
                                PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDtoByLicenceId);
                                if (preOrPostInspectionResultDto == null) {
                                    appSubmissionDtoByLicenceId.setPreInspection(true);
                                    appSubmissionDtoByLicenceId.setRequirement(true);
                                } else {
                                    appSubmissionDtoByLicenceId.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
                                    appSubmissionDtoByLicenceId.setRequirement(preOrPostInspectionResultDto.isRequirement());
                                }
                                appSubmissionDtoByLicenceId.setAutoRfc(equals);
                                appEditSelectDto.setPremisesEdit(true);
                                appEditSelectDto.setServiceEdit(false);
                                appEditSelectDto.setDocEdit(false);
                                appSubmissionDtoByLicenceId.setAppEditSelectDto(appEditSelectDto);
                                appSubmissionDtoByLicenceId.setChangeSelectDto(appEditSelectDto);
                                appSubmissionDtoByLicenceId.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                                appSubmissionDtoByLicenceId.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
                                String draftNo = appSubmissionDtoByLicenceId.getDraftNo();
                                if(StringUtil.isEmpty(draftNo)){
                                    appSubmissionService.setDraftNo(appSubmissionDtoByLicenceId);
                                }
                                if(0.0==rfcTotal){
                                    appSubmissionDtoByLicenceId.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                                    appSubmissionDtoByLicenceId.setAutoRfc(true);
                                }else {
                                    appSubmissionDtoByLicenceId.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
                                }
                                appSubmissionDtoByLicenceId.setAppGrpPrimaryDocDtos(null);
                                RequestForChangeMenuDelegator.oldPremiseToNewPremise(appSubmissionDtoByLicenceId);
                                NewApplicationDelegator.premisesDocToSvcDoc(appSubmissionDtoByLicenceId);
                                rfcAppSubmissionDtos.add(appSubmissionDtoByLicenceId);
                            }
                        }
                    }
                }
            }
            String licenceId = appSubmissionDto.getLicenceId();
            renewLicIds.add(licenceId);
            FeeDto feeDto = appSubmissionService.getGroupAmount(appSubmissionDto);
            appSubmissionDto.setLicenseeId(licenseeId);
            //set fee detail
            List<FeeExtDto> detailFeeDtos = feeDto.getDetailFeeDto();
            if(!IaisCommonUtils.isEmpty(detailFeeDtos)){
                appSubmissionDto.setDetailFeeDto(detailFeeDtos.get(0));
            }else{
                log.error(StringUtil.changeForLog("feeDto detailFeeDtos null"));
            }
            Double amount = feeDto.getTotal();
            Double lateFee=0.0;
            for(FeeExtDto feeExtDto : detailFeeDtos){
                Double lateFeeAmoumt = feeExtDto.getLateFeeAmoumt();
                if(lateFeeAmoumt!=null){
                    lateFee+=lateFeeAmoumt;
                }
            }
            appFeeDetailsDto.setAdmentFee(0.0);
            appFeeDetailsDto.setLaterFee(lateFee);
            appFeeDetailsDto.setBaseFee(amount);
            if(!StringUtil.isEmpty(amount)){
                renewTotal+=amount;
                total +=amount;
                appSubmissionDto.setAmount(amount);
                String amountStr = Formatter.formatterMoney(amount);
                appSubmissionDto.setAmountStr(amountStr);
            }
            NewApplicationDelegator.premisesDocToSvcDoc(appSubmissionDto);
        }
        //get licIds under the idNo that not renew
        List<String> notReNewLicIds = IaisCommonUtils.genNewArrayList();
        for(String licId :renewLicIds){
            String idNo = requestForChangeService.getIdNoByLicId(licId);
            List<String> personIds = requestForChangeService.getPersonnelIdsByIdNo(idNo);
            List<LicKeyPersonnelDto> licByPerId = requestForChangeService.getLicKeyPersonnelDtoByPerId(personIds);
            for(LicKeyPersonnelDto dto :licByPerId){
                String licenceId = dto.getLicenceId();
                String liceseeId = dto.getLicenseeId();
                if(!licId.equals(licenceId)&&!notReNewLicIds.contains(licenceId)&&licenseeId.equals(liceseeId)){
                    notReNewLicIds.add(licenceId);
                }
            }
        }
        List<AppSvcRelatedInfoDto> oldAppSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppSvcRelatedInfoDto> newAppSvcRelatedInfoDtoList = appSubmissionDtos.get(0).getAppSvcRelatedInfoDtoList();
        if(oldAppSvcRelatedInfoDtoList.equals(newAppSvcRelatedInfoDtoList)){
            String appGroupNo = requestForChangeService.getApplicationGroupNumber(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
            List<AppSubmissionDto> notReNewappSubmissionDtos = requestForChangeService.getAppSubmissionDtoByLicenceIds(notReNewLicIds);
            List<AppSubmissionDto> rfcPersonnelAppSubmissionDtos = IaisCommonUtils.genNewArrayList();
            for (AppSubmissionDto appSubmissionDto : notReNewappSubmissionDtos) {
                appSubmissionDto.setAppSvcRelatedInfoDtoList(newAppSvcRelatedInfoDtoList);
                appSubmissionDto.setLicenseeId(licenseeId);
                NewApplicationHelper.setSubmissionDtoSvcData(bpc.request, appSubmissionDto);
                appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
                appSubmissionDto.setAppGrpNo(appGroupNo);
                appSubmissionDto.setAmount(0.0);
                appSubmissionDto.setAutoRfc(true);
                String draftNo = appSubmissionDto.getDraftNo();
                if (StringUtil.isEmpty(draftNo)) {
                    appSubmissionService.setDraftNo(appSubmissionDto);
                }
                List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
                if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                        appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
                    }
                }
                //judge is the preInspection
                PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDto);
                if (preOrPostInspectionResultDto == null) {
                    appSubmissionDto.setPreInspection(true);
                    appSubmissionDto.setRequirement(true);
                } else {
                    appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
                    appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
                }
                //set Risk Score
                appSubmissionService.setRiskToDto(appSubmissionDto);
                appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                rfcPersonnelAppSubmissionDtos.add(appSubmissionDto);
            }
            //rfc personnel
            AppSubmissionListDto appSubmissionListPersonnelDto =new AppSubmissionListDto();
            String submissionPersonnelId = generateIdClient.getSeqId().getEntity();
            Long personnelL = System.currentTimeMillis();
            appSubmissionListPersonnelDto.setAppSubmissionDtos(rfcPersonnelAppSubmissionDtos);
            appSubmissionListPersonnelDto.setEventRefNo(personnelL.toString());
            eventBusHelper.submitAsyncRequest(appSubmissionListPersonnelDto,submissionPersonnelId, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                    EventBusConsts.OPERATION_REQUEST_INFORMATION_SUBMIT,personnelL.toString(),bpc.process);
        }

        AppSubmissionListDto appSubmissionListDto =new AppSubmissionListDto();
        String submissionId = generateIdClient.getSeqId().getEntity();
        Long l = System.currentTimeMillis();
        List<AppSubmissionDto> appSubmissionDtos1=IaisCommonUtils.genNewArrayList();

        String totalStr = Formatter.formatterMoney(total);
        //do app submit
        String appGrpNo = appSubmissionDtos.get(0).getAppGrpNo();
        appSubmissionDtos1.addAll(appSubmissionDtos);
  /*      ApplicationGroupDto applicationGroupDto = appSubmissionService.createApplicationDataByWithOutRenewal(renewDto);*/
        if(!rfcAppSubmissionDtos.isEmpty()){
            for(AppSubmissionDto appSubmissionDto : rfcAppSubmissionDtos){
                appSubmissionDto.setAppGrpNo(appGrpNo);
            }
        }
        appFeeDetailsDto.setApplicationNo(appGrpNo+"-01");
        appSubmissionService.saveAppFeeDetails(appFeeDetailsDto);
        appSubmissionDtos1.addAll(rfcAppSubmissionDtos);

        List<AppSubmissionDto> appSubmissionDtos3 = requestForChangeService.saveAppsForRequestForGoupAndAppChangeByList(appSubmissionDtos1);

        appSubmissionListDto.setAppSubmissionDtos(appSubmissionDtos3);
        appSubmissionListDto.setEventRefNo(l.toString());
        eventBusHelper.submitAsyncRequest(appSubmissionListDto,submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_REQUEST_INFORMATION_SUBMIT,l.toString(),bpc.process);

       /* for(AppSubmissionDto appSubmissionDto : appSubmissionDtos ){
            appSubmissionDto.setDraftStatus(AppConsts.COMMON_STATUS_IACTIVE);
            appSubmissionService.doSaveDraft(appSubmissionDto);
        }*/

        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos=IaisCommonUtils.genNewArrayList();
        for(AppSubmissionDto appSubmissionDto : rfcAppSubmissionDtos){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            appSvcRelatedInfoDtoList.get(0).setAmount(appSubmissionDto.getAmount());
            appSvcRelatedInfoDtoList.get(0).setAmountStr(Formatter.formatterMoney(appSubmissionDto.getAmount()));
            appSvcRelatedInfoDtoList.get(0).setApplicationType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
            appSvcRelatedInfoDtoList.get(0).setGroupNo(appSubmissionDto.getAppGrpNo());
            appSvcRelatedInfoDtos.addAll(appSvcRelatedInfoDtoList);
        }
        //set group no.
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtos){
            appSubmissionDto.setAppGrpNo(appGrpNo);
            appSubmissionDto.setAppGrpId(appSubmissionDtos3.get(0).getAppGrpId());
            appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).setAmount(appSubmissionDto.getAmount());
            appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).setAmountStr(Formatter.formatterMoney(appSubmissionDto.getAmount()));
            appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).setApplicationType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).setGroupNo(appSubmissionDto.getAppGrpNo());
            appSubmissionDto.getAppSvcRelatedInfoDtoList().addAll(appSvcRelatedInfoDtos);
        }
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR,renewDto);
        bpc.request.getSession().setAttribute("otherAppSubmissionDtos",appSubmissionDtos1);
    /*    ParamUtil.setRequestAttr(bpc.request,"applicationGroupDto",applicationGroupDto);*/
        ParamUtil.setSessionAttr(bpc.request,"totalStr",totalStr);
        ParamUtil.setSessionAttr(bpc.request,"totalAmount",total);
        //has app submit
        ParamUtil.setSessionAttr(bpc.request,"hasAppSubmit","Y");
    }

    private boolean eqGrpPremises( List<AppGrpPremisesDto> appGrpPremisesDtoList, List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) throws  Exception{
        List<AppGrpPremisesDto> appGrpPremisesDtos = copyAppGrpPremises(appGrpPremisesDtoList);
        List<AppGrpPremisesDto> oldAppGrpPremisesDtos = copyAppGrpPremises(oldAppGrpPremisesDtoList);
        if(!appGrpPremisesDtos.equals(oldAppGrpPremisesDtos)){
            return true;
        }
        return false;
    }

    private List<AppGrpPremisesDto> copyAppGrpPremises( List<AppGrpPremisesDto> appGrpPremisesDtoList) throws Exception{
        List<AppGrpPremisesDto> n=( List<AppGrpPremisesDto>)CopyUtil.copyMutableObject(appGrpPremisesDtoList);
        for(AppGrpPremisesDto appGrpPremisesDto : n){
            appGrpPremisesDto.setLicenceDtos(null);
            if(StringUtil.isEmpty(appGrpPremisesDto.getOffTelNo())){
                appGrpPremisesDto.setOffTelNo(null);
            }
            if(StringUtil.isEmpty(appGrpPremisesDto.getCertIssuedDtStr())){
                appGrpPremisesDto.setCertIssuedDtStr(null);
            }
        }
        return n;
    }
    //prepareAcknowledgement
    public void prepareAcknowledgement(BaseProcessClass bpc)throws Exception{
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto)ParamUtil.getSessionAttr(bpc.request,"INTER_INBOX_USER_INFO");
        String licenseeId = null;
        if(interInboxUserDto!=null){
            licenseeId = interInboxUserDto.getLicenseeId();
        }else{
            log.error(StringUtil.changeForLog("interInboxUserDto null"));
        }
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        String emailAddress = emailAddressesToString(licenseeEmailAddrs);
        ParamUtil.setRequestAttr(bpc.request,"emailAddress",emailAddress);
        ParamUtil.setRequestAttr(bpc.request,"hasDetail","N");
    }

    //doInstructions
    public void doInstructions(BaseProcessClass bpc)throws Exception{
        //go page2
        ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE2);
    }

    //doLicenceReview
    public void doLicenceReview(BaseProcessClass bpc)throws Exception{
        AppSubmissionDto oldAppSubmissionDto  =(AppSubmissionDto)bpc.request.getSession().getAttribute("oldAppSubmissionDto");
        List<AppGrpPremisesDto> oldAppSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        RenewDto renewDto = (RenewDto)ParamUtil.getSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        Map<String, String> stringStringMap = newApplicationDelegator.doPreviewAndSumbit(bpc);
        if(stringStringMap!=null){
            ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE2);
            return;
        }
        String renewEffectiveDate = ParamUtil.getDate(bpc.request, "renewEffectiveDate");
        if(!StringUtil.isEmpty(renewEffectiveDate)){
            Date date = Formatter.parseDate(renewEffectiveDate);
            renewDto.getAppSubmissionDtos().get(0).setEffectiveDate(date);
            renewDto.getAppSubmissionDtos().get(0).setEffectiveDateStr(renewEffectiveDate);
        }
        if(renewDto!=null){
            List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
            for(AppSubmissionDto appSubmissionDto : appSubmissionDtos){
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                Boolean otherOperation = requestForChangeService.isOtherOperation(appSubmissionDto.getLicenceId());
                if(!otherOperation){
                    ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE2);
                    return;
                }
                boolean flag= eqGrpPremises(appGrpPremisesDtoList,oldAppSubmissionDtoAppGrpPremisesDtoList);
                if(flag){
                    List<ApplicationDto> appByLicIdAndExcludeNew = requestForChangeService.getAppByLicIdAndExcludeNew(appSubmissionDto.getLicenceId());
                    if(!IaisCommonUtils.isEmpty(appByLicIdAndExcludeNew)){
                        ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE2);
                        return;
                    }
                    for(int i=0;i<appGrpPremisesDtoList.size();i++){
                        List<LicenceDto> licenceDtos =(List<LicenceDto>) bpc.request.getSession().getAttribute("selectLicence"+i);
                        if(licenceDtos!=null){
                            for(LicenceDto licenceDto : licenceDtos){
                                if(flag){
                                    List<ApplicationDto> appByLicIdAndExcludeNewOther = requestForChangeService.getAppByLicIdAndExcludeNew(licenceDto.getId());
                                    if(!IaisCommonUtils.isEmpty(appByLicIdAndExcludeNewOther)){
                                        ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE2);
                                        return;
                                    }
                                }
                                Boolean otherLicenceOperation = requestForChangeService.isOtherOperation(licenceDto.getId());
                                if(!otherLicenceOperation){
                                    ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE2);
                                    return;
                                }
                            }
                        }
                    }
                }
            }
        }
        //go page3
        ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE3);
    }

    //doPayment
    public void doPayment(BaseProcessClass bpc)throws Exception{
        PaymentValidate paymentValidate = new PaymentValidate();
        Map<String, String> errorMap = paymentValidate.validate(bpc.request);
        if(!errorMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE3);
            return;
        }
        String backUrl = "hcsa-licence-web/eservice/INTERNET/MohWithOutRenewal/1/preparatData";
        //set back url
        ParamUtil.setSessionAttr(bpc.request,"backUrl",backUrl);
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        Double totalAmount = (Double)ParamUtil.getSessionAttr(bpc.request,"totalAmount");
        RenewDto renewDto = (RenewDto)ParamUtil.getSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtos){
            appSubmissionDto.setPaymentMethod(payMethod);
        }
        //
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR,renewDto);
        String groupNo = "";
        String appGrpId = "";
        String licenseeId=null;
        if(appSubmissionDtos.size() != 0){
            groupNo = appSubmissionDtos.get(0).getAppGrpNo();
            licenseeId = appSubmissionDtos.get(0).getLicenseeId();
            appGrpId = appSubmissionDtos.get(0).getAppGrpId();
        }
        if(ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)){
            StringBuffer url = new StringBuffer();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/payment-web/eservice/INTERNET/PaymentRequest")
                    .append("?amount=").append(MaskUtil.maskValue("amount",String.valueOf(totalAmount)))
                    .append("&payMethod=").append(MaskUtil.maskValue("payMethod",payMethod))
                    .append("&reqNo=").append(MaskUtil.maskValue("reqNo",groupNo))
                    .append("&backUrl=").append(MaskUtil.maskValue("backUrl",backUrl));


            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            try {
                bpc.request.setAttribute("paymentAmount",totalAmount);

                sendEmail(bpc.request,groupNo,"onlinePayment",licenseeId,totalAmount,"xxxx-xxxx-xxxx");

            }catch (Exception e){
                log.error(e.getMessage(),e);
            }

        }else if(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod) && !StringUtil.isEmpty(appGrpId)){
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS);
            serviceConfigService.updatePaymentStatus(appGrp);
            try {
                sendEmail(bpc.request,groupNo,ApplicationConsts.PAYMENT_METHOD_NAME_GIRO,licenseeId,totalAmount,"xxxx-xxxx-xxxx");

            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
            String txnDt = DateUtil.formatDate(new Date(), "dd/MM/yyyy");
            ParamUtil.setSessionAttr(bpc.request,"txnDt",txnDt);
            ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE4);
        }

    }

    //doAcknowledgement
    public void doAcknowledgement(BaseProcessClass bpc)throws Exception{
        String payMethod = ParamUtil.getString(bpc.request,"payMethod");
        if("Credit".equals(payMethod)){

        }else{
            ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE3);
            return;
        }
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
            String userAgreement = ParamUtil.getString(bpc.request, "verifyInfoCheckbox");
            if(!StringUtil.isEmpty(userAgreement) && AppConsts.YES.equals(userAgreement)){
                ParamUtil.setSessionAttr(bpc.request,"userAgreement",Boolean.TRUE);
            }else{
                ParamUtil.setSessionAttr(bpc.request,"userAgreement",Boolean.FALSE);
            }
        }else if(PAGE2.equals(switch_value)){
            ParamUtil.setRequestAttr(bpc.request,CONTROL_SWITCH,BACK);
            ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,switch_value);
        }else if(EDIT.equals(switch_value)){
            ParamUtil.setRequestAttr(bpc.request,CONTROL_SWITCH,EDIT);
            ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE2);
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
        String editValue = (String) ParamUtil.getString(bpc.request,"EditValue");
        RenewDto renewDto  = (RenewDto) ParamUtil.getSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
        AppSubmissionDto appSubmissionDto = appSubmissionDtos.get(0);
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto()==null? new AppEditSelectDto():appSubmissionDto.getAppEditSelectDto();
        if(!StringUtil.isEmpty(editValue)){
            ParamUtil.setSessionAttr(bpc.request,PREFIXTITLE,"amending");
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
        if(appSubmissionDto != null){
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            appEditSelectDto.setPremisesEdit(true);
            appEditSelectDto.setDocEdit(true);
            appEditSelectDto.setServiceEdit(true);
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = IaisCommonUtils.genNewArrayList();
            List<Map<String,List<AppSvcDisciplineAllocationDto>>> reloadDisciplineAllocationMapList = IaisCommonUtils.genNewArrayList();
            List<List<AppSvcPrincipalOfficersDto>> principalOfficersDtosList = IaisCommonUtils.genNewArrayList();
            List<List<AppSvcPrincipalOfficersDto>> deputyPrincipalOfficersDtosList = IaisCommonUtils.genNewArrayList();
            List<String> serviceNames = IaisCommonUtils.genNewArrayList();
            if(!appSubmissionDto.getAppSvcRelatedInfoDtoList().isEmpty()) {
                String serviceName = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName();
                //add to service name list
                serviceNames.add(serviceName);
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
                String svcId = hcsaServiceDto.getId();
                Map<String, List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = NewApplicationHelper.getDisciplineAllocationDtoList(appSubmissionDto, svcId);
                reloadDisciplineAllocationMapList.add(reloadDisciplineAllocationMap);

                //set AppSvcRelatedInfoDtoList chkName
                List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = serviceConfigService.loadLaboratoryDisciplines(svcId);
                NewApplicationHelper.setLaboratoryDisciplinesInfo(appSubmissionDto, hcsaSvcSubtypeOrSubsumedDtos);
                AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
                appSvcRelatedInfoDtoList.add(appSvcRelatedInfoDto);

                List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                List<AppSvcPrincipalOfficersDto> principalOfficersDtos = IaisCommonUtils.genNewArrayList();
                List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
                if (appSvcPrincipalOfficersDtos != null && !appSvcPrincipalOfficersDtos.isEmpty()) {
                    for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtos) {
                        if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(appSvcPrincipalOfficersDto.getPsnType())) {
                            principalOfficersDtos.add(appSvcPrincipalOfficersDto);
                        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())) {
                            deputyPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                        }
                    }
                }
                principalOfficersDtosList.add(principalOfficersDtos);
                deputyPrincipalOfficersDtosList.add(deputyPrincipalOfficersDtos);
            }

            ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfoList", (Serializable)appSvcRelatedInfoDtoList);
            ParamUtil.setSessionAttr(bpc.request, "reloadDisciplineAllocationMapList", (Serializable)reloadDisciplineAllocationMapList);
            ParamUtil.setSessionAttr(bpc.request, "ReloadPrincipalOfficersList", (Serializable)principalOfficersDtosList);
            ParamUtil.setSessionAttr(bpc.request, "deputyPrincipalOfficersDtosList", (Serializable)deputyPrincipalOfficersDtosList);
        }
        RenewDto renewDto = new RenewDto();
        List<AppSubmissionDto> appSubmissionDtos = IaisCommonUtils.genNewArrayList();
        appSubmissionDtos.add(appSubmissionDto);
        renewDto.setAppSubmissionDtos(appSubmissionDtos);
        ParamUtil.setSessionAttr(bpc.request,RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR, renewDto);
        ParamUtil.setRequestAttr(bpc.request,PAGE_SWITCH,PAGE2);
        ParamUtil.setRequestAttr(bpc.request,RfcConst.FIRSTVIEW,AppConsts.TRUE);
        log.info(StringUtil.changeForLog("the do toPrepareData end ...."));
    }

    public static String emailAddressesToString(List<String> emailAddresses){
        StringBuffer emailAddress = new StringBuffer();
        //String emailAddress = "";
        if(emailAddresses.isEmpty()){
            return emailAddress.toString();
        }

        if(emailAddresses.size() == 1){
            emailAddress.append(emailAddresses.get(0));
//            emailAddress += emailAddresses.get(0);
        }else{
            for(int i = 0;i < emailAddresses.size(); i++){
                if(i == emailAddresses.size() -1){
//                    emailAddress += emailAddresses.get(i);
                    emailAddress.append(emailAddresses.get(i));
                }else{
//                    emailAddress += emailAddresses.get(i) + ", ";
                    emailAddress.append(emailAddresses.get(i)).append(", ");
                }
            }
        }
        return emailAddress.toString();
    }
    //=============================================================================
    //private method
    //=============================================================================

    public void sendEmail(HttpServletRequest request,String applicationNumber, String type, String licenseeId, Double amount,  String GIROAccountNumber ) throws IOException, TemplateException {
        List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(request,AppServicesConsts.HCSASERVICEDTOLIST);
        List<String> serviceNames = new ArrayList<String>();
        for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
            String svcName = hcsaServiceDto.getSvcName();
            if(!StringUtil.isEmpty(svcName)){
                serviceNames.add(svcName);
            }
        }

        MsgTemplateDto msgTemplateDto=null;
        String mesContext ;

        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        String subject="";
        if(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(type)){
            msgTemplateDto = appSubmissionService.getMsgTemplateById("10FF81AF-267D-EA11-BE7A-000C29D29DB0");

            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("serviceLicenceName", serviceNames.toString());
            map.put("GIROAccountNumber",GIROAccountNumber);
            subject="MOH IAIS – Successful Submission of Renewal "+applicationNumber;

        }else if("onlinePayment".equals(type)){
            msgTemplateDto = appSubmissionService.getMsgTemplateById("F77860C0-687D-EA11-BE7A-000C29D29DB0");

            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("serviceLicenceName", serviceNames.toString());
            subject="MOH IAIS – Successful Submission of Renewal "+applicationNumber;

        }else if("successfulOnlinePayment".equals(type)){
            msgTemplateDto = appSubmissionService.getMsgTemplateById("A4CE953C-6A7D-EA11-BE7A-000C29D29DB0");
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("serviceLicenceName", serviceNames.toString());
            subject="MOH IAIS – Successful Submission of Auto Renewal Application "+applicationNumber;

        }else if("emailLink".equals(type)){
            msgTemplateDto = appSubmissionService.getMsgTemplateById("2C775ADE-6B7D-EA11-BE7A-000C29D29DB0");
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("serviceLicenceName", serviceNames);
            map.put("GIROAccountNumber",GIROAccountNumber);

        }
        EmailDto emailDto = new EmailDto();
        if (msgTemplateDto != null){
            mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), map);
            emailDto.setContent(mesContext);
        }
        emailDto.setSender(mailSender);
        emailDto.setSubject(subject);
        emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
        emailDto.setClientQueryCode(applicationNumber);
        appSubmissionService.feSendEmail(emailDto);
    }
}
