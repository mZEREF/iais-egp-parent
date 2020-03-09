package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.WithOutRenewalDto;

/**
 * WithOutRenewalService
 *
 * @author caijing
 * @date 2020/1/7
 */

public interface WithOutRenewalService {
    WithOutRenewalDto getRenewalViewByLicNo(String licenceNo);
}
