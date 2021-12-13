package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.web.multipart.MultipartFile;

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
    String saveFileToRepo(MultipartFile file) throws IOException;
    List<HcsaSvcDocConfigDto> getAllHcsaSvcDocs(String serviceId);
    List<HcsaSvcSubtypeOrSubsumedDto> loadLaboratoryDisciplines(String serviceId);
    List<HcsaSvcPersonnelDto> getGOSelectInfo(String serviceId, String psnType);
    byte[] downloadFile(String fileRepoId);
    void updatePaymentStatus(ApplicationGroupDto appGrp);
    AppSubmissionDto getAppSubmissionDtoDraft(String draftNo);
    List<HcsaServiceDto> getAllService();
    List<HcsaServiceDto> getServicesInActive();

    List<HcsaServiceStepSchemeDto> getHcsaServiceStepSchemesByServiceId(String serviceId);

    HcsaServiceStepSchemeDto getHcsaServiceStepSchemeByConds(String serviceId, String stepCode);

    List<HcsaServiceCorrelationDto> getCorrelation();
    List<HcsaSvcPersonnelDto> getSvcAllPsnConfig(List<HcsaServiceStepSchemeDto> svcStep, String svcId);
    Map<String,List<HcsaSvcPersonnelDto>> getAllSvcAllPsnConfig(List<HcsaServiceStepSchemeDto> svcStep, List<String> svcIds);
    List<HcsaServiceStepSchemeDto> getHcsaServiceStepSchemesByServiceId(List<String> svcIds);
    List<HcsaServiceDto> getHcsaServiceByNames(List<String> names);
    List<SelectOption> getPubHolidaySelect();
    HcsaServiceDto getHcsaServiceDtoById(String id);
    void paymentUpDateByGrpNo(ApplicationGroupDto appGrp);

    HcsaServiceDto getActiveHcsaServiceDtoByName(String svcName);
    ApplicationGroupDto updateAppGrpPmtStatus(ApplicationGroupDto appGrp);
    List<HcsaSvcDocConfigDto> getPrimaryDocConfigByVersion(Integer version);
    List<HcsaSvcDocConfigDto> getPrimaryDocConfigByIds(List<String> ids);
    List<HcsaSvcDocConfigDto> getPrimaryDocConfigById(String id);
    HcsaServiceDto getActiveHcsaServiceDtoById(String serviceId);
    AppSubmissionDto giroPaymentXmlUpdateByGrpNo(AppSubmissionDto appGrp);

    void sendGiroXmlToSftp();
    void getGiroXmlFromSftpAndSaveXml();
    HcsaServiceDto getServiceDtoById(String id);
    void sysnSaveGroupToBe();
    String saveAppGroupGiroSysnEic(ApplicationGroupDto applicationGroupDto);
    List<HcsaServiceCorrelationDto> getActiveSvcCorrelation();
    List<HcsaSvcSubtypeOrSubsumedDto> getSvcSubtypeOrSubsumedByIdList(List<String> idList);
    List<HcsaServiceDto> getActiveHcsaSvcByNames(List<String> names);
    String getGiroAccountByGroupNo(String groupNo);
    ApplicationGroupDto updateAppGrpPmtStatus(ApplicationGroupDto applicationGroupDto, String giroAccNo);
    public OrganizationDto findOrganizationByUen(String uen);
}
