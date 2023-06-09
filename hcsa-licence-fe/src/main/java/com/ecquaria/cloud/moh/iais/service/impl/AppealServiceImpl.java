package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.action.AppealDelegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppliSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.AppealService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.ComFileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.ConfigCommClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceFeMsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.sz.commons.util.FileUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Optional;

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
    private static final String SAVE_DRAFT_NO = "saveDraftNo";
    private static final String APPPREMISESSPECIALDOCDTO = "appPremisesSpecialDocDto";
    private static final String REASON_SELECT = "reasonSelect";
    private static final String PROPOSED_HCI_NAME = "proposedHciName";
    private static final String REMARKS = "remarks";
    private static final String OTHERS_REASON = "othersReason";
    private static final String DRAFT_STATUS = "draftStatus";
    private static final String PAGE_SHOW_FILE_HASH_MAP = "pageShowFileHashMap";
    private static final String SELECTED_FILE_DIV = "selectedFileDiv";
    private static final String PAGE_SHOW_FILES = "pageShowFiles";
    private static final String SELECTED_FILE = "selectedFile";
    private static final String FILENAME = "filename";
    private static final String FILE_REPORT_ID_FOR_APPEAL = "fileReportIdForAppeal";
    private static final String CGO_MANDATORY_COUNT = "CgoMandatoryCount";
    private static final String GOVERNANCE_OFFICERS_LIST= "GovernanceOfficersList";
    private static final String GROUP_ID = "groupId";
    private static final String APPPREMISEMISCDTO = "appPremiseMiscDto";
    private static final String SERVICE_NAME = "serviceName";
    private static final String MS = "MS007";
    private static final String OTHERS = "Others";
    private static final String SELECT_OPTION_LIST = "selectOptionList";
    private static final String PROFESSION_REGO_NO = "professionRegoNo";
    private static final String OTHER_DESIGNATION = "otherDesignation";
    private static final String OTHER_QUALIFICATION = "otherQualification";
    private static final String MOBILE_NO = "mobileNo";
    private static final String FILE_TYPE = "fileType";
    private static final String SIZE_MAX = "sizeMax";
    private static final String FIELD = "field";
    private static final String MAXLENGTH = "maxlength";

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private ApplicationFeClient applicationFeClient;
    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private ConfigCommClient configCommClient;
    @Autowired
    private LicenceFeMsgTemplateClient licenceFeMsgTemplateClient;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;
    @Autowired
    private RequestForChangeService requestForChangeService;
    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;
    @Autowired
    private ComFileRepoClient comFileRepoClient;
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private AppCommService appCommService;

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
        String  licenseeId = loginContext.getLicenseeId();
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) req.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String saveDraftId = (String) req.getSession().getAttribute(SAVE_DRAFT_NO);
        AppPremisesSpecialDocDto appPremisesSpecialDocDto = (AppPremisesSpecialDocDto) req.getSession().getAttribute(APPPREMISESSPECIALDOCDTO);
        String appealingFor = request.getParameter(APPEALING_FOR);
        String isDelete = request.getParameter("isDelete");
        String reasonSelect = request.getParameter(REASON_SELECT);
        String proposedHciName = request.getParameter(PROPOSED_HCI_NAME);
        String remarks = request.getParameter(REMARKS);
        String othersReason = request.getParameter(OTHERS_REASON);
        String draftStatus = (String) request.getAttribute(DRAFT_STATUS);
        AppealPageDto appealPageDto = reAppealPage(request);
        Map<String, File> map = (Map<String, File>)req.getSession().getAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX_FE_SELECT_FILE);
        Map<String, PageShowFileDto> pageShowFileHashMap = (Map<String, PageShowFileDto>)request.getSession().getAttribute(PAGE_SHOW_FILE_HASH_MAP);
        List<AppPremisesSpecialDocDto> appPremisesSpecialDocDtoList =new ArrayList<>(5);
        List<PageShowFileDto> pageShowFileDtos =new ArrayList<>(5);
        List<File> files=new ArrayList<>(5);
        if(map!=null&&!map.isEmpty()){
            map.forEach((k,v)->{
                if(v!=null){
                    long length = v.length();
                    if(length>0){
                        Long size=length/1024;
                        files.add(v);
                        AppPremisesSpecialDocDto premisesSpecialDocDto=new AppPremisesSpecialDocDto();
                        premisesSpecialDocDto.setDocName(v.getName());
                        String fileMd5 = FileUtils.getFileMd5(v);
                        premisesSpecialDocDto.setMd5Code(fileMd5);
                        premisesSpecialDocDto.setSubmitBy(loginContext.getUserId());
                        premisesSpecialDocDto.setDocSize(Integer.valueOf(size.toString()));
                        PageShowFileDto pageShowFileDto =new PageShowFileDto();
                        pageShowFileDto.setFileName(v.getName());
                        String e = k.substring(k.lastIndexOf('e') + 1);
                        pageShowFileDto.setIndex(e);
                        pageShowFileDto.setFileMapId(SELECTED_FILE_DIV+e);
                        pageShowFileDto.setSize(Integer.valueOf(size.toString()));
                        pageShowFileDto.setMd5Code(fileMd5);
                        premisesSpecialDocDto.setIndex(k);
                        appPremisesSpecialDocDtoList.add(premisesSpecialDocDto);
                        pageShowFileDtos.add(pageShowFileDto);
                    }
                }else {
                    if(pageShowFileHashMap!=null){
                        PageShowFileDto pageShowFileDto = pageShowFileHashMap.get(k);
                        AppPremisesSpecialDocDto premisesSpecialDocDto=new AppPremisesSpecialDocDto();
                        premisesSpecialDocDto.setDocName(pageShowFileDto.getFileName());
                        premisesSpecialDocDto.setFileRepoId(pageShowFileDto.getFileUploadUrl());
                        premisesSpecialDocDto.setDocSize(pageShowFileDto.getSize());
                        premisesSpecialDocDto.setMd5Code(pageShowFileDto.getMd5Code());
                        premisesSpecialDocDto.setSubmitBy(loginContext.getUserId());
                        premisesSpecialDocDto.setIndex(k.substring(k.lastIndexOf('e') + 1));
                        appPremisesSpecialDocDtoList.add(premisesSpecialDocDto);
                        pageShowFileDtos.add(pageShowFileDto);
                    }
                }

            });
        }
        List<String> list = comFileRepoClient.saveFileRepo(files);
        if(list!=null){
            ListIterator<String> iterator = list.listIterator();
            for(int j=0;j< appPremisesSpecialDocDtoList.size();j++){
                String fileRepoId = appPremisesSpecialDocDtoList.get(j).getFileRepoId();
                if(fileRepoId==null && iterator.hasNext()){
                    String next = iterator.next();
                    pageShowFileDtos.get(j).setFileUploadUrl(next);
                    appPremisesSpecialDocDtoList.get(j).setFileRepoId(next);
                    iterator.remove();
                }
            }
        }
        appealPageDto.setAppPremisesSpecialDocDtos(appPremisesSpecialDocDtoList);
        Collections.sort(pageShowFileDtos,(s1,s2)->s1.getFileMapId().compareTo(s2.getFileMapId()));
        req.getSession().setAttribute(PAGE_SHOW_FILES, pageShowFileDtos);
        CommonsMultipartFile selectedFile = (CommonsMultipartFile) request.getFile(SELECTED_FILE);
        if (selectedFile != null && selectedFile.getSize() > 0) {
            String filename = selectedFile.getOriginalFilename();
            req.setAttribute(FILENAME, filename);
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
                    appPremisesSpecialDocDto.setSubmitBy(loginContext.getUserId());
                    req.getSession().setAttribute(APPPREMISESSPECIALDOCDTO, appPremisesSpecialDocDto);
                    req.getSession().setAttribute(FILE_REPORT_ID_FOR_APPEAL,fileToRepo);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            appealPageDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDto);
        } else if (appPremisesSpecialDocDto != null && appPremisesSpecialDocDto.getDocSize() > 0 && Y.equals(isDelete)) {
            String filename = appPremisesSpecialDocDto.getDocName();
            req.setAttribute(FILENAME, filename);
            appealPageDto.setFileName(filename);
            appealPageDto.setAppPremisesSpecialDocDto(appPremisesSpecialDocDto);
        }
        if (N.equals(isDelete)) {
            req.getSession().removeAttribute(APPPREMISESSPECIALDOCDTO);
            req.getSession().removeAttribute(FILENAME);
        }
        ParamUtil.setSessionAttr(req, CGO_MANDATORY_COUNT, appealPageDto.getAppSvcCgoDto().size());
        ParamUtil.setSessionAttr(req, GOVERNANCE_OFFICERS_LIST, (Serializable) appealPageDto.getAppSvcCgoDto());
        String groupId = (String) request.getAttribute(GROUP_ID);
        appealPageDto.setOtherReason(othersReason);
        String s = JsonUtil.parseToJson(appealPageDto);
        AppPremiseMiscDto appPremiseMiscDto = new AppPremiseMiscDto();
        if (!StringUtil.isEmpty(saveDraftId)) {
            AppSubmissionDto entity = applicationFeClient.draftNumberGet(saveDraftId).getEntity();
            if (entity != null) {
                entity.setAmountStr(s);
                entity.setAppGrpId(groupId);
                if (!StringUtil.isEmpty(draftStatus)) {
                    entity.setDraftStatus(draftStatus);
                }
                if (groupId == null) {
                    entity.setAppGrpStatus(AppConsts.COMMON_STATUS_ACTIVE);
                } else {
                    entity.setAppGrpStatus(AppConsts.COMMON_STATUS_IACTIVE);
                }
                entity.setLicenseeId(licenseeId);
                Object errorMsg = req.getAttribute("errorMsg");
                entity.setLicenceId(appealPageDto.getAppealFor());
                if(errorMsg==null){
                    req.setAttribute("saveDraftSuccess", "success");
                    entity.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    applicationFeClient.saveDraft(entity).getEntity();
                }


            }
            appPremiseMiscDto.setRemarks(remarks);
            appPremiseMiscDto.setReason(reasonSelect);
            if (ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME.equals(reasonSelect)) {
                appPremiseMiscDto.setNewHciName(proposedHciName);
            }
            req.setAttribute(APPEALING_FOR, appealingFor);
            appPremiseMiscDto.setOtherReason(othersReason);
            req.setAttribute(APPPREMISEMISCDTO, appPremiseMiscDto);
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
        appSubmissionDto.setLicenseeId(licenseeId);
        String serviceName = (String) req.getSession().getAttribute(SERVICE_NAME);
        HcsaServiceDto serviceByServiceName = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = new ArrayList<>(1);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
        appSvcRelatedInfoDto.setServiceCode(serviceByServiceName.getSvcCode());
        appSvcRelatedInfoDto.setServiceName(serviceByServiceName.getSvcName());
        appSvcRelatedInfoDtoList.add(appSvcRelatedInfoDto);
        appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
        appSubmissionDto.setServiceName(serviceName);
        appSubmissionDto.setLicenceId(appealPageDto.getAppealFor());
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        Object errorMsg = req.getAttribute("errorMsg");
        if(errorMsg==null){
            req.setAttribute("saveDraftSuccess", "success");
            AppSubmissionDto entity = applicationFeClient.saveDraft(appSubmissionDto).getEntity();
            String draftNo = entity.getDraftNo();
            req.getSession().setAttribute(SAVE_DRAFT_NO, draftNo);
        }

        appPremiseMiscDto.setRemarks(remarks);
        appPremiseMiscDto.setOtherReason(othersReason);
        appPremiseMiscDto.setReason(reasonSelect);
        appPremiseMiscDto.setNewHciName(proposedHciName);
        req.setAttribute(APPPREMISEMISCDTO, appPremiseMiscDto);
        req.setAttribute(APPEALING_FOR, appealingFor);



        return null;
    }

    @Override
    public void getMessage(HttpServletRequest request) {
        String draftNumber = ParamUtil.getMaskedString(request, "DraftNumber");
        if (draftNumber != null) {
            AppSubmissionDto appSubmissionDto = applicationFeClient.draftNumberGet(draftNumber).getEntity();
            String serviceName = appSubmissionDto.getServiceName();
            request.getSession().setAttribute(SERVICE_NAME, serviceName);
            HcsaServiceDto serviceDto= HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
            ParamUtil.setSessionAttr(request, HcsaAppConst.CURRENTSVCCODE,serviceDto.getSvcCode());
            String amountStr = appSubmissionDto.getAmountStr();
            try {
                AppealPageDto appealPageDto = JsonUtil.parseToObject(amountStr, AppealPageDto.class);
                String appealReason = appealPageDto.getAppealReason();
                String remarks = appealPageDto.getRemarks();
                AppPremiseMiscDto appPremiseMiscDto = new AppPremiseMiscDto();
                appPremiseMiscDto.setReason(appealReason);
                appPremiseMiscDto.setRemarks(remarks);
                appPremiseMiscDto.setOtherReason(appealPageDto.getOtherReason());
                appPremiseMiscDto.setNewHciName(appealPageDto.getNewHciName());
                String appealFor = appealPageDto.getAppealFor();
                AppPremisesSpecialDocDto appPremisesSpecialDocDto = appealPageDto.getAppPremisesSpecialDocDto();
                String type = appealPageDto.getType();
                if (appPremisesSpecialDocDto != null) {
                    String fileName = appPremisesSpecialDocDto.getDocName();
                    request.getSession().setAttribute(FILENAME, fileName);
                    request.getSession().setAttribute(FILE_REPORT_ID_FOR_APPEAL, appPremisesSpecialDocDto.getFileRepoId());
                    request.getSession().setAttribute(APPPREMISESSPECIALDOCDTO, appPremisesSpecialDocDto);
                }
                List<AppPremisesSpecialDocDto> appPremisesSpecialDocDtos = appealPageDto.getAppPremisesSpecialDocDtos();
                List<PageShowFileDto> pageShowFileDtos =new ArrayList<>(5);
                HashMap<String,File> map = IaisCommonUtils.genNewHashMap();
                HashMap<String, PageShowFileDto> pageShowFileHashMap= IaisCommonUtils.genNewHashMap();
                int indexMax = -1;
                if(appPremisesSpecialDocDtos!=null&&!appPremisesSpecialDocDtos.isEmpty()){
                    for(int i=0;i<appPremisesSpecialDocDtos.size();i++){
                        AppPremisesSpecialDocDto appPremisesSpecialDocDtoOne  = appPremisesSpecialDocDtos.get(i);
                        String index = appPremisesSpecialDocDtoOne.getIndex();
                        if(StringUtil.isEmpty(index)){
                            index = String.valueOf(i);
                        }
                        String e = index.substring(index.lastIndexOf('e') + 1);
                        int indexInt = Integer.parseInt(e);
                        if(indexInt >= indexMax){
                            indexMax = indexInt;
                        }
                        PageShowFileDto pageShowFileDto =new PageShowFileDto();
                        pageShowFileDto.setFileName(appPremisesSpecialDocDtoOne.getDocName());
                        pageShowFileDto.setIndex(e);
                        pageShowFileDto.setFileMapId(SELECTED_FILE_DIV+indexInt);
                        pageShowFileDto.setSize(appPremisesSpecialDocDtoOne.getDocSize());
                        pageShowFileDto.setMd5Code(appPremisesSpecialDocDtoOne.getMd5Code());
                        pageShowFileDto.setFileUploadUrl(appPremisesSpecialDocDtoOne.getFileRepoId());
                        pageShowFileDto.setVersion(appPremisesSpecialDocDtoOne.getVersion());
                        pageShowFileDtos.add(pageShowFileDto);
                        map.put(index,null);
                        pageShowFileHashMap.put(index, pageShowFileDto);
                    }
                    request.getSession().setAttribute(PAGE_SHOW_FILE_HASH_MAP,pageShowFileHashMap);
                    request.getSession().setAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX_FE_SELECT_FILE,map);
                    request.getSession().setAttribute("seesion_files_map_ajax_feselectedFile_MaxIndex", indexMax+1);

                }
                Collections.sort(pageShowFileDtos,(s1,s2)->s1.getFileMapId().compareTo(s2.getFileMapId()));
                request.getSession().setAttribute(PAGE_SHOW_FILES, pageShowFileDtos);
                if (ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(appealReason)) {
                    List<AppSvcPrincipalOfficersDto> appSvcCgoDto = appealPageDto.getAppSvcCgoDto();
                    request.getSession().setAttribute(CGO_MANDATORY_COUNT, appSvcCgoDto.size());
                    request.getSession().setAttribute(GOVERNANCE_OFFICERS_LIST, appSvcCgoDto);
                    LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( request, AppConsts.SESSION_ATTR_LOGIN_USER);
                    List<FeUserDto> feUserDtos = requestForChangeService.getFeUserDtoByLicenseeId(loginContext.getLicenseeId());
                    ParamUtil.setSessionAttr(request, HcsaAppConst.CURR_ORG_USER_ACCOUNT, (Serializable) feUserDtos);
                    List<PersonnelListQueryDto> licPersonList = requestForChangeService.getLicencePersonnelListQueryDto(loginContext.getLicenseeId());
                    Map<String, AppSvcPersonAndExtDto> licPersonMap=IaisCommonUtils.genNewHashMap();
                    Map<String, AppSvcPersonAndExtDto> personMap = ApplicationHelper.getLicPsnIntoSelMap(feUserDtos,licPersonList,licPersonMap);
                    ParamUtil.setSessionAttr(request, HcsaAppConst.PERSONSELECTMAP, (Serializable) personMap);
                    List<SelectOption> cgoSelectList = ApplicationHelper.genAssignPersonSel(request, true);
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
                request.setAttribute(APPPREMISEMISCDTO, appPremiseMiscDto);
                request.getSession().setAttribute(TYPE, type);
                request.getSession().setAttribute(APPEALING_FOR, appealFor);
                request.getSession().setAttribute(SAVE_DRAFT_NO, draftNumber);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            return;
        }
        String appealingFor = ParamUtil.getMaskedString(request, APPEALING_FOR);
        if(request.getAttribute("appealRfi")!=null){
             appealingFor =(String)request.getAttribute("applicationId");
        }
        String type = request.getParameter(TYPE);
        AppPremiseMiscDto appPremiseMiscDto = initRfi(request, appealingFor);
        if(appPremiseMiscDto!=null){
            if(ApplicationConsts.APPEAL_TYPE_APPLICAITON.equals(appPremiseMiscDto.getAppealType())){
                type=APPLICATION;
            }else if(ApplicationConsts.APPEAL_TYPE_LICENCE.equals(appPremiseMiscDto.getAppealType())){
                type=LICENCE;
            }
        }
        typeApplicationOrLicence(request, type, appealingFor);
        request.getSession().setAttribute(APPEALING_FOR, appealingFor);
        request.getSession().setAttribute(TYPE, type);
    }

    private AppPremiseMiscDto initRfi(HttpServletRequest request, String appealingFor) {
        AppPremiseMiscDto entity = applicationFeClient.getAppPremiseMiscDtoByAppId(appealingFor).getEntity();
        if (entity != null) {
            requetForInformationGetMessage(request, entity);
        }
        return entity;
    }

    private void typeApplicationOrLicence(HttpServletRequest request, String type, String appealingFor) {
        List<SelectOption> selectOptionList=new ArrayList<>(7);
        if (LICENCE.equals(type)) {
            LicenceDto licenceDto = licenceClient.getLicDtoById(appealingFor).getEntity();
            Date createdAt = licenceDto.getCreatedAt();
            Calendar calendar=Calendar.getInstance();
            calendar.setTime(createdAt);
            calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(systemParamConfig.getLicencePeriod()));
            boolean periodEqDay = calendar.getTime().after(new Date());
            if(periodEqDay){
                SelectOption selectOption1=new SelectOption();
                selectOption1.setText("Appeal for change of licence period");
                selectOption1.setValue("MS004");
                selectOptionList.add(selectOption1);
            }
            request.getSession().setAttribute("periodEqDay",periodEqDay);
            calendar.setTime(createdAt);
            calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(systemParamConfig.getAppealOthers()));
            boolean otherEqDay = calendar.getTime().after(new Date());
            if(otherEqDay){
                SelectOption selectOption=new SelectOption();
                selectOption.setValue(MS);
                selectOption.setText(OTHERS);
                selectOptionList.add(selectOption);
            }
            request.getSession().setAttribute("otherEqDay",otherEqDay);
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
            request.getSession().setAttribute(SERVICE_NAME, svcName);
            request.getSession().setAttribute("licenceNo", licenceNo);
            request.getSession().setAttribute("appealNo", licenceDto.getLicenceNo());
        } else if (APPLICATION.equals(type)) {
            ApplicationDto applicationDto = applicationFeClient.getApplicationById(appealingFor).getEntity();
            Map<String,String> map=new HashMap<>();
            validateCgoIdNo(applicationDto,map);
            request.getSession().setAttribute("map",map);
            Calendar calendar=Calendar.getInstance();
            Date createAt = applicationDto.getCreateAt();
            calendar.setTime(createAt);
            calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(systemParamConfig.getAdditionalCgo()));
            boolean cgoEqDay = calendar.getTime().after(new Date());
            request.getSession().setAttribute("cgoEqDay",cgoEqDay);
            calendar.setTime(createAt);
            calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(systemParamConfig.getRestrictedName()));
            boolean nameEqDay = calendar.getTime().after(new Date());
            if(nameEqDay){
                SelectOption selectOption1=new SelectOption();
                selectOption1.setValue("MS008");
                selectOption1.setText("Appeal against use of restricted words in HCI Name");
                selectOptionList.add(selectOption1);
            }
            request.getSession().setAttribute("nameEqDay",nameEqDay);
            calendar.setTime(createAt);
            calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(systemParamConfig.getAppealOthers()));
            boolean otherEqDay = calendar.getTime().after(new Date());
            if(otherEqDay){
                SelectOption selectOption1=new SelectOption();
                selectOption1.setValue(MS);
                selectOption1.setText(OTHERS);
                selectOptionList.add(selectOption1);
            }
            request.getSession().setAttribute("otherEqDay",otherEqDay);
            calendar.setTime(createAt);
            calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(systemParamConfig.getRenewalFee()));
            boolean feeEqDay = calendar.getTime().after(new Date());
            request.getSession().setAttribute("feeEqDay",feeEqDay);
            calendar.setTime(createAt);
            calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(systemParamConfig.getAgainstRejection()));
            boolean rejectEqDay = calendar.getTime().after(new Date());
            request.getSession().setAttribute("rejectEqDay",rejectEqDay);
            if(cgoEqDay){
                boolean maxCGOnumber = isMaxCGOnumber(applicationDto);
                if (maxCGOnumber) {
                    SelectOption selectOption1=new SelectOption();
                    selectOption1.setValue("MS003");
                    selectOption1.setText("Appeal for appointment of additional CGO to a service");
                    selectOptionList.add(selectOption1);
                    request.getSession().setAttribute("maxCGOnumber", maxCGOnumber);
                }
            }
            if(feeEqDay){
                AppFeeDetailsDto appFeeDetailsDto =
                        applicationFeClient.getAppFeeDetailsDtoByApplicationNo(applicationDto.getApplicationNo()).getEntity();
                if (appFeeDetailsDto != null) {
                    try {
                        if (appFeeDetailsDto.getLaterFee() > 0.0) {
                            SelectOption selectOption1=new SelectOption();
                            selectOption1.setValue("MS002");
                            selectOption1.setText("Appeal against late renewal fee");
                            selectOptionList.add(selectOption1);
                            request.getSession().setAttribute("lateFee", Boolean.TRUE);
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage() + "------", e);
                    }
                }
            }

            String serviceId = applicationDto.getServiceId();
            String id = applicationDto.getId();
            if (id != null) {
                AppInsRepDto entity = applicationFeClient.getHciNameAndAddress(id).getEntity();
                String hciName = entity.getHciName();
                String hciAddres = entity.getHciAddress();
                List<String> hciNames = IaisCommonUtils.genNewArrayList();
                hciNames.add(hciName);
                List<String> hciAddress = IaisCommonUtils.genNewArrayList();
                hciAddress.add(hciAddres);
                request.getSession().setAttribute("hciAddress", hciAddress);
                request.getSession().setAttribute("hciNames", hciNames);
            }

            List<String> list = IaisCommonUtils.genNewArrayList();
            list.add(serviceId);
            List<HcsaServiceDto> entity = configCommClient.getHcsaService(list).getEntity();
            for (int i = 0; i < entity.size(); i++) {
                String svcName = entity.get(i).getSvcName();
                request.getSession().setAttribute(SERVICE_NAME, svcName);
            }
            String applicationNo = applicationDto.getApplicationNo();
            request.getSession().setAttribute("id", applicationDto.getId());
            request.getSession().setAttribute("applicationNo", applicationNo);
            request.setAttribute("applicationDto", applicationDto);
            String status = applicationDto.getStatus();
            if (ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(status)) {
                if(rejectEqDay){
                    SelectOption selectOption1=new SelectOption();
                    selectOption1.setValue("MS001");
                    selectOption1.setText("Appeal against rejection");
                    selectOptionList.add(selectOption1);
                }
                request.getSession().setAttribute("applicationAPPROVED", "APPROVED");
            }
            request.getSession().setAttribute("appealNo", applicationDto.getApplicationNo());
            request.getSession().setAttribute("serviceId", applicationDto.getServiceId());
        }
        Collections.sort(selectOptionList,(s1,s2)->(s1.getText().compareTo(s2.getText())));
        request.getSession().setAttribute(SELECT_OPTION_LIST,selectOptionList);
    }

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        validae(request, errorMap);
        String type =(String)request.getSession().getAttribute(TYPE);
        String appealingFor = (String) request.getSession().getAttribute(APPEALING_FOR);
        if(LICENCE.equals(type)){
            LicenceDto licenceDto = licenceClient.getLicBylicId(appealingFor).getEntity();
            WebValidationHelper.saveAuditTrailForNoUseResult(licenceDto,errorMap);
        }else if(APPLICATION.equals(type)){
            ApplicationDto applicationDto = applicationFeClient.getApplicationById(appealingFor).getEntity();
            WebValidationHelper.saveAuditTrailForNoUseResult(applicationDto,errorMap);
        }else {
            WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
        }
        return errorMap;
    }

    @Override
    public void inbox(HttpServletRequest request, String appNo) {
        AppPremisesCorrelationDto entity1 = applicationFeClient.getCorrelationByAppNo(appNo).getEntity();
        AppPremiseMiscDto entity2 = applicationFeClient.getAppPremisesMisc(entity1.getId()).getEntity();
        String appealType = entity2.getAppealType();
        requetForInformationGetMessage(request, entity2);
        if("APPEAL001".equals(appealType)){
            request.getSession().setAttribute(TYPE, APPLICATION);
            typeApplicationOrLicence(request,APPLICATION,entity2.getRelateRecId());
            List<SelectOption> selectOptionList=new ArrayList<>(5);
            SelectOption selectOption1=new SelectOption();
            selectOption1.setValue("MS008");
            selectOption1.setText("Appeal against use of restricted words in HCI Name");
            SelectOption selectOption2=new SelectOption();
            selectOption2.setValue(MS);
            selectOption2.setText(OTHERS);
            SelectOption selectOption3=new SelectOption();
            selectOption3.setValue("MS003");
            selectOption3.setText("Appeal for appointment of additional CGO to a service");
            SelectOption selectOption4=new SelectOption();
            selectOption4.setValue("MS002");
            selectOption4.setText("Appeal against late renewal fee");
            SelectOption selectOption5=new SelectOption();
            selectOption5.setValue("MS001");
            selectOption5.setText("Appeal against rejection");
            selectOptionList.add(selectOption5);
            selectOptionList.add(selectOption4);
            selectOptionList.add(selectOption1);
            selectOptionList.add(selectOption2);
            selectOptionList.add(selectOption3);
            request.getSession().setAttribute(SELECT_OPTION_LIST,selectOptionList);
        }else if("APPEAL002".equals(appealType)){
            request.getSession().setAttribute(TYPE, LICENCE);
            typeApplicationOrLicence(request,LICENCE,entity2.getRelateRecId());
            List<SelectOption> selectOptionList=new ArrayList<>(2);
            SelectOption selectOption1=new SelectOption();
            selectOption1.setText("Appeal for change of licence period");
            selectOption1.setValue("MS004");
            SelectOption selectOption=new SelectOption();
            selectOption.setValue(MS);
            selectOption.setText(OTHERS);
            selectOptionList.add(selectOption1);
            selectOptionList.add(selectOption);
            request.getSession().setAttribute(SELECT_OPTION_LIST,selectOptionList);
        }

    }

    @Override
    public ProfessionalResponseDto prsFlag(String regNo) {
        return appCommService.retrievePrsInfo(regNo);
    }

    @Override
    public void print(HttpServletRequest req) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) req.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String saveDraftId = (String) req.getSession().getAttribute(SAVE_DRAFT_NO);
        AppPremisesSpecialDocDto appPremisesSpecialDocDto = (AppPremisesSpecialDocDto) req.getSession().getAttribute(APPPREMISESSPECIALDOCDTO);
        String reasonSelect = request.getParameter(REASON_SELECT);
        String proposedHciName = request.getParameter(PROPOSED_HCI_NAME);
        String remarks = request.getParameter(REMARKS);
        String othersReason = request.getParameter(OTHERS_REASON);
        Map<String, File> map = (Map<String, File>)req.getSession().getAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX_FE_SELECT_FILE);
        Map<String, PageShowFileDto> pageShowFileHashMap = (Map<String, PageShowFileDto>)request.getSession().getAttribute(PAGE_SHOW_FILE_HASH_MAP);
        List<AppPremisesSpecialDocDto> appPremisesSpecialDocDtoList =new ArrayList<>(5);
        List<PageShowFileDto> pageShowFileDtos =new ArrayList<>(5);
        List<File> files=new ArrayList<>(5);
        if(map!=null&&!map.isEmpty()){
            map.forEach((k,v)->{
                if(v!=null){
                    long length = v.length();
                    if(length>0){
                        Long size=length/1024;
                        files.add(v);
                        AppPremisesSpecialDocDto premisesSpecialDocDto=new AppPremisesSpecialDocDto();
                        premisesSpecialDocDto.setDocName(v.getName());
                        String fileMd5 = FileUtils.getFileMd5(v);
                        premisesSpecialDocDto.setMd5Code(fileMd5);
                        premisesSpecialDocDto.setDocSize(Integer.valueOf(size.toString()));
                        PageShowFileDto pageShowFileDto =new PageShowFileDto();
                        pageShowFileDto.setFileName(v.getName());
                        String e = k.substring(k.lastIndexOf('e') + 1);
                        pageShowFileDto.setIndex(e);
                        pageShowFileDto.setFileMapId(SELECTED_FILE_DIV+e);
                        pageShowFileDto.setSize(Integer.valueOf(size.toString()));
                        pageShowFileDto.setMd5Code(fileMd5);
                        premisesSpecialDocDto.setIndex(k);
                        appPremisesSpecialDocDtoList.add(premisesSpecialDocDto);
                        pageShowFileDtos.add(pageShowFileDto);
                    }
                }else {
                    if(pageShowFileHashMap!=null){
                        PageShowFileDto pageShowFileDto = pageShowFileHashMap.get(k);
                        AppPremisesSpecialDocDto premisesSpecialDocDto=new AppPremisesSpecialDocDto();
                        premisesSpecialDocDto.setDocName(pageShowFileDto.getFileName());
                        premisesSpecialDocDto.setFileRepoId(pageShowFileDto.getFileUploadUrl());
                        premisesSpecialDocDto.setDocSize(pageShowFileDto.getSize());
                        premisesSpecialDocDto.setMd5Code(pageShowFileDto.getMd5Code());
                        premisesSpecialDocDto.setIndex(k.substring(k.lastIndexOf('e') + 1));
                        appPremisesSpecialDocDtoList.add(premisesSpecialDocDto);
                        pageShowFileDtos.add(pageShowFileDto);
                    }
                }

            });
        }
        List<String> list = comFileRepoClient.saveFileRepo(files);
        if(list!=null){
            ListIterator<String> iterator = list.listIterator();
            for(int j=0;j< appPremisesSpecialDocDtoList.size();j++){
                String fileRepoId = appPremisesSpecialDocDtoList.get(j).getFileRepoId();
                if(fileRepoId==null && iterator.hasNext()){
                    String next = iterator.next();
                    pageShowFileDtos.get(j).setFileUploadUrl(next);
                    appPremisesSpecialDocDtoList.get(j).setFileRepoId(next);
                    iterator.remove();
                }
            }
        }
        Collections.sort(pageShowFileDtos,(s1,s2)->s1.getFileMapId().compareTo(s2.getFileMapId()));
        req.getSession().setAttribute(PAGE_SHOW_FILES, pageShowFileDtos);
        CommonsMultipartFile selectedFile = (CommonsMultipartFile) request.getFile(SELECTED_FILE);
        if (selectedFile != null && selectedFile.getSize() > 0) {
            String filename = selectedFile.getOriginalFilename();
            req.setAttribute(FILENAME, filename);
            Long size = selectedFile.getSize() / 1024;
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
                    req.getSession().setAttribute(APPPREMISESSPECIALDOCDTO, appPremisesSpecialDocDto);
                    req.getSession().setAttribute(FILE_REPORT_ID_FOR_APPEAL,fileToRepo);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = reAppSvcCgo(request);
        ParamUtil.setSessionAttr(req, CGO_MANDATORY_COUNT, appSvcCgoDtoList.size());
        ParamUtil.setSessionAttr(req, GOVERNANCE_OFFICERS_LIST, (Serializable) appSvcCgoDtoList);

        AppPremiseMiscDto appPremiseMiscDto = new AppPremiseMiscDto();
        if (!StringUtil.isEmpty(saveDraftId)) {
            appPremiseMiscDto.setRemarks(remarks);
            appPremiseMiscDto.setReason(reasonSelect);
            if (ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME.equals(reasonSelect)) {
                appPremiseMiscDto.setNewHciName(proposedHciName);
            }
            appPremiseMiscDto.setOtherReason(othersReason);
        }
        appPremiseMiscDto.setRemarks(remarks);
        appPremiseMiscDto.setOtherReason(othersReason);
        appPremiseMiscDto.setReason(reasonSelect);
        appPremiseMiscDto.setNewHciName(proposedHciName);
        req.getSession().setAttribute(APPPREMISEMISCDTO, appPremiseMiscDto);
    }


    public List<AppSvcPrincipalOfficersDto> reAppSvcCgo(HttpServletRequest request) {
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = IaisCommonUtils.genNewArrayList();
        AppSvcPrincipalOfficersDto appSvcCgoDto ;
        String[] assignSelect = ParamUtil.getStrings(request, "assignSelect");
        int size = 0;
        if (assignSelect != null && assignSelect.length > 0) {
            size = assignSelect.length;
        }
        log.info(StringUtil.changeForLog("assignSelect size :"+size));
        String[] salutation = ParamUtil.getStrings(request, "salutation");
        String[] name = ParamUtil.getStrings(request, "name");
        String[] idType = ParamUtil.getStrings(request, "idType");
        String[] idNo = ParamUtil.getStrings(request, "idNo");
        String[] nationality = ParamUtil.getStrings(request, "nationality");
        String[] designation = ParamUtil.getStrings(request, "designation");
        String[] professionType = ParamUtil.getStrings(request, "professionType");
        String[] professionRegoNo = ParamUtil.getStrings(request, PROFESSION_REGO_NO);
        String[] otherDesignations = ParamUtil.getStrings(request, OTHER_DESIGNATION);
        String[] otherQualifications = ParamUtil.getStrings(request, OTHER_QUALIFICATION);
        String[] mobileNo = ParamUtil.getStrings(request, MOBILE_NO);
        String[] emailAddress = ParamUtil.getStrings(request, "emailAddress");
        String[] professionBoard = ParamUtil.getStrings(request, "professionBoard");
        String[] typeOfCurrRegi = ParamUtil.getStrings(request, "typeOfCurrRegi");
        String[] praCerEndDate = ParamUtil.getStrings(request, "praCerEndDate");
        String[] currRegiDate = ParamUtil.getStrings(request, "currRegiDate");
        String[] typeOfRegister = ParamUtil.getStrings(request, "typeOfRegister");
        String[] specialityOther = ParamUtil.getStrings(request, "specialityOther");
        String[] specialtyGetDate = ParamUtil.getStrings(request, "specialtyGetDate");
        for (int i = 0; i < size; i++) {
            appSvcCgoDto = new AppSvcPrincipalOfficersDto();
            //indexNo
            String indexNo = new StringBuffer().append("cgo-").append(i).append("-").toString();
            appSvcCgoDto.setAssignSelect(assignSelect[i]);
            appSvcCgoDto.setSalutation(salutation[i]);
            appSvcCgoDto.setName(name[i]);
            appSvcCgoDto.setIdType(idType[i]);
            appSvcCgoDto.setIdNo(StringUtil.toUpperCase(idNo[i]));
            if("IDTYPE003".equals(appSvcCgoDto.getIdType())){
                appSvcCgoDto.setNationality(nationality[i]);
            }
            appSvcCgoDto.setDesignation(designation[i]);
            appSvcCgoDto.setProfessionType(professionType[i]);
            appSvcCgoDto.setProfRegNo(professionRegoNo[i]);
            appSvcCgoDto.setOtherDesignation(otherDesignations[i]);
            appSvcCgoDto.setMobileNo(mobileNo[i]);
            appSvcCgoDto.setEmailAddr(emailAddress[i]);
            appSvcCgoDto.setIndexNo(indexNo);
            appSvcCgoDto.setOtherQualification(otherQualifications[i]);
            appSvcCgoDto.setProfessionBoard(professionBoard[i]);
            appSvcCgoDto.setTypeOfCurrRegi(typeOfCurrRegi[i]);
            appSvcCgoDto.setPraCerEndDateStr(praCerEndDate[i]);
            if (StringUtil.isEmpty(praCerEndDate[i])) {
                appSvcCgoDto.setPraCerEndDate(null);
            } else {
                Date date = DateUtil.parseDate(praCerEndDate[i], Formatter.DATE);
                appSvcCgoDto.setPraCerEndDate(date);
            }
            appSvcCgoDto.setCurrRegiDateStr(currRegiDate[i]);
            if (StringUtil.isEmpty(currRegiDate[i])) {
                appSvcCgoDto.setCurrRegiDate(null);
            } else {
                Date date = DateUtil.parseDate(currRegiDate[i], Formatter.DATE);
                appSvcCgoDto.setCurrRegiDate(date);
            }
            appSvcCgoDto.setTypeOfRegister(typeOfRegister[i]);
            appSvcCgoDto.setSpecialityOther(specialityOther[i]);
            appSvcCgoDto.setSpecialtyGetDateStr(specialtyGetDate[i]);
            if (StringUtil.isEmpty(specialtyGetDate[i])) {
                appSvcCgoDto.setSpecialtyGetDate(null);
            } else {
                Date date = DateUtil.parseDate(specialtyGetDate[i], Formatter.DATE);
                appSvcCgoDto.setSpecialtyGetDate(date);
            }
            ProfessionalResponseDto professionalResponseDto = prsFlag(professionRegoNo[i]);
            if(professionalResponseDto != null){
                List<String> qualification = professionalResponseDto.getQualification();
                if(qualification!=null&&!qualification.isEmpty()){
                    appSvcCgoDto.setQualification(qualification.get(0));
                }
                List<String> specialty = professionalResponseDto.getSpecialty();
                if(specialty!=null&&!specialty.isEmpty()){
                  appSvcCgoDto.setSpeciality(specialty.get(0));
                }
                List<String> subspecialty = professionalResponseDto.getSubspecialty();
                if(subspecialty!=null&&!subspecialty.isEmpty()){
                    appSvcCgoDto.setSubSpeciality(subspecialty.get(0));
                }

            }
            appSvcCgoDtoList.add(appSvcCgoDto);
        }
        return appSvcCgoDtoList;
    }


    public void validae(HttpServletRequest req, Map<String, String> map) {
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) req.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String isDelete = request.getParameter("isDelete");
        AppPremisesSpecialDocDto appPremisesSpecialDocDto = (AppPremisesSpecialDocDto) req.getSession().getAttribute(APPPREMISESSPECIALDOCDTO);
        CommonsMultipartFile file = (CommonsMultipartFile) request.getFile(SELECTED_FILE);
        Map<String, File> fileMap = (Map<String, File>)req.getSession().getAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX_FE_SELECT_FILE);
        Map<String, PageShowFileDto> pageShowFileHashMap = (Map<String, PageShowFileDto>)request.getSession().getAttribute(PAGE_SHOW_FILE_HASH_MAP);
        List<PageShowFileDto> pageShowFileDtos =new ArrayList<>(5);
        if(fileMap!=null&&!fileMap.isEmpty()){
            fileMap.forEach((k,v)->{
                if(v!=null){
                    PageShowFileDto pageShowFileDto =new PageShowFileDto();
                    pageShowFileDto.setFileName(v.getName());
                    String e = k.substring(k.lastIndexOf('e') + 1);
                    pageShowFileDto.setIndex(e);
                    pageShowFileDto.setFileMapId(SELECTED_FILE_DIV+e);
                    Long l = v.length() / 1024;
                    pageShowFileDto.setSize(Integer.valueOf(l.toString()));
                    pageShowFileDtos.add(pageShowFileDto);
                }else {
                    if(pageShowFileHashMap!=null){
                        pageShowFileDtos.add(pageShowFileHashMap.get(k));
                    }
                }
            });
        }
        Collections.sort(pageShowFileDtos,(s1,s2)->s1.getFileMapId().compareTo(s2.getFileMapId()));
        for(int i=0;i<pageShowFileDtos.size();i++){
            validateFile(pageShowFileDtos.get(i),map,i);
        }
        req.getSession().setAttribute(PAGE_SHOW_FILES, pageShowFileDtos);
        String errLen= IaisEGPConstant.ERR_FILENAME_CHARACTERS;
        if (file != null && file.getSize() > 0) {
            int configFileSize = systemParamConfig.getUploadFileLimit();
            String configFileType = FileUtils.getStringFromSystemConfigString(systemParamConfig.getUploadFileType());
            List<String> fileTypes = Arrays.asList(configFileType.split(","));
            Map<String, Boolean> booleanMap = ValidationUtils.validateFile(file,fileTypes,(configFileSize * 1024 *1024L));
            Boolean fileSize = booleanMap.get("fileSize");
            Boolean fileType = booleanMap.get(FILE_TYPE);
            Boolean fileNameLength = booleanMap.get("fileNameLength");
            //size
            if(!fileSize){
                map.put("file", MessageUtil.replaceMessage(IaisEGPConstant.ERR_FILE_UPLOAD_MAX, String.valueOf(configFileSize),SIZE_MAX));
            }
            //type
            if(!fileType){
                map.put("file",MessageUtil.replaceMessage(IaisEGPConstant.ERR_RE_FILE_UPLOAD, configFileType,FILE_TYPE));
            }
            if(!fileNameLength){
                map.put("file",errLen);
            }
            AppPremisesSpecialDocDto specialDocDto=new AppPremisesSpecialDocDto();
            long size = file.getSize() / 1024;
            String filename = file.getOriginalFilename();
            specialDocDto.setDocName(filename);
            specialDocDto.setDocSize(Integer.valueOf(size+""));
            req.getSession().setAttribute(APPPREMISESSPECIALDOCDTO, specialDocDto);

        } else if (appPremisesSpecialDocDto != null && appPremisesSpecialDocDto.getDocSize() > 0 && Y.equals(isDelete)) {
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
            if (!Boolean.TRUE.equals(flag)) {
                map.put("file",MessageUtil.replaceMessage(IaisEGPConstant.ERR_RE_FILE_UPLOAD, configFileType,FILE_TYPE));
            }
            if(filename.length()>100){
                map.put("file",errLen);
            }
        }


        AppealPageDto appealPageDto = reAppealPage(request);
        String remarks = appealPageDto.getRemarks();
        if (StringUtil.isEmpty(remarks)) {
            map.put(REMARKS, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY, "Any supporting remarks", FIELD));
        } else if (remarks.length() > 300) {
            Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
            repMap.put(MAXLENGTH, "300");
            repMap.put(FIELD, "Any supporting remarks");
            map.put(REMARKS, MessageUtil.getMessageDesc(IaisEGPConstant.ERR_ENTERS_VALUE_MAXLENGTH, repMap));
        }
        String appealReason = appealPageDto.getAppealReason();

        if (StringUtil.isEmpty(appealReason)) {
            map.put("reason", MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Reason For Appeal",FIELD));
        } else {
            if (ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(appealReason)) {
                List<AppSvcPrincipalOfficersDto> appSvcCgoList = appealPageDto.getAppSvcCgoDto();
                if(IaisCommonUtils.isEmpty(appSvcCgoList)){
                    //todo
                    map.put("addCgo",  MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Add Another Clinical Governance Officer ",FIELD));
                }
                String designationMsg = MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY, "Designation", FIELD);
                for (int i = 0; i < appSvcCgoList.size(); i++) {
                    StringBuilder stringBuilder1 = new StringBuilder();
                    String assignSelect = appSvcCgoList.get(i).getAssignSelect();
                    if ("-1".equals(assignSelect)) {
                        map.put("assignSelect" + i,  MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Add/Assign a Clinical Governance Officer",FIELD));
                    } else {
                        String idTyp = appSvcCgoList.get(i).getIdType();
                        String nationality  = appSvcCgoList.get(i).getNationality();

                        if ("-1".equals(idTyp) || StringUtil.isEmpty(idTyp)) {
                            map.put("idTyp" + i, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"ID No.",FIELD));
                        }else if("IDTYPE003".equals(idTyp)){
                            if ("-1".equals(nationality) || StringUtil.isEmpty(nationality)) {
                                map.put("nationality" + i, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Nationality",FIELD));
                            }

                        }
                        String salutation = appSvcCgoList.get(i).getSalutation();
                        if (StringUtil.isEmpty(salutation)) {
                            map.put("salutation" + i, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"ID No. Type",FIELD));
                        }
                        String specialityOther =appSvcCgoList.get(i).getSpecialityOther();
                        if(StringUtil.isNotEmpty(specialityOther)){
                            Date specialtyGetDate =appSvcCgoList.get(i).getSpecialtyGetDate();
                            if(specialtyGetDate==null){
                                map.put("specialtyGetDate" + i, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Date when specialty was obtained",FIELD));
                            }
                        }

                        /*String professionType = appSvcCgoList.get(i).getProfessionType();
                        if (StringUtil.isEmpty(professionType)) {
                            map.put("professionType" + i, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Professional Type ",FIELD));
                        }
                        String typeOfCurrRegi = appSvcCgoList.get(i).getTypeOfCurrRegi();
                        if (StringUtil.isEmpty(typeOfCurrRegi)) {
                            map.put("typeOfCurrRegi" + i, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Type of Current Registration ",FIELD));
                        }
                        String currRegiDate = appSvcCgoList.get(i).getCurrRegiDateStr();
                        if (StringUtil.isEmpty(currRegiDate)) {
                            map.put("currRegiDate" + i, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Current Registration Date",FIELD));
                        }
                        String praCerEndDate = appSvcCgoList.get(i).getPraCerEndDateStr();
                        if (StringUtil.isEmpty(praCerEndDate)) {
                            map.put("praCerEndDate" + i, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Practicing Certificate End Date",FIELD));
                        }
                        String typeOfRegister = appSvcCgoList.get(i).getTypeOfRegister();
                        if (StringUtil.isEmpty(typeOfRegister)) {
                            map.put("typeOfRegister" + i, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Type of Register",FIELD));
                        }

                        String specialtyGetDate = appSvcCgoList.get(i).getSpecialtyGetDateStr();
                        if (StringUtil.isEmpty(specialtyGetDate)) {
                            map.put("specialtyGetDate" + i, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Date when specialty was obtained",FIELD));
                        }*/
                        String designation = appSvcCgoList.get(i).getDesignation();
                        if (StringUtil.isEmpty(designation)) {
                            map.put("designation" + i, designationMsg);
                        }else if("DES999".equals(designation)){
                            String otherDesignation = appSvcCgoList.get(i).getOtherDesignation();
                            if (StringUtil.isEmpty(otherDesignation)) {
                                map.put(OTHER_DESIGNATION + i, designationMsg);
                            } else if (otherDesignation.length() > 100) {
                                Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                                repMap.put(MAXLENGTH, "100");
                                repMap.put(FIELD, "Other Designation");
                                map.put(OTHER_DESIGNATION + i, MessageUtil.getMessageDesc(IaisEGPConstant.ERR_ENTERS_VALUE_MAXLENGTH, repMap));
                            }
                        }
                        String professionRegoNo = appSvcCgoList.get(i).getProfRegNo();
                        String otherQualification = appSvcCgoList.get(i).getOtherQualification();

                        if(StringUtil.isNotEmpty(otherQualification)&&otherQualification.length()>100){
                            Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                            repMap.put(MAXLENGTH,"100");
                            repMap.put(FIELD,"Other Qualification");
                            map.put(OTHER_QUALIFICATION+i,MessageUtil.getMessageDesc(IaisEGPConstant.ERR_ENTERS_VALUE_MAXLENGTH,repMap));

                        }
                        if (!StringUtil.isEmpty(professionRegoNo))  {
                            if (professionRegoNo.length() > 20) {
                                Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                                repMap.put(MAXLENGTH, "20");
                                repMap.put(FIELD, "Professional Regn. No.");
                                map.put(PROFESSION_REGO_NO + i, MessageUtil.getMessageDesc(IaisEGPConstant.ERR_ENTERS_VALUE_MAXLENGTH, repMap));
                            }
                            ProfessionalResponseDto professionalResponseDto = prsFlag(professionRegoNo);
                            if (professionalResponseDto != null) {
                                if (professionalResponseDto.isHasException()) {
                                    map.put(PROFESSION_REGO_NO + i, "GENERAL_ERR0048");
                                } else if ("401".equals(professionalResponseDto.getStatusCode())) {
                                    map.put(PROFESSION_REGO_NO + i, "GENERAL_ERR0054");
                                } else {
                                    List<String> specialty = professionalResponseDto.getSpecialty();
                                    if (IaisCommonUtils.isEmpty(specialty)) {
                                        map.put(PROFESSION_REGO_NO + i, "GENERAL_ERR0042");
                                    }
                                }
                            }
                        }

                        String idNo = appSvcCgoList.get(i).getIdNo();
                        //to do
                        if (StringUtil.isEmpty(idNo)) {
                            map.put("idNo" + i, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"ID No.  ",FIELD));
                        } else {
                            if (OrganizationConstants.ID_TYPE_FIN.equals(idTyp)) {
                                boolean b = SgNoValidator.validateFin(idNo);
                                if (!b) {
                                    map.put("idNo" + i, "RFC_ERR0012");
                                }
                            } else if (OrganizationConstants.ID_TYPE_NRIC.equals(idTyp)) {
                                boolean b1 = SgNoValidator.validateNric(idNo);
                                if (!b1) {
                                    map.put("idNo" + i, "RFC_ERR0012");
                                }
                            }

                            if (idNo.length() > 20) {
                                Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                                repMap.put(MAXLENGTH, "20");
                                repMap.put(FIELD, "ID No.");
                                map.put("idNo" + i, MessageUtil.getMessageDesc(IaisEGPConstant.ERR_ENTERS_VALUE_MAXLENGTH, repMap));
                            }
                            String personKey = ApplicationHelper.getPersonKey(nationality, idTyp, idNo);
                            Map<String,String> map1 =(Map<String, String>) req.getSession().getAttribute("map");
                            if (!StringUtil.isEmpty(personKey) && !StringUtil.isEmpty(
                                    idNo) ) {
                                String person = map1.get(personKey);
                                if (person != null) {
                                    map.put("idNo", MessageUtil.replaceMessage("NEW_ERR0006", idNo, "ID No."));
                                }
                            }

                        }
                        //to do



                        String name = appSvcCgoList.get(i).getName();
                        if (StringUtil.isEmpty(name)) {
                            map.put("name" + i, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY, "Name", FIELD));
                        } else if (name.length() > 100) {
                            Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                            repMap.put(MAXLENGTH, "100");
                            repMap.put(FIELD, "Name");
                            map.put("name" + i, MessageUtil.getMessageDesc(IaisEGPConstant.ERR_ENTERS_VALUE_MAXLENGTH, repMap));
                        }

                        String mobileNo = appSvcCgoList.get(i).getMobileNo();
                        if (StringUtil.isEmpty(mobileNo)) {
                            map.put(MOBILE_NO + i, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Mobile No. ",FIELD));
                        } else if (!StringUtil.isEmpty(mobileNo)) {
                            if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                                map.put(MOBILE_NO + i, "GENERAL_ERR0007");
                            }
                        }
                        String emailAddr = appSvcCgoList.get(i).getEmailAddr();
                        if (StringUtil.isEmpty(emailAddr)) {
                            map.put("emailAddr" + i, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Email Address ",FIELD));
                        } else if (!StringUtil.isEmpty(emailAddr)) {
                            if (!ValidationUtils.isEmail(emailAddr)) {
                                map.put("emailAddr" + i, "GENERAL_ERR0014");
                            } else if (emailAddr.length() > 320) {
                                Map<String, String> repMap = IaisCommonUtils.genNewHashMap();
                                repMap.put(MAXLENGTH, "320");
                                repMap.put(FIELD, "Email Address");
                                map.put("emailAddr" + i, MessageUtil.getMessageDesc(IaisEGPConstant.ERR_ENTERS_VALUE_MAXLENGTH, repMap));
                            }
                        }
                    }

                }
                ParamUtil.setSessionAttr(request,GOVERNANCE_OFFICERS_LIST, (Serializable) appSvcCgoList);
            } else if (ApplicationConsts.APPEAL_REASON_OTHER.equals(appealReason)) {
                String otherReason = request.getParameter(OTHERS_REASON);
                if (StringUtil.isEmpty(otherReason)) {
                    map.put("otherReason", MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY,"Others reason",FIELD));
                }
            }else if(ApplicationConsts.APPEAL_REASON_APPLICATION_CHANGE_HCI_NAME.equals(appealReason)){
                String proposedHciName = request.getParameter(PROPOSED_HCI_NAME);
                if(StringUtil.isEmpty(proposedHciName)){
                    map.put(PROPOSED_HCI_NAME, MessageUtil.replaceMessage(IaisEGPConstant.ERR_MANDATORY, "Proposed HCI Name",FIELD));
                }
            }
        }


    }

    private AppealPageDto reAppealPage(HttpServletRequest request) {
        AppealPageDto appealPageDto = new AppealPageDto();

        String appealingFor = (String) request.getSession().getAttribute(APPEALING_FOR);

        String type = (String) request.getSession().getAttribute(TYPE);
        List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = reAppSvcCgo(request);
        String reasonSelect = request.getParameter(REASON_SELECT);
        String proposedHciName = request.getParameter(PROPOSED_HCI_NAME);
        String remarks = request.getParameter(REMARKS);
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
        LicenceDto licenceDto = licenceClient.getLicDtoById(licenceId).getEntity();
        ApplicationDto entity1 = (ApplicationDto) request.getSession().getAttribute(IaisEGPConstant.RFI_APPLICATION);
        String licenseeId = licenceDto.getLicenseeId();
        String rfi = (String) request.getSession().getAttribute("rfi");
        List<ApplicationDto> applicationDtoListlist = IaisCommonUtils.genNewArrayList();
        AuditTrailHelper.auditFunctionWithLicNo(AuditTrailConsts.MODULE_APPEAL,AuditTrailConsts.FUNCTION_APPEAL,licenceDto.getLicenceNo());
        List<PremisesDto> premisess = licenceClient.getPremisesDto(licenceDto.getId()).getEntity();
        String appNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_APPEAL).getEntity();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDto(appNo);
        applicationGroupDto.setLicenseeId(licenseeId);
        applicationGroupDto.setSubmitBy(loginContext.getUserId());
        StringBuilder stringBuilder = new StringBuilder(appNo);
        String s = stringBuilder.append("-01").toString();
        List<AppGrpPremisesDto> premisesDtos = new ArrayList<>(1);
        if(!StringUtil.isEmpty(premisess)){
            AppGrpPremisesDto appGrpPremisesDto = MiscUtil.transferEntityDto(premisess.get(0), AppGrpPremisesDto.class);
            if(IaisCommonUtils.isNotEmpty(premisess.get(0).getPremisesOperationalUnitDtos())){
                appGrpPremisesDto.setAppPremisesOperationalUnitDtos(MiscUtil.transferEntityDtos(premisess.get(0).getPremisesOperationalUnitDtos(), AppPremisesOperationalUnitDto.class));
            }
            premisesDtos.add(appGrpPremisesDto);
        }
        //appealPageDto
        AppealPageDto appealDto = getAppealPageDto(request);
        List<AppSvcPrincipalOfficersDto> list = IaisCommonUtils.genNewArrayList();
        for (AppGrpPremisesDto every : premisesDtos) {
            AppSvcPrincipalOfficersDto appSvcCgoDto = MiscUtil.transferEntityDto(every, AppSvcPrincipalOfficersDto.class);
            list.add(appSvcCgoDto);
            ApplicationDto applicationDto = new ApplicationDto();
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
            applicationDto.setApplicationType(ApplicationConsts.APPLICATION_TYPE_APPEAL);
            applicationDto.setVersion(1);
            if ("rfi".equals(rfi)) {
                AppPremisesSpecialDocDto appPremisesSpecialDocDto = appealDto.getAppPremisesSpecialDocDto();
                String grpId = entity1.getAppGrpId();
                if(appPremisesSpecialDocDto!=null){
                    List<AppliSpecialDocDto> appliSpecialDocDtos = applicationFeClient.getAppliSpecialDocDtoByGroupId(grpId).getEntity();
                    if(!appliSpecialDocDtos.isEmpty()){
                        AppliSpecialDocDto appliSpecialDocDto1 = appliSpecialDocDtos.get(appliSpecialDocDtos.size() - 1);
                        if(!appliSpecialDocDto1.getMd5Code().equals(appPremisesSpecialDocDto.getMd5Code())){
                            appPremisesSpecialDocDto.setVersion(appliSpecialDocDto1.getVersion()+1);
                        }else {
                            appPremisesSpecialDocDto.setVersion(appliSpecialDocDto1.getVersion());
                        }
                    }
                }
                List<AppPremisesSpecialDocDto> appPremisesSpecialDocDtos = appealDto.getAppPremisesSpecialDocDtos();
                if(appPremisesSpecialDocDtos!=null){
                    Map<String, PageShowFileDto> pageShowFileHashMap = (Map<String, PageShowFileDto>)request.getSession().getAttribute(PAGE_SHOW_FILE_HASH_MAP);
                    for(AppPremisesSpecialDocDto v : appPremisesSpecialDocDtos){
                        PageShowFileDto pageShowFileDto = pageShowFileHashMap.get(SELECTED_FILE+v.getIndex());
                        if(pageShowFileDto!=null){
                            boolean equals = v.getMd5Code().equals(pageShowFileDto.getMd5Code());
                            if(equals){
                                v.setFileRepoId(pageShowFileDto.getFileUploadUrl());
                            }else {
                                v.setVersion(pageShowFileDto.getVersion()+1);
                            }
                        }
                    }
                }
                applicationDto.setVersion(entity1.getVersion() + 1);
                //if not need new group
                applicationGroupDto.setId(entity1.getAppGrpId());
                applicationGroupDto.setGroupNo(entity1.getApplicationNo().substring(0, entity1.getApplicationNo().lastIndexOf('-')));
                applicationDto.setApplicationNo(entity1.getApplicationNo());
                applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
                Map<String, Object> params = IaisCommonUtils.genNewHashMap(1);
                appNo=entity1.getApplicationNo();
                params.put("appNo", appNo);
                List<AppPremisesRoutingHistoryDto> hisList = feEicGatewayClient.getRoutingHistoryDtos(params).getEntity();
                if (hisList != null) {
                    String status = null;
                    for (AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : hisList) {
                        if (!ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(
                                appPremisesRoutingHistoryDto.getProcessDecision())
                                && !InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(
                                appPremisesRoutingHistoryDto.getProcessDecision())) {
                            continue;
                        }
                        switch (appPremisesRoutingHistoryDto.getAppStatus()){
                            case ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING:
                            case ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_ASO:status = ApplicationConsts.PENDING_ASO_REPLY;break;
                            case ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING:
                            case ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_PSO:status = ApplicationConsts.PENDING_PSO_REPLY;break;
                            case ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS:
                            case ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_INSPECTOR:status = ApplicationConsts.PENDING_INP_REPLY;break;
                            case ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01:
                            case ApplicationConsts.APPLICATION_STATUS_AO_ROUTE_BACK_AO:status = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01;break;
                            default:
                        }
                    }
                    if(status!=null){
                        applicationDto.setStatus(status);
                    }
                }

                s = entity1.getApplicationNo();
                entity1.setStatus(ApplicationConsts.APPLICATION_STATUS_DELETED);
                request.getSession().setAttribute(IaisEGPConstant.RFI_APPLICATION, entity1);
                applicationFeClient.updateApplication(entity1);
            }
            HcsaServiceDto hcsaServiceDto = configCommClient.getActiveHcsaServiceDtoByName(licenceDto.getSvcName()).getEntity();
            applicationDto.setServiceId(hcsaServiceDto.getId());
            applicationDto.setApplicationNo(s);
            applicationDto.setOriginLicenceId(licenceDto.getId());
            String baseServiceId = requestForChangeService.baseSpecLicenceRelation(licenceDto, false);
            if(!StringUtil.isEmpty(baseServiceId)){
                applicationDto.setBaseServiceId(baseServiceId);
            }
            applicationDtoListlist.add(applicationDto);
        }
        String reasonSelect = appealDto.getAppealReason();

        List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = null;
        if (ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(reasonSelect)) {
            appSvcCgoDtos = (List<AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(request,GOVERNANCE_OFFICERS_LIST);
            if(appSvcCgoDtos==null){
                appSvcCgoDtos = reAppSvcCgo(request);
            }
        }

        SubLicenseeDto subLicenseeDto=licenceClient.getSubLicenseesById(licenceDto.getSubLicenseeId()).getEntity();
        appealDto.setApplicationGroupDto(applicationGroupDto);
        appealDto.setAppId(licenceDto.getId());
        appealDto.setSubLicenseeDto(subLicenseeDto);
        appealDto.setApplicationDto(applicationDtoListlist);
        appealDto.setAppealType(ApplicationConsts.APPEAL_TYPE_LICENCE);


        if (appSvcCgoDtos != null && !appSvcCgoDtos.isEmpty()) {
            log.info(StringUtil.changeForLog("appSvcCgoDtos json: " + JsonUtil.parseToJson(appSvcCgoDtos)));
            appealDto.setAppSvcCgoDto(appSvcCgoDtos);

        }else {
            log.info(StringUtil.changeForLog("appSvcCgoDtos isEmpty " ));
        }
        appealDto.setAppGrpPremisesDtos(premisesDtos);
        AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        appealDto.setAuditTrailDto(currentAuditTrailDto);
        appealDto = applicationFeClient.submitAppeal(appealDto).getEntity();
        ApplicationGroupDto applicationGroupDto1 = appealDto.getApplicationGroupDto();
        String groupId = applicationGroupDto1.getId();
        request.setAttribute(GROUP_ID, groupId);
        request.setAttribute(DRAFT_STATUS, AppConsts.COMMON_STATUS_IACTIVE);
        saveData(request);
        request.setAttribute("newApplicationNo", appNo);
        if (!"rfi".equals(rfi)) {
            try {
                String svcName = licenceDto.getSvcName();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
                LicenseeDto licenseeDto = organizationLienceseeClient.getLicenseeById(licenseeId).getEntity();
                sendAllNotification(appNo, "Appeal", licenceDto, licenseeDto, hcsaServiceDto,applicationGroupDto1);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return applicationGroupDto.getGroupNo();
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
        applicationGroupDto.setIsBundledFee(0);
        applicationGroupDto.setIsCharitable(0);
        applicationGroupDto.setIsByGiro(0);
        //applicationGroupDto.setGrpLic(false);
        applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        applicationGroupDto.setDeclStmt(N);
        applicationGroupDto.setAppType(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        return applicationGroupDto;

    }

    private String applicationPresmies(HttpServletRequest request, String applicationId) {
        int maxFileIndex = 0;
        if (ParamUtil.getSessionAttr(request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR) != null) {
            maxFileIndex = (int) ParamUtil.getSessionAttr(request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR);
        }
        ApplicationDto applicationDto = applicationFeClient.getApplicationById(applicationId).getEntity();
        String grpId = applicationDto.getAppGrpId();
        AuditTrailHelper.auditFunctionWithAppNo(AuditTrailConsts.MODULE_APPEAL,AuditTrailConsts.FUNCTION_APPEAL,applicationDto.getApplicationNo());
        String rfi = (String) request.getSession().getAttribute("rfi");
        ApplicationGroupDto entity = applicationFeClient.getApplicationGroup(grpId).getEntity();
        AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByAppGrpNo(entity.getGroupNo());

        String appNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_APPEAL).getEntity();
        StringBuilder stringBuilder = new StringBuilder(appNo);
        String s = stringBuilder.append("-01").toString();
        //appealPageDto
        AppealPageDto appealDto = getAppealPageDto(request);
        String reasonSelect = appealDto.getAppealReason();


        List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = null;
        if (ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(reasonSelect)) {
            appSvcCgoDtos = (List<AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(request,GOVERNANCE_OFFICERS_LIST);
            if(appSvcCgoDtos==null){
                appSvcCgoDtos = reAppSvcCgo(request);
            }
        }

        AppliSpecialDocDto appliSpecialDocDto = new AppliSpecialDocDto();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        appliSpecialDocDto.setSubmitBy(loginContext.getUserId());

        //group
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDto(appNo);
        //info
        applicationGroupDto.setLicenseeId(entity.getLicenseeId());
        applicationGroupDto.setSubmitBy(loginContext.getUserId());
        ApplicationDto applicationDto1 = new ApplicationDto();
        applicationDto1.setApplicationType(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        applicationDto1.setApplicationNo(s);
        //info

        applicationDto1.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
        HcsaServiceDto hcsaServiceDto = configCommClient.getActiveHcsaServiceDtoById(applicationDto.getServiceId()).getEntity();
        applicationDto1.setServiceId(hcsaServiceDto.getId());
        applicationDto1.setBaseServiceId(applicationDto.getBaseServiceId());
        applicationDto1.setVersion(1);
        List<ApplicationDto> list = IaisCommonUtils.genNewArrayList();
        list.add(applicationDto1);
        appealDto.setApplicationGroupDto(applicationGroupDto);
        appealDto.setSubLicenseeDto(appSubmissionDto.getSubLicenseeDto());
        appealDto.setAppId(applicationDto.getId());
        appealDto.setApplicationDto(list);
        appealDto.setAppealType(ApplicationConsts.APPEAL_TYPE_APPLICAITON);
        //if infomation
        if ("rfi".equals(rfi)) {
            AppPremisesSpecialDocDto appPremisesSpecialDocDto = appealDto.getAppPremisesSpecialDocDto();
            ApplicationDto rfiApplication = (ApplicationDto) ParamUtil.getSessionAttr(request,IaisEGPConstant.RFI_APPLICATION);
            if(appPremisesSpecialDocDto!=null){
                List<AppliSpecialDocDto> appliSpecialDocDtos = applicationFeClient.getAppliSpecialDocDtoByGroupId(rfiApplication.getAppGrpId()).getEntity();
                if(!appliSpecialDocDtos.isEmpty()){
                    AppliSpecialDocDto appliSpecialDocDto1 = appliSpecialDocDtos.get(appliSpecialDocDtos.size() - 1);
                    if(!appliSpecialDocDto1.getMd5Code().equals(appPremisesSpecialDocDto.getMd5Code())){
                        appPremisesSpecialDocDto.setVersion(appliSpecialDocDto1.getVersion()+1);
                    }else {
                        appPremisesSpecialDocDto.setVersion(appliSpecialDocDto1.getVersion());
                    }
                }
            }
            List<AppPremisesSpecialDocDto> appPremisesSpecialDocDtos = appealDto.getAppPremisesSpecialDocDtos();
            if(appPremisesSpecialDocDtos!=null){
                Map<String, PageShowFileDto> pageShowFileHashMap = (Map<String, PageShowFileDto>)request.getSession().getAttribute(PAGE_SHOW_FILE_HASH_MAP);
                for(AppPremisesSpecialDocDto v : appPremisesSpecialDocDtos){
                    PageShowFileDto pageShowFileDto = pageShowFileHashMap.get(SELECTED_FILE+v.getIndex());
                    if(pageShowFileDto!=null){
                        boolean equals = v.getMd5Code().equals(pageShowFileDto.getMd5Code());
                        if(equals){
                            v.setFileRepoId(pageShowFileDto.getFileUploadUrl());
                        }else {
                            v.setVersion(pageShowFileDto.getVersion()+1);
                        }
                    }
                }
            }
            applicationDto1.setVersion(rfiApplication.getVersion() + 1);
            //if need new group
            applicationGroupDto.setId(rfiApplication.getAppGrpId());
            applicationGroupDto.setGroupNo(rfiApplication.getApplicationNo().substring(0,rfiApplication.getApplicationNo().lastIndexOf('-')));
            applicationDto1.setApplicationNo(rfiApplication.getApplicationNo());
            applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
            s = rfiApplication.getApplicationNo();
            appNo=s;
            rfiApplication.setStatus(ApplicationConsts.APPLICATION_STATUS_DELETED);
            Map<String, Object> params = IaisCommonUtils.genNewHashMap(1);
            params.put("appNo", s);
            List<AppPremisesRoutingHistoryDto> hisList = feEicGatewayClient.getRoutingHistoryDtos(params).getEntity();
            if (hisList != null) {
                for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : hisList){
                    if(ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(appPremisesRoutingHistoryDto.getProcessDecision())
                            || InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(appPremisesRoutingHistoryDto.getProcessDecision())){
                        if(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(appPremisesRoutingHistoryDto.getAppStatus())){
                            applicationDto1.setStatus(ApplicationConsts.PENDING_ASO_REPLY);
                        }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(appPremisesRoutingHistoryDto.getAppStatus())){
                            applicationDto1.setStatus(ApplicationConsts.PENDING_PSO_REPLY);
                        }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(appPremisesRoutingHistoryDto.getAppStatus())){
                            applicationDto1.setStatus(ApplicationConsts.PENDING_INP_REPLY);
                        }
                    }
                }
            }
            applicationFeClient.updateApplication(rfiApplication);
            ParamUtil.setSessionAttr(request,IaisEGPConstant.RFI_APPLICATION,rfiApplication);
        }

        if (appSvcCgoDtos != null && !appSvcCgoDtos.isEmpty()) {
            log.info(StringUtil.changeForLog("appSvcCgoDtos json: " + JsonUtil.parseToJson(appSvcCgoDtos)));
            appealDto.setAppSvcCgoDto(appSvcCgoDtos);

        }else {
            log.info(StringUtil.changeForLog("appSvcCgoDtos isEmpty " ));
        }
        appealDto.setMaxFileIndex(maxFileIndex);
        AppealPageDto appealPageDto = applicationFeClient.submitAppeal(appealDto).getEntity();
        ApplicationGroupDto applicationGroupDto1 = appealPageDto.getApplicationGroupDto();
        String groupId = applicationGroupDto1.getId();
        request.setAttribute(GROUP_ID, groupId);
        request.setAttribute(DRAFT_STATUS, AppConsts.COMMON_STATUS_IACTIVE);
        saveData(request);
        request.setAttribute("newApplicationNo", appNo);
        if (!"rfi".equals(rfi)) {
            try {
                LicenseeDto licenseeDto = organizationLienceseeClient.getLicenseeById(entity.getLicenseeId()).getEntity();
                sendAllNotification(appNo,"Appeal", null, licenseeDto,hcsaServiceDto,applicationGroupDto1);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        return applicationGroupDto.getGroupNo();
    }

    private void requetForInformationGetMessage(HttpServletRequest request, AppPremiseMiscDto appPremiseMiscDto) {
        String reason = appPremiseMiscDto.getReason();
        String appPremCorreId = appPremiseMiscDto.getAppPremCorreId();
        List<AppliSpecialDocDto> appliSpecialDocDto = applicationFeClient.getAppliSpecialDocDtoByCorrId(appPremCorreId).getEntity();
        List<PageShowFileDto> pageShowFileDtos =new ArrayList<>(5);
        HashMap<String,File> map = IaisCommonUtils.genNewHashMap();
        HashMap<String, PageShowFileDto> pageShowFileHashMap = IaisCommonUtils.genNewHashMap();
        if (appliSpecialDocDto != null) {
            int indexMax = -1;
            for(int i=0;i<appliSpecialDocDto.size();i++){
                AppliSpecialDocDto appliSpecialDocDtoOne = appliSpecialDocDto.get(i);
                String index = appliSpecialDocDtoOne.getIndex();
                if(StringUtil.isEmpty(index)){
                    index = String.valueOf(i);
                }
                int indexInt = Integer.parseInt(index);
                if(indexInt >= indexMax){
                    indexMax = indexInt;
                }
                PageShowFileDto pageShowFileDto =new PageShowFileDto();
                pageShowFileDto.setFileName(appliSpecialDocDtoOne.getDocName());
                pageShowFileDto.setIndex(index);
                pageShowFileDto.setFileMapId(SELECTED_FILE_DIV+index);
                pageShowFileDto.setSize(Integer.valueOf(appliSpecialDocDtoOne.getDocSize()));
                pageShowFileDto.setMd5Code(appliSpecialDocDtoOne.getMd5Code());
                pageShowFileDto.setFileUploadUrl(appliSpecialDocDtoOne.getFileRepoId());
                pageShowFileDto.setVersion(appliSpecialDocDtoOne.getVersion());
                pageShowFileDtos.add(pageShowFileDto);
                map.put(SELECTED_FILE+index,null);
                pageShowFileHashMap.put(SELECTED_FILE+index, pageShowFileDto);
            }
            request.getSession().setAttribute(PAGE_SHOW_FILE_HASH_MAP,pageShowFileHashMap);
            request.getSession().setAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX_FE_SELECT_FILE,map);
            request.getSession().setAttribute("seesion_files_map_ajax_feselectedFile_MaxIndex",indexMax+1);
        }
        Collections.sort(pageShowFileDtos,(s1,s2)->s1.getFileMapId().compareTo(s2.getFileMapId()));
        request.getSession().setAttribute(PAGE_SHOW_FILES, pageShowFileDtos);
        request.getSession().setAttribute(APPPREMISEMISCDTO, appPremiseMiscDto);
        ApplicationDto entity = applicationFeClient.getApplicationByCorrId(appPremCorreId).getEntity();
        if (entity != null) {
            AppGroupMiscDto grpMisc = applicationFeClient.getAppGroupMiscDtoByGrpIdAndTypeAndStatus(entity.getAppGrpId(),
                    ApplicationConsts.APP_GROUP_MISC_TYPE_MAX_FILE_INDEX, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
            if (grpMisc != null) {
                ParamUtil.setSessionAttr(request, IaisEGPConstant.GLOBAL_MAX_INDEX_SESSION_ATTR,
                        Integer.valueOf(grpMisc.getMiscValue()));
            }
            if (ApplicationConsts.APPEAL_REASON_APPLICATION_ADD_CGO.equals(reason)) {
                String appGrpId = entity.getAppGrpId();
                List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = applicationFeClient.getAppGrpPersonnelByGrpId(appGrpId).getEntity();
                if(appSvcCgoDtos!=null){
                    for(AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoDtos){
                        String personKey = ApplicationHelper.getPersonKey(appSvcCgoDto.getNationality(), appSvcCgoDto.getIdType(), appSvcCgoDto.getIdNo().toLowerCase());
                        appSvcCgoDto.setAssignSelect(personKey);
                    }
                    ParamUtil.setSessionAttr(request, CGO_MANDATORY_COUNT, appSvcCgoDtos.size());
                }
                LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr( request, AppConsts.SESSION_ATTR_LOGIN_USER);
                List<FeUserDto> feUserDtos = requestForChangeService.getFeUserDtoByLicenseeId(loginContext.getLicenseeId());
                ParamUtil.setSessionAttr(request, HcsaAppConst.CURR_ORG_USER_ACCOUNT, (Serializable) feUserDtos);
                List<PersonnelListQueryDto> licPersonList = requestForChangeService.getLicencePersonnelListQueryDto(loginContext.getLicenseeId());
                Map<String, AppSvcPersonAndExtDto> licPersonMap=IaisCommonUtils.genNewHashMap();
                Map<String, AppSvcPersonAndExtDto> personMap = ApplicationHelper.getLicPsnIntoSelMap(feUserDtos,licPersonList,licPersonMap);
                ParamUtil.setSessionAttr(request, HcsaAppConst.PERSONSELECTMAP, (Serializable) personMap);
                List<SelectOption> cgoSelectList = ApplicationHelper.genAssignPersonSel(request, true);
                ParamUtil.setSessionAttr(request, "CgoSelectList", (Serializable) cgoSelectList);
                ParamUtil.setSessionAttr(request, GOVERNANCE_OFFICERS_LIST, (Serializable) appSvcCgoDtos);
                HcsaServiceDto serviceDto= HcsaServiceCacheHelper.getServiceById(entity.getServiceId());
                ParamUtil.setSessionAttr(request, HcsaAppConst.CURRENTSVCCODE,serviceDto.getSvcCode());
                List<SelectOption> idTypeSelOp = AppealDelegator.getIdTypeSelOp();
                ParamUtil.setSessionAttr(request, "IdTypeSelect",(Serializable)  idTypeSelOp);
                List<SelectOption> list = AppealDelegator.genSpecialtySelectList(serviceDto.getSvcCode());
                ParamUtil.setSessionAttr(request, "SpecialtySelectList", (Serializable) list);
            }
        }else {
            return;
        }

        request.getSession().setAttribute("rfi", "rfi");
        request.getSession().setAttribute(IaisEGPConstant.RFI_APPLICATION, entity);

    }

    private AppealPageDto getAppealPageDto(HttpServletRequest req) {
        LoginContext loginContext = (LoginContext) req.getSession().getAttribute("loginContext");
        AppealPageDto appealDto = new AppealPageDto();
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) req.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String reasonSelect = request.getParameter(REASON_SELECT);
        String licenceYear = request.getParameter("licenceYear");
        String proposedHciName = request.getParameter(PROPOSED_HCI_NAME);

        String remarks = request.getParameter(REMARKS);
        String othersReason = request.getParameter(OTHERS_REASON);
        appealDto.setOtherReason(othersReason);
        Map<String, File> map = (Map<String, File>)req.getSession().getAttribute(IaisEGPConstant.SEESION_FILES_MAP_AJAX_FE_SELECT_FILE);
        Map<String, PageShowFileDto> pageShowFileHashMap = (Map<String, PageShowFileDto>)request.getSession().getAttribute(PAGE_SHOW_FILE_HASH_MAP);
        List<AppPremisesSpecialDocDto> appPremisesSpecialDocDtos =new ArrayList<>(5);
        List<File> files=new ArrayList<>(5);
        if(map!=null&&!map.isEmpty()){
            map.forEach((k,v)->{
                if(v!=null){
                    long length = v.length();
                    if(length>0){
                        Long size=length/1024;
                        files.add(v);
                        AppPremisesSpecialDocDto premisesSpecialDocDto=new AppPremisesSpecialDocDto();
                        premisesSpecialDocDto.setDocName(v.getName());
                        String fileMd5 = FileUtils.getFileMd5(v);
                        premisesSpecialDocDto.setMd5Code(fileMd5);
                        premisesSpecialDocDto.setSubmitBy(loginContext.getUserId());
                        premisesSpecialDocDto.setDocSize(Integer.valueOf(size.toString()));
                        premisesSpecialDocDto.setIndex( k.substring(k.lastIndexOf('e') + 1));
                        appPremisesSpecialDocDtos.add(premisesSpecialDocDto);
                    }
                }else {
                    if(pageShowFileHashMap!=null){
                        PageShowFileDto pageShowFileDto = pageShowFileHashMap.get(k);
                        AppPremisesSpecialDocDto premisesSpecialDocDto=new AppPremisesSpecialDocDto();
                        premisesSpecialDocDto.setFileRepoId(pageShowFileDto.getFileUploadUrl());
                        premisesSpecialDocDto.setDocSize(pageShowFileDto.getSize());
                        premisesSpecialDocDto.setDocName(pageShowFileDto.getFileName());
                        premisesSpecialDocDto.setMd5Code(pageShowFileDto.getMd5Code());
                        premisesSpecialDocDto.setSubmitBy(loginContext.getUserId());
                        premisesSpecialDocDto.setIndex(pageShowFileDto.getIndex());
                        premisesSpecialDocDto.setVersion(pageShowFileDto.getVersion());
                        appPremisesSpecialDocDtos.add(premisesSpecialDocDto);
                    }
                }

            });
        }

        List<String> list = comFileRepoClient.saveFileRepo(files);
        if(list!=null) {
            ListIterator<String> iterator = list.listIterator();

            for (int j = 0; j < appPremisesSpecialDocDtos.size(); j++) {
                String fileRepoId = appPremisesSpecialDocDtos.get(j).getFileRepoId();
                if (fileRepoId == null) {
                    if (iterator.hasNext()) {
                        String next = iterator.next();
                        appPremisesSpecialDocDtos.get(j).setFileRepoId(next);
                        iterator.remove();
                    }
                }
            }
        }

            appealDto.setAppPremisesSpecialDocDtos(appPremisesSpecialDocDtos);

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
        appealDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return appealDto;
    }

    private void sendAllNotification(String appNo,String appType,LicenceDto licenceDto, LicenseeDto licenseeDto,HcsaServiceDto hcsaServiceDto,ApplicationGroupDto applicationGroupDto) throws IOException, TemplateException{
        log.info("start send email sms and msg");
        log.info(StringUtil.changeForLog("appNo: " + appNo));
        Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        String applicantId = applicationGroupDto.getSubmitBy();
        OrgUserDto orgUserDto = organizationLienceseeClient.retrieveOneOrgUserAccount(applicantId).getEntity();
        String applicantName = orgUserDto.getDisplayName();
        templateContent.put("ApplicantName", applicantName);
        templateContent.put("ApplicationType",appType);
        templateContent.put("ApplicationNo", appNo);
        templateContent.put("ApplicationDate", Formatter.formatDateTime(new Date()));
        templateContent.put("newSystem", loginUrl);
        templateContent.put("emailAddress", systemParamConfig.getSystemAddressOne());
        MsgTemplateDto emailTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_GENERIC_EMAIL).getEntity();
        MsgTemplateDto smsTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_GENERIC_SMS).getEntity();
        MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_GENERIC_MSG).getEntity();
        Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
        subMap.put("ApplicationType", appType);
        subMap.put("ApplicationNumber", appNo);
        String emailSubject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subMap);
        String smsSubject = MsgUtil.getTemplateMessageByContent(smsTemplateDto.getTemplateName(),subMap);
        String msgSubject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),subMap);

        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_GENERIC_EMAIL);
        emailParam.setTemplateContent(templateContent);
        emailParam.setSubject(emailSubject);
        emailParam.setQueryCode(appNo);
        emailParam.setReqRefNum(appNo);
        if(licenceDto == null){
            emailParam.setRefId(appNo+"-01");
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        }else{
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
            emailParam.setRefId(licenceDto.getId());
        }

        notificationHelper.sendNotification(emailParam);
        EmailParam smsParam = new EmailParam();
        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_GENERIC_SMS);
        smsParam.setSubject(smsSubject);
        smsParam.setTemplateContent(templateContent);
        smsParam.setQueryCode(appNo);
        smsParam.setReqRefNum(appNo);
        if(licenceDto == null) {
            smsParam.setRefId(appNo+"-01");
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        }else{
            smsParam.setRefId(licenceDto.getId());
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
        }
        notificationHelper.sendNotification(smsParam);
        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_GENERIC_MSG);
        msgParam.setTemplateContent(templateContent);
        msgParam.setSubject(msgSubject);
        msgParam.setQueryCode(appNo);
        msgParam.setReqRefNum(appNo);
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        svcCodeList.add(hcsaServiceDto.getSvcCode());
        msgParam.setSvcCodeList(svcCodeList);
        if(licenceDto == null) {
            msgParam.setRefId(appNo+"-01");
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        }else{
            msgParam.setRefId(licenceDto.getId());
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        }
        notificationHelper.sendNotification(msgParam);
        log.info("end send email sms and msg");
    }


    private boolean isMaxCGOnumber(ApplicationDto applicationDto) {
        String serviceId = applicationDto.getServiceId();
        HcsaServiceDto activeHcsaServiceDtoById = serviceConfigService.getActiveHcsaServiceDtoById(serviceId);
        if(activeHcsaServiceDtoById!=null){
            List<AppSvcKeyPersonnelDto> appSvcKeyPersonnelDtos = applicationFeClient.getAppSvcKeyPersonnel(applicationDto).getEntity();
            List<HcsaServiceStepSchemeDto> list = configCommClient.getServiceStepsByServiceId(activeHcsaServiceDtoById.getId()).getEntity();
            if(list!=null){
                Optional<HcsaServiceStepSchemeDto> any = list.stream().filter(v -> HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(v.getStepCode())).findAny();
                if(!any.isPresent()){
                   return false;
                }
            }
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = configCommClient.getHcsaSvcPersonnelDtoByServiceId(activeHcsaServiceDtoById.getId()).getEntity();
            if (hcsaSvcPersonnelDto != null) {
                int maximumCount = hcsaSvcPersonnelDto.getMaximumCount();
                int size = appSvcKeyPersonnelDtos.size();
                if (size < maximumCount) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    private void validateFiles(File file,Map<String,String> map){
        if(file==null){
            throw new NullPointerException();
        }
        int configFileSize = systemParamConfig.getUploadFileLimit();
        String configFileType = FileUtils.getStringFromSystemConfigString(systemParamConfig.getUploadFileType());
        List<String> fileTypes = Arrays.asList(configFileType.split(","));
        Map<String, Boolean> booleanMap = ValidationUtils.validateFile(file,fileTypes,(configFileSize * 1024 *1024L));
        Boolean fileSize = booleanMap.get("fileSize");
        Boolean fileType = booleanMap.get(FILE_TYPE);
        Boolean fileNameLength = booleanMap.get("fileNameLength");
        if(!fileSize){
            map.put("file", MessageUtil.replaceMessage(IaisEGPConstant.ERR_FILE_UPLOAD_MAX, String.valueOf(configFileSize),SIZE_MAX));
        }
        //type
        if(!fileType){
            map.put("file",MessageUtil.replaceMessage(IaisEGPConstant.ERR_RE_FILE_UPLOAD, configFileType,FILE_TYPE));
        }
        if(!fileNameLength){
            map.put("file",IaisEGPConstant.ERR_FILENAME_CHARACTERS);
        }
    }
    private void validateFile(PageShowFileDto pageShowFileDto,Map<String,String> map,int i){
        int configFileSize = systemParamConfig.getUploadFileLimit();
        String configFileType = FileUtils.getStringFromSystemConfigString(systemParamConfig.getUploadFileType());
        List<String> fileTypes = Arrays.asList(configFileType.split(","));
        if(pageShowFileDto.getSize()/1024>configFileSize){
            map.put("file"+i, MessageUtil.replaceMessage(IaisEGPConstant.ERR_FILE_UPLOAD_MAX, String.valueOf(configFileSize),SIZE_MAX));
        }
        String substring = pageShowFileDto.getFileName().substring(pageShowFileDto.getFileName().lastIndexOf('.') + 1);
        if(!fileTypes.contains(substring.toUpperCase())){
            map.put("file"+i,MessageUtil.replaceMessage(IaisEGPConstant.ERR_RE_FILE_UPLOAD, configFileType,FILE_TYPE));
        }
        if(pageShowFileDto.getFileName().length()>100){
            map.put("file"+i,IaisEGPConstant.ERR_FILENAME_CHARACTERS);
        }
    }

    private void validateCgoIdNo(ApplicationDto applicationDto,Map<String,String> map){
        AppSubmissionDto entity = appCommService.getAppSubmissionDtoByAppNo(applicationDto.getApplicationNo());
        if(entity!=null){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = entity.getAppSvcRelatedInfoDtoList();
            if(appSvcRelatedInfoDtoList!=null&&!appSvcRelatedInfoDtoList.isEmpty()){
                List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDtoList.get(0).getAppSvcCgoDtoList();
                if(appSvcCgoDtoList!=null){
                    Iterator<AppSvcPrincipalOfficersDto> iterator = appSvcCgoDtoList.iterator();
                    while (iterator.hasNext()){
                        AppSvcPrincipalOfficersDto next = iterator.next();
                        String personKey = ApplicationHelper.getPersonKey(next.getNationality(), next.getIdType(), next.getIdNo().toUpperCase());
                        map.put(personKey,personKey);

                    }
                }
            }
        }

    }
}
