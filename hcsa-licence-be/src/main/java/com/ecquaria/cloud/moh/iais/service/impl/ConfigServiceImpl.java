package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpeRoutingSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.ConfigService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
    @Override
    public List<HcsaServiceDto> getAllHcsaServices(HttpServletRequest request) {
        List<HcsaServiceDto> entity = hcsaConfigClient.allHcsaService().getEntity();
        request.getSession().setAttribute("hcsaServiceDtos",entity);
        return entity;
    }

    @Override
    public void viewPageInfo(HttpServletRequest request) {

        String crud_action_value = request.getParameter("crud_action_value");

        if(crud_action_value!=null){
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
        }

    }

    @Override
    public void editPageInfo(HttpServletRequest request) {


    }

    @Override
    public void saveOrUpdate(HttpServletRequest request) {


    try {
        HcsaServiceConfigDto hcsaServiceConfigDto = getDateOfHcsaService(request);
        hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto);

    }catch (Exception e){

    }

    }

    @Override
    public void addNewService(HttpServletRequest request) {
        List<String> list=new ArrayList<>();
        list.add("new application");
        list.add("appeal");
        list.add("renew");

        List<HcsaSvcRoutingStageDto> entity = hcsaConfigClient.stagelist().getEntity();
        request.setAttribute("routingStages",entity);
        request.setAttribute("applicationType",list);
    }

    private HcsaServiceConfigDto getDateOfHcsaService(HttpServletRequest request) throws ParseException {
        HcsaServiceConfigDto hcsaServiceConfigDto=new HcsaServiceConfigDto();
        HcsaServiceDto hcsaServiceDto =new HcsaServiceDto();

        List<HcsaSvcRoutingStageDto> entity = hcsaConfigClient.stagelist().getEntity();
        String serviceName = request.getParameter("serviceName");
        String description = request.getParameter("description");
        String displayDescription = request.getParameter("displayDescription");
        String serviceCode = request.getParameter("serviceCode");
        String serviceType = request.getParameter("ServiceType");
        String[] premisesTypes = request.getParameterValues("PremisesType");
        List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos=new ArrayList<>();
        for(String str:premisesTypes){
            HcsaSvcSpePremisesTypeDto hcsaSvcSpePremisesTypeDto=new HcsaSvcSpePremisesTypeDto();
            hcsaSvcSpePremisesTypeDto.setPremisesType(str);
            hcsaSvcSpePremisesTypeDto.setStatus("CMSTAT003");
            hcsaSvcSpePremisesTypeDtos.add(hcsaSvcSpePremisesTypeDto);
        }

        String manprincipalOfficer = request.getParameter("man-principalOfficer");
        String mixprincipalOfficer = request.getParameter("mix-principalOfficer");
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos=new ArrayList<>();
        HcsaSvcPersonnelDto poDto=new HcsaSvcPersonnelDto();
        poDto.setPsnType("PO");
        if(!StringUtil.isEmpty(manprincipalOfficer)){
            poDto.setMandatoryCount(Integer.parseInt(manprincipalOfficer));
        }
        if(!StringUtil.isEmpty(mixprincipalOfficer)){
            poDto.setMaximumCount(Integer.parseInt(mixprincipalOfficer));
        }
        poDto.setStatus("CMSTAT003");
        hcsaSvcPersonnelDtos.add(poDto);
        String mandeputyPrincipalOfficer = request.getParameter("man-DeputyPrincipalOfficer");
        String mixdeputyPrincipalOfficer = request.getParameter("mix-DeputyPrincipalOfficer");
        HcsaSvcPersonnelDto dpoDto=new HcsaSvcPersonnelDto();
        dpoDto.setPsnType("DPO");
        if(!StringUtil.isEmpty(mandeputyPrincipalOfficer)){
            dpoDto.setMandatoryCount(Integer.parseInt(mandeputyPrincipalOfficer));
        }
        if(!StringUtil.isEmpty(mixdeputyPrincipalOfficer)){
            dpoDto.setMaximumCount(Integer.parseInt(mixdeputyPrincipalOfficer));
        }
        dpoDto.setStatus("CMSTAT003");
        hcsaSvcPersonnelDtos.add(dpoDto);
        String manclinicalGovernanceOfficer = request.getParameter("man-ClinicalGovernanceOfficer");
        String mixclinicalGovernanceOfficer = request.getParameter("mix-ClinicalGovernanceOfficer");
        HcsaSvcPersonnelDto cgoDto=new HcsaSvcPersonnelDto();
        cgoDto.setPsnType("CGO");
        if(!StringUtil.isEmpty(manclinicalGovernanceOfficer)){
            cgoDto.setMandatoryCount(Integer.parseInt(manclinicalGovernanceOfficer));
        }
        if(!StringUtil.isEmpty(mixclinicalGovernanceOfficer)){
            cgoDto.setMaximumCount(Integer.parseInt(mixclinicalGovernanceOfficer));
        }
        cgoDto.setStatus("CMSTAT003");
        hcsaSvcPersonnelDtos.add(cgoDto);
        String manservicePersonnel = request.getParameter("man-ServicePersonnel");
        String mixservicePersonnel = request.getParameter("mix-ServicePersonnel");
        HcsaSvcPersonnelDto svcPersonnelDto=new HcsaSvcPersonnelDto();
        svcPersonnelDto.setPsnType("SVCPSN");
        if(!StringUtil.isEmpty(manservicePersonnel)){
            svcPersonnelDto.setMandatoryCount(Integer.valueOf(manservicePersonnel));
        }
        if(!StringUtil.isEmpty(mixservicePersonnel)){
            svcPersonnelDto.setMaximumCount(Integer.valueOf(mixservicePersonnel));
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
            if (!StringUtil.isEmpty(workloadManhours)) {
                hcsaSvcSpecificStageWorkloadDto.setManhourCount(Integer.parseInt(workloadManhours));
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


        hcsaServiceDto.setSvcName(serviceName);
        hcsaServiceDto.setSvcCode(serviceCode);
        hcsaServiceDto.setEffectiveDate(startDate);
        if(!StringUtil.isEmpty(endDate)){
             hcsaServiceDto.setEndDate(new SimpleDateFormat("dd/MM/yyyy").parse(endDate));
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
        String svcType = hcsaServiceDto.getSvcType();
        if(StringUtil.isEmpty(svcCode)){
            errorMap.put("svcCode","error");
        }
        if(StringUtil.isEmpty(svcName)){
            errorMap.put("svcName","error");
        }
        if(StringUtil.isEmpty(svcDesc)){
            errorMap.put("svcDesc","error");
        }
        if (StringUtil.isEmpty(svcType)) {
            errorMap.put("svcType","error");
        }

        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaServiceConfigDto.getHcsaSvcPersonnelDtos();
        for(int i=0;i<hcsaSvcPersonnelDtos.size();i++ ){
            String psnType = hcsaSvcPersonnelDtos.get(i).getPsnType();
            int mandatoryCount = hcsaSvcPersonnelDtos.get(i).getMandatoryCount();
            int maximumCount = hcsaSvcPersonnelDtos.get(i).getMaximumCount();
            if(StringUtil.isEmpty(psnType)){
                errorMap.put("psnType"+i,"error");
            }
            if(StringUtil.isEmpty(mandatoryCount)){
                errorMap.put("mandatoryCount"+i,"error");
            }
            if(StringUtil.isEmpty(maximumCount)){
                errorMap.put("maximumCount"+i,"error");
            }
        }

        List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos = hcsaServiceConfigDto.getHcsaSvcSpeRoutingSchemeDtos();
        for(int i=0;i<hcsaSvcSpeRoutingSchemeDtos.size();i++){

            String schemeType = hcsaSvcSpeRoutingSchemeDtos.get(i).getSchemeType();
            if(StringUtil.isEmpty(schemeType)){
                errorMap.put("schemeType"+i,"error");
            }
        }
        List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtos = hcsaServiceConfigDto.getHcsaSvcSpecificStageWorkloadDtos();
        for(int i=0;i<hcsaSvcSpecificStageWorkloadDtos.size();i++ ){
            Integer manhourCount = hcsaSvcSpecificStageWorkloadDtos.get(i).getManhourCount();
            if (StringUtil.isEmpty(manhourCount)){
                errorMap.put("manhourCount"+i,"error");
            }
        }


    }
}
