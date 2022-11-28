package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ServiceConfigConstant;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaConfigPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCategoryDisciplineDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCategoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubServiceErrorsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubServicePageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcCateWrkgrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageCompoundDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpeRoutingSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecificStageWorkloadDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.SuppleFormItemConfigDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.AjaxResDto;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.ControllerHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.ConfigService;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author Wenkang
 * @date 2020/2/11 16:19
 */
@Delegator("congfigSeriviceDelegator")
@Log4j
public class ConfigServiceDelegator {
    @Autowired
    private ConfigService configService;
    @Autowired
    private ConfigCommService configCommService;


    @PostMapping(value = "/getDropdownSelect")
    public @ResponseBody
    AjaxResDto getDropdownSelect(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("the getDropdownSelect start ..."));
        AjaxResDto ajaxResDto = new AjaxResDto();
        String premisType = ParamUtil.getString(request, "premisType");
        String specOrOthers = ParamUtil.getString(request, "specOrOthers");
        log.info(StringUtil.changeForLog("the getDropdownSelect premisType is -->:"+premisType));
        log.info(StringUtil.changeForLog("the getDropdownSelect specOrOthers is -->:"+specOrOthers));
        List<SelectOption> selectOptions ;
        if(HcsaConsts.SERVICE_TYPE_SPECIFIED.equals(specOrOthers)){
            selectOptions =  (List<SelectOption>)ParamUtil.getSessionAttr(request,"specHcsaServiceOptions");
        }else{
            selectOptions =  (List<SelectOption>)ParamUtil.getSessionAttr(request,"otherHcsaServiceOptions");
        }
        ajaxResDto.setResCode(AppConsts.AJAX_RES_CODE_SUCCESS);
        Map<String, String> chargesTypeAttr = IaisCommonUtils.genNewHashMap();
        chargesTypeAttr.put("name", premisType+"-"+specOrOthers+"-subServiceCodes");
        String chargeTypeSelHtml = ApplicationHelper.genMutilSelectOpHtml(chargesTypeAttr,
                selectOptions, HcsaAppConst.FIRESTOPTION, null, false,true);
        ajaxResDto.setResultJson(chargeTypeSelHtml);
        log.info(StringUtil.changeForLog("the getDropdownSelect end ..."));
        return ajaxResDto;

    }

    @PostMapping(value = "/getSelectOptionForServiceDocPersonnel")
    public @ResponseBody
    AjaxResDto getSelectOptionForServiceDocPersonnelAjx(HttpServletRequest request) {
        log.info(StringUtil.changeForLog("the getSelectOptionForServiceDocPersonnelAjx start ..."));
        AjaxResDto ajaxResDto = new AjaxResDto();
        String serviceType = ParamUtil.getString(request, "serviceType");
        String serviceCode = ParamUtil.getString(request, "serviceCode");
        //String suppFormSelect = ParamUtil.getString(request, "suppFormSelect");
        log.info(StringUtil.changeForLog("the getSelectOptionForServiceDocPersonnelAjx serviceType is -->:"+serviceType));
        log.info(StringUtil.changeForLog("the getSelectOptionForServiceDocPersonnelAjx serviceCode is -->:"+serviceCode));
        //log.info(StringUtil.changeForLog("the getSelectOptionForServiceDocPersonnelAjx suppFormSelect is -->:"+suppFormSelect));
        List<SelectOption> selectOptions = getSelectOptionForServiceDocPersonnel(serviceType,serviceCode,false);
        ajaxResDto.setResCode(AppConsts.AJAX_RES_CODE_SUCCESS);
        Map<String, String> chargesTypeAttr = IaisCommonUtils.genNewHashMap();
        chargesTypeAttr.put("name", "selectDocPerson");
        String chargeTypeSelHtml = ApplicationHelper.genMutilSelectOpHtml(chargesTypeAttr,
                selectOptions, "To duplicate for the personnel?", null, false,true);
        ajaxResDto.setResultJson(chargeTypeSelHtml);
        log.info(StringUtil.changeForLog("the getSelectOptionForServiceDocPersonnelAjx end ..."));

        return ajaxResDto;

    }


    // 		start->OnStepProcess
    public void start(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("confige start "));
        log.info(StringUtil.changeForLog("confige start end"));
    }
    // 		prepareAddAndListPage->OnStepProcess
    public void prepareAddAndListPage(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("confige prepareAddAndListPage start "));
        removeSession(bpc);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG, AuditTrailConsts.FUNCTION_SERVICE_CONFIGURATOR);
        log.info(StringUtil.changeForLog("confige prepareAddAndListPage end "));
    }

    // 		prepareAddOrList->OnStepProcess
    public void prepareAddOrList(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("confige prepareAddOrList start"));
        log.info(StringUtil.changeForLog("confige prepareAddOrList  end"));
    }

    // 		prepareAddNewService->OnStepProcess
    public void prepareAddNewService(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("confige prepareAddNewService start"));
        bpc.request.getSession().removeAttribute("routingStage");
        preparePage(bpc.request);
        log.info(StringUtil.changeForLog("confige prepareAddNewService  end"));
    }



    // 		doCreate->OnStepProcess
    public void doCreate(BaseProcessClass bpc) throws Exception{
        log.info(StringUtil.changeForLog("confige doCreate start"));
        String crud_action_type =  bpc.request.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setRequestAttr(bpc.request,"crud_action_type_create",crud_action_type);
        log.info(StringUtil.changeForLog("The doCreate crud_action_type is -->:"+crud_action_type));

        if("save".equals(crud_action_type)){
            HcsaServiceConfigDto hcsaServiceConfigDto = getDateOfHcsaService(bpc.request);
            hcsaServiceConfigDto.setCreate(true);
            ValidationResult validationResult = WebValidationHelper.validateProperty(hcsaServiceConfigDto, "save");
            if(validationResult.isHasErrors()){
                Map<String, String> errorMap = validationResult.retrieveAll();
                printErrorMap(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMAP, errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request,"crud_action_type_create","dovalidate");
                ParamUtil.setRequestAttr(bpc.request,"hcsaServiceConfigDto",hcsaServiceConfigDto);
            }else{
                transferHcsaServiceConfigDtoForDB(hcsaServiceConfigDto);
                configService.saveHcsaServiceConfigDto(hcsaServiceConfigDto);
            }
        }

        log.info(StringUtil.changeForLog("confige doCreate end"));
    }

    private  void printErrorMap(Map<String, String> errorMap){
        log.info(StringUtil.changeForLog("The printErrorMap start ..."));
        if(errorMap != null && errorMap.size() >0){
         for(String key :errorMap.keySet()){
             log.info(StringUtil.changeForLog(key + "" + errorMap.get(key)));
         }
        }
        log.info(StringUtil.changeForLog("The printErrorMap end ..."));
    }

    // 		prepareList->OnStepProcess
    public void prepareList(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("confige prepareList start"));
        List<HcsaServiceDto> allHcsaServices = configService.getAllHcsaServices();
        bpc.request.setAttribute("hcsaServiceDtos", allHcsaServices);
        log.info(StringUtil.changeForLog("confige prepareList end"));
    }

    // 		prepareViewOrBack->OnStepProcess
    public void prepareViewOrBack(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("confige prepareViewOrBack start"));
        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.info(StringUtil.changeForLog("The action is -->:"+action));
        if(!"back".equals(action)){
            action = "view";
        }
        ParamUtil.setRequestAttr(bpc.request,"action_type",action);
        log.info(StringUtil.changeForLog("confige prepareViewOrBack  end"));

    }
    // 		prepareView->OnStepProcess
    public void prepareView(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("confige prepareView start"));
        //set data for this hcsaServiceConfigDto
        String serviceId = ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        log.info(StringUtil.changeForLog("The serviceid is -->:"+serviceId));
        HcsaServiceConfigDto hcsaServiceConfigDto = configService.getHcsaServiceConfigDtoByServiceId(serviceId);
        ParamUtil.setRequestAttr(bpc.request,"hcsaServiceConfigDto",hcsaServiceConfigDto);
        preparePage(bpc.request);
        log.info(StringUtil.changeForLog("confige prepareView end"));
    }
    // 		PrepareEditOrDelete->OnStepProcess
    public void prepareEditOrDelete(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("confige prepareEditOrDelete start"));
        String crud_action_type = bpc.request.getParameter("crud_action_type");
        log.info(StringUtil.changeForLog("The crud_action_type is -->:"+crud_action_type));
        log.info(StringUtil.changeForLog("confige prepareEditOrDelete end"));

    }

    // 		PrepeareEdit->OnStepProcess
    public void prepeareEdit(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("confige prepeareEdit start"));
        String serviceId = ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        log.info(StringUtil.changeForLog("The serviceId is -->:"+serviceId));
        HcsaServiceConfigDto hcsaServiceConfigDto = (HcsaServiceConfigDto)ParamUtil.getRequestAttr(bpc.request,"hcsaServiceConfigDto");
        if(hcsaServiceConfigDto == null){
            hcsaServiceConfigDto = configService.getHcsaServiceConfigDtoByServiceId(serviceId);
            ParamUtil.setRequestAttr(bpc.request,"hcsaServiceConfigDto",hcsaServiceConfigDto);
        }
        preparePage(bpc.request);
        log.info(StringUtil.changeForLog("confige prepeareEdit end"));
    }
    // 		doUpdate->OnStepProcess
    public void doUpdate(BaseProcessClass bpc) throws  Exception{
        log.info(StringUtil.changeForLog("confige doUpdate start"));
        String crud_action_type = bpc.request.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        log.info(StringUtil.changeForLog("The crud_action_type is -->:"+crud_action_type));
        if("back".equals(crud_action_type)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_TYPE,crud_action_type);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"edit");
        }else if("version".equals(crud_action_type)){
            String versionServiceId = ParamUtil.getMaskedString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
            log.info(StringUtil.changeForLog("The versionServiceId is -->:"+versionServiceId));
            HcsaServiceConfigDto hcsaServiceConfigDto = configService.getHcsaServiceConfigDtoByServiceId(versionServiceId);
            ParamUtil.setRequestAttr(bpc.request,"hcsaServiceConfigDto",hcsaServiceConfigDto);
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_TYPE,"validate");
        }else{
            HcsaServiceConfigDto hcsaServiceConfigDto = getDateOfHcsaService(bpc.request);
            hcsaServiceConfigDto.setCreate(false);
            List<HcsaServiceDto> hcsaServiceDtos = configService.getServicesBySvcCode(hcsaServiceConfigDto.getHcsaServiceDto().getSvcCode());
            hcsaServiceConfigDto.setHcsaServiceDtosVersion(hcsaServiceDtos);
            ValidationResult validationResult = WebValidationHelper.validateProperty(hcsaServiceConfigDto, "save");
            if(validationResult.isHasErrors()){
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMAP, errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_TYPE,"validate");
                String serviceId = ParamUtil.getMaskedString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
                hcsaServiceConfigDto.getHcsaServiceDto().setId(serviceId);
                ParamUtil.setRequestAttr(bpc.request,"hcsaServiceConfigDto",hcsaServiceConfigDto);
            }else{
                //configService.saveOrUpdate(bpc.request,bpc.response,hcsaServiceConfigDto);
                transferHcsaServiceConfigDtoForDB(hcsaServiceConfigDto);
                configService.saveHcsaServiceConfigDto(hcsaServiceConfigDto);
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_TYPE,"saveSuccess");
                bpc.request.setAttribute("successMSG","Update success");
            }
        }
        log.info(StringUtil.changeForLog("confige doUpdate end"));

    }


    // 		doDelete->OnStepProcess
    public void doDelete(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("confige doDelete start"));
        String serviceId = bpc.request.getParameter("crud_action_value");
        log.info(StringUtil.changeForLog("The serviceId is -->:"+serviceId));
        configService.doDeleteService(serviceId);
        bpc.request.setAttribute("successMSG","Delete success");
       // configService.deleteOrCancel(bpc.request,bpc.response);
        log.info(StringUtil.changeForLog("confige doDelete end"));
    }

    private HcsaServiceConfigDto  preparePage(HttpServletRequest request){
        HcsaServiceConfigDto hcsaServiceConfigDto = (HcsaServiceConfigDto)ParamUtil.getRequestAttr(request,"hcsaServiceConfigDto");
        if(hcsaServiceConfigDto != null){
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = hcsaServiceConfigDto.getHcsaSvcPersonnelDtos();
            if(IaisCommonUtils.isNotEmpty(hcsaSvcPersonnelDtos)){
                for(HcsaSvcPersonnelDto hcsaSvcPersonnelDto : hcsaSvcPersonnelDtos){
                    ParamUtil.setRequestAttr(request,hcsaSvcPersonnelDto.getPsnType(),hcsaSvcPersonnelDto);
                }
            }
        }else{
            hcsaServiceConfigDto = new  HcsaServiceConfigDto();
            HcsaServiceDto hcsaServiceDto = new HcsaServiceDto();
            hcsaServiceDto.setVersion("1");
            hcsaServiceConfigDto.setHcsaServiceDto(hcsaServiceDto);
            Map<String, List<HcsaConfigPageDto>> HcsaConfigPageDto =   configService.getHcsaConfigPageDto();
            hcsaServiceConfigDto.setHcsaConfigPageDtoMap(HcsaConfigPageDto);
        }
        ParamUtil.setRequestAttr(request,"hcsaServiceConfigDto",hcsaServiceConfigDto);

        List<HcsaServiceCategoryDto> categoryDtos = configService.getHcsaServiceCategoryDto();
        categoryDtos.sort((s1, s2) -> (s1.getName().compareTo(s2.getName())));
        request.getSession().setAttribute("categoryDtos",categoryDtos);

        List<SelectOption> selectOptionList1 = MasterCodeUtil.retrieveOptionsByCodes(ServiceConfigConstant.SERVICE_CODE);
        selectOptionList1.sort((s1, s2) -> (s1.getText().compareTo(s2.getText())));
        request.setAttribute("codeSelectOptionList",selectOptionList1);

        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        selectOptions.add(new SelectOption("mandatory","Mandatory"));
        selectOptions.add(new SelectOption("optional","Optional"));
        request.setAttribute("selectOptions",selectOptions);

        //Routing Stages
        List<SelectOption> routingStagesOption = IaisCommonUtils.genNewArrayList();
        routingStagesOption.add(new SelectOption("common","Common Pool"));
        routingStagesOption.add(new SelectOption("round","Round Robin"));
        routingStagesOption.add(new SelectOption("assign","Supervisor Assign"));
        request.setAttribute("routingStagesOption",routingStagesOption);

        // for Service Doc personnel
        List<SelectOption> serviceDocPersonnelsOption = getSelectOptionForServiceDocPersonnel(hcsaServiceConfigDto.getHcsaServiceDto().getSvcType(),
                hcsaServiceConfigDto.getHcsaServiceDto().getSvcCode(),hcsaServiceConfigDto.getSupplementaryForm());
        request.setAttribute("serviceDocPersonnelsOption",serviceDocPersonnelsOption);

        // get all Specialised service
        List<HcsaServiceDto>  specHcsaServiceDtos = configService.getActiveServicesBySvcType(HcsaConsts.SERVICE_TYPE_SPECIFIED);
        ParamUtil.setSessionAttr(request,"specHcsaServiceOptions",(Serializable)getSelectOptionForHcsaServiceDtos(specHcsaServiceDtos));
        //get all Other service
        List<HcsaServiceDto>  otherHcsaServiceDtos = configService.getActiveServicesBySvcType(HcsaConsts.SERVICE_TYPE_OTHERS);
        ParamUtil.setSessionAttr(request,"otherHcsaServiceOptions",(Serializable)getSelectOptionForHcsaServiceDtos(otherHcsaServiceDtos));

        return hcsaServiceConfigDto;
    }

    private List<SelectOption> getSelectOptionForServiceDocPersonnel(String serviceType,String serviceCode,Boolean isSuppFormSelect){
        List<SelectOption> result = IaisCommonUtils.genNewArrayList();
        if(StringUtil.isEmpty(serviceType) || HcsaConsts.SERVICE_TYPE_BASE.equals(serviceType)){
           for(String key:ServiceConfigConstant.SERVICE_DOC_PERSONNEL_BASE.keySet()){
             result.add(new SelectOption(key,ServiceConfigConstant.SERVICE_DOC_PERSONNEL_BASE.get(key)));
           }
          /* if(isSuppFormSelect != null && isSuppFormSelect){
               for(String key:ServiceConfigConstant.SERVICE_DOC_PERSONNEL_SUPPLEMENTARY_FORM.keySet()){
                   result.add(new SelectOption(key,ServiceConfigConstant.SERVICE_DOC_PERSONNEL_SUPPLEMENTARY_FORM.get(key)));
               }
           }*/
           if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode) || AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
               result.add(new SelectOption(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR,"Clinical Director"));
           }else{
               result.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO,"Clinical Governance Officer (CGO)"));
           }
        }else if(HcsaConsts.SERVICE_TYPE_SPECIFIED.equals(serviceType)){
            for(String key:ServiceConfigConstant.SERVICE_DOC_PERSONNEL_SPECIAL.keySet()){
                result.add(new SelectOption(key,ServiceConfigConstant.SERVICE_DOC_PERSONNEL_SPECIAL.get(key)));
            }
        }
        return result;
    }

    private List<SelectOption> getSelectOptionForHcsaServiceDtos(List<HcsaServiceDto> hcsaServiceDtos){
        List<SelectOption> result = IaisCommonUtils.genNewArrayList();
        if(IaisCommonUtils.isNotEmpty(hcsaServiceDtos)){
            for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos ){
                result.add(new SelectOption(hcsaServiceDto.getSvcCode(),hcsaServiceDto.getSvcDisplayDesc()));
            }
        }
        return result;
    }

    private void removeSession(BaseProcessClass bpc){
        bpc.request.getSession().removeAttribute("hcsaServiceCategoryDtos");
        bpc.request.getSession().removeAttribute("categoryDtos");
        bpc.request.getSession().removeAttribute("maskHcsaServiceCategory");
    }


    private HcsaServiceDto getActiveHcsaServiceDto(List<HcsaServiceDto> hcsaServiceDtosVersion){
        HcsaServiceDto result = null;

        if(IaisCommonUtils.isNotEmpty(hcsaServiceDtosVersion)){
            for(HcsaServiceDto hcsaServiceDto :hcsaServiceDtosVersion ){
                if(AppConsts.COMMON_STATUS_ACTIVE.equals(hcsaServiceDto.getStatus())){
                    result = hcsaServiceDto;
                }
            }
        }
        return result;

    }

    private String getLasterVersion(List<HcsaServiceDto> hcsaServiceDtosVersion){
        String result = "1";
        if(IaisCommonUtils.isNotEmpty(hcsaServiceDtosVersion)){
            result = String.valueOf(Integer.parseInt(hcsaServiceDtosVersion.get(hcsaServiceDtosVersion.size()-1).getVersion())+1);
        }
        return result;
    }

    private void setHcsaServiceDto(HcsaServiceConfigDto hcsaServiceConfigDto){
        log.info(StringUtil.changeForLog("The setHcsaServiceDto start ..."));

        HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
        List<HcsaServiceDto> hcsaServiceDtosVersion = hcsaServiceConfigDto.getHcsaServiceDtosVersion();

        hcsaServiceDto.setVersion(getLasterVersion(hcsaServiceDtosVersion));
        HcsaServiceDto activeHcsaServiceDto = getActiveHcsaServiceDto(hcsaServiceDtosVersion);
        hcsaServiceConfigDto.setActiveHcsaServiceDto(activeHcsaServiceDto);

        Date parse = null;
        try {
            parse = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).parse(hcsaServiceDto.getEffectiveDate());
        } catch (Exception e) {
            log.error(StringUtil.changeForLog("The Date format error"),e);
        }
        if(activeHcsaServiceDto == null){
            if(parse.before(new Date())){
                hcsaServiceDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            }else {
                hcsaServiceDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            }
        }else{
            if(parse.before(new Date())){
                hcsaServiceDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                activeHcsaServiceDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                activeHcsaServiceDto.setEndDate(Formatter.formatDateTime(new Date(),AppConsts.DEFAULT_DATE_FORMAT));
            }else {
                hcsaServiceDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                activeHcsaServiceDto.setEndDate(hcsaServiceDto.getEffectiveDate());
            }
        }
        log.info(StringUtil.changeForLog("The setHcsaServiceDto end ..."));
    }


    private HcsaServiceConfigDto transferHcsaServiceConfigDtoForDB(HcsaServiceConfigDto hcsaServiceConfigDto){
        HcsaServiceDto hcsaServiceDto = hcsaServiceConfigDto.getHcsaServiceDto();
        setHcsaServiceDto(hcsaServiceConfigDto);
        hcsaServiceConfigDto.setHcsaServiceDto(hcsaServiceDto);

        //for HcsaSvcSpePremisesTypeDto and HcsaServiceCategoryDisciplineDto and subservice
        hcsaServiceConfigDto.setHcsaSvcSpePremisesTypeDtos(transferToHcsaSvcSpePremisesTypeDtoAndHcsaServiceCategoryDisciplineDto(hcsaServiceConfigDto));

        //for routing stage
        if(HcsaConsts.SERVICE_TYPE_BASE.equals(hcsaServiceDto.getSvcType())){
            Map<String, List<HcsaConfigPageDto>> hcsaConfigPageDtoMap = hcsaServiceConfigDto.getHcsaConfigPageDtoMap();
            hcsaServiceConfigDto.setHcsaSvcRoutingStageCompoundDtos(getHcsaSvcRoutingStageCompoundDtos(hcsaConfigPageDtoMap,hcsaServiceDto.getCategoryId()));
        }

        return  hcsaServiceConfigDto;
    }


    private List<HcsaSvcRoutingStageCompoundDto> getHcsaSvcRoutingStageCompoundDtos(Map<String, List<HcsaConfigPageDto>> hcsaConfigPageDtoMap,
                                                                                    String categoryId){
        log.info(StringUtil.changeForLog("The getHcsaSvcRoutingStageCompoundDtos start ..."));
        List<HcsaSvcCateWrkgrpCorrelationDto> hcsaSvcCateWrkgrpCorrelationDtos =
                configService.getHcsaSvcCateWrkgrpCorrelationDtoBySvcCateId(categoryId);
        List<HcsaSvcRoutingStageCompoundDto> hcsaSvcRoutingStageCompoundDtos = IaisCommonUtils.genNewArrayList();
        for(String appType : hcsaConfigPageDtoMap.keySet()){
            log.info(StringUtil.changeForLog("The getHcsaSvcRoutingStageCompoundDtos appType is -->:"+appType));
            List<HcsaConfigPageDto> hcsaConfigPageDtos = hcsaConfigPageDtoMap.get(appType);
            for(HcsaConfigPageDto hcsaConfigPageDto : hcsaConfigPageDtos){
                String schemeName = hcsaConfigPageDto.getRoutingSchemeName();
                if(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appType) || ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(appType)){
                    if("INS".equals(hcsaConfigPageDto.getStageCode()) || "AO2".equals(hcsaConfigPageDto.getStageCode())){
                        schemeName = "common";
                    }else{
                        schemeName = "round";
                    }
                }
                log.info(StringUtil.changeForLog("The getHcsaSvcRoutingStageCompoundDtos schemeName is -->:"+schemeName));
                log.info(StringUtil.changeForLog("The getHcsaSvcRoutingStageCompoundDtos hcsaConfigPageDto.getStageCode() is -->:"+hcsaConfigPageDto.getStageCode()));
                if(StringUtil.isNotEmpty(schemeName)){
                    HcsaSvcRoutingStageCompoundDto hcsaSvcRoutingStageCompoundDto = new HcsaSvcRoutingStageCompoundDto();

                    HcsaSvcRoutingStageDto hcsaSvcRoutingStageDto = new HcsaSvcRoutingStageDto();
                    hcsaSvcRoutingStageDto.setAppType(hcsaConfigPageDto.getAppType());
                    hcsaSvcRoutingStageDto.setStageId(hcsaConfigPageDto.getStageId());
                    hcsaSvcRoutingStageDto.setCanApprove("1".equals(hcsaConfigPageDto.getCanApprove())?"1":"0");
                    hcsaSvcRoutingStageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    hcsaSvcRoutingStageCompoundDto.setHcsaSvcRoutingStageDto(hcsaSvcRoutingStageDto);

                    HcsaSvcSpecificStageWorkloadDto hcsaSvcSpecificStageWorkloadDto = new HcsaSvcSpecificStageWorkloadDto();
                    hcsaSvcSpecificStageWorkloadDto.setAppType(hcsaConfigPageDto.getAppType());
                    hcsaSvcSpecificStageWorkloadDto.setManhourCount(StringUtil.isNotEmpty(hcsaConfigPageDto.getManhours())?hcsaConfigPageDto.getManhours():"1");
                    hcsaSvcSpecificStageWorkloadDto.setStageId(hcsaConfigPageDto.getStageId());
                    hcsaSvcSpecificStageWorkloadDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    hcsaSvcRoutingStageCompoundDto.setHcsaSvcSpecificStageWorkloadDto(hcsaSvcSpecificStageWorkloadDto);

                    HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                    hcsaSvcStageWorkingGroupDto.setType(hcsaConfigPageDto.getAppType());
                    hcsaSvcStageWorkingGroupDto.setStageId(hcsaConfigPageDto.getStageId());
                    hcsaSvcStageWorkingGroupDto.setOrder(1);
                    hcsaSvcStageWorkingGroupDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    setWorkGroupId(hcsaSvcCateWrkgrpCorrelationDtos,hcsaSvcStageWorkingGroupDto);
                    hcsaSvcRoutingStageCompoundDto.setHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto);


                    HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto = new HcsaSvcSpeRoutingSchemeDto();
                    hcsaSvcSpeRoutingSchemeDto.setAppType(hcsaConfigPageDto.getAppType());
                    hcsaSvcSpeRoutingSchemeDto.setStageId(hcsaConfigPageDto.getStageId());
                    hcsaSvcSpeRoutingSchemeDto.setSchemeType(schemeName);
                    hcsaSvcSpeRoutingSchemeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    hcsaSvcRoutingStageCompoundDto.setHcsaSvcSpeRoutingSchemeDto(hcsaSvcSpeRoutingSchemeDto);

                    hcsaSvcRoutingStageCompoundDtos.add(hcsaSvcRoutingStageCompoundDto);

                    //for ins
                    List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos = hcsaConfigPageDto.getHcsaSvcSpeRoutingSchemeDtos();
                    if(IaisCommonUtils.isNotEmpty(hcsaSvcSpeRoutingSchemeDtos)){
                        for(HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDtoIns : hcsaSvcSpeRoutingSchemeDtos){
                            String routingSchemeName = hcsaSvcSpeRoutingSchemeDtoIns.getSchemeType();
                            if(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appType) || ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(appType)){
                                routingSchemeName = "round";
                            }
                            log.info(StringUtil.changeForLog("The getHcsaSvcRoutingStageCompoundDtos routingSchemeName is -->:"+routingSchemeName));
                            if(StringUtil.isNotEmpty(routingSchemeName)){
                                HcsaSvcRoutingStageCompoundDto hcsaSvcRoutingStageCompoundDtoIns = new HcsaSvcRoutingStageCompoundDto();
                                hcsaSvcSpeRoutingSchemeDtoIns.setAppType(hcsaConfigPageDto.getAppType());
                                hcsaSvcSpeRoutingSchemeDtoIns.setStageId(hcsaConfigPageDto.getStageId());
                                hcsaSvcSpeRoutingSchemeDtoIns.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                                hcsaSvcSpeRoutingSchemeDtoIns.setSchemeType(routingSchemeName);
                                hcsaSvcRoutingStageCompoundDtoIns.setHcsaSvcSpeRoutingSchemeDto(hcsaSvcSpeRoutingSchemeDtoIns);

                                HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDtoIns = new HcsaSvcStageWorkingGroupDto();
                                hcsaSvcStageWorkingGroupDtoIns.setType(hcsaConfigPageDto.getAppType());
                                hcsaSvcStageWorkingGroupDtoIns.setStageId(hcsaConfigPageDto.getStageId());
                                hcsaSvcStageWorkingGroupDtoIns.setOrder(Integer.parseInt(hcsaSvcSpeRoutingSchemeDtoIns.getInsOder()));
                                hcsaSvcStageWorkingGroupDtoIns.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                                setWorkGroupId(hcsaSvcCateWrkgrpCorrelationDtos,hcsaSvcStageWorkingGroupDtoIns);
                                hcsaSvcRoutingStageCompoundDtoIns.setHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDtoIns);

                                hcsaSvcRoutingStageCompoundDtos.add(hcsaSvcRoutingStageCompoundDtoIns);
                            }
                        }
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("The getHcsaSvcRoutingStageCompoundDtos end ..."));
        return hcsaSvcRoutingStageCompoundDtos;
    }

    private void setWorkGroupId(
            List<HcsaSvcCateWrkgrpCorrelationDto> hcsaSvcCateWrkgrpCorrelationDtos,
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto){
            String groupId = null;
            Integer subOrder = hcsaSvcStageWorkingGroupDto.getOrder();
            if(subOrder == 3){
                subOrder = 1;
            }
           if(IaisCommonUtils.isNotEmpty(hcsaSvcCateWrkgrpCorrelationDtos)){
             for(HcsaSvcCateWrkgrpCorrelationDto hcsaSvcCateWrkgrpCorrelationDto :hcsaSvcCateWrkgrpCorrelationDtos ){
                 if(hcsaSvcCateWrkgrpCorrelationDto.getStageId().equals(hcsaSvcStageWorkingGroupDto.getStageId())
                         && hcsaSvcCateWrkgrpCorrelationDto.getSubOrder().equals(String.valueOf(subOrder))){
                     groupId = hcsaSvcCateWrkgrpCorrelationDto.getWrkGrpId();
                     break;
                 }
             }
           }
           if(StringUtil.isNotEmpty(groupId)){
               hcsaSvcStageWorkingGroupDto.setGroupId(groupId);
           }else{
               log.error(StringUtil.changeForLog(
                       "stagetId:" + hcsaSvcStageWorkingGroupDto.getStageId()
                       +" -- SubOrder:" + hcsaSvcStageWorkingGroupDto.getOrder())
                       + " can no get the work groupId");
           }


    }

    private List<HcsaSvcSpecifiedCorrelationDto>  transferToHcsaSvcSpecifiedCorrelationDto(Map<String,HcsaServiceSubServicePageDto> hcsaServiceSubServicePageDtoMap,
                                                                                           String premisesType,
                                                                                           String serviceType,
                                                                                           HcsaSvcSpePremisesTypeDto hcsaSvcSpePremisesTypeDto){
        log.info(StringUtil.changeForLog("The transferToHcsaSvcSpecifiedCorrelationDto start ..."));
        log.info(StringUtil.changeForLog("The transferToHcsaSvcSpecifiedCorrelationDto premisesType -->:"+premisesType));
        log.info(StringUtil.changeForLog("The transferToHcsaSvcSpecifiedCorrelationDto serviceType -->:"+serviceType));
        List<HcsaSvcSpecifiedCorrelationDto> hcsaSvcSpecifiedCorrelationDtos = IaisCommonUtils.genNewArrayList();
        if(hcsaServiceSubServicePageDtoMap != null){
            HcsaServiceSubServicePageDto hcsaServiceSubServicePageDto =  hcsaServiceSubServicePageDtoMap.get(premisesType);
            if(HcsaConsts.SERVICE_TYPE_SPECIFIED.equals(serviceType)){
                hcsaSvcSpePremisesTypeDto.setSpecialSvcSecName(hcsaServiceSubServicePageDto.getSectionHeader());
            }
            List<HcsaServiceSubServiceErrorsDto> hcsaServiceSubServiceErrorsDtos = hcsaServiceSubServicePageDto.getHcsaServiceSubServiceErrorsDtos();
            if(IaisCommonUtils.isNotEmpty(hcsaServiceSubServiceErrorsDtos)){
                for(HcsaServiceSubServiceErrorsDto hcsaServiceSubServiceErrorsDto : hcsaServiceSubServiceErrorsDtos){
                    HcsaSvcSpecifiedCorrelationDto hcsaSvcSpecifiedCorrelationDto = new HcsaSvcSpecifiedCorrelationDto();
                    hcsaSvcSpecifiedCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    hcsaSvcSpecifiedCorrelationDto.setSubSvcType(serviceType);
                    hcsaSvcSpecifiedCorrelationDto.setSpecifiedSvcId(hcsaServiceSubServiceErrorsDto.getSubServiceCode());
                    String level = hcsaServiceSubServiceErrorsDto.getLevel();
                    if("0".equals(level)){
                        hcsaSvcSpecifiedCorrelationDtos.add(hcsaSvcSpecifiedCorrelationDto);
                    }else if("1".equals(level)){
                        if(IaisCommonUtils.isEmpty(hcsaSvcSpecifiedCorrelationDtos)){
                            hcsaSvcSpecifiedCorrelationDtos.add(hcsaSvcSpecifiedCorrelationDto);
                        }else{
                            HcsaSvcSpecifiedCorrelationDto hcsaSvcSpecifiedCorrelationDto0 =   hcsaSvcSpecifiedCorrelationDtos.get(hcsaSvcSpecifiedCorrelationDtos.size()-1);
                            List<HcsaSvcSpecifiedCorrelationDto> subHcsaSvcSpecifiedCorrelationDtos = hcsaSvcSpecifiedCorrelationDto0.getSubHcsaSvcSpecifiedCorrelationDtos();
                            subHcsaSvcSpecifiedCorrelationDtos.add(hcsaSvcSpecifiedCorrelationDto);
                            hcsaSvcSpecifiedCorrelationDto0.setSubHcsaSvcSpecifiedCorrelationDtos(subHcsaSvcSpecifiedCorrelationDtos);
                        }
                    }else{
                        if(IaisCommonUtils.isEmpty(hcsaSvcSpecifiedCorrelationDtos)){
                            hcsaSvcSpecifiedCorrelationDtos.add(hcsaSvcSpecifiedCorrelationDto);
                        }else{
                            HcsaSvcSpecifiedCorrelationDto hcsaSvcSpecifiedCorrelationDto0 =   hcsaSvcSpecifiedCorrelationDtos.get(hcsaSvcSpecifiedCorrelationDtos.size()-1);
                            List<HcsaSvcSpecifiedCorrelationDto> subHcsaSvcSpecifiedCorrelationDtos1 = hcsaSvcSpecifiedCorrelationDto0.getSubHcsaSvcSpecifiedCorrelationDtos();
                            if(IaisCommonUtils.isEmpty(subHcsaSvcSpecifiedCorrelationDtos1)){
                                subHcsaSvcSpecifiedCorrelationDtos1.add(hcsaSvcSpecifiedCorrelationDto);
                                hcsaSvcSpecifiedCorrelationDto0.setSubHcsaSvcSpecifiedCorrelationDtos(subHcsaSvcSpecifiedCorrelationDtos1);
                            }else{
                                HcsaSvcSpecifiedCorrelationDto hcsaSvcSpecifiedCorrelationDto1 = subHcsaSvcSpecifiedCorrelationDtos1.get(subHcsaSvcSpecifiedCorrelationDtos1.size() -1);
                                List<HcsaSvcSpecifiedCorrelationDto> subHcsaSvcSpecifiedCorrelationDtos2 =  hcsaSvcSpecifiedCorrelationDto1.getSubHcsaSvcSpecifiedCorrelationDtos();
                                subHcsaSvcSpecifiedCorrelationDtos2.add(hcsaSvcSpecifiedCorrelationDto);
                                hcsaSvcSpecifiedCorrelationDto1.setSubHcsaSvcSpecifiedCorrelationDtos(subHcsaSvcSpecifiedCorrelationDtos2);
                            }
                        }
                    }
                }

            }
        }else{
          log.error(StringUtil.changeForLog("The transferToHcsaSvcSpecifiedCorrelationDto hcsaServiceSubServicePageDtoMap is null "));
        }
        log.info(StringUtil.changeForLog("The transferToHcsaSvcSpecifiedCorrelationDto end ..."));
        return hcsaSvcSpecifiedCorrelationDtos;
    }


    // transfer HcsaSvcSpePremisesTypeDto and HcsaServiceCategoryDisciplineDto
    private List<HcsaSvcSpePremisesTypeDto> transferToHcsaSvcSpePremisesTypeDtoAndHcsaServiceCategoryDisciplineDto(HcsaServiceConfigDto hcsaServiceConfigDto){
        List<HcsaSvcSpePremisesTypeDto> result = IaisCommonUtils.genNewArrayList();
        String[] premisesTypes = hcsaServiceConfigDto.getPremisesTypes();
        if(premisesTypes != null && premisesTypes.length > 0){
            Map<String,HcsaServiceCategoryDisciplineDto> hcsaServiceCategoryDisciplineDtoMap = hcsaServiceConfigDto.getHcsaServiceCategoryDisciplineDtoMap();
            Map<String,HcsaServiceSubServicePageDto> specHcsaServiceSubServicePageDtoMap = hcsaServiceConfigDto.getSpecHcsaServiceSubServicePageDtoMap();
            Map<String,HcsaServiceSubServicePageDto> otherHcsaServiceSubServicePageDtoMap = hcsaServiceConfigDto.getOtherHcsaServiceSubServicePageDtoMap();
            for(String premisesType : premisesTypes){
                //for HcsaSvcSpePremisesTypeDto
                HcsaSvcSpePremisesTypeDto hcsaSvcSpePremisesTypeDto =  new HcsaSvcSpePremisesTypeDto();
                hcsaSvcSpePremisesTypeDto.setPremisesType(premisesType);
                hcsaSvcSpePremisesTypeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);

                //for HcsaServiceCategoryDisciplineDto
                if(hcsaServiceCategoryDisciplineDtoMap != null){
                    HcsaServiceCategoryDisciplineDto hcsaServiceCategoryDisciplineDto = hcsaServiceCategoryDisciplineDtoMap.get(premisesType);
                    hcsaSvcSpePremisesTypeDto.setCategorySectionName(hcsaServiceCategoryDisciplineDto.getSectionHeader());
                    List<HcsaServiceSubTypeDto> hcsaServiceSubTypeDtos = transferHcsaServiceSubTypeDto(hcsaServiceCategoryDisciplineDto);
                    hcsaSvcSpePremisesTypeDto.setHcsaServiceSubTypeDtos(hcsaServiceSubTypeDtos);
                }

                //for specHcsaServiceSubServicePageDtoMap
                List<HcsaSvcSpecifiedCorrelationDto> specHcsaSvcSpecifiedCorrelationDtos  = transferToHcsaSvcSpecifiedCorrelationDto(specHcsaServiceSubServicePageDtoMap,
                        premisesType,HcsaConsts.SERVICE_TYPE_SPECIFIED,hcsaSvcSpePremisesTypeDto);
                hcsaSvcSpePremisesTypeDto.setSpecHcsaSvcSpecifiedCorrelationDtos(specHcsaSvcSpecifiedCorrelationDtos);

                //for specHcsaServiceSubServicePageDtoMap
                List<HcsaSvcSpecifiedCorrelationDto> otherHcsaSvcSpecifiedCorrelationDtos  = transferToHcsaSvcSpecifiedCorrelationDto(otherHcsaServiceSubServicePageDtoMap,
                        premisesType,HcsaConsts.SERVICE_TYPE_OTHERS,hcsaSvcSpePremisesTypeDto);
                hcsaSvcSpePremisesTypeDto.setOtherHcsaSvcSpecifiedCorrelationDtos(otherHcsaSvcSpecifiedCorrelationDtos);

                result.add(hcsaSvcSpePremisesTypeDto);
            }
        }
        return result;
    }

    private List<HcsaServiceSubTypeDto> transferHcsaServiceSubTypeDto(HcsaServiceCategoryDisciplineDto hcsaServiceCategoryDisciplineDto){
        List<HcsaServiceSubTypeDto> result = IaisCommonUtils.genNewArrayList();
        String[] categoryDisciplines = hcsaServiceCategoryDisciplineDto.getCategoryDisciplines();
        if(categoryDisciplines != null && categoryDisciplines.length > 0){
            for(String categoryDiscipline : categoryDisciplines){
                HcsaServiceSubTypeDto hcsaServiceSubTypeDto = new HcsaServiceSubTypeDto();

                hcsaServiceSubTypeDto.setSubtypeName(categoryDiscipline);
                hcsaServiceSubTypeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                hcsaServiceSubTypeDto.setType(ApplicationConsts.SUB_TYPE_MODALITY);

                result.add(hcsaServiceSubTypeDto);
            }
        }
        return result;
    }



    private Map<String, List<HcsaConfigPageDto>> addRoutingStage(HttpServletRequest request){
        log.info(StringUtil.changeForLog("The addRoutingStage start ..."));
        Map<String, List<HcsaConfigPageDto>> hcsaConfigPageDtoMap =   configService.getHcsaConfigPageDto();
        for(String appType :hcsaConfigPageDtoMap.keySet() ){
            List<HcsaConfigPageDto> hcsaConfigPageDtos = hcsaConfigPageDtoMap.get(appType);
            for(HcsaConfigPageDto hcsaConfigPageDto : hcsaConfigPageDtos){

                String isMandatory=  request.getParameter("isMandatory"+ hcsaConfigPageDto.getStageCode()+appType);
                String canApprove = request.getParameter("canApprove" +  hcsaConfigPageDto.getStageCode()+appType);
                String routingSchemeName = request.getParameter("routingSchemeName" +  hcsaConfigPageDto.getStageCode()+appType);
                String manhours = request.getParameter("manhours" +  hcsaConfigPageDto.getStageCode()+appType);
                hcsaConfigPageDto.setCanApprove(canApprove);
                hcsaConfigPageDto.setIsMandatory(isMandatory);
                hcsaConfigPageDto.setRoutingSchemeName(routingSchemeName);
                hcsaConfigPageDto.setManhours(manhours);

                List<HcsaSvcSpeRoutingSchemeDto> hcsaSvcSpeRoutingSchemeDtos = hcsaConfigPageDto.getHcsaSvcSpeRoutingSchemeDtos();
                if(IaisCommonUtils.isNotEmpty(hcsaSvcSpeRoutingSchemeDtos)){
                    for(HcsaSvcSpeRoutingSchemeDto hcsaSvcSpeRoutingSchemeDto : hcsaSvcSpeRoutingSchemeDtos){
                        String schemeType=  request.getParameter("schemeType"+ hcsaConfigPageDto.getStageCode()+appType+hcsaSvcSpeRoutingSchemeDto.getInsOder());
                        hcsaSvcSpeRoutingSchemeDto.setSchemeType(schemeType);
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("The addRoutingStage end ..."));
        return hcsaConfigPageDtoMap;
    }

    private void addCategoryDisciplineAndSubService(HttpServletRequest request,HcsaServiceConfigDto hcsaServiceConfigDto){
        log.info(StringUtil.changeForLog("The addCategoryDisciplineAndSubService start ..."));
        String[] premisesTypes = hcsaServiceConfigDto.getPremisesTypes();
        if(premisesTypes != null && premisesTypes.length > 0){
            Map<String,HcsaServiceCategoryDisciplineDto> hcsaServiceCategoryDisciplineDtoMap = IaisCommonUtils.genNewHashMap();
            Map<String,HcsaServiceSubServicePageDto> specHcsaServiceSubServicePageDtoMap = IaisCommonUtils.genNewHashMap();
            Map<String,HcsaServiceSubServicePageDto> otherHcsaServiceSubServicePageDtoMap = IaisCommonUtils.genNewHashMap();
            for(String premisesType : premisesTypes){
                //for hcsaServiceCategoryDisciplineDtoMap
                HcsaServiceCategoryDisciplineDto hcsaServiceCategoryDisciplineDto = new HcsaServiceCategoryDisciplineDto();
                //String sectionHeader = ParamUtil.getString(request,premisesType+"-sectionHeader");
                String[] categoryDisciplines = ParamUtil.getStrings(request,premisesType+"-categoryDisciplines");
                hcsaServiceCategoryDisciplineDto.setSectionHeader(hcsaServiceConfigDto.getDisciplineSectionHeader());
                hcsaServiceCategoryDisciplineDto.setCategoryDisciplines(categoryDisciplines);
                hcsaServiceCategoryDisciplineDtoMap.put(premisesType,hcsaServiceCategoryDisciplineDto);
                hcsaServiceConfigDto.setHcsaServiceCategoryDisciplineDtoMap(hcsaServiceCategoryDisciplineDtoMap);
                //for specHcsaServiceSubServicePageDtoMap
                addSubServiceData(request,premisesType,HcsaConsts.SERVICE_TYPE_SPECIFIED,specHcsaServiceSubServicePageDtoMap,hcsaServiceConfigDto.getSpecialisedSectionHeader());
                //for otherHcsaServiceSubServicePageDtoMap
                addSubServiceData(request,premisesType,HcsaConsts.SERVICE_TYPE_OTHERS,otherHcsaServiceSubServicePageDtoMap,null);

            }
            hcsaServiceConfigDto.setHcsaServiceCategoryDisciplineDtoMap(hcsaServiceCategoryDisciplineDtoMap);
            hcsaServiceConfigDto.setSpecHcsaServiceSubServicePageDtoMap(specHcsaServiceSubServicePageDtoMap);
            hcsaServiceConfigDto.setOtherHcsaServiceSubServicePageDtoMap(otherHcsaServiceSubServicePageDtoMap);

        }else{
            log.error(StringUtil.changeForLog("The premisesTypes is null"));
        }
        log.info(StringUtil.changeForLog("The addCategoryDisciplineAndSubService end ..."));
    }

    private void addSubServiceData(HttpServletRequest request,String premisesType,String serviceType,
                                   Map<String,HcsaServiceSubServicePageDto> hcsaServiceSubServicePageDtoMap,String specSectionHeader){
        log.info(StringUtil.changeForLog("The addSubServiceData start ..."));
        log.info(StringUtil.changeForLog("The addSubServiceData premisesType -->:"+premisesType));
        log.info(StringUtil.changeForLog("The addSubServiceData serviceType -->:"+serviceType));
        HcsaServiceSubServicePageDto specHcsaServiceCategoryDisciplineDto = new HcsaServiceSubServicePageDto();
        //String specSectionHeader = ParamUtil.getString(request,premisesType+"-"+serviceType+"-sectionHeader");
        String[] specSubServiceCodes = ParamUtil.getStrings(request,premisesType+"-"+serviceType+"-subServiceCodes");
        String[] specLevels = ParamUtil.getStrings(request,premisesType+"-"+serviceType+"-levels");
        specHcsaServiceCategoryDisciplineDto.setSectionHeader(specSectionHeader);
        specHcsaServiceCategoryDisciplineDto.setSubServiceCodes(specSubServiceCodes);
        specHcsaServiceCategoryDisciplineDto.setLevels(specLevels);
        hcsaServiceSubServicePageDtoMap.put(premisesType,specHcsaServiceCategoryDisciplineDto);
        log.info(StringUtil.changeForLog("The addSubServiceData end ..."));
    }
    /*
    * get page all data
    * -----------------------
    * service name ,service code ...
    * --------------------------
    * ( NEW APPLICATION ) ; ( REQUEST FOR CHANGE ) ...BUTTON
    * these info in session
     *
    * */
    private HcsaServiceConfigDto getDateOfHcsaService(HttpServletRequest request)  {
        log.info(StringUtil.changeForLog("The getDateOfHcsaService start ..."));
        log.info(StringUtil.changeForLog("The getDateOfHcsaService hcsaServiceConfigDto"));
        HcsaServiceConfigDto hcsaServiceConfigDto = new HcsaServiceConfigDto();
        hcsaServiceConfigDto = ControllerHelper.get(request,hcsaServiceConfigDto);
        hcsaServiceConfigDto.setHcsaConfigPageDtoMap(null);//clear routing
        log.info(StringUtil.changeForLog("The getDateOfHcsaService hcsaServiceDto"));
        HcsaServiceDto hcsaServiceDto = new HcsaServiceDto();
        hcsaServiceDto =  ControllerHelper.get(request,hcsaServiceDto);
        hcsaServiceConfigDto.setHcsaServiceDto(hcsaServiceDto);
        log.info(StringUtil.changeForLog("The getDateOfHcsaService hcsaServiceDto.getSvcType() is -->:"+hcsaServiceDto.getSvcType()));
        if(HcsaConsts.SERVICE_TYPE_OTHERS.equals(hcsaServiceDto.getSvcType())){
          return hcsaServiceConfigDto;
        }
        //for HcsaServiceCategoryDisciplineDto  and sub service
        addCategoryDisciplineAndSubService(request,hcsaServiceConfigDto);
        //for minimum and maximum
        addSvcPersionnelAndStepConfigsFromPage(hcsaServiceConfigDto, request);
        //Number of service-related document (for specialised service) to be uploaded
        addDocumentConfigsFromPage(hcsaServiceConfigDto, request);
        if(HcsaConsts.SERVICE_TYPE_SPECIFIED.equals(hcsaServiceDto.getSvcType())){
            return hcsaServiceConfigDto;
        }

        //for routingStages
        Map<String, List<HcsaConfigPageDto>> hcsaConfigPageDtoMap = addRoutingStage(request);
        hcsaServiceConfigDto.setHcsaConfigPageDtoMap(hcsaConfigPageDtoMap);

        log.info(StringUtil.changeForLog("The getDateOfHcsaService end ..."));
        return hcsaServiceConfigDto;
    }

    private void addDocumentConfigsFromPage(HcsaServiceConfigDto hcsaServiceConfigDto, HttpServletRequest request) {
        log.info(StringUtil.changeForLog("The addDocumentConfigsFromPage start ..."));
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = hcsaServiceConfigDto.getHcsaServiceStepSchemeDtos();
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = IaisCommonUtils.genNewArrayList();
        String serviceDocSize = request.getParameter("serviceDocSize");
        String[] descriptionServiceDocs = request.getParameterValues("descriptionServiceDoc");
        String[] parameterValues = request.getParameterValues("selectDocPerson");
        String[] serviceDocMandatories = request.getParameterValues("serviceDocMandatory");
        String[] serviceDocPremises = request.getParameterValues("serviceDocPremises");

        if (descriptionServiceDocs != null) {
            for (int i = 0; i < descriptionServiceDocs.length; i++) {
                HcsaSvcDocConfigDto hcsaSvcDocConfigDto = new HcsaSvcDocConfigDto();
                hcsaSvcDocConfigDto.setDocDesc(descriptionServiceDocs[i]);
                hcsaSvcDocConfigDto.setDocTitle(descriptionServiceDocs[i]);
                hcsaSvcDocConfigDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                hcsaSvcDocConfigDto.setDispOrder(i);
                hcsaSvcDocConfigDto.setServiceId("");
                hcsaSvcDocConfigDto.setDupForPrem(String.valueOf(0));

                if ("0".equals(serviceDocMandatories[i])) {
                    hcsaSvcDocConfigDto.setIsMandatory(Boolean.FALSE);
                } else if ("1".equals(serviceDocMandatories[i])) {
                    hcsaSvcDocConfigDto.setIsMandatory(Boolean.TRUE);
                }
                if (StringUtil.isNotEmpty(parameterValues[i])) {
                   // hcsaSvcDocConfigDto.setDupForPerson(IaisCommonUtils.getDupForPersonFromType(parameterValues[i]));
                    hcsaSvcDocConfigDto.setDupForPerson(parameterValues[i]);
                }
                hcsaSvcDocConfigDto.setDupForPrem(serviceDocPremises[i]);

                hcsaSvcDocConfigDtos.add(hcsaSvcDocConfigDto);
            }

            addStepSchemeDto(true, HcsaConsts.STEP_DOCUMENTS, HcsaConsts.DOCUMENTS, hcsaServiceStepSchemeDtos);
        }
        hcsaServiceConfigDto.setServiceDocSize(serviceDocSize);
        hcsaServiceConfigDto.setHcsaSvcDocConfigDtos(hcsaSvcDocConfigDtos);
        log.info(StringUtil.changeForLog("The addDocumentConfigsFromPage end ..."));
    }

    private void addSvcPersionnelAndStepConfigsFromPage(HcsaServiceConfigDto hcsaServiceConfigDto, HttpServletRequest request) {
        log.info(StringUtil.changeForLog("The addSvcStepConfigsFromPage start ..."));
        String serviceType = hcsaServiceConfigDto.getHcsaServiceDto().getSvcType();
        String serviceCode = hcsaServiceConfigDto.getHcsaServiceDto().getSvcCode();
        log.info(StringUtil.changeForLog("The addSvcStepConfigsFromPage serviceType is -->:"+serviceType));
        log.info(StringUtil.changeForLog("The addSvcStepConfigsFromPage serviceCode is -->:"+serviceCode));
        // personnel
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = IaisCommonUtils.genNewArrayList();
        //for Licensable  and Specialised  Service
        HcsaSvcPersonnelDto cgoDto = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, request);
        HcsaSvcPersonnelDto slPersonnelDto = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER, request);

        //for Licensable  Service
        HcsaSvcPersonnelDto poDto = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_PSN_TYPE_PO, request);
        HcsaSvcPersonnelDto dpoDto = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, request);
        HcsaSvcPersonnelDto kahPersonnelDto = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_PSN_KAH, request);
        HcsaSvcPersonnelDto director = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR, request);
        HcsaSvcPersonnelDto svcPersonnelDto = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL, request);
        HcsaSvcPersonnelDto vehicles = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_VEHICLES, request);
        HcsaSvcPersonnelDto charges = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_CHARGES, request);
        HcsaSvcPersonnelDto otherCharges = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_CHARGES_OTHER, request);
        HcsaSvcPersonnelDto mapPersonnelDto = getHcsaSvcPersonnelDto(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, request);

        HcsaSvcPersonnelDto SP001 = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMBRYOLOGIST, request);
        HcsaSvcPersonnelDto SP002 = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_TYPE_AR_PRACTITIONER, request);
        HcsaSvcPersonnelDto SP003 = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_TYPE_NURSES, request);

        HcsaSvcPersonnelDto spmsc = getHcsaSvcPersonnelDto(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_PERSON_MANAGING_SPECIAL_CARE, request);
        HcsaSvcPersonnelDto smdp = getHcsaSvcPersonnelDto(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_MEDICAL_DENTAL_PRACTITION, request);
        HcsaSvcPersonnelDto srp = getHcsaSvcPersonnelDto(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_RENAL_PHYSICIAN, request);
        HcsaSvcPersonnelDto snic = getHcsaSvcPersonnelDto(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_NURSE_IN_CHARGE, request);
        HcsaSvcPersonnelDto sdtrn = getHcsaSvcPersonnelDto(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_DIALYSIS_TRAINED_REGISTERED_NURSE, request);
        HcsaSvcPersonnelDto srnndt = getHcsaSvcPersonnelDto(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_REGISTERED_NURSES_NOT_DIALYSIS_TRAINED, request);
        HcsaSvcPersonnelDto sen = getHcsaSvcPersonnelDto(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_ENROLLED_NURSE, request);
        HcsaSvcPersonnelDto sa = getHcsaSvcPersonnelDto(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_ACUPUNCTURIST, request);
        HcsaSvcPersonnelDto sam = getHcsaSvcPersonnelDto(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_ANAESTHETIST_MEDICAL, request);
        HcsaSvcPersonnelDto stnm = getHcsaSvcPersonnelDto(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_TRAINED_NURSES_MEDICAL, request);
        HcsaSvcPersonnelDto spdo = getHcsaSvcPersonnelDto(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_PRACTICING_DOCTOR, request);
        HcsaSvcPersonnelDto sad = getHcsaSvcPersonnelDto(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_ANAESTHETIST_DENTAL, request);
        HcsaSvcPersonnelDto stnd = getHcsaSvcPersonnelDto(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_TRAINED_NURSES_DENTAL, request);
        HcsaSvcPersonnelDto spde = getHcsaSvcPersonnelDto(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_PRACTISING_DENTIST, request);
        HcsaSvcPersonnelDto soht = getHcsaSvcPersonnelDto(ApplicationConsts.SUPPLEMENTARY_FORM_TYPE_ORAL_HEALTHCARE_THERAPIST, request);

        //for Specialised  Service
        HcsaSvcPersonnelDto edd = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_DIRECTOR, request);
        HcsaSvcPersonnelDto ednd = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_TYPE_EMERGENCY_DEPARTMENT_NURSING_DIRECTOR, request);
        HcsaSvcPersonnelDto nurseInCharge = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE, request);
        HcsaSvcPersonnelDto rso = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER, request);
       // HcsaSvcPersonnelDto diagnosticRadiographer  = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_DR, request);
        HcsaSvcPersonnelDto medicalPhysicist = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST, request);
        HcsaSvcPersonnelDto radiationPhysicist = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL, request);
        //HcsaSvcPersonnelDto nMTechnologist = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NM, request);
        HcsaSvcPersonnelDto sppt007 = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_COMBINE, request);
        HcsaSvcPersonnelDto sppt008 = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_ONCOLOGIST, request);
        HcsaSvcPersonnelDto sppt009 = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_CQMP, request);
        HcsaSvcPersonnelDto sppt010 = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_DOSIMETRIST, request);
        HcsaSvcPersonnelDto sppt011 = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_THERAPIST, request);
        HcsaSvcPersonnelDto sppt012 = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_CREDENTIALING_COMMITTEE, request);
        HcsaSvcPersonnelDto sppt013 = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_SERVICE_REVIEW_COMMITTEE, request);
        HcsaSvcPersonnelDto sppt014 = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_CPP, request);
        HcsaSvcPersonnelDto sppt015 = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MAIN_COLLABORATING_MP, request);
        HcsaSvcPersonnelDto sppt016 = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_PERSON_DOCTOR_LIPOSUCTION, request);
        HcsaSvcPersonnelDto sppt017 = getHcsaSvcPersonnelDto(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE_MONITOR, request);


//        if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode) || AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
//            hcsaSvcPersonnelDtos.add(director);//Clinical Director
//        }else{
//            hcsaSvcPersonnelDtos.add(cgoDto);//Clinical Governance Officer (CGO)
//        }
        hcsaSvcPersonnelDtos.add(cgoDto);//Clinical Governance Officer (CGO)
        hcsaSvcPersonnelDtos.add(slPersonnelDto);//Section Leader
        if(HcsaConsts.SERVICE_TYPE_BASE.equals(serviceType)){
            hcsaSvcPersonnelDtos.add(poDto);//Principal Officer (PO)
            hcsaSvcPersonnelDtos.add(dpoDto);//Nominee
            hcsaSvcPersonnelDtos.add(kahPersonnelDto);//Key Appointment Holder (KAH)
            hcsaSvcPersonnelDtos.add(director);//Clinical Director
            hcsaSvcPersonnelDtos.add(svcPersonnelDto);//Service Personnel
            hcsaSvcPersonnelDtos.add(vehicles);//Vehicles
            hcsaSvcPersonnelDtos.add(charges);//General Conveyance Charges
            hcsaSvcPersonnelDtos.add(otherCharges);//Medical Equipment and Other Charges
            hcsaSvcPersonnelDtos.add(mapPersonnelDto);//MedAlert Person
            hcsaSvcPersonnelDtos.add(SP001);//Embryologist
            hcsaSvcPersonnelDtos.add(SP002);//AR Practitioner
            hcsaSvcPersonnelDtos.add(SP003);//Nurses
            //if(hcsaServiceConfigDto.getSupplementaryForm()){
                hcsaSvcPersonnelDtos.add(spmsc);//Person managing the Special Care Service
                hcsaSvcPersonnelDtos.add(smdp);//Medical / Dental Practition
                hcsaSvcPersonnelDtos.add(srp);//Renal Physician
                hcsaSvcPersonnelDtos.add(snic);//Nurse in Charge
                hcsaSvcPersonnelDtos.add(sdtrn);//Dialysis Trained Registered Nurse
                hcsaSvcPersonnelDtos.add(srnndt);//Registered Nurses not Dialysis Trained
                hcsaSvcPersonnelDtos.add(sen);//Enrolled Nurse
                hcsaSvcPersonnelDtos.add(sa);//Acupuncturist
                hcsaSvcPersonnelDtos.add(sam);//Anaesthetist (Medical Service)
                hcsaSvcPersonnelDtos.add(stnm);//Trained Nurses (Medical Service)
                hcsaSvcPersonnelDtos.add(spdo);//Practicing Doctor
                hcsaSvcPersonnelDtos.add(sad);//Anaesthetist (Dental Service)
                hcsaSvcPersonnelDtos.add(stnd);//Trained Nurses (Dental Service)
                hcsaSvcPersonnelDtos.add(spde);//Practising Dentist
                hcsaSvcPersonnelDtos.add(soht);//Trained Dental Hygienist/ Dental Therapist / Oral Healthcare Therapist
            //}
        }else if(HcsaConsts.SERVICE_TYPE_SPECIFIED.equals(serviceType)){
            hcsaSvcPersonnelDtos.add(edd);//Emergency Department Director
            hcsaSvcPersonnelDtos.add(ednd);//Emergency Department Nursing Director
            hcsaSvcPersonnelDtos.add(nurseInCharge);//Nurse In Charge
            hcsaSvcPersonnelDtos.add(rso);//Radiation Safety Officer (RSO)
           // hcsaSvcPersonnelDtos.add(diagnosticRadiographer);//Diagnostic Radiographer
            hcsaSvcPersonnelDtos.add(medicalPhysicist);//Medical Physicist
            hcsaSvcPersonnelDtos.add(radiationPhysicist);//Radiation Physicist
            //hcsaSvcPersonnelDtos.add(nMTechnologist);//NM Technologist

            hcsaSvcPersonnelDtos.add(sppt007);//Diagnostic Radiographer, Radiation Therapist or NM Technologist
            hcsaSvcPersonnelDtos.add(sppt008); //Radiation Oncologist (RO)
            hcsaSvcPersonnelDtos.add(sppt009); //Clinically Qualified Medical Physicist (CQMP)
            hcsaSvcPersonnelDtos.add(sppt010); //Medical Dosimetrist
            hcsaSvcPersonnelDtos.add(sppt011); //Radiation Therapist (RT)
            hcsaSvcPersonnelDtos.add(sppt012); //Credentialing Committee
            hcsaSvcPersonnelDtos.add(sppt013); //Service Review Committee
            hcsaSvcPersonnelDtos.add(sppt014); //Collaborative Prescribing Practitioners
            hcsaSvcPersonnelDtos.add(sppt015); //Main collaborating medical practitioner
            hcsaSvcPersonnelDtos.add(sppt016); //Person(s) on site to assist the doctor in liposuction
            hcsaSvcPersonnelDtos.add(sppt017); //Registered nurse(s) or doctor(s) to monitor sedated patient
        }

        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = IaisCommonUtils.genNewArrayList();
        //for step
        if(HcsaConsts.SERVICE_TYPE_BASE.equals(serviceType)){
            // step
           // List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = hcsaServiceConfigDto.getHcsaSvcSubtypeOrSubsumedDtos();
            addStepSchemeDto(isNeed(vehicles), HcsaConsts.STEP_VEHICLES, HcsaConsts.VEHICLES, hcsaServiceStepSchemeDtos);
            //addStepSchemeDto(!hcsaSvcSubtypeOrSubsumedDtos.isEmpty(), HcsaConsts.STEP_LABORATORY_DISCIPLINES, pageName, hcsaServiceStepSchemeDtos);
            if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode) || AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                addStepSchemeDto(isNeed(director), HcsaConsts.STEP_CLINICAL_DIRECTOR, HcsaConsts.CLINICAL_DIRECTORS_BE, hcsaServiceStepSchemeDtos);
            }
            addStepSchemeDto(isNeed(charges), HcsaConsts.STEP_CHARGES, HcsaConsts.CHARGES, hcsaServiceStepSchemeDtos);
            addStepSchemeDto(hcsaServiceConfigDto.getBusinessInformation(), HcsaConsts.STEP_BUSINESS_NAME, HcsaConsts.BUSINESS_NAME, hcsaServiceStepSchemeDtos);
            if(!(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode) || AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode))) {
                addStepSchemeDto(isNeed(cgoDto), HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS, HcsaConsts.CLINICAL_GOVERNANCE_OFFICER, hcsaServiceStepSchemeDtos);
            }
           // addStepSchemeDto(!hcsaSvcSubtypeOrSubsumedDtos.isEmpty() && isNeed(cgoDto), HcsaConsts.STEP_DISCIPLINE_ALLOCATION, pageName + " Allocation", hcsaServiceStepSchemeDtos);
            addStepSchemeDto(isNeed(svcPersonnelDto), HcsaConsts.STEP_SERVICE_PERSONNEL, HcsaConsts.SERVICE_PERSONNEL, hcsaServiceStepSchemeDtos);
            addStepSchemeDto(isNeed(poDto), HcsaConsts.STEP_PRINCIPAL_OFFICERS, HcsaConsts.PRINCIPAL_OFFICER, hcsaServiceStepSchemeDtos);
            addStepSchemeDto(isNeed(mapPersonnelDto), HcsaConsts.STEP_MEDALERT_PERSON, HcsaConsts.MEDALERT_PERSON, hcsaServiceStepSchemeDtos);
            addStepSchemeDto(isNeed(slPersonnelDto), HcsaConsts.STEP_SECTION_LEADER, HcsaConsts.SECTION_LEADER, hcsaServiceStepSchemeDtos);
            addStepSchemeDto(isNeed(kahPersonnelDto), HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER, HcsaConsts.KEY_APPOINTMENT_HOLDER, hcsaServiceStepSchemeDtos);
            //handle the step for Other Information, Supplementary Form,Special Services Information,Outsourced Providers
            handleStep(hcsaServiceConfigDto);

            addStepSchemeDto(hcsaServiceConfigDto.getOtherInformation()
                    , HcsaConsts.STEP_OTHER_INFORMATION, HcsaConsts.OTHER_INFORMATION, hcsaServiceStepSchemeDtos);
            addStepSchemeDto(hcsaServiceConfigDto.getSupplementaryForm()
                    , HcsaConsts.STEP_SUPPLEMENTARY_FORM, HcsaConsts.SUPPLEMENTARY_FORM, hcsaServiceStepSchemeDtos);
            addStepSchemeDto(hcsaServiceConfigDto.getSpecialServicesInformation()
                    , HcsaConsts.STEP_SPECIAL_SERVICES_FORM, hcsaServiceConfigDto.getSpecialisedSectionHeader()+HcsaConsts.SPECIAL_SERVICES_FORM, hcsaServiceStepSchemeDtos);
            addStepSchemeDto(hcsaServiceConfigDto.getOutsourcedProviders()
                    , HcsaConsts.STEP_OUTSOURCED_PROVIDERS, HcsaConsts.OUTSOURCED_PROVIDERS, hcsaServiceStepSchemeDtos);
        }
//        else if(HcsaConsts.SERVICE_TYPE_SPECIFIED.equals(serviceType)){
//            boolean isNeedSvcPersonnelDto = isNeed(nurseInCharge)||isNeed(rso)||isNeed(diagnosticRadiographer)||isNeed(medicalPhysicist)||isNeed(radiationPhysicist)||isNeed(nMTechnologist);
//            log.info(StringUtil.changeForLog("The addSvcStepConfigsFromPage isNeedSvcPersonnelDto is -->:"+isNeedSvcPersonnelDto));
//            addStepSchemeDto(isNeed(cgoDto), HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS, HcsaConsts.CLINICAL_GOVERNANCE_OFFICERS, hcsaServiceStepSchemeDtos);
//            addStepSchemeDto(isNeedSvcPersonnelDto, HcsaConsts.STEP_SERVICE_PERSONNEL, HcsaConsts.SERVICE_PERSONNEL, hcsaServiceStepSchemeDtos);
//            addStepSchemeDto(isNeed(slPersonnelDto), HcsaConsts.STEP_SECTION_LEADER, HcsaConsts.SECTION_LEADER, hcsaServiceStepSchemeDtos);
//        }

        hcsaServiceConfigDto.setHcsaSvcPersonnelDtos(hcsaSvcPersonnelDtos);
        hcsaServiceConfigDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
        log.info(StringUtil.changeForLog("The addSvcStepConfigsFromPage end ..."));
    }


    private void handleStep(HcsaServiceConfigDto hcsaServiceConfigDto){
        log.info(StringUtil.changeForLog("The handleStep start ..."));
        String serviceCode = hcsaServiceConfigDto.getHcsaServiceDto().getSvcCode();
        log.info(StringUtil.changeForLog("The handleStep serviceCode is -->:"+serviceCode));
        Map<String, HcsaServiceSubServicePageDto> specHcsaServiceSubServicePageDtoMap = hcsaServiceConfigDto.getSpecHcsaServiceSubServicePageDtoMap();
        Map<String, HcsaServiceSubServicePageDto> otherHcsaServiceSubServicePageDtoMap = hcsaServiceConfigDto.getOtherHcsaServiceSubServicePageDtoMap();
        hcsaServiceConfigDto.setOtherInformation(false);
        hcsaServiceConfigDto.setSupplementaryForm(false);
        hcsaServiceConfigDto.setSpecialServicesInformation(false);
        hcsaServiceConfigDto.setOutsourcedProviders(false);
        if(StringUtil.isNotEmpty(serviceCode)){
            if(ServiceConfigConstant.NEEDOTHERINFORMATIONSTEP.contains(serviceCode)
                    || isNotEmpty(otherHcsaServiceSubServicePageDtoMap)){
                hcsaServiceConfigDto.setOtherInformation(true);
            }
            if(ServiceConfigConstant.NEEDSUPPLEMENTARYFORMSTEP.contains(serviceCode)){
                hcsaServiceConfigDto.setSupplementaryForm(true);
            }else{
                List<SuppleFormItemConfigDto>  suppleFormItemConfigDtos = configCommService.getSuppleFormItemConfigs(serviceCode,HcsaConsts.ITME_TYPE_SUPLFORM);
                if(IaisCommonUtils.isNotEmpty(suppleFormItemConfigDtos)){
                    hcsaServiceConfigDto.setSupplementaryForm(true);
                }else{
                    log.info(StringUtil.changeForLog("The handleStep suppleFormItemConfigDtos is null"));
                }
            }
            if(ServiceConfigConstant.NEEDSPECIALSERVICESINFORMATIONSTEP.contains(serviceCode)
                    || isNotEmpty(specHcsaServiceSubServicePageDtoMap)){
                hcsaServiceConfigDto.setSpecialServicesInformation(true);
            }
            if(ServiceConfigConstant.NEEDOUTSOURCEDPROVIDERSSTEP.contains(serviceCode)){
                hcsaServiceConfigDto.setOutsourcedProviders(true);
            }
        }else{
            log.info(StringUtil.changeForLog("The handleStep serviceCode is null"));
        }
        log.info(StringUtil.changeForLog("The handleStep end ..."));
    }

    private boolean isNotEmpty(Map<String, HcsaServiceSubServicePageDto> hcsaServiceSubServicePageDtoMap){
        log.info(StringUtil.changeForLog("The isNotEmpty start ..."));
        boolean result = false;
        if(IaisCommonUtils.isNotEmpty(hcsaServiceSubServicePageDtoMap)){
          for(String key : hcsaServiceSubServicePageDtoMap.keySet()){
              HcsaServiceSubServicePageDto hcsaServiceSubServicePageDto = hcsaServiceSubServicePageDtoMap.get(key);
              String[] subServiceCodes = hcsaServiceSubServicePageDto.getSubServiceCodes();
              if(subServiceCodes != null && subServiceCodes.length >0){
                  for(String subServiceCode : subServiceCodes){
                    if(StringUtil.isNotEmpty(subServiceCode)){
                        result = true;
                        break;
                    }
                  }
                  if(result){
                      break;
                  }
              }
          }
        }
        log.info(StringUtil.changeForLog("The isNotEmpty result is -->:"+result));
        log.info(StringUtil.changeForLog("The isNotEmpty end ..."));
        return result;
    }

    private HcsaSvcPersonnelDto getHcsaSvcPersonnelDto(String psnType, HttpServletRequest request) {
        String manMedalertPerson = request.getParameter("man-" + ServiceConfigConstant.NAME_MAP.get(psnType));
        String mixMedalertPerson = request.getParameter("mix-" + ServiceConfigConstant.NAME_MAP.get(psnType));
        return configService.getHcsaSvcPersonnelDto(manMedalertPerson, mixMedalertPerson, psnType);
    }

    private void addStepSchemeDto(boolean isNeed, String stepCode, String stepName,
            List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos) {
        if (!isNeed) {
            return;
        }
        HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto = new HcsaServiceStepSchemeDto();
        hcsaServiceStepSchemeDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        hcsaServiceStepSchemeDto.setStepCode(stepCode);
        hcsaServiceStepSchemeDto.setSeqNum(ServiceConfigConstant.SEQ_MAP.get(stepCode));
        hcsaServiceStepSchemeDto.setStepName(stepName);
        hcsaServiceStepSchemeDtos.add(hcsaServiceStepSchemeDto);
    }

    private boolean isNeed(HcsaSvcPersonnelDto dto) {
        return isPositive(dto.getMandatoryCount()) && isPositive(dto.getMaximumCount());
    }

    private boolean isPositive(Integer i) {
        return i != null && i > 0;
    }
}
