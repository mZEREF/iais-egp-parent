package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessatonConfirmDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppSpecifiedLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/2/7 13:16
 */
public interface CessationFeService {

    List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds);

    void updateLicenceFe(List<String> licNos);

    Map<String, String>  saveCessations(List<AppCessationDto> appCessationDtos,LoginContext loginContext);

    List<String> listHciName();

    List<AppSpecifiedLicDto> getSpecLicInfo(List<String> licIds);

    List<AppCessatonConfirmDto> getConfirmDto(List<AppCessationDto> appCessationDtos, Map<String, String> appIdPremisesMap, LoginContext loginContext) throws ParseException;

    boolean isGrpLicence(List<String> licIds);

    String getStageId(String serviceId,String appType);
}
