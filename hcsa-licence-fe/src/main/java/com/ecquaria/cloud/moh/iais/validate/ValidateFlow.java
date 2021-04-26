package com.ecquaria.cloud.moh.iais.validate;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcClinicalDirectorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;

import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/4/23 13:10
 */
public interface ValidateFlow {
   default void doValidatePremises(Map<String,String> map, AppGrpPremisesDto appGrpPremisesDto,Integer index, String masterCodeDto){};

   default void doValidateAdressType(String floorNo,String blkNo,String unitNo,Integer index,Map<String ,String> map,List<String> errorName){};

   default void doValidateVehicles(Map<String,String>map,List<AppSvcVehicleDto> appSvcVehicleDtos){};

   default void doValidateClincalDirector(Map<String,String>map,List<AppSvcClinicalDirectorDto> appSvcClinicalDirectorDtos){};
}

