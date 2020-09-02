package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.action.AppealDelegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppliSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.AppealService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.sz.commons.util.FileUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;

/**
 * @author Wenkang
 * @date 2020/2/5 9:41
 */
@Service
@Slf4j
public class AppealServiceImpl implements AppealService {

    private static final String LICENCE = "licence";
    private static final String APPLICATION = "application";
    private static final String Y = "Y";
    private static final String N = "N";
    private static final String APPEALING_FOR = "appealingFor";
    private static final String TYPE = "type";

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Value("${iais.email.sender}")
    private String mailSender;

    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private AppConfigClient appConfigClient;
    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;
    @Autowired
    private RequestForChangeService requestForChangeService;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private Environment env;
    @Override
    public String submitData(HttpServletRequest request) {
        String appealingFor = (String) request.getSession().getAttribute(APPEALING_FOR);
        String type = (String) request.getSession().getAttribute(TYPE);
        if (LICENCE.equals(type)) {
            return licencePresmises(request, appealingFor);
        } else if (APPLICATION.equals(type)) {
            return applicationPresmies(request, appealingFor);
        } else {
            return null;
        }
    }

    @Override
    public String saveData(HttpServletRequest req) {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(req, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId;
        if (loginContext != null) {
            licenseeId = loginContext.getLicenseeId();
        } else {
            licenseeId = "9ED45E34-B4E9-E911-BE76-000C29C8FBE4";
        }
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) req.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String saveDraftId = (String) req.getSession().getAttribute("saveDraftNo");
        AppPremisesSpecialDocDto appPremisesSpecialDocDto = (AppPremisesSpecialDocDto) req.getSession().getAttribute("appPremisesSpecialDocDto");
        String appealingFor = request.getParameter("appealingFor");
        String isDelete = request.getParameter("isDelete");
        String reasonSelect = request.getParameter("reasonSelect");
        String proposedHciName = request.getParameter("proposedHciName");
        String remarks = request.getParameter("remarks");
        String othersReason = request.getParameter("othersReason");
        String draftStatus = (String) request.getAttribute("draftStatus");
        AppealPageDto appealPageDto = reAppealPage(request);
        CommonsMultipartFile selectedFile = (CommonsMultipartFile) request.getFile("selectedFile");
        if (selectedFile != null && selectedFile.getSize() > 0) {
            String filename = selectedFile.getOriginalFilename();
            req.setAttribute("filename", filename);
            byte[] bytes = selectedFile.getBytes();
            Long size = selectedFile.getSize() / 1024;
            appealPageDto.setFileName(filename);
            appealPageDto.setFileSize(size);
            appealPageDto.setFile(bytes);
            if (appPremisesSpecialDocDto == null) {
                appPremisesSpecialDocDto = new AppPremisesSpecialDocDto();
            }
            appPremisesSpecialDocDto.setDocName(selectedFile.getOriginalFilename());
            if (size < 5 * 1024) {
                appPremisesSpecialDocDto.setDocSize(Integer.valueOf(size.toString()));
                String s = FileUtil.genMd5FileChecksum(selectedFile.getBytes());
                appPremisesSpecialDocDto.setMd5Code(s);
                try {
                    String fileToRepo = serviceConfigService.saveFileToRepo(selectedFile);
                    appPremisesSpecialDocDto.setFileRepoId(fileToRepo);
                    appPremisesSpecialDocDto.setSubmitBy(licenseeId);
                    req.getSession().setAttribute("appPremisesSpecialDocDto", appPremisesSpecialDocDto);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            appealPageDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDto);
        } else if (appPremisesSpecialDocDto != null && appPremisesSpecialDocDto.getDocSize() > 0) {
            if (Y.equals(isDelete)) {
                String filename = appPremisesSpecialDocDto.getDocName();
                req.setAttribute("filename", filename);
                appealPageDto.setFileName(filename);
                appealPageDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDto);
            }
        }
        if (N.equals(isDelete)) {
            req.getSession().removeAttribute("appPremisesSpecialDocDto");
            req.getSession().removeAttribute("filename");
        }
        List<AppSvcCgoDto> appSvcCgoDtoList = reAppSvcCgo(request);
        ParamUtil.setRequestAttr(req, "CgoMandatoryCount", appSvcCgoDtoList.size());
        ParamUtil.setSessionAttr(req, "GovernanceOfficersList", (Serializable) appSvcCgoDtoList);
        String groupId = (String) request.getAttribute("groupId");
        appealPageDto.setOtherReason(othersReason);
        String s = JsonUtil.parseToJson(appealPageDto);
        AppPremiseMiscDto appPremiseMiscDto = new AppPremiseMiscDto();
        if (!StringUtil.isEmpty(saveDraftId)) {
            AppSubmissionDto entity = applicationClient.draftNumberGet(saveDraftId).getEntity();
            if (entity != null) {
                entity.setAmountStr(s);
                entity.setAppGrpId(groupId);
                if (!StringUtil.isEmpty(draftStatus)) {
                    entity.setDraftStatus(draftStatus);
                }
                if (groupId == null) {
                    entity.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                } else {
                    entity.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                }
                entity.setLicenseeId(licenseeId);
                Object errorMsg = req.getAttribute("errorMsg");
                entity.setLicenceId(appealPageDto.getAppealFor());
                if(errorMsg==null){
                    req.setAttribute("saveDraftSuccess", "success");
                    applicationClient.saveDraft(entity).getEntity();
                }


            }
            appPremiseMiscDto.setRemarks(remarks);
            appPremiseMiscDto.setReason(reasonSelect);
            if (ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME.equals(reasonSelect)) {
                appPremiseMiscDto.setNewHciName(proposedHciName);
            }
            req.setAttribute(APPEALING_FOR, appealingFor);
            appPremiseMiscDto.setOtherReason(othersReason);
            req.setAttribute("appPremiseMiscDto", appPremiseMiscDto);
            return null;
        }
        AppSubmissionDto appSubmissionDto = new AppSubmissionDto();
        String apty = systemAdminClient.draftNumber(ApplicationConsts.APPLICATION_TYPE_APPEAL).getEntity();
        appSubmissionDto.setDraftNo(apty);
        appSubmissionDto.setAppGrpId(groupId);
        appSubmissionDto.setAmountStr(s);
        if (groupId == null) {
            appSubmissionDto.setDraftStatus(AppConsts.COMMON_STATUS_ACTIVE);
        } else {
            appSubmissionDto.setDraftStatus(AppConsts.COMMON_STATUS_IACTIVE);
        }
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        //todo
        appSubmissionDto.setLicenseeId(licenseeId);
        String serviceName = (String) req.getSession().getAttribute("serviceName");
        HcsaServiceDto serviceByServiceName = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = new ArrayList<>(1);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
        appSvcRelatedInfoDto.setServiceCode(serviceByServiceName.getSvcCode());
        appSvcRelatedInfoDtoList.add(appSvcRelatedInfoDto);
        appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
        appSubmissionDto.setServiceName(serviceName);
        appSubmissionDto.setLicenceId(appealPageDto.getAppealFor());
        Object errorMsg = req.getAttribute("errorMsg");
        if(errorMsg==null){
            req.setAttribute("saveDraftSuccess", "success");
            AppSubmissionDto entity = applicationClient.saveDraft(appSubmissionDto).getEntity();
            String draftNo = entity.getDraftNo();
            req.getSession().setAttribute("saveDraftNo", draftNo);
        }

        appPremiseMiscDto.setRemarks(remarks);
        appPremiseMiscDto.setOtherReason(othersReason);
        appPremiseMiscDto.setReason(reasonSelect);
        appPremiseMiscDto.setNewHciName(proposedHciName);
        req.setAttribute("appPremiseMiscDto", appPremiseMiscDto);
        req.setAttribute(APPEALING_FOR, appealingFor);



        return null;
    }

    @Override
    public void getMessage(HttpServletRequest request) {
        String draftNumber = ParamUtil.getMaskedString(request, "DraftNumber");
        if (draftNumber != null) {
            AppSubmissionDto appSubmissionDto = applicationClient.draftNumberGet(draftNumber).getEntity();
            String serviceName = appSubmissionDto.getServiceName();
            String amountStr = appSubmissionDto.getAmountStr();
            try {
                AppealPageDto appealPageDto = JsonUtil.parseToObject(amountStr, AppealPageDto.class);
                String appealReason = appealPageDto.getAppealReason();
                String remarks = appealPageDto.getRemarks();
                AppPremiseMiscDto appPremiseMiscDto = new AppPremiseMiscDto();
                appPremiseMiscDto.setReason(appealReason);
                appPremiseMiscDto.setRemarks(remarks);
                appPremiseMiscDto.setOtherReason(appealPageDto.getOtherReason());
                String appealFor = appealPageDto.getAppealFor();
                AppPremisesSpecialDocDto appPremisesSpecialDocDto = appealPageDto.getAppPremisesSpecialDocDto();
                String type = appealPageDto.getType();
                if (appPremisesSpecialDocDto != null) {
                    String fileName = appPremisesSpecialDocDto.getDocName();
                    request.getSession().setAttribute("filename", fileName);
                    request.getSession().setAttribute("appPremisesSpecialDocDto", appPremisesSpecialDocDto);
                }
                if (ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(appealReason)) {
                    List<AppSvcCgoDto> appSvcCgoDto = appealPageDto.getAppSvcCgoDto();
                    request.getSession().setAttribute("CgoMandatoryCount", appSvcCgoDto.size());
                    request.getSession().setAttribute("GovernanceOfficersList", appSvcCgoDto);
                    SelectOption sp0 = new SelectOption("-1", "Please Select");
                    List<SelectOption> cgoSelectList = IaisCommonUtils.genNewArrayList();
                    cgoSelectList.add(sp0);
                    SelectOption sp1 = new SelectOption("newOfficer", "I'd like to add a new personnel");
                    cgoSelectList.add(sp1);
                    ParamUtil.setSessionAttr(request, "CgoSelectList", (Serializable) cgoSelectList);
                    List<SelectOption> idTypeSelOp = AppealDelegator.getIdTypeSelOp();
                    ParamUtil.setSessionAttr(request, "IdTypeSelect", (Serializable) idTypeSelOp);
                    if (serviceName != null) {
                        HcsaServiceDto serviceByServiceName = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
                        List<SelectOption> list = AppealDelegator.genSpecialtySelectList(serviceByServiceName.getSvcCode());
                        ParamUtil.setSessionAttr(request, "SpecialtySelectList", (Serializable) list);
                    }
                }
                typeApplicationOrLicence(request, type, appealFor);
                request.setAttribute("appPremiseMiscDto", appPremiseMiscDto);
                request.getSession().setAttribute(TYPE, type);
                request.getSession().setAttribute(APPEALING_FOR, appealFor);
                request.getSession().setAttribute("saveDraftNo", draftNumber);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            return;
        }
        String appealingFor = ParamUtil.getMaskedString(request, APPEALING_FOR);
        String type = request.getParameter(TYPE);
        initRfi(request, appealingFor);
        typeApplicationOrLicence(request, type, appealingFor);
        request.getSession().setAttribute(APPEALING_FOR, appealingFor);
        request.getSession().setAttribute(TYPE, type);

    }

    private AppPremiseMiscDto initRfi(HttpServletRequest request, String appealingFor) {
        AppPremiseMiscDto entity = applicationClient.getAppPremiseMiscDtoByAppId(appealingFor).getEntity();
        if (entity != null) {
            requetForInformationGetMessage(request, entity);
        }
        return entity;
    }

    private void typeApplicationOrLicence(HttpServletRequest request, String type, String appealingFor) {
        if (LICENCE.equals(type)) {
            LicenceDto licenceDto = licenceClient.getLicBylicId(appealingFor).getEntity();
            String svcName = licenceDto.getSvcName();
            String licenceNo = licenceDto.getLicenceNo();
            List<PremisesListQueryDto> premisesListQueryDtos = licenceClient.getPremisesByLicneceId(appealingFor).getEntity();
            List<PremisesListQueryDto> listQueryDtos = IaisCommonUtils.genNewArrayList();
            if (premisesListQueryDtos != null) {
                for (int i = 0; i < premisesListQueryDtos.size(); i++) {
                    listQueryDtos.add(premisesListQueryDtos.get(i));
                }
            }
            List<String> hciNames = IaisCommonUtils.genNewArrayList();
            List<String> addresses = IaisCommonUtils.genNewArrayList();
            for (int i = 0; i < listQueryDtos.size(); i++) {
                String hciName = listQueryDtos.get(i).getHciName();
                String address = listQueryDtos.get(i).getAddress();
                hciNames.add(hciName);
                addresses.add(address);
            }
            request.getSession().setAttribute("id", licenceDto.getId());
            request.getSession().setAttribute("hciAddress", addresses);
            request.getSession().setAttribute("hciNames", hciNames);
            request.getSession().setAttribute("serviceName", svcName);
            request.getSession().setAttribute("licenceNo", licenceNo);
            request.getSession().setAttribute("appealNo", licenceDto.getLicenceNo());
        } else if (APPLICATION.equals(type)) {
            ApplicationDto applicationDto = applicationClient.getApplicationById(appealingFor).getEntity();
            boolean maxCGOnumber = isMaxCGOnumber(applicationDto);
            if (!maxCGOnumber) {
                request.getSession().setAttribute("maxCGOnumber", !maxCGOnumber);
            }
            AppFeeDetailsDto appFeeDetailsDto =
                    applicationClient.getAppFeeDetailsDtoByApplicationNo(applicationDto.getApplicationNo()).getEntity();
            if (appFeeDetailsDto != null) {
                try {
                    if (appFeeDetailsDto.getLaterFee() > 0.0) {
                        request.getSession().setAttribute("lateFee", Boolean.TRUE);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage() + "------", e);
                }
            }
            String serviceId = applicationDto.getServiceId();
            String id = applicationDto.getId();
            if (id != null) {
                AppInsRepDto entity = applicationClient.getHciNameAndAddress(id).getEntity();
                String hciName = entity.getHciName();
                String hciAddres = entity.getHciAddress();
                List<String> hciNames = IaisCommonUtils.genNewArrayList();
                hciNames.add(hciName);
                List<String> hciAddress = IaisCommonUtils.genNewArrayList();
                hciAddress.add(hciAddres);
                request.getSession().setAttribute("hciAddress", (Serializable) hciAddress);
                request.getSession().setAttribute("hciNames", (Serializable) hciNames);
            }

            List<String> list = IaisCommonUtils.genNewArrayList();
            list.add(serviceId);
            List<HcsaServiceDto> entity = appConfigClient.getHcsaService(list).getEntity();
            for (int i = 0; i < entity.size(); i++) {
                String svcName = entity.get(i).getSvcName();
                request.getSession().setAttribute("serviceName", svcName);
            }
            String applicationNo = applicationDto.getApplicationNo();
            request.getSession().setAttribute("id", applicationDto.getId());
            request.getSession().setAttribute("applicationNo", applicationNo);
            request.setAttribute("applicationDto", applicationDto);
            String status = applicationDto.getStatus();
            if (ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(status)) {
                request.getSession().setAttribute("applicationAPPROVED", "APPROVED");
            }
            request.getSession().setAttribute("appealNo", applicationDto.getApplicationNo());
            request.getSession().setAttribute("serviceId", applicationDto.getServiceId());
        }

    }

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        validae(request, errorMap);
        return errorMap;
    }

    @Override
    public void inbox(HttpServletRequest request, String appNo) {
        AppPremisesCorrelationDto entity1 = applicationClient.getCorrelationByAppNo(appNo).getEntity();
        AppPremiseMiscDto entity2 = applicationClient.getAppPremisesMisc(entity1.getId()).getEntity();
        String appealType = entity2.getAppealType();
        requetForInformationGetMessage(request, entity2);
        if("APPEAL001".equals(appealType)){
            request.getSession().setAttribute(TYPE, APPLICATION);
            typeApplicationOrLicence(request,APPLICATION,entity2.getRelateRecId());
        }else if("APPEAL002".equals(appealType)){
            request.getSession().setAttribute(TYPE, LICENCE);
            typeApplicationOrLicence(request,LICENCE,entity2.getRelateRecId());
        }

    }


    private List<AppSvcCgoDto> reAppSvcCgo(HttpServletRequest req) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) req.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        List<AppSvcCgoDto> appSvcCgoDtoList = IaisCommonUtils.genNewArrayList();
        AppSvcCgoDto appSvcCgoDto = null;
        String[] assignSelect = ParamUtil.getStrings(request, "assignSelect");
        int size = 0;
        if (assignSelect != null && assignSelect.length > 0) {
            size = assignSelect.length;
        }
        String[] salutation = ParamUtil.getStrings(request, "salutation");
        String[] name = ParamUtil.getStrings(request, "name");
        String[] idType = ParamUtil.getStrings(request, "idType");
        String[] idNo = ParamUtil.getStrings(request, "idNo");
        String[] designation = ParamUtil.getStrings(request, "designation");
        String[] professionType = ParamUtil.getStrings(request, "professionType");
        String[] professionRegoNo = ParamUtil.getStrings(request, "professionRegoNo");
        String[] specialty = ParamUtil.getStrings(request, "specialty");
        String[] specialtyOther = ParamUtil.getStrings(request, "specialtyOther");
        String[] qualification = ParamUtil.getStrings(request, "qualification");
        String[] mobileNo = ParamUtil.getStrings(request, "mobileNo");
        String[] emailAddress = ParamUtil.getStrings(request, "emailAddress");

        for (int i = 0; i < size; i++) {
            appSvcCgoDto = new AppSvcCgoDto();
            //cgoIndexNo
            String cgoIndexNo = new StringBuffer().append("cgo-").append(i).append("-").toString();
            appSvcCgoDto.setAssignSelect(assignSelect[i]);
            appSvcCgoDto.setSalutation(salutation[i]);
            appSvcCgoDto.setName(name[i]);
            appSvcCgoDto.setIdType(idType[i]);
            appSvcCgoDto.setIdNo(idNo[i]);
            appSvcCgoDto.setDesignation(designation[i]);
            appSvcCgoDto.setProfessionType(professionType[i]);
            appSvcCgoDto.setProfRegNo(professionRegoNo[i]);
            String specialtyStr = specialty[i];
            appSvcCgoDto.setSpeciality(specialtyStr);
            if ("other".equals(specialtyStr)) {
                appSvcCgoDto.setSpecialityOther(specialtyOther[i]);
            }
            //qualification
            appSvcCgoDto.setSubSpeciality(qualification[i]);
            appSvcCgoDto.setMobileNo(mobileNo[i]);
            appSvcCgoDto.setEmailAddr(emailAddress[i]);
            appSvcCgoDto.setCgoIndexNo(cgoIndexNo);
            appSvcCgoDtoList.add(appSvcCgoDto);
        }
        return appSvcCgoDtoList;
    }


    public void validae(HttpServletRequest req, Map<String, String> map) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) req.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        //CGO mix can add
        String serviceId = (String) request.getSession().getAttribute("serviceId");
        if (serviceId != null) {
            appConfigClient.getServiceType(serviceId, "CGO");
        }
        String isDelete = request.getParameter("isDelete");
        AppPremisesSpecialDocDto appPremisesSpecialDocDto = (AppPremisesSpecialDocDto) req.getSession().getAttribute("appPremisesSpecialDocDto");
        CommonsMultipartFile file = (CommonsMultipartFile) request.getFile("selectedFile");
        if (file != null && file.getSize() > 0) {
            int configFileSize = systemParamConfig.getUploadFileLimit();
            String configFileType = FileUtils.getStringFromSystemConfigString(systemParamConfig.getUploadFileType());
            List<String> fileTypes = Arrays.asList(configFileType.split(","));
            Map<String, Boolean> booleanMap = ValidationUtils.validateFile(file,fileTypes,(configFileSize * 1024 *1024L));
            Boolean fileSize = booleanMap.get("fileSize");
            Boolean fileType = booleanMap.get("fileType");
            //size
            if(!fileSize){
                map.put("file", MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(configFileSize),"sizeMax"));
            }
            //type
            if(!fileType){
                map.put("file",MessageUtil.replaceMessage("GENERAL_ERR0018", configFileType,"fileType"));
            }
            AppPremisesSpecialDocDto specialDocDto=new AppPremisesSpecialDocDto();
            long size = file.getSize() / 1024;
            String filename = file.getOriginalFilename();
            specialDocDto.setDocName(filename);
            specialDocDto.setDocSize(Integer.valueOf(size+""));
            req.getSession().setAttribute("appPremisesSpecialDocDto", specialDocDto);

        } else if (appPremisesSpecialDocDto != null && appPremisesSpecialDocDto.getDocSize() > 0) {
            if (Y.equals(isDelete)) {
                long size = appPremisesSpecialDocDto.getDocSize();
                if (size > 5 * 1024) {
                    map.put("file", "UC_GENERAL_ERR0015");
                }
                String filename = appPremisesSpecialDocDto.getDocName();
                String fileType = filename.substring(filename.lastIndexOf('.') + 1);
                String sysFileType = systemParamConfig.getUploadFileType();
                String configFileType = FileUtils.getStringFromSystemConfigString(sysFileType);
                String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
                Boolean flag=Boolean.FALSE;
                for(String f:sysFileTypeArr){
                    if(f.equalsIgnoreCase(fileType)){
                        flag=Boolean.TRUE;
                    }
                }
                if (!flag) {
                    map.put("file",MessageUtil.replaceMessage("GENERAL_ERR0018", configFileType,"fileType"));
                }
            }

        }


        AppealPageDto appealPageDto = reAppealPage(request);
        String remarks = appealPageDto.getRemarks();
        if (StringUtil.isEmpty(remarks)) {
            map.put("remarks", MessageUtil.replaceMessage("GENERAL_ERR0006","Any supporting remarks","field"));
        }
        String appealReason = appealPageDto.getAppealReason();

        if (StringUtil.isEmpty(appealReason)) {
            map.put("reason", MessageUtil.replaceMessage("GENERAL_ERR0006","Reason For Appeal","field"));
        } else {
            if (ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(appealReason)) {
                List<AppSvcCgoDto> appSvcCgoList = appealPageDto.getAppSvcCgoDto();
                if(IaisCommonUtils.isEmpty(appSvcCgoList)){
                    //todo
                    map.put("addCgo", MessageUtil.getMessageDesc("APPEAL_ERR002"));
                }
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < appSvcCgoList.size(); i++) {
                    StringBuilder stringBuilder1 = new StringBuilder();
                    String assignSelect = appSvcCgoList.get(i).getAssignSelect();
                    if ("-1".equals(assignSelect)) {
                        map.put("assignSelect" + i,  MessageUtil.replaceMessage("GENERAL_ERR0006","Add/Assign a Clinical Governance Officer","field"));
                    } else {
                        String idTyp = appSvcCgoList.get(i).getIdType();
                        if ("-1".equals(idTyp)) {
                            map.put("idTyp" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","ID No.","field"));
                        }
                        String salutation = appSvcCgoList.get(i).getSalutation();
                        if (StringUtil.isEmpty(salutation)) {
                            map.put("salutation" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","ID No. Type","field"));
                        }
                        String speciality = appSvcCgoList.get(i).getSpeciality();
                        if ("-1".equals(speciality)) {
                            map.put("speciality" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","Specialty","field"));
                        }
                        String professionType = appSvcCgoList.get(i).getProfessionType();
                        if (StringUtil.isEmpty(professionType)) {
                            map.put("professionType" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","Professional Type ","field"));
                        }
                        String designation = appSvcCgoList.get(i).getDesignation();
                        if (StringUtil.isEmpty(designation)) {
                            map.put("designation" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","Designation ","field"));
                        }
                        String professionRegoNo = appSvcCgoList.get(i).getProfRegNo();
                        if (StringUtil.isEmpty(professionRegoNo)) {
                            map.put("professionRegoNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","Professional Regn No.  ","field"));
                        }
                        String idNo = appSvcCgoList.get(i).getIdNo();
                        //to do
                        if (StringUtil.isEmpty(idNo)) {
                            map.put("idNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","ID No.  ","field"));
                        } else {
                            if ("FIN".equals(idTyp)) {
                                boolean b = SgNoValidator.validateFin(idNo);
                                if (!b) {
                                    map.put("idNo" + i, "CHKLMD001_ERR005");
                                }
                                stringBuilder1.append(idTyp).append(idNo);

                            } else if ("NRIC".equals(idTyp)) {
                                boolean b1 = SgNoValidator.validateNric(idNo);
                                if (!b1) {
                                    map.put("idNo" + i, "CHKLMD001_ERR005");
                                }
                                stringBuilder1.append(idTyp).append(idNo);

                            }

                        }
                        //to do

                        String Specialty = appSvcCgoList.get(i).getSpeciality();
                        if (StringUtil.isEmpty(Specialty)) {
                            map.put("speciality" + i,  MessageUtil.replaceMessage("GENERAL_ERR0006","Specialty","field"));
                        }

                        String name = appSvcCgoList.get(i).getName();
                        if (StringUtil.isEmpty(name)) {
                            map.put("name" + i,MessageUtil.replaceMessage("GENERAL_ERR0006","Name","field"));
                        }

                        String mobileNo = appSvcCgoList.get(i).getMobileNo();
                        if (StringUtil.isEmpty(mobileNo)) {
                            map.put("mobileNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","Mobile No. ","field"));
                        } else if (!StringUtil.isEmpty(mobileNo)) {
                            if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                                map.put("mobileNo" + i, "CHKLMD001_ERR004");
                            }
                        }
                        String emailAddr = appSvcCgoList.get(i).getEmailAddr();
                        if (StringUtil.isEmpty(emailAddr)) {
                            map.put("emailAddr" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","Email Address ","field"));
                        } else if (!StringUtil.isEmpty(emailAddr)) {
                            if (!ValidationUtils.isEmail(emailAddr)) {
                                map.put("emailAddr" + i, "CHKLMD001_ERR006");
                            }
                        }
                        String s = stringBuilder.toString();
                        if (!StringUtil.isEmpty(stringBuilder1.toString())) {
                            if (s.contains(stringBuilder1.toString())) {
                                map.put("idNo", "UC_CHKLMD001_ERR002");
                            } else {
                                stringBuilder.append(stringBuilder1.toString());
                            }
                        }

                    }

                }

            } else if (ApplicationConsts.APPEAL_REASON_OTHER.equals(appealReason)) {
                String otherReason = request.getParameter("othersReason");
                if (StringUtil.isEmpty(otherReason)) {
                    map.put("otherReason", "UC_CHKLMD001_ERR001");
                }
            }
        }


    }

    private AppealPageDto reAppealPage(HttpServletRequest request) {
        AppealPageDto appealPageDto = new AppealPageDto();

        String appealingFor = (String) request.getSession().getAttribute(APPEALING_FOR);

        String type = (String) request.getSession().getAttribute(TYPE);
        List<AppSvcCgoDto> appSvcCgoDtos = reAppSvcCgo(request);
        String reasonSelect = request.getParameter("reasonSelect");
        String proposedHciName = request.getParameter("proposedHciName");
        String remarks = request.getParameter("remarks");
        appealPageDto.setAppealReason(reasonSelect);
        appealPageDto.setAppealFor(appealingFor);
        appealPageDto.setType(type);
        appealPageDto.setAppSvcCgoDto(appSvcCgoDtos);
        if (ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME.equals(reasonSelect)) {
            appealPageDto.setNewHciName(proposedHciName);
        }
        appealPageDto.setRemarks(remarks);


        return appealPageDto;
    }


    private String licencePresmises(HttpServletRequest request, String licenceId) {
        LicenceDto licenceDto = licenceClient.getLicBylicId(licenceId).getEntity();
        ApplicationDto entity1 = (ApplicationDto) request.getSession().getAttribute("rfiApplication");
        String licenseeId = licenceDto.getLicenseeId();
        String rfi = (String) request.getSession().getAttribute("rfi");
        List<ApplicationDto> applicationDtoListlist = IaisCommonUtils.genNewArrayList();
        List<PremisesDto> premisess = licenceClient.getPremisesDto(licenceDto.getId()).getEntity();
        String appNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_APPEAL).getEntity();
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDto(appNo);
        applicationGroupDto.setLicenseeId(licenseeId);
        StringBuilder stringBuilder = new StringBuilder(appNo);
        String s = stringBuilder.append("-01").toString();
        List<AppGrpPremisesDto> premisesDtos = IaisCommonUtils.genNewArrayList();
        for (PremisesDto every : premisess) {
            AppGrpPremisesDto appGrpPremisesDto = MiscUtil.transferEntityDto(every, AppGrpPremisesDto.class);
            appGrpPremisesDto.setOffTelNo(every.getHciContactNo());
            premisesDtos.add(appGrpPremisesDto);
        }
        List<AppSvcCgoDto> list = IaisCommonUtils.genNewArrayList();
        for (AppGrpPremisesDto every : premisesDtos) {
            AppSvcCgoDto appSvcCgoDto = MiscUtil.transferEntityDto(every, AppSvcCgoDto.class);
            list.add(appSvcCgoDto);
            ApplicationDto applicationDto = new ApplicationDto();
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
            applicationDto.setApplicationType(ApplicationConsts.APPLICATION_TYPE_APPEAL);
            applicationDto.setVersion(1);
            if ("rfi".equals(rfi)) {
                applicationDto.setVersion(entity1.getVersion() + 1);
                //if not need new group
                applicationGroupDto.setId(entity1.getAppGrpId());
                applicationGroupDto.setGroupNo(entity1.getApplicationNo().substring(0, entity1.getApplicationNo().lastIndexOf('-')));
                applicationDto.setApplicationNo(entity1.getApplicationNo());
                applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
                List<AppPremisesRoutingHistoryDto> hisList;
                HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                String gatewayUrl = env.getProperty("iais.inter.gateway.url");
                Map<String, Object> params = IaisCommonUtils.genNewHashMap(1);
                params.put("appNo", appNo);
                hisList = IaisEGPHelper.callEicGatewayWithParamForList(gatewayUrl + "/v1/app-routing-history", HttpMethod.GET, params,
                        MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization(), AppPremisesRoutingHistoryDto.class).getEntity();
                if(hisList!=null){
                    for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : hisList){
                        if(ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(appPremisesRoutingHistoryDto.getProcessDecision())
                                || InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(appPremisesRoutingHistoryDto.getProcessDecision())){
                            if(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(appPremisesRoutingHistoryDto.getAppStatus())){
                                applicationDto.setStatus(ApplicationConsts.PENDING_ASO_REPLY);
                            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(appPremisesRoutingHistoryDto.getAppStatus())){
                                applicationDto.setStatus(ApplicationConsts.PENDING_PSO_REPLY);
                            }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(appPremisesRoutingHistoryDto.getAppStatus())){
                                applicationDto.setStatus(ApplicationConsts.PENDING_INP_REPLY);
                            }
                        }
                    }
                }

                s = entity1.getApplicationNo();
                entity1.setStatus(ApplicationConsts.APPLICATION_STATUS_DELETED);
                request.getSession().setAttribute("rfiApplication", entity1);
                applicationClient.updateApplication(entity1);
            }
            List<String> svcNames = IaisCommonUtils.genNewArrayList();
            svcNames.add(licenceDto.getSvcName());
            List<HcsaServiceDto> hcsaServiceDtos = appConfigClient.getHcsaServiceByNames(svcNames).getEntity();
            applicationDto.setServiceId(hcsaServiceDtos.get(0).getId());
            applicationDto.setApplicationNo(s);
            applicationDto.setOriginLicenceId(licenceDto.getOriginLicenceId());

            applicationDtoListlist.add(applicationDto);
        }
        //appealPageDto
        AppealPageDto appealDto = getAppealPageDto(request);
        String reasonSelect = appealDto.getAppealReason();

        List<AppSvcCgoDto> appSvcCgoDtos = null;
        if (ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(reasonSelect)) {
            appSvcCgoDtos = reAppSvcCgo(request);
        }


        appealDto.setApplicationGroupDto(applicationGroupDto);
        appealDto.setAppId(licenceDto.getId());
        appealDto.setApplicationDto(applicationDtoListlist);
        appealDto.setAppealType(ApplicationConsts.APPEAL_TYPE_LICENCE);
        if (entity1 != null) {
            String status = entity1.getStatus();
            if (ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(status)) {
                appealDto.setAppealType("APPEAL006");
            }
        }


        if (appSvcCgoDtos != null && !appSvcCgoDtos.isEmpty()) {
            appealDto.setAppSvcCgoDto(appSvcCgoDtos);

        }
        appealDto.setAppGrpPremisesDtos(premisesDtos);
        appealDto = applicationClient.submitAppeal(appealDto).getEntity();
        ApplicationGroupDto applicationGroupDto1 = appealDto.getApplicationGroupDto();
        String groupId = applicationGroupDto1.getId();
        request.setAttribute("groupId", groupId);
        request.setAttribute("draftStatus", AppConsts.COMMON_STATUS_IACTIVE);
        saveData(request);
        String newApplicationNo = arrayToString(applicationDtoListlist,appNo);
        request.setAttribute("newApplicationNo", appNo);
        //todo send email
        try {
            sendEmail(request);
            sendAdminEmail(request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return appNo;
    }

    public String arrayToString(List<ApplicationDto> applicationDtoListlist,String groupNo){
        StringBuilder appNos = new StringBuilder();

        if(applicationDtoListlist.size() == 1){
            appNos.append(groupNo).append("-01");
        }else{
            for(int i = 0;i < applicationDtoListlist.size(); i++){
                if(i == applicationDtoListlist.size() -1){
                    int number = i + 1;
                    if(number <= 9){
                        appNos.append(groupNo).append("-0").append(number);
                    }else{
                        appNos.append(groupNo).append('-').append(number);
                    }
                }else{
                    int number = i + 1;
                    if(number <= 9){
                        appNos.append(groupNo).append("-0").append(number).append(", ");
                    }else{
                        appNos.append(groupNo).append('-').append(number).append(", ");
                    }
                }
            }
        }
        return appNos.toString();
    }

    private ApplicationGroupDto getApplicationGroupDto(String appNo) {

        ApplicationGroupDto applicationGroupDto = new ApplicationGroupDto();
        applicationGroupDto.setSubmitDt(new Date());
        applicationGroupDto.setGroupNo(appNo);
        applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        applicationGroupDto.setAmount(0.0);
        applicationGroupDto.setIsPreInspection(1);
        applicationGroupDto.setIsInspectionNeeded(1);
        applicationGroupDto.setLicenseeId("36F8537B-FE17-EA11-BE78-000C29D29DB0");
        applicationGroupDto.setIsBundledFee(0);
        applicationGroupDto.setIsCharitable(0);
        applicationGroupDto.setIsByGiro(0);
        //applicationGroupDto.setGrpLic(false);
        applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        applicationGroupDto.setDeclStmt(N);
        applicationGroupDto.setSubmitBy("C55C9E62-750B-EA11-BE7D-000C29F371DC");
        applicationGroupDto.setAppType(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        return applicationGroupDto;

    }

    private String applicationPresmies(HttpServletRequest request, String applicationId) {
        ApplicationDto applicationDto = applicationClient.getApplicationById(applicationId).getEntity();
        String grpId = applicationDto.getAppGrpId();
        String rfi = (String) request.getSession().getAttribute("rfi");
        ApplicationGroupDto entity = applicationClient.getApplicationGroup(grpId).getEntity();
        String appNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_APPEAL).getEntity();
        StringBuilder stringBuilder = new StringBuilder(appNo);
        String s = stringBuilder.append("-01").toString();
        //appealPageDto
        AppealPageDto appealDto = getAppealPageDto(request);
        String reasonSelect = appealDto.getAppealReason();


        List<AppSvcCgoDto> appSvcCgoDtos = null;
        if (ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(reasonSelect)) {
            appSvcCgoDtos = reAppSvcCgo(request);
        }

        AppliSpecialDocDto appliSpecialDocDto = new AppliSpecialDocDto();
        appliSpecialDocDto.setSubmitBy("68F8BB01-F70C-EA11-BE7D-000C29F371DC");

        //group
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDto(appNo);
        //info
        applicationGroupDto.setLicenseeId(entity.getLicenseeId());

        ApplicationDto applicationDto1 = new ApplicationDto();
        applicationDto1.setApplicationType(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        applicationDto1.setApplicationNo(s);
        //info

        applicationDto1.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
        applicationDto1.setServiceId(applicationDto.getServiceId());
        applicationDto1.setVersion(1);
        List<ApplicationDto> list = IaisCommonUtils.genNewArrayList();
        list.add(applicationDto1);
        appealDto.setApplicationGroupDto(applicationGroupDto);

        appealDto.setAppId(applicationDto.getId());
        appealDto.setApplicationDto(list);
        appealDto.setAppealType(ApplicationConsts.APPEAL_TYPE_APPLICAITON);
        //if infomation
        if ("rfi".equals(rfi)) {
            ApplicationDto rfiApplication = (ApplicationDto) ParamUtil.getSessionAttr(request,"rfiApplication");
            applicationDto1.setVersion(rfiApplication.getVersion() + 1);
            //if need new group
            applicationGroupDto.setId(rfiApplication.getAppGrpId());
            applicationGroupDto.setGroupNo(rfiApplication.getApplicationNo().substring(0,rfiApplication.getApplicationNo().lastIndexOf('-')));
            applicationDto1.setApplicationNo(rfiApplication.getApplicationNo());
            appealDto.setAppealType("APPEAL006");
            applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
            applicationDto1.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY);
            s = rfiApplication.getApplicationNo();
            rfiApplication.setStatus(ApplicationConsts.APPLICATION_STATUS_DELETED);
            applicationClient.updateApplication(rfiApplication);
            ParamUtil.setSessionAttr(request,"rfiApplication",rfiApplication);
        }

        if (appSvcCgoDtos != null && !appSvcCgoDtos.isEmpty()) {
            appealDto.setAppSvcCgoDto(appSvcCgoDtos);

        }
        AppealPageDto appealPageDto = applicationClient.submitAppeal(appealDto).getEntity();
        ApplicationGroupDto applicationGroupDto1 = appealPageDto.getApplicationGroupDto();
        String groupId = applicationGroupDto1.getId();
        request.setAttribute("groupId", groupId);
        request.setAttribute("draftStatus", AppConsts.COMMON_STATUS_IACTIVE);
        saveData(request);
        request.setAttribute("newApplicationNo", s);
        try {
            sendEmail(request);
            sendAdminEmail(request);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        //todo send email
        return s;
    }

    private void requetForInformationGetMessage(HttpServletRequest request, AppPremiseMiscDto appPremiseMiscDto) {
        String reason = appPremiseMiscDto.getReason();
        String appPremCorreId = appPremiseMiscDto.getAppPremCorreId();
        AppPremisesSpecialDocDto appPremisesSpecialDocDto = applicationClient.getAppPremisesSpecialDocDtoByCorreId(appPremCorreId).getEntity();
        if (appPremisesSpecialDocDto != null) {
            String docName = appPremisesSpecialDocDto.getDocName();
            request.getSession().setAttribute("filename", docName);
            request.getSession().setAttribute("appPremisesSpecialDocDto", appPremisesSpecialDocDto);
        }
        request.setAttribute("appPremiseMiscDto", appPremiseMiscDto);
        ApplicationDto entity = applicationClient.getApplicationByCorrId(appPremCorreId).getEntity();
        if (entity != null) {
            if (ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(reason)) {
                String appGrpId = entity.getAppGrpId();
                List<AppSvcCgoDto> appSvcCgoDtos = applicationClient.getAppGrpPersonnelByGrpId(appGrpId).getEntity();
                ParamUtil.setRequestAttr(request, "CgoMandatoryCount", appSvcCgoDtos.size());
                List<SelectOption> cgoSelectList = IaisCommonUtils.genNewArrayList();
                SelectOption sp0 = new SelectOption("-1", "Select Personnel");
                cgoSelectList.add(sp0);
                SelectOption sp1 = new SelectOption("newOfficer", "I'd like to add a new personnel");
                cgoSelectList.add(sp1);
                ParamUtil.setSessionAttr(request, "CgoSelectList", (Serializable) cgoSelectList);
                ParamUtil.setSessionAttr(request, "GovernanceOfficersList", (Serializable) appSvcCgoDtos);
                HcsaServiceDto serviceDto= HcsaServiceCacheHelper.getServiceById(entity.getServiceId());
                List<SelectOption> idTypeSelOp = AppealDelegator.getIdTypeSelOp();
                ParamUtil.setSessionAttr(request, "IdTypeSelect",(Serializable)  idTypeSelOp);
                if (serviceDto != null) {
                    HcsaServiceDto serviceByServiceName = HcsaServiceCacheHelper.getServiceByServiceName(serviceDto.getSvcName());
                    List<SelectOption> list = AppealDelegator.genSpecialtySelectList(serviceByServiceName.getSvcCode());
                    ParamUtil.setSessionAttr(request, "SpecialtySelectList", (Serializable) list);
                }
            }
        }else {
            return;
        }

        request.getSession().setAttribute("rfi", "rfi");
        request.getSession().setAttribute("rfiApplication", entity);

    }

    private AppealPageDto getAppealPageDto(HttpServletRequest req) {
        LoginContext loginContext = (LoginContext) req.getSession().getAttribute("loginContext");
        AppealPageDto appealDto = new AppealPageDto();
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) req.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        AppPremisesSpecialDocDto sessionAppPremisesSpecialDoc = (AppPremisesSpecialDocDto) req.getSession().getAttribute("appPremisesSpecialDocDto");
        String reasonSelect = request.getParameter("reasonSelect");
        String licenceYear = request.getParameter("licenceYear");
        String proposedHciName = request.getParameter("proposedHciName");
        String isDelete = request.getParameter("isDelete");
        String remarks = request.getParameter("remarks");
        String othersReason = request.getParameter("othersReason");
        appealDto.setOtherReason(othersReason);
        CommonsMultipartFile selectedFile = (CommonsMultipartFile) request.getFile("selectedFile");
        if (selectedFile != null && selectedFile.getSize() > 0) {
            try {
                String fileToRepo = serviceConfigService.saveFileToRepo(selectedFile);
                Long size = selectedFile.getSize() / 1024;
                String filename = selectedFile.getOriginalFilename();
                String s = FileUtil.genMd5FileChecksum(selectedFile.getBytes());
                AppPremisesSpecialDocDto appPremisesSpecialDocDto = new AppPremisesSpecialDocDto();
                appPremisesSpecialDocDto.setDocName(filename);
                appPremisesSpecialDocDto.setMd5Code(s);
                appPremisesSpecialDocDto.setFileRepoId(fileToRepo);
                if (loginContext != null) {
                    appPremisesSpecialDocDto.setSubmitBy(loginContext.getUserId());
                } else {
                    appPremisesSpecialDocDto.setSubmitBy("68F8BB01-F70C-EA11-BE7D-000C29F371DC");
                }
                appPremisesSpecialDocDto.setDocSize(Integer.valueOf(size.toString()));
                appealDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDto);

            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        } else if (sessionAppPremisesSpecialDoc != null && Y.equals(isDelete)) {
            AppPremisesSpecialDocDto appPremisesSpecialDocDto = new AppPremisesSpecialDocDto();
            appPremisesSpecialDocDto.setDocName(sessionAppPremisesSpecialDoc.getDocName());
            appPremisesSpecialDocDto.setMd5Code(sessionAppPremisesSpecialDoc.getMd5Code());
            appPremisesSpecialDocDto.setFileRepoId(sessionAppPremisesSpecialDoc.getFileRepoId());
            if (loginContext != null) {
                appPremisesSpecialDocDto.setSubmitBy(loginContext.getUserId());
            } else {
                appPremisesSpecialDocDto.setSubmitBy("68F8BB01-F70C-EA11-BE7D-000C29F371DC");
            }
            appPremisesSpecialDocDto.setDocSize(sessionAppPremisesSpecialDoc.getDocSize());
            if (sessionAppPremisesSpecialDoc.getFileRepoId() != null) {
                appealDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDto);
            }
        }

        appealDto.setRemarks(remarks);
        appealDto.setAppealReason(reasonSelect);
        if (ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME.equals(reasonSelect)) {
            appealDto.setNewHciName(proposedHciName);
        }
        if (ApplicationConsts.APPEAL_REASON_LICENCE_CHANGE_PERIOD.equals(reasonSelect)) {
            if (!StringUtil.isEmpty(licenceYear)) {
                appealDto.setNewLicYears(Integer.valueOf(licenceYear));
            }
        }

        return appealDto;
    }


    private void sendEmail(HttpServletRequest request) throws IOException, TemplateException {
        LoginContext loginContext = (LoginContext) request.getSession().getAttribute("loginContext");
        String newApplicationNo = (String) request.getAttribute("newApplicationNo");
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("applicationNo", newApplicationNo);
        MsgTemplateDto entity = msgTemplateClient.getMsgTemplate("55314F99-F97A-EA11-BE82-000C29F371DC").getEntity();
        String messageContent = entity.getMessageContent();
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(messageContent, map);
        EmailDto emailDto = new EmailDto();
        emailDto.setContent(templateMessageByContent);
        emailDto.setSubject(" MOH IAIS –Submission of Appeal - " + newApplicationNo);
        emailDto.setSender(mailSender);
        emailDto.setClientQueryCode("Appeal");
        if (loginContext != null) {
            List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(loginContext.getLicenseeId());
            if (licenseeEmailAddrs != null) {
                log.info(licenseeEmailAddrs.toString());
                emailDto.setReceipts(licenseeEmailAddrs);
                HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                try {
                    feEicGatewayClient.feSendEmail(emailDto, signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }


            }
        }
        //need address form login
    }

    private void sendAdminEmail(HttpServletRequest request) throws IOException, TemplateException {
        List<String> email = adminEmail(request);
        String newApplicationNo =(String) request.getAttribute("newApplicationNo");
        EmailDto emailDto=new EmailDto();
        emailDto.setContent("Send notification to Admin Officer when appeal application is submitted.");
        emailDto.setSubject(" MOH IAIS –Submission of Appeal - "+newApplicationNo);
        emailDto.setSender(mailSender);
        emailDto.setClientQueryCode(newApplicationNo);
        emailDto.setReceipts(email);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        try {
            feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
        }catch (Exception e){
            log.error(e.getMessage(),e);
            }
        }


        private List<String> adminEmail(HttpServletRequest request) {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String orgId = loginContext.getOrgId();
        List<String> email = requestForChangeService.getAdminEmail(orgId);
        return email;
    }


    private boolean isMaxCGOnumber(ApplicationDto applicationDto) {
        String serviceId = applicationDto.getServiceId();

        List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos = applicationClient.getAppSvcKeyPersonnel(applicationDto).getEntity();
        HcsaSvcPersonnelDto hcsaSvcPersonnelDto = appConfigClient.getHcsaSvcPersonnelDtoByServiceId(serviceId).getEntity();
        if (hcsaSvcPersonnelDto != null) {
            int maximumCount = hcsaSvcPersonnelDto.getMaximumCount();
            int size = appSvcKeyPersonnelDtos.size();
            if (size <= maximumCount) {
                return false;
            }
        }

        return true;
    }


}
