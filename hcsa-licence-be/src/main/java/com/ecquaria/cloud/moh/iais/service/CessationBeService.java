package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessatonConfirmDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.EmailAuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloudfeign.FeignException;

import java.util.List;
import java.util.Map;

/**
 * @author weilu
 * @date 2020/2/26 16:27
 */
public interface CessationBeService {

    List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds);

    Map<String, List<String>> saveCessations(List<AppCessationDto> appCessationDtos);

    Map<String,Boolean> listResultCeased(List<String> licIds);

    List<String> listHciName();

    SearchResult<EmailAuditTrailDto> auditList(SearchParam searchParam);

    List<AppCessatonConfirmDto> getConfirmDto(List<AppCessationDto> appCessationDtos, Map<String, List<String>> appIdPremisesMap , LoginContext loginContext) throws Exception;

    boolean isGrpLicence(List<String> licIds);

    List<AppCessLicDto> initData (String corrId);

    void saveRfiCessation(AppCessationDto appCessationDto, TaskDto taskDto, LoginContext loginContext) throws FeignException;

    PremisesDto getPremiseByHciCodeName(String hciNameCode);

}
