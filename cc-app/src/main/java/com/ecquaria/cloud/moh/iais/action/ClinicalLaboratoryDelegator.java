package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.service.AppGrpSvcRelatedInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator.APPSUBMISSIONDTO;
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
        if(!AppServicesConsts.NAVTABS_SERVICEFORMS.equals(crud_action_type)){
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

        //wait update api url
        //List checkList= appGrpSvcRelatedInfoService.loadLaboratoryDisciplines("Laboratory Disciplines");
        Map<String,String> map = new HashMap<>();
        map.put("Histocompatibility", "Histocompatibility");
        map.put("Immunology", "Immunology");
        map.put("HIV Testing", "HIV Testing");
        map.put("HIV Screening", "HIV Screening");
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
            cgoList =appGrpSvcRelatedInfoService.loadCGOBySvcIdAndPsnType(serviceId, psnType);
            ParamUtil.setSessionAttr(bpc.request, GOVERNANCEOFFICERS, (Serializable) cgoList);
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
        //save

        log.debug(StringUtil.changeForLog("the do doLaboratoryDisciplines end ...."));
    }

    /**
     * StartStep: doGovernanceOfficers
     *
     * @param bpc
     * @throws
     */
    public void doGovernanceOfficers(BaseProcessClass bpc, AppSvcCgoDto dto){
        log.debug(StringUtil.changeForLog("the do doGovernanceOfficers start ...."));
        //get CGO count
        //String cgoCount = ParamUtil.getDate(bpc.request, "cgoCount");
        String salutation = ParamUtil.getDate(bpc.request, "salutation");
        String name = ParamUtil.getDate(bpc.request, "name");
        String idType = ParamUtil.getDate(bpc.request, "idType");
        String idNo = ParamUtil.getDate(bpc.request, "idNo");
        String designation = ParamUtil.getDate(bpc.request, "designation");
        String professionType = ParamUtil.getDate(bpc.request, "professionType");
        String professionRegnNo = ParamUtil.getDate(bpc.request, "professionRegnNo");
        String specialty = ParamUtil.getDate(bpc.request, "specialty");
        String otherSpecialty = ParamUtil.getDate(bpc.request, "otherSpecialty");
        String qualification = ParamUtil.getDate(bpc.request, "qualification");
        String mobileNo = ParamUtil.getDate(bpc.request, "mobileNo");
        String emailAddress = ParamUtil.getDate(bpc.request, "emailAddress");


        AppSvcCgoDto appSvcCgoDto = new AppSvcCgoDto();

        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if(appSubmissionDto == null){
            appSubmissionDto = new AppSubmissionDto();
        }

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


}
