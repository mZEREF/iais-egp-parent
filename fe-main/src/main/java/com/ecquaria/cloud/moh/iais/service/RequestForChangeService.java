package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.*;
import sop.webflow.rt.api.Process;

import java.util.List;

/****
 *
 *   @date 12/26/2019
 *   @author zixian
 */
public interface RequestForChangeService {
    List<PremisesListQueryDto> getPremisesList(String licenseeId);

    ApplicationDto getApplicationByLicenceId(String licenceId);

    AppSubmissionDto submitChange(AppSubmissionDto appSubmissionDto, Process process);

    String getApplicationGroupNumber(String appType);

    SearchResult<PremisesListQueryDto> searchPreInfo(SearchParam searchParam);

    SearchResult<PersonnelQueryDto> searchPsnInfo(SearchParam searchParam);

    SearchResult<PersonnlAssessQueryDto> searchAssessPsnInfo(SearchParam searchParam);

    List<PersonnelListQueryDto> getLicencePersonnelListQueryDto(String licenseeId);

    List<PersonnelListDto> getPersonnelListAssessment(List<String> idNos);
}
