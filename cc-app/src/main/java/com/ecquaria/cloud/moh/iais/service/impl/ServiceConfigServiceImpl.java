package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * ServiceConfigServiceImpl
 *
 * @author suocheng
 * @date 10/14/2019
 */
@Service
public class ServiceConfigServiceImpl implements ServiceConfigService {
    @Autowired
    private FileRepoClient fileRepoClient;
    @Autowired
    private AppConfigClient appConfigClient;
    @Autowired
    private SystemAdminClient systemAdminClient;
    @Override
    public List<HcsaServiceDto> getHcsaServiceDtosById(List<String> ids) {

        return   appConfigClient.getHcsaService(ids).getEntity();
    }

    @Override
    public Set<String> getAppGrpPremisesTypeBySvcId(List<String> svcIds) {

        return   appConfigClient.getAppGrpPremisesTypeBySvcId(svcIds).getEntity();
    }

    @Override
    public PostCodeDto getPremisesByPostalCode(String searchField, String filterValue) {
        Map<String,Object> map = new HashMap<>();
        map.put("searchField", searchField);
        map.put("filterValue", filterValue);

        return   systemAdminClient.getPostCodeByCode(searchField,filterValue).getEntity();
    }

    @Override
    public String getSvcIdBySvcCode(String svcCode) {
        Map<String,Object> map = new HashMap<>();
        map.put("code", svcCode);

        return   appConfigClient.getServiceIdByCode(svcCode).getEntity();
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
        MultipartFile file = fileList.get(0);
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        String auditTrailStr = JsonUtil.parseToJson(auditTrailDto);
        FeignResponseEntity<String> re = fileRepoClient.saveFiles(file, auditTrailStr);
        List<String> idList = new ArrayList<>();
        if (re.getStatusCode() == HttpStatus.SC_OK) {
            String str = re.getEntity();
            idList.add(str);
        }
        return idList;
    }

    @Override
    public Map<String,List<HcsaSvcDocConfigDto>> getAllHcsaSvcDocs(String serviceId) {
        //common doc
        Map<String,Object> common = new HashMap<>();
        //common.put("serviceId", "");==>serviceId null
        common.put("flag", false);//==>false =0

        List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigDtos =  appConfigClient.getHcsaSvcDocConfig(serviceId,false).getEntity();

        //premises doc
        Map<String,Object> premises = new HashMap<>();
        //premises.put("serviceId", "");==>serviceId null
        premises.put("flag", true);//==>true =1
        List<HcsaSvcDocConfigDto> premHcsaSvcDocConfigDtos = appConfigClient.getHcsaSvcDocConfig(serviceId,true).getEntity();

        Map<String,List<HcsaSvcDocConfigDto>> map = new HashMap<>();
        map.put("common", commonHcsaSvcDocConfigDtos);
        return map;
    }

    @Override
    public List<HcsaSvcSubtypeOrSubsumedDto> loadLaboratoryDisciplines(String serviceId) {
        Map<String,Object> map = new HashMap<>();
        map.put("svcId", serviceId);

        return appConfigClient.listSubCorrelation(serviceId).getEntity();
    }


    @Override
    public List<HcsaSvcPersonnelDto> getGOSelectInfo(String serviceId, String psnType) {
        Map<String,Object> map = new HashMap<>();
        map.put("serviceId", serviceId);
        map.put("psnType", psnType);

        return  appConfigClient.getServiceType(serviceId,psnType).getEntity();
    }

    @Override
    public AppSvcCgoDto loadGovernanceOfficerByCgoId(String cgoId) {
        //to do
        return null;
    }




}
