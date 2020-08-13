package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/****
 *
 *   @date 12/26/2019
 *   @author zixian
 */
public interface RequestForChangeService {
    List<PremisesListQueryDto> getPremisesList(String licenseeId);

    AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId);

    List<ApplicationDto> getAppByLicIdAndExcludeNew(String licenceId);

    AppSubmissionDto submitChange(AppSubmissionDto appSubmissionDto);

    String getApplicationGroupNumber(String appType);
    
    void upDateLicStatus(LicenceDto licenceDto);

    void saveLicence(LicenceDto licenceDto);

    List<LicenseeKeyApptPersonDto> getLicenseeKeyApptPersonDtoListByUen(String uenNo);

    LicenceDto getByLicNo(String licNo);

    List<LicenseeKeyApptPersonDto> getLicenseeKeyApptPersonDtoListByLicenseeId(String licenseeId);

    List<PersonnelListQueryDto> getLicencePersonnelListQueryDto(String licenseeId);

    List<AppSubmissionDto> getAppSubmissionDtoByLicenceIds(List<String> licenceIds);

    List<AppSubmissionDto> saveAppsBySubmissionDtos(List<AppSubmissionDto> appSubmissionDtos);

    LicenceDto getLicenceDtoByLicenceId(String licenceId);

    LicenseeIndividualDto getLicIndByNRIC(String nric);

    LicenseeDto getLicenseeByUenNo(String uenNo);


    SearchResult<PersonnelQueryDto> psnDoQuery(SearchParam searchParam);


    void sendEmail(String appGrpId,String type,String appNo,String serviceName,String licenceNo,Double amount,String licenceeName,String giroNo,String licenseeId,String subject,String aoName) throws Exception;
    List<LicenceDto> getLicenceDtoByPremisesId(String premisesId);

    SearchResult<PremisesListQueryDto> searchPreInfo(SearchParam searchParam);

    LicenceDto getLicenceById(String licenceId);

    List<LicenceDto> getLicenceDtoByHciCode(String hciCode,String licenseeId);

    List<LicKeyPersonnelDto> getLicKeyPersonnelDtoByPerId(List<String> personIds);

    List<String> getPersonnelIdsByIdNo(String idNo);

    String getIdNoByLicId(String licId);

    List<PersonnelListDto> getPersonnelListDto(PersonnelTypeDto personnelTypeDto);

    List<AppSubmissionDto> saveAppsForRequestForGoupAndAppChangeByList(List<AppSubmissionDto> appSubmissionDtos);

    String sendNotification(EmailDto email);

    void sendMessageHelper(String subject, String messageType, String srcSystemId,String serviceId, String licenseeId, String templateMessageByContent, HashMap<String, String> maskParams);

    List<String> getAdminEmail(String orgId);

    Boolean isOtherOperation(String licenceId);
    // need delete
     Map<String, String> doValidatePremiss(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto, List<String> premisesHciList, String  masterCodeDto ,boolean isRfi);
     void svcDocToPresmise(AppSubmissionDto appSubmissionDto);
    void premisesDocToSvcDoc( AppSubmissionDto appSubmissionDtoByLicenceId);
    void sendRfcSubmittedEmail(AppSubmissionDto appSubmissionDto, String pmtMethod);
    void sendRfcLicenseeEmail(AppSubmissionDto appSubmissionDto);
    void sendRfcEmailToOfficer(AppSubmissionDto appSubmissionDto);
    void sendRfcPaymentOnlineOrGIROSuccesedEmail(AppSubmissionDto appSubmissionDto);
    }
