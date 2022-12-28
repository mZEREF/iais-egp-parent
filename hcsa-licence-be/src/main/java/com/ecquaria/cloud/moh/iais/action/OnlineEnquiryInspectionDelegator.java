package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InspectionEnquiryFilterDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InspectionQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
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
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OnlineEnquiryInspectionDelegator
 *
 * @author junyu
 * @date 2022/12/28
 */
@Delegator(value = "onlineEnquiryInspectionDelegator")
@Slf4j
public class OnlineEnquiryInspectionDelegator extends InspectionCheckListCommonMethodDelegator{
    private static Integer pageSize = SystemParamUtil.getDefaultPageSize();

    FilterParameter inspectionParameter = new FilterParameter.Builder()
            .clz(InspectionQueryResultsDto.class)
            .searchAttr("inspectionParam")
            .resultAttr("inspectionResult")
            .sortField("ID").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(pageSize).build();

    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private OnlineEnquiriesService onlineEnquiriesService;
    @Autowired
    private RequestForInformationService requestForInformationService;

    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private OnlineEnquiryLicenceDelegator licenceDelegator;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private LicenceViewServiceDelegator licenceViewServiceDelegator;


    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_ONLINE_ENQUIRY,  AuditTrailConsts.FUNCTION_ONLINE_ENQUIRY);
        String p = systemParamConfig.getPagingSize();
        String defaultValue = IaisEGPHelper.getPageSizeByStrings(p)[0];
        pageSize= Integer.valueOf(defaultValue);
        inspectionParameter.setPageSize(pageSize);
        inspectionParameter.setPageNo(1);
        inspectionParameter.setSortField("ID");
        inspectionParameter.setSortType(SearchParam.DESCENDING);
        ParamUtil.setSessionAttr(bpc.request,"inspectionEnquiryFilterDto",null);
        ParamUtil.setSessionAttr(bpc.request, "inspectionParam",null);
    }

    public void preSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request=bpc.request;

        List<SelectOption> inspectionTypeOption =licenceDelegator.getInspectionTypeOption();
        ParamUtil.setRequestAttr(request,"inspectionTypeOption", inspectionTypeOption);
        List<SelectOption> licSvcTypeOption =requestForInformationService.getLicSvcTypeOption();
        List<SelectOption> mosdTypeOption =licenceDelegator.getMosdTypeOption();
        ParamUtil.setRequestAttr(request,"mosdTypeOption", mosdTypeOption);
        ParamUtil.setRequestAttr(request,"licSvcTypeOption", licSvcTypeOption);

        String back =  ParamUtil.getString(request,"back");
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, "inspectionParam");

        if(!"back".equals(back)||searchParam==null){
            String sortFieldName = ParamUtil.getString(request,"crud_action_value");
            String sortType = ParamUtil.getString(request,"crud_action_additional");
            if(!StringUtil.isEmpty(sortFieldName)&&!StringUtil.isEmpty(sortType)){
                inspectionParameter.setSortType(sortType);
                inspectionParameter.setSortField(sortFieldName);
            }
            InspectionEnquiryFilterDto filterDto=setInsEnquiryFilterDto(request);

            setInsQueryFilter(filterDto,inspectionParameter);
            if(inspectionParameter.getFilters().isEmpty()){
                return;
            }
            SearchParam inspectionParam = SearchResultHelper.getSearchParam(request, inspectionParameter,true);


            if(searchParam!=null){
                inspectionParam.setPageNo(searchParam.getPageNo());
                inspectionParam.setPageSize(searchParam.getPageSize());
            }
            CrudHelper.doPaging(inspectionParam,bpc.request);

            QueryHelp.setMainSql("hcsaOnlineEnquiry","inspectionOnlineEnquiry",inspectionParam);
            SearchResult<InspectionQueryResultsDto> inspectionResult = onlineEnquiriesService.searchInspectionQueryResult(inspectionParam);
            ParamUtil.setRequestAttr(request,"inspectionResult",inspectionResult);
            ParamUtil.setSessionAttr(request,"inspectionParam",inspectionParam);
        }else {
            SearchResult<InspectionQueryResultsDto> inspectionResult = onlineEnquiriesService.searchInspectionQueryResult(searchParam);
            ParamUtil.setRequestAttr(request,"inspectionResult",inspectionResult);
            ParamUtil.setSessionAttr(request,"inspectionParam",searchParam);
        }

    }

    private void setInsQueryFilter(InspectionEnquiryFilterDto filterDto, FilterParameter inspectionParameter) {

        Map<String,Object> filter= IaisCommonUtils.genNewHashMap();
        if(filterDto.getApplicationNo()!=null) {
            filter.put("getApplicationNo", filterDto.getApplicationNo());
        }
        if(filterDto.getApplicationType()!=null) {
            filter.put("getApplicationType", filterDto.getApplicationType());
        }
        if(filterDto.getMosdType()!=null) {
            filter.put("getMosdType", filterDto.getMosdType());
        }
        if(filterDto.getAuditType()!=null) {
            filter.put("getAuditType", filterDto.getAuditType());
        }
        if(filterDto.getServiceName()!=null) {
            filter.put("getServiceName",filterDto.getServiceName());
        }
        if(filterDto.getInspectionReason()!=null) {
        }
        if(filterDto.getBusinessName()!=null){
            filter.put("getBusinessName", filterDto.getBusinessName());
        }
        if(filterDto.getInspectionType()!=null){
            filter.put("getInspectionType", filterDto.getInspectionType());
        }
        if(filterDto.getInspectionDateFrom()!=null){
            String dateTime = Formatter.formatDateTime(filterDto.getInspectionDateFrom(),
                    SystemAdminBaseConstants.DATE_FORMAT);
            filter.put("getInspectionDateFrom", dateTime);
        }

        if(filterDto.getInspectionDateTo()!=null){
            String dateTime = Formatter.formatDateTime(filterDto.getInspectionDateTo(),
                    SystemAdminBaseConstants.DATE_FORMAT+SystemAdminBaseConstants.TIME_FORMAT);
            filter.put("getInspectionDateTo", dateTime);
        }

        inspectionParameter.setFilters(filter);
    }

    private InspectionEnquiryFilterDto setInsEnquiryFilterDto(HttpServletRequest request) throws ParseException {
        InspectionEnquiryFilterDto filterDto=new InspectionEnquiryFilterDto();
        String applicationNo=ParamUtil.getString(request,"applicationNo");
        filterDto.setApplicationNo(applicationNo);
        String applicationType=ParamUtil.getString(request,"applicationType");
        filterDto.setApplicationType(applicationType);
        String mosdType=ParamUtil.getString(request,"mosdType");
        filterDto.setMosdType(mosdType);
        String inspectionReason=ParamUtil.getString(request,"inspectionReason");
        filterDto.setInspectionReason(inspectionReason);
        String auditType=ParamUtil.getString(request,"auditType");
        filterDto.setAuditType(auditType);
        String serviceName=ParamUtil.getString(request,"serviceName");
        filterDto.setServiceName(serviceName);
        String businessName=ParamUtil.getString(request,"businessName");
        filterDto.setBusinessName(businessName);
        Date inspectionDateFrom= Formatter.parseDate(ParamUtil.getString(request, "inspectionDateFrom"));
        filterDto.setInspectionDateFrom(inspectionDateFrom);
        Date inspectionDateTo= Formatter.parseDate(ParamUtil.getString(request, "inspectionDateTo"));
        filterDto.setInspectionDateTo(inspectionDateTo);
        String inspectionType=ParamUtil.getString(request,"inspectionType");
        filterDto.setInspectionType(inspectionType);
        ParamUtil.setSessionAttr(request,"inspectionEnquiryFilterDto",filterDto);
        return filterDto;
    }

    public void nextStep(BaseProcessClass bpc){
        String appCorrId = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if (!StringUtil.isEmpty(appCorrId)) {
            try {
                appCorrId= MaskUtil.unMaskValue("appCorrId",appCorrId);
                ParamUtil.setSessionAttr(bpc.request, "appCorrId",appCorrId);
            }catch (Exception e){
                log.info("no appCorrId");
            }
        }

    }

    public void perDetails(BaseProcessClass bpc){
        String appCorrId = (String) ParamUtil.getSessionAttr(bpc.request, "appCorrId");
        ApplicationViewDto applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(appCorrId);
        OrgUserDto submitDto=organizationClient.retrieveOrgUserAccountById(applicationViewDto.getApplicationGroupDto().getSubmitBy()).getEntity();
        applicationViewDto.getApplicationGroupDto().setSubmitBy(submitDto.getDisplayName());
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        AppSubmissionDto appSubmissionDto = licenceViewServiceDelegator.getAppSubmissionAndHandLicence(applicationViewDto.getNewAppPremisesCorrelationDto(), bpc.request);
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

        TaskDto taskDtoIns=organizationClient.getTaskByApplicationNoAndRoleIdAndStatus(applicationViewDto.getApplicationDto().getApplicationNo(), RoleConsts.USER_ROLE_INSPECTIOR, TaskConsts.TASK_STATUS_COMPLETED).getEntity().get(0);
        setCheckDataHaveFinished(bpc.request,taskDtoIns);
        List<TaskDto> taskDtos = organizationClient.getCurrTaskByRefNo(appCorrId).getEntity();
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

        LicAppCorrelationDto licAppCorrelationDto = hcsaLicenceClient.getOneLicAppCorrelationByApplicationId(applicationViewDto.getApplicationDto().getId()).getEntity();
        LicenceDto licenceDto = new LicenceDto();
        if(licAppCorrelationDto!=null){
            licenceDto = hcsaLicenceClient.getLicDtoById(licAppCorrelationDto.getLicenceId()).getEntity();
            String kpiInfo = MessageUtil.getMessageDesc("LOLEV_ACK051");
            ParamUtil.setSessionAttr(bpc.request, "kpiInfo", kpiInfo);

            onlineEnquiriesService.getInspReport(bpc,appCorrId,licAppCorrelationDto.getLicenceId());
        }else {
            licenceDto.setStatus("-");
            licenceDto.setLicenceNo("-");
        }
        ParamUtil.setSessionAttr(bpc.request, "licenceDto", licenceDto);


    }

    public void back(BaseProcessClass bpc){


    }
}
