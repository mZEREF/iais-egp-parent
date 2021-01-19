package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLicenceTenureDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.LicenceTenShowDto;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/1/3 17:00
 */
public interface HcsaRiskLicenceTenureSerice {
    LicenceTenShowDto getTenShowDto();
    List<SelectOption> getDateTypeOps();
    void remove(String removeVal,LicenceTenShowDto showDto);

    void add(String svcCode, LicenceTenShowDto showDto);

    boolean doIsEditLogic(HcsaRiskLicenceTenureDto temp);

    void saveDto(LicenceTenShowDto showDto);
}
