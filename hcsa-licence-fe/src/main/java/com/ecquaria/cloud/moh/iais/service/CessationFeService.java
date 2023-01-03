package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessatonConfirmDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/2/7 13:16
 */
public interface CessationFeService {

    List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds);


    void updateLicenceFe(List<String> licNos);

    Map<String, List<String>> saveCessations(List<AppCessationDto> appCessationDtos,LoginContext loginContext);

    void saveRfiCessations(List<AppCessationDto> appCessationDtos,LoginContext loginContext,String rfiAppId) throws Exception;

    List<String> listHciName();


    List<AppCessatonConfirmDto> getConfirmDto(List<AppCessationDto> appCessationDtos, Map<String, List<String>> appIdPremisesMap, LoginContext loginContext) throws ParseException;

    boolean isGrpLicence(List<String> licIds);


    List<AppCessLicDto> initRfiData(String appId,String premiseId);

    PremisesDto getPremiseByHciCodeName(String hciNameCode);
}
