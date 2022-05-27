package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;

import java.io.File;
import java.util.List;
import java.util.Set;

/**
 * @Auther chenlei on 5/3/2022.
 */
public interface ConfigCommService {

    List<HcsaSvcSubtypeOrSubsumedDto> loadLaboratoryDisciplines(String serviceId);

    HcsaServiceDto getActiveHcsaServiceDtoByName(String svcName);

    List<HcsaServiceDto> getHcsaServiceDtosById(List<String> ids);

    List<HcsaServiceDto> getActiveHcsaSvcByNames(List<String> names);

    List<HcsaServiceDto> getHcsaServiceByNames(List<String> svcNames);

    List<HcsaServiceDto> allHcsaService();

    List<HcsaServiceCorrelationDto> getActiveSvcCorrelation();

    String getServiceNameById(String svcId);

    boolean serviceConfigIsChange(List<String> serviceIds, String premiseType);

    Set<String> getAppGrpPremisesTypeBySvcId(List<String> svcIds);

    List<HcsaSvcDocConfigDto> getAllHcsaSvcDocs(String serviceId);

    List<HcsaSvcDocConfigDto> listSvcDocConfigByIds(List<String> ids);

    List<HcsaSvcDocConfigDto> getPrimaryDocConfigById(String id);

    HcsaSvcDocConfigDto getHcsaSvcDocConfigDtoById(String svcDocId);

    List<HcsaServiceStepSchemeDto> getHcsaServiceStepSchemesByServiceId(String serviceId);

    List<HcsaSvcPersonnelDto> getSvcAllPsnConfig(List<HcsaServiceStepSchemeDto> svcStep, String svcId);

    PreOrPostInspectionResultDto judgeIsPreInspection(AppSubmissionDto appSubmissionDto);

    List<RiskResultDto> getRiskResult(List<RiskAcceptiionDto> riskAcceptiionDtoList);

    FeeDto getGroupAmendAmount(AmendmentFeeDto amendmentFeeDto);

    List<HcsaSvcSubtypeOrSubsumedDto> getSvcSubtypeOrSubsumedByIdList(List<String> idList);

    List<HcsaSvcPersonnelDto> getHcsaSvcPersonnel(String currSvcId, String psnType);

    HcsaServiceStepSchemeDto getHcsaServiceStepSchemeByConds(String serviceId, String stepCode);

    PostCodeDto getPostCodeByCode(String postalCode);

    List<String> saveFileRepo(List<File> files);

    byte[] downloadFileRepo(String fileRepoId);

}
