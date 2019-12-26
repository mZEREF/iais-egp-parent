package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskWeightageShowDto;

/**
 * @Author: jiahao
 * @Date: 2019/12/26 13:29
 */
public interface HcsaRiskWeightageService {
    HcsaRiskWeightageShowDto getWeightage();
    void getOneWdto(HcsaRiskWeightageDto wDto,String lastInp,String secLastInp,String finan,String leadership,String legislative,String inStartDate,String inEndDate);

}
