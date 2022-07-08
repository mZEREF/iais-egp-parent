package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesDoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;

import java.util.List;
import java.util.Map;

/**
 *
 * @Auther chenlei on 5/3/2022.
 */
public interface AppCommService {

    AppSubmissionDto getAppSubmissionDtoByAppNo(String appNo);

    AppSubmissionDto getRfiAppSubmissionDtoByAppNo(String appNo);

    List<ApplicationDto> getApplicationsByGroupNo(String appGrpNo);

    ApplicationDto getApplicationDtoByAppNo(String appNo);

    String getDraftNo(String appType);
    
    String getGroupNo(String appType);

    ProfessionalResponseDto retrievePrsInfo(String profRegNo);

    List<ApplicationDto> getAppByLicIdAndExcludeNew(String licenceId);

    Boolean isOtherOperation(String licenceId);

    AppGrpPremisesDto getAppGrpPremisesById(String appPreId);

    List<AppGrpPremisesDto> getActivePendingPremiseList(String licenseeId);

    List<AppSvcDocDto> getMaxSeqNumSvcDocList(String appGrpId);

    List<String> getHciFromPendAppAndLic(String licenseeId, List<HcsaServiceDto> hcsaServiceDtos,
            List<PremisesDto> excludePremisesList, List<AppGrpPremisesDto> excludeAppPremList);

    List<AppSvcVehicleDto> getActiveVehicles(List<String> appIds);

    List<AppPremiseMiscDto> getActiveWithdrawAppPremiseMiscsByApp(String appId);

    void transform(AppSubmissionDto appSubmissionDto, String licenseeId, String appType, boolean isRfi);

    String getSeqId();

    void saveAutoRFCLinkAppGroupMisc(String notAutoGroupId, String autoGroupId);

    Map<String, String> checkAffectedAppSubmissions(List<LicenceDto> selectLicence, AppGrpPremisesDto appGrpPremisesDto,
            double amount, String draftNo, String appGroupNo, AppEditSelectDto appEditSelectDto,
            List<AppSubmissionDto> appSubmissionDtos);

    Map<String, String> checkAffectedAppSubmissions(AppSubmissionDto appSubmissionDto, LicenceDto licence, Double amount,
            String draftNo, String appGroupNo, AppEditSelectDto appEditSelectDto, List<AppSubmissionDto> appSubmissionDtos);

    AppSvcDocDto getMaxVersionSvcComDoc(String appGrpId, String configDocId, String valueOf);

    AppSvcDocDto getMaxVersionSvcSpecDoc(AppSvcDocDto appSvcDocDto,String appNo);

    List<AppGrpPremisesDto> getPendAppPremises(AppPremisesDoQueryDto appPremisesDoQueryDto);

    AppGrpPremisesDto getActivePremisesByAppNo(String appNo);

}
