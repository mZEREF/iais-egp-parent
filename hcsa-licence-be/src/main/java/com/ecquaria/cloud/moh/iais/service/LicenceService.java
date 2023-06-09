package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.GenerateLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.SuperLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;

import java.util.List;

/**
 * LicenceService
 *
 * @author suocheng
 * @date 11/29/2019
 */
public interface LicenceService {
    List<ApplicationLicenceDto> getCanGenerateApplications(GenerateLicenceDto generateLicenceDto);
    List<HcsaServiceDto> getHcsaServiceById(List<String> serviceIds);
    String getHciCode(String serviceCode);
    String getGroupLicenceNo(String serviceCode,AppPremisesRecommendationDto appPremisesRecommendationDto,String orgLicecnceId,Integer premisesNumber);
    AppPremisesRecommendationDto getTcu(String appPremCorrecId);
    PremisesDto getLatestVersionPremisesByHciCode(String hciCode);
    KeyPersonnelDto getLatestVersionKeyPersonnelByIdNoAndOrgId(String idNo,String orgId,String nationality);
    LicenceDto getLicenceDto(String licenceId);
    LicenceDto getLicenceDtoByLicNo(String licNo);
    List<String> getLicenceOutDate(int outMonth);
    List<LicenceGroupDto> createSuperLicDto(EventBusLicenceGroupDtos eventBusLicenceGroupDtos);
    EventBusLicenceGroupDtos createFESuperLicDto(String eventRefNum,String submissionId);
    void createFESuperLicDto(EventBusLicenceGroupDtos eventBusLicenceGroupDtos,String submissionId);
    void sendNotification(SuperLicDto superLicDto);
    void sendUenEmail(EventBusLicenceGroupDtos eventBusLicenceGroupDtos);
    EventBusLicenceGroupDtos getEventBusLicenceGroupDtosByRefNo(String refNo);

    void updateLicEicRequestTrackingDto(EicRequestTrackingDto licEicRequestTrackingDto);

    EicRequestTrackingDto getLicEicRequestTrackingDtoByRefNo(String refNo);

    MsgTemplateDto getMsgTemplateById(String id);


    List<PremisesGroupDto> getPremisesGroupDtoByOriginLicenceId (String originLicenceId);

    List<LicAppCorrelationDto> getLicAppCorrelationDtosByApplicationIds(List<String> appIds);

    PremisesDto getHciCode(AppGrpPremisesDto appGrpPremisesDto);

    void sendRfcApproveLicenseeEmail(ApplicationGroupDto applicationGroupDto, ApplicationDto applicationDto, String licenceNo, List<String> svcCodeList);

    /*List<LicBaseSpecifiedCorrelationDto> getLicBaseSpecifiedCorrelationDtos(String svcType, String originLicenceId);*/

    void changePostInsForTodoAudit( ApplicationViewDto applicationViewDto );

    LicenceDto getLicDtoById(String relateRecId);

    SearchResult<LicPremisesQueryDto> searchLicencesInChangeTCUDate(SearchParam searchParam);

    List<LicPremisesDto> getPremisesByLicIds(List<String> licenceIds);

    List<LicPremisesDto> saveLicPremises(List<LicPremisesDto> licPremisesDtos);
}
