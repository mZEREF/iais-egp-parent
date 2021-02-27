package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcCateWrkgrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2020/2/12 17:36
 */
public interface ConfigService {

    List<HcsaServiceDto> getAllHcsaServices();

    void viewPageInfo(HttpServletRequest request);

    void editPageInfo(HttpServletRequest request);

    void saveOrUpdate(HttpServletRequest request, HttpServletResponse response, HcsaServiceConfigDto hcsaServiceConfigDto) throws Exception;

    void addNewService(HttpServletRequest request);

    void update(HttpServletRequest request,HttpServletResponse response, HcsaServiceConfigDto hcsaServiceConfigDto) throws Exception;

    void saData(HttpServletRequest request);

    void delete(HttpServletRequest request);

    void deleteOrCancel(HttpServletRequest request,HttpServletResponse response);

    List<HcsaSvcRoutingStageDto> getHcsaSvcRoutingStageDtos();

    List<String>  split(String str);

    List<String> getType();

    List<HcsaSvcCateWrkgrpCorrelationDto> getHcsaSvcCateWrkgrpCorrelationDtoBySvcCateId(String svcCateId);

    Map<String,String> getMaskHcsaServiceCategory();
}
