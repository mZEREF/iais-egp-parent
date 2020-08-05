package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.sz.commons.util.FileUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

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

    @Value("${iais.email.sender}")
    private String mailSender;

    @Autowired
    private SystemParamConfig systemParamConfig;

    /**
     *
     * @param bpc
     * @Decription doStart
     */
    public void doStart(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do doStart start ...."));
        String licenceId = ParamUtil.getMaskedString(bpc.request, "licenceId");
        AuditTrailHelper.auditFunction("amend application", "amend application");
        ParamUtil.setSessionAttr(bpc.request, RfcConst.LICENCEID, null);
        ParamUtil.setSessionAttr(bpc.request,"SvcName",null);
        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, null);
        ParamUtil.setSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO,null);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.DODRAFTCONFIG,null);
        ParamUtil.setSessionAttr(bpc.request,"AmendTypeValue", null);
        ParamUtil.setRequestAttr(bpc.request, "premisesIndexNo", null);
        ParamUtil.setSessionAttr(bpc.request, "prepareTranfer", null);
        init(bpc,licenceId);

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
        String amendTypeValue = ParamUtil.getString(bpc.request,"AmendTypeValue");
        if(!StringUtil.isEmpty(amendTypeValue)){
            ParamUtil.setSessionAttr(bpc.request,"AmendTypeValue",amendTypeValue);
        }
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
        String draftNo = ParamUtil.getMaskedString(bpc.request, "DraftNumber");
        loadingDraft(bpc,draftNo);

        log.debug(StringUtil.changeForLog("the do prepareDraft end ...."));
    }


    /**
     *
     * @param bpc
     * @Decription doChoose
     */
    public void doChoose(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do start doChoose ...."));
        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if("back".equals(action)){
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/main-web/eservice/INTERNET/MohInternetInbox")
                    .append("?init_to_page=initLic");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            return;
        }
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
                ParamUtil.setSessionAttr(bpc.request,"UEN",null);
                ParamUtil.setSessionAttr(bpc.request,"premisesInput",null);
                ParamUtil.setSessionAttr(bpc.request,"appPremisesSpecialDocDto",null);
            }else if(AppConsts.YES.equals(amendType)){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "doAmend");
            }
        }
        if(!flag){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prepare");
            ParamUtil.setRequestAttr(bpc.request, "AmendType", amendType);
        }
        ParamUtil.setSessionAttr(bpc.request,"AmendTypeValue", amendType);
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
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO);
        NewApplicationHelper.setPreviewDta(appSubmissionDto,bpc);
        ParamUtil.setSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO,appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request,RfcConst.APPSUBMISSIONDTO,appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request,RfcConst.FIRSTVIEW,AppConsts.TRUE);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(0);
            String svcId = appSvcRelatedInfoDto.getServiceId();
            if(!StringUtil.isEmpty(svcId)){
                List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(svcId);
                appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
                ParamUtil.setRequestAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
            }
        }
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
        log.debug(StringUtil.changeForLog("the do prepareTranfer start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO);
        String serviceName = (String)ParamUtil.getSessionAttr(bpc.request,"SvcName");
        if(!StringUtil.isEmpty(serviceName)){
            appSubmissionDto.setServiceName(serviceName);
        }
        if(!appSubmissionDto.isGroupLic()){
            String premisesIndexNo = appSubmissionDto.getAppGrpPremisesDtoList().get(0).getPremisesIndexNo();
            ParamUtil.setRequestAttr(bpc.request, "premisesIndexNo", premisesIndexNo);
        }
        int maxFile = systemParamConfig.getUploadFileLimit();
        ParamUtil.setSessionAttr(bpc.request, "prepareTranfer", appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, "AppSubmissionDto", appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, "maxFile", 1);
        log.debug(StringUtil.changeForLog("the do prepareTranfer end ...."));
    }


    /**
     * @param bpc
     * @Decription compareChangePercentage
     */
    public void preparePage(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("The preparePage start ..."));
         prepareTranfer(bpc);

        log.info(StringUtil.changeForLog("The preparePage end ..."));
    }
    /**
     * @param bpc
     * @Decription doTransfer
     */
    public void doTransfer(BaseProcessClass bpc)throws CloneNotSupportedException,IOException{
        log.info(StringUtil.changeForLog("The compareChangePercentage start ..."));
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        AppSubmissionDto appSubmissionDto  = (AppSubmissionDto)ParamUtil.getSessionAttr(bpc.request,"prepareTranfer");
        String licenceId = (String) ParamUtil.getSessionAttr(bpc.request, RfcConst.LICENCEID);
        String uen = (String) ParamUtil.getSessionAttr(bpc.request, "UEN");
        String[] selectCheakboxs = null;
//        if(appSubmissionDto.isGroupLic()){
//            selectCheakboxs = ParamUtil.getStrings(bpc.request, "premisesInput");
//        }else {
//            String premisesIndexNo = (String)ParamUtil.getSessionAttr(bpc.request,"premisesIndexNo");
//            selectCheakboxs = new String[]{premisesIndexNo};
//            log.info(StringUtil.changeForLog("The doTransfer premisesIndexNo is -->:"+premisesIndexNo));
//        }

        selectCheakboxs =  (String[])ParamUtil.getSessionAttr(bpc.request,"premisesInput");

        String email = ParamUtil.getString(mulReq,"email");
        String reason = ParamUtil.getString(mulReq,"reason");
        AppPremisesSpecialDocDto appPremisesSpecialDocDto = null;
        CommonsMultipartFile file = (CommonsMultipartFile) mulReq.getFile("selectedFile");
        if(file ==  null || file.getSize() == 0){
          String commDelFlag = ParamUtil.getString(mulReq, "commDelFlag");
          if("N".equals(commDelFlag)){
              appPremisesSpecialDocDto = (AppPremisesSpecialDocDto)ParamUtil.getSessionAttr(bpc.request,"appPremisesSpecialDocDto");
          }
        }
        String[] confirms = ParamUtil.getStrings(mulReq, "confirm");
        log.info(StringUtil.changeForLog("The compareChangePercentage licenceId is -->:"+licenceId));
        log.info(StringUtil.changeForLog("The compareChangePercentage uen is -->:"+uen));
        Map<String,String> error = doValidateEmpty(uen,selectCheakboxs,email);
        boolean isEmail = ValidationUtils.isEmail(email);
        if(file != null && file.getSize() != 0){
            int maxFile = systemParamConfig.getUploadFileLimit();
            String fileType = systemParamConfig.getUploadFileType();
            appPremisesSpecialDocDto = new AppPremisesSpecialDocDto();
            log.info(StringUtil.changeForLog("The maxFile is -->:"+maxFile));
            log.info(StringUtil.changeForLog("The fileType is -->:"+fileType));
            Map<String,Boolean> fileValidate =  ValidationUtils.validateFile(file,Arrays.asList(FileUtils.fileTypeToArray(fileType)),(maxFile * 1024 *1024L));
           if(fileValidate != null && fileValidate.size() >0){
               if(!fileValidate.get("fileType")){
                   error.put("selectedFileError",MessageUtil.replaceMessage("GENERAL_ERR0018",fileType,"fileType"));
               }
               if(!fileValidate.get("fileSize")){
                   error.put("selectedFileError",MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(maxFile),"sizeMax"));
               }
               appPremisesSpecialDocDto.setDocName(file.getOriginalFilename());
           }else{
               String fileRepoGuid = serviceConfigService.saveFileToRepo(file);
               log.info(StringUtil.changeForLog("The fileRepoGuid is -->:"+fileRepoGuid));
               Long size= file.getSize()/1024;
               AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
               appPremisesSpecialDocDto.setDocName(file.getOriginalFilename());
               appPremisesSpecialDocDto.setMd5Code(FileUtil.genMd5FileChecksum(file.getBytes()));
               appPremisesSpecialDocDto.setFileRepoId(fileRepoGuid);
               appPremisesSpecialDocDto.setDocSize(Integer.valueOf(size.toString()));
               appPremisesSpecialDocDto.setSubmitBy(auditTrailDto.getMohUserGuid());
               appPremisesSpecialDocDto.setSubmitDt(new Date());
           }
        }else if(appPremisesSpecialDocDto == null || StringUtil.isEmpty(appPremisesSpecialDocDto.getFileRepoId())){
            error.put("selectedFileError",MessageUtil.replaceMessage("GENERAL_ERR0006","Letter of Undertaking","field"));
        }
        if(!isEmail){
            error.put("emailError","RFC_ERR003");
        }
        if(confirms == null || confirms.length == 0){
            error.put("confirmError","RFC_ERR004");
        }

        if(error.isEmpty()){
            LicenceDto licenceDto = requestForChangeService.getLicenceDtoByLicenceId(licenceId);
            LicenseeDto licenseeDto = requestForChangeService.getLicenseeByUenNo(uen);
            doValidateLojic(uen,error,licenceDto,licenseeDto);
            if(error.isEmpty()){
                String newLicenseeId="";
                if(licenseeDto!=null){
                    newLicenseeId = licenseeDto.getId();
                }
                log.info(StringUtil.changeForLog("The newLicenseeId is -->:"+newLicenseeId));
                appSubmissionDto = requestForChangeService.getAppSubmissionDtoByLicenceId(licenceId);
                appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                appSubmissionDto.setLicenseeId(newLicenseeId);
                appSubmissionDto.setAutoRfc(false);
                FeeDto feeDto = getTransferFee();
                if(feeDto != null){
                    Double amount = feeDto.getTotal();
                    appSubmissionDto.setAmount(amount);
                    log.info(StringUtil.changeForLog("The selectCheakboxs.length is -->:"+selectCheakboxs.length));
                    log.info(StringUtil.changeForLog("The appSubmissionDto.getAppGrpPremisesDtoList().size() is -->:"
                            +appSubmissionDto.getAppGrpPremisesDtoList().size()));
                    if (selectCheakboxs.length == appSubmissionDto.getAppGrpPremisesDtoList().size()) {
                        for (AppGrpPremisesDto appGrpPremisesDto: appSubmissionDto.getAppGrpPremisesDtoList()) {
                            appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
                            appGrpPremisesDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_TRANSFER);
                        }
                    } else {
                        for (AppGrpPremisesDto appGrpPremisesDto : appSubmissionDto.getAppGrpPremisesDtoList()) {
                            String premise = appGrpPremisesDto.getPremisesIndexNo();
                            boolean isSelect  = isSelect(selectCheakboxs,premise);
                            if(isSelect){
                                appGrpPremisesDto.setNeedNewLicNo(Boolean.TRUE);
                                appGrpPremisesDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_TRANSFER);
                            }else {
                                appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
                                appGrpPremisesDto.setGroupLicenceFlag(ApplicationConsts.GROUP_LICENCE_FLAG_ORIGIN);
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

                    String draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
                    log.info(StringUtil.changeForLog("the draftNo -->:"+ draftNo) );
                    appSubmissionDto.setDraftNo(draftNo);

                    //file
                    List<AppPremisesSpecialDocDto> appPremisesSpecialDocDtoList = IaisCommonUtils.genNewArrayList();
                    if(appPremisesSpecialDocDto != null && !StringUtil.isEmpty(appPremisesSpecialDocDto.getFileRepoId())){
                        log.info(StringUtil.changeForLog("the appPremisesSpecialDocDto is not null"));
                        appPremisesSpecialDocDtoList.add(appPremisesSpecialDocDto);
                        appSubmissionDto.setAppPremisesSpecialDocDtos(appPremisesSpecialDocDtoList);
                    }
                    //save the email to the app_group_misc
                    List<AppGroupMiscDto> appGroupMiscDtoList = IaisCommonUtils.genNewArrayList();
                    if(!StringUtil.isEmpty(email)){
                        AppGroupMiscDto appGroupMiscDto = new AppGroupMiscDto();
                        appGroupMiscDto.setMiscType(ApplicationConsts.APP_GROUP_MISC_TYPE_TRANSFER_EMAIL);
                        appGroupMiscDto.setMiscValue(email);
                        appGroupMiscDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                        appGroupMiscDtoList.add(appGroupMiscDto);
                    }
                    if(!StringUtil.isEmpty(reason)){
                        AppGroupMiscDto appGroupMiscDto = new AppGroupMiscDto();
                        appGroupMiscDto.setMiscType(ApplicationConsts.APP_GROUP_MISC_TYPE_TRANSFER_REASON);
                        appGroupMiscDto.setMiscValue(reason);
                        appGroupMiscDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                        appGroupMiscDtoList.add(appGroupMiscDto);
                    }
                    appSubmissionDto.setAppGroupMiscDtos(appGroupMiscDtoList);

                    AppSubmissionDto tranferSub = requestForChangeService.submitChange(appSubmissionDto);
                    LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
                    sendRFCNotification(loginContext,licenceDto,licenseeDto,appSubmissionDto.getLicenseeId(),newLicenseeId,tranferSub);
                    ParamUtil.setSessionAttr(bpc.request, "app-rfc-tranfer", tranferSub);
                    StringBuilder url = new StringBuilder();
                    url.append("https://").append(bpc.request.getServerName())
                            .append(RfcConst.PAYMENTPROCESS);
                    String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                    bpc.response.sendRedirect(tokenUrl);
                }
            }
        }else{
            ParamUtil.setRequestAttr(bpc.request,"errorMsg" , WebValidationHelper.generateJsonStr(error));
            ParamUtil.setRequestAttr(bpc.request,"UEN",uen);
            ParamUtil.setRequestAttr(bpc.request,"email",email);
            ParamUtil.setRequestAttr(bpc.request,"reason",reason);
            ParamUtil.setSessionAttr(bpc.request,"appPremisesSpecialDocDto",appPremisesSpecialDocDto);
            log.info(StringUtil.changeForLog("The selectCheakboxs.toString() is -->:"+ArrayUtils.toString(selectCheakboxs)));
            ParamUtil.setRequestAttr(bpc.request,"selectCheakboxs",ArrayUtils.toString(selectCheakboxs));
        }
        log.info(StringUtil.changeForLog("The compareChangePercentage end ..."));
    }
    /**
     * @param bpc
     * @Decription compareChangePercentage
     */
    public void doValidate(BaseProcessClass bpc) throws CloneNotSupportedException,IOException {
        log.info(StringUtil.changeForLog("The doValidate start ..."));
        AppSubmissionDto appSubmissionDto  = (AppSubmissionDto)ParamUtil.getSessionAttr(bpc.request,"prepareTranfer");
        String licenceId = (String) ParamUtil.getSessionAttr(bpc.request, RfcConst.LICENCEID);
        String uen = ParamUtil.getString(bpc.request, "UEN");
        String[] selectCheakboxs = null;
        if(appSubmissionDto.isGroupLic()){
             selectCheakboxs = ParamUtil.getStrings(bpc.request, "premisesInput");
        }else {
            String premisesIndexNo = (String)ParamUtil.getSessionAttr(bpc.request,"premisesIndexNo");
            selectCheakboxs = new String[]{premisesIndexNo};
            log.info(StringUtil.changeForLog("The doValidate premisesIndexNo is -->:"+premisesIndexNo));
        }

        ParamUtil.setSessionAttr(bpc.request,"UEN",uen);
        ParamUtil.setSessionAttr(bpc.request,"premisesInput",selectCheakboxs);
        log.info(StringUtil.changeForLog("The doValidate licenceId is -->:"+licenceId));
        log.info(StringUtil.changeForLog("The doValidate uen is -->:"+uen));

        Map<String,String> error = doValidateEmpty(uen,selectCheakboxs,"Email");
        if(error.isEmpty()){
            LicenceDto licenceDto = requestForChangeService.getLicenceDtoByLicenceId(licenceId);
            LicenseeDto licenseeDto = requestForChangeService.getLicenseeByUenNo(uen);
            doValidateLojic(uen,error,licenceDto,licenseeDto);
        }

        ParamUtil.setRequestAttr(bpc.request,"errorMsg" , WebValidationHelper.generateJsonStr(error));
        ParamUtil.setRequestAttr(bpc.request,"UEN",uen);
        log.info(StringUtil.changeForLog("The selectCheakboxs.toString() is -->:"+ArrayUtils.toString(selectCheakboxs)));
        ParamUtil.setRequestAttr(bpc.request,"selectCheakboxs",ArrayUtils.toString(selectCheakboxs));
        if(!error.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"isValidate","N");
        }else{
            prepareTranfer(bpc);
            ParamUtil.setRequestAttr(bpc.request,"isValidate","Y");
        }
        log.info(StringUtil.changeForLog("The doValidate end ..."));
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

    private void init(BaseProcessClass bpc, String licenceId) throws CloneNotSupportedException {
        HcsaServiceCacheHelper.receiveServiceMapping();
        ParamUtil.setSessionAttr(bpc.request, RfcConst.LICENCEID, licenceId);

        //load data
        if(!StringUtil.isEmpty(licenceId)){
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByLicenceId(licenceId);
            if(appSubmissionDto == null || IaisCommonUtils.isEmpty(appSubmissionDto.getAppGrpPremisesDtoList()) ||
                    IaisCommonUtils.isEmpty(appSubmissionDto.getAppSvcRelatedInfoDtoList())){
                log.info(StringUtil.changeForLog("appSubmissionDto incomplete , licenceId:"+licenceId));
            }else{
                //set audit trail licNo
                AuditTrailHelper.setAuditLicNo(appSubmissionDto.getLicenceNo());
                appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                String svcName = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceName();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
                List<HcsaServiceDto> hcsaServiceDtoList = IaisCommonUtils.genNewArrayList();
                hcsaServiceDtoList.add(hcsaServiceDto);
                ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
                for(AppGrpPremisesDto appGrpPremisesDto:appSubmissionDto.getAppGrpPremisesDtoList()){
                    NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                }
                //set svcInfo
                NewApplicationHelper.setSubmissionDtoSvcData(bpc.request,appSubmissionDto);
                //set laboratory disciplines info
                if(!IaisCommonUtils.isEmpty(hcsaServiceDtoList)){
                    List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos= serviceConfigService.loadLaboratoryDisciplines(hcsaServiceDtoList.get(0).getId());
                    NewApplicationHelper.setLaboratoryDisciplinesInfo(appSubmissionDto,hcsaSvcSubtypeOrSubsumedDtos);
                    ParamUtil.setSessionAttr(bpc.request, "SvcId",hcsaServiceDtoList.get(0).getId());
                }
                //set doc info
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                    List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
                    List<HcsaSvcDocConfigDto> primaryDocConfig = serviceConfigService.getAllHcsaSvcDocs(null);
                    for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                        String currentSvcId = appSvcRelatedInfoDto.getServiceId();
                        if(!StringUtil.isEmpty(currentSvcId)){
                            List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                            List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(currentSvcId);
                            NewApplicationHelper.setDocInfo(appGrpPrimaryDocDtos,appSvcDocDtos,primaryDocConfig,svcDocConfig);
                        }
                    }
                }
                requestForChangeService.svcDocToPresmise(appSubmissionDto);
                AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDto);
                appSubmissionDto.setOldAppSubmissionDto(oldAppSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request,"SvcName",svcName);
                ParamUtil.setSessionAttr(bpc.request,RfcConst.RFCAPPSUBMISSIONDTO,appSubmissionDto);
            }
        }
    }

    private void loadingDraft(BaseProcessClass bpc,String draftNo){
        log.info(StringUtil.changeForLog("the do loadingDraft start ...."));
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
        amendmentFeeDto.setChangeInHCIName(Boolean.FALSE);
        amendmentFeeDto.setChangeInLicensee(Boolean.TRUE);
        amendmentFeeDto.setChangeInLocation(Boolean.FALSE);
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

    private Map<String,String> doValidateEmpty(String uen,String[] selectCheakboxs,String email){
        Map<String,String> error = IaisCommonUtils.genNewHashMap();
        if(selectCheakboxs == null || selectCheakboxs.length == 0){
            error.put("premisesError","RFC_ERR005");
        }
        if(StringUtil.isEmpty(uen) || uen.length() > 10){
            error.put("uenError","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(email)){
            error.put("emailError","UC_CHKLMD001_ERR001");
        }
        return error;
    }
    private Map<String,String> doValidateLojic(String uen,Map<String,String> error,LicenceDto licenceDto,LicenseeDto licenseeDto){
        if(licenceDto==null){
            error.put("uenError","Licence Error!!!");
        }else{
            if(licenseeDto == null){
                error.put("uenError","RFC_ERR002");
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
                    error.put("uenError","RFC_ERR007");
                }
            }
        }
        return error;
    }

    private void sendRFCNotification(LoginContext loginContext, LicenceDto licenceDto, LicenseeDto licenseeDto, String transfor, String transfee, AppSubmissionDto tranferSub){
        try {
            //Send notification to transferor when licence transfer application is submitted.
            List<String> emailTransfor = IaisEGPHelper.getLicenseeEmailAddrs(transfor);
            List<String> emailTransfee = IaisEGPHelper.getLicenseeEmailAddrs(transfee);
            Map<String,Object> notifyMap = IaisCommonUtils.genNewHashMap();
            notifyMap.put("licensee",licenseeDto.getName());
            notifyMap.put("licence",licenceDto.getLicenceNo() + " " + licenceDto.getSvcName());
            MsgTemplateDto templateDto = appSubmissionService.getMsgTemplateById(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_TRANSFER_APPLICATION);

            sendEmail(tranferSub.getAppGrpNo(),templateDto.getMessageContent(),emailTransfor,notifyMap,templateDto.getTemplateName());
            // Send notification to transferee when licence transfer application is submitted.
            sendEmail(tranferSub.getAppGrpNo(),templateDto.getMessageContent(),emailTransfee,notifyMap,templateDto.getTemplateName());
            //RFC Application - Send notification to admin officers when amendment application is submitted.

            String orgId = loginContext.getOrgId();
            List<String> adminEmailList = requestForChangeService.getAdminEmail(orgId);
            sendEmail(tranferSub.getAppGrpNo(),templateDto.getMessageContent(),adminEmailList,notifyMap,templateDto.getTemplateName());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private void sendEmail(String appId, String templateHtml, List<String> emailAddr, Map<String,Object> map, String subject){
        EmailDto email = new EmailDto();
        String mesContext = null;
        try {
            mesContext = MsgUtil.getTemplateMessageByContent(templateHtml, map);
            email.setReqRefNum(appId);
            email.setSubject(subject);
            email.setContent(mesContext);
            email.setSender(mailSender);
            email.setClientQueryCode(appId);
            email.setReceipts(emailAddr);
            requestForChangeService.sendNotification(email);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
