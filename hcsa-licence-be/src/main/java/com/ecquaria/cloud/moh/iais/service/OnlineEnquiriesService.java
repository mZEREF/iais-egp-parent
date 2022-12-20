package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ComplianceHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ApplicationTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.EnquiryInspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.InspectionTabQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicAppMainQueryResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenceQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.LicenseeQueryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ProfessionalInformationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.RfiTabQueryResultsDto;
import sop.webflow.rt.api.BaseProcessClass;

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
    SearchResult<ProfessionalInformationQueryDto> searchProfessionalInformation(SearchParam searchParam);
    void setLicInfo(HttpServletRequest request);

    void getInspReport(BaseProcessClass bpc, String appPremisesCorrelationId, String licenceId);

    void setAppInfo(HttpServletRequest request);
    List<SelectOption> getServicePersonnelRoleOption();
    List<String> getLicIdsByappIds(List<String> appIds);
    List<ComplianceHistoryDto> complianceHistoryDtosByLicId(List<ComplianceHistoryDto> complianceHistoryDtos, String licenceId, Set<String> appIds);

    SearchResult<LicAppMainQueryResultDto> searchMainQueryResult(SearchParam searchParam);

    SearchResult<LicenceQueryResultsDto> searchLicenceQueryResult(SearchParam searchParam);

    SearchResult<LicenseeQueryResultsDto> searchLicenseeQueryResult(SearchParam searchParam);

    SearchResult<RfiTabQueryResultsDto> searchLicenceRfiTabQueryResult(SearchParam searchParam);

    SearchResult<ApplicationTabQueryResultsDto> searchLicenceAppTabQueryResult(SearchParam searchParam);

    SearchResult<InspectionTabQueryResultsDto> searchLicenceInsTabQueryResult(SearchParam searchParam);


}
