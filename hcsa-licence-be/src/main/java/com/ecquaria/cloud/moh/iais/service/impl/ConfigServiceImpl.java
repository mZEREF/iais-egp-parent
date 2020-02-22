package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpeRoutingSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.HcsaConfigPageDto;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ConfigService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Wenkang
 * @date 2020/2/12 17:36
 */
@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Override
    public List<HcsaServiceDto> getAllHcsaServices(HttpServletRequest request) {
        List<HcsaServiceDto> entity = hcsaConfigClient.allHcsaService().getEntity();
        request.getSession().setAttribute("hcsaServiceDtos",entity);
        return entity;
    }

    @Override
    public void viewPageInfo(HttpServletRequest request) {

        String crud_action_value = request.getParameter("crud_action_value");

        if(crud_action_value!=null&&!"".equals(crud_action_value)){
            List<String> list=new ArrayList<>();
            list.add(crud_action_value);
            List<HcsaServiceDto> entity = hcsaConfigClient.getHcsaServiceDtoByCode(list).getEntity();
            HcsaServiceDto hcsaServiceDto = entity.get(0);
            request.getSession().setAttribute("hcsaServiceDto",hcsaServiceDto);
            String id = hcsaServiceDto.getId();
            List<String> ids=new ArrayList<>();
            ids.add(id);
            Set<String> set = hcsaConfigClient.getAppGrpPremisesTypeBySvcId(ids).getEntity();
            request.getSession().setAttribute("PremisesType",set);
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaConfigClient.getSvcPersonnelByServiceId(id).getEntity();
            for(HcsaSvcPersonnelDto hcsaSvcPersonnelDto:hcsaSvcPersonnelDtos){

                String psnType = hcsaSvcPersonnelDto.getPsnType();
                request.getSession().setAttribute(psnType,hcsaSvcPersonnelDto);
            }
            List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = hcsaConfigClient.stagelist().getEntity();
            for(int i=0;i<hcsaSvcRoutingStageDtos.size();i++ ){
                String stageOrder = hcsaSvcRoutingStageDtos.get(i).getStageOrder();
                try {
                    if(Integer.valueOf(stageOrder)%100!=0){
                        hcsaSvcRoutingStageDtos.remove(i);
                        i--;
                    }
                }catch (Exception e){

                }
            }
            List<HcsaSvcStageWorkloadDto> hcsaSvcStageWorkloadDtos =
                    hcsaConfigClient.getHcsaSvcSpeRoutingSchemeByServiceId(hcsaServiceDto.getId()).getEntity();
                List<String> stageIds=new ArrayList<>();

            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = hcsaConfigClient.getHcsaSvcWorkingGroupByStages(stageIds).getEntity();
            for(HcsaSvcStageWorkingGroupDto hcsaConfigPageDtos:hcsaSvcStageWorkingGroupDtos){
                String stageId = hcsaConfigPageDtos.getStageId();
                String stageWorkGroupId = hcsaConfigPageDtos.getStageWorkGroupId();

            }

            List<WorkingGroupDto> hcsa = organizationClient.getWorkingGroup("hcsa").getEntity();
            for(WorkingGroupDto every:hcsa){
                String workingGropId = every.getId();

            }

            /*           List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = hcsaConfigClient.getHcsaStageWorkingGroup(hcsaServiceDto.getId()).getEntity();*/

        List<HcsaConfigPageDto> hcsaConfigPageDtos=new ArrayList<>();
        for(HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto:hcsaSvcRoutingStageDtos){

            HcsaConfigPageDto hcsaConfigPageDto=new HcsaConfigPageDto();
            hcsaConfigPageDto.setStageCode(hcsaSvcRoutingStageDto.getStageCode());

            hcsaConfigPageDto.setStageName(hcsaSvcRoutingStageDto.getStageName());
            for(int i=0;i<hcsaSvcStageWorkloadDtos.size();i++ ){
                String stageId = hcsaSvcStageWorkloadDtos.get(i).getStageId();
                Integer manhourCount = hcsaSvcStageWorkloadDtos.get(i).getManhourCount();
                String appType = hcsaSvcStageWorkloadDtos.get(i).getAppType();
                if ("APTY002".equals(appType)) {
                        String id1 = hcsaSvcRoutingStageDto.getId();
                        if(id1.equals(stageId)){
                            hcsaConfigPageDto.setManhours(manhourCount);
                            hcsaConfigPageDto.setAppType("APTY002");
                            hcsaConfigPageDto.setWorkloadId(hcsaSvcStageWorkloadDtos.get(i).getId());
                            hcsaConfigPageDto.setRoutingSchemeId(hcsaSvcStageWorkloadDtos.get(i).getId());
                        }

                }
            }
            hcsaConfigPageDtos.add(hcsaConfigPageDto);
        }

            request.setAttribute("routingStages",hcsaConfigPageDtos);
        }


    }

    @Override
    public void editPageInfo(HttpServletRequest request) {


    }

    @Override
    public void saveOrUpdate(HttpServletRequest request) {
        Map<String,String> errorMap=new HashMap<>();

        HcsaServiceConfigDto hcsaServiceConfigDto = getDateOfHcsaService(request);
        doValidate(hcsaServiceConfigDto,errorMap);
        if(!errorMap.isEmpty()){
            HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
            List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = hcsaServiceConfigDto.getHcsaSvcSpePremisesTypeDtos();
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaServiceConfigDto.getHcsaSvcPersonnelDtos();
            List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtos = hcsaServiceConfigDto.getHcsaSvcSpecificStageWorkloadDtos();
            for(HcsaSvcSpecificStageWorkloadDto hcsaSvcSpecificStageWorkloadDto:hcsaSvcSpecificStageWorkloadDtos){
                Integer manhourCount = hcsaSvcSpecificStageWorkloadDto.getManhourCount();

            }

            for(HcsaSvcPersonnelDto hcsaSvcPersonnelDto:hcsaSvcPersonnelDtos){
                String psnType = hcsaSvcPersonnelDto.getPsnType();
                request.setAttribute(psnType,hcsaSvcPersonnelDto);
            }
            Set<String> premisesSet=new HashSet<>();
            for(HcsaSvcSpePremisesTypeDto hcsaSvcSpePremisesTypeDto :hcsaSvcSpePremisesTypeDtos){
                premisesSet.add(hcsaSvcSpePremisesTypeDto.getPremisesType());
            }
            request.setAttribute("PremisesType",premisesSet);
            request.setAttribute("hcsaServiceDto",hcsaServiceDto);
            request.setAttribute("crud_action_type","dovalidate");
            request.setAttribute("errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            return;
        }
        String crud_action_value = request.getParameter("crud_action_value");
        if("update".equals(crud_action_value)){
            hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto);
        }
      /*  hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto);*/
        request.setAttribute("crud_action_type","save");
    }




    @Override
    public void addNewService(HttpServletRequest request) {
        List<String> list=new ArrayList<>();
        list.add("new application");
        list.add("appeal");
        list.add("renew");

        List<HcsaSvcRoutingStageDto> entity = hcsaConfigClient.stagelist().getEntity();

        for(int i=0;i<entity.size();i++ ){
            String stageOrder = entity.get(i).getStageOrder();
            try {
                if(Integer.valueOf(stageOrder)%100!=0){
                    entity.remove(i);
                    i--;
                }
            }catch (Exception e){

            }
        }

        List<WorkingGroupDto> hcsa = organizationClient.getWorkingGroup("hcsa").getEntity();
        for(WorkingGroupDto every:hcsa){

        }

        request.setAttribute("routingStages",entity);
        request.setAttribute("applicationType",list);
    }

    private HcsaServiceConfigDto getDateOfHcsaService(HttpServletRequest request) {
        HcsaServiceConfigDto hcsaServiceConfigDto=new HcsaServiceConfigDto();
        HcsaServiceDto hcsaServiceDto =new HcsaServiceDto();

        List<HcsaSvcRoutingStageDto> entity = hcsaConfigClient.stagelist().getEntity();
        for(int i=0;i<entity.size();i++ ){
            String stageOrder = entity.get(i).getStageOrder();
            try {
                if(Integer.valueOf(stageOrder)%100!=0){
                    entity.remove(i);
                    i--;
                }
            }catch (Exception e){

            }
        }
        String serviceId = request.getParameter("serviceId");
        String serviceName = request.getParameter("serviceName");
        String description = request.getParameter("description");
        String displayDescription = request.getParameter("displayDescription");
        String serviceCode = request.getParameter("serviceCode");
        String serviceType = request.getParameter("ServiceType");
        String[] premisesTypes = request.getParameterValues("PremisesType");
        List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos=new ArrayList<>();
        if(premisesTypes!=null){
            for(String str:premisesTypes){
                HcsaSvcSpePremisesTypeDto hcsaSvcSpePremisesTypeDto=new HcsaSvcSpePremisesTypeDto();
                hcsaSvcSpePremisesTypeDto.setPremisesType(str);
                hcsaSvcSpePremisesTypeDto.setStatus("CMSTAT003");
                hcsaSvcSpePremisesTypeDtos.add(hcsaSvcSpePremisesTypeDto);
            }
        }

        String manprincipalOfficer = request.getParameter("man-principalOfficer");
        String mixprincipalOfficer = request.getParameter("mix-principalOfficer");
        String poId = request.getParameter("poId");
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos=new ArrayList<>();
        HcsaSvcPersonnelDto poDto=new HcsaSvcPersonnelDto();
        poDto.setPsnType("PO");
        if(!StringUtil.isEmpty(manprincipalOfficer)){
            poDto.setMandatoryCount(Integer.parseInt(manprincipalOfficer));
        }
        if(!StringUtil.isEmpty(mixprincipalOfficer)){
            poDto.setMaximumCount(Integer.parseInt(mixprincipalOfficer));
        }
        if(!StringUtil.isEmpty(poId)){
            poDto.setServiceId(poId);
        }
        poDto.setStatus("CMSTAT003");
        hcsaSvcPersonnelDtos.add(poDto);
        String dpoId = request.getParameter("dpoId");
        String mandeputyPrincipalOfficer = request.getParameter("man-DeputyPrincipalOfficer");
        String mixdeputyPrincipalOfficer = request.getParameter("mix-DeputyPrincipalOfficer");
        HcsaSvcPersonnelDto dpoDto=new HcsaSvcPersonnelDto();
        if(!StringUtil.isEmpty(dpoId)){
            dpoDto.setId(dpoId);
        }
        dpoDto.setPsnType("DPO");
        if(!StringUtil.isEmpty(mandeputyPrincipalOfficer)){
            dpoDto.setMandatoryCount(Integer.parseInt(mandeputyPrincipalOfficer));
        }
        if(!StringUtil.isEmpty(mixdeputyPrincipalOfficer)){
            dpoDto.setMaximumCount(Integer.parseInt(mixdeputyPrincipalOfficer));
        }
        dpoDto.setStatus("CMSTAT003");
        hcsaSvcPersonnelDtos.add(dpoDto);
        String cgoId = request.getParameter("cgoId");
        String manclinicalGovernanceOfficer = request.getParameter("man-ClinicalGovernanceOfficer");
        String mixclinicalGovernanceOfficer = request.getParameter("mix-ClinicalGovernanceOfficer");
        HcsaSvcPersonnelDto cgoDto=new HcsaSvcPersonnelDto();
        if(!StringUtil.isEmpty(cgoId)){
            cgoDto.setId(cgoId);
        }
        cgoDto.setPsnType("CGO");
        if(!StringUtil.isEmpty(manclinicalGovernanceOfficer)){
            cgoDto.setMandatoryCount(Integer.parseInt(manclinicalGovernanceOfficer));
        }
        if(!StringUtil.isEmpty(mixclinicalGovernanceOfficer)){
            cgoDto.setMaximumCount(Integer.parseInt(mixclinicalGovernanceOfficer));
        }
        cgoDto.setStatus("CMSTAT003");
        hcsaSvcPersonnelDtos.add(cgoDto);
        String svcpsnId = request.getParameter("svcpsnId");
        String manservicePersonnel = request.getParameter("man-ServicePersonnel");
        String mixservicePersonnel = request.getParameter("mix-ServicePersonnel");
        HcsaSvcPersonnelDto svcPersonnelDto=new HcsaSvcPersonnelDto();
        if(!StringUtil.isEmpty(svcpsnId)){
            svcPersonnelDto.setId(svcpsnId);
        }
        svcPersonnelDto.setPsnType("SVCPSN");
        try {
            if(!StringUtil.isEmpty(manservicePersonnel)){
                svcPersonnelDto.setMandatoryCount(Integer.valueOf(manservicePersonnel));
            }
            if(!StringUtil.isEmpty(mixservicePersonnel)){
                svcPersonnelDto.setMaximumCount(Integer.valueOf(mixservicePersonnel));
            }
        }catch (Exception e){

            svcPersonnelDto.setMaximumCount(-1);
            svcPersonnelDto.setMandatoryCount(-1);
        }

        svcPersonnelDto.setStatus("CMSTAT003");
        hcsaSvcPersonnelDtos.add(svcPersonnelDto);
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos=new ArrayList<>();
        String numberDocument = request.getParameter("NumberDocument");
        String descriptionDocument = request.getParameter("DescriptionDocument");
        String numberfields = request.getParameter("Numberfields");
        String descriptionGeneral = request.getParameter("DescriptionGeneral");

        List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtoList=new ArrayList<>();
        List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtoList=new ArrayList<>();
        for(HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto:entity){
            HcsaSvcSpecificStageWorkloadDto hcsaSvcSpecificStageWorkloadDto =new HcsaSvcSpecificStageWorkloadDto();
            HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto=new HcsaSvcSpeRoutingSchemeDto();
            String stageCode = hcsaSvcRoutingStageDto.getStageCode();
            String id = hcsaSvcRoutingStageDto.getId();
            String routingScheme = request.getParameter("RoutingScheme"+stageCode);
            String workloadManhours = request.getParameter("WorkloadManhours"+stageCode);
            String stageId=  request.getParameter("stageId"+stageCode);
            if (!StringUtil.isEmpty(workloadManhours)) {
                hcsaSvcSpecificStageWorkloadDto.setManhourCount(Integer.parseInt(workloadManhours));
            }
            if(!StringUtil.isEmpty(stageId)){
                hcsaSvcSpeRoutingSchemeDto.setId(stageId);
            }
            hcsaSvcSpeRoutingSchemeDto.setSchemeType(routingScheme);
            hcsaSvcSpeRoutingSchemeDto.setAppType("APTY002");
            hcsaSvcSpeRoutingSchemeDto.setStatus("CMSTAT003");
            hcsaSvcSpecificStageWorkloadDto.setStageId(id);
            hcsaSvcSpecificStageWorkloadDto.setAppType("APTY002");
            hcsaSvcSpecificStageWorkloadDto.setStatus("CMSTAT003");
            hcsaSvcSpecificStageWorkloadDtoList.add(hcsaSvcSpecificStageWorkloadDto);
            hcsaSvcSpeRoutingSchemeDtoList.add(hcsaSvcSpeRoutingSchemeDto);

        }

        String[] steps = request.getParameterValues("step");
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos=new ArrayList<>();
        for(int i=0;i<steps.length;i++ ){
            HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto=new HcsaServiceStepSchemeDto();
            hcsaServiceStepSchemeDto.setStatus("CMSTAT003");
            hcsaServiceStepSchemeDto.setSeqNum(i);
            hcsaServiceStepSchemeDto.setStepCode(steps[i]);
            hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
        }

        String parameter = request.getParameter("Sub-Types");
        String startDate = request.getParameter("StartDate");
        String endDate = request.getParameter("EndDate");

        if (!StringUtil.isEmpty(serviceId)) {
            hcsaServiceDto.setId(serviceId);
        }
        hcsaServiceDto.setSvcName(serviceName);
        hcsaServiceDto.setSvcCode(serviceCode);
        hcsaServiceDto.setEffectiveDate(startDate);
        if(!StringUtil.isEmpty(endDate)){
            try {
                hcsaServiceDto.setEndDate(new SimpleDateFormat("dd/MM/yyyy").parse(endDate));
            } catch (ParseException e) {
                hcsaServiceDto.setEndDate(new Date("1976-01-01"));
            }
        }

        hcsaServiceDto.setSvcDisplayDesc(displayDescription);
        hcsaServiceDto.setSvcDesc( description);
        hcsaServiceDto.setSvcType(serviceType);
        hcsaServiceDto.setStatus("CMSTAT003");
        hcsaServiceDto.setVersion("1");
        hcsaServiceConfigDto.setHcsaSvcSpePremisesTypeDtos(hcsaSvcSpePremisesTypeDtos);
        hcsaServiceConfigDto.setHcsaSvcDocConfigDtos(hcsaSvcDocConfigDtos);
        hcsaServiceConfigDto.setHcsaSvcPersonnelDtos(hcsaSvcPersonnelDtos);
        hcsaServiceConfigDto.setHcsaServiceDto(hcsaServiceDto);
        hcsaServiceConfigDto.setHcsaSvcSpeRoutingSchemeDtos(hcsaSvcSpeRoutingSchemeDtoList);
        hcsaServiceConfigDto.setHcsaSvcSpecificStageWorkloadDtos(hcsaSvcSpecificStageWorkloadDtoList);
        hcsaServiceConfigDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
            return hcsaServiceConfigDto;
    }


    private void doValidate(HcsaServiceConfigDto hcsaServiceConfigDto, Map<String,String> errorMap){
        HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
        String svcCode = hcsaServiceDto.getSvcCode();
        String svcName = hcsaServiceDto.getSvcName();
        String svcDesc = hcsaServiceDto.getSvcDesc();
        String svcDisplayDesc = hcsaServiceDto.getSvcDisplayDesc();
        String svcType = hcsaServiceDto.getSvcType();
        String effectiveDate = hcsaServiceDto.getEffectiveDate();
        if(StringUtil.isEmpty(effectiveDate)){
            errorMap.put("effectiveDate","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(svcCode)){
            errorMap.put("svcCode","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(svcName)){
            errorMap.put("svcName","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(svcDisplayDesc)){
            errorMap.put("svcDisplayDesc","UC_CHKLMD001_ERR001");
        }
        if(StringUtil.isEmpty(svcDesc)){
            errorMap.put("svcDesc","UC_CHKLMD001_ERR001");
        }
        if (StringUtil.isEmpty(svcType)) {
            errorMap.put("svcType","UC_CHKLMD001_ERR001");
        }
        List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = hcsaServiceConfigDto.getHcsaSvcSpePremisesTypeDtos();
        if(hcsaSvcSpePremisesTypeDtos.isEmpty()){
            errorMap.put("premieseType","UC_CHKLMD001_ERR001");
        }
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaServiceConfigDto.getHcsaSvcPersonnelDtos();
        for(int i=0;i<hcsaSvcPersonnelDtos.size();i++ ){
            String psnType = hcsaSvcPersonnelDtos.get(i).getPsnType();
            int mandatoryCount = hcsaSvcPersonnelDtos.get(i).getMandatoryCount();
            int maximumCount = hcsaSvcPersonnelDtos.get(i).getMaximumCount();
            if(StringUtil.isEmpty(psnType)){
                errorMap.put("psnType"+i,"UC_CHKLMD001_ERR001");
            }
            if(StringUtil.isEmpty(mandatoryCount)){
                errorMap.put("mandatoryCount"+i,"UC_CHKLMD001_ERR001");
            }
            if(StringUtil.isEmpty(maximumCount)){
                errorMap.put("maximumCount"+i,"UC_CHKLMD001_ERR001");
            }
        }

        List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos = hcsaServiceConfigDto.getHcsaSvcSpeRoutingSchemeDtos();
        for(int i=0;i<hcsaSvcSpeRoutingSchemeDtos.size();i++){

            String schemeType = hcsaSvcSpeRoutingSchemeDtos.get(i).getSchemeType();
            if(StringUtil.isEmpty(schemeType)){
                errorMap.put("schemeType"+i,"UC_CHKLMD001_ERR001");
            }
        }
        List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtos = hcsaServiceConfigDto.getHcsaSvcSpecificStageWorkloadDtos();
        for(int i=0;i<hcsaSvcSpecificStageWorkloadDtos.size();i++ ){
            Integer manhourCount = hcsaSvcSpecificStageWorkloadDtos.get(i).getManhourCount();
            if (StringUtil.isEmpty(manhourCount)){
                errorMap.put("manhourCount"+i,"UC_CHKLMD001_ERR001");
            }
        }

        Boolean entity = hcsaConfigClient.isExistHcsaService(hcsaServiceDto).getEntity();
        if(!entity){

        }
    }
}
