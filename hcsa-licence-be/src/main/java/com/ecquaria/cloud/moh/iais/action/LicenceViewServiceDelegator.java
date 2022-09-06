package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewHciNameDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.HfsmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremNonLicRelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmFormDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.OperationHoursReloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ComplaintDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.DisciplinaryRecordResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private BeEicGatewayClient beEicGatewayClient;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private LicCommService licCommService;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Value("${moh.halp.prs.enable}")
    private String prsFlag;
    @Value("${moh.halp.herims.enable:N}")
    private String herimsFlag;
    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator cleanSession start ...."));
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, null);
        bpc.request.getSession().removeAttribute("AuthorisedPerson");
        bpc.request.getSession().removeAttribute("BoardMember");
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator cleanSession end ...."));
    }

    /**
     * StartStep: PrepareViewData
     *
     * @param bpc
     * @throws
     */
    public void prepareViewData(BaseProcessClass bpc) throws Exception {
        // licence AppSubmissionDto doucument add md5
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator prepareData start ..."));
        String cess_ack002 = MessageUtil.getMessageDesc("CESS_ACK002");
        ParamUtil.setSessionAttr(bpc.request,"cess_ack002",cess_ack002);
        ParamUtil.setSessionAttr(bpc.request, "appealSpecialDocDto",null);
        String rfi = bpc.request.getParameter("rfi");
        String requestRfi = (String)bpc.request.getAttribute("rfi");
        if (!StringUtil.isEmpty(rfi)||!StringUtil.isEmpty(requestRfi)) {
            bpc.request.setAttribute("rfi", "rfi");
        }
        bpc.request.getSession().removeAttribute(NOT_VIEW);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) bpc.request.getSession().getAttribute("applicationViewDto");
        if (applicationViewDto == null) {
            return;
        }
        // Get AppSubmissionDto
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
        if (appPremisesCorrelationDto == null) {
            // the appPremisesCorrelationDto should not be null, not used any more
            String appId = ParamUtil.getString(bpc.request, "appId");
            appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
        }
        AppSubmissionDto appSubmissionDto = getAppSubmissionAndHandLicence(appPremisesCorrelationDto, bpc.request);
        // set App Edit Select Dto
        AppEditSelectDto appEditSelectDto = (AppEditSelectDto) bpc.request.getSession().getAttribute("appEditSelectDto");
        if (appEditSelectDto == null) {
            appEditSelectDto = applicationViewDto.getAppEditSelectDto();
        }
        AppEditSelectDto rfiAppEditSelectDto=(AppEditSelectDto) bpc.request.getSession().getAttribute("rfiAppEditSelectDto");
        String isSaveRfiSelect = (String)bpc.request.getSession().getAttribute("isSaveRfiSelect");
        if(AppConsts.YES.equals(isSaveRfiSelect)){
            bpc.request.getSession().setAttribute("pageAppEditSelectDto",rfiAppEditSelectDto);
        }else {
            bpc.request.getSession().setAttribute("pageAppEditSelectDto",null);
        }
        log.info(StringUtil.changeForLog(appEditSelectDto+"appEditSelectDto"));
        bpc.request.getSession().setAttribute("appEditSelectDto",appEditSelectDto);

        if (appPremisesCorrelationDto != null && appSubmissionDto != null) {
            handleWithDrawalDoc(appSubmissionDto.getAppType(), appSubmissionDto.getAppGrpId(), appPremisesCorrelationDto.getApplicationId(),
                    bpc.request);
        }

        if(appSubmissionDto == null){
            return;
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        /*List<PublicHolidayDto> publicHolidayDtos = appointmentClient.getActiveHoliday().getEntity();
        if(appGrpPremisesDtoList!=null&&publicHolidayDtos!=null){
            formatDate(appGrpPremisesDtoList, publicHolidayDtos);
        }*/
        String serviceId = applicationViewDto.getApplicationDto().getServiceId();
        List<String> list = IaisCommonUtils.genNewArrayList();
        list.add(serviceId);
        List<HcsaServiceStepSchemeDto> entity = hcsaConfigClient.getServiceStepsByServiceIds(list).getEntity();
        List<String> stringList = IaisCommonUtils.genNewArrayList();
        //Map<String,String> stepNameMap= Maps.newHashMapWithExpectedSize(entity.size());
        for (HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto : entity) {
            stringList.add(hcsaServiceStepSchemeDto.getStepCode());
            //stepNameMap.put(hcsaServiceStepSchemeDto.getStepCode(),hcsaServiceStepSchemeDto.getStepName());
        }
        //bpc.request.getSession().setAttribute("stepNameMap", stepNameMap);
        bpc.request.getSession().setAttribute("hcsaServiceStepSchemeDtoList", stringList);

        boolean canEidtPremise  = canEidtPremise(applicationViewDto.getApplicationGroupDto().getId());
        ParamUtil.setRequestAttr(bpc.request,"canEidtPremise",canEidtPremise);
        //log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator prepareData end ..."));
        //String appType = appSubmissionDto.getAppType();
        try {
            contrastNewAndOld(appSubmissionDto,bpc.request);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        if(appGrpPremisesDtoList!=null){
            String licenseeId =applicationViewDto.getApplicationGroupDto().getLicenseeId();
            for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
                String premisesType = appGrpPremisesDto.getPremisesType();
                String checkhciName = appGrpPremisesDto.getHciName();
                if(checkhciName!=null){
                    List<ApplicationViewHciNameDto> applicationViewHciNameDtos = hcsaLicenceClient.getApplicationViewHciNameDtoByHciName(checkhciName, licenseeId,premisesType).getEntity();
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
                String conveyanceVehicleNo = appGrpPremisesDto.getVehicleNo();
                List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = appGrpPremisesDto.getAppPremisesOperationalUnitDtos();
                map.put("floorNo0",floorNo);
                map.put("unitNo0",unitNo);
                int i = 1;
                if (appPremisesOperationalUnitDtos != null) {
                    for (AppPremisesOperationalUnitDto appPremisesOperationalUnitDto : appPremisesOperationalUnitDtos) {
                        map.put("floorNo" + i, appPremisesOperationalUnitDto.getFloorNo());
                        map.put("unitNo" + i, appPremisesOperationalUnitDto.getUnitNo());
                        i++;
                    }
                }
                map.put("floorUnitSize", String.valueOf(i));
                map.put("blkNo",blkNo);
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
        ApplicationGroupDto groupDto = applicationViewDto.getApplicationGroupDto();
        if(groupDto!=null){
            authorisedPerson(groupDto.getLicenseeId(),appSubmissionDto);
        }
        if(appSubmissionDto.getOldAppSubmissionDto()!=null){
            /*if(publicHolidayDtos!=null){
                formatDate(appSubmissionDto.getOldAppSubmissionDto().getAppGrpPremisesDtoList(), publicHolidayDtos);
            }*/
//            premise(appSubmissionDto,appSubmissionDto.getOldAppSubmissionDto(),bpc.request,groupDto);
        }
        List<AppDeclarationDocDto> appDeclarationDocDtos = appSubmissionDto.getAppDeclarationDocDtos();
        if(appDeclarationDocDtos!=null){
            Collections.sort(appDeclarationDocDtos, Comparator.comparingInt(AppDeclarationDocDto::getSeqNum));
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        prepareViewServiceForm(bpc);
        if ("Y".equals(prsFlag)) {
            disciplinaryRecord(appSubmissionDto, bpc.request);
        }
        if ("Y".equals(herimsFlag)) {
            herimsRecod(appSubmissionDto, bpc.request);
        }
    }

    private AppSubmissionDto getAppSubmissionAndHandLicence(AppPremisesCorrelationDto appPremisesCorrelationDto, HttpServletRequest request) {
        if (appPremisesCorrelationDto == null) {
            return null;
        }
        String applicationId = appPremisesCorrelationDto.getApplicationId();
        AppSubmissionDto appSubmissionDto = licenceViewService.getAppSubmissionByAppId(applicationId);
        DealSessionUtil.initView(appSubmissionDto);
        if (appSubmissionDto == null) {
            return appSubmissionDto;
        }
        /*List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        HcsaServiceDto hcsaServiceDto = null;
        if (appSvcRelatedInfoDtos != null && !appSvcRelatedInfoDtos.isEmpty()) {
            String serviceId = appSvcRelatedInfoDtos.get(0).getServiceId();
            hcsaServiceDto = applicationViewService.getHcsaServiceDtoById(serviceId);
            appSvcRelatedInfoDtos.get(0).setServiceCode(hcsaServiceDto.getSvcCode());
            ParamUtil.setRequestAttr(request, HCSASERVICEDTO, hcsaServiceDto);
        }*/
        // new
        ApplicationDto entity = applicationClient.getApplicationById(appPremisesCorrelationDto.getApplicationId()).getEntity();
        String newGrpId = entity.getAppGrpId();
        ApplicationGroupDto newApplicationGroupDto = applicationClient.getAppById(newGrpId).getEntity();
        String newApplicationGroupDtoLicenseeId = newApplicationGroupDto.getLicenseeId();
        LicenseeDto newLicenceDto = organizationClient.getLicenseeDtoById(newApplicationGroupDtoLicenseeId).getEntity();
        request.setAttribute("newLicenceDto", newLicenceDto);
        // last - previous version record
        if (entity.getVersion() > 1) {
            ApplicationDto applicationDto = applicationClient.getLastApplicationByAppNo(entity).getEntity();
            if (applicationDto != null) {
                String oldGrpId = applicationDto.getAppGrpId();
                ApplicationGroupDto oldApplicationGroupDto = applicationClient.getAppById(oldGrpId).getEntity();
                String licenseeId = oldApplicationGroupDto.getLicenseeId();
                LicenseeDto oldLicenceDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
                request.setAttribute("oldLicenceDto", oldLicenceDto);
                AppSubmissionDto appSubmissionByAppId = applicationClient.getAppSubmissionByoldAppId(
                        applicationDto.getId()).getEntity();
                DealSessionUtil.initView(appSubmissionByAppId);
                /*if (hcsaServiceDto != null) {
                    appSubmissionByAppId.getAppSvcRelatedInfoDtoList().get(0).setServiceCode(hcsaServiceDto.getSvcCode());
                }*/
                appSubmissionDto.setOldAppSubmissionDto(appSubmissionByAppId);
            }
        } else if (entity.getOriginLicenceId() != null) {
            LicenceDto licenceDto = hcsaLicenceClient.getLicenceDtoById(entity.getOriginLicenceId()).getEntity();
            if (licenceDto != null) {
                LicenseeDto oldLicenceDto = organizationClient.getLicenseeDtoById(licenceDto.getLicenseeId()).getEntity();
                request.setAttribute("oldLicenceDto", oldLicenceDto);
            }
            AppSubmissionDto appSubmission = licCommService.viewAppSubmissionDto(entity.getOriginLicenceId());
            if (appSubmission != null) {
                DealSessionUtil.initView(appSubmission);
                /*if (hcsaServiceDto != null) {
                    appSubmission.getAppSvcRelatedInfoDtoList().get(0).setServiceCode(hcsaServiceDto.getSvcCode());
                }*/
                appSubmissionDto.setOldAppSubmissionDto(appSubmission);
            }
        } else {
            ParamUtil.setSessionAttr(request, NOT_VIEW, NOT_VIEW);
        }
        return appSubmissionDto;
    }

    private void handleWithDrawalDoc(String appType, String appGrpId, String appId, HttpServletRequest request) {
        if (!ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(appType)) {
            return;
        }
        List<WithdrawnDto> withdrawnDtoList = IaisCommonUtils.genNewArrayList();
        AppPremiseMiscDto premiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(appId).getEntity();
        List<ApplicationDto> applicationDtoList = applicationClient.getAppDtosByAppGrpId(appGrpId).getEntity();
        if (applicationDtoList != null && premiseMiscDto != null){
            applicationDtoList.forEach(h -> {
                ApplicationDto oldApplicationDto = applicationClient.getApplicationById(premiseMiscDto.getRelateRecId()).getEntity();
                WithdrawnDto withdrawnDto = new WithdrawnDto();
                withdrawnDto.setApplicationNo(oldApplicationDto.getApplicationNo());

                withdrawnDto.setWithdrawnReason(premiseMiscDto.getReason());
                withdrawnDto.setWithdrawnRemarks(premiseMiscDto.getRemarks());
                List<AppPremisesSpecialDocDto> appealSpecialDocDto = fillUpCheckListGetAppClient.getAppPremisesSpecialDocByPremId(premiseMiscDto.getAppPremCorreId()).getEntity();
                List<PageShowFileDto> pageShowFileDtos = IaisCommonUtils.genNewArrayList();
                HashMap<String,File> map= IaisCommonUtils.genNewHashMap();
                HashMap<String, PageShowFileDto> pageShowFileHashMap = IaisCommonUtils.genNewHashMap();
                if(appealSpecialDocDto!=null&&!appealSpecialDocDto.isEmpty()){
                    for(int i=0;i<appealSpecialDocDto.size();i++){
                        PageShowFileDto pageShowFileDto =new PageShowFileDto();
                        pageShowFileDto.setFileUploadUrl(appealSpecialDocDto.get(i).getFileRepoId());
                        pageShowFileDto.setFileName(appealSpecialDocDto.get(i).getDocName());
                        pageShowFileDto.setFileMapId("selectedFileDiv"+i);
                        pageShowFileDto.setSize(appealSpecialDocDto.get(i).getDocSize());
                        pageShowFileDto.setMd5Code(appealSpecialDocDto.get(i).getMd5Code());
                        pageShowFileDto.setIndex(String.valueOf(i));
                        pageShowFileDtos.add(pageShowFileDto);
                        map.put("selectedFile"+i,null);
                        pageShowFileHashMap.put("selectedFile"+i, pageShowFileDto);
                    }
                    request.getSession().setAttribute("seesion_files_map_ajax_feselectedFile",map);
                    request.getSession().setAttribute("pageShowFileHashMap",pageShowFileHashMap);
                    request.getSession().setAttribute("seesion_files_map_ajax_feselectedFile_MaxIndex",appealSpecialDocDto.size());
                }
                request.getSession().setAttribute("pageShowFiles", pageShowFileDtos);
                withdrawnDtoList.add(withdrawnDto);
            });
        }
        if(withdrawnDtoList.size() > 0){
            ParamUtil.setRequestAttr(request, WITHDRAWDTO, withdrawnDtoList.get(0));
            ParamUtil.setRequestAttr(request, WITHDRAWDTOLIST, withdrawnDtoList);
        }
    }

    private void disciplinaryRecord(AppSubmissionDto appSubmissionDto,HttpServletRequest request) {
        if (appSubmissionDto == null) {
            return;
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtoList = appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();
        //List<AppSvcPersonnelDto> appSvcPersonnelDtoList = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
        Set<String> redNo = new HashSet<>();
        List<String> list = new ArrayList<>();
        if (appSvcCgoDtoList != null) {
            for (AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoDtoList) {
                String profRegNo = appSvcCgoDto.getProfRegNo();
                redNo.add(profRegNo);
            }
        }
        /*if (appSvcPersonnelDtoList != null) {
            for (AppSvcPersonnelDto appSvcPersonnelDto : appSvcPersonnelDtoList) {
                if (!StringUtil.isEmpty(appSvcPersonnelDto.getProfRegNo())) {
                    redNo.add(appSvcPersonnelDto.getProfRegNo());
                }
            }
        }*/
        if (appSvcClinicalDirectorDtoList != null) {
            for (AppSvcPrincipalOfficersDto v : appSvcClinicalDirectorDtoList) {
                if (!StringUtil.isEmpty(v.getProfRegNo())) {
                    String regNo = v.getProfRegNo();
                    redNo.add(regNo);
                }
            }
        }
        if (oldAppSubmissionDto != null) {
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
            List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList = oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList();
            if (oldAppSvcCgoDtoList != null) {
                for (AppSvcPrincipalOfficersDto appSvcCgoDto : oldAppSvcCgoDtoList) {
                    redNo.add(appSvcCgoDto.getProfRegNo());
                }
            }
            /*List<AppSvcPersonnelDto> oldAppSvcPersonnelDtoList = oldAppSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
            if (oldAppSvcPersonnelDtoList != null) {
                for (AppSvcPersonnelDto appSvcPersonnelDto : oldAppSvcPersonnelDtoList) {
                    if (!StringUtil.isEmpty(appSvcPersonnelDto.getProfRegNo())) {
                        redNo.add(appSvcPersonnelDto.getProfRegNo());
                    }
                }
            }*/
            List<AppSvcPrincipalOfficersDto> oldAppSvcClinicalDirectorDtoList = oldAppSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();
            if (oldAppSvcClinicalDirectorDtoList != null) {
                for (AppSvcPrincipalOfficersDto v : oldAppSvcClinicalDirectorDtoList) {
                    if (!StringUtil.isEmpty(v.getProfRegNo())) {
                        redNo.add(v.getProfRegNo());
                    }
                }
            }
        }
        list.addAll(redNo);
        ProfessionalParameterDto professionalParameterDto = new ProfessionalParameterDto();
        professionalParameterDto.setRegNo(list);
        professionalParameterDto.setClientId("22222");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String format = simpleDateFormat.format(new Date());
        professionalParameterDto.setTimestamp(format);
        professionalParameterDto.setSignature("2222");
        List<DisciplinaryRecordResponseDto> disciplinaryRecordResponseDtos = new ArrayList<>();
        String msg = MessageUtil.getMessageDesc("GENERAL_ERR0048");
        if (!list.isEmpty()) {
            try {
                disciplinaryRecordResponseDtos = applicationClient.getDisciplinaryRecord(professionalParameterDto).getEntity();
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                request.setAttribute("beEicGatewayClient", msg);
            }
        }
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<ProfessionalResponseDto> professionalResponseDtos = null;
        HashMap<String, ProfessionalResponseDto> proHashMap = IaisCommonUtils.genNewHashMap();
        if (!list.isEmpty()) {
            try {
                professionalResponseDtos = beEicGatewayClient.getProfessionalDetail(professionalParameterDto, signature.date(),
                        signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
            } catch (Throwable e) {
                log.error(e.getMessage(), e);
                request.setAttribute("beEicGatewayClient", msg);
                log.error("------>this have error<----- Not able to connect to professionalResponseDtos at this moment!");
            }
        }
        if (professionalResponseDtos != null) {
            for (ProfessionalResponseDto v : professionalResponseDtos) {
                proHashMap.put(v.getRegno(), v);
            }
        }
        HashMap<String, List<ComplaintDto>> listHashMap = IaisCommonUtils.genNewHashMap();
        if (disciplinaryRecordResponseDtos != null) {
            for (DisciplinaryRecordResponseDto disciplinaryRecordResponseDto : disciplinaryRecordResponseDtos) {
                if (disciplinaryRecordResponseDto.getComplaints() != null) {
                    List<ComplaintDto> complaintDtos = listHashMap.get(disciplinaryRecordResponseDto.getRegno());
                    if (complaintDtos == null) {
                        complaintDtos = new ArrayList<>();
                        List<ComplaintDto> complaintDtoList = addMoneySymbol(disciplinaryRecordResponseDto.getComplaints());
                        complaintDtos.addAll(complaintDtoList);
                        listHashMap.put(disciplinaryRecordResponseDto.getRegno(), complaintDtos);
                    } else {
                        complaintDtos.addAll(disciplinaryRecordResponseDto.getComplaints());
                        listHashMap.put(disciplinaryRecordResponseDto.getRegno(), complaintDtos);
                    }
                }
            }
        }
        request.getSession().setAttribute("proHashMap", proHashMap);
        request.getSession().setAttribute("listHashMap", listHashMap);
    }

    private void herimsRecod(AppSubmissionDto appSubmissionDto,HttpServletRequest request) {
        if (appSubmissionDto == null) {
            return;
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
        List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtoList = appSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();
        Set<String> idNoSet = new HashSet<>();
        List<String> idList = new ArrayList<>();
        Object newLicenceDto = request.getAttribute("newLicenceDto");
        if (newLicenceDto != null) {
            LicenseeDto newLic = (LicenseeDto) newLicenceDto;
            idNoSet.add(newLic.getUenNo());
        }
        Object oldLicenceDto = request.getAttribute("oldLicenceDto");
        if (oldLicenceDto != null) {
            LicenseeDto oldL = (LicenseeDto) oldLicenceDto;
            idNoSet.add(oldL.getUenNo());
        }
        // licensee
        SubLicenseeDto subLicenseeDto = appSubmissionDto.getSubLicenseeDto();
        if (subLicenseeDto != null) {
            if (!StringUtil.isEmpty(subLicenseeDto.getUenNo())) {
                idNoSet.add(subLicenseeDto.getUenNo());
            }
            if (!StringUtil.isEmpty(subLicenseeDto.getIdNumber())) {
                idNoSet.add(subLicenseeDto.getIdNumber());
            }
        }
        if (appSvcCgoDtoList != null) {
            for (AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoDtoList) {
                String idNo = appSvcCgoDto.getIdNo();
                idNoSet.add(idNo);
            }
        }
        if (appSvcPrincipalOfficersDtoList != null) {
            for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtoList) {
                String idNo = appSvcPrincipalOfficersDto.getIdNo();
                idNoSet.add(idNo);
            }
        }
        if (appSvcMedAlertPersonList != null) {
            for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcMedAlertPersonList) {
                String idNo = appSvcPrincipalOfficersDto.getIdNo();
                idNoSet.add(idNo);
            }
        }
        if (appSvcClinicalDirectorDtoList != null) {
            for (AppSvcPrincipalOfficersDto v : appSvcClinicalDirectorDtoList) {
                idNoSet.add(v.getIdNo());
            }
        }
        // board member and authorised person
        List<LicenseeKeyApptPersonDto> boardMember = appSubmissionDto.getBoardMember();
        if (boardMember != null) {
            for (LicenseeKeyApptPersonDto v : boardMember) {
                idNoSet.add(v.getIdNo());
            }
        }
        List<OrgUserDto> authorisedPerson = appSubmissionDto.getAuthorisedPerson();
        if (authorisedPerson != null) {
            for (OrgUserDto orgUserDto : authorisedPerson) {
                idNoSet.add(orgUserDto.getIdNumber());
            }
        }
        if (oldAppSubmissionDto != null) {
            // licensee
            subLicenseeDto = oldAppSubmissionDto.getSubLicenseeDto();
            if (subLicenseeDto != null) {
                idNoSet.add(subLicenseeDto.getUenNo());
                idNoSet.add(subLicenseeDto.getIdNumber());
            }
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
            List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList = oldAppSvcRelatedInfoDto.getAppSvcCgoDtoList();
            if (oldAppSvcCgoDtoList != null) {
                for (AppSvcPrincipalOfficersDto appSvcCgoDto : oldAppSvcCgoDtoList) {
                    idNoSet.add(appSvcCgoDto.getIdNo());
                }
            }
            List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDtoList = oldAppSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
            if (oldAppSvcPrincipalOfficersDtoList != null) {
                for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : oldAppSvcPrincipalOfficersDtoList) {
                    idNoSet.add(appSvcPrincipalOfficersDto.getIdNo());
                }
            }
            List<AppSvcPrincipalOfficersDto> oldAppSvcMedAlertPersonList = oldAppSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
            if (oldAppSvcMedAlertPersonList != null) {
                for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : oldAppSvcMedAlertPersonList) {
                    idNoSet.add(appSvcPrincipalOfficersDto.getIdNo());
                }
            }
            List<AppSvcPrincipalOfficersDto> oldAppSvcClinicalDirectorDtoList = oldAppSvcRelatedInfoDto.getAppSvcClinicalDirectorDtoList();
            if (oldAppSvcClinicalDirectorDtoList != null) {
                for (AppSvcPrincipalOfficersDto v : oldAppSvcClinicalDirectorDtoList) {
                    idNoSet.add(v.getIdNo());
                }
            }
        }
        idList.addAll(idNoSet);
        List<HfsmsDto> hfsmsDtos = IaisCommonUtils.genNewArrayList();
        try {
            hfsmsDtos = applicationClient.getHfsmsDtoByIdNo(idList).getEntity();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
             //GENERAL_ERR0068 - Not able to connect to HERIMS at this moment!
            request.setAttribute("beEicGatewayClient", MessageUtil.getMessageDesc("GENERAL_ERR0068"));
            log.error("------>this have error<----- Not able to connect to HERIMS at this moment!");
        }
        HashMap<String, List<HfsmsDto>> hashMap = IaisCommonUtils.genNewHashMap();
        if (!IaisCommonUtils.isEmpty(hfsmsDtos)) {
            for (HfsmsDto hfsmsDto : hfsmsDtos) {
                String identificationNo = hfsmsDto.getIdentificationNo();
                List<HfsmsDto> hfsmsDtoList = hashMap.get(identificationNo);
                if (hfsmsDtoList == null) {
                    hfsmsDtoList = new ArrayList<>();
                    hfsmsDtoList.add(hfsmsDto);
                    hashMap.put(identificationNo, hfsmsDtoList);
                } else {
                    hfsmsDtoList.add(hfsmsDto);
                    hashMap.put(identificationNo, hfsmsDtoList);
                }
            }
        }
        request.getSession().setAttribute("hashMap", hashMap);
    }

    private List<ComplaintDto> addMoneySymbol(List<ComplaintDto> complaints) {
        if(!IaisCommonUtils.isEmpty(complaints)){
            for(ComplaintDto complaintDto : complaints){
                if(complaintDto != null && !StringUtil.isEmpty(complaintDto.getFineamount())) {
                    String money = "$" + complaintDto.getFineamount();
                    complaintDto.setFineamount(money);
                }
            }
        }
        return complaints;
    }

    private void authorisedPerson( String licenseeId,AppSubmissionDto appSubmissionDto){
        if(licenseeId==null){
            return;
        }
        LicenseeDto licenceDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
        if(licenceDto!=null){
            String organizationId = licenceDto.getOrganizationId();
            List<OrgUserDto> orgUserDtos = organizationClient.getOrgUserAccountSampleDtoByOrganizationId(organizationId).getEntity();
            List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDtos = organizationClient.getLicenseeKeyApptPersonByLiceseeId(licenseeId).getEntity();
            appSubmissionDto.setAuthorisedPerson(orgUserDtos);
            appSubmissionDto.setBoardMember(licenseeKeyApptPersonDtos);
        }
    }
    private void oldAuthorisedPerson(String licenseeId,AppSubmissionDto oldAppSubmissionDto){
        if(licenseeId==null){
            return;
        }
        LicenseeDto oldLicenceDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
        if(oldLicenceDto!=null){
            String organizationId = oldLicenceDto.getOrganizationId();
            List<OrgUserDto> orgUserDtos = organizationClient.getOrgUserAccountSampleDtoByOrganizationId(organizationId).getEntity();
            List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDtos = organizationClient.getLicenseeKeyApptPersonByLiceseeId(licenseeId).getEntity();
            oldAppSubmissionDto.setAuthorisedPerson(orgUserDtos);
            oldAppSubmissionDto.setBoardMember(licenseeKeyApptPersonDtos);
        }
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
        /*if(appGrpPremisesDtoList!=null){
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDto.getAppPremPhOpenPeriodList();
                if(appPremPhOpenPeriodList!=null){
                    for (AppPremPhOpenPeriodDto appPremPhOpenPeriodDto : appPremPhOpenPeriodList) {
                        Time startFrom = appPremPhOpenPeriodDto.getStartFrom();
                        Time endTo = appPremPhOpenPeriodDto.getEndTo();
                        String phDate = appPremPhOpenPeriodDto.getPhDate();
                        if(phDate==null){
                            continue;
                        }
                        appPremPhOpenPeriodDto.setDayName(MasterCodeUtil.getCodeDesc(appPremPhOpenPeriodDto.getPhDate()));
                        if (startFrom != null && endTo != null) {
                            String string = startFrom.toString();
                            String string1 = endTo.toString();
                            appPremPhOpenPeriodDto.setConvEndToMM(string1.split(":")[1]);
                            appPremPhOpenPeriodDto.setConvStartFromMM(string.split(":")[1]);
                            appPremPhOpenPeriodDto.setConvStartFromHH(string.split(":")[0]);
                            appPremPhOpenPeriodDto.setConvEndToHH(string1.split(":")[0]);
                        }
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
        }*/
    }

    /**
     * StartStep: doSaveSelect
     *
     * not used,
     *
     * Refer to {@link com.ecquaria.cloud.moh.iais.ajax.RequestForInformationSubmitAjaxController}#callRfiSubmit(bpc.request)
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
    public void prepareViewServiceForm(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the do prepareView start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            return;
        }
        if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(appSubmissionDto.getAppType())){
            return;
        }
        AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();

        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = null;
        if (oldAppSubmissionDto != null) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (appSvcRelatedInfoDtoList != null) {
                oldAppSvcRelatedInfoDto = doAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList, oldAppSubmissionDto);
            }
        }

        AppSvcRelatedInfoDto appSvcRelatedInfoDto = doAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos, appSubmissionDto);
        appSvcRelatedInfoDto.setOldAppSvcRelatedInfoDto(oldAppSvcRelatedInfoDto);

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

    private AppSvcRelatedInfoDto doAppSvcRelatedInfoDtoList(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList, AppSubmissionDto appSubmissionDto) {
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)) {
            appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(0);
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
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">Mode of Service Delivery</li>";
            rfiUpWindowsCheck.add("Premises");
        }
        /*if (selectsList.contains("primary")) {
            appEditSelectDto.setDocEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">Primary Documents</li>";
            rfiUpWindowsCheck.add("Primary Documents");
        }*/
        if (selectsList.contains("service")) {
            appEditSelectDto.setServiceEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">Service Related Information - " + serviceName+ "</li>";
            rfiUpWindowsCheck.add("Service Related Information - " + serviceName);
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

    private void contrastNewAndOld(AppSubmissionDto appSubmissionDto,HttpServletRequest request) throws Exception {
        AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
        //dealWithMultipleDoc(appSubmissionDto);
        if (oldAppSubmissionDto == null) {
            /*AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
            if (appSvcRelatedInfoDto != null) {
                List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
                if (appSvcPrincipalOfficersDtoList != null) {
                    List<AppSvcPrincipalOfficersDto> i_PO = new ArrayList<>(appSvcPrincipalOfficersDtoList.size());
                    List<AppSvcPrincipalOfficersDto> j_DPO = new ArrayList<>(appSvcPrincipalOfficersDtoList.size());
                    for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtoList) {
                        if ("PO".equals(appSvcPrincipalOfficersDto.getPsnType())) {
                            i_PO.add(appSvcPrincipalOfficersDto);
                        } else if ("DPO".equals(appSvcPrincipalOfficersDto.getPsnType())) {
                            j_DPO.add(appSvcPrincipalOfficersDto);
                        }
                    }
                    appSvcPrincipalOfficersDtoList.clear();
                    appSvcPrincipalOfficersDtoList.addAll(i_PO);
                    appSvcPrincipalOfficersDtoList.addAll(j_DPO);
                    request.setAttribute("PO_SIZE", i_PO.size());
                    request.setAttribute("DPO_SIZE", j_DPO.size());
                }
//                Map<String, List<AppSvcDocDto>> multipleSvcDoc = appSvcRelatedInfoDto.getMultipleSvcDoc();
//                sortSvcDoc(multipleSvcDoc);
//                multipleSvcDoc = translateForShow(multipleSvcDoc, appSvcRelatedInfoDto.getServiceId());
//                appSvcRelatedInfoDto.setMultipleSvcDoc(multipleSvcDoc);
            }*/
            return;
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        List<AppGrpPremisesDto> oldAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        dealAppGrpPremisesList(appGrpPremisesDtoList, oldAppGrpPremisesDtoList);
//        publicPH(appGrpPremisesDtoList, oldAppGrpPremisesDtoList);
//        event(appGrpPremisesDtoList, oldAppGrpPremisesDtoList);
//        weekly(appGrpPremisesDtoList, oldAppGrpPremisesDtoList);
//        ph(appGrpPremisesDtoList, oldAppGrpPremisesDtoList);
        // specialised
        List<AppPremSpecialisedDto> appPremSpecialisedDtoList = appSubmissionDto.getAppPremSpecialisedDtoList();
        List<AppPremSpecialisedDto> oldAppPremSpecialisedDtoList = oldAppSubmissionDto.getAppPremSpecialisedDtoList();
        dealAppPremSpecialisedDtoList(appPremSpecialisedDtoList, oldAppPremSpecialisedDtoList);
        appSubmissionDto.setAppPremSpecialisedDtoList(appPremSpecialisedDtoList);
        oldAppSubmissionDto.setAppPremSpecialisedDtoList(oldAppPremSpecialisedDtoList);
        // service info
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        log.info(StringUtil.changeForLog("The multipleSvcDoc  show change"));
        // CGO
        dealKeyPersonnel(appSvcRelatedInfoDto, oldAppSvcRelatedInfoDto, ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
        // CD
        dealKeyPersonnel(appSvcRelatedInfoDto, oldAppSvcRelatedInfoDto, ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR);
        // Med Alert Person
        dealKeyPersonnel(appSvcRelatedInfoDto, oldAppSvcRelatedInfoDto, ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
        // PO
        dealKeyPersonnel(appSvcRelatedInfoDto, oldAppSvcRelatedInfoDto, ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
        // Nominee
        dealKeyPersonnel(appSvcRelatedInfoDto, oldAppSvcRelatedInfoDto, ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
        // KAH
        dealKeyPersonnel(appSvcRelatedInfoDto, oldAppSvcRelatedInfoDto, ApplicationConsts.PERSONNEL_PSN_KAH);
        // Svc Personnel
        /*List<AppSvcPersonnelDto> appSvcPersonnelDtoList = IaisCommonUtils.getList(appSvcRelatedInfoDto.getAppSvcPersonnelDtoList());
        List<AppSvcPersonnelDto> oldAppSvcPersonnelDtoList = IaisCommonUtils.getList(oldAppSvcRelatedInfoDto.getAppSvcPersonnelDtoList());
        appSvcPersonnelDtoList(appSvcPersonnelDtoList, oldAppSvcPersonnelDtoList);
        oldAppSvcRelatedInfoDto.setAppSvcPersonnelDtoList(oldAppSvcPersonnelDtoList);
        appSvcRelatedInfoDto.setAppSvcPersonnelDtoList(appSvcPersonnelDtoList);*/
        // section leader
        List<AppSvcPersonnelDto> appSvcSectionLeaderList = IaisCommonUtils.getList(appSvcRelatedInfoDto.getAppSvcSectionLeaderList());
        List<AppSvcPersonnelDto> oldAppSvcSectionLeaderList = IaisCommonUtils.getList(oldAppSvcRelatedInfoDto.getAppSvcSectionLeaderList());
        oldAppSvcSectionLeaderList = dealSvcPersonnel(appSvcSectionLeaderList, oldAppSvcSectionLeaderList);
        oldAppSvcRelatedInfoDto.setAppSvcSectionLeaderList(oldAppSvcSectionLeaderList);
        appSvcRelatedInfoDto.setAppSvcSectionLeaderList(appSvcSectionLeaderList);
        //new service for cr
        List<AppSvcVehicleDto> appSvcVehicleDtoList = IaisCommonUtils.getList(appSvcRelatedInfoDto.getAppSvcVehicleDtoList());
        List<AppSvcVehicleDto> oldAppSvcVehicleDtoList = IaisCommonUtils.getList(oldAppSvcRelatedInfoDto.getAppSvcVehicleDtoList());
        dealVehicle(appSvcVehicleDtoList, oldAppSvcVehicleDtoList);
        appSvcRelatedInfoDto.setAppSvcVehicleDtoList(appSvcVehicleDtoList);
        oldAppSvcRelatedInfoDto.setAppSvcVehicleDtoList(oldAppSvcVehicleDtoList);
        // Charges
        AppSvcChargesPageDto appSvcChargesPageDto = appSvcRelatedInfoDto.getAppSvcChargesPageDto();
        if (appSvcChargesPageDto == null) {
            appSvcChargesPageDto = new AppSvcChargesPageDto();
        }
        AppSvcChargesPageDto oldAppSvcChargesPageDto = oldAppSvcRelatedInfoDto.getAppSvcChargesPageDto();
        if (oldAppSvcChargesPageDto == null) {
            oldAppSvcChargesPageDto = new AppSvcChargesPageDto();
        }
        dealCharges(appSvcChargesPageDto, oldAppSvcChargesPageDto);
        appSvcRelatedInfoDto.setAppSvcChargesPageDto(appSvcChargesPageDto);
        oldAppSvcRelatedInfoDto.setAppSvcChargesPageDto(oldAppSvcChargesPageDto);
        // Supplementary Form
        List<AppSvcSuplmFormDto> appSvcSuplmFormList = appSvcRelatedInfoDto.getAppSvcSuplmFormList();
        List<AppSvcSuplmFormDto> oldAppSvcSuplmFormList = oldAppSvcRelatedInfoDto.getAppSvcSuplmFormList();
        dealAppSvcSuplmFormList(appSvcSuplmFormList, oldAppSvcSuplmFormList);
    }

    private void dealAppSvcSuplmFormList(List<AppSvcSuplmFormDto> appSvcSuplmFormList, List<AppSvcSuplmFormDto> oldAppSvcSuplmFormList) {

    }

    private void dealAppSvcSuplmForm(AppSvcSuplmFormDto appSvcSuplmForm, AppSvcSuplmFormDto oldAppSvcSuplmForm) {
        appSvcSuplmForm.checkDisplay();
        List<AppSvcSuplmItemDto> appSvcSuplmItemList = appSvcSuplmForm.getAppSvcSuplmItemListByCon(dto -> dto.isDisplay());
        oldAppSvcSuplmForm.checkDisplay();
        List<AppSvcSuplmItemDto> oldAppSvcSuplmItemList = oldAppSvcSuplmForm.getAppSvcSuplmItemListByCon(dto -> dto.isDisplay());
        oldAppSvcSuplmItemList = dealList(appSvcSuplmItemList, oldAppSvcSuplmItemList,
                (newDto, oldDto) -> Objects.equals(newDto.getItemConfigId(), oldDto.getItemConfigId()),
            dto -> {
                AppSvcSuplmItemDto newDto = new AppSvcSuplmItemDto();
                newDto.setItemConfigDto(newDto.getItemConfigDto());
                newDto.setLevel(dto.getLevel());
                newDto.setDisplay(true);
                return newDto;
            }, Comparator.comparingInt(o -> o.getItemConfigDto().getSeqNum()));
        //appSvcSuplmForm
    }

    private void dealAppPremSpecialisedDtoList(List<AppPremSpecialisedDto> appPremSpecialisedDtoList, List<AppPremSpecialisedDto> oldAppPremSpecialisedDtoList) {
        int size = appPremSpecialisedDtoList.size();
        for (int i = 0; i < size; i++) {
            dealAppPremSpecialisedDto(appPremSpecialisedDtoList.get(i), oldAppPremSpecialisedDtoList.get(i));
        }
    }

    private void dealAppPremSpecialisedDto(AppPremSpecialisedDto appPremSpecialisedDto, AppPremSpecialisedDto oldAppPremSpecialisedDto) {
        List<AppPremScopeDto> appPremScopeDtoList = appPremSpecialisedDto.getCheckedAppPremScopeDtoList();
        List<AppPremScopeDto> oldAppPremScopeDtoList = oldAppPremSpecialisedDto.getCheckedAppPremScopeDtoList();
        oldAppPremScopeDtoList = dealList(appPremScopeDtoList, oldAppPremScopeDtoList,
                (newDto, oldDto) -> Objects.equals(newDto.getScopeName(), oldDto.getScopeName()), dto -> new AppPremScopeDto(),
                null);
        appPremSpecialisedDto.setAllAppPremScopeDtoList(appPremScopeDtoList);
        oldAppPremSpecialisedDto.setAllAppPremScopeDtoList(oldAppPremScopeDtoList);

        List<AppPremSubSvcRelDto> appPremSubSvcRelDtoList = appPremSpecialisedDto.getCheckedAppPremSubSvcRelDtoList();
        List<AppPremSubSvcRelDto> oldAppPremSubSvcRelDtoList = oldAppPremSpecialisedDto.getCheckedAppPremSubSvcRelDtoList();
        oldAppPremSubSvcRelDtoList = dealList(appPremSubSvcRelDtoList, oldAppPremSubSvcRelDtoList,
                (newDto, oldDto) -> Objects.equals(newDto.getSvcCode(), oldDto.getSvcCode()), dto -> new AppPremSubSvcRelDto(),
                null);
        appPremSpecialisedDto.setAllAppPremSubSvcRelDtoList(appPremSubSvcRelDtoList);
        oldAppPremSpecialisedDto.setAllAppPremSubSvcRelDtoList(oldAppPremSubSvcRelDtoList);
    }

    private List<AppSvcPersonnelDto> dealSvcPersonnel(List<AppSvcPersonnelDto> newList,
            List<AppSvcPersonnelDto> oldList) {
        return dealList(newList, oldList, (newDto, oldDto) -> Objects.equals(newDto.getPersonnelKey(), oldDto.getPersonnelKey()),
                dto -> {
                    AppSvcPersonnelDto newDto = new AppSvcPersonnelDto();
                    newDto.setPersonnelType(dto.getPersonnelType());
                    return newDto;
                }, null);
    }

    private void dealKeyPersonnel(AppSvcRelatedInfoDto appSvcRelatedInfoDto, AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto,
            String psnType) {
        List<AppSvcPrincipalOfficersDto> newList = ApplicationHelper.getKeyPersonnel(psnType, appSvcRelatedInfoDto);
        List<AppSvcPrincipalOfficersDto> oldList = ApplicationHelper.getKeyPersonnel(psnType, oldAppSvcRelatedInfoDto);
        oldList = dealKeyPersonnel(newList, oldList);
        ApplicationHelper.setKeyPersonnel(newList, psnType, appSvcRelatedInfoDto);
        ApplicationHelper.setKeyPersonnel(oldList, psnType, oldAppSvcRelatedInfoDto);
    }

    private List<AppSvcPrincipalOfficersDto> dealKeyPersonnel(List<AppSvcPrincipalOfficersDto> newList,
            List<AppSvcPrincipalOfficersDto> oldList) {
        return dealList(newList, oldList, (newDto, oldDto) -> Objects.equals(newDto.getAssignSelect(), oldDto.getAssignSelect()),
                dto -> {
                    AppSvcPrincipalOfficersDto newDto = new AppSvcPrincipalOfficersDto();
                    newDto.setPsnType(dto.getPsnType());
                    return newDto;
                }, null);
    }

    private <T> List<T> dealList(List<T> newList, List<T> oldList, BiPredicate<T, T> check, Function<T, T> newFun,
            Comparator<T> comparator) {
        int newSize = newList.size();
        int oldSize = oldList.size();
        if (newSize == oldSize) {
            return oldList;
        }
        List<T> newOldList = IaisCommonUtils.genNewArrayList(Math.max(newSize, oldSize));
        for (T personnel : newList) {
            T old = oldList.stream()
                    .filter(dto -> check.test(dto, personnel))
                    .findAny()
                    .orElse(null);
            if (old == null) {
                old = newFun.apply(personnel);
            } else {
                oldList.remove(old);
            }
            newOldList.add(old);
        }
        for (T personnel : oldList) {
            T newDto = newFun.apply(personnel);
            newList.add(newDto);
            newOldList.add(newDto);
        }
        if (comparator != null) {
            newList.sort(comparator);
            newOldList.sort(comparator);
        }
        return newOldList;
    }

    private void dealAppGrpPremisesList(List<AppGrpPremisesDto> appGrpPremisesDtoList, List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        if (appGrpPremisesDtoList.size() < oldAppGrpPremisesDtoList.size()) {
            creatNewPremise(appGrpPremisesDtoList, oldAppGrpPremisesDtoList);
        } else if (oldAppGrpPremisesDtoList.size() < appGrpPremisesDtoList.size()) {
            creatNewPremise(oldAppGrpPremisesDtoList, appGrpPremisesDtoList);
        }
        int size = appGrpPremisesDtoList.size();
        for (int i = 0; i < size; i++) {
            AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.get(i);
            AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtoList.get(i);
            // floor and unit
            List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = getOperationalUnitDtos(appGrpPremisesDto);
            List<AppPremisesOperationalUnitDto> oldAppPremisesOperationalUnitDtos = getOperationalUnitDtos(oldAppGrpPremisesDto);
            oldAppPremisesOperationalUnitDtos = dealFloorAndUnit(appPremisesOperationalUnitDtos, oldAppPremisesOperationalUnitDtos);
            reSetFloorAndUnit(appGrpPremisesDto, appPremisesOperationalUnitDtos);
            reSetFloorAndUnit(oldAppGrpPremisesDto, oldAppPremisesOperationalUnitDtos);
            // Co-Location Service
            dealCoLocation(appGrpPremisesDto, oldAppGrpPremisesDto);
        }
    }

    private void dealCoLocation(AppGrpPremisesDto appGrpPremisesDto, AppGrpPremisesDto oldAppGrpPremisesDto) {
        List<AppPremNonLicRelationDto> appPremNonLicRelationDtos = appGrpPremisesDto.getAppPremNonLicRelationDtos();
        List<AppPremNonLicRelationDto> oldAppPremNonLicRelationDtos = oldAppGrpPremisesDto.getAppPremNonLicRelationDtos();
        oldAppPremNonLicRelationDtos = dealList(appPremNonLicRelationDtos, oldAppPremNonLicRelationDtos, (newDto, oldDto) ->
                        Objects.equals(newDto.getBusinessName(), oldDto.getBusinessName())
                                && Objects.equals(newDto.getProvidedService(), oldDto.getProvidedService()),
                dto -> new AppPremNonLicRelationDto(), null);
        appGrpPremisesDto.setAppPremNonLicRelationDtos(appPremNonLicRelationDtos);
        oldAppGrpPremisesDto.setAppPremNonLicRelationDtos(oldAppPremNonLicRelationDtos);
    }

    private void dealFloorAndUnits(List<AppGrpPremisesDto> appGrpPremisesDtoList, List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
            List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = getOperationalUnitDtos(appGrpPremisesDtoList.get(i));
            List<AppPremisesOperationalUnitDto> oldAppPremisesOperationalUnitDtos =
                    getOperationalUnitDtos(oldAppGrpPremisesDtoList.get(i));
            oldAppPremisesOperationalUnitDtos = dealFloorAndUnit(appPremisesOperationalUnitDtos, oldAppPremisesOperationalUnitDtos);
            reSetFloorAndUnit(appGrpPremisesDtoList.get(i), appPremisesOperationalUnitDtos);
            reSetFloorAndUnit(oldAppGrpPremisesDtoList.get(i), oldAppPremisesOperationalUnitDtos);
        }
    }

    private void reSetFloorAndUnit(AppGrpPremisesDto appGrpPremisesDto, List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos) {
        AppPremisesOperationalUnitDto dto = appPremisesOperationalUnitDtos.get(0);
        appGrpPremisesDto.setFloorNo(dto.getFloorNo());
        appGrpPremisesDto.setUnitNo(dto.getUnitNo());
        appPremisesOperationalUnitDtos.remove(0);
        appGrpPremisesDto.setAppPremisesOperationalUnitDtos(appPremisesOperationalUnitDtos);
    }

    /**
     * Deal Floor and Unit
     *
     * @param currOpeUnitDtos
     * @param oldOpeUnitDtos
     * @return new old operational unit dto list
     */
    private List<AppPremisesOperationalUnitDto> dealFloorAndUnit(List<AppPremisesOperationalUnitDto> currOpeUnitDtos,
            List<AppPremisesOperationalUnitDto> oldOpeUnitDtos) {
        int n1 = currOpeUnitDtos.size();
        int n2 = oldOpeUnitDtos.size();
        List<AppPremisesOperationalUnitDto> newOldList = IaisCommonUtils.genNewArrayList(Math.max(n1, n2));
        for (int i = 0; i < n1; i++) {
            AppPremisesOperationalUnitDto dto = currOpeUnitDtos.get(i);
            AppPremisesOperationalUnitDto opeUnitDto = getNewOpeUnitDto(dto, oldOpeUnitDtos);
            if (opeUnitDto == null) {
                AppPremisesOperationalUnitDto premisesOperationalUnitDto = new AppPremisesOperationalUnitDto();
                premisesOperationalUnitDto.setFloorNo("");
                premisesOperationalUnitDto.setUnitNo("");
                premisesOperationalUnitDto.setPremType(dto.getPremType());
                newOldList.add(premisesOperationalUnitDto);
            } else {
                newOldList.add(opeUnitDto);
                oldOpeUnitDtos.remove(opeUnitDto);
            }
        }
        /*for (int i = 0; i < n1; i++) {
            AppPremisesOperationalUnitDto dto = newOldList.get(i);
            if ("".equals(dto.getFloorNo()) && "".equals(dto.getUnitNo()) && !oldOpeUnitDtos.isEmpty()) {
                newOldList.set(i, oldOpeUnitDtos.get(0));
                oldOpeUnitDtos.remove(0);
            }
        }*/
        n2 = oldOpeUnitDtos.size();
        for (int i = 0; i < n2; i++) {
            AppPremisesOperationalUnitDto oldDto = oldOpeUnitDtos.get(i);
            AppPremisesOperationalUnitDto premisesOperationalUnitDto = new AppPremisesOperationalUnitDto();
            premisesOperationalUnitDto.setFloorNo("");
            premisesOperationalUnitDto.setUnitNo("");
            premisesOperationalUnitDto.setPremType(oldDto.getPremType());
            currOpeUnitDtos.add(premisesOperationalUnitDto);
            newOldList.add(oldDto);
        }
        return newOldList;
    }

    private AppPremisesOperationalUnitDto getNewOpeUnitDto(AppPremisesOperationalUnitDto originalDto,
            List<AppPremisesOperationalUnitDto> opeUnitDtos) {
        return opeUnitDtos.parallelStream()
                .filter(dto -> Objects.equals(dto.getUnitNo(), originalDto.getUnitNo())
                        && Objects.equals(dto.getFloorNo(), originalDto.getFloorNo()))
                .findAny()
                .orElse(null);
    }

    private List<AppPremisesOperationalUnitDto> getOperationalUnitDtos(AppGrpPremisesDto appGrpPremisesDto) {
        List<AppPremisesOperationalUnitDto> operationalUnitDtos =
                IaisCommonUtils.getList(appGrpPremisesDto.getAppPremisesOperationalUnitDtos());
        AppPremisesOperationalUnitDto currDto = new AppPremisesOperationalUnitDto();
        currDto.setFloorNo(appGrpPremisesDto.getFloorNo());
        currDto.setUnitNo(appGrpPremisesDto.getUnitNo());
        operationalUnitDtos.add(0, currDto);
        return operationalUnitDtos;
    }

    private void dealCharges(AppSvcChargesPageDto appSvcChargesPageDto, AppSvcChargesPageDto oldAppSvcChargesPageDto) {
        List<AppSvcChargesDto> chargesDtos = IaisCommonUtils.getList(appSvcChargesPageDto.getGeneralChargesDtos());
        List<AppSvcChargesDto> oldChargesDtos = IaisCommonUtils.getList(oldAppSvcChargesPageDto.getGeneralChargesDtos());
        int maxSize = Math.max(chargesDtos.size(), oldChargesDtos.size());
        for (int i = 0, len = maxSize - chargesDtos.size(); i < len; i++) {
            chargesDtos.add(new AppSvcChargesDto());
        }
        for (int i = 0, len = maxSize - oldChargesDtos.size(); i < len; i++) {
            oldChargesDtos.add(new AppSvcChargesDto());
        }
        appSvcChargesPageDto.setGeneralChargesDtos(chargesDtos);
        oldAppSvcChargesPageDto.setGeneralChargesDtos(oldChargesDtos);
        List<AppSvcChargesDto> otherChargesDtos = IaisCommonUtils.getList(appSvcChargesPageDto.getOtherChargesDtos());
        List<AppSvcChargesDto> oldOtherChargesDtos = IaisCommonUtils.getList(oldAppSvcChargesPageDto.getOtherChargesDtos());
        maxSize = Math.max(otherChargesDtos.size(), oldOtherChargesDtos.size());
        for (int i = 0, len = maxSize - otherChargesDtos.size(); i < len; i++) {
            otherChargesDtos.add(new AppSvcChargesDto());
        }
        for (int i = 0, len = maxSize - oldOtherChargesDtos.size(); i < len; i++) {
            oldOtherChargesDtos.add(new AppSvcChargesDto());
        }
        appSvcChargesPageDto.setOtherChargesDtos(otherChargesDtos);
        oldAppSvcChargesPageDto.setOtherChargesDtos(oldOtherChargesDtos);
    }

    private void dealMapSvcDoc( Map<String, List<AppSvcDocDto>> multipleSvcDoc, Map<String, List<AppSvcDocDto>> oldMultipleSvcDoc){
        Set<String> strings = multipleSvcDoc.keySet();
        Set<String> oldSet = oldMultipleSvcDoc.keySet();
        Set<String> difference = difference(strings, oldSet);
        for(String v: difference){
            List<AppSvcDocDto> appSvcDocDtos = multipleSvcDoc.get(v);
            if(appSvcDocDtos==null){
                multipleSvcDoc.put(v,new ArrayList<>(10));
            }
            List<AppSvcDocDto> oldAppSvcDocDtos = oldMultipleSvcDoc.get(v);
            if(oldAppSvcDocDtos==null){
                oldMultipleSvcDoc.put(v,new ArrayList<>(10));
            }
        }
    }

    private <T> Set difference(Set<T> set,Set<T> oldSet){
        Set<T> s=new HashSet<>();
        s.addAll(set);
        s.addAll(oldSet);
        return s;
    }

    private void dealWithMultipleDoc(AppSubmissionDto appSubmissionDto) {
        /*AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        Map<String, List<AppSvcDocDto>> multipleSvcDoc = appSvcRelatedInfoDto.getMultipleSvcDoc();
        if(multipleSvcDoc==null){
            multipleSvcDoc=new LinkedHashMap<>();
        }
        groupWithSvcDoc(appSvcDocDtoLit,multipleSvcDoc);
        //appSvcRelatedInfoDto.setMultipleSvcDoc(multipleSvcDoc);
        if(appSubmissionDto.getOldAppSubmissionDto()!=null){
            dealWithMultipleDoc(appSubmissionDto.getOldAppSubmissionDto());
        }*/
    }

    private void groupWithSvcDoc(List<AppSvcDocDto> appSvcDocDtoLit, Map<String, List<AppSvcDocDto>> multipleSvcDoc){
        if(appSvcDocDtoLit==null){
            return;
        }
        ListIterator<AppSvcDocDto> iterator = appSvcDocDtoLit.listIterator();
        while (iterator.hasNext()){
            AppSvcDocDto next = iterator.next();
            String personType = next.getPersonType();
            int i = checkPersonType(personType);
            String svcDocId = next.getSvcDocId();
            Integer personTypeNum = next.getPersonTypeNum();
            if(1==i){
                String appGrpPersonId = next.getAppGrpPersonId();
                if(appGrpPersonId==null){
                    log.error(StringUtil.changeForLog("this have error file ,need to remove----> "+next));
                    iterator.remove();
                }else {
                    docDealWith(multipleSvcDoc,next,personType+svcDocId+":"+personTypeNum);
                }
            }else if(2==i){
                String appSvcPersonId = next.getAppSvcPersonId();
                if(appSvcPersonId==null){
                    log.error(StringUtil.changeForLog("this have error file ,need to remove----> "+next));
                    iterator.remove();
                }else {
                    docDealWith(multipleSvcDoc,next,personType+svcDocId+":"+personTypeNum);
                }
            }else {
                docDealWith(multipleSvcDoc,next,personType+svcDocId+":"+personTypeNum);
            }
        }
    }

    private Map<String, List<AppSvcDocDto>> translateForShow(Map<String, List<AppSvcDocDto>> multipleSvcDoc,
            String serviceId) {
        log.info(StringUtil.changeForLog("The translateForShow start ..."));
        Map<String, List<AppSvcDocDto>> result = IaisCommonUtils.genNewLinkedHashMap();
        Map<String, List<AppSvcDocDto>> temp = IaisCommonUtils.genNewLinkedHashMap();
        Map<String,Integer> nums = IaisCommonUtils.genNewHashMap();
        if (multipleSvcDoc != null && !multipleSvcDoc.isEmpty()) {
            List<HcsaSvcDocConfigDto> docConfigDtos = getAllHcsaSvcDocs(serviceId);
            Collections.sort(docConfigDtos, Comparator.comparing(HcsaSvcDocConfigDto::getDispOrder));
            for (HcsaSvcDocConfigDto docConfig : docConfigDtos) {
                String svcDocId = docConfig.getId();
                List<String> keys = multipleSvcDoc.keySet().stream()
                        .filter(k -> k.indexOf(svcDocId) >= 0)
                        .sorted()
                        .collect(Collectors.toList());
                for (String key : keys) {
                    temp.put(key, multipleSvcDoc.get(key));
                }
            }
            temp.forEach((k,appSvcDocDtos)->{
                if (!IaisCommonUtils.isEmpty(appSvcDocDtos)) {
                    AppSvcDocDto appSvcDocDto = appSvcDocDtos.get(0);
                    String  personType = k.substring(0,k.indexOf(':'));
                    log.info(StringUtil.changeForLog("The translateForShow personType is -->:"+personType));
                    Integer num = nums.get(personType);
                    if(num == null){
                        num = 1;
                    }else{
                        num = num + 1;
                    }
                    nums.put(personType,num);
                    String newKey = dealWithSvcDoc(appSvcDocDto,num);
                    log.info(StringUtil.changeForLog("The translateForShow num is -->:"+num));
                    log.info(StringUtil.changeForLog("The translateForShow newKey is -->:"+newKey));
                    result.put(newKey,appSvcDocDtos);
                }
            });
        }
        log.info(StringUtil.changeForLog("The translateForShow end ..."));
        return result;
    }

    private String dealWithSvcDoc(AppSvcDocDto appSvcDocDto, Integer num) {
        String svcDocId = appSvcDocDto.getSvcDocId();
        HcsaSvcDocConfigDto entity = hcsaConfigClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
        String upFileName = appSvcDocDto.getUpFileName();
        if (upFileName != null && entity != null) {
            entity.setDocTitle(upFileName);
        }
        return "";
        //return ApplicationHelper.getDocDisplayTitle(entity, num);
    }

    private void docDealWith(Map<String, List<AppSvcDocDto>> multipleSvcDoc,AppSvcDocDto v,String key){
        List<AppSvcDocDto> appSvcDocDtos = multipleSvcDoc.get(key);
        if(appSvcDocDtos==null){
            appSvcDocDtos=new ArrayList<>();
            appSvcDocDtos.add(v);
            multipleSvcDoc.put(key,appSvcDocDtos);
        }else {
            appSvcDocDtos.add(v);
        }
    }

    private void appSvcPrincipalOfficersDto( List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList, List<AppSvcPrincipalOfficersDto> olAppSvcPrincipalOfficersDtoList){
        if (IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtoList) && !IaisCommonUtils.isEmpty(olAppSvcPrincipalOfficersDtoList)) {
            appSvcPrincipalOfficersDtoList = new ArrayList<>(olAppSvcPrincipalOfficersDtoList.size());
            for (int i = 0; i < olAppSvcPrincipalOfficersDtoList.size(); i++) {
                AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
                appSvcPrincipalOfficersDto.setDescription("");
                appSvcPrincipalOfficersDto.setOfficeTelNo("");
                appSvcPrincipalOfficersDto.setName("");
                appSvcPrincipalOfficersDto.setSalutation("");
                appSvcPrincipalOfficersDto.setIdType("");
                appSvcPrincipalOfficersDto.setDesignation("");
                appSvcPrincipalOfficersDto.setMobileNo("");
                appSvcPrincipalOfficersDto.setEmailAddr("");
                appSvcPrincipalOfficersDtoList.add(appSvcPrincipalOfficersDto);
            }

        } else {
            int size = appSvcPrincipalOfficersDtoList.size();
            int oldSize = olAppSvcPrincipalOfficersDtoList.size();
            if (size < oldSize) {
                for(int i = 0;i < oldSize-size;i++){
                    AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();

                    appSvcPrincipalOfficersDto.setDescription("");
                    appSvcPrincipalOfficersDto.setOfficeTelNo("");
                    appSvcPrincipalOfficersDto.setName("");
                    appSvcPrincipalOfficersDto.setSalutation("");
                    appSvcPrincipalOfficersDto.setIdType("");
                    appSvcPrincipalOfficersDto.setDesignation("");
                    appSvcPrincipalOfficersDto.setMobileNo("");
                    appSvcPrincipalOfficersDto.setEmailAddr("");
                    appSvcPrincipalOfficersDtoList.add(appSvcPrincipalOfficersDto);
                }
            }else  if(oldSize<size){
                for(int i=0;i<size-oldSize;i++){
                    AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();

                    appSvcPrincipalOfficersDto.setDescription("");
                    appSvcPrincipalOfficersDto.setOfficeTelNo("");
                    appSvcPrincipalOfficersDto.setName("");
                    appSvcPrincipalOfficersDto.setSalutation("");
                    appSvcPrincipalOfficersDto.setIdType("");
                    appSvcPrincipalOfficersDto.setDesignation("");
                    appSvcPrincipalOfficersDto.setMobileNo("");
                    appSvcPrincipalOfficersDto.setEmailAddr("");
                    olAppSvcPrincipalOfficersDtoList.add(appSvcPrincipalOfficersDto);
                }
            }
        }
    }

    /*private void appSvcPersonnelDtoList(List<AppSvcPersonnelDto> appSvcPersonnelDtoList,
            List<AppSvcPersonnelDto> oldAppSvcPersonnelDtoList) {
        int m = appSvcPersonnelDtoList.size();
        int n = oldAppSvcPersonnelDtoList.size();
        int len = Math.max(m, n);
        List<AppSvcPersonnelDto> newList = IaisCommonUtils.genNewArrayList(len);
        List<AppSvcPersonnelDto> oldList = IaisCommonUtils.genNewArrayList(len);
        for (AppSvcPersonnelDto svcPersonnelDto : appSvcPersonnelDtoList) {
            newList.add(svcPersonnelDto);
            oldList.add(oldAppSvcPersonnelDtoList.stream()
                    .filter(dto -> Objects.equals(dto.getName(), svcPersonnelDto.getName())
                            && Objects.equals(dto.getPersonnelType(), svcPersonnelDto.getPersonnelType()))
                    .findAny()
                    .orElseGet(() -> {
                        AppSvcPersonnelDto appSvcPersonnelDto = new AppSvcPersonnelDto();
                        appSvcPersonnelDto.setPersonnelType(svcPersonnelDto.getPersonnelType());
                        appSvcPersonnelDto.setDesignation("");
                        appSvcPersonnelDto.setName("");
                        appSvcPersonnelDto.setProfRegNo("");
                        appSvcPersonnelDto.setQualification("");
                        return appSvcPersonnelDto;
                    }));
        }
        oldAppSvcPersonnelDtoList.removeAll(oldList);
        n = oldAppSvcPersonnelDtoList.size();
        for (int i = 0; i < n; i++) {
            AppSvcPersonnelDto svcPersonnelDto = oldAppSvcPersonnelDtoList.get(i);
            oldList.add(svcPersonnelDto);
            AppSvcPersonnelDto appSvcPersonnelDto = new AppSvcPersonnelDto();
            appSvcPersonnelDto.setPersonnelType(svcPersonnelDto.getPersonnelType());
            appSvcPersonnelDto.setDesignation("");
            appSvcPersonnelDto.setName("");
            appSvcPersonnelDto.setProfRegNo("");
            appSvcPersonnelDto.setQualification("");
            newList.add(appSvcPersonnelDto);
        }
        appSvcPersonnelDtoList.clear();
        oldAppSvcPersonnelDtoList.clear();
        appSvcPersonnelDtoList.addAll(newList);
        oldAppSvcPersonnelDtoList.addAll(oldList);
    }*/

    private static void removeSvcDocFileIdIsNull( List<AppSvcDocDto> appSvcDocDtoLit){
        if(appSvcDocDtoLit==null){
            return;
        }
        ListIterator<AppSvcDocDto> appSvcDocDtoListIterator = appSvcDocDtoLit.listIterator();
        while(appSvcDocDtoListIterator.hasNext()){
            AppSvcDocDto next = appSvcDocDtoListIterator.next();
            String fileRepoId = next.getFileRepoId();
            if(fileRepoId==null){
                appSvcDocDtoListIterator.remove();
            }
        }
    }

    private void creatNewPremise( List<AppGrpPremisesDto> appGrpPremisesDtoList, List<AppGrpPremisesDto> oldAppGrpPremisesDtoList){
        int size = appGrpPremisesDtoList.size();
        int oldSize = oldAppGrpPremisesDtoList.size();
        for (int i = 0; i <oldSize - size; i++) {
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            appGrpPremisesDto.setPremisesType(oldAppGrpPremisesDtoList.get(size+i).getPremisesType());
            /*appGrpPremisesDto.setPostalCode("");
            appGrpPremisesDto.setScdfRefNo("");
            appGrpPremisesDto.setCertIssuedDtStr("");
            appGrpPremisesDto.setStreetName("");
            appGrpPremisesDto.setBlkNo("");
            appGrpPremisesDto.setUnitNo("");
            appGrpPremisesDto.setFloorNo("");
            appGrpPremisesDto.setHciName("");
            appGrpPremisesDto.setAddrType("");
            appGrpPremisesDto.setBuildingName("");
            appGrpPremisesDto.setEasMtsPubHotline("");
            appGrpPremisesDto.setEasMtsPubEmail("");
            appGrpPremisesDto.setEasMtsUseOnly("");*/
            appGrpPremisesDtoList.add(appGrpPremisesDto);
        }
    }

    private void creatCgo(List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList,List<AppSvcPrincipalOfficersDto>oldAppSvcCgoDtoList){
        int size = appSvcCgoDtoList.size();
        int oldSize = oldAppSvcCgoDtoList.size();
        for(int i=0;i<oldSize-size;i++){
            AppSvcPrincipalOfficersDto appSvcCgoDto = generateCgo();
            appSvcCgoDtoList.add(appSvcCgoDto);
        }
    }

    private void copyServiceDoc(List<AppSvcDocDto> appSvcDocDtoLit,List<AppSvcDocDto> oldAppSvcDocDtoLit){
        removeSvcDocFileIdIsNull(appSvcDocDtoLit);
        removeSvcDocFileIdIsNull(oldAppSvcDocDtoLit);
        if(appSvcDocDtoLit==null&&oldAppSvcDocDtoLit!=null){
            appSvcDocDtoLit=new ArrayList<>(oldAppSvcDocDtoLit.size());
            for(AppSvcDocDto appSvcDocDto : oldAppSvcDocDtoLit){
                AppSvcDocDto svcDocDto=new AppSvcDocDto();
                svcDocDto.setSvcDocId(appSvcDocDto.getSvcDocId());
                svcDocDto.setFileRepoId(appSvcDocDto.getFileRepoId());
                svcDocDto.setSeqNum(appSvcDocDto.getSeqNum());
                appSvcDocDtoLit.add(svcDocDto);
            }
        }else if(appSvcDocDtoLit!=null&&oldAppSvcDocDtoLit==null){
            oldAppSvcDocDtoLit=new ArrayList<>(appSvcDocDtoLit.size());
            for(AppSvcDocDto appSvcDocDto : appSvcDocDtoLit){
                AppSvcDocDto svcDocDto=new AppSvcDocDto();
                svcDocDto.setSvcDocId(appSvcDocDto.getSvcDocId());
                svcDocDto.setFileRepoId(appSvcDocDto.getFileRepoId());
                svcDocDto.setSeqNum(appSvcDocDto.getSeqNum());
                oldAppSvcDocDtoLit.add(svcDocDto);
            }
        }else if(appSvcDocDtoLit!=null&&oldAppSvcDocDtoLit!=null){
            serviceDoc(appSvcDocDtoLit,oldAppSvcDocDtoLit);
        }
    }
    private void serviceDoc(List<AppSvcDocDto> appSvcDocDtos,  List<AppSvcDocDto> oldAppSvcDocDtos){
        Set<Integer> set=new TreeSet<>();
        appSvcDocDtos.forEach((v)->{
            set.add(v.getSeqNum());
            String upFileName = v.getUpFileName();
            if(upFileName==null){
                HcsaSvcDocConfigDto entity = hcsaConfigClient.getHcsaSvcDocConfigDtoById(v.getSvcDocId()).getEntity();
                v.setUpFileName(entity.getDocTitle());
            }
        });
        oldAppSvcDocDtos.forEach((v)->{
            set.add(v.getSeqNum());
            String upFileName = v.getUpFileName();
            if(upFileName==null){
                HcsaSvcDocConfigDto entity = hcsaConfigClient.getHcsaSvcDocConfigDtoById(v.getSvcDocId()).getEntity();
                v.setUpFileName(entity.getDocTitle());
            }
        });
        List<AppSvcDocDto> n=new ArrayList<>(appSvcDocDtos.size());
        List<AppSvcDocDto> o=new ArrayList<>(oldAppSvcDocDtos.size());
        for(Integer v: set){
            boolean flag=false;
            boolean flag1=flag;
            for(AppSvcDocDto appSvcDocDto : appSvcDocDtos){
                Integer seqNum = appSvcDocDto.getSeqNum();
                if(v.equals(seqNum)){
                    n.add(appSvcDocDto);
                    flag=true;
                }
            }
            for(AppSvcDocDto appSvcDocDto : oldAppSvcDocDtos){
                Integer seqNum = appSvcDocDto.getSeqNum();
                if(v.equals(seqNum)){
                    o.add(appSvcDocDto);
                    flag1=true;
                }
            }
            if(flag&&!flag1){
                AppSvcDocDto appSvcDocDto = n.get(n.size() - 1);
                AppSvcDocDto svcDocDto =new AppSvcDocDto();
                svcDocDto.setSvcDocId(appSvcDocDto.getSvcDocId());
                svcDocDto.setFileRepoId(appSvcDocDto.getFileRepoId());
                svcDocDto.setUpFileName(appSvcDocDto.getUpFileName());
                svcDocDto.setSeqNum(appSvcDocDto.getSeqNum());
                o.add(svcDocDto);
            }
            if(!flag&&flag1){
                AppSvcDocDto appSvcDocDto = o.get(o.size() - 1);
                AppSvcDocDto svcDocDto =new AppSvcDocDto();
                svcDocDto.setSvcDocId(appSvcDocDto.getSvcDocId());
                svcDocDto.setFileRepoId(appSvcDocDto.getFileRepoId());
                svcDocDto.setUpFileName(appSvcDocDto.getUpFileName());
                svcDocDto.setSeqNum(appSvcDocDto.getSeqNum());
                n.add(svcDocDto);
            }
        }
        appSvcDocDtos.clear();
        appSvcDocDtos.addAll(n);
        oldAppSvcDocDtos.clear();
        oldAppSvcDocDtos.addAll(o);
    }

//    private void premiseTrans(AppSubmissionDto appSubmissionDto){
//        if(appSubmissionDto==null){
//            return;
//        }
//        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
//        if(appGrpPremisesDtoList==null || appGrpPremisesDtoList.isEmpty()){
//            return;
//        }
//        for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
//            String premisesType = appGrpPremisesDto.getPremisesType();
//            if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesType)){
//                appGrpPremisesDto.setStreetName(appGrpPremisesDto.getOffSiteStreetName());
//                appGrpPremisesDto.setPostalCode(appGrpPremisesDto.getOffSitePostalCode());
//                appGrpPremisesDto.setBuildingName(appGrpPremisesDto.getOffSiteBuildingName());
//                appGrpPremisesDto.setFloorNo(appGrpPremisesDto.getOffSiteFloorNo());
//                appGrpPremisesDto.setUnitNo(appGrpPremisesDto.getOffSiteUnitNo());
//                appGrpPremisesDto.setAddrType(appGrpPremisesDto.getOffSiteAddressType());
//                appGrpPremisesDto.setBlkNo(appGrpPremisesDto.getOffSiteBlockNo());
//            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)){
//                appGrpPremisesDto.setStreetName(appGrpPremisesDto.getConveyanceStreetName());
//                appGrpPremisesDto.setPostalCode(appGrpPremisesDto.getConveyancePostalCode());
//                appGrpPremisesDto.setBuildingName(appGrpPremisesDto.getConveyanceBuildingName());
//                appGrpPremisesDto.setFloorNo(appGrpPremisesDto.getConveyanceFloorNo());
//                appGrpPremisesDto.setUnitNo(appGrpPremisesDto.getConveyanceUnitNo());
//                appGrpPremisesDto.setAddrType(appGrpPremisesDto.getConveyanceAddressType());
//                appGrpPremisesDto.setBlkNo(appGrpPremisesDto.getConveyanceBlockNo());
//            }
//        }
//
//    }

    private void premise(AppSubmissionDto appSubmissionDto,AppSubmissionDto oldAppSubmissionDto,HttpServletRequest request,ApplicationGroupDto groupDto){
        if(appSubmissionDto==null||oldAppSubmissionDto==null){
            return;
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if(appGrpPremisesDtoList==null || appGrpPremisesDtoList.isEmpty()){
            return;
        }
        String appType = appSubmissionDto.getAppType();
        List<AppGrpPremisesDto> oldAppSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDto.getAppGrpPremisesDtoList();
        if(oldAppSubmissionDtoAppGrpPremisesDtoList==null || oldAppSubmissionDtoAppGrpPremisesDtoList.isEmpty()){
            return;
        }
        for(int i=0;i<appGrpPremisesDtoList.size();i++){
            if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getStreetName())&&StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getStreetName())){

            }else {
                if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getStreetName())){
                    appGrpPremisesDtoList.get(i).setStreetName("");
                }
                if(StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getStreetName())){
                    oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).setStreetName("");
                }
            }
            if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getBlkNo())&&StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getBlkNo())){

            }else {
                if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getBlkNo())){
                    appGrpPremisesDtoList.get(i).setBlkNo("");
                }
                if(StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getBlkNo())){
                    oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).setBlkNo("");
                }

            }
            if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getPostalCode())&&StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getPostalCode())){

            }else {
                if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getPostalCode())){
                    appGrpPremisesDtoList.get(i).setPostalCode("");
                }
                if(StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getPostalCode())){
                    oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).setPostalCode("");
                }
            }
            if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getBuildingName())&&StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getBuildingName())){

            }else {
                if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getBuildingName())){
                    appGrpPremisesDtoList.get(i).setBuildingName("");
                }
                if(StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getBuildingName())){
                    oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).setBuildingName("");
                }
            }
            if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getFloorNo())&&StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getFloorNo())){

            }else {
                if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getFloorNo())){
                    appGrpPremisesDtoList.get(i).setFloorNo("");
                }
                if(StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getFloorNo())){
                    oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).setFloorNo("");
                }
            }
            if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getUnitNo())&&StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getUnitNo())){

            }else {
                if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getUnitNo())){
                    appGrpPremisesDtoList.get(i).setUnitNo("");
                }
                if(StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getUnitNo())){
                    oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).setUnitNo("");
                }
            }
            if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getAddrType())&&StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getAddrType())){

            }else {
                if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getAddrType())){
                    appGrpPremisesDtoList.get(i).setAddrType("");
                }
                if(StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getAddrType())){
                    oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).setAddrType("");
                }
            }
            if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getScdfRefNo())&&StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getScdfRefNo())){

            }else {
                if(StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getScdfRefNo())){
                    appGrpPremisesDtoList.get(i).setScdfRefNo("");
                }
                if(StringUtil.isEmpty(oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).getScdfRefNo())){
                    oldAppSubmissionDtoAppGrpPremisesDtoList.get(i).setScdfRefNo("");
                }
            }
            if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
                AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto.getAppDeclarationMessageDto();
                if(groupDto!=null&& groupDto.getGroupNo()!=null&& groupDto.getGroupNo().startsWith("AR")){
                    if(appDeclarationMessageDto!=null){
                       request.setAttribute("renew_rfc_show","Y");
                    }
                }else {
                    if(appDeclarationMessageDto!=null){
                        request.setAttribute("RFC_HCAI_NAME_CHNAGE",String.valueOf(false));
                    }
                }
            }else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
                AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto.getAppDeclarationMessageDto();
                if (appDeclarationMessageDto != null) {
                    request.setAttribute("isSingle", "Y");
                }
            }
        }
    }

    private void publicPH(List<AppGrpPremisesDto> appGrpPremisesDtoList,List<AppGrpPremisesDto> oldAppGrpPremisesDtoList){
        if(appGrpPremisesDtoList.size()!=oldAppGrpPremisesDtoList.size()){
            return;
        }/*
        for(int i=0;i<appGrpPremisesDtoList.size();i++){
            List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDtoList.get(i).getAppPremPhOpenPeriodList();
            List<AppPremPhOpenPeriodDto> oldAppPremPhOpenPeriodList = oldAppGrpPremisesDtoList.get(i).getAppPremPhOpenPeriodList();
            List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos=IaisCommonUtils.genNewArrayList();
            if(appPremPhOpenPeriodList==null&&oldAppPremPhOpenPeriodList!=null){
                for(AppPremPhOpenPeriodDto appPremPhOpenPeriod : oldAppPremPhOpenPeriodList){
                    AppPremPhOpenPeriodDto appPremPhOpenPeriodDto=new AppPremPhOpenPeriodDto();
                    appPremPhOpenPeriodDto.setDayName("");
                    appPremPhOpenPeriodDtos.add(appPremPhOpenPeriodDto);
                }
                appGrpPremisesDtoList.get(i).setAppPremPhOpenPeriodList(appPremPhOpenPeriodDtos);
            }else if(appPremPhOpenPeriodList!=null&&oldAppPremPhOpenPeriodList==null){
                for(AppPremPhOpenPeriodDto appPremPhOpenPeriod : appPremPhOpenPeriodList){
                    AppPremPhOpenPeriodDto appPremPhOpenPeriodDto=new AppPremPhOpenPeriodDto();
                    appPremPhOpenPeriodDto.setDayName("");
                    appPremPhOpenPeriodDtos.add(appPremPhOpenPeriodDto);
                }
                oldAppGrpPremisesDtoList.get(i).setAppPremPhOpenPeriodList(appPremPhOpenPeriodDtos);
            }else if(appPremPhOpenPeriodList!=null&&oldAppPremPhOpenPeriodList!=null){
                if(appPremPhOpenPeriodList.size()>oldAppPremPhOpenPeriodList.size()){
                    for(int j=0;j<appPremPhOpenPeriodList.size()-oldAppPremPhOpenPeriodList.size();j++){
                        AppPremPhOpenPeriodDto appPremPhOpenPeriodDto=new AppPremPhOpenPeriodDto();
                        appPremPhOpenPeriodDto.setDayName("");
                        appPremPhOpenPeriodDtos.add(appPremPhOpenPeriodDto);
                    }
                    oldAppPremPhOpenPeriodList.addAll(appPremPhOpenPeriodDtos);
                }else if(oldAppPremPhOpenPeriodList.size()>appPremPhOpenPeriodList.size()){
                    for(int j=0;j<oldAppPremPhOpenPeriodList.size()-appPremPhOpenPeriodList.size();j++){
                        AppPremPhOpenPeriodDto appPremPhOpenPeriodDto=new AppPremPhOpenPeriodDto();
                        appPremPhOpenPeriodDto.setDayName("");
                        appPremPhOpenPeriodDtos.add(appPremPhOpenPeriodDto);
                    }
                    appPremPhOpenPeriodList.addAll(appPremPhOpenPeriodDtos);
                }

            }

        }*/
    }

    private void event(List<AppGrpPremisesDto> appGrpPremisesDtoList,List<AppGrpPremisesDto> oldAppGrpPremisesDtoList){
        if(appGrpPremisesDtoList.size()!=oldAppGrpPremisesDtoList.size()){
            return;
        }/*
        for(int i=0;i<appGrpPremisesDtoList.size();i++){
            List<AppPremEventPeriodDto> appPremEventPeriodDtos = appGrpPremisesDtoList.get(i).getEventDtoList();
            List<AppPremEventPeriodDto> oldAppPremEventPeriodDtos = oldAppGrpPremisesDtoList.get(i).getEventDtoList();
            List<AppPremEventPeriodDto> appPremEventPeriodDtoList=IaisCommonUtils.genNewArrayList();
            if(appPremEventPeriodDtos==null&&oldAppPremEventPeriodDtos!=null){
                for(AppPremEventPeriodDto eventPeriodDto : oldAppPremEventPeriodDtos){
                    AppPremEventPeriodDto appPremEventPeriodDto=new AppPremEventPeriodDto();
                    appPremEventPeriodDto.setEventName("");
                    appPremEventPeriodDtoList.add(appPremEventPeriodDto);
                }
                appGrpPremisesDtoList.get(i).setEventDtoList(appPremEventPeriodDtoList);
            }else if(appPremEventPeriodDtos!=null&&oldAppPremEventPeriodDtos==null){
                for(AppPremEventPeriodDto eventPeriodDto : appPremEventPeriodDtos){
                    AppPremEventPeriodDto appPremEventPeriodDto=new AppPremEventPeriodDto();
                    appPremEventPeriodDto.setEventName("");
                    appPremEventPeriodDtoList.add(appPremEventPeriodDto);
                }
                oldAppGrpPremisesDtoList.get(i).setEventDtoList(appPremEventPeriodDtoList);
            }else if(appPremEventPeriodDtos!=null&&oldAppPremEventPeriodDtos!=null){
                if(appPremEventPeriodDtos.size()>oldAppPremEventPeriodDtos.size()){
                    for(int j=0;j<appPremEventPeriodDtos.size()-oldAppPremEventPeriodDtos.size();j++){
                        AppPremEventPeriodDto appPremEventPeriodDto=new AppPremEventPeriodDto();
                        appPremEventPeriodDto.setEventName("");
                        appPremEventPeriodDtoList.add(appPremEventPeriodDto);
                    }
                    oldAppPremEventPeriodDtos.addAll(appPremEventPeriodDtoList);
                }else if(oldAppPremEventPeriodDtos.size()>appPremEventPeriodDtos.size()){
                    for(int j=0;j<oldAppPremEventPeriodDtos.size()-appPremEventPeriodDtos.size();j++){
                        AppPremEventPeriodDto appPremEventPeriodDto=new AppPremEventPeriodDto();
                        appPremEventPeriodDto.setEventName("");
                        appPremEventPeriodDtoList.add(appPremEventPeriodDto);
                    }
                    appPremEventPeriodDtos.addAll(appPremEventPeriodDtoList);
                }

            }

        }*/
    }

    private void weekly(List<AppGrpPremisesDto> appGrpPremisesDtoList,List<AppGrpPremisesDto> oldAppGrpPremisesDtoList){
        if(appGrpPremisesDtoList.size()!=oldAppGrpPremisesDtoList.size()){
            return;
        }/*
        for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
            List<OperationHoursReloadDto> weeklyDtoList = appGrpPremisesDtoList.get(i).getWeeklyDtoList();
            List<OperationHoursReloadDto> oldWeeklyDtoList = oldAppGrpPremisesDtoList.get(i).getWeeklyDtoList();
            List<OperationHoursReloadDto> operationHoursReloadDtoList = IaisCommonUtils.genNewArrayList();
            if (weeklyDtoList == null && oldWeeklyDtoList != null) {
                for (OperationHoursReloadDto hoursReloadDto : oldWeeklyDtoList) {
                    operationHoursReloadDtoList.add(genOperationHoursReloadDto(hoursReloadDto));
                }
                appGrpPremisesDtoList.get(i).setWeeklyDtoList(operationHoursReloadDtoList);
            } else if (weeklyDtoList != null && oldWeeklyDtoList == null) {
                for (OperationHoursReloadDto hoursReloadDto : weeklyDtoList) {
                    operationHoursReloadDtoList.add(genOperationHoursReloadDto(hoursReloadDto));
                }
                oldAppGrpPremisesDtoList.get(i).setWeeklyDtoList(operationHoursReloadDtoList);
            } else if (weeklyDtoList != null && oldWeeklyDtoList != null) {
                if (weeklyDtoList.size() > oldWeeklyDtoList.size()) {
                    int k = oldWeeklyDtoList.size();
                    for (int j = 0; j < weeklyDtoList.size() - oldWeeklyDtoList.size(); j++) {
                        operationHoursReloadDtoList.add(genOperationHoursReloadDto(weeklyDtoList.get(k++)));
                    }
                    oldWeeklyDtoList.addAll(operationHoursReloadDtoList);
                } else if (oldWeeklyDtoList.size() > weeklyDtoList.size()) {
                    int k = weeklyDtoList.size();
                    for (int j = 0; j < oldWeeklyDtoList.size() - weeklyDtoList.size(); j++) {
                        operationHoursReloadDtoList.add(genOperationHoursReloadDto(oldWeeklyDtoList.get(k++)));
                    }
                    weeklyDtoList.addAll(operationHoursReloadDtoList);
                }

            }

        }*/
    }

    private OperationHoursReloadDto genOperationHoursReloadDto(OperationHoursReloadDto hoursReloadDto) {
        OperationHoursReloadDto operationHoursReloadDto = new OperationHoursReloadDto();
        if (hoursReloadDto != null && hoursReloadDto.isSelectAllDay()) {
            operationHoursReloadDto.setSelectAllDay(true);
            operationHoursReloadDto.setStartFrom((Time) CopyUtil.copyMutableObject(hoursReloadDto.getStartFrom()));
            operationHoursReloadDto.setEndTo((Time) CopyUtil.copyMutableObject(hoursReloadDto.getEndTo()));
        }
        return operationHoursReloadDto;
    }

    private void ph(List<AppGrpPremisesDto> appGrpPremisesDtoList,List<AppGrpPremisesDto> oldAppGrpPremisesDtoList){
        if(appGrpPremisesDtoList.size()!=oldAppGrpPremisesDtoList.size()){
            return;
        }/*
        for (int i = 0; i < appGrpPremisesDtoList.size(); i++) {
            List<OperationHoursReloadDto> weeklyDtoList = appGrpPremisesDtoList.get(i).getPhDtoList();
            List<OperationHoursReloadDto> oldWeeklyDtoList = oldAppGrpPremisesDtoList.get(i).getPhDtoList();
            List<OperationHoursReloadDto> operationHoursReloadDtoList = IaisCommonUtils.genNewArrayList();
            if (weeklyDtoList == null && oldWeeklyDtoList != null) {
                for (OperationHoursReloadDto hoursReloadDto : oldWeeklyDtoList) {
                    operationHoursReloadDtoList.add(genOperationHoursReloadDto(hoursReloadDto));
                }
                appGrpPremisesDtoList.get(i).setPhDtoList(operationHoursReloadDtoList);
            } else if (weeklyDtoList != null && oldWeeklyDtoList == null) {
                for (OperationHoursReloadDto hoursReloadDto : weeklyDtoList) {
                    operationHoursReloadDtoList.add(genOperationHoursReloadDto(hoursReloadDto));
                }
                oldAppGrpPremisesDtoList.get(i).setPhDtoList(operationHoursReloadDtoList);
            } else if (weeklyDtoList != null && oldWeeklyDtoList != null) {
                if (weeklyDtoList.size() > oldWeeklyDtoList.size()) {
                    int k = oldWeeklyDtoList.size();
                    for (int j = 0; j < weeklyDtoList.size() - oldWeeklyDtoList.size(); j++) {
                        operationHoursReloadDtoList.add(genOperationHoursReloadDto(weeklyDtoList.get(k++)));
                    }
                    oldWeeklyDtoList.addAll(operationHoursReloadDtoList);
                } else if (oldWeeklyDtoList.size() > weeklyDtoList.size()) {
                    int k = weeklyDtoList.size();
                    for (int j = 0; j < oldWeeklyDtoList.size() - weeklyDtoList.size(); j++) {
                        operationHoursReloadDtoList.add(genOperationHoursReloadDto(oldWeeklyDtoList.get(k++)));
                    }
                    weeklyDtoList.addAll(operationHoursReloadDtoList);
                }
            }

        }*/
    }

    private AppSvcPrincipalOfficersDto  generatePo(){
        AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = new AppSvcPrincipalOfficersDto();
        appSvcPrincipalOfficersDto.setOfficeTelNo("");
        appSvcPrincipalOfficersDto.setName("");
        appSvcPrincipalOfficersDto.setSalutation("");
        appSvcPrincipalOfficersDto.setIdType("");
        appSvcPrincipalOfficersDto.setDesignation("");
        appSvcPrincipalOfficersDto.setMobileNo("");
        appSvcPrincipalOfficersDto.setEmailAddr("");
        return appSvcPrincipalOfficersDto;
    }

    private AppSvcPrincipalOfficersDto generateCgo(){
        AppSvcPrincipalOfficersDto appSvcCgoDto = new AppSvcPrincipalOfficersDto();
        appSvcCgoDto.setSpecialityOther("");
        appSvcCgoDto.setSpeciality("");
        appSvcCgoDto.setProfRegNo("");
        appSvcCgoDto.setOfficeTelNo("");
        appSvcCgoDto.setMobileNo("");
        appSvcCgoDto.setDescription("");
        appSvcCgoDto.setName("");
        appSvcCgoDto.setEmailAddr("");
        appSvcCgoDto.setIdType("");
        appSvcCgoDto.setSpeciality("");
        return appSvcCgoDto;
    }

    private void sortSvcDoc(Map<String, List<AppSvcDocDto>> multipleSvcDoc) {
        if (multipleSvcDoc != null) {
            multipleSvcDoc.forEach((k, v) -> Collections.sort(v, Comparator.comparing(AppSvcDocDto::getSeqNum)));
        }
    }

    private  void  dealVehicle( List<AppSvcVehicleDto> appSvcVehicleDtoList, List<AppSvcVehicleDto> oldAppSvcVehicleDtoList){
        int size = appSvcVehicleDtoList.size();
        int oldSize = oldAppSvcVehicleDtoList.size();
        if(size < oldSize){
            copyDealVehicle(appSvcVehicleDtoList,oldAppSvcVehicleDtoList);
        }else if(oldSize < size){
            copyDealVehicle(oldAppSvcVehicleDtoList,appSvcVehicleDtoList);
        }
    }

    private void copyDealVehicle(List<AppSvcVehicleDto> appSvcVehicleDtoList, List<AppSvcVehicleDto> oldAppSvcVehicleDtoList){
        int size=oldAppSvcVehicleDtoList.size() - appSvcVehicleDtoList.size();
        for(int i=0;i< size;i++){
            AppSvcVehicleDto appSvcVehicleDto = generateAppSvcVehicleDto();
            appSvcVehicleDtoList.add(appSvcVehicleDto);
        }
    }
    private AppSvcVehicleDto generateAppSvcVehicleDto(){
        AppSvcVehicleDto appSvcVehicleDto =new AppSvcVehicleDto();
        appSvcVehicleDto.setVehicleName("");
        appSvcVehicleDto.setChassisNum("");
        appSvcVehicleDto.setEngineNum("");
        return appSvcVehicleDto;
    }
    private void dealClinicalDirector(List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtoList,List<AppSvcPrincipalOfficersDto> oldAppSvcClinicalDirectorDtoList){
        int size = appSvcClinicalDirectorDtoList.size();
        int oldSize = oldAppSvcClinicalDirectorDtoList.size();
        if(size < oldSize){
            copyDealClinicalDirector(appSvcClinicalDirectorDtoList,oldAppSvcClinicalDirectorDtoList);
        }else if(oldSize < size){
            copyDealClinicalDirector(oldAppSvcClinicalDirectorDtoList,appSvcClinicalDirectorDtoList);
        }
    }
    private void copyDealClinicalDirector(List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtoList,List<AppSvcPrincipalOfficersDto> oldAppSvcClinicalDirectorDtoList){
        for(int i=0;i<oldAppSvcClinicalDirectorDtoList.size()-appSvcClinicalDirectorDtoList.size();i++){
            AppSvcPrincipalOfficersDto appSvcClinicalDirectorDto = generateAppSvcClinicalDirectorDto();
            appSvcClinicalDirectorDtoList.add(appSvcClinicalDirectorDto);
        }
    }
    private AppSvcPrincipalOfficersDto generateAppSvcClinicalDirectorDto(){
        AppSvcPrincipalOfficersDto appSvcClinicalDirectorDto=new AppSvcPrincipalOfficersDto();
        appSvcClinicalDirectorDto.setProfessionBoard("");
        appSvcClinicalDirectorDto.setSalutation("");
        appSvcClinicalDirectorDto.setName("");
        appSvcClinicalDirectorDto.setIdType("");
        appSvcClinicalDirectorDto.setIdNo("");
        appSvcClinicalDirectorDto.setProfRegNo("");
        appSvcClinicalDirectorDto.setDesignation("");
        appSvcClinicalDirectorDto.setSpeciality("");
        appSvcClinicalDirectorDto.setTypeOfRegister("");
        appSvcClinicalDirectorDto.setTypeOfCurrRegi("");
        appSvcClinicalDirectorDto.setRelevantExperience("");
        appSvcClinicalDirectorDto.setHoldCerByEMS("");
        appSvcClinicalDirectorDto.setEmailAddr("");
        appSvcClinicalDirectorDto.setMobileNo("");
        return appSvcClinicalDirectorDto;
    }

    private int checkPersonType(String type){
        switch (type){
            case ApplicationConsts.PERSONNEL_PSN_TYPE_CGO:
            case ApplicationConsts.PERSONNEL_PSN_TYPE_PO:
            case ApplicationConsts.PERSONNEL_PSN_TYPE_DPO:
            case ApplicationConsts.PERSONNEL_PSN_TYPE_MAP:
            case ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR:
                return 1;
            case ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL:
                return 2;
            default:return -1;
        }
    }

    public List<HcsaSvcDocConfigDto> getAllHcsaSvcDocs(String serviceId) {
        Map<String, String> docMap = IaisCommonUtils.genNewHashMap();
        if (StringUtil.isEmpty(serviceId)) {
            docMap.put("common", "0");
            docMap.put("premises", "1");
        } else {
            docMap.put("svc", serviceId);
            docMap.put("common", "0");
        }
        String docMapJson = JsonUtil.parseToJson(docMap);
        return hcsaConfigClient.getHcsaSvcDocConfig(docMapJson).getEntity();
    }
}
