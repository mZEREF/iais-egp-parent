package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.GolbalRiskShowDto;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/12/30 9:45
 */
public interface HcsaRiskGolbalService {
    GolbalRiskShowDto getGolbalRiskShowDto();
    List<SelectOption> getAutoOp();
}
