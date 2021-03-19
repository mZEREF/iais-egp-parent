package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;

import java.util.List;

/**
 * @author Shicheng
 * @date 2020/10/19 15:52
 **/
public interface GiroDeductionBeService {
    List<ApplicationGroupDto> sendMessageEmail(List<String> appGroupList);
}
