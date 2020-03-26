package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Wenkang
 * @date 2020/2/12 17:36
 */
public interface ConfigService {

    List<HcsaServiceDto> getAllHcsaServices(HttpServletRequest request);

    void viewPageInfo(HttpServletRequest request);

    void editPageInfo(HttpServletRequest request);

    void saveOrUpdate(HttpServletRequest request, HttpServletResponse response);

    void addNewService(HttpServletRequest request);

    void update(HttpServletRequest request,HttpServletResponse response);

    void saData(HttpServletRequest request);

    void delete(HttpServletRequest request);

    void deleteOrCancel(HttpServletRequest request,HttpServletResponse response);
}
