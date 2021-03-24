package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.eLIS.ApplicationElisToHalpDto;
import com.ecquaria.cloud.moh.iais.common.dto.eLIS.HciElisToHalpDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ComplianceHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.EnquiryInspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ProfessionalInformationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.LicenseeQueryDto;
import com.ecquaria.cloud.moh.iais.dto.RegistrationDetailDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * OnlineEnquiriesService
 *
 * @author junyu
 * @date 2020/2/11
 */
public interface OnlineEnquiriesService {
    EnquiryInspectionReportDto getInsRepDto(ApplicationViewDto applicationViewDto,String licenceId);
    SearchResult<LicenseeQueryDto> searchLicenseeIdsParam(SearchParam searchParam);
    SearchResult<HcsaSvcQueryDto> searchSvcNamesParam(SearchParam searchParam);
    SearchResult<ProfessionalInformationQueryDto> searchProfessionalInformation(SearchParam searchParam);
    void setLicInfo(HttpServletRequest request);
    void preInspReport(HttpServletRequest request);
    void setAppInfo(HttpServletRequest request);
    List<SelectOption> getServicePersonnelRoleOption();
    List<String> getLicIdsByappIds(List<String> appIds);
    List<ComplianceHistoryDto> complianceHistoryDtosByLicId(List<ComplianceHistoryDto> complianceHistoryDtos, String licenceId, Set<String> appIds);
    PersonnelsDto getProfessionalInformationByKeyPersonnelId(String psnId);

    RegistrationDetailDto getRegnDetailListByRegnNo(String regnNo);

    SearchResult<HciElisToHalpDto> searchHciElisParam(SearchParam hciLicParam);

    SearchResult<ApplicationElisToHalpDto> searchAppElisParam(SearchParam hciLicParam);
}
