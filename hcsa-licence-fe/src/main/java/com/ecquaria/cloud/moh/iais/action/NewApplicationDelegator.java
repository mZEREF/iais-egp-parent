package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.base.FileType;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.VehNoValidator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.ApplicationValidateDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.sz.commons.util.FileUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.util.CopyUtil;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * egator
 *
 * @author suocheng
 * @date 9/23/2019
 */
@Delegator("newApplicationDelegator")
@Slf4j
public class NewApplicationDelegator {
    private static final String ERRORMAP_PREMISES = "errorMap_premises";
    public static final String PREMISESTYPE = "premisesType";

    public static final String CURRENTSERVICEID = "currentServiceId";
    public static final String CURRENTSVCCODE = "currentSvcCode";
    public static final String APPSUBMISSIONDTO = "AppSubmissionDto";
    public static final String OLDAPPSUBMISSIONDTO = "oldAppSubmissionDto";
    public static final String COMMONHCSASVCDOCCONFIGDTO = "commonHcsaSvcDocConfigDto";
    public static final String PREMHCSASVCDOCCONFIGDTO = "premHcsaSvcDocConfigDto";
    public static final String RELOADAPPGRPPRIMARYDOCMAP = "reloadAppGrpPrimaryDocMap";
    public static final String  APPGRPPRIMARYDOCERRMSGMAP = "appGrpPrimaryDocErrMsgMap";

    public static final String REQUESTINFORMATIONCONFIG  = "requestInformationConfig";
    public static final String ACKSTATUS = "AckStatus";
    public static final String ACKMESSAGE = "AckMessage";
    public static final String SERVICEALLPSNCONFIGMAP = "ServiceAllPsnConfigMap";
    public static final String FIRESTOPTION = "Please Select";
    public static final String LICAPPGRPPREMISESDTOMAP = "LicAppGrpPremisesDtoMap";
    private static final String DRAFTCONFIG = "DraftConfig";
    private static final String GROUPLICENCECONFIG = "GroupLicenceConfig";

    //page name
    public static final String APPLICATION_PAGE_NAME_PREMISES                       = "APPPN01";
    public static final String APPLICATION_PAGE_NAME_PRIMARY                        = "APPPN02";
    public static final String APPLICATION_SVC_PAGE_NAME_LABORATORY                 = "APPSPN01";
    public static final String APPLICATION_SVC_PAGE_NAME_GOVERNANCE_OFFICERS        = "APPSPN02";
    public static final String APPLICATION_SVC_PAGE_NAME_DISCIPLINE_ALLOCATION      = "APPSPN03";
    public static final String APPLICATION_SVC_PAGE_NAME_PRINCIPAL_OFFICERS         = "APPSPN04";
    public static final String APPLICATION_SVC_PAGE_NAME_DEPUTY_PRINCIPAL_OFFICERS  = "APPSPN05";
    public static final String APPLICATION_SVC_PAGE_NAME_DOCUMENT                   = "APPSPN06";
    public static final String APPLICATION_SVC_PAGE_NAME_SERVICE_PERSONNEL          = "APPSPN07";

    //isClickEdit
    public static final String IS_EDIT = "isEdit";

    @Autowired
    private ServiceConfigService serviceConfigService;

    @Autowired
    private AppSubmissionService appSubmissionService;

    @Autowired
    private RequestForChangeService requestForChangeService;


    @Autowired
    private WithOutRenewalService withOutRenewalService;


    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the do Start start ...."));
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
        //clear Session
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, null);

        //Primary Documents
        ParamUtil.setSessionAttr(bpc.request, COMMONHCSASVCDOCCONFIGDTO, null);
        ParamUtil.setSessionAttr(bpc.request, PREMHCSASVCDOCCONFIGDTO, null);
        ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, null);
        ParamUtil.setSessionAttr(bpc.request,DRAFTCONFIG,null);

        Map<String,String> coMap=new HashMap<>();
        coMap.put("premises","");
        coMap.put("document","");
        coMap.put("information","");
        coMap.put("previewli","");
        bpc.request.getSession().setAttribute("coMap",coMap);

        //request For Information Loading
        ParamUtil.setSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG,null);
        requestForChangeOrRenewLoading(bpc);
        //renewLicence(bpc);
        requestForInformationLoading(bpc);
        //for loading the draft by appId
        loadingDraft(bpc);
        //for loading Service Config
        boolean flag = loadingServiceConfig(bpc);
        log.info(StringUtil.changeForLog("The loadingServiceConfig -->:"+flag));
        if(flag){
            initSession(bpc);
        }
        log.info(StringUtil.changeForLog("the do Start end ...."));
    }


    /**
     * StartStep: Prepare
     *
     * @param bpc
     * @throws
     */
    public void prepare(BaseProcessClass bpc) {
         log.info(StringUtil.changeForLog("the do prepare start ...."));
        //String action = ParamUtil.getRequestString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        String action = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (StringUtil.isEmpty(action)) {
            action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
            if (StringUtil.isEmpty(action) || "validation".equals(action)) {
                //first
                action = "premises";
            }
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, action);
        log.info(StringUtil.changeForLog("the do prepare end ...."));
    }
    /**
     * StartStep: PreparePremises
     *
     * @param bpc
     * @throws
     */
    public void preparePremises(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do preparePremises start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        //get svcCode to get svcId
        List<HcsaServiceDto> hcsaServiceDtoList = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
        List<String> svcIds = IaisCommonUtils.genNewArrayList();
        if (hcsaServiceDtoList != null) {
            hcsaServiceDtoList.forEach(item -> svcIds.add(item.getId()));
        }
        List premisesSelect = new ArrayList<SelectOption>();
        List conveyancePremSel = new ArrayList<SelectOption>();
        String licenseeId = appSubmissionDto.getLicenseeId();
        log.info(StringUtil.changeForLog("The preparePremises licenseeId is -->:"+licenseeId));
        Map<String,AppGrpPremisesDto> licAppGrpPremisesDtoMap = null;
        if(!StringUtil.isEmpty(licenseeId)){
            licAppGrpPremisesDtoMap = serviceConfigService.getAppGrpPremisesDtoByLoginId(licenseeId);
        }
        SelectOption sp0 = new SelectOption("-1", FIRESTOPTION);
        premisesSelect.add(sp0);
        SelectOption sp1 = new SelectOption("newPremise", "Add a new premises");
        premisesSelect.add(sp1);
        SelectOption cps1 = new SelectOption("-1", FIRESTOPTION);
        SelectOption cps2 = new SelectOption("newPremise", "Add a new premises");
        conveyancePremSel.add(cps1);
        conveyancePremSel.add(cps2);
        if (licAppGrpPremisesDtoMap != null && !licAppGrpPremisesDtoMap.isEmpty()) {
            for (AppGrpPremisesDto item : licAppGrpPremisesDtoMap.values()) {
                SelectOption sp2 = new SelectOption(item.getPremisesSelect(), item.getAddress());
                if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(item.getPremisesType())) {
                    premisesSelect.add(sp2);
                }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(item.getPremisesType())){
                    conveyancePremSel.add(sp2);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request, LICAPPGRPPREMISESDTOMAP, (Serializable) licAppGrpPremisesDtoMap);
        ParamUtil.setSessionAttr(bpc.request, "premisesSelect", (Serializable) premisesSelect);
        ParamUtil.setSessionAttr(bpc.request, "conveyancePremSel", (Serializable) conveyancePremSel);

        //addressType
        List<SelectOption> addrTypeOpt = new ArrayList<>();
        SelectOption addrTypeSp = new SelectOption("",FIRESTOPTION);
        addrTypeOpt.add(addrTypeSp);
        addrTypeOpt.addAll(MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ADDRESS_TYPE));
        ParamUtil.setRequestAttr(bpc.request,"addressType",addrTypeOpt);
        //get premises type
        if (!IaisCommonUtils.isEmpty(svcIds)) {
            log.info(StringUtil.changeForLog("svcId not null"));
            Set<String> premisesType = serviceConfigService.getAppGrpPremisesTypeBySvcId(svcIds);
            ParamUtil.setSessionAttr(bpc.request, PREMISESTYPE, (Serializable) premisesType);
        }else{
           log.error(StringUtil.changeForLog("do not have select the services"));
        }

        //reload dateTime
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        if(!IaisCommonUtils.isEmpty(appGrpPremisesDtoList)){
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtoList){
                appGrpPremisesDto = NewApplicationHelper.setWrkTime(appGrpPremisesDto);
            }
        }

        //
        int baseSvcCount = 0;
        for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtoList){
            if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equalsIgnoreCase(hcsaServiceDto.getSvcType())) {
                baseSvcCount++;
            }
        }
        if(baseSvcCount>1 ){
            ParamUtil.setRequestAttr(bpc.request, "multiBase", AppConsts.TRUE);
        }else{
            ParamUtil.setRequestAttr(bpc.request, "multiBase", AppConsts.FALSE);
        }
        //when rfc/renew check is select existing premises
        String appType = appSubmissionDto.getAppType();
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO);
            if(appSubmissionDto.getAppGrpPremisesDtoList().size() == oldAppSubmissionDto.getAppGrpPremisesDtoList().size()){
                int length = appSubmissionDto.getAppGrpPremisesDtoList().size();
                for(int i=0;i<length;i++){
                    AppGrpPremisesDto appGrpPremisesDto = appSubmissionDto.getAppGrpPremisesDtoList().get(i);
                    AppGrpPremisesDto oldAppGrpPremisesDto = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(i);
                    if(appGrpPremisesDto != null && oldAppGrpPremisesDto != null){
                        String premSel = appGrpPremisesDto.getPremisesSelect();
                        String oldPremSel = oldAppGrpPremisesDto.getPremisesSelect();
                        if(oldPremSel.equals(premSel) || "-1".equals(premSel)){
                            ParamUtil.setRequestAttr(bpc.request,"PageCanEdit",AppConsts.TRUE);
                        }
                    }
                }
            }
        }



        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
        log.info(StringUtil.changeForLog("the do preparePremises end ...."));
    }

    /**
     * StartStep: PrepareDocuments
     *
     * @param bpc
     * @throws
     */
    public void prepareDocuments(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareDocuments start ...."));

        String currentSvcId = (String) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.CURRENTSERVICEID);
        List<HcsaSvcDocConfigDto> hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(null);
        if (hcsaSvcDocDtos != null) {
            List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigDto = IaisCommonUtils.genNewArrayList();
            List<HcsaSvcDocConfigDto> premHcsaSvcDocConfigDto = IaisCommonUtils.genNewArrayList();
            for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:hcsaSvcDocDtos){
                if("0".equals(hcsaSvcDocConfigDto.getDupForPrem())){
                    commonHcsaSvcDocConfigDto.add(hcsaSvcDocConfigDto);
                }else if("1".equals(hcsaSvcDocConfigDto.getDupForPrem())){
                    premHcsaSvcDocConfigDto.add(hcsaSvcDocConfigDto);
                }
            }
            ParamUtil.setSessionAttr(bpc.request, COMMONHCSASVCDOCCONFIGDTO, (Serializable) commonHcsaSvcDocConfigDto);
            ParamUtil.setSessionAttr(bpc.request, PREMHCSASVCDOCCONFIGDTO, (Serializable) premHcsaSvcDocConfigDto);
        }

        //reload page
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
        if(appGrpPrimaryDocDtoList != null && appGrpPrimaryDocDtoList.size()>0){
            Map<String,AppGrpPrimaryDocDto> reloadDocMap = new HashMap();
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtoList){
                reloadDocMap.put(appGrpPrimaryDocDto.getPrimaryDocReloadName(), appGrpPrimaryDocDto);
            }
            ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, (Serializable) reloadDocMap);
        }else{
            ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, (Serializable) new HashMap());
        }

        log.info(StringUtil.changeForLog("the do prepareDocuments end ...."));
    }



    /**
     * StartStep: PrepareForms
     *
     * @param bpc
     * @throws
     */
    public void prepareForms(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareForms start ...."));

        log.info(StringUtil.changeForLog("the do prepareForms end ...."));
    }

    /**
     * StartStep: PreparePreview
     *
     * @param bpc
     * @throws
     */
    public void preparePreview(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do preparePreview start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        List<AppGrpPremisesDto> appGrpPremisesDto = appSubmissionDto.getAppGrpPremisesDtoList();
        List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST);
        if(!IaisCommonUtils.isEmpty(appGrpPremisesDto) && !IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            int premCount = appGrpPremisesDto.size();
            int svcCount = appGrpPremisesDto.size();
            log.info(StringUtil.changeForLog("premises count:"+premCount+" ,service count:"+svcCount));
            if(premCount >1 && svcCount == 1){
                //multi prem one svc
                ParamUtil.setRequestAttr(bpc.request,GROUPLICENCECONFIG,"test");
            }
        }

        log.info(StringUtil.changeForLog("the do preparePreview end ...."));
    }

    /**
     * StartStep: PreparePayment
     *
     * @param bpc
     * @throws
     */
    public void preparePayment(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do preparePayment start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        AppSubmissionDto tranferSub = (AppSubmissionDto)ParamUtil.getSessionAttr(bpc.request,"app-rfc-tranfer");
        if(tranferSub!=null){
            appSubmissionDto=tranferSub;
        }
        if(!StringUtil.isEmpty(appSubmissionDto.getAmount())){
            String amountStr = Formatter.formatterMoney(appSubmissionDto.getAmount());
            log.info(StringUtil.changeForLog("The amountStr is -->:"+amountStr));
            appSubmissionDto.setAmountStr(amountStr);
        }
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
        log.info(StringUtil.changeForLog("the do preparePayment end ...."));
    }

    /**
     * StartStep: DoPremises
     *
     * @param bpc
     * @throws
     */
    public void doPremises(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doPremises start ...."));

        //gen dto
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);

        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);

        if("back".equals(action)||RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "jump");
            return;
        }
       /* if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
            if(RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)) {
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "jump");
                return;
            }
        }*/

        String isEdit = ParamUtil.getString(bpc.request, IS_EDIT);
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG);
        boolean isRfi = false;
        if(requestInformationConfig != null){
            isRfi = true;
        }
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_PREMISES_INFORMATION, isEdit, isRfi);
        log.info("isGetDataFromPage:"+isGetDataFromPage);
        if(isGetDataFromPage ){
            List<AppGrpPremisesDto> appGrpPremisesDtoList = genAppGrpPremisesDtoList(bpc.request);
            appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
            if(appSubmissionDto.isNeedEditController()){
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null? IaisCommonUtils.genNewHashSet() :appSubmissionDto.getClickEditPage();
                clickEditPages.add(APPLICATION_PAGE_NAME_PREMISES);
                appSubmissionDto.setClickEditPage(clickEditPages);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                appEditSelectDto.setPremisesEdit(true);
                appSubmissionDto.setChangeSelectDto(appEditSelectDto);
            }


            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            String crud_action_value = ParamUtil.getString(bpc.request, "crud_action_value");
            if(!"saveDraft".equals(crud_action_value)){
                Map<String, String> errorMap= doValidatePremiss(bpc);
                if(errorMap.size()>0){
                    ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"premises");
                    Map<String,String> coMap=(Map<String, String>) bpc.request.getSession().getAttribute("coMap");
                    coMap.put("premises","");
                    bpc.request.getSession().setAttribute("coMap",coMap);
                }else {
                    Map<String,String> coMap=(Map<String, String>) bpc.request.getSession().getAttribute("coMap");
                    coMap.put("premises","premises");
                    bpc.request.getSession().setAttribute("coMap",coMap);
                }
            }

        }


        log.info(StringUtil.changeForLog("the do doPremises end ...."));
    }



    /**
     * StartStep: DoDocument
     *
     * @param bpc
     * @throws
     */
    public void doDocument(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do doDocument start ...."));
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);

        AppGrpPrimaryDocDto appGrpPrimaryDocDto = null;
        CommonsMultipartFile file = null;
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);

        String action = ParamUtil.getRequestString(bpc.request, "crud_action_value");
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
            if(RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)) {
                //clear
                ParamUtil.setSessionAttr(bpc.request,RELOADAPPGRPPRIMARYDOCMAP,null);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "jump");
                return;
            }
        }

        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG);
        boolean isRfi = false;
        if(requestInformationConfig != null){
            isRfi = true;
        }
        String isEdit = ParamUtil.getString(mulReq, IS_EDIT);
        boolean isGetDataFromPage = NewApplicationHelper.isGetDataFromPage(appSubmissionDto, ApplicationConsts.REQUEST_FOR_CHANGE_TYPE_SUPPORTING_DOCUMENT, isEdit, isRfi);


        if(isGetDataFromPage){
            List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, COMMONHCSASVCDOCCONFIGDTO);
            List<HcsaSvcDocConfigDto> premHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>) ParamUtil.getSessionAttr(bpc.request, PREMHCSASVCDOCCONFIGDTO);
            List<AppGrpPremisesDto> appGrpPremisesList = appSubmissionDto.getAppGrpPremisesDtoList();
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = IaisCommonUtils.genNewArrayList();
            Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
            Map<String,AppGrpPrimaryDocDto> beforeReloadDocMap = (Map<String, AppGrpPrimaryDocDto>) ParamUtil.getSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP);
            if(appSubmissionDto.isNeedEditController()){
                Set<String> clickEditPages = appSubmissionDto.getClickEditPage() == null ? IaisCommonUtils.genNewHashSet() : appSubmissionDto.getClickEditPage();
                clickEditPages.add(NewApplicationDelegator.APPLICATION_PAGE_NAME_PRIMARY);
                appSubmissionDto.setClickEditPage(clickEditPages);
                AppEditSelectDto appEditSelectDto = appSubmissionDto.getChangeSelectDto();
                appEditSelectDto.setDpoEdit(true);
                appSubmissionDto.setChangeSelectDto(appEditSelectDto);
            }

            Map<String,CommonsMultipartFile> commonsMultipartFileMap = IaisCommonUtils.genNewHashMap();
            for(HcsaSvcDocConfigDto comm:commonHcsaSvcDocConfigList){
                String name = "common"+comm.getId();
                file = (CommonsMultipartFile) mulReq.getFile(name);
                String delFlag = name+"flag";
                String delFlagValue =  mulReq.getParameter(delFlag);
                if(file != null && file.getSize() != 0){
                    if (!StringUtil.isEmpty(file.getOriginalFilename())) {
                        file.getFileItem().setFieldName("selectedFile");
                        appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
                        appGrpPrimaryDocDto.setSvcComDocId(comm.getId());
                        appGrpPrimaryDocDto.setDocName(file.getOriginalFilename());
                        appGrpPrimaryDocDto.setRealDocSize(file.getSize());
                        long size = file.getSize() / 1024;
                        appGrpPrimaryDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                        String md5Code = FileUtil.genMd5FileChecksum(file.getBytes());
                        appGrpPrimaryDocDto.setMd5Code(md5Code);
                        //if  common ==> set null
                        appGrpPrimaryDocDto.setPremisessName("");
                        appGrpPrimaryDocDto.setPremisessType("");
                        commonsMultipartFileMap.put(comm.getId(), file);
                        appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto);

                    }
                }else if("N".equals(delFlagValue)){
                    AppGrpPrimaryDocDto beforeDto = beforeReloadDocMap.get(name);
                    if(beforeDto != null){
                        appGrpPrimaryDocDtoList.add(beforeDto);
                    }
                } else{
                    if(comm.getIsMandatory()){
                        errorMap.put(name, "UC_CHKLMD001_ERR001");
                    }
                }
            }
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesList){
                for(HcsaSvcDocConfigDto prem:premHcsaSvcDocConfigList){

                    String premisesIndexNo = "";
                    if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
                        premisesIndexNo = appGrpPremisesDto.getHciName();
                    }else  if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
                        premisesIndexNo = appGrpPremisesDto.getConveyanceVehicleNo();
                    }
                    String name = "prem"+prem.getId()+premisesIndexNo;
                    file = (CommonsMultipartFile) mulReq.getFile(name);
                    String delFlag = name+"flag";
                    String delFlagValue =  mulReq.getParameter(delFlag);
                    if(file != null && file.getSize() != 0){
                        if (!StringUtil.isEmpty(file.getOriginalFilename())) {
                            file.getFileItem().setFieldName("selectedFile");
                            appGrpPrimaryDocDto = new AppGrpPrimaryDocDto();
                            appGrpPrimaryDocDto.setSvcComDocId(prem.getId());
                            appGrpPrimaryDocDto.setDocName(file.getOriginalFilename());
                            appGrpPrimaryDocDto.setRealDocSize(file.getSize());
                            long size = file.getSize() / 1024;
                            appGrpPrimaryDocDto.setDocSize(Integer.valueOf(String.valueOf(size)));
                            String md5Code = FileUtil.genMd5FileChecksum(file.getBytes());
                            appGrpPrimaryDocDto.setMd5Code(md5Code);
                            appGrpPrimaryDocDto.setPremisessName(premisesIndexNo);
                            appGrpPrimaryDocDto.setPremisessType(appGrpPremisesDto.getPremisesType());
                            commonsMultipartFileMap.put(premisesIndexNo+prem.getId(), file);
                            appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto);
                        }
                    }else if("N".equals(delFlagValue)){
                        AppGrpPrimaryDocDto beforeDto = (AppGrpPrimaryDocDto) beforeReloadDocMap.get(name);
                        if(beforeDto != null){
                            appGrpPrimaryDocDtoList.add(beforeDto);
                        }
                    } else{
                        if(prem.getIsMandatory()) {
                            errorMap.put(name, "UC_CHKLMD001_ERR001");
                        }
                    }
                }
            }
            //set value into AppSubmissionDto
            appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtoList);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);


            String crud_action_values = ParamUtil.getRequestString(bpc.request, "crud_action_value");
            if("next".equals(crud_action_values)){
                documentValid(bpc.request, errorMap);
                doIsCommom(bpc.request, errorMap);
                Map<String,String> coMap=(Map<String, String>)bpc.request.getSession().getAttribute("coMap");
                if(errorMap.isEmpty()){
                    coMap.put("document","document");
                }else {
                    coMap.put("document","");
                }

                bpc.request.getSession().setAttribute("coMap",coMap);
            }
            if(errorMap.size()>0){
                ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setSessionAttr(bpc.request, APPGRPPRIMARYDOCERRMSGMAP, (Serializable) errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "documents");
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE, "documents");
                return;
            }

            if( commonsMultipartFileMap!= null && commonsMultipartFileMap.size()>0){
                for(AppGrpPrimaryDocDto primaryDoc:appGrpPrimaryDocDtoList){
                    String key = primaryDoc.getPremisessName()+primaryDoc.getSvcComDocId();
                    CommonsMultipartFile commonsMultipartFile = commonsMultipartFileMap.get(key);
                    if(commonsMultipartFile != null && commonsMultipartFile.getSize() != 0){
                        String fileRepoGuid = serviceConfigService.saveFileToRepo(commonsMultipartFile);
                        primaryDoc.setFileRepoId(fileRepoGuid);
                    }
                }
            }

        }



        log.info(StringUtil.changeForLog("the do doDocument end ...."));
    }

    private void doIsCommom(HttpServletRequest request, Map<String, String> errorMap) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        CommonsMultipartFile file ;
        List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>)   request.getSession().getAttribute(COMMONHCSASVCDOCCONFIGDTO);
        for(HcsaSvcDocConfigDto comm : commonHcsaSvcDocConfigList){
            String name = "common"+comm.getId();
            file = (CommonsMultipartFile) mulReq.getFile(name);
            Boolean isMandatory = comm.getIsMandatory();
            if(isMandatory&&file.getSize()==0){
                errorMap.put(name, "UC_CHKLMD001_ERR001");
            }

        }

    }


    /**
     * StartStep: doForms
     *
     * @param bpc
     * @throws
     */
    public void doForms(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doForms start ...."));

        log.info(StringUtil.changeForLog("the do doForms end ...."));
    }

    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    public void doPreview(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doPreview start ...."));

        log.info(StringUtil.changeForLog("the do doPreview end ...."));
    }

    /**
     * StartStep: doPreview
     *
     * @param bpc
     * @throws
     */
    public void doPayment(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doPayment start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String switch2 = "loading";
        String pmtMethod = appSubmissionDto.getPaymentMethod();
        if(StringUtil.isEmpty(pmtMethod)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "payment");
            return;
        }
        if(!StringUtil.isEmpty(pmtMethod) && "GIRO".equals(pmtMethod)){
            switch2 = "ack";
            String txnDt = DateUtil.formatDate(new Date(), "yyyy-MM-dd");
            ParamUtil.setRequestAttr(bpc.request,"txnDt",txnDt);
        }
        String result = bpc.request.getParameter("result");
        if (!StringUtil.isEmpty(result)) {
            log.info(StringUtil.changeForLog("payment result:" + result));
            String pmtRefNo = bpc.request.getParameter("reqRefNo");
            if ("success".equals(result) && !StringUtil.isEmpty(pmtRefNo)) {
                log.info("credit card payment success");
                String txnDt = ParamUtil.getString(bpc.request,"txnDt");
                String txnRefNo = ParamUtil.getString(bpc.request,"txnRefNo");
                ParamUtil.setRequestAttr(bpc.request,"txnDt",txnDt);
                ParamUtil.setRequestAttr(bpc.request,"txnRefNo",txnRefNo);
                switch2 = "ack";
                //update status
                String appGrpId = appSubmissionDto.getAppGrpId();
                ApplicationGroupDto appGrp = new ApplicationGroupDto();
                appGrp.setId(appGrpId);
                appGrp.setPmtRefNo(pmtRefNo);
                appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                serviceConfigService.updatePaymentStatus(appGrp);
            }
        }

        if("ack".equals(switch2)){
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "payment success !!!");
        }

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, switch2);
        log.info(StringUtil.changeForLog("the do doPayment end ...."));
    }

    /**
     * StartStep: preInvoke
     *
     * @param bpc
     * @throws
     */
    public void preInvoke(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do preInvoke start ...."));
        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!StringUtil.isEmpty(action)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE_VALUE,action);
        }

        log.info(StringUtil.changeForLog("the do preInvoke start ...."));
    }

    /**
     * StartStep: doSaveDraft
     *
     * @param bpc
     * @throws
     */
    public void doSaveDraft(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do doSaveDraft start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if(StringUtil.isEmpty(appSubmissionDto.getDraftNo())){
            String draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            log.info(StringUtil.changeForLog("the draftNo -->:") + draftNo);
            appSubmissionDto.setDraftNo(draftNo);
        }
        appSubmissionDto = appSubmissionService.doSaveDraft(appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        log.info(StringUtil.changeForLog("the do doSaveDraft end ...."));
    }

    /**
     * StartStep: doReDquestInformationSubmit
     *
     * @param bpc
     * @throws
     */
    public void doRequestInformationSubmit(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do doRequestInformationSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, OLDAPPSUBMISSIONDTO);

        log.info("appSubmissionDto:"+appSubmissionDto.toString());
        log.info("oldAppSubmissionDto:"+oldAppSubmissionDto.toString());
        Map<String, String> doComChangeMap = doComChange(appSubmissionDto,oldAppSubmissionDto);
        if(!doComChangeMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"Msg",doComChangeMap);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"preview");
            ParamUtil.setRequestAttr(bpc.request,"isrfiSuccess","N");
            return;
        }
        log.info("doComChange is ok ...");
        Map<String, String> map = doPreviewAndSumbit(bpc);
        if(!map.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"Msg",map);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"preview");
            ParamUtil.setRequestAttr(bpc.request,"isrfiSuccess","N");
            return;
        }
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        oldAppSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppSubmissionRequestInformationDto appSubmissionRequestInformationDto = new AppSubmissionRequestInformationDto();
        appSubmissionRequestInformationDto.setAppSubmissionDto(appSubmissionDto);
        appSubmissionRequestInformationDto.setOldAppSubmissionDto(oldAppSubmissionDto);
        appSubmissionDto = appSubmissionService.submitRequestInformation(appSubmissionRequestInformationDto, bpc.process);
        // ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request,"isrfiSuccess","Y");
        ParamUtil.setRequestAttr(bpc.request,ACKMESSAGE,"The request for information save success");
        log.info(StringUtil.changeForLog("the do doRequestInformationSubmit end ...."));
    }


    public void doRenewSubmit(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do doRenewSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        List<ApplicationDto> applicationDtos = requestForChangeService.getAppByLicIdAndExcludeNew(appSubmissionDto.getLicenceId());
       /* if(!IaisCommonUtils.isEmpty(applicationDtos)){
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "Y");
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "error");
            ParamUtil.setRequestAttr(bpc.request,"content","There is  ongoing application for the licence");
            return ;
        }*/
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, OLDAPPSUBMISSIONDTO);

        boolean isAutoRfc = compareAndSendEmail(appSubmissionDto, oldAppSubmissionDto);
        appSubmissionDto.setAutoRfc(isAutoRfc);
        Map<String, String> map = doPreviewAndSumbit(bpc);
        if(!map.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"isrfiSuccess","N");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"preview");
            return;
        }

        String draftNo = appSubmissionDto.getDraftNo();
        if(StringUtil.isEmpty(draftNo)){
            draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            appSubmissionDto.setDraftNo(draftNo);
        }
        //get appGroupNo
        String appGroupNo = appSubmissionService.getGroupNo(appSubmissionDto.getAppType());
        log.info(StringUtil.changeForLog("the appGroupNo is -->:") + appGroupNo);
        appSubmissionDto.setAppGrpNo(appGroupNo);
        //get Amount
        FeeDto feeDto = appSubmissionService.getGroupAmount(appSubmissionDto);
        appSubmissionDto.setFeeInfoDtos(feeDto.getFeeInfoDtos());
        Double amount = feeDto.getTotal();
        log.info(StringUtil.changeForLog("the amount is -->:") + amount);
        appSubmissionDto.setAmount(amount);
        //judge is the preInspection
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }

        //set Risk Score
        appSubmissionService.setRiskToDto(appSubmissionDto);

        appSubmissionDto = appSubmissionService.submitRenew(appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);

        //back to renewal licence view page
        ParamUtil.setRequestAttr(bpc.request,"isrfiSuccess","N");
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"jump");
        ParamUtil.setRequestAttr(bpc.request,"jumpPmt","Y");

        log.info(StringUtil.changeForLog("the do doRenewSubmit end ...."));
    }




    /**
     * StartStep: doRequestForChangeSubmit
     *
     * @param bpc
     * @throws
     */
    public void doRequestForChangeSubmit(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do doRequestForChangeSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        List<ApplicationDto> applicationDtos = requestForChangeService.getAppByLicIdAndExcludeNew(appSubmissionDto.getLicenceId());
        //todo chnage edit
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setServiceEdit(true);
        appEditSelectDto.setPremisesEdit(true);
        appEditSelectDto.setDocEdit(true);
        appEditSelectDto.setPoEdit(true);
        appEditSelectDto.setDocEdit(true);
        appSubmissionDto.setAppEditSelectDto(appEditSelectDto);

       /* Map<String, String> map = doPreviewAndSumbit(bpc);
        if(!map.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"Msg",map);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"preview");
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "N");
            return;
        }*/


        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, OLDAPPSUBMISSIONDTO);

        boolean isAutoRfc = compareAndSendEmail(appSubmissionDto, oldAppSubmissionDto);
        if(!IaisCommonUtils.isEmpty(applicationDtos)){
            ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "Y");
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "error");
            ParamUtil.setRequestAttr(bpc.request,"content",MessageUtil.getMessageDesc("ERRRFC001"));
            return ;
        }
        appSubmissionDto.setAutoRfc(isAutoRfc);
        String draftNo = appSubmissionDto.getDraftNo();
        if(StringUtil.isEmpty(draftNo)){
            draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            appSubmissionDto.setDraftNo(draftNo);
        }
        //appSubmissionDto =checkAndSetData(bpc.request, appSubmissionDto);
        //get appGroupNo
        String appGroupNo = appSubmissionService.getGroupNo(appSubmissionDto.getAppType());
        log.info(StringUtil.changeForLog("the appGroupNo is -->:") + appGroupNo);
        appSubmissionDto.setAppGrpNo(appGroupNo);
        AmendmentFeeDto amendmentFeeDto = getAmendmentFeeDto(appSubmissionDto, oldAppSubmissionDto);
        if(!amendmentFeeDto.getChangeInHCIName() && !amendmentFeeDto.getChangeInLocation()){
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                    appGrpPremisesDto.setNeedNewLicNo(false);
                }
            }
        }
        FeeDto  feeDto = appSubmissionService.getGroupAmendAmount(amendmentFeeDto);
        double amount = feeDto.getTotal();
        log.info(StringUtil.changeForLog("the amount is -->:") + amount);
        appSubmissionDto.setAmount(amount);
        //judge is the preInspection
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }
        //set Risk Score
        appSubmissionService.setRiskToDto(appSubmissionDto);

        appSubmissionDto = appSubmissionService.submitRequestChange(appSubmissionDto, bpc.process);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        String isrfiSuccess = "N";
        if(isAutoRfc){
            //change pmt status for carry file
            String appGrpId = appSubmissionDto.getAppGrpId();
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
            serviceConfigService.updatePaymentStatus(appGrp);
            isrfiSuccess = "Y";
            ParamUtil.setRequestAttr(bpc.request,ACKMESSAGE,"The request for change save success");
        }
        ParamUtil.setRequestAttr(bpc.request,"isrfiSuccess",isrfiSuccess);


        log.info(StringUtil.changeForLog("the do doRequestForChangeSubmit start ...."));
    }



    /**
     * StartStep: doSubmit
     *
     * @param bpc
     * @throws
     */
    public void doSubmit(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do doSubmit start ...."));

        Map<String, String> map = doPreviewAndSumbit(bpc);
        if(!map.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request,"Msg",map);
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"preview");
            Map<String,String> coMap=(Map<String, String>)bpc.request.getSession().getAttribute("coMap");
            coMap.put("previewli","");
            bpc.request.getSession().setAttribute("coMap",coMap);
            return;
        }else {
            Map<String,String> coMap=(Map<String, String>)bpc.request.getSession().getAttribute("coMap");
            coMap.put("previewli","previewli");
            bpc.request.getSession().setAttribute("coMap",coMap);
        }
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);

        String isGroupLic = ParamUtil.getString(bpc.request,"isGroupLic");
        if(!StringUtil.isEmpty(isGroupLic) && AppConsts.YES.equals(isGroupLic)){
            appSubmissionDto.setGroupLic(true);
        }

        String draftNo = appSubmissionDto.getDraftNo();
        if(StringUtil.isEmpty(draftNo)){
            draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            appSubmissionDto.setDraftNo(draftNo);
        }
        //get appGroupNo
        String appGroupNo = appSubmissionService.getGroupNo(appSubmissionDto.getAppType());
        log.info(StringUtil.changeForLog("the appGroupNo is -->:") + appGroupNo);
        appSubmissionDto.setAppGrpNo(appGroupNo);
        //get Amount
        FeeDto feeDto = appSubmissionService.getGroupAmount(appSubmissionDto);
        appSubmissionDto.setFeeInfoDtos(feeDto.getFeeInfoDtos());
        Double amount = feeDto.getTotal();
        log.info(StringUtil.changeForLog("the amount is -->:") + amount);
        appSubmissionDto.setAmount(amount);
        //judge is the preInspection
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }

        //set Risk Score
        appSubmissionService.setRiskToDto(appSubmissionDto);

        appSubmissionDto = appSubmissionService.submit(appSubmissionDto, bpc.process);
        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);

        //get wrokgroup
        log.info(StringUtil.changeForLog("the do doSubmit end ...."));
    }




    /**
     * StartStep: ControlSwitch
     *
     * @param bpc
     * @throws
     */
    public void controlSwitch(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do controlSwitch start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String switch2 = "loading";
        String crudActionValue = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if (StringUtil.isEmpty(crudActionValue)) {
            crudActionValue = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        }
        Object requestInformationConfig = ParamUtil.getSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG);
        if ("saveDraft".equals(crudActionValue) || "ack".equals(crudActionValue)) {
            switch2 = crudActionValue;
        }else if("doSubmit".equals(crudActionValue)){
            if(requestInformationConfig == null){
                switch2 = crudActionValue;
                if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
                    switch2 = "requstChange";
                }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())){
                    switch2 = "renew";
                }
            }else{
                switch2 = "information";
            }

        }
        ParamUtil.setRequestAttr(bpc.request, "Switch2", switch2);
        log.info(StringUtil.changeForLog("the do controlSwitch end ...."));

    }
    /**
     * StartStep: ControlSwitch
     *
     * @param bpc
     * @throws
     */
    public void jumpBank(BaseProcessClass bpc) throws IOException {
        log.info(StringUtil.changeForLog("the do jumpBank start ...."));
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        if(StringUtil.isEmpty(payMethod)){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE,"payment");
            return;
        }
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        String a=appSubmissionDto.getPaymentMethod();
        appSubmissionDto.setPaymentMethod(payMethod);
        ParamUtil.setSessionAttr(bpc.request,APPSUBMISSIONDTO,appSubmissionDto);
        if("Credit".equals(payMethod)){
            //send email
            inspectionDateSendNewApplicationPaymentOnlineEmail(appSubmissionDto,bpc);
            StringBuffer url = new StringBuffer();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/payment-web/eservice/INTERNET/PaymentRequest")
                    .append("?amount=").append(appSubmissionDto.getAmount())
                    .append("&payMethod=").append(payMethod)
                    .append("&reqNo=").append(appSubmissionDto.getAppGrpNo());
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            return;
        }else if("GIRO".equals(payMethod)){
            String appGrpId = appSubmissionDto.getAppGrpId();
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_GIRO);
            serviceConfigService.updatePaymentStatus(appGrp);
            ParamUtil.setRequestAttr(bpc.request, "PmtStatus", "GIRO");
        }
        log.info(StringUtil.changeForLog("the do jumpBank end ...."));
    }

    /**
     * StartStep: PrePareErrorAckPage
     *
     * @param bpc
     * @throws
     */
    public void prepareErrorAck(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do prepareErrorAck start ...."));

        log.info(StringUtil.changeForLog("the do prepareErrorAck end ...."));
    }

    public void doErrorAck(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do doErrorAck start ...."));


        log.info(StringUtil.changeForLog("the do doErrorAck end ...."));
    }



    /**
     * StartStep: PrepareAckPage
     *
     * @param bpc
     * @throws
     */
    public void prepareAckPage(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareAckPage start ...."));

        log.info(StringUtil.changeForLog("the do prepareAckPage end ...."));
    }


    /**
     * StartStep: prepareJump
     *
     * @param bpc
     * @throws
     */
    public void prepareJump(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do prepareJump start ...."));
        String action = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if(StringUtil.isEmpty(action)){
            action = ParamUtil.getRequestString(bpc.request, "nextStep");
        }
        if(RfcConst.RFC_BTN_OPTION_UNDO_ALL_CHANGES.equals(action)){
            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.OLDAPPSUBMISSIONDTO);
            AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
            AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
            if(appEditSelectDto.isPremisesEdit()){
                appSubmissionDto.setAppGrpPremisesDtoList(oldAppSubmissionDto.getAppGrpPremisesDtoList());
            }
            if(appEditSelectDto.isDocEdit()){
                appSubmissionDto.setAppGrpPrimaryDocDtos(oldAppSubmissionDto.getAppGrpPrimaryDocDtos());
            }
            if(appEditSelectDto.isServiceEdit()){
                appSubmissionDto.setAppSvcRelatedInfoDtoList(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList());
            }
            ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.APPSUBMISSIONDTO,appSubmissionDto);
        }

        log.info(StringUtil.changeForLog("the do prepareJump end ...."));
    }



    //=============================================================================
    //private method
    //=============================================================================

    private void inspectionDateSendNewApplicationPaymentOnlineEmail(AppSubmissionDto appSubmissionDto,BaseProcessClass bpc) {
        MsgTemplateDto msgTemplateDto = appSubmissionService.getMsgTemplateById(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_PAYMENT_ONLINE_ID);
        if(msgTemplateDto != null) {
            Double amount = appSubmissionDto.getAmount();
            String licenseeId = appSubmissionDto.getLicenseeId();
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST);
            List<String> serviceNames = new ArrayList<String>();
            for(HcsaServiceDto hcsaServiceDto : hcsaServiceDtos){
                String svcName = hcsaServiceDto.getSvcName();
                if(!StringUtil.isEmpty(svcName)){
                    serviceNames.add(svcName);
                }
            }
            String appGrpNo = appSubmissionDto.getAppGrpNo();
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            map.put("serviceNames", serviceNames);
            map.put("paymentAmount",Formatter.formatNumber(amount));
            map.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
            String mesContext = null;
            try {
                mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), map);
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(),e);
            }
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject(" " + msgTemplateDto.getTemplateName() + " " + appGrpNo);
            emailDto.setSender(AppConsts.MOH_AGENCY_NAME);
            emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
            emailDto.setClientQueryCode(appSubmissionDto.getAppGrpId());
            //send email
            appSubmissionService.feSendEmail(emailDto);
        }
    }

    private Map<String,String> doComChange( AppSubmissionDto appSubmissionDto,AppSubmissionDto oldAppSubmissionDto){
        StringBuilder sB=new StringBuilder();
        Map<String,String> result=IaisCommonUtils.genNewHashMap();
        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
        if(appEditSelectDto!=null){
            if(!appEditSelectDto.isPremisesEdit()){
                if(!appSubmissionDto.getAppGrpPremisesDtoList().equals(oldAppSubmissionDto.getAppGrpPremisesDtoList())){
                    result.put("premiss","UC_CHKLMD001_ERR001");
                }
            }
            if(!appEditSelectDto.isDocEdit()){
                if(!appSubmissionDto.getAppGrpPrimaryDocDtos().equals(oldAppSubmissionDto.getAppGrpPrimaryDocDtos())){
                    result.put("document","UC_CHKLMD001_ERR001");
                }
            }
            if(!appEditSelectDto.isServiceEdit()){
                if(!appSubmissionDto.getAppSvcRelatedInfoDtoList().equals(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList())){
                    result.put("serviceId","UC_CHKLMD001_ERR001");
                }
            }
        }
        return result;
    }
    private boolean compareAndSendEmail(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto){
        boolean isAuto = true;

        AppEditSelectDto appEditSelectDto = appSubmissionDto.getAppEditSelectDto();
        if(appEditSelectDto != null ){
            if(appEditSelectDto.isPremisesEdit()){
                if(!appSubmissionDto.getAppGrpPremisesDtoList().equals(oldAppSubmissionDto)){
                    isAuto =  compareHciName(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
                    if(isAuto){
                        isAuto = compareLocation(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
                    }
                    //send eamil

                }
            }

            if(appEditSelectDto.isMedAlertEdit()){
                //todo:
            }
            //one svc
            AppSvcRelatedInfoDto appSvcRelatedInfoDtoList = getAppSvcRelatedInfoDto(appSubmissionDto.getAppSvcRelatedInfoDtoList());
            AppSvcRelatedInfoDto oldAppSvcRelatedInfoDtoList = getAppSvcRelatedInfoDto(oldAppSubmissionDto.getAppSvcRelatedInfoDtoList());
            if(appEditSelectDto.isPoEdit() || appEditSelectDto.isDpoEdit() || appEditSelectDto.isServiceEdit()){
                //remove
                List<AppSvcPrincipalOfficersDto> newAppSvcPrincipalOfficersDto  = appSvcRelatedInfoDtoList.getAppSvcPrincipalOfficersDtoList();
                List<AppSvcPrincipalOfficersDto> oldAppSvcPrincipalOfficersDto = oldAppSvcRelatedInfoDtoList.getAppSvcPrincipalOfficersDtoList();
                if(newAppSvcPrincipalOfficersDto.size() > oldAppSvcPrincipalOfficersDto.size()){
                    isAuto = false;
                    //send eamil
                }
                //change
                List<String> newPoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> oldPoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> newDpoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> olddDpoIdNos = IaisCommonUtils.genNewArrayList();

                for(AppSvcPrincipalOfficersDto item:newAppSvcPrincipalOfficersDto){
                    if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(item.getPsnType())){
                        newPoIdNos.add(item.getIdNo());
                    }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(item.getPsnType())){
                        newDpoIdNos.add(item.getIdNo());
                    }
                }
                for(AppSvcPrincipalOfficersDto item:oldAppSvcPrincipalOfficersDto){
                    if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(item.getPsnType())){
                        oldPoIdNos.add(item.getIdNo());
                    }else if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(item.getPsnType())){
                        olddDpoIdNos.add(item.getIdNo());
                    }
                }
                if(!newPoIdNos.equals(oldPoIdNos)){
                    isAuto = false;
                }
                if(!newDpoIdNos.equals(olddDpoIdNos)){
                    isAuto = false;
                }
            }

            if(appEditSelectDto.isServiceEdit()){
                List<AppSvcCgoDto> newAppSvcCgoDto =  appSvcRelatedInfoDtoList.getAppSvcCgoDtoList();
                List<AppSvcCgoDto> oldAppSvcCgoDto =  oldAppSvcRelatedInfoDtoList.getAppSvcCgoDtoList();
                List<String> newCgoIdNos = IaisCommonUtils.genNewArrayList();
                List<String> oldCgoIdNos = IaisCommonUtils.genNewArrayList();
                if(!IaisCommonUtils.isEmpty(newAppSvcCgoDto) && !IaisCommonUtils.isEmpty(oldAppSvcCgoDto)){
                    for(AppSvcCgoDto item:newAppSvcCgoDto){
                        newCgoIdNos.add(item.getIdNo());
                    }
                    for(AppSvcCgoDto item:oldAppSvcCgoDto){
                        oldCgoIdNos.add(item.getIdNo());
                    }
                    if(!newCgoIdNos.equals(oldCgoIdNos)){
                        isAuto = false;
                    }

                }

                List<AppSvcLaboratoryDisciplinesDto> newDisciplinesDto = appSvcRelatedInfoDtoList.getAppSvcLaboratoryDisciplinesDtoList();
                List<AppSvcLaboratoryDisciplinesDto> oldDisciplinesDto = oldAppSvcRelatedInfoDtoList.getAppSvcLaboratoryDisciplinesDtoList();

                if(!IaisCommonUtils.isEmpty(newDisciplinesDto) && !IaisCommonUtils.isEmpty(oldDisciplinesDto)){
                    for(AppSvcLaboratoryDisciplinesDto item:newDisciplinesDto){
                        String hciName = item.getPremiseVal();
                        AppSvcLaboratoryDisciplinesDto oldAppSvcLaboratoryDisciplinesDto = getDisciplinesDto(oldDisciplinesDto,hciName);
                        List<AppSvcChckListDto> newCheckList =  item.getAppSvcChckListDtoList();
                        List<AppSvcChckListDto> oldCheckList = oldAppSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                        if(!IaisCommonUtils.isEmpty(newCheckList) && !IaisCommonUtils.isEmpty(oldCheckList)){
                            List<String> newCheckListIds = IaisCommonUtils.genNewArrayList();
                            List<String> oldCheckListIds = IaisCommonUtils.genNewArrayList();
                            for(AppSvcChckListDto checBox:oldCheckList){
                                oldCheckListIds.add(checBox.getChkLstConfId());
                            }
                            for(AppSvcChckListDto checBox:newCheckList){
                                if(!oldCheckListIds.contains(checBox.getChkLstConfId())){
                                    isAuto = false;
                                    break;
                                }
                            }
                        }
                    }
                    if(!newDisciplinesDto.equals(oldDisciplinesDto)){

                    }

                }


            }

            if(appEditSelectDto.isDocEdit()){
            /*if(!appSubmissionDto.getAppGrpPrimaryDocDtos().equals(oldAppSubmissionDto.getAppGrpPrimaryDocDtos()) ||
                    !appSvcRelatedInfoDtoList.getAppSvcDocDtoLit().equals(oldAppSvcRelatedInfoDtoList.getAppSvcDocDtoLit())){
                //send eamil

            }*/

            }
        }

        return isAuto;
    }

    private AppSvcLaboratoryDisciplinesDto getDisciplinesDto(List<AppSvcLaboratoryDisciplinesDto> disciplinesDto,String hciName){
        for(AppSvcLaboratoryDisciplinesDto iten:disciplinesDto){
            if(hciName.equals(iten.getPremiseVal())){
                return iten;
            }
        }
        return new AppSvcLaboratoryDisciplinesDto();
    }

    private boolean compareHciName(List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPremisesDto> oldAppGrpPremisesDtos){
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if(length == oldLength){
            for(int i=0; i<length; i++){
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if(!getHciName(appGrpPremisesDto).equals(getHciName(oldAppGrpPremisesDto))){
                    return false;
                }
            }
        }
        //is same
        return true;
    }

    private boolean compareLocation(List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPremisesDto> oldAppGrpPremisesDtos){
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if(length == oldLength){
            for(int i=0; i<length; i++){
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if(!appGrpPremisesDto.getAddress().equals(oldAppGrpPremisesDto.getAddress())){
                    return false;
                }
            }
        }
        //is same
        return true;
    }

    private String getHciName(AppGrpPremisesDto appGrpPremisesDto){
        String hciName = "";
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
            hciName = appGrpPremisesDto.getHciName();
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            hciName = appGrpPremisesDto.getConveyanceVehicleNo();
        }
        return hciName;
    }

    private AmendmentFeeDto getAmendmentFeeDto(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto){
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        boolean changeHciName = compareHciName(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
        boolean changeLocation = compareLocation(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
        amendmentFeeDto.setChangeInLicensee(false);
        amendmentFeeDto.setChangeInHCIName(!changeHciName);
        amendmentFeeDto.setChangeInLocation(!changeLocation);
        return amendmentFeeDto;
    }

    private AppSvcRelatedInfoDto getAppSvcRelatedInfoDto(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos){
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            return appSvcRelatedInfoDtos.get(0);
        }
        return new AppSvcRelatedInfoDto();
    }
    /**
     * @param request
     * @return
     * @description: for the page validate call.
     */
    private ApplicationValidateDto getValueFromPage(HttpServletRequest request) {
        ApplicationValidateDto dto = new ApplicationValidateDto();
        String pageCon = request.getParameter("pageCon");
        chose(request,pageCon);

        return dto;
    }
    private void chose(HttpServletRequest request,String type){
        if("valPremiseList".equals(type)){
            List<AppGrpPremisesDto> list = genAppGrpPremisesDtoList(request);
            ParamUtil.setRequestAttr(request, "valPremiseList", list);
        }if("prinOffice".equals(type)){
            List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDto =
                    (List<AppSvcPrincipalOfficersDto>)ParamUtil.getSessionAttr(request, "AppSvcPrincipalOfficersDto");
            ParamUtil.setRequestAttr(request,"prinOffice",appSvcPrincipalOfficersDto);
        }

    }
    /**
     * @description: get data from page
     * @author: zixian
     * @date: 11/6/2019 5:05 PM
     * @param: request
     * @return: AppGrpPremisesDto
     */
    //todo:move to NewApplicationHelper
    public static List<AppGrpPremisesDto> genAppGrpPremisesDtoList(HttpServletRequest request){
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request,APPSUBMISSIONDTO);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
        int count = 0;
        String [] premisesType = ParamUtil.getStrings(request, "premType");
        String [] hciName = ParamUtil.getStrings(request, "onSiteHciName");
        /*if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            int i = 0;
            if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                    premisesType[i] =  appGrpPremisesDto.getPremisesType();

                }
            }
            //todo:Grandfather Rights(RFC)
        }*/
        if(premisesType != null){
            count = premisesType.length;
        }
        String [] premisesIndexNo = ParamUtil.getStrings(request,"premisesIndexNo");
        //onsite
        String [] premisesSelect = ParamUtil.getStrings(request, "onSiteSelect");
        String [] postalCode = ParamUtil.getStrings(request,  "onSitePostalCode");
        String [] blkNo = ParamUtil.getStrings(request, "onSiteBlkNo");
        String [] streetName = ParamUtil.getStrings(request, "onSiteStreetName");
        String [] floorNo = ParamUtil.getStrings(request, "onSiteFloorNo");
        String [] unitNo = ParamUtil.getStrings(request, "onSiteUnitNo");
        String [] buildingName = ParamUtil.getStrings(request, "onSiteBuildingName");
        String [] siteAddressType = ParamUtil.getStrings(request, "onSiteAddressType");
        String [] offTelNo= ParamUtil.getStrings(request,"onSiteOffTelNo");
        String [] scdfRefNo = ParamUtil.getStrings(request, "onSiteScdfRefNo");
        String [] onsiteStartHH = ParamUtil.getStrings(request, "onSiteStartHH");
        String [] onsiteStartMM = ParamUtil.getStrings(request, "onSiteStartMM");
        String [] onsiteEndHHS = ParamUtil.getStrings(request, "onSiteEndHH");
        String [] onsiteEndMMS = ParamUtil.getStrings(request, "onSiteEndMM");
        String [] fireSafetyCertIssuedDateStr  = ParamUtil.getStrings(request, "onSiteFireSafetyCertIssuedDate");
        String [] isOtherLic = ParamUtil.getStrings(request, "onSiteIsOtherLic");
        //conveyance
        String [] conPremisesSelect = ParamUtil.getStrings(request, "conveyanceSelect");
        String [] conVehicleNo = ParamUtil.getStrings(request, "conveyanceVehicleNo");
        String [] conPostalCode = ParamUtil.getStrings(request,  "conveyancePostalCode");
        String [] conBlkNo = ParamUtil.getStrings(request, "conveyanceBlockNo");
        String [] conStreetName = ParamUtil.getStrings(request, "conveyanceStreetName");
        String [] conFloorNo = ParamUtil.getStrings(request, "conveyanceFloorNo");
        String [] conUnitNo = ParamUtil.getStrings(request, "conveyanceUnitNo");
        String [] conBuildingName = ParamUtil.getStrings(request, "conveyanceBuildingName");
        String [] conSiteAddressType = ParamUtil.getStrings(request, "conveyanceAddrType");
        String [] conStartHH = ParamUtil.getStrings(request, "conveyanceStartHH");
        String [] conStartMM = ParamUtil.getStrings(request, "conveyanceStartMM");
        String [] conEndHHS = ParamUtil.getStrings(request, "conveyanceEndHH");
        String [] conEndMMS = ParamUtil.getStrings(request, "conveyanceEndMM");

        //every prem's ph length
        String [] phLength = ParamUtil.getStrings(request,"phLength");
        String [] premValue = ParamUtil.getStrings(request, "premValue");

        Map<String,AppGrpPremisesDto> licAppGrpPremisesDtoMap = (Map<String, AppGrpPremisesDto>) ParamUtil.getSessionAttr(request, LICAPPGRPPREMISESDTOMAP);
        for(int i =0 ; i<count;i++){
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            String premisesSel = premisesSelect[i];
            String appType = appSubmissionDto.getAppType();
            if(!ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)
                    && !ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
                if(!StringUtil.isEmpty(premisesSel) && !premisesSel.equals("-1") && !premisesSel.equals(ApplicationConsts.NEW_PREMISES)){
                    if(appGrpPremisesDto != null){
                        appGrpPremisesDto = licAppGrpPremisesDtoMap.get(premisesSel);
                        appGrpPremisesDtoList.add(appGrpPremisesDto);
                    }
                    continue;
                }
            }
            if(StringUtil.isEmpty(premisesIndexNo[i])){
                appGrpPremisesDto.setPremisesIndexNo(String.valueOf(System.currentTimeMillis()));
            }
            appGrpPremisesDto.setPremisesType(premisesType[i]);
            List<AppPremPhOpenPeriodDto> appPremPhOpenPeriods = IaisCommonUtils.genNewArrayList();
            int length = 0;
            try {
                length = Integer.parseInt(phLength[i]);
            }catch (Exception e){
                log.error(StringUtil.changeForLog("length can not parse to int"));
            }
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType[i])){
                appGrpPremisesDto.setOnsiteStartHH(onsiteStartHH[i]);
                appGrpPremisesDto.setOnsiteStartMM(onsiteStartMM[i]);
                appGrpPremisesDto.setOnsiteEndHH(onsiteEndHHS[i]);
                appGrpPremisesDto.setOnsiteEndMM(onsiteEndMMS[i]);
                appGrpPremisesDto.setPremisesSelect(premisesSelect[i]);
                if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())){

                }
                appGrpPremisesDto.setHciName(hciName[i]);
                appGrpPremisesDto.setPostalCode(postalCode[i]);
                appGrpPremisesDto.setBlkNo(blkNo[i]);
                appGrpPremisesDto.setStreetName(streetName[i]);
                appGrpPremisesDto.setFloorNo(floorNo[i]);
                appGrpPremisesDto.setUnitNo(unitNo[i]);
                appGrpPremisesDto.setBuildingName(buildingName[i]);
                appGrpPremisesDto.setScdfRefNo(scdfRefNo[i]);
                appGrpPremisesDto.setAddrType(siteAddressType[i]);
                appGrpPremisesDto.setOffTelNo(offTelNo[i]);
                appGrpPremisesDto.setCertIssuedDtStr(fireSafetyCertIssuedDateStr[i]);
                Date fireSafetyCertIssuedDateDate = DateUtil.parseDate(fireSafetyCertIssuedDateStr[i], Formatter.DATE);
                appGrpPremisesDto.setCertIssuedDt(fireSafetyCertIssuedDateDate);
                if(AppConsts.YES.equals(isOtherLic)){
                    appGrpPremisesDto.setLocateWithOthers(true);
                }else if(AppConsts.NO.equals(isOtherLic)){
                    appGrpPremisesDto.setLocateWithOthers(false);
                }
                for(int j =0; j<length; j++){
                    AppPremPhOpenPeriodDto appPremPhOpenPeriod = new AppPremPhOpenPeriodDto();
                    String onsitePubHolidayName = premValue[i]+"onSitePubHoliday"+j;
                    String onsitePbHolDayStartHHName = premValue[i]+"onSitePbHolDayStartHH"+j;
                    String onsitePbHolDayStartMMName = premValue[i]+"onSitePbHolDayStartMM"+j;
                    String onsitePbHolDayEndHHName = premValue[i]+"onSitePbHolDayEndHH"+j;
                    String onsitePbHolDayEndMMName = premValue[i]+"onSitePbHolDayEndMM"+j;

                    String onsitePubHoliday = ParamUtil.getDate(request, onsitePubHolidayName);
                    String onsitePbHolDayStartHH = ParamUtil.getString(request, onsitePbHolDayStartHHName);
                    String onsitePbHolDayStartMM = ParamUtil.getString(request, onsitePbHolDayStartMMName);
                    String onsitePbHolDayEndHH = ParamUtil.getString(request, onsitePbHolDayEndHHName);
                    String onsitePbHolDayEndMM = ParamUtil.getString(request, onsitePbHolDayEndMMName);
                    appPremPhOpenPeriod.setPhDateStr(onsitePubHoliday);
                    Date phDate = DateUtil.parseDate(onsitePubHoliday, Formatter.DATE);
                    appPremPhOpenPeriod.setPhDate(phDate);
                    appPremPhOpenPeriod.setOnsiteStartFromHH(onsitePbHolDayStartHH);
                    appPremPhOpenPeriod.setOnsiteStartFromMM(onsitePbHolDayStartMM);
                    appPremPhOpenPeriod.setOnsiteEndToHH(onsitePbHolDayEndHH);
                    appPremPhOpenPeriod.setOnsiteEndToMM(onsitePbHolDayEndMM);
                    if(!StringUtil.isEmpty(onsitePubHoliday)||!StringUtil.isEmpty(onsitePbHolDayStartHH) || !StringUtil.isEmpty(onsitePbHolDayStartMM)
                            ||!StringUtil.isEmpty(onsitePbHolDayEndHH) ||!StringUtil.isEmpty(onsitePbHolDayEndMM)){
                        appPremPhOpenPeriods.add(appPremPhOpenPeriod);
                    }
                }

            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType[i])){
                appGrpPremisesDto.setConStartHH(conStartHH[i]);
                appGrpPremisesDto.setConStartMM(conStartMM[i]);
                appGrpPremisesDto.setConEndHH(conEndHHS[i]);
                appGrpPremisesDto.setConEndMM(conEndMMS[i]);
                appGrpPremisesDto.setPremisesSelect(conPremisesSelect[i]);
                appGrpPremisesDto.setConveyanceVehicleNo(conVehicleNo[i]);
                appGrpPremisesDto.setConveyancePostalCode(conPostalCode[i]);
                appGrpPremisesDto.setConveyanceBlockNo(conBlkNo[i]);
                appGrpPremisesDto.setConveyanceStreetName(conStreetName[i]);
                appGrpPremisesDto.setConveyanceFloorNo(conFloorNo[i]);
                appGrpPremisesDto.setConveyanceUnitNo(conUnitNo[i]);
                appGrpPremisesDto.setConveyanceBuildingName(conBuildingName[i]);
                appGrpPremisesDto.setConveyanceAddressType(conSiteAddressType[i]);
                /*appGrpPremisesDto.setConveyanceSalutation(conSalutation[i]);
                appGrpPremisesDto.setConveyanceVehicleOwnerName(conVehicleOwnerName[i]);*/
                for(int j =0; j<length; j++){
                    AppPremPhOpenPeriodDto appPremPhOpenPeriod = new AppPremPhOpenPeriodDto();
                    String convPubHolidayName = premValue[i]+"conveyancePubHoliday"+j;
                    String convPbHolDayStartHHName = premValue[i]+"conveyancePbHolDayStartHH"+j;
                    String convPbHolDayStartMMName = premValue[i]+"conveyancePbHolDayStartMM"+j;
                    String convPbHolDayEndHHName = premValue[i]+"conveyancePbHolDayEndHH"+j;
                    String convPbHolDayEndMMName = premValue[i]+"conveyancePbHolDayEndMM"+j;

                    String convPubHoliday = ParamUtil.getDate(request, convPubHolidayName);
                    String convPbHolDayStartHH = ParamUtil.getString(request, convPbHolDayStartHHName);
                    String convPbHolDayStartMM = ParamUtil.getString(request, convPbHolDayStartMMName);
                    String convPbHolDayEndHH = ParamUtil.getString(request, convPbHolDayEndHHName);
                    String convPbHolDayEndMM = ParamUtil.getString(request, convPbHolDayEndMMName);
                    appPremPhOpenPeriod.setPhDateStr(convPubHoliday);
                    Date phDate = DateUtil.parseDate(convPubHoliday, "dd/mm/yyyy");
                    appPremPhOpenPeriod.setPhDate(phDate);
                    appPremPhOpenPeriod.setConvStartFromHH(convPbHolDayStartHH);
                    appPremPhOpenPeriod.setConvStartFromMM(convPbHolDayStartMM);
                    appPremPhOpenPeriod.setConvEndToHH(convPbHolDayEndHH);
                    appPremPhOpenPeriod.setConvEndToMM(convPbHolDayEndMM);

                    if(!StringUtil.isEmpty(convPubHoliday)||!StringUtil.isEmpty(convPbHolDayStartHH) || !StringUtil.isEmpty(convPbHolDayStartMM)
                            ||!StringUtil.isEmpty(convPbHolDayEndHH) ||!StringUtil.isEmpty(convPbHolDayEndMM)){
                        appPremPhOpenPeriods.add(appPremPhOpenPeriod);
                    }
                }
            }
            appGrpPremisesDto.setAppPremPhOpenPeriodList(appPremPhOpenPeriods);
            appGrpPremisesDtoList.add(appGrpPremisesDto);
        }
        return  appGrpPremisesDtoList;
    }


    private Map<String,String> doPreviewAndSumbit( BaseProcessClass bpc){
        StringBuilder sB=new StringBuilder();
        Map<String,String> previewAndSubmitMap=IaisCommonUtils.genNewHashMap();
        //
        Map<String, String> premissMap = doValidatePremiss(bpc);
        if(!premissMap.isEmpty()){
            previewAndSubmitMap.put("premiss","UC_CHKLMD001_ERR001");
            String premissMapStr = JsonUtil.parseToJson(premissMap);
            log.info("premissMap json str:"+premissMapStr);
        }
        //
        List<AppSvcPrincipalOfficersDto> poDto = (List<AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(bpc.request, "AppSvcPrincipalOfficersDto");
        Map<String, String> poMap = NewApplicationHelper.doValidatePo(poDto);
        if(!poMap.isEmpty()){
            previewAndSubmitMap.put("po","UC_CHKLMD001_ERR001");
            String poMapStr = JsonUtil.parseToJson(poMap);
            log.info("poMap json str:"+poMapStr);
        }
        //
        List<AppSvcCgoDto> appSvcCgoList = (List<AppSvcCgoDto>) ParamUtil.getSessionAttr(bpc.request, ClinicalLaboratoryDelegator.GOVERNANCEOFFICERSDTOLIST);
        Map<String, String> govenMap = NewApplicationHelper.doValidateGovernanceOfficers(appSvcCgoList);
        if(!govenMap.isEmpty()){
            previewAndSubmitMap.put("goven","UC_CHKLMD001_ERR001");
            String govenMapStr = JsonUtil.parseToJson(govenMap);
            log.info("govenMap json str:"+govenMapStr);
        }
        //
        Map<String, String> map = doCheckBox(bpc,sB);
        if(!map.isEmpty()){
            previewAndSubmitMap.putAll(map);
            String mapStr = JsonUtil.parseToJson(map);
            log.info("map json str:"+mapStr);
        }

        Map<String,String> documentMap=IaisCommonUtils.genNewHashMap();
        documentValid(bpc.request,documentMap);
        doCommomDocument(bpc.request,documentMap);
        if(!documentMap.isEmpty()){
            previewAndSubmitMap.put("document","UC_CHKLMD001_ERR001");
            String documentMapStr = JsonUtil.parseToJson(documentMap);
            log.info("documentMap json str:"+documentMapStr);
        }

        if(!StringUtil.isEmpty(sB.toString())){
            previewAndSubmitMap.put("serviceId",sB.toString());
        }

        return previewAndSubmitMap;
    }

    private void doCommomDocument(HttpServletRequest request, Map<String, String> documentMap) {
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList  = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>)   request.getSession().getAttribute(COMMONHCSASVCDOCCONFIGDTO);
        Boolean flag =false;
        for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto : commonHcsaSvcDocConfigList) {
            Boolean isMandatory = hcsaSvcDocConfigDto.getIsMandatory();
            String id = hcsaSvcDocConfigDto.getId();
            if(isMandatory&&appGrpPrimaryDocDtoList.size()==0){
                documentMap.put("common","UC_CHKLMD001_ERR001");
            }else {
                for(AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList){
                    String svcDocId = appGrpPrimaryDocDto.getSvcDocId();
                    if(id.equals(svcDocId)){
                        flag=true;
                    }

                }

            }

        }

        if(flag){
            documentMap.put("common","UC_CHKLMD001_ERR001");
        }

    }

    //todo

    private Map<String,String> doCheckBox( BaseProcessClass bpc,StringBuilder sB){

        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        Map<String,String> errorMap=IaisCommonUtils.genNewHashMap();
        List<AppSvcRelatedInfoDto> dto = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        Map<String, List<HcsaSvcPersonnelDto>> allSvcAllPsnConfig = getAllSvcAllPsnConfig(bpc.request);
        for(int i=0;i< dto.size();i++ ){
            String serviceId = dto.get(i).getServiceId();
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = dto.get(i).getAppSvcLaboratoryDisciplinesDtoList();
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = allSvcAllPsnConfig.get(serviceId);
            dolabory(errorMap,appSvcLaboratoryDisciplinesDtoList,serviceId,sB);
            List<AppSvcCgoDto> appSvcCgoDtoList = dto.get(i).getAppSvcCgoDtoList();
            doAppSvcCgoDto(hcsaSvcPersonnelDtos,errorMap,appSvcCgoDtoList,serviceId,sB);
            List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = dto.get(i).getAppSvcDisciplineAllocationDtoList();
            doSvcDis(errorMap,appSvcDisciplineAllocationDtoList,serviceId,sB);
            List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = dto.get(i).getAppSvcPrincipalOfficersDtoList();

            Map<String, String> map = NewApplicationHelper.doValidatePo(appSvcPrincipalOfficersDtoList);
            if(!map.isEmpty()){
                sB.append(serviceId);
            }
            List<AppSvcPersonnelDto> appSvcPersonnelDtoList = dto.get(i).getAppSvcPersonnelDtoList();
            doAppSvcPersonnelDtoList(hcsaSvcPersonnelDtos,errorMap,appSvcPersonnelDtoList,serviceId,sB);
            List<AppSvcDocDto> appSvcDocDtoLit = dto.get(i).getAppSvcDocDtoLit();
            doSvcDocument(errorMap,appSvcDocDtoLit,serviceId,sB);
        }


        return  errorMap;
    }

    private void doSvcDocument(Map<String,String> map,   List<AppSvcDocDto> appSvcDocDtoLit,String serviceId,StringBuilder sB ){
        if(appSvcDocDtoLit!=null){
            for (AppSvcDocDto appSvcDocDto:appSvcDocDtoLit){
                Integer docSize = appSvcDocDto.getDocSize();
                String docName = appSvcDocDto.getDocName();
                Boolean flag=false;
                if(docSize>4*1024*1024){
                    flag=true;
                }
                String substring = docName.substring(docName.lastIndexOf(".") + 1);
                FileType[] fileType = FileType.values();
                for(FileType f:fileType){
                    if(f.name().equalsIgnoreCase(substring)){
                        flag=true;
                    }
                }

                if(!flag){
                    /*   sB.append(serviceId);*/
                }
            }


        }


    }

    private void dolabory(Map<String,String> map ,List<AppSvcLaboratoryDisciplinesDto> list,String serviceId, StringBuilder sB){
        if(list!=null&&list.isEmpty()){

        }
    }


    private void doAppSvcPersonnelDtoList(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos ,Map map,List<AppSvcPersonnelDto> appSvcPersonnelDtos,String serviceId, StringBuilder sB){
        if(appSvcPersonnelDtos==null){
            if(hcsaSvcPersonnelDtos!=null){
                for(HcsaSvcPersonnelDto every:hcsaSvcPersonnelDtos){
                    String psnType = every.getPsnType();
                    if(ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL.equals(psnType)){
                        sB.append(serviceId);
                        return;
                    }
                }
            }

            return;
        }


        boolean flag =false;
        for(int i=0;i<appSvcPersonnelDtos.size();i++){
            String personnelSel = appSvcPersonnelDtos.get(i).getPersonnelType();
            if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE.equals(personnelSel)){
                String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                String name = appSvcPersonnelDtos.get(i).getName();
                if(StringUtil.isEmpty(name)){
                    map.put("name"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
                if(StringUtil.isEmpty(profRegNo)){
                    map.put("regnNo"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
            }
            if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(personnelSel)){
                String name = appSvcPersonnelDtos.get(i).getName();
                String designation = appSvcPersonnelDtos.get(i).getDesignation();
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                String qualification = appSvcPersonnelDtos.get(i).getQuaification();

                if(StringUtil.isEmpty(name)){
                    map.put("name"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
                if(StringUtil.isEmpty(designation)){
                    map.put("designation"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
                if(StringUtil.isEmpty(wrkExpYear)){
                    map.put("wrkExpYear"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }else {
                    if(!wrkExpYear.matches("^[0-9]*$")){
                        map.put("wrkExpYear"+i,"CHKLMD001_ERR003");
                        flag=true;
                    }
                }
                if(StringUtil.isEmpty(qualification)){
                    map.put("qualification"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
            }

            if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(personnelSel)){
                String name = appSvcPersonnelDtos.get(i).getName();
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                String quaification = appSvcPersonnelDtos.get(i).getQuaification();
                if(StringUtil.isEmpty(name)){
                    map.put("name"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
                if(StringUtil.isEmpty(wrkExpYear)){
                    map.put("wrkExpYear"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
                else {
                    if(!wrkExpYear.matches("^[0-9]*$")){
                        map.put("wrkExpYear"+i,"CHKLMD001_ERR003");
                        flag=true;
                    }
                }
                if(StringUtil.isEmpty(quaification)){
                    map.put("quaification"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
            }
            if(ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER.equals(personnelSel)){
                String name = appSvcPersonnelDtos.get(i).getName();
                if(StringUtil.isEmpty(name)){
                    map.put("name"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
            }
            if(flag){
                sB.append(serviceId);
            }
        }

    }
    private void doAppSvcCgoDto(  List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, Map map ,List<AppSvcCgoDto> list,String serviceId,StringBuilder sB){
        if(list==null){
            if(hcsaSvcPersonnelDtos!=null){
                for(HcsaSvcPersonnelDto every:hcsaSvcPersonnelDtos){
                    String psnType = every.getPsnType();
                    if(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)){
                        sB.append(serviceId);
                        return;
                    }
                }
            }

            return;
        }


        boolean flag =false;
        for(int i=0;i<list.size();i++ ){
            String assignSelect = list.get(i).getAssignSelect();
            if("".equals(assignSelect)||assignSelect==null){
                map.put("cgoassignSelect"+i,"UC_CHKLMD001_ERR001");
                flag =true;
            }
            String idType = list.get(i).getIdType();
            if(StringUtil.isEmpty(idType)){
                map.put("cgotype"+i,"UC_CHKLMD001_ERR001");
                flag =true;
            }
            String mobileNo = list.get(i).getMobileNo();
            if(StringUtil.isEmpty(mobileNo)){
                map.put("cgomobileNo"+i,"UC_CHKLMD001_ERR001");
                flag =true;
            }else {
                if(!mobileNo.matches("^[8|9][0-9]{7}$")){
                    map.put("cgomobileNo"+i,"CHKLMD001_ERR004");
                    flag =true;
                }
            }
            String emailAddr = list.get(i).getEmailAddr();
            if(StringUtil.isEmpty(emailAddr)){
                map.put("cgoemailAddr"+i,"UC_CHKLMD001_ERR001");
                flag =true;
            }else {
                if(!ValidationUtils.isEmail(emailAddr)){
                    map.put("cgoemailAddr"+i,"CHKLMD001_ERR006");
                    flag =true;
                }
            }
            if( flag ){
                sB.append(serviceId);

            }

        }
    }


    private void doSvcDis( Map map ,List<AppSvcDisciplineAllocationDto> list,String serviceId,StringBuilder sB){
        if(list==null){

        }
    }

    private void doPO( List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, Map map ,List<AppSvcPrincipalOfficersDto> list,String serviceId,StringBuilder sB){
        if(list==null){
            if(hcsaSvcPersonnelDtos!=null){
                for(HcsaSvcPersonnelDto every :hcsaSvcPersonnelDtos){
                    String psnType = every.getPsnType();
                    if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)){
                        sB.append(serviceId);
                        return;
                    }
                }
            }

            return;
        }


        boolean flag=false;
        StringBuilder stringBuilder=new StringBuilder();

        for(int i=0;i<list.size();i++){
            StringBuilder stringBuilder1=new StringBuilder();
            String mobileNo = list.get(i).getMobileNo();
            String emailAddr = list.get(i).getEmailAddr();
            String psnType = list.get(i).getPsnType();
            String assignSelect = list.get(i).getAssignSelect();
            if(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnType)){

            }
            if(ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)){
                if(StringUtil.isEmpty(assignSelect)){
                    map.put("assignSelect"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }
                String officeTelNo = list.get(i).getOfficeTelNo();
                if(StringUtil.isEmpty(officeTelNo)){
                    map.put("POofficeTelNo"+i,"UC_CHKLMD001_ERR001");
                    flag=true;
                }else {
                    if(!officeTelNo.matches("^[6][0-9]{7}$")){
                        map.put("POofficeTelNo"+i,"CHKLMD001_ERR007");
                        flag=true;
                    }
                }
            }
            if(StringUtil.isEmpty(emailAddr)){
                map.put("POeamilAddr"+i,"UC_CHKLMD001_ERR001");
                flag=true;
            }else {
                if (!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
                    map.put("POeamilAddr"+i,"CHKLMD001_ERR006");
                    flag=true;
                }
            }

            if(StringUtil.isEmpty(mobileNo)){
                map.put("POmobileNo"+i,"UC_CHKLMD001_ERR001");
                flag=true;
            }else {
                if(!mobileNo.matches("^[8|9][0-9]{7}$")){
                    map.put("POmobileNo"+i,"CHKLMD001_ERR004");
                    flag=true;
                }
            }

            String idType = list.get(i).getIdType();
            if(StringUtil.isEmpty(idType)){
                map.put("POidType"+i,"UC_CHKLMD001_ERR001");
                flag=true;
            }
            String idNo = list.get(i).getIdNo();
            if(StringUtil.isEmpty(idNo)){
                map.put("POidNo"+i,"UC_CHKLMD001_ERR001");
                flag=true;
            }else {
                //todo change is error
                if("FIN".equals(idType)){
                    boolean b = SgNoValidator.validateFin(idNo);
                    if(!b){
                        map.put("POidNo"+i,"CHKLMD001_ERR005");
                        flag=true;
                    }else {
                        stringBuilder1.append(idType).append(idNo);
                    }
                }
                if("NRIC".equals(idType)){
                    boolean b1 = SgNoValidator.validateNric(idNo);
                    if(!b1){
                        map.put("POidNo"+i,"CHKLMD001_ERR005");
                        flag=true;
                    }else {
                        stringBuilder1.append(idType).append(idNo);
                    }
                }

            }
            String salutation = list.get(i).getSalutation();
            if(StringUtil.isEmpty(salutation)){
                map.put("POsalutation"+i,"UC_CHKLMD001_ERR001");
                flag=true;
            }
            String designation = list.get(i).getDesignation();
            if(StringUtil.isEmpty(designation)){
                map.put("POdesignation"+i,"UC_CHKLMD001_ERR001");
                flag=true;
            }
            String name = list.get(i).getName();
            if(StringUtil.isEmpty(name)){
                map.put("POname"+i,"UC_CHKLMD001_ERR001");
                flag=true;
            }
            String s = stringBuilder.toString();
            if(!StringUtil.isEmpty(stringBuilder1.toString())){
                if(s.contains(stringBuilder1.toString())){
                    map.put("POidNo","error");
                    flag=true;
                }else {
                    stringBuilder.append(stringBuilder1.toString());
                }
            }

        }
        if(flag){
            sB.append(serviceId);
        }

    }
    private void loadingDraft(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do loadingDraft start ...."));
        String draftNo = (String) ParamUtil.getString(bpc.request, "DraftNumber");
        //draftNo = "DN191118000001";
        if(!StringUtil.isEmpty(draftNo)){
            log.info(StringUtil.changeForLog("draftNo is not empty"));
            AppSubmissionDto appSubmissionDto = serviceConfigService.getAppSubmissionDtoDraft(draftNo);
            ParamUtil.setSessionAttr(bpc.request,DRAFTCONFIG,"test");
            if(appSubmissionDto.getAppGrpPremisesDtoList() != null && appSubmissionDto.getAppGrpPremisesDtoList().size() >0){
                ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            }else{
                ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, null);
            }

        }
        log.info(StringUtil.changeForLog("the do loadingDraft end ...."));
    }
    private void requestForChangeOrRenewLoading(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do requestForChangeLoading start ...."));
        String appType = (String) ParamUtil.getRequestAttr(bpc.request,"appType");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getRequestAttr(bpc.request,RfcConst.APPSUBMISSIONDTORFCATTR);
        String currentEdit = (String) ParamUtil.getRequestAttr(bpc.request,RfcConst.RFC_CURRENT_EDIT);
        if((ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType))
                && appSubmissionDto!= null && !StringUtil.isEmpty(currentEdit)){
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            if(RfcConst.EDIT_PREMISES.equals(currentEdit)){
                appEditSelectDto.setPremisesEdit(true);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"premises");
            }else if(RfcConst.EDIT_PRIMARY_DOC.equals(currentEdit)){
                appEditSelectDto.setDocEdit(true);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"documents");
            }else if(RfcConst.EDIT_SERVICE.equals(currentEdit)){
                appEditSelectDto.setServiceEdit(true);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE,"serviceForms");
            }
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            appSubmissionDto.setNeedEditController(true);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        }
        log.info(StringUtil.changeForLog("the do requestForChangeLoading end ...."));
    }
    private void renewLicence(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do renewLicence start ...."));
        String licenceId = ParamUtil.getString(bpc.request, "licenceId");
        String type = ParamUtil.getString(bpc.request, "type");
        log.info(StringUtil.changeForLog("The type is -->:"+type));
        if(!StringUtil.isEmpty(licenceId) && ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(type)){
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByLicenceId(licenceId);
            if(appSubmissionDto != null){
                appSubmissionDto.setNeedEditController(true);
                AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
                appEditSelectDto.setPremisesEdit(true);
                appEditSelectDto.setDocEdit(true);
                appEditSelectDto.setPoEdit(true);
                appEditSelectDto.setDocEdit(true);
                appEditSelectDto.setServiceEdit(true);
                appEditSelectDto.setMedAlertEdit(true);
                appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
                appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
            }
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        }
        log.info(StringUtil.changeForLog("the do renewLicence end ...."));
    }

    private void requestForInformationLoading(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the do requestForInformationLoading start ...."));

        String appNo = ParamUtil.getString(bpc.request,"appNo");
        if(!StringUtil.isEmpty(appNo)){
            ParamUtil.setSessionAttr(bpc.request,DRAFTCONFIG,"test");
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByAppNo(appNo);
            if(appSubmissionDto != null){
                String appType =  appSubmissionDto.getAppType();
                boolean isRenewalOrRfc = ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType) || ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType);
                if (isRenewalOrRfc){
                    appSubmissionDto.setNeedEditController(true);
                    // set the required information
                    String licenceId = appSubmissionDto.getLicenceId();
                    appSubmissionDto.setLicenceNo(withOutRenewalService.getLicenceNumberByLicenceId(licenceId));
                }

            }

            AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
            //ParamUtil.setSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO,oldAppSubmissionDto);
            ParamUtil.setSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG,"test");
        }
        log.info(StringUtil.changeForLog("the do requestForInformationLoading end ...."));
    }

    private boolean loadingServiceConfig(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the do loadingServiceConfig start ...."));
        //loading the service
        List<String> serviceConfigIds = IaisCommonUtils.genNewArrayList();
        List<String> names = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if(appSubmissionDto != null ){
            // from draft,rfi
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtoList){
                    if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getServiceId())){
                        serviceConfigIds.add(appSvcRelatedInfoDto.getServiceId());
                    }
                    //if get the data from licence, only have the serviceName
                    if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getServiceName())){
                        names.add(appSvcRelatedInfoDto.getServiceName());
                    }

                }
            }
        }else {
            List<String> baseServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "baseService");
            List<String> specifiedServiceIds = (List<String>) ParamUtil.getSessionAttr(bpc.request, "specifiedService");
            if(!IaisCommonUtils.isEmpty(baseServiceIds)){
                serviceConfigIds.addAll(baseServiceIds);
            }
            if(!IaisCommonUtils.isEmpty(specifiedServiceIds)){
                serviceConfigIds.addAll(specifiedServiceIds);
            }
        }


        if(IaisCommonUtils.isEmpty(serviceConfigIds) && IaisCommonUtils.isEmpty(names)){
            log.info(StringUtil.changeForLog("service id is empty"));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "errorAck");
            ParamUtil.setRequestAttr(bpc.request,ACKSTATUS,"error");
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "you have encountered some problems, please contact the administrator !!!");

            return false;
        }

        List<HcsaServiceDto> hcsaServiceDtoList = null;
        if(!serviceConfigIds.isEmpty()){
            hcsaServiceDtoList = serviceConfigService.getHcsaServiceDtosById(serviceConfigIds);
        }else if(!names.isEmpty()){
            hcsaServiceDtoList = serviceConfigService.getHcsaServiceByNames(names);
        }

        sortHcsaServiceDto(hcsaServiceDtoList);
        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
        log.info(StringUtil.changeForLog("the do loadingServiceConfig end ...."));
        return true;
    }

    private void sortHcsaServiceDto(List<HcsaServiceDto> hcsaServiceDtoList) {
        List<HcsaServiceDto> baseList = new ArrayList();
        List<HcsaServiceDto> specifiedList = new ArrayList();
        List<HcsaServiceDto> subList = new ArrayList();
        List<HcsaServiceDto> otherList = new ArrayList();
        //class
        for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtoList) {
            switch (hcsaServiceDto.getSvcCode()) {
                case ApplicationConsts.SERVICE_CONFIG_TYPE_BASE:
                    baseList.add(hcsaServiceDto);
                    break;
                case ApplicationConsts.SERVICE_CONFIG_TYPE_SPECIFIED:
                    specifiedList.add(hcsaServiceDto);
                    break;
                case ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED:
                    subList.add(hcsaServiceDto);
                    break;
                default:
                    otherList.add(hcsaServiceDto);
                    break;
            }
        }
        //Sort
        sortService(baseList);
        sortService(specifiedList);
        sortService(subList);
        sortService(otherList);
        hcsaServiceDtoList = IaisCommonUtils.genNewArrayList();
        hcsaServiceDtoList.addAll(baseList);
        hcsaServiceDtoList.addAll(specifiedList);
        hcsaServiceDtoList.addAll(subList);
        hcsaServiceDtoList.addAll(otherList);
    }

    private void sortService(List<HcsaServiceDto> list) {
        list.sort((h1, h2) -> h1.getSvcName().compareTo(h2.getSvcName()));
    }

    private static int validateTime(Map<String, String> errorMap, String onsiteHH, String onsiteMM, int date, String key, int i, String error){
        try {
            int i1 = Integer.parseInt(onsiteHH);
            int i2= Integer.parseInt(onsiteMM);
            date=  i1*60+i2*1;
            if(i1>=24||i2>=60){
                errorMap.put(key+i,error);
            }
        }catch (Exception e){
            errorMap.put(key+i,error);
        }
        return date;
    }

    //todo:move to NewApplicationHelper
    public static Map<String, String> doValidatePremiss(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        //do validate one premiss
        List<String> list=IaisCommonUtils.genNewArrayList();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,APPSUBMISSIONDTO);
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request,OLDAPPSUBMISSIONDTO);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        for(int i=0;i<appGrpPremisesDtoList.size();i++){
            String premiseType = appGrpPremisesDtoList.get(i).getPremisesType();
            if (StringUtil.isEmpty(premiseType)) {
                errorMap.put("premisesType"+i, "UC_CHKLMD001_ERR001");
            }else {
                String premisesSelect = appGrpPremisesDtoList.get(i).getPremisesSelect();
                String appType = appSubmissionDto.getAppType();
                boolean needValidate = false;
                if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
                    String oldPremSel = oldAppSubmissionDto.getAppGrpPremisesDtoList().get(0).getPremisesSelect();
                    if(!StringUtil.isEmpty(oldPremSel) && oldPremSel.equals(premisesSelect)){
                        needValidate = true;
                    }
                }
                if (StringUtil.isEmpty(premisesSelect) || "-1".equals(premisesSelect)) {
                    errorMap.put("premisesSelect"+i, "UC_CHKLMD001_ERR001");
                } else if ("newPremise".equals(premisesSelect) || needValidate) {
                    if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premiseType)) {
                        String onsiteStartHH = appGrpPremisesDtoList.get(i).getOnsiteStartHH();
                        String onsiteStartMM = appGrpPremisesDtoList.get(i).getOnsiteStartMM();
                        int startDate=0;
                        int endDate=0;
                        if(StringUtil.isEmpty(onsiteStartHH)||StringUtil.isEmpty(onsiteStartMM)){
                            errorMap.put("onsiteStartMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            startDate = validateTime(errorMap, onsiteStartHH, onsiteStartMM, startDate, "onsiteStartMM", i, "UC_CHKLMD001_ERR003");
                        }

                        String onsiteEndHH = appGrpPremisesDtoList.get(i).getOnsiteEndHH();
                        String onsiteEndMM = appGrpPremisesDtoList.get(i).getOnsiteEndMM();
                        if(StringUtil.isEmpty(onsiteEndHH)||StringUtil.isEmpty(onsiteEndMM)){
                            errorMap.put("onsiteEndMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                             endDate = validateTime(errorMap, onsiteEndHH, onsiteEndMM, endDate, "onsiteEndMM", i, "UC_CHKLMD001_ERR003");
                        }
                        if(endDate<startDate){
                            errorMap.put("onsiteEndMM"+i,"UC_CHKLMD001_ERR003");
                        }
                        /*Boolean isOtherLic = appGrpPremisesDtoList.get(i).isLocateWithOthers();
                        if(StringUtil.isEmpty(isOtherLic)){
                            errorMap.put("isOtherLic"+i,"UC_CHKLMD001_ERR002");
                        }*/

                        //set  time
                        if(!errorMap.containsKey("onsiteStartMM"+i) && !errorMap.containsKey("onsiteEndMM"+i)){
                            LocalTime startTime = LocalTime.of(Integer.parseInt(onsiteStartHH),Integer.parseInt(onsiteStartMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeFrom(Time.valueOf(startTime));

                            LocalTime endTime = LocalTime.of(Integer.parseInt(onsiteEndHH),Integer.parseInt(onsiteEndMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeTo(Time.valueOf(endTime));
                        }

                        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDtoList.get(i).getAppPremPhOpenPeriodList();
                        if(!IaisCommonUtils.isEmpty(appPremPhOpenPeriodList)){
                            for(AppPremPhOpenPeriodDto every :appPremPhOpenPeriodList){

                                String convStartFromHH = every.getOnsiteStartFromHH();
                                String convStartFromMM = every.getOnsiteStartFromMM();
                                String onsiteEndToHH = every.getOnsiteEndToHH();
                                String onsiteEndToMM = every.getOnsiteEndToMM();
                                Date phDate = every.getPhDate();
                                if(!StringUtil.isEmpty(phDate)){
                                    if(StringUtil.isEmpty(convStartFromHH)||StringUtil.isEmpty(convStartFromMM)
                                    ||StringUtil.isEmpty(onsiteEndToHH)||StringUtil.isEmpty(onsiteEndToMM)){
                                        errorMap.put("onsiteEndToMM"+i,"UC_CHKLMD001_ERR001");
                                    }
                                }
                                if(!StringUtil.isEmpty(convStartFromHH)&&!StringUtil.isEmpty(convStartFromMM)&&!StringUtil.isEmpty(onsiteEndToHH)
                                        &&!StringUtil.isEmpty(onsiteEndToMM)||StringUtil.isEmpty(convStartFromHH)&&StringUtil.isEmpty(convStartFromMM)
                                        &&StringUtil.isEmpty(onsiteEndToHH)&&StringUtil.isEmpty(onsiteEndToMM)){
                                    if(!StringUtil.isEmpty(convStartFromHH)&&!StringUtil.isEmpty(convStartFromMM)&&!StringUtil.isEmpty(onsiteEndToHH)
                                            &&!StringUtil.isEmpty(onsiteEndToMM)){
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);

                                            if(i1>=24||i2>=60){
                                                errorMap.put("onsiteStartToMM"+i,"CHKLMD001_ERR003");
                                            }

                                        }catch (Exception e){
                                            errorMap.put("onsiteStartToMM"+i,"CHKLMD001_ERR003");
                                        }
                                    }
                                    try {
                                        int i3 = Integer.parseInt(onsiteEndToHH);
                                        int i4 = Integer.parseInt(onsiteEndToMM);
                                        if(i3>=24||i4>=60){
                                            errorMap.put("onsiteEndToMM"+i,"CHKLMD001_ERR003");
                                        }
                                    }catch (Exception e){
                                        errorMap.put("onsiteEndToMM"+i,"CHKLMD001_ERR003");
                                    }
                                    try {
                                        int i1 = Integer.parseInt(convStartFromHH);
                                        int i2 = Integer.parseInt(convStartFromMM);
                                        int i3 = Integer.parseInt(onsiteEndToHH);
                                        int i4 = Integer.parseInt(onsiteEndToMM);
                                        if((i1*60+i2)>(i3*60+i4)){
                                            errorMap.put("onsiteEndToMM"+i,"UC_CHKLMD001_ERR003");
                                        }
                                    }catch (Exception e){

                                    }

                                }else {
                                    if(StringUtil.isEmpty(convStartFromHH)||StringUtil.isEmpty(convStartFromMM)){
                                        errorMap.put("onsiteStartToMM"+i,"UC_CHKLMD001_ERR001");
                                    }else {
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);

                                            if(i1>=24||i2>=60){
                                                errorMap.put("onsiteStartToMM"+i,"CHKLMD001_ERR003");
                                            }

                                        }catch (Exception e){
                                            errorMap.put("onsiteStartToMM"+i,"CHKLMD001_ERR003");
                                        }
                                    }
                                    if(StringUtil.isEmpty(onsiteEndToHH)||StringUtil.isEmpty(onsiteEndToMM)){
                                        errorMap.put("onsiteEndToMM"+i,"UC_CHKLMD001_ERR001");
                                    }else {
                                        try {
                                            int i3 = Integer.parseInt(onsiteEndToHH);
                                            int i4 = Integer.parseInt(onsiteEndToMM);
                                            if(i3>=24||i4>=60){
                                                errorMap.put("onsiteEndToMM"+i,"CHKLMD001_ERR003");
                                            }
                                        }catch (Exception e){
                                            errorMap.put("onsiteEndToMM"+i,"CHKLMD001_ERR003");
                                        }

                                    }


                                }

                            }
                            //set ph time
                            String errorOnsiteEndToMM = errorMap.get("onsiteEndToMM"+i);
                            if(StringUtil.isEmpty(errorOnsiteEndToMM) && !IaisCommonUtils.isEmpty(appPremPhOpenPeriodList)){
                                for(AppPremPhOpenPeriodDto ph :appPremPhOpenPeriodList){
                                    if(!StringUtil.isEmpty(ph.getOnsiteStartFromHH()) && !StringUtil.isEmpty(ph.getOnsiteStartFromMM())){
                                        LocalTime startTime = LocalTime.of(Integer.parseInt(ph.getOnsiteStartFromHH()),Integer.parseInt(ph.getOnsiteStartFromMM()));
                                        ph.setStartFrom(Time.valueOf(startTime));
                                    }
                                    if(!StringUtil.isEmpty(ph.getOnsiteEndToHH()) && !StringUtil.isEmpty(ph.getOnsiteEndToMM())){
                                        LocalTime endTime = LocalTime.of(Integer.parseInt(ph.getOnsiteEndToHH()),Integer.parseInt(ph.getOnsiteEndToMM()));
                                        ph.setEndTo(Time.valueOf(endTime));
                                    }
                                }

                            }
                        }
                        String hciName = appGrpPremisesDtoList.get(i).getHciName();
                        if(StringUtil.isEmpty(hciName)){
                            errorMap.put("hciName"+i,"UC_CHKLMD001_ERR001");
                        }
                        String offTelNo = appGrpPremisesDtoList.get(i).getOffTelNo();
                        if(StringUtil.isEmpty(offTelNo)){
                            errorMap.put("offTelNo"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            boolean matches = offTelNo.matches("^[6][0-9]{7}$");
                            if(!matches) {
                                errorMap.put("offTelNo"+i,"CHKLMD001_ERR007");
                            }
                        }

                        String streetName = appGrpPremisesDtoList.get(i).getStreetName();
                        if(StringUtil.isEmpty(streetName)){
                            errorMap.put("streetName"+i,"UC_CHKLMD001_ERR001");
                        }

                        String addrType = appGrpPremisesDtoList.get(i).getAddrType();

                        StringBuilder stringBuilder =new StringBuilder();
                        if(StringUtil.isEmpty(addrType)){
                            errorMap.put("addrType"+i, "UC_CHKLMD001_ERR001");
                        }else {
                            boolean empty = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getFloorNo());
                            boolean empty1 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getBlkNo());
                            boolean empty2 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getUnitNo());
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(addrType)) {

                                if (empty) {
                                    errorMap.put("floorNo"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty1) {
                                    errorMap.put("blkNo"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty2) {
                                    errorMap.put("unitNo"+i, "UC_CHKLMD001_ERR001");
                                }
                            }
                            if(!empty&&!empty1&&!empty2){
                                stringBuilder.append(appGrpPremisesDtoList.get(i).getFloorNo())
                                        .append(appGrpPremisesDtoList.get(i).getBlkNo())
                                        .append(appGrpPremisesDtoList.get(i).getUnitNo());
                            }
                        }
                        String postalCode = appGrpPremisesDtoList.get(i).getPostalCode();
                        if (!StringUtil.isEmpty(postalCode)) {
                            if (!postalCode.matches("^[0-9]{6}$")) {
                                errorMap.put("postalCode"+i, "UC_CHKLMD001_ERR004");
                            }else {

                                if(!StringUtil.isEmpty(stringBuilder.toString())){
                                    stringBuilder.append(postalCode);
                                    if(list.contains(stringBuilder.toString())){
                                        errorMap.put("postalCode"+i,"There is a duplicated entry for this premises address.");

                                    }else {
                                        list.add(stringBuilder.toString());
                                    }
                                }
                            }
                        }else {
                            errorMap.put("postalCode"+i, "UC_CHKLMD001_ERR001");
                        }
                    } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premiseType)) {
                        String conStartHH = appGrpPremisesDtoList.get(i).getConStartHH();
                        String conStartMM = appGrpPremisesDtoList.get(i).getConStartMM();
                        int conStartDate=0;
                        int conEndDate=0;
                        StringBuilder stringBuilder=new StringBuilder();
                        if(StringUtil.isEmpty(conStartHH)||StringUtil.isEmpty(conStartMM)){
                            errorMap.put("conStartMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            validateTime(errorMap,conStartHH,conStartMM,conStartDate,"conStartMM",i,"UC_CHKLMD001_ERR003");
                        }
                        String conEndHH = appGrpPremisesDtoList.get(i).getConEndHH();
                        String conEndMM = appGrpPremisesDtoList.get(i).getConEndMM();
                        if(StringUtil.isEmpty(conEndHH)||StringUtil.isEmpty(conEndMM)){
                            errorMap.put("conEndMM"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            validateTime(errorMap,conEndHH,conEndMM,conStartDate,"conEndMM",i,"UC_CHKLMD001_ERR003");
                        }
                        if(conEndDate<conStartDate){
                            errorMap.put("conEndMM"+i,"UC_CHKLMD001_ERR003");
                        }

                        //set  time
                        String errorStartMM = errorMap.get("conStartMM"+i);
                        String errorEndMM = errorMap.get("conEndMM"+i);
                        if(StringUtil.isEmpty(errorStartMM) && StringUtil.isEmpty(errorEndMM)){
                            LocalTime startTime = LocalTime.of(Integer.parseInt(conStartHH),Integer.parseInt(conStartMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeFrom(Time.valueOf(startTime));

                            LocalTime endTime = LocalTime.of(Integer.parseInt(conEndHH),Integer.parseInt(conEndMM));
                            appGrpPremisesDtoList.get(i).setWrkTimeTo(Time.valueOf(endTime));
                        }

                        List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodList = appGrpPremisesDtoList.get(i).getAppPremPhOpenPeriodList();
                        if(appPremPhOpenPeriodList!=null){
                            for(AppPremPhOpenPeriodDto every:appPremPhOpenPeriodList){
                                String convEndToHH = every.getConvEndToHH();
                                String convEndToMM = every.getConvEndToMM();
                                String convStartFromHH = every.getConvStartFromHH();
                                String convStartFromMM = every.getConvStartFromMM();
                                if(StringUtil.isEmpty(convEndToHH)&&StringUtil.isEmpty(convEndToMM)&StringUtil.isEmpty(convStartFromHH)
                                        &StringUtil.isEmpty(convStartFromMM)||!StringUtil.isEmpty(convEndToHH)&&!StringUtil.isEmpty(convEndToMM)
                                        &&!StringUtil.isEmpty(convStartFromHH)&!StringUtil.isEmpty(convStartFromMM)){
                                    if(!StringUtil.isEmpty(convEndToHH)&&!StringUtil.isEmpty(convEndToMM)
                                            &&!StringUtil.isEmpty(convStartFromHH)&!StringUtil.isEmpty(convStartFromMM)){
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);
                                            if(i1>=24||i2>=60){
                                                errorMap.put("convStartToHH"+i,"CHKLMD001_ERR003");
                                            }

                                        }catch (Exception e){
                                            errorMap.put("convStartToHH"+i,"CHKLMD001_ERR003");
                                        }
                                        try {
                                            int i3 = Integer.parseInt(convEndToHH);
                                            int i4 = Integer.parseInt(convEndToMM);
                                            if(i3>=24||i4>=60){
                                                errorMap.put("convEndToHH"+i,"CHKLMD001_ERR003");
                                            }
                                        }catch (Exception e){
                                            errorMap.put("convEndToHH"+i,"CHKLMD001_ERR003");
                                        }
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);
                                            int i3 = Integer.parseInt(convEndToHH);
                                            int i4 = Integer.parseInt(convEndToMM);
                                            if((i1*60+i2)>(i3*60+i4)){
                                                errorMap.put("convEndToHH"+i,"CHKLMD001_ERR003");
                                            }
                                        }catch (Exception e){

                                        }
                                    }
                                }else {
                                    if(StringUtil.isEmpty(convStartFromHH)||StringUtil.isEmpty(convStartFromMM)){
                                        errorMap.put("convStartToHH"+i,"UC_CHKLMD001_ERR001");
                                    }else {
                                        try {
                                            int i1 = Integer.parseInt(convStartFromHH);
                                            int i2 = Integer.parseInt(convStartFromMM);
                                            if(i1>=24||i2>=60){
                                                errorMap.put("convStartToHH"+i,"CHKLMD001_ERR003");
                                            }
                                        }catch (Exception e){
                                            errorMap.put("convStartToHH"+i,"CHKLMD001_ERR003");

                                        }
                                    }
                                    if(StringUtil.isEmpty(convEndToHH)||StringUtil.isEmpty(convEndToMM)){
                                        errorMap.put("convEndToHH"+i,"UC_CHKLMD001_ERR001");
                                    }else {

                                        try {
                                            int i3 = Integer.parseInt(convEndToHH);
                                            int i4 = Integer.parseInt(convEndToMM);
                                            if(i3>=24||i4>=60){
                                                errorMap.put("convEndToHH"+i,"CHKLMD001_ERR003");
                                            }

                                        }catch (Exception e){
                                            errorMap.put("convEndToHH"+i,"CHKLMD001_ERR003");

                                        }

                                    }


                                }

                            }

                            //set ph time
                            String errorConvEndToHH = errorMap.get("convEndToHH"+i);
                            if(StringUtil.isEmpty(errorConvEndToHH) && !IaisCommonUtils.isEmpty(appPremPhOpenPeriodList) ){
                                for(AppPremPhOpenPeriodDto ph :appPremPhOpenPeriodList){
                                    if(!StringUtil.isEmpty(ph.getConvStartFromHH()) && !StringUtil.isEmpty(ph.getConvStartFromMM())){
                                        LocalTime startTime = LocalTime.of(Integer.parseInt(ph.getConvStartFromHH()),Integer.parseInt(ph.getConvStartFromMM()));
                                        ph.setStartFrom(Time.valueOf(startTime));
                                    }

                                    if(!StringUtil.isEmpty(ph.getConvEndToHH()) && !StringUtil.isEmpty(ph.getConvEndToMM())){
                                        LocalTime endTime = LocalTime.of(Integer.parseInt(ph.getConvEndToHH()),Integer.parseInt(ph.getConvEndToMM()));
                                        ph.setEndTo(Time.valueOf(endTime));
                                    }

                                }

                            }
                        }
                        String conveyanceVehicleNo = appGrpPremisesDtoList.get(i).getConveyanceVehicleNo();
                        if(StringUtil.isEmpty(conveyanceVehicleNo)){
                            errorMap.put("conveyanceVehicleNo"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            boolean b = VehNoValidator.validateNumber(conveyanceVehicleNo);
                            if(!b){
                                errorMap.put("conveyanceVehicleNo"+i,"CHKLMD001_ERR008");
                            }
                        }

                        String cStreetName = appGrpPremisesDtoList.get(i).getConveyanceStreetName();

                        if(StringUtil.isEmpty(cStreetName)){
                            errorMap.put("conveyanceStreetName"+i,"UC_CHKLMD001_ERR001");
                        }
                        String conveyanceAddressType = appGrpPremisesDtoList.get(i).getConveyanceAddressType();
                        if(StringUtil.isEmpty(conveyanceAddressType)){
                            errorMap.put("conveyanceAddressType"+i, "UC_CHKLMD001_ERR001");
                        }else {
                            boolean empty = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getConveyanceFloorNo());
                            boolean empty1 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getConveyanceBlockNo());
                            boolean empty2 = StringUtil.isEmpty(appGrpPremisesDtoList.get(i).getConveyanceUnitNo());
                            if (ApplicationConsts.ADDRESS_TYPE_APT_BLK.equals(conveyanceAddressType)) {

                                if (empty) {
                                    errorMap.put("conveyanceFloorNo"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty1) {
                                    errorMap.put("conveyanceBlockNos"+i, "UC_CHKLMD001_ERR001");
                                }
                                if (empty2) {
                                    errorMap.put("conveyanceUnitNo"+i, "UC_CHKLMD001_ERR001");
                                }

                            }
                            if(!empty&&!empty1&&!empty2){
                                stringBuilder.append(appGrpPremisesDtoList.get(i).getConveyanceFloorNo())
                                        .append(appGrpPremisesDtoList.get(i).getConveyanceBlockNo())
                                        .append(appGrpPremisesDtoList.get(i).getConveyanceUnitNo());

                            }
                        }
                        String conveyancePostalCode = appGrpPremisesDtoList.get(i).getConveyancePostalCode();
                        if(StringUtil.isEmpty(conveyancePostalCode)){
                            errorMap.put("conveyancePostalCode"+i,"UC_CHKLMD001_ERR001");
                        }else {
                            if(!conveyancePostalCode.matches("^[0-9]{6}$")){
                                errorMap.put("conveyancePostalCode"+i, "UC_CHKLMD001_ERR004");
                            }else {
                                if(!StringUtil.isEmpty(stringBuilder.toString())){
                                    stringBuilder.append(conveyancePostalCode);
                                }

                            }
                        }

                        if(list.contains(stringBuilder.toString())){
                            errorMap.put("conveyancePostalCode"+i,"There is a duplicated entry for this premises address.");
                        }else {
                            list.add(stringBuilder.toString());
                        }

                    }
                } else {
                    //premiseSelect = organization hci code

                }
            }

        }
        log.info(StringUtil.changeForLog("the do doValidatePremiss end ...."));

        return errorMap;
    }
    private Map<String,String> doValidatePremissCgo(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        //do validate premiss
        Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
        AppSvcCgoDto appSvcCgoDto=  (AppSvcCgoDto) ParamUtil.getSessionAttr(bpc.request,"AppSvcCgoDto");
        String mobileNo = appSvcCgoDto.getMobileNo();
        String emailAddr = appSvcCgoDto.getEmailAddr();
        if(!mobileNo.startsWith("8")||!mobileNo.startsWith("9")){
            errorMap.put("mobileNo","Please key in a valid mobile number");
        }
        if(!emailAddr.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")){
            errorMap.put("emailAddr","Please key in a valid email address");
        }
        log.info(StringUtil.changeForLog("the do doValidatePremiss end ...."));
        return errorMap;
    }

    private AppSubmissionDto getAppSubmissionDto(HttpServletRequest request){
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, APPSUBMISSIONDTO);
        if(appSubmissionDto == null){
            log.info(StringUtil.changeForLog("appSubmissionDto is empty "));
            appSubmissionDto = new AppSubmissionDto();
        }
        return appSubmissionDto;
    }

    private void initSession(BaseProcessClass bpc) throws CloneNotSupportedException {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, APPSUBMISSIONDTO);
        if(appSubmissionDto == null){
            appSubmissionDto = new AppSubmissionDto();
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
            List<AppGrpPremisesDto> appGrpPremisesDtoList = IaisCommonUtils.genNewArrayList();
            AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
            appGrpPremisesDtoList.add(appGrpPremisesDto);
            appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
            List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,  AppServicesConsts.HCSASERVICEDTOLIST);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = IaisCommonUtils.genNewArrayList();
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = null;
            for(HcsaServiceDto svc:hcsaServiceDtos){
                appSvcRelatedInfoDto = new AppSvcRelatedInfoDto();
                appSvcRelatedInfoDto.setServiceId(svc.getId());
                appSvcRelatedInfoDto.setServiceCode(svc.getSvcCode());
                appSvcRelatedInfoDto.setServiceType(svc.getSvcType());
                appSvcRelatedInfoDto.setServiceName(svc.getSvcName());
                appSvcRelatedInfoDtoList.add(appSvcRelatedInfoDto);
            }
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtoList);
        }else{
            //set svc info
            appSubmissionDto = NewApplicationHelper.setSubmissionDtoSvcData(bpc.request, appSubmissionDto);
            Object rfi = ParamUtil.getSessionAttr(bpc.request,REQUESTINFORMATIONCONFIG);
            if(rfi != null){
                List<HcsaServiceDto> hcsaServiceDtos = (List<HcsaServiceDto>) ParamUtil.getSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST);
                List<HcsaServiceDto> oneHcsaServiceDto = IaisCommonUtils.genNewArrayList();
                for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                    if(hcsaServiceDto.getId().equals(appSubmissionDto.getRfiServiceId())){
                        oneHcsaServiceDto.add(hcsaServiceDto);
                        break;
                    }
                }
                ParamUtil.setSessionAttr(bpc.request,AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) oneHcsaServiceDto);
            }

            //set premises info
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                    appGrpPremisesDto = NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                }
            }

            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    String currentSvcId = appSvcRelatedInfoDto.getServiceId();
                    List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos = null;
                    if(!StringUtil.isEmpty(currentSvcId)){
                        hcsaSvcSubtypeOrSubsumedDtos= serviceConfigService.loadLaboratoryDisciplines(currentSvcId);
                    }
                    //todo:set AppSvcLaboratoryDisciplinesDto
                    if(!IaisCommonUtils.isEmpty(hcsaSvcSubtypeOrSubsumedDtos)){
                       NewApplicationHelper.setLaboratoryDisciplinesInfo(appSvcRelatedInfoDto,hcsaSvcSubtypeOrSubsumedDtos);
                    }
                    //todo:set AppSvcDisciplineAllocationDto
                    //NewApplicationHelper.setDisciplineAllocationDtoInfo(appSvcRelatedInfoDto);

                }
            }





            //set oldAppSubmission when rfi,rfc,renew
            if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appSubmissionDto.getAppType())
                    ||ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())
                    || rfi != null){
                AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request,NewApplicationDelegator.OLDAPPSUBMISSIONDTO,oldAppSubmissionDto);
            }

        }
        AppEditSelectDto changeSelectDto1 = appSubmissionDto.getChangeSelectDto()==null?new AppEditSelectDto():appSubmissionDto.getChangeSelectDto();
        appSubmissionDto.setChangeSelectDto(changeSelectDto1);
        //set licenseeId
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(bpc.request,"inter-inbox-user-info");
        if(interInboxUserDto != null){
            appSubmissionDto.setLicenseeId(interInboxUserDto.getLicenseeId());
        }else{
            appSubmissionDto.setLicenseeId("");
            log.info(StringUtil.changeForLog("user info is empty....."));
        }

        ParamUtil.setSessionAttr(bpc.request, APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, "IndexNoCount", 0);

        //reload
        Map<String,AppGrpPrimaryDocDto> initBeforeReloadDocMap = IaisCommonUtils.genNewHashMap();
        ParamUtil.setSessionAttr(bpc.request, RELOADAPPGRPPRIMARYDOCMAP, (Serializable) initBeforeReloadDocMap);

        //error_msg
        ParamUtil.setSessionAttr(bpc.request, ERRORMAP_PREMISES, null);
        ParamUtil.setSessionAttr(bpc.request, APPGRPPRIMARYDOCERRMSGMAP, null);

        //init svc psn conifg
        Map<String,List<HcsaSvcPersonnelDto>> svcConfigInfo =  null;
        ParamUtil.setSessionAttr(bpc.request, SERVICEALLPSNCONFIGMAP, (Serializable) svcConfigInfo);

    }

    private void  documentValid(HttpServletRequest request, Map<String,String> errorMap){
        log.info(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList  = appSubmissionDto.getAppGrpPrimaryDocDtos();
        if(appGrpPrimaryDocDtoList == null){
            return;
        }
        for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtoList){
            String keyName = "";
            if(StringUtil.isEmpty(appGrpPrimaryDocDto.getPremisessName()) && StringUtil.isEmpty(appGrpPrimaryDocDto.getPremisessType())){
                //common
                keyName = "common"+appGrpPrimaryDocDto.getSvcComDocId();
            }else{
                keyName = "prem"+appGrpPrimaryDocDto.getSvcComDocId()+appGrpPrimaryDocDto.getPremisessName();
            }
            long length = appGrpPrimaryDocDto.getRealDocSize();
            if(length>4*1024*1024){
                errorMap.put(keyName,"UC_CHKLMD001_ERR007");
                continue;
            }
            Boolean flag=false;
            String name = appGrpPrimaryDocDto.getDocName();
            String substring = name.substring(name.lastIndexOf(".")+1);
            FileType[] fileType = FileType.values();
            for(FileType f:fileType){
                if(f.name().equalsIgnoreCase(substring)){
                    flag=true;
                }
            }
            if(!flag){
                errorMap.put(keyName,"Wrong file type");
            }
        }
    }

    private Map<String,List<HcsaSvcPersonnelDto>> getAllSvcAllPsnConfig(HttpServletRequest request){
        Map<String,List<HcsaSvcPersonnelDto>> svcAllPsnConfig = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(request, SERVICEALLPSNCONFIGMAP);
        if(svcAllPsnConfig == null){
            AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                svcIds.add(appSvcRelatedInfoDto.getServiceId());
            }
            List<HcsaServiceStepSchemeDto>  svcStepConfigs = serviceConfigService.getHcsaServiceStepSchemesByServiceId(svcIds);
            svcAllPsnConfig = serviceConfigService.getAllSvcAllPsnConfig(svcStepConfigs, svcIds);
        }
        return svcAllPsnConfig;
    }


}

