package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaConfigPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCategoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcCateWrkgrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2020/2/12 17:36
 */
public interface ConfigService {

    List<HcsaServiceDto> getAllHcsaServices();

    List<HcsaServiceDto> getActiveServicesBySvcType(String svcType);

    List<HcsaServiceDto> getServicesBySvcCode(String svcCode);

    HcsaServiceConfigDto getHcsaServiceConfigDtoByServiceId(String serviceId);

    Map<String, List<HcsaConfigPageDto>>  getHcsaConfigPageDto();

    void doDeleteService(String serviceId);

    List<HcsaSvcRoutingStageDto> getHcsaSvcRoutingStageDtos();

    List<String> getType();

    List<HcsaSvcCateWrkgrpCorrelationDto> getHcsaSvcCateWrkgrpCorrelationDtoBySvcCateId(String svcCateId);

    List<HcsaServiceCategoryDto> getHcsaServiceCategoryDto(boolean excludeBsb) ;

    HcsaSvcPersonnelDto getHcsaSvcPersonnelDto(String man,String mix,String psnType);

    Map<String,Boolean> isExistHcsaService(HcsaServiceDto hcsaServiceDto);

    void saveHcsaServiceConfigDto(HcsaServiceConfigDto hcsaServiceConfigDto);


}
