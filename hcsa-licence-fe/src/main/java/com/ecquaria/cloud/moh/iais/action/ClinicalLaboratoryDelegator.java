package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.ClinicalOfficerValidateDto;
import com.ecquaria.cloud.moh.iais.dto.ServiceStepDto;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * ClinicalLaboratoryDelegator
 *
 * @author suocheng
 * @date 10/11/2019
 */
@Delegator("clinicalLaboratoryDelegator")
@Slf4j
public class ClinicalLaboratoryDelegator {

    @Autowired
    private ServiceConfigService serviceConfigService;

    public static final String  GOVERNANCEOFFICERS = "GovernanceOfficers";
    public static final String  GOVERNANCEOFFICERSDTO = "GovernanceOfficersDto";
    public static final String  GOVERNANCEOFFICERSDTOLIST = "GovernanceOfficersList";
    public static final String  APPSVCRELATEDINFODTO ="AppSvcRelatedInfoDto";
    public static final String  ERRORMAP_GOVERNANCEOFFICERS = "errorMap_governanceOfficers";
    public static final String  RELOADSVCDOC = "ReloadSvcDoc";
    public static final String  SERVICEPERSONNELTYPE = "ServicePersonnelType";


    //dropdown
    public static final String DROPWOWN_IDTYPESELECT = "IdTypeSelect";

    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doStart start ...."));

        //svc
        ParamUtil.setSessionAttr(bpc.request, GOVERNANCEOFFICERSDTOLIST, null);
        ParamUtil.setSessionAttr(bpc.request, ERRORMAP_GOVERNANCEOFFICERS, null);
        ParamUtil.setSessionAttr(bpc.request, RELOADSVCDOC,  null);

        //ParamUtil.setSessionAttr(bpc.request, SERVICEPERSONNELCONFIG, null);

        log.debug(StringUtil.changeForLog("the do doStart end ...."));
    }


    /**
     * StartStep: prepareJumpPage
     *
     * @param bpc
     * @throws
     */
    public void prepareJumpPage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareJumpPage start ...."));
        String actionForm = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_TAB);
        actionForm = (String)ParamUtil.getRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_TAB);
        String action = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(StringUtil.isEmpty(action)){
            action = ParamUtil.getRequestString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM);
            if(StringUtil.isEmpty(action)){
                action = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_PAGE);
            }
        }


        log.debug(StringUtil.changeForLog("The prepareJumpPage action is -->;"+action));
        String formTab = (String)ParamUtil.getRequestAttr(bpc.request,IaisEGPConstant.FORM_TAB);
        log.debug(StringUtil.changeForLog("The form_tab action is -->;"+formTab));
        //controller the step.
        if(IaisEGPConstant.YES.equals(formTab)){
            action = null;
        }
        ServiceStepDto serviceStepDto = (ServiceStepDto)ParamUtil.getSessionAttr(bpc.request,ShowServiceFormsDelegator.SERVICESTEPDTO);
        String svcId = (String) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.CURRENTSERVICEID);
        List<HcsaServiceDto> hcsaServiceDtoList= (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
        serviceStepDto = getServiceStepDto(serviceStepDto,action,hcsaServiceDtoList,svcId);
        ParamUtil.setSessionAttr(bpc.request, ShowServiceFormsDelegator.SERVICESTEPDTO, (Serializable) serviceStepDto);

        if(StringUtil.isEmpty(action)||IaisEGPConstant.YES.equals(formTab)){
            if(serviceStepDto.getCurrentStep()!=null){
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, serviceStepDto.getCurrentStep().getStepCode());
            }else{
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.LABORATORYDISCIPLINES);
            }
        }else{
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,action);
        }


        String crudActionType = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
        if(StringUtil.isEmpty(crudActionType)){
            crudActionType = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE , crudActionType);
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, crudActionType);
        log.debug(StringUtil.changeForLog("The crud_action_type  is -->;"+crudActionType));
        if(!AppServicesConsts.NAVTABS_SERVICEFORMS.equals(crudActionType)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,"jump");
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,"jump");
        }


        log.debug(StringUtil.changeForLog("the do prepareJumpPage end ...."));
    }

    private ServiceStepDto getServiceStepDto(ServiceStepDto serviceStepDto,String action,List<HcsaServiceDto> hcsaServiceDtoList,String svcId){
        //get the service information
         int serviceNum = -1;
         if(svcId!=null && hcsaServiceDtoList!=null && hcsaServiceDtoList.size() >0){
            for (int i = 0 ; i< hcsaServiceDtoList.size();i++){
                if(svcId.equals(hcsaServiceDtoList.get(i).getId())){
                  serviceNum = i;
                  break;
                }
            }
         }
        serviceStepDto.setServiceNumber(serviceNum);
         boolean serviceFirst = false;
         boolean serviceEnd =  false;
         if(serviceNum == 0){
             serviceFirst = true;
         }
         if(serviceNum+1 == hcsaServiceDtoList.size()){
             serviceEnd = true;
         }
        serviceStepDto.setServiceFirst(serviceFirst);
        serviceStepDto.setServiceEnd(serviceEnd);
        //get the step information
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = serviceStepDto.getHcsaServiceStepSchemeDtos();
        if(hcsaServiceStepSchemeDtos!=null && hcsaServiceStepSchemeDtos.size()>0){
          int number = -1;
            if(StringUtil.isEmpty(action)){
                number = 0;
            }else{
                for (int i = 0 ;i<hcsaServiceStepSchemeDtos.size();i++){
                    if(action.equals(hcsaServiceStepSchemeDtos.get(i).getStepCode())){
                        number =i;
                        break;
                    }
                }
            }
          boolean stepFirst = false;
          boolean stepEnd = false;
          if(number == 0){
              stepFirst = true;
          }
          if(number+1 == hcsaServiceStepSchemeDtos.size()){
              stepEnd = true;
          }
          serviceStepDto.setStepFirst(stepFirst);
          serviceStepDto.setStepEnd(stepEnd);
          if(number!=-1){
              //clear the old data
              serviceStepDto.setPreviousStep(null);
              serviceStepDto.setNextStep(null);
              //set the new data
              serviceStepDto.setCurrentNumber(number);
              serviceStepDto.setCurrentStep(hcsaServiceStepSchemeDtos.get(number));
              if(stepFirst){
                  if(!serviceFirst){
                      HcsaServiceDto preHcsaServiceDto =  hcsaServiceDtoList.get(serviceNum-1);
                      HcsaServiceStepSchemeDto preHcsaServiceStepSchemeDto = new HcsaServiceStepSchemeDto();
                      preHcsaServiceStepSchemeDto.setStepCode(preHcsaServiceDto.getSvcCode());
                      serviceStepDto.setPreviousStep(preHcsaServiceStepSchemeDto);
                  }
                  if(stepEnd){
                      if(!serviceEnd){
                          HcsaServiceDto nextHcsaServiceDto =  hcsaServiceDtoList.get(serviceNum+1);
                          HcsaServiceStepSchemeDto nextHcsaServiceStepSchemeDto = new HcsaServiceStepSchemeDto();
                          nextHcsaServiceStepSchemeDto.setStepCode(nextHcsaServiceDto.getSvcCode());
                          serviceStepDto.setNextStep(nextHcsaServiceStepSchemeDto);
                      }
                  }else{
                      serviceStepDto.setNextStep(hcsaServiceStepSchemeDtos.get(number+1));
                  }
              }else if(stepEnd){
                  if(stepFirst){
                      if(!serviceFirst){
                          HcsaServiceDto preHcsaServiceDto =  hcsaServiceDtoList.get(serviceNum-1);
                          HcsaServiceStepSchemeDto preHcsaServiceStepSchemeDto = new HcsaServiceStepSchemeDto();
                          preHcsaServiceStepSchemeDto.setStepCode(preHcsaServiceDto.getSvcCode());
                          serviceStepDto.setPreviousStep(preHcsaServiceStepSchemeDto);
                      }
                  }else{
                      serviceStepDto.setPreviousStep(hcsaServiceStepSchemeDtos.get(number-1));
                  }
                  if(!serviceEnd){
                      HcsaServiceDto nextHcsaServiceDto =  hcsaServiceDtoList.get(serviceNum+1);
                      HcsaServiceStepSchemeDto nextHcsaServiceStepSchemeDto = new HcsaServiceStepSchemeDto();
                      nextHcsaServiceStepSchemeDto.setStepCode(nextHcsaServiceDto.getSvcCode());
                      serviceStepDto.setNextStep(nextHcsaServiceStepSchemeDto);
                  }

              }else{
                  serviceStepDto.setPreviousStep(hcsaServiceStepSchemeDtos.get(number-1));
                  serviceStepDto.setNextStep(hcsaServiceStepSchemeDtos.get(number+1));
              }
          }
        }

        return serviceStepDto;
    }

    /**
     * StartStep: prepareLaboratoryDisciplines
     *
     * @param bpc
     * @throws
     */
    public void prepareLaboratoryDisciplines(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareLaboratoryDisciplines start ...."));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        List<HcsaSvcSubtypeOrSubsumedDto> checkList= serviceConfigService.loadLaboratoryDisciplines(currentSvcId);
        ParamUtil.setSessionAttr(bpc.request, "HcsaSvcSubtypeOrSubsumedDto", (Serializable) checkList);

        //reload
        Map<String,String> reloadChkLstMap = new HashMap<>();
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        if(appSvcRelatedInfoDto != null){
           List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
           if(appSvcLaboratoryDisciplinesDtoList != null && !appSvcLaboratoryDisciplinesDtoList.isEmpty()){
               for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                   String hciName = appSvcLaboratoryDisciplinesDto.getPremiseVal();
                   List<AppSvcChckListDto> appSvcChckListDtos =  appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                   if(appSvcChckListDtos != null && !appSvcChckListDtos.isEmpty()){
                       for(AppSvcChckListDto appSvcChckListDto:appSvcChckListDtos){
                           reloadChkLstMap.put(currentSvcId+hciName+appSvcChckListDto.getChkLstConfId(), "checked");
                       }
                   }
               }
           }
        }
        ParamUtil.setSessionAttr(bpc.request, "reloadLaboratoryDisciplines", (Serializable) reloadChkLstMap);
        log.debug(StringUtil.changeForLog("the do prepareLaboratoryDisciplines end ...."));
    }
    private void turn(List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos,Map<String,HcsaSvcSubtypeOrSubsumedDto> allCheckListMap){

        for(HcsaSvcSubtypeOrSubsumedDto dto:hcsaSvcSubtypeOrSubsumedDtos){
            allCheckListMap.put(dto.getId(),dto);
            if(dto.getList() != null && dto.getList().size()>0){
                turn(dto.getList(), allCheckListMap);
            }
        }

    }


    /**
     * StartStep: prepareGovernanceOfficers
     *
     * @param bpc
     * @throws
     */
    public void prepareGovernanceOfficers(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareGovernanceOfficers start ...."));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        int mandatoryCount = 0;
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        if(!StringUtil.isEmpty(currentSvcId)){
            //min and max count
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList  =serviceConfigService.getGOSelectInfo(currentSvcId, ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
            if(hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size()>0){
                HcsaSvcPersonnelDto hcsaSvcPersonnelDto  = hcsaSvcPersonnelList.get(0);
                mandatoryCount = hcsaSvcPersonnelDto.getMandatoryCount();
                ParamUtil.setSessionAttr(bpc.request, "HcsaSvcPersonnel", hcsaSvcPersonnelDto);
            }
        }
        if(appSvcRelatedInfoDto != null){
            List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            if(appSvcCgoDtoList != null  && appSvcCgoDtoList.size()>mandatoryCount){
                mandatoryCount = appSvcCgoDtoList.size();
            }
        }
        ParamUtil.setRequestAttr(bpc.request, "CgoMandatoryCount", mandatoryCount);
        List<SelectOption> cgoSelectList = new ArrayList<>();
        SelectOption sp0 = new SelectOption("-1", "Select Personnel");
        cgoSelectList.add(sp0);
        SelectOption sp1 = new SelectOption("newOfficer", "I'd like to add a new personnel");
        cgoSelectList.add(sp1);
        ParamUtil.setSessionAttr(bpc.request, "CgoSelectList", (Serializable) cgoSelectList);

        List<SelectOption> idTypeSelectList = getIdTypeSelOp();
        ParamUtil.setRequestAttr(bpc.request, DROPWOWN_IDTYPESELECT, idTypeSelectList);

        List<HcsaServiceDto> hcsaServiceDtoList= (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
        String currentSvcCode = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);
        List<SelectOption> specialtySelectList = genSpecialtySelectList(currentSvcCode);
        ParamUtil.setSessionAttr(bpc.request, "SpecialtySelectList", (Serializable) specialtySelectList);

        //reload
        if(appSvcRelatedInfoDto != null){
            List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            ParamUtil.setRequestAttr(bpc.request, GOVERNANCEOFFICERSDTOLIST, appSvcCgoDtoList);
        }
        log.debug(StringUtil.changeForLog("the do prepareGovernanceOfficers end ...."));
    }

    /**
     * StartStep: prepare
     *
     * @param bpc
     * @throws
     */
    public void prepareDisciplineAllocation(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareDisciplineAllocation start ...."));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        List<AppSvcLaboratoryDisciplinesDto> newChkLstDtoList = new ArrayList<>();
        for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
                if(appSvcLaboratoryDisciplinesDto.getPremiseVal().equals(appGrpPremisesDto.getConveyanceVehicleNo())){
                    appSvcLaboratoryDisciplinesDto.setPremiseGetAddress(appGrpPremisesDto.getAddress());
                }else if (appSvcLaboratoryDisciplinesDto.getPremiseVal().equals(appGrpPremisesDto.getHciName())){
                    appSvcLaboratoryDisciplinesDto.setPremiseGetAddress(appGrpPremisesDto.getAddress());
                }
            }
            newChkLstDtoList.add(appSvcLaboratoryDisciplinesDto);
        }

        ParamUtil.setSessionAttr(bpc.request, "PremisesAndChkLst", (Serializable) newChkLstDtoList);
        List<SelectOption> spList = new ArrayList<>();
        List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        SelectOption sp = null;
        if(appSvcCgoDtoList != null && !appSvcCgoDtoList.isEmpty()){
            for(AppSvcCgoDto cgo:appSvcCgoDtoList){
                sp = new SelectOption(cgo.getIdNo(), cgo.getName());
                spList.add(sp);
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "CgoSelect", (Serializable) spList);

        Map<String,String> reloadAllocation = new HashMap<>();
        List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        if(appSvcDisciplineAllocationDtoList != null && !appSvcDisciplineAllocationDtoList.isEmpty()){
            for(AppSvcDisciplineAllocationDto allocationDto:appSvcDisciplineAllocationDtoList){
                reloadAllocation.put(allocationDto.getPremiseVal()+allocationDto.getChkLstConfId(), allocationDto.getIdNo());
            }
        }
        ParamUtil.setSessionAttr(bpc.request, "ReloadAllocationMap", (Serializable) reloadAllocation);

        log.debug(StringUtil.changeForLog("the do prepareDisciplineAllocation end ...."));
    }

    /**
     * StartStep: preparePrincipalOfficers
     *
     * @param bpc
     * @throws
     */
    public void preparePrincipalOfficers(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do preparePrincipalOfficers start ...."));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        List<HcsaSvcPersonnelDto> principalOfficerConfig  =serviceConfigService.getGOSelectInfo(currentSvcId, ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
        List<HcsaSvcPersonnelDto> deputyPrincipalOfficerConfig   =serviceConfigService.getGOSelectInfo(currentSvcId, ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
        //todo
        int mandatory = 1;
        int deputyMandatory = 1;
        if(principalOfficerConfig != null && !principalOfficerConfig.isEmpty()){
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = principalOfficerConfig.get(0);
            if(hcsaSvcPersonnelDto != null){
                mandatory = hcsaSvcPersonnelDto.getMandatoryCount();
            }
        }

        if(deputyPrincipalOfficerConfig != null && !deputyPrincipalOfficerConfig.isEmpty()){
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = deputyPrincipalOfficerConfig.get(0);
            if(hcsaSvcPersonnelDto != null){
                deputyMandatory = hcsaSvcPersonnelDto.getMandatoryCount();
            }
        }

        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
        List<AppSvcPrincipalOfficersDto> principalOfficersDtos = new ArrayList<>();
        List<AppSvcPrincipalOfficersDto> deputyPrincipalOfficersDtos = new ArrayList<>();
        if(appSvcPrincipalOfficersDtos != null && ! appSvcPrincipalOfficersDtos.isEmpty()){
            for(AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto:appSvcPrincipalOfficersDtos){
                if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(appSvcPrincipalOfficersDto.getPsnType())){
                    principalOfficersDtos.add(appSvcPrincipalOfficersDto);
                }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(appSvcPrincipalOfficersDto.getPsnType())){
                    deputyPrincipalOfficersDtos.add(appSvcPrincipalOfficersDto);
                }
            }
            if(principalOfficersDtos.size() > mandatory){
                mandatory = principalOfficersDtos.size();
            }
            if(deputyPrincipalOfficersDtos.size() > deputyMandatory){
                deputyMandatory = deputyPrincipalOfficersDtos.size();
            }
        }
        //reload
        ParamUtil.setRequestAttr(bpc.request, "PrincipalOfficersMandatory", mandatory);
        ParamUtil.setRequestAttr(bpc.request, "DeputyPrincipalOfficersMandatory", deputyMandatory);
        ParamUtil.setRequestAttr(bpc.request, "ReloadPrincipalOfficers", principalOfficersDtos);
        ParamUtil.setRequestAttr(bpc.request, "ReloadDeputyPrincipalOfficers", deputyPrincipalOfficersDtos);

        List<SelectOption> IdTypeSelect = getIdTypeSelOp();
        ParamUtil.setRequestAttr(bpc.request, "IdTypeSelect", IdTypeSelect);

        List<SelectOption> assignSelectList = getAssignPrincipalOfficerSel(currentSvcId, true);
        ParamUtil.setRequestAttr(bpc.request, "PrincipalOfficersAssignSelect", assignSelectList);

        List<SelectOption> MedAlertSelectList = getMedAlertSelectList(true);
        ParamUtil.setRequestAttr(bpc.request, "MedAlertSelect", MedAlertSelectList);

        List<SelectOption> deputyFlagSelect = new ArrayList<>();
        SelectOption deputyFlagOp1 = new SelectOption("-1", NewApplicationDelegator.FIRESTOPTION);
        deputyFlagSelect.add(deputyFlagOp1);
        SelectOption deputyFlagOp2 = new SelectOption("0", "N");
        deputyFlagSelect.add(deputyFlagOp2);
        SelectOption deputyFlagOp3 = new SelectOption("1", "Y");
        deputyFlagSelect.add(deputyFlagOp3);
        ParamUtil.setRequestAttr(bpc.request, "DeputyFlagSelect", deputyFlagSelect);


        log.debug(StringUtil.changeForLog("the do preparePrincipalOfficers end ...."));
    }

    /**
     * StartStep: prepareDocuments
     *
     * @param bpc
     * @throws
     */
    public void prepareDocuments(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareDocuments start ...."));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<HcsaSvcDocConfigDto> hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(currentSvcId);
        if(hcsaSvcDocDtos != null && !hcsaSvcDocDtos.isEmpty()){
            ParamUtil.setSessionAttr(bpc.request, "serviceDocConfigDto", (Serializable) hcsaSvcDocDtos);
        }

        Map<String,AppSvcDocDto> reloadSvcDo = new HashMap<>();
        if(appSvcRelatedInfoDto != null){
            List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
            if(appSvcDocDtos != null && !appSvcDocDtos.isEmpty()){
                for(AppSvcDocDto appSvcDocDto:appSvcDocDtos){
                    reloadSvcDo.put(appSvcDocDto.getSvcDocId(), appSvcDocDto);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, RELOADSVCDOC, (Serializable) reloadSvcDo);

        log.debug(StringUtil.changeForLog("the do prepareDocuments end ...."));
    }
    /**
     * StartStep: prepareJump
     *
     * @param bpc
     * @throws
     */
    public void prepareJump(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareJump start ...."));

        log.debug(StringUtil.changeForLog("the do prepareJump end ...."));
    }

    /**
     * StartStep: prepareView
     *
     * @param bpc
     * @throws
     */
    public void prepareView(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareView start ...."));
        String svcId = ParamUtil.getMaskedString(bpc.request, "svcId");
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, svcId);
        ParamUtil.setSessionAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDto);
        List<AppSvcDisciplineAllocationDto> allocationDto = null;
        if(appSvcRelatedInfoDto != null){
            allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        }
        List<AppGrpPremisesDto> appGrpPremisesDtoList = getAppSubmissionDto(bpc.request).getAppGrpPremisesDtoList();
        Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = new HashMap<>();
        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
            List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocation = new ArrayList<>();
            String hciName = "";
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                hciName = appGrpPremisesDto.getHciName();
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                hciName = appGrpPremisesDto.getConveyanceVehicleNo();
            }

           if(!StringUtil.isEmpty(hciName) && allocationDto !=null && allocationDto.size()>0 ){
               for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:allocationDto){
                   List<AppSvcChckListDto> appSvcChckListDtoList = null;
                   if(hciName.equals(appSvcDisciplineAllocationDto.getPremiseVal())){
                       String chkLstId = appSvcDisciplineAllocationDto.getChkLstConfId();
                       String idNo = appSvcDisciplineAllocationDto.getIdNo();
                       //set chkLstName
                       List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                       if(appSvcLaboratoryDisciplinesDtoList != null && appSvcLaboratoryDisciplinesDtoList.size()>0){
                           for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                               if(hciName.equals(appSvcLaboratoryDisciplinesDto.getPremiseVal())){
                                   appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                               }
                           }
                       }
                       if(appSvcChckListDtoList != null && appSvcChckListDtoList.size()>0){
                           for(AppSvcChckListDto appSvcChckListDto:appSvcChckListDtoList){
                               if(chkLstId.equals(appSvcChckListDto.getChkLstConfId())){
                                   appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                               }
                           }
                       }
                       //set selCgoName
                       List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                       if(appSvcCgoDtoList != null && appSvcCgoDtoList.size()>0){
                           for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtoList){
                               if(idNo.equals(appSvcCgoDto.getIdNo())){
                                   appSvcDisciplineAllocationDto.setCgoSelName(appSvcCgoDto.getName());
                               }
                           }
                       }
                       reloadDisciplineAllocation.add(appSvcDisciplineAllocationDto);
                   }
               }
           }
            reloadDisciplineAllocationMap.put(hciName, reloadDisciplineAllocation);
        }
        ParamUtil.setSessionAttr(bpc.request, "reloadDisciplineAllocationMap", (Serializable) reloadDisciplineAllocationMap);



        log.debug(StringUtil.changeForLog("the do prepareView end ...."));
    }
    /**
     * StartStep: doLaboratoryDisciplines
     *
     * @param bpc
     * @throws
     */
    public void doLaboratoryDisciplines(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doLaboratoryDisciplines start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
        boolean isGetDataFromPage = NewApplicationDelegator.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_RELATED_INFORMATION, isEdit);
        if(judegCanEdit(appSubmissionDto)) {
            if (!isGetDataFromPage) {
                // when rfc not click edit
                log.debug(StringUtil.changeForLog("the do doLaboratoryDisciplines return end ...."));
                return;
            }

            if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? new HashSet<>() : appSubmissionDto.getClickEditPage();
                clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_LABORATORY);
                appSubmissionDto.setClickEditPage(clickEditPages);
            }

            AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto = null;
            Map<String, HcsaSvcSubtypeOrSubsumedDto> map = new HashMap<>();
            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = (List<HcsaSvcSubtypeOrSubsumedDto>) ParamUtil.getSessionAttr(bpc.request, "HcsaSvcSubtypeOrSubsumedDto");
            turn(hcsaSvcSubtypeOrSubsumedDtos, map);
            String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
            AppSvcRelatedInfoDto currentSvcDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            Map<String, String> reloadChkLstMap = new HashMap<>();
            Map<String, String> errorMap = new HashMap<>();
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = new ArrayList<>();
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                String premiseName = "";
                if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())) {
                    premiseName = appGrpPremisesDto.getHciName();
                } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())) {
                    premiseName = appGrpPremisesDto.getConveyanceVehicleNo();
                }
                String name = premiseName + "control--runtime--1";
                String[] checkList = ParamUtil.getStrings(bpc.request, name);
                List<AppSvcChckListDto> appSvcChckListDtoList = new ArrayList<>();
                AppSvcChckListDto appSvcChckListDto = new AppSvcChckListDto();
                if (!StringUtil.isEmpty(checkList)) {
                    for (String maskName : checkList) {
                        String checkBoxId = ParamUtil.getMaskedString(bpc.request, maskName);
                        HcsaSvcSubtypeOrSubsumedDto checkInfo = map.get(checkBoxId);

                        appSvcChckListDto = new AppSvcChckListDto();
                        appSvcChckListDto.setChkLstConfId(checkInfo.getId());
                        appSvcChckListDto.setChkLstType(checkInfo.getType());
                        appSvcChckListDto.setChkName(checkInfo.getName());
                        appSvcChckListDto.setParentName(checkInfo.getParentId());
                        appSvcChckListDto.setChildrenName(checkInfo.getChildrenId());
                        appSvcChckListDtoList.add(appSvcChckListDto);

                        //PremisesIndexNo()+checkCode()+checkParentId()
                        reloadChkLstMap.put(currentSvcId + appGrpPremisesDto.getHciName() + checkInfo.getId(), "checked");
                    }
                    String premisesType = appGrpPremisesDto.getPremisesType();
                    String premisesValue = "";
                    if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)) {
                        premisesValue = appGrpPremisesDto.getHciName();
                    } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)) {
                        premisesValue = appGrpPremisesDto.getConveyanceVehicleNo();
                    }
                    //else his .....
                    appSvcLaboratoryDisciplinesDto = new AppSvcLaboratoryDisciplinesDto();
                    appSvcLaboratoryDisciplinesDto.setPremiseType(premisesType);
                    appSvcLaboratoryDisciplinesDto.setPremiseVal(premisesValue);
                    appSvcLaboratoryDisciplinesDto.setPremiseGetAddress(appGrpPremisesDto.getAddress());
                    appSvcLaboratoryDisciplinesDto.setAppSvcChckListDtoList(appSvcChckListDtoList);
                    appSvcLaboratoryDisciplinesDtoList.add(appSvcLaboratoryDisciplinesDto);
                }
                String crud_action_type = ParamUtil.getRequestString(bpc.request, "nextStep");
                if ("next".equals(crud_action_type)) {
                    errorMap = doValidateLaboratory(appSvcChckListDtoList, currentSvcId);
                }

            }
            ParamUtil.setSessionAttr(bpc.request, "reloadLaboratoryDisciplines", (Serializable) reloadChkLstMap);
            if (!errorMap.isEmpty()) {
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.LABORATORYDISCIPLINES);
                return;
            }
            currentSvcDto.setAppSvcLaboratoryDisciplinesDtoList(appSvcLaboratoryDisciplinesDtoList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcDto);
        }
        log.debug(StringUtil.changeForLog("the do doLaboratoryDisciplines end ...."));
    }

    public static Map<String,String> doValidateLaboratory(List<AppSvcChckListDto>  listDtos,String serviceId){
        Map<String,String> map=new HashMap<>();
        int count=0;

        if(listDtos.isEmpty()){
         /*   map.put("checkError","UC_CHKLMD001_ERR002");*/
        }else {
            for(int i=0;i<listDtos.size();i++){
                String parentName = listDtos.get(i).getParentName();
                if(parentName==null){
                    count++;
                    continue;
                }else  if(listDtos.get(i).isChkLstType()){
                    if(serviceId.equals(parentName)){
                        count++;
                        continue;
                    }
                   for(AppSvcChckListDto every :listDtos) {
                       if(every.getChildrenName()!=null){
                           if(every.getChildrenName().equals(parentName)){
                               count++;
                               break;
                           }
                       }

                   }
                }
                else if(!listDtos.get(i).isChkLstType()){
                    for(AppSvcChckListDto every :listDtos) {
                            if (every.getChkLstConfId().equals(parentName)) {
                                count++;
                                break;

                        }
                    }
                }
            }

        }
        if(count!=listDtos.size()){
            map.put("checkError","UC_CHKLMD001_ERR002");
        }

        return map;

    }

    /**
     * StartStep: doGovernanceOfficers
     *
     * @param bpc
     * @throws
     */
    public void doGovernanceOfficers(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doGovernanceOfficers start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
        boolean isGetDataFromPage = NewApplicationDelegator.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_RELATED_INFORMATION, isEdit);
        if(judegCanEdit(appSubmissionDto)) {
            if (!isGetDataFromPage) {
                // when rfc not click edit
                log.debug(StringUtil.changeForLog("the do doLaboratoryDisciplines return end ...."));
                return;
            }
            List<AppSvcCgoDto> appSvcCgoDtoList = genAppSvcCgoDto(bpc.request);
            if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? new HashSet<>() : appSubmissionDto.getClickEditPage();
                clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_GOVERNANCE_OFFICERS);
                appSubmissionDto.setClickEditPage(clickEditPages);
            }
            //do validate
            Map<String, String> errList = new HashMap<>();


            String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
            AppSvcRelatedInfoDto currentSvcRelatedDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
            currentSvcRelatedDto.setAppSvcCgoDtoList(appSvcCgoDtoList);

            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto);
            String crud_action_additional = bpc.request.getParameter("nextStep");
            if ("next".equals(crud_action_additional)) {
                errList = doValidateGovernanceOfficers(bpc.request);
            }
            if (!errList.isEmpty()) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.GOVERNANCEOFFICERS);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errList));
                return;
            }

        }
        log.debug(StringUtil.changeForLog("the do doGovernanceOfficers end ...."));
    }


    /**
     * StartStep: doDisciplineAllocation
     *
     * @param bpc
     * @throws
     */
    public void doDisciplineAllocation(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doDisciplineAllocation start ...."));
        Map<String, String> errorMap = new HashMap<>();
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        boolean isGetDataFromPage = NewApplicationDelegator.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_RELATED_INFORMATION, AppConsts.YES);
        if (judegCanEdit(appSubmissionDto)) {
            if (!isGetDataFromPage) {
                // when rfc not click edit
                log.debug(StringUtil.changeForLog("the do doDisciplineAllocation return end ...."));
                return;
            }
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
            AppSvcRelatedInfoDto currentSvcRelatedDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
            List<AppSvcDisciplineAllocationDto> daList = new ArrayList<>();
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = currentSvcRelatedDto.getAppSvcLaboratoryDisciplinesDtoList();
            if (appSvcLaboratoryDisciplinesDtoList != null) {
                for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList) {
                    String premisesType = "";
                    String premisesValue = "";
                    for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                        if (appSvcLaboratoryDisciplinesDto.getPremiseVal().equals(appGrpPremisesDto.getConveyanceVehicleNo())) {
                            premisesType = appGrpPremisesDto.getPremisesType();
                            premisesValue = appGrpPremisesDto.getConveyanceVehicleNo();
                            break;
                        } else if (appSvcLaboratoryDisciplinesDto.getPremiseVal().equals(appGrpPremisesDto.getHciName())) {
                            premisesType = appGrpPremisesDto.getPremisesType();
                            premisesValue = appGrpPremisesDto.getHciName();
                            break;
                        }
                    }
                    AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto = null;
                    int chkLstSize = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList().size();
                    for (int i = 0; i < chkLstSize; i++) {
                        StringBuilder chkAndCgoName = new StringBuilder()
                                .append(premisesValue)
                                .append(i);
                        String[] chkAndCgoValue = ParamUtil.getStrings(bpc.request, chkAndCgoName.toString());
                        if (chkAndCgoValue != null && chkAndCgoValue.length > 0) {
                            appSvcDisciplineAllocationDto = new AppSvcDisciplineAllocationDto();
                            appSvcDisciplineAllocationDto.setPremiseType(premisesType);
                            appSvcDisciplineAllocationDto.setPremiseVal(premisesValue);
                            appSvcDisciplineAllocationDto.setChkLstConfId(chkAndCgoValue[0]);
                            appSvcDisciplineAllocationDto.setIdNo(chkAndCgoValue[1]);
                            daList.add(appSvcDisciplineAllocationDto);
                        }
                    }
                }
            }

            String crud_action_additional = ParamUtil.getRequestString(bpc.request, "nextStep");
            if ("next".equals(crud_action_additional)) {
                doValidateDisciplineAllocation(errorMap, daList);
            }

            if (!errorMap.isEmpty()) {
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.DISCIPLINEALLOCATION);
                return;
            }

            //save into sub-svc dto
            currentSvcRelatedDto.setAppSvcDisciplineAllocationDtoList(daList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto);
        }

        log.debug(StringUtil.changeForLog("the do doDisciplineAllocation end ...."));

    }

    private void doValidateDisciplineAllocation(Map<String ,String> map, List<AppSvcDisciplineAllocationDto> daList){
        for(int i=0;i< daList.size();i++){
            String idNo = daList.get(i).getIdNo();
            if(StringUtil.isEmpty(idNo)){
                map.put("disciplineAllocation"+i,"UC_CHKLMD001_ERR002");
            }
        }
    }
    /**
     * StartStep: doPrincipalOfficers
     *
     * @param bpc
     * @throws
     */
    public void doPrincipalOfficers(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doPrincipalOfficers start ...."));

        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        if (judegCanEdit(appSubmissionDto)) {
            String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
            String isEditDpo = ParamUtil.getString(bpc.request, "isEditDpo");
            boolean isGetDataFromPagePo = NewApplicationDelegator.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_RELATED_INFORMATION, isEdit);
            boolean isGetDataFromPageDpo = NewApplicationDelegator.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_RELATED_INFORMATION, isEditDpo);
            if (!isGetDataFromPagePo && !isGetDataFromPageDpo) {
                // when rfc not click edit
                log.debug(StringUtil.changeForLog("the do doPrincipalOfficers return end ...."));
                return;
            }
            List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = genAppSvcPrincipalOfficersDto(bpc.request, isGetDataFromPagePo, isGetDataFromPageDpo);
            ParamUtil.setSessionAttr(bpc.request, "AppSvcPrincipalOfficersDto", (Serializable) appSvcPrincipalOfficersDtoList);
            if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())) {
                appSubmissionDto = setPrincipalOfficersClickEditPage(appSubmissionDto, isGetDataFromPagePo, isGetDataFromPageDpo);
                ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
            }
            Map<String, String> map = new HashMap<>();
            String crud_action_additional = ParamUtil.getRequestString(bpc.request, "nextStep");


            String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
            appSvcRelatedInfoDto.setAppSvcPrincipalOfficersDtoList(appSvcPrincipalOfficersDtoList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto);

            if ("next".equals(crud_action_additional)) {
                map = NewApplicationDelegator.doValidatePo(bpc.request);
            }
            if (!map.isEmpty()) {
                //ParamUtil.setSessionAttr(bpc.request, "", );
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, HcsaLicenceFeConstant.PRINCIPALOFFICERS);
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(map));
                return;
            }
        }
        
        log.debug(StringUtil.changeForLog("the do doPrincipalOfficers end ...."));
    }
    
    /**
     * StartStep: doDocuments
     *
     * @param bpc
     * @throws
     */
    public void doDocuments(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do doDocuments start ...."));
        List<AppSvcDocDto> appSvcDocDtoList = new ArrayList<>();
        AppSvcDocDto appSvcDocDto = null;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String crudActionType =  mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
        String crudActionTypeTab =  mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE_TAB);
        String crudActionTypeForm = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE_FORM);
        String crudActionTypeFormPage =  mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE_FORM_PAGE);
        String formTab = mulReq.getParameter(IaisEGPConstant.FORM_TAB);

        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,crudActionType);
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE,crudActionValue);
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_TAB,crudActionTypeTab);
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM,crudActionTypeForm);
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_PAGE,crudActionTypeFormPage);
        ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.FORM_TAB,formTab);
        if(!StringUtil.isEmpty(crudActionTypeFormPage)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,crudActionTypeFormPage);
        }else{
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "jump");
        }
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        if(judegCanEdit(appSubmissionDto)){
            boolean isGetDataFromPage = NewApplicationDelegator.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SUPPORTING_DOCUMENT, AppConsts.YES);
            if(!isGetDataFromPage){
                log.debug(StringUtil.changeForLog("the do doDocuments return end ...."));
                return;
            }
            Map<String,AppSvcDocDto> beforeReloadDocMap = (Map<String, AppSvcDocDto>) ParamUtil.getSessionAttr(bpc.request, RELOADSVCDOC);
            List<HcsaSvcDocConfigDto> svcDocConfigDtos = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, "serviceDocConfigDto");
            Map<String,CommonsMultipartFile> commonsMultipartFileMap = new HashMap<>();
            CommonsMultipartFile file = null;
            if(svcDocConfigDtos != null && !svcDocConfigDtos.isEmpty()){
                for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:svcDocConfigDtos){
                    String docConfigId = hcsaSvcDocConfigDto.getId();
                    String delFlag = docConfigId+"flag";
                    String delFlagValue =  mulReq.getParameter(delFlag);
                    String fileName = docConfigId + "selectedFile";
                    file = (CommonsMultipartFile) mulReq.getFile(fileName);
                    if(file != null && file.getSize() != 0) {
                        if (!StringUtil.isEmpty(file.getOriginalFilename())) {
                            file.getFileItem().setFieldName("selectedFile");
                            appSvcDocDto = new AppSvcDocDto();
                            appSvcDocDto.setSvcDocId(docConfigId);
                            appSvcDocDto.setDocName(file.getOriginalFilename());
                            long size = file.getSize()/1024;
                            appSvcDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                            String md5Code = FileUtil.genMd5FileChecksum(file.getBytes());
                            appSvcDocDto.setMd5Code(md5Code);
                            commonsMultipartFileMap.put(docConfigId, file);
                            //wait api change to get fileRepoId
                            appSvcDocDtoList.add(appSvcDocDto);
                        }
                    }else if("N".equals(delFlagValue)){
                        AppSvcDocDto beforeDto =  beforeReloadDocMap.get(docConfigId);
                        if(beforeDto != null){
                            appSvcDocDtoList.add(beforeDto);
                        }
                    }

                }
            }

            if( commonsMultipartFileMap!= null && commonsMultipartFileMap.size()>0){
                for(AppSvcDocDto appSvcDocDto1:appSvcDocDtoList){
                    String key = appSvcDocDto1.getSvcDocId();
                    CommonsMultipartFile commonsMultipartFile = commonsMultipartFileMap.get(key);
                    if(commonsMultipartFile != null && commonsMultipartFile.getSize() != 0){
                        String fileRepoGuid = serviceConfigService.saveFileToRepo(commonsMultipartFile);
                        appSvcDocDto1.setFileRepoId(fileRepoGuid);
                    }
                }
            }

        }



        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtoList);
        setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto);
        log.debug(StringUtil.changeForLog("the do doDocuments end ...."));
    }

    /**
     * StartStep: doSaveDraft
     *
     * @param bpc
     * @throws
     */
    public void doSaveDraft(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doSaveDraft start ...."));
        log.info("The ClinicalLaboratoryDelegator doSaveDraft ... ");
        log.debug(StringUtil.changeForLog("the do doSaveDraft end ...."));
    }

    /**
     * StartStep: doSubmit
     *
     * @param bpc
     * @throws
     */
    public void doSubmit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doSubmit start ...."));
        log.info("The ClinicalLaboratoryDelegator doSubmit ... ");
        log.debug(StringUtil.changeForLog("the do doSubmit end ...."));
    }

    /**
     * StartStep: prepareResult
     *
     * @param bpc
     * @throws
     */
    public void prepareResult(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareResult start ...."));
        String crudActionValue = (String)ParamUtil.getRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if(StringUtil.isEmpty(crudActionValue)){
            crudActionValue = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        }
        if("saveDraft".equals(crudActionValue)){
            ParamUtil.setRequestAttr(bpc.request,"Switch2","saveDraft");
        }else{
            ParamUtil.setRequestAttr(bpc.request,"Switch2","jumPage");
        }
        log.debug(StringUtil.changeForLog("the do prepareResult end ...."));
    }

    /**
     * StartStep: prepareResult
     *
     * @param bpc
     * @throws
     */
    public void prepareServicePersonnel(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do prepareServicePersonnel start ...."));
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        String currentSvcCod = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList  =serviceConfigService.getGOSelectInfo(currentSvcId, ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL);
        int mandatory = 0;
        if(hcsaSvcPersonnelList != null && !hcsaSvcPersonnelList.isEmpty()){
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto = hcsaSvcPersonnelList.get(0);
            if(hcsaSvcPersonnelDto != null){
                mandatory = hcsaSvcPersonnelDto.getMandatoryCount();
            }
        }

        //reload
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<AppSvcPersonnelDto> appSvcPersonnelDtos = appSvcRelatedInfoDto.getAppSvcPersonnelDtoList();
        if(appSvcPersonnelDtos != null && !appSvcPersonnelDtos.isEmpty()){
            if(appSvcPersonnelDtos.size() > mandatory){
                mandatory = appSvcPersonnelDtos.size();
            }
            ParamUtil.setRequestAttr(bpc.request, "AppSvcPersonnelDtoList", appSvcPersonnelDtos);
        }

        ParamUtil.setRequestAttr(bpc.request, "ServicePersonnelMandatory", mandatory);


        List<SelectOption> personnelTypeSel = genPersonnelTypeSel(currentSvcCod);
        ParamUtil.setRequestAttr(bpc.request, SERVICEPERSONNELTYPE, personnelTypeSel);


        List<SelectOption> designation = new ArrayList<>();
        SelectOption designationOp1 = new SelectOption("Diagnostic radiographer", "Diagnostic radiographer");
        SelectOption designationOp2 = new SelectOption("Radiation therapist", "Radiation therapist");
        SelectOption designationOp3 = new SelectOption("Nuclear Medicine Technologist", "Nuclear Medicine Technologist");
        designation.add(designationOp1);
        designation.add(designationOp2);
        designation.add(designationOp3);
        ParamUtil.setSessionAttr(bpc.request, "NuclearMedicineImagingDesignation", (Serializable) designation);


        log.debug(StringUtil.changeForLog("the do prepareServicePersonnel end ...."));
    }

    /**
     * StartStep: prepareResult
     *
     * @param bpc
     * @throws
     */
    public void doServicePersonnel (BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doServicePersonnel start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String isEdit = ParamUtil.getString(bpc.request, NewApplicationDelegator.IS_EDIT);
        boolean isGetDataFromPage = NewApplicationDelegator.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SERVICE_RELATED_INFORMATION, isEdit);
        if(!isGetDataFromPage){

            log.debug(StringUtil.changeForLog("the do doServicePersonnel return end ...."));
            return;
        }

        Map<String ,String >errorMap=new HashMap<>();
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        String currentSvcCod = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);

        if(judegCanEdit(appSubmissionDto)){
            List<AppSvcPersonnelDto> appSvcPersonnelDtos = new ArrayList<>();
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
            List<String> personnelTypeList = new ArrayList<>();
            List<SelectOption> personnelTypeSel = genPersonnelTypeSel(currentSvcCod);
            for(SelectOption sp:personnelTypeSel){
                personnelTypeList.add(sp.getValue());
            }

            appSvcPersonnelDtos = genAppSvcPersonnelDtoList(bpc.request, personnelTypeList);
            appSvcRelatedInfoDto.setAppSvcPersonnelDtoList(appSvcPersonnelDtos);

       /* if(AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(currentSvcCod)){

        }else if(AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(currentSvcCod)){

        }else if(AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(currentSvcCod)){
            //:todo
        }else if(AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(currentSvcCod)){
            //:todo
        }*/

            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto);

            String nextStep = ParamUtil.getRequestString(bpc.request, "nextStep");
            if(!StringUtil.isEmpty(nextStep)){
                doValidatetionServicePerson(errorMap,appSvcPersonnelDtos);
            }
            if(!errorMap.isEmpty()){
                ParamUtil.setRequestAttr(bpc.request,"errorMsg",WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,HcsaLicenceFeConstant.NUCLEARMEDICINEIMAGING);
                return;
            }
        }


        log.debug(StringUtil.changeForLog("the do doServicePersonnel end ...."));
    }

    private static void doValidatetionServicePerson(Map <String,String> errorMap,List<AppSvcPersonnelDto> appSvcPersonnelDtos){

        for(int i=0;i<appSvcPersonnelDtos.size();i++){
            String personnelSel = appSvcPersonnelDtos.get(i).getPersonnelType();
            if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE.equals(personnelSel)){
                String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                String name = appSvcPersonnelDtos.get(i).getName();
                if(StringUtil.isEmpty(name)){
                    errorMap.put("name"+i,"UC_CHKLMD001_ERR001");
                }
                if(StringUtil.isEmpty(profRegNo)){
                    errorMap.put("regnNo"+i,"UC_CHKLMD001_ERR001");
                }
            }
            if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(personnelSel)){
                String name = appSvcPersonnelDtos.get(i).getName();
                String designation = appSvcPersonnelDtos.get(i).getDesignation();
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                String qualification = appSvcPersonnelDtos.get(i).getQuaification();

                if(StringUtil.isEmpty(name)){
                    errorMap.put("name"+i,"UC_CHKLMD001_ERR001");
                }
                if(StringUtil.isEmpty(designation)){
                    errorMap.put("designation"+i,"UC_CHKLMD001_ERR001");
                }
                if(StringUtil.isEmpty(wrkExpYear)){
                    errorMap.put("wrkExpYear"+i,"UC_CHKLMD001_ERR001");
                }else {
                    if(!wrkExpYear.matches("^[0-9]*$")){
                        errorMap.put("wrkExpYear"+i,"CHKLMD001_ERR003");
                    }
                }
                if(StringUtil.isEmpty(qualification)){
                    errorMap.put("qualification"+i,"UC_CHKLMD001_ERR001");
                }
            }

            if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(personnelSel)){
                String name = appSvcPersonnelDtos.get(i).getName();
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                String quaification = appSvcPersonnelDtos.get(i).getQuaification();
                if(StringUtil.isEmpty(name)){
                    errorMap.put("name"+i,"UC_CHKLMD001_ERR001");
                }
                if(StringUtil.isEmpty(wrkExpYear)){
                    errorMap.put("wrkExpYear"+i,"UC_CHKLMD001_ERR001");
                }
                else {
                    if(!wrkExpYear.matches("^[0-9]*$")){
                        errorMap.put("wrkExpYear"+i,"CHKLMD001_ERR003");
                    }
                }
                if(StringUtil.isEmpty(quaification)){
                    errorMap.put("quaification"+i,"UC_CHKLMD001_ERR001");
                }
            }
            if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER.equals(personnelSel)){
                String name = appSvcPersonnelDtos.get(i).getName();
                if(StringUtil.isEmpty(name)){
                    errorMap.put("name"+i,"UC_CHKLMD001_ERR001");
                }
            }

        }

    }

    /**
     * ajax
     */
    public void loadGovernanceOfficerByCGOId(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do loadGovernanceOfficerByCGOId start ...."));
        //
        String cgoId = bpc.request.getParameter("");
        AppSvcCgoDto governanceOfficersDto = serviceConfigService.loadGovernanceOfficerByCgoId(cgoId);
        ParamUtil.setSessionAttr(bpc.request, GOVERNANCEOFFICERSDTO, governanceOfficersDto);
        log.debug(StringUtil.changeForLog("the do loadGovernanceOfficerByCGOId end ...."));
    }

    @RequestMapping(value = "/governance-officer-html", method = RequestMethod.GET)
    public @ResponseBody String genGovernanceOfficerHtmlList(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("gen governance officer html start ...."));
        String sql = SqlMap.INSTANCE.getSql("governanceOfficer", "generateGovernanceOfficerHtml").getSqlStr();

        //assign cgo select
        List<SelectOption> cgoSelectList= (List) ParamUtil.getSessionAttr(request, "CgoSelectList");
        Map<String,String> cgoSelectAttr = new HashMap<>();
        cgoSelectAttr.put("class", "assignSel");
        cgoSelectAttr.put("name", "assignSelect");
        cgoSelectAttr.put("style", "display: none;");
        String cgoSelectStr = NewApplicationDelegator.generateDropDownHtml(cgoSelectAttr, cgoSelectList, null);

        //salutation
        List<SelectOption> salutationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
        Map<String,String> salutationAttr = new HashMap<>();
        salutationAttr.put("name", "salutation");
        salutationAttr.put("style", "display: none;");
        String salutationSelectStr = NewApplicationDelegator.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION);

        //ID Type
        List<SelectOption> idTypeList = getIdTypeSelOp();
        Map<String,String>  idTypeAttr = new HashMap<>();
        idTypeAttr.put("name", "idType");
        idTypeAttr.put("style", "display: none;");
        String idTypeSelectStr = NewApplicationDelegator.generateDropDownHtml(idTypeAttr, idTypeList, null);

        //Designation
        List<SelectOption> designationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DESIGNATION);
        Map<String,String> designationAttr = new HashMap<>();
        designationAttr.put("name", "designation");
        designationAttr.put("style", "display: none;");
        String designationSelectStr = NewApplicationDelegator.generateDropDownHtml(designationAttr, designationList, NewApplicationDelegator.FIRESTOPTION);

        //Professional Regn Type
        List<SelectOption> proRegnTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PROFESSIONAL_TYPE);
        Map<String,String> proRegnTypeAttr = new HashMap<>();
        proRegnTypeAttr.put("name", "professionType");
        proRegnTypeAttr.put("style", "display: none;");
        String proRegnTypeSelectStr = NewApplicationDelegator.generateDropDownHtml(proRegnTypeAttr, proRegnTypeList, NewApplicationDelegator.FIRESTOPTION);

        //Specialty
        List<SelectOption> specialtyList = (List<SelectOption>) ParamUtil.getSessionAttr(request, "SpecialtySelectList");
        Map<String,String> specialtyAttr = new HashMap<>();
        specialtyAttr.put("name", "specialty");
        specialtyAttr.put("class", "specialty");
        specialtyAttr.put("style", "display: none;");
        String specialtySelectStr = NewApplicationDelegator.generateDropDownHtml(specialtyAttr, specialtyList, null);



        sql = sql.replace("(1)", cgoSelectStr);
        sql = sql.replace("(2)", salutationSelectStr);
        sql = sql.replace("(3)", idTypeSelectStr);
        sql = sql.replace("(4)", designationSelectStr);
        sql = sql.replace("(5)", proRegnTypeSelectStr);
        sql = sql.replace("(6)", specialtySelectStr);



        log.debug(StringUtil.changeForLog("gen governance officer html end ...."));
        return sql;
    }

    /**
     * @param
     * @description: ajax
     * @author: zixia
     */
    @RequestMapping(value = "/psn-info", method = RequestMethod.GET)
    public @ResponseBody AppSvcCgoDto getPsnInfoByIdNo (HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("getPsnInfoByIdNo start ...."));
        String idNo = ParamUtil.getRequestString(request, "idNo");
        AppSvcCgoDto appSvcCgoDto =new AppSvcCgoDto();
        if(StringUtil.isEmpty(idNo)){
            return appSvcCgoDto;
        }
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtoList){
                List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                if(!IaisCommonUtils.isEmpty(appSvcCgoDtoList)){
                    appSvcCgoDto = isExistIdNo(appSvcCgoDtoList, idNo);
                    if(appSvcCgoDto != null){
                        break;
                    }
                }
            }
        }
        log.debug(StringUtil.changeForLog("getPsnInfoByIdNo end ...."));
        return  appSvcCgoDto;
    }

    private AppSvcCgoDto isExistIdNo(List<AppSvcCgoDto> appSvcCgoDtoList, String idNo){
        for (AppSvcCgoDto appSvcCgoDto:appSvcCgoDtoList){
            if(idNo.equals(appSvcCgoDto.getIdNo())){
                log.info(StringUtil.changeForLog("had matching dto"));
                return appSvcCgoDto;
            }
        }
        return  null;
    }

    private List<AppSvcCgoDto> genAppSvcCgoDto(HttpServletRequest request){
        ParamUtil.setSessionAttr(request, ERRORMAP_GOVERNANCEOFFICERS,null);
        List<AppSvcCgoDto> appSvcCgoDtoList = new ArrayList<>();
        AppSvcCgoDto appSvcCgoDto = null;

        String[] assignSelect = ParamUtil.getStrings(request, "assignSelect");
        int size = 0 ;
        if(assignSelect != null && assignSelect.length>0){
            size = assignSelect.length;
        }
        String[] salutation = ParamUtil.getStrings(request, "salutation");
        String[] name = ParamUtil.getStrings(request, "name");
        String[] idType = ParamUtil.getStrings(request, "idType");
        String[] idNo = ParamUtil.getStrings(request, "idNo");
        String[] designation = ParamUtil.getStrings(request, "designation");
        String[] professionType = ParamUtil.getStrings(request, "professionType");
        String[] professionRegoNo = ParamUtil.getStrings(request, "professionRegoNo");
        String[] specialty = ParamUtil.getStrings(request, "specialty");
        String[] specialtyOther = ParamUtil.getStrings(request, "specialtyOther");
        String[] qualification = ParamUtil.getStrings(request, "qualification");
        String[] mobileNo = ParamUtil.getStrings(request, "mobileNo");
        String[] emailAddress = ParamUtil.getStrings(request, "emailAddress");
        for(int i = 0; i<size; i++){
            appSvcCgoDto = new AppSvcCgoDto();
            //cgoIndexNo
            String cgoIndexNo = new StringBuffer().append("cgo-").append(i).append("-").toString();
            appSvcCgoDto.setAssignSelect(assignSelect[i]);
            appSvcCgoDto.setSalutation(salutation[i]);
            appSvcCgoDto.setName(name[i]);
            appSvcCgoDto.setIdType(idType[i]);
            appSvcCgoDto.setIdNo(idNo[i]);
            appSvcCgoDto.setDesignation(designation[i]);
            appSvcCgoDto.setProfessionType(professionType[i]);
            appSvcCgoDto.setProfessionRegoNo(professionRegoNo[i]);
            String specialtyStr = specialty[i];
            appSvcCgoDto.setSpeciality(specialtyStr);
            if("other".equals(specialtyStr)){
                appSvcCgoDto.setSpecialityOther(specialtyOther[i]);
            }
            //qualification
            appSvcCgoDto.setQualification(qualification[i]);
            appSvcCgoDto.setMobileNo(mobileNo[i]);
            appSvcCgoDto.setEmailAddr(emailAddress[i]);
            appSvcCgoDto.setCgoIndexNo(cgoIndexNo);
            appSvcCgoDtoList.add(appSvcCgoDto);
        }
        ParamUtil.setSessionAttr(request, GOVERNANCEOFFICERSDTOLIST, (Serializable) appSvcCgoDtoList);
        return appSvcCgoDtoList;
    }



    private List<AppSvcPrincipalOfficersDto> genAppSvcPrincipalOfficersDto(HttpServletRequest request, Boolean isGetDataFromPagePo, Boolean isGetDataFromPageDpo){
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = new ArrayList<>();
        String deputySelect = ParamUtil.getString(request, "deputyPrincipalOfficer");
        if(isGetDataFromPagePo){
            String [] assignSelect = ParamUtil.getStrings(request, "assignSelect");
            String [] salutation = ParamUtil.getStrings(request, "salutation");
            String [] name = ParamUtil.getStrings(request, "name");
            String [] idType = ParamUtil.getStrings(request, "idType");
            String [] idNo = ParamUtil.getStrings(request, "idNo");
            String [] designation = ParamUtil.getStrings(request, "designation");
            String [] mobileNo = ParamUtil.getStrings(request, "mobileNo");
            String [] officeTelNo = ParamUtil.getStrings(request, "officeTelNo");
            String [] emailAddress = ParamUtil.getStrings(request, "emailAddress");
            if(assignSelect!=null && assignSelect.length>0){
                for(int i=0 ;i<assignSelect.length;i++){
                    AppSvcPrincipalOfficersDto poDto = new AppSvcPrincipalOfficersDto();
                    poDto.setAssignSelect(assignSelect[i]);
                    poDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                    poDto.setSalutation(salutation[i]);
                    poDto.setName(name[i]);
                    poDto.setIdType(idType[i]);
                    poDto.setIdNo(idNo[i]);
                    poDto.setOfficeTelNo(officeTelNo[i]);
                    poDto.setDesignation(designation[i]);
                    poDto.setMobileNo(mobileNo[i]);
                    poDto.setEmailAddr(emailAddress[i]);
                    appSvcPrincipalOfficersDtos.add(poDto);
                }
            }
        }

        //depo
        if("1".equals(deputySelect) && isGetDataFromPageDpo){
            String [] deputySalutation = ParamUtil.getStrings(request, "deputySalutation");
            String [] deputyDesignation = ParamUtil.getStrings(request, "deputyDesignation");
            String [] deputyName = ParamUtil.getStrings(request, "deputyName");
            String [] deputyIdType = ParamUtil.getStrings(request, "deputyIdType");
            String [] deputyIdNo = ParamUtil.getStrings(request, "deputyIdNo");
            String [] deputyMobileNo = ParamUtil.getStrings(request, "deputyMobileNo");
            String [] modeOfMedAlert = ParamUtil.getStrings(request, "modeOfMedAlert");
            String [] deputyEmailAddr = ParamUtil.getStrings(request, "deputyEmailAddr");
            if(deputyDesignation != null && deputyDesignation.length>0){
                for(int i=0 ;i <deputyDesignation.length;i++){
                    AppSvcPrincipalOfficersDto dpoDto = new AppSvcPrincipalOfficersDto();
                    dpoDto.setPsnType(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
                    dpoDto.setDesignation(deputyDesignation[i]);
                    dpoDto.setEmailAddr(deputyEmailAddr[i]);
                    dpoDto.setSalutation(deputySalutation[i]);
                    dpoDto.setName(deputyName[i]);
                    dpoDto.setIdType(deputyIdType[i]);
                    dpoDto.setIdNo(deputyIdNo[i]);
                    dpoDto.setMobileNo(deputyMobileNo[i]);
                    dpoDto.setModeOfMedAlert(modeOfMedAlert[i]);
                    appSvcPrincipalOfficersDtos.add(dpoDto);
                }
            }
        }

        return  appSvcPrincipalOfficersDtos;
    }

    private AppSvcRelatedInfoDto getAppSvcRelatedInfoDto(HttpServletRequest request){
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = (AppSvcRelatedInfoDto) ParamUtil.getSessionAttr(request, APPSVCRELATEDINFODTO);
        if(appSvcRelatedInfoDto == null){
            appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
        }
        return appSvcRelatedInfoDto;
    }

    private AppSubmissionDto getAppSubmissionDto(HttpServletRequest request){
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, NewApplicationDelegator.APPSUBMISSIONDTO);
        if(appSubmissionDto == null){
            appSubmissionDto = new AppSubmissionDto();
        }

        return appSubmissionDto;
    }

    public static Map<String,String> doValidateGovernanceOfficers(HttpServletRequest request){
        List<AppSvcCgoDto> appSvcCgoList = (List<AppSvcCgoDto>) ParamUtil.getSessionAttr(request, GOVERNANCEOFFICERSDTOLIST);
        if(appSvcCgoList == null){
            return new HashMap<>();
        }

        Map<String,String> errMap = new HashMap<>();
        StringBuilder stringBuilder =new StringBuilder();
        for(int i=0;i<appSvcCgoList.size();i++ ){
            StringBuilder stringBuilder1=new StringBuilder();
            String assignSelect = appSvcCgoList.get(i).getAssignSelect();
            if("-1".equals(assignSelect)){
                errMap.put("assignSelect"+i, "UC_CHKLMD001_ERR002");
            }else {
                String idTyp = appSvcCgoList.get(i).getIdType();
                if("-1".equals(idTyp)){
                    errMap.put("idTyp"+i, "UC_CHKLMD001_ERR002");
                }
                String salutation = appSvcCgoList.get(i).getSalutation();
                if(StringUtil.isEmpty(salutation)){
                    errMap.put("salutation"+i,"UC_CHKLMD001_ERR001");
                }
                String speciality = appSvcCgoList.get(i).getSpeciality();
                if("-1".equals(speciality)){
                    errMap.put("speciality"+i,"UC_CHKLMD001_ERR002");
                }
                String professionType = appSvcCgoList.get(i).getProfessionType();
                if(StringUtil.isEmpty(professionType)){
                    errMap.put("professionType"+i,"UC_CHKLMD001_ERR002");
                }
                String designation = appSvcCgoList.get(i).getDesignation();
                if(StringUtil.isEmpty(designation)){
                    errMap.put("designation"+i,"UC_CHKLMD001_ERR001");
                }
                String professionRegoNo = appSvcCgoList.get(i).getProfessionRegoNo();
                if(StringUtil.isEmpty(professionRegoNo)){
                    errMap.put("professionRegoNo"+i,"UC_CHKLMD001_ERR001");
                }
                String idNo = appSvcCgoList.get(i).getIdNo();
                //to do
                if(StringUtil.isEmpty(idNo)){
                    errMap.put("idNo"+i,"UC_CHKLMD001_ERR001");
                }else {
                    if("FIN".equals(idTyp)){
                        boolean b = SgNoValidator.validateFin(idNo);
                        if(!b){
                            errMap.put("idNo"+i,"CHKLMD001_ERR005");
                        }
                        stringBuilder1.append(idTyp).append(idNo);

                    }
                    if("NRIC".equals(idTyp)){
                        boolean b1 = SgNoValidator.validateNric(idNo);
                        if(!b1){
                            errMap.put("idNo"+i,"CHKLMD001_ERR005");
                        }
                            stringBuilder1.append(idTyp).append(idNo);

                    }

                }
                //to do

                String Specialty = appSvcCgoList.get(i).getSpeciality();
                if (StringUtil.isEmpty(Specialty)) {
                    errMap.put("speciality"+i, "UC_CHKLMD001_ERR002");
                }

                String specialty = appSvcCgoList.get(i).getSpeciality();
                if(StringUtil.isEmpty(specialty)){
                    errMap.put("specialty"+i, "UC_CHKLMD001_ERR001");
                }
                String name = appSvcCgoList.get(i).getName();
                if(StringUtil.isEmpty(name)){
                    errMap.put("name"+i,"UC_CHKLMD001_ERR001");
                }

                String mobileNo = appSvcCgoList.get(i).getMobileNo();
                if(StringUtil.isEmpty(mobileNo)){
                    errMap.put("mobileNo"+i, "UC_CHKLMD001_ERR001");
                }else if (!StringUtil.isEmpty(mobileNo)) {
                    if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                        errMap.put("mobileNo"+i, "CHKLMD001_ERR004");
                    }
                }
                String emailAddr = appSvcCgoList.get(i).getEmailAddr();
                if(StringUtil.isEmpty(emailAddr)){
                    errMap.put("emailAddr"+i, "UC_CHKLMD001_ERR001");
                }else if (!StringUtil.isEmpty(emailAddr)) {
                    if (!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                        errMap.put("emailAddr"+i, "CHKLMD001_ERR006");
                    }
                }
                String s = stringBuilder.toString();
                if(!StringUtil.isEmpty(stringBuilder1.toString())){
                    if(s.contains(stringBuilder1.toString())){
                        errMap.put("idNo","UC_CHKLMD001_ERR002");
                    }else {
                        stringBuilder.append(stringBuilder1.toString());
                    }
                }

            }

        }
        return errMap;
    }


    public ClinicalOfficerValidateDto getValueFromPage(HttpServletRequest request){
        ClinicalOfficerValidateDto dto =new ClinicalOfficerValidateDto();
        String goveOffice = request.getParameter("pageCon");

        return dto;
    }

    private void chose(HttpServletRequest request,String type){
        if("goveOffice".equals(type)){
            List<AppSvcCgoDto> appSvcCgoList = (List<AppSvcCgoDto>) ParamUtil.getSessionAttr(request, GOVERNANCEOFFICERSDTOLIST);
            ParamUtil.setRequestAttr(request,"goveOffice",appSvcCgoList);
        }
        if("checkBox".equals(type)){
            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = (List<HcsaSvcSubtypeOrSubsumedDto>) ParamUtil.getSessionAttr(request, "HcsaSvcSubtypeOrSubsumedDto");
            ParamUtil.setRequestAttr(request,"hcsaSvcSubtypeOrSubsumedDtos",hcsaSvcSubtypeOrSubsumedDtos);
        }
    }



    /*
    * get current svc dto
    * */
    private AppSvcRelatedInfoDto getAppSvcRelatedInfo(HttpServletRequest request, String currentSvcId){
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,NewApplicationDelegator.APPSUBMISSIONDTO);
        if(appSubmissionDto != null){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(appSvcRelatedInfoDtos != null && !appSvcRelatedInfoDtos.isEmpty()){
                for(AppSvcRelatedInfoDto svcRelatedInfoDto:appSvcRelatedInfoDtos){
                    if(currentSvcId.equals(svcRelatedInfoDto.getServiceId())){
                        appSvcRelatedInfoDto = svcRelatedInfoDto;
                        break;
                    }
                }
            }
        }
        return appSvcRelatedInfoDto;
    }

    private void setAppSvcRelatedInfoMap(HttpServletRequest request, String currentSvcId, AppSvcRelatedInfoDto appSvcRelatedInfoDto){
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,NewApplicationDelegator.APPSUBMISSIONDTO);
        if(appSubmissionDto != null){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(appSvcRelatedInfoDtos != null && !appSvcRelatedInfoDtos.isEmpty()){
                for(AppSvcRelatedInfoDto svcRelatedInfoDto:appSvcRelatedInfoDtos){
                    if(currentSvcId.equals(svcRelatedInfoDto.getServiceId())){
                        svcRelatedInfoDto = appSvcRelatedInfoDto;
                        break;
                    }
                }
            }
        }
        ParamUtil.setSessionAttr(request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
    }

    private  List<SelectOption> genSpecialtySelectList(String svcCode){
        List<SelectOption> specialtySelectList = null;
        if(!StringUtil.isEmpty(svcCode)){
            if(AppServicesConsts.SERVICE_CODE_CLINICAL_LABORATORY.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)){
                specialtySelectList = new ArrayList<>();
                SelectOption ssl1 = new SelectOption("-1", "Select Specialty");
                SelectOption ssl2 = new SelectOption("Pathology", "Pathology");
                SelectOption ssl3 = new SelectOption("Haematology", "Haematology");
                SelectOption ssl4 = new SelectOption("other", "Others");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl4);
            }else if(AppServicesConsts.SERVICE_CODE_RADIOLOGICAL_SERVICES.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode) ||
                    AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)){
                specialtySelectList = new ArrayList<>();
                SelectOption ssl1 = new SelectOption("-1", "Select Specialty");
                SelectOption ssl2 = new SelectOption("Diagnostic Radiology", "Diagnostic Radiology");
                SelectOption ssl3 = new SelectOption("Nuclear Medicine", "Nuclear Medicine");
                SelectOption ssl4 = new SelectOption("other", "Others");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl4);
            }
        }
        return specialtySelectList;
    }


    public List<AppSvcPersonnelDto> genAppSvcPersonnelDtoList(HttpServletRequest request, List<String> personnelTypeList){
        List<AppSvcPersonnelDto> appSvcPersonnelDtos = new ArrayList<>();
        String [] personnelSels =  ParamUtil.getStrings(request, "personnelSel");
        String [] designations = ParamUtil.getStrings(request, "designation");
        String [] names = ParamUtil.getStrings(request, "name");
        String [] qualifications = ParamUtil.getStrings(request, "qualification");
        String [] wrkExpYears = ParamUtil.getStrings(request, "wrkExpYear");
        String [] professionalRegnNos = ParamUtil.getStrings(request, "regnNo");
        if(personnelSels != null && personnelSels.length>0){
            for(int i =0; i <personnelSels.length; i++){
                AppSvcPersonnelDto appSvcPersonnelDto = new AppSvcPersonnelDto();
                String personnelSel = personnelSels[i];
                appSvcPersonnelDto.setPersonnelType(personnelSel);
                if(StringUtil.isEmpty(personnelSel) || !personnelTypeList.contains(personnelSel)){
                    appSvcPersonnelDtos.add(appSvcPersonnelDto);
                    continue;
                }
                String designation = "";
                String name = "";
                String qualification = "";
                String wrkExpYear = "";
                String professionalRegnNo = "";


                if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(personnelSel)){
                    designation = designations[i];
                    name = names[i];
                    qualification = qualifications[i];
                    wrkExpYear = wrkExpYears[i];
                }else if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(personnelSel)){
                    name = names[i];
                    qualification = qualifications[i];
                    wrkExpYear = wrkExpYears[i];
                }else if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER.equals(personnelSel)){
                    name = names[i];
                }else if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE.equals(personnelSel)){
                    name = names[i];
                    professionalRegnNo = professionalRegnNos[i];
                }
                appSvcPersonnelDto.setDesignation(designation);
                appSvcPersonnelDto.setName(name);
                appSvcPersonnelDto.setQuaification(qualification);
                appSvcPersonnelDto.setWrkExpYear(wrkExpYear);
                appSvcPersonnelDto.setProfRegNo(professionalRegnNo);
                appSvcPersonnelDtos.add(appSvcPersonnelDto);
            }
        }
        return appSvcPersonnelDtos;
    }

    @RequestMapping(value = "/nuclear-medicine-imaging-html", method = RequestMethod.GET)
    public @ResponseBody String addNuclearMedicineImagingHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add NuclearMedicineImaging html start ...."));
        String sql = SqlMap.INSTANCE.getSql("servicePersonnel", "NuclearMedicineImaging").getSqlStr();
        String currentSvcCod = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSVCCODE);
        List<SelectOption> personnel = genPersonnelTypeSel(currentSvcCod);
        Map<String,String> personnelAttr = new HashMap<>();
        personnelAttr.put("name", "personnelSel");
        personnelAttr.put("class", "personnelSel");
        personnelAttr.put("style", "display: none;");
        String personnelSelectStr = NewApplicationDelegator.generateDropDownHtml(personnelAttr, personnel, NewApplicationDelegator.FIRESTOPTION);

        List<SelectOption> designation = (List) ParamUtil.getSessionAttr(request, "NuclearMedicineImagingDesignation");
        Map<String,String> designationAttr = new HashMap<>();
        designationAttr.put("name", "designation");
        designationAttr.put("style", "display: none;");
        String designationSelectStr = NewApplicationDelegator.generateDropDownHtml(designationAttr, designation, NewApplicationDelegator.FIRESTOPTION);

        sql = sql.replace("(1)", personnelSelectStr);
        sql = sql.replace("(2)", designationSelectStr);

        log.debug(StringUtil.changeForLog("the add NuclearMedicineImaging html end ...."));
        return sql;
    }

    private List<SelectOption> genPersonnelTypeSel(String currentSvcCod){
        List<SelectOption> personnelTypeSel = new ArrayList<>();
        if(AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(currentSvcCod)){
            SelectOption personnelTypeOp1 = new SelectOption("SPPT001", "Radiology Professional");
            SelectOption personnelTypeOp2 = new SelectOption("SPPT002", "Medical Physicist");
            SelectOption personnelTypeOp3 = new SelectOption("SPPT003", "Radiation Safety Officer");
            SelectOption personnelTypeOp4 = new SelectOption("SPPT004", "Registered Nurse");
            personnelTypeSel.add(personnelTypeOp1);
            personnelTypeSel.add(personnelTypeOp2);
            personnelTypeSel.add(personnelTypeOp3);
            personnelTypeSel.add(personnelTypeOp4);
        }else if(AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(currentSvcCod)){
            SelectOption personnelTypeOp2 = new SelectOption("SPPT002", "Medical Physicist");
            SelectOption personnelTypeOp3 = new SelectOption("SPPT003", "Radiation Safety Officer");
            personnelTypeSel.add(personnelTypeOp2);
            personnelTypeSel.add(personnelTypeOp3);
        }else if(AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(currentSvcCod)){

        }else if(AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(currentSvcCod)){

        }
        return personnelTypeSel;
    }


    @RequestMapping(value = "/principal-officer-html", method = RequestMethod.GET)
    public @ResponseBody String addPrincipalOfficeHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add addPrincipalOfficeHtml html start ...."));
        String svcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        String sql = SqlMap.INSTANCE.getSql("principalOfficers", "generatePrincipalOfficersHtml").getSqlStr();

        //assign select
        List<SelectOption> assignPrincipalOfficerSel = getAssignPrincipalOfficerSel(svcId, false);
        Map<String,String> assignPrincipalOfficerAttr = new HashMap<>();
        assignPrincipalOfficerAttr.put("name", "assignSelect");
        assignPrincipalOfficerAttr.put("class", "poSelect");
        assignPrincipalOfficerAttr.put("style", "display: none;");
        String principalOfficerSelStr = NewApplicationDelegator.generateDropDownHtml(assignPrincipalOfficerAttr, assignPrincipalOfficerSel, NewApplicationDelegator.FIRESTOPTION);

        //salutation
        List<SelectOption> salutationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
        Map<String,String> salutationAttr = new HashMap<>();
        salutationAttr.put("name", "salutation");
        salutationAttr.put("style", "display: none;");
        String salutationSelectStr = NewApplicationDelegator.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION);

        //ID Type
        List<SelectOption> idTypeList = getIdTypeSelOp();
        Map<String,String>  idTypeAttr = new HashMap<>();
        idTypeAttr.put("name", "idType");
        idTypeAttr.put("style", "display: none;");
        String idTypeSelectStr = NewApplicationDelegator.generateDropDownHtml(idTypeAttr, idTypeList, null);

        //Designation
        List<SelectOption> designationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DESIGNATION);
        Map<String,String> designationAttr = new HashMap<>();
        designationAttr.put("name", "designation");
        designationAttr.put("style", "display: none;");
        String designationSelectStr = NewApplicationDelegator.generateDropDownHtml(designationAttr, designationList, NewApplicationDelegator.FIRESTOPTION);

        sql = sql.replace("(1)", principalOfficerSelStr);
        sql = sql.replace("(2)", salutationSelectStr);
        sql = sql.replace("(3)", idTypeSelectStr);
        sql = sql.replace("(4)", designationSelectStr);

        log.debug(StringUtil.changeForLog("the add addPrincipalOfficeHtml html end ...."));
        return sql;
    }


    @RequestMapping(value = "/deputy-principal-officer-html", method = RequestMethod.GET)
    public @ResponseBody String addDeputyPrincipalOfficeHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add addDeputyPrincipalOfficeHtml html start ...."));
        String sql = SqlMap.INSTANCE.getSql("principalOfficers", "generateDeputyPrincipalOfficersHtml").getSqlStr();

        //salutation
        List<SelectOption> salutationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
        Map<String,String> salutationAttr = new HashMap<>();
        salutationAttr.put("name", "deputySalutation");
        salutationAttr.put("style", "display: none;");
        String salutationSelectStr = NewApplicationDelegator.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION);

        //ID Type
        List<SelectOption> idTypeList = getIdTypeSelOp();
        Map<String,String>  idTypeAttr = new HashMap<>();
        idTypeAttr.put("name", "deputyIdType");
        idTypeAttr.put("style", "display: none;");
        String idTypeSelectStr = NewApplicationDelegator.generateDropDownHtml(idTypeAttr, idTypeList, null);

        //Designation
        List<SelectOption> designationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DESIGNATION);
        Map<String,String> designationAttr = new HashMap<>();
        designationAttr.put("name", "deputyDesignation");
        designationAttr.put("style", "display: none;");
        String designationSelectStr = NewApplicationDelegator.generateDropDownHtml(designationAttr, designationList, NewApplicationDelegator.FIRESTOPTION);

        //MedAlert
        List<SelectOption> medAlertSelectList = getMedAlertSelectList(false);
        Map<String,String> medAlertSelectAttr = new HashMap<>();
        medAlertSelectAttr.put("name", "modeOfMedAlert");
        medAlertSelectAttr.put("style", "display: none;");
        String medAlertSelectStr = NewApplicationDelegator.generateDropDownHtml(medAlertSelectAttr, medAlertSelectList, NewApplicationDelegator.FIRESTOPTION);

        sql = sql.replace("(1)", salutationSelectStr);
        sql = sql.replace("(2)", idTypeSelectStr);
        sql = sql.replace("(3)", designationSelectStr);
        sql = sql.replace("(4)", medAlertSelectStr);

        log.debug(StringUtil.changeForLog("the add addDeputyPrincipalOfficeHtml html end ...."));
        return sql;
    }


    private List<SelectOption> getAssignPrincipalOfficerSel(String svcId, boolean needFirstOpt){
        List<SelectOption> assignSelectList = new ArrayList<>();
        if(needFirstOpt){
            SelectOption assignOp1 = new SelectOption("-1", NewApplicationDelegator.FIRESTOPTION);
            assignSelectList.add(assignOp1);
        }
        SelectOption assignOp2 = new SelectOption("newOfficer", "I'd like to add a new personnel");
        assignSelectList.add(assignOp2);
        //todo his


        return  assignSelectList;
    }

    private List<SelectOption> getIdTypeSelOp(){
        List<SelectOption> idTypeSelectList = new ArrayList<>();
        SelectOption idType0 = new SelectOption("-1", NewApplicationDelegator.FIRESTOPTION);
        idTypeSelectList.add(idType0);
        SelectOption idType1 = new SelectOption("NRIC", "NRIC");
        idTypeSelectList.add(idType1);
        SelectOption idType2 = new SelectOption("FIN", "FIN");
        idTypeSelectList.add(idType2);
        return idTypeSelectList;
    }

    private List<SelectOption> getMedAlertSelectList(boolean needFirstOp){
        List<SelectOption> MedAlertSelectList = new ArrayList<>();
        if(needFirstOp){
            SelectOption idType0 = new SelectOption("-1", NewApplicationDelegator.FIRESTOPTION);
            MedAlertSelectList.add(idType0);
        }
        SelectOption idType1 = new SelectOption("Email", "Email");
        MedAlertSelectList.add(idType1);
        SelectOption idType2 = new SelectOption("SMS", "SMS");
        MedAlertSelectList.add(idType2);
        return MedAlertSelectList;
    }

    private boolean judegCanEdit(AppSubmissionDto appSubmissionDto){
        boolean canEdit = true;
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
        if(appEditSelectDto!=null){
            if(ApplicationConsts.APPLICATION_EDIT_TYPE_RFI.equals(appEditSelectDto.getEditType())&&!appEditSelectDto.isServiceEdit()){
               canEdit = false;
            }
        }
        return  canEdit;
    }

    private Set<String> getRfcClickEditPageSet(AppSubmissionDto appSubmissionDto){
        return appSubmissionDto.getClickEditPage() == null ? new HashSet<>() : appSubmissionDto.getClickEditPage();
    }

    private AppSubmissionDto setPrincipalOfficersClickEditPage(AppSubmissionDto appSubmissionDto, Boolean isGetDataFromPagePo, Boolean isGetDataFromPageDpo){
        Set<String> clickEditPages = getRfcClickEditPageSet(appSubmissionDto);
        if (isGetDataFromPagePo) {
            clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_PRINCIPAL_OFFICERS);
        }
        if (isGetDataFromPageDpo) {
            clickEditPages.add(NewApplicationDelegator.APPLICATION_SVC_PAGE_NAME_DEPUTY_PRINCIPAL_OFFICERS);
        }
        appSubmissionDto.setClickEditPage(clickEditPages);
        return appSubmissionDto;
    }
}
