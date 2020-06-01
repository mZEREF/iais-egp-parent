package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessatonConfirmDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author weilu
 * @date 2020/2/7 13:16
 */
public interface CessationFeService {


    List<String> getActiveLicence(List<String> licIds);

    List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds);

    List<AppCessLicDto> getOldCessationByIds(List<String> licIds);

    void updateLicenceFe(List<String> licNos);

    List<String> saveCessations(List<AppCessationDto> appCessationDtos,LoginContext loginContext);

    AppPremisesCorrelationDto getAppPreCorDto(String appId);

    List<String> listHciName();

    List<Boolean> listResultCeased(List<String> licIds);

    void sendEmail(String msgId, Date date, String svcName, String appGrpId, String licenseeId,String licNo) throws IOException, TemplateException;

    List<AppCessatonConfirmDto> getConfirmDto(List<AppCessationDto> appCessationDtos, List<String> appIds, LoginContext loginContext) throws Exception;
}
