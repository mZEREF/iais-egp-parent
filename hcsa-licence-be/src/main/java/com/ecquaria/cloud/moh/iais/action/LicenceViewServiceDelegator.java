package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.job.executor.util.SpringHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewHciNameDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSecDetailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.DocumentShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.HfsmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.SpecialServiceSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremGroupOutsourcedDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoAbortDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoTopPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOutsouredDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSpecialServiceInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmFormDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcSuplmItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ComplaintDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.DisciplinaryRecordResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.OrganizationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
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
    private static final String WITHDRAWDTO = "withdrawDto";
    private static final String WITHDRAWDTOLIST = "withdrawDtoList";
    private static final String NOT_VIEW = "NOT_VIEW";
    private static final String APPLICATION_VIEWDTO = "applicationViewDto";
    private static final String APP_EDIT_SELECTDTO = "appEditSelectDto";
    private static final String OLD_LICENCE_DTO = "oldLicenceDto";
    private static final String BE_EIC_GATEWAY_CLIENT = "beEicGatewayClient";
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

    @Autowired
    private OrganizationService organizationService;

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
     */
    public void prepareViewData(BaseProcessClass bpc) {
        // licence AppSubmissionDto doucument add md5
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator prepareData start ..."));
        ParamUtil.setSessionAttr(bpc.request, "cess_ack002", MessageUtil.getMessageDesc("CESS_ACK002"));
        ParamUtil.setSessionAttr(bpc.request, "appealSpecialDocDto", null);
        String rfi = bpc.request.getParameter("rfi");
        String requestRfi = (String) bpc.request.getAttribute("rfi");
        if (!StringUtil.isEmpty(rfi) || !StringUtil.isEmpty(requestRfi)) {
            bpc.request.setAttribute("rfi", "rfi");
        }
        bpc.request.getSession().removeAttribute(NOT_VIEW);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) bpc.request.getSession().getAttribute(APPLICATION_VIEWDTO);
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
        AppEditSelectDto appEditSelectDto = (AppEditSelectDto) bpc.request.getSession().getAttribute(APP_EDIT_SELECTDTO);
        if (appEditSelectDto == null) {
            appEditSelectDto = applicationViewDto.getAppEditSelectDto();
        }
        AppEditSelectDto rfiAppEditSelectDto = (AppEditSelectDto) bpc.request.getSession().getAttribute("rfiAppEditSelectDto");
        String isSaveRfiSelect = (String) bpc.request.getSession().getAttribute("isSaveRfiSelect");
        if (AppConsts.YES.equals(isSaveRfiSelect)) {
            bpc.request.getSession().setAttribute("pageAppEditSelectDto", rfiAppEditSelectDto);
        } else {
            bpc.request.getSession().setAttribute("pageAppEditSelectDto", null);
        }
        log.info(StringUtil.changeForLog(appEditSelectDto + APP_EDIT_SELECTDTO));
        bpc.request.getSession().setAttribute(APP_EDIT_SELECTDTO, appEditSelectDto);

        if (appPremisesCorrelationDto != null && appSubmissionDto != null) {
            handleWithDrawalDoc(appSubmissionDto.getAppType(), appSubmissionDto.getAppGrpId(),
                    appPremisesCorrelationDto.getApplicationId(),
                    bpc.request);
        }

        if (appSubmissionDto == null) {
            return;
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        String serviceId = applicationViewDto.getApplicationDto().getServiceId();
        List<String> list = IaisCommonUtils.genNewArrayList();
        list.add(serviceId);
        List<HcsaServiceStepSchemeDto> entity = hcsaConfigClient.getServiceStepsByServiceIds(list).getEntity();
        bpc.request.getSession().setAttribute("hcsaServiceStepSchemeDtoList", entity);

        boolean canEidtPremise = canEidtPremise(applicationViewDto.getApplicationGroupDto().getId());
        ParamUtil.setRequestAttr(bpc.request, "canEidtPremise", canEidtPremise);
        try {
            contrastNewAndOld(appSubmissionDto);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        if (appGrpPremisesDtoList != null) {
            String licenseeId = applicationViewDto.getApplicationGroupDto().getLicenseeId();
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                /*
                 * 5.2.3 (4). If the address of the Mode of Service Delivery keyed in by applicant is currently used by a different
                 * licensee, the System will indicate the following information of another licensee to the ASO:
                 * a.	Name of licensee
                 * b.	Business Name (Mode of Service Delivery)
                 * c.	Service Nam
                 */
                String premisesType = appGrpPremisesDto.getPremisesType();
                String checkhciName = appGrpPremisesDto.getHciName();
                if (checkhciName != null) {
                    List<ApplicationViewHciNameDto> applicationViewHciNameDtos = hcsaLicenceClient.getApplicationViewHciNameDtoByHciName(
                            checkhciName, licenseeId, premisesType).getEntity();
                    for (ApplicationViewHciNameDto applicationViewHciNameDto : applicationViewHciNameDtos) {
                        LicenseeDto licenseeDto = organizationService.getLicenseeById(applicationViewHciNameDto.getLicensee());
                        applicationViewHciNameDto.setLicensee(licenseeDto.getName());
                    }
                    appGrpPremisesDto.setApplicationViewHciNameDtos(applicationViewHciNameDtos);
                }
                Map<String, String> map = new HashMap<>(5);
                String blkNo = appGrpPremisesDto.getBlkNo();
                String floorNo = appGrpPremisesDto.getFloorNo();
                String unitNo = appGrpPremisesDto.getUnitNo();
                String postalCode = appGrpPremisesDto.getPostalCode();
                String hciName = appGrpPremisesDto.getHciName();
                String conveyanceVehicleNo = appGrpPremisesDto.getVehicleNo();
                List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = appGrpPremisesDto.getAppPremisesOperationalUnitDtos();
                map.put("floorNo0", floorNo);
                map.put("unitNo0", unitNo);
                int i = 1;
                if (appPremisesOperationalUnitDtos != null) {
                    for (AppPremisesOperationalUnitDto appPremisesOperationalUnitDto : appPremisesOperationalUnitDtos) {
                        map.put("floorNo" + i, appPremisesOperationalUnitDto.getFloorNo());
                        map.put("unitNo" + i, appPremisesOperationalUnitDto.getUnitNo());
                        i++;
                    }
                }
                map.put("floorUnitSize", String.valueOf(i));
                map.put("blkNo", blkNo);
                map.put("postCode", postalCode);
                map.put("hciName", hciName);
                map.put("vehicleNo", conveyanceVehicleNo);
                map.put("licensee", licenseeId);
                map.put("premisesType", premisesType);
                List<ApplicationViewHciNameDto> applicationViewHciNameDtos = hcsaLicenceClient.getApplicationViewHciNameDtoByAddress(
                        map).getEntity();
                for (ApplicationViewHciNameDto applicationViewHciNameDto : applicationViewHciNameDtos) {
                    LicenseeDto licenseeDto = organizationService.getLicenseeById(applicationViewHciNameDto.getLicensee());
                    applicationViewHciNameDto.setLicensee(licenseeDto.getName());
                }
                appGrpPremisesDto.setApplicationViewAddress(applicationViewHciNameDtos);
            }
        }
        ApplicationGroupDto groupDto = applicationViewDto.getApplicationGroupDto();
        if (groupDto != null) {
            authorisedPerson(groupDto.getLicenseeId(), appSubmissionDto);
        }
        List<AppDeclarationDocDto> appDeclarationDocDtos = appSubmissionDto.getAppDeclarationDocDtos();
        if (appDeclarationDocDtos != null) {
            appDeclarationDocDtos.sort(Comparator.comparingInt(AppDeclarationDocDto::getSeqNum));
        }
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        prepareViewServiceForm(bpc);
        if ("Y".equals(prsFlag)) {
            disciplinaryRecord(appSubmissionDto, bpc.request);
        }
        if ("Y".equals(herimsFlag)) {
            herimsRecod(appSubmissionDto, bpc.request);
        }

         // volidata role
        boolean isEdit = SpringHelper.getBean(ApplicationDelegator.class).checkData(HcsaAppConst.CHECKED_BTN_SHOW, bpc.request);
        ParamUtil.setRequestAttr(bpc.request,"isEdit",isEdit);
        // declaration
        checkDeclaration(appSubmissionDto, bpc.request);
    }

    public void checkDeclaration(AppSubmissionDto appSubmissionDto, HttpServletRequest request) {
        String appType = appSubmissionDto.getAppType();
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)) {
            AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto.getAppDeclarationMessageDto();
            String appGrpNo = appSubmissionDto.getAppGrpNo();
            if (!StringUtil.isEmpty(appGrpNo) && appGrpNo.startsWith("AR")) {
                if (appDeclarationMessageDto != null) {
                    ParamUtil.setRequestAttr(request, "renew_rfc_show", "Y");
                }
            } else {
                if (appDeclarationMessageDto != null) {
                    ParamUtil.setRequestAttr(request, "RFC_HCAI_NAME_CHNAGE", String.valueOf(false));
                }
            }
        } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            AppDeclarationMessageDto appDeclarationMessageDto = appSubmissionDto.getAppDeclarationMessageDto();
            if (appDeclarationMessageDto != null) {
                ParamUtil.setRequestAttr(request, RenewalConstants.IS_SINGLE, "Y");
            }
        }
    }

    public AppSubmissionDto getAppSubmissionAndHandLicence(AppPremisesCorrelationDto appPremisesCorrelationDto,
            HttpServletRequest request) {
        if (appPremisesCorrelationDto == null) {
            return null;
        }
        String applicationId = appPremisesCorrelationDto.getApplicationId();
        AppSubmissionDto appSubmissionDto = licenceViewService.getAppSubmissionByAppId(applicationId);
        DealSessionUtil.initView(appSubmissionDto);
        if (appSubmissionDto == null) {
            return null;
        }
        // new
        ApplicationDto entity = applicationClient.getApplicationById(appPremisesCorrelationDto.getApplicationId()).getEntity();
        String newGrpId = entity.getAppGrpId();
        ApplicationGroupDto newApplicationGroupDto = applicationClient.getAppById(newGrpId).getEntity();
        LicenseeDto newLicenceDto = organizationService.getLicenseeById(newApplicationGroupDto.getLicenseeId());
        request.setAttribute("newLicenceDto", newLicenceDto);
        // last - previous version record
        if (entity.getVersion() > 1) {
            ApplicationDto applicationDto = applicationClient.getLastApplicationByAppNo(entity).getEntity();
            if (applicationDto != null) {
                String oldGrpId = applicationDto.getAppGrpId();
                ApplicationGroupDto oldApplicationGroupDto = applicationClient.getAppById(oldGrpId).getEntity();
                LicenseeDto oldLicenceDto = organizationService.getLicenseeById(oldApplicationGroupDto.getLicenseeId());
                request.setAttribute(OLD_LICENCE_DTO, oldLicenceDto);
                AppSubmissionDto appSubmissionByAppId = applicationClient.getAppSubmissionByoldAppId(
                        applicationDto.getId()).getEntity();
                DealSessionUtil.initView(appSubmissionByAppId);
                appSubmissionDto.setOldAppSubmissionDto(appSubmissionByAppId);
            }
        } else if (entity.getOriginLicenceId() != null) {
            AppSubmissionDto appSubmission = licCommService.viewAppSubmissionDto(entity.getOriginLicenceId());
            if (appSubmission != null) {
                LicenseeDto oldLicenceDto = organizationService.getLicenseeById(appSubmission.getLicenseeId());
                request.setAttribute(OLD_LICENCE_DTO, oldLicenceDto);
                DealSessionUtil.initView(appSubmission);
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
        if (applicationDtoList != null && premiseMiscDto != null) {
            applicationDtoList.forEach(h -> {
                ApplicationDto oldApplicationDto = applicationClient.getApplicationById(premiseMiscDto.getRelateRecId()).getEntity();
                WithdrawnDto withdrawnDto = new WithdrawnDto();
                withdrawnDto.setApplicationNo(oldApplicationDto.getApplicationNo());

                withdrawnDto.setWithdrawnReason(premiseMiscDto.getReason());
                withdrawnDto.setWithdrawnRemarks(premiseMiscDto.getRemarks());
                List<AppPremisesSpecialDocDto> appealSpecialDocDto = fillUpCheckListGetAppClient.getAppPremisesSpecialDocByPremId(
                        premiseMiscDto.getAppPremCorreId()).getEntity();
                List<PageShowFileDto> pageShowFileDtos = IaisCommonUtils.genNewArrayList();
                HashMap<String, File> map = IaisCommonUtils.genNewHashMap();
                HashMap<String, PageShowFileDto> pageShowFileHashMap = IaisCommonUtils.genNewHashMap();
                if (appealSpecialDocDto != null && !appealSpecialDocDto.isEmpty()) {
                    for (int i = 0; i < appealSpecialDocDto.size(); i++) {
                        PageShowFileDto pageShowFileDto = new PageShowFileDto();
                        pageShowFileDto.setFileUploadUrl(appealSpecialDocDto.get(i).getFileRepoId());
                        pageShowFileDto.setFileName(appealSpecialDocDto.get(i).getDocName());
                        pageShowFileDto.setFileMapId("selectedFileDiv" + i);
                        pageShowFileDto.setSize(appealSpecialDocDto.get(i).getDocSize());
                        pageShowFileDto.setMd5Code(appealSpecialDocDto.get(i).getMd5Code());
                        pageShowFileDto.setIndex(String.valueOf(i));
                        pageShowFileDtos.add(pageShowFileDto);
                        map.put("selectedFile" + i, null);
                        pageShowFileHashMap.put("selectedFile" + i, pageShowFileDto);
                    }
                    request.getSession().setAttribute("seesion_files_map_ajax_feselectedFile", map);
                    request.getSession().setAttribute("pageShowFileHashMap", pageShowFileHashMap);
                    request.getSession().setAttribute("seesion_files_map_ajax_feselectedFile_MaxIndex", appealSpecialDocDto.size());
                }
                request.getSession().setAttribute("pageShowFiles", pageShowFileDtos);
                withdrawnDtoList.add(withdrawnDto);
            });
        }
        if (withdrawnDtoList.size() > 0) {
            ParamUtil.setRequestAttr(request, WITHDRAWDTO, withdrawnDtoList.get(0));
            ParamUtil.setRequestAttr(request, WITHDRAWDTOLIST, withdrawnDtoList);
        }
    }

    private void disciplinaryRecord(AppSubmissionDto appSubmissionDto, HttpServletRequest request) {
        if (appSubmissionDto == null) {
            return;
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
        Set<String> redNo = getProfRegNos(appSvcRelatedInfoDto);
        if (oldAppSubmissionDto != null) {
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
            redNo.addAll(getProfRegNos(oldAppSvcRelatedInfoDto));
        }
        List<String> list = new ArrayList<>(redNo);
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
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                request.setAttribute(BE_EIC_GATEWAY_CLIENT, msg);
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
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                request.setAttribute(BE_EIC_GATEWAY_CLIENT, msg);
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
                        List<ComplaintDto> complaintDtoList = addMoneySymbol(disciplinaryRecordResponseDto.getComplaints());
                        complaintDtos = new ArrayList<>(complaintDtoList);
                    } else {
                        complaintDtos.addAll(disciplinaryRecordResponseDto.getComplaints());
                    }
                    listHashMap.put(disciplinaryRecordResponseDto.getRegno(), complaintDtos);
                }
            }
        }
        request.getSession().setAttribute("proHashMap", proHashMap);
        request.getSession().setAttribute("listHashMap", listHashMap);
    }

    private Set<String> getProfRegNos(AppSvcRelatedInfoDto appSvcRelatedInfoDto) {
        // key personnel
        List<String> keyPsnTypes = ApplicationHelper.getKeyPsnTypes(appSvcRelatedInfoDto);
        Set<String> set = keyPsnTypes.stream()
                .map(psnType -> ApplicationHelper.getKeyPersonnel(psnType, true, appSvcRelatedInfoDto))
                .filter(IaisCommonUtils::isNotEmpty)
                .flatMap(psnList -> psnList.stream()
                        .map(AppSvcPrincipalOfficersDto::getProfRegNo)
                        .filter(StringUtil::isNotEmpty))
                .collect(Collectors.toSet());
        // service personnel
        List<String> svcPsnTypes = ApplicationHelper.getSvcPsnTypes(appSvcRelatedInfoDto);
        svcPsnTypes.stream()
                .map(psnType -> ApplicationHelper.getSvcPersonnel(psnType, true, appSvcRelatedInfoDto))
                .filter(IaisCommonUtils::isNotEmpty)
                .flatMap(psnList -> psnList.stream()
                        .map(AppSvcPersonnelDto::getProfRegNo)
                        .filter(StringUtil::isNotEmpty))
                .forEach(set::add);
        // other information
        AppSvcOtherInfoDto appSvcOtherInfoDto = Optional.ofNullable(appSvcRelatedInfoDto.getAppSvcOtherInfoList())
                .filter(IaisCommonUtils::isNotEmpty)
                .map(list -> list.get(0))
                .orElse(null);
        if (appSvcOtherInfoDto != null) {
            Optional.ofNullable(appSvcOtherInfoDto.getOtherInfoTopPersonPractitionersList())
                    .filter(IaisCommonUtils::isNotEmpty)
                    .ifPresent(list -> list.forEach(dto -> set.add(dto.getProfRegNo())));
            Optional.ofNullable(appSvcOtherInfoDto.getOtherInfoTopPersonAnaesthetistsList())
                    .filter(IaisCommonUtils::isNotEmpty)
                    .ifPresent(list -> list.forEach(dto -> set.add(dto.getProfRegNo())));
            Optional.ofNullable(appSvcOtherInfoDto.getOtherInfoTopPersonNursesList())
                    .filter(IaisCommonUtils::isNotEmpty)
                    .ifPresent(list -> list.forEach(dto -> set.add(dto.getProfRegNo())));
            Optional.ofNullable(appSvcOtherInfoDto.getOtherInfoTopPersonCounsellorsList())
                    .filter(IaisCommonUtils::isNotEmpty)
                    .ifPresent(list -> list.forEach(dto -> set.add(dto.getProfRegNo())));
            Optional.ofNullable(appSvcOtherInfoDto.getAppSvcSuplmFormDto())
                    .map(AppSvcSuplmFormDto::getActiveAppSvcSuplmItemDtoList)
                    .filter(IaisCommonUtils::isNotEmpty)
                    .ifPresent(list -> list.stream()
                            .filter(dto -> HcsaConsts.SUPFORM_SPEC_COND_PRS.equals(dto.getSpecialCondition()))
                            .forEach(dto -> set.add(dto.getInputValue()))
                    );
        }
        // supplementory form
        List<AppSvcSuplmFormDto> appSvcSuplmFormList = appSvcRelatedInfoDto.getAppSvcSuplmFormList();
        if (IaisCommonUtils.isNotEmpty(appSvcSuplmFormList)) {
            for (AppSvcSuplmFormDto appSvcSuplmFormDto : appSvcSuplmFormList) {
                appSvcSuplmFormDto.getActiveAppSvcSuplmItemDtoList().stream()
                        .filter(dto -> HcsaConsts.SUPFORM_SPEC_COND_PRS.equals(dto.getSpecialCondition()))
                        .forEach(dto -> set.add(dto.getInputValue()));
            }
        }
        return set;
    }

    private void herimsRecod(AppSubmissionDto appSubmissionDto, HttpServletRequest request) {
        if (appSubmissionDto == null) {
            return;
        }
        AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();
        Set<String> idNoSet = new HashSet<>();
        Object newLicenceDto = request.getAttribute("newLicenceDto");
        if (newLicenceDto != null) {
            LicenseeDto newLic = (LicenseeDto) newLicenceDto;
            idNoSet.add(newLic.getUenNo());
        }
        Object oldLicenceDto = request.getAttribute(OLD_LICENCE_DTO);
        if (oldLicenceDto != null) {
            LicenseeDto oldL = (LicenseeDto) oldLicenceDto;
            idNoSet.add(oldL.getUenNo());
        }
        idNoSet.addAll(getIdNoSet(appSubmissionDto));

        if (oldAppSubmissionDto != null) {
            idNoSet.addAll(getIdNoSet(oldAppSubmissionDto));
        }
        List<String> idList = new ArrayList<>(idNoSet);
        List<HfsmsDto> hfsmsDtos = IaisCommonUtils.genNewArrayList();
        try {
            hfsmsDtos = applicationClient.getHfsmsDtoByIdNo(idList).getEntity();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            //GENERAL_ERR0068 - Not able to connect to HERIMS at this moment!
            request.setAttribute(BE_EIC_GATEWAY_CLIENT, MessageUtil.getMessageDesc("GENERAL_ERR0068"));
            log.error("------>this have error<----- Not able to connect to HERIMS at this moment!");
        }
        HashMap<String, List<HfsmsDto>> hashMap = IaisCommonUtils.genNewHashMap();
        if (!IaisCommonUtils.isEmpty(hfsmsDtos)) {
            for (HfsmsDto hfsmsDto : hfsmsDtos) {
                String identificationNo = hfsmsDto.getIdentificationNo();
                List<HfsmsDto> hfsmsDtoList = hashMap.get(identificationNo);
                if (hfsmsDtoList == null) {
                    hfsmsDtoList = new ArrayList<>();
                }
                hfsmsDtoList.add(hfsmsDto);
                hashMap.put(identificationNo, hfsmsDtoList);
            }
        }
        request.getSession().setAttribute("hashMap", hashMap);
    }

    private Set<String> getIdNoSet(AppSubmissionDto appSubmissionDto) {
        if (appSubmissionDto == null) {
            return IaisCommonUtils.genNewHashSet();
        }
        Set<String> idNoSet = IaisCommonUtils.genNewHashSet();
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
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        // key personnel
        List<String> keyPsnTypes = ApplicationHelper.getKeyPsnTypes(appSvcRelatedInfoDto);
        keyPsnTypes.stream()
                .map(psnType -> ApplicationHelper.getKeyPersonnel(psnType, true, appSvcRelatedInfoDto))
                .filter(IaisCommonUtils::isNotEmpty)
                .flatMap(psnList -> psnList.stream()
                        .map(AppSvcPrincipalOfficersDto::getIdNo)
                        .filter(StringUtil::isNotEmpty))
                .forEach(idNoSet::add);
        // other information
        AppSvcOtherInfoDto appSvcOtherInfoDto = Optional.ofNullable(appSvcRelatedInfoDto.getAppSvcOtherInfoList())
                .filter(IaisCommonUtils::isNotEmpty)
                .map(list -> list.get(0))
                .orElse(null);
        if (appSvcOtherInfoDto != null) {
            Optional.ofNullable(appSvcOtherInfoDto.getOtherInfoTopPersonPractitionersList())
                    .filter(IaisCommonUtils::isNotEmpty)
                    .ifPresent(list -> list.forEach(dto -> idNoSet.add(dto.getIdNo())));
            Optional.ofNullable(appSvcOtherInfoDto.getOtherInfoTopPersonAnaesthetistsList())
                    .filter(IaisCommonUtils::isNotEmpty)
                    .ifPresent(list -> list.forEach(dto -> idNoSet.add(dto.getIdNo())));
            Optional.ofNullable(appSvcOtherInfoDto.getOtherInfoTopPersonCounsellorsList())
                    .filter(IaisCommonUtils::isNotEmpty)
                    .ifPresent(list -> list.forEach(dto -> idNoSet.add(dto.getIdNo())));
        }
        return idNoSet;
    }

    private List<ComplaintDto> addMoneySymbol(List<ComplaintDto> complaints) {
        if (!IaisCommonUtils.isEmpty(complaints)) {
            for (ComplaintDto complaintDto : complaints) {
                if (complaintDto != null && !StringUtil.isEmpty(complaintDto.getFineamount())) {
                    String money = "$" + complaintDto.getFineamount();
                    complaintDto.setFineamount(money);
                }
            }
        }
        return complaints;
    }

    public void authorisedPerson(String licenseeId, AppSubmissionDto appSubmissionDto) {
        if (licenseeId == null) {
            return;
        }
        LicenseeDto licenseeDto = organizationService.getLicenseeById(licenseeId);
        if (licenseeDto != null) {
            String organizationId = licenseeDto.getOrganizationId();
            List<OrgUserDto> orgUserDtos = organizationClient.getOrgUserAccountSampleDtoByOrganizationId(organizationId).getEntity();
            List<LicenseeKeyApptPersonDto> licenseeKeyApptPersonDtos = organizationClient.getLicenseeKeyApptPersonByLiceseeId(
                    licenseeId).getEntity();
            appSubmissionDto.setAuthorisedPerson(orgUserDtos);
            appSubmissionDto.setBoardMember(licenseeKeyApptPersonDtos);
        }
    }

    private boolean canEidtPremise(String appGrpId) {
        log.info(StringUtil.changeForLog("The canEidtPremise is start ..."));
        log.info(StringUtil.changeForLog("The canEidtPremise appGrpId is -->:" + appGrpId));
        boolean result = applicationService.getApplicationDtoByGroupIdAndStatus(appGrpId,
                ApplicationConsts.APPLICATION_STATUS_APPROVED) == null;
        log.info(StringUtil.changeForLog("The canEidtPremise result is -->:" + result));
        log.info(StringUtil.changeForLog("The canEidtPremise is end ..."));
        return result;
    }

    /**
     * StartStep: doSaveSelect
     * <p>
     * not used,
     * <p>
     * Refer to {@link com.ecquaria.cloud.moh.iais.ajax.RequestForInformationSubmitAjaxController}#callRfiSubmit(bpc.request)
     *
     * @param bpc
     */
    public void doSaveSelect(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator doSaveSelect start ..."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, APPLICATION_VIEWDTO);
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
                    AppEditSelectDto appEditSelectDto = setAppEditSelectDto(newAppPremisesCorrelationDto, selectsList,
                            appSubmissionDto);
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
            bpc.request.setAttribute("rfi", "rfi");
        }
        ParamUtil.setRequestAttr(bpc.request, "successMsg", successMsg);
        ParamUtil.setRequestAttr(bpc.request, "isSuccess", isSuccess);
        ParamUtil.setRequestAttr(bpc.request, "errorMsg", errorMsg);
        ParamUtil.setSessionAttr(bpc.request, "parentMsg", parentMsg);
        ParamUtil.setSessionAttr(bpc.request, APPLICATION_VIEWDTO, applicationViewDto);
        log.debug(StringUtil.changeForLog("the do LicenceViewServiceDelegator doSaveSelect end ..."));
    }


    /**
     * StartStep: prepareView
     *
     * @param bpc
     */
    public void prepareViewServiceForm(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareView start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            return;
        }
        if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(appSubmissionDto.getAppType())) {
            return;
        }
        AppSubmissionDto oldAppSubmissionDto = appSubmissionDto.getOldAppSubmissionDto();

        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = null;
        if (oldAppSubmissionDto != null) {
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList();
            if (appSvcRelatedInfoDtoList != null) {
                oldAppSvcRelatedInfoDto = doAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
            }
        }

        AppSvcRelatedInfoDto appSvcRelatedInfoDto = doAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
        appSvcRelatedInfoDto.setOldAppSvcRelatedInfoDto(oldAppSvcRelatedInfoDto);

        List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        if (appSvcDocDtoLit != null) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtoLit) {
                String svcDocId = appSvcDocDto.getSvcDocId();
                if (StringUtil.isEmpty(svcDocId)) {
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

    private AppSvcRelatedInfoDto doAppSvcRelatedInfoDtoList(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList) {
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)) {
            appSvcRelatedInfoDto = appSvcRelatedInfoDtoList.get(0);
        }
        return appSvcRelatedInfoDto;
    }

    private AppEditSelectDto setAppEditSelectDto(AppPremisesCorrelationDto newAppPremisesCorrelationDto, List<String> selectsList,
            AppSubmissionDto appSubmissionDto) {
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
        if (selectsList.contains("specialised")) {
            appEditSelectDto.setSpecialisedEdit(true);
            String title = MessageUtil.getMessageDesc("GENERAL_TITLE01");
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">" + title + "</li>";
            rfiUpWindowsCheck.add(title);
        }
        if (selectsList.contains("service")) {
            appEditSelectDto.setServiceEdit(true);
            parentMsg = parentMsg + "<li style=\"padding-left: 0px;\">Service Related Information - " + serviceName + "</li>";
            rfiUpWindowsCheck.add("Service Related Information - " + serviceName);
        }
        appEditSelectDto.setParentMsg(parentMsg);
        appEditSelectDto.setEditType(ApplicationConsts.APPLICATION_EDIT_TYPE_RFI);
        appEditSelectDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        if (IaisCommonUtils.isEmpty(rfiUpWindowsCheck)) {
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
        dealAppGrpPremisesList(appGrpPremisesDtoList, oldAppGrpPremisesDtoList);
        // specialised
        List<AppPremSpecialisedDto> appPremSpecialisedDtoList = IaisCommonUtils.getList(
                appSubmissionDto.getAppPremSpecialisedDtoList());
        List<AppPremSpecialisedDto> oldAppPremSpecialisedDtoList = IaisCommonUtils.getList(
                oldAppSubmissionDto.getAppPremSpecialisedDtoList());
        dealAppPremSpecialisedDtoList(appPremSpecialisedDtoList, oldAppPremSpecialisedDtoList);
        appSubmissionDto.setAppPremSpecialisedDtoList(appPremSpecialisedDtoList);
        oldAppSubmissionDto.setAppPremSpecialisedDtoList(oldAppPremSpecialisedDtoList);
        // service info
        AppSvcRelatedInfoDto oldAppSvcRelatedInfoDto = oldAppSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        log.info(StringUtil.changeForLog("The multipleSvcDoc  show change"));
        // key personnel
        for (String psnType : IaisCommonUtils.getKeyPersonnel()) {
            dealKeyPersonnel(appSvcRelatedInfoDto, oldAppSvcRelatedInfoDto, psnType);
        }
        // section leader
        List<AppSvcPersonnelDto> appSvcSectionLeaderList = IaisCommonUtils.getList(appSvcRelatedInfoDto.getAppSvcSectionLeaderList());
        List<AppSvcPersonnelDto> oldAppSvcSectionLeaderList = IaisCommonUtils.getList(
                oldAppSvcRelatedInfoDto.getAppSvcSectionLeaderList());
        oldAppSvcSectionLeaderList = dealSvcPersonnel(appSvcSectionLeaderList, oldAppSvcSectionLeaderList);
        oldAppSvcRelatedInfoDto.setAppSvcSectionLeaderList(oldAppSvcSectionLeaderList);
        appSvcRelatedInfoDto.setAppSvcSectionLeaderList(appSvcSectionLeaderList);
        //new service for cr
        List<AppSvcVehicleDto> appSvcVehicleDtoList = IaisCommonUtils.getList(appSvcRelatedInfoDto.getAppSvcVehicleDtoList());
        List<AppSvcVehicleDto> oldAppSvcVehicleDtoList = IaisCommonUtils.getList(oldAppSvcRelatedInfoDto.getAppSvcVehicleDtoList());
        oldAppSvcVehicleDtoList = dealList(appSvcVehicleDtoList, oldAppSvcVehicleDtoList,
                (newDto, oldDto) -> Objects.equals(newDto.getVehicleName(), oldDto.getVehicleName())
                        || Objects.equals(newDto.getEngineNum(), oldDto.getEngineNum())
                        || Objects.equals(newDto.getChassisNum(), oldDto.getChassisNum()),
                dto -> new AppSvcVehicleDto(), null);
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
        // Other Info
        List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList = IaisCommonUtils.getList(appSvcRelatedInfoDto.getAppSvcOtherInfoList());
        List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList = IaisCommonUtils.getList(oldAppSvcRelatedInfoDto.getAppSvcOtherInfoList());
        dealAppSvcOtherInfoDtoList(appSvcOtherInfoDtoList,oldAppSvcOtherInfoDtoList);
        // Supplementary Form
        List<AppSvcSuplmFormDto> appSvcSuplmFormList = IaisCommonUtils.getList(appSvcRelatedInfoDto.getAppSvcSuplmFormList());
        List<AppSvcSuplmFormDto> oldAppSvcSuplmFormList = IaisCommonUtils.getList(oldAppSvcRelatedInfoDto.getAppSvcSuplmFormList());
        dealAppSvcSuplmFormList(appSvcSuplmFormList, oldAppSvcSuplmFormList);
        //special service information
        List<AppSvcSpecialServiceInfoDto> appSvcSpecialServiceInfoDtoList = IaisCommonUtils.getList(appSvcRelatedInfoDto.getAppSvcSpecialServiceInfoList());
        List<AppSvcSpecialServiceInfoDto> oldAppSvcSpecialServiceInfoDtoList = IaisCommonUtils.getList(oldAppSvcRelatedInfoDto.getAppSvcSpecialServiceInfoList());
        dealSpecialServiceInformation(appSvcSpecialServiceInfoDtoList,oldAppSvcSpecialServiceInfoDtoList);
        appSvcRelatedInfoDto.setAppSvcSpecialServiceInfoList(appSvcSpecialServiceInfoDtoList);
        oldAppSvcRelatedInfoDto.setAppSvcSpecialServiceInfoList(oldAppSvcSpecialServiceInfoDtoList);
        // documents
        List<DocumentShowDto> documentShowDtoList = IaisCommonUtils.getList(appSvcRelatedInfoDto.getDocumentShowDtoList());
        List<DocumentShowDto> oldDocumentShowDtoList = IaisCommonUtils.getList(oldAppSvcRelatedInfoDto.getDocumentShowDtoList());
        dealDocuments(documentShowDtoList, oldDocumentShowDtoList);
        appSvcRelatedInfoDto.setDocumentShowDtoList(documentShowDtoList);
        oldAppSvcRelatedInfoDto.setDocumentShowDtoList(oldDocumentShowDtoList);
        // Outsource
        AppSvcOutsouredDto appSvcOutsouredDto = appSvcRelatedInfoDto.getAppSvcOutsouredDto();
        if (appSvcOutsouredDto == null){
            appSvcOutsouredDto = new AppSvcOutsouredDto();
        }
        AppSvcOutsouredDto oldAppSvcOutsouredDto = oldAppSvcRelatedInfoDto.getAppSvcOutsouredDto();
        if (oldAppSvcOutsouredDto == null){
            oldAppSvcOutsouredDto = new AppSvcOutsouredDto();
        }
        dealAppSvcOutsource(appSvcOutsouredDto, oldAppSvcOutsouredDto);
    }

    private void dealSpecialServiceInformation(List<AppSvcSpecialServiceInfoDto> appSvcSpecialServiceInfoDtoList, List<AppSvcSpecialServiceInfoDto> oldAppSvcSpecialServiceInfoDtoList) {
        dealList(appSvcSpecialServiceInfoDtoList, oldAppSvcSpecialServiceInfoDtoList,
                (newDto, oldList) -> oldList.stream()
                        .filter(dto -> Objects.equals(newDto.getPremisesType(), dto.getPremisesType())
                                && Objects.equals(newDto.getPremAddress(), dto.getPremAddress()))
                        .findAny()
                        .orElseGet(() -> oldList.stream()
                                .filter(dto -> Objects.equals(newDto.getPremisesType(), dto.getPremisesType()))
                                .findAny()
                                .orElse(null)),
                dto -> {
                    AppSvcSpecialServiceInfoDto newDto = CopyUtil.copyMutableObject(dto);
                    newDto.setSpecialServiceSectionDtoList(IaisCommonUtils.genNewArrayList());
                    return newDto;
                });
        int size = appSvcSpecialServiceInfoDtoList.size();
        for (int i = 0; i < size; i++) {
            AppSvcSpecialServiceInfoDto appSvcSpecialServiceInfoDto = appSvcSpecialServiceInfoDtoList.get(i);
            AppSvcSpecialServiceInfoDto oldAppSvcSpecialServiceInfoDto = oldAppSvcSpecialServiceInfoDtoList.get(i);
            List<SpecialServiceSectionDto> specialServiceSectionDtoList = IaisCommonUtils.getList(appSvcSpecialServiceInfoDto.getSpecialServiceSectionDtoList());
            List<SpecialServiceSectionDto> oldSpecialServiceSectionDtoList = IaisCommonUtils.getList(oldAppSvcSpecialServiceInfoDto.getSpecialServiceSectionDtoList());
            dealSpecialServiceSectionList(specialServiceSectionDtoList,oldSpecialServiceSectionDtoList);
            appSvcSpecialServiceInfoDto.setSpecialServiceSectionDtoList(specialServiceSectionDtoList);
            oldAppSvcSpecialServiceInfoDto.setSpecialServiceSectionDtoList(oldSpecialServiceSectionDtoList);
        }
    }

    private void dealSpecialServiceSectionList(List<SpecialServiceSectionDto> specialServiceSectionDtoList, List<SpecialServiceSectionDto> oldSpecialServiceSectionDtoList) {
        dealList(specialServiceSectionDtoList, oldSpecialServiceSectionDtoList,
                (newDto, oldList) -> oldList.stream()
                        .filter(dto -> Objects.equals(newDto.getSvcId(), dto.getSvcId()))
                        .findAny()
                        .orElseGet(() -> oldList.stream()
                                .filter(dto -> Objects.equals(newDto.getSvcName(), dto.getSvcName()))
                                .findAny()
                                .orElse(null)),
                dto -> {
                    SpecialServiceSectionDto newDto = CopyUtil.copyMutableObject(dto);
                    newDto.setAppSvcCgoDtoList(IaisCommonUtils.genNewArrayList());
                    newDto.setAppSvcSectionLeaderList(IaisCommonUtils.genNewArrayList());
                    newDto.setAppSvcNurseDtoList(IaisCommonUtils.genNewArrayList());
                    newDto.setAppSvcRadiationSafetyOfficerDtoList(IaisCommonUtils.genNewArrayList());
                    newDto.setAppSvcPersonnelDtoList(IaisCommonUtils.genNewArrayList());
                    newDto.setAppSvcMedicalPhysicistDtoList(IaisCommonUtils.genNewArrayList());
                    newDto.setAppSvcRadiationPhysicistDtoList(IaisCommonUtils.genNewArrayList());
                    newDto.setAppSvcDirectorDtoList(IaisCommonUtils.genNewArrayList());
                    newDto.setAppSvcNurseDirectorDtoList(IaisCommonUtils.genNewArrayList());
                    newDto.setAppSvcRadiationOncologist(IaisCommonUtils.genNewArrayList());
                    newDto.setAppSvcMedicalDosimetrist(IaisCommonUtils.genNewArrayList());
                    newDto.setAppSvcRadiationTherapist(IaisCommonUtils.genNewArrayList());
                    newDto.setAppSvcRadiationCqmp(IaisCommonUtils.genNewArrayList());
                    newDto.setAppSvcSuplmFormDto(new AppSvcSuplmFormDto());
                    return newDto;
                });
        int size = specialServiceSectionDtoList.size();
        for (int i = 0; i < size; i++) {
            SpecialServiceSectionDto specialServiceSectionDto = specialServiceSectionDtoList.get(i);
            SpecialServiceSectionDto oldSpecialServiceSectionDto = oldSpecialServiceSectionDtoList.get(i);
            dealSpecialSectionDetail(specialServiceSectionDto,oldSpecialServiceSectionDto);
        }
    }

    private void dealSpecialSectionDetail(SpecialServiceSectionDto specialServiceSectionDto, SpecialServiceSectionDto oldSpecialServiceSectionDto) {
        //CGO
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = IaisCommonUtils.getList(specialServiceSectionDto.getAppSvcCgoDtoList());
        List<AppSvcPrincipalOfficersDto> oldAppSvcCgoDtoList = IaisCommonUtils.getList(oldSpecialServiceSectionDto.getAppSvcCgoDtoList());
        oldAppSvcCgoDtoList = dealKeyPersonnel(appSvcCgoDtoList, oldAppSvcCgoDtoList);
        specialServiceSectionDto.setAppSvcCgoDtoList(appSvcCgoDtoList);
        oldSpecialServiceSectionDto.setAppSvcCgoDtoList(oldAppSvcCgoDtoList);

        //SectionLeader
        List<AppSvcPersonnelDto> appSvcSectionLeaderList = IaisCommonUtils.getList(specialServiceSectionDto.getAppSvcSectionLeaderList());
        List<AppSvcPersonnelDto> oldAppSvcSectionLeaderList = IaisCommonUtils.getList(oldSpecialServiceSectionDto.getAppSvcSectionLeaderList());
        oldAppSvcSectionLeaderList = dealSvcPersonnel(appSvcSectionLeaderList,oldAppSvcSectionLeaderList);
        specialServiceSectionDto.setAppSvcSectionLeaderList(appSvcSectionLeaderList);
        oldSpecialServiceSectionDto.setAppSvcSectionLeaderList(oldAppSvcSectionLeaderList);

        //nurse
        List<AppSvcPersonnelDto> appSvcNurseDtoList = IaisCommonUtils.getList(specialServiceSectionDto.getAppSvcNurseDtoList());
        List<AppSvcPersonnelDto> oldAppSvcNurseDtoList = IaisCommonUtils.getList(oldSpecialServiceSectionDto.getAppSvcNurseDtoList());
        oldAppSvcNurseDtoList = dealSvcPersonnel(appSvcNurseDtoList,oldAppSvcNurseDtoList);
        specialServiceSectionDto.setAppSvcNurseDtoList(appSvcNurseDtoList);
        oldSpecialServiceSectionDto.setAppSvcNurseDtoList(oldAppSvcNurseDtoList);

        //rso
        List<AppSvcPersonnelDto> appSvcRadiationSafetyOfficerDtoList = IaisCommonUtils.getList(specialServiceSectionDto.getAppSvcRadiationSafetyOfficerDtoList());
        List<AppSvcPersonnelDto> oldAppSvcRadiationSafetyOfficerDtoList = IaisCommonUtils.getList(oldSpecialServiceSectionDto.getAppSvcRadiationSafetyOfficerDtoList());
        oldAppSvcRadiationSafetyOfficerDtoList = dealSvcPersonnel(appSvcRadiationSafetyOfficerDtoList,oldAppSvcRadiationSafetyOfficerDtoList);
        specialServiceSectionDto.setAppSvcRadiationSafetyOfficerDtoList(appSvcRadiationSafetyOfficerDtoList);
        oldSpecialServiceSectionDto.setAppSvcRadiationSafetyOfficerDtoList(oldAppSvcRadiationSafetyOfficerDtoList);

        //sv
        List<AppSvcPersonnelDto> appSvcPersonnelDtoList = IaisCommonUtils.getList(specialServiceSectionDto.getAppSvcPersonnelDtoList());
        List<AppSvcPersonnelDto> oldAppSvcPersonnelDtoList = IaisCommonUtils.getList(oldSpecialServiceSectionDto.getAppSvcPersonnelDtoList());
        oldAppSvcPersonnelDtoList = dealSvcPersonnel(appSvcPersonnelDtoList,oldAppSvcPersonnelDtoList);
        specialServiceSectionDto.setAppSvcPersonnelDtoList(appSvcPersonnelDtoList);
        oldSpecialServiceSectionDto.setAppSvcPersonnelDtoList(oldAppSvcPersonnelDtoList);

        //mp
        List<AppSvcPersonnelDto> appSvcMedicalPhysicistDtoList = IaisCommonUtils.getList(specialServiceSectionDto.getAppSvcMedicalPhysicistDtoList());
        List<AppSvcPersonnelDto> oldAppSvcMedicalPhysicistDtoList = IaisCommonUtils.getList(oldSpecialServiceSectionDto.getAppSvcMedicalPhysicistDtoList());
        oldAppSvcMedicalPhysicistDtoList = dealSvcPersonnel(appSvcMedicalPhysicistDtoList,oldAppSvcMedicalPhysicistDtoList);
        specialServiceSectionDto.setAppSvcMedicalPhysicistDtoList(appSvcMedicalPhysicistDtoList);
        oldSpecialServiceSectionDto.setAppSvcMedicalPhysicistDtoList(oldAppSvcMedicalPhysicistDtoList);

        //rp
        List<AppSvcPersonnelDto> appSvcRadiationPhysicistDtoList = IaisCommonUtils.getList(specialServiceSectionDto.getAppSvcRadiationPhysicistDtoList());
        List<AppSvcPersonnelDto> oldAppSvcRadiationPhysicistDtoList = IaisCommonUtils.getList(oldSpecialServiceSectionDto.getAppSvcRadiationPhysicistDtoList());
        oldAppSvcRadiationPhysicistDtoList = dealSvcPersonnel(appSvcRadiationPhysicistDtoList,oldAppSvcRadiationPhysicistDtoList);
        specialServiceSectionDto.setAppSvcRadiationPhysicistDtoList(appSvcRadiationPhysicistDtoList);
        oldSpecialServiceSectionDto.setAppSvcRadiationPhysicistDtoList(oldAppSvcRadiationPhysicistDtoList);

        //cqmp
        List<AppSvcPersonnelDto> appSvcRadiationCqmp = IaisCommonUtils.getList(specialServiceSectionDto.getAppSvcRadiationCqmp());
        List<AppSvcPersonnelDto> oldAppSvcRadiationCqmp = IaisCommonUtils.getList(oldSpecialServiceSectionDto.getAppSvcRadiationCqmp());
        oldAppSvcRadiationCqmp = dealSvcPersonnel(appSvcRadiationCqmp,oldAppSvcRadiationCqmp);
        specialServiceSectionDto.setAppSvcRadiationCqmp(appSvcRadiationCqmp);
        oldSpecialServiceSectionDto.setAppSvcRadiationCqmp(oldAppSvcRadiationCqmp);

        //ro
        List<AppSvcPersonnelDto> appSvcRadiationOncologist = IaisCommonUtils.getList(specialServiceSectionDto.getAppSvcRadiationOncologist());
        List<AppSvcPersonnelDto> oldAppSvcRadiationOncologist = IaisCommonUtils.getList(oldSpecialServiceSectionDto.getAppSvcRadiationOncologist());
        oldAppSvcRadiationOncologist = dealSvcPersonnel(appSvcRadiationOncologist,oldAppSvcRadiationOncologist);
        specialServiceSectionDto.setAppSvcRadiationOncologist(appSvcRadiationOncologist);
        oldSpecialServiceSectionDto.setAppSvcRadiationOncologist(oldAppSvcRadiationOncologist);

        //md
        List<AppSvcPersonnelDto> appSvcMedicalDosimetrist = IaisCommonUtils.getList(specialServiceSectionDto.getAppSvcMedicalDosimetrist());
        List<AppSvcPersonnelDto> oldAppSvcMedicalDosimetrist = IaisCommonUtils.getList(oldSpecialServiceSectionDto.getAppSvcMedicalDosimetrist());
        oldAppSvcMedicalDosimetrist = dealSvcPersonnel(appSvcMedicalDosimetrist,oldAppSvcMedicalDosimetrist);
        specialServiceSectionDto.setAppSvcMedicalDosimetrist(appSvcMedicalDosimetrist);
        oldSpecialServiceSectionDto.setAppSvcMedicalDosimetrist(oldAppSvcMedicalDosimetrist);

        //rt
        List<AppSvcPersonnelDto> appSvcRadiationTherapist = IaisCommonUtils.getList(specialServiceSectionDto.getAppSvcRadiationTherapist());
        List<AppSvcPersonnelDto> oldAppSvcRadiationTherapist = IaisCommonUtils.getList(oldSpecialServiceSectionDto.getAppSvcRadiationTherapist());
        oldAppSvcRadiationTherapist = dealSvcPersonnel(appSvcRadiationTherapist,oldAppSvcRadiationTherapist);
        specialServiceSectionDto.setAppSvcRadiationTherapist(appSvcRadiationTherapist);
        oldSpecialServiceSectionDto.setAppSvcRadiationTherapist(oldAppSvcRadiationTherapist);

        //doctor
        List<AppSvcPersonnelDto> appSvcDirectorDtoList = IaisCommonUtils.getList(specialServiceSectionDto.getAppSvcDirectorDtoList());
        List<AppSvcPersonnelDto> oldAppSvcDirectorDtoList = IaisCommonUtils.getList(oldSpecialServiceSectionDto.getAppSvcDirectorDtoList());
        oldAppSvcDirectorDtoList = dealSvcPersonnel(appSvcDirectorDtoList,oldAppSvcDirectorDtoList);
        specialServiceSectionDto.setAppSvcDirectorDtoList(appSvcDirectorDtoList);
        oldSpecialServiceSectionDto.setAppSvcDirectorDtoList(oldAppSvcDirectorDtoList);

        //nurse in charge
        List<AppSvcPersonnelDto> appSvcNurseDirectorDtoList = IaisCommonUtils.getList(specialServiceSectionDto.getAppSvcNurseDirectorDtoList());
        List<AppSvcPersonnelDto> oldAppSvcNurseDirectorDtoList = IaisCommonUtils.getList(oldSpecialServiceSectionDto.getAppSvcNurseDirectorDtoList());
        oldAppSvcNurseDirectorDtoList = dealSvcPersonnel(appSvcNurseDirectorDtoList,oldAppSvcNurseDirectorDtoList);
        specialServiceSectionDto.setAppSvcNurseDirectorDtoList(appSvcNurseDirectorDtoList);
        oldSpecialServiceSectionDto.setAppSvcNurseDirectorDtoList(oldAppSvcNurseDirectorDtoList);

        //sup form
        AppSvcSuplmFormDto appSvcSuplmFormDto = specialServiceSectionDto.getAppSvcSuplmFormDto();
        AppSvcSuplmFormDto oldAppSvcSuplmFormDto = oldSpecialServiceSectionDto.getAppSvcSuplmFormDto();
        dealAppSvcSuplmForm(appSvcSuplmFormDto,oldAppSvcSuplmFormDto);
        specialServiceSectionDto.setAppSvcSuplmFormDto(appSvcSuplmFormDto);
        oldSpecialServiceSectionDto.setAppSvcSuplmFormDto(oldAppSvcSuplmFormDto);
    }

    private void dealAppSvcOutsource(AppSvcOutsouredDto appSvcOutsouredDto, AppSvcOutsouredDto oldAppSvcOutsouredDto){
        // clb
        List<AppPremGroupOutsourcedDto> clbList = IaisCommonUtils.getList(appSvcOutsouredDto.getClinicalLaboratoryList());
        List<AppPremGroupOutsourcedDto> oldClbList = IaisCommonUtils.getList(oldAppSvcOutsouredDto.getClinicalLaboratoryList());
        int maxSize = Math.max(clbList.size(), oldClbList.size());
        for (int i = 0, len = maxSize - clbList.size(); i < len; i++) {
            clbList.add(new AppPremGroupOutsourcedDto());
        }
        for (int i = 0, len = maxSize -oldClbList.size(); i < len; i++) {
            oldClbList.add(new AppPremGroupOutsourcedDto());
        }
        appSvcOutsouredDto.setClinicalLaboratoryList(clbList);
        oldAppSvcOutsouredDto.setClinicalLaboratoryList(oldClbList);
        // rds
        List<AppPremGroupOutsourcedDto> rdsList = IaisCommonUtils.getList(appSvcOutsouredDto.getRadiologicalServiceList());
        List<AppPremGroupOutsourcedDto> oldRdsList = IaisCommonUtils.getList(oldAppSvcOutsouredDto.getRadiologicalServiceList());
        maxSize = Math.max(rdsList.size(), oldRdsList.size());
        for (int i = 0, len = maxSize - rdsList.size(); i < len; i++) {
            rdsList.add(new AppPremGroupOutsourcedDto());
        }
        for (int i = 0, len = maxSize -oldRdsList.size(); i < len; i++) {
            oldRdsList.add(new AppPremGroupOutsourcedDto());
        }
        appSvcOutsouredDto.setRadiologicalServiceList(rdsList);
        oldAppSvcOutsouredDto.setRadiologicalServiceList(oldRdsList);
    }

    private void dealAppSvcOtherInfoDtoList(List<AppSvcOtherInfoDto> appSvcOtherInfoDtoList,
                                         List<AppSvcOtherInfoDto> oldAppSvcOtherInfoDtoList){
        if (IaisCommonUtils.isEmpty(appSvcOtherInfoDtoList) || IaisCommonUtils.isEmpty(oldAppSvcOtherInfoDtoList)){
            return;
        }
        /*
         * Comparison of List in other info:
         * 1. topPerson
         * 2. abort
         * 3. suplmForm
         * 4. otherService
         */
        for (int i = 0; i < appSvcOtherInfoDtoList.size(); i++) {
            // topPerson
            dealAppSvcOtherInfoTopPerson(appSvcOtherInfoDtoList.get(i), oldAppSvcOtherInfoDtoList.get(i));
            // abort
            dealAppSvcOtherInfoAbort(appSvcOtherInfoDtoList.get(i), oldAppSvcOtherInfoDtoList.get(i));
            // suplmForm
            dealAppSvcSuplmForm(appSvcOtherInfoDtoList.get(i).getAppSvcSuplmFormDto(), oldAppSvcOtherInfoDtoList.get(i).getAppSvcSuplmFormDto());
            // otherService
            dealAppSvcOtherService(appSvcOtherInfoDtoList.get(i), oldAppSvcOtherInfoDtoList.get(i));
        }
    }

    private void dealAppSvcOtherService(AppSvcOtherInfoDto appSvcOtherInfoDto, AppSvcOtherInfoDto oldAppSvcOtherInfoDto){
        List<AppPremSubSvcRelDto> appPremSubSvcRelDtoList = IaisCommonUtils.getList(
                appSvcOtherInfoDto.getAllAppPremSubSvcRelDtoList());
        List<AppPremSubSvcRelDto> oldAppPremSubSvcRelDtoList = IaisCommonUtils.getList(
                oldAppSvcOtherInfoDto.getAllAppPremSubSvcRelDtoList());
        oldAppPremSubSvcRelDtoList = dealList(appPremSubSvcRelDtoList, oldAppPremSubSvcRelDtoList,
                (newDto, oldDto) -> Objects.equals(newDto.getSvcCode(), oldDto.getSvcCode()), dto -> new AppPremSubSvcRelDto(),
                null);
        appSvcOtherInfoDto.setAllAppPremSubSvcRelDtoList(appPremSubSvcRelDtoList);
        oldAppSvcOtherInfoDto.setAllAppPremSubSvcRelDtoList(oldAppPremSubSvcRelDtoList);
    }

    private void dealAppSvcOtherInfoAbort(AppSvcOtherInfoDto appSvcOtherInfoDto, AppSvcOtherInfoDto oldAppSvcOtherInfoDto){
        // abortDrug
        List<AppSvcOtherInfoAbortDto> abortDrugList = IaisCommonUtils.getList(appSvcOtherInfoDto.getOtherInfoAbortDrugList());
        List<AppSvcOtherInfoAbortDto> oldAbortDrugList = IaisCommonUtils.getList(oldAppSvcOtherInfoDto.getOtherInfoAbortDrugList());
        int maxSize = Math.max(abortDrugList.size(), oldAbortDrugList.size());
        for (int i = 0, len = maxSize - abortDrugList.size(); i < len; i++) {
            abortDrugList.add(new AppSvcOtherInfoAbortDto());
        }
        for (int i = 0, len = maxSize - oldAbortDrugList.size(); i < len; i++) {
            oldAbortDrugList.add(new AppSvcOtherInfoAbortDto());
        }
        appSvcOtherInfoDto.setOtherInfoAbortDrugList(abortDrugList);
        oldAppSvcOtherInfoDto.setOtherInfoAbortDrugList(oldAbortDrugList);
        // surgicalProcedure
        List<AppSvcOtherInfoAbortDto> surgicalProcedureList = IaisCommonUtils.getList(appSvcOtherInfoDto.getOtherInfoAbortSurgicalProcedureList());
        List<AppSvcOtherInfoAbortDto> oldSurgicalProcedureList = IaisCommonUtils.getList(oldAppSvcOtherInfoDto.getOtherInfoAbortSurgicalProcedureList());
        maxSize = Math.max(surgicalProcedureList.size(), oldSurgicalProcedureList.size());
        for (int i = 0, len = maxSize - surgicalProcedureList.size(); i < len; i++) {
            surgicalProcedureList.add(new AppSvcOtherInfoAbortDto());
        }
        for (int i = 0, len = maxSize - oldSurgicalProcedureList.size(); i < len; i++) {
            oldSurgicalProcedureList.add(new AppSvcOtherInfoAbortDto());
        }
        appSvcOtherInfoDto.setOtherInfoAbortSurgicalProcedureList(surgicalProcedureList);
        oldAppSvcOtherInfoDto.setOtherInfoAbortSurgicalProcedureList(oldSurgicalProcedureList);
        // drugAndSurgical
        List<AppSvcOtherInfoAbortDto> drugAndSurgicalList = IaisCommonUtils.getList(appSvcOtherInfoDto.getOtherInfoAbortDrugAndSurgicalList());
        List<AppSvcOtherInfoAbortDto> oldDrugAndSurgicalList = IaisCommonUtils.getList(oldAppSvcOtherInfoDto.getOtherInfoAbortDrugAndSurgicalList());
        maxSize = Math.max(drugAndSurgicalList.size(), oldDrugAndSurgicalList.size());
        for (int i = 0, len = maxSize - drugAndSurgicalList.size(); i < len; i++) {
            drugAndSurgicalList.add(new AppSvcOtherInfoAbortDto());
        }
        for (int i = 0, len = maxSize - oldDrugAndSurgicalList.size(); i < len; i++) {
            oldDrugAndSurgicalList.add(new AppSvcOtherInfoAbortDto());
        }
        appSvcOtherInfoDto.setOtherInfoAbortDrugAndSurgicalList(drugAndSurgicalList);
        oldAppSvcOtherInfoDto.setOtherInfoAbortDrugAndSurgicalList(oldDrugAndSurgicalList);
    }

    private void dealAppSvcOtherInfoTopPerson(AppSvcOtherInfoDto appSvcOtherInfoDto, AppSvcOtherInfoDto oldAppSvcOtherInfoDto){
        // practitioners
        List<AppSvcOtherInfoTopPersonDto> practitionersList = IaisCommonUtils.getList(appSvcOtherInfoDto.getOtherInfoTopPersonPractitionersList());
        List<AppSvcOtherInfoTopPersonDto> oldPractitionersList = IaisCommonUtils.getList(oldAppSvcOtherInfoDto.getOtherInfoTopPersonPractitionersList());
        int maxSize = Math.max(practitionersList.size(), oldPractitionersList.size());
        for (int i = 0, len = maxSize - practitionersList.size(); i < len; i++) {
            practitionersList.add(new AppSvcOtherInfoTopPersonDto());
        }
        for (int i = 0, len = maxSize - oldPractitionersList.size(); i < len; i++) {
            oldPractitionersList.add(new AppSvcOtherInfoTopPersonDto());
        }
        appSvcOtherInfoDto.setOtherInfoTopPersonPractitionersList(practitionersList);
        oldAppSvcOtherInfoDto.setOtherInfoTopPersonPractitionersList(oldPractitionersList);
        // anaesthetists
        List<AppSvcOtherInfoTopPersonDto> anaesthetistsList = IaisCommonUtils.getList(appSvcOtherInfoDto.getOtherInfoTopPersonAnaesthetistsList());
        List<AppSvcOtherInfoTopPersonDto> oldAnaesthetistsList = IaisCommonUtils.getList(oldAppSvcOtherInfoDto.getOtherInfoTopPersonAnaesthetistsList());
        maxSize = Math.max(anaesthetistsList.size(), oldAnaesthetistsList.size());
        for (int i = 0, len = maxSize - anaesthetistsList.size(); i < len; i++) {
            anaesthetistsList.add(new AppSvcOtherInfoTopPersonDto());
        }
        for (int i = 0, len = maxSize - oldAnaesthetistsList.size(); i < len; i++) {
            oldAnaesthetistsList.add(new AppSvcOtherInfoTopPersonDto());
        }
        appSvcOtherInfoDto.setOtherInfoTopPersonAnaesthetistsList(anaesthetistsList);
        oldAppSvcOtherInfoDto.setOtherInfoTopPersonAnaesthetistsList(oldAnaesthetistsList);
        // nurses
        List<AppSvcOtherInfoTopPersonDto> nursesList = IaisCommonUtils.getList(appSvcOtherInfoDto.getOtherInfoTopPersonNursesList());
        List<AppSvcOtherInfoTopPersonDto> oldNursesList = IaisCommonUtils.getList(oldAppSvcOtherInfoDto.getOtherInfoTopPersonNursesList());
        maxSize = Math.max(nursesList.size(), oldNursesList.size());
        for (int i = 0, len = maxSize - nursesList.size(); i < len; i++) {
            nursesList.add(new AppSvcOtherInfoTopPersonDto());
        }
        for (int i = 0, len = maxSize - oldNursesList.size(); i < len; i++) {
            oldNursesList.add(new AppSvcOtherInfoTopPersonDto());
        }
        appSvcOtherInfoDto.setOtherInfoTopPersonNursesList(nursesList);
        oldAppSvcOtherInfoDto.setOtherInfoTopPersonNursesList(oldNursesList);
        // counsellors
        List<AppSvcOtherInfoTopPersonDto> counsellorsList = IaisCommonUtils.getList(appSvcOtherInfoDto.getOtherInfoTopPersonCounsellorsList());
        List<AppSvcOtherInfoTopPersonDto> oldCounsellorsList = IaisCommonUtils.getList(oldAppSvcOtherInfoDto.getOtherInfoTopPersonCounsellorsList());
        maxSize = Math.max(counsellorsList.size(), oldCounsellorsList.size());
        for (int i = 0, len = maxSize - counsellorsList.size(); i < len; i++) {
            counsellorsList.add(new AppSvcOtherInfoTopPersonDto());
        }
        for (int i = 0, len = maxSize - oldCounsellorsList.size(); i < len; i++) {
            oldCounsellorsList.add(new AppSvcOtherInfoTopPersonDto());
        }
        appSvcOtherInfoDto.setOtherInfoTopPersonCounsellorsList(counsellorsList);
        oldAppSvcOtherInfoDto.setOtherInfoTopPersonCounsellorsList(oldCounsellorsList);
    }

    private void dealAppSvcSuplmFormList(List<AppSvcSuplmFormDto> appSvcSuplmFormList,
            List<AppSvcSuplmFormDto> oldAppSvcSuplmFormList) {
        resolveEquList(appSvcSuplmFormList, oldAppSvcSuplmFormList, dto -> {
            AppSvcSuplmFormDto newDto = CopyUtil.copyMutableObject(dto);
            newDto.setSuppleFormItemConfigDtos(null);
            return newDto;
        });
        int size = appSvcSuplmFormList.size();
        for (int i = 0; i < size; i++) {
            dealAppSvcSuplmForm(appSvcSuplmFormList.get(i), oldAppSvcSuplmFormList.get(i));
        }
    }

    private void dealAppSvcSuplmForm(AppSvcSuplmFormDto appSvcSuplmForm, AppSvcSuplmFormDto oldAppSvcSuplmForm) {
        List<AppSvcSuplmItemDto> appSvcSuplmItemList = appSvcSuplmForm.getAppSvcSuplmItemListByCon(AppSvcSuplmItemDto::isDisplay);
        List<AppSvcSuplmItemDto> oldAppSvcSuplmItemList = oldAppSvcSuplmForm.getAppSvcSuplmItemListByCon(
                AppSvcSuplmItemDto::isDisplay);
        oldAppSvcSuplmItemList = dealList(appSvcSuplmItemList, oldAppSvcSuplmItemList,
                (newDto, oldDto) -> Objects.equals(newDto.getItemConfigId(), oldDto.getItemConfigId())
                        && Objects.equals(newDto.getSeqNum(), oldDto.getSeqNum()),
                dto -> {
                    AppSvcSuplmItemDto newDto = CopyUtil.copyMutableObject(dto);
                    newDto.setInputValue(null);
                    newDto.setDisplay(true);
                    return newDto;
                }, null);
        appSvcSuplmForm.setSuppleFormItemConfigDtos(appSvcSuplmItemList);
        oldAppSvcSuplmForm.setSuppleFormItemConfigDtos(oldAppSvcSuplmItemList);
    }

    private void dealAppPremSpecialisedDtoList(List<AppPremSpecialisedDto> appPremSpecialisedDtoList,
            List<AppPremSpecialisedDto> oldAppPremSpecialisedDtoList) {
        dealList(appPremSpecialisedDtoList, oldAppPremSpecialisedDtoList,
                (newDto, oldList) -> oldList.stream()
                        .filter(dto -> Objects.equals(newDto.getBaseSvcCode(), dto.getBaseSvcCode())
                                && Objects.equals(newDto.getPremisesType(), dto.getPremisesType())
                                && Objects.equals(newDto.getPremAddress(), dto.getPremAddress()))
                        .findFirst()
                        .orElseGet(() -> oldList.stream()
                                .filter(dto -> Objects.equals(newDto.getBaseSvcCode(), dto.getBaseSvcCode())
                                        && Objects.equals(newDto.getPremisesType(), dto.getPremisesType()))
                                .findFirst()
                                .orElse(null)),
                dto -> {
                    AppPremSpecialisedDto newDto = CopyUtil.copyMutableObject(dto);
                    newDto.setSvcSpecifiedCorrelationList(null);
                    newDto.setSvcSubtypeList(null);
                    return newDto;
                });
        int size = appPremSpecialisedDtoList.size();
        for (int i = 0; i < size; i++) {
            dealAppPremSpecialisedDto(appPremSpecialisedDtoList.get(i), oldAppPremSpecialisedDtoList.get(i));
        }
    }

    private void dealAppPremSpecialisedDto(AppPremSpecialisedDto appPremSpecialisedDto,
            AppPremSpecialisedDto oldAppPremSpecialisedDto) {
        List<AppPremScopeDto> appPremScopeDtoList = IaisCommonUtils.getList(appPremSpecialisedDto.getAllAppPremScopeDtoList());
        List<AppPremScopeDto> oldAppPremScopeDtoList = IaisCommonUtils.getList(oldAppPremSpecialisedDto.getAllAppPremScopeDtoList());
        oldAppPremScopeDtoList = dealList(appPremScopeDtoList, oldAppPremScopeDtoList,
                (newDto, oldDto) -> Objects.equals(newDto.getScopeName(), oldDto.getScopeName()), dto -> new AppPremScopeDto(),
                null);
        appPremSpecialisedDto.setAllAppPremScopeDtoList(appPremScopeDtoList);
        oldAppPremSpecialisedDto.setAllAppPremScopeDtoList(oldAppPremScopeDtoList);

        List<AppPremSubSvcRelDto> appPremSubSvcRelDtoList = IaisCommonUtils.getList(
                appPremSpecialisedDto.getAllAppPremSubSvcRelDtoList());
        List<AppPremSubSvcRelDto> oldAppPremSubSvcRelDtoList = IaisCommonUtils.getList(
                oldAppPremSpecialisedDto.getAllAppPremSubSvcRelDtoList());
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
        return dealList(newList, oldList,
                (newDto, oldDto) -> Objects.equals(newDto.getAssignSelect(), oldDto.getAssignSelect()),
                dto -> {
                    AppSvcPrincipalOfficersDto newDto = new AppSvcPrincipalOfficersDto();
                    newDto.setPsnType(dto.getPsnType());
                    return newDto;
                }, null);
    }

    private void dealAppGrpPremisesList(List<AppGrpPremisesDto> appGrpPremisesDtoList,
            List<AppGrpPremisesDto> oldAppGrpPremisesDtoList) {
        resolveEquList(appGrpPremisesDtoList, oldAppGrpPremisesDtoList, dto -> new AppGrpPremisesDto());
        int size = appGrpPremisesDtoList.size();
        for (int i = 0; i < size; i++) {
            AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtoList.get(i);
            AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtoList.get(i);
            // floor and unit
            List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos = getOperationalUnitDtos(appGrpPremisesDto);
            List<AppPremisesOperationalUnitDto> oldAppPremisesOperationalUnitDtos = getOperationalUnitDtos(oldAppGrpPremisesDto);
            oldAppPremisesOperationalUnitDtos = dealList(appPremisesOperationalUnitDtos, oldAppPremisesOperationalUnitDtos,
                    (newDto, oldDto) -> Objects.equals(newDto.getFloorNo(), oldDto.getFloorNo())
                            && Objects.equals(newDto.getUnitNo(), oldDto.getUnitNo()),
                    dto -> {
                        AppPremisesOperationalUnitDto premisesOperationalUnitDto = new AppPremisesOperationalUnitDto();
                        premisesOperationalUnitDto.setPremType(dto.getPremType());
                        return premisesOperationalUnitDto;
                    }, null);
            reSetFloorAndUnit(appGrpPremisesDto, appPremisesOperationalUnitDtos);
            reSetFloorAndUnit(oldAppGrpPremisesDto, oldAppPremisesOperationalUnitDtos);
            // Co-Location Services
            dealCoLocation(appGrpPremisesDto, oldAppGrpPremisesDto);
        }
    }

    private void dealCoLocation(AppGrpPremisesDto appGrpPremisesDto, AppGrpPremisesDto oldAppGrpPremisesDto) {
        List<AppPremNonLicRelationDto> appPremNonLicRelationDtos = IaisCommonUtils.getList(
                appGrpPremisesDto.getAppPremNonLicRelationDtos());
        List<AppPremNonLicRelationDto> oldAppPremNonLicRelationDtos = IaisCommonUtils.getList(
                oldAppGrpPremisesDto.getAppPremNonLicRelationDtos());
        oldAppPremNonLicRelationDtos = dealList(appPremNonLicRelationDtos, oldAppPremNonLicRelationDtos,
                (newDto, oldDto) -> Objects.equals(newDto.getBusinessName(), oldDto.getBusinessName())
                        && Objects.equals(newDto.getProvidedService(), oldDto.getProvidedService()),
                dto -> new AppPremNonLicRelationDto(), null);
        appGrpPremisesDto.setAppPremNonLicRelationDtos(appPremNonLicRelationDtos);
        oldAppGrpPremisesDto.setAppPremNonLicRelationDtos(oldAppPremNonLicRelationDtos);
    }

    private void reSetFloorAndUnit(AppGrpPremisesDto appGrpPremisesDto,
            List<AppPremisesOperationalUnitDto> appPremisesOperationalUnitDtos) {
        AppPremisesOperationalUnitDto dto = appPremisesOperationalUnitDtos.get(0);
        appGrpPremisesDto.setFloorNo(dto.getFloorNo());
        appGrpPremisesDto.setUnitNo(dto.getUnitNo());
        appPremisesOperationalUnitDtos.remove(0);
        appGrpPremisesDto.setAppPremisesOperationalUnitDtos(appPremisesOperationalUnitDtos);
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

    private void dealDocuments(List<DocumentShowDto> documentShowDtoList, List<DocumentShowDto> oldDocumentShowDtoList) {
        dealList(documentShowDtoList, oldDocumentShowDtoList,
                (newDto, oldList) -> oldList.stream()
                        .filter(dto -> Objects.equals(newDto.getPremisesType(), dto.getPremisesType())
                                && Objects.equals(newDto.getPremAddress(), dto.getPremAddress()))
                        .findAny()
                        .orElseGet(() -> oldList.stream()
                                .filter(dto -> Objects.equals(newDto.getPremisesType(), dto.getPremisesType()))
                                .findAny()
                                .orElse(null)),
                dto -> {
                    DocumentShowDto newDto = CopyUtil.copyMutableObject(dto);
                    newDto.setDocSectionList(IaisCommonUtils.genNewArrayList());
                    return newDto;
                });
        int size = documentShowDtoList.size();
        for (int i = 0; i < size; i++) {
            DocumentShowDto documentShowDto = documentShowDtoList.get(i);
            DocumentShowDto oldDocumentShowDto = oldDocumentShowDtoList.get(i);
            List<DocSectionDto> docSectionList = IaisCommonUtils.getList(documentShowDto.getDocSectionList());
            List<DocSectionDto> oldDocSectionList = IaisCommonUtils.getList(oldDocumentShowDto.getDocSectionList());
            dealDocSectionDtos(docSectionList, oldDocSectionList);
            documentShowDto.setDocSectionList(docSectionList);
            oldDocumentShowDto.setDocSectionList(oldDocSectionList);
        }
    }

    private void dealDocSectionDtos(List<DocSectionDto> docSectionList, List<DocSectionDto> oldDocSectionList) {
        dealList(docSectionList, oldDocSectionList,
                (newDto, oldList) -> oldList.stream()
                        .filter(dto -> Objects.equals(newDto.getSvcCode(), dto.getSvcCode()))
                        .findAny().orElse(null),
                dto -> {
                    DocSectionDto newDto = CopyUtil.copyMutableObject(dto);
                    newDto.setDocSecDetailList(IaisCommonUtils.genNewArrayList());
                    return newDto;
                });
        int size = docSectionList.size();
        for (int i = 0; i < size; i++) {
            DocSectionDto docSectionDto = docSectionList.get(i);
            DocSectionDto oldDocSectionDto = oldDocSectionList.get(i);
            List<DocSecDetailDto> docSecDetailList = IaisCommonUtils.getList(docSectionDto.getDocSecDetailList());
            List<DocSecDetailDto> oldDocSecDetailList = IaisCommonUtils.getList(oldDocSectionDto.getDocSecDetailList());
            dealDocSecDetailDtos(docSecDetailList, oldDocSecDetailList);
            docSectionDto.setDocSecDetailList(docSecDetailList);
            oldDocSectionDto.setDocSecDetailList(oldDocSecDetailList);
        }
    }

    private void dealDocSecDetailDtos(List<DocSecDetailDto> docSecDetailList, List<DocSecDetailDto> oldDocSecDetailList) {
        dealList(docSecDetailList, oldDocSecDetailList,
                (newDto, oldList) -> oldList.stream()
                        .filter(dto -> Objects.equals(newDto.getPersonnelKey(), dto.getPersonnelKey()))
                        .findAny().orElse(null),
                dto -> {
                    DocSecDetailDto newDto = CopyUtil.copyMutableObject(dto);
                    newDto.setAppSvcDocDtoList(IaisCommonUtils.genNewArrayList());
                    return newDto;
                });
        // rest psnTypeIndex
        Map<String, Integer> map = docSecDetailList.stream()
                .filter(dto -> StringUtil.isNotEmpty(dto.getPsnType()))
                .collect(Collectors.toMap(DocSecDetailDto::getPsnType, dto -> 1, Integer::sum));
        Map<String, Integer> psnMap = IaisCommonUtils.genNewHashMap();
        map.forEach((k, v) -> {
            if (v != null && v > 1) {
                psnMap.put(k, 0);
            }
        });
        int size = docSecDetailList.size();
        for (int i = 0; i < size; i++) {
            DocSecDetailDto docSecDetailDto = docSecDetailList.get(i);
            DocSecDetailDto oldDocSecDetailDto = oldDocSecDetailList.get(i);
            Integer count = psnMap.computeIfPresent(docSecDetailDto.getPsnType(), (key, oldValue) -> ++oldValue);
            docSecDetailDto.setPsnTypeIndex(count);
            docSecDetailDto.setBackend(true);
            docSecDetailDto.initDisplayTitle();
            oldDocSecDetailDto.setPsnTypeIndex(count);
            oldDocSecDetailDto.setBackend(true);
            oldDocSecDetailDto.initDisplayTitle();
            List<AppSvcDocDto> appSvcDocDtoList = IaisCommonUtils.getList(docSecDetailDto.getAppSvcDocDtoList());
            List<AppSvcDocDto> oldAppSvcDocDtoList = IaisCommonUtils.getList(oldDocSecDetailDto.getAppSvcDocDtoList());
            oldAppSvcDocDtoList = dealList(appSvcDocDtoList, oldAppSvcDocDtoList,
                    (newDto, oldDto) -> Objects.equals(newDto.getSeqNum(), oldDto.getSeqNum()),
                    dto -> {
                        //newDto.setPersonTypeNum(dto.getPersonTypeNum());
                        return new AppSvcDocDto();
                    }, null);
            docSecDetailDto.setAppSvcDocDtoList(appSvcDocDtoList);
            oldDocSecDetailDto.setAppSvcDocDtoList(oldAppSvcDocDtoList);
        }
    }

    private <T> void resolveEquList(List<T> newList, List<T> oldList, Function<T, T> createFun) {
        if (newList.isEmpty() && oldList.isEmpty()) {
            return;
        }
        int newSize = newList.size();
        int oldSize = oldList.size();
        if (newSize == oldSize) {
            return;
        }
        int i = Math.min(newSize, oldSize);
        int j = i;
        while (i < newSize) {
            oldList.add(createFun.apply(newList.get(i++)));
        }
        while (j < oldSize) {
            newList.add(createFun.apply(oldList.get(j++)));
        }
    }

    private <T> List<T> dealList(List<T> newList, List<T> oldList, BiPredicate<T, T> check, Function<T, T> createFun,
            Comparator<T> comparator) {
        if (newList.isEmpty() && oldList.isEmpty()) {
            return oldList;
        }
        int newSize = newList.size();
        int oldSize = oldList.size();
        List<T> newOldList = IaisCommonUtils.genNewArrayList(Math.max(newSize, oldSize));
        for (T obj : newList) {
            T old = oldList.stream()
                    .filter(dto -> check.test(dto, obj))
                    .findAny()
                    .orElse(null);
            if (old == null) {
                old = createFun.apply(obj);
            } else {
                oldList.remove(old);
            }
            newOldList.add(old);
        }
        for (T obj : oldList) {
            T newDto = createFun.apply(obj);
            newList.add(newDto);
            newOldList.add(obj);
        }
        if (comparator != null) {
            newList.sort(comparator);
            newOldList.sort(comparator);
        }
        return newOldList;
    }

    private <T> void dealList(List<T> newList, List<T> oldList, BiFunction<T, List<T>, T> check,
            Function<T, T> createFun) {
        if (newList.isEmpty() && oldList.isEmpty()) {
            return;
        }
        int newSize = newList.size();
        int oldSize = oldList.size();
        List<T> newOldList = IaisCommonUtils.genNewArrayList(Math.max(newSize, oldSize));
        for (T obj : newList) {
            T old = check.apply(obj, oldList);
            if (old == null) {
                old = createFun.apply(obj);
            } else {
                oldList.remove(old);
            }
            newOldList.add(old);
        }
        for (T obj : oldList) {
            T newDto = createFun.apply(obj);
            newList.add(newDto);
            newOldList.add(obj);
        }
        oldList.clear();
        oldList.addAll(newOldList);
    }

}
