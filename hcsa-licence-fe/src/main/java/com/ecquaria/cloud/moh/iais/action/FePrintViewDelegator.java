package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * @author zixian
 * @date 2021/4/13 17:26
 * @description
 */
@Delegator("fePrintViewDelegator")
@Slf4j
public class FePrintViewDelegator {
    private final static String SESSION_VIEW_SUBMISSONS = "viewSubmissons";
    private final static  String ATTR_PRINT_VIEW = "printView";
    private final static String LICENCE_VIEW="licenceView";
    private final static String RFC_EQHCINAMECHANGE ="RFC_eqHciNameChange";
    private final static String GROUP_RENEW_APP_RFC ="group_renewal_app_rfc";
    @Autowired
    AppSubmissionService appSubmissionService;
    @Autowired
    private ServiceConfigService serviceConfigService;

    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("print view doStart start ..."));
        HttpServletRequest request = bpc.request;
        //remove session
        ParamUtil.setSessionAttr(request,SESSION_VIEW_SUBMISSONS, null);
        // View and Print
        String viewPrint = (String) ParamUtil.getSessionAttr(request,"viewPrint");
        String appType = ParamUtil.getString(request,"appType");
        log.debug("print view appType is {}",appType);
        List<AppSubmissionDto> appSubmissionDtoList = IaisCommonUtils.genNewArrayList();
        String licenceView = request.getParameter(LICENCE_VIEW);
        if(LICENCE_VIEW.equals(licenceView)){
            request.setAttribute(LICENCE_VIEW,LICENCE_VIEW);
        }
        ParamUtil.setRequestAttr(request,"serviceNameMiss",ParamUtil.getRequestString(request,"serviceNameMiss"));
        if(StringUtil.isEmpty(appType)){
            AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, NewApplicationDelegator.APPSUBMISSIONDTO);
            if (appSubmissionDto != null) {
                AppSubmissionDto newAppSubmissionDto = null;
                if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
                    String rfc_eqHciNameChange = request.getParameter(RFC_EQHCINAMECHANGE);
                    log.info(StringUtil.changeForLog("hciNameChange: " + rfc_eqHciNameChange));
                    request.setAttribute(RFC_EQHCINAMECHANGE, rfc_eqHciNameChange);
                    if (RFC_EQHCINAMECHANGE.equals(rfc_eqHciNameChange)) {
                        ParamUtil.setRequestAttr(request, GROUP_RENEW_APP_RFC,
                                ParamUtil.getRequestString(request, GROUP_RENEW_APP_RFC));
                    }
                }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){//inbox view dec
                    RenewDto renewDto=new RenewDto();
                    renewDto.setAppSubmissionDtos(Collections.singletonList(appSubmissionDto));
                    request.setAttribute("renewDto",renewDto);
                } else if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())) {
                    String rfiAppNo = appSubmissionDto.getRfiAppNo();
                    if (!StringUtil.isEmpty(rfiAppNo)) {
                        newAppSubmissionDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
                        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = newAppSubmissionDto.getAppSvcRelatedInfoDtoList();
                        if (appSvcRelatedInfoDtoList != null && !appSvcRelatedInfoDtoList.isEmpty()) {
                            List<AppSvcRelatedInfoDto> newList = IaisCommonUtils.genNewArrayList(1);
                            appSvcRelatedInfoDtoList.stream()
                                    .filter(dto -> rfiAppNo.equals(dto.getAppNo()))
                                    .findAny()
                                    .ifPresent(dto -> newList.add(dto));
                            newAppSubmissionDto.setAppSvcRelatedInfoDtoList(newList);
                        }

                    }
                }
                if (newAppSubmissionDto != null) {
                    appSubmissionDtoList.add(newAppSubmissionDto);
                } else {
                    appSubmissionDtoList.add(appSubmissionDto);
                }
            }
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
            RenewDto renewDto = (RenewDto) ParamUtil.getSessionAttr(request, RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
            if(renewDto != null){
                List<AppSubmissionDto> appSubmissionDtos = renewDto.getAppSubmissionDtos();
                if(!IaisCommonUtils.isEmpty(appSubmissionDtos)){
                    if(appSubmissionDtos.size()==1){
                        if (StringUtil.isEmpty(viewPrint)) {
                            AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionService.getAppDeclarationMessageDto(request, ApplicationConsts.APPLICATION_TYPE_RENEWAL);
                            appSubmissionDtos.get(0).setAppDeclarationMessageDto(appDeclarationMessageDto);
                            appSubmissionDtos.get(0).setAppDeclarationDocDtos(appSubmissionService.getDeclarationFiles(ApplicationConsts.APPLICATION_TYPE_RENEWAL, request));
                            appSubmissionService.initDeclarationFiles(appSubmissionDtos.get(0).getAppDeclarationDocDtos(),ApplicationConsts.APPLICATION_TYPE_RENEWAL,request);
                        }
                    }
                    appSubmissionDtoList.addAll(appSubmissionDtos);
                }
            }
        }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, RfcConst.RFCAPPSUBMISSIONDTO);
            if(appSubmissionDto != null){
                appSubmissionDtoList.add(appSubmissionDto);
            }
        }
        ParamUtil.setRequestAttr(request, "viewPrint", "Y");
        ParamUtil.setSessionAttr(request,SESSION_VIEW_SUBMISSONS, (Serializable) appSubmissionDtoList);
        log.debug(StringUtil.changeForLog("print view doStart end ..."));
    }


    public void prepareData(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("print view prepareData start ..."));
        List<AppSubmissionDto> appSubmissionDtoList = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request,SESSION_VIEW_SUBMISSONS);
        for(AppSubmissionDto appSubmissionDto:appSubmissionDtoList){
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
            //set primary doc
            if(appGrpPrimaryDocDtoList != null && appGrpPrimaryDocDtoList.size() > 0){
                List<HcsaSvcDocConfigDto> primaryDocConfig = serviceConfigService.getPrimaryDocConfigById(appGrpPrimaryDocDtoList.get(0).getSvcComDocId());
                ParamUtil.setRequestAttr(bpc.request,NewApplicationDelegator.PRIMARY_DOC_CONFIG, primaryDocConfig);
                Map<String,List<AppGrpPrimaryDocDto>> reloadPrimaryDocMap = NewApplicationHelper.genPrimaryDocReloadMap(primaryDocConfig,appSubmissionDto.getAppGrpPremisesDtoList(),appGrpPrimaryDocDtoList);
                appSubmissionDto.setMultipleGrpPrimaryDoc(reloadPrimaryDocMap);
            }
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = IaisCommonUtils.genNewArrayList();
            List<AppSvcPrincipalOfficersDto> principalOfficersDtos = IaisCommonUtils.genNewArrayList();
            List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = IaisCommonUtils.genNewArrayList();
            List<AppSvcPrincipalOfficersDto> medAlertPsnDtos = IaisCommonUtils.genNewArrayList();
            List<AppSvcPersonnelDto> appSvcPersonnelDtos = IaisCommonUtils.genNewArrayList();
            List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos = IaisCommonUtils.genNewArrayList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    String svcId = appSvcRelatedInfoDto.getServiceId();
                    //set po dpo
                    List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                    List<AppSvcPrincipalOfficersDto> reloadPoList = IaisCommonUtils.genNewArrayList();
                    List<AppSvcPrincipalOfficersDto> reloadDpoList = IaisCommonUtils.genNewArrayList();
                    if (!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)) {
                        NewApplicationHelper.assignPoDpoDto(appSvcPrincipalOfficersDtos,reloadPoList,reloadDpoList);
                    }
                    appSvcRelatedInfoDto.setReloadPoDtoList(reloadPoList);
                    appSvcRelatedInfoDto.setReloadDpoList(reloadDpoList);
                    //set allocation
                    Map<String, List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = appSubmissionService.getDisciplineAllocationDtoList(appSubmissionDto, svcId);
                    appSvcRelatedInfoDto.setReloadDisciplineAllocationMap(reloadDisciplineAllocationMap);
                    //set step
                    List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemesByServiceId = serviceConfigService.getHcsaServiceStepSchemesByServiceId(svcId);
                    appSvcRelatedInfoDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemesByServiceId);
                    //set svc doc
                    List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(svcId);
                    List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                    if(!IaisCommonUtils.isEmpty(appSvcCgoDtos)){
                        appSvcCgoDtoList.addAll(appSvcCgoDtos);
                    }
                    List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                    if(appSvcPrincipalOfficersDtoList!=null){
                        ListIterator<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoListIterator =
                                appSvcPrincipalOfficersDtoList.listIterator();

                        while (appSvcPrincipalOfficersDtoListIterator.hasNext()){
                            AppSvcPrincipalOfficersDto next = appSvcPrincipalOfficersDtoListIterator.next();
                            if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(next.getPsnType())){
                                principalOfficersDtos.add(next);
                            }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(next.getPsnType())){
                                deputyPrincipalOfficersDtos.add(next);
                            }
                        }
                    }
                    List<AppSvcPrincipalOfficersDto> appSvcMedAlertPsnDtos = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
                    if(!IaisCommonUtils.isEmpty(appSvcMedAlertPsnDtos)){
                        medAlertPsnDtos.addAll(appSvcMedAlertPsnDtos);
                    }
                    List<AppSvcPersonnelDto> appSvcPersonnelDtos1 = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
                    if(!IaisCommonUtils.isEmpty(appSvcPersonnelDtos1)){
                        appSvcPersonnelDtos.addAll(appSvcPersonnelDtos1);
                    }
                    List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos1 = appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();
                    if (!IaisCommonUtils.isEmpty(appSvcClinicalDirectorDtos1)){
                        appSvcClinicalDirectorDtos.addAll(appSvcClinicalDirectorDtos1);
                    }
                    appSvcRelatedInfoDto.setSvcDocConfig(svcDocConfig);
                    Map<String,List<AppSvcDocDto>> reloadSvcDocMap = NewApplicationHelper.genSvcDocReloadMap(svcDocConfig,appSubmissionDto.getAppGrpPremisesDtoList(),appSvcRelatedInfoDto);
                    appSvcRelatedInfoDto.setMultipleSvcDoc(reloadSvcDocMap);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request,SESSION_VIEW_SUBMISSONS, (Serializable) appSubmissionDtoList);
        ParamUtil.setRequestAttr(bpc.request,ATTR_PRINT_VIEW,"test");
        log.debug(StringUtil.changeForLog("print view prepareData end ..."));
    }

    @RequestMapping(value = "/init-print", method = RequestMethod.POST)
    public @ResponseBody String initPrint(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,
                NewApplicationDelegator.APPSUBMISSIONDTO);
        if (appSubmissionDto != null) {
            log.info(StringUtil.changeForLog("init-print"));
            appSubmissionDto.setAppDeclarationMessageDto(
                    appSubmissionService.getAppDeclarationMessageDto(request, appSubmissionDto.getAppType()));
            appSubmissionDto.setAppDeclarationDocDtos(
                    appSubmissionService.getDeclarationFiles(appSubmissionDto.getAppType(), request, true));
            ParamUtil.setSessionAttr(request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
            String verifyInfoCheckbox = ParamUtil.getString(request, "verifyInfoCheckbox");
            appSubmissionDto.setUserAgreement(AppConsts.YES.equals(verifyInfoCheckbox));
            String effectiveDateStr = ParamUtil.getString(request, "rfcEffectiveDate");
            appSubmissionDto.setEffectiveDateStr(effectiveDateStr);
            if (!StringUtil.isEmpty(effectiveDateStr) && CommonValidator.isDate(effectiveDateStr)) {
                appSubmissionDto.setEffectiveDate(DateUtil.parseDate(effectiveDateStr, Formatter.DATE));
            } else {
                appSubmissionDto.setEffectiveDate(null);
            }
        }
        return AppConsts.YES;
    }

}
