package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.BeDashboardConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppStageSlaTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.HcsaSvcKpiDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashAppDetailsQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashAssignMeAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashKpiPoolAjaxQuery;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashRenewAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashReplyAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashWaitApproveAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.DashWorkTeamAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranet.dashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.BeDashboardAjaxService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.MohHcsaBeDashboardService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryMainClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import com.ecquaria.cloud.moh.iais.service.client.IntraDashboardClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2021/4/19 10:26
 **/
@Service
@Slf4j
public class BeDashboardAjaxServiceImpl implements BeDashboardAjaxService {

    @Autowired
    private MohHcsaBeDashboardService mohHcsaBeDashboardService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private InspectionMainAssignTaskService inspectionMainAssignTaskService;

    @Autowired
    private ApplicationMainClient applicationMainClient;

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private HcsaConfigMainClient hcsaConfigMainClient;

    @Autowired
    private InspectionTaskMainClient inspectionTaskMainClient;

    @Autowired
    private IntraDashboardClient intraDashboardClient;

    @Autowired
    private OrganizationMainClient organizationMainClient;

    @Autowired
    private AppPremisesRoutingHistoryMainClient appPremisesRoutingHistoryMainClient;

    private static final String APPLICATION_NO = "APPLICATION_NO";

    private static final String GROUP_NO = "groupNo";

    private static final String T_ID = "T2.ID";

    private static final String APPCORRID_LIST = "appCorrId_list";

    private static final String DASH_FILTER_APPNO = "dashFilterAppNo";

    private static final String INTRA_DASHBOARD_QUERY = "intraDashboardQuery";

    private static final String SUCCESS = "Success";
    private static final String RESULT = "result";
    private static final String AJAXRESULT = "ajaxResult";
    private static final String TOTAL_NUMBER = "totalNumber";
    private static final String T4_GROUP_NO = "T4.GROUP_NO";
    private static final String T7_GROUP_NO = "T7.GROUP_NO";
    private static final String APP_PREM_ID = "APP_PREM_ID";
    private static final String T1_GROUP_NO = "T1.GROUP_NO";
    private static final String T7_REF_NO = "T7.REF_NO";
    private static final String GROUP_NO_IN = "groupNoIn";

    @Override
    public Map<String, Object> getCommonDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                       String actionValue, String dashFilterAppNo, HcsaTaskAssignDto hcsaTaskAssignDto, String hciAddress) {
        return getCommonDropdownResultAct(groupNo,null, loginContext, map, searchParamGroup, actionValue,
                dashFilterAppNo, hcsaTaskAssignDto, hciAddress);
    }

    @Override
    public Map<String, Object> getCommonDropdownResultOnce(ArrayList<String> groupNos, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                       String actionValue, String dashFilterAppNo, HcsaTaskAssignDto hcsaTaskAssignDto, String hciAddress) {
        return getCommonDropdownResultAct(null, groupNos, loginContext, map, searchParamGroup, actionValue,
                dashFilterAppNo, hcsaTaskAssignDto, hciAddress);
    }

    @Override
    public Map<String, Object> getKpiDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                    String switchAction, String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto,
                                                    String hciAddress) {
        return getKpiDropdownResultAct(groupNo, null, loginContext, map, searchParamGroup,
                switchAction, dashFilterAppNo, dashAppStatus, hcsaTaskAssignDto, hciAddress);
    }

    @Override
    public Map<String, Object> getKpiDropdownResultOnce(ArrayList<String> groupNos, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                    String switchAction, String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto,
                                                    String hciAddress) {
        return getKpiDropdownResultAct(null, groupNos, loginContext, map, searchParamGroup,
                switchAction, dashFilterAppNo, dashAppStatus, hcsaTaskAssignDto, hciAddress);
    }

    @Override
    public Map<String, Object> getAssignMeDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                         String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto, String hciAddress) {
        return getAssignMeDropdownResultAct(groupNo, null, loginContext, map, searchParamGroup,
                dashFilterAppNo, dashAppStatus, hcsaTaskAssignDto, hciAddress);
    }

    @Override
    public Map<String, Object> getAssignMeDropdownResultOnce(ArrayList<String> groupNos, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                         String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto, String hciAddress) {
        return getAssignMeDropdownResultAct(null, groupNos, loginContext, map, searchParamGroup,
                dashFilterAppNo, dashAppStatus, hcsaTaskAssignDto, hciAddress);
    }

    @Override
    public Map<String, Object> getWorkTeamDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                         String switchAction, String dashFilterAppNo, String dashCommonPoolStatus, String dashAppStatus,
                                                         HcsaTaskAssignDto hcsaTaskAssignDto, String hciAddress) {
        return getWorkTeamDropdownResultAct(groupNo, null, loginContext, map, searchParamGroup,
                switchAction, dashFilterAppNo, dashCommonPoolStatus, dashAppStatus, hcsaTaskAssignDto, hciAddress);
    }

    @Override
    public Map<String, Object> getWorkTeamDropdownResultOnce(ArrayList<String> groupNos, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                         String switchAction, String dashFilterAppNo, String dashCommonPoolStatus, String dashAppStatus,
                                                         HcsaTaskAssignDto hcsaTaskAssignDto, String hciAddress) {
        return getWorkTeamDropdownResultAct(null, groupNos, loginContext, map, searchParamGroup,
                switchAction, dashFilterAppNo, dashCommonPoolStatus, dashAppStatus, hcsaTaskAssignDto, hciAddress);
    }

    @Override
    public Map<String, Object> getRenewDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                      String switchAction, String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto,
                                                      String hciAddress) {
        return getRenewDropdownResultAct(groupNo, null, loginContext, map, searchParamGroup,
                switchAction, dashFilterAppNo, dashAppStatus, hcsaTaskAssignDto, hciAddress);
    }

    @Override
    public Map<String, Object> getRenewDropdownResultOnce(ArrayList<String> groupNos, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                      String switchAction, String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto,
                                                      String hciAddress) {
        return getRenewDropdownResultAct(null, groupNos, loginContext, map, searchParamGroup,
                switchAction, dashFilterAppNo, dashAppStatus, hcsaTaskAssignDto, hciAddress);
    }

    @Override
    public Map<String, Object> getReplyDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                      String switchAction, String dashFilterAppNo, HcsaTaskAssignDto hcsaTaskAssignDto, String hciAddress) {
        return getReplyDropdownResultAct(groupNo, null, loginContext, map, searchParamGroup,
                switchAction, dashFilterAppNo, hcsaTaskAssignDto, hciAddress);
    }

    @Override
    public Map<String, Object> getReplyDropdownResultOnce(ArrayList<String> groupNos, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                      String switchAction, String dashFilterAppNo, HcsaTaskAssignDto hcsaTaskAssignDto, String hciAddress) {
        return getReplyDropdownResultAct(null, groupNos, loginContext, map, searchParamGroup,
                switchAction, dashFilterAppNo, hcsaTaskAssignDto, hciAddress);
    }

    @Override
    public Map<String, Object> getWaitApproveDropResult(String groupNo, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                        String switchAction, String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto,
                                                        String hciAddress) {
        return getWaitApproveDropResultAct(groupNo, null, loginContext, map, searchParamGroup,
                switchAction, dashFilterAppNo, dashAppStatus, hcsaTaskAssignDto, hciAddress);
    }

    @Override
    public Map<String, Object> getWaitApproveDropResultOnce(ArrayList<String> groupNos, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                        String switchAction, String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto,
                                                        String hciAddress) {
        return getWaitApproveDropResultAct(null, groupNos, loginContext, map, searchParamGroup,
                switchAction, dashFilterAppNo, dashAppStatus, hcsaTaskAssignDto, hciAddress);
    }

    private List<DashWaitApproveAjaxQueryDto> setWaitApproveAjaxDataToShow(List<DashWaitApproveAjaxQueryDto> dashWaitApproveAjaxQueryDtos,
               HcsaTaskAssignDto hcsaTaskAssignDto, Map<String, AppGrpPremisesDto> premMap, Map<String, LicenceDto> licMap) {
        if(!IaisCommonUtils.isEmpty(dashWaitApproveAjaxQueryDtos)){
            for(DashWaitApproveAjaxQueryDto dashWaitApproveAjaxQueryDto : dashWaitApproveAjaxQueryDtos){
                //get hciName / address
                AppGrpPremisesDto appGrpPremisesDto = premMap.get(dashWaitApproveAjaxQueryDto.getAppPremId());
                String address = inspectionMainAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    dashWaitApproveAjaxQueryDto.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    dashWaitApproveAjaxQueryDto.setHciAddress(StringUtil.viewHtml(address));
                }
                //app status
                dashWaitApproveAjaxQueryDto.setAppStatusStrShow(MasterCodeUtil.getCodeDesc(dashWaitApproveAjaxQueryDto.getAppStatus()));
                //service
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(dashWaitApproveAjaxQueryDto.getServiceId());
                dashWaitApproveAjaxQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                dashWaitApproveAjaxQueryDto.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getHciCode()));
                //get license date
                if(StringUtil.isEmpty(dashWaitApproveAjaxQueryDto.getOriginLicenceId())){
                    dashWaitApproveAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = licMap.get(dashWaitApproveAjaxQueryDto.getOriginLicenceId());
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    if(licExpiryDate != null) {
                        dashWaitApproveAjaxQueryDto.setLicenceExpiryDate(licExpiryDate);
                        String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                        dashWaitApproveAjaxQueryDto.setLicenceExpiryDateStr(licExpiryDateStr);
                    } else {
                        dashWaitApproveAjaxQueryDto.setLicenceExpiryDate(null);
                        dashWaitApproveAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    }
                }
            }
        }
        return dashWaitApproveAjaxQueryDtos;
    }

    private List<DashReplyAjaxQueryDto> setReplyAjaxDataToShow(List<DashReplyAjaxQueryDto> dashReplyAjaxQueryDtos,
            HcsaTaskAssignDto hcsaTaskAssignDto, Map<String, AppGrpPremisesDto> premMap, Map<String, LicenceDto> licMap) {
        if(!IaisCommonUtils.isEmpty(dashReplyAjaxQueryDtos)){
            for(DashReplyAjaxQueryDto dashReplyAjaxQueryDto : dashReplyAjaxQueryDtos){
                //get hciName / address
                AppGrpPremisesDto appGrpPremisesDto = premMap.get(dashReplyAjaxQueryDto.getAppPremId());
                String address = inspectionMainAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    dashReplyAjaxQueryDto.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    dashReplyAjaxQueryDto.setHciAddress(StringUtil.viewHtml(address));
                }
                //app status
                dashReplyAjaxQueryDto.setAppStatusStrShow(MasterCodeUtil.getCodeDesc(dashReplyAjaxQueryDto.getAppStatus()));
                //service
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(dashReplyAjaxQueryDto.getServiceId());
                dashReplyAjaxQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                dashReplyAjaxQueryDto.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getHciCode()));
                //get license date
                if(StringUtil.isEmpty(dashReplyAjaxQueryDto.getOriginLicenceId())){
                    dashReplyAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = licMap.get(dashReplyAjaxQueryDto.getOriginLicenceId());
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    if(licExpiryDate != null) {
                        dashReplyAjaxQueryDto.setLicenceExpiryDate(licExpiryDate);
                        String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                        dashReplyAjaxQueryDto.setLicenceExpiryDateStr(licExpiryDateStr);
                    } else {
                        dashReplyAjaxQueryDto.setLicenceExpiryDate(null);
                        dashReplyAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    }
                }
            }
        }
        return dashReplyAjaxQueryDtos;
    }

    private List<DashRenewAjaxQueryDto> setRenewAjaxDataToShow(List<DashRenewAjaxQueryDto> dashRenewAjaxQueryDtos,
           HcsaTaskAssignDto hcsaTaskAssignDto, Map<String, AppGrpPremisesDto> premMap, Map<String, LicenceDto> licMap) {
        if(!IaisCommonUtils.isEmpty(dashRenewAjaxQueryDtos)){
            for(DashRenewAjaxQueryDto dashRenewAjaxQueryDto : dashRenewAjaxQueryDtos){
                //get hciName / address
                AppGrpPremisesDto appGrpPremisesDto = premMap.get(dashRenewAjaxQueryDto.getAppPremId());
                String address = inspectionMainAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    dashRenewAjaxQueryDto.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    dashRenewAjaxQueryDto.setHciAddress(StringUtil.viewHtml(address));
                }
                //app status
                dashRenewAjaxQueryDto.setAppStatusStrShow(MasterCodeUtil.getCodeDesc(dashRenewAjaxQueryDto.getAppStatus()));
                //service
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(dashRenewAjaxQueryDto.getServiceId());
                dashRenewAjaxQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                dashRenewAjaxQueryDto.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getHciCode()));
                //get license date
                if(StringUtil.isEmpty(dashRenewAjaxQueryDto.getOriginLicenceId())){
                    dashRenewAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = licMap.get(dashRenewAjaxQueryDto.getOriginLicenceId());
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    if(licExpiryDate != null) {
                        dashRenewAjaxQueryDto.setLicenceExpiryDate(licExpiryDate);
                        String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                        dashRenewAjaxQueryDto.setLicenceExpiryDateStr(licExpiryDateStr);
                    } else {
                        dashRenewAjaxQueryDto.setLicenceExpiryDate(null);
                        dashRenewAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    }
                }
            }
        }
        return dashRenewAjaxQueryDtos;
    }

    private List<DashWorkTeamAjaxQueryDto> setWorkTeamAjaxDataToShow(List<DashWorkTeamAjaxQueryDto> dashWorkTeamAjaxQueryDtos,
             HcsaTaskAssignDto hcsaTaskAssignDto, Map<String, AppGrpPremisesDto> premMap, Map<String, LicenceDto> licMap) {
        if(!IaisCommonUtils.isEmpty(dashWorkTeamAjaxQueryDtos)){
            for(DashWorkTeamAjaxQueryDto dashWorkTeamAjaxQueryDto : dashWorkTeamAjaxQueryDtos){
                //get hciName / address
                AppGrpPremisesDto appGrpPremisesDto = premMap.get(dashWorkTeamAjaxQueryDto.getAppPremId());
                String address = inspectionMainAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    dashWorkTeamAjaxQueryDto.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    dashWorkTeamAjaxQueryDto.setHciAddress(StringUtil.viewHtml(address));
                }
                //app status
                dashWorkTeamAjaxQueryDto.setAppStatusStrShow(MasterCodeUtil.getCodeDesc(dashWorkTeamAjaxQueryDto.getAppStatus()));
                //service
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(dashWorkTeamAjaxQueryDto.getServiceId());
                dashWorkTeamAjaxQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                dashWorkTeamAjaxQueryDto.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getHciCode()));
                //get license date
                if(StringUtil.isEmpty(dashWorkTeamAjaxQueryDto.getOriginLicenceId())){
                    dashWorkTeamAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = licMap.get(dashWorkTeamAjaxQueryDto.getOriginLicenceId());
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    if(licExpiryDate != null) {
                        dashWorkTeamAjaxQueryDto.setLicenceExpiryDate(licExpiryDate);
                        String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                        dashWorkTeamAjaxQueryDto.setLicenceExpiryDateStr(licExpiryDateStr);
                    } else {
                        dashWorkTeamAjaxQueryDto.setLicenceExpiryDate(null);
                        dashWorkTeamAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    }
                }
            }
        }
        return dashWorkTeamAjaxQueryDtos;
    }

    private List<DashAssignMeAjaxQueryDto> setAssignMeAjaxDataToShow(List<DashAssignMeAjaxQueryDto> dashAssignMeAjaxQueryDtos,
            HcsaTaskAssignDto hcsaTaskAssignDto, Map<String, AppGrpPremisesDto> premMap, Map<String, LicenceDto> licMap) {
        if(!IaisCommonUtils.isEmpty(dashAssignMeAjaxQueryDtos)){
            for(DashAssignMeAjaxQueryDto dashAssignMeAjaxQueryDto : dashAssignMeAjaxQueryDtos){
                //get hciName / address
                AppGrpPremisesDto appGrpPremisesDto = premMap.get(dashAssignMeAjaxQueryDto.getAppPremId());
                String address = inspectionMainAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    dashAssignMeAjaxQueryDto.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    dashAssignMeAjaxQueryDto.setHciAddress(StringUtil.viewHtml(address));
                }
                //app status
                dashAssignMeAjaxQueryDto.setAppStatusStrShow(MasterCodeUtil.getCodeDesc(dashAssignMeAjaxQueryDto.getAppStatus()));
                //service
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(dashAssignMeAjaxQueryDto.getServiceId());
                dashAssignMeAjaxQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                dashAssignMeAjaxQueryDto.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getReuseHciCode()));
                //get license date
                if(StringUtil.isEmpty(dashAssignMeAjaxQueryDto.getOriginLicenceId())){
                    dashAssignMeAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = licMap.get(dashAssignMeAjaxQueryDto.getOriginLicenceId());
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    if(licExpiryDate != null) {
                        dashAssignMeAjaxQueryDto.setLicenceExpiryDate(licExpiryDate);
                        String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                        dashAssignMeAjaxQueryDto.setLicenceExpiryDateStr(licExpiryDateStr);
                    } else {
                        dashAssignMeAjaxQueryDto.setLicenceExpiryDate(null);
                        dashAssignMeAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    }
                }
            }
        }
        return dashAssignMeAjaxQueryDtos;
    }

    private SearchParam filterPageConditions(SearchParam searchParam, SearchParam searchParamGroup, String appStatusKey, String applicationStatus,
                                             HcsaTaskAssignDto hcsaTaskAssignDto, String fieldName, String hciAddress) {
        if(searchParamGroup != null) {
            Map<String, Object> filters = searchParamGroup.getFilters();
            if(filters != null) {
                String applicationType = (String)filters.get("application_type");
                String hciCode = (String)filters.get("hci_code");
                String hciName = (String)filters.get("hci_name");
                if(!StringUtil.isEmpty(applicationType)) {
                    searchParam.addFilter("application_type", applicationType, true);
                }
                if(!StringUtil.isEmpty(applicationStatus)) {
                    List<String> appStatus = mohHcsaBeDashboardService.getSearchAppStatus(applicationStatus);
                    String appStatusStr = SqlHelper.constructInCondition(appStatusKey, appStatus.size());
                    searchParam.addParam("application_status", appStatusStr);
                    for(int i = 0; i < appStatus.size(); i++) {
                        searchParam.addFilter(appStatusKey + i, appStatus.get(i));
                    }
                }
                if(!StringUtil.isEmpty(hciCode)) {
                    searchParam.addFilter("hci_code", hciCode, true);
                }
                if(!StringUtil.isEmpty(hciName)) {
                    searchParam.addFilter("hci_name", hciName, true);
                }
                if(!StringUtil.isEmpty(hciAddress)) {
                    searchParam = mohHcsaBeDashboardService.setAppPremisesIdsByUnitNos(searchParam, hciAddress, hcsaTaskAssignDto, fieldName, "appPremId_list");
                }
            }
        }
        return searchParam;
    }

    private List<DashKpiPoolAjaxQuery> setKpiPoolAjaxDataToShow(List<DashKpiPoolAjaxQuery> dashKpiPoolAjaxQueryList,
            HcsaTaskAssignDto hcsaTaskAssignDto, Map<String, AppGrpPremisesDto> premMap, Map<String, LicenceDto> licMap) {
        if(!IaisCommonUtils.isEmpty(dashKpiPoolAjaxQueryList)){
            for(DashKpiPoolAjaxQuery dashKpiPoolAjaxQuery : dashKpiPoolAjaxQueryList){
                //get hciName / address
                AppGrpPremisesDto appGrpPremisesDto = premMap.get(dashKpiPoolAjaxQuery.getAppPremId());
                String address = inspectionMainAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    dashKpiPoolAjaxQuery.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    dashKpiPoolAjaxQuery.setHciAddress(StringUtil.viewHtml(address));
                }
                //app status
                dashKpiPoolAjaxQuery.setAppStatusStrShow(MasterCodeUtil.getCodeDesc(dashKpiPoolAjaxQuery.getAppStatus()));
                //service
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(dashKpiPoolAjaxQuery.getServiceId());
                dashKpiPoolAjaxQuery.setServiceName(hcsaServiceDto.getSvcName());
                dashKpiPoolAjaxQuery.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getHciCode()));
                //get license date
                if(StringUtil.isEmpty(dashKpiPoolAjaxQuery.getOriginLicenceId())){
                    dashKpiPoolAjaxQuery.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = licMap.get(dashKpiPoolAjaxQuery.getOriginLicenceId());
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    if(licExpiryDate != null) {
                        dashKpiPoolAjaxQuery.setLicenceExpiryDate(licExpiryDate);
                        String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                        dashKpiPoolAjaxQuery.setLicenceExpiryDateStr(licExpiryDateStr);
                    } else {
                        dashKpiPoolAjaxQuery.setLicenceExpiryDate(null);
                        dashKpiPoolAjaxQuery.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    }
                }
            }
        }
        return dashKpiPoolAjaxQueryList;
    }

    private List<DashComPoolAjaxQueryDto> setComPoolAjaxDataToShow(List<DashComPoolAjaxQueryDto> dashComPoolAjaxQueryDtos,
            HcsaTaskAssignDto hcsaTaskAssignDto, Map<String, AppGrpPremisesDto> premMap, Map<String, LicenceDto> licMap) {
        if(!IaisCommonUtils.isEmpty(dashComPoolAjaxQueryDtos)){
            for(DashComPoolAjaxQueryDto dashComPoolAjaxQueryDto : dashComPoolAjaxQueryDtos){
                //get hciName / address
                AppGrpPremisesDto appGrpPremisesDto = premMap.get(dashComPoolAjaxQueryDto.getAppPremId());
                String address = inspectionMainAssignTaskService.getAddress(appGrpPremisesDto, hcsaTaskAssignDto);
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    dashComPoolAjaxQueryDto.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    dashComPoolAjaxQueryDto.setHciAddress(StringUtil.viewHtml(address));
                }
                //app status
                dashComPoolAjaxQueryDto.setAppStatusStrShow(MasterCodeUtil.getCodeDesc(ApplicationConsts.APPLICATION_STATUS_PENDING_TASK_ASSIGNMENT));
                //service
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(dashComPoolAjaxQueryDto.getServiceId());
                dashComPoolAjaxQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                dashComPoolAjaxQueryDto.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getHciCode()));
                //get license date
                if(StringUtil.isEmpty(dashComPoolAjaxQueryDto.getOriginLicenceId())){
                    dashComPoolAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = licMap.get(dashComPoolAjaxQueryDto.getOriginLicenceId());
                    Date licExpiryDate = licenceDto.getExpiryDate();
                    if(licExpiryDate != null) {
                        dashComPoolAjaxQueryDto.setLicenceExpiryDate(licExpiryDate);
                        String licExpiryDateStr = Formatter.formatDateTime(licExpiryDate, AppConsts.DEFAULT_DATE_FORMAT);
                        dashComPoolAjaxQueryDto.setLicenceExpiryDateStr(licExpiryDateStr);
                    } else {
                        dashComPoolAjaxQueryDto.setLicenceExpiryDate(null);
                        dashComPoolAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                    }
                }
            }
        }
        return dashComPoolAjaxQueryDtos;
    }

    @Override
    public String getKpiColorByTask(TaskDto taskDto) {
        String colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
        ApplicationDto applicationDto = inspectionTaskMainClient.getApplicationByCorreId(taskDto.getRefNo()).getEntity();
        if(applicationDto != null) {
            String stage;
            if (HcsaConsts.ROUTING_STAGE_INS.equals(taskDto.getTaskKey())) {
                AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =
                        appPremisesRoutingHistoryMainClient.getAppPremisesRoutingHistorySubStage(taskDto.getRefNo(), taskDto.getTaskKey()).getEntity();
                stage = appPremisesRoutingHistoryDto.getSubStage();
            } else {
                stage = taskDto.getTaskKey();
            }
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
            HcsaSvcKpiDto hcsaSvcKpiDto = hcsaConfigMainClient.searchKpiResult(hcsaServiceDto.getSvcCode(), applicationDto.getApplicationType()).getEntity();
            if (hcsaSvcKpiDto != null) {
                //get current stage worked days
                int days = 0;
                if (!StringUtil.isEmpty(stage)) {
                    AppStageSlaTrackingDto appStageSlaTrackingDto = inspectionTaskMainClient.getSlaTrackByAppNoStageId(applicationDto.getApplicationNo(), stage).getEntity();
                    if (appStageSlaTrackingDto != null) {
                        days = appStageSlaTrackingDto.getKpiSlaDays();
                    }
                }
                //get warning value
                Map<String, Integer> kpiMap = hcsaSvcKpiDto.getStageIdKpi();
                int kpi = 0;
                if (!StringUtil.isEmpty(stage) && (kpiMap != null && kpiMap.get(stage) != null)) {
                    kpi = kpiMap.get(stage);
                }
                //get threshold value
                int remThreshold = 0;
                if (hcsaSvcKpiDto.getRemThreshold() != null) {
                    remThreshold = hcsaSvcKpiDto.getRemThreshold();
                }
                //get color
                colour = getColorByWorkAndKpiDay(kpi, days, remThreshold);
            }
        }
        return colour;
    }

    private String getColorByWorkAndKpiDay(int kpi, int days, int remThreshold) {
        String colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
        if(remThreshold != 0) {
            if (days < remThreshold) {
                colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
            }
            if (kpi != 0) {
                if (remThreshold <= days && days <= kpi) {
                    colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_AMBER;
                } else if (days > kpi) {
                    colour = HcsaConsts.PERFORMANCE_TIME_COLOUR_RED;
                }
            }
        }
        return colour;
    }

    @SearchTrack(catalog = "intraDashboardQuery", key = "dashCommonTaskAjax")
    private SearchResult<DashComPoolAjaxQueryDto> getCommonAjaxResultByParam(SearchParam searchParam) {
        return inspectionTaskMainClient.searchDashComPoolDropResult(searchParam).getEntity();
    }

    @SearchTrack(catalog = "intraDashboardQuery", key = "dashKpiTaskAjax")
    private SearchResult<DashKpiPoolAjaxQuery> getKpiAjaxResultByParam(SearchParam searchParam) {
        return intraDashboardClient.searchDashKpiPoolDropResult(searchParam).getEntity();
    }

    @SearchTrack(catalog = "intraDashboardQuery", key = "dashAppRenewAjax")
    private SearchResult<DashRenewAjaxQueryDto> getRenewAjaxResultByParam(SearchParam searchParam) {
        return intraDashboardClient.searchDashRenewDropResult(searchParam).getEntity();
    }

    @SearchTrack(catalog = "intraDashboardQuery", key = "dashWaitApproveAjax")
    private SearchResult<DashWaitApproveAjaxQueryDto> getWaitAjaxResultByParam(SearchParam searchParam) {
        return intraDashboardClient.searchDashWaitApproveDropResult(searchParam).getEntity();
    }

    @SearchTrack(catalog = "intraDashboardQuery", key = "dashAppReplyAjax")
    private SearchResult<DashReplyAjaxQueryDto> getReplyAjaxResultByParam(SearchParam searchParam) {
        return intraDashboardClient.searchDashReplyPoolDropResult(searchParam).getEntity();
    }

    @Override
    @SearchTrack(catalog = "intraDashboardQuery", key = "dashSystemDetailAjax")
    public SearchResult<DashAppDetailsQueryDto> getDashAllActionResult(SearchParam searchParam) {
        return intraDashboardClient.searchDashAppDetailsResult(searchParam).getEntity();
    }

    @Override
    public SearchResult<DashAppDetailsQueryDto> setDashSysDetailsDropOtherData(SearchResult<DashAppDetailsQueryDto> searchResult) {
        if(searchResult != null) {
            List<DashAppDetailsQueryDto> dashAppDetailsQueryDtos = searchResult.getRows();
            if(!IaisCommonUtils.isEmpty(dashAppDetailsQueryDtos)) {
                for(DashAppDetailsQueryDto dashAppDetailsQueryDto : dashAppDetailsQueryDtos) {
                    if(dashAppDetailsQueryDto != null) {
                        //set app appType
                        String appType = dashAppDetailsQueryDto.getAppType();
                        dashAppDetailsQueryDto.setAppTypeStrShow(MasterCodeUtil.getCodeDesc(appType));
                        //set service name
                        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(dashAppDetailsQueryDto.getSvcCode());
                        if(hcsaServiceDto != null) {
                            dashAppDetailsQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                            //set all stage kpi
                            HcsaSvcKpiDto hcsaSvcKpiDto = hcsaConfigMainClient.searchKpiResult(hcsaServiceDto.getSvcCode(), appType).getEntity();
                            dashAppDetailsQueryDto = setSumKpiByHcsaSvcKpiDto(hcsaSvcKpiDto, dashAppDetailsQueryDto);
                        }
                        //set kpi color
                        String color = HcsaConsts.PERFORMANCE_TIME_COLOUR_BLACK;
                        List<TaskDto> taskDtos = organizationMainClient.getCurrTaskByRefNo(dashAppDetailsQueryDto.getId()).getEntity();
                        if(!IaisCommonUtils.isEmpty(taskDtos)) {
                            TaskDto taskDto = taskDtos.get(0);
                            color = getKpiColorByTask(taskDto);
                        }
                        dashAppDetailsQueryDto.setKpiColor(color);
                        //set officer's name
                        List<String> appOwnerList = IaisCommonUtils.genNewArrayList();
                        if(IaisCommonUtils.isEmpty(taskDtos)) {
                            appOwnerList.add("Pending Assignment");
                        } else {
                            for(TaskDto taskDto : taskDtos) {
                                if(taskDto != null && !StringUtil.isEmpty(taskDto.getUserId())) {
                                    OrgUserDto orgUserDto = organizationMainClient.retrieveOrgUserAccountById(taskDto.getUserId()).getEntity();
                                    if(orgUserDto != null) {
                                        appOwnerList.add(orgUserDto.getDisplayName());
                                    }
                                }
                            }
                            if(IaisCommonUtils.isEmpty(appOwnerList)) {
                                appOwnerList.add("Pending Assignment");
                            }
                        }
                        Collections.sort(appOwnerList);
                        dashAppDetailsQueryDto.setAppOwnerList(appOwnerList);
                    }
                }
            }
        }
        return searchResult;
    }

    private DashAppDetailsQueryDto setSumKpiByHcsaSvcKpiDto(HcsaSvcKpiDto hcsaSvcKpiDto, DashAppDetailsQueryDto dashAppDetailsQueryDto) {
        dashAppDetailsQueryDto.setAllStageSumKpi(0 + "");
        if(hcsaSvcKpiDto != null) {
            Map<String, Integer> stageIdKpi = hcsaSvcKpiDto.getStageIdKpi();
            if(stageIdKpi != null) {
                int sumKpi = 0;
                for(Map.Entry<String, Integer> map : stageIdKpi.entrySet()) {
                    Integer kpi = map.getValue();
                    if(kpi != null) {
                        sumKpi = sumKpi + kpi;
                    }
                }
                dashAppDetailsQueryDto.setAllStageSumKpi(sumKpi + "");
            }
        }
        return dashAppDetailsQueryDto;
    }

    @SearchTrack(catalog = "intraDashboardQuery", key = "dashAssignMeAjax")
    private SearchResult<DashAssignMeAjaxQueryDto> getAssignMeAjaxResultByParam(SearchParam searchParam) {
        return inspectionTaskMainClient.searchDashAssignMeAjaxResult(searchParam).getEntity();
    }

    @SearchTrack(catalog = "intraDashboardQuery", key = "dashSupervisorAjax")
    private SearchResult<DashWorkTeamAjaxQueryDto> getWorkTeamAjaxResultByParam(SearchParam searchParam) {
        return inspectionTaskMainClient.searchDashWorkTeamDropResult(searchParam).getEntity();
    }

    private List<String> getAppPremCorrIdsByDto(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos) {
        List<String> appPremCorrIds = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appPremisesCorrelationDtos)) {
            for (AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos) {
                if(appPremisesCorrelationDto != null) {
                    appPremCorrIds.add(appPremisesCorrelationDto.getId());
                }
            }
        }
        return appPremCorrIds;
    }

    private Map<String, Object> getCommonDropdownResultAct(String groupNo, ArrayList<String> appGrpNums, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                           String actionValue, String dashFilterAppNo, HcsaTaskAssignDto hcsaTaskAssignDto, String hciAddress) {
        SearchParam searchParam = new SearchParam(DashComPoolAjaxQueryDto.class.getName());
        searchParam.setPageNo(1);
        searchParam.setSort(APPLICATION_NO, SearchParam.ASCENDING);
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(groupNo) || !IaisCommonUtils.isEmpty(appGrpNums)) {
            searchParam.setPageSize(-1);
            //filter appGroup NO.
            if (IaisCommonUtils.isEmpty(appGrpNums)) {
                searchParam.addFilter(GROUP_NO, groupNo, true);
                appGrpNums = IaisCommonUtils.genNewArrayList(1);
                appGrpNums.add(groupNo);
            } else {
                searchParam.addParam(GROUP_NO_IN, SqlHelper.constructInCondition("T5.GROUP_NO", appGrpNums.size()));
                for (int i = 0; i < appGrpNums.size(); i++) {
                    searchParam.addFilter("T5.GROUP_NO" + i, appGrpNums.get(i));
                }
            }
            List<ApplicationGroupDto> applicationGroupDtos = applicationMainClient.getGroupsByNos(appGrpNums).getEntity();
            List<String> grpIds = IaisCommonUtils.genNewArrayList();
            for (ApplicationGroupDto appGrpDto : applicationGroupDtos) {
                grpIds.add(appGrpDto.getId());
            }
            Map<String, AppGrpPremisesDto> premMap = inspectionTaskMainClient.getGroupAppsByNos(grpIds).getEntity();
            //filter app Premises Correlation
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationMainClient.getPremCorrDtoByAppGroupIds(grpIds).getEntity();
            List<String> appCorrIdList = getAppPremCorrIdsByDto(appPremisesCorrelationDtos);
            String appPremCorrId = SqlHelper.constructInCondition(T_ID, appCorrIdList.size());
            searchParam.addParam(APPCORRID_LIST, appPremCorrId);
            for(int i = 0; i < appCorrIdList.size(); i++){
                searchParam.addFilter(T_ID + i, appCorrIdList.get(i));
            }
            //filter appNo
            if(!StringUtil.isEmpty(dashFilterAppNo)){
                searchParam.addFilter(DASH_FILTER_APPNO, dashFilterAppNo,true);
            }
            //filter page conditions
            searchParam = filterPageConditions(searchParam, searchParamGroup, null, null, hcsaTaskAssignDto,
                    "T5.APP_PREM_ID", hciAddress);
            //filter work groups
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, actionValue, workGroupIds);
            //search
            QueryHelp.setMainSql(INTRA_DASHBOARD_QUERY, "dashCommonTaskAjax", searchParam);
            SearchResult<DashComPoolAjaxQueryDto> ajaxResult = getCommonAjaxResultByParam(searchParam);
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            for (DashComPoolAjaxQueryDto queryDto : ajaxResult.getRows()) {
                if (!StringUtil.isEmpty(queryDto.getOriginLicenceId())) {
                    licIds.add(queryDto.getOriginLicenceId());
                }
            }
            Map<String, LicenceDto> licMap = licenceClient.getLicenceList(licIds).getEntity();
            //set other data
            setComPoolAjaxDataToShow(ajaxResult.getRows(), hcsaTaskAssignDto, premMap, licMap);
            map.put(RESULT, SUCCESS);
            map.put(AJAXRESULT, ajaxResult);
        } else {
            searchParam.setPageSize(1);
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, actionValue, workGroupIds);
            QueryHelp.setMainSql(INTRA_DASHBOARD_QUERY, "dashCommonTaskAjax", searchParam);
            SearchResult<DashComPoolAjaxQueryDto> ajaxResult = getCommonAjaxResultByParam(searchParam);
            map.put(TOTAL_NUMBER, ajaxResult.getRowCount());
        }
        return map;
    }

    private Map<String, Object> getAssignMeDropdownResultAct(String groupNo, ArrayList<String> appGrpNums, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                         String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto, String hciAddress) {
        SearchParam searchParam = new SearchParam(DashAssignMeAjaxQueryDto.class.getName());
        searchParam.setPageNo(1);
        searchParam.setSort(APPLICATION_NO, SearchParam.ASCENDING);
        if (!StringUtil.isEmpty(groupNo) || !IaisCommonUtils.isEmpty(appGrpNums)) {
            searchParam.setPageSize(-1);
            //filter appGroup NO.
            if (IaisCommonUtils.isEmpty(appGrpNums)) {
                searchParam.addFilter(GROUP_NO, groupNo, true);
                appGrpNums = IaisCommonUtils.genNewArrayList(1);
                appGrpNums.add(groupNo);
            } else {
                searchParam.addParam(GROUP_NO_IN, SqlHelper.constructInCondition(T4_GROUP_NO, appGrpNums.size()));
                for (int i = 0; i < appGrpNums.size(); i++) {
                    searchParam.addFilter(T4_GROUP_NO + i, appGrpNums.get(i));
                }
            }

            List<ApplicationGroupDto> applicationGroupDtos = applicationMainClient.getGroupsByNos(appGrpNums).getEntity();
            List<String> grpIds = IaisCommonUtils.genNewArrayList();
            for (ApplicationGroupDto appGrpDto : applicationGroupDtos) {
                grpIds.add(appGrpDto.getId());
            }
            Map<String, AppGrpPremisesDto> premMap = inspectionTaskMainClient.getGroupAppsByNos(grpIds).getEntity();
            //filter app Premises Correlation
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationMainClient.getPremCorrDtoByAppGroupIds(grpIds).getEntity();
            List<String> appCorrIdList = getAppPremCorrIdsByDto(appPremisesCorrelationDtos);
            String appPremCorrId = SqlHelper.constructInCondition(T_ID, appCorrIdList.size());
            searchParam.addParam(APPCORRID_LIST, appPremCorrId);
            for(int i = 0; i < appCorrIdList.size(); i++){
                searchParam.addFilter(T_ID + i, appCorrIdList.get(i));
            }
            //filter appNo
            if(!StringUtil.isEmpty(dashFilterAppNo)){
                searchParam.addFilter(DASH_FILTER_APPNO, dashFilterAppNo,true);
            }
            if(loginContext != null) {
                //role
                String curRoleId = loginContext.getCurRoleId();
                if (!StringUtil.isEmpty(curRoleId)) {
                    searchParam.addFilter("dashRoleId", curRoleId, true);
                }
                //user uuid
                String userId = loginContext.getUserId();
                if (!StringUtil.isEmpty(userId)) {
                    searchParam.addFilter("dashUserId", userId, true);
                }
            }
            //filter page conditions
            searchParam = filterPageConditions(searchParam, searchParamGroup, "T5.STATUS", dashAppStatus, hcsaTaskAssignDto,
                    "T5.APP_PREM_ID", hciAddress);
            //search
            QueryHelp.setMainSql(INTRA_DASHBOARD_QUERY, "dashAssignMeAjax", searchParam);
            SearchResult<DashAssignMeAjaxQueryDto> ajaxResult = getAssignMeAjaxResultByParam(searchParam);
            //set other data
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            for (DashAssignMeAjaxQueryDto queryDto : ajaxResult.getRows()) {
                if (!StringUtil.isEmpty(queryDto.getOriginLicenceId())) {
                    licIds.add(queryDto.getOriginLicenceId());
                }
            }
            Map<String, LicenceDto> licMap = licenceClient.getLicenceList(licIds).getEntity();
            setAssignMeAjaxDataToShow(ajaxResult.getRows(), hcsaTaskAssignDto, premMap, licMap);
            map.put(RESULT, SUCCESS);
            map.put(AJAXRESULT, ajaxResult);
        } else {
            searchParam.setPageSize(1);
            QueryHelp.setMainSql(INTRA_DASHBOARD_QUERY, "dashAssignMeAjax", searchParam);
            SearchResult<DashAssignMeAjaxQueryDto> ajaxResult = getAssignMeAjaxResultByParam(searchParam);
            map.put(TOTAL_NUMBER, ajaxResult.getRowCount());
        }
        return map;
    }

    private Map<String, Object> getReplyDropdownResultAct(String groupNo, ArrayList<String> appGrpNums, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                      String switchAction, String dashFilterAppNo, HcsaTaskAssignDto hcsaTaskAssignDto, String hciAddress) {
        SearchParam searchParam = new SearchParam(DashReplyAjaxQueryDto.class.getName());
        searchParam.setPageNo(1);
        searchParam.setSort(APPLICATION_NO, SearchParam.ASCENDING);
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(groupNo) || !IaisCommonUtils.isEmpty(appGrpNums)) {
            searchParam.setPageSize(-1);
            //filter appGroup NO.
            if (IaisCommonUtils.isEmpty(appGrpNums)) {
                searchParam.addFilter(GROUP_NO, groupNo, true);
                appGrpNums = IaisCommonUtils.genNewArrayList(1);
                appGrpNums.add(groupNo);
            } else {
                searchParam.addParam(GROUP_NO_IN, SqlHelper.constructInCondition(T7_GROUP_NO, appGrpNums.size()));
                for (int i = 0; i < appGrpNums.size(); i++) {
                    searchParam.addFilter(T7_GROUP_NO + i, appGrpNums.get(i));
                }
            }
            List<ApplicationGroupDto> applicationGroupDtos = applicationMainClient.getGroupsByNos(appGrpNums).getEntity();
            List<String> grpIds = IaisCommonUtils.genNewArrayList();
            for (ApplicationGroupDto appGrpDto : applicationGroupDtos) {
                grpIds.add(appGrpDto.getId());
            }
            Map<String, AppGrpPremisesDto> premMap = inspectionTaskMainClient.getGroupAppsByNos(grpIds).getEntity();
            //filter app Premises Correlation
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationMainClient.getPremCorrDtoByAppGroupIds(grpIds).getEntity();
            List<String> appCorrIdList = getAppPremCorrIdsByDto(appPremisesCorrelationDtos);
            String appPremCorrId = SqlHelper.constructInCondition("T7.ID", appCorrIdList.size());
            searchParam.addParam(APPCORRID_LIST, appPremCorrId);
            for(int i = 0; i < appCorrIdList.size(); i++){
                searchParam.addFilter("T7.ID" + i, appCorrIdList.get(i));
            }
            //filter appNo
            if(!StringUtil.isEmpty(dashFilterAppNo)){
                searchParam.addFilter(DASH_FILTER_APPNO, dashFilterAppNo,true);
            }
            //filter page conditions
            searchParam = filterPageConditions(searchParam, searchParamGroup, null, null, hcsaTaskAssignDto,
                    APP_PREM_ID, hciAddress);
            //filter work groups
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            //search
            QueryHelp.setMainSql(INTRA_DASHBOARD_QUERY, "dashAppReplyAjax", searchParam);
            SearchResult<DashReplyAjaxQueryDto> ajaxResult = getReplyAjaxResultByParam(searchParam);
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            for (DashReplyAjaxQueryDto queryDto : ajaxResult.getRows()) {
                if (!StringUtil.isEmpty(queryDto.getOriginLicenceId())) {
                    licIds.add(queryDto.getOriginLicenceId());
                }
            }
            Map<String, LicenceDto> licMap = licenceClient.getLicenceList(licIds).getEntity();
            //set other data
            setReplyAjaxDataToShow(ajaxResult.getRows(), hcsaTaskAssignDto, premMap, licMap);
            map.put(RESULT, SUCCESS);
            map.put(AJAXRESULT, ajaxResult);
        } else {
            searchParam.setPageSize(1);
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            QueryHelp.setMainSql(INTRA_DASHBOARD_QUERY, "dashAppReplyAjax", searchParam);
            SearchResult<DashReplyAjaxQueryDto> ajaxResult = getReplyAjaxResultByParam(searchParam);
            map.put(TOTAL_NUMBER, ajaxResult.getRowCount());
        }
        return map;
    }

    private Map<String, Object> getKpiDropdownResultAct(String groupNo, ArrayList<String> appGrpNums, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                    String switchAction, String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto,
                                                    String hciAddress) {
        SearchParam searchParam = new SearchParam(DashKpiPoolAjaxQuery.class.getName());
        searchParam.setPageNo(1);
        searchParam.setSort(APPLICATION_NO, SearchParam.ASCENDING);
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(groupNo) || !IaisCommonUtils.isEmpty(appGrpNums)) {
            searchParam.setPageSize(-1);
            //filter appGroup NO.
            if (IaisCommonUtils.isEmpty(appGrpNums)) {
                searchParam.addFilter(GROUP_NO, groupNo, true);
                appGrpNums = IaisCommonUtils.genNewArrayList(1);
                appGrpNums.add(groupNo);
            } else {
                searchParam.addParam(GROUP_NO_IN, SqlHelper.constructInCondition(T1_GROUP_NO, appGrpNums.size()));
                for (int i = 0; i < appGrpNums.size(); i++) {
                    searchParam.addFilter(T1_GROUP_NO + i, appGrpNums.get(i));
                }
            }
            List<ApplicationGroupDto> applicationGroupDtos = applicationMainClient.getGroupsByNos(appGrpNums).getEntity();
            List<String> grpIds = IaisCommonUtils.genNewArrayList();
            for (ApplicationGroupDto appGrpDto : applicationGroupDtos) {
                grpIds.add(appGrpDto.getId());
            }
            Map<String, AppGrpPremisesDto> premMap = inspectionTaskMainClient.getGroupAppsByNos(grpIds).getEntity();
            //filter app Premises Correlation
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationMainClient.getPremCorrDtoByAppGroupIds(grpIds).getEntity();
            List<String> appCorrIdList = getAppPremCorrIdsByDto(appPremisesCorrelationDtos);
            String appPremCorrId = SqlHelper.constructInCondition(T7_REF_NO, appCorrIdList.size());
            searchParam.addParam(APPCORRID_LIST, appPremCorrId);
            for(int i = 0; i < appCorrIdList.size(); i++){
                searchParam.addFilter(T7_REF_NO + i, appCorrIdList.get(i));
            }
            //filter appNo
            if(!StringUtil.isEmpty(dashFilterAppNo)){
                searchParam.addFilter(DASH_FILTER_APPNO, dashFilterAppNo,true);
            }
            //filter page conditions
            searchParam = filterPageConditions(searchParam, searchParamGroup, "T1.STATUS", dashAppStatus, hcsaTaskAssignDto,
                    APP_PREM_ID, hciAddress);
            //filter work groups
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            //search
            QueryHelp.setMainSql(INTRA_DASHBOARD_QUERY, "dashKpiTaskAjax", searchParam);
            SearchResult<DashKpiPoolAjaxQuery> ajaxResult = getKpiAjaxResultByParam(searchParam);
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            for (DashKpiPoolAjaxQuery queryDto : ajaxResult.getRows()) {
                if (!StringUtil.isEmpty(queryDto.getOriginLicenceId())) {
                    licIds.add(queryDto.getOriginLicenceId());
                }
            }
            Map<String, LicenceDto> licMap = licenceClient.getLicenceList(licIds).getEntity();
            //set other data
            setKpiPoolAjaxDataToShow(ajaxResult.getRows(), hcsaTaskAssignDto, premMap, licMap);
            map.put(RESULT, SUCCESS);
            map.put(AJAXRESULT, ajaxResult);
        } else {
            searchParam.setPageSize(1);
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            QueryHelp.setMainSql(INTRA_DASHBOARD_QUERY, "dashKpiTaskAjax", searchParam);
            SearchResult<DashKpiPoolAjaxQuery> ajaxResult = getKpiAjaxResultByParam(searchParam);
            map.put(TOTAL_NUMBER, ajaxResult.getRowCount());
        }
        return map;
    }

    private Map<String, Object> getRenewDropdownResultAct(String groupNo, ArrayList<String> appGrpNums, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                      String switchAction, String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto,
                                                      String hciAddress) {
        SearchParam searchParam = new SearchParam(DashRenewAjaxQueryDto.class.getName());
        searchParam.setPageNo(1);
        searchParam.setSort(APPLICATION_NO, SearchParam.ASCENDING);
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        //licence expire days
        if(BeDashboardConstant.SWITCH_ACTION_RE_RENEW.equals(switchAction)) {
            searchParam.addFilter("lic_renew_exp", systemParamConfig.getDashRenewDate(), true);
        }
        if (!StringUtil.isEmpty(groupNo) || !IaisCommonUtils.isEmpty(appGrpNums)) {
            searchParam.setPageSize(-1);
            //filter appGroup NO.
            if (IaisCommonUtils.isEmpty(appGrpNums)) {
                searchParam.addFilter(GROUP_NO, groupNo, true);
                appGrpNums = IaisCommonUtils.genNewArrayList(1);
                appGrpNums.add(groupNo);
            } else {
                searchParam.addParam(GROUP_NO_IN, SqlHelper.constructInCondition(T1_GROUP_NO, appGrpNums.size()));
                for (int i = 0; i < appGrpNums.size(); i++) {
                    searchParam.addFilter(T1_GROUP_NO + i, appGrpNums.get(i));
                }
            }
            List<ApplicationGroupDto> applicationGroupDtos = applicationMainClient.getGroupsByNos(appGrpNums).getEntity();
            List<String> grpIds = IaisCommonUtils.genNewArrayList();
            for (ApplicationGroupDto appGrpDto : applicationGroupDtos) {
                grpIds.add(appGrpDto.getId());
            }
            Map<String, AppGrpPremisesDto> premMap = inspectionTaskMainClient.getGroupAppsByNos(grpIds).getEntity();
            //filter app Premises Correlation
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationMainClient.getPremCorrDtoByAppGroupIds(grpIds).getEntity();
            List<String> appCorrIdList = getAppPremCorrIdsByDto(appPremisesCorrelationDtos);
            String appPremCorrId = SqlHelper.constructInCondition(T7_REF_NO, appCorrIdList.size());
            searchParam.addParam(APPCORRID_LIST, appPremCorrId);
            for(int i = 0; i < appCorrIdList.size(); i++){
                searchParam.addFilter(T7_REF_NO + i, appCorrIdList.get(i));
            }
            //filter appNo
            if(!StringUtil.isEmpty(dashFilterAppNo)){
                searchParam.addFilter(DASH_FILTER_APPNO, dashFilterAppNo,true);
            }

            //filter page conditions
            searchParam = filterPageConditions(searchParam, searchParamGroup, "T1.STATUS", dashAppStatus, hcsaTaskAssignDto,
                    APP_PREM_ID, hciAddress);
            //filter work groups
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            //search
            QueryHelp.setMainSql(INTRA_DASHBOARD_QUERY, "dashAppRenewAjax", searchParam);
            SearchResult<DashRenewAjaxQueryDto> ajaxResult = getRenewAjaxResultByParam(searchParam);
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            for (DashRenewAjaxQueryDto queryDto : ajaxResult.getRows()) {
                if (!StringUtil.isEmpty(queryDto.getOriginLicenceId())) {
                    licIds.add(queryDto.getOriginLicenceId());
                }
            }
            Map<String, LicenceDto> licMap = licenceClient.getLicenceList(licIds).getEntity();
            //set other data
            setRenewAjaxDataToShow(ajaxResult.getRows(), hcsaTaskAssignDto, premMap, licMap);
            map.put(RESULT, SUCCESS);
            map.put(AJAXRESULT, ajaxResult);
        } else {
            searchParam.setPageSize(1);
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            QueryHelp.setMainSql(INTRA_DASHBOARD_QUERY, "dashAppRenewAjax", searchParam);
            SearchResult<DashRenewAjaxQueryDto> ajaxResult = getRenewAjaxResultByParam(searchParam);
            map.put(TOTAL_NUMBER, ajaxResult.getRowCount());
        }
        return map;
    }

    private Map<String, Object> getWaitApproveDropResultAct(String groupNo, ArrayList<String> appGrpNums, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                        String switchAction, String dashFilterAppNo, String dashAppStatus, HcsaTaskAssignDto hcsaTaskAssignDto,
                                                        String hciAddress) {
        SearchParam searchParam = new SearchParam(DashWaitApproveAjaxQueryDto.class.getName());
        int pageSize=SystemParamUtil.getDefaultPageSize();
        searchParam.setPageSize(pageSize);
        searchParam.setPageNo(1);
        searchParam.setSort(APPLICATION_NO, SearchParam.ASCENDING);
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(groupNo) || !IaisCommonUtils.isEmpty(appGrpNums)) {
            searchParam.setPageSize(-1);
            //filter appGroup NO.
            if (IaisCommonUtils.isEmpty(appGrpNums)) {
                searchParam.addFilter(GROUP_NO, groupNo, true);
                appGrpNums = IaisCommonUtils.genNewArrayList(1);
                appGrpNums.add(groupNo);
            } else {
                searchParam.addParam(GROUP_NO_IN, SqlHelper.constructInCondition(T7_GROUP_NO, appGrpNums.size()));
                for (int i = 0; i < appGrpNums.size(); i++) {
                    searchParam.addFilter(T7_GROUP_NO + i, appGrpNums.get(i));
                }
            }
            List<ApplicationGroupDto> applicationGroupDtos = applicationMainClient.getGroupsByNos(appGrpNums).getEntity();
            List<String> grpIds = IaisCommonUtils.genNewArrayList();
            for (ApplicationGroupDto appGrpDto : applicationGroupDtos) {
                grpIds.add(appGrpDto.getId());
            }
            Map<String, AppGrpPremisesDto> premMap = inspectionTaskMainClient.getGroupAppsByNos(grpIds).getEntity();
            //filter app Premises Correlation
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationMainClient.getPremCorrDtoByAppGroupIds(grpIds).getEntity();
            List<String> appCorrIdList = getAppPremCorrIdsByDto(appPremisesCorrelationDtos);
            String appPremCorrId = SqlHelper.constructInCondition("T1.REF_NO", appCorrIdList.size());
            searchParam.addParam(APPCORRID_LIST, appPremCorrId);
            for(int i = 0; i < appCorrIdList.size(); i++){
                searchParam.addFilter("T1.REF_NO" + i, appCorrIdList.get(i));
            }
            //filter appNo
            if(!StringUtil.isEmpty(dashFilterAppNo)){
                searchParam.addFilter(DASH_FILTER_APPNO, dashFilterAppNo,true);
            }
            //filter page conditions
            searchParam = filterPageConditions(searchParam, searchParamGroup, "T7.STATUS", dashAppStatus, hcsaTaskAssignDto,
                    APP_PREM_ID, hciAddress);
            //filter work groups
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            //search
            QueryHelp.setMainSql(INTRA_DASHBOARD_QUERY, "dashWaitApproveAjax", searchParam);
            SearchResult<DashWaitApproveAjaxQueryDto> ajaxResult = getWaitAjaxResultByParam(searchParam);
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            for (DashWaitApproveAjaxQueryDto queryDto : ajaxResult.getRows()) {
                if (!StringUtil.isEmpty(queryDto.getOriginLicenceId())) {
                    licIds.add(queryDto.getOriginLicenceId());
                }
            }
            Map<String, LicenceDto> licMap = licenceClient.getLicenceList(licIds).getEntity();
            //set other data
            setWaitApproveAjaxDataToShow(ajaxResult.getRows(), hcsaTaskAssignDto, premMap, licMap);
            map.put(RESULT, SUCCESS);
            map.put(AJAXRESULT, ajaxResult);
        } else {
            searchParam.setPageSize(1);
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            QueryHelp.setMainSql(INTRA_DASHBOARD_QUERY, "dashWaitApproveAjax", searchParam);
            SearchResult<DashWaitApproveAjaxQueryDto> ajaxResult = getWaitAjaxResultByParam(searchParam);
            map.put(TOTAL_NUMBER, ajaxResult.getRowCount());
        }
        return map;
    }

    private Map<String, Object> getWorkTeamDropdownResultAct(String groupNo, ArrayList<String> appGrpNums, LoginContext loginContext, Map<String, Object> map, SearchParam searchParamGroup,
                                                         String switchAction, String dashFilterAppNo, String dashCommonPoolStatus, String dashAppStatus,
                                                         HcsaTaskAssignDto hcsaTaskAssignDto, String hciAddress) {
        SearchParam searchParam = new SearchParam(DashWorkTeamAjaxQueryDto.class.getName());
        searchParam.setPageNo(1);
        searchParam.setSort(APPLICATION_NO, SearchParam.ASCENDING);
        List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(groupNo) || !IaisCommonUtils.isEmpty(appGrpNums)) {
            searchParam.setPageSize(-1);
            //filter appGroup NO.
            if (IaisCommonUtils.isEmpty(appGrpNums)) {
                searchParam.addFilter(GROUP_NO, groupNo, true);
                appGrpNums = IaisCommonUtils.genNewArrayList(1);
                appGrpNums.add(groupNo);
            } else {
                searchParam.addParam(GROUP_NO_IN, SqlHelper.constructInCondition(T4_GROUP_NO, appGrpNums.size()));
                for (int i = 0; i < appGrpNums.size(); i++) {
                    searchParam.addFilter(T4_GROUP_NO + i, appGrpNums.get(i));
                }
            }
            List<ApplicationGroupDto> applicationGroupDtos = applicationMainClient.getGroupsByNos(appGrpNums).getEntity();
            List<String> grpIds = IaisCommonUtils.genNewArrayList();
            for (ApplicationGroupDto appGrpDto : applicationGroupDtos) {
                grpIds.add(appGrpDto.getId());
            }
            Map<String, AppGrpPremisesDto> premMap = inspectionTaskMainClient.getGroupAppsByNos(grpIds).getEntity();
            //filter app Premises Correlation
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationMainClient.getPremCorrDtoByAppGroupIds(grpIds).getEntity();
            List<String> appCorrIdList = getAppPremCorrIdsByDto(appPremisesCorrelationDtos);
            String appPremCorrId = SqlHelper.constructInCondition(T7_REF_NO, appCorrIdList.size());
            searchParam.addParam(APPCORRID_LIST, appPremCorrId);
            for(int i = 0; i < appCorrIdList.size(); i++){
                searchParam.addFilter(T7_REF_NO + i, appCorrIdList.get(i));
            }
            //filter appNo
            if(!StringUtil.isEmpty(dashFilterAppNo)){
                searchParam.addFilter(DASH_FILTER_APPNO, dashFilterAppNo,true);
            }
            //filter Common pool
            if(!StringUtil.isEmpty(dashCommonPoolStatus)){
                searchParam.addFilter("dashCommonPoolStatus", dashCommonPoolStatus,true);
            }
            //filter page conditions
            searchParam = filterPageConditions(searchParam, searchParamGroup, "T5.STATUS", dashAppStatus, hcsaTaskAssignDto,
                    APP_PREM_ID, hciAddress);
            //filter work groups
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            //search
            QueryHelp.setMainSql(INTRA_DASHBOARD_QUERY, "dashSupervisorAjax", searchParam);
            SearchResult<DashWorkTeamAjaxQueryDto> ajaxResult = getWorkTeamAjaxResultByParam(searchParam);
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            for (DashWorkTeamAjaxQueryDto queryDto : ajaxResult.getRows()) {
                if (!StringUtil.isEmpty(queryDto.getOriginLicenceId())) {
                    licIds.add(queryDto.getOriginLicenceId());
                }
            }
            Map<String, LicenceDto> licMap = licenceClient.getLicenceList(licIds).getEntity();
            //set other data
            setWorkTeamAjaxDataToShow(ajaxResult.getRows(), hcsaTaskAssignDto, premMap, licMap);
            map.put(RESULT, SUCCESS);
            map.put(AJAXRESULT, ajaxResult);
        } else {
            searchParam.setPageSize(1);
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, switchAction, workGroupIds);
            QueryHelp.setMainSql(INTRA_DASHBOARD_QUERY, "dashSupervisorAjax", searchParam);
            SearchResult<DashWorkTeamAjaxQueryDto> ajaxResult = getWorkTeamAjaxResultByParam(searchParam);
            map.put(TOTAL_NUMBER, ajaxResult.getRowCount());
        }
        return map;
    }
}