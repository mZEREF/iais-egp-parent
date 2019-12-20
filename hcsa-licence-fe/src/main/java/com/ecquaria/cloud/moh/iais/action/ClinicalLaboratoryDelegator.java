package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.ClinicalOfficerValidateDto;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
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
import java.util.List;
import java.util.Map;



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
    //public static final String  APPSVCRELATEDINFOMAP = "AppsvcRelatedInfoMap";
    public static final String  ERRORMAP_GOVERNANCEOFFICERS = "errorMap_governanceOfficers";

    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doStart start ...."));

        //svc
        ParamUtil.setSessionAttr(bpc.request, ClinicalLaboratoryDelegator.GOVERNANCEOFFICERSDTOLIST, null);
        ParamUtil.setSessionAttr(bpc.request, ClinicalLaboratoryDelegator.ERRORMAP_GOVERNANCEOFFICERS, null);

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
        if(StringUtil.isEmpty(action)||IaisEGPConstant.YES.equals(formTab)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,"laboratoryDisciplines");
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
        ParamUtil.setSessionAttr(bpc.request, "CgoMandatoryCount", 0);
        ParamUtil.setRequestAttr(bpc.request, GOVERNANCEOFFICERSDTOLIST, null);
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        String psnType = "CGO";
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        if(!StringUtil.isEmpty(currentSvcId) && !StringUtil.isEmpty(psnType)){
            //min and max count
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList  =serviceConfigService.getGOSelectInfo(currentSvcId, psnType);
            if(hcsaSvcPersonnelList != null && hcsaSvcPersonnelList.size()>0){
                HcsaSvcPersonnelDto hcsaSvcPersonnelDto  = hcsaSvcPersonnelList.get(0);
                int mandatoryCount = hcsaSvcPersonnelDto.getMandatoryCount();
                if(appSvcRelatedInfoDto != null){
                    List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                    if(appSvcCgoDtoList != null  && appSvcCgoDtoList.size()>mandatoryCount){
                        mandatoryCount = appSvcCgoDtoList.size();
                    }
                }

                ParamUtil.setSessionAttr(bpc.request, "CgoMandatoryCount", mandatoryCount);
                ParamUtil.setSessionAttr(bpc.request, "HcsaSvcPersonnel", hcsaSvcPersonnelDto);
            }
        }
        List<SelectOption> cgoSelectList = new ArrayList<>();
        SelectOption sp0 = new SelectOption("-1", "Select Personnel");
        cgoSelectList.add(sp0);
        SelectOption sp1 = new SelectOption("newOfficer", "I'd like to add a new personnel");
        cgoSelectList.add(sp1);
        ParamUtil.setSessionAttr(bpc.request, "CgoSelectList", (Serializable) cgoSelectList);

        List<SelectOption> idTypeSelectList = new ArrayList<>();
        SelectOption idType0 = new SelectOption("-1", "Select Personnel");
        idTypeSelectList.add(idType0);
        SelectOption idType1 = new SelectOption("NRIC", "NRIC");
        idTypeSelectList.add(idType1);
        SelectOption idType2 = new SelectOption("FIN", "FIN");
        idTypeSelectList.add(idType2);
        ParamUtil.setSessionAttr(bpc.request, "IdTypeSelect", (Serializable) idTypeSelectList);

        List<HcsaServiceDto> hcsaServiceDtoList= (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
        String svcName = "";
        for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtoList){
            String svcId = hcsaServiceDto.getId();
            if(currentSvcId.equals(svcId)){
                svcName = hcsaServiceDto.getSvcName();
                break;
            }
        }
        List<SelectOption> specialtySelectList = genSpecialtySelectList(svcName);
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
        String psnType = "PO";
        //List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList  =serviceConfigService.getGOSelectInfo(currentSvcId, psnType);


        List<SelectOption> MedAlertSelectList = new ArrayList<>();
        SelectOption idType0 = new SelectOption("-1", "Select MedAlert");
        MedAlertSelectList.add(idType0);
        SelectOption idType1 = new SelectOption("Email", "Email");
        MedAlertSelectList.add(idType1);
        SelectOption idType2 = new SelectOption("SMS", "SMS");
        MedAlertSelectList.add(idType2);
        ParamUtil.setSessionAttr(bpc.request, "MedAlertSelect", (Serializable) MedAlertSelectList);



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
        List<HcsaSvcDocConfigDto> hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(currentSvcId);
        if(hcsaSvcDocDtos != null && !hcsaSvcDocDtos.isEmpty()){
            ParamUtil.setSessionAttr(bpc.request, "serviceDocConfigDto", (Serializable) hcsaSvcDocDtos);
        }

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
        //demo
        List<String> reloadCheckedList = new ArrayList<>();
        AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto = null;
        Map<String,HcsaSvcSubtypeOrSubsumedDto> map = new HashMap<>();
        List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = (List<HcsaSvcSubtypeOrSubsumedDto>) ParamUtil.getSessionAttr(bpc.request, "HcsaSvcSubtypeOrSubsumedDto");
        turn(hcsaSvcSubtypeOrSubsumedDtos, map);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currentSvcDto =getAppSvcRelatedInfo(bpc.request,currentSvcId);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        Map<String,String> reloadChkLstMap = new HashMap<>();
        Map<String,String> errorMap=new HashMap<>();
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = new ArrayList<>();
        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
            String name = appGrpPremisesDto.getHciName()+"control--runtime--1";
            String [] checkList = ParamUtil.getStrings(bpc.request, name);
            List<AppSvcChckListDto> appSvcChckListDtoList = new ArrayList<>();
            AppSvcChckListDto appSvcChckListDto = new AppSvcChckListDto();
            if(!StringUtil.isEmpty(checkList)){
                for(String maskName:checkList){
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
                    reloadChkLstMap.put(currentSvcId+appGrpPremisesDto.getHciName()+checkInfo.getId(), "checked");
                }
                String premisesType = appGrpPremisesDto.getPremisesType();
                String premisesValue = "";
                if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)){
                    premisesValue = appGrpPremisesDto.getHciName();
                }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)){
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
            errorMap = doValidateLaboratory(appSvcChckListDtoList,currentSvcId);
        }
        ParamUtil.setSessionAttr(bpc.request, "reloadLaboratoryDisciplines", (Serializable) reloadChkLstMap);
        if(!errorMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"errorMsg",WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,"laboratoryDisciplines");
            return;
        }
        currentSvcDto.setAppSvcLaboratoryDisciplinesDtoList(appSvcLaboratoryDisciplinesDtoList);
        setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcDto);
        log.debug(StringUtil.changeForLog("the do doLaboratoryDisciplines end ...."));
    }

    private Map<String,String> doValidateLaboratory(  List<AppSvcChckListDto>  listDtos,String serviceId  ){
        Map<String,String> map=new HashMap<>();
        int count=0;

        if(listDtos.isEmpty()){

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
            map.put("checkError","Illegal or not empty operation");
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
        List<AppSvcCgoDto> appSvcCgoDtoList = genAppSvcCgoDto(bpc.request);
        //do validate
       Map<String,String> errList = doValidateGovernanceOfficers(bpc.request);

            if(!errList.isEmpty()){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "governanceOfficers");
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errList));
                return;
            }

        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currentSvcRelatedDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        currentSvcRelatedDto.setAppSvcCgoDtoList(appSvcCgoDtoList);
        setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto);
        log.debug(StringUtil.changeForLog("the do doGovernanceOfficers end ...."));
    }


    /**
     * StartStep: doDisciplineAllocation
     *
     * @param bpc
     * @throws
     */
    public void doDisciplineAllocation(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doDisciplineAllocation start ...."));
        Map<String,String > errorMap=new HashMap<>();
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currentSvcRelatedDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        List<AppSvcDisciplineAllocationDto> daList = new ArrayList<>();
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = currentSvcRelatedDto.getAppSvcLaboratoryDisciplinesDtoList();
        if(appSvcLaboratoryDisciplinesDtoList != null){
            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                String premisesType = "";
                String premisesValue = "";
                String premisesIndexNo = "";
                for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
                    if(appSvcLaboratoryDisciplinesDto.getPremiseVal().equals(appGrpPremisesDto.getConveyanceVehicleNo())){
                    //    premisesIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                        premisesType = appGrpPremisesDto.getPremisesType();
                        premisesValue = appGrpPremisesDto.getConveyanceVehicleNo();
                        break;
                    }else if (appSvcLaboratoryDisciplinesDto.getPremiseVal().equals(appGrpPremisesDto.getHciName())){
                    //    premisesIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                        premisesType = appGrpPremisesDto.getPremisesType();
                        premisesValue = appGrpPremisesDto.getHciName();
                        break;
                    }
                }
                AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto = null;
                int chkLstSize = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList().size();
                for(int i =0 ;i<chkLstSize;i++){
                    StringBuilder chkAndCgoName = new StringBuilder()
                            .append(premisesValue)
                            .append(i);
                    String [] chkAndCgoValue = ParamUtil.getStrings(bpc.request, chkAndCgoName.toString());
                    if(chkAndCgoValue != null && chkAndCgoValue.length>0){
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
        if(!errorMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE,"disciplineAllocation");
            return;
        }

        //save into sub-svc dto
        currentSvcRelatedDto.setAppSvcDisciplineAllocationDtoList(daList);
        setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto);
        log.debug(StringUtil.changeForLog("the do doDisciplineAllocation end ...."));

    }

    private void doValidateDisciplineAllocation(Map<String ,String> map, List<AppSvcDisciplineAllocationDto> daList){
        for(AppSvcDisciplineAllocationDto every:daList){
            String idNo = every.getIdNo();

        }
    }
    /**
     * StartStep: doPrincipalOfficers
     *
     * @param bpc
     * @throws
     */
    public void doPrincipalOfficers(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doPrincipalOfficers start ...."));
        AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto = genAppSvcPrincipalOfficersDto(bpc.request) ;
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = new ArrayList<>();
        appSvcPrincipalOfficersDtoList.add(appSvcPrincipalOfficersDto);
        ParamUtil.setSessionAttr(bpc.request, "AppSvcPrincipalOfficersDto", (Serializable) appSvcPrincipalOfficersDtoList);
        Map<String,String> map = NewApplicationDelegator.doValidatePo(bpc.request);
        if(!map.isEmpty()){
            //ParamUtil.setSessionAttr(bpc.request, "", );
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "principalOfficers");
            ParamUtil.setRequestAttr(bpc.request,"errorMsg",WebValidationHelper.generateJsonStr(map));
            return;
        }

        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfo(bpc.request, currentSvcId);
        appSvcRelatedInfoDto.setAppSvcPrincipalOfficersDtoList(appSvcPrincipalOfficersDtoList);
        setAppSvcRelatedInfoMap(bpc.request, currentSvcId, appSvcRelatedInfoDto);
//        ParamUtil.setSessionAttr(bpc.request, APPSVCRELATEDINFOMAP, (Serializable) appSvcRelatedInfoMap);
        //ParamUtil.setSessionAttr(bpc.request, APPSVCRELATEDINFODTO, appSvcRelatedInfoDto);


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
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "jump");

        List<HcsaSvcDocConfigDto> svcDocConfigDtos = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, "serviceDocConfigDto");

        CommonsMultipartFile file = null;
        if(svcDocConfigDtos != null && !svcDocConfigDtos.isEmpty()){
            for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:svcDocConfigDtos){
                String docConfigId = hcsaSvcDocConfigDto.getId();
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
                        String fileRepoGuid = serviceConfigService.saveFileToRepo(file);
                        appSvcDocDto.setFileRepoId(fileRepoGuid);
                        //wait api change to get fileRepoId
                        appSvcDocDtoList.add(appSvcDocDto);
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
        List<SelectOption> idTypeList = (List<SelectOption>) ParamUtil.getSessionAttr(request, "IdTypeSelect");
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
        proRegnTypeAttr.put("name", "professionRegoType");
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


    private List<AppSvcCgoDto> genAppSvcCgoDto(HttpServletRequest request){
        ParamUtil.setSessionAttr(request, ERRORMAP_GOVERNANCEOFFICERS,null);
        HcsaSvcPersonnelDto hcsaSvcPersonnelDto  = (HcsaSvcPersonnelDto) ParamUtil.getSessionAttr(request, "HcsaSvcPersonnel");
        if(hcsaSvcPersonnelDto ==null){
            return null;
        }
        int mandatoryCount = hcsaSvcPersonnelDto.getMandatoryCount();
        List<AppSvcCgoDto> appSvcCgoDtoList = new ArrayList<>();
        AppSvcCgoDto appSvcCgoDto = null;

        String[] assignSelect = ParamUtil.getStrings(request, "assignSelect");
        int size = assignSelect.length;
        String[] salutation = ParamUtil.getStrings(request, "salutation");
        String[] name = ParamUtil.getStrings(request, "name");
        String[] idType = ParamUtil.getStrings(request, "idType");
        String[] idNo = ParamUtil.getStrings(request, "idNo");
        String[] designation = ParamUtil.getStrings(request, "designation");
        //String[] professionType = ParamUtil.getStrings(request, "professionType");
        String[] professionRegoType = ParamUtil.getStrings(request, "professionRegoType");
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
            //appSvcCgoDto.setProfessionType(professionType[i]);
            appSvcCgoDto.setProfessionRegoType(professionRegoType[i]);
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



    private AppSvcPrincipalOfficersDto genAppSvcPrincipalOfficersDto(HttpServletRequest request){
        AppSvcPrincipalOfficersDto dto = new AppSvcPrincipalOfficersDto();
        String assignSelect = ParamUtil.getString(request, "assignSelect");
        String deputySelect = ParamUtil.getString(request, "deputySelect");
        String salutation = ParamUtil.getString(request, "salutation");
        String name = ParamUtil.getString(request, "name");
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        String designation = ParamUtil.getString(request, "designation");
        String mobileNo = ParamUtil.getString(request, "mobileNo");
        String officeTelNo = ParamUtil.getString(request, "officeTelNo");
        String emailAddress = ParamUtil.getString(request, "emailAddress");

        dto.setAssignSelect(assignSelect);
        dto.setDeputyPrincipalOfficer(deputySelect);
        dto.setSalutation(salutation);
        dto.setName(name);
        dto.setOfficeTelNo(officeTelNo);
        dto.setDesignation(designation);
        dto.setMobileNo(mobileNo);
        dto.setEmailAddr(emailAddress);
        dto.setIdType(idType);
        dto.setIdNo(idNo);
        return  dto;
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
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        return appSubmissionDto;
    }

    private Map<String,String> doValidateGovernanceOfficers(HttpServletRequest request){
        List<AppSvcCgoDto> appSvcCgoList = (List<AppSvcCgoDto>) ParamUtil.getSessionAttr(request, GOVERNANCEOFFICERSDTOLIST);
        if(appSvcCgoList == null){
            return null;
        }

        Map<String,String> errMap = new HashMap<>();
        for(int i=0;i<appSvcCgoList.size();i++ ){
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
                String professionRegoType = appSvcCgoList.get(i).getProfessionRegoType();
                if(StringUtil.isEmpty(professionRegoType)){
                    errMap.put("professionRegoType"+i,"UC_CHKLMD001_ERR002");
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
                    boolean b = SgNoValidator.validateFin(idNo);
                    boolean b1 = SgNoValidator.validateNric(idNo);
                    if(!(b||b1)){
                 /*       errMap.put("idNo"+i,"CHKLMD001_ERR005");*/
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

    private  List<SelectOption> genSpecialtySelectList(String svcName){
        List<SelectOption> specialtySelectList = null;
        if(!StringUtil.isEmpty(svcName)){
            if(AppServicesConsts.SERVICE_NAME_CLINICAL_LABORATORY.equals(svcName) ||
                    AppServicesConsts.SERVICE_NAME_BLOOD_BANKING.equals(svcName) ||
                    AppServicesConsts.SERVICE_NAME_TISSUE_BANKING.equals(svcName)){
                specialtySelectList = new ArrayList<>();
                SelectOption ssl1 = new SelectOption("-1", "Select Specialty");
                SelectOption ssl2 = new SelectOption("Pathology", "Pathology");
                SelectOption ssl3 = new SelectOption("Haematology", "Haematology");
                SelectOption ssl4 = new SelectOption("other", "Others");
                specialtySelectList.add(ssl1);
                specialtySelectList.add(ssl2);
                specialtySelectList.add(ssl3);
                specialtySelectList.add(ssl4);
            }else if(AppServicesConsts.SERVICE_NAME_RADIOLOGICAL_SERVICES.equals(svcName) ||
                    AppServicesConsts.SERVICE_NAME_NUCLEAR_MEDICINE.equals(svcName)){
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

}
