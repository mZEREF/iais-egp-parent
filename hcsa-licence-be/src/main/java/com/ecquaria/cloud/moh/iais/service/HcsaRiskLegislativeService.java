package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLegislativeMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLegislativeShowDto;

/**
 * @Author: jiahao
 * @Date: 2019/12/24 19:13
 */
public interface HcsaRiskLegislativeService {
    RiskLegislativeShowDto getLegShowDto();
    void getOneFinDto(HcsaRiskLegislativeMatrixDto leg,
                      String inthershold, String inleftmod, String inlefthigh, String inrightlow, String inrightmod,
                      String inStartDate, String inEndDate);
    void saveDto(RiskLegislativeShowDto legDto);
}
