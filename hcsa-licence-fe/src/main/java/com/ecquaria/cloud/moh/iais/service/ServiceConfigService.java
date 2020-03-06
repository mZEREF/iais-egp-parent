package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.*;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ServiceConfigService
 *
 * @author suocheng
 * @date 10/14/2019
 */
public interface ServiceConfigService {
    List<HcsaServiceDto> getHcsaServiceDtosById(List<String> ids);
    Set<String> getAppGrpPremisesTypeBySvcId(List<String> svcIds);
    PostCodeDto getPremisesByPostalCode(String postalCode);
    String getSvcIdBySvcCode(String svcCode);
    Map<String,AppGrpPremisesDto> getAppGrpPremisesDtoByLoginId(String loginId);
    String saveFileToRepo(MultipartFile file) throws IOException;
    List<HcsaSvcDocConfigDto> getAllHcsaSvcDocs(String serviceId);
    List<HcsaSvcSubtypeOrSubsumedDto> loadLaboratoryDisciplines(String serviceId);
    List<HcsaSvcPersonnelDto> getGOSelectInfo(String serviceId, String psnType);
    AppSvcCgoDto loadGovernanceOfficerByCgoId(String cgoId);
    byte[] downloadFile(String fileRepoId);
    void updatePaymentStatus(ApplicationGroupDto appGrp);
    AppSubmissionDto getAppSubmissionDtoDraft(String draftNo);
    List<HcsaServiceDto> getAllService();
    List<HcsaServiceDto> getServicesInActive();
    public List<HcsaServiceStepSchemeDto> getHcsaServiceStepSchemesByServiceId(String serviceId);
    List<HcsaServiceCorrelationDto> getCorrelation();
    List<HcsaSvcPersonnelDto> getSvcAllPsnConfig(List<HcsaServiceStepSchemeDto> svcStep, String svcId);
    Map<String,List<HcsaSvcPersonnelDto>> getAllSvcAllPsnConfig(List<HcsaServiceStepSchemeDto> svcStep, List<String> svcIds);
    List<HcsaServiceStepSchemeDto> getHcsaServiceStepSchemesByServiceId(List<String> svcIds);
    List<HcsaServiceDto> getHcsaServiceByNames(List<String> names);
}
