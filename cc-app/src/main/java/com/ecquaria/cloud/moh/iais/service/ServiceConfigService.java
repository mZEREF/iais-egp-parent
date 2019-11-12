package com.ecquaria.cloud.moh.iais.service;


import java.util.List;

/**
 * ServiceConfigService
 *
 * @author suocheng
 * @date 10/14/2019
 */
public interface ServiceConfigService {
    List getHcsaServiceDtosById(List<String> ids);
}
