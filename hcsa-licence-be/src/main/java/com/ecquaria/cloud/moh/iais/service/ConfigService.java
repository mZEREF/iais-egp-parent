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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Wenkang
 * @date 2020/2/12 17:36
 */
public interface ConfigService {

    List<HcsaServiceDto> getAllHcsaServices();

    List<HcsaServiceDto> getActiveServicesBySvcType(String svcType);

    HcsaServiceConfigDto getHcsaServiceConfigDtoByServiceId(String serviceId);

    void viewPageInfo(HttpServletRequest request);

    void editPageInfo(HttpServletRequest request);

    void saveOrUpdate(HttpServletRequest request, HttpServletResponse response, HcsaServiceConfigDto hcsaServiceConfigDto) throws Exception;

    void addNewService(HttpServletRequest request);

    Map<String, List<HcsaConfigPageDto>>  getHcsaConfigPageDto();

    void update(HttpServletRequest request,HttpServletResponse response, HcsaServiceConfigDto hcsaServiceConfigDto) throws Exception;

    void saData(HttpServletRequest request);

    void delete(HttpServletRequest request);

    void doDeleteService(String serviceId);

    void deleteOrCancel(HttpServletRequest request,HttpServletResponse response);

    List<HcsaSvcRoutingStageDto> getHcsaSvcRoutingStageDtos();

    List<String>  split(String str);

    List<String> getType();

    List<HcsaSvcCateWrkgrpCorrelationDto> getHcsaSvcCateWrkgrpCorrelationDtoBySvcCateId(String svcCateId);

    public List<HcsaServiceCategoryDto> getHcsaServiceCategoryDto() ;

    Map<String,String> getMaskHcsaServiceCategory();

    HcsaSvcPersonnelDto getHcsaSvcPersonnelDto(String man,String mix,String psnType);

    Map<String,Boolean> isExistHcsaService(HcsaServiceDto hcsaServiceDto);

    void saveHcsaServiceConfigDto(HcsaServiceConfigDto hcsaServiceConfigDto);
}
