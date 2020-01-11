package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiApplicationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.RequestForInformationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * RequestForInformationServiceImpl
 *
 * @author junyu
 * @date 2019/12/16
 */
@Service
public class RequestForInformationServiceImpl implements RequestForInformationService {
    @Autowired
    RequestForInformationClient requestForInformationClient;
    @Autowired
    ApplicationClient applicationClient;
    @Autowired
    AppPremisesCorrClient appPremisesCorrClient;
    @Autowired
    HcsaConfigClient hcsaConfigClient;

    private final String[] appType=new String[]{
            ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION,
            ApplicationConsts.APPLICATION_TYPE_RENEWAL,
            ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE,
            ApplicationConsts.APPLICATION_TYPE_APPEAL,
            ApplicationConsts.APPLICATION_TYPE_REINSTATEMENT,
            ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL
    };
    private final String[] appStatus=new String[]{
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01,
            ApplicationConsts.APPLICATION_STATUS_APPROVED,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION,
            ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING,
            ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST,
            ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS,
            ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING,
            ApplicationConsts.APPLICATION_STATUS_REJECTED,
            ApplicationConsts.APPLICATION_STATUS_ROLL_BACK,
            ApplicationConsts.APPLICATION_STATUS_DRAFT,
            ApplicationConsts.APPLICATION_STATUS_DELETED,
            ApplicationConsts.APPLICATION_STATUS_ROUTE_TO_DMS,
            ApplicationConsts.APPLICATION_STATUS_SUPPORT,
            ApplicationConsts.APPLICATION_STATUS_VERIFIED,
            ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_INFORMATION,
            ApplicationConsts.APPLICATION_STATUS_LICENCE_START_DATE,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT_REVIEW,
            ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT,
            ApplicationConsts.APPLICATION_STATUS_FE_TO_BE_RECTIFICATION,
            ApplicationConsts.APPLICATION_STATUS_PENDING_RECTIFICATION_REVIEW,
            ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION,
            ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION_REPLY,
            ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_NOTIFICATION,
            ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_AMEND,
            ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION
};
    private final String[] licServiceType=new String[]{
            ApplicationConsts.SERVICE_CONFIG_TYPE_BASE,
            ApplicationConsts.SERVICE_CONFIG_TYPE_SPECIFIED,
            ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED
    };
    private final String[] licStatus=new String[]{
            ApplicationConsts.LICENCE_STATUS_ACTIVE,
            ApplicationConsts.LICENCE_STATUS_IACTIVE,
            ApplicationConsts.LICENCE_STATUS_EXPIRY
    };

    @Override
    public List<SelectOption> getAppTypeOption() {
        return MasterCodeUtil.retrieveOptionsByCodes(appType);
    }

    @Override
    public List<SelectOption> getAppStatusOption() {
        return  MasterCodeUtil.retrieveOptionsByCodes(appStatus);
    }

    @Override
    public List<SelectOption> getLicSvcTypeOption() {
        return MasterCodeUtil.retrieveOptionsByCodes(licServiceType);
    }

    @Override
    public List<SelectOption> getLicStatusOption() {
        return MasterCodeUtil.retrieveOptionsByCodes(licStatus);
    }

    @Override
    public SearchResult<RfiApplicationQueryDto> appDoQuery(SearchParam searchParam) {
        return applicationClient.searchApp(searchParam).getEntity();
    }

    @Override
    public SearchResult<RfiLicenceQueryDto> licenceDoQuery(SearchParam searchParam) {

        return requestForInformationClient.searchRfiLicence(searchParam).getEntity();
    }

    @Override
    public List<String> getSvcNamesByType(String type) {
        return hcsaConfigClient.getHcsaServiceNameByType(type).getEntity();
    }

    @Override
    public LicPremisesReqForInfoDto updateLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        return requestForInformationClient.updateLicPremisesReqForInfoFe(licPremisesReqForInfoDto).getEntity();
    }

    @Override
    public LicPremisesReqForInfoDto createLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        return requestForInformationClient.createLicPremisesReqForInfo(licPremisesReqForInfoDto).getEntity();
    }

    @Override
    public List<LicPremisesReqForInfoDto> getAllReqForInfo() {
        return requestForInformationClient.getAllReqForInfo().getEntity();
    }

    @Override
    public List<LicPremisesReqForInfoDto> searchLicPremisesReqForInfo(String licPremId) {
        return requestForInformationClient.searchLicPremisesReqForInfo(licPremId).getEntity();
    }

    @Override
    public LicPremisesReqForInfoDto getLicPreReqForInfo(String id) {
        return requestForInformationClient.getLicPreReqForInfo(id).getEntity();
    }

    @Override
    public void deleteLicPremisesReqForInfo(String id) {
        requestForInformationClient.deleteLicPremisesReqForInfo(id);

    }

    @Override
    public void acceptLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        requestForInformationClient.acceptLicPremisesReqForInfo(licPremisesReqForInfoDto);

    }


}
