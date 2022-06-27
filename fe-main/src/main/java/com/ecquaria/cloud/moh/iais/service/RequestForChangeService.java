package com.ecquaria.cloud.moh.iais.service;

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
import sop.webflow.rt.api.Process;

import java.util.List;

/****
 *
 *   @date 12/26/2019
 *   @author zixian
 */
public interface RequestForChangeService {
    List<PremisesListQueryDto> getPremisesList(String licenseeId);

    AppSubmissionDto submitChange(AppSubmissionDto appSubmissionDto, Process process);

    String getApplicationGroupNumber(String appType);

    SearchResult<SelfPremisesListQueryDto> searchPreInfo(SearchParam searchParam);

    SearchResult<PersonnelQueryDto> searchPsnInfo(SearchParam searchParam);

    LicenseeDto getLicenseeByOrgId(String orgId);

    List<LicenseeKeyApptPersonDto> getLicenseeKeyApptPersonDtoListByLicenseeId(String licenseeId);

    List<FeUserDto> getAccountByOrgId(String orgId);

    SearchResult<PersonnlAssessQueryDto> searchAssessPsnInfo(SearchParam searchParam);

    List<PersonnelListQueryDto> getLicencePersonnelListQueryDto(String licenseeId);

    List<PersonnelListDto> getPersonnelListAssessment(List<String> idNos,String orgId);
}
