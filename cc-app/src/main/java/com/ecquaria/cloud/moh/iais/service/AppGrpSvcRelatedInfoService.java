package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.dto.TestDto;

import java.util.List;
import java.util.Map;

/****
 *
 *   @date 10/29/2019
 *   @author zixian
 */
public interface AppGrpSvcRelatedInfoService {
    List<HcsaSvcPersonnelDto> loadCGOBySvcIdAndPsnType(String serviceId, String psnType);
    AppSvcCgoDto loadGovernanceOfficerByCgoId(String cgoId);
    List<TestDto> loadLaboratoryDisciplines(String str);
    Map loadCGOByDisciplines(List disciplines);
    List loadPO();
    List saveLaboratoryDisciplines(List checkLists);

}
