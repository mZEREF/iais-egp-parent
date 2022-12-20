package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/****
 *
 *   @date 12/26/2019
 *   @author zixian
 */
public interface RequestForChangeService {

    List<PremisesListQueryDto> getPremisesList(String licenseeId);

    AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId);

    List<ApplicationDto> getAppByLicIdAndExcludeNew(String licenceId);

    Boolean isAllCanRfc(List<String> licIds);

    AppSubmissionDto submitChange(AppSubmissionDto appSubmissionDto);

    String getApplicationGroupNumber(String appType);

    void upDateLicStatus(LicenceDto licenceDto);

    void saveLicence(LicenceDto licenceDto);

    List<LicenseeKeyApptPersonDto> getLicenseeKeyApptPersonDtoListByUen(String uenNo);

    LicenceDto getByLicNo(String licNo);

    List<LicenseeKeyApptPersonDto> getLicenseeKeyApptPersonDtoListByLicenseeId(String licenseeId);

    List<FeUserDto> getAccountByOrgId(String orgId);

    LicenseeDto getLicenseeByOrgId(String orgId);

    List<PersonnelListQueryDto> getLicencePersonnelListQueryDto(String licenseeId);

    List<AppSubmissionDto> getAppSubmissionDtosByLicenceIds(List<String> licenceIds);

    List<AppSubmissionDto> saveAppsBySubmissionDtos(List<AppSubmissionDto> appSubmissionDtos);

    LicenceDto getLicenceDtoByLicenceId(String licenceId);

    LicenceDto getLicDtoById(String licenceId) ;

    LicenseeIndividualDto getLicIndByNRIC(String nric);

    LicenseeDto getLicenseeByUenNo(String uenNo);

    SearchResult<PersonnelQueryDto> psnDoQuery(SearchParam searchParam);

    void sendEmail(String appGrpId,String type,String appNo,String serviceName,String licenceNo,Double amount,String licenceeName,String giroNo,String licenseeId,String subject,String aoName) throws Exception;
    List<LicenceDto> getLicenceDtoByPremisesId(String premisesId);

    SearchResult<PremisesListQueryDto> searchPreInfo(SearchParam searchParam);

    LicenceDto getLicenceById(String licenceId);

    List<PersonnelListDto> getPersonnelListDto(PersonnelTypeDto personnelTypeDto);

    void sendNotification(EmailDto email);

    List<String> getAdminEmail(String orgId);

    void sendRfcSubmittedEmail(List<AppSubmissionDto> appSubmissionDtos, String pmtMethod) throws Exception;
    List<FeUserDto> getFeUserDtoByLicenseeId(String licenseeId);
    LicenceDto getLicenceDtoByLicNo(String licenceNo);
    boolean serviceConfigIsChange(List<String> serviceId ,String presmiseType);

    String baseSpecLicenceRelation(LicenceDto licenceDto,boolean flag);
    boolean baseSpecLicenceRelation(LicenceDto licenceDto);
    LicenceDto getLicenceDtoIncludeMigrated(String licenceId);

    List<AppSubmissionDto> saveAppSubmissionList(List<AppSubmissionDto> appSubmissionDtoList, String eventRefNo, BaseProcessClass bpc);
}
