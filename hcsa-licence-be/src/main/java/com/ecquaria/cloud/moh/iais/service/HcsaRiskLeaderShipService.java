package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskLeadershipMatrixDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskLeaderShipShowDto;

/**
 * @Author: jiahao
 * @Date: 2019/12/21 17:50
 */
public interface HcsaRiskLeaderShipService {
    RiskLeaderShipShowDto getLeaderShowDto();
    void saveDto(RiskLeaderShipShowDto dto);
    void getOneFinDto(HcsaRiskLeadershipMatrixDto lea, String prsource, String prthershold,
                      String prleftmod, String prlefthigh, String prrightlow, String prrightmod,
                      String insource, String inthershold, String inleftmod, String inlefthigh, String inrightlow, String inrightmod,
                      String inStartDate, String inEndDate, String prStartDate, String prEndDate);
}
