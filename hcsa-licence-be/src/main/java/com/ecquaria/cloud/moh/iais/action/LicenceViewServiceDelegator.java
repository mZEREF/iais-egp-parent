package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import java.io.Serializable;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
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
    private static final  String NOT_VIEW="NOT_VIEW";
    @Autowired
    private LicenceViewService licenceViewService;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private ApplicationViewService applicationViewService;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private AppointmentClient appointmentClient;
    @Autowired
    private CessationClient cessationClient;
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
        String rfi = bpc.request.getParameter("rfi");
        if(!StringUtil.isEmpty(rfi)){
            bpc.request.getSession().setAttribute("rfi","rfi");
        }
        bpc.request.getSession().removeAttribute(NOT_VIEW);
        ApplicationViewDto applicationViewDto=(ApplicationViewDto) bpc.request.getSession().getAttribute("applicationViewDto");
        AppSubmissionDto appSubmissionDto;
        String newCorrelationId="";
        String oldCorrelationId="";
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
        if(appPremisesCorrelationDto!=null){

            newCorrelationId = appPremisesCorrelationDto.getId();
            oldCorrelationId = appPremisesCorrelationDto.getOldCorrelationId();
            String applicationId = appPremisesCorrelationDto.getApplicationId();
            appSubmissionDto = licenceViewService.getAppSubmissionByAppId(applicationId);

            ApplicationDto applicationDto = applicationClient.getApplicationById(applicationId).getEntity();
            ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
            String licenseeId = applicationGroupDto.getLicenseeId();
                LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
            bpc.request.setAttribute("newLicenceDto",licenseeDto);
        }else {
            String appId = ParamUtil.getString(bpc.request,"appId");
            appSubmissionDto = licenceViewService.getAppSubmissionByAppId(appId);
        }
        String status = appSubmissionDto.getStatus();


        if(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY.equals(status)){
            //new
            if(appPremisesCorrelationDto!=null){
                ApplicationDto entity = applicationClient.getApplicationById(appPremisesCorrelationDto.getApplicationId()).getEntity();
                String newGrpId = entity.getAppGrpId();
                ApplicationGroupDto newApplicationGroupDto = applicationClient.getAppById(newGrpId).getEntity();
                String newApplicationGroupDtoLicenseeId = newApplicationGroupDto.getLicenseeId();
                LicenseeDto newLicenceDto = organizationClient.getLicenseeDtoById(newApplicationGroupDtoLicenseeId).getEntity();
                bpc.request.setAttribute("newLicenceDto",newLicenceDto);
                //last
                ApplicationDto applicationDto = applicationClient.getLastApplicationByAppNo(entity).getEntity();
                if(applicationDto!=null){
                    String oldGrpId = applicationDto.getAppGrpId();
                    ApplicationGroupDto oldApplicationGroupDto = applicationClient.getAppById(oldGrpId).getEntity();
                    String licenseeId = oldApplicationGroupDto.getLicenseeId();
                    LicenseeDto oldLicenceDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
                    bpc.request.setAttribute("oldLicenceDto",oldLicenceDto);

                    AppSubmissionDto appSubmissionByAppId = licenceViewService.getAppSubmissionByAppId(applicationDto.getId());
                    if(appSubmissionDto!=null){
                        appSubmissionDto.setOldAppSubmissionDto(appSubmissionByAppId);
                    }
                }
            }

        }else if(!StringUtil.isEmpty(newCorrelationId)&&!StringUtil.isEmpty(oldCorrelationId)&&!newCorrelationId.equals(oldCorrelationId)){
            AppPremisesCorrelationDto lastAppPremisesCorrelationDtoById =
                    applicationViewService.getLastAppPremisesCorrelationDtoById(newCorrelationId);

            String applicationId = lastAppPremisesCorrelationDtoById.getApplicationId();

            //new
            ApplicationDto entity = applicationClient.getApplicationById(applicationId).getEntity();
            String newGrpId = entity.getAppGrpId();
            ApplicationGroupDto newApplicationGroupDto = applicationClient.getAppById(newGrpId).getEntity();
            String newApplicationGroupDtoLicenseeId = newApplicationGroupDto.getLicenseeId();
            LicenseeDto newLicenceDto = organizationClient.getLicenseeDtoById(newApplicationGroupDtoLicenseeId).getEntity();
            bpc.request.setAttribute("newLicenceDto",newLicenceDto);


            //last
            ApplicationDto applicationDto = applicationClient.getLastApplicationByAppNo(entity).getEntity();
            if(applicationDto!=null){
                String oldGrpId = applicationDto.getAppGrpId();
                ApplicationGroupDto oldApplicationGroupDto = applicationClient.getAppById(oldGrpId).getEntity();
                String licenseeId = oldApplicationGroupDto.getLicenseeId();
                LicenseeDto oldLicenceDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
                bpc.request.setAttribute("oldLicenceDto",oldLicenceDto);


                AppSubmissionDto appSubmissionByAppId1 = licenceViewService.getAppSubmissionByAppId(applicationDto.getId());
                if(appSubmissionDto!=null){

                    appSubmissionDto.setOldAppSubmissionDto(appSubmissionByAppId1);

                }
            }

        }else {
            ParamUtil.setSessionAttr(bpc.request,NOT_VIEW,NOT_VIEW);

        }


        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            String serviceId = appSvcRelatedInfoDtos.get(0).getServiceId();
            HcsaServiceDto hcsaServiceDto = applicationViewService.getHcsaServiceDtoById(serviceId);
            ParamUtil.setRequestAttr(bpc.request,HCSASERVICEDTO,hcsaServiceDto);
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<PublicHolidayDto> publicHolidayDtos = appointmentClient.getActiveHoliday().getEntity();
        formatDate(appGrpPremisesDtoList,publicHolidayDtos);
        AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
        if(oldAppSubmissionDto!=null){
            List<AppGrpPremisesDto> appGrpPremisesDtoList1 = oldAppSubmissionDto.getAppGrpPremisesDtoList();
            formatDate(appGrpPremisesDtoList1,publicHolidayDtos);
        }

        String serviceId = applicationViewDto.getApplicationDto().getServiceId();
        List<String> list=IaisCommonUtils.genNewArrayList();
        list.add(serviceId);
        List<HcsaServiceStepSchemeDto> entity = hcsaConfigClient.getServiceStepsByServiceIds(list).getEntity();
        List<String> stringList=IaisCommonUtils.genNewArrayList();
        for(HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto : entity){
            stringList.add(hcsaServiceStepSchemeDto.getStepCode());
        }
        bpc.request.getSession().setAttribute("hcsaServiceStepSchemeDtoList",stringList);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        if(appGrpPrimaryDocDtos!=null){
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto: appGrpPrimaryDocDtos) {
                String svcDocId = appGrpPrimaryDocDto.getSvcDocId();
                HcsaSvcDocConfigDto hcsaSvcDocConfigDto = hcsaConfigClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
                if(hcsaSvcDocConfigDto!=null){
                    appGrpPrimaryDocDto.setSvcComDocName( hcsaSvcDocConfigDto.getDocTitle());
                }
            }
        }

        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator prepareData end ..."));
        prepareViewServiceForm(bpc);
    }

    private void formatDate( List<AppGrpPremisesDto> appGrpPremisesDtoList, List<PublicHolidayDto> publicHolidayDtos) throws ParseException {
        for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
            List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDto.getAppPremPhOpenPeriodList();
            for(AppPremPhOpenPeriodDto appPremPhOpenPeriodDto : appPremPhOpenPeriodList){
                Time startFrom = appPremPhOpenPeriodDto.getStartFrom();
                Time endTo = appPremPhOpenPeriodDto.getEndTo();
                Date phDate = appPremPhOpenPeriodDto.getPhDate();
                for(PublicHolidayDto publicHolidayDto : publicHolidayDtos){
                    if(publicHolidayDto.getFromDate().compareTo(phDate)==0){
                        appPremPhOpenPeriodDto.setDayName(publicHolidayDto.getDescription());
                    }
                }
                if(startFrom!=null&&endTo!=null){
                    String string = startFrom.toString();
                    String string1 = endTo.toString();
                    appPremPhOpenPeriodDto.setConvEndToMM(string1.split(":")[1]);
                    appPremPhOpenPeriodDto.setConvStartFromMM(string.split(":")[1]);
                    appPremPhOpenPeriodDto.setConvStartFromHH(string.split(":")[0]);
                    appPremPhOpenPeriodDto.setConvEndToHH(string1.split(":")[0]);
                }
            }
            Time wrkTimeFrom = appGrpPremisesDto.getWrkTimeFrom();
            Time wrkTimeTo = appGrpPremisesDto.getWrkTimeTo();
            if(wrkTimeFrom!=null&&wrkTimeTo!=null){
                String s = wrkTimeFrom.toString();
                String s1 = wrkTimeTo.toString();
                appGrpPremisesDto.setOnsiteEndMM(s1.split(":")[1]);
                appGrpPremisesDto.setOnsiteStartMM(s.split(":")[1]);
                appGrpPremisesDto.setOnsiteStartHH(s.split(":")[0]);
                appGrpPremisesDto.setOnsiteEndHH(s1.split(":")[0]);
            }
        }
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
         String isSuccess = "Y";
         String parentMsg= null;
         String successMsg = null;
         String errorMsg = null;
         if(applicationViewDto !=null){
             AppPremisesCorrelationDto newAppPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
             if(newAppPremisesCorrelationDto!=null){
                 String[] selects = ParamUtil.getStrings(bpc.request,"editCheckbox");
                 if(selects!=null && selects.length > 0){
                     List<String> selectsList = Arrays.asList(selects);
                     AppEditSelectDto  appEditSelectDto = setAppEditSelectDto(newAppPremisesCorrelationDto,selectsList);
                     parentMsg= "<ul>";
                     parentMsg = parentMsg + appEditSelectDto.getParentMsg();
                     parentMsg = parentMsg+"</ul>";
                     appEditSelectDto = licenceViewService.saveAppEditSelect(appEditSelectDto);
                     licenceViewService.saveAppEditSelectToFe(appEditSelectDto);
                     successMsg = "save success";
                     ParamUtil.setSessionAttr(bpc.request,"isSaveRfiSelect",AppConsts.YES);
                 }else{
                     errorMsg = "Please select at least a section";
                 }
             }else{
               errorMsg = "Data Error!!!";
             }
         }else{
           errorMsg = "Session Time out !!!";
         }
         if(!StringUtil.isEmpty(errorMsg)){
             isSuccess = "N";
         }
         ParamUtil.setRequestAttr(bpc.request,"successMsg",successMsg);
         ParamUtil.setRequestAttr(bpc.request,"isSuccess",isSuccess);
         ParamUtil.setRequestAttr(bpc.request,"errorMsg",errorMsg);
         ParamUtil.setRequestAttr(bpc.request,"parentMsg",parentMsg);
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
            oldAppSvcRelatedInfoDto = doAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList, oldAppSubmissionDto,bpc.request);

        }
        /*************************/
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(0);
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList1 = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        if(appSvcLaboratoryDisciplinesDtoList1!=null){
            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList1){
                List<AppSvcChckListDto> appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                if(appSvcChckListDtoList!=null){
                    List<AppSvcChckListDto> appSvcChckListDtos = hcsaConfigClient.getAppSvcChckListDto(appSvcChckListDtoList).getEntity();
                    appSvcLaboratoryDisciplinesDto.setAppSvcChckListDtoList(appSvcChckListDtos);
                }
            }
        }
        appSvcRelatedInfoDto.setOldAppSvcRelatedInfoDto(oldAppSvcRelatedInfoDto);
        List<AppSvcDisciplineAllocationDto> allocationDto = null;
        List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
        String serviceId = appSvcRelatedInfoDto.getServiceId();
        hcsaSvcSubtypeOrSubsumedDtos = applicationViewService.getHcsaSvcSubtypeOrSubsumedByServiceId(serviceId);
        allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();

        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = IaisCommonUtils.genNewHashMap();
        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
            List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocation = IaisCommonUtils.genNewArrayList();
            String  hciName = appGrpPremisesDto.getPremisesIndexNo();

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

                                appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();

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
        List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        if(appSvcDocDtoLit!=null){
            for(AppSvcDocDto appSvcDocDto : appSvcDocDtoLit){
                String svcDocId = appSvcDocDto.getSvcDocId();
                HcsaSvcDocConfigDto entity = hcsaConfigClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
                if(entity!=null){
                    appSvcDocDto.setUpFileName(entity.getDocTitle());
                }
            }
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


    private  AppSvcRelatedInfoDto doAppSvcRelatedInfoDtoList(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, AppSubmissionDto appSubmissionDto, HttpServletRequest request){
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
            Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = IaisCommonUtils.genNewHashMap();
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
                List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocation = IaisCommonUtils.genNewArrayList();
                String  hciName = appGrpPremisesDto.getPremisesIndexNo();
                if(!StringUtil.isEmpty(hciName) && allocationDto !=null && !allocationDto.isEmpty() ){
                    for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:allocationDto){
                        List<AppSvcChckListDto> appSvcChckListDtoList = null;
                        if(hciName.equals(appSvcDisciplineAllocationDto.getPremiseVal())){
                            String chkLstId = appSvcDisciplineAllocationDto.getChkLstConfId();
                            String idNo = appSvcDisciplineAllocationDto.getIdNo();
                            //set chkLstName
                            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                            if(appSvcLaboratoryDisciplinesDtoList != null && !appSvcLaboratoryDisciplinesDtoList.isEmpty()){
                                for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                                    if(hciName.equals(appSvcLaboratoryDisciplinesDto.getPremiseVal())){
                                        appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                                    }
                                }
                            }
                            if(appSvcChckListDtoList != null && !appSvcChckListDtoList.isEmpty()){
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
                            if(appSvcCgoDtoList != null && !appSvcCgoDtoList.isEmpty()){
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
            ParamUtil.setSessionAttr(request, "reloadOld", (Serializable) reloadDisciplineAllocationMap);

        }

        return  appSvcRelatedInfoDto;
    }
    private AppEditSelectDto setAppEditSelectDto(AppPremisesCorrelationDto newAppPremisesCorrelationDto,List<String> selectsList){
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appEditSelectDto.setApplicationId(newAppPremisesCorrelationDto.getApplicationId());
        String  parentMsg = "";
        if(selectsList.contains("premises")){
            appEditSelectDto.setPremisesEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">Premises</li>";
        }
        if(selectsList.contains("doc")){
            appEditSelectDto.setDocEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">Doc</li>";
        }
        if(selectsList.contains("service")){
            appEditSelectDto.setServiceEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">Service</li>";
        }
        if(selectsList.contains("po")){
            appEditSelectDto.setPoEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">PO</li>";
        }
        if(selectsList.contains("dpo")){
            appEditSelectDto.setDpoEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">DPO</li>";
        }
        if(selectsList.contains("medAlert")){
            appEditSelectDto.setMedAlertEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">medAlert</li>";
        }
        appEditSelectDto.setParentMsg(parentMsg);
        appEditSelectDto.setEditType(ApplicationConsts.APPLICATION_EDIT_TYPE_RFI);
        appEditSelectDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        return  appEditSelectDto;
    }
}
