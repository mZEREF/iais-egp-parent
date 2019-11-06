package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
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
    //load Laboratory Disciplines page
    private static final String GET_DISCIPLINE_URL = "";
    private static final String POST_DISCIPLINE_URL = "";
    private static final String GET_CGO_URL = "";
    private static final String GET_PO_URL = "";
    private static final String GET_CGO = "hcsa-config/service-type";

    @Override
    public List loadLaboratoryDisciplines(String str) {
        Map<String,Object> map = new HashMap<>();
        //List list= RestApiUtil.getListByReqParam(GET_DISCIPLINE_URL, map, TestDto.class);

        return null;
    }

    @Override
    public Map loadCGOByDisciplines(List disciplines) {
        Map<String,Object> map = new HashMap<>();
        //List<TestDto> list= RestApiUtil.getListByReqParam(GET_CGO_URL, map, TestDto.class);
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
        Map<String,Object> map = new HashMap<>();
        List poList =RestApiUtil.getListByReqParam(GET_PO_URL, map, String.class);
        List list = new ArrayList();
        list.add("po1");
        list.add("po2");
        return list;
    }

    @Override
    public List saveLaboratoryDisciplines(List checkLists) {
        RestApiUtil.save(POST_DISCIPLINE_URL, checkLists);
        return null;
    }

    @Override
    public List<HcsaSvcPersonnelDto> loadCGOBySvcIdAndPsnType(String serviceId, String psnType) {
        Map<String,Object> map = new HashMap<>();
        map.put("serviceId", serviceId);
        map.put("psnType", "CGO");
        return RestApiUtil.getListByReqParam(GET_CGO,map, HcsaSvcPersonnelDto.class);
    }

    @Override
    public AppSvcCgoDto loadGovernanceOfficerByCgoId(String cgoId) {


        return null;
    }
}
