package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCategoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcCateWrkgrpCorrelationDto;
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
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.HcsaConfigPageDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ConfigService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceCommonClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author Wenkang
 * @date 2020/2/12 17:36
 */
@Service
@Slf4j
public class ConfigServiceImpl implements ConfigService {

    private static final String DATE_FORMAT = "yyyy-MM-dd";
    private static final String VERSION = "version";
    private static final String DATE_PARSE = "yyyy-MM-dd HH:mm:ss";
    private static final String ILLEGAL_OPERATION = "Illegal operation";
    private static final String NEW_APPLICATION="New Application";
    private static final String  APPEAL="Appeal";
    private static final String REQUEST_FOR_CHANGE="Request For Change";
    private static final String RENEW="Renew";
    private static final String CESSATION ="Cessation";
    private static final String SUSPENSION="Suspension";
    private static final String WITHDRAWAL="Withdrawal";

    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private MsgTemplateClient msgTemplateClient;
    @Autowired
    private HcsaLicenceCommonClient hcsaLicenceClient;
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
    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;
    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    private CopyOnWriteArrayList<HcsaServiceCategoryDto> hcsaServiceCatgoryDtos;

    private static List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos;

    @Autowired
    public ConfigServiceImpl(HcsaConfigClient hcsaConfigClient) {
        this.hcsaConfigClient = hcsaConfigClient;
        this.hcsaServiceCatgoryDtos = hcsaConfigClient.getHcsaServiceCategorys().getEntity();
    }

    @Override
    public List<HcsaServiceDto> getAllHcsaServices() {
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
        Collections.sort(entity, (o1, o2) -> {
            if(o1.getSvcName().equals(o2.getSvcName())){
               return (int) (Double.parseDouble(o1.getVersion())-Double.parseDouble(o2.getVersion()));
            }else {
                return o1.getSvcName().compareTo(o2.getSvcName());
            }
        });
        return entity;
    }

    @Override
    public void viewPageInfo(HttpServletRequest request) {
        String crud_action_value = request.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
        String crud_action_type = request.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        if(VERSION.equals(crud_action_value)){
            String crud_action_additional = ParamUtil.getMaskedString(request,"crud_action_additional");
            log.info(crud_action_additional);
            HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(crud_action_additional).getEntity();
            List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getServiceVersions(hcsaServiceDto.getSvcCode()).getEntity();
            Boolean flag = hcsaConfigClient.serviceIdIsUsed(crud_action_additional).getEntity();
            List<LicenceDto> entity = hcsaLicenceClient.getLicenceDtosBySvcName(hcsaServiceDto.getSvcName()).getEntity();
            if(flag || !entity.isEmpty()){
                hcsaServiceDto.setServiceIsUsed(true);
            }else {
                hcsaServiceDto.setServiceIsUsed(false);
            }
            request.setAttribute("hcsaServiceDtosVersion",hcsaServiceDtos);
            request.setAttribute(VERSION,VERSION);
            setAttribute(request,hcsaServiceDto);
        }else if(crud_action_value != null && !"".equals(crud_action_value) && "edit".equals(crud_action_type)){
            String crud_action_value1 = ParamUtil.getMaskedString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
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
        String crud_action_value = request.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
        if("cancel".equals(crud_action_value)){
            sendURL(request,response);
            return;
        }else if("back".equals(crud_action_value)){
            request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, "back");
            return;
        }
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        doValidate(hcsaServiceConfigDto, errorMap,request);
        HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
        Map<String, Boolean> entity = hcsaConfigClient.isExistHcsaService(hcsaServiceDto).getEntity();
        Boolean svcCode = entity.get("svcCode");
        if(svcCode!=null&&svcCode){
            errorMap.put("code",MessageUtil.getMessageDesc("SC_ERR002"));
        }
        Boolean svcName = entity.get("svcName");
        if(svcName!=null&&svcName){
            errorMap.put("svcName",MessageUtil.getMessageDesc("SC_ERR001"));
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

            request.setAttribute("PremisesType", premisesSet);
            request.setAttribute("hcsaServiceDto", hcsaServiceDto);
            request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, "dovalidate");
            request.setAttribute(IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            request.setAttribute(IaisEGPConstant.ERRORMAP, errorMap);
            return;
        }
        String effectiveDate = hcsaServiceDto.getEffectiveDate();
        Date parse = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).parse(effectiveDate);
        String format = new SimpleDateFormat("yyyy-MM-dd").format(parse);
        hcsaServiceDto.setEffectiveDate(format);
        transFor(hcsaServiceConfigDto);

        hcsaServiceConfigDto = hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto).getEntity();
        eicGateway(hcsaServiceConfigDto);
        HcsaServiceCacheHelper.flushServiceMapping();
        // todo send email
        request.setAttribute("option","added");
        request.setAttribute("serviceName",hcsaServiceDto.getSvcName());
        try {
          /*  sendEmail(request);*/
        } catch (Exception e) {
         log.error(e.getMessage(),e);
        }
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, "save");
    }
    static String[] code ={ApplicationConsts.SERVICE_TYPE_BASE,ApplicationConsts.SERVICE_TYPE_SUBSUMED,ApplicationConsts.SERVICE_TYPE_SPECIFIED};

    @Override
    public void addNewService(HttpServletRequest request) {

        Map<String, List<HcsaConfigPageDto>> tables = this.getTables(request);
        request.setAttribute("routingStagess", tables);
        List<HcsaServiceDto> baseHcsaServiceDto = hcsaConfigClient.baseHcsaService().getEntity();
        List<SelectOption> selectOptionList=new ArrayList<>(baseHcsaServiceDto.size());
        for(HcsaServiceDto hcsaServiceDto : baseHcsaServiceDto){
            SelectOption selectOption=new SelectOption();
            selectOption.setValue(hcsaServiceDto.getId());
            selectOption.setText(hcsaServiceDto.getSvcName());
            selectOptionList.add(selectOption);
        }
        List<HcsaServiceCategoryDto> categoryDtos = getHcsaServiceCategoryDto();
        categoryDtos.sort((s1, s2) -> (s1.getName().compareTo(s2.getName())));
        request.getSession().setAttribute("categoryDtos",categoryDtos);
        selectOptionList.sort((s1, s2) -> (s1.getText().compareTo(s2.getText())));
        List<SelectOption> selectOptionList1 = MasterCodeUtil.retrieveOptionsByCodes(code);
        selectOptionList1.sort((s1, s2) -> (s1.getText().compareTo(s2.getText())));
        request.getSession().setAttribute("codeSelectOptionList",selectOptionList1);
        request.getSession().setAttribute("selsectBaseHcsaServiceDto",selectOptionList);
    }

    @Override
    public void update(HttpServletRequest request,HttpServletResponse response,  HcsaServiceConfigDto hcsaServiceConfigDto) throws Exception{
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        String crud_action_value = request.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
        if("cancel".equals(crud_action_value)){
            sendURL(request,response);
            return;
        }else if("back".equals(crud_action_value)){
            request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, "back");
            return;
        }
         if("version".equals(crud_action_value)){
            request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, "version");
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
                 Collections.sort(selectOptionList,(s1,s2)->(s1.getText().compareTo(s2.getText())));
                 request.setAttribute("selsectBaseHcsaServiceDto",selectOptionList);
                 request.setAttribute("hcsaServiceStepSchemeDto", stringList);
                 request.setAttribute("PremisesType", premisesSet);
                 request.setAttribute("hcsaServiceDto", hcsaServiceDto);
                 request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, "validate");

                 Map<String, List<HcsaConfigPageDto>> tables = getTables(request);
                 request.setAttribute("routingStagess", tables);
                 request.setAttribute(IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                 request.setAttribute(IaisEGPConstant.ERRORMAP, errorMap);
                 return;
             }
             if ("save".equals(crud_action_value)) {
                 HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
                 List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getServiceVersions(hcsaServiceDto.getSvcCode()).getEntity();
                 HcsaServiceDto hcsaServiceDto1 = hcsaServiceDtos.get(hcsaServiceDtos.size() - 1);

                 Integer i = (int) Double.parseDouble(hcsaServiceDto1.getVersion()) + 1;
                 hcsaServiceDto.setVersion(i.toString());
                 String effectiveDate = hcsaServiceDto.getEffectiveDate();
                 Date endDate = hcsaServiceDto.getEndDate();
                 Date parse = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).parse(effectiveDate);
                 if(hcsaServiceDto.isSelectAsNewVersion()){
                     String maxVersionEffectiveDate = hcsaServiceDto.getMaxVersionEffectiveDate();
                     Date parse1 = new SimpleDateFormat(DATE_PARSE).parse(maxVersionEffectiveDate);
                     if(new Date().after(parse1)){
                         Calendar calendar =Calendar.getInstance();
                         calendar.setTime(new Date());
                         calendar.add(Calendar.SECOND,1);
                         String format = new SimpleDateFormat(DATE_PARSE).format(calendar.getTime());
                         hcsaServiceDto.setEffectiveDate(format);
                         if(endDate!=null){
                             hcsaServiceDto.setEndDate(endDate);
                         }
                     }else {
                         Calendar calendar=Calendar.getInstance();
                         calendar.setTime(parse);
                         calendar.add(Calendar.SECOND,1);
                         String format = new SimpleDateFormat(DATE_PARSE).format(calendar.getTime());
                         hcsaServiceDto.setEffectiveDate(format);
                     }
                 }else {
                     String format = new SimpleDateFormat(DATE_PARSE).format(parse);
                     hcsaServiceDto.setEffectiveDate(format);
                 }
                 hcsaServiceDto.setId(null);

                 transFor(hcsaServiceConfigDto);
                 hcsaServiceConfigDto= hcsaConfigClient.saveHcsaServiceConfig(hcsaServiceConfigDto).getEntity();
                 eicGateway(hcsaServiceConfigDto);
                 request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, "save");
                 //todo send email update (if start date or end date change need send  Effective Start/End )
                 HcsaServiceCacheHelper.flushServiceMapping();
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

    private void transFor( HcsaServiceConfigDto hcsaServiceConfigDto){
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = hcsaServiceConfigDto.getHcsaSvcDocConfigDtos();
        if(hcsaSvcDocConfigDtos!=null){
            hcsaSvcDocConfigDtos.forEach(v->{
                v.transFor();
            });
        }
    }
    @Override
    public void saData(HttpServletRequest request) {


    }

    @Override
    public void delete(HttpServletRequest request) {
        String crud_action_type = request.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crud_action_value = ParamUtil.getMaskedString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if ("delete".equals(crud_action_type)) {
            if (!StringUtil.isEmpty(crud_action_value)) {
                view(request, crud_action_value);
            }

        }

    }

    @Override
    public void deleteOrCancel(HttpServletRequest request,HttpServletResponse response) {
        String crud_action_value = request.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
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
            String sc_err005 = MessageUtil.getMessageDesc("SC_ERR005");
            if(!entity.isEmpty()){
                request.setAttribute("delete","fail");
                request.setAttribute("deleteFile",sc_err005);
                return;
            }
            if(flag){
                request.setAttribute("delete","fail");
                request.setAttribute("deleteFile",sc_err005);
                return;

            }
            //todo delete send email
            request.setAttribute("option","deleted");
            request.setAttribute("serviceName",hcsaServiceDto.getSvcName());
            try {
             /*   sendEmail(request);*/
            } catch (Exception e) {
              log.error(e.getMessage(),e);
            }
            hcsaConfigClient.updateService(crud_action_value);
            HcsaServiceConfigDto hcsaServiceConfigDto=new HcsaServiceConfigDto();
            hcsaServiceDto.setUseDelete(true);
            hcsaServiceConfigDto.setHcsaServiceDto(hcsaServiceDto);
            eicGateway(hcsaServiceConfigDto);
            request.setAttribute("delete","success");
            request.setAttribute("deleteFile","delete success");
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
            int j=0;
            for(int i=0;i< hcsaSvcSubtypeOrSubsumedDtos.size();i++ ){
                j++;
                List<HcsaSvcSubtypeOrSubsumedDto> list = hcsaSvcSubtypeOrSubsumedDtos.get(i).getList();
                String name = hcsaSvcSubtypeOrSubsumedDtos.get(i).getName();
                if(!subtypeName.contains(name)){
                    subtypeName.add(name);
                }else {
                    errorMap.put("hcsaSvcSubtypeOrSubsumed"+j,"SC_ERR011");
                }
                if(list!=null){
                    for(HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto1:list){
                        j++;
                        String name1 = hcsaSvcSubtypeOrSubsumedDto1.getName();
                        List<HcsaSvcSubtypeOrSubsumedDto> list1 = hcsaSvcSubtypeOrSubsumedDto1.getList();
                        if(!subtypeName.contains(name1)){
                            subtypeName.add(name1);
                        }else {
                            errorMap.put("hcsaSvcSubtypeOrSubsumed"+j,"SC_ERR011");
                        }
                        if(list1!=null){
                            for(HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto2:list1){
                                j++;
                                String name2 = hcsaSvcSubtypeOrSubsumedDto2.getName();
                                if(!subtypeName.contains(name2)){
                                    subtypeName.add(name2);
                                }else {
                                    errorMap.put("hcsaSvcSubtypeOrSubsumed"+j,"SC_ERR011");
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
        boolean selectAsNewVersion = hcsaServiceDto.isSelectAsNewVersion();
        Date maxVersionEndDate = hcsaServiceDto.getMaxVersionEndDate();
        String maxVersionEffectiveDate = hcsaServiceDto.getMaxVersionEffectiveDate();
        if (StringUtil.isEmpty(effectiveDate)) {
            errorMap.put("effectiveDate", MessageUtil.replaceMessage("GENERAL_ERR0006","Effective Start Date","field"));
        }else if(!selectAsNewVersion){
            Date parse = new SimpleDateFormat("dd/MM/yyyy").parse(effectiveDate);
            if(parse.before(new Date()) || parse.compareTo(new Date())==0){
                errorMap.put("effectiveDate","RSM_ERR012");
            }else {
               if(maxVersionEndDate!=null){
                 if(parse.before(maxVersionEndDate) || parse.compareTo(maxVersionEndDate)==0){
                     errorMap.put("effectiveDate","SC_ERR009");
                 }
               }else {
                   if(maxVersionEffectiveDate!=null){
                       Date parse1 = new SimpleDateFormat(DATE_PARSE).parse(maxVersionEffectiveDate);
                       if(parse.before(parse1) || parse.compareTo(parse1)==0){
                           errorMap.put("effectiveDate","SC_ERR010");
                       }
                   }

               }
            }
        }else if(selectAsNewVersion){
            if(!StringUtil.isEmpty(endDate)){
                if(endDate.before(new Date()) || endDate.compareTo(new Date())==0){
                    errorMap.put("effectiveEndDate","RSM_ERR012");
                }
            }
        }
        if(!StringUtil.isEmpty(endDate)){
            Date parse = new SimpleDateFormat("dd/MM/yyyy").parse(effectiveDate);
            if(endDate.before(parse) || parse.compareTo(endDate)==0){
                errorMap.put("effectiveEndDate", "EMM_ERR004");
            }
        }
        String service_category_error = MessageUtil.replaceMessage("GENERAL_ERR0006", "Service Category", "field");

        if (StringUtil.isEmpty(svcCode)) {
            errorMap.put("svcCode", MessageUtil.replaceMessage("GENERAL_ERR0006","Service Code","field"));
        }else if(svcCode.length()>3){
            errorMap.put("svcCode",ILLEGAL_OPERATION);
        }
        if (StringUtil.isEmpty(svcName)) {
            errorMap.put("svcName", MessageUtil.replaceMessage("GENERAL_ERR0006","Service Name","field"));
        }else  if(svcName.length()>100){
            errorMap.put("svcName",ILLEGAL_OPERATION);
        }
        if (StringUtil.isEmpty(svcDisplayDesc)) {
            errorMap.put("svcDisplayDesc", MessageUtil.replaceMessage("GENERAL_ERR0006","Service Display Description","field"));
        }else if(svcDisplayDesc.length()>255){
            errorMap.put("svcDisplayDesc",ILLEGAL_OPERATION);
        }
        if (StringUtil.isEmpty(svcDesc)) {
            errorMap.put("svcDesc", MessageUtil.replaceMessage("GENERAL_ERR0006","Service Description","field"));
        }else if(svcDesc.length()>255){
            errorMap.put("svcDesc",ILLEGAL_OPERATION);
        }
        if (StringUtil.isEmpty(svcType)) {
            errorMap.put("svcType", MessageUtil.replaceMessage("GENERAL_ERR0006","Service Type","field"));
        }else if(ApplicationConsts.SERVICE_TYPE_SUBSUMED.equals(svcType)){
            List<HcsaServiceSubTypeDto> serviceSubTypeDtos = hcsaServiceDto.getServiceSubTypeDtos();
            if(serviceSubTypeDtos.isEmpty()){
                errorMap.put("Subsumption",MessageUtil.replaceMessage("GENERAL_ERR0006","Base Service Subsumed Under","field"));
            }
        }else if(ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(svcType)){
            List<HcsaServiceSubTypeDto> serviceSubTypeDtos = hcsaServiceDto.getServiceSubTypeDtos();
            if(serviceSubTypeDtos.isEmpty()){
                errorMap.put("Prerequisite",MessageUtil.replaceMessage("GENERAL_ERR0006","Pre-requisite Base Service","field"));
            }
            String categoryId = hcsaServiceDto.getCategoryId();
            if(StringUtil.isEmpty(categoryId)){
                errorMap.put("serviceCategory",service_category_error);
            }
        }else if(ApplicationConsts.SERVICE_TYPE_BASE.equals(svcType)){
            String categoryId = hcsaServiceDto.getCategoryId();
            if(StringUtil.isEmpty(categoryId)){
                errorMap.put("serviceCategory",service_category_error);
            }
        }
        List<HcsaSvcSpePremisesTypeDto> hcsaSvcSpePremisesTypeDtos = hcsaServiceConfigDto.getHcsaSvcSpePremisesTypeDtos();
        if (hcsaSvcSpePremisesTypeDtos.isEmpty()) {
            errorMap.put("premieseType", MessageUtil.replaceMessage("GENERAL_ERR0006","Premises Type","field"));
        }
        String businessName = request.getParameter("business-name");
        if(StringUtil.isEmpty(businessName)){
            errorMap.put("businessName",MessageUtil.replaceMessage("GENERAL_ERR0006","Business Name","field"));
        }
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaServiceConfigDto.getHcsaSvcPersonnelDtos();

        for (int i = 0; i < hcsaSvcPersonnelDtos.size(); i++) {
            String psnType = hcsaSvcPersonnelDtos.get(i).getPsnType();
            int mandatoryCount = hcsaSvcPersonnelDtos.get(i).getMandatoryCount();
            int maximumCount = hcsaSvcPersonnelDtos.get(i).getMaximumCount();
            String pageMandatoryCount = hcsaSvcPersonnelDtos.get(i).getPageMandatoryCount();
            String pageMaximumCount = hcsaSvcPersonnelDtos.get(i).getPageMaximumCount();
            boolean pageManFlag=false;
            boolean pageMaxFlag=false;
            if (StringUtil.isEmpty(psnType)) {
                errorMap.put("psnType" + i, "CHKLMD001_ERR001");
            }
            String generalErr0002 = MessageUtil.getMessageDesc("GENERAL_ERR0002");
            if (StringUtil.isEmpty(pageMandatoryCount)) {
                errorMap.put("mandatoryCount" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","Minimum Count","field"));
            }  else  {
                if(pageMandatoryCount.matches("^[0-9]+$")){
                    pageManFlag=true;
                    int i1 = Integer.parseInt(pageMandatoryCount);
                    if (i1<0){
                        errorMap.put("mandatoryCount"+i, generalErr0002);
                    }
                }else {
                    errorMap.put("mandatoryCount"+i,generalErr0002);
                }
            }
            if (StringUtil.isEmpty(pageMaximumCount)) {
                errorMap.put("maximumCount" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","Maximum Count","field"));
            }else {
                if(pageMaximumCount.matches("^[0-9]+$")){
                    pageMaxFlag=true;
                    int i1 = Integer.parseInt(pageMaximumCount);
                    if(i1<0){
                        errorMap.put("maximumCount"+i,generalErr0002);
                    }
                }else {
                    errorMap.put("maximumCount"+i,generalErr0002);
                }
            }
            if(pageManFlag&&pageMaxFlag){
                if(mandatoryCount>maximumCount){
                    errorMap.put("maximumCount"+i,"SC_ERR006");
                }
            }
        }
        Map<String, List<HcsaSvcSpeRoutingSchemeDto>> hcsaSvcSpeRoutingSchemeDtoMap =hcsaServiceConfigDto.getHcsaSvcSpeRoutingSchemeDtoMap();
        String GENERAL_ERR0006 = MessageUtil.replaceMessage("GENERAL_ERR0006", "Service Routing Scheme", "field");
        hcsaSvcSpeRoutingSchemeDtoMap.forEach((k,v)->{
            for(int i=0;i<v.size();i++){
                String isMandatory = v.get(i).getIsMandatory();
                String schemeType = v.get(i).getSchemeType();
                String stageId = v.get(i).getStageId();
                if(HcsaConsts.ROUTING_STAGE_ASO.equals(stageId)||HcsaConsts.ROUTING_STAGE_AO3.equals(stageId)){
                    if(ApplicationConsts.SERVICE_TYPE_BASE.equals(svcType)||ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(svcType)){
                        if(!ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(k)&&
                                !ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(k)&&
                                !ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(k)&&
                                !ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(k)){
                            if (StringUtil.isEmpty(schemeType)) {
                                errorMap.put("schemeType"+k+ i,GENERAL_ERR0006);
                                errorMap.put(k,"error");
                            }
                            continue;
                        }
                    }
                }

                if("false".equals(isMandatory)){
                    continue;
                }
                if (StringUtil.isEmpty(schemeType)) {
                    errorMap.put("schemeType"+k+ i, GENERAL_ERR0006);
                    errorMap.put(k,"error");
                }
            }
        });
        Map<String,List<HcsaSvcSpecificStageWorkloadDto>>map=hcsaServiceConfigDto.getHcsaSvcSpecificStageWorkloadDtoMap();
        String general_err0041 = MessageUtil.getMessageDesc("GENERAL_ERR0041");
        String service_routing_scheme = general_err0041.replace("{field}", "Service Routing Scheme max length");
        String replace = service_routing_scheme.replace("{maxlength}", "2");
        String message = MessageUtil.replaceMessage("GENERAL_ERR0006", "Service Routing Scheme", "field");
        String message_this = MessageUtil.replaceMessage("GENERAL_ERR0006", "This", "field");
        String messageWorload = MessageUtil.replaceMessage("GENERAL_ERR0006", "Service Workload Manhours", "field");
        map.forEach((k,v)->{
            for(int i=0;i<v.size();i++){
                String isMandatory = v.get(i).getIsMandatory();
                String stringManhourCount = v.get(i).getStringManhourCount();
                String stageId = v.get(i).getStageId();
                if(HcsaConsts.ROUTING_STAGE_ASO.equals(stageId)||HcsaConsts.ROUTING_STAGE_AO3.equals(stageId)){
                    if(ApplicationConsts.SERVICE_TYPE_BASE.equals(svcType)||ApplicationConsts.SERVICE_TYPE_SPECIFIED.equals(svcType)){
                        if(!ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(k)&&
                                !ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(k)&&
                        !ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(k)&&
                                !ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(k)){
                            if (StringUtil.isEmpty(stringManhourCount)) {
                                errorMap.put("manhourCount"+k+ i, message);
                                errorMap.put(k,"error");
                            }else if(stringManhourCount.length()>2){
                                errorMap.put("manhourCount"+k+i,replace);
                                errorMap.put(k,"error");
                            }
                            if(StringUtil.isEmpty(isMandatory)){
                                errorMap.put("isMandatory"+k+i,message_this);
                                errorMap.put(k,"error");
                            }else if("false".equals(isMandatory)){
                                errorMap.put("isMandatory"+k+i,"This option must be mandatory");
                                errorMap.put(k,"error");
                            }
                            continue;
                        }
                    }

                }
                if("false".equals(isMandatory)){
                    continue;
                }else if("".equals(isMandatory)){
                    errorMap.put("isMandatory"+k+i,message_this);
                    errorMap.put(k,"error");
                }
                if(StringUtil.isEmpty(stringManhourCount)){
                    errorMap.put("manhourCount"+k+i,messageWorload);
                    errorMap.put(k,"error");
                }else if(stringManhourCount.length()>2){
                    errorMap.put("manhourCount"+k+i,replace);
                    errorMap.put(k,"error");
                }else if(!stringManhourCount.matches("^[0-9]+$")){
                    errorMap.put("manhourCount"+k+i,"GENERAL_ERR0002");
                    errorMap.put(k,"error");
                }
            }
        });
        String numberDocument = hcsaServiceConfigDto.getComDocSize();
        if(StringUtil.isEmpty(numberDocument)){
            errorMap.put("NumberDocument",MessageUtil.replaceMessage("GENERAL_ERR0006","Number of Service-Related Document to be uploaded","field"));
        }else if(numberDocument.length()>2){
            errorMap.put("NumberDocument",ILLEGAL_OPERATION);
        }else if(!numberDocument.matches("^[0-9]+$")){
            errorMap.put("NumberDocument","GENERAL_ERR0002");
        }
        String numberfields = hcsaServiceConfigDto.getServiceDocSize();
        if(StringUtil.isEmpty(numberfields)){
            errorMap.put("Numberfields",MessageUtil.replaceMessage("GENERAL_ERR0006","Number of Service-Related General Info fields to be captured","field"));
        }else if(numberfields.length()>2){
            errorMap.put("Numberfields",ILLEGAL_OPERATION);
        }else if(!numberfields.matches("^[0-9]+$")){
            errorMap.put("Numberfields","GENERAL_ERR0002");
        }
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = (List<HcsaSvcDocConfigDto>)request.getAttribute("serviceDoc");
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtoList = ( List<HcsaSvcDocConfigDto>)request.getAttribute("comDoc");
        String Name_of_Info_Field = MessageUtil.replaceMessage("GENERAL_ERR0006", "Name of Info Field", "field");
        if(hcsaSvcDocConfigDtos!=null){
            StringBuilder stringBuilder=new StringBuilder();
            for(int i = 0; i < hcsaSvcDocConfigDtos.size(); i++){
                String docTitle = hcsaSvcDocConfigDtos.get(i).getDocTitle();
                if(StringUtil.isEmpty(docTitle)){
                    errorMap.put("serviceDoc"+i,Name_of_Info_Field);
                }else if(docTitle.length()>255){
                    errorMap.put("serviceDoc",ILLEGAL_OPERATION);
                }else {
                    if(stringBuilder.toString().contains(docTitle)){
                        errorMap.put("serviceDoc"+i,"SC_ERR011");
                    }else {
                        stringBuilder.append(docTitle);
                    }
                }
            }
        }
        if(hcsaSvcDocConfigDtoList!=null){
            StringBuilder stringBuilder=new StringBuilder();
            for(int i = 0;i<hcsaSvcDocConfigDtoList.size();i++){
                String docTitle = hcsaSvcDocConfigDtoList.get(i).getDocTitle();
                if(StringUtil.isEmpty(docTitle)){
                    errorMap.put("commonDoc"+i,Name_of_Info_Field);
                }else if(docTitle.length()>255){
                    errorMap.put("commonDoc",ILLEGAL_OPERATION);
                }else {
                    if(stringBuilder.toString().contains(docTitle)){

                    }else {
                        stringBuilder.append(docTitle);
                    }
                }
            }
        }
        Map<String,List<HcsaSvcSpeRoutingSchemeDto>> newHashMap=(Map<String,List<HcsaSvcSpeRoutingSchemeDto>>) request.getAttribute("insRoutingStage");
        if(newHashMap!=null){
            newHashMap.forEach((k,v)->{
                if(v!=null){
                    for(int i=0;i<v.size();i++){
                        String isMandatory = v.get(i).getIsMandatory();
                        if("false".equals(isMandatory)){
                            continue;
                        }
                        if(StringUtil.isEmpty(v.get(i).getSchemeType())){
                            errorMap.put("insRoutingStage"+k+i,MessageUtil.replaceMessage("GENERAL_ERR0006","This","field"));
                            errorMap.put(k,"error");
                        }
                    }
                }
            });
        }
        WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
    }

    private   Map<String, List<HcsaConfigPageDto>> getHcsaConfigPageDtos(HcsaServiceDto hcsaServiceDto) {

        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos = getHcsaSvcRoutingStageDtos();

        List<HcsaSvcStageWorkloadDto> hcsaSvcStageWorkloadDtos =
                hcsaConfigClient.getHcsaSvcSpeRoutingSchemeByServiceId(hcsaServiceDto.getId()).getEntity();
        List<String> stageIds = IaisCommonUtils.genNewArrayList();
        stageIds.add(HcsaConsts.ROUTING_STAGE_ASO);
        stageIds.add(HcsaConsts.ROUTING_STAGE_PSO);
        stageIds.add(HcsaConsts.ROUTING_STAGE_INS);
        stageIds.add(HcsaConsts.ROUTING_STAGE_AO1);
        stageIds.add(HcsaConsts.ROUTING_STAGE_AO2);
        stageIds.add(HcsaConsts.ROUTING_STAGE_AO3);

        List<WorkingGroupDto> hcsa = organizationClient.getWorkingGroup("hcsa").getEntity();
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = hcsaConfigClient.getHcsaStageWorkingGroup(hcsaServiceDto.getId()).getEntity();

        List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos = hcsaConfigClient.getHcsaSvcSpeRoutingSchemeDtoByServiceId(hcsaServiceDto.getId()).getEntity();
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList = hcsaConfigClient.getHcsaSvcRoutingStageDtoByServiceId(hcsaServiceDto.getId()).getEntity();

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
                    if(stageWrkGrpID.equals(hcsaSvcStageWorkingGroupDto.getId())){
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
                    processingData(type,hcsaSvcRoutingStageDtos, hcsaSvcStageWorkloadDtos1, hcsaSvcStageWorkingGroupDtos1, hcsa);
            List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos1 = hcsaSvcSpeRoutingSchemeDtoMap.get(type);
            for(HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto:hcsaSvcSpeRoutingSchemeDtos1){
                String stageWrkGrpID = hcsaSvcSpeRoutingSchemeDto.getStageWrkGrpID();
                for(HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto:hcsaSvcStageWorkingGroupDtos1){
                    String schemeType = hcsaSvcSpeRoutingSchemeDto.getSchemeType();
                        for(HcsaConfigPageDto hcsaConfigPageDto:hcsaConfigPageDtos){
                            String workingGroupId = hcsaConfigPageDto.getWorkingGroupId();
                            List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos2 = hcsaConfigPageDto.getHcsaSvcSpeRoutingSchemeDtos();
                            if(hcsaSvcStageWorkingGroupDto.getStageWorkGroupId().equals(workingGroupId)&&stageWrkGrpID.equals(hcsaSvcStageWorkingGroupDto.getId())&&hcsaSvcStageWorkingGroupDto.getOrder()==1){
                                hcsaConfigPageDto.setRoutingSchemeName(schemeType);
                            }
                            if(hcsaSvcSpeRoutingSchemeDtos2!=null){
                                for(HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto1 : hcsaSvcSpeRoutingSchemeDtos2){
                                    if(stageWrkGrpID.equals(hcsaSvcStageWorkingGroupDto.getId()) && String.valueOf(hcsaSvcStageWorkingGroupDto.getOrder()-2).equals(hcsaSvcSpeRoutingSchemeDto1.getInsOder())){
                                        hcsaSvcSpeRoutingSchemeDto1.setSchemeType(schemeType);
                                    }
                                }
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
                for(HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtoList){
                    String appType = hcsaSvcRoutingStageDto.getAppType();
                    if(type.equals(appType)&& hcsaConfigPageDto.getStageId().equals(hcsaSvcRoutingStageDto.getStageId())){
                        hcsaConfigPageDto.setCanApprove(hcsaSvcRoutingStageDto.getCanApprove());
                    }
                }
            }

            hcsaConfigPageDtoMap.put(type,hcsaConfigPageDtos);
        }




        return hcsaConfigPageDtoMap;
    }

    private  List<HcsaConfigPageDto> processingData(String type,List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtos,List<HcsaSvcStageWorkloadDto> hcsaSvcStageWorkloadDtos,List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos,  List<WorkingGroupDto> hcsa  ){
        List<HcsaConfigPageDto> hcsaConfigPageDtos = IaisCommonUtils.genNewArrayList();
        for (HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto : hcsaSvcRoutingStageDtos) {
            HcsaConfigPageDto hcsaConfigPageDto = new HcsaConfigPageDto();
            hcsaConfigPageDto.setStageCode(hcsaSvcRoutingStageDto.getStageCode());
            hcsaConfigPageDto.setStage(hcsaSvcRoutingStageDto.getId());
            hcsaConfigPageDto.setStageId(hcsaSvcRoutingStageDto.getId());
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
    public synchronized List<HcsaSvcRoutingStageDto> getHcsaSvcRoutingStageDtos() {
        if (hcsaSvcRoutingStageDtos == null) {
            List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDto = hcsaConfigClient.stagelist().getEntity();
            for (int i = 0; i < hcsaSvcRoutingStageDto.size(); i++) {
                String stageOrder = hcsaSvcRoutingStageDto.get(i).getStageOrder();
                try {
                    if (Integer.parseInt(stageOrder) % 100 != 0) {
                        hcsaSvcRoutingStageDto.remove(i);
                        i--;
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
            hcsaSvcRoutingStageDtos = hcsaSvcRoutingStageDto;
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
                hcsaConfigPageDtos.get(i).setHcsaSvcSpeRoutingSchemeDtos(hcsaConfigPageDtos1.get(i).getHcsaSvcSpeRoutingSchemeDtos());
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
        return hcsaServiceCatgoryDtos;
    }

    @Override
    public Map<String,String> getMaskHcsaServiceCategory(){
        List<HcsaServiceCategoryDto> hcsaServiceCategoryDtos = getHcsaServiceCategoryDto();
        Map<String,String> hashMap=IaisCommonUtils.genNewHashMap();
        for(HcsaServiceCategoryDto hcsaServiceCategoryDto : hcsaServiceCategoryDtos){
            hashMap.put(hcsaServiceCategoryDto.getDesc(),hcsaServiceCategoryDto.getId());
        }
        return hashMap;
    }

    @Override
    public HcsaSvcPersonnelDto getHcsaSvcPersonnelDto(String man, String mix, String psnType) {
        HcsaSvcPersonnelDto personnelDto=new HcsaSvcPersonnelDto();
        personnelDto.setPageMandatoryCount(man);
        personnelDto.setPageMaximumCount(mix);
        try {
            if(!StringUtil.isEmpty(man)){
                personnelDto.setMandatoryCount(Integer.parseInt(man));
            }
        }catch (NumberFormatException e){}
        try {
            if(!StringUtil.isEmpty(mix)){
                personnelDto.setMaximumCount(Integer.parseInt(mix));
            }
        }catch (NumberFormatException e){}
        personnelDto.setPsnType(psnType);
        personnelDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        return personnelDto;
    }

    static String[] codeSvc ={ApplicationConsts.SERVICE_TYPE_BASE,ApplicationConsts.SERVICE_TYPE_SUBSUMED,ApplicationConsts.SERVICE_TYPE_SPECIFIED};


    private void view(HttpServletRequest request, String crud_action_value) {
        HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(crud_action_value).getEntity();
        List<HcsaServiceCategoryDto> categoryDtos = getHcsaServiceCategoryDto();
        categoryDtos.sort((s1, s2) -> (s1.getName().compareTo(s2.getName())));
        request.getSession().setAttribute("categoryDtos",categoryDtos);
        List<SelectOption> selectOptionList = MasterCodeUtil.retrieveOptionsByCodes(codeSvc);
        request.getSession().setAttribute("codeSelectOptionList",selectOptionList);
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
            if (groupName.contains("Inspection") && stageCode.contains("INS")) {
                workingGroupDtoList.add(workingGroupDto);
                List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos=new ArrayList<>(2);
                for(int i=0;i<2;i++){
                    HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto=new HcsaSvcSpeRoutingSchemeDto();
                    hcsaSvcSpeRoutingSchemeDto.setInsOder(String.valueOf(i));
                    hcsaSvcSpeRoutingSchemeDtos.add(hcsaSvcSpeRoutingSchemeDto);
                }
                hcsaConfigPageDto.setHcsaSvcSpeRoutingSchemeDtos(hcsaSvcSpeRoutingSchemeDtos);
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
                if (groupName.contains("Inspection") && stageCode.contains("INS")) {
                    workingGroupDtoList.add(workingGroupDto);
                    List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos=new ArrayList<>(2);
                    for(int i=0;i<2;i++){
                        HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto=new HcsaSvcSpeRoutingSchemeDto();
                        hcsaSvcSpeRoutingSchemeDto.setInsOder(String.valueOf(i));
                        hcsaSvcSpeRoutingSchemeDtos.add(hcsaSvcSpeRoutingSchemeDto);
                    }
                    hcsaConfigPageDto.setHcsaSvcSpeRoutingSchemeDtos(hcsaSvcSpeRoutingSchemeDtos);
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
       /* list.add(ApplicationConsts.APPLICATION_TYPE_SUSPENSION) ;*/
        list.add(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        list.add(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        //post audit insApplicationConsts
        list.add(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK);
        list.add(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION);
        return list;
    }

    @Override
    public List<HcsaSvcCateWrkgrpCorrelationDto> getHcsaSvcCateWrkgrpCorrelationDtoBySvcCateId(String svcCateId) {
        if(StringUtil.isEmpty(svcCateId)){
            return new ArrayList<>();
        }
        List<HcsaSvcCateWrkgrpCorrelationDto> entity =
                hcsaConfigClient.getHcsaSvcCateWrkgrpCorrelationDtoBySvcCateId(svcCateId).getEntity();
        return entity;
    }

    private void setValueOfhcsaConfigPageDtos( List<HcsaConfigPageDto> hcsaConfigPageDtos1  ,List<HcsaConfigPageDto> hcsaConfigPageDtos){
        if (hcsaConfigPageDtos1 != null) {
            for (int i = 0; i < hcsaConfigPageDtos.size(); i++) {
                String manhours = hcsaConfigPageDtos1.get(i).getManhours();
                String stage = hcsaConfigPageDtos1.get(i).getStage();
                String workingGroupId = hcsaConfigPageDtos1.get(i).getWorkingGroupId();
                String routingSchemeName = hcsaConfigPageDtos1.get(i).getRoutingSchemeName();
                String isMandatory = hcsaConfigPageDtos1.get(i).getIsMandatory();
                String canApprove = hcsaConfigPageDtos1.get(i).getCanApprove();
                List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos = hcsaConfigPageDtos1.get(i).getHcsaSvcSpeRoutingSchemeDtos();
                hcsaConfigPageDtos.get(i).setHcsaSvcSpeRoutingSchemeDtos(hcsaSvcSpeRoutingSchemeDtos);
                hcsaConfigPageDtos.get(i).setManhours(manhours);
                hcsaConfigPageDtos.get(i).setStage(stage);
                hcsaConfigPageDtos.get(i).setWorkingGroupId(workingGroupId);
                hcsaConfigPageDtos.get(i).setRoutingSchemeName(routingSchemeName);
                hcsaConfigPageDtos.get(i).setIsMandatory(isMandatory);
                hcsaConfigPageDtos.get(i).setCanApprove(canApprove);
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
                appeal= getWorkGrop(type,APPEAL);
            }else if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(type)){
                appeal=  getWorkGrop(type,NEW_APPLICATION);
            }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(type)){
               appeal = getWorkGrop(type, REQUEST_FOR_CHANGE);
            }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(type)){
                appeal=  getWorkGrop(type,RENEW);
            }else if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(type)){
                appeal= getWorkGrop(type,CESSATION);
            }else  if(ApplicationConsts.APPLICATION_TYPE_SUSPENSION.equals(type)){
                appeal= getWorkGrop(type,SUSPENSION);
            }else if(ApplicationConsts.APPLICATION_TYPE_REINSTATEMENT.equals(type)){
                appeal= getWorkGrop(type,"Revocation");
            }else if(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(type)){
                appeal= getWorkGrop(type,WITHDRAWAL);
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
        boolean serviceIsUsed = hcsaServiceDto.isServiceIsUsed();
        Date parse;
        try {
            parse = new SimpleDateFormat(DATE_FORMAT).parse(effectiveDate);
            String format = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).format(parse);
            hcsaServiceDto.setEffectiveDate(format);
            hcsaServiceDto.setOldEffectiveDate(format);
            hcsaServiceDto.setOldEndDate(hcsaServiceDto.getEndDate());
        } catch (ParseException e) {
          log.error(e.getMessage(),e);
        }
        Date maxVersionEndDate = hcsaServiceDto.getMaxVersionEndDate();
        String maxVersionEffectiveDate = hcsaServiceDto.getMaxVersionEffectiveDate();
        if(serviceIsUsed){
            try {
                Date parse1 = new SimpleDateFormat(DATE_PARSE).parse(maxVersionEffectiveDate);
                Date parse2 = new SimpleDateFormat(DATE_PARSE).parse(effectiveDate);
                if(parse1.compareTo(parse2)==0 ){
                    if(maxVersionEndDate==null){
                        hcsaServiceDto.setSelectAsNewVersion(true);
                        String version = (String)request.getAttribute("version");
                        if("version".equals(version)){
                            hcsaServiceDto.setSelectAsNewVersion(false);
                        }
                    }else if(maxVersionEndDate!=null){
                        hcsaServiceDto.setSelectAsNewVersion(true);
                    }
                }else if(maxVersionEndDate==null){
                    hcsaServiceDto.setSelectAsNewVersion(false);
                }else if(maxVersionEndDate!=null){
                    Calendar calendar =Calendar.getInstance();
                    calendar.setTime(maxVersionEndDate);
                    calendar.add(Calendar.DAY_OF_MONTH,1);
                    String format = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).format(calendar.getTime());
                    hcsaServiceDto.setEffectiveDate(format);
                    hcsaServiceDto.setSelectAsNewVersion(true);
                }
            } catch (ParseException e) {
                log.error(e.getMessage(),e);
            }

        }else {
            hcsaServiceDto.setSelectAsNewVersion(false);
        }


        List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getServiceVersions(hcsaServiceDto.getSvcCode()).getEntity();
        request.getSession().setAttribute("hcsaServiceDtosVersion",hcsaServiceDtos);
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
        Collections.sort(selectOptionList,(s1,s2)->(s1.getText().compareTo(s2.getText())));
        request.setAttribute("selsectBaseHcsaServiceDto",selectOptionList);
        request.setAttribute("hcsaServiceDto", hcsaServiceDto);
        String id = hcsaServiceDto.getId();
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = hcsaConfigClient.getHcsaSvcDocConfigDto(id).getEntity();

        if(hcsaSvcDocConfigDtos!=null){
            request.setAttribute("serviceDocSize",hcsaSvcDocConfigDtos.size());
            hcsaSvcDocConfigDtos.forEach(v ->{
                v.transFor();
            });
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
        Set<String> premisesType = hcsaConfigClient.getAppGrpPremisesTypeBySvcId(ids).getEntity();
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = hcsaConfigClient.getHcsaServiceStepSchemeDtoByServiceId(hcsaServiceDto.getId()).getEntity();
        List<String> stringList = IaisCommonUtils.genNewArrayList();
        //business Name initialization
        request.setAttribute("businessName",String.valueOf(0));
        for (HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto : hcsaServiceStepSchemeDtos) {
            String stepCode = hcsaServiceStepSchemeDto.getStepCode();
            if(HcsaConsts.STEP_LABORATORY_DISCIPLINES.equals(stepCode)){
                request.setAttribute("pageName",hcsaServiceStepSchemeDto.getStepName());
            }
            if(HcsaConsts.STEP_BUSINESS_NAME.equals(stepCode)){
                request.setAttribute("businessName",String.valueOf(1));
            }
            stringList.add(stepCode);
        }
        request.setAttribute("PremisesType", premisesType);
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
                .append("/main-web/eservice/INTRANET/MohHcsaBeDashboard");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(),request);
        try {
            IaisEGPHelper.redirectUrl(response, tokenUrl);
            request.getSession().removeAttribute("orgUserDto");
        } catch (IOException e) {
          log.error(e.getMessage(),e);
        }
    }

    private void sendHcsaConfigPageDtoTypeName(HcsaConfigPageDto hcsaConfigPageDto,String type){
        hcsaConfigPageDto.setAppType(type);
        if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(type)){
            hcsaConfigPageDto.setAppTypeName(NEW_APPLICATION);
        }else if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(type)){
            hcsaConfigPageDto.setAppTypeName(APPEAL);
        } else if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(type)) {
            hcsaConfigPageDto.setAppTypeName(REQUEST_FOR_CHANGE);
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(type)){
            hcsaConfigPageDto.setAppTypeName(RENEW);
        }else if(ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(type)){
            hcsaConfigPageDto.setAppTypeName(CESSATION);
        }else  if(ApplicationConsts.APPLICATION_TYPE_SUSPENSION.equals(type)){
            hcsaConfigPageDto.setAppTypeName(SUSPENSION);
        }else if(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(type)){
            hcsaConfigPageDto.setAppTypeName(WITHDRAWAL);
        }
    }


    private void eic(HcsaServiceConfigDto hcsaServiceConfigDto){
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        gatewayClient.saveFeServiceConfig(hcsaServiceConfigDto,signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }

    private void eicGateway(HcsaServiceConfigDto hcsaServiceConfigDto){
            EicRequestTrackingDto postSaveTrack = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, ConfigServiceImpl.class.getName(),
                    "eic", currentApp + "-" + currentDomain,
                    HcsaServiceConfigDto.class.getName(), JsonUtil.parseToJson(hcsaServiceConfigDto));
            AuditTrailDto intenet = AuditTrailHelper.getCurrentAuditTrailDto();
            FeignResponseEntity<EicRequestTrackingDto> fetchResult = eicRequestTrackingHelper.getAppEicClient().getPendingRecordByReferenceNumber(postSaveTrack.getRefNo());
            if (fetchResult != null && HttpStatus.SC_OK == fetchResult.getStatusCode()) {
                log.info(StringUtil.changeForLog("------"+JsonUtil.parseToJson(fetchResult)));
                EicRequestTrackingDto entity = fetchResult.getEntity();
                if (AppConsts.EIC_STATUS_PENDING_PROCESSING.equals(entity.getStatus())){
                    eic(hcsaServiceConfigDto);
                    entity.setProcessNum(1);
                    Date now = new Date();
                    entity.setFirstActionAt(now);
                    entity.setLastActionAt(now);
                    entity.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                    entity.setAuditTrailDto(intenet);
                    eicRequestTrackingHelper.getAppEicClient().saveEicTrack(entity);

                }
            } else {
                log.info(StringUtil.changeForLog("------ null----"));
            }


    }
}
