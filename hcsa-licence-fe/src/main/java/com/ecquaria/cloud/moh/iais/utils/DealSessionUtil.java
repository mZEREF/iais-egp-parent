package com.ecquaria.cloud.moh.iais.utils;

import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Wenkang
 * @date 2021/4/14 16:33
 */
@Component
@Slf4j
public class DealSessionUtil {
    @Autowired
    private RequestForChangeService requestForChangeService;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private AppSubmissionService appSubmissionService;
    public static void setSvcDocSession(List<AppSvcDocDto> appSvcDocDtos, String docSessionKey, HttpServletRequest request, List<AppSvcDocDto> maxVersionSvcDocList, String dupForPerson, Set<String> psnIndexList){
        if(appSvcDocDtos != null && appSvcDocDtos.size() > 0){
            if(appSvcDocDtos.size() > 1){
                Collections.sort(appSvcDocDtos,(s1, s2)->s1.getSeqNum().compareTo(s2.getSeqNum()));
            }
            Map<String, File> fileMap = IaisCommonUtils.genNewHashMap();
            Map<String,Map<String,File>> dupPsnFileMap = IaisCommonUtils.genNewHashMap();
            for(AppSvcDocDto appSvcDocDto:appSvcDocDtos){
                int seqNum = appSvcDocDto.getSeqNum();
                String fileMapKey = docSessionKey + seqNum;
                fileMap.put(fileMapKey,null);
                String psnIndex = appSvcDocDto.getPsnIndexNo();
                if(!StringUtil.isEmpty(dupForPerson)){
                    Map<String,File> psnFileMap = dupPsnFileMap.get(psnIndex);
                    if(psnFileMap == null){
                        psnFileMap = IaisCommonUtils.genNewHashMap();
                    }
                    psnFileMap.put(docSessionKey + psnIndex + seqNum ,null);
                    dupPsnFileMap.put(psnIndex,psnFileMap);
                }
            }
            String configId = appSvcDocDtos.get(0).getSvcDocId();
            int initSeqNum = appSvcDocDtos.get(appSvcDocDtos.size()-1).getSeqNum()+1;
            if(!IaisCommonUtils.isEmpty(maxVersionSvcDocList)){
                for(AppSvcDocDto appSvcDocDto:maxVersionSvcDocList){
                    int seqNum = appSvcDocDto.getSeqNum();
                    if(seqNum > initSeqNum  &&  configId.equals(appSvcDocDto.getSvcDocId())){
                        initSeqNum = seqNum;
                    }
                }
            }
            if(!StringUtil.isEmpty(dupForPerson)){
                for(String psnIndex:psnIndexList){
                    Map<String,File> psnFileMap = dupPsnFileMap.get(psnIndex);
                    String psnDocSessionKey =  docSessionKey+ psnIndex;
                    ParamUtil.setSessionAttr(request, HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+psnDocSessionKey, (Serializable) psnFileMap);
                    //ParamUtil.setSessionAttr(request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+psnDocSessionKey+HcsaFileAjaxController.SEESION_FILES_MAP_AJAX_MAX_INDEX,initSeqNum);
                }
            }else{
                ParamUtil.setSessionAttr(request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docSessionKey, (Serializable) fileMap);
                //ParamUtil.setSessionAttr(request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docSessionKey+HcsaFileAjaxController.SEESION_FILES_MAP_AJAX_MAX_INDEX,initSeqNum);
            }
        }
    }
    public static void setPrimaryDocSession(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos, String docSessionKey, HttpServletRequest request, List<AppGrpPrimaryDocDto> maxVersionPrimaryDocList){
        if(appGrpPrimaryDocDtos != null && appGrpPrimaryDocDtos.size() > 0){
            if(appGrpPrimaryDocDtos.size() >1 ){
                Collections.sort(appGrpPrimaryDocDtos,(s1,s2)->s1.getSeqNum().compareTo(s2.getSeqNum()));
            }
            Map<String,File> fileMap = IaisCommonUtils.genNewHashMap();
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtos){
                fileMap.put(docSessionKey+appGrpPrimaryDocDto.getSeqNum(),null);
            }
            String configId = appGrpPrimaryDocDtos.get(0).getSvcComDocId();
            int initSeqNum = appGrpPrimaryDocDtos.get(appGrpPrimaryDocDtos.size()-1).getSeqNum()+1;
            if(!IaisCommonUtils.isEmpty(maxVersionPrimaryDocList)){
                for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:maxVersionPrimaryDocList){
                    int seqNum = appGrpPrimaryDocDto.getSeqNum();
                    if(seqNum > initSeqNum  &&  configId.equals(appGrpPrimaryDocDto.getSvcComDocId())){
                        initSeqNum = seqNum;
                    }
                }
            }

            ParamUtil.setSessionAttr(request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docSessionKey, (Serializable) fileMap);
            //ParamUtil.setSessionAttr(request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docSessionKey+HcsaFileAjaxController.SEESION_FILES_MAP_AJAX_MAX_INDEX,initSeqNum);
        }
    }
    public void initSession(BaseProcessClass bpc) throws CloneNotSupportedException {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO);
        if (appSubmissionDto == null) {
            appSubmissionDto = new AppSubmissionDto();
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            List<AppGrpPremisesDto> appGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            appGrpPremisesDtoList.add(appGrpPremisesDto);
            appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = IaisCommonUtils.genNewArrayList();
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = null;
            for (HcsaServiceDto svc : hcsaServiceDtos) {
                appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                appSvcRelatedInfoDto.setServiceId(svc.getId());
                appSvcRelatedInfoDto.setServiceCode(svc.getSvcCode());
                appSvcRelatedInfoDto.setServiceType(svc.getSvcType());
                appSvcRelatedInfoDto.setServiceName(svc.getSvcName());
                appSvcRelatedInfoDtoList.add(appSvcRelatedInfoDto);
            }
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
            //set licseeId and psn drop down
            setLicseeAndPsnDropDown(appSubmissionDto, bpc);
        } else {
            String appType = appSubmissionDto.getAppType();
            boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
            //set svc info,this fun will set oldAppSubmission
            appSubmissionDto = NewApplicationHelper.setSubmissionDtoSvcData(bpc.request, appSubmissionDto);
            //Object rfi = ParamUtil.getSessionAttr(bpc.request, REQUESTINFORMATIONCONFIG);
            //rfi just show one service
            if (isRfi) {
                List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
                List<HcsaServiceDto> oneHcsaServiceDto = IaisCommonUtils.genNewArrayList();
                for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
                    if (hcsaServiceDto.getId().equals(appSubmissionDto.getRfiServiceId())) {
                        oneHcsaServiceDto.add(hcsaServiceDto);
                        break;
                    }
                }
                ParamUtil.setSessionAttr(bpc.request, "rfiHcsaService", (Serializable) hcsaServiceDtos);
                ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) oneHcsaServiceDto);
            }

            //set premises info
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                    appGrpPremisesDto = NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                    List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesDto.getAppPremPhOpenPeriodList();
                    //set ph name
                    NewApplicationHelper.setPhName(appPremPhOpenPeriodDtos);
                    appGrpPremisesDto.setAppPremPhOpenPeriodList(appPremPhOpenPeriodDtos);
                }
            }

            //set licseeId and psn drop down
            setLicseeAndPsnDropDown(appSubmissionDto, bpc);

            Map<String, AppSvcPersonAndExtDto> personMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
                List<HcsaSvcDocConfigDto> primaryDocConfig;
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
                if(isRfi && appGrpPrimaryDocDtos != null && appGrpPrimaryDocDtos.size() > 0){
                    primaryDocConfig = serviceConfigService.getPrimaryDocConfigById(appGrpPrimaryDocDtos.get(0).getSvcComDocId());
                }else{
                    primaryDocConfig = serviceConfigService.getAllHcsaSvcDocs(null);
                }
                ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.PRIMARY_DOC_CONFIG, (Serializable) primaryDocConfig);
                //rfc/renew for primary doc
                List<AppGrpPrimaryDocDto> newGrpPrimaryDocList = appSubmissionService.syncPrimaryDoc(appType,isRfi,appGrpPrimaryDocDtos,primaryDocConfig);
                //set dupForPrem info
                if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
                    if(!IaisCommonUtils.isEmpty(newGrpPrimaryDocList)){
                        String premTye = appGrpPremisesDtos.get(0).getPremisesType();
                        String premVal = appGrpPremisesDtos.get(0).getPremisesIndexNo();
                        for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:newGrpPrimaryDocList){
                            HcsaSvcDocConfigDto docConfig = NewApplicationHelper.getHcsaSvcDocConfigDtoById(primaryDocConfig,appGrpPrimaryDocDto.getSvcComDocId());
                            if(docConfig != null && "1".equals(docConfig.getDupForPrem())){
                                appGrpPrimaryDocDto.setPremisessName(premVal);
                                appGrpPrimaryDocDto.setPremisessType(premTye);
                            }
                        }
                    }
                }
                appSubmissionDto.setAppGrpPrimaryDocDtos(newGrpPrimaryDocList);
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    String currentSvcId = appSvcRelatedInfoDto.getServiceId();
                    List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
                    if (!StringUtil.isEmpty(currentSvcId)) {
                        hcsaSvcSubtypeOrSubsumedDtos = serviceConfigService.loadLaboratoryDisciplines(currentSvcId);
                        //set doc name
                        List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                        List<HcsaSvcDocConfigDto> svcDocConfig = serviceConfigService.getAllHcsaSvcDocs(currentSvcId);
                        NewApplicationHelper.setDocInfo(null, appSvcDocDtos, null, svcDocConfig);
                        //set dupForPrem info for not rfi rfc or renew
                        if(!isRfi &&(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType))){
                            if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                                String premTye = appGrpPremisesDtos.get(0).getPremisesType();
                                String premVal = appGrpPremisesDtos.get(0).getPremisesIndexNo();
                                for(AppSvcDocDto svcDocDto:appSvcDocDtos){
                                    HcsaSvcDocConfigDto docConfig = NewApplicationHelper.getHcsaSvcDocConfigDtoById(svcDocConfig,svcDocDto.getSvcDocId());
                                    if(docConfig != null && "1".equals(docConfig.getDupForPrem())){
                                        svcDocDto.setPremisesVal(premVal);
                                        svcDocDto.setPremisesType(premTye);
                                    }
                                }
                            }
                        }
                        //handle dupForPerson svc doc
                        /*if(!IaisCommonUtils.isEmpty(appSvcDocDtos)){
                            for(AppSvcDocDto svcDocDto:appSvcDocDtos){
                                HcsaSvcDocConfigDto docConfig = getHcsaSvcDocConfigDtoById(svcDocConfig,svcDocDto.getSvcDocId());


                            }
                        }*/
                    }
                    //set AppSvcLaboratoryDisciplinesDto
                    if (!IaisCommonUtils.isEmpty(hcsaSvcSubtypeOrSubsumedDtos)) {
                        NewApplicationHelper.setLaboratoryDisciplinesInfo(appGrpPremisesDtos, appSvcRelatedInfoDto, hcsaSvcSubtypeOrSubsumedDtos);
                    }
                    //set AppSvcDisciplineAllocationDto
                    //NewApplicationHelper.setDisciplineAllocationDtoInfo(appSvcRelatedInfoDto);
                    if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                            || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())
                            || isRfi) {
                        //gen dropdown map
                        String svcCode = appSvcRelatedInfoDto.getServiceCode();
                        List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = NewApplicationHelper.transferCgoToPsnDtoList(appSvcRelatedInfoDto.getAppSvcCgoDtoList());
                        NewApplicationHelper.initSetPsnIntoSelMap(personMap, appSvcCgoDtos, svcCode);
                        //reset dto
                        List<AppSvcPrincipalOfficersDto> newCgoDtoList = IaisCommonUtils.genNewArrayList();
                        for (AppSvcPrincipalOfficersDto item : appSvcCgoDtos) {
                            newCgoDtoList.add(MiscUtil.transferEntityDto(item, AppSvcPrincipalOfficersDto.class));
                        }
                        appSvcRelatedInfoDto.setAppSvcCgoDtoList(newCgoDtoList);
                        NewApplicationHelper.initSetPsnIntoSelMap(personMap, appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList(), svcCode);
                        NewApplicationHelper.initSetPsnIntoSelMap(personMap, appSvcRelatedInfoDto.getAppSvcMedAlertPersonList(), svcCode);
                        NewApplicationHelper.initSetPsnIntoSelMap(personMap, appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList(), svcCode);
                    }
                    //set dpo select flag
                    List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                    if (!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)) {
                        for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtos) {
                            if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())) {
                                appSvcRelatedInfoDto.setDeputyPoFlag(AppConsts.YES);
                                break;
                            }
                        }
                    }
                }
            }
            if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType()) || isRfi) {
                //set oldAppSubmission when rfi,rfc,rene
                if(isRfi){
                    groupLicencePremiseRelationDis(appSubmissionDto);
                }else {
                    AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionService.getAppDeclarationMessageDto(bpc.request, ApplicationConsts.APPLICATION_TYPE_RENEWAL);
                    List<AppDeclarationDocDto> declarationFiles = appSubmissionService.getDeclarationFiles(ApplicationConsts.APPLICATION_TYPE_RENEWAL, bpc.request);
                    appSubmissionDto.setAppDeclarationMessageDto(appDeclarationMessageDto);
                    appSubmissionDto.setAppDeclarationDocDtos(declarationFiles);
                }
                AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.OLDAPPSUBMISSIONDTO, oldAppSubmissionDto);
            } else if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {

                AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
                ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.OLDAPPSUBMISSIONDTO, oldAppSubmissionDto);
            }
        }

        AppEditSelectDto changeSelectDto1 = appSubmissionDto.getChangeSelectDto() == null ? new AppEditSelectDto() : appSubmissionDto.getChangeSelectDto();
        appSubmissionDto.setChangeSelectDto(changeSelectDto1);

        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, "IndexNoCount", 0);

        //reload
        Map<String, AppGrpPrimaryDocDto> initBeforeReloadDocMap = IaisCommonUtils.genNewHashMap();
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.RELOADAPPGRPPRIMARYDOCMAP, (Serializable) initBeforeReloadDocMap);

        //error_msg
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.ERRORMAP_PREMISES, null);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPGRPPRIMARYDOCERRMSGMAP, null);

        //init svc psn conifg
        Map<String, List<HcsaSvcPersonnelDto>> svcConfigInfo = null;
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.SERVICEALLPSNCONFIGMAP, (Serializable) svcConfigInfo);

        //clear primary file session
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.OLDAPPSUBMISSIONDTO);
        List<AppGrpPrimaryDocDto> oldAppGrpPrimaryDocDtos = IaisCommonUtils.genNewArrayList();
        if(oldAppSubmissionDto != null){
            oldAppGrpPrimaryDocDtos = oldAppSubmissionDto.getAppGrpPrimaryDocDtos();
        }
        List<HcsaSvcDocConfigDto> hcsaSvcDocDtos;
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        if(isRfi && oldAppGrpPrimaryDocDtos != null && oldAppGrpPrimaryDocDtos.size() > 0){
            hcsaSvcDocDtos = serviceConfigService.getPrimaryDocConfigById(oldAppGrpPrimaryDocDtos.get(0).getSvcComDocId());
        }else{
            hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(null);
        }
        int initSeqNum = 0;
        String appType = appSubmissionDto.getAppType();
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        if(!IaisCommonUtils.isEmpty(hcsaSvcDocDtos)){
            for(int i =0;i<hcsaSvcDocDtos.size();i++){
                HcsaSvcDocConfigDto hcsaSvcDocConfigDto = hcsaSvcDocDtos.get(i);
                String dupForPrem = hcsaSvcDocConfigDto.getDupForPrem();
                if("0".equals(dupForPrem)){
                    String docKey = i+"primaryDoc";
                    ParamUtil.setSessionAttr(bpc.request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey,null);
                    ParamUtil.setSessionAttr(bpc.request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey+HcsaFileAjaxController.SEESION_FILES_MAP_AJAX_MAX_INDEX,initSeqNum);
                }else if("1".equals(dupForPrem) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                    for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                        String docKey = i+"primaryDoc"+appGrpPremisesDto.getPremisesIndexNo();
                        ParamUtil.setSessionAttr(bpc.request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey,null);
                        ParamUtil.setSessionAttr(bpc.request,HcsaFileAjaxController.SEESION_FILES_MAP_AJAX+docKey+HcsaFileAjaxController.SEESION_FILES_MAP_AJAX_MAX_INDEX,initSeqNum);
                    }
                }
            }
            //set primary file session
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
            if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtoList) && !IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                //premIndex + config
                Map<String,List<AppGrpPrimaryDocDto>> primaryDocMap = IaisCommonUtils.genNewHashMap();
                List<AppGrpPrimaryDocDto> maxVersionPrimaryDocList = IaisCommonUtils.genNewArrayList();
                if(isRfi){
                    if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
                        maxVersionPrimaryDocList = appSubmissionService.getMaxSeqNumPrimaryDocList(oldAppSubmissionDto.getAppGrpId());
                    }else{
                        List<AppSvcDocDto> maxVersionSvcDocList = appSubmissionService.getMaxSeqNumSvcDocList(oldAppSubmissionDto.getAppGrpId());
                        for(AppSvcDocDto appSvcDocDto:maxVersionSvcDocList){
                            AppGrpPrimaryDocDto appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
                            appGrpPrimaryDocDto.setSvcComDocId(appSvcDocDto.getSvcDocId());
                            appGrpPrimaryDocDto.setSeqNum(appSvcDocDto.getSeqNum());
                            maxVersionPrimaryDocList.add(appGrpPrimaryDocDto);
                        }
                    }
                }
                for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtoList){
                    String premIndex = appGrpPrimaryDocDto.getPremisessName();
                    if(StringUtil.isEmpty(premIndex)){
                        premIndex = "";
                    }
                    String docMapKey = premIndex + appGrpPrimaryDocDto.getSvcComDocId();
                    List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = primaryDocMap.get(docMapKey);
                    if(IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
                        appGrpPrimaryDocDtos = IaisCommonUtils.genNewArrayList();
                    }
                    /*if(!StringUtil.isEmpty(appGrpPrimaryDocDto.getFileRepoId())){
                        appGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
                    }*/
                    appGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
                    primaryDocMap.put(docMapKey,appGrpPrimaryDocDtos);
                }

                for(int i =0;i<hcsaSvcDocDtos.size();i++){
                    HcsaSvcDocConfigDto hcsaSvcDocConfigDto = hcsaSvcDocDtos.get(i);
                    String dupForPrem = hcsaSvcDocConfigDto.getDupForPrem();
                    String docMapKey;
                    if("0".equals(dupForPrem)){
                        docMapKey = hcsaSvcDocConfigDto.getId();
                        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = primaryDocMap.get(docMapKey);
                        String docSessionKey = i+"primaryDoc";
                        DealSessionUtil.setPrimaryDocSession(appGrpPrimaryDocDtos,docSessionKey,bpc.request,maxVersionPrimaryDocList);
                    }else if("1".equals(dupForPrem)){
                        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                            docMapKey = appGrpPremisesDto.getPremisesIndexNo() + hcsaSvcDocConfigDto.getId();
                            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = primaryDocMap.get(docMapKey);
                            String docSessionKey = i+"primaryDoc"+appGrpPremisesDto.getPremisesIndexNo();
                            DealSessionUtil.setPrimaryDocSession(appGrpPrimaryDocDtos,docSessionKey,bpc.request,maxVersionPrimaryDocList);
                        }
                    }
                }
            }
        }
        //clear and set svc file session
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                String svcCode = appSvcRelatedInfoDto.getServiceCode();
                List<HcsaSvcDocConfigDto> svcDocConfigList = serviceConfigService.getAllHcsaSvcDocs(appSvcRelatedInfoDto.getServiceId());
                //premIndex + config + svcCode
                Map<String,List<AppSvcDocDto>> svcDocMap = IaisCommonUtils.genNewHashMap();
                List<AppSvcDocDto> maxVersionSvcDocList = IaisCommonUtils.genNewArrayList();
                Set<String> psnIndexList = new HashSet<>(2);
                if(!IaisCommonUtils.isEmpty(appSvcDocDtos)){
                    if(isRfi){
                        maxVersionSvcDocList = appSubmissionService.getMaxSeqNumSvcDocList(oldAppSubmissionDto.getAppGrpId());
                    }

                    for(AppSvcDocDto appSvcDocDto:appSvcDocDtos){
                        String premIndex = appSvcDocDto.getPremisesVal();
                        if(StringUtil.isEmpty(premIndex)){
                            premIndex = "";
                        }

                        String docMapKey = appSvcDocDto.getSvcDocId() + premIndex + svcCode ;
                        List<AppSvcDocDto> appSvcDocDtos1 = svcDocMap.get(docMapKey);
                        if(IaisCommonUtils.isEmpty(appSvcDocDtos1)){
                            appSvcDocDtos1 = IaisCommonUtils.genNewArrayList();
                        }
                        appSvcDocDtos1.add(appSvcDocDto);
                        svcDocMap.put(docMapKey,appSvcDocDtos1);
                        if(!StringUtil.isEmpty(appSvcDocDto.getPsnIndexNo())){
                            psnIndexList.add(appSvcDocDto.getPsnIndexNo());
                        }
                    }
                }
                if(!IaisCommonUtils.isEmpty(svcDocConfigList)){
                    for(int i =0;i<svcDocConfigList.size();i++){
                        HcsaSvcDocConfigDto hcsaSvcDocConfigDto = svcDocConfigList.get(i);
                        String dupForPrem = hcsaSvcDocConfigDto.getDupForPrem();
                        String dupForPerson = hcsaSvcDocConfigDto.getDupForPerson();
                        String docMapKey;
                        if("0".equals(dupForPrem)){
                            docMapKey = hcsaSvcDocConfigDto.getId() + svcCode;
                            List<AppSvcDocDto> appSvcDocDtosList = svcDocMap.get(docMapKey);
                            String docSessionKey = i + "svcDoc" + svcCode;
                            DealSessionUtil.setSvcDocSession(appSvcDocDtosList,docSessionKey,bpc.request,maxVersionSvcDocList,dupForPerson,psnIndexList);
                        }else if("1".equals(dupForPrem)){
                            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                                docMapKey = hcsaSvcDocConfigDto.getId()+ appGrpPremisesDto.getPremisesIndexNo() + svcCode;
                                List<AppSvcDocDto> appSvcDocDtosList = svcDocMap.get(docMapKey);
                                String docSessionKey = i + "svcDoc" + svcCode + appGrpPremisesDto.getPremisesIndexNo();
                                DealSessionUtil.setSvcDocSession(appSvcDocDtosList,docSessionKey,bpc.request,maxVersionSvcDocList,dupForPerson,psnIndexList);
                            }
                        }
                    }
                }
            }
        }

    }
    public void setLicseeAndPsnDropDown(AppSubmissionDto appSubmissionDto, BaseProcessClass bpc) {
        //set licenseeId
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        Map<String, AppSvcPersonAndExtDto> licPersonMap = IaisCommonUtils.genNewHashMap();
        if (loginContext != null) {
            appSubmissionDto.setLicenseeId(loginContext.getLicenseeId());
            //user account
            List<FeUserDto> feUserDtos = requestForChangeService.getFeUserDtoByLicenseeId(loginContext.getLicenseeId());
            ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.CURR_ORG_USER_ACCOUNT, (Serializable) feUserDtos);
            //existing person
            List<PersonnelListQueryDto> licPersonList = requestForChangeService.getLicencePersonnelListQueryDto(loginContext.getLicenseeId());
            licPersonMap = NewApplicationHelper.getLicPsnIntoSelMap(feUserDtos,licPersonList,licPersonMap);
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP, (Serializable) licPersonMap);
            Object draft = ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.DRAFTCONFIG);
            //set data into psnMap
            Map<String, AppSvcPersonAndExtDto> personMap = IaisCommonUtils.genNewHashMap();
            personMap.putAll(licPersonMap);
            if (draft != null) {
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
                    for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                        String svcCode = appSvcRelatedInfoDto.getServiceCode();
                        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                        List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = NewApplicationHelper.transferCgoToPsnDtoList(appSvcCgoDtoList);
                        personMap = NewApplicationHelper.initSetPsnIntoSelMap(personMap, appSvcCgoDtos, svcCode);
                        personMap = NewApplicationHelper.initSetPsnIntoSelMap(personMap, appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList(), svcCode);
                        personMap = NewApplicationHelper.initSetPsnIntoSelMap(personMap, appSvcRelatedInfoDto.getAppSvcMedAlertPersonList(), svcCode);
                        personMap = NewApplicationHelper.initSetPsnIntoSelMap(personMap, appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList(), svcCode);
                        personMap = NewApplicationHelper.initSetPsnIntoSelMap(personMap, appSvcRelatedInfoDto.getAppSvcKeyAppointmentHolderDtoList(), svcCode);
                    }
                }
            }
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP, (Serializable) personMap);
        } else {
            appSubmissionDto.setLicenseeId("");
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP, (Serializable) licPersonMap);
            log.info(StringUtil.changeForLog("user info is empty....."));
        }
    }
    public void groupLicencePremiseRelationDis(AppSubmissionDto appSubmissionDto){
        if(appSubmissionDto==null){
            return;
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcDisciplineAllocationDtoList();
        if(appSvcDisciplineAllocationDtoList==null){
            return;
        }
        List<String> list=new ArrayList<>(appGrpPremisesDtoList.size());
        for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
            list.add(appGrpPremisesDto.getPremisesIndexNo());
        }
        List<AppSvcDisciplineAllocationDto> svcLaboratoryDisciplinesDtos=new ArrayList<>(appSvcDisciplineAllocationDtoList.size());
        for(AppSvcDisciplineAllocationDto svcDisciplineAllocationDto : appSvcDisciplineAllocationDtoList){
            if(!list.contains(svcDisciplineAllocationDto.getPremiseVal())){
                svcLaboratoryDisciplinesDtos.add(svcDisciplineAllocationDto);
            }
        }
        appSvcDisciplineAllocationDtoList.removeAll(svcLaboratoryDisciplinesDtos);
    }
}
