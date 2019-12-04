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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
    public static final String  APPSVCRELATEDINFOMAP = "AppsvcRelatedInfoMap";
    public static final String  ERRORMAP_GOVERNANCEOFFICERS = "errorMap_governanceOfficers";

    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doStart start ...."));

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
        log.debug(StringUtil.changeForLog("the do prepareLaboratoryDisciplines end ...."));
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
        String psnType = "CGO";
        if(!StringUtil.isEmpty(currentSvcId) && !StringUtil.isEmpty(psnType)){
            //min and max count
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelList  =serviceConfigService.getGOSelectInfo(currentSvcId, psnType);
            List<AppSvcCgoDto> appSvcCgoDtoList = (List<AppSvcCgoDto>) ParamUtil.getSessionAttr(bpc.request, GOVERNANCEOFFICERSDTOLIST);
            HcsaSvcPersonnelDto hcsaSvcPersonnelDto  =hcsaSvcPersonnelList.get(0);
            int mandatoryCount = hcsaSvcPersonnelDto.getMandatoryCount();
            if(appSvcCgoDtoList != null){
                mandatoryCount = appSvcCgoDtoList.size();
            }
            ParamUtil.setSessionAttr(bpc.request, "CgoMandatoryCount", mandatoryCount);
            ParamUtil.setSessionAttr(bpc.request, "HcsaSvcPersonnel", hcsaSvcPersonnelDto);
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
        Map<String,AppSvcRelatedInfoDto> appSvcRelatedInfoMap = (Map<String, AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(bpc.request, APPSVCRELATEDINFOMAP);
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoMap.get(currentSvcId);
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
        List<AppSvcLaboratoryDisciplinesDto> newChkLstDtoList = new ArrayList<>();
        int chkLstSize = appSvcLaboratoryDisciplinesDtoList.size();
        for(int i=0;i<chkLstSize;i++){
            AppSvcLaboratoryDisciplinesDto chkLstDto = appSvcLaboratoryDisciplinesDtoList.get(i);
            String premiseGetAddress = appSubmissionDto.getAppGrpPremisesDto().getAddress();
            chkLstDto.setPremiseGetAddress(premiseGetAddress);
            newChkLstDtoList.add(chkLstDto);
        }
        ParamUtil.setSessionAttr(bpc.request, "PremisesAndChkLst", (Serializable) newChkLstDtoList);
        List<SelectOption> spList = new ArrayList<>();
        List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
        SelectOption sp = null;
        for(AppSvcCgoDto cgo:appSvcCgoDtoList){
            sp = new SelectOption(cgo.getIdNo(), cgo.getName());
            spList.add(sp);
        }
        ParamUtil.setSessionAttr(bpc.request, "CgoSelect", (Serializable) spList);
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
        /*List poList = appGrpSvcRelatedInfoService.loadPO();
        ParamUtil.setSessionAttr(bpc.request, "POList", (Serializable) poList);*/
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
        //one premises
        List<AppSvcDisciplineAllocationDto> allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();

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
        String [] checkListInfo = bpc.request.getParameterValues("control--runtime--1");
        if(StringUtil.isEmpty(checkListInfo)){
            return;
        }
        // one premises flow
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currentSvcDto =getAppSvcRelatedInfo(bpc.request,currentSvcId);


        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        AppGrpPremisesDto premisesDto = appSubmissionDto.getAppGrpPremisesDto();
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = new ArrayList<>();
        AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto = new AppSvcLaboratoryDisciplinesDto();
        List<AppSvcChckListDto> appSvcChckListDtoList = new ArrayList<>();
        AppSvcChckListDto appSvcChckListDto = null;
        if(!StringUtil.isEmpty(checkListInfo)){
            for(String item:checkListInfo){
                String[] config = item.split(";");
                if(StringUtil.isEmpty(item) || config.length!=3){
                    continue;
                }
                appSvcChckListDto = new AppSvcChckListDto();
                appSvcChckListDto.setChkLstConfId(config[0]);
                if("true".equals(config[1])){
                    appSvcChckListDto.setChkLstType(1);
                }else if("false".equals(config[1])){
                    appSvcChckListDto.setChkLstType(0);
                }
                appSvcChckListDto.setChkName(config[2]);
                appSvcChckListDtoList.add(appSvcChckListDto);
            }
            String premisesType = premisesDto.getPremisesType();
            String premisesValue = "";
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)){
                premisesValue = premisesDto.getHciName();
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)){
                premisesValue = premisesDto.getConveyanceVehicleNo();
            }
            //else his .....
            appSvcLaboratoryDisciplinesDto.setPremiseType(premisesType);
            appSvcLaboratoryDisciplinesDto.setPremiseVal(premisesValue);
            appSvcLaboratoryDisciplinesDto.setPremisesIndexNo(premisesDto.getPremisesIndexNo());
            appSvcLaboratoryDisciplinesDto.setPremiseGetAddress(premisesDto.getAddress());
            appSvcLaboratoryDisciplinesDto.setAppSvcChckListDtoList(appSvcChckListDtoList);
            appSvcLaboratoryDisciplinesDtoList.add(appSvcLaboratoryDisciplinesDto);

            //save into sub-svc dto
          /*  AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfoDto(bpc.request);
            appSvcRelatedInfoDto.setAppSvcLaboratoryDisciplinesDtoList(appSvcLaboratoryDisciplinesDtoList);*/
            currentSvcDto.setAppSvcLaboratoryDisciplinesDtoList(appSvcLaboratoryDisciplinesDtoList);
            setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcDto);
            //get current service info
            /*String currentSvcCode = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSVCCODE);
            appSvcRelatedInfoDto.setServiceCode(currentSvcCode);
            appSvcRelatedInfoDto.setServiceId(currentSvcId);*/
            //hard-code wait when dostart init set
            /*appSvcRelatedInfoDto.setServiceType("BASE");*/
            //ParamUtil.setSessionAttr(bpc.request, APPSVCRELATEDINFODTO, appSvcRelatedInfoDto);
        }


        log.debug(StringUtil.changeForLog("the do doLaboratoryDisciplines end ...."));
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
        /*List<Map<String,String>> errList = doValidateGovernanceOfficers(bpc.request);
        for(Map<String,String> errMap:errList){
            if(errMap.size() > 0){
                ParamUtil.setSessionAttr(bpc.request, ERRORMAP_GOVERNANCEOFFICERS, (Serializable) errList);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "governanceOfficers");
                return;
            }
        }*/

        //save into sub-svc dto
        /*AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfoDto(bpc.request);
        appSvcRelatedInfoDto.setAppSvcCgoDtoList(appSvcCgoDtoList);*/

        Map<String,AppSvcRelatedInfoDto> appSvcRelatedInfoMap = (Map<String, AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(bpc.request, APPSVCRELATEDINFOMAP);
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currentSvcRelatedDto = appSvcRelatedInfoMap.get(currentSvcId);
        currentSvcRelatedDto.setAppSvcCgoDtoList(appSvcCgoDtoList);

        appSvcRelatedInfoMap.put(currentSvcId, currentSvcRelatedDto);
        ParamUtil.setSessionAttr(bpc.request, APPSVCRELATEDINFOMAP, (Serializable) appSvcRelatedInfoMap);
        //ParamUtil.setSessionAttr(bpc.request, APPSVCRELATEDINFODTO, appSvcRelatedInfoDto);

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

        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        AppGrpPremisesDto appGrpPremisesDto = appSubmissionDto.getAppGrpPremisesDto();
        String premisesIndexNo = appGrpPremisesDto.getPremisesIndexNo();
        Map<String,AppSvcRelatedInfoDto> appSvcRelatedInfoMap = (Map<String, AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(bpc.request, APPSVCRELATEDINFOMAP);
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto currentSvcRelatedDto = appSvcRelatedInfoMap.get(currentSvcId);
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = currentSvcRelatedDto.getAppSvcLaboratoryDisciplinesDtoList();
        List<AppSvcDisciplineAllocationDto> daList = new ArrayList<>();
        AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto = null;
        //one premises
        String premisesType = appGrpPremisesDto.getPremisesType();
        String premisesValue = "";
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)){
            premisesValue = appGrpPremisesDto.getHciName();
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)){
            premisesValue = appGrpPremisesDto.getConveyanceVehicleNo();
        }


        Map<String,String> reloadAllocation = new HashMap<>();
        if(appSvcLaboratoryDisciplinesDtoList != null){
            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                int chkLstSize = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList().size();
                for(int i =0 ;i<chkLstSize;i++){
                    StringBuilder chkAndCgoName = new StringBuilder()
                            .append(premisesIndexNo)
                            .append(i);
                    String [] chkAndCgoValue = ParamUtil.getStrings(bpc.request, chkAndCgoName.toString());
                    appSvcDisciplineAllocationDto = new AppSvcDisciplineAllocationDto();
                    appSvcDisciplineAllocationDto.setPremiseType(premisesType);
                    appSvcDisciplineAllocationDto.setPremiseVal(premisesValue);
                    appSvcDisciplineAllocationDto.setChkLstConfId(chkAndCgoValue[0]);
                    appSvcDisciplineAllocationDto.setIdNo(chkAndCgoValue[1]);
                    daList.add(appSvcDisciplineAllocationDto);
                    String cgoIdNo = chkAndCgoValue[1];
                    reloadAllocation.put(premisesIndexNo+chkAndCgoValue[0], cgoIdNo);
                }
            }
        }

        ParamUtil.setSessionAttr(bpc.request, "ReloadAllocationMap", (Serializable) reloadAllocation);

        //save into sub-svc dto
        currentSvcRelatedDto.setAppSvcDisciplineAllocationDtoList(daList);
        setAppSvcRelatedInfoMap(bpc.request, currentSvcId, currentSvcRelatedDto);
        //ParamUtil.setSessionAttr(bpc.request, APPSVCRELATEDINFOMAP, currentSvcRelatedDto);
        //ParamUtil.setSessionAttr(bpc.request, APPSVCRELATEDINFODTO, appSvcRelatedInfoDto);

        log.debug(StringUtil.changeForLog("the do doDisciplineAllocation end ...."));
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
        Map<String,Map<String,String>> map = NewApplicationDelegator.doValidatePo(bpc.request);
        if(map.size() >0 ){
            //ParamUtil.setSessionAttr(bpc.request, "", );
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "principalOfficers");
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

        List<MultipartFile> files = null;
        for (Iterator<String> en = mulReq.getFileNames(); en.hasNext(); ) {
            String name = en.next();
            files = mulReq.getFiles(name);
        }
        String [] docConfig = mulReq.getParameterValues("docConfig");
        int count =0;
        if(files != null && docConfig !=null){
            for(MultipartFile file:files){
                if(!StringUtil.isEmpty(file.getOriginalFilename())){
                    String[] config = docConfig[count].split(";");
                    appSvcDocDto = new AppSvcDocDto();
                    appSvcDocDto.setSvcDocId(config[0]);
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

        Map<String,AppSvcRelatedInfoDto> appSvcRelatedInfoMap = (Map<String, AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(bpc.request, APPSVCRELATEDINFOMAP);
        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSvcRelatedInfoMap.get(currentSvcId);
        appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtoList);
        appSvcRelatedInfoMap.put(currentSvcId, appSvcRelatedInfoDto);
        ParamUtil.setSessionAttr(bpc.request, APPSVCRELATEDINFOMAP, (Serializable) appSvcRelatedInfoMap);
        //AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO);
        /*List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList =new ArrayList<>();
        appSvcRelatedInfoDtoList.add(appSvcRelatedInfoDto);
        appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);*/
        //ParamUtil.setSessionAttr(bpc.request, APPSVCRELATEDINFODTO, appSvcRelatedInfoDto);
        //ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
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

    @RequestMapping(value = "/governance-officer-list", method = RequestMethod.GET)
    public @ResponseBody String genGovernanceOfficerHtmlList(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("gen governance officer html start ...."));


        log.debug(StringUtil.changeForLog("gen governance officer html end ...."));
        return "testtest";
    }

    private Map<String,String[]> governanceOfficerListDate(HttpServletRequest bpc){
        Map<String,String[]> map = new HashMap<>();
        //name , m/o/d , dropdown,input,...  ,dropdown
        String [] salutation = {"Salutation","M","DRP",""};
        String [] name ={"Name","M","INP"};
        String [] idType = {"ID Type","M","DRP",""};
        String [] idNo = {"ID No.","M","INP",""};
        String [] designation = {"Designation","M","DRP",""};
        String [] professionType = {"Profession Type","M","DRP",""};
        String [] professionalRegnType = {"Professional Regn Type","M","DRP",""};
        String [] professionalRegnNo = {"Professional Regn No.","M","DRP",""};
        String [] specialty = {"Specialty","M","DRP",""};
        String [] qualification = {"Subspeciality or relevant qualification","M","DRP",""};
        String [] mobileNo = {"Mobile No.","M","DRP",""};
        String [] emailAddress  = {"Email address ","M","DRP",""};
        return null;
    }

    private List<AppSvcCgoDto> genAppSvcCgoDto(HttpServletRequest request){
        ParamUtil.setSessionAttr(request, ERRORMAP_GOVERNANCEOFFICERS,null);
        HcsaSvcPersonnelDto hcsaSvcPersonnelDto  = (HcsaSvcPersonnelDto) ParamUtil.getSessionAttr(request, "HcsaSvcPersonnel");
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
        String assignSelect = ParamUtil.getString(request, "deputySelect");
        String deputySelect = ParamUtil.getString(request, "deputySelect");
        String salutation = ParamUtil.getString(request, "salutation");
        String name = ParamUtil.getString(request, "name");
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        String designation = ParamUtil.getString(request, "designation");
        String mobileNo = ParamUtil.getString(request, "mobileNo");
        String emailAddress = ParamUtil.getString(request, "emailAddress");
        dto.setAssignSelect(assignSelect);
        dto.setDeputyPrincipalOfficer(deputySelect);
        dto.setSalutation(salutation);
        dto.setName(name);
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

    private List<Map<String,String>> doValidateGovernanceOfficers(HttpServletRequest request){
        List<AppSvcCgoDto> appSvcCgoList = (List<AppSvcCgoDto>) ParamUtil.getSessionAttr(request, GOVERNANCEOFFICERSDTOLIST);
        if(appSvcCgoList == null){
            return null;
        }
        List<Map<String,String>> errList = new ArrayList<>();
        Map<String,String> errMap = null;
        for(AppSvcCgoDto appSvcCgoDto:appSvcCgoList){
            errMap = new HashMap<>();
            String assignSelect = appSvcCgoDto.getAssignSelect();
            if("-1".equals(assignSelect)){
                errMap.put("assignSelect", "not selected assign Clinical Governance Officer");
            }else {
                ValidationResult validationResult = WebValidationHelper.validateProperty(appSvcCgoDto, AppServicesConsts.VALIDATE_PROFILES_CREATE);
                if (validationResult.isHasErrors()) {
                    errMap = validationResult.retrieveAll();
                }
                String idTyp = appSvcCgoDto.getIdType();
                if(StringUtil.isEmpty(idTyp)){
                    errMap.put("idTyp", "can not null");
                }else{

                }
                String idNo = appSvcCgoDto.getIdNo();
                //to do

                //to do

                String qualification = appSvcCgoDto.getQualification();
                if (!StringUtil.isEmpty(qualification)) {
                    String Specialty = appSvcCgoDto.getSpeciality();
                    if (StringUtil.isEmpty(Specialty)) {
                        errMap.put("speciality", "Specialty don not select");
                    }

                }
                String specialty = appSvcCgoDto.getSpeciality();
                if(StringUtil.isEmpty(specialty)){
                    errMap.put("specialty", "can not null");
                }

                String mobileNo = appSvcCgoDto.getMobileNo();
                if (!StringUtil.isEmpty(mobileNo)) {
                    if (!mobileNo.startsWith("8") && !mobileNo.startsWith("9")) {
                        errMap.put("mobileNo", "Please key in a valid mobile number");
                    }
                }
                String emailAddr = appSvcCgoDto.getEmailAddr();
                if (!StringUtil.isEmpty(emailAddr)) {
                    if (!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                        errMap.put("emailAddr", "Please key in a valid email address");
                    }
                }
            }
            errList.add(errMap);
        }
        return errList;
    }

    private AppSvcRelatedInfoDto getAppSvcRelatedInfo(HttpServletRequest request, String currentSvcId){
        Map<String,AppSvcRelatedInfoDto> map = (Map<String, AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(request, APPSVCRELATEDINFOMAP);
        return map.get(currentSvcId);
    }

    private void setAppSvcRelatedInfoMap(HttpServletRequest request, String currentSvcId, AppSvcRelatedInfoDto appSvcRelatedInfoDto){
        Map<String,AppSvcRelatedInfoDto> map = (Map<String, AppSvcRelatedInfoDto>) ParamUtil.getSessionAttr(request, APPSVCRELATEDINFOMAP);
        map.put(currentSvcId, appSvcRelatedInfoDto);
        ParamUtil.setSessionAttr(request, APPSVCRELATEDINFOMAP, (Serializable) map);
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
