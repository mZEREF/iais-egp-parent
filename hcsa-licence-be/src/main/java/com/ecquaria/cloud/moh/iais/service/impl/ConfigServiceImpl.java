package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCategoryDto;
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
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.HcsaConfigPageDto;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ConfigService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.list.AbstractLinkedList;
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
        request.getSession().setAttribute("hcsaServiceDtos", entity);
        return entity;
    }

    @Override
    public void viewPageInfo(HttpServletRequest request) {

        String crud_action_value = request.getParameter("crud_action_value");
        String crud_action_type = request.getParameter("crud_action_type");
        if (crud_action_value != null && !"".equals(crud_action_value) && "edit".equals(crud_action_type)) {
            view(request, crud_action_value);
        }

    }

    @Override
    public void editPageInfo(HttpServletRequest request) {


    }

    @Override
    public void saveOrUpdate(HttpServletRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        String crud_action_value = request.getParameter("crud_action_value");
        HcsaServiceConfigDto hcsaServiceConfigDto = getDateOfHcsaService(request);
        doValidate(hcsaServiceConfigDto, errorMap);
        if (!errorMap.isEmpty()) {
            HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
            hcsaServiceConfigDto.getHcsaSvcSpecificStageWorkloadDtos();
            List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = hcsaServiceConfigDto.getHcsaSvcSpePremisesTypeDtos();
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaServiceConfigDto.getHcsaSvcPersonnelDtos();
            List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtos = hcsaServiceConfigDto.getHcsaSvcSpecificStageWorkloadDtos();
            for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : hcsaSvcPersonnelDtos) {
                String psnType = hcsaSvcPersonnelDto.getPsnType();
                request.setAttribute(psnType, hcsaSvcPersonnelDto);
            }
            Set<String> premisesSet = new HashSet<>();
            for (HcsaSvcSpePremisesTypeDto hcsaSvcSpePremisesTypeDto : hcsaSvcSpePremisesTypeDtos) {
                premisesSet.add(hcsaSvcSpePremisesTypeDto.getPremisesType());
            }
            List<HcsaConfigPageDto> hcsaConfigPageDtos = (List<HcsaConfigPageDto>) request.getAttribute("hcsaConfigPageDtos");
            request.setAttribute("PremisesType", premisesSet);
            request.setAttribute("hcsaServiceDto", hcsaServiceDto);
            request.setAttribute("crud_action_type", "dovalidate");
            Map<String, List<HcsaConfigPageDto>> map = new HashMap<>();
            map.put("APTY002",hcsaConfigPageDtos);
            request.setAttribute("routingStagess", map);
            request.setAttribute("errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            return;
        }

        if ("update".equals(crud_action_value)) {
            HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
            Integer i = Integer.valueOf(hcsaServiceDto.getVersion()) + 1;
            hcsaServiceDto.setVersion(i.toString());
            hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto);

        }
//        hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto);
        request.setAttribute("crud_action_type", "save");
    }


    @Override
    public void addNewService(HttpServletRequest request) {

        Map<String, List<HcsaConfigPageDto>> hcsaConfigPageDtos = getHcsaConfigPageDtos(request);
        Map<String, List<HcsaConfigPageDto>> map = new HashMap<>();
        List<HcsaConfigPageDto> hcsaConfigPageDtos1 = hcsaConfigPageDtos.get("APTY001");
        List<HcsaConfigPageDto> hcsaConfigPageDtos2 = hcsaConfigPageDtos.get("APTY002");
        List<HcsaConfigPageDto> workGrop = getWorkGrop("APTY001", "renew");
        List<HcsaConfigPageDto> workGrop1 = getWorkGrop("APTY002", "new Application");
        setValueOfhcsaConfigPageDtos(hcsaConfigPageDtos1,workGrop);
        setValueOfhcsaConfigPageDtos(hcsaConfigPageDtos2,workGrop1);
        map.put("APTY002",workGrop1);
        map.put("APTY001",workGrop);
        request.setAttribute("routingStagess", map);

    }

    @Override
    public void update(HttpServletRequest request) {
        Map<String, String> errorMap = new HashMap<>();
        String crud_action_value = request.getParameter("crud_action_value");
        HcsaServiceConfigDto hcsaServiceConfigDto = getDateOfHcsaService(request);
        doValidate(hcsaServiceConfigDto, errorMap);
        if (!errorMap.isEmpty()) {
            HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
            List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = hcsaServiceConfigDto.getHcsaSvcSpePremisesTypeDtos();
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaServiceConfigDto.getHcsaSvcPersonnelDtos();
            List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtos = hcsaServiceConfigDto.getHcsaSvcSpecificStageWorkloadDtos();
            for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : hcsaSvcPersonnelDtos) {
                String psnType = hcsaSvcPersonnelDto.getPsnType();
                request.setAttribute(psnType, hcsaSvcPersonnelDto);
            }
            Set<String> premisesSet = new HashSet<>();
            for (HcsaSvcSpePremisesTypeDto hcsaSvcSpePremisesTypeDto : hcsaSvcSpePremisesTypeDtos) {
                premisesSet.add(hcsaSvcSpePremisesTypeDto.getPremisesType());
            }
            List<HcsaConfigPageDto> hcsaConfigPageDtos1 = (List<HcsaConfigPageDto>) request.getAttribute("hcsaConfigPageDtos");
            Map<String, List<HcsaConfigPageDto>> hcsaConfigPageDtos2 = getHcsaConfigPageDtos(request);
            List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = (List<HcsaServiceStepSchemeDto>) request.getAttribute("hcsaServiceStepSchemeDtos");
            List<String> stringList = new ArrayList<>();
            for (HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto : hcsaServiceStepSchemeDtos) {
                String stepCode = hcsaServiceStepSchemeDto.getStepCode();
                stringList.add(stepCode);
            }
            request.setAttribute("hcsaServiceStepSchemeDto", stringList);
            request.setAttribute("PremisesType", premisesSet);
            request.setAttribute("hcsaServiceDto", hcsaServiceDto);
            request.setAttribute("crud_action_type", "validate");
            List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = getHcsaSvcRoutingStageDtos();
            List<String> type = getType();
            List<WorkingGroupDto> workingGroup = getWorkingGroup();
            List<HcsaConfigPageDto> hcsaConfigPageDtos = new ArrayList<>();
            for (HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtos) {
                HcsaConfigPageDto hcsaConfigPageDto = new HcsaConfigPageDto();
                hcsaConfigPageDto.setStageCode(hcsaSvcRoutingStageDto.getStageCode());
                hcsaConfigPageDto.setStageName(hcsaSvcRoutingStageDto.getStageName());
                hcsaConfigPageDto.setAppTypeName("new Application");
                hcsaConfigPageDto.setAppType("APTY002");
                getWorkingGroupDto(workingGroup,hcsaSvcRoutingStageDto,hcsaConfigPageDto);
                hcsaConfigPageDtos.add(hcsaConfigPageDto);
            }
            if (hcsaConfigPageDtos1 != null) {
                for (int i = 0; i < hcsaConfigPageDtos.size(); i++) {
                    Integer manhours = hcsaConfigPageDtos1.get(i).getManhours();
                    String workingGroupId = hcsaConfigPageDtos1.get(i).getWorkingGroupId();
                    String workloadId = hcsaConfigPageDtos1.get(i).getWorkloadId();
                    String routingSchemeId = hcsaConfigPageDtos1.get(i).getRoutingSchemeId();
                    String workStageId = hcsaConfigPageDtos1.get(i).getWorkStageId();
                    hcsaConfigPageDtos.get(i).setManhours(manhours);
                    hcsaConfigPageDtos.get(i).setWorkingGroupId(workingGroupId);
                    hcsaConfigPageDtos.get(i).setWorkloadId(workloadId);
                    hcsaConfigPageDtos.get(i).setRoutingSchemeId(routingSchemeId);
                    hcsaConfigPageDtos.get(i).setWorkStageId(workStageId);
                }
            }
            Map<String, List<HcsaConfigPageDto>> map = new HashMap<>();
            map.put("APTY002",hcsaConfigPageDtos);
            request.setAttribute("routingStagess", map);
            request.setAttribute("errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            return;
        }

        if ("save".equals(crud_action_value)) {
            HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
            Integer i = (int) Double.parseDouble(hcsaServiceDto.getVersion()) + 1;
            hcsaServiceDto.setVersion(i.toString());
            hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto);

        }
//        hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto);
        request.setAttribute("crud_action_type", "save");
    }


    @Override
    public void saData(HttpServletRequest request) {


    }

    @Override
    public void delete(HttpServletRequest request) {
        String crud_action_type = request.getParameter("crud_action_type");
        String crud_action_value = request.getParameter("crud_action_value");
        if ("delete".equals(crud_action_type)) {
            if (crud_action_value != null && !"".equals(crud_action_value)) {
                view(request, crud_action_value);
            }

        }

    }

    private HcsaServiceConfigDto getDateOfHcsaService(HttpServletRequest request) {
        HcsaServiceConfigDto hcsaServiceConfigDto = new HcsaServiceConfigDto();
        HcsaServiceDto hcsaServiceDto = new HcsaServiceDto();
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = getHcsaSvcRoutingStageDtos();
        String serviceId = request.getParameter("serviceId");
        String serviceName = request.getParameter("serviceName");
        String description = request.getParameter("description");
        String displayDescription = request.getParameter("displayDescription");
        String serviceCode = request.getParameter("serviceCode");
        String serviceType = request.getParameter("ServiceType");
        String[] premisesTypes = request.getParameterValues("PremisesType");
        String version = request.getParameter("version");
        List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = new ArrayList<>();
        if (premisesTypes != null) {
            for (String str : premisesTypes) {
                HcsaSvcSpePremisesTypeDto hcsaSvcSpePremisesTypeDto = new HcsaSvcSpePremisesTypeDto();
                hcsaSvcSpePremisesTypeDto.setPremisesType(str);
                hcsaSvcSpePremisesTypeDto.setStatus("CMSTAT001");
                hcsaSvcSpePremisesTypeDtos.add(hcsaSvcSpePremisesTypeDto);
            }
        }

        String manprincipalOfficer = request.getParameter("man-principalOfficer");
        String mixprincipalOfficer = request.getParameter("mix-principalOfficer");
        String poId = request.getParameter("poId");
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = new ArrayList<>();
        HcsaSvcPersonnelDto poDto = new HcsaSvcPersonnelDto();
        poDto.setPsnType("PO");
        if (!StringUtil.isEmpty(manprincipalOfficer)) {
            poDto.setMandatoryCount(Integer.parseInt(manprincipalOfficer));
        }
        if (!StringUtil.isEmpty(mixprincipalOfficer)) {
            poDto.setMaximumCount(Integer.parseInt(mixprincipalOfficer));
        }
        if (!StringUtil.isEmpty(poId)) {
            poDto.setServiceId(poId);
        }
        poDto.setStatus("CMSTAT001");
        hcsaSvcPersonnelDtos.add(poDto);
        String dpoId = request.getParameter("dpoId");
        String mandeputyPrincipalOfficer = request.getParameter("man-DeputyPrincipalOfficer");
        String mixdeputyPrincipalOfficer = request.getParameter("mix-DeputyPrincipalOfficer");
        HcsaSvcPersonnelDto dpoDto = new HcsaSvcPersonnelDto();
        if (!StringUtil.isEmpty(dpoId)) {
            dpoDto.setId(dpoId);
        }
        dpoDto.setPsnType("DPO");
        if (!StringUtil.isEmpty(mandeputyPrincipalOfficer)) {
            dpoDto.setMandatoryCount(Integer.parseInt(mandeputyPrincipalOfficer));
        }
        if (!StringUtil.isEmpty(mixdeputyPrincipalOfficer)) {
            dpoDto.setMaximumCount(Integer.parseInt(mixdeputyPrincipalOfficer));
        }
        dpoDto.setStatus("CMSTAT001");
        hcsaSvcPersonnelDtos.add(dpoDto);
        String cgoId = request.getParameter("cgoId");
        String manclinicalGovernanceOfficer = request.getParameter("man-ClinicalGovernanceOfficer");
        String mixclinicalGovernanceOfficer = request.getParameter("mix-ClinicalGovernanceOfficer");
        HcsaSvcPersonnelDto cgoDto = new HcsaSvcPersonnelDto();
        if (!StringUtil.isEmpty(cgoId)) {
            cgoDto.setId(cgoId);
        }
        cgoDto.setPsnType("CGO");
        if (!StringUtil.isEmpty(manclinicalGovernanceOfficer)) {
            cgoDto.setMandatoryCount(Integer.parseInt(manclinicalGovernanceOfficer));
        }
        if (!StringUtil.isEmpty(mixclinicalGovernanceOfficer)) {
            cgoDto.setMaximumCount(Integer.parseInt(mixclinicalGovernanceOfficer));
        }
        cgoDto.setStatus("CMSTAT001");
        hcsaSvcPersonnelDtos.add(cgoDto);
        String svcpsnId = request.getParameter("svcpsnId");
        String manservicePersonnel = request.getParameter("man-ServicePersonnel");
        String mixservicePersonnel = request.getParameter("mix-ServicePersonnel");
        HcsaSvcPersonnelDto svcPersonnelDto = new HcsaSvcPersonnelDto();
        if (!StringUtil.isEmpty(svcpsnId)) {
            svcPersonnelDto.setId(svcpsnId);
        }
        svcPersonnelDto.setPsnType("SVCPSN");
        try {
            if (!StringUtil.isEmpty(manservicePersonnel)) {
                svcPersonnelDto.setMandatoryCount(Integer.valueOf(manservicePersonnel));
            }
            if (!StringUtil.isEmpty(mixservicePersonnel)) {
                svcPersonnelDto.setMaximumCount(Integer.valueOf(mixservicePersonnel));
            }
        } catch (Exception e) {

            svcPersonnelDto.setMaximumCount(-1);
            svcPersonnelDto.setMandatoryCount(-1);
        }

        svcPersonnelDto.setStatus("CMSTAT001");
        hcsaSvcPersonnelDtos.add(svcPersonnelDto);
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = new ArrayList<>();
        String numberDocument = request.getParameter("NumberDocument");
        String descriptionDocument = request.getParameter("DescriptionDocument");
        String numberfields = request.getParameter("Numberfields");
        String descriptionGeneral = request.getParameter("DescriptionGeneral");

        List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtoList = new ArrayList<>();
        List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtoList = new ArrayList<>();
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = new ArrayList<>();
        List<HcsaConfigPageDto> hcsaConfigPageDtos = new ArrayList<>();
        List<String> type = getType();
        for(String every:type){
            for (HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtos) {
                HcsaConfigPageDto hcsaConfigPageDto = new HcsaConfigPageDto();
                HcsaSvcSpecificStageWorkloadDto hcsaSvcSpecificStageWorkloadDto = new HcsaSvcSpecificStageWorkloadDto();
                HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto = new HcsaSvcSpeRoutingSchemeDto();
                HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                String stageCode = hcsaSvcRoutingStageDto.getStageCode();
                String id = hcsaSvcRoutingStageDto.getId();
                String routingScheme = request.getParameter("RoutingScheme" + stageCode+every);
                String workloadManhours = request.getParameter("WorkloadManhours" + stageCode+every);
                String workloadId = request.getParameter("workloadId" + stageCode+every);
                String workingGroupId = request.getParameter("workingGroup" + stageCode+every);
                String stageId = request.getParameter("stageId" + stageCode+every);
                String workstageId = request.getParameter("workstageId" + stageCode+every);
                if (!StringUtil.isEmpty(workloadManhours)) {
                    hcsaSvcSpecificStageWorkloadDto.setManhourCount(Integer.parseInt(workloadManhours));
                    hcsaConfigPageDto.setManhours(Integer.parseInt(workloadManhours));
                }
                hcsaSvcSpecificStageWorkloadDto.setId(workloadId);
                hcsaConfigPageDto.setWorkloadId(workloadId);
                if (!StringUtil.isEmpty(stageId)) {
                    hcsaSvcSpeRoutingSchemeDto.setId(stageId);
                    hcsaSvcSpeRoutingSchemeDto.setStageId(id);
                    hcsaConfigPageDto.setRoutingSchemeId(stageId);
                }
                if (!StringUtil.isEmpty(workingGroupId)) {
                    hcsaSvcStageWorkingGroupDto.setStageWorkGroupId(workingGroupId);
                    hcsaSvcStageWorkingGroupDto.setStageId(id);
                    hcsaSvcStageWorkingGroupDto.setId(workstageId);
                    hcsaConfigPageDto.setWorkingGroupId(workingGroupId);

                }
                hcsaConfigPageDto.setWorkStageId(workstageId);
                hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
                hcsaSvcSpeRoutingSchemeDto.setSchemeType(routingScheme);
                hcsaSvcSpeRoutingSchemeDto.setAppType("APTY002");
                hcsaSvcSpeRoutingSchemeDto.setStatus("CMSTAT001");
                hcsaSvcSpecificStageWorkloadDto.setStageId(id);
                hcsaSvcSpecificStageWorkloadDto.setAppType("APTY002");
                hcsaSvcSpecificStageWorkloadDto.setStatus("CMSTAT001");
                hcsaSvcSpecificStageWorkloadDtoList.add(hcsaSvcSpecificStageWorkloadDto);
                hcsaSvcSpeRoutingSchemeDtoList.add(hcsaSvcSpeRoutingSchemeDto);
                hcsaConfigPageDto.setAppType(every);
                hcsaConfigPageDtos.add(hcsaConfigPageDto);
            }
        }


        String[] steps = request.getParameterValues("step");
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = new ArrayList<>();
        if (steps != null) {
            for (int i = 0; i < steps.length; i++) {
                HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto = new HcsaServiceStepSchemeDto();
                hcsaServiceStepSchemeDto.setStatus("CMSTAT001");
                hcsaServiceStepSchemeDto.setSeqNum(i);
                hcsaServiceStepSchemeDto.setStepCode(steps[i]);
                hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
            }
        }

        String parameter = request.getParameter("Sub-Types");
        String startDate = request.getParameter("StartDate");
        String endDate = request.getParameter("EndDate");

        if (!StringUtil.isEmpty(serviceId)) {
            hcsaServiceDto.setId(serviceId);
        }
        hcsaServiceDto.setSvcName(serviceName);
        hcsaServiceDto.setSvcCode(serviceCode);

        try {
            Date parse = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
            String format = new SimpleDateFormat("yyyy-MM-dd").format(parse);
            hcsaServiceDto.setEffectiveDate(format);
        } catch (Exception e) {
            hcsaServiceDto.setEffectiveDate(startDate);
        }


        if (!StringUtil.isEmpty(endDate)) {
            try {
                hcsaServiceDto.setEndDate(new SimpleDateFormat("dd/MM/yyyy").parse(endDate));
            } catch (ParseException e) {
                hcsaServiceDto.setEndDate(new Date("1976-01-01"));
            }
        }

        hcsaServiceDto.setSvcDisplayDesc(displayDescription);
        hcsaServiceDto.setSvcDesc(description);
        hcsaServiceDto.setSvcType(serviceType);
        hcsaServiceDto.setStatus("CMSTAT003");
        if (StringUtil.isEmpty(version)) {
            hcsaServiceDto.setVersion("1");
        } else {
            hcsaServiceDto.setVersion(version);
        }

        hcsaServiceConfigDto.setHcsaSvcSpePremisesTypeDtos(hcsaSvcSpePremisesTypeDtos);
        hcsaServiceConfigDto.setHcsaSvcDocConfigDtos(hcsaSvcDocConfigDtos);
        hcsaServiceConfigDto.setHcsaSvcPersonnelDtos(hcsaSvcPersonnelDtos);
        hcsaServiceConfigDto.setHcsaServiceDto(hcsaServiceDto);
        hcsaServiceConfigDto.setHcsaSvcSpeRoutingSchemeDtos(hcsaSvcSpeRoutingSchemeDtoList);
        hcsaServiceConfigDto.setHcsaSvcSpecificStageWorkloadDtos(hcsaSvcSpecificStageWorkloadDtoList);
        hcsaServiceConfigDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
        hcsaServiceConfigDto.setHcsaSvcStageWorkingGroupDtos(hcsaSvcStageWorkingGroupDtos);
        request.setAttribute("hcsaConfigPageDtos", hcsaConfigPageDtos);
        request.setAttribute("hcsaServiceStepSchemeDtos", hcsaServiceStepSchemeDtos);
        return hcsaServiceConfigDto;
    }


    private void doValidate(HcsaServiceConfigDto hcsaServiceConfigDto, Map<String, String> errorMap) {
        HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = hcsaServiceConfigDto.getHcsaSvcStageWorkingGroupDtos();
        for (int i = 0; i < hcsaSvcStageWorkingGroupDtos.size(); i++) {
            String stageWorkGroupId = hcsaSvcStageWorkingGroupDtos.get(i).getStageWorkGroupId();
            if (StringUtil.isEmpty(stageWorkGroupId)) {

                errorMap.put("stageWorkGroupId" + i, "UC_CHKLMD001_ERR001");
            }

        }
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = hcsaServiceConfigDto.getHcsaServiceStepSchemeDtos();
        if (hcsaServiceStepSchemeDtos == null || hcsaServiceStepSchemeDtos.isEmpty()) {
            errorMap.put("serviceStep", "UC_CHKLMD001_ERR001");
        }
        String svcCode = hcsaServiceDto.getSvcCode();
        String svcName = hcsaServiceDto.getSvcName();
        String svcDesc = hcsaServiceDto.getSvcDesc();
        String svcDisplayDesc = hcsaServiceDto.getSvcDisplayDesc();
        String svcType = hcsaServiceDto.getSvcType();
        String effectiveDate = hcsaServiceDto.getEffectiveDate();
        if (StringUtil.isEmpty(effectiveDate)) {
            errorMap.put("effectiveDate", "UC_CHKLMD001_ERR001");
        }
        if (StringUtil.isEmpty(svcCode)) {
            errorMap.put("svcCode", "UC_CHKLMD001_ERR001");
        }
        if (StringUtil.isEmpty(svcName)) {
            errorMap.put("svcName", "UC_CHKLMD001_ERR001");
        }
        if (StringUtil.isEmpty(svcDisplayDesc)) {
            errorMap.put("svcDisplayDesc", "UC_CHKLMD001_ERR001");
        }
        if (StringUtil.isEmpty(svcDesc)) {
            errorMap.put("svcDesc", "UC_CHKLMD001_ERR001");
        }
        if (StringUtil.isEmpty(svcType)) {
            errorMap.put("svcType", "UC_CHKLMD001_ERR001");
        }
        List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = hcsaServiceConfigDto.getHcsaSvcSpePremisesTypeDtos();
        if (hcsaSvcSpePremisesTypeDtos.isEmpty()) {
            errorMap.put("premieseType", "UC_CHKLMD001_ERR001");
        }
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaServiceConfigDto.getHcsaSvcPersonnelDtos();
        for (int i = 0; i < hcsaSvcPersonnelDtos.size(); i++) {
            String psnType = hcsaSvcPersonnelDtos.get(i).getPsnType();
            int mandatoryCount = hcsaSvcPersonnelDtos.get(i).getMandatoryCount();
            int maximumCount = hcsaSvcPersonnelDtos.get(i).getMaximumCount();
            if (StringUtil.isEmpty(psnType)) {
                errorMap.put("psnType" + i, "UC_CHKLMD001_ERR001");
            }
            if (StringUtil.isEmpty(mandatoryCount)) {
                errorMap.put("mandatoryCount" + i, "UC_CHKLMD001_ERR001");
            }
            if (StringUtil.isEmpty(maximumCount)) {
                errorMap.put("maximumCount" + i, "UC_CHKLMD001_ERR001");
            }
        }

        List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos = hcsaServiceConfigDto.getHcsaSvcSpeRoutingSchemeDtos();
        for (int i = 0; i < hcsaSvcSpeRoutingSchemeDtos.size(); i++) {

            String schemeType = hcsaSvcSpeRoutingSchemeDtos.get(i).getSchemeType();
            if (StringUtil.isEmpty(schemeType)) {
                errorMap.put("schemeType" + i, "UC_CHKLMD001_ERR001");
            }
        }
        List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtos = hcsaServiceConfigDto.getHcsaSvcSpecificStageWorkloadDtos();
        for (int i = 0; i < hcsaSvcSpecificStageWorkloadDtos.size(); i++) {
            Integer manhourCount = hcsaSvcSpecificStageWorkloadDtos.get(i).getManhourCount();
            if (StringUtil.isEmpty(manhourCount)) {
                errorMap.put("manhourCount" + i, "UC_CHKLMD001_ERR001");
            }
        }

        Boolean entity = hcsaConfigClient.isExistHcsaService(hcsaServiceDto).getEntity();
        if (!entity) {

        }
    }

    private List<HcsaConfigPageDto> getHcsaConfigPageDtos(HcsaServiceDto hcsaServiceDto) {

        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = getHcsaSvcRoutingStageDtos();

        List<HcsaSvcStageWorkloadDto> hcsaSvcStageWorkloadDtos =
                hcsaConfigClient.getHcsaSvcSpeRoutingSchemeByServiceId(hcsaServiceDto.getId()).getEntity();
        List<String> stageIds = new ArrayList<>();
        stageIds.add("12848A70-820B-EA11-BE7D-000C29F371DC");
        stageIds.add("13848A70-820B-EA11-BE7D-000C29F371DC");
        stageIds.add("14848A70-820B-EA11-BE7D-000C29F371DC");
        stageIds.add("15848A70-820B-EA11-BE7D-000C29F371DC");
        stageIds.add("16848A70-820B-EA11-BE7D-000C29F371DC");
        stageIds.add("17848A70-820B-EA11-BE7D-000C29F371DC");

        List<WorkingGroupDto> hcsa = organizationClient.getWorkingGroup("hcsa").getEntity();
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = hcsaConfigClient.getHcsaStageWorkingGroup(hcsaServiceDto.getId()).getEntity();

        List<HcsaConfigPageDto> hcsaConfigPageDtos = new ArrayList<>();
        for (HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtos) {
            HcsaConfigPageDto hcsaConfigPageDto = new HcsaConfigPageDto();
            hcsaConfigPageDto.setStageCode(hcsaSvcRoutingStageDto.getStageCode());
            hcsaConfigPageDto.setStage(hcsaSvcRoutingStageDto.getId());
            hcsaConfigPageDto.setStageName(hcsaSvcRoutingStageDto.getStageName());
            for (int i = 0; i < hcsaSvcStageWorkloadDtos.size(); i++) {
                String stageId = hcsaSvcStageWorkloadDtos.get(i).getStageId();
                Integer manhourCount = hcsaSvcStageWorkloadDtos.get(i).getManhourCount();
                String appType = hcsaSvcStageWorkloadDtos.get(i).getAppType();
                if ("APTY002".equals(appType)) {
                    String id1 = hcsaSvcRoutingStageDto.getId();
                    if (id1.equals(stageId)) {
                        hcsaConfigPageDto.setManhours(manhourCount);
                        hcsaConfigPageDto.setAppType("APTY002");
                        hcsaConfigPageDto.setAppTypeName("new application");
                        hcsaConfigPageDto.setWorkloadId(hcsaSvcStageWorkloadDtos.get(i).getId());
                        hcsaConfigPageDto.setRoutingSchemeId(hcsaSvcStageWorkloadDtos.get(i).getId());
                    }
                }
            }

            getWorkingGroupDto(hcsa,hcsaSvcRoutingStageDto,hcsaConfigPageDto);

            hcsaConfigPageDtos.add(hcsaConfigPageDto);
        }
        for (int i = 0; i < hcsaConfigPageDtos.size(); i++) {
            if (!hcsaSvcStageWorkingGroupDtos.isEmpty()) {
                String stageId = hcsaSvcStageWorkingGroupDtos.get(i).getStageId();
                String workingStageId = hcsaSvcStageWorkingGroupDtos.get(i).getId();
                String stage = hcsaConfigPageDtos.get(i).getStage();
                if (stageId.equals(stage)) {
                    String id = hcsaSvcStageWorkingGroupDtos.get(i).getStageWorkGroupId();
                    hcsaConfigPageDtos.get(i).setWorkingGroupId(id);
                    hcsaConfigPageDtos.get(i).setWorkStageId(workingStageId);
                }
            }

        }

        return hcsaConfigPageDtos;
    }

    private List<HcsaSvcRoutingStageDto> getHcsaSvcRoutingStageDtos() {
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = hcsaConfigClient.stagelist().getEntity();
        for (int i = 0; i < hcsaSvcRoutingStageDtos.size(); i++) {
            String stageOrder = hcsaSvcRoutingStageDtos.get(i).getStageOrder();
            try {
                if (Integer.valueOf(stageOrder) % 100 != 0) {
                    hcsaSvcRoutingStageDtos.remove(i);
                    i--;
                }
            } catch (Exception e) {

            }
        }
        return hcsaSvcRoutingStageDtos;
    }

    private List<WorkingGroupDto> getWorkingGroup() {
        List<WorkingGroupDto> workingGroup = organizationClient.getWorkingGroup("hcsa").getEntity();
        return workingGroup;
    }


    private List<HcsaSvcStageWorkloadDto> getHcsaSvcStageWorkloadDtos(HcsaServiceDto hcsaServiceDto) {
        List<HcsaSvcStageWorkloadDto> hcsaSvcStageWorkloadDtos =
                hcsaConfigClient.getHcsaSvcSpeRoutingSchemeByServiceId(hcsaServiceDto.getId()).getEntity();
        return hcsaSvcStageWorkloadDtos;
    }


    private    Map<String ,  List<HcsaConfigPageDto>> getHcsaConfigPageDtos(HttpServletRequest request) {
        List<HcsaConfigPageDto> hcsaConfigPageDtos1 = (List<HcsaConfigPageDto>) request.getAttribute("hcsaConfigPageDtos");
        List<String> types = getType();
        Map<String ,  List<HcsaConfigPageDto>> map=new HashMap<>();
        if(hcsaConfigPageDtos1!=null){
            for(String type:types){
                List<HcsaConfigPageDto> list=new ArrayList<>();
                for(HcsaConfigPageDto hcsaConfigPageDto:hcsaConfigPageDtos1){
                    String appType = hcsaConfigPageDto.getAppType();
                    if(type.equals(appType)){
                        list.add(hcsaConfigPageDto);
                    }
                }
                map.put(type,list);
            }
        }


        return map;
    }

    private List<HcsaServiceCategoryDto> getHcsaServiceCategoryDto() {

        List<HcsaServiceCategoryDto> hcsaServiceCategoryDtos = hcsaConfigClient.getHcsaServiceCategorys().getEntity();
        return hcsaServiceCategoryDtos;
    }

    private void view(HttpServletRequest request, String crud_action_value) {
        List<String> list = new ArrayList<>();
        list.add(crud_action_value);
        List<HcsaServiceDto> entity = hcsaConfigClient.getHcsaServiceDtoByCode(list).getEntity();
        HcsaServiceDto hcsaServiceDto = entity.get(0);
        request.setAttribute("hcsaServiceDto", hcsaServiceDto);
        String id = hcsaServiceDto.getId();
        List<String> ids = new ArrayList<>();
        ids.add(id);
        Set<String> set = hcsaConfigClient.getAppGrpPremisesTypeBySvcId(ids).getEntity();
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = hcsaConfigClient.getHcsaServiceStepSchemeDtoByServiceId(hcsaServiceDto.getId()).getEntity();
        List<String> stringList = new ArrayList<>();
        for (HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto : hcsaServiceStepSchemeDtos) {
            String stepCode = hcsaServiceStepSchemeDto.getStepCode();
            stringList.add(stepCode);
        }
        request.setAttribute("PremisesType", set);
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaConfigClient.getSvcPersonnelByServiceId(id).getEntity();
        for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : hcsaSvcPersonnelDtos) {
            String psnType = hcsaSvcPersonnelDto.getPsnType();
            request.setAttribute(psnType, hcsaSvcPersonnelDto);
        }

        request.setAttribute("hcsaServiceStepSchemeDto", stringList);
        List<HcsaConfigPageDto> hcsaConfigPageDtos = getHcsaConfigPageDtos(hcsaServiceDto);
        Map<String, List<HcsaConfigPageDto>> map = new HashMap<>();
        map.put("APTY002",hcsaConfigPageDtos);
        request.setAttribute("routingStagess", map);
    }

    private void getWorkingGroupDto(List<WorkingGroupDto> hcsa, HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto, HcsaConfigPageDto hcsaConfigPageDto) {
        List<WorkingGroupDto> workingGroupDtoList = new ArrayList<>();
        for (WorkingGroupDto workingGroupDto : hcsa) {
            String groupName = workingGroupDto.getGroupName();
            String stageName = hcsaSvcRoutingStageDto.getStageName();
            if (groupName.contains("Admin Screening") && stageName.contains("Admin Screening")) {
                workingGroupDtoList.add(workingGroupDto);
            }
            if (groupName.contains("Professional Screening") && stageName.contains("Professional Screening")) {
                workingGroupDtoList.add(workingGroupDto);
            }
            if (groupName.contains("Inspection Stage") && stageName.contains("Inspection Stage")) {
                workingGroupDtoList.add(workingGroupDto);
            }
            if (groupName.contains("Level 1 Approval") && stageName.contains("Level 1 Approval")) {
                workingGroupDtoList.add(workingGroupDto);
            }
            if (groupName.contains("Level 2 Approval") && stageName.contains("Level 2 Approval")) {
                workingGroupDtoList.add(workingGroupDto);
            }
            if (groupName.contains("Level 3 Approval") && stageName.contains("Level 3 Approval")) {
                workingGroupDtoList.add(workingGroupDto);
            }
            hcsaConfigPageDto.setWorkingGroup(workingGroupDtoList);
        }
    }

    private List<HcsaConfigPageDto> getWorkGrop(String type,String typeName){
        List<HcsaConfigPageDto> hcsaConfigPageDtos = new ArrayList<>();
        List<WorkingGroupDto> workingGroup = getWorkingGroup();
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = getHcsaSvcRoutingStageDtos();
        for (HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtos) {
            HcsaConfigPageDto hcsaConfigPageDto = new HcsaConfigPageDto();
            hcsaConfigPageDto.setStageCode(hcsaSvcRoutingStageDto.getStageCode());
            hcsaConfigPageDto.setStageName(hcsaSvcRoutingStageDto.getStageName());
            hcsaConfigPageDto.setAppTypeName(typeName);
            hcsaConfigPageDto.setAppType(type);
            List<WorkingGroupDto> workingGroupDtoList = new ArrayList<>();
            for (WorkingGroupDto workingGroupDto : workingGroup) {
                String groupName = workingGroupDto.getGroupName();
                String stageName = hcsaSvcRoutingStageDto.getStageName();
                if (groupName.contains("Admin Screening") && stageName.contains("Admin Screening")) {
                    workingGroupDtoList.add(workingGroupDto);
                }
                if (groupName.contains("Professional Screening") && stageName.contains("Professional Screening")) {
                    workingGroupDtoList.add(workingGroupDto);
                }
                if (groupName.contains("Inspection Stage") && stageName.contains("Inspection Stage")) {
                    workingGroupDtoList.add(workingGroupDto);
                }
                if (groupName.contains("Level 1 Approval") && stageName.contains("Level 1 Approval")) {
                    workingGroupDtoList.add(workingGroupDto);
                }
                if (groupName.contains("Level 2 Approval") && stageName.contains("Level 2 Approval")) {
                    workingGroupDtoList.add(workingGroupDto);
                }
                if (groupName.contains("Level 3 Approval") && stageName.contains("Level 3 Approval")) {
                    workingGroupDtoList.add(workingGroupDto);
                }
                hcsaConfigPageDto.setWorkingGroup(workingGroupDtoList);
            }

            hcsaConfigPageDtos.add(hcsaConfigPageDto);
        }
        return  hcsaConfigPageDtos;
    }

    private List<String> getType(){
        List<String> list=new ArrayList<>();
        list.add("APTY002");
       /* list.add("APTY003");
        list.add("APTY004");
        list.add("APTY005");*/
     /*   list.add("APTY001");*/
        return list;
    }

    private void setValueOfhcsaConfigPageDtos( List<HcsaConfigPageDto> hcsaConfigPageDtos1  ,List<HcsaConfigPageDto> hcsaConfigPageDtos){
        if (hcsaConfigPageDtos1 != null) {
            for (int i = 0; i < hcsaConfigPageDtos.size(); i++) {
                Integer manhours = hcsaConfigPageDtos1.get(i).getManhours();
                String stage = hcsaConfigPageDtos1.get(i).getStage();
                String workingGroupId = hcsaConfigPageDtos1.get(i).getWorkingGroupId();
                hcsaConfigPageDtos.get(i).setManhours(manhours);
                hcsaConfigPageDtos.get(i).setStage(stage);
                hcsaConfigPageDtos.get(i).setWorkingGroupId(workingGroupId);
            }
        }
    }

}
