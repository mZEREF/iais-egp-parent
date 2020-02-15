package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AutoRenewalDto;

/**
 * AutoRenewalViewService
 *
 * @author caijing
 * @date 2020/1/7
 */
public interface AutoRenewalViewService {
    AutoRenewalDto getAutoRenewalViewByLicNo(String licenceNo);
}
