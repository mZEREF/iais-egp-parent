package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.service.AppGrpSvcRelatedInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator.SERVICEID;


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
    AppGrpSvcRelatedInfoService appGrpSvcRelatedInfoService;

    public static final String  GOVERNANCEOFFICERS = "GovernanceOfficers";
    public static final String  GOVERNANCEOFFICERSDTO = "GovernanceOfficersDto";
    public static final String  APPSVCRELATEDINFODTO ="AppSvcRelatedInfoDto";

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
        String action = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM_PAGE);
        log.debug(StringUtil.changeForLog("The prepareJumpPage action is -->;"+action));
        String form_tab = (String)ParamUtil.getRequestAttr(bpc.request,IaisEGPConstant.FORM_TAB);
        log.debug(StringUtil.changeForLog("The form_tab action is -->;"+form_tab));
        if(StringUtil.isEmpty(action)||IaisEGPConstant.YES.equals(form_tab)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM,"laboratoryDisciplines");
        }else{
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM,action);
        }
        String crud_action_type = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        log.debug(StringUtil.changeForLog("The crud_action_type  is -->;"+crud_action_type));
        if((!StringUtil.isEmpty(crud_action_type))&&(!AppServicesConsts.NAVTABS_SERVICEFORMS.equals(crud_action_type))){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM,"jump");
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

        String serviceId = (String) ParamUtil.getSessionAttr(bpc.request, SERVICEID);
        //wait update api url
        List<HcsaSvcSubtypeOrSubsumedDto> checkList= appGrpSvcRelatedInfoService.loadLaboratoryDisciplines(serviceId);
        Map<String,String> map = new HashMap<>();
        map.put("Histocompatibility", "Histocompatibility");
        map.put("Immunology", "Immunology");
        map.put("HIV Testing", "HIV Testing");
        map.put("HIV Screening", "HIV Screening");
        ParamUtil.setSessionAttr(bpc.request, "checkList", (Serializable) checkList);
        ParamUtil.setSessionAttr(bpc.request, "LaboratoryDisciplines", (Serializable) map);
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

        String serviceId = (String) ParamUtil.getSessionAttr(bpc.request, SERVICEID);
        String psnType = "CGO";
        List<HcsaSvcPersonnelDto> cgoList = null;
        if(!StringUtil.isEmpty(serviceId) && !StringUtil.isEmpty(psnType)){
            //min and max count
            cgoList =appGrpSvcRelatedInfoService.getGOSelectInfo(serviceId, psnType);
            ParamUtil.setSessionAttr(bpc.request, GOVERNANCEOFFICERS, (Serializable) cgoList);
        }
        List<SelectOption> cgoSelectList = new ArrayList<>();
        SelectOption sp1 = new SelectOption("new COG", "new CGO");
        cgoSelectList.add(sp1);
        ParamUtil.setSessionAttr(bpc.request, "CgoSelectList", (Serializable) cgoSelectList);
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
        //load cgo select options
        //List disciplines = (List) ParamUtil.getSessionAttr(bpc.request, "CheckedDisciplines");
        List disciplines = new ArrayList();
        disciplines.add("Cytology");
        disciplines.add("HIV Testing");
        Map map = appGrpSvcRelatedInfoService.loadCGOByDisciplines(disciplines);
        ParamUtil.setSessionAttr(bpc.request, "CGOOptions", (Serializable) map);
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
        List poList = appGrpSvcRelatedInfoService.loadPO();
        ParamUtil.setSessionAttr(bpc.request, "POList", (Serializable) poList);
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
        String crud_action_type = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        String crud_action_type_tab = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_TAB);
        String jumpUrl = "/hcsaapplication/eservice/IAIS/MOHCCServiceForms?crud_action_type="+crud_action_type+"&crud_action_type_tab="+crud_action_type_tab;
        if(!AppServicesConsts.NAVTABS_SERVICEFORMS.equals(crud_action_type)){
            jumpUrl = "/hcsaapplication/eservice/IAIS/MOHCCNewApplication/1/Prepare?crud_action_type="+crud_action_type+"&crud_action_type_tab="+crud_action_type_tab;
        }
        log.info(StringUtil.changeForLog("The JumpUrl is -->:"+jumpUrl));
        ParamUtil.setRequestAttr(bpc.request,"jumpToServiceFormUrl",jumpUrl);
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
        String [] checkListIds = bpc.request.getParameterValues("control--runtime--1");

        //save into sub-svc dto
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfoDto(bpc.request);
        //appSvcRelatedInfoDto.setCheckListIds(checkListIds);
        ParamUtil.setSessionAttr(bpc.request, APPSVCRELATEDINFODTO, appSvcRelatedInfoDto);

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
        //get CGO count
        //String cgoCount = ParamUtil.getDate(bpc.request, "cgoCount");

        AppSvcCgoDto appSvcCgoDto = genAppSvcCgoDto(bpc.request);
        List<AppSvcCgoDto> appSvcCgoDtoList = new ArrayList<>();
        appSvcCgoDtoList.add(appSvcCgoDto);
        //save into sub-svc dto
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfoDto(bpc.request);
        appSvcRelatedInfoDto.setAppSvcCgoDtoList(appSvcCgoDtoList);
        ParamUtil.setSessionAttr(bpc.request, APPSVCRELATEDINFODTO, appSvcRelatedInfoDto);


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
        String premisesId = ParamUtil.getString(bpc.request, "premId");
        List<AppSvcDisciplineAllocationDto> daList = new ArrayList<>();

        AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto = new AppSvcDisciplineAllocationDto();
         //Cytology,HIV Testing,.. load from xxxx
        String 	nameStr = new StringBuffer().append(premisesId).append("-").append("Cytology").toString();
        //get Laboratory Disciplines id and	Clinical Governance Officers id
        String [] ids  = ParamUtil.getStrings(bpc.request, nameStr);
//        appSvcDisciplineAllocationDto.setAppGrpPremiseId(premisesId);
        appSvcDisciplineAllocationDto.setChkLstConfId(ids[0]);
        //appSvcDisciplineAllocationDto.setAppSvcCgoId(ids[1]);
        daList.add(appSvcDisciplineAllocationDto);


        //save into sub-svc dto
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfoDto(bpc.request);
        appSvcRelatedInfoDto.setAppSvcDisciplineAllocationDtoList(daList);
        ParamUtil.setSessionAttr(bpc.request, APPSVCRELATEDINFODTO, appSvcRelatedInfoDto);

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


        log.debug(StringUtil.changeForLog("the do doPrincipalOfficers end ...."));
    }

    /**
     * StartStep: doDocuments
     *
     * @param bpc
     * @throws
     */
    public void doDocuments(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doDocuments start ...."));
        List<AppSvcDocDto> AppSvcDocDtoList = new ArrayList<>();
        AppSvcDocDto appSvcDocDto = null;
        MultipartHttpServletRequest mulReq =
                (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        List<MultipartFile> files = null;
        for (Iterator<String> en = mulReq.getFileNames(); en.hasNext(); ) {
            String name = en.next();
            files = mulReq.getFiles(name);
        }
        if(files != null ){
            for(MultipartFile file:files){
                if(!StringUtil.isEmpty(file.getOriginalFilename())){
                    appSvcDocDto = new AppSvcDocDto();
                    appSvcDocDto.setSvcConfDocId("");
                    appSvcDocDto.setFileName(file.getOriginalFilename());
                    //appSvcDocDto.setFileSize(Math.round(file.getSize()/1024));
                    //wait api change to get fileRepoId
                    AppSvcDocDtoList.add(appSvcDocDto);
                }
            }
        }


        AppSvcRelatedInfoDto appSvcRelatedInfoDto = getAppSvcRelatedInfoDto(bpc.request);
        appSvcRelatedInfoDto.setAppSvcDocDtoLit(AppSvcDocDtoList);
        ParamUtil.setSessionAttr(bpc.request, APPSVCRELATEDINFODTO, appSvcRelatedInfoDto);
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
//        String crud_action_type = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
//        //String crud_action_type_tab = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_TAB);
//        if(!AppServicesConsts.NAVTABS_SERVICEFORMS.equals(crud_action_type)){
//            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_FORM,"jump");
//        }
        ParamUtil.setRequestAttr(bpc.request,"Switch2","jumPage");
        log.debug(StringUtil.changeForLog("the do prepareResult end ...."));
    }

    /**
     * ajax
     */
    public void loadGovernanceOfficerByCGOId(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do loadGovernanceOfficerByCGOId start ...."));
        String cgoId = bpc.request.getParameter("");
        if(cgoId == "new"){
            return;
        }
        AppSvcCgoDto governanceOfficersDto = appGrpSvcRelatedInfoService.loadGovernanceOfficerByCgoId(cgoId);
        ParamUtil.setSessionAttr(bpc.request, GOVERNANCEOFFICERSDTO, governanceOfficersDto);
        log.debug(StringUtil.changeForLog("the do loadGovernanceOfficerByCGOId end ...."));
    }



    private AppSvcCgoDto genAppSvcCgoDto(HttpServletRequest request){
        AppSvcCgoDto appSvcCgoDto = new AppSvcCgoDto();
        String salutation = ParamUtil.getString(request, "salutation");
        String name = ParamUtil.getString(request, "name");
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        String designation = ParamUtil.getString(request, "designation");
        String professionType = ParamUtil.getString(request, "professionType");
        String professionRegoType = ParamUtil.getString(request, "professionRegoType");
        String professionRegoNo = ParamUtil.getString(request, "professionRegoNo");
        String specialty = ParamUtil.getString(request, "specialty");
        String specialtyOther = ParamUtil.getString(request, "specialtyOther");
        String qualification = ParamUtil.getString(request, "qualification");
        String mobileNo = ParamUtil.getString(request, "mobileNo");
        String emailAddress = ParamUtil.getString(request, "emailAddress");
        appSvcCgoDto.setSalutation(salutation);
        appSvcCgoDto.setName(name);
        appSvcCgoDto.setIdType(idType);
        appSvcCgoDto.setIdNo(idNo);
        appSvcCgoDto.setDesignation(designation);
        appSvcCgoDto.setProfessionType(professionType);
        appSvcCgoDto.setProfessionRegoType(professionRegoType);
        appSvcCgoDto.setProfessionRegoNo(professionRegoNo);
        appSvcCgoDto.setSpeciality(specialty);
        if("other".equals(specialty)){
            appSvcCgoDto.setSpecialityOther(specialtyOther);
        }
        //qualification
        appSvcCgoDto.setQualification(qualification);
        appSvcCgoDto.setMobileNo(mobileNo);
        appSvcCgoDto.setEmailAddr(emailAddress);

        return appSvcCgoDto;
    }

    private AppSvcPrincipalOfficersDto genAppSvcPrincipalOfficersDto(HttpServletRequest request){
        AppSvcPrincipalOfficersDto dto = new AppSvcPrincipalOfficersDto();
        String salutation = ParamUtil.getString(request, "salutation");
        String name = ParamUtil.getString(request, "name");
        String idType = ParamUtil.getString(request, "idType");
        String idNo = ParamUtil.getString(request, "idNo");
        String designation = ParamUtil.getString(request, "designation");
        String mobileNo = ParamUtil.getString(request, "mobileNo");
        String emailAddress = ParamUtil.getString(request, "emailAddress");
        dto.setSalutation(salutation);
        dto.setName(name);
        dto.setDesignation(designation);
        dto.setMobileNo(mobileNo);
        dto.setEmailAddr(emailAddress);
        return  dto;
    }

    private AppSvcRelatedInfoDto getAppSvcRelatedInfoDto(HttpServletRequest request){
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = (AppSvcRelatedInfoDto) ParamUtil.getSessionAttr(request, APPSVCRELATEDINFODTO);
        if(appSvcRelatedInfoDto == null){
            appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
        }
        return appSvcRelatedInfoDto;
    }



}
