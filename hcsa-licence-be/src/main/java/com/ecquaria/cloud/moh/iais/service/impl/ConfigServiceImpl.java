package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.HcsaConfigPageDto;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.CessationBeService;
import com.ecquaria.cloud.moh.iais.service.ConfigService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
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
    private static final String DATE_FORMAT="yyyy-MM-dd";

    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private EmailClient emailClient;

    @Value("${iais.email.sender}")
    private String mailSender;
    @Autowired
    private BeEicGatewayClient gatewayClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;

    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Override
    public List<HcsaServiceDto> getAllHcsaServices(HttpServletRequest request) {
        List<HcsaServiceDto> entity = hcsaConfigClient.allHcsaService().getEntity();
        for(int i=0;i<entity.size();i++){
            if("CMSTAT005".equals(entity.get(i).getStatus())){
                entity.remove(entity.get(i));
                i--;
            }else {
                String effectiveDate = entity.get(i).getEffectiveDate();
                try {
                Date pare=new SimpleDateFormat(DATE_FORMAT).parse(effectiveDate);
                String format = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).format(pare);
                entity.get(i).setEffectiveDate(format);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }

        request.setAttribute("hcsaServiceDtos", entity);
        return entity;
    }

    @Override
    public void viewPageInfo(HttpServletRequest request) {
        String crud_action_value = request.getParameter("crud_action_value");
        String crud_action_type = request.getParameter("crud_action_type");
        if("version".equals(crud_action_value)){
            String crud_action_additional = ParamUtil.getMaskedString(request,"crud_action_additional");
            log.info(crud_action_additional);
            HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(crud_action_additional).getEntity();
            List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getServiceVersions(hcsaServiceDto.getSvcCode()).getEntity();
            request.setAttribute("hcsaServiceDtosVersion",hcsaServiceDtos);
            setAttribute(request,hcsaServiceDto);
        }else if(crud_action_value != null && !"".equals(crud_action_value) && "edit".equals(crud_action_type)){
            String crud_action_value1 = ParamUtil.getMaskedString(request, "crud_action_value");
            view(request, crud_action_value1);
        }else if("back".equals(crud_action_value)){
            String crud_action_additional = request.getParameter("crud_action_additional");
            view(request, crud_action_additional);
        }


    }

    @Override
    public void editPageInfo(HttpServletRequest request) {


    }

    @Override
    public void saveOrUpdate(HttpServletRequest request, HttpServletResponse response, HcsaServiceConfigDto hcsaServiceConfigDto) throws Exception{
        String crud_action_value = request.getParameter("crud_action_value");
        if("cancel".equals(crud_action_value)){
            sendURL(request,response);
            return;
        }else if("back".equals(crud_action_value)){
            request.setAttribute("crud_action_type", "back");
            return;
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
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
            Set<String> premisesSet = IaisCommonUtils.genNewHashSet();
            for (HcsaSvcSpePremisesTypeDto hcsaSvcSpePremisesTypeDto : hcsaSvcSpePremisesTypeDtos) {
                premisesSet.add(hcsaSvcSpePremisesTypeDto.getPremisesType());
            }

            List<HcsaConfigPageDto> hcsaConfigPageDtos = (List<HcsaConfigPageDto>) request.getAttribute("hcsaConfigPageDtos");
            request.setAttribute("PremisesType", premisesSet);
            request.setAttribute("hcsaServiceDto", hcsaServiceDto);
            request.setAttribute("crud_action_type", "dovalidate");
            Map<String, List<HcsaConfigPageDto>> map = IaisCommonUtils.genNewHashMap();

            map.put("APTY002",hcsaConfigPageDtos);
            request.setAttribute("routingStagess", map);
            request.setAttribute("errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            return;
        }

        String effectiveDate = hcsaServiceDto.getEffectiveDate();
        Date parse = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).parse(effectiveDate);
        String format = new SimpleDateFormat("yyyy-MM-dd").format(parse);
        hcsaServiceDto.setEffectiveDate(format);
        hcsaServiceConfigDto = hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto).getEntity();
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

        gatewayClient.saveFeServiceConfig(hcsaServiceConfigDto,signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        HcsaServiceCacheHelper.receiveServiceMapping();
        // todo send email
        request.setAttribute("option","added");
        request.setAttribute("serviceName",hcsaServiceDto.getSvcName());
        try {
            sendEmail(request);
        } catch (Exception e) {
         log.error(e.getMessage(),e);
        }
        request.setAttribute("crud_action_type", "save");
    }


    @Override
    public void addNewService(HttpServletRequest request) {

        Map<String, List<HcsaConfigPageDto>> tables = getTables(request);
        request.setAttribute("routingStagess", tables);
        List<HcsaServiceDto> baseHcsaServiceDto = hcsaConfigClient.baseHcsaService().getEntity();
        List<SelectOption> selectOptionList=new ArrayList<>(baseHcsaServiceDto.size());
        for(HcsaServiceDto hcsaServiceDto : baseHcsaServiceDto){
            SelectOption selectOption=new SelectOption();
            selectOption.setValue(hcsaServiceDto.getId());
            selectOption.setText(hcsaServiceDto.getSvcName());
            selectOptionList.add(selectOption);
        }
        request.getSession().setAttribute("selsectBaseHcsaServiceDto",selectOptionList);
    }

    @Override
    public void update(HttpServletRequest request,HttpServletResponse response,  HcsaServiceConfigDto hcsaServiceConfigDto) throws Exception{
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_value = request.getParameter("crud_action_value");
        if("cancel".equals(crud_action_value)){
            sendURL(request,response);
            return;
        }else if("back".equals(crud_action_value)){
            request.setAttribute("crud_action_type", "back");
            return;
        }
         if("version".equals(crud_action_value)){
            request.setAttribute("crud_action_type", "version");
        }else {
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
                 Set<String> premisesSet = IaisCommonUtils.genNewHashSet();
                 for (HcsaSvcSpePremisesTypeDto hcsaSvcSpePremisesTypeDto : hcsaSvcSpePremisesTypeDtos) {
                     premisesSet.add(hcsaSvcSpePremisesTypeDto.getPremisesType());
                 }
                 List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = (List<HcsaServiceStepSchemeDto>) request.getAttribute("hcsaServiceStepSchemeDtos");
                 List<String> stringList = IaisCommonUtils.genNewArrayList();
                 for (HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto : hcsaServiceStepSchemeDtos) {
                     String stepCode = hcsaServiceStepSchemeDto.getStepCode();
                     stringList.add(stepCode);
                 }
                 List<HcsaServiceDto> baseHcsaServiceDtos = hcsaConfigClient.baseHcsaService().getEntity();
                 List<SelectOption> selectOptionList=new ArrayList<>(baseHcsaServiceDtos.size());
                 for(HcsaServiceDto baseHcsaService : baseHcsaServiceDtos){
                     SelectOption selectOption =new SelectOption();
                     selectOption.setText(baseHcsaService.getSvcName());
                     selectOption.setValue(baseHcsaService.getId());
                     selectOptionList.add(selectOption);
                 }
                 request.setAttribute("selsectBaseHcsaServiceDto",selectOptionList);
                 request.setAttribute("hcsaServiceStepSchemeDto", stringList);
                 request.setAttribute("PremisesType", premisesSet);
                 request.setAttribute("hcsaServiceDto", hcsaServiceDto);
                 request.setAttribute("crud_action_type", "validate");

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
                 String effectiveDate = hcsaServiceDto.getEffectiveDate();
                 Date parse = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).parse(effectiveDate);
                 String format = new SimpleDateFormat("yyyy-MM-dd").format(parse);
                 hcsaServiceDto.setEffectiveDate(format);
                 hcsaServiceConfigDto= hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto).getEntity();
                 HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                 HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                 gatewayClient.saveFeServiceConfig(hcsaServiceConfigDto,signature.date(), signature.authorization(),
                         signature2.date(), signature2.authorization());
                 request.setAttribute("crud_action_type", "save");
                 //todo send email update (if start date or end date change need send  Effective Start/End )
                 HcsaServiceCacheHelper.receiveServiceMapping();
         /*   request.setAttribute("option","updated");
            request.setAttribute("serviceName",hcsaServiceDto.getSvcName());
            try {
                sendEmail(request);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } catch (TemplateException e) {
                log.error(e.getMessage(), e);
            }
*/
             }
         }

//        hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto);

    }


    @Override
    public void saData(HttpServletRequest request) {


    }

    @Override
    public void delete(HttpServletRequest request) {
        String crud_action_type = request.getParameter("crud_action_type");
        String crud_action_value = ParamUtil.getMaskedString(request,"crud_action_value");
        if ("delete".equals(crud_action_type)) {
            if (!StringUtil.isEmpty(crud_action_value)) {
                view(request, crud_action_value);
            }

        }

    }

    @Override
    public void deleteOrCancel(HttpServletRequest request,HttpServletResponse response) {
        String crud_action_value = request.getParameter("crud_action_value");
        if(!StringUtil.isEmpty(crud_action_value)){
            if("cancel".equals(crud_action_value)){
                sendURL(request,response);
                return;
            } else if("back".equals(crud_action_value)){
                return;
            }
            Boolean flag = hcsaConfigClient.serviceIdIsUsed(crud_action_value).getEntity();
            HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(crud_action_value).getEntity();
            List<LicenceDto> entity = hcsaLicenceClient.getLicenceDtosBySvcName(hcsaServiceDto.getSvcName()).getEntity();

            if(!entity.isEmpty()){
                request.setAttribute("delete","fail");
                return;
            }
            if(flag){
                request.setAttribute("delete","fail");
                return;

            }
            //todo delete send email
            request.setAttribute("option","deleted");
            request.setAttribute("serviceName",hcsaServiceDto.getSvcName());
            try {
                sendEmail(request);
            } catch (Exception e) {
              log.error(e.getMessage(),e);
            }
            hcsaConfigClient.updateService(crud_action_value);
            request.setAttribute("delete","success");
        }

    }



    private void doValidate(HcsaServiceConfigDto hcsaServiceConfigDto, Map<String, String> errorMap,HttpServletRequest request) throws Exception {
        HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
        Map<String,List<HcsaSvcStageWorkingGroupDto>> hcsaSvcStageWorkingGroupDtoMap=hcsaServiceConfigDto.getHcsaSvcStageWorkingGroupDtoMap();
        hcsaSvcStageWorkingGroupDtoMap.forEach((k,v)->{
            for(int i=0;i<v.size();i++){
                String isMandatory = v.get(i).getIsMandatory();
                if("false".equals(isMandatory)){
                    continue;
                }
                String stageWorkGroupId = v.get(i).getStageWorkGroupId();
                if (StringUtil.isEmpty(stageWorkGroupId)) {
                    errorMap.put("stageWorkGroupId"+k+ i, MessageUtil.replaceMessage("GENERAL_ERR0006","Working Group","field"));
                }
            }
        });

        List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = hcsaServiceConfigDto.getHcsaSvcSubtypeOrSubsumedDtos();
        String pageName = request.getParameter("pageName");
        List<String> subtypeName=IaisCommonUtils.genNewArrayList();
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
            if(StringUtil.isEmpty(pageName)){
                errorMap.put("pageName",MessageUtil.replaceMessage("GENERAL_ERR0006","Page Name","field"));
            }
        }
        String svcCode = hcsaServiceDto.getSvcCode();
        String svcName = hcsaServiceDto.getSvcName();
        String svcDesc = hcsaServiceDto.getSvcDesc();
        String svcDisplayDesc = hcsaServiceDto.getSvcDisplayDesc();
        String svcType = hcsaServiceDto.getSvcType();
        String effectiveDate = hcsaServiceDto.getEffectiveDate();
        Date endDate = hcsaServiceDto.getEndDate();
        boolean serviceIsUsed = hcsaServiceDto.isServiceIsUsed();
        if (StringUtil.isEmpty(effectiveDate)) {
            errorMap.put("effectiveDate", MessageUtil.replaceMessage("GENERAL_ERR0006","Effective Start Date","field"));
        }else if(!serviceIsUsed){
            Date parse = new SimpleDateFormat("dd/MM/yyyy").parse(effectiveDate);
            if(parse.before(new Date())){
                errorMap.put("effectiveDate","Effective Date should be future date");
            }
        }
        if(!StringUtil.isEmpty(endDate)){
            Date parse = new SimpleDateFormat("dd/MM/yyyy").parse(effectiveDate);
            if(endDate.before(parse)){
                errorMap.put("effectiveEndDate", "CHKL_ERR002");
            }
        }

        if (StringUtil.isEmpty(svcCode)) {
            errorMap.put("svcCode", MessageUtil.replaceMessage("GENERAL_ERR0006","Service Code","field"));
        }
        if (StringUtil.isEmpty(svcName)) {
            errorMap.put("svcName", MessageUtil.replaceMessage("GENERAL_ERR0006","Service Name","field"));
        }
        if (StringUtil.isEmpty(svcDisplayDesc)) {
            errorMap.put("svcDisplayDesc", MessageUtil.replaceMessage("GENERAL_ERR0006","Service Display Description","field"));
        }
        if (StringUtil.isEmpty(svcDesc)) {
            errorMap.put("svcDesc", MessageUtil.replaceMessage("GENERAL_ERR0006","Service Description","field"));
        }
        if (StringUtil.isEmpty(svcType)) {
            errorMap.put("svcType", MessageUtil.replaceMessage("GENERAL_ERR0006","Service Type","field"));
        }else if("SVTP002".equals(svcType)){
            List<HcsaServiceSubTypeDto> serviceSubTypeDtos = hcsaServiceDto.getServiceSubTypeDtos();
            if(serviceSubTypeDtos.isEmpty()){
                errorMap.put("Subsumption",MessageUtil.replaceMessage("GENERAL_ERR0006","Base Service Subsumed Under","field"));
            }
        }else if("SVTP003".equals(svcType)){
            List<HcsaServiceSubTypeDto> serviceSubTypeDtos = hcsaServiceDto.getServiceSubTypeDtos();
            if(serviceSubTypeDtos.isEmpty()){
                errorMap.put("Prerequisite",MessageUtil.replaceMessage("GENERAL_ERR0006","Pre-requisite Base Service","field"));
            }
        }
        List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = hcsaServiceConfigDto.getHcsaSvcSpePremisesTypeDtos();
        if (hcsaSvcSpePremisesTypeDtos.isEmpty()) {
            errorMap.put("premieseType", MessageUtil.replaceMessage("GENERAL_ERR0006","Premises Type","field"));
        }
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaServiceConfigDto.getHcsaSvcPersonnelDtos();

        for (int i = 0; i < hcsaSvcPersonnelDtos.size(); i++) {
            String psnType = hcsaSvcPersonnelDtos.get(i).getPsnType();
            int mandatoryCount = hcsaSvcPersonnelDtos.get(i).getMandatoryCount();
            int maximumCount = hcsaSvcPersonnelDtos.get(i).getMaximumCount();
            String pageMandatoryCount = hcsaSvcPersonnelDtos.get(i).getPageMandatoryCount();
            String pageMaximumCount = hcsaSvcPersonnelDtos.get(i).getPageMaximumCount();
            if (StringUtil.isEmpty(psnType)) {
                errorMap.put("psnType" + i, "CHKLMD001_ERR001");
        }
            if (StringUtil.isEmpty(pageMandatoryCount)) {
                errorMap.put("mandatoryCount" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","Minimum Count","field"));
            }else  {
                if(pageMandatoryCount.matches("^[0-9]+$")){
                    int i1 = Integer.parseInt(pageMandatoryCount);
                    if (i1<0){
                        errorMap.put("mandatoryCountCGO", "CHKLMD001_ERR003");
                    }
                }else {
                    errorMap.put("mandatoryCount"+i,"CHKLMD001_ERR003");
                }
            }
            if (StringUtil.isEmpty(pageMaximumCount)) {
                errorMap.put("maximumCount" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","Maximum Count","field"));
            }else {
                if(pageMaximumCount.matches("^[0-9]+$")){
                    int i1 = Integer.parseInt(pageMandatoryCount);
                    if(i1<0){
                        errorMap.put("maximumCount"+i,"CHKLMD001_ERR003");
                    }
                }else {
                    errorMap.put("maximumCount"+i,"CHKLMD001_ERR003");
                }
            }
            if(!StringUtil.isEmpty(mandatoryCount)&&!StringUtil.isEmpty(maximumCount)){
                if(mandatoryCount>maximumCount){
                    errorMap.put("maximumCount"+i,"Maximum Count has to be larger than Minimum Count");
                }
            }
        }
        Map<String, List<HcsaSvcSpeRoutingSchemeDto>> hcsaSvcSpeRoutingSchemeDtoMap =hcsaServiceConfigDto.getHcsaSvcSpeRoutingSchemeDtoMap();
        hcsaSvcSpeRoutingSchemeDtoMap.forEach((k,v)->{
            for(int i=0;i<v.size();i++){
                String isMandatory = v.get(i).getIsMandatory();
                if("false".equals(isMandatory)){
                    continue;
                }
                String schemeType = v.get(i).getSchemeType();
                if (StringUtil.isEmpty(schemeType)) {
                    errorMap.put("schemeType"+k+ i, MessageUtil.replaceMessage("GENERAL_ERR0006","Service Routing Scheme","field"));
                }
            }
        });
        Map<String,List<HcsaSvcSpecificStageWorkloadDto>>map=hcsaServiceConfigDto.getHcsaSvcSpecificStageWorkloadDtoMap();
        map.forEach((k,v)->{
            for(int i=0;i<v.size();i++){
                String isMandatory = v.get(i).getIsMandatory();
                if("false".equals(isMandatory)){
                    continue;
                }else if("".equals(isMandatory)){
                    errorMap.put("isMandatory"+k+i,MessageUtil.replaceMessage("GENERAL_ERR0006","This","field"));
                }
                String stringManhourCount = v.get(i).getStringManhourCount();
                if(StringUtil.isEmpty(stringManhourCount)){
                    errorMap.put("manhourCount"+k+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Service Workload Manhours","field"));
                }else if(!stringManhourCount.matches("^[0-9]+$")){
                    errorMap.put("manhourCount"+k+i,"NEW_ERR0003");
                }
            }
        });
        String numberDocument = hcsaServiceConfigDto.getComDocSize();
        if(StringUtil.isEmpty(numberDocument)){
            errorMap.put("NumberDocument",MessageUtil.replaceMessage("GENERAL_ERR0006","Number of Service-Related Document to be uploaded","field"));
        }else if(!numberDocument.matches("^[0-9]+$")){
            errorMap.put("NumberDocument","NEW_ERR0003");
        }
        String numberfields = hcsaServiceConfigDto.getServiceDocSize();
        if(StringUtil.isEmpty(numberfields)){
            errorMap.put("Numberfields",MessageUtil.replaceMessage("GENERAL_ERR0006","Number of Service-Related General Info fields to be captured","field"));
        }else if(!numberfields.matches("^[0-9]+$")){
            errorMap.put("Numberfields","NEW_ERR0003");
        }
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = (List<HcsaSvcDocConfigDto>)request.getAttribute("serviceDoc");
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtoList = ( List<HcsaSvcDocConfigDto>)request.getAttribute("comDoc");
        if(hcsaSvcDocConfigDtos!=null){
            for(int i = 0; i < hcsaSvcDocConfigDtos.size(); i++){
                String docTitle = hcsaSvcDocConfigDtos.get(i).getDocTitle();
                if(StringUtil.isEmpty(docTitle)){
                    errorMap.put("serviceDoc"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Name of Info Field","field"));
                }
            }
        }
        if(hcsaSvcDocConfigDtoList!=null){
            for(int i = 0;i<hcsaSvcDocConfigDtoList.size();i++){
                String docTitle = hcsaSvcDocConfigDtoList.get(i).getDocTitle();
                if(StringUtil.isEmpty(docTitle)){
                    errorMap.put("commonDoc"+i,MessageUtil.replaceMessage("GENERAL_ERR0006","Name of Info Field","field"));
                }
            }
        }

    }

    private   Map<String, List<HcsaConfigPageDto>> getHcsaConfigPageDtos(HcsaServiceDto hcsaServiceDto) {

        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = getHcsaSvcRoutingStageDtos();

        List<HcsaSvcStageWorkloadDto> hcsaSvcStageWorkloadDtos =
                hcsaConfigClient.getHcsaSvcSpeRoutingSchemeByServiceId(hcsaServiceDto.getId()).getEntity();
        List<String> stageIds = IaisCommonUtils.genNewArrayList();
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
        Map<String,List<HcsaSvcStageWorkloadDto>> map=IaisCommonUtils.genNewHashMap();
        Map<String, List<HcsaSvcSpeRoutingSchemeDto>> hcsaSvcSpeRoutingSchemeDtoMap=IaisCommonUtils.genNewHashMap();
        for(String type:types){
            List<HcsaSvcStageWorkloadDto> hcsaSvcStageWorkloadDtos1=IaisCommonUtils.genNewArrayList();
            List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos1=IaisCommonUtils.genNewArrayList();
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
        Map<String,  List<HcsaSvcStageWorkingGroupDto>> hcsaSvcStageWorkingGroupDtoMap=IaisCommonUtils.genNewHashMap();
        hcsaSvcSpeRoutingSchemeDtoMap.forEach((k,v)->{
            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos1=IaisCommonUtils.genNewArrayList();
            for(HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto:v){
                String stageWrkGrpID = hcsaSvcSpeRoutingSchemeDto.getStageWrkGrpID();
                for(HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto:hcsaSvcStageWorkingGroupDtos){
                    if(stageWrkGrpID.equals(hcsaSvcStageWorkingGroupDto.getId())&&1==hcsaSvcStageWorkingGroupDto.getOrder()){
                        hcsaSvcStageWorkingGroupDtos1.add(hcsaSvcStageWorkingGroupDto);
                    }

                }

            }
            hcsaSvcStageWorkingGroupDtoMap.put(k,hcsaSvcStageWorkingGroupDtos1);
        });
        Map<String, List<HcsaConfigPageDto>> hcsaConfigPageDtoMap=IaisCommonUtils.genNewHashMap();
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
                            if(hcsaSvcStageWorkingGroupDto.getStageWorkGroupId().equals(workingGroupId)&&stageWrkGrpID.equals(hcsaSvcStageWorkingGroupDto.getId())){
                                hcsaConfigPageDto.setRoutingSchemeName(schemeType);
                            }
                        }
                }


            }
            for (HcsaConfigPageDto hcsaConfigPageDto:hcsaConfigPageDtos){
                sendHcsaConfigPageDtoTypeName(hcsaConfigPageDto,type);
                if(hcsaConfigPageDto.getRoutingSchemeName()!=null){
                    hcsaConfigPageDto.setIsMandatory("true");
                }else if(hcsaConfigPageDto.getRoutingSchemeName()==null){
                    hcsaConfigPageDto.setIsMandatory("false");
                }
            }

            hcsaConfigPageDtoMap.put(type,hcsaConfigPageDtos);
        }




        return hcsaConfigPageDtoMap;
    }

    private  List<HcsaConfigPageDto> ProcessingData(String type,List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos,List<HcsaSvcStageWorkloadDto> hcsaSvcStageWorkloadDtos,List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos,  List<WorkingGroupDto> hcsa  ){
        List<HcsaConfigPageDto> hcsaConfigPageDtos = IaisCommonUtils.genNewArrayList();
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
                        hcsaConfigPageDto.setManhours(manhourCount+"");
                        sendHcsaConfigPageDtoTypeName(hcsaConfigPageDto,type);
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

    @Override
    public List<HcsaSvcRoutingStageDto> getHcsaSvcRoutingStageDtos() {
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = hcsaConfigClient.stagelist().getEntity();
        for (int i = 0; i < hcsaSvcRoutingStageDtos.size(); i++) {
            String stageOrder = hcsaSvcRoutingStageDtos.get(i).getStageOrder();
            try {
                if (Integer.parseInt(stageOrder) % 100 != 0) {
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
        Map<String ,  List<HcsaConfigPageDto>> map=IaisCommonUtils.genNewHashMap();
        if(hcsaConfigPageDtos1!=null){
            for(String type:types){
                List<HcsaConfigPageDto> list=IaisCommonUtils.genNewArrayList();
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
        List<HcsaConfigPageDto> hcsaConfigPageDtos = IaisCommonUtils.genNewArrayList();
        for(String type:types){
            for (HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtos) {
                HcsaConfigPageDto hcsaConfigPageDto = new HcsaConfigPageDto();
                hcsaConfigPageDto.setStageCode(hcsaSvcRoutingStageDto.getStageCode());
                hcsaConfigPageDto.setStageName(hcsaSvcRoutingStageDto.getStageName());
                sendHcsaConfigPageDtoTypeName(hcsaConfigPageDto,type);
                getWorkingGroupDto(workingGroup,hcsaSvcRoutingStageDto,hcsaConfigPageDto);
                hcsaConfigPageDtos.add(hcsaConfigPageDto);
            }

        }

        if (hcsaConfigPageDtos1 != null) {
            for (int i = 0; i < hcsaConfigPageDtos.size(); i++) {
                String isMandatory = hcsaConfigPageDtos1.get(i).getIsMandatory();
                if("optional".equals(isMandatory)){
                    hcsaConfigPageDtos.get(i).setIsMandatory("false");
                    continue;
                }
                String manhours = hcsaConfigPageDtos1.get(i).getManhours();
                String workingGroupId = hcsaConfigPageDtos1.get(i).getWorkingGroupId();
                String workloadId = hcsaConfigPageDtos1.get(i).getWorkloadId();
                String routingSchemeId = hcsaConfigPageDtos1.get(i).getRoutingSchemeId();
                String workStageId = hcsaConfigPageDtos1.get(i).getWorkStageId();
                hcsaConfigPageDtos.get(i).setManhours(manhours);
                hcsaConfigPageDtos.get(i).setWorkingGroupId(workingGroupId);
                hcsaConfigPageDtos.get(i).setWorkloadId(workloadId);
                hcsaConfigPageDtos.get(i).setRoutingSchemeId(routingSchemeId);
                hcsaConfigPageDtos.get(i).setWorkStageId(workStageId);
                hcsaConfigPageDtos.get(i).setIsMandatory("true");
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
        Boolean flag = hcsaConfigClient.serviceIdIsUsed(crud_action_value).getEntity();
        List<LicenceDto> entity = hcsaLicenceClient.getLicenceDtosBySvcName(hcsaServiceDto.getSvcName()).getEntity();
        if(flag || !entity.isEmpty()){
            hcsaServiceDto.setServiceIsUsed(true);
        }else {
            hcsaServiceDto.setServiceIsUsed(false);
        }
        setAttribute(request,hcsaServiceDto);
    }


    private void getWorkingGroupDto(List<WorkingGroupDto> hcsa, HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto, HcsaConfigPageDto hcsaConfigPageDto) {
        List<WorkingGroupDto> workingGroupDtoList = IaisCommonUtils.genNewArrayList();
        for (WorkingGroupDto workingGroupDto : hcsa) {
            String groupName = workingGroupDto.getGroupName();
            String stageCode = hcsaSvcRoutingStageDto.getStageCode();
            if (groupName.contains("Admin Screening") && stageCode.contains("ASO")) {
                workingGroupDtoList.add(workingGroupDto);
            }
            if (groupName.contains("Professional Screening") && stageCode.contains("PSO")) {
                workingGroupDtoList.add(workingGroupDto);
            }
            if (groupName.contains("Inspection Stage") && stageCode.contains("INS")) {
                workingGroupDtoList.add(workingGroupDto);
            }
            if (groupName.contains("Level 1 Approval") && stageCode.contains("AO1")) {
                workingGroupDtoList.add(workingGroupDto);
            }
            if (groupName.contains("Level 2 Approval") && stageCode.contains("AO2")) {
                workingGroupDtoList.add(workingGroupDto);
            }
            if (groupName.contains("Level 3 Approval") && stageCode.contains("AO3")) {
                workingGroupDtoList.add(workingGroupDto);
            }
            hcsaConfigPageDto.setWorkingGroup(workingGroupDtoList);
        }
    }

    private List<HcsaConfigPageDto> getWorkGrop(String type,String typeName){
        List<HcsaConfigPageDto> hcsaConfigPageDtos = IaisCommonUtils.genNewArrayList();
        List<WorkingGroupDto> workingGroup = getWorkingGroup();
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = getHcsaSvcRoutingStageDtos();
        for (HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtos) {
            HcsaConfigPageDto hcsaConfigPageDto = new HcsaConfigPageDto();
            hcsaConfigPageDto.setStageCode(hcsaSvcRoutingStageDto.getStageCode());
            hcsaConfigPageDto.setStageName(hcsaSvcRoutingStageDto.getStageName());
            hcsaConfigPageDto.setAppTypeName(typeName);
            hcsaConfigPageDto.setAppType(type);
            List<WorkingGroupDto> workingGroupDtoList = IaisCommonUtils.genNewArrayList();
            for (WorkingGroupDto workingGroupDto : workingGroup) {
                String groupName = workingGroupDto.getGroupName();
                String stageCode = hcsaSvcRoutingStageDto.getStageCode();
                if (groupName.contains("Admin Screening") && stageCode.contains("ASO")) {
                    workingGroupDtoList.add(workingGroupDto);
                }
                if (groupName.contains("Professional Screening") && stageCode.contains("PSO")) {
                    workingGroupDtoList.add(workingGroupDto);
                }
                if (groupName.contains("Inspection Stage") && stageCode.contains("INS")) {
                    workingGroupDtoList.add(workingGroupDto);
                }
                if (groupName.contains("Level 1 Approval") && stageCode.contains("AO1")) {
                    workingGroupDtoList.add(workingGroupDto);
                }
                if (groupName.contains("Level 2 Approval") && stageCode.contains("AO2")) {
                    workingGroupDtoList.add(workingGroupDto);
                }
                if (groupName.contains("Level 3 Approval") && stageCode.contains("AO3")) {
                    workingGroupDtoList.add(workingGroupDto);
                }
                hcsaConfigPageDto.setWorkingGroup(workingGroupDtoList);
            }

            hcsaConfigPageDtos.add(hcsaConfigPageDto);
        }
        return  hcsaConfigPageDtos;
    }

    @Override
    public List<String> getType(){
        List<String> list=IaisCommonUtils.genNewArrayList();
        list.add(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
       /* list.add(ApplicationConsts.APPLICATION_TYPE_REINSTATEMENT);*/
        list.add(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
        list.add(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL) ;
        list.add(ApplicationConsts.APPLICATION_TYPE_CESSATION) ;
     /*   list.add("APTY007") ;*/
        list.add(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        list.add(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        return list;
    }

    private void setValueOfhcsaConfigPageDtos( List<HcsaConfigPageDto> hcsaConfigPageDtos1  ,List<HcsaConfigPageDto> hcsaConfigPageDtos){
        if (hcsaConfigPageDtos1 != null) {
            for (int i = 0; i < hcsaConfigPageDtos.size(); i++) {
                String manhours = hcsaConfigPageDtos1.get(i).getManhours();
                String stage = hcsaConfigPageDtos1.get(i).getStage();
                String workingGroupId = hcsaConfigPageDtos1.get(i).getWorkingGroupId();
                String routingSchemeName = hcsaConfigPageDtos1.get(i).getRoutingSchemeName();
                String isMandatory = hcsaConfigPageDtos1.get(i).getIsMandatory();
                hcsaConfigPageDtos.get(i).setManhours(manhours);
                hcsaConfigPageDtos.get(i).setStage(stage);
                hcsaConfigPageDtos.get(i).setWorkingGroupId(workingGroupId);
                hcsaConfigPageDtos.get(i).setRoutingSchemeName(routingSchemeName);
                hcsaConfigPageDtos.get(i).setIsMandatory(isMandatory);
            }
        }
    }

    @Override
    public   List<String>  split(String str){
        String[] split = str.split(",");
        List<String> list=IaisCommonUtils.genNewArrayList();
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
        Map<String, List<HcsaConfigPageDto>> map = IaisCommonUtils.genNewHashMap();
        List<String> types = getType();
        for(String type:types){
            List<HcsaConfigPageDto> hcsaConfigPageDto = hcsaConfigPageDtos.get(type);
            List<HcsaConfigPageDto> appeal=IaisCommonUtils.genNewArrayList();
            if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(type)){
                appeal= getWorkGrop(type,"Appeal");
            }else if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(type)){
                appeal=  getWorkGrop(type,"New Application");
            }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(type)){
               appeal = getWorkGrop(type, "Request For Change");
            }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(type)){
                appeal=  getWorkGrop(type,"Renew");
            }else if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(type)){
                appeal= getWorkGrop(type,"Cessation");
            }else  if("APTY007".equals(type)){
                appeal= getWorkGrop(type,"Suspension");
            }else if(ApplicationConsts.APPLICATION_TYPE_REINSTATEMENT.equals(type)){
                appeal= getWorkGrop(type,"Revocation");
            }else if(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(type)){
                appeal= getWorkGrop(type,"Withdrawal");
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
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        String userId = orgUserDto.getUserId();
        String serviceName =(String) request.getAttribute("serviceName");
        map.put("serviceName",serviceName);
        map.put("option",option);
        map.put("SystemAdministratorID",userId);
        //todo
        MsgTemplateDto entity = msgTemplateClient.getMsgTemplate("978FD1F4-616E-EA11-BE82-000C29F371DC").getEntity();
        String messageContent = entity.getMessageContent();
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(messageContent, map);
        EmailDto emailDto=new EmailDto();
        emailDto.setContent(templateMessageByContent);
        emailDto.setSubject("The following HCSA Service Template:"+serviceName+" has been "+option);
        emailDto.setSender(mailSender);
        List<String> address=new ArrayList<>();
        address.add(orgUserDto.getEmail());
        emailDto.setReceipts(address);
        emailClient.sendNotification(emailDto).getEntity();
    }

    private void setAttribute(HttpServletRequest request, HcsaServiceDto hcsaServiceDto){
        String effectiveDate = hcsaServiceDto.getEffectiveDate();
        Date parse;
        try {
            parse = new SimpleDateFormat(DATE_FORMAT).parse(effectiveDate);
            String format = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).format(parse);
            hcsaServiceDto.setEffectiveDate(format);
        } catch (ParseException e) {
          log.error(e.getMessage(),e);
        }
        List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getServiceVersions(hcsaServiceDto.getSvcCode()).getEntity();
        request.setAttribute("hcsaServiceDtosVersion",hcsaServiceDtos);
        List<HcsaServiceDto> baseHcsaServiceDtos = hcsaConfigClient.baseHcsaService().getEntity();
        List<SelectOption> selectOptionList=new ArrayList<>(baseHcsaServiceDtos.size());
        for(HcsaServiceDto baseHcsaService : baseHcsaServiceDtos){
            SelectOption selectOption =new SelectOption();
            selectOption.setText(baseHcsaService.getSvcName());
            selectOption.setValue(baseHcsaService.getId());
            selectOptionList.add(selectOption);
        }
        List<HcsaSvcSpecifiedCorrelationDto> hcsaSvcSpecifiedCorrelationDtos = hcsaConfigClient.getHcsaSvcSpecifiedCorrelationDto(hcsaServiceDto.getId()).getEntity();
        StringBuilder stringBuilder=new StringBuilder();
        for(HcsaSvcSpecifiedCorrelationDto hcsaSvcSpecifiedCorrelationDto : hcsaSvcSpecifiedCorrelationDtos){
            stringBuilder.append(hcsaSvcSpecifiedCorrelationDto.getBaseSvcId()).append('#');
        }
        String string = stringBuilder.toString();
        if(!StringUtil.isEmpty(string)){
            request.setAttribute("selectSubsumption",string.substring(0,string.lastIndexOf('#')));
            request.setAttribute("selectPreRequisite",string.substring(0,string.lastIndexOf('#')));
        }
        request.setAttribute("selsectBaseHcsaServiceDto",selectOptionList);
        request.setAttribute("hcsaServiceDto", hcsaServiceDto);
        String id = hcsaServiceDto.getId();
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = hcsaConfigClient.getHcsaSvcDocConfigDto(id).getEntity();

        if(hcsaSvcDocConfigDtos!=null){
            request.setAttribute("serviceDocSize",hcsaSvcDocConfigDtos.size());
            request.setAttribute("serviceDoc",hcsaSvcDocConfigDtos);
        }
        Map<String,String> docMap = IaisCommonUtils.genNewHashMap();
        docMap.put("common", "0");
        docMap.put("premises", "1");
        String docMapJson = JsonUtil.parseToJson(docMap);
        List<HcsaSvcDocConfigDto> comDocConfigDtos =  hcsaConfigClient.getHcsaSvcDocConfig(docMapJson).getEntity();
        request.setAttribute("comDocSize",comDocConfigDtos.size());
        request.setAttribute("comDoc",comDocConfigDtos);
        List<HcsaSvcSubtypeOrSubsumedDto> entity = hcsaConfigClient.listSubtype(id).getEntity();
        request.setAttribute("hcsaSvcSubtypeOrSubsumedDto",entity);
        List<String> ids = IaisCommonUtils.genNewArrayList();
        ids.add(id);
        Set<String> set = hcsaConfigClient.getAppGrpPremisesTypeBySvcId(ids).getEntity();
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = hcsaConfigClient.getHcsaServiceStepSchemeDtoByServiceId(hcsaServiceDto.getId()).getEntity();
        List<String> stringList = IaisCommonUtils.genNewArrayList();
        for (HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto : hcsaServiceStepSchemeDtos) {
            String stepCode = hcsaServiceStepSchemeDto.getStepCode();
            stringList.add(stepCode);
        }
        request.setAttribute("PremisesType", set);
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaConfigClient.getSvcPersonnelByServiceId(id).getEntity();
        for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : hcsaSvcPersonnelDtos) {
            String psnType = hcsaSvcPersonnelDto.getPsnType();
            hcsaSvcPersonnelDto.setPageMandatoryCount(hcsaSvcPersonnelDto.getMandatoryCount()+"");
            hcsaSvcPersonnelDto.setPageMaximumCount(hcsaSvcPersonnelDto.getMaximumCount()+"");
            request.setAttribute(psnType, hcsaSvcPersonnelDto);
        }

        request.setAttribute("hcsaServiceStepSchemeDto", stringList);
        Map<String, List<HcsaConfigPageDto>> hcsaConfigPageDtos = getHcsaConfigPageDtos(hcsaServiceDto);
        request.setAttribute("routingStagess", hcsaConfigPageDtos);
    }
    private void sendStartOrEndDateChangeEmail(HttpServletRequest request) throws IOException, TemplateException {
        OrgUserDto orgUserDto=(OrgUserDto)request.getSession().getAttribute("orgUserDto");
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        String userId = orgUserDto.getUserId();
        String serviceName = request.getParameter("serviceName");
        map.put("serviceName",serviceName);
        map.put("SystemAdministratorID",userId);
        //todo
        MsgTemplateDto entity = msgTemplateClient.getMsgTemplate("2E06165B-626E-EA11-BE82-000C29F371DC").getEntity();
        String messageContent = entity.getMessageContent();
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(messageContent, map);
        EmailDto emailDto=new EmailDto();
        emailDto.setContent(templateMessageByContent);
        emailDto.setSubject("The Effective Start/End Date of the following HCSA Service Template: "+serviceName+"  has been amended");
        emailDto.setSender(mailSender);
        emailDto.setClientQueryCode("isNotAuto");
        //address
        List<String> address=new ArrayList<>();
        address.add(orgUserDto.getEmail());
        emailDto.setReceipts(address);
        emailClient.sendNotification(emailDto).getEntity();
    }

    private void sendURL(HttpServletRequest request,HttpServletResponse response){
        StringBuilder url = new StringBuilder();
        url.append("https://").append(request.getServerName())
                .append("/main-web/eservice/INTRANET/MohBackendInbox");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(),request);
        try {
            response.sendRedirect(tokenUrl);
            request.getSession().removeAttribute("orgUserDto");
        } catch (IOException e) {
          log.error(e.getMessage(),e);
        }
    }

    private void sendHcsaConfigPageDtoTypeName(HcsaConfigPageDto hcsaConfigPageDto,String type){
        hcsaConfigPageDto.setAppType(type);
        if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(type)){
            hcsaConfigPageDto.setAppTypeName("New Application");
        }else if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(type)){
            hcsaConfigPageDto.setAppTypeName("Appeal");
        } else if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(type)) {
            hcsaConfigPageDto.setAppTypeName("Request For Change");
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(type)){
            hcsaConfigPageDto.setAppTypeName("Renew");
        }else if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(type)){
            hcsaConfigPageDto.setAppTypeName("Cessation");
        }else  if("APTY007".equals(type)){
            hcsaConfigPageDto.setAppTypeName("Suspension");
        }else if(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(type)){
            hcsaConfigPageDto.setAppTypeName("Withdrawal");
        }

    }
}
