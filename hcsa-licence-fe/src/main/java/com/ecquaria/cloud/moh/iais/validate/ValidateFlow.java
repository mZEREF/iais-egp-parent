package com.ecquaria.cloud.moh.iais.validate;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;

import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/4/23 13:10
 */
public interface ValidateFlow {
    Map<String, String> doValidatePremises(Map<String,String> map, AppGrpPremisesDto appGrpPremisesDto,Integer index);

}

