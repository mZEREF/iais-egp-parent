package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.DashComPoolAjaxQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.BeDashboardAjaxService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.MohHcsaBeDashboardService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private TaskService taskService;

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

    @Override
    public Map<String, Object> getCommonDropdownResult(String groupNo, LoginContext loginContext, Map<String, Object> map, String actionValue) {
        if(!StringUtil.isEmpty(groupNo)){
            SearchParam searchParam = new SearchParam(DashComPoolAjaxQueryDto.class.getName());
            searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
            searchParam.setPageNo(1);
            searchParam.setSort("APPLICATION_NO", SearchParam.ASCENDING);
            //filter appGroup NO.
            searchParam.addFilter("groupNo", groupNo, true);
            ApplicationGroupDto applicationGroupDto = applicationMainClient.getAppGrpByNo(groupNo).getEntity();
            //filter app Premises Correlation
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationMainClient.getPremCorrDtoByAppGroupId(applicationGroupDto.getId()).getEntity();
            List<String> appCorrId_list = getAppPremCorrIdsByDto(appPremisesCorrelationDtos);
            String appPremCorrId = SqlHelper.constructInCondition("T2.ID", appCorrId_list.size());
            searchParam.addParam("appCorrId_list", appPremCorrId);
            for(int i = 0; i < appCorrId_list.size(); i++){
                searchParam.addFilter("T2.ID" + i, appCorrId_list.get(i));
            }
            //filter work groups
            List<String> workGroupIds = IaisCommonUtils.genNewArrayList();
            mohHcsaBeDashboardService.setPoolScopeByCurRoleId(searchParam, loginContext, actionValue, workGroupIds);
            //search
            QueryHelp.setMainSql("intraDashboardQuery", "dashCommonTaskAjax", searchParam);
            SearchResult<DashComPoolAjaxQueryDto> ajaxResult = getCommonAjaxResultByParam(searchParam);
            //set other data
            List<DashComPoolAjaxQueryDto> dashComPoolAjaxQueryDtos = ajaxResult.getRows();
            dashComPoolAjaxQueryDtos = setComPoolAjaxDataToShow(dashComPoolAjaxQueryDtos, loginContext);
            ajaxResult.setRows(dashComPoolAjaxQueryDtos);
            map.put("result", "Success");
            map.put("ajaxResult", ajaxResult);
        } else {
            map.put("result", "Fail");
        }
        return map;
    }

    private List<DashComPoolAjaxQueryDto> setComPoolAjaxDataToShow(List<DashComPoolAjaxQueryDto> dashComPoolAjaxQueryDtos, LoginContext loginContext) {
        if(!IaisCommonUtils.isEmpty(dashComPoolAjaxQueryDtos)){
            for(DashComPoolAjaxQueryDto dashComPoolAjaxQueryDto : dashComPoolAjaxQueryDtos){
                //get hciName / address
                AppGrpPremisesDto appGrpPremisesDto = inspectionMainAssignTaskService.getAppGrpPremisesDtoByAppGroId(dashComPoolAjaxQueryDto.getId());
                String address = inspectionMainAssignTaskService.getAddress(appGrpPremisesDto);
                if(!StringUtil.isEmpty(appGrpPremisesDto.getHciName())) {
                    dashComPoolAjaxQueryDto.setHciAddress(StringUtil.viewHtml(appGrpPremisesDto.getHciName() + " / " + address));
                } else {
                    dashComPoolAjaxQueryDto.setHciAddress(StringUtil.viewHtml(address));
                }
                //app status
                dashComPoolAjaxQueryDto.setAppStatus(MasterCodeUtil.getCodeDesc(dashComPoolAjaxQueryDto.getAppStatus()));
                //service
                HcsaServiceDto hcsaServiceDto = hcsaConfigMainClient.getHcsaServiceDtoByServiceId(dashComPoolAjaxQueryDto.getServiceId()).getEntity();;
                dashComPoolAjaxQueryDto.setServiceName(hcsaServiceDto.getSvcName());
                dashComPoolAjaxQueryDto.setHciCode(StringUtil.viewHtml(appGrpPremisesDto.getHciCode()));
                //application
                ApplicationDto applicationDto = applicationMainClient.getAppByNo(dashComPoolAjaxQueryDto.getApplicationNo()).getEntity();
                //get license date
                if(StringUtil.isEmpty(applicationDto.getOriginLicenceId())){
                    dashComPoolAjaxQueryDto.setLicenceExpiryDateStr(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                } else {
                    LicenceDto licenceDto = licenceClient.getLicDtoById(applicationDto.getOriginLicenceId()).getEntity();
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

    @SearchTrack(catalog = "intraDashboardQuery", key = "dashCommonTaskAjax")
    private SearchResult<DashComPoolAjaxQueryDto> getCommonAjaxResultByParam(SearchParam searchParam) {
        return inspectionTaskMainClient.searchDashComPoolDropResult(searchParam).getEntity();
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
}