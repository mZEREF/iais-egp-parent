package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloudfeign.FeignException;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author weilu
 * @date 2020/2/26 16:27
 */
public interface CessationService {

    List<String> getActiveLicence(List<String> licIds);

    List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds);

    List<String> saveCessations(List<AppCessationDto> appCessationDtos);

    void updateCesation(List<AppCessationDto> appCessationDtos);

    void updateLicence(List<String> licNos);

    void routingTaskToAo3(List<ApplicationDto> applicationDtos, LoginContext loginContext) throws FeignException;

    List<String> listLicIdsCeased(List<String> licIds);

    List<String> listHciName();

    void sendEmail(String msgId, Date date,String svcName,String appGrpId,String licenseeId) throws IOException, TemplateException;
}
