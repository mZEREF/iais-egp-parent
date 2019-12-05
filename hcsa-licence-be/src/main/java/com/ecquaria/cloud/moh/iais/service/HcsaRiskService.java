package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskFinancialShowDto;
import org.springframework.stereotype.Service;

/**
 * @Author: jiahao
 * @Date: 2019/11/13 16:06
 */
@Service
public interface HcsaRiskService {
    RiskFinancialShowDto getfinancialShowDto();
    void saveDto(RiskFinancialShowDto dto);

}
