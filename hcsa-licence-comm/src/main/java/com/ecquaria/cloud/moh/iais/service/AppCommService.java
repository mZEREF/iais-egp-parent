package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesDoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.Process;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 *
 * @Auther chenlei on 5/3/2022.
 */
public interface AppCommService {

    AppSubmissionDto getAppSubmissionDtoByAppNo(String applicationNo);

    AppSubmissionDto getRfiAppSubmissionDtoByAppNo(String appNo);

    List<ApplicationDto> getApplicationsByGroupNo(String appGrpNo);

    String getDraftNo(String appType);
    
    String getGroupNo(String appType);

    ProfessionalResponseDto retrievePrsInfo(String profRegNo);

    List<ApplicationDto> getAppByLicIdAndExcludeNew(String licenceId);

    Boolean isOtherOperation(String licenceId);

    List<AppGrpPremisesDto> getActivePendingPremiseList(String licenseeId);

    List<AppGrpPrimaryDocDto> getMaxSeqNumPrimaryDocList(String appGrpId);

    List<AppSvcDocDto> getMaxSeqNumSvcDocList(String appGrpId);

    List<String> getHciFromPendAppAndLic(String licenseeId, List<HcsaServiceDto> hcsaServiceDtos);

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

    AppGrpPrimaryDocDto getMaxVersionPrimaryComDoc(String appGrpId, String configDocId, String valueOf);

    AppSvcDocDto getMaxVersionSvcComDoc(String appGrpId, String configDocId, String valueOf);

    AppGrpPrimaryDocDto getMaxVersionPrimarySpecDoc(String appGrpId, String configDocId, String appNo, String valueOf);

    AppSvcDocDto getMaxVersionSvcSpecDoc(AppSvcDocDto appSvcDocDto,String appNo);

    List<AppGrpPremisesEntityDto> getPendAppPremises(AppPremisesDoQueryDto appPremisesDoQueryDto);

}
