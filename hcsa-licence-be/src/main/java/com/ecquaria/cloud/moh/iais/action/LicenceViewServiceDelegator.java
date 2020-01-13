package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * LicenceViewServiceDelegator
 *
 * @author suocheng
 * @date 12/16/2019
 */
@Delegator("licenceViewServiceDelegator")
@Slf4j
public class LicenceViewServiceDelegator {

    private static final String APPSUBMISSIONDTO = "appSubmissionDto";
    private static  final String HCSASERVICEDTO  = "hcsaServiceDto";

    @Autowired
    private LicenceViewService licenceViewService;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private ApplicationViewService applicationViewService;

    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator cleanSession start ...."));
        AuditTrailHelper.auditFunction("hcsa-licence", "hcsa licence View Service");
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,null);
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator cleanSession end ...."));
    }

    /**
     * StartStep: PrepareViewData
     *
     * @param bpc
     * @throws
     */
    public void PrepareViewData(BaseProcessClass bpc) throws Exception{
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator prepareData start ..."));
        ApplicationViewDto applicationViewDto=(ApplicationViewDto) bpc.request.getSession().getAttribute("applicationViewDto");
        AppSubmissionDto appSubmissionDto=null;
        String newCorrelationId="";
        String oldCorrelationId="";
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
        if(appPremisesCorrelationDto!=null){
             newCorrelationId = appPremisesCorrelationDto.getId();
             oldCorrelationId = appPremisesCorrelationDto.getOldCorrelationId();
            String applicationId = appPremisesCorrelationDto.getApplicationId();
            appSubmissionDto = licenceViewService.getAppSubmissionByAppId(applicationId);
        }else {
            String appId = ParamUtil.getString(bpc.request,"appId");
            appSubmissionDto = licenceViewService.getAppSubmissionByAppId(appId);
        }
        String status = appSubmissionDto.getStatus();
        if("APST024".equals(status)){
            //new
            ApplicationDto entity = applicationClient.getApplicationById(appPremisesCorrelationDto.getApplicationId()).getEntity();
            //last
            ApplicationDto applicationDto = applicationClient.getLastApplicationByAppNo(entity).getEntity();
            if(applicationDto!=null){
                AppSubmissionDto appSubmissionByAppId = licenceViewService.getAppSubmissionByAppId(applicationDto.getId());
                if(appSubmissionDto!=null){
                    appSubmissionDto.setOldAppSubmissionDto(appSubmissionByAppId);
                }
            }


        }else if(!StringUtil.isEmpty(newCorrelationId)&&!StringUtil.isEmpty(oldCorrelationId)&&!newCorrelationId.equals(oldCorrelationId)){
            AppPremisesCorrelationDto lastAppPremisesCorrelationDtoById =
                    applicationViewService.getLastAppPremisesCorrelationDtoById(newCorrelationId);

            String applicationId = lastAppPremisesCorrelationDtoById.getApplicationId();

            //new
            ApplicationDto entity = applicationClient.getApplicationById(applicationId).getEntity();
            //last
            ApplicationDto applicationDto = applicationClient.getLastApplicationByAppNo(entity).getEntity();
            AppSubmissionDto appSubmissionByAppId1 = licenceViewService.getAppSubmissionByAppId(applicationDto.getId());
            if(appSubmissionDto!=null){

                appSubmissionDto.setOldAppSubmissionDto(appSubmissionByAppId1);

             }

        }



        /*ApplicationDto entity = applicationClient.getApplicationById(appId).getEntity();
        ApplicationDto applicationDto = applicationClient.getLastApplicationByAppNo(entity).getEntity();
        String id = applicationDto.getId();

        if(id!=null){
            AppSubmissionDto appSubmissionByAppId = licenceViewService.getAppSubmissionByAppId(id);

        }
*/
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            String serviceId = appSvcRelatedInfoDtos.get(0).getServiceId();
            HcsaServiceDto hcsaServiceDto = applicationViewService.getHcsaServiceDtoById(serviceId);
            ParamUtil.setRequestAttr(bpc.request,HCSASERVICEDTO,hcsaServiceDto);
        }
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator prepareData end ..."));
    }

    /**
     * StartStep: doSaveSelect
     *
     * @param bpc
     * @throws
     */
    public void doSaveSelect(BaseProcessClass bpc) throws Exception{
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator doSaveSelect start ..."));
         ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
         String successMsg = null;
         String errorMsg = null;
         if(applicationViewDto !=null){
             AppPremisesCorrelationDto newAppPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
             if(newAppPremisesCorrelationDto!=null){
                 String[] selects = ParamUtil.getStrings(bpc.request,"editCheckbox");
                 if(selects!=null && selects.length > 0){
                     List<String> selectsList = Arrays.asList(selects);

                     AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
                     appEditSelectDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                     appEditSelectDto.setApplicationId(newAppPremisesCorrelationDto.getApplicationId());
                     if(selectsList.contains("premises")){
                         appEditSelectDto.setPremisesEdit(true);
                     }
                     if(selectsList.contains("primary")){
                         appEditSelectDto.setPrimaryEdit(true);
                     }
                     if(selectsList.contains("service")){
                         appEditSelectDto.setServiceEdit(true);
                     }
                     appEditSelectDto.setEditType(ApplicationConsts.APPLICATION_EDIT_TYPE_RFI);
                     appEditSelectDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                     appEditSelectDto = licenceViewService.saveAppEditSelect(appEditSelectDto);
                     appEditSelectDto = licenceViewService.saveAppEditSelectToFe(appEditSelectDto);
                     successMsg = "save success";
                 }else{
                     successMsg = "do not select save success!!!";
                 }
             }else{
               errorMsg = "Data Error!!!";
             }
         }else{
           errorMsg = "Session Time out !!!";
         }

         ParamUtil.setRequestAttr(bpc.request,"successMsg",successMsg);
         ParamUtil.setRequestAttr(bpc.request,"errorMsg",errorMsg);
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator doSaveSelect end ..."));
    }

    /**
     * StartStep: prepareView
     *
     * @param bpc
     * @throws
     */
    public void prepareViewServiceForm(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareView start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,APPSUBMISSIONDTO);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos =  appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            return;
        }
        AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto=null;
        if(oldAppSubmissionDto!=null){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
            oldAppSvcRelatedInfoDto = doAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList, oldAppSubmissionDto);

        }
        /*************************/
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(0);
        appSvcRelatedInfoDto.setOldAppSvcRelatedInfoDto(oldAppSvcRelatedInfoDto);
        List<AppSvcDisciplineAllocationDto> allocationDto = null;
        List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
        if(appSvcRelatedInfoDto != null){
            String serviceId = appSvcRelatedInfoDto.getServiceId();
            hcsaSvcSubtypeOrSubsumedDtos = applicationViewService.getHcsaSvcSubtypeOrSubsumedByServiceId(serviceId);
            allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = new HashMap<>();
        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
            List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocation = new ArrayList<>();
            String hciName = "";
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                hciName = appGrpPremisesDto.getHciName();
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                hciName = appGrpPremisesDto.getConveyanceVehicleNo();
            }

            if(!StringUtil.isEmpty(hciName) && allocationDto !=null && allocationDto.size()>0 ){
                for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:allocationDto){
                    List<AppSvcChckListDto> appSvcChckListDtoList = null;
                    if(hciName.equals(appSvcDisciplineAllocationDto.getPremiseVal())){
                        String chkLstId = appSvcDisciplineAllocationDto.getChkLstConfId();
                        String idNo = appSvcDisciplineAllocationDto.getIdNo();
                        //set chkLstName
                        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                        if(appSvcLaboratoryDisciplinesDtoList != null && appSvcLaboratoryDisciplinesDtoList.size()>0){
                            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                                if(hciName.equals(appSvcLaboratoryDisciplinesDto.getPremiseVal())){
                                    appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                                }
                            }
                        }
                        if(appSvcChckListDtoList != null && appSvcChckListDtoList.size()>0){
                            for(AppSvcChckListDto appSvcChckListDto:appSvcChckListDtoList){
                                HcsaSvcSubtypeOrSubsumedDto  hcsaSvcSubtypeOrSubsumedDto = getHcsaSvcSubtypeOrSubsumedDtoById(hcsaSvcSubtypeOrSubsumedDtos,appSvcChckListDto.getChkLstConfId());
                                if(hcsaSvcSubtypeOrSubsumedDto!=null){
                                    appSvcChckListDto.setChkName(hcsaSvcSubtypeOrSubsumedDto.getName());
                                }
                                if(chkLstId.equals(appSvcChckListDto.getChkLstConfId())){
                                    appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                                }
                            }
                        }
                        //set selCgoName
                        List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                        if(appSvcCgoDtoList != null && appSvcCgoDtoList.size()>0){
                            for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtoList){
                                if(idNo.equals(appSvcCgoDto.getIdNo())){
                                    appSvcDisciplineAllocationDto.setCgoSelName(appSvcCgoDto.getName());
                                }
                            }
                        }
                        reloadDisciplineAllocation.add(appSvcDisciplineAllocationDto);
                    }
                }
            }
            reloadDisciplineAllocationMap.put(hciName, reloadDisciplineAllocation);
        }

        ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
        ParamUtil.setSessionAttr(bpc.request, "reloadDisciplineAllocationMap", (Serializable) reloadDisciplineAllocationMap);

        log.debug(StringUtil.changeForLog("the do prepareView end ...."));
    }

    private HcsaSvcSubtypeOrSubsumedDto getHcsaSvcSubtypeOrSubsumedDtoById(List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos,String id){
        HcsaSvcSubtypeOrSubsumedDto result = null;
        if(!IaisCommonUtils.isEmpty(hcsaSvcSubtypeOrSubsumedDtos)&&!StringUtil.isEmpty(id)){
            for (HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto : hcsaSvcSubtypeOrSubsumedDtos){
                if(id.equals(hcsaSvcSubtypeOrSubsumedDto.getId())){
                    result = hcsaSvcSubtypeOrSubsumedDto;
                    break;
                }else{
                    result = getHcsaSvcSubtypeOrSubsumedDtoById(hcsaSvcSubtypeOrSubsumedDto.getList(),id);
                    if(result!=null){
                     break;
                    }
                }
            }
        }
        return result;
    }


    private  AppSvcRelatedInfoDto doAppSvcRelatedInfoDtoList( List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, AppSubmissionDto appSubmissionDto){
        AppSvcRelatedInfoDto appSvcRelatedInfoDto=new AppSvcRelatedInfoDto();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)){
             appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(0);
            List<AppSvcDisciplineAllocationDto> allocationDto = null;
            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
            if(appSvcRelatedInfoDto != null){
                String serviceId = appSvcRelatedInfoDto.getServiceId();
                hcsaSvcSubtypeOrSubsumedDtos = applicationViewService.getHcsaSvcSubtypeOrSubsumedByServiceId(serviceId);
                allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
            }
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = new HashMap<>();
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
                List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocation = new ArrayList<>();
                String hciName = "";
                if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                    hciName = appGrpPremisesDto.getHciName();
                }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                    hciName = appGrpPremisesDto.getConveyanceVehicleNo();
                }

                if(!StringUtil.isEmpty(hciName) && allocationDto !=null && allocationDto.size()>0 ){
                    for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:allocationDto){
                        List<AppSvcChckListDto> appSvcChckListDtoList = null;
                        if(hciName.equals(appSvcDisciplineAllocationDto.getPremiseVal())){
                            String chkLstId = appSvcDisciplineAllocationDto.getChkLstConfId();
                            String idNo = appSvcDisciplineAllocationDto.getIdNo();
                            //set chkLstName
                            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                            if(appSvcLaboratoryDisciplinesDtoList != null && appSvcLaboratoryDisciplinesDtoList.size()>0){
                                for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                                    if(hciName.equals(appSvcLaboratoryDisciplinesDto.getPremiseVal())){
                                        appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                                    }
                                }
                            }
                            if(appSvcChckListDtoList != null && appSvcChckListDtoList.size()>0){
                                for(AppSvcChckListDto appSvcChckListDto:appSvcChckListDtoList){
                                    HcsaSvcSubtypeOrSubsumedDto  hcsaSvcSubtypeOrSubsumedDto = getHcsaSvcSubtypeOrSubsumedDtoById(hcsaSvcSubtypeOrSubsumedDtos,appSvcChckListDto.getChkLstConfId());
                                    if(hcsaSvcSubtypeOrSubsumedDto!=null){
                                        appSvcChckListDto.setChkName(hcsaSvcSubtypeOrSubsumedDto.getName());
                                    }
                                    if(chkLstId.equals(appSvcChckListDto.getChkLstConfId())){
                                        appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                                    }
                                }
                            }
                            //set selCgoName
                            List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                            if(appSvcCgoDtoList != null && appSvcCgoDtoList.size()>0){
                                for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtoList){
                                    if(idNo.equals(appSvcCgoDto.getIdNo())){
                                        appSvcDisciplineAllocationDto.setCgoSelName(appSvcCgoDto.getName());
                                    }
                                }
                            }
                            reloadDisciplineAllocation.add(appSvcDisciplineAllocationDto);
                        }
                    }
                }
                reloadDisciplineAllocationMap.put(hciName, reloadDisciplineAllocation);
            }
        }

return  appSvcRelatedInfoDto;
    }
}
