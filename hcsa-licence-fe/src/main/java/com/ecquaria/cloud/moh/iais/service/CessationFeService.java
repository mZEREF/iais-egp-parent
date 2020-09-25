package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessatonConfirmDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppSpecifiedLicDto;
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

    List<String> filtrateSpecLicIds(List<String> licIds);

    void updateLicenceFe(List<String> licNos);

    Map<String, String> saveCessations(List<AppCessationDto> appCessationDtos,LoginContext loginContext);

    void saveRfiCessations(List<AppCessationDto> appCessationDtos,LoginContext loginContext,String rfiAppId) throws Exception;

    List<String> listHciName();

    List<AppSpecifiedLicDto> getSpecLicInfo(List<String> licIds);

    List<AppCessatonConfirmDto> getConfirmDto(List<AppCessationDto> appCessationDtos, Map<String, String> appIdPremisesMap, LoginContext loginContext) throws ParseException;

    boolean isGrpLicence(List<String> licIds);

    String getStageId(String serviceId,String appType);

    List<AppCessLicDto> initRfiData(String appId,String premiseId);


}
