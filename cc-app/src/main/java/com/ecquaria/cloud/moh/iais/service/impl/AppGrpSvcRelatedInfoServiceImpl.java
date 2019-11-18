package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.AppGrpSvcRelatedInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/****
 *
 *   @date 10/29/2019
 *   @author zixian
 */
@Service
@Slf4j
public class AppGrpSvcRelatedInfoServiceImpl implements AppGrpSvcRelatedInfoService {

    @Override
    public List<HcsaSvcSubtypeOrSubsumedDto> loadLaboratoryDisciplines(String serviceId) {
        Map<String,Object> map = new HashMap<>();
        map.put("svcId", "AA1A7D00-2AEB-E911-BE76-000C29C8FBE4");
        return RestApiUtil.getListByReqParam(RestApiUrlConsts.SVC_CHECKLIST_URL, map, HcsaSvcSubtypeOrSubsumedDto.class);
    }

    @Override
    public Map loadCGOByDisciplines(List disciplines) {
        Map<String,Object> map = new HashMap<>();
        Map<String,List<SelectOption>> map2 = new HashMap<>();
        List<SelectOption> list1 = new ArrayList<>();
        SelectOption sp1 = new SelectOption("1", "Option1");
        list1.add(sp1);
        SelectOption sp2 = new SelectOption("2", "Option2");
        list1.add(sp2);
        map.put("Cytology", list1);

        List<SelectOption> list2 = new ArrayList<>();
        SelectOption sp3 = new SelectOption("1", "Option1");
        list1.add(sp3);
        SelectOption sp4 = new SelectOption("2", "Option2");
        list1.add(sp4);
        map.put("HIV Testing", list2);

        return map2;
    }


    @Override
    public List loadPO() {
        List list = new ArrayList();
        list.add("po1");
        list.add("po2");
        return list;
    }


    @Override
    public List<HcsaSvcPersonnelDto> getGOSelectInfo(String serviceId, String psnType) {
        Map<String,Object> map = new HashMap<>();
        map.put("serviceId", serviceId);
        map.put("psnType", psnType);
        return RestApiUtil.getListByReqParam(RestApiUrlConsts.SVC_CGO_URL,map, HcsaSvcPersonnelDto.class);
    }

    @Override
    public AppSvcCgoDto loadGovernanceOfficerByCgoId(String cgoId) {


        return null;
    }
}
