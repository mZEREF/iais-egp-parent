package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;

import java.util.List;

/**
 * RequestForInfomationService
 *
 * @author junyu
 * @date 2019/12/16
 */
public interface RequestForInformationService {
    List<SelectOption> getAppTypeOption();
    List<SelectOption> getAppStatusOption();
    List<SelectOption> getLicSvcTypeOption();
    List<SelectOption> getLicStatusOption();

}
