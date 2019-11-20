package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
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
    PostCodeDto getPremisesByPostalCode(String searchField, String filterValue);
    String getSvcIdBySvcCode(String svcCode);
    List<AppGrpPremisesDto> getAppGrpPremisesDtoByLoginId(String loginId);
    List<String> saveFileToRepo(List<MultipartFile> fileList) throws IOException;
    Map<String,List<HcsaSvcDocConfigDto>> getAllHcsaSvcDocs(String serviceId);
    List<HcsaSvcSubtypeOrSubsumedDto> loadLaboratoryDisciplines(String serviceId);
    List<HcsaSvcPersonnelDto> getGOSelectInfo(String serviceId, String psnType);
    AppSvcCgoDto loadGovernanceOfficerByCgoId(String cgoId);

}
