package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
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
    public List<ApplicationLicenceDto> getCanGenerateApplications(int day);
    public List<HcsaServiceDto> getHcsaServiceById(List<String> serviceIds);
    public String getHciCode(String serviceCode);
    public String getLicenceNo(String hciCode,String serviceCode,AppPremisesRecommendationDto appPremisesRecommendationDto);
    public String getGroupLicenceNo(String serviceCode,AppPremisesRecommendationDto appPremisesRecommendationDto,String orgLicecnceId,Integer premisesNumber);
    public AppPremisesRecommendationDto getTcu(String appPremCorrecId);
    public PremisesDto getLatestVersionPremisesByHciCode(String hciCode);
    public KeyPersonnelDto getLatestVersionKeyPersonnelByIdNoAndOrgId(String idNo,String orgId);
    public LicenceDto getLicenceDto(String licenceId);

    public List<LicenceGroupDto> createSuperLicDto(EventBusLicenceGroupDtos eventBusLicenceGroupDtos);
    public EventBusLicenceGroupDtos createFESuperLicDto(String eventRefNum,String submissionId);

    public EventBusLicenceGroupDtos getEventBusLicenceGroupDtosByRefNo(String refNo);

    public EicRequestTrackingDto updateLicEicRequestTrackingDto(EicRequestTrackingDto licEicRequestTrackingDto);

    public EicRequestTrackingDto getLicEicRequestTrackingDtoByRefNo(String refNo);

    public MsgTemplateDto getMsgTemplateById(String id);

    public void sendEmail(EmailDto emailDto);

}
