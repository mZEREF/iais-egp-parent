package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Wenkang
 * @date 2020/2/12 17:36
 */
public interface ConfigService {

    List<HcsaServiceDto> getAllHcsaServices(HttpServletRequest request);

    void viewPageInfo(HttpServletRequest request);

    void editPageInfo(HttpServletRequest request);

    void saveOrUpdate(HttpServletRequest request);

    void addNewService(HttpServletRequest request);
}
