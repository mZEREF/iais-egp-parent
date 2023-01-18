package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationTabEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InsTabEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InspectionTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.OnlineEnquiriesService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Comparator;
import java.util.List;

/**
 * OnlineEnquiryApplicationDelegator
 *
 * @author junyu
 * @date 2022/12/22
 */
@Delegator(value = "onlineEnquiryApplicationDelegator")
@Slf4j
public class OnlineEnquiryApplicationDelegator {
    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();
    private static final String APP_ID = "appId";
    private static final String INSTAB_PARAM = "insTabParam";
    private static final String INS_TAB_RESULT = "insTabResult";

    FilterParameter insTabParameter = new FilterParameter.Builder()
            .clz(InspectionTabQueryResultsDto.class)
            .searchAttr(INSTAB_PARAM)
            .resultAttr(INS_TAB_RESULT)
            .sortField("ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();

    FilterParameter applicationParameter = new FilterParameter.Builder()
            .clz(ApplicationQueryResultsDto.class)
            .searchAttr("appParam")
            .resultAttr("appResult")
            .sortField("APP_ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();

    @Autowired
    private OnlineEnquiryLicenceDelegator licenceDelegator;
    @Autowired
    private RequestForInformationService requestForInformationService;
    @Autowired
    private OnlineEnquiriesService onlineEnquiriesService;
    @Autowired
    private ApplicationViewService applicationViewService;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private TaskOrganizationClient taskOrganizationClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private LicenceViewServiceDelegator licenceViewServiceDelegator;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;




    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY,  AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        applicationParameter.setPageSize(pageSize);
        applicationParameter.setPageNo(1);
        applicationParameter.setSortField("APP_ID");
        applicationParameter.setSortType(SearchParam.DESCENDING);
        ParamUtil.setSessionAttr(bpc.request,"applicationEnquiryFilterDto",null);
        ParamUtil.setSessionAttr(bpc.request, "appParam",null);
        ParamUtil.setSessionAttr(bpc.request, APP_ID,null);
    }

    public void preSearch(BaseProcessClass bpc){
        HttpServletRequest request=bpc.request;
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);

        List<SelectOption> autoApprovedOption =getAutoApprovedOption();
        ParamUtil.setRequestAttr(request,"autoApprovedOption", autoApprovedOption);

        List<SelectOption> assignedOfficerOption =getAdminRolesNameOption();
        ParamUtil.setRequestAttr(request,"assignedOfficerOption", assignedOfficerOption);
        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "appParam");



        if(!"back".equals(back)||searchParam==null){
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                applicationParameter.setSortType(sortType);
                applicationParameter.setSortField(sortFieldName);
            }
            ApplicationTabEnquiryFilterDto appFilterDto=licenceDelegator.setAppEnquiryFilterDto(request);

            licenceDelegator.setAppQueryFilter(appFilterDto,applicationParameter);
            if(applicationParameter.getFilters().isEmpty()){
                return;
            }

            SearchParam appParam = SearchResultHelper.getSearchParam(request, applicationParameter,true);

            if(appFilterDto.getAppStatus()!=null){
                licenceDelegator.setSearchParamAppStatus(appFilterDto.getAppStatus(),appParam,"aqrv");
                appParam.removeFilter("getAppStatus");
            }
            if(searchParam!=null){
                appParam.setPageNo(searchParam.getPageNo());
                appParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(appParam,bpc.request);
            QueryHelp.setMainSql("hcsaOnlineEnquiry","applicationOnlineEnquiry",appParam);
            SearchResult<ApplicationQueryResultsDto> appResult = onlineEnquiriesService.searchApplicationQueryResult(appParam);
            ParamUtil.setRequestAttr(request,"appResult",appResult);
            ParamUtil.setSessionAttr(request,"appParam",appParam);
        }else {
            SearchResult<ApplicationQueryResultsDto> appResult = onlineEnquiriesService.searchApplicationQueryResult(searchParam);
            ParamUtil.setRequestAttr(request,"appResult",appResult);
            ParamUtil.setSessionAttr(request,"appParam",searchParam);
        }

    }

    List<SelectOption> getAutoApprovedOption() {
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();

        selectOptions.add(new SelectOption("0", "No"));
        selectOptions.add(new SelectOption("1", "Yes"));
        selectOptions.sort(Comparator.comparing(SelectOption::getText));
        return selectOptions;
    }

    private List<SelectOption> getAdminRolesNameOption() {
        List<String> adminRoles = IaisCommonUtils.genNewArrayList();
        adminRoles.add(RoleConsts.USER_ROLE_ASO);
        adminRoles.add(RoleConsts.USER_ROLE_PSO);
        adminRoles.add(RoleConsts.USER_ROLE_AO1);
        adminRoles.add(RoleConsts.USER_ROLE_AO2);
        adminRoles.add(RoleConsts.USER_ROLE_AO3);
        adminRoles.add(RoleConsts.USER_ROLE_INSPECTIOR);
        adminRoles.add(RoleConsts.USER_ROLE_INSPECTION_LEAD);
        adminRoles.add(RoleConsts.USER_ROLE_AUDIT_PLAN);
        List<OrgUserDto> userList= taskOrganizationClient.retrieveOrgUserByroleId(adminRoles).getEntity();

        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();

        for (OrgUserDto user:userList
        ) {
            selectOptions.add(new SelectOption(user.getDisplayName(), user.getDisplayName()));
        }
        selectOptions.sort(Comparator.comparing(SelectOption::getText));
        return selectOptions;
    }

    public void nextStep(BaseProcessClass bpc){
        String appId = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(appId)) {
            try {
                appId= MaskUtil.unMaskValue(APP_ID,appId);
                ParamUtil.setSessionAttr(bpc.request, APP_ID,appId);
            }catch (Exception e){
                log.info("no APP_ID");
            }
        }
        String payAppNo = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(payAppNo)) {
            try {
                payAppNo= MaskUtil.unMaskValue("payAppNo",payAppNo);
                ParamUtil.setSessionAttr(bpc.request, "payAppNo",payAppNo);
                ParamUtil.setSessionAttr(bpc.request, "payAppStep",payAppNo);
                ParamUtil.setSessionAttr(bpc.request, "payAppInsStep",null);
                ParamUtil.setSessionAttr(bpc.request, "payLicStep",null);
                StringBuilder url = new StringBuilder();
                url.append("https://")
                        .append(bpc.request.getServerName())
                        .append("/hcsa-licence-web/eservice/INTRANET/MohPaymentOnlineEnquiry/1/preSearch");
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            }catch (Exception e){
                log.info("no payAppNo");
            }
        }
    }

    public void preAppInfo(BaseProcessClass bpc){
        String appId = (String) ParamUtil.getSessionAttr(bpc.request, APP_ID);
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
        ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(appPremisesCorrelationDto.getId());
        OrgUserDto submitDto=organizationClient.retrieveOrgUserAccountById(applicationViewDto.getApplicationGroupDto().getSubmitBy()).getEntity();
        applicationViewDto.getApplicationGroupDto().setSubmitBy(submitDto.getDisplayName());
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        AppSubmissionDto appSubmissionDto = licenceViewServiceDelegator.getAppSubmissionAndHandLicence(appPremisesCorrelationDto, bpc.request);
        ParamUtil.setSessionAttr(bpc.request, HcsaAppConst.APPSUBMISSIONDTO, appSubmissionDto);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            appSvcRelatedInfoDto = appSvcRelatedInfoDtos.get(0);
        }
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
        ParamUtil.setSessionAttr(bpc.request,"isSingle",0);
        List<TaskDto> taskDtos = organizationClient.getCurrTaskByRefNo(appPremisesCorrelationDto.getId()).getEntity();
        TaskDto taskDto=new TaskDto();

        if(IaisCommonUtils.isNotEmpty(taskDtos)){
            taskDto=taskDtos.get(0);
            if(StringUtil.isNotEmpty(taskDto.getWkGrpId())){
                WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(taskDto.getWkGrpId()).getEntity();
                taskDto.setWkGrpId(workingGroupDto.getGroupName());
            }
            if(StringUtil.isNotEmpty(taskDto.getUserId())){
                OrgUserDto userDto=organizationClient.retrieveOrgUserAccountById(taskDto.getUserId()).getEntity();
                taskDto.setUserId(userDto.getDisplayName());
            }
        }else {
            taskDto.setUserId("-");
        }
        ParamUtil.setSessionAttr(bpc.request, "currTask", taskDto);

        LicAppCorrelationDto licAppCorrelationDto = hcsaLicenceClient.getOneLicAppCorrelationByApplicationId(appId).getEntity();
        LicenceDto licenceDto = new LicenceDto();
        if(licAppCorrelationDto!=null){
            licenceDto = hcsaLicenceClient.getLicDtoById(licAppCorrelationDto.getLicenceId()).getEntity();
        }else {
            licenceDto.setStatus("-");
            licenceDto.setLicenceNo("-");
        }
        ParamUtil.setSessionAttr(bpc.request, "licenceDto", licenceDto);

        insTabParameter.setPageSize(pageSize);
        insTabParameter.setPageNo(1);
        insTabParameter.setSortField("ID");
        insTabParameter.setSortType(SearchParam.DESCENDING);
        ParamUtil.setSessionAttr(bpc.request,"insTabEnquiryFilterDto",null);
        ParamUtil.setSessionAttr(bpc.request, INSTAB_PARAM,null);
    }

    public void preInsTab(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request=bpc.request;

        String appId = (String) ParamUtil.getSessionAttr(bpc.request, APP_ID);
        ApplicationDto applicationDto=applicationClient.getApplicationById(appId).getEntity();
        List<SelectOption> appStatusOption =requestForInformationService.getAppStatusOption();
        List<SelectOption> inspectionTypeOption =licenceDelegator.getInspectionTypeOption();
        ParamUtil.setRequestAttr(request,"inspectionTypeOption", inspectionTypeOption);
        ParamUtil.setRequestAttr(request,"appStatusOption", appStatusOption);

        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, INSTAB_PARAM);

        if(!"back".equals(back)||searchParam==null){
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                insTabParameter.setSortType(sortType);
                insTabParameter.setSortField(sortFieldName);
            }
            InsTabEnquiryFilterDto filterDto=licenceDelegator.setInsEnquiryFilterDto(request);

            licenceDelegator.setInsQueryFilter(filterDto,insTabParameter);

            SearchParam insTabParam = SearchResultHelper.getSearchParam(request, insTabParameter,true);

            if(filterDto.getAppStatus()!=null){
                licenceDelegator.setSearchParamAppStatus(filterDto.getAppStatus(),insTabParam,"insTab");
                insTabParam.removeFilter("getAppStatus");
            }
            if(searchParam!=null){
                ParamUtil.setRequestAttr(bpc.request, "preActive", "3");
                insTabParam.setPageNo(searchParam.getPageNo());
                insTabParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(insTabParam,bpc.request);
            insTabParam.addFilter("appNo",applicationDto.getApplicationNo(),true);

            QueryHelp.setMainSql("hcsaOnlineEnquiry","inspectionsTabOnlineEnquiry",insTabParam);
            SearchResult<InspectionTabQueryResultsDto> insTabResult = onlineEnquiriesService.searchLicenceInsTabQueryResult(insTabParam);
            ParamUtil.setRequestAttr(request,INS_TAB_RESULT,insTabResult);
            ParamUtil.setSessionAttr(request,INSTAB_PARAM,insTabParam);
        }else {
            SearchResult<InspectionTabQueryResultsDto> insTabResult = onlineEnquiriesService.searchLicenceInsTabQueryResult(searchParam);
            ParamUtil.setRequestAttr(request,INS_TAB_RESULT,insTabResult);
            ParamUtil.setSessionAttr(request,INSTAB_PARAM,searchParam);
        }

    }

    public void insStep(BaseProcessClass bpc){
        String corrId = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(corrId)) {
            try {
                corrId= MaskUtil.unMaskValue("appCorrId",corrId);
                ParamUtil.setSessionAttr(bpc.request, "appCorrId",corrId);
            }catch (Exception e){
                log.info("no appCorrId");
            }
        }
        String payAppNo = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(payAppNo)) {
            try {
                payAppNo= MaskUtil.unMaskValue("payAppNo",payAppNo);
                ParamUtil.setSessionAttr(bpc.request, "payAppNo",payAppNo);
                ParamUtil.setSessionAttr(bpc.request, "payAppInsStep",payAppNo);
                ParamUtil.setSessionAttr(bpc.request, "payAppStep",null);
                ParamUtil.setSessionAttr(bpc.request, "payLicStep",null);

                StringBuilder url = new StringBuilder();
                url.append("https://")
                        .append(bpc.request.getServerName())
                        .append("/hcsa-licence-web/eservice/INTRANET/MohPaymentOnlineEnquiry/1/preSearch");
                String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
                IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            }catch (Exception e){
                log.info("no payAppNo");
            }
        }
    }


    public void preInspectionReport(BaseProcessClass bpc){
        String kpiInfo = MessageUtil.getMessageDesc("LOLEV_ACK051");
        ParamUtil.setSessionAttr(bpc.request, "kpiInfo", kpiInfo);
        HttpServletRequest request=bpc.request;
        String appPremisesCorrelationId=(String) ParamUtil.getSessionAttr(request, "appCorrId");
        LicenceDto licenceDto = (LicenceDto) ParamUtil.getSessionAttr(bpc.request, "licenceDto");
        onlineEnquiriesService.getInspReport(bpc,appPremisesCorrelationId,licenceDto.getId());
    }

    public void backInsTab(BaseProcessClass bpc){

    }
}
