package com.ecquaria.cloud.moh.iais.service.impl;


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
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
    public void saveOrUpdate(HttpServletRequest request) {

        Map<String, String> errorMap = new TreeMap<>();
        HcsaServiceConfigDto hcsaServiceConfigDto = getDateOfHcsaService(request);
        doValidate(hcsaServiceConfigDto, errorMap);
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
            Map<String, List<HcsaConfigPageDto>> map = new TreeMap<>();

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

        List<HcsaServiceCategoryDto> hcsaServiceCategoryDto = getHcsaServiceCategoryDto();
        request.setAttribute("hcsaServiceCategoryDtos",hcsaServiceCategoryDto);

    }

    @Override
    public void update(HttpServletRequest request) {
        Map<String, String> errorMap = new TreeMap<>();
        String crud_action_value = request.getParameter("crud_action_value");
        HcsaServiceConfigDto hcsaServiceConfigDto = getDateOfHcsaService(request);
        doValidate(hcsaServiceConfigDto, errorMap);
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

            request.setAttribute("errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            return;
        }

        if ("save".equals(crud_action_value)) {
            HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
            Integer i = (int) Double.parseDouble(hcsaServiceDto.getVersion()) + 1;
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
        }else if("version".equals(crud_action_value)){
            request.setAttribute("crud_action_type", "version");

        }

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
                    hcsaServiceSubTypeDto.setServiceId(str);
                    list.add(hcsaServiceSubTypeDto);
                }
            }

        } else if ("SVTP003".equals(serviceType)) {

            if (preRequisite != null){
                for(String str : preRequisite){
                    HcsaServiceSubTypeDto hcsaServiceSubTypeDto=new HcsaServiceSubTypeDto();
                    hcsaServiceSubTypeDto.setServiceId(str);
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
                    hcsaSvcDocConfigDto.setServiceId("");
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

    private void doValidate(HcsaServiceConfigDto hcsaServiceConfigDto, Map<String, String> errorMap) {
        HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = hcsaServiceConfigDto.getHcsaSvcStageWorkingGroupDtos();
        for (int i = 0; i < hcsaSvcStageWorkingGroupDtos.size(); i++) {
            String stageWorkGroupId = hcsaSvcStageWorkingGroupDtos.get(i).getStageWorkGroupId();
            if (StringUtil.isEmpty(stageWorkGroupId)) {
                errorMap.put("stageWorkGroupId" + i, "UC_CHKLMD001_ERR001");
            }

        }

        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = hcsaServiceConfigDto.getHcsaSvcDocConfigDtos();
        if(hcsaSvcDocConfigDtos.isEmpty()){

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
        }else if("SVTP002".equals(svcType)){
            String categoryId = hcsaServiceDto.getCategoryId();
            if(StringUtil.isEmpty(categoryId)){
                errorMap.put("Subsumption","UC_CHKLMD001_ERR001");
            }
        }else if("SVTP003".equals(svcType)){
            String categoryId = hcsaServiceDto.getCategoryId();
            if(StringUtil.isEmpty(categoryId)){
                errorMap.put("Prerequisite","UC_CHKLMD001_ERR001");

            }

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
            }else  if(mandatoryCount<0){
                errorMap.put("mandatoryCount"+i,"Please enter a valid number");
            }
            if (StringUtil.isEmpty(maximumCount)) {
                errorMap.put("maximumCount" + i, "UC_CHKLMD001_ERR001");
            }else if(maximumCount<0){
                errorMap.put("maximumCount"+i,"Please enter a valid number");
            }
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
        List<HcsaSvcSpecificStageWorkloadDto> hcsaSvcSpecificStageWorkloadDtos = hcsaServiceConfigDto.getHcsaSvcSpecificStageWorkloadDtos();
        for (int i = 0; i < hcsaSvcSpecificStageWorkloadDtos.size(); i++) {
            Integer manhourCount = hcsaSvcSpecificStageWorkloadDtos.get(i).getManhourCount();
            if (StringUtil.isEmpty(manhourCount)) {
                errorMap.put("manhourCount" + i, "UC_CHKLMD001_ERR001");
            }else if(manhourCount<0) {
                errorMap.put("manhourCount" + i, "Please enter a valid number");
            }
        }

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



    private List<HcsaServiceCategoryDto> getHcsaServiceCategoryDto() {

        List<HcsaServiceCategoryDto> hcsaServiceCategoryDtos = hcsaConfigClient.getHcsaServiceCategorys().getEntity();
        return hcsaServiceCategoryDtos;
    }

    private void view(HttpServletRequest request, String crud_action_value) {

        HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(crud_action_value).getEntity();
        List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getServiceVersions(hcsaServiceDto.getSvcCode()).getEntity();
        request.setAttribute("hcsaServiceDtosVersion",hcsaServiceDtos);

        setAttribute(request,hcsaServiceDto);
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


    private void setAttribute(HttpServletRequest request, HcsaServiceDto hcsaServiceDto){
        List<HcsaServiceCategoryDto> hcsaServiceCategoryDto = getHcsaServiceCategoryDto();
        request.setAttribute("hcsaServiceCategoryDtos",hcsaServiceCategoryDto);
        String effectiveDate = hcsaServiceDto.getEffectiveDate();
        Date parse = null;
        try {
            parse = new SimpleDateFormat("yyyy-MM-dd").parse(effectiveDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        String format = new SimpleDateFormat("dd/MM/yyyy").format(parse);
        hcsaServiceDto.setEffectiveDate(format);
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
    }
    //neend to send
    private void  sendEmail(HttpServletRequest request) throws IOException, TemplateException {
        OrgUserDto orgUserDto=(OrgUserDto)request.getSession().getAttribute("orgUserDto");
        String option = (String) request.getAttribute("option");
        Map<String,Object> map=new TreeMap<>();
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

    private void sendStartOrEndDateChangeEmail(HttpServletRequest request) throws IOException, TemplateException {
        OrgUserDto orgUserDto=(OrgUserDto)request.getSession().getAttribute("orgUserDto");
        Map<String,Object> map=new TreeMap<>();
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
}
