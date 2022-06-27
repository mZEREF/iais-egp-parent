package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnlAssessQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.SelfPremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.client.AppInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.FeAdminClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceInboxClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminMainFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sop.webflow.rt.api.Process;

import java.util.List;

/****
 *
 *   @date 1/15/2020
 *   @author zixian
 */
@Service
@Slf4j
public class RequestForChangeServiceImpl implements RequestForChangeService {
    @Autowired
    LicenceInboxClient licenceClient;
    
    @Autowired
    AppInboxClient applicationClient;
    
    @Autowired
    SystemAdminMainFeClient systemAdminClient;

    @Autowired
    FeAdminClient feAdminClient;
    
    @Override
    public List<PremisesListQueryDto> getPremisesList(String licenseeId) {
        return licenceClient.getPremises(licenseeId).getEntity();
    }
    
    @Override
    public AppSubmissionDto submitChange(AppSubmissionDto appSubmissionDto, Process process) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        //save appGrp and app
        appSubmissionDto = applicationClient.saveAppsForRequestForChange(appSubmissionDto).getEntity();
        //asynchronous save the other data.
        //eventBus(appSubmissionDto, process);
        return appSubmissionDto;
    }
    
    @Override
    public String getApplicationGroupNumber(String appType) {
        return systemAdminClient.applicationNumber(appType).getEntity();
    }

    @Override
    @SearchTrack(catalog = "interInboxQuery", key = "queryPremises")
    public SearchResult<SelfPremisesListQueryDto> searchPreInfo(SearchParam searchParam) {
        SearchResult<SelfPremisesListQueryDto> result = licenceClient.searchResultPremises(searchParam).getEntity();
        if (result == null || result.getRows() == null) {
            return result;
        }
        result.getRows().stream().forEach(h -> {
            List<String> addressList = IaisCommonUtils.genNewArrayList();
            addressList.add(h.getAddress());
            h.setPremisesDtoList(addressList);
            /*List<PremisesDto> premisesDtoList = inboxService.getPremisesByLicId(h.getLicenceId());
            List<String> addressList = IaisCommonUtils.genNewArrayList();
            for (PremisesDto premisesDto:premisesDtoList
            ) {
                addressList.add(MiscUtil.getAddress(premisesDto.getBlkNo(),premisesDto.getStreetName(),premisesDto.getBuildingName(),premisesDto.getFloorNo(),premisesDto.getUnitNo(),premisesDto.getPostalCode()));
                h.setPremisesDtoList(addressList);
            }*/
        });
        return result;
    }

    @Override
    public SearchResult<PersonnelQueryDto> searchPsnInfo(SearchParam searchParam) {
        return licenceClient.searchPsnInfo(searchParam).getEntity();
    }

    @Override
    public LicenseeDto getLicenseeByOrgId(String orgId){
        return feAdminClient.getLicenseeByOrgId(orgId).getEntity().get(0);
    }

    @Override
    public List<LicenseeKeyApptPersonDto> getLicenseeKeyApptPersonDtoListByLicenseeId(String licenseeId) {
        return feAdminClient.getLicenseeKeyApptPersonDtoListByLicenseeId(licenseeId).getEntity();
    }

    @Override
    public List<FeUserDto> getAccountByOrgId(String orgId){
        return feAdminClient.getAccountByOrgId(orgId).getEntity();
    }

    @Override
    @SearchTrack(catalog = "interInboxQuery", key = "appPersonnelQuery")
    public SearchResult<PersonnlAssessQueryDto> searchAssessPsnInfo(SearchParam searchParam) {
        return licenceClient.assessPsnDoQuery(searchParam).getEntity();
    }

    @Override
    public List<PersonnelListQueryDto> getLicencePersonnelListQueryDto(String licenseeId) {
        return licenceClient.getPersonnel(licenseeId).getEntity();
    }

    @Override
    public List<PersonnelListDto> getPersonnelListAssessment(List<String> idNos,String orgId) {
        return licenceClient.getPersonnelListAssessment(idNos,orgId).getEntity();
    }

}
