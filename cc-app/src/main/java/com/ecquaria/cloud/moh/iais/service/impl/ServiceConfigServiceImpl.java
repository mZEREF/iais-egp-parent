package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ServiceConfigServiceImpl
 *
 * @author suocheng
 * @date 10/14/2019
 */
@Service
public class ServiceConfigServiceImpl implements ServiceConfigService {
    @Override
    public List<HcsaServiceDto> getHcsaServiceDtosById(List<String> ids) {
        return RestApiUtil.postGetList(RestApiUrlConsts.GET_HCSA_SERVICE_BY_IDS, ids,HcsaServiceDto.class);
    }

    @Override
    public Set<String> getAppGrpPremisesTypeBySvcId(List<String> svcIds) {
        return RestApiUtil.postGetObject(RestApiUrlConsts.GET_PREMISES_TYPE_BY_ID, svcIds, Set.class);
    }

    @Override
    public PostCodeDto getPremisesByPostalCode(String searchField, String filterValue) {
        Map<String,Object> map = new HashMap<>();
        map.put("searchField", searchField);
        map.put("filterValue", filterValue);
        return RestApiUtil.getByReqParam(RestApiUrlConsts.POSTAL_CODE_INFO, map, PostCodeDto.class);
    }

    @Override
    public String getSvcIdBySvcCode(String svcCode) {
        Map<String,Object> map = new HashMap<>();
        map.put("code", svcCode);
        return RestApiUtil.getByReqParam(RestApiUrlConsts.SERVICEID_BY_SVCCODE, map, String.class);
    }

    @Override
    public List<AppGrpPremisesDto> getAppGrpPremisesDtoByLoginId(String loginId) {
        List<AppGrpPremisesDto> result = new ArrayList<>();
        AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
        appGrpPremisesDto.setId("123");
        appGrpPremisesDto.setPostalCode("019191");
        appGrpPremisesDto.setBlkNo("123");
        appGrpPremisesDto.setBuildingName("building Name");
        appGrpPremisesDto.setStreetName("String Name");
        appGrpPremisesDto.setFloorNo("6");
        appGrpPremisesDto.setUnitNo("3");
        appGrpPremisesDto.setPremisesType(ApplicationConsts.PREMISES_TYPE_ON_SITE);
        return result;
    }

    @Override
    public List<String> saveFileToRepo(List<MultipartFile> fileList) throws IOException {
        List<MultipartFile> multipartFileList = new ArrayList();
        multipartFileList.add(fileList.get(0));
        return RestApiUtil.saveFile(RestApiUrlConsts.URLREPO,multipartFileList,IaisEGPHelper.getCurrentAuditTrailDto());
    }

    @Override
    public Map<String,List<HcsaSvcDocConfigDto>> getAllHcsaSvcDocs(String serviceId) {
        //common doc
        Map<String,Object> common = new HashMap<>();
        //common.put("serviceId", "");==>serviceId null
        common.put("flag", false);//==>false =0
        List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigDtos = RestApiUtil.getListByReqParam(RestApiUrlConsts.HCSASVCDOCURL, common, HcsaSvcDocConfigDto.class);

        //premises doc
        Map<String,Object> premises = new HashMap<>();
        //premises.put("serviceId", "");==>serviceId null
        premises.put("flag", true);//==>true =1
        List<HcsaSvcDocConfigDto> premHcsaSvcDocConfigDtos = RestApiUtil.getListByReqParam(RestApiUrlConsts.HCSASVCDOCURL, premises, HcsaSvcDocConfigDto.class);
        Map<String,List<HcsaSvcDocConfigDto>> map = new HashMap<>();
        map.put("common", commonHcsaSvcDocConfigDtos);
        return map;
    }

    @Override
    public List<HcsaSvcSubtypeOrSubsumedDto> loadLaboratoryDisciplines(String serviceId) {
        Map<String,Object> map = new HashMap<>();
        map.put("svcId", serviceId);
        return RestApiUtil.getListByReqParam(RestApiUrlConsts.SVC_CHECKLIST_URL, map, HcsaSvcSubtypeOrSubsumedDto.class);
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
        //to do
        return null;
    }




}
