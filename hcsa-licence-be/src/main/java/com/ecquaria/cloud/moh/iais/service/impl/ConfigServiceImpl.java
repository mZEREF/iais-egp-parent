package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCategoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpeRoutingSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.HcsaConfigPageDto;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ConfigService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.list.AbstractLinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Override
    public List<HcsaServiceDto> getAllHcsaServices(HttpServletRequest request) {
        List<HcsaServiceDto> entity = hcsaConfigClient.allHcsaService().getEntity();
        for(int i=0;i<entity.size();i++){
            if("CMSTAT005".equals(entity.get(i).getStatus())){
                entity.remove(entity.get(i));
                i--;
            }
        }

        request.setAttribute("hcsaServiceDtos", entity);
        return entity;
    }

    @Override
    public void viewPageInfo(HttpServletRequest request) {

        String crud_action_value = request.getParameter("crud_action_value");
        String crud_action_type = request.getParameter("crud_action_type");
        if (crud_action_value != null && !"".equals(crud_action_value) && "edit".equals(crud_action_type)) {
            view(request, crud_action_value);
        }else if("version".equals(crud_action_value)){
            String crud_action_additional = request.getParameter("crud_action_additional");
            log.info(crud_action_additional);
            HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(crud_action_additional).getEntity();
            List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getServiceVersions(hcsaServiceDto.getSvcCode()).getEntity();
            request.setAttribute("hcsaServiceDtosVersion",hcsaServiceDtos);
            setAttribute(request,hcsaServiceDto);
        }

    }

    @Override
    public void editPageInfo(HttpServletRequest request) {


    }

    @Override
    public void saveOrUpdate(HttpServletRequest request, HttpServletResponse response) {
        String crud_action_value = request.getParameter("crud_action_value");
        if("cancel".equals(crud_action_value)){
            sendURL(request,response);
            return;
        }
        Map<String, String> errorMap = new HashMap<>();
        HcsaServiceConfigDto hcsaServiceConfigDto = getDateOfHcsaService(request);
        doValidate(hcsaServiceConfigDto, errorMap,request);
        HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
        Map<String, Boolean> entity = hcsaConfigClient.isExistHcsaService(hcsaServiceDto).getEntity();
        Boolean svcCode = entity.get("svcCode");
        if(svcCode!=null&&svcCode){
            errorMap.put("code","This Service Code is already in use , please select another Service Code");
        }
        Boolean svcName = entity.get("svcName");
        if(svcName!=null&&svcName){
            errorMap.put("Name","This Service Name is already in use , Please select another Service Name");
        }

        if (!errorMap.isEmpty()) {
            hcsaServiceConfigDto.getHcsaSvcSpecificStageWorkloadDtos();
            List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = hcsaServiceConfigDto.getHcsaSvcSpePremisesTypeDtos();
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaServiceConfigDto.getHcsaSvcPersonnelDtos();
            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = hcsaServiceConfigDto.getHcsaSvcSubtypeOrSubsumedDtos();
            request.setAttribute("hcsaSvcSubtypeOrSubsumedDto",hcsaSvcSubtypeOrSubsumedDtos);
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

        hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto);
        // todo send email
      /*  request.setAttribute("option","added");
        request.setAttribute("serviceName",hcsaServiceDto.getSvcName());
        try {
            sendEmail(request);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        }*/
        request.setAttribute("crud_action_type", "save");
    }


    @Override
    public void addNewService(HttpServletRequest request) {

        Map<String, List<HcsaConfigPageDto>> tables = getTables(request);
        request.setAttribute("routingStagess", tables);
        List<HcsaServiceCategoryDto> hcsaServiceCategoryDto = getHcsaServiceCategoryDto();
        request.setAttribute("hcsaServiceCategoryDtos",hcsaServiceCategoryDto);

    }

    @Override
    public void update(HttpServletRequest request,HttpServletResponse response) {
        Map<String, String> errorMap = new HashMap<>();
        String crud_action_value = request.getParameter("crud_action_value");
        if("cancel".equals(crud_action_value)){
            sendURL(request,response);
            return;
        }
        HcsaServiceConfigDto hcsaServiceConfigDto = getDateOfHcsaService(request);
        doValidate(hcsaServiceConfigDto, errorMap,request);
        if (!errorMap.isEmpty()) {
            HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
            List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = hcsaServiceConfigDto.getHcsaSvcSpePremisesTypeDtos();
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaServiceConfigDto.getHcsaSvcPersonnelDtos();
            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = hcsaServiceConfigDto.getHcsaSvcSubtypeOrSubsumedDtos();
            request.setAttribute("hcsaSvcSubtypeOrSubsumedDto",hcsaSvcSubtypeOrSubsumedDtos);
            for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : hcsaSvcPersonnelDtos) {
                String psnType = hcsaSvcPersonnelDto.getPsnType();
                request.setAttribute(psnType, hcsaSvcPersonnelDto);
            }
            Set<String> premisesSet = new HashSet<>();
            for (HcsaSvcSpePremisesTypeDto hcsaSvcSpePremisesTypeDto : hcsaSvcSpePremisesTypeDtos) {
                premisesSet.add(hcsaSvcSpePremisesTypeDto.getPremisesType());
            }

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

                List<HcsaConfigPageDto> hcsaConfigPageDtos3 = hcsaConfigPageDtos2.get("APTY001");
                List<HcsaConfigPageDto> hcsaConfigPageDtos4 = hcsaConfigPageDtos2.get("APTY002");
                List<HcsaConfigPageDto> workGrop = getWorkGrop("APTY001", "renew");
                List<HcsaConfigPageDto> workGrop1 = getWorkGrop("APTY002", "new Application");
                setValueOfhcsaConfigPageDtos(hcsaConfigPageDtos3,workGrop);
                setValueOfhcsaConfigPageDtos(hcsaConfigPageDtos4,workGrop1);

            Map<String, List<HcsaConfigPageDto>> map = new HashMap<>();
            map.put("APTY002",workGrop1);
            map.put("APTY001",workGrop);
            Map<String, List<HcsaConfigPageDto>> tables = getTables(request);
            request.setAttribute("routingStagess", tables);
            request.setAttribute("errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            return;
        }

        if ("save".equals(crud_action_value)) {
            HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
            List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getServiceVersions(hcsaServiceDto.getSvcCode()).getEntity();
            HcsaServiceDto hcsaServiceDto1 = hcsaServiceDtos.get(hcsaServiceDtos.size() - 1);

            Integer i = (int) Double.parseDouble(hcsaServiceDto1.getVersion()) + 1;
            hcsaServiceDto.setVersion(i.toString());
            hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto);
            request.setAttribute("crud_action_type", "save");
            //todo send email update (if start date or end date change need send  Effective Start/End )

         /*   request.setAttribute("option","updated");
            request.setAttribute("serviceName",hcsaServiceDto.getSvcName());
            try {
                sendEmail(request);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
*/
        }
        else if("version".equals(crud_action_value)){
            request.setAttribute("crud_action_type", "version");

        }
//        hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto);

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

    @Override
    public void deleteOrCancel(HttpServletRequest request) {
        String serviceId = request.getParameter("crud_action_value");
        if(!StringUtil.isEmpty(serviceId)){
            Boolean flag = hcsaConfigClient.serviceIdIsUsed(serviceId).getEntity();
            HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
            List<LicenceDto> entity = hcsaLicenceClient.getLicenceDtosBySvcName(hcsaServiceDto.getSvcName()).getEntity();
            if(!entity.isEmpty()){
                return;
            }
            if(flag){

                return;
            }
            //todo delete send email

         /*   request.setAttribute("option","deleted ");
            request.setAttribute("serviceName",hcsaServiceDto.getSvcName());
            try {
                sendEmail(request);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
            hcsaConfigClient.updateService(serviceId);*/
        }

    }

    private HcsaServiceConfigDto getDateOfHcsaService(HttpServletRequest request) {
        HcsaServiceConfigDto hcsaServiceConfigDto = new HcsaServiceConfigDto();
        HcsaServiceDto hcsaServiceDto = new HcsaServiceDto();
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = getHcsaSvcRoutingStageDtos();
        //if service type is sub must to chose
        String[] subsumption = request.getParameterValues("Subsumption");
        // if service type is pre must be Choice
        String[] preRequisite = request.getParameterValues("Pre-requisite");
        String serviceId = request.getParameter("serviceId");
        String serviceName = request.getParameter("serviceName");
        String description = request.getParameter("description");
        String displayDescription = request.getParameter("displayDescription");
        String serviceCode = request.getParameter("serviceCode");
        String serviceType = request.getParameter("ServiceType");
        String[] premisesTypes = request.getParameterValues("PremisesType");
        String version = request.getParameter("version");
        String[] subTypes = request.getParameterValues("subType");
        String[] levels = request.getParameterValues("level");

        List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos=new ArrayList<>();
        Map<Integer ,String> level1=new HashMap<>();
        if(levels!=null){
            for(int i=0;i<levels.length;i++ ){
                level1.put(i,levels[i]);
            }
            for(int i=0;i<levels.length;i++){
                String s = level1.get(i);
                if("0".equals(s)){
                    HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto1=new HcsaSvcSubtypeOrSubsumedDto();
                    hcsaSvcSubtypeOrSubsumedDto1.setName(subTypes[i]);
                    List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos2=new ArrayList<>();
                    hcsaSvcSubtypeOrSubsumedDtos.add(hcsaSvcSubtypeOrSubsumedDto1);
                    for(int j=i+1;j<levels.length;j++){
                        String s1 = level1.get(j);
                        if("1".equals(s1)){
                            HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto2=new HcsaSvcSubtypeOrSubsumedDto();
                            hcsaSvcSubtypeOrSubsumedDto2.setName(subTypes[j]);
                            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos3=new ArrayList<>();
                            hcsaSvcSubtypeOrSubsumedDtos2.add(hcsaSvcSubtypeOrSubsumedDto2);
                            hcsaSvcSubtypeOrSubsumedDto1.setList(hcsaSvcSubtypeOrSubsumedDtos2);
                            for(int k=j+1;k<levels.length;k++){
                                String s2 = level1.get(k);
                                if("2".equals(s2)){
                                    HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto3=new HcsaSvcSubtypeOrSubsumedDto();
                                    hcsaSvcSubtypeOrSubsumedDto3.setName(subTypes[k]);
                                    hcsaSvcSubtypeOrSubsumedDtos3.add(hcsaSvcSubtypeOrSubsumedDto3);
                                    hcsaSvcSubtypeOrSubsumedDto2.setList(hcsaSvcSubtypeOrSubsumedDtos3);
                                }else if(!"2".equals(s2)){

                                    break;
                                }
                            }
                        }else if(!"1".equals(s1)){
                            break;
                        }
                    }
                }
            }
        }




        List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = new ArrayList<>();
        List<HcsaServiceSubTypeDto> list=new ArrayList<>();
        if("SVTP002".equals(serviceType)){
            if(subsumption!=null){
                for(String str:subsumption){
                    HcsaServiceSubTypeDto hcsaServiceSubTypeDto=new HcsaServiceSubTypeDto();
                    if(!"".equals(str)){
                        hcsaServiceSubTypeDto.setServiceId(str);
                    }
                    list.add(hcsaServiceSubTypeDto);
                }
            }


        } else if ("SVTP003".equals(serviceType)) {

            if (preRequisite != null){
                for(String str : preRequisite){
                    HcsaServiceSubTypeDto hcsaServiceSubTypeDto=new HcsaServiceSubTypeDto();
                    if(!"".equals(str)){
                        hcsaServiceSubTypeDto.setServiceId(str);
                    }
                    list.add(hcsaServiceSubTypeDto);
                }
            }

        }
        hcsaServiceDto.setServiceSubTypeDtos(list);
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
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = new ArrayList<>();
        HcsaSvcPersonnelDto poDto = new HcsaSvcPersonnelDto();
        poDto.setPsnType("PO");
        try {
            if (!StringUtil.isEmpty(manprincipalOfficer)) {
                poDto.setMandatoryCount(Integer.parseInt(manprincipalOfficer));
            }
        }catch (NumberFormatException e){
            poDto.setMandatoryCount(-1);
        }
        try {
            if (!StringUtil.isEmpty(mixprincipalOfficer)) {
                poDto.setMaximumCount(Integer.parseInt(mixprincipalOfficer));
            }
        }catch (NumberFormatException e){
            poDto.setMaximumCount(-1);
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
        try {
            if (!StringUtil.isEmpty(mandeputyPrincipalOfficer)) {
                dpoDto.setMandatoryCount(Integer.parseInt(mandeputyPrincipalOfficer));
            }
        }catch (NumberFormatException e){
            dpoDto.setMandatoryCount(-1);
        }
        try {
            if (!StringUtil.isEmpty(mixdeputyPrincipalOfficer)) {
                dpoDto.setMaximumCount(Integer.parseInt(mixdeputyPrincipalOfficer));
            }
        }catch (NumberFormatException e){
            dpoDto.setMaximumCount(-1);
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
        try {
            if (!StringUtil.isEmpty(manclinicalGovernanceOfficer)) {
                cgoDto.setMandatoryCount(Integer.parseInt(manclinicalGovernanceOfficer));
            }
        }catch (NumberFormatException e){
            cgoDto.setMandatoryCount(-1);
        }
        try {
            if (!StringUtil.isEmpty(mixclinicalGovernanceOfficer)) {
                cgoDto.setMaximumCount(Integer.parseInt(mixclinicalGovernanceOfficer));
            }
        }catch (NumberFormatException e){
            cgoDto.setMaximumCount(-1);
        }

        //todo is mandatory ,cannot
        String poMandatory = request.getParameter("POMandatory");
        String dpoMandatory = request.getParameter("DPOMandatory");
        String cgoMandatory = request.getParameter("CGOMandatory");

        int count=1;

        if(!hcsaSvcSubtypeOrSubsumedDtos.isEmpty()){
            HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto=new HcsaServiceStepSchemeDto();
            hcsaServiceStepSchemeDto.setStatus("CMSTAT001");
            hcsaServiceStepSchemeDto.setStepCode("SVST001");
            hcsaServiceStepSchemeDto.setSeqNum(count);
            hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
            count++;
        }
        if(cgoMandatory!=null){

        }
        if(cgoDto.getMandatoryCount()>0&&cgoDto.getMaximumCount()>0){
            HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto=new HcsaServiceStepSchemeDto();
            hcsaServiceStepSchemeDto.setStatus("CMSTAT001");
            hcsaServiceStepSchemeDto.setStepCode("SVST002");
            hcsaServiceStepSchemeDto.setSeqNum(count);
            hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
            count++;
        }
        if(dpoMandatory!=null){

        }
        if(dpoDto.getMandatoryCount()>0&&dpoDto.getMaximumCount()>0){
            HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto=new HcsaServiceStepSchemeDto();
            hcsaServiceStepSchemeDto.setStatus("CMSTAT001");
            hcsaServiceStepSchemeDto.setStepCode("SVST003");
            hcsaServiceStepSchemeDto.setSeqNum(count);
            hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
            count++;
        }
        if(poMandatory!=null){

        }
        if(poDto.getMandatoryCount()>0&&poDto.getMaximumCount()>0){
            HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto=new HcsaServiceStepSchemeDto();
            hcsaServiceStepSchemeDto.setStatus("CMSTAT001");
            hcsaServiceStepSchemeDto.setStepCode("SVST004");
            hcsaServiceStepSchemeDto.setSeqNum(count);
            hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
            count++;
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
        } catch (Exception e) {

            svcPersonnelDto.setMaximumCount(-1);
        }
        try {
            if (!StringUtil.isEmpty(mixservicePersonnel)) {
                svcPersonnelDto.setMaximumCount(Integer.valueOf(mixservicePersonnel));
            }
        }catch (NumberFormatException e){
            svcPersonnelDto.setMandatoryCount(-1);
        }


        svcPersonnelDto.setStatus("CMSTAT001");
        hcsaSvcPersonnelDtos.add(svcPersonnelDto);
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = new ArrayList<>();
        String numberDocument = request.getParameter("NumberDocument");
        String descriptionDocument = request.getParameter("DescriptionDocument");
        String numberfields = request.getParameter("Numberfields");
        String descriptionGeneral = request.getParameter("DescriptionGeneral");
        String numberfieldsMandatory = request.getParameter("NumberfieldsMandatory");
        String numberDocumentMandatory = request.getParameter("NumberDocumentMandatory");
        String descriptionDocumentMandatory = request.getParameter("DescriptionDocumentMandatory");
        try {
            request.setAttribute("numberDocument",numberDocument);
            request.setAttribute("descriptionDocument",descriptionDocument);
            Integer integer = Integer.valueOf(numberDocument);
            List<String> split = split(descriptionDocument);

            if(integer!=split.size()){



            }else {
                for(int i=0;i<integer;i++){

                    HcsaSvcDocConfigDto hcsaSvcDocConfigDto=new HcsaSvcDocConfigDto();

                    hcsaSvcDocConfigDto.setDocDesc(split.get(i));
                    hcsaSvcDocConfigDto.setDocTitle(split.get(i));
                    hcsaSvcDocConfigDto.setStatus("CMSTAT001");
                    hcsaSvcDocConfigDto.setDispOrder(0);
                    hcsaSvcDocConfigDto.setDupForPrem("0");
                    hcsaSvcDocConfigDto.setIsMandatory(false);
                    if(numberDocumentMandatory!=null&&descriptionDocumentMandatory!=null){
                        hcsaSvcDocConfigDto.setIsMandatory(true);
                    }
                    hcsaSvcDocConfigDtos.add(hcsaSvcDocConfigDto);
                }
                HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto=new HcsaServiceStepSchemeDto();
                hcsaServiceStepSchemeDto.setStatus("CMSTAT001");
                hcsaServiceStepSchemeDto.setStepCode("SVST005");
                hcsaServiceStepSchemeDto.setSeqNum(count);
                hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
                count++;
            }
        }catch (NumberFormatException e){

        }
        try {
            request.setAttribute("numberfields",numberfields);
            request.setAttribute("descriptionGeneral",descriptionGeneral);
            Integer integer = Integer.valueOf(numberfields);
            List<String> split = split(descriptionGeneral);
            if(integer!=split.size()){

            }else {
                for(int i=0;i<integer;i++){
                    HcsaSvcDocConfigDto hcsaSvcDocConfigDto=new HcsaSvcDocConfigDto();
                    hcsaSvcDocConfigDto.setDocDesc(split.get(i));
                    hcsaSvcDocConfigDto.setDocTitle(split.get(i));
                    hcsaSvcDocConfigDto.setStatus("CMSTAT001");
                    hcsaSvcDocConfigDto.setDispOrder(0);
                    hcsaSvcDocConfigDto.setDupForPrem("0");
                    hcsaSvcDocConfigDto.setIsMandatory(false);
                    if(numberfieldsMandatory!=null){
                        hcsaSvcDocConfigDto.setIsMandatory(true);
                    }
                    hcsaSvcDocConfigDtos.add(hcsaSvcDocConfigDto);
                }

            }
        }catch (NumberFormatException e){


        }

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
                String isMandatory=  request.getParameter("isMandatory"+ stageCode+every);


                if (!StringUtil.isEmpty(workloadManhours)) {
                    try {
                        hcsaSvcSpecificStageWorkloadDto.setManhourCount(Integer.parseInt(workloadManhours));
                        hcsaConfigPageDto.setManhours(Integer.parseInt(workloadManhours));
                    }catch (NumberFormatException e){
                        hcsaSvcSpecificStageWorkloadDto.setManhourCount(-1);
                        hcsaConfigPageDto.setManhours(-1);
                    }

                }

                hcsaConfigPageDto.setWorkloadId(workloadId);
                if (!StringUtil.isEmpty(stageId)) {
                    //todo delete
                 /*   hcsaSvcSpeRoutingSchemeDto.setId(stageId);*/
                    hcsaConfigPageDto.setRoutingSchemeId(stageId);
                }
                hcsaSvcSpeRoutingSchemeDto.setStageId(id);
                if (!StringUtil.isEmpty(workingGroupId)) {
                    hcsaSvcStageWorkingGroupDto.setStageWorkGroupId(workingGroupId);
                    hcsaSvcStageWorkingGroupDto.setStageId(id);
                    hcsaConfigPageDto.setWorkingGroupId(workingGroupId);
                }
                if(!StringUtil.isEmpty(workstageId)){
                    hcsaSvcStageWorkingGroupDto.setId(workstageId);
                    //todo delete
                  /*  hcsaSvcSpecificStageWorkloadDto.setId(workloadId);*/
                }
                if ("optional".equals(isMandatory)) {
                    hcsaConfigPageDto.setIsMandatory(isMandatory);

                }else if("mandatory".equals(isMandatory)){
                    hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
                    hcsaSvcSpeRoutingSchemeDto.setSchemeType(routingScheme);
                    hcsaSvcSpeRoutingSchemeDto.setAppType(every);
                    hcsaSvcSpeRoutingSchemeDto.setStatus("CMSTAT001");
                    hcsaSvcSpeRoutingSchemeDtoList.add(hcsaSvcSpeRoutingSchemeDto);
                }
                hcsaConfigPageDto.setWorkStageId(workstageId);
                hcsaConfigPageDto.setRoutingSchemeName(routingScheme);
                hcsaConfigPageDto.setAppType(every);
                hcsaConfigPageDtos.add(hcsaConfigPageDto);
            }
        }

        String startDate = request.getParameter("StartDate");
        String endDate = request.getParameter("EndDate");

        //todo delete
        if (!StringUtil.isEmpty(serviceId)) {
       /*     hcsaServiceDto.setId(serviceId);*/
        }
        hcsaServiceDto.setSvcName(serviceName);
        hcsaServiceDto.setSvcCode(serviceCode);

        try {
            Date parse = new SimpleDateFormat("dd/MM/yyyy").parse(startDate);
            String format = new SimpleDateFormat("yyyy-MM-dd").format(parse);
            hcsaServiceDto.setEffectiveDate(format);
            if(parse.after(new Date())){
                hcsaServiceDto.setStatus("CMSTAT003");
            }else {
                hcsaServiceDto.setStatus("CMSTAT001");
            }
        } catch (Exception e) {
            hcsaServiceDto.setEffectiveDate(startDate);
        }


        if (!StringUtil.isEmpty(endDate)) {
            try {
                hcsaServiceDto.setEndDate(new SimpleDateFormat("dd/MM/yyyy").parse(endDate));
            } catch (ParseException e) {
                hcsaServiceDto.setEndDate(new Date(99,1,1));
            }
        }

        hcsaServiceDto.setSvcDisplayDesc(displayDescription);
        hcsaServiceDto.setSvcDesc(description);
        hcsaServiceDto.setSvcType(serviceType);
        if (StringUtil.isEmpty(version)) {
            hcsaServiceDto.setVersion("1");
        } else {
            hcsaServiceDto.setVersion(version);
        }
        hcsaServiceConfigDto.setHcsaSvcSubtypeOrSubsumedDtos(hcsaSvcSubtypeOrSubsumedDtos);
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


    private void doValidate(HcsaServiceConfigDto hcsaServiceConfigDto, Map<String, String> errorMap,HttpServletRequest request) {
        HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = hcsaServiceConfigDto.getHcsaSvcStageWorkingGroupDtos();
        for (int i = 0; i < hcsaSvcStageWorkingGroupDtos.size(); i++) {
            String stageWorkGroupId = hcsaSvcStageWorkingGroupDtos.get(i).getStageWorkGroupId();
            if (StringUtil.isEmpty(stageWorkGroupId)) {
                errorMap.put("stageWorkGroupId" + i, "UC_CHKLMD001_ERR001");
            }
        }


        List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = hcsaServiceConfigDto.getHcsaSvcSubtypeOrSubsumedDtos();
        List<String> subtypeName=new ArrayList<>();
        if(hcsaSvcSubtypeOrSubsumedDtos!=null&&!hcsaSvcSubtypeOrSubsumedDtos.isEmpty()){
            for(HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto :hcsaSvcSubtypeOrSubsumedDtos){
                List<HcsaSvcSubtypeOrSubsumedDto> list = hcsaSvcSubtypeOrSubsumedDto.getList();
                String name = hcsaSvcSubtypeOrSubsumedDto.getName();
                if(!subtypeName.contains(name)){
                    subtypeName.add(name);
                }else {
                    errorMap.put("hcsaSvcSubtypeOrSubsumed","All name cannot repeat");
                }
                if(list!=null){
                    for(HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto1:list){
                        String name1 = hcsaSvcSubtypeOrSubsumedDto1.getName();
                        List<HcsaSvcSubtypeOrSubsumedDto> list1 = hcsaSvcSubtypeOrSubsumedDto1.getList();
                        if(!subtypeName.contains(name1)){
                            subtypeName.add(name1);
                        }else {
                            errorMap.put("hcsaSvcSubtypeOrSubsumed","All name cannot repeat");
                        }
                        if(list1!=null){
                            for(HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto2:list1){
                                String name2 = hcsaSvcSubtypeOrSubsumedDto2.getName();
                                if(!subtypeName.contains(name2)){
                                    subtypeName.add(name2);
                                }else {

                                    errorMap.put("hcsaSvcSubtypeOrSubsumed","All name cannot repeat");
                                }

                            }
                        }
                    }

                }
            }

        }


        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = hcsaServiceConfigDto.getHcsaSvcDocConfigDtos();
        if(hcsaSvcDocConfigDtos.isEmpty()){

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
        }else if("SVTP002".equals(svcType)){
            List<HcsaServiceSubTypeDto> serviceSubTypeDtos = hcsaServiceDto.getServiceSubTypeDtos();
            if(serviceSubTypeDtos.isEmpty()){
                errorMap.put("Subsumption","UC_CHKLMD001_ERR001");
            }
        }else if("SVTP003".equals(svcType)){
            List<HcsaServiceSubTypeDto> serviceSubTypeDtos = hcsaServiceDto.getServiceSubTypeDtos();
            if(serviceSubTypeDtos.isEmpty()){
                errorMap.put("Prerequisite","UC_CHKLMD001_ERR001");
            }
        }
        List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = hcsaServiceConfigDto.getHcsaSvcSpePremisesTypeDtos();
        if (hcsaSvcSpePremisesTypeDtos.isEmpty()) {
            errorMap.put("premieseType", "UC_CHKLMD001_ERR001");
        }
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaServiceConfigDto.getHcsaSvcPersonnelDtos();
        String poMandatory = request.getParameter("POMandatory");
        String manprincipalOfficer = request.getParameter("man-principalOfficer");
        String manDeputyPrincipalOfficer=request.getParameter("man-DeputyPrincipalOfficer");
        String manClinicalGovernanceOfficer=request.getParameter("man-ClinicalGovernanceOfficer");
        String dpoMandatory = request.getParameter("DPOMandatory");
        String cgoMandatory = request.getParameter("CGOMandatory");
        if(poMandatory!=null){
            if(StringUtil.isEmpty(manprincipalOfficer)){
                errorMap.put("mandatoryCount0", "UC_CHKLMD001_ERR001");
            }else {
                int i = Integer.parseInt(manprincipalOfficer);
                if(i<1){
                    errorMap.put("mandatoryCount0", "UC_CHKLMD001_ERR001");
                }
            }

        }
        if(dpoMandatory!=null){
            if(StringUtil.isEmpty(manDeputyPrincipalOfficer)){
                errorMap.put("mandatoryCount1", "UC_CHKLMD001_ERR001");
            }else {
                int i = Integer.parseInt(manDeputyPrincipalOfficer);
                if(i<1){
                    errorMap.put("mandatoryCount1", "UC_CHKLMD001_ERR001");
                }
            }

        }
        if(cgoMandatory!=null){
            if(StringUtil.isEmpty(manClinicalGovernanceOfficer)){
                errorMap.put("mandatoryCount2", "UC_CHKLMD001_ERR001");
            }else {
                int i = Integer.parseInt(manClinicalGovernanceOfficer);
                if(i<1){
                    errorMap.put("mandatoryCount2", "UC_CHKLMD001_ERR001");
                }
            }
        }

        for (int i = 0; i < hcsaSvcPersonnelDtos.size(); i++) {
            String psnType = hcsaSvcPersonnelDtos.get(i).getPsnType();
            int mandatoryCount = hcsaSvcPersonnelDtos.get(i).getMandatoryCount();
            int maximumCount = hcsaSvcPersonnelDtos.get(i).getMaximumCount();
            if (StringUtil.isEmpty(psnType)) {
                errorMap.put("psnType" + i, "UC_CHKLMD001_ERR001");
            }
            if (StringUtil.isEmpty(mandatoryCount)) {
                errorMap.put("mandatoryCount" + i, "UC_CHKLMD001_ERR001");
            }/*else  if(mandatoryCount<0){
                errorMap.put("mandatoryCount"+i,"Please enter a valid number (greater than or equal to 0)");
            }*/
            if (StringUtil.isEmpty(maximumCount)) {
                errorMap.put("maximumCount" + i, "UC_CHKLMD001_ERR001");
            }/*else if(maximumCount<0){
                errorMap.put("maximumCount"+i,"Please enter a valid number (greater than or equal to 0)");
            }*/
            if(!StringUtil.isEmpty(mandatoryCount)&&!StringUtil.isEmpty(maximumCount)){
                if(mandatoryCount>maximumCount){
                    errorMap.put("maximumCount"+i,"Incorrect format");
                }
            }
        }

        List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos = hcsaServiceConfigDto.getHcsaSvcSpeRoutingSchemeDtos();
        for (int i = 0; i < hcsaSvcSpeRoutingSchemeDtos.size(); i++) {

            String schemeType = hcsaSvcSpeRoutingSchemeDtos.get(i).getSchemeType();
            if (StringUtil.isEmpty(schemeType)) {
                errorMap.put("schemeType" + i, "UC_CHKLMD001_ERR001");
            }
        }

    }

    private   Map<String, List<HcsaConfigPageDto>> getHcsaConfigPageDtos(HcsaServiceDto hcsaServiceDto) {

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

        List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos = hcsaConfigClient.getHcsaSvcSpeRoutingSchemeDtoByServiceId(hcsaServiceDto.getId()).getEntity();


        List<String> types = getType();
        Map<String,List<HcsaSvcStageWorkloadDto>> map=new HashMap<>();
        Map<String, List<HcsaSvcSpeRoutingSchemeDto>> hcsaSvcSpeRoutingSchemeDtoMap=new HashMap<>();
        for(String type:types){
            List<HcsaSvcStageWorkloadDto> hcsaSvcStageWorkloadDtos1=new ArrayList<>();
            List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos1=new ArrayList<>();
            for(HcsaSvcStageWorkloadDto hcsaSvcStageWorkloadDto:hcsaSvcStageWorkloadDtos){
                String appType = hcsaSvcStageWorkloadDto.getAppType();
                if(type.equals(appType)){
                    hcsaSvcStageWorkloadDtos1.add(hcsaSvcStageWorkloadDto);
                }
            }
            for(HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto:hcsaSvcSpeRoutingSchemeDtos){
                String appType = hcsaSvcSpeRoutingSchemeDto.getAppType();
                if(type.equals(appType)){
                    hcsaSvcSpeRoutingSchemeDtos1.add(hcsaSvcSpeRoutingSchemeDto);
                }
            }
            map.put(type,hcsaSvcStageWorkloadDtos1);
            hcsaSvcSpeRoutingSchemeDtoMap.put(type,hcsaSvcSpeRoutingSchemeDtos1);
        }
        Map<String,  List<HcsaSvcStageWorkingGroupDto>> hcsaSvcStageWorkingGroupDtoMap=new HashMap<>();
        hcsaSvcSpeRoutingSchemeDtoMap.forEach((k,v)->{
            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos1=new ArrayList<>();
            for(HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto:v){
                String stageWrkGrpID = hcsaSvcSpeRoutingSchemeDto.getStageWrkGrpID();
                for(HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto:hcsaSvcStageWorkingGroupDtos){
                    if(stageWrkGrpID.equals(hcsaSvcStageWorkingGroupDto.getId())){
                        hcsaSvcStageWorkingGroupDtos1.add(hcsaSvcStageWorkingGroupDto);
                    }

                }

            }
            hcsaSvcStageWorkingGroupDtoMap.put(k,hcsaSvcStageWorkingGroupDtos1);
        });
        Map<String, List<HcsaConfigPageDto>> hcsaConfigPageDtoMap=new HashMap<>();
        for (String type:types){
            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos1 = hcsaSvcStageWorkingGroupDtoMap.get(type);
            List<HcsaSvcStageWorkloadDto> hcsaSvcStageWorkloadDtos1 = map.get(type);
            List<HcsaConfigPageDto> hcsaConfigPageDtos =
                    ProcessingData(type,hcsaSvcRoutingStageDtos, hcsaSvcStageWorkloadDtos1, hcsaSvcStageWorkingGroupDtos1, hcsa);
            List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos1 = hcsaSvcSpeRoutingSchemeDtoMap.get(type);
            for(HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto:hcsaSvcSpeRoutingSchemeDtos1){
                String stageWrkGrpID = hcsaSvcSpeRoutingSchemeDto.getStageWrkGrpID();

                for(HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto:hcsaSvcStageWorkingGroupDtos1){

                    String schemeType = hcsaSvcSpeRoutingSchemeDto.getSchemeType();
                        for(HcsaConfigPageDto hcsaConfigPageDto:hcsaConfigPageDtos){
                            String workingGroupId = hcsaConfigPageDto.getWorkingGroupId();
                            if(workingGroupId.equals(hcsaSvcStageWorkingGroupDto.getStageWorkGroupId())&&stageWrkGrpID.equals(hcsaSvcStageWorkingGroupDto.getId())){
                                hcsaConfigPageDto.setRoutingSchemeName(schemeType);
                            }

                        }
                }


            }
            for (HcsaConfigPageDto hcsaConfigPageDto:hcsaConfigPageDtos){
                if("APTY001".equals(type)){
                    hcsaConfigPageDto.setAppTypeName("appeal");
                } else if("APTY002".equals(type)){
                    hcsaConfigPageDto.setAppTypeName("new application");
                }else if("APTY005".equals(type)){
                    hcsaConfigPageDto.setAppTypeName("request for change");
                }else if("APTY004".equals(type)){
                    hcsaConfigPageDto.setAppTypeName("renew");
                }else if("APTY008".equals(type)){
                    hcsaConfigPageDto.setAppTypeName("cessation");
                }else  if("APTY007".equals(type)){
                    hcsaConfigPageDto.setAppTypeName("suspension");
                }else if("APTY006".equals(type)){
                    hcsaConfigPageDto.setAppTypeName("withdrawal");
                }
            }

            hcsaConfigPageDtoMap.put(type,hcsaConfigPageDtos);
        }




        return hcsaConfigPageDtoMap;
    }

    private  List<HcsaConfigPageDto> ProcessingData(String type,List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos,List<HcsaSvcStageWorkloadDto> hcsaSvcStageWorkloadDtos,List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos,  List<WorkingGroupDto> hcsa  ){
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
                if (type.equals(appType)) {
                    String id1 = hcsaSvcRoutingStageDto.getId();
                    if (id1.equals(stageId)) {
                        hcsaConfigPageDto.setManhours(manhourCount);
                        hcsaConfigPageDto.setAppType(type);
                        if("APTY001".equals(type)){
                            hcsaConfigPageDto.setAppTypeName("appeal");
                        } else if("APTY002".equals(type)){
                            hcsaConfigPageDto.setAppTypeName("new application");
                        }else if("APTY005".equals(type)){
                            hcsaConfigPageDto.setAppTypeName("request for change");
                        }else if("APTY004".equals(type)){
                            hcsaConfigPageDto.setAppTypeName("renew");
                        }else if("APTY008".equals(type)){
                            hcsaConfigPageDto.setAppTypeName("cessation");
                        }else  if("APTY007".equals(type)){
                            hcsaConfigPageDto.setAppTypeName("suspension");
                        }else if("APTY006".equals(type)){
                            hcsaConfigPageDto.setAppTypeName("withdrawal");
                        }
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
                for(HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto:hcsaSvcStageWorkingGroupDtos){
                    String stageId = hcsaSvcStageWorkingGroupDto.getStageId();
                    String workingStageId = hcsaSvcStageWorkingGroupDto.getId();
                    String stage = hcsaConfigPageDtos.get(i).getStage();
                    if (stageId.equals(stage)) {
                        String id = hcsaSvcStageWorkingGroupDto.getStageWorkGroupId();
                        hcsaConfigPageDtos.get(i).setWorkingGroupId(id);
                        hcsaConfigPageDtos.get(i).setWorkStageId(workingStageId);
                    }
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
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = getHcsaSvcRoutingStageDtos();
        List<WorkingGroupDto> workingGroup = getWorkingGroup();
        List<HcsaConfigPageDto> hcsaConfigPageDtos = new ArrayList<>();
        for(String type:types){
            for (HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtos) {
                HcsaConfigPageDto hcsaConfigPageDto = new HcsaConfigPageDto();
                hcsaConfigPageDto.setStageCode(hcsaSvcRoutingStageDto.getStageCode());
                hcsaConfigPageDto.setStageName(hcsaSvcRoutingStageDto.getStageName());

                hcsaConfigPageDto.setAppType(type);
                if("APTY002".equals(type)){
                    hcsaConfigPageDto.setAppTypeName("new Application");
                }else if("APTY001".equals(type)){
                    hcsaConfigPageDto.setAppTypeName("appeal");
                } else if ("APTY005".equals(type)) {
                    hcsaConfigPageDto.setAppTypeName("request for change");
                }else if("APTY004".equals(type)){
                    hcsaConfigPageDto.setAppTypeName("renew");
                }else if("APTY008".equals(type)){
                    hcsaConfigPageDto.setAppTypeName("cessation");
                }else  if("APTY007".equals(type)){
                    hcsaConfigPageDto.setAppTypeName("suspension");
                }else if("APTY006".equals(type)){
                    hcsaConfigPageDto.setAppTypeName("withdrawal");
                }
                getWorkingGroupDto(workingGroup,hcsaSvcRoutingStageDto,hcsaConfigPageDto);
                hcsaConfigPageDtos.add(hcsaConfigPageDto);
            }

        }

        if (hcsaConfigPageDtos1 != null) {
            for (int i = 0; i < hcsaConfigPageDtos.size(); i++) {
                String isMandatory = hcsaConfigPageDtos1.get(i).getIsMandatory();
                if("optional".equals(isMandatory)){
                    continue;
                }
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

        return map;
    }

    private List<HcsaServiceCategoryDto> getHcsaServiceCategoryDto() {

        List<HcsaServiceCategoryDto> hcsaServiceCategoryDtos = hcsaConfigClient.getHcsaServiceCategorys().getEntity();
        return hcsaServiceCategoryDtos;
    }

    private void view(HttpServletRequest request, String crud_action_value) {

        HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(crud_action_value).getEntity();

        setAttribute(request,hcsaServiceDto);
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
        list.add("APTY006") ;
        list.add("APTY008") ;
        list.add("APTY007") ;*/
        list.add("APTY005");
        list.add("APTY001");
        return list;
    }

    private void setValueOfhcsaConfigPageDtos( List<HcsaConfigPageDto> hcsaConfigPageDtos1  ,List<HcsaConfigPageDto> hcsaConfigPageDtos){
        if (hcsaConfigPageDtos1 != null) {
            for (int i = 0; i < hcsaConfigPageDtos.size(); i++) {
                Integer manhours = hcsaConfigPageDtos1.get(i).getManhours();
                String stage = hcsaConfigPageDtos1.get(i).getStage();
                String workingGroupId = hcsaConfigPageDtos1.get(i).getWorkingGroupId();
                String routingSchemeName = hcsaConfigPageDtos1.get(i).getRoutingSchemeName();
                hcsaConfigPageDtos.get(i).setManhours(manhours);
                hcsaConfigPageDtos.get(i).setStage(stage);
                hcsaConfigPageDtos.get(i).setWorkingGroupId(workingGroupId);
                hcsaConfigPageDtos.get(i).setRoutingSchemeName(routingSchemeName);
            }
        }
    }

    private  List<String>  split(String str){
        String[] split = str.split(",");
        List<String> list=new ArrayList<>();
        Collections.addAll(list,split);
        for(int i=0;i<list.size();i++){
           if("".equals(list.get(i))){
               list.remove(i);
               i--;
           }
        }
        return list;

    }

    private  Map<String, List<HcsaConfigPageDto>>  getTables(HttpServletRequest request){
        Map<String, List<HcsaConfigPageDto>> hcsaConfigPageDtos = getHcsaConfigPageDtos(request);
        Map<String, List<HcsaConfigPageDto>> map = new HashMap<>();
        List<String> types = getType();
        for(String type:types){
            List<HcsaConfigPageDto> hcsaConfigPageDto = hcsaConfigPageDtos.get(type);
            List<HcsaConfigPageDto> appeal=new ArrayList<>();
            if("APTY001".equals(type)){
                appeal= getWorkGrop(type,"appeal");
            }else if("APTY002".equals(type)){
                appeal=  getWorkGrop(type,"new application");
            }else if("APTY005".equals(type)){
               appeal = getWorkGrop(type, "request for change");
            }else if("APTY004".equals(type)){
                appeal=  getWorkGrop(type,"renew");
            }else if("APTY008".equals(type)){
                appeal= getWorkGrop(type,"cessation");
            }else  if("APTY007".equals(type)){
                appeal= getWorkGrop(type,"suspension");
            }else if("APTY006".equals(type)){
                appeal= getWorkGrop(type,"withdrawal");
            }
            else if("APTY003".equals(type)){
                appeal= getWorkGrop(type,"revocation");
            }
            setValueOfhcsaConfigPageDtos(hcsaConfigPageDto,appeal);
            map.put(type,appeal);
        }

        return map;
    }


    //neend to send
    private void  sendEmail(HttpServletRequest request) throws IOException, TemplateException {
        OrgUserDto orgUserDto=(OrgUserDto)request.getSession().getAttribute("orgUserDto");
        String option = (String) request.getAttribute("option");
        Map<String,Object> map=new HashMap<>();
        String userId = orgUserDto.getUserId();
        String serviceName =(String) request.getAttribute("serviceName");
        map.put("serviceName",serviceName);
        map.put("option",option);
        map.put("SystemAdministratorID",userId);
        //todo
        MsgTemplateDto entity = msgTemplateClient.getMsgTemplate("").getEntity();
        String messageContent = entity.getMessageContent();
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(messageContent, map);
        EmailDto emailDto=new EmailDto();
        emailDto.setContent(templateMessageByContent);
        emailDto.setSubject("The following HCSA Service Template:"+serviceName+" has been "+option);
        emailDto.setSender("MOH");
    }

    private void setAttribute(HttpServletRequest request, HcsaServiceDto hcsaServiceDto){
        String effectiveDate = hcsaServiceDto.getEffectiveDate();
        Date parse = null;
        try {
            parse = new SimpleDateFormat("yyyy-MM-dd").parse(effectiveDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String format = new SimpleDateFormat("dd/MM/yyyy").format(parse);
        hcsaServiceDto.setEffectiveDate(format);
        List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getServiceVersions(hcsaServiceDto.getSvcCode()).getEntity();
        request.setAttribute("hcsaServiceDtosVersion",hcsaServiceDtos);
        List<HcsaServiceCategoryDto> hcsaServiceCategoryDto = getHcsaServiceCategoryDto();
        request.setAttribute("hcsaServiceCategoryDtos",hcsaServiceCategoryDto);
        request.setAttribute("hcsaServiceDto", hcsaServiceDto);
        String id = hcsaServiceDto.getId();
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = hcsaConfigClient.getHcsaSvcDocConfigDto(id).getEntity();
        StringBuilder stringBuilder=new StringBuilder();
        for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:hcsaSvcDocConfigDtos){
            stringBuilder.append(",");
            stringBuilder.append(hcsaSvcDocConfigDto.getDocTitle());
        }
        if(!hcsaSvcDocConfigDtos.isEmpty()){
            Boolean isMandatory = hcsaSvcDocConfigDtos.get(0).getIsMandatory();
            if(isMandatory){
                request.setAttribute("documentMandatory",isMandatory);
            }
            request.setAttribute("NumberDocument",hcsaSvcDocConfigDtos.size());
            request.setAttribute("DescriptionDocument",stringBuilder.toString().substring(1));
        }
        List<HcsaSvcSubtypeOrSubsumedDto> entity = hcsaConfigClient.listSubtype(id).getEntity();
        request.setAttribute("hcsaSvcSubtypeOrSubsumedDto",entity);
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
        Map<String, List<HcsaConfigPageDto>> hcsaConfigPageDtos = getHcsaConfigPageDtos(hcsaServiceDto);
        request.setAttribute("routingStagess", hcsaConfigPageDtos);
    }
    private void sendStartOrEndDateChangeEmail(HttpServletRequest request) throws IOException, TemplateException {
        OrgUserDto orgUserDto=(OrgUserDto)request.getSession().getAttribute("orgUserDto");
        Map<String,Object> map=new HashMap<>();
        String userId = orgUserDto.getUserId();
        String serviceName = request.getParameter("serviceName");
        map.put("serviceName",serviceName);
        map.put("SystemAdministratorID",userId);
        //todo
        MsgTemplateDto entity = msgTemplateClient.getMsgTemplate("").getEntity();
        String messageContent = entity.getMessageContent();
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(messageContent, map);
        EmailDto emailDto=new EmailDto();
        emailDto.setContent(templateMessageByContent);
        emailDto.setSubject("The Effective Start/End Date of the following HCSA Service Template: "+serviceName+"  has been amended");
        emailDto.setSender("MOH");
    }

    private void sendURL(HttpServletRequest request,HttpServletResponse response){
        StringBuilder url = new StringBuilder();
        url.append("https://").append(request.getServerName())
                .append("/main-web/eservice/INTRANET/MohBackendInbox");
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(),request);
        try {
            response.sendRedirect(tokenUrl);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
