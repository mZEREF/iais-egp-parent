package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;

import java.util.List;
import java.util.Map;

/****
 *
 *   @date 10/29/2019
 *   @author zixian
 */
public interface AppGrpSvcRelatedInfoService {
    List<HcsaSvcPersonnelDto> getGOSelectInfo(String serviceId, String psnType);
    AppSvcCgoDto loadGovernanceOfficerByCgoId(String cgoId);
    List<HcsaSvcSubtypeOrSubsumedDto> loadLaboratoryDisciplines(String serviceId);
    Map loadCGOByDisciplines(List disciplines);
    List loadPO();
    List saveLaboratoryDisciplines(List checkLists);
}
