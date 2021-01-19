package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskFinanceMatrixDto;
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
    void getOneFinDto(HcsaRiskFinanceMatrixDto fin, String prsource, String prthershold,
                      String prleftmod, String prlefthigh, String prrightlow, String prrightmod,
                      String insource, String inthershold, String inleftmod, String inlefthigh, String inrightlow, String inrightmod,
                      String inStartDate, String inEndDate, String prStartDate, String prEndDate);
}
