package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.api.config.GatewayConstants;
import com.ecquaria.cloud.moh.iais.api.services.GatewayAPI;
import com.ecquaria.cloud.moh.iais.api.services.GatewayNetsAPI;
import com.ecquaria.cloud.moh.iais.api.services.GatewayPayNowAPI;
import com.ecquaria.cloud.moh.iais.api.services.GatewayStripeAPI;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPsnTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.memorypage.PaginationHandler;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.rfcutil.EqRequestForChangeSubmitResultChange;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator.ACKMESSAGE;

/****
 *
 *   @date 1/4/2020
 *   @author zixian
 */
@Slf4j
@Delegator("MohRequestForChangeMenuDelegator")
public class RequestForChangeMenuDelegator {
    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(PersonnelQueryDto.class)
            .searchAttr("PersonnelSearchParam")
            .resultAttr("PersonnelSearchResult")
            .sortField("NAME").sortType(SearchParam.ASCENDING).build();

    private final FilterParameter premiseFilterParameter = new FilterParameter.Builder()
            .clz(PremisesListQueryDto.class)
            .searchAttr("PremisesSearchParam")
            .resultAttr("PremisesSearchResult")
            .sortField("LICENCE_ID").sortType(SearchParam.ASCENDING).build();

    @Autowired
    private RequestForChangeService requestForChangeService;
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private ApplicationFeClient applicationFeClient;
    @Autowired
    private SystemParamConfig systemParamConfig;

    /**
     * @param bpc
     * @Decription start
     */
    public void start(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("the do start start ...."));
        appSubmissionService.clearSession(bpc.request);
        removeSession(bpc);
        String appNo = ParamUtil.getMaskedString(bpc.request, "appNo");
        HcsaServiceCacheHelper.flushServiceMapping();
        premiseFilterParameter.setPageSize(SystemParamUtil.getDefaultPageSize());
        premiseFilterParameter.setPageNo(1);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_REQUEST_FOR_CHANGE, AuditTrailConsts.FUNCTION_PREMISES_LIST);
        requestForInformation(bpc, appNo);
        log.info(StringUtil.changeForLog("the do start end ...."));
    }

    private void removeSession(BaseProcessClass bpc) {
        bpc.getSession().removeAttribute("dAmount");
        bpc.getSession().removeAttribute("payMethod");
        bpc.getSession().removeAttribute("premiseDoSearch");
        bpc.getSession().removeAttribute("doSearch");
        bpc.getSession().removeAttribute("personnelListDtos");
        bpc.getSession().removeAttribute("licenceDtoList");
        ParamUtil.setSessionAttr(bpc.request, "txnRefNo", null);
        ParamUtil.setSessionAttr(bpc.request, "emailAddress", null);
        ParamUtil.setSessionAttr(bpc.request, "createDate", null);
        ParamUtil.setSessionAttr(bpc.request,"personListAmend", null);
        ParamUtil.setSessionAttr(bpc.request, ACKMESSAGE, null);
    }

    /**
     * @param bpc
     * @Decription personnleListStart
     */
    public void personnleListStart(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("the do personnleListStart start ...."));
        appSubmissionService.clearSession(bpc.request);
        removeSession(bpc);
        filterParameter.setPageSize(SystemParamUtil.getDefaultPageSize());
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_REQUEST_FOR_CHANGE, AuditTrailConsts.FUNCTION_PERSONAL_LIST);
        log.info(StringUtil.changeForLog("the do personnleListStart end ...."));
    }

    /**
     * @param bpc
     * @Decription prepare  premises List entrance
     */
    public void prepare(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the prepare start ...."));
        String action = (String) ParamUtil.getRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if (StringUtil.isEmpty(action)) {
            action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
            if (StringUtil.isEmpty(action)) {
                action = "prePremisesList";
            }
        }
        Object rfi = ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        if (rfi != null) {
            action = "prePremisesEdit";
            if ("prePremisesList".equals(action)) {
                action = "prePremisesEdit";
            }
        }
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, action);
        log.debug(StringUtil.changeForLog("the prepare end ...."));
    }


    /**
     * @param bpc
     * @Decription controlSwitch
     */
    public void controlSwitch(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the controlSwitch start ...."));
        String switchValue = (String) ParamUtil.getRequestAttr(bpc.request, RfcConst.SWITCH_VALUE);
        if (StringUtil.isEmpty(switchValue)) {
            switchValue = "loading";
        }
        if ("doSubmit".equals(switchValue)) {
            Object rfi = ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
            if (rfi != null) {
                switchValue = "doRfi";
            }
        }

        ParamUtil.setRequestAttr(bpc.request, "switch", switchValue);
        log.debug(StringUtil.changeForLog("the controlSwitch end ...."));
    }

    /**
     * @param bpc
     * @Decription preparePremisesList
     */
    public void preparePremisesList(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do preparePremisesList start ...."));
        //set licenseeId
        String doSearch = (String) bpc.request.getSession().getAttribute("doSearch");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        SearchParam searchParam = SearchResultHelper.getSearchParam(bpc.request, premiseFilterParameter, false);
        searchParam.addFilter("licenseeId", licenseeId, true);
        if (!StringUtil.isEmpty(doSearch)) {
            searchParam.addFilter("type", doSearch, true);
            bpc.request.getSession().setAttribute("premiseDoSearch", doSearch);
        }
        searchParam.addParam("activeMigrated", IaisEGPHelper.isActiveMigrated());
        QueryHelp.setMainSql("applicationPersonnelQuery", "queryPremises", searchParam);
        SearchResult<PremisesListQueryDto> searchResult = requestForChangeService.searchPreInfo(searchParam);
        if (!StringUtil.isEmpty(searchResult)) {

            ParamUtil.setRequestAttr(bpc.request, "PremisesSearchParam", searchParam);
            ParamUtil.setRequestAttr(bpc.request, "PremisesSearchResult", searchResult);
        }
        List<PremisesListQueryDto> rows = searchResult.getRows();
        for (PremisesListQueryDto premisesListQueryDto : rows) {
            StringBuilder stringBuilder = new StringBuilder();
            List<LicenceDto> licenceDtoByHciCode = requestForChangeService.getLicenceDtoByHciCode(premisesListQueryDto.getHciCode(), licenseeId);
            for (LicenceDto licenceDto : licenceDtoByHciCode) {
                stringBuilder.append(licenceDto.getSvcName()).append(", ");
            }
            premisesListQueryDto.setSvcId(stringBuilder.substring(0, stringBuilder.toString().lastIndexOf(',')));
        }
        if (rows.isEmpty()) {
            bpc.request.setAttribute("ACK018", MessageUtil.getMessageDesc("GENERAL_ACK018"));
        }
        ParamUtil.setSessionAttr(bpc.request, RfcConst.PREMISESLISTDTOS, (Serializable) rows);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE, "Mode of Service Delivery List");
        List<SelectOption> list = new ArrayList<>();
        setSelectOption(list);
        ParamUtil.setRequestAttr(bpc.request, "applicationType", (Serializable) list);
        log.debug(StringUtil.changeForLog("the do preparePremisesList end ...."));
    }

    private void setSelectOption(List<SelectOption> list) {
        SelectOption onsite = new SelectOption();
        onsite.setText("Premises");
        onsite.setValue("ONSITE");
        SelectOption conveyance = new SelectOption();
        conveyance.setText("Conveyance");
        conveyance.setValue("CONVEYANCE");
        SelectOption offsiet = new SelectOption();
        offsiet.setText("Off-site");
        offsiet.setValue("OFFSITE");
        SelectOption EASMTSsiet = new SelectOption();
        EASMTSsiet.setText("Conveyance (in a mobile clinic / ambulance)");
        EASMTSsiet.setValue("EASMTS");
        list.add(offsiet);
        list.add(conveyance);
        list.add(onsite);
        list.add(EASMTSsiet);
        list.sort(Comparator.comparing(SelectOption::getText));
    }


    public void doSort(BaseProcessClass bpc) {
        log.info("-----------doSort -------");
    }


    public void doPage(BaseProcessClass bpc) {
        log.info("---------doPage -------");
        premiseFilterParameter.setPageNo(1);
        SearchResultHelper.doPage(bpc.request, premiseFilterParameter);
    }


    public void doSearch(BaseProcessClass bpc) {
        String crud_action_value = bpc.request.getParameter("crud_action_value");
        premiseFilterParameter.setPageNo(1);
        if (!StringUtil.isEmpty(crud_action_value)) {
            bpc.request.getSession().setAttribute("doSearch", crud_action_value);
        } else {
            bpc.request.getSession().removeAttribute("doSearch");
            bpc.request.getSession().removeAttribute("premiseDoSearch");
        }
    }

    /**
     * @param bpc
     * @Decription preparePremisesEdit
     */
    public void preparePremisesEdit(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the do preparePremisesEdit start ...."));
        NewApplicationHelper.setTimeList(bpc.request);

        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setPremisesEdit(true);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        if (appSubmissionDto != null) {
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
            if (!StringUtil.isEmpty(licenseeId)) {
                appSubmissionDto.setLicenseeId(licenseeId);
            }
        }
        PremisesListQueryDto premisesListQueryDto = (PremisesListQueryDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.PREMISESLISTQUERYDTO);

        List<AppGrpPremisesDto> reloadPremisesDtoList = IaisCommonUtils.genNewArrayList();
        AppGrpPremisesDto appGrpPremisesDto = null;
        Object rfi = ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        if (appSubmissionDto != null) {
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            if (rfi == null) {
                if (!IaisCommonUtils.isEmpty(appGrpPremisesDtoList) && premisesListQueryDto != null) {
                    String premType = premisesListQueryDto.getPremisesType();
                    String premHciOrConvName = "";
                    if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premType)) {
                        premHciOrConvName = premisesListQueryDto.getHciName();
                    } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premType)) {
                        premHciOrConvName = premisesListQueryDto.getVehicleNo();
                    }
                    appGrpPremisesDto = getAppGrpPremisesDtoFromAppGrpPremisesDtoList(appGrpPremisesDtoList, premType, premHciOrConvName);
                    if (appGrpPremisesDto == null) {
                        //todo:get prem by indexNo
                        appGrpPremisesDto = appSubmissionDto.getAppGrpPremisesDtoList().get(0);
                    }
                    reloadPremisesDtoList.add(appGrpPremisesDto);
                }
            } else {
                reloadPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            }
            for (AppGrpPremisesDto appGrpPremisesDto1 : appGrpPremisesDtoList) {
                String hciCode = appGrpPremisesDto1.getHciCode();
                List<LicenceDto> licenceDtoList = requestForChangeService.getLicenceDtoByHciCode(hciCode, licenseeId);
                List<LicenceDto> licenceDtos = appGrpPremisesDto1.getLicenceDtos();
                if(licenceDtos==null){
                    appGrpPremisesDto1.setLicenceDtos(licenceDtoList);
                    bpc.request.getSession().setAttribute("licenceDtoList",licenceDtoList);
                }
            }
        }
        if (appGrpPremisesDto != null || rfi != null) {
            log.info(StringUtil.changeForLog("The preparePremises licenseeId is -->:" + licenseeId));
            //premise select
            NewApplicationHelper.setPremSelect(bpc.request);
            if (rfi == null) {
                //when rfc/renew check is select existing premises
                String oldPremSel = NewApplicationHelper.getPremisesKey(premisesListQueryDto);
                if (oldPremSel.equals(appGrpPremisesDto.getPremisesSelect()) || "-1".equals(appGrpPremisesDto.getPremisesSelect())) {
                    ParamUtil.setRequestAttr(bpc.request, "PageCanEdit", AppConsts.TRUE);
                }
                if(appSubmissionDto.getAppGrpPremisesDtoList()!=null){
                    for (AppGrpPremisesDto appGrpPremisesDto1 : appSubmissionDto.getAppGrpPremisesDtoList()) {
                        NewApplicationHelper.setWrkTime(appGrpPremisesDto1);
                    }
                }
            }
        }
        AppSubmissionDto oldAppSubmissionDto ;
        oldAppSubmissionDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
        AppSubmissionDto appSubmissionDto1 = (AppSubmissionDto) bpc.request.getSession().getAttribute("oldAppSubmissionDto");
        if (appSubmissionDto1 != null) {
            oldAppSubmissionDto = appSubmissionDto1;
        }
        //init to set this session , if have do not to check change premise hcicode
        Object o = bpc.getSession().getAttribute("licenceDtoList");
        if (premisesListQueryDto != null && o==null) {
            String hciCode = premisesListQueryDto.getHciCode();
            List<LicenceDto> licenceDtoList = requestForChangeService.getLicenceDtoByHciCode(hciCode, licenseeId);
            bpc.request.getSession().setAttribute("licenceDtoList", licenceDtoList);
        }

        if (appSubmissionDto != null) {
            appSubmissionDto.setAppGrpPremisesDtoList(reloadPremisesDtoList);
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        }
        /*boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        if(isRfi&&appSubmissionDto!= null&&appSubmissionDto.getAppGrpPremisesDtoList()!=null){
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            for(AppGrpPremisesDto v : appGrpPremisesDtoList){
                String hciCode = v.getHciCode();
                String oldHciCode = v.getOldHciCode();
                if(hciCode!=null&&oldHciCode!=null){
                    boolean equals = hciCode.equals(oldHciCode);
                    v.setExistingData(AppConsts.YES);
                    bpc.request.getSession().setAttribute("eqHciCode",String.valueOf(equals));
                }else if(hciCode==null){
                    v.setExistingData(AppConsts.NO);
                    bpc.request.getSession().setAttribute("eqHciCode","true");
                }
            }

        }else if (appSubmissionDto != null) {
            boolean eqHciCode = EqRequestForChangeSubmitResultChange.eqHciCode(appSubmissionDto.getAppGrpPremisesDtoList().get(0), oldAppSubmissionDto.getAppGrpPremisesDtoList().get(0));
            appSubmissionDto.getAppGrpPremisesDtoList().get(0).setExistingData(AppConsts.NO);
            bpc.request.setAttribute("eqHciCode",String.valueOf(eqHciCode));
        }*/
        ParamUtil.setRequestAttr(bpc.request, RfcConst.RELOADPREMISES, reloadPremisesDtoList);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, "oldAppSubmissionDto", oldAppSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE, "Mode of Service Delivery Amendment");
        ParamUtil.setRequestAttr(bpc.request, "premisesList", AppConsts.YES);
        List<SelectOption> weeklyOpList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DAY_NAMES);
        ParamUtil.setRequestAttr(bpc.request, "weeklyOpList", weeklyOpList);
        List<SelectOption> phOpList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PUBLIC_HOLIDAY);
        ParamUtil.setRequestAttr(bpc.request, "phOpList", phOpList);
        ParamUtil.setRequestAttr(bpc.request, "weeklyCount", systemParamConfig.getWeeklyCount());
        ParamUtil.setRequestAttr(bpc.request, "phCount", systemParamConfig.getPhCount());
        ParamUtil.setRequestAttr(bpc.request, "eventCount", systemParamConfig.getEventCount());
        ParamUtil.setRequestAttr(bpc.request,"postalCodeAckMsg",MessageUtil.getMessageDesc("NEW_ACK016"));
        log.debug(StringUtil.changeForLog("the do preparePremisesEdit end ...."));
        ParamUtil.setRequestAttr(bpc.request, "not_view", "notView");
    }

    /**
     * @param bpc
     * @Decription doPremisesList
     *//*
     */
    public void doPremisesList(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do doPremisesList start ...."));
        String crudActionType = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (StringUtil.isEmpty(crudActionType)) {
            crudActionType = "back";
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
            return;
        }
        String crud_action_type = bpc.request.getParameter("crud_action_type");
        String index = ParamUtil.getString(bpc.request, "hiddenIndex");
        String licId = ParamUtil.getMaskedString(bpc.request, "licId" + index);
        String premId = ParamUtil.getMaskedString(bpc.request, "premisesId" + index);
        PremisesListQueryDto premisesListQueryDto = new PremisesListQueryDto();
        AppSubmissionDto appSubmissionDto = null;
        String status = "";
        if (!StringUtil.isEmpty(licId) && !StringUtil.isEmpty(premId)) {
            List<PremisesListQueryDto> premisesListQueryDtos = (List<PremisesListQueryDto>) ParamUtil.getSessionAttr(bpc.request, RfcConst.PREMISESLISTDTOS);
            if (!IaisCommonUtils.isEmpty(premisesListQueryDtos)) {
                premisesListQueryDto = getPremisesListQueryDto(premisesListQueryDtos, licId, premId);
                if (premisesListQueryDto != null) {
                    appSubmissionDto = requestForChangeService.getAppSubmissionDtoByLicenceId(premisesListQueryDto.getLicenceId());
                    List<String> names = IaisCommonUtils.genNewArrayList();
                    if (appSubmissionDto != null) {
                        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                        // from draft,rfi
                        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                        if (appSvcRelatedInfoDtoList != null && appSvcRelatedInfoDtoList.size() > 0) {
                            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList) {
                                if (!StringUtil.isEmpty(appSvcRelatedInfoDto.getServiceName())) {
                                    names.add(appSvcRelatedInfoDto.getServiceName());
                                }
                            }
                        }
                    }
                    if (!IaisCommonUtils.isEmpty(names)) {
                        List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getHcsaServiceByNames(names);
                        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
                        NewApplicationHelper.setSubmissionDtoSvcData(bpc.request, appSubmissionDto);
                    }
                    status = premisesListQueryDto.getLicenceStatus();
                }
            }
        }

        if ("doPage".equals(crud_action_type)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePremisesList");
        } else if ("doSearch".equals(crud_action_type)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePremisesList");
        } else if (!ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(status)) {
            ParamUtil.setRequestAttr(bpc.request, "Error_Status", "licence status is not active");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePremisesList");
        }

        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, appSubmissionDto);
        NewApplicationHelper.setOldAppSubmissionDto((AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDto), bpc.request);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.PREMISESLISTQUERYDTO, premisesListQueryDto);

        //ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, crudActionType);
        log.debug(StringUtil.changeForLog("the do doPremisesList end ...."));

    }

    /**
     * @param bpc
     * @Decription doPremisesEdit
     */
    public void doPremisesEdit(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do doPremisesEdit start ...."));
        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
        if ("back".equals(action)) {
            return;
        }
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = NewApplicationDelegator.genAppGrpPremisesDtoList(bpc.request);
        ParamUtil.setRequestAttr(bpc.request, RfcConst.RELOADPREMISES, appGrpPremisesDtoList);
        List<LicenceDto> licenceDtoList = (List<LicenceDto>)bpc.getSession().getAttribute("licenceDtoList");
        if(appGrpPremisesDtoList!=null&&!appGrpPremisesDtoList.isEmpty()){
            appGrpPremisesDtoList.get(0).setLicenceDtos(licenceDtoList);
        }
        appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);

        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "oldAppSubmissionDto");

        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        Map<String, String> errorMap = requestForChangeService.doValidatePremiss(appSubmissionDto, oldAppSubmissionDto, null,
                isRfi, true);
        String crud_action_type_continue = bpc.request.getParameter("crud_action_type_continue");
        String crud_action_type_form_value = bpc.request.getParameter("crud_action_type_form_value");
        String crud_action_additional = bpc.request.getParameter("crud_action_additional");
        if ("exitSaveDraft".equals(crud_action_additional)) {
            if (appSubmissionDto.getDraftNo() == null) {
                String draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
                appSubmissionDto.setDraftNo(draftNo);
            }
            /*  appSubmissionService.doSaveDraft(appSubmissionDto);*/
            jumpYeMian(bpc.request, bpc.response);
            return;
        }
        bpc.request.setAttribute("continueStep", crud_action_type_form_value);
        bpc.request.setAttribute("crudActionTypeContinue", crud_action_type_continue);
        if ("continue".equals(crud_action_type_continue)) {
            errorMap.remove("hciNameUsed");
        }
        String string = errorMap.get("hciNameUsed");
        if (string != null) {
            bpc.request.setAttribute("hciNameUsed", "hciNameUsed");
        }
        if (errorMap.size() > 0) {
            String hciNameUsed = errorMap.get("hciNameUsed");
            if (!StringUtil.isEmpty(hciNameUsed)) {
                ParamUtil.setRequestAttr(bpc.request, "newAppPopUpMsg", hciNameUsed);
            }
            bpc.request.setAttribute("errormapIs", "error");
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePremisesEdit");
            return;
        }

        ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "doSubmit");
        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, appSubmissionDto);
        //test
        //ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePremisesEdit");
        log.debug(StringUtil.changeForLog("the do doPremisesEdit end ...."));
    }

    private List<String> getSelectLicence(HttpServletRequest request) {
        List<String> list = IaisCommonUtils.genNewArrayList();
        String[] licenceNames = ParamUtil.getMaskedStrings(request, "licenceName");
        for (String str : licenceNames) {
            if (!StringUtil.isEmpty(str)) {
                list.add(str);
            }
        }
        return list;
    }

    /**
     * @param bpc
     * @Decription preparePersonnel  personnel List entrance
     */
    public void preparePersonnel(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the preparePersonnel start ...."));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        String psnTypeSearch = ParamUtil.getRequestString(bpc.request, "psnType");
        SearchParam searchParam = SearchResultHelper.getSearchParam(bpc.request, filterParameter, true);
        searchParam.setPageSize(0);
        searchParam.setPageNo(0);
        String personName = ParamUtil.getRequestString(bpc.request, "perName");
        if (!StringUtil.isEmpty(personName)) {
            searchParam.addFilter("personName",  personName , true);
        }
        searchParam.addFilter("licenseeId", licenseeId, true);
        searchParam.addParam("activeMigrated", IaisEGPHelper.isActiveMigrated());
        QueryHelp.setMainSql("applicationPersonnelQuery", "appPersonnelQuery", searchParam);
        SearchResult searchResult = requestForChangeService.psnDoQuery(searchParam);

        if (!StringUtil.isEmpty(searchResult)) {
            ParamUtil.setSessionAttr(bpc.request, "PersonnelSearchParam", searchParam);
            ParamUtil.setRequestAttr(bpc.request, "PersonnelSearchResult", searchResult);
        }
        List<PersonnelQueryDto> personnelQueryDtos = searchResult.getRows();
        PersonnelTypeDto personnelTypeDto = new PersonnelTypeDto();
        personnelTypeDto.setPersonnelQueryDtoList(personnelQueryDtos);
        personnelTypeDto.setPsnType(psnTypeSearch);
        List<PersonnelListDto> personnelListDtos = requestForChangeService.getPersonnelListDto(personnelTypeDto);
        if (!IaisCommonUtils.isEmpty(personnelListDtos)) {
            PaginationHandler<PersonnelListDto> handler = new PaginationHandler<>("personPagDiv", "personBodyDiv");
            handler.setAllData(personnelListDtos);
            handler.preLoadingPage();
        } else {
            ParamUtil.setRequestAttr(bpc.request, "noRecord", "Y");
        }
        List<SelectOption> personelRoles = getPsnType();
        ParamUtil.setRequestAttr(bpc.request, "PersonnelRoleList", personelRoles);
        ParamUtil.setSessionAttr(bpc.request, "personnelListDtos", (Serializable) personnelListDtos);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE, "Personnel List");
        List<SelectOption> specialtySelectList = genSpecialtySelectList();
        List<SelectOption> replaceOptions = genReplacePersonnel(licenseeId);
        ParamUtil.setSessionAttr(bpc.request, "SpecialtySelectList", (Serializable) specialtySelectList);
        ParamUtil.setSessionAttr(bpc.request, "replaceOptions", (Serializable) replaceOptions);

        log.debug(StringUtil.changeForLog("the do preparePersonnelList end ...."));
        log.debug(StringUtil.changeForLog("the preparePersonnel end ...."));
    }

    public void initPsnEditInfo(BaseProcessClass bpc) {
        bpc.request.getSession().removeAttribute("oldPersonnelDtooldPersonnelDto");
        String idNo = ParamUtil.getMaskedString(bpc.request, "personnelNo");
        List<PersonnelListDto> personnelList = (List<PersonnelListDto>) ParamUtil.getSessionAttr(bpc.request, "personnelListDtos");
        PersonnelListDto personnelEditDto = null;
        if (personnelList != null) {
            String finalIdNo = Optional.ofNullable(idNo).orElse("");
            personnelEditDto = personnelList.stream().filter(dto -> finalIdNo.equals(dto.getIdNo())).findAny().orElse(null);
        }
        log.info(StringUtil.changeForLog("personnelEditDto: " +
                Optional.ofNullable(personnelEditDto).map(PersonnelListDto::getIdNo).orElse(null)));
        ParamUtil.setSessionAttr(bpc.request, "oldPersonnelDto", transferDto(personnelEditDto));
        ParamUtil.setSessionAttr(bpc.request, "personnelEditDto", personnelEditDto);
    }

    public void preparePersonnelEdit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doPersonnelList start ...."));
        PersonnelListDto personnelEditDto = (PersonnelListDto) ParamUtil.getSessionAttr(bpc.request, "personnelEditDto");
        if (personnelEditDto == null) {
            personnelEditDto = new PersonnelListDto();
            personnelEditDto.setLicPsnTypeDtoMaps(IaisCommonUtils.genNewHashMap(0));
        }
        String replaceName = ParamUtil.getString(bpc.request, "replaceName");
        if (StringUtil.isEmpty(replaceName)) {
            ParamUtil.setRequestAttr(bpc.request, "replaceName", replaceName);
        }
        Map<String, LicPsnTypeDto> licPsnTypeDtoMaps = personnelEditDto.getLicPsnTypeDtoMaps();
        Set<String> psnTypes = IaisCommonUtils.genNewHashSet();
        for (LicPsnTypeDto dto : licPsnTypeDtoMaps.values()) {
            List<String> psnTypes1 = dto.getPsnTypes();
            if (!IaisCommonUtils.isEmpty(psnTypes1)) {
                for (String type : psnTypes1) {
                    psnTypes.add(type);
                }
            }
        }
        String onlyKAH = "0";
        if (psnTypes.size() == 1 && psnTypes.contains("KAH")){
            onlyKAH = "1";
        }
        ParamUtil.setRequestAttr(bpc.request, "psnTypes", psnTypes);
        ParamUtil.setRequestAttr(bpc.request, "onlyKAH", onlyKAH);
        ParamUtil.setSessionAttr(bpc.request, "personnelEditDto", personnelEditDto);
        log.debug(StringUtil.changeForLog("the do doPersonnelList end ...."));
        List<SelectOption> idTypeSelectList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ID_TYPE);
        ParamUtil.setRequestAttr(bpc.request, ClinicalLaboratoryDelegator.DROPWOWN_IDTYPESELECT, idTypeSelectList);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE, "Personnel Amendment");
        log.debug(StringUtil.changeForLog("the do preparePersonnelEdit end ...."));
    }

    public void doPersonnelEdit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doPersonnelEdit start ...."));
        String actionType = ParamUtil.getRequestString(bpc.request, "actionType");
        if ("back".equals(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, "action_type", "back");
            return;
        }
        PersonnelListDto personnelEditDto = (PersonnelListDto) ParamUtil.getSessionAttr(bpc.request, "personnelEditDto");
        PersonnelListDto oldPersonnelDto = (PersonnelListDto) ParamUtil.getSessionAttr(bpc.request, "oldPersonnelDto");
        PersonnelListDto newPerson = new PersonnelListDto();
        Map<String, String> errMap = valiant(bpc, personnelEditDto, newPerson);
        if (!errMap.isEmpty()) {
            WebValidationHelper.saveAuditTrailForNoUseResult(errMap);
            return;
        }
        List<String> licenceIds = personnelEditDto.getLicenceIds();
        //is edit
        boolean edit = isEdit(personnelEditDto, oldPersonnelDto, newPerson);
        if (!edit) {
            ParamUtil.setRequestAttr(bpc.request, "action_type", "valid");
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "personnelEdit");
            return;
        }
        if (IaisCommonUtils.isEmpty(licenceIds)) {
            ParamUtil.setRequestAttr(bpc.request, "action_type", "valid");
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "personnelAck");
            return;
        } else {
            Boolean allCanRfc = requestForChangeService.isAllCanRfc(licenceIds);
            if (!allCanRfc) {
                ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "personnelAck");
                ParamUtil.setRequestAttr(bpc.request, "action_type", "valid");
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
                return;
            }
        }
        List<AppSubmissionDto> appSubmissionDtos = requestForChangeService.getAppSubmissionDtoByLicenceIds(licenceIds);
        // validate the related app submissions
        Map<AppSubmissionDto, List<String>> errorListMap = IaisCommonUtils.genNewHashMap();
        for (AppSubmissionDto dto : appSubmissionDtos) {
            List<String> errorList = appSubmissionService.doPreviewSubmitValidate(null, dto, false);
            if (!errorList.isEmpty()) {
                errorListMap.put(dto, errorList);
            }
        }
        if (!errorListMap.isEmpty()) {
            bpc.request.setAttribute(NewApplicationConstant.SHOW_OTHER_ERROR, NewApplicationHelper.getErrorMsg(errorListMap));
            ParamUtil.setRequestAttr(bpc.request, "action_type", "valid");
            return;
        }
        String appGroupNo = requestForChangeService.getApplicationGroupNumber(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        String draftNo = appSubmissionService.getDraftNo(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setServiceEdit(true);
        String editSelect = ParamUtil.getString(bpc.request, "editSelect");
        if ("replace".equals(editSelect)) {
            appEditSelectDto.setChangePersonnel(!Objects.equals(newPerson.getIdNo(), oldPersonnelDto.getIdNo()));
        }
        log.info(StringUtil.changeForLog("The App Edit Select Dto - " + JsonUtil.parseToJson(appEditSelectDto)));
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
            requestForChangeService.checkAffectedAppSubmissions(appSubmissionDto, null, 0.0D, draftNo, appGroupNo,
                    appEditSelectDto, null, bpc.request);
            if ("replace".equals(editSelect)) {
                replacePersonnelDate(appSubmissionDto, newPerson, oldPersonnelDto.getIdNo());
            } else {
                setPersonnelDate(appSubmissionDto, personnelEditDto);
            }
        }
        //save
        List<AppSubmissionDto> appSubmissionDtos1 = requestForChangeService.saveAppsBySubmissionDtos(appSubmissionDtos);
        ParamUtil.setRequestAttr(bpc.request, "action_type", "bank");
        appSubmissionDtos.get(0).setLicenceNo(null);
        ParamUtil.setSessionAttr(bpc.request, "AppSubmissionDto", appSubmissionDtos.get(0));
        bpc.request.getSession().setAttribute("appSubmissionDtos", appSubmissionDtos1);
        bpc.request.getSession().setAttribute("personnelEditDto",personnelEditDto);
        log.debug(StringUtil.changeForLog("the do doPersonnelEdit end ...."));
    }

    public void paymentSwitch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the paymentSwitch start ...."));
        String switchVal = ParamUtil.getString(bpc.request,"psnSwitch");
        if("back".equals(switchVal)){
            AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);
            appSubmissionDto.setAppGrpNo(null);
            appSubmissionDto.setId(null);
            appSubmissionDto.setAppGrpId(null);
            ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO,appSubmissionDto);
        }else {
            List<AppSubmissionDto> appSubmissionDtos=( List<AppSubmissionDto>)bpc.request.getSession().getAttribute("appSubmissionDtos");
            if(appSubmissionDtos!=null){
                updateGroupStatus(appSubmissionDtos);
            }
            try {
                if (appSubmissionDtos!=null&&appSubmissionDtos.get(0).getAppType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)) {
                    requestForChangeService.sendRfcSubmittedEmail(appSubmissionDtos, appSubmissionDtos.get(0).getPaymentMethod());
                }
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
        }

        log.debug(StringUtil.changeForLog("the paymentSwitch end ...."));
    }

    private boolean isEdit(PersonnelListDto newDto, PersonnelListDto oldDto, PersonnelListDto newPersonDto) {
        PersonnelListDto compareNewDto = null;
        if (!StringUtil.isEmpty(newPersonDto.getIdNo())) {
            compareNewDto = transferDto(newPersonDto);
        } else {
            compareNewDto = transferDto(newDto);
        }
        if (compareNewDto != null && !compareNewDto.equals(oldDto)) {
            return true;
        } else {
            return false;
        }
    }

    private Map<String, String> valiant(BaseProcessClass bpc, PersonnelListDto personnelEditDto, PersonnelListDto newPerson) {
        //update
        String psnName = ParamUtil.getString(bpc.request, "psnName");
        String salutation = ParamUtil.getString(bpc.request, "salutation");
        String email = ParamUtil.getString(bpc.request, "emailAddr");
        String mobile = ParamUtil.getString(bpc.request, "mobileNo");
        String officeTelNo = ParamUtil.getString(bpc.request, "officeTelNo");
        String designation = ParamUtil.getString(bpc.request, "designation");
        String otherDesignation = ParamUtil.getString(bpc.request, "otherDesignation");
        //new
        String salutation1 = ParamUtil.getString(bpc.request, "salutation1");
        String psnName1 = ParamUtil.getString(bpc.request, "psnName1");
        String idType1 = ParamUtil.getString(bpc.request, "idType1");
        String idNo1 = StringUtil.toUpperCase(ParamUtil.getString(bpc.request, "idNo1"));
        String email1 = ParamUtil.getString(bpc.request, "emailAddr1");
        String mobile1 = ParamUtil.getString(bpc.request, "mobileNo1");
        String designation1 = ParamUtil.getString(bpc.request, "designation1");
        String otherDesignation1 = ParamUtil.getString(bpc.request, "otherDesignation1");
        String officeTelNo1 = ParamUtil.getString(bpc.request, "officeTelNo1");
        String replaceName = ParamUtil.getString(bpc.request, "replaceName");
        String editSelect = ParamUtil.getString(bpc.request, "editSelect");
        ParamUtil.setRequestAttr(bpc.request, "editSelectResult", editSelect);
        Map<String, LicPsnTypeDto> licPsnTypeDtoMaps = personnelEditDto.getLicPsnTypeDtoMaps();
        List<String> psnTypes = IaisCommonUtils.genNewArrayList();
        for (LicPsnTypeDto dto : licPsnTypeDtoMaps.values()) {
            psnTypes.addAll(dto.getPsnTypes());
        }
        boolean onlyKAH = true;
        for (String psnType : psnTypes){
            if(!"KAH".equals(psnType)){
                onlyKAH = false;
                break;
            }
        }
        personnelEditDto.setEmailAddr(email);
        personnelEditDto.setMobileNo(mobile);
        personnelEditDto.setSalutation(salutation);
        personnelEditDto.setPsnName(psnName);
        if (psnTypes.contains("CGO")|| psnTypes.contains("CD")) {
            personnelEditDto.setDesignation(designation);
            personnelEditDto.setOtherDesignation(otherDesignation);
        }
        if (psnTypes.contains("PO") || psnTypes.contains("DPO")) {
            personnelEditDto.setDesignation(designation);
            personnelEditDto.setOtherDesignation(otherDesignation);
            personnelEditDto.setOfficeTelNo(officeTelNo);
        }
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String emailMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Email Address", "field");
        String designationMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Designation", "field");
        if ("replace".equals(editSelect) && "new".equals(replaceName)) {
            newPerson.setIdNo(idNo1);
            newPerson.setIdType(idType1);
            newPerson.setPsnName(psnName1);
            newPerson.setSalutation(salutation1);
            newPerson.setDesignation(designation1);
            newPerson.setOtherDesignation(otherDesignation1);
            newPerson.setEmailAddr(email1);
            newPerson.setMobileNo(mobile1);
            newPerson.setOfficeTelNo(officeTelNo1);
            newPerson.setLicPsnTypeDtoMaps(licPsnTypeDtoMaps);
            if (StringUtil.isEmpty(email1) && !onlyKAH) {
                errMap.put("emailAddr1", emailMsg);
            } else if (!StringUtil.isEmpty(email1)) {
                if (!ValidationUtils.isEmail(email1)) {
                    errMap.put("emailAddr1", "GENERAL_ERR0014");
                }
            }
            if (StringUtil.isEmpty(mobile1) && !onlyKAH) {
                errMap.put("mobileNo1", MessageUtil.replaceMessage("GENERAL_ERR0006", "Mobile No.", "field"));
            } else if (!StringUtil.isEmpty(mobile1)) {
                if (!mobile1.matches("^[8|9][0-9]{7}$")) {
                    errMap.put("mobileNo1", "GENERAL_ERR0007");
                }
            }
            String generalSixSal = MessageUtil.replaceMessage("GENERAL_ERR0006", "Salutation", "field");
            if (StringUtil.isEmpty(salutation1)) {
                errMap.put("salutation1", generalSixSal);
            }
            if (StringUtil.isEmpty(psnName1)) {
                errMap.put("psnName1", MessageUtil.replaceMessage("GENERAL_ERR0006", "Name", "field"));
            }
            if (StringUtil.isEmpty(idType1)) {
                errMap.put("idType1", MessageUtil.replaceMessage("GENERAL_ERR0006", "ID Type", "field"));
            }
            if (StringUtil.isEmpty(idNo1)) {
                errMap.put("idNo1", MessageUtil.replaceMessage("GENERAL_ERR0006", "ID No.", "field"));
            } else {
                if (OrganizationConstants.ID_TYPE_FIN.equals(idType1)) {
                    boolean b = SgNoValidator.validateFin(idNo1);
                    if (!b) {
                        errMap.put("idNo1", "RFC_ERR0012");
                    }
                }
                if (OrganizationConstants.ID_TYPE_NRIC.equals(idType1)) {
                    boolean b1 = SgNoValidator.validateNric(idNo1);
                    if (!b1) {
                        errMap.put("idNo1", "RFC_ERR0012");
                    }
                }
            }
            if ((psnTypes.contains("CGO")||psnTypes.contains("CD")) && StringUtil.isEmpty(designation1)) {
                errMap.put("designation1", designationMsg);
            }else if((psnTypes.contains("CGO")||psnTypes.contains("CD")) &&"DES999".equals(designation1)){
                if(StringUtil.isEmpty(otherDesignation1)){
                    errMap.put("otherDesignation1" , designationMsg);
                }
            }
            if ((psnTypes.contains("PO") || psnTypes.contains("DPO")) && StringUtil.isEmpty(designation1)) {
                errMap.put("designation1", designationMsg);
            }else if((psnTypes.contains("PO") || psnTypes.contains("DPO")) && "DES999".equals(designation1)){
                if(StringUtil.isEmpty(otherDesignation1)){
                    errMap.put("otherDesignation1" , designationMsg);
                }
            }
            if ((psnTypes.contains("PO") || psnTypes.contains("DPO")) && StringUtil.isEmpty(officeTelNo1)) {
                errMap.put("officeTelNo1", MessageUtil.replaceMessage("GENERAL_ERR0006", "Office Telephone No.", "field"));
            }
            if ((psnTypes.contains("PO") || psnTypes.contains("DPO")) && !StringUtil.isEmpty(officeTelNo1) && !officeTelNo1.matches(IaisEGPConstant.OFFICE_TELNO_MATCH)) {
                errMap.put("officeTelNo1", "GENERAL_ERR0015");
            }
        }
        if ("update".equals(editSelect)) {
            if (StringUtil.isEmpty(salutation)) {
                errMap.put("salutation", MessageUtil.replaceMessage("GENERAL_ERR0006", "Salutation", "field"));
            }
            if (StringUtil.isEmpty(psnName)) {
                errMap.put("psnName", MessageUtil.replaceMessage("GENERAL_ERR0006", "Name", "field"));
            }
            if (StringUtil.isEmpty(email) && !onlyKAH) {
                errMap.put("emailAddr", emailMsg);
            } else if (!StringUtil.isEmpty(email)) {
                if (!ValidationUtils.isEmail(email)) {
                    errMap.put("emailAddr", "GENERAL_ERR0014");
                }
            }
            if (StringUtil.isEmpty(mobile) && !onlyKAH) {
                errMap.put("mobileNo", MessageUtil.replaceMessage("GENERAL_ERR0006", "Mobile No.", "field"));
            } else if (!StringUtil.isEmpty(mobile)) {
                if (!mobile.matches("^[8|9][0-9]{7}$")) {
                    errMap.put("mobileNo", "GENERAL_ERR0007");
                }
            }
            if ((psnTypes.contains("CGO")||psnTypes.contains("CD")) && StringUtil.isEmpty(designation)) {
                errMap.put("designation", designationMsg);
            }else if((psnTypes.contains("CGO") || psnTypes.contains("CD"))&&"DES999".equals(designation)){
                if(StringUtil.isEmpty(otherDesignation)){
                    errMap.put("otherDesignation" , designationMsg);
                }
            }
            if ((psnTypes.contains("PO") || psnTypes.contains("DPO")) && StringUtil.isEmpty(designation)) {
                errMap.put("designation", designationMsg);
            }else if((psnTypes.contains("PO") || psnTypes.contains("DPO")) && "DES999".equals(designation)){
                if(StringUtil.isEmpty(otherDesignation)){
                    errMap.put("otherDesignation" , designationMsg);
                }
            }
            if ((psnTypes.contains("PO") || psnTypes.contains("DPO")) && StringUtil.isEmpty(officeTelNo)) {
                errMap.put("officeTelNo", MessageUtil.replaceMessage("GENERAL_ERR0006", "Office Telephone No.", "field"));
            }
            if ((psnTypes.contains("PO") || psnTypes.contains("DPO")) && !StringUtil.isEmpty(officeTelNo) && !officeTelNo.matches(IaisEGPConstant.OFFICE_TELNO_MATCH)) {
                errMap.put("officeTelNo", "GENERAL_ERR0015");
            }
        }
        if ("replace".equals(editSelect) && !"new".equals(replaceName)) {
            if (StringUtil.isEmpty(replaceName)) {
                errMap.put("replaceName", MessageUtil.replaceMessage("GENERAL_ERR0006", "Name", "field"));
            } else {
                String[] split = replaceName.split(",");
                String idType = split[0];
                String idNo = split[1];
                String psnKey = idType + "," + idNo;
                Map<String, AppSvcPrincipalOfficersDto> psnMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.PERSONSELECTMAP);
                AppSvcPrincipalOfficersDto psn = psnMap.get(psnKey);

                newPerson.setIdNo(psn.getIdNo());
                newPerson.setIdType(psn.getIdType());
                newPerson.setPsnName(psn.getName());
                newPerson.setSalutation(psn.getSalutation());
                newPerson.setDesignation(psn.getDesignation());
                newPerson.setOtherDesignation(psn.getOtherDesignation());
                newPerson.setEmailAddr(psn.getEmailAddr());
                newPerson.setMobileNo(psn.getMobileNo());
                newPerson.setOfficeTelNo(psn.getOfficeTelNo());
                newPerson.setLicPsnTypeDtoMaps(licPsnTypeDtoMaps);

                if (StringUtil.isEmpty(newPerson.getEmailAddr()) && !onlyKAH) {
                    errMap.put("emailAddr2", emailMsg);
                } else if (!StringUtil.isEmpty(newPerson.getEmailAddr())) {
                    if (!ValidationUtils.isEmail(newPerson.getEmailAddr())) {
                        errMap.put("emailAddr2", "GENERAL_ERR0014");
                    }
                }
                if (StringUtil.isEmpty(newPerson.getMobileNo()) && !onlyKAH) {
                    errMap.put("mobileNo2", MessageUtil.replaceMessage("GENERAL_ERR0006", "Mobile No.", "field"));
                } else if (!StringUtil.isEmpty(newPerson.getMobileNo())) {
                    if (!newPerson.getMobileNo().matches("^[8|9][0-9]{7}$")) {
                        errMap.put("mobileNo2", "GENERAL_ERR0007");
                    }
                }
                if ((psnTypes.contains("CGO") && StringUtil.isEmpty(newPerson.getSalutation()))
                        || ((psnTypes.contains("PO") || psnTypes.contains("DPO")) && StringUtil.isEmpty(newPerson.getSalutation()))) {
                    errMap.put("salutation2", MessageUtil.replaceMessage("GENERAL_ERR0006", "Salutation", "field"));
                }
                if (StringUtil.isEmpty(newPerson.getPsnName())) {
                    errMap.put("psnName2", MessageUtil.replaceMessage("GENERAL_ERR0006", "Name", "field"));
                }
                if (StringUtil.isEmpty(newPerson.getIdType())) {
                    errMap.put("idType2", MessageUtil.replaceMessage("GENERAL_ERR0006", "ID Type", "field"));
                }
                if (StringUtil.isEmpty(newPerson.getIdNo())) {
                    errMap.put("idNo2", MessageUtil.replaceMessage("GENERAL_ERR0006", "ID No.", "field"));
                }
                if (psnTypes.contains("CGO") && StringUtil.isEmpty(newPerson.getDesignation())) {
                    errMap.put("designation2", designationMsg);
                }else if(psnTypes.contains("CGO") &&  "DES999".equals(newPerson.getDesignation())){
                    if(StringUtil.isEmpty(newPerson.getOtherDesignation())){
                        errMap.put("otherDesignation2", designationMsg);
                    }
                }
                if ((psnTypes.contains("PO") || psnTypes.contains("DPO")) && StringUtil.isEmpty(newPerson.getDesignation())) {
                    errMap.put("designation2", designationMsg);
                }else if((psnTypes.contains("PO") || psnTypes.contains("DPO")) && "DES999".equals(newPerson.getDesignation())){
                   if(StringUtil.isEmpty(newPerson.getOtherDesignation())){
                       errMap.put("otherDesignation2", designationMsg);
                   }
                }
                String generalSixTelNo = MessageUtil.replaceMessage("GENERAL_ERR0006", "Office Telephone No", "field");
                if ((psnTypes.contains("PO") || psnTypes.contains("DPO")) && StringUtil.isEmpty(newPerson.getOfficeTelNo())) {
                    errMap.put("officeTelNo2", generalSixTelNo);
                }
                if ((psnTypes.contains("PO") || psnTypes.contains("DPO")) && !StringUtil.isEmpty(newPerson.getOfficeTelNo()) && !newPerson.getOfficeTelNo().matches(IaisEGPConstant.OFFICE_TELNO_MATCH)) {
                    errMap.put("officeTelNo", generalSixTelNo);
                }
            }
        }
        if (StringUtil.isEmpty(editSelect)) {
            errMap.put("editSelect", MessageUtil.replaceMessage("GENERAL_ERR0006", "Edit", "field"));
        }
        //rfc valiant

        if (!errMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, "action_type", "valid");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setSessionAttr(bpc.request, "personnelEditDto", personnelEditDto);
            ParamUtil.setSessionAttr(bpc.request, "newPerson", newPerson);
            if (StringUtil.isEmpty(replaceName)) {
                ParamUtil.setSessionAttr(bpc.request, "replaceName", null);
            }
            ParamUtil.setRequestAttr(bpc.request, "replaceName", replaceName);
        }
        return errMap;
    }

    private PersonnelListDto transferDto(PersonnelListDto personnelListDto) {
        if (personnelListDto == null) {
            return null;
        }
        PersonnelListDto oldDto = new PersonnelListDto();
        oldDto.setIdNo(personnelListDto.getIdNo());
        oldDto.setIdType(personnelListDto.getIdType());
        oldDto.setPsnName(personnelListDto.getPsnName());
        oldDto.setSalutation(personnelListDto.getSalutation());
        oldDto.setDesignation(personnelListDto.getDesignation());
        oldDto.setOtherDesignation(personnelListDto.getOtherDesignation());
        oldDto.setOfficeTelNo(personnelListDto.getOfficeTelNo());
        oldDto.setMobileNo(personnelListDto.getMobileNo());
        oldDto.setEmailAddr(personnelListDto.getEmailAddr());
        return oldDto;
    }

    private List<SelectOption> genSpecialtySelectList() {
        List<SelectOption> specialtySelectList = IaisCommonUtils.genNewArrayList();
        SelectOption ssl2 = new SelectOption("Pathology", "Pathology");
        SelectOption ssl3 = new SelectOption("Haematology", "Haematology");
        SelectOption ssl4 = new SelectOption("other", "Others");
        specialtySelectList.add(ssl2);
        specialtySelectList.add(ssl3);
        specialtySelectList.add(ssl4);
        return specialtySelectList;
    }

    private List<SelectOption> genReplacePersonnel(String licenseeId) {
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        SelectOption s1 = new SelectOption("new", "I'd like to add a new personnel");
        selectOptions.add(s1);
        List<PersonnelListQueryDto> persons = requestForChangeService.getLicencePersonnelListQueryDto(licenseeId);
        if (!IaisCommonUtils.isEmpty(persons)) {
            List<String> idNos = IaisCommonUtils.genNewArrayList();
            for (PersonnelListQueryDto dto : persons) {
                String idNo = dto.getIdNo();
                if (!idNos.contains(idNo)) {
                    idNos.add(idNo);
                    String name = dto.getName();
                    String idType = dto.getIdType();
                    SelectOption s = new SelectOption(idType + "," + idNo, name + ", " + idNo + " (" + MasterCodeUtil.getCodeDesc(idType) + ")");
                    selectOptions.add(s);
                }
            }
        }
        return selectOptions;
    }

    private void setLicseeAndPsnDropDown(BaseProcessClass bpc) {
        //set licenseeId
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        if (loginContext != null) {
            List<PersonnelListQueryDto> licPersonList = requestForChangeService.getLicencePersonnelListQueryDto(loginContext.getLicenseeId());
            //exchange order
            Map<String, AppSvcPrincipalOfficersDto> licPersonMap = NewApplicationHelper.getLicPsnIntoSelMap(bpc.request, licPersonList);
            ParamUtil.setSessionAttr(bpc.request, "LicPersonSelectMap", (Serializable) licPersonMap);
            Map<String, AppSvcPrincipalOfficersDto> personMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(bpc.request, "PersonSelectMap");
            if (personMap != null) {
                licPersonMap.forEach((k, v) -> {
                    personMap.put(k, v);
                });
                ParamUtil.setSessionAttr(bpc.request, "PersonSelectMap", (Serializable) personMap);
            } else {
                ParamUtil.setSessionAttr(bpc.request, "PersonSelectMap", (Serializable) licPersonMap);
            }
        } else {
            log.info(StringUtil.changeForLog("user info is empty....."));
        }
    }

    private List<SelectOption> getPsnType() {
        List<SelectOption> personelRoles = IaisCommonUtils.genNewArrayList();
        personelRoles.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, HcsaConsts.CLINICAL_GOVERNANCE_OFFICER));
        personelRoles.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_PO, HcsaConsts.PRINCIPAL_OFFICER));
        personelRoles.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, HcsaConsts.NOMINEE));
        personelRoles.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP, HcsaConsts.MEDALERT_PERSON));
        personelRoles.add(new SelectOption(ApplicationConsts.PERSONNEL_PSN_KAH, HcsaConsts.KEY_APPOINTMENT_HOLDER));
        Collections.sort(personelRoles);
        return personelRoles;
    }

    public void doPersonnelList(BaseProcessClass bpc) {
        setLicseeAndPsnDropDown(bpc);
        log.debug(StringUtil.changeForLog("the do doPersonnelList start ...."));
        String actionType = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        log.debug(StringUtil.changeForLog("the do doPersonnelList end ...."));
    }

    public void personnleSearch(BaseProcessClass bpc) {
        log.info("search================????>>>>>");
        String personName = ParamUtil.getString(bpc.request, "personName");
        String psnType = ParamUtil.getString(bpc.request, "psnTypes");
        SearchParam searchParam = SearchResultHelper.getSearchParam(bpc.request, filterParameter, true);
        if (!StringUtil.isEmpty(personName)) {
            searchParam.addFilter("personName",  personName , true);
            QueryHelp.setMainSql("applicationPersonnelQuery", "appPersonnelQuery", searchParam);
            ParamUtil.setRequestAttr(bpc.request, "personName", personName);
            ParamUtil.setRequestAttr(bpc.request, "perName", personName);
        } else {
            ParamUtil.setRequestAttr(bpc.request, "perName", null);
            ParamUtil.setRequestAttr(bpc.request, "personName", null);
        }
        if (!StringUtil.isEmpty(psnType)) {
            ParamUtil.setRequestAttr(bpc.request, "psnType", psnType);
        }
    }

    public void personnleSorting(BaseProcessClass bpc) {
        SearchResultHelper.doSort(bpc.request, filterParameter);
    }

    public void personnlePaging(BaseProcessClass bpc) {
        SearchResultHelper.doPage(bpc.request, filterParameter);
    }

    public void preparePersonnelBank(BaseProcessClass bpc) throws IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do prePayment start ...."));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        String emailAddr = WithOutRenewalDelegator.emailAddressesToString(licenseeEmailAddrs);
        ParamUtil.setSessionAttr(bpc.request, "emailAddress", emailAddr);
        ParamUtil.setSessionAttr(bpc.request, "pmtRefNo", "N/A");
        ParamUtil.setSessionAttr(bpc.request, "txnRefNo", "N/A");
        ParamUtil.setSessionAttr(bpc.request, "createDate", new Date());
        ParamUtil.setSessionAttr(bpc.request, "dAmount", "$0");
        ParamUtil.setSessionAttr(bpc.request, "payMethod", "N/A");
        ParamUtil.setSessionAttr(bpc.request, RfcConst.SWITCH_VALUE, "ack");
        log.debug(StringUtil.changeForLog("the do prePayment end ...."));
    }

    public void personnelDashboard(BaseProcessClass bpc) {
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        try {
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            bpc.request.getSession().setAttribute("appSubmissionDtos", null);
            bpc.request.getSession().setAttribute("appSubmissionDto", null);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void updateGroupStatus(List<AppSubmissionDto> appSubmissionDtos) {
        Set<String> appGrpSet = IaisCommonUtils.genNewHashSet();
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
            Double amount = appSubmissionDto.getAmount();
            if (MiscUtil.doubleEquals(amount, 0.0)) {
                String appGrpNo = appSubmissionDto.getAppGrpNo();
                if (appGrpSet.contains(appGrpNo)) {
                    continue;
                }
                List<ApplicationDto> entity = applicationFeClient.getApplicationsByGroupNo(appGrpNo).getEntity();
                if (entity != null && !entity.isEmpty()) {
                    for (ApplicationDto applicationDto : entity) {
                        applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
                    }
                    applicationFeClient.saveApplicationDtos(entity);
                    String grpId = entity.get(0).getAppGrpId();
                    ApplicationGroupDto applicationGroupDto = applicationFeClient.getApplicationGroup(grpId).getEntity();
                    applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
                    applicationFeClient.updateAppGrpPmtStatus(applicationGroupDto);
                }
                appGrpSet.add(appGrpNo);
            }
        }
        log.info(StringUtil.changeForLog("the 0.0 amount app group numbers: " + appGrpSet));
    }

    /**
     * @param bpc
     * @Decription prepareAckPage
     */
    public void prepareAckPage(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareAckPage start ...."));
        Date createDate = (Date) ParamUtil.getSessionAttr(bpc.request, "createDate");
        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "appSubmissionDtos");
        if (appSubmissionDtos != null) {
            updateGroupStatus(appSubmissionDtos);
        }
        if (createDate == null) {
            ParamUtil.setSessionAttr(bpc.request, "createDate", new Date());
        }
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(bpc.request, "INTER_INBOX_USER_INFO");
        String licenseeId = null;
        if (interInboxUserDto != null) {
            licenseeId = interInboxUserDto.getLicenseeId();
        } else {
            log.debug(StringUtil.changeForLog("interInboxUserDto null"));
        }
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        String emailAddress = WithOutRenewalDelegator.emailAddressesToString(licenseeEmailAddrs);
        ParamUtil.setSessionAttr(bpc.request, "emailAddress", emailAddress);
        log.debug(StringUtil.changeForLog("the do prepareAckPage end ...."));
    }

    public void selectLicence(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do selectLicence start ...."));
        PremisesListQueryDto premisesListQueryDto = (PremisesListQueryDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.PREMISESLISTQUERYDTO);
        String premisesId = premisesListQueryDto.getPremisesId();
        List<LicenceDto> licenceDtoList = requestForChangeService.getLicenceDtoByPremisesId(premisesId);
        bpc.request.setAttribute("licenceDtoList", licenceDtoList);
        log.debug(StringUtil.changeForLog("the do selectLicence end ...."));
        InterInboxUserDto interInboxUserDto = (InterInboxUserDto) ParamUtil.getSessionAttr(bpc.request, "INTER_INBOX_USER_INFO");
        String licenseeId = null;
        if (interInboxUserDto != null) {
            licenseeId = interInboxUserDto.getLicenseeId();
        } else {
            log.debug(StringUtil.changeForLog("interInboxUserDto null"));
        }
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        String emailAddress = WithOutRenewalDelegator.emailAddressesToString(licenseeEmailAddrs);
        ParamUtil.setSessionAttr(bpc.request, "emailAddress", emailAddress);
    }


    /**
     * @param bpc
     * @Decription prePayment
     */
    public void prePayment(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prePayment start ...."));
        Double dAmount = 0.0;
        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "appSubmissionDtos");
        for (AppSubmissionDto every : appSubmissionDtos) {
            if (!StringUtil.isEmpty(every.getAmount())) {
                DecimalFormat decimalFormat = new DecimalFormat("###,###");
                Double amount = every.getAmount();
                String amountStr = "$" + decimalFormat.format(amount);
                dAmount = dAmount + amount;
                every.setAmountStr(amountStr);
            }
        }
        bpc.request.getSession().setAttribute("dAmount", Formatter.formatterMoney(dAmount));
//        boolean isGiroAcc = appSubmissionService.isGiroAccount(NewApplicationHelper.getLicenseeId(appSubmissionDtos));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String orgId = "";
        if (loginContext != null) {
            orgId = loginContext.getOrgId();
        }
        AppSubmissionDto mainSubmisDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);
        if(mainSubmisDto != null){
            boolean isGiroAcc = false;
            List<SelectOption> giroAccSel = NewApplicationHelper.getGiroAccOptions(appSubmissionDtos, mainSubmisDto);
            if (!IaisCommonUtils.isEmpty(giroAccSel)) {
                isGiroAcc = true;
                ParamUtil.setRequestAttr(bpc.request, "giroAccSel", giroAccSel);
            }
            ParamUtil.setRequestAttr(bpc.request, "IsGiroAcc", isGiroAcc);
            ParamUtil.setRequestAttr(bpc.request,NewApplicationConstant.ATTR_RELOAD_PAYMENT_METHOD,mainSubmisDto.getPaymentMethod());
        }
        log.debug(StringUtil.changeForLog("the do prePayment end ...."));
    }

    /**
     * @param bpc
     * @Decription jumpBank
     * @Decription jumpBank
     */
    public void jumpBank(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do jumpBank start ...."));
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        String noNeedPayment = bpc.request.getParameter("noNeedPayment");
        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "appSubmissionDtos");

        if (StringUtil.isEmpty(payMethod) && StringUtil.isEmpty(noNeedPayment)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, "prePayment");
            return;
        }
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
            appSubmissionDto.setPaymentMethod(payMethod);
        }
        bpc.request.getSession().setAttribute("payMethod", payMethod);
        if (MiscUtil.doubleEquals(appSubmissionDtos.get(0).getAmount(), 0.0)) {
            StringBuilder url = new StringBuilder();
            url.append("https://")
                    .append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohRfcPermisesList/1/prepareAckPage");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
            return;
        }
        double a = 0.0;
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
            a = a + appSubmissionDto.getAmount();
        }
        bpc.request.getSession().setAttribute("appSubmissionDtos", appSubmissionDtos);
        if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)) {
            Map<String, String> fieldMap = new HashMap<String, String>();
            fieldMap.put(GatewayConstants.AMOUNT_KEY, String.valueOf(a));
            fieldMap.put(GatewayConstants.PYMT_DESCRIPTION_KEY, payMethod);
            fieldMap.put(GatewayConstants.SVCREF_NO, appSubmissionDtos.get(0).getAppGrpNo() + "_" + System.currentTimeMillis());
            try {
                String url = "/hcsa-licence-web/eservice/INTERNET/MohRfcPermisesList/1/doPayment";
                String html = "";
                switch (payMethod) {
                    case ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT:
                        html = GatewayStripeAPI.create_partner_trade_by_buyer_url(fieldMap, bpc.request, url);
                        break;
                    case ApplicationConsts.PAYMENT_METHOD_NAME_NETS:
                        html = GatewayNetsAPI.create_partner_trade_by_buyer_url(fieldMap, bpc.request, url);
                        break;
                    case ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW:
                        html = GatewayPayNowAPI.create_partner_trade_by_buyer_url(fieldMap, bpc.request, url);
                        break;
                    default:
                        html = GatewayAPI.create_partner_trade_by_buyer_url(fieldMap, bpc.request, url);
                        break;
                }
                IaisEGPHelper.redirectUrl(bpc.response, html);
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
            return;
        } else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)) {
            if(appSubmissionDtos.size() > 1){
                appSubmissionDtos.get(0).setTotalAmountGroup(a);
            }
            try {
                if ( appSubmissionDtos.get(0).getAppType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)) {
                    requestForChangeService.sendRfcSubmittedEmail(appSubmissionDtos, appSubmissionDtos.get(0).getPaymentMethod());
                }
            } catch (Exception e) {
                log.info(e.getMessage(), e);
            }
            String giroAccNum = "";
            if(!StringUtil.isEmpty(payMethod) && ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)){
                giroAccNum = ParamUtil.getString(bpc.request, "giroAccount");
            }
            appSubmissionDtos.get(0).setGiroAcctNum(giroAccNum);
            String appGrpId = appSubmissionDtos.get(0).getAppGrpId();
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(serviceConfigService.giroPaymentXmlUpdateByGrpNo(appSubmissionDtos.get(0)).getPmtStatus());
            String giroTranNo = appSubmissionDtos.get(0).getGiroTranNo();
            appGrp.setPmtRefNo(giroTranNo);
            appGrp.setPayMethod(payMethod);
            serviceConfigService.updateAppGrpPmtStatus(appGrp, giroAccNum);
            serviceConfigService.updatePaymentStatus(appGrp);
//            serviceConfigService.updatePaymentStatus(appGrp);
            ParamUtil.setRequestAttr(bpc.request, "PmtStatus", ApplicationConsts.PAYMENT_METHOD_NAME_GIRO);
            ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "ack");
            ParamUtil.setSessionAttr(bpc.request, "txnRefNo", giroTranNo);
            //todo change
            StringBuilder url = new StringBuilder();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/hcsa-licence-web/eservice/INTERNET/MohRfcPermisesList/1/prepareAckPage");
            String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        }

        log.debug(StringUtil.changeForLog("the do jumpBank end ...."));
    }

    /**/

    /**
     * @param bpc
     * @Decription toBank
     */
    public void toBank(BaseProcessClass bpc) {

    }

    public void jumpYeMian(HttpServletRequest request, HttpServletResponse response) throws IOException {
        StringBuilder url = new StringBuilder(10);
        url.append("https://").append(request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), request);
        IaisEGPHelper.redirectUrl(response, tokenUrl);
    }
    /**/

    /**
     * @param bpc
     * @Decription doPayment
     */
    public void doPayment(BaseProcessClass bpc) throws IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the do doPayment start ...."));
        String switchValue = "loading";
        /*String pmtStatus = ParamUtil.getString(bpc.request, "result");
        if (StringUtil.isEmpty(pmtStatus)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, "prePayment");
            return;
        }*/
        String result = ParamUtil.getMaskedString(bpc.request, "result");
        //result="failed";
        if (!StringUtil.isEmpty(result)) {
            log.debug(StringUtil.changeForLog("payment result:" + result));
            String pmtRefNo = ParamUtil.getMaskedString(bpc.request, "txnRefNo");
            if ("success".equals(result) && !StringUtil.isEmpty(pmtRefNo)) {
                log.info("credit card payment success");
                switchValue = "ack";
                //update status
                List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "appSubmissionDtos");
                String pmtMethod = "";
                String grpId = "";
                if (appSubmissionDtos != null && appSubmissionDtos.size() > 0) {
                    if (appSubmissionDtos.get(0) != null) {
                        pmtMethod = appSubmissionDtos.get(0).getPaymentMethod();
                        grpId = appSubmissionDtos.get(0).getAppGrpId();
                    }
                }
                ApplicationGroupDto appGrp = new ApplicationGroupDto();
                appGrp.setId(grpId);
                appGrp.setPmtRefNo(pmtRefNo);
                appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                appGrp.setPayMethod(pmtMethod);
                serviceConfigService.updatePaymentStatus(appGrp);
                ParamUtil.setSessionAttr(bpc.request, "createDate", new Date());
                try {
                    if (appSubmissionDtos != null && appSubmissionDtos.get(0).getAppType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)) {
                        requestForChangeService.sendRfcSubmittedEmail(appSubmissionDtos, appSubmissionDtos.get(0).getPaymentMethod());
                    }
                } catch (Exception e) {
                    log.info(e.getMessage(), e);
                }
            } else {
                if(!"cancelled".equals(result)){
                    Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                    errorMap.put("pay",MessageUtil.getMessageDesc("NEW_ERR0024"));
                    ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                }
                switchValue = "loading";
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePayment");
            }

            bpc.request.setAttribute("pmtRefNo", pmtRefNo);
        }else{
            switchValue = "loading";
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePayment");
        }
        String txnDt = ParamUtil.getMaskedString(bpc.request, "txnDt");
        String txnRefNo = ParamUtil.getMaskedString(bpc.request, "txnRefNo");
        ParamUtil.setSessionAttr(bpc.request, "txnDt", txnDt);
        ParamUtil.setSessionAttr(bpc.request, "txnRefNo", txnRefNo);
        ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, switchValue);
        log.debug(StringUtil.changeForLog("the do doPayment end ...."));
    }

    public void dashboard(BaseProcessClass bpc) {
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil.appendCsrfGuardToken(url.toString(), bpc.request);
        try {
            IaisEGPHelper.redirectUrl(bpc.response, tokenUrl);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void doSubmit(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the do doSubmit start ...."));
        ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "loading");
        List<LicenceDto> selectLicence = (List<LicenceDto>) bpc.request.getSession().getAttribute("licenceDtoList");
        AppSubmissionDto oldAppSubmissionDtoappSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "oldAppSubmissionDto");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);
        PremisesListQueryDto premisesListQueryDto = (PremisesListQueryDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.PREMISESLISTQUERYDTO);
        List<AppGrpPremisesDto> appGrpPremisesDtoList1 = appSubmissionDto.getAppGrpPremisesDtoList();
        for(AppGrpPremisesDto v : appGrpPremisesDtoList1){
            v.setLicenceDtos(selectLicence);
        }
        boolean eqHciCode = EqRequestForChangeSubmitResultChange.eqHciCode(appSubmissionDto.getAppGrpPremisesDtoList().get(0), oldAppSubmissionDtoappSubmissionDto.getAppGrpPremisesDtoList().get(0));
        bpc.request.setAttribute("eqHciCode",String.valueOf(eqHciCode));
        List<AppGrpPremisesDto> oldAppSubmissionDtoappSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDtoappSubmissionDto.getAppGrpPremisesDtoList();
        if (selectLicence != null) {
            for (LicenceDto string : selectLicence) {
                HcsaServiceDto activeHcsaServiceDtoByName = serviceConfigService.getActiveHcsaServiceDtoByName(string.getSvcName());
                if (activeHcsaServiceDtoByName != null) {
                    List<String> serviceIds = IaisCommonUtils.genNewArrayList();
                    serviceIds.add(activeHcsaServiceDtoByName.getId());
                    for (AppGrpPremisesDto appGrpPremisesDto : oldAppSubmissionDtoappSubmissionDtoAppGrpPremisesDtoList) {
                        boolean configIsChange = requestForChangeService.serviceConfigIsChange(serviceIds, appGrpPremisesDto.getPremisesType());
                        if (!configIsChange) {

                            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePremisesEdit");
                            bpc.request.setAttribute("SERVICE_CONFIG_CHANGE", MessageUtil.replaceMessage("RFC_ERR020", string.getSvcName(), "ServiceName"));
                            return;
                        }
                    }
                }
                List<ApplicationDto> applicationDtos = requestForChangeService.getAppByLicIdAndExcludeNew(string.getId());
                if (!IaisCommonUtils.isEmpty(applicationDtos)) {
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePremisesEdit");
                    bpc.request.setAttribute("rfcPendingApplication", "errorRfcPendingApplication");
                    return;
                }
                boolean b = requestForChangeService.baseSpecLicenceRelation(string);
                if(!b){
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePremisesEdit");
                    bpc.request.setAttribute("rfcPendingApplication", "errorRfcPendingApplication");
                    return;
                }
            }
        }

        boolean eqGrpPremises = EqRequestForChangeSubmitResultChange.isChangeGrpPremises(appGrpPremisesDtoList1,
                oldAppSubmissionDtoappSubmissionDtoAppGrpPremisesDtoList);
        if (!eqGrpPremises) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePremisesEdit");
            bpc.request.setAttribute("RFC_ERROR_NO_CHANGE", "RFC_ERR015");
            return;
        }
        String licenceId = appSubmissionDto.getLicenceId();
        if (!StringUtil.isEmpty(licenceId)) {
            List<ApplicationDto> applicationDtos = requestForChangeService.getAppByLicIdAndExcludeNew(licenceId);
            if (!IaisCommonUtils.isEmpty(applicationDtos)) {
                ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "ack");
                ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "There is ongoing application for the licence");
                return;
            }
        }
        List<AppSubmissionDto> appSubmissionDtos = IaisCommonUtils.genNewArrayList();
        String appType = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE;
        if (!StringUtil.isEmpty(appSubmissionDto.getAppType())) {
            appType = appSubmissionDto.getAppType();
        }
        String appGroupNo = requestForChangeService.getApplicationGroupNumber(appType);
        appSubmissionDto.setAppGrpNo(appGroupNo);

        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setPremisesListEdit(true);
        appEditSelectDto.setPremisesEdit(true);

        //amount
        boolean eqHciNameChange = !compareHciName(premisesListQueryDto, appSubmissionDto.getAppGrpPremisesDtoList().get(0));
        boolean changeInLocation = compareLocation(premisesListQueryDto, appSubmissionDto.getAppGrpPremisesDtoList().get(0));
        boolean eqAddFloorNo = EqRequestForChangeSubmitResultChange.isChangeFloorUnit(appSubmissionDto,
                oldAppSubmissionDtoappSubmissionDto);
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        amendmentFeeDto.setChangeInLicensee(Boolean.FALSE);
        amendmentFeeDto.setChangeInHCIName(eqHciNameChange);
        amendmentFeeDto.setChangeInLocation(changeInLocation || eqAddFloorNo);
        // check app edit select dto
        appEditSelectDto.setChangeHciName(eqHciNameChange);
        appEditSelectDto.setChangeInLocation(changeInLocation);
        appEditSelectDto.setChangeAddFloorUnit(eqAddFloorNo);
        log.info(StringUtil.changeForLog("The App Edit Select Dto - " + JsonUtil.parseToJson(appEditSelectDto)));
        NewApplicationHelper.reSetAdditionalFields(appSubmissionDto, appEditSelectDto);

        boolean isCharity = NewApplicationHelper.isCharity(bpc.request);
        amendmentFeeDto.setIsCharity(isCharity);
        FeeDto feeDto = appSubmissionService.getGroupAmendAmount(amendmentFeeDto);
        Double total = feeDto.getTotal();
        if (total == null) {
            total = 0.0;
        }
        if (selectLicence != null) {
            boolean isValid = requestForChangeService.checkAffectedAppSubmissions(selectLicence, appGrpPremisesDtoList1.get(0), null,
                    total, null, appGroupNo, appEditSelectDto, appSubmissionDtos, bpc.request);
            log.info(StringUtil.changeForLog("The affected data is valid - " + isValid));
        }
        // validate the related app submissions
        Map<AppSubmissionDto, List<String>> errorListMap = IaisCommonUtils.genNewHashMap();
        for (AppSubmissionDto dto : appSubmissionDtos) {
            List<String> errorList = appSubmissionService.doPreviewSubmitValidate(null, dto, false);
            if (!errorList.isEmpty()) {
                errorListMap.put(dto, errorList);
            }
        }
        if (!errorListMap.isEmpty()) {
            bpc.request.setAttribute(NewApplicationConstant.SHOW_OTHER_ERROR, NewApplicationHelper.getErrorMsg(errorListMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePremisesEdit");
            return;
        }
        String submissionId = generateIdClient.getSeqId().getEntity();
        AppSubmissionListDto appSubmissionListDto = new AppSubmissionListDto();
        Long l = System.currentTimeMillis();
        appSubmissionListDto.setEventRefNo(l.toString());
        AuditTrailDto currentAuditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        for (AppSubmissionDto appSubmissionDto1 : appSubmissionDtos) {
            appSubmissionDto1.setAuditTrailDto(currentAuditTrailDto);
        }
        List<AppSubmissionDto> appSubmissionDtos1 = requestForChangeService.saveAppsForRequestForGoupAndAppChangeByList(appSubmissionDtos);
        appSubmissionListDto.setAppSubmissionDtos(appSubmissionDtos1);
        eventBusHelper.submitAsyncRequest(appSubmissionListDto, submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_REQUEST_INFORMATION_SUBMIT, l.toString(), bpc.process);
        ParamUtil.setSessionAttr(bpc.request, "appSubmissionDtos", (Serializable) appSubmissionDtos1);
        log.debug(StringUtil.changeForLog("the do doSubmit end ...."));
    }

    public static void oldPremiseToNewPremise(AppSubmissionDto appSubmissionDto) {
        if (appSubmissionDto != null) {
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
            if (appGrpPremisesDtoList != null) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                    String premisesIndexNo = appGrpPremisesDto.getPremisesIndexNo();
                    if (appSvcLaboratoryDisciplinesDtoList != null) {
                        for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvcLaboratoryDisciplinesDtoList) {
                            String premiseVal = appSvcLaboratoryDisciplinesDto.getPremiseVal();
                            if (!premisesIndexNo.equals(premiseVal)) {
                                appSvcLaboratoryDisciplinesDto.setPremiseVal(premisesIndexNo);
                            }
                        }

                    }
                }
            }
        }

    }

    private boolean compareHciName(List<AppGrpPremisesDto> appGrpPremisesDtos, List<AppGrpPremisesDto> oldAppGrpPremisesDtos) {
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if (length == oldLength) {
            for (int i = 0; i < length; i++) {
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if (!getHciName(appGrpPremisesDto).equals(getHciName(oldAppGrpPremisesDto))) {
                    return false;
                }
            }
        }
        //is same
        return true;
    }

    private String getHciName(AppGrpPremisesDto appGrpPremisesDto) {
        String hciName = "";
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())) {
            hciName = appGrpPremisesDto.getHciName();
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())) {
            hciName = appGrpPremisesDto.getConveyanceVehicleNo();
        }
        return hciName;
    }

    /**
     * @param bpc
     * @Decription doRequestForInformationSubmit
     *//**/
    public void doRequestForInformationSubmit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doRequestForInformationSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "AppSubmissionDto");
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.OLDAPPSUBMISSIONDTO);
        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setServiceEdit(false);
        appEditSelectDto.setDocEdit(false);
        appEditSelectDto.setPoEdit(false);
        appEditSelectDto.setPremisesListEdit(true);
        appEditSelectDto.setPremisesEdit(true);
        String appGrpNo = appSubmissionDto.getAppGrpNo();
        //oldAppSubmissionDtos
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        oldAppSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppSubmissionRequestInformationDto appSubmissionRequestInformationDto = new AppSubmissionRequestInformationDto();
        appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        appSubmissionRequestInformationDto.setAppSubmissionDto(appSubmissionDto);
        appSubmissionRequestInformationDto.setOldAppSubmissionDto(oldAppSubmissionDto);
       /* appSubmissionDto = appSubmissionService.submitRequestInformation(appSubmissionRequestInformationDto, bpc.process);*/
        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, appSubmissionDto);

        ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "Y");
        ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "The request for information save success");

        log.debug(StringUtil.changeForLog("the do doRequestForInformationSubmit end ...."));
    }

    public void doPayValidate(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("do doPayValidate start ..."));
        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "appSubmissionDtos");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        appSubmissionDto.setPaymentMethod(payMethod);
        String giroAccNum = "";
        if(!StringUtil.isEmpty(payMethod) && ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)){
            giroAccNum = ParamUtil.getString(bpc.request, "giroAccount");
        }
        appSubmissionDto.setGiroAcctNum(giroAccNum);
        String noNeedPayment = bpc.request.getParameter("noNeedPayment");
        log.debug(StringUtil.changeForLog("payMethod:" + payMethod));
        log.debug(StringUtil.changeForLog("noNeedPayment:" + noNeedPayment));
        String action = ParamUtil.getString(bpc.request, "crud_action_additional");
        if ("next".equals(action)) {
            if (!MiscUtil.doubleEquals(appSubmissionDtos.get(0).getAmount(), 0.0)) {
                Map<String,String> errorMap = IaisCommonUtils.genNewHashMap();
                if (StringUtil.isEmpty(payMethod)) {
                    errorMap.put("pay", MessageUtil.replaceMessage("GENERAL_ERR0006", "Payment Method", "field"));
                }else if(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod) && StringUtil.isEmpty(giroAccNum)){
                    errorMap.put("pay",MessageUtil.replaceMessage("GENERAL_ERR0006", "Giro Account", "field"));
                }
                if(!errorMap.isEmpty()){
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "payment");
                    NewApplicationHelper.setAudiErrMap(NewApplicationHelper.checkIsRfi(bpc.request),appSubmissionDto.getAppType(),errorMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
                    ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                }
                if(!errorMap.isEmpty()){
                    boolean isRfi = false;
                    NewApplicationHelper.setAudiErrMap(isRfi, ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE, errorMap, null, appSubmissionDtos.get(0).getLicenceNo());
                    ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePayment");
                    ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, appSubmissionDto);
                    return;
                }
            }
            if(!StringUtil.isEmpty(noNeedPayment)){
                ParamUtil.setSessionAttr(bpc.request,"txnRefNo","");
                try{
                    if(appSubmissionDto.getAppType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)){
                        requestForChangeService.sendRfcSubmittedEmail(appSubmissionDtos,null);
                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        } else {
            appSubmissionDto.setAppGrpNo(null);
            appSubmissionDto.setId(null);
            appSubmissionDto.setAppGrpId(null);
            List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
            if(appGrpPremisesDtoList!=null&&!appGrpPremisesDtoList.isEmpty()){
                appGrpPremisesDtoList.get(0).setExistingData(AppConsts.NO);
            }
            String eqHciCode = bpc.request.getParameter("eqHciCode");
            bpc.request.setAttribute("eqHciCode",eqHciCode);
        }
        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, appSubmissionDto);
        log.info(StringUtil.changeForLog("do doPayValidate end ..."));
    }

    private PremisesListQueryDto getPremisesListQueryDto(List<PremisesListQueryDto> premisesListQueryDtos, String liceId, String premId) {
        PremisesListQueryDto result = null;
        for (PremisesListQueryDto premisesListQueryDto : premisesListQueryDtos) {
            String thisLicId = premisesListQueryDto.getLicenceId();
            String thisPremId = premisesListQueryDto.getPremisesId();
            if (liceId.equals(thisLicId) && premId.equals(thisPremId)) {
                result = premisesListQueryDto;
                break;
            }
        }
        return result;
    }

    private AppGrpPremisesDto getAppGrpPremisesDtoFromAppGrpPremisesDtoList(List<AppGrpPremisesDto> appGrpPremisesDtoList, String premType, String premHciOrConvName) {
        AppGrpPremisesDto result = null;
        if (!StringUtil.isEmpty(premType)) {
            for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList) {
                String premisesVal = getPremisesVal(appGrpPremisesDto);
                if (premType.equals(appGrpPremisesDto.getPremisesType()) && premisesVal.equals(premHciOrConvName)) {
                    result = appGrpPremisesDto;
                    break;
                }
            }
        }
        return result;
    }

    private String getPremisesVal(AppGrpPremisesDto appGrpPremisesDto) {
        String premisesVal = "";
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())) {
            premisesVal = appGrpPremisesDto.getHciName();
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())) {
            premisesVal = appGrpPremisesDto.getConveyanceVehicleNo();
        }
        return premisesVal;
    }

  /*  private String getPremisesVal(PremisesListQueryDto premisesListQueryDto) {
        String premisesVal = "";
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesListQueryDto.getPremisesType())) {
            premisesVal = premisesListQueryDto.getHciName();
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesListQueryDto.getPremisesType())) {
            premisesVal = premisesListQueryDto.getVehicleNo();
        }
        return premisesVal;
    }*/

    private AppGrpPremisesDto genAppGrpPremisesDto(PremisesListQueryDto premisesListQueryDto, HttpServletRequest request) {
        AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
        String premisesType = premisesListQueryDto.getPremisesType();
        appGrpPremisesDto.setPremisesType(premisesType);
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesType)) {
            String postalCode = ParamUtil.getString(request, "postalCode");
            String blkNo = ParamUtil.getString(request, "blkNo");
            String streetName = ParamUtil.getString(request, "streetName");
            String floorNo = ParamUtil.getString(request, "floorNo");
            String unitNo = ParamUtil.getString(request, "unitNo");
            String buildingName = ParamUtil.getString(request, "buildingName");
            String siteAddressType = ParamUtil.getString(request, "siteAddressType");
            String scdfRefNo = ParamUtil.getString(request, "scdfRefNo");
            appGrpPremisesDto.setHciName(premisesListQueryDto.getHciName());
            appGrpPremisesDto.setPostalCode(postalCode);
            appGrpPremisesDto.setBlkNo(blkNo);
            appGrpPremisesDto.setStreetName(streetName);
            appGrpPremisesDto.setFloorNo(floorNo);
            appGrpPremisesDto.setUnitNo(unitNo);
            appGrpPremisesDto.setBuildingName(buildingName);
            appGrpPremisesDto.setAddrType(siteAddressType);
            appGrpPremisesDto.setScdfRefNo(scdfRefNo);
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesType)) {
            String conPostalCode = ParamUtil.getString(request, "conveyancePostalCode");
            String conBlkNo = ParamUtil.getString(request, "conveyanceBlockNo");
            String conStreetName = ParamUtil.getString(request, "conveyanceStreetName");
            String conFloorNo = ParamUtil.getString(request, "conveyanceFloorNo");
            String conUnitNo = ParamUtil.getString(request, "conveyanceUnitNo");
            String conBuildingName = ParamUtil.getString(request, "conveyanceBuildingName");
            String conSiteAddressType = ParamUtil.getString(request, "conveyanceAddrType");
            appGrpPremisesDto.setConveyanceVehicleNo(premisesListQueryDto.getVehicleNo());
            appGrpPremisesDto.setConveyancePostalCode(conPostalCode);
            appGrpPremisesDto.setConveyanceBlockNo(conBlkNo);
            appGrpPremisesDto.setConveyanceStreetName(conStreetName);
            appGrpPremisesDto.setConveyanceFloorNo(conFloorNo);
            appGrpPremisesDto.setConveyanceUnitNo(conUnitNo);
            appGrpPremisesDto.setConveyanceUnitNo(conUnitNo);
            appGrpPremisesDto.setConveyanceBuildingName(conBuildingName);
            appGrpPremisesDto.setConveyanceAddressType(conSiteAddressType);
        }
        return appGrpPremisesDto;
    }

    private boolean compareLocation(PremisesListQueryDto premisesListQueryDto, AppGrpPremisesDto appGrpPremisesDto) {
        String oldAddress = premisesListQueryDto.getAddressWithoutFU();
        String newAddress = appGrpPremisesDto.getAddressWithoutFU();
        if (!oldAddress.equals(newAddress)) {
            return false;
        }
        return true;
    }

    private boolean compareHciName(PremisesListQueryDto premisesListQueryDto, AppGrpPremisesDto appGrpPremisesDto) {
        String newHciName = "";
        String oldHciName = "";
        String newVehicleNo="";
        String oldVehicleNo="";
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesListQueryDto.getPremisesType())) {
            oldHciName = premisesListQueryDto.getHciName();
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesListQueryDto.getPremisesType())) {
            oldHciName = premisesListQueryDto.getHciName();
            oldVehicleNo=premisesListQueryDto.getVehicleNo();
        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesListQueryDto.getPremisesType())){
            oldHciName = premisesListQueryDto.getHciName();
        }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesListQueryDto.getPremisesType())){
            oldHciName=premisesListQueryDto.getHciName();
        }
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())) {
            newHciName = appGrpPremisesDto.getHciName();
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())) {
            newHciName = appGrpPremisesDto.getConveyanceHciName();
            newVehicleNo=appGrpPremisesDto.getConveyanceVehicleNo();
        }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(premisesListQueryDto.getPremisesType())){
            newHciName=appGrpPremisesDto.getOffSiteHciName();
        }else if(ApplicationConsts.PREMISES_TYPE_EAS_MTS_CONVEYANCE.equals(premisesListQueryDto.getPremisesType())){
            newHciName=appGrpPremisesDto.getEasMtsHciName();
        }
        if (!newHciName.equals(oldHciName)||!newVehicleNo.equals(oldVehicleNo)) {
            return false;
        }

        return true;
    }

    private AppSubmissionDto setPersonnelDate(AppSubmissionDto appSubmissionDto, PersonnelListDto personnelListDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        Map<String, LicPsnTypeDto> licPsnTypeDtoMaps = personnelListDto.getLicPsnTypeDtoMaps();
        String licenceNo = appSubmissionDto.getLicenceNo();
        List<String> psnTypes = licPsnTypeDtoMaps.get(licenceNo).getPsnTypes();
        if (psnTypes == null || psnTypes.isEmpty()) {
            return appSubmissionDto;
        }
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            for (String psnType : psnTypes) {
                reSetPersonnels(appSvcRelatedInfoDto, personnelListDto, psnType, null);
            }
        }
        return appSubmissionDto;
    }

    private AppSubmissionDto replacePersonnelDate(AppSubmissionDto appSubmissionDto, PersonnelListDto personnelListDto,
            String oldIdNo) {
        Map<String, LicPsnTypeDto> licPsnTypeDtoMaps = personnelListDto.getLicPsnTypeDtoMaps();
        String licenceNo = appSubmissionDto.getLicenceNo();
        List<String> psnTypes = licPsnTypeDtoMaps.get(licenceNo).getPsnTypes();
        if (psnTypes == null || psnTypes.isEmpty()) {
            return appSubmissionDto;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            for (String psnType : psnTypes) {
                reSetPersonnels(appSvcRelatedInfoDto, personnelListDto, psnType, oldIdNo);
            }
        }
        return appSubmissionDto;
    }

    private void reSetPersonnels(AppSvcRelatedInfoDto targetReletedInfo, PersonnelListDto newPerson, String psnType, String oldIdNo) {
        if (targetReletedInfo == null || newPerson == null || psnType == null) {
            return;
        }
        if (oldIdNo == null) {
            oldIdNo = newPerson.getIdNo();
        }
        boolean changePersonnel = !Objects.equals(oldIdNo, newPerson.getIdNo());
        List<AppSvcPrincipalOfficersDto> targetList = null;
        if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)) {
            targetList = targetReletedInfo.getAppSvcCgoDtoList();
        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(psnType)) {
            targetList = targetReletedInfo.getAppSvcMedAlertPersonList();
        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)
                || ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnType)) {
            targetList = targetReletedInfo.getAppSvcPrincipalOfficersDtoList();
        } else if (ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR.equals(psnType)) {
            targetList = targetReletedInfo.getAppSvcClinicalDirectorDtoList();
        } else if (ApplicationConsts.PERSONNEL_PSN_KAH.equals(psnType)) {
            targetList = targetReletedInfo.getAppSvcKeyAppointmentHolderDtoList();
        }
        if (!IaisCommonUtils.isEmpty(targetList)) {
            for (AppSvcPrincipalOfficersDto target : targetList) {
                if (Objects.equals(target.getIdNo(), oldIdNo) && Objects.equals(target.getPsnType(), psnType)) {
                    if (changePersonnel) {
                        target.setIdNo(newPerson.getIdNo());
                        target.setIdType(newPerson.getIdType());
                    }
                    target.setName(newPerson.getPsnName());
                    target.setSalutation(newPerson.getSalutation());
                    target.setMobileNo(newPerson.getMobileNo());
                    target.setEmailAddr(newPerson.getEmailAddr());
                }
            }
        }
        if (changePersonnel && ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)) {
            List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = targetReletedInfo.getAppSvcDisciplineAllocationDtoList();
            if (appSvcDisciplineAllocationDtoList != null) {
                String idNo = newPerson.getIdNo();
                for (AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : appSvcDisciplineAllocationDtoList) {
                    if (Objects.equals(appSvcDisciplineAllocationDto.getIdNo(), oldIdNo)) {
                        appSvcDisciplineAllocationDto.setIdNo(idNo);
                    }
                }
            }
        }
    }

    private void requestForInformation(BaseProcessClass bpc, String appNo) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do requestForInformationLoading start ...."));
        if (!StringUtil.isEmpty(appNo)) {
            AppSubmissionDto appSubmissionDto = appSubmissionService.getAppSubmissionDtoByAppNo(appNo);

            if (appSubmissionDto != null) {
                String appGrpNo = appSubmissionDto.getAppGrpNo();
                List<AppSubmissionDto> appSubmissionDtoByGroupNo = appSubmissionService.getAppSubmissionDtoByGroupNo(appGrpNo);
                appSubmissionDto.setNeedEditController(true);
                for (AppGrpPremisesDto appGrpPremisesDto1 : appSubmissionDto.getAppGrpPremisesDtoList()) {
                    NewApplicationHelper.setWrkTime(appGrpPremisesDto1);
                }
                if (appSubmissionDtoByGroupNo != null) {
              /*  for(AppSubmissionDto appSubmissionDto1 : appSubmissionDtoByGroupNo){
                    appSubmissionDto1.setNeedEditController(true);
                    for (AppGrpPremisesDto appGrpPremisesDto1 : appSubmissionDto1.getAppGrpPremisesDtoList()) {
                        NewApplicationHelper.setWrkTime(appGrpPremisesDto1);
                    }
                }*/
                }

                AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request, "AppSubmissionDto", appSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request, RfcConst.OLDAPPSUBMISSIONDTO, oldAppSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG, "test");
            }


        }
        log.debug(StringUtil.changeForLog("the do requestForInformationLoading end ...."));
    }

}