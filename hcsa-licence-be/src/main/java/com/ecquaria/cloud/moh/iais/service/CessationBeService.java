package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessatonConfirmDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppSpecifiedLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/2/26 16:27
 */
public interface CessationBeService {

    List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds);

    Map<String, String> saveCessations(List<AppCessationDto> appCessationDtos);

    Map<String,Boolean> listResultCeased(List<String> licIds);

    List<String> listHciName();

    List<AppSpecifiedLicDto> getSpecLicInfo(List<String> licIds);

    List<String> filtrateSpecLicIds(List<String> licIds);

    List<AppCessatonConfirmDto> getConfirmDto(List<AppCessationDto> appCessationDtos, Map<String, String> appIdPremisesMap , LoginContext loginContext) throws Exception;

    boolean isGrpLicence(List<String> licIds);

    List<AppCessLicDto> initData (String corrId);

    void saveRfiCessation(List<AppCessationDto> appCessationDtos, TaskDto taskDto, LoginContext loginContext);


}
