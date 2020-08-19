package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewHciNameDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final String HCSASERVICEDTO = "hcsaServiceDto";
    private static final String WITHDRAWDTO = "withdrawDto";
    private static final String WITHDRAWDTOLIST = "withdrawDtoList";
    private static final String NOT_VIEW = "NOT_VIEW";
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
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private CessationClient cessationClient;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator cleanSession start ...."));
        AuditTrailHelper.auditFunction("hcsa-licence", "hcsa licence View Service");
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, null);
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator cleanSession end ...."));
    }

    /**
     * StartStep: PrepareViewData
     *
     * @param bpc
     * @throws
     */
    public void PrepareViewData(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator prepareData start ..."));
        String rfi = bpc.request.getParameter("rfi");
        String requestRfi = (String)bpc.request.getAttribute("rfi");
        if (!StringUtil.isEmpty(rfi)||!StringUtil.isEmpty(requestRfi)) {
            bpc.request.setAttribute("rfi", "rfi");
        }
        bpc.request.getSession().removeAttribute(NOT_VIEW);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) bpc.request.getSession().getAttribute("applicationViewDto");
        if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationViewDto.getApplicationDto().getApplicationType())){
            return;
        }
        AppEditSelectDto appEditSelectDto;
        appEditSelectDto=(AppEditSelectDto) bpc.request.getSession().getAttribute("appEditSelectDto");
        if(appEditSelectDto==null){
            appEditSelectDto = applicationViewDto.getAppEditSelectDto();
        }else {
            bpc.request.getSession().setAttribute("pageAppEditSelectDto",applicationViewDto.getAppEditSelectDto());
        }
        log.info(StringUtil.changeForLog(appEditSelectDto+"appEditSelectDto"));
        bpc.request.getSession().setAttribute("appEditSelectDto",appEditSelectDto);
        AppSubmissionDto appSubmissionDto;
        String newCorrelationId = "";
        String oldCorrelationId = "";
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();

        if (appPremisesCorrelationDto != null) {
            String applicationId = appPremisesCorrelationDto.getApplicationId();
            newCorrelationId = appPremisesCorrelationDto.getId();
            oldCorrelationId = appPremisesCorrelationDto.getOldCorrelationId();
            appSubmissionDto = licenceViewService.getAppSubmissionByAppId(applicationId);
            ApplicationDto applicationDto = applicationClient.getApplicationById(applicationId).getEntity();
            if (ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationDto.getApplicationType())){
                List<WithdrawnDto> withdrawnDtoList = IaisCommonUtils.genNewArrayList();
                String appGrpId = applicationDto.getAppGrpId();
                String appId = applicationDto.getId();
                AppPremiseMiscDto premiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(appId).getEntity();
                List<ApplicationDto> applicationDtoList = applicationClient.getAppDtosByAppGrpId(appGrpId).getEntity();
                if (applicationDtoList != null && premiseMiscDto != null){
                    applicationDtoList.forEach(h -> {
                        WithdrawnDto withdrawnDto = new WithdrawnDto();
                        withdrawnDto.setApplicationNo(h.getApplicationNo());

                        withdrawnDto.setWithdrawnReason(premiseMiscDto.getReason());
                        withdrawnDto.setWithdrawnRemarks(premiseMiscDto.getRemarks());
//                        withdrawnDto.setAppPremisesSpecialDocDto();
                        AppPremisesSpecialDocDto appealSpecialDocDto = fillUpCheckListGetAppClient.getAppPremisesSpecialDocByPremId(premiseMiscDto.getAppPremCorreId()).getEntity();
                        if(appealSpecialDocDto != null){
                            ParamUtil.setSessionAttr(bpc.request, "appealSpecialDocDto",appealSpecialDocDto);
                        }
                        withdrawnDtoList.add(withdrawnDto);
                    });
                }

                ParamUtil.setRequestAttr(bpc.request, WITHDRAWDTO, withdrawnDtoList.get(0));
                ParamUtil.setRequestAttr(bpc.request, WITHDRAWDTOLIST, withdrawnDtoList);
            }
            List<String> list = new ArrayList<>(1);
            if (applicationDto.getOriginLicenceId() != null) {
                list.add(applicationDto.getOriginLicenceId());
                LicenceDto licenceDto = hcsaLicenceClient.getLicenceDtoById(applicationDto.getOriginLicenceId()).getEntity();
                if (licenceDto != null) {
                    LicenseeDto oldLicenceDto = organizationClient.getLicenseeDtoById(licenceDto.getLicenseeId()).getEntity();
                    bpc.request.setAttribute("oldLicenceDto", oldLicenceDto);
                }

                AppSubmissionDto entity = hcsaLicenceClient.getAppSubmissionDto(list.get(0)).getEntity();
                if (entity!=null) {
                    List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = entity.getAppSvcRelatedInfoDtoList();
                    if (appSvcRelatedInfoDtoList != null && !appSvcRelatedInfoDtoList.isEmpty()) {
                        String serviceName = appSvcRelatedInfoDtoList.get(0).getServiceName();
                        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
                        appSvcRelatedInfoDtoList.get(0).setServiceId(hcsaServiceDto.getId());
                        premiseTrans(entity);
                        appSubmissionDto.setOldAppSubmissionDto(entity);
                    }

                }
            }

            ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
            String licenseeId = applicationGroupDto.getLicenseeId();
            LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
            bpc.request.setAttribute("newLicenceDto", licenseeDto);
        } else {
            String appId = ParamUtil.getString(bpc.request, "appId");
            appSubmissionDto = licenceViewService.getAppSubmissionByAppId(appId);
        }
        if (appSubmissionDto != null) {
                //new
            if (appPremisesCorrelationDto != null) {
                ApplicationDto entity = applicationClient.getApplicationById(appPremisesCorrelationDto.getApplicationId()).getEntity();
                if(entity.getVersion()>1){
                    String newGrpId = entity.getAppGrpId();
                    ApplicationGroupDto newApplicationGroupDto = applicationClient.getAppById(newGrpId).getEntity();
                    String newApplicationGroupDtoLicenseeId = newApplicationGroupDto.getLicenseeId();
                    LicenseeDto newLicenceDto = organizationClient.getLicenseeDtoById(newApplicationGroupDtoLicenseeId).getEntity();
                    bpc.request.setAttribute("newLicenceDto", newLicenceDto);
                    //last
                    ApplicationDto applicationDto = applicationClient.getLastApplicationByAppNo(entity).getEntity();
                    if (applicationDto != null) {
                        String oldGrpId = applicationDto.getAppGrpId();
                        ApplicationGroupDto oldApplicationGroupDto = applicationClient.getAppById(oldGrpId).getEntity();
                        String licenseeId = oldApplicationGroupDto.getLicenseeId();
                        LicenseeDto oldLicenceDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
                        bpc.request.setAttribute("oldLicenceDto", oldLicenceDto);
                        AppSubmissionDto appSubmissionByAppId = applicationClient.getAppSubmissionByoldAppId(applicationDto.getId()).getEntity();

                        if (appSubmissionDto != null) {
                            appSubmissionDto.setOldAppSubmissionDto(appSubmissionByAppId);
                        }
                    }
                }else if (!StringUtil.isEmpty(newCorrelationId) && !StringUtil.isEmpty(oldCorrelationId) && !newCorrelationId.equals(oldCorrelationId)) {
                    AppPremisesCorrelationDto lastAppPremisesCorrelationDtoById =
                            applicationViewService.getLastAppPremisesCorrelationDtoById(newCorrelationId);
                    String applicationId = lastAppPremisesCorrelationDtoById.getApplicationId();
                    //new
                    String newGrpId = entity.getAppGrpId();
                    ApplicationGroupDto newApplicationGroupDto = applicationClient.getAppById(newGrpId).getEntity();
                    String newApplicationGroupDtoLicenseeId = newApplicationGroupDto.getLicenseeId();
                    LicenseeDto newLicenceDto = organizationClient.getLicenseeDtoById(newApplicationGroupDtoLicenseeId).getEntity();
                    bpc.request.setAttribute("newLicenceDto", newLicenceDto);
                    //last
                    ApplicationDto applicationDto = applicationClient.getLastApplicationByAppNo(entity).getEntity();
                    if (applicationDto != null) {
                        String oldGrpId = applicationDto.getAppGrpId();
                        ApplicationGroupDto oldApplicationGroupDto = applicationClient.getAppById(oldGrpId).getEntity();
                        String licenseeId = oldApplicationGroupDto.getLicenseeId();
                        LicenseeDto oldLicenceDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
                        bpc.request.setAttribute("oldLicenceDto", oldLicenceDto);
                        AppSubmissionDto appSubmissionByAppId1 = licenceViewService.getAppSubmissionByAppId(applicationDto.getId());
                        if (appSubmissionDto != null) {
                            appSubmissionDto.setOldAppSubmissionDto(appSubmissionByAppId1);
                        }
                    }
                } else {
                    ParamUtil.setSessionAttr(bpc.request, NOT_VIEW, NOT_VIEW);
                }

            }
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = null;
        if(appSubmissionDto != null){
            appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        }
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            String serviceId = appSvcRelatedInfoDtos.get(0).getServiceId();
            HcsaServiceDto hcsaServiceDto = applicationViewService.getHcsaServiceDtoById(serviceId);
            ParamUtil.setRequestAttr(bpc.request, HCSASERVICEDTO, hcsaServiceDto);
        }
        if(appSubmissionDto == null){
            return;
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<PublicHolidayDto> publicHolidayDtos = appointmentClient.getActiveHoliday().getEntity();
        if(appGrpPremisesDtoList!=null&&publicHolidayDtos!=null){
            formatDate(appGrpPremisesDtoList, publicHolidayDtos);
        }
        String serviceId = applicationViewDto.getApplicationDto().getServiceId();
        List<String> list = IaisCommonUtils.genNewArrayList();
        list.add(serviceId);
        List<HcsaServiceStepSchemeDto> entity = hcsaConfigClient.getServiceStepsByServiceIds(list).getEntity();
        List<String> stringList = IaisCommonUtils.genNewArrayList();
        Map<String,String> stepNameMap= Maps.newHashMapWithExpectedSize(entity.size());
        for (HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto : entity) {
            stringList.add(hcsaServiceStepSchemeDto.getStepCode());
            stepNameMap.put(hcsaServiceStepSchemeDto.getStepCode(),hcsaServiceStepSchemeDto.getStepName());
        }
        bpc.request.getSession().setAttribute("stepNameMap",(Serializable)stepNameMap);
        bpc.request.getSession().setAttribute("hcsaServiceStepSchemeDtoList", stringList);

        boolean canEidtPremise  = canEidtPremise(applicationViewDto.getApplicationGroupDto().getId());
        ParamUtil.setRequestAttr(bpc.request,"canEidtPremise",canEidtPremise);
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator prepareData end ..."));
        String appType = appSubmissionDto.getAppType();
        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)||ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
             svcDocToPresmise(appSubmissionDto);
            AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
            if(oldAppSubmissionDto!=null){
                svcDocToPresmise(oldAppSubmissionDto);
            }
        }
        try {
            contrastNewAndOld(appSubmissionDto);
        }catch (Exception e){
            log.info("error");
        }
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        if (appGrpPrimaryDocDtos != null) {
            for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos) {
                String svcDocId = appGrpPrimaryDocDto.getSvcDocId();
                HcsaSvcDocConfigDto hcsaSvcDocConfigDto = hcsaConfigClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
                if (hcsaSvcDocConfigDto != null) {
                    appGrpPrimaryDocDto.setSvcComDocName(hcsaSvcDocConfigDto.getDocTitle());
                }
            }
        }
        if(appGrpPremisesDtoList!=null){
            String licenseeId =applicationViewDto.getApplicationGroupDto().getLicenseeId();
            for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
                String premisesType = appGrpPremisesDto.getPremisesType();
                if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)){
                    String hciName = appGrpPremisesDto.getHciName();
                    List<ApplicationViewHciNameDto> applicationViewHciNameDtos = hcsaLicenceClient.getApplicationViewHciNameDtoByHciName(hciName, licenseeId).getEntity();
                    for(ApplicationViewHciNameDto applicationViewHciNameDto : applicationViewHciNameDtos){
                        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(applicationViewHciNameDto.getLicensee()).getEntity();
                        applicationViewHciNameDto.setLicensee(licenseeDto.getName());
                    }
                    appGrpPremisesDto.setApplicationViewHciNameDtos(applicationViewHciNameDtos);

                }
                Map<String,String> map=new HashMap<>(5);
                String blkNo = appGrpPremisesDto.getBlkNo();
                String floorNo = appGrpPremisesDto.getFloorNo();
                String unitNo = appGrpPremisesDto.getUnitNo();
                String postalCode = appGrpPremisesDto.getPostalCode();
                String hciName = appGrpPremisesDto.getHciName();
                String conveyanceVehicleNo = appGrpPremisesDto.getConveyanceVehicleNo();
                map.put("blkNo",blkNo);
                map.put("floorNo",floorNo);
                map.put("unitNo",unitNo);
                map.put("postCode",postalCode);
                map.put("hciName",hciName);
                map.put("vehicleNo",conveyanceVehicleNo);
                map.put("licensee",licenseeId);
                map.put("premisesType",premisesType);
                List<ApplicationViewHciNameDto> applicationViewHciNameDtos = hcsaLicenceClient.getApplicationViewHciNameDtoByAddress(map).getEntity();
                for(ApplicationViewHciNameDto applicationViewHciNameDto : applicationViewHciNameDtos){
                    LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(applicationViewHciNameDto.getLicensee()).getEntity();
                    applicationViewHciNameDto.setLicensee(licenseeDto.getName());
                }
                appGrpPremisesDto.setApplicationViewAddress(applicationViewHciNameDtos);
            }
        }
        if(appSubmissionDto.getOldAppSubmissionDto()!=null){
            premise(appSubmissionDto,appSubmissionDto.getOldAppSubmissionDto());
            premise(appSubmissionDto.getOldAppSubmissionDto(),appSubmissionDto);
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        prepareViewServiceForm(bpc);
    }
    public void svcDocToPresmise(AppSubmissionDto appSubmissionDto) {
        if(appSubmissionDto==null){
            return;
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos=IaisCommonUtils.genNewArrayList();
        List<AppSvcDocDto> appSvcDocDtos=IaisCommonUtils.genNewArrayList();
        if(appSvcDocDtoLit!=null){
            for(AppSvcDocDto appSvcDocDto : appSvcDocDtoLit){
                String svcDocId = appSvcDocDto.getSvcDocId();
                if(StringUtil.isEmpty(svcDocId)){
                    continue;
                }
                HcsaSvcDocConfigDto entity = hcsaConfigClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
                if(entity!=null){
                    String serviceId = entity.getServiceId();
                    if(StringUtil.isEmpty(serviceId)){
                        AppGrpPrimaryDocDto appGrpPrimaryDocDto= new  AppGrpPrimaryDocDto();
                        appGrpPrimaryDocDto.setSvcDocId(svcDocId);
                        appGrpPrimaryDocDto.setSvcComDocId(svcDocId);
                        appGrpPrimaryDocDto.setSvcComDocName(entity.getDocTitle());
                        appGrpPrimaryDocDto.setDocName(appSvcDocDto.getDocName());
                        appGrpPrimaryDocDto.setAppGrpId(appSubmissionDto.getAppGrpId());
                        appGrpPrimaryDocDto.setDocSize(appSvcDocDto.getDocSize());
                        appGrpPrimaryDocDto.setFileRepoId(appSvcDocDto.getFileRepoId());
                        appGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
                        appSvcDocDtos.add(appSvcDocDto);
                    }
                }
            }
            appSvcDocDtoLit.removeAll(appSvcDocDtos);
            for(int i=0;i < appSvcDocDtoLit.size();i++){
                for(int j=0;j < appSvcDocDtoLit.size() && j!=i;j++){
                    if(appSvcDocDtoLit.get(i).getFileRepoId().equals(appSvcDocDtoLit.get(j).getFileRepoId())){
                        appSvcDocDtoLit.remove(appSvcDocDtoLit.get(i));
                        i--;
                        break;
                    }
                }
            }
        }
        if(dtoAppGrpPrimaryDocDtos!=null){
            if(appGrpPrimaryDocDtos.isEmpty()){
                appGrpPrimaryDocDtos.addAll(dtoAppGrpPrimaryDocDtos);
            }else {
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList=IaisCommonUtils.genNewArrayList();
                for(AppGrpPrimaryDocDto appGrpPrimaryDocDto1 : dtoAppGrpPrimaryDocDtos){
                    for(AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos){
                        String svcDocId = appGrpPrimaryDocDto.getSvcDocId();
                        String svcDocId1 = appGrpPrimaryDocDto1.getSvcDocId();
                        if(svcDocId1!=null){
                            if(svcDocId1.equals(svcDocId)){
                                continue;
                            }else {
                                appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto1);
                            }
                        }else if(svcDocId!=null){
                            if(svcDocId.equals(svcDocId1)){
                                continue;
                            }else {
                                appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto1);
                            }
                        }

                    }
                }
                appGrpPrimaryDocDtos.addAll(appGrpPrimaryDocDtoList);
            }
        }
        for(int i=0;i < appGrpPrimaryDocDtos.size();i++){
            for(int j=0;j < appGrpPrimaryDocDtos.size() && j != i;j++){
                if(appGrpPrimaryDocDtos.get(i).getFileRepoId().equals(appGrpPrimaryDocDtos.get(j).getFileRepoId())){
                    appGrpPrimaryDocDtos.remove(appGrpPrimaryDocDtos.get(i));
                    i--;
                    break;
                }
            }
        }
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);

    }

    private boolean canEidtPremise(String appGrpId){
        log.info(StringUtil.changeForLog("The canEidtPremise is start ..."));
        log.info(StringUtil.changeForLog("The canEidtPremise appGrpId is -->:"+appGrpId));
        boolean result = applicationService.getApplicationDtoByGroupIdAndStatus(appGrpId,ApplicationConsts.APPLICATION_STATUS_APPROVED) == null;
        log.info(StringUtil.changeForLog("The canEidtPremise result is -->:" + result));
        log.info(StringUtil.changeForLog("The canEidtPremise is end ..."));
        return result;
    }

    private void formatDate(List<AppGrpPremisesDto> appGrpPremisesDtoList, List<PublicHolidayDto> publicHolidayDtos)  {
        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
            List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDto.getAppPremPhOpenPeriodList();
            for (AppPremPhOpenPeriodDto appPremPhOpenPeriodDto : appPremPhOpenPeriodList) {
                Time startFrom = appPremPhOpenPeriodDto.getStartFrom();
                Time endTo = appPremPhOpenPeriodDto.getEndTo();
                Date phDate = appPremPhOpenPeriodDto.getPhDate();
                for (PublicHolidayDto publicHolidayDto : publicHolidayDtos) {
                    if (publicHolidayDto.getFromDate().compareTo(phDate) == 0) {
                        appPremPhOpenPeriodDto.setDayName(publicHolidayDto.getDescription());
                    }
                }
                if (startFrom != null && endTo != null) {
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
            if (wrkTimeFrom != null && wrkTimeTo != null) {
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
    public void doSaveSelect(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator doSaveSelect start ..."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        String isSuccess = "Y";
        String parentMsg = null;
        String successMsg = null;
        String errorMsg = null;
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if (applicationViewDto != null) {
            AppPremisesCorrelationDto newAppPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
            if (newAppPremisesCorrelationDto != null) {
                String[] selects = ParamUtil.getStrings(bpc.request, "editCheckbox");
                if (selects != null && selects.length > 0) {
                    List<String> selectsList = Arrays.asList(selects);
                    AppEditSelectDto appEditSelectDto = setAppEditSelectDto(newAppPremisesCorrelationDto, selectsList,appSubmissionDto);
                    //pre-inspection show, The following method(licenceViewService.saveAppEditSelect) will clear the field. Please do not move it
                    ParamUtil.setSessionAttr(bpc.request, "rfiUpWindowsCheck", (Serializable) appEditSelectDto.getRfiUpWindowsCheck());
                    parentMsg = "<ul>";
                    parentMsg = parentMsg + appEditSelectDto.getParentMsg();
                    parentMsg = parentMsg + "</ul>";
                    appEditSelectDto = licenceViewService.saveAppEditSelect(appEditSelectDto);
                    applicationViewDto.setAppEditSelectDto(appEditSelectDto);
                    licenceViewService.saveAppEditSelectToFe(appEditSelectDto);
                    successMsg = "save success";
                    ParamUtil.setSessionAttr(bpc.request, "isSaveRfiSelect", AppConsts.YES);
                } else {
                    errorMsg = "Please select at least a section";
                }
            } else {
                errorMsg = "Data Error!!!";
            }
        } else {
            errorMsg = "Session Time out !!!";
        }
        if (!StringUtil.isEmpty(errorMsg)) {
            isSuccess = "N";
            bpc.request.setAttribute("rfi","rfi");
        }
        ParamUtil.setRequestAttr(bpc.request, "successMsg", successMsg);
        ParamUtil.setRequestAttr(bpc.request, "isSuccess", isSuccess);
        ParamUtil.setRequestAttr(bpc.request, "errorMsg", errorMsg);
        ParamUtil.setSessionAttr(bpc.request, "parentMsg", parentMsg);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator doSaveSelect end ..."));
    }


    /**
     * StartStep: prepareView
     *
     * @param bpc
     * @throws
     */
    public void prepareViewServiceForm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareView start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            return;
        }
        AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();

        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = null;
        if (oldAppSubmissionDto != null) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (appSvcRelatedInfoDtoList != null) {
                oldAppSvcRelatedInfoDto = doAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList, oldAppSubmissionDto, bpc.request);
            }
        }
        /*************************/
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(0);
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList1 = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        if (appSvcLaboratoryDisciplinesDtoList1 != null) {
            for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList1) {
                List<AppSvcChckListDto> appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                if (appSvcChckListDtoList != null) {
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
        HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
        if (hcsaServiceDto != null) {
            appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        Map<String, List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = IaisCommonUtils.genNewHashMap();
        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
            List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocation = IaisCommonUtils.genNewArrayList();
            String hciName = appGrpPremisesDto.getPremisesIndexNo();

            if (!StringUtil.isEmpty(hciName) && allocationDto != null && allocationDto.size() > 0) {
                for (AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : allocationDto) {
                    List<AppSvcChckListDto> appSvcChckListDtoList = null;
                    if (hciName.equals(appSvcDisciplineAllocationDto.getPremiseVal())) {
                        String chkLstId = appSvcDisciplineAllocationDto.getChkLstConfId();
                        String idNo = appSvcDisciplineAllocationDto.getIdNo();
                        //set chkLstName
                        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                        if (appSvcLaboratoryDisciplinesDtoList != null && appSvcLaboratoryDisciplinesDtoList.size() > 0) {
                            for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList) {

                                appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();

                            }
                        }
                        if (appSvcChckListDtoList != null && appSvcChckListDtoList.size() > 0) {
                            for (AppSvcChckListDto appSvcChckListDto : appSvcChckListDtoList) {
                                HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto = getHcsaSvcSubtypeOrSubsumedDtoById(hcsaSvcSubtypeOrSubsumedDtos, appSvcChckListDto.getChkLstConfId());
                                if (hcsaSvcSubtypeOrSubsumedDto != null) {
                                    appSvcChckListDto.setChkName(hcsaSvcSubtypeOrSubsumedDto.getName());
                                }
                                if (chkLstId.equals(appSvcChckListDto.getChkLstConfId())) {
                                    if("Please indicate".equals(appSvcChckListDto.getChkName())){
                                        appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getOtherScopeName());
                                    }else {
                                        appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                                    }

                                }
                            }
                        }
                        //set selCgoName
                        List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                        if (appSvcCgoDtoList != null && appSvcCgoDtoList.size() > 0) {
                            for (AppSvcCgoDto appSvcCgoDto : appSvcCgoDtoList) {
                                if (idNo.equals(appSvcCgoDto.getIdNo())) {
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
        if (appSvcDocDtoLit != null) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtoLit) {
                String svcDocId = appSvcDocDto.getSvcDocId();
                if(StringUtil.isEmpty(svcDocId)){
                    continue;
                }
                HcsaSvcDocConfigDto entity = hcsaConfigClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
                if (entity != null) {
                    appSvcDocDto.setUpFileName(entity.getDocTitle());
                }
            }
        }

        ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
        ParamUtil.setSessionAttr(bpc.request, "reloadDisciplineAllocationMap", (Serializable) reloadDisciplineAllocationMap);

        log.debug(StringUtil.changeForLog("the do prepareView end ...."));
    }

    private HcsaSvcSubtypeOrSubsumedDto getHcsaSvcSubtypeOrSubsumedDtoById(List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos, String id) {
        HcsaSvcSubtypeOrSubsumedDto result = null;
        if (!IaisCommonUtils.isEmpty(hcsaSvcSubtypeOrSubsumedDtos) && !StringUtil.isEmpty(id)) {
            for (HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto : hcsaSvcSubtypeOrSubsumedDtos) {
                if (id.equals(hcsaSvcSubtypeOrSubsumedDto.getId())) {
                    result = hcsaSvcSubtypeOrSubsumedDto;
                    break;
                } else {
                    result = getHcsaSvcSubtypeOrSubsumedDtoById(hcsaSvcSubtypeOrSubsumedDto.getList(), id);
                    if (result != null) {
                        break;
                    }
                }
            }
        }
        return result;
    }


    private AppSvcRelatedInfoDto doAppSvcRelatedInfoDtoList(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, AppSubmissionDto appSubmissionDto, HttpServletRequest request) {
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)) {
            appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(0);
            if(appSvcRelatedInfoDto!=null){
                List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList1 = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                if (appSvcLaboratoryDisciplinesDtoList1 != null) {
                    for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList1) {
                        List<AppSvcChckListDto> appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                        if (appSvcChckListDtoList != null) {
                            List<AppSvcChckListDto> appSvcChckListDtos = hcsaConfigClient.getAppSvcChckListDto(appSvcChckListDtoList).getEntity();
                            appSvcLaboratoryDisciplinesDto.setAppSvcChckListDtoList(appSvcChckListDtos);
                        }
                    }
                }
            }
            List<AppSvcDisciplineAllocationDto> allocationDto = null;
            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
            if (appSvcRelatedInfoDto != null) {
                String serviceId = appSvcRelatedInfoDto.getServiceId();
                hcsaSvcSubtypeOrSubsumedDtos = applicationViewService.getHcsaSvcSubtypeOrSubsumedByServiceId(serviceId);
                allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
            }
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            Map<String, List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = IaisCommonUtils.genNewHashMap();
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocation = IaisCommonUtils.genNewArrayList();
                String hciName = appGrpPremisesDto.getPremisesIndexNo();
                if (!StringUtil.isEmpty(hciName) && allocationDto != null && !allocationDto.isEmpty()) {
                    for (AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : allocationDto) {
                        List<AppSvcChckListDto> appSvcChckListDtoList = null;
                        if (hciName.equals(appSvcDisciplineAllocationDto.getPremiseVal())) {
                            String chkLstId = appSvcDisciplineAllocationDto.getChkLstConfId();
                            String idNo = appSvcDisciplineAllocationDto.getIdNo();
                            //set chkLstName
                            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                            if (appSvcLaboratoryDisciplinesDtoList != null && !appSvcLaboratoryDisciplinesDtoList.isEmpty()) {
                                for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList) {
                                    if (hciName.equals(appSvcLaboratoryDisciplinesDto.getPremiseVal())) {
                                        appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                                    }
                                }
                            }
                            if (appSvcChckListDtoList != null && !appSvcChckListDtoList.isEmpty()) {
                                for (AppSvcChckListDto appSvcChckListDto : appSvcChckListDtoList) {
                                    HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto = getHcsaSvcSubtypeOrSubsumedDtoById(hcsaSvcSubtypeOrSubsumedDtos, appSvcChckListDto.getChkLstConfId());
                                    if (hcsaSvcSubtypeOrSubsumedDto != null) {
                                        appSvcChckListDto.setChkName(hcsaSvcSubtypeOrSubsumedDto.getName());
                                    }
                                    if (chkLstId.equals(appSvcChckListDto.getChkLstConfId())) {
                                        if("Please indicate".equals(appSvcChckListDto.getChkName())){
                                            appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getOtherScopeName());
                                        }else {
                                            appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                                        }

                                    }
                                }
                            }
                            //set selCgoName
                            List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                            if (appSvcCgoDtoList != null && !appSvcCgoDtoList.isEmpty()) {
                                for (AppSvcCgoDto appSvcCgoDto : appSvcCgoDtoList) {
                                    if (idNo.equals(appSvcCgoDto.getIdNo())) {
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

        return appSvcRelatedInfoDto;
    }

    private AppEditSelectDto setAppEditSelectDto(AppPremisesCorrelationDto newAppPremisesCorrelationDto, List<String> selectsList, AppSubmissionDto appSubmissionDto) {
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appEditSelectDto.setApplicationId(newAppPremisesCorrelationDto.getApplicationId());
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        String serviceName = "";
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            String serviceId = appSvcRelatedInfoDtos.get(0).getServiceId();
            HcsaServiceDto hcsaServiceDto = applicationViewService.getHcsaServiceDtoById(serviceId);
            serviceName = hcsaServiceDto.getSvcName();
        }
        String parentMsg = "";
        List<String> rfiUpWindowsCheck = IaisCommonUtils.genNewArrayList();
        if (selectsList.contains("premises")) {
            appEditSelectDto.setPremisesEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">Premises</li>";
            rfiUpWindowsCheck.add("Premises");
        }
        if (selectsList.contains("primary")) {
            appEditSelectDto.setDocEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">Primary Documents</li>";
            rfiUpWindowsCheck.add("Primary Documents");
        }
        if (selectsList.contains("service")) {
            appEditSelectDto.setServiceEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">Service Related Information - " + serviceName+ "</li>";
            rfiUpWindowsCheck.add("Service Related Information - " + serviceName);
        }
        if (selectsList.contains("po")) {
            appEditSelectDto.setPoEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">PO</li>";
            rfiUpWindowsCheck.add("PO");
        }
        if (selectsList.contains("dpo")) {
            appEditSelectDto.setDpoEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">DPO</li>";
            rfiUpWindowsCheck.add("DPO");
        }
        if (selectsList.contains("medAlert")) {
            appEditSelectDto.setMedAlertEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">medAlert</li>";
            rfiUpWindowsCheck.add("medAlert");
        }
        appEditSelectDto.setParentMsg(parentMsg);
        appEditSelectDto.setEditType(ApplicationConsts.APPLICATION_EDIT_TYPE_RFI);
        appEditSelectDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        if(IaisCommonUtils.isEmpty(rfiUpWindowsCheck)){
            appEditSelectDto.setRfiUpWindowsCheck(null);
        } else {
            appEditSelectDto.setRfiUpWindowsCheck(rfiUpWindowsCheck);
        }
        return appEditSelectDto;
    }

    private void contrastNewAndOld(AppSubmissionDto appSubmissionDto) {
       AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
        if (oldAppSubmissionDto == null) {
            return;
        }
         List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        if (appGrpPremisesDtoList.size() < oldAppGrpPremisesDtoList.size()) {
            creatNewPremise(appGrpPremisesDtoList,oldAppGrpPremisesDtoList);
        }else if(oldAppGrpPremisesDtoList.size() < appGrpPremisesDtoList.size()){
            creatNewPremise(oldAppGrpPremisesDtoList,appGrpPremisesDtoList);
        }

      List<AppGrpPrimaryDocDto> oldAppGrpPrimaryDocDtos = oldAppSubmissionDto.getAppGrpPrimaryDocDtos();
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        if (IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos) && oldAppGrpPrimaryDocDtos != null) {
            appGrpPrimaryDocDtos = new ArrayList<>(oldAppGrpPrimaryDocDtos.size());
            for (int i = 0; i < oldAppGrpPrimaryDocDtos.size(); i++) {
                AppGrpPrimaryDocDto appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
                String svcDocId = oldAppGrpPrimaryDocDtos.get(i).getSvcDocId();
                if(svcDocId!=null){
                    appGrpPrimaryDocDto.setSvcDocId(svcDocId);
                }else {
                    appGrpPrimaryDocDto.setSvcDocId(oldAppGrpPrimaryDocDtos.get(i).getSvcComDocId());
                }
                appGrpPrimaryDocDto.setDocName("-");
                appGrpPrimaryDocDto.setDocSize(0);
                appGrpPrimaryDocDto.setSvcComDocName(oldAppGrpPrimaryDocDtos.get(i).getSvcComDocName());
                appGrpPrimaryDocDto.setFileRepoId(oldAppGrpPrimaryDocDtos.get(i).getFileRepoId());
                appGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
            }
        } else if (appGrpPrimaryDocDtos != null && oldAppGrpPrimaryDocDtos != null) {
            int size = appGrpPrimaryDocDtos.size();
            int oldSize = oldAppGrpPrimaryDocDtos.size();
            if (size < oldSize) {
                for (int i = 0; i <oldSize - size; i++) {
                    AppGrpPrimaryDocDto appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
                    appGrpPrimaryDocDto.setDocSize(0);
                    appGrpPrimaryDocDto.setSvcComDocName(oldAppGrpPrimaryDocDtos.get(oldSize-size-1+i).getSvcComDocName());
                    appGrpPrimaryDocDto.setDocName("-");
                    String svcDocId = oldAppGrpPrimaryDocDtos.get(oldSize-size - 1 + i).getSvcDocId();
                    if(svcDocId!=null){
                        appGrpPrimaryDocDto.setSvcDocId(svcDocId);
                    }else {
                        appGrpPrimaryDocDto.setSvcDocId(oldAppGrpPrimaryDocDtos.get(oldSize-size-1+i).getSvcComDocId());
                    }
                    appGrpPrimaryDocDto.setFileRepoId(oldAppGrpPrimaryDocDtos.get(oldSize-size-1+i).getFileRepoId());
                    appGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
                }
            }else if(oldSize<size){
                for(int i=0;i<size-oldSize;i++){
                    AppGrpPrimaryDocDto appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
                    appGrpPrimaryDocDto.setDocSize(0);
                    appGrpPrimaryDocDto.setSvcComDocName(appGrpPrimaryDocDtos.get(size-oldSize-1+i).getSvcComDocName());
                    appGrpPrimaryDocDto.setDocName("-");
                    String svcDocId = appGrpPrimaryDocDtos.get(size-oldSize - 1 + i).getSvcDocId();
                    if(svcDocId!=null){
                        appGrpPrimaryDocDto.setSvcDocId(svcDocId);
                    }else {
                        appGrpPrimaryDocDto.setSvcDocId(appGrpPrimaryDocDtos.get(size-oldSize-1+i).getSvcComDocId());
                    }
                    appGrpPrimaryDocDto.setFileRepoId(appGrpPrimaryDocDtos.get(size-oldSize-1+i).getFileRepoId());
                    oldAppGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
                }
            }
        }
         sortPremiseDoc(appGrpPrimaryDocDtos,oldAppGrpPrimaryDocDtos);
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        List<AppSvcDocDto> oldAppSvcDocDtoLit = oldAppSvcRelatedInfoDto.getAppSvcDocDtoLit();
        if (IaisCommonUtils.isEmpty(appSvcDocDtoLit) && oldAppSvcDocDtoLit != null) {
            appSvcDocDtoLit = new ArrayList<>(oldAppSvcDocDtoLit.size());
            for (int i = 0; i < oldAppSvcDocDtoLit.size(); i++) {
                AppSvcDocDto appSvcDocDto = new AppSvcDocDto();
                appSvcDocDto.setSvcDocId(oldAppSvcDocDtoLit.get(i).getSvcDocId());
                appSvcDocDto.setUpFileName(oldAppSvcDocDtoLit.get(i).getUpFileName());
                appSvcDocDto.setDocName("-");
                appSvcDocDto.setDocSize(0);
                appSvcDocDto.setFileRepoId(oldAppSvcDocDtoLit.get(i).getFileRepoId());
                appSvcDocDtoLit.add(appSvcDocDto);
            }
        } else if (appSvcDocDtoLit != null && oldAppSvcDocDtoLit != null) {
            int size = appSvcDocDtoLit.size();
            int oldSize = oldAppSvcDocDtoLit.size();
            if (size < oldSize) {
                for (int i = 0; i < oldSize - size; i++) {
                    AppSvcDocDto appSvcDocDto = new AppSvcDocDto();
                    appSvcDocDto.setDocSize(0);
                    appSvcDocDto.setDocName("-");
                    appSvcDocDto.setUpFileName(oldAppSvcDocDtoLit.get(oldSize-size-1+i).getUpFileName());
                    appSvcDocDto.setSvcDocId(oldAppSvcDocDtoLit.get(oldSize-size-1+i).getSvcDocId());
                    appSvcDocDto.setFileRepoId(oldAppSvcDocDtoLit.get(oldSize-size-1+i).getFileRepoId());
                    appSvcDocDtoLit.add(appSvcDocDto);
                }
            }else if(oldSize<size){
                for(int i=0;i < size-oldSize;i++){
                    AppSvcDocDto appSvcDocDto = new AppSvcDocDto();
                    appSvcDocDto.setDocName("-");
                    appSvcDocDto.setSvcDocId(appSvcDocDtoLit.get(size-oldSize-1+i).getSvcDocId());
                    appSvcDocDto.setUpFileName(appSvcDocDtoLit.get(size-oldSize-1+i).getUpFileName());
                    appSvcDocDto.setFileRepoId(appSvcDocDtoLit.get(size-oldSize-1+i).getFileRepoId());
                    oldAppSvcDocDtoLit.add(appSvcDocDto);
                }
            }
        }
        appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtoLit);
        List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        List<AppSvcCgoDto> oldAppSvcCgoDtoList = oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList();
       if (IaisCommonUtils.isEmpty(appSvcCgoDtoList) && !IaisCommonUtils.isEmpty(oldAppSvcCgoDtoList)) {
            appSvcCgoDtoList = new ArrayList<>(oldAppSvcCgoDtoList.size());
            for (int i = 0; i < oldAppSvcCgoDtoList.size(); i++) {
                AppSvcCgoDto appSvcCgoDto = new AppSvcCgoDto();
                appSvcCgoDto.setSpecialityOther("-");
                appSvcCgoDto.setSpeciality("-");
                appSvcCgoDto.setProfRegNo("-");
                appSvcCgoDto.setOfficeTelNo("-");
                appSvcCgoDto.setMobileNo("-");
                appSvcCgoDto.setPreferredMode("-");
                appSvcCgoDto.setName("-");
                appSvcCgoDto.setEmailAddr("-");
                appSvcCgoDto.setIdNo("-");
                appSvcCgoDto.setIdType("-");
                appSvcCgoDto.setSpeciality("-");
                appSvcCgoDtoList.add(appSvcCgoDto);
            }
        } else if (appSvcCgoDtoList != null && oldAppSvcCgoDtoList != null) {
            if (appSvcCgoDtoList.size() < oldAppSvcCgoDtoList.size()) {
                creatCgo(appSvcCgoDtoList,oldAppSvcCgoDtoList);
            }else if( oldAppSvcCgoDtoList.size()<appSvcCgoDtoList.size()){
                creatCgo(oldAppSvcCgoDtoList,appSvcCgoDtoList);
            }
        }
        appSvcRelatedInfoDto.setAppSvcCgoDtoList(appSvcCgoDtoList);

        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
        List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList = oldAppSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
          if (IaisCommonUtils.isEmpty(appSvcMedAlertPersonList) && oldAppSvcMedAlertPersonList != null) {
            appSvcMedAlertPersonList = new ArrayList<>(oldAppSvcMedAlertPersonList.size());
            for (int i = 0; i < oldAppSvcMedAlertPersonList.size(); i++) {
                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();

                appSvcPrincipalOfficersDto.setOfficeTelNo("-");
                appSvcPrincipalOfficersDto.setName("-");
                appSvcPrincipalOfficersDto.setSalutation("-");
                appSvcPrincipalOfficersDto.setIdType("-");
                appSvcPrincipalOfficersDto.setIdNo("-");
                appSvcPrincipalOfficersDto.setDesignation("-");
                appSvcPrincipalOfficersDto.setMobileNo("-");
                appSvcPrincipalOfficersDto.setEmailAddr("-");
                appSvcPrincipalOfficersDto.setProfRegNo("-");
                appSvcMedAlertPersonList.add(appSvcPrincipalOfficersDto);
            }
        } else if (appSvcMedAlertPersonList != null && oldAppSvcMedAlertPersonList != null) {
            int size = appSvcMedAlertPersonList.size();
            int oldSize = oldAppSvcMedAlertPersonList.size();
            if (size < oldSize) {
                for (int i = 0; i < oldSize -size; i++) {
                    AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
                    appSvcPrincipalOfficersDto.setOfficeTelNo("-");
                    appSvcPrincipalOfficersDto.setName("-");
                    appSvcPrincipalOfficersDto.setSalutation("-");
                    appSvcPrincipalOfficersDto.setIdType("-");
                    appSvcPrincipalOfficersDto.setIdNo("-");
                    appSvcPrincipalOfficersDto.setDesignation("-");
                    appSvcPrincipalOfficersDto.setMobileNo("-");
                    appSvcPrincipalOfficersDto.setEmailAddr("-");
                    appSvcPrincipalOfficersDto.setProfRegNo("-");
                    appSvcMedAlertPersonList.add(appSvcPrincipalOfficersDto);
                }
            }else if(oldSize<size){
                for(int i =0 ;i< size-oldSize;i++){
                    AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
                    appSvcPrincipalOfficersDto.setOfficeTelNo("-");
                    appSvcPrincipalOfficersDto.setName("-");
                    appSvcPrincipalOfficersDto.setSalutation("-");
                    appSvcPrincipalOfficersDto.setIdType("-");
                    appSvcPrincipalOfficersDto.setIdNo("-");
                    appSvcPrincipalOfficersDto.setDesignation("-");
                    appSvcPrincipalOfficersDto.setMobileNo("-");
                    appSvcPrincipalOfficersDto.setEmailAddr("-");
                    appSvcPrincipalOfficersDto.setProfRegNo("-");
                    oldAppSvcMedAlertPersonList.add(appSvcPrincipalOfficersDto);
                }
            }
        }
        appSvcRelatedInfoDto.setAppSvcMedAlertPersonList(appSvcMedAlertPersonList);
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> olAppSvcPrincipalOfficersDtoList = oldAppSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        if (IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtoList) && !IaisCommonUtils.isEmpty(olAppSvcPrincipalOfficersDtoList)) {
            appSvcPrincipalOfficersDtoList = new ArrayList<>(olAppSvcPrincipalOfficersDtoList.size());
            for (int i = 0; i < olAppSvcPrincipalOfficersDtoList.size(); i++) {
                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
                appSvcPrincipalOfficersDto.setPreferredMode("-");
                appSvcPrincipalOfficersDto.setOfficeTelNo("-");
                appSvcPrincipalOfficersDto.setName("-");
                appSvcPrincipalOfficersDto.setSalutation("-");
                appSvcPrincipalOfficersDto.setIdType("-");
                appSvcPrincipalOfficersDto.setIdNo("-");
                appSvcPrincipalOfficersDto.setDesignation("-");
                appSvcPrincipalOfficersDto.setMobileNo("-");
                appSvcPrincipalOfficersDto.setEmailAddr("-");
                appSvcPrincipalOfficersDto.setProfRegNo("-");
                appSvcPrincipalOfficersDtoList.add(appSvcPrincipalOfficersDto);
            }

        } else if (appSvcPrincipalOfficersDtoList != null && olAppSvcPrincipalOfficersDtoList != null) {
            int size = appSvcPrincipalOfficersDtoList.size();
            int oldSize = olAppSvcPrincipalOfficersDtoList.size();
            if (size < oldSize) {
                for(int i = 0;i < oldSize-size;i++){
                    AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
                    appSvcPrincipalOfficersDto.setPreferredMode("-");
                    appSvcPrincipalOfficersDto.setOfficeTelNo("-");
                    appSvcPrincipalOfficersDto.setName("-");
                    appSvcPrincipalOfficersDto.setSalutation("-");
                    appSvcPrincipalOfficersDto.setIdType("-");
                    appSvcPrincipalOfficersDto.setIdNo("-");
                    appSvcPrincipalOfficersDto.setDesignation("-");
                    appSvcPrincipalOfficersDto.setMobileNo("-");
                    appSvcPrincipalOfficersDto.setEmailAddr("-");
                    appSvcPrincipalOfficersDto.setProfRegNo("-");
                    appSvcPrincipalOfficersDtoList.add(appSvcPrincipalOfficersDto);
                }
            }else  if(oldSize<size){
                for(int i=0;i<size-oldSize;i++){
                    AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
                    appSvcPrincipalOfficersDto.setPreferredMode("-");
                    appSvcPrincipalOfficersDto.setOfficeTelNo("-");
                    appSvcPrincipalOfficersDto.setName("-");
                    appSvcPrincipalOfficersDto.setSalutation("-");
                    appSvcPrincipalOfficersDto.setIdType("-");
                    appSvcPrincipalOfficersDto.setIdNo("-");
                    appSvcPrincipalOfficersDto.setDesignation("-");
                    appSvcPrincipalOfficersDto.setMobileNo("-");
                    appSvcPrincipalOfficersDto.setEmailAddr("-");
                    appSvcPrincipalOfficersDto.setProfRegNo("-");
                    olAppSvcPrincipalOfficersDtoList.add(appSvcPrincipalOfficersDto);
                }
            }
        }
        appSvcRelatedInfoDto.setAppSvcPrincipalOfficersDtoList(appSvcPrincipalOfficersDtoList);
        List<AppSvcPersonnelDto> appSvcPersonnelDtoList = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
        List<AppSvcPersonnelDto> oldAppSvcPersonnelDtoList = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
        if (IaisCommonUtils.isEmpty(appSvcPersonnelDtoList) && oldAppSvcPersonnelDtoList != null) {
            appSvcPersonnelDtoList = new ArrayList<>(oldAppSvcPersonnelDtoList.size());
            for (int i = 0; i < oldAppSvcPersonnelDtoList.size(); i++) {
                AppSvcPersonnelDto appSvcPersonnelDto = new AppSvcPersonnelDto();
                appSvcPersonnelDto.setPersonnelType(oldAppSvcPersonnelDtoList.get(i).getPersonnelType());
                appSvcPersonnelDto.setDesignation("-");
                appSvcPersonnelDto.setName("-");
                appSvcPersonnelDto.setProfRegNo("-");
                appSvcPersonnelDto.setQualification("-");
                appSvcPersonnelDtoList.add(appSvcPersonnelDto);
            }
        } else if (appSvcPersonnelDtoList != null && oldAppSvcPersonnelDtoList != null) {
            int size = appSvcPersonnelDtoList.size();
            int oldSize = oldAppSvcPersonnelDtoList.size();
            if( size < oldSize ){
                for(int i=0;i<oldSize-size;i++){
                    AppSvcPersonnelDto appSvcPersonnelDto = new AppSvcPersonnelDto();
                    appSvcPersonnelDto.setPersonnelType(oldAppSvcPersonnelDtoList.get(size-1+i).getPersonnelType());
                    appSvcPersonnelDto.setDesignation("-");
                    appSvcPersonnelDto.setName("-");
                    appSvcPersonnelDto.setProfRegNo("-");

                    appSvcPersonnelDto.setQualification("-");
                    appSvcPersonnelDtoList.add(appSvcPersonnelDto);
                }
            }else if(oldSize < size){
                for(int i=0;i<size-oldSize;i++){
                    AppSvcPersonnelDto appSvcPersonnelDto = new AppSvcPersonnelDto();
                    appSvcPersonnelDto.setPersonnelType(oldAppSvcPersonnelDtoList.get(oldSize-1+i).getPersonnelType());
                    appSvcPersonnelDto.setDesignation("-");
                    appSvcPersonnelDto.setName("-");
                    appSvcPersonnelDto.setProfRegNo("-");

                    appSvcPersonnelDto.setQualification("-");
                    oldAppSvcPersonnelDtoList.add(appSvcPersonnelDto);
                }
            }

        }
        appSvcRelatedInfoDto.setAppSvcPersonnelDtoList(appSvcPersonnelDtoList);
    }

    private void creatNewPremise( List<AppGrpPremisesDto> appGrpPremisesDtoList, List<AppGrpPremisesDto> oldAppGrpPremisesDtoList){
        int size = appGrpPremisesDtoList.size();
        int oldSize = oldAppGrpPremisesDtoList.size();
        for (int i = 0; i <oldSize - size; i++) {
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            appGrpPremisesDto.setPremisesType(oldAppGrpPremisesDtoList.get(appGrpPremisesDtoList.size()-1+i).getPremisesType());
            appGrpPremisesDto.setPostalCode("-");
            appGrpPremisesDto.setOffTelNo("-");
            appGrpPremisesDto.setScdfRefNo("-");
            appGrpPremisesDto.setStreetName("-");
            appGrpPremisesDto.setBlkNo("-");
            appGrpPremisesDto.setUnitNo("-");
            appGrpPremisesDto.setFloorNo("-");
            appGrpPremisesDto.setHciName("-");
            appGrpPremisesDto.setOnsiteEndHH("-");
            appGrpPremisesDto.setOnsiteStartHH("-");
            appGrpPremisesDto.setOnsiteStartMM("-");
            appGrpPremisesDto.setOnsiteEndMM("-");
            appGrpPremisesDto.setAddrType("-");
            appGrpPremisesDto.setBuildingName("-");
            appGrpPremisesDtoList.add(appGrpPremisesDto);
        }
    }

    private void creatCgo(List<AppSvcCgoDto> appSvcCgoDtoList,List<AppSvcCgoDto>oldAppSvcCgoDtoList){
        int size = appSvcCgoDtoList.size();
        int oldSize = oldAppSvcCgoDtoList.size();
        for(int i=0;i<oldSize-size;i++){
            AppSvcCgoDto appSvcCgoDto = new AppSvcCgoDto();
            appSvcCgoDto.setSpecialityOther("-");
            appSvcCgoDto.setSpeciality("-");
            appSvcCgoDto.setProfRegNo("-");
            appSvcCgoDto.setOfficeTelNo("-");
            appSvcCgoDto.setMobileNo("-");
            appSvcCgoDto.setPreferredMode("-");
            appSvcCgoDto.setName("-");
            appSvcCgoDto.setEmailAddr("-");
            appSvcCgoDto.setIdNo("-");
            appSvcCgoDto.setIdType("-");
            appSvcCgoDto.setSpeciality("-");
            appSvcCgoDtoList.add(appSvcCgoDto);
        }
    }

    private void sortPremiseDoc(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos,List<AppGrpPrimaryDocDto> oldAppGrpPrimaryDocDtos){
        if(appGrpPrimaryDocDtos==null||oldAppGrpPrimaryDocDtos==null){
            return;
        }
        List<AppGrpPrimaryDocDto> sortAppGrpPrimaryDocDtos=new ArrayList<>(appGrpPrimaryDocDtos.size());
        List<AppGrpPrimaryDocDto> sortOldAppGrpPrimaryDocDtos=new ArrayList<>(oldAppGrpPrimaryDocDtos.size());
        for(AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos){
            String svcDocId = appGrpPrimaryDocDto.getSvcDocId();
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto1 : oldAppGrpPrimaryDocDtos){
                String svcDocId1 = appGrpPrimaryDocDto1.getSvcComDocId();
                if(svcDocId!=null&&svcDocId1!=null){
                    if(svcDocId.equals(svcDocId1)){
                        sortAppGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
                        sortOldAppGrpPrimaryDocDtos.add(appGrpPrimaryDocDto1);
                        continue;
                    }
                }

            }
        }
        appGrpPrimaryDocDtos.removeAll(sortAppGrpPrimaryDocDtos);
        oldAppGrpPrimaryDocDtos.removeAll(sortOldAppGrpPrimaryDocDtos);
        sortAppGrpPrimaryDocDtos.addAll(appGrpPrimaryDocDtos);
        sortOldAppGrpPrimaryDocDtos.addAll(oldAppGrpPrimaryDocDtos);
        appGrpPrimaryDocDtos.clear();
        oldAppGrpPrimaryDocDtos.clear();
        appGrpPrimaryDocDtos.addAll(sortAppGrpPrimaryDocDtos);
        oldAppGrpPrimaryDocDtos.addAll(sortOldAppGrpPrimaryDocDtos);
    }

    private void premiseTrans(AppSubmissionDto appSubmissionDto){
        if(appSubmissionDto==null){
            return;
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if(appGrpPremisesDtoList==null || appGrpPremisesDtoList.isEmpty()){
            return;
        }
        for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
            String premisesType = appGrpPremisesDto.getPremisesType();
            if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType)){
                appGrpPremisesDto.setStreetName(appGrpPremisesDto.getOffSiteStreetName());
                appGrpPremisesDto.setPostalCode(appGrpPremisesDto.getOffSitePostalCode());
                appGrpPremisesDto.setBuildingName(appGrpPremisesDto.getOffSiteBuildingName());
                appGrpPremisesDto.setFloorNo(appGrpPremisesDto.getOffSiteFloorNo());
                appGrpPremisesDto.setUnitNo(appGrpPremisesDto.getOffSiteUnitNo());
                appGrpPremisesDto.setAddrType(appGrpPremisesDto.getOffSiteAddressType());
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)){
                appGrpPremisesDto.setStreetName(appGrpPremisesDto.getConveyanceStreetName());
                appGrpPremisesDto.setPostalCode(appGrpPremisesDto.getConveyancePostalCode());
                appGrpPremisesDto.setBuildingName(appGrpPremisesDto.getConveyanceBuildingName());
                appGrpPremisesDto.setFloorNo(appGrpPremisesDto.getConveyanceFloorNo());
                appGrpPremisesDto.setUnitNo(appGrpPremisesDto.getConveyanceUnitNo());
                appGrpPremisesDto.setAddrType(appGrpPremisesDto.getConveyanceAddressType());
            }
        }

    }

    private void premise(AppSubmissionDto appSubmissionDto,AppSubmissionDto oldAppSubmissionDto){
        if(appSubmissionDto==null||oldAppSubmissionDto==null){
            return;
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if(appGrpPremisesDtoList==null || appGrpPremisesDtoList.isEmpty()){
            return;
        }
        List<AppGrpPremisesDto> oldAppSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        if(oldAppSubmissionDtoAppGrpPremisesDtoList==null || oldAppSubmissionDtoAppGrpPremisesDtoList.isEmpty()){
            return;
        }
        for(int i=0;i<appGrpPremisesDtoList.size();i++){
            if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getStreetName())){
                appGrpPremisesDtoList.get(i).setStreetName("-");
            }
            if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getPostalCode())){
                appGrpPremisesDtoList.get(i).setPostalCode("-");
            }
            if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getBuildingName())){
                appGrpPremisesDtoList.get(i).setBuildingName("-");
            }
            if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getFloorNo())){
                appGrpPremisesDtoList.get(i).setFloorNo("-");
            }
            if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getUnitNo())){
                appGrpPremisesDtoList.get(i).setUnitNo("-");
            }
            if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getAddrType())){
                appGrpPremisesDtoList.get(i).setAddrType("-");
            }
        }
    }

    private void oldAppSubmission(AppSubmissionDto appSubmissionDto,String hciCode){
        if(appSubmissionDto==null){
            return;
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if(appGrpPremisesDtoList==null || appGrpPremisesDtoList.isEmpty()||hciCode==null){
            return;
        }
        List<AppGrpPremisesDto> appGrpPremisesDtos=IaisCommonUtils.genNewArrayList();
        for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
            String hciCode1 = appGrpPremisesDto.getHciCode();
            if(hciCode1!=null){
                if(hciCode1.equals(hciCode)){
                    appGrpPremisesDtos.add(appGrpPremisesDto);
                }
            }else {
                appGrpPremisesDtos.add(appGrpPremisesDto);
            }
        }
    }
}
