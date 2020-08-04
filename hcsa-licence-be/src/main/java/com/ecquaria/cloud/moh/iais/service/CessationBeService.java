package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessatonConfirmDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppSpecifiedLicDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloudfeign.FeignException;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/2/26 16:27
 */
public interface CessationBeService {

    List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds);

    List<String> saveCessations(List<AppCessationDto> appCessationDtos,String licenseeId);

    Map<String,Boolean> listResultCeased(List<String> licIds);

    List<String> listHciName();

    List<AppSpecifiedLicDto> getSpecLicInfo(List<String> licIds);

    void sendEmail(String msgId, Date date,String svcName,String appGrpId,String licenseeId,String licNo) throws IOException, TemplateException;

    List<AppCessatonConfirmDto> getConfirmDto(List<AppCessationDto> appCessationDtos, List<String> appIds, LoginContext loginContext) throws Exception;

    boolean isGrpLicence(List<String> licIds);
}
