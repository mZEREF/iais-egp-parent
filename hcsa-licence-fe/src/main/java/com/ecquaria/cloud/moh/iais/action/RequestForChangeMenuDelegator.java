package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator.ACKMESSAGE;

/****
 *
 *   @date 1/4/2020
 *   @author zixian
 */
@Slf4j
@Delegator("MohRequestForChangeMenuDelegator")
public class RequestForChangeMenuDelegator {
    @Autowired
    private SystemParamConfig systemParamConfig;

    private FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(PersonnelQueryDto.class)
            .searchAttr("PersonnelSearchParam")
            .resultAttr("PersonnelSearchResult")
            .sortField("NAME").sortType(SearchParam.ASCENDING).build();

    private final FilterParameter premiseFilterParameter = new FilterParameter.Builder()
            .clz(PremisesListQueryDto.class)
            .searchAttr("PremisesSearchParam")
            .resultAttr("PremisesSearchResult")
            .sortField("PREMISES_TYPE").sortType(SearchParam.ASCENDING).build();


    @Autowired
    RequestForChangeService requestForChangeService;
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private SystemAdminClient systemAdminClient;

    /**
     * @param bpc
     * @Decription start
     */
    public void start(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do start start ...."));
        String appNo = ParamUtil.getMaskedString(bpc.request, "appNo");
        premiseFilterParameter.setPageSize(systemParamConfig.getPagingSize());
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, null);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG, null);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.OLDAPPSUBMISSIONDTO, null);
        removeSession(bpc);
        requestForInformation(bpc, appNo);


        log.debug(StringUtil.changeForLog("the do start end ...."));
    }

    private void removeSession(BaseProcessClass bpc) {
        bpc.getSession().removeAttribute("dAmount");
        bpc.getSession().removeAttribute("payMethod");
    }

    /**
     * @param bpc
     * @Decription personnleListStart
     */
    public void personnleListStart(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do personnleListStart start ...."));
        filterParameter.setPageSize(systemParamConfig.getPagingSize());
        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, null);
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        log.debug(StringUtil.changeForLog("the do personnleListStart end ...."));
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
        String doSearch = (String) bpc.request.getAttribute("doSearch");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        SearchParam searchParam = SearchResultHelper.getSearchParam(bpc.request, premiseFilterParameter, true);
        searchParam.addFilter("licenseeId", licenseeId, true);
        if (!StringUtil.isEmpty(doSearch)) {
            searchParam.addFilter("type", doSearch, true);
            bpc.request.setAttribute("premiseDoSearch", doSearch);
        }
        QueryHelp.setMainSql("applicationPersonnelQuery", "queryPremises", searchParam);
        SearchResult<PremisesListQueryDto> searchResult = requestForChangeService.searchPreInfo(searchParam);
        if (!StringUtil.isEmpty(searchResult)) {
            ParamUtil.setSessionAttr(bpc.request, "PremisesSearchParam", searchParam);
            ParamUtil.setRequestAttr(bpc.request, "PremisesSearchResult", searchResult);
        }
        List<PremisesListQueryDto> rows = searchResult.getRows();
        for (PremisesListQueryDto premisesListQueryDto : rows) {
            StringBuilder stringBuilder = new StringBuilder();
            List<LicenceDto> licenceDtoByHciCode = requestForChangeService.getLicenceDtoByHciCode(premisesListQueryDto.getHciCode());
            for (LicenceDto licenceDto : licenceDtoByHciCode) {
                stringBuilder.append(licenceDto.getSvcName()).append(',');
            }
            premisesListQueryDto.setSvcId(stringBuilder.toString().substring(0, stringBuilder.toString().lastIndexOf(',')));
        }
        ParamUtil.setSessionAttr(bpc.request, RfcConst.PREMISESLISTDTOS, (Serializable) rows);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE, "Premises List");
        List<SelectOption> list = new ArrayList<>();
        setSelectOption(list);
        ParamUtil.setSessionAttr(bpc.request, "applicationType", (Serializable) list);
        log.debug(StringUtil.changeForLog("the do preparePremisesList end ...."));
    }

    private void setSelectOption(List<SelectOption> list) {
        SelectOption onsite = new SelectOption();
        onsite.setText("On-site");
        onsite.setValue("ONSITE");
        SelectOption conveyance = new SelectOption();
        conveyance.setText("Conveyance");
        conveyance.setValue("CONVEYANCE");
        SelectOption offsiet = new SelectOption();
        offsiet.setText("Off-site");
        offsiet.setValue("OFFSIET");
        list.add(offsiet);
        list.add(conveyance);
        list.add(onsite);
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
        if (!StringUtil.isEmpty(crud_action_value)) {
            bpc.request.setAttribute("doSearch", crud_action_value);
        }
    }

    /**
     * @param bpc
     * @Decription preparePremisesEdit
     *//*
     */
    public void preparePremisesEdit(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the do preparePremisesEdit start ...."));
        NewApplicationHelper.setTimeList(bpc.request);
        List<SelectOption> publicHolidayList = serviceConfigService.getPubHolidaySelect();
        ParamUtil.setSessionAttr(bpc.request, "publicHolidaySelect", (Serializable) publicHolidayList);

        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);
        PremisesListQueryDto premisesListQueryDto = (PremisesListQueryDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.PREMISESLISTQUERYDTO);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        appSubmissionDto.setLicenseeId(licenseeId);
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
                List<LicenceDto> licenceDtoList = requestForChangeService.getLicenceDtoByHciCode(hciCode);
                appGrpPremisesDto1.setLicenceDtos(licenceDtoList);
            }
        }
        if (appGrpPremisesDto != null || rfi != null) {
            log.info(StringUtil.changeForLog("The preparePremises licenseeId is -->:" + licenseeId));
            Map<String, AppGrpPremisesDto> licAppGrpPremisesDtoMap = null;
            if (!StringUtil.isEmpty(licenseeId)) {
                licAppGrpPremisesDtoMap = serviceConfigService.getAppGrpPremisesDtoByLoginId(licenseeId);
            }
            //premise select
            NewApplicationHelper.setPremSelect(bpc.request, licAppGrpPremisesDtoMap);
            //addressType
            NewApplicationHelper.setPremAddressSelect(bpc.request);
            ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.LICAPPGRPPREMISESDTOMAP, (Serializable) licAppGrpPremisesDtoMap);
            if (rfi == null) {
                //when rfc/renew check is select existing premises
                String oldPremSel = IaisCommonUtils.genPremisesKey(premisesListQueryDto.getPostalCode(), premisesListQueryDto.getBlkNo(), premisesListQueryDto.getFloorNo(), premisesListQueryDto.getUnitNo());
                if (oldPremSel.equals(appGrpPremisesDto.getPremisesSelect()) || "-1".equals(appGrpPremisesDto.getPremisesSelect())) {
                    ParamUtil.setRequestAttr(bpc.request, "PageCanEdit", AppConsts.TRUE);
                }

                for (AppGrpPremisesDto appGrpPremisesDto1 : appSubmissionDto.getAppGrpPremisesDtoList()) {
                    NewApplicationHelper.setWrkTime(appGrpPremisesDto1);
                }
            }
        }
        String hciCode = premisesListQueryDto.getHciCode();
        AppSubmissionDto oldAppSubmissionDto = new AppSubmissionDto();
        oldAppSubmissionDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
        AppSubmissionDto appSubmissionDto1 = (AppSubmissionDto) bpc.request.getSession().getAttribute("oldAppSubmissionDto");
        if (appSubmissionDto1 != null) {
            oldAppSubmissionDto = appSubmissionDto1;
        }
        List<LicenceDto> licenceDtoList = requestForChangeService.getLicenceDtoByHciCode(hciCode);
        bpc.request.getSession().setAttribute("licenceDtoList", licenceDtoList);
        appSubmissionDto.setAppGrpPremisesDtoList(reloadPremisesDtoList);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        ParamUtil.setRequestAttr(bpc.request, RfcConst.RELOADPREMISES, reloadPremisesDtoList);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, "oldAppSubmissionDto", oldAppSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE, "Premises Amendment");
        ParamUtil.setRequestAttr(bpc.request, "premisesList", AppConsts.YES);
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
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.OLDAPPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.PREMISESLISTQUERYDTO, premisesListQueryDto);


        //ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, crudActionType);
        log.debug(StringUtil.changeForLog("the do doPremisesList end ...."));

    }

    /**
     * @param bpc
     * @Decription doPremisesEdit
     */
    public void doPremisesEdit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doPremisesEdit start ...."));
        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_VALUE);
        if ("back".equals(action)) {
            return;
        }
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = NewApplicationDelegator.genAppGrpPremisesDtoList(bpc.request);
        ParamUtil.setRequestAttr(bpc.request, RfcConst.RELOADPREMISES, appGrpPremisesDtoList);
        appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);

        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.APPSUBMISSIONDTO, appSubmissionDto);
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "oldAppSubmissionDto");

        List<String> premisesHciList = (List<String>) ParamUtil.getSessionAttr(bpc.request, NewApplicationConstant.PREMISES_HCI_LIST);
        String keyWord = MasterCodeUtil.getCodeDesc("MS001");

        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        Map<String, String> errorMap = requestForChangeService.doValidatePremiss(appSubmissionDto, oldAppSubmissionDto, premisesHciList, keyWord, isRfi);
        String crud_action_type_continue = bpc.request.getParameter("crud_action_type_continue");
        String crud_action_type_form_value = bpc.request.getParameter("crud_action_type_form_value");
        bpc.request.setAttribute("continueStep", crud_action_type_form_value);
        bpc.request.setAttribute("crudActionTypeContinue", crud_action_type_continue);
        if ("continue".equals(crud_action_type_continue)) {
            errorMap.remove("hciNameUsed");
        }
        String string = errorMap.get("hciNameUsed");
        if (string != null) {
            bpc.request.setAttribute("hciNameUsed", "hciNameUsed");
        }
        /*  List<String> selectLicence = getSelectLicence(bpc.request);
        if (selectLicence.isEmpty()) {
            errorMap.put("selectLicence", "UC_CHKLMD001_ERR001");
        } else {
            //todo application is not padding
        }
*/
        if (errorMap.size() > 0) {
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
        String personName = ParamUtil.getRequestString(bpc.request, "perName");
        if (!StringUtil.isEmpty(personName)) {
            searchParam.addFilter("personName", "%" + personName + "%", true);
        }
        QueryHelp.setMainSql("applicationPersonnelQuery", "appPersonnelQuery", searchParam);
        SearchResult searchResult = requestForChangeService.psnDoQuery(searchParam);
//        PaginationHandler<PersonnelListDto> handler = new PaginationHandler<>("personPagDiv", "personBodyDiv");
        if (!StringUtil.isEmpty(searchResult)) {
            ParamUtil.setSessionAttr(bpc.request, "PersonnelSearchParam", searchParam);
            ParamUtil.setRequestAttr(bpc.request, "PersonnelSearchResult", searchResult);
        }
        if (!StringUtil.isEmpty(psnTypeSearch)) {
            List<PersonnelQueryDto> personnelQueryDtos = searchResult.getRows();
            List<PersonnelListDto> personnelListDtos = IaisCommonUtils.genNewArrayList();
            for (PersonnelQueryDto dto : personnelQueryDtos) {
                String idNo = dto.getIdNo();
                List<String> personIds = requestForChangeService.getPersonnelIdsByIdNo(idNo);
                PersonnelListDto personnelListDto = MiscUtil.transferEntityDto(dto, PersonnelListDto.class);
                Map<String, LicPsnTypeDto> map = IaisCommonUtils.genNewHashMap();
                List<LicKeyPersonnelDto> licByPerId = requestForChangeService.getLicKeyPersonnelDtoByPerId(personIds);
                List<String> licIds = IaisCommonUtils.genNewArrayList();
                for (LicKeyPersonnelDto dto1 : licByPerId) {
                    String licseeId = dto1.getLicenseeId();
                    if (licenseeId.equals(licseeId)) {
                        String licSvcName = dto1.getLicSvcName();
                        String psnType = dto1.getPsnType();
                        String licenceId = dto1.getLicenceId();
                        String licNo = dto1.getLicNo();
                        String professionRegnNo = dto1.getProfessionRegnNo();
                        String professionType = dto1.getProfessionType();
                        if (psnTypeSearch.equals(psnType)) {
                            personnelListDto.setProfessionRegnNo(professionRegnNo);
                            personnelListDto.setProfessionType(professionType);
                            LicPsnTypeDto licPsnTypeDto = map.get(licNo);
                            if (!licIds.contains(licenceId)) {
                                licIds.add(licenceId);
                            }
                            if (licPsnTypeDto == null) {
                                licPsnTypeDto = new LicPsnTypeDto();
                                licPsnTypeDto.setLicSvcName(licSvcName);
                                List<String> psnTypes = IaisCommonUtils.genNewArrayList();
                                psnTypes.add(psnType);
                                licPsnTypeDto.setPsnTypes(psnTypes);
                                map.put(licNo, licPsnTypeDto);
                            } else {
                                List<String> psnTypes = licPsnTypeDto.getPsnTypes();
                                if (!psnTypes.contains(psnType)) {
                                    psnTypes.add(psnType);
                                }
                            }
                            personnelListDto.setLicenceIds(licIds);
                            personnelListDto.setLicPsnTypeDtoMaps(map);
                            if (!IaisCommonUtils.isEmpty(licIds)) {
                                personnelListDtos.add(personnelListDto);
                            }
                        }
                    }
                }
            }
            List<SelectOption> personelRoles = getPsnType();
            ParamUtil.setRequestAttr(bpc.request, "PersonnelRoleList", personelRoles);
            ParamUtil.setSessionAttr(bpc.request, "personnelListDtos", (Serializable) personnelListDtos);
            ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE, "Personnel List");
            ParamUtil.setRequestAttr(bpc.request, "psnType", psnTypeSearch);
            return;
        }
        List<PersonnelQueryDto> personnelQueryDtos = searchResult.getRows();
        List<PersonnelListDto> personnelListDtos = IaisCommonUtils.genNewArrayList();
        for (PersonnelQueryDto dto : personnelQueryDtos) {
            String idNo = dto.getIdNo();
            List<String> personIds = requestForChangeService.getPersonnelIdsByIdNo(idNo);
            PersonnelListDto personnelListDto = MiscUtil.transferEntityDto(dto, PersonnelListDto.class);
            Map<String, LicPsnTypeDto> map = IaisCommonUtils.genNewHashMap();
            List<LicKeyPersonnelDto> licByPerId = requestForChangeService.getLicKeyPersonnelDtoByPerId(personIds);
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            for (LicKeyPersonnelDto dto1 : licByPerId) {
                String licseeId = dto1.getLicenseeId();
                if (licenseeId.equals(licseeId)) {
                    String licSvcName = dto1.getLicSvcName();
                    String psnType = dto1.getPsnType();
                    String licenceId = dto1.getLicenceId();
                    String licNo = dto1.getLicNo();
                    String professionRegnNo = dto1.getProfessionRegnNo();
                    String professionType = dto1.getProfessionType();
                    String speciality = dto1.getSpeciality();
                    String specialityOther = dto1.getSpecialityOther();
                    String subSpeciality = dto1.getSubSpeciality();
                    if (!StringUtil.isEmpty(professionRegnNo) && !StringUtil.isEmpty(professionType)) {
                        personnelListDto.setProfessionRegnNo(professionRegnNo);
                        personnelListDto.setProfessionType(professionType);
                    }
                    if (!StringUtil.isEmpty(speciality)) {
                        personnelListDto.setSpeciality(speciality);
                        personnelListDto.setSubSpeciality(subSpeciality);
                        personnelListDto.setSpecialityOther(specialityOther);
                    }
                    LicPsnTypeDto licPsnTypeDto = map.get(licNo);
                    if (!licIds.contains(licenceId)) {
                        licIds.add(licenceId);
                    }
                    if (licPsnTypeDto == null) {
                        licPsnTypeDto = new LicPsnTypeDto();
                        licPsnTypeDto.setLicSvcName(licSvcName);
                        List<String> psnTypes = IaisCommonUtils.genNewArrayList();
                        psnTypes.add(psnType);
                        licPsnTypeDto.setPsnTypes(psnTypes);
                        map.put(licNo, licPsnTypeDto);
                        personnelListDto.setLicenceNo(licNo);
                    } else {
                        List<String> psnTypes = licPsnTypeDto.getPsnTypes();
                        if (!psnTypes.contains(psnType)) {
                            psnTypes.add(psnType);
                        }
                    }
                }
            }
            personnelListDto.setLicenceIds(licIds);
            personnelListDto.setLicPsnTypeDtoMaps(map);
            if (!IaisCommonUtils.isEmpty(licIds)) {
                personnelListDtos.add(personnelListDto);
            }
        }
//        handler.setAllData(personnelListDtos);
//        handler.preLoadingPage();
        List<SelectOption> personelRoles = getPsnType();
        ParamUtil.setRequestAttr(bpc.request, "PersonnelRoleList", personelRoles);
        ParamUtil.setSessionAttr(bpc.request, "personnelListDtos", (Serializable) personnelListDtos);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE, "Personnel List");
        List<SelectOption> specialtySelectList = genSpecialtySelectList("CLB");
        List<SelectOption> replaceOptions = genReplacePersonnel(licenseeId);
        ParamUtil.setSessionAttr(bpc.request, "SpecialtySelectList", (Serializable) specialtySelectList);
        ParamUtil.setSessionAttr(bpc.request, "replaceOptions", (Serializable) replaceOptions);

        log.debug(StringUtil.changeForLog("the do preparePersonnelList end ...."));
        log.debug(StringUtil.changeForLog("the preparePersonnel end ...."));
    }

    private List<SelectOption> genSpecialtySelectList(String svcCode) {
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
            for (PersonnelListQueryDto dto : persons) {
                String idNo = dto.getIdNo();
                String name = dto.getName();
                String idType = dto.getIdType();
                SelectOption s = new SelectOption(idType + "," + idNo, name + "," + idNo + "," + idType);
                selectOptions.add(s);
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
        SelectOption sp1 = new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, "Clinical Governance Officer");
        SelectOption sp2 = new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_PO, "Principal Officer");
        SelectOption sp3 = new SelectOption(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO, "Deputy Principal Officer");
        personelRoles.add(sp1);
        personelRoles.add(sp2);
        personelRoles.add(sp3);
        return personelRoles;
    }

    /**
     * @param bpc
     * @Decription doPersonnelList
     */
    public void doPersonnelList(BaseProcessClass bpc) {
        setLicseeAndPsnDropDown(bpc);
        log.debug(StringUtil.changeForLog("the do doPersonnelList start ...."));
        String actionType = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setSessionAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, actionType);
        log.debug(StringUtil.changeForLog("the do doPersonnelList end ...."));
    }

    /**
     * @param bpc
     * @Decription search
     */
    public void personnleSearch(BaseProcessClass bpc) {
        log.info("search================????>>>>>");
        String personName = ParamUtil.getString(bpc.request, "personName");
        String psnType = ParamUtil.getString(bpc.request, "psnTypes");
        SearchParam searchParam = SearchResultHelper.getSearchParam(bpc.request, filterParameter, true);
        if (!StringUtil.isEmpty(personName)) {
            searchParam.addFilter("personName", "%" + personName + "%", true);
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

    /**
     * @param bpc
     * @Decription sort
     */
    public void personnleSorting(BaseProcessClass bpc) {
        SearchResultHelper.doSort(bpc.request, filterParameter);
    }


    /**
     * @param bpc
     * @Decription page
     */
    public void personnlePaging(BaseProcessClass bpc) {
        SearchResultHelper.doPage(bpc.request, filterParameter);
    }

    /**
     * @param bpc
     * @Decription preparePersonnelEdit
     */
    public void preparePersonnelEdit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doPersonnelList start ...."));
        String hiddenIndex = ParamUtil.getString(bpc.request, "hiddenIndex");
        String idNo = ParamUtil.getMaskedString(bpc.request, "personnelNo" + hiddenIndex);
        List<PersonnelListDto> personnelList = (List<PersonnelListDto>) ParamUtil.getSessionAttr(bpc.request, "personnelListDtos");
        PersonnelListDto personnelEditDto = new PersonnelListDto();
        if (StringUtil.isEmpty(idNo)) {
            personnelEditDto = (PersonnelListDto) ParamUtil.getSessionAttr(bpc.request, "personnelEditDto");
        } else {
            for (PersonnelListDto dto : personnelList) {
                String idNo1 = dto.getIdNo();
                if (idNo.equals(idNo1)) {
                    personnelEditDto = dto;
                    break;
                }
            }
        }
        String replaceName = ParamUtil.getString(bpc.request, "replaceName");
        if (StringUtil.isEmpty(replaceName)) {
            ParamUtil.setRequestAttr(bpc.request, "replaceName", replaceName);
        }
        Map<String, LicPsnTypeDto> licPsnTypeDtoMaps = personnelEditDto.getLicPsnTypeDtoMaps();
        List<String> psnTypes = IaisCommonUtils.genNewArrayList();
        for (LicPsnTypeDto dto : licPsnTypeDtoMaps.values()) {
            psnTypes.addAll(dto.getPsnTypes());
        }
        if(psnTypes.contains("PO")){
            ParamUtil.setRequestAttr(bpc.request, "containsPO", Boolean.TRUE);
        }else {
            ParamUtil.setRequestAttr(bpc.request, "containsPO", Boolean.FALSE);
        }
        ParamUtil.setSessionAttr(bpc.request, "personnelEditDto", personnelEditDto);
        log.debug(StringUtil.changeForLog("the do doPersonnelList end ...."));
        List<SelectOption> idTypeSelectList = NewApplicationHelper.getIdTypeSelOp();
        ParamUtil.setRequestAttr(bpc.request, ClinicalLaboratoryDelegator.DROPWOWN_IDTYPESELECT, idTypeSelectList);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE, "Personnel Amendment");
        log.debug(StringUtil.changeForLog("the do preparePersonnelEdit end ...."));
    }


    /**
     * @param bpc
     * @Decription doPersonnelEdit
     */
    public void doPersonnelEdit(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do doPersonnelEdit start ...."));
        String actionType = ParamUtil.getRequestString(bpc.request, "actionType");
        if ("back".equals(actionType)) {
            ParamUtil.setRequestAttr(bpc.request, "action_type", "back");
            return;
        }
        PersonnelListDto personnelEditDto = (PersonnelListDto) ParamUtil.getSessionAttr(bpc.request, "personnelEditDto");
        //update
        //String salutation = ParamUtil.getString(bpc.request, "salutation");
        String email = ParamUtil.getString(bpc.request, "emailAddr");
        String mobile = ParamUtil.getString(bpc.request, "mobileNo");
        String designation = ParamUtil.getString(bpc.request, "designation");
        String professionType = ParamUtil.getString(bpc.request, "professionType");
        String professionRegnNo = ParamUtil.getString(bpc.request, "professionRegnNo");
        String specialty = ParamUtil.getString(bpc.request, "specialty");
        String specialityOther = ParamUtil.getString(bpc.request, "specialityOther");
        String officeTelNo = ParamUtil.getString(bpc.request, "officeTelNo");
        String subspecialize = ParamUtil.getString(bpc.request, "subspecialize");
        //new
        String salutation1 = ParamUtil.getString(bpc.request, "salutation1");
        String psnName1 = ParamUtil.getString(bpc.request, "psnName1");
        String idType1 = ParamUtil.getString(bpc.request, "idType1");
        String idNo1 = ParamUtil.getString(bpc.request, "idNo1");
        String email1 = ParamUtil.getString(bpc.request, "emailAddr1");
        String mobile1 = ParamUtil.getString(bpc.request, "mobileNo1");
        String designation1 = ParamUtil.getString(bpc.request, "designation1");
        String professionType1 = ParamUtil.getString(bpc.request, "professionType1");
        String professionRegnNo1 = ParamUtil.getString(bpc.request, "professionRegnNo1");
        String specialty1 = ParamUtil.getString(bpc.request, "specialty1");
        String specialityOther1 = ParamUtil.getString(bpc.request, "specialityOther1");
        String officeTelNo1 = ParamUtil.getString(bpc.request, "officeTelNo1");
        String subspecialize1 = ParamUtil.getString(bpc.request, "subspecialize1");
        String replaceName = ParamUtil.getString(bpc.request, "replaceName");
        String editSelect = ParamUtil.getString(bpc.request, "editSelect");
        ParamUtil.setRequestAttr(bpc.request, "editSelectResult", editSelect);
        Map<String, LicPsnTypeDto> licPsnTypeDtoMaps = personnelEditDto.getLicPsnTypeDtoMaps();
        List<String> psnTypes = IaisCommonUtils.genNewArrayList();
        for (LicPsnTypeDto dto : licPsnTypeDtoMaps.values()) {
            psnTypes.addAll(dto.getPsnTypes());
        }
        personnelEditDto.setEmailAddr(email);
        personnelEditDto.setMobileNo(mobile);
        if (psnTypes.contains("CGO")) {
            personnelEditDto.setSpeciality(specialty);
            personnelEditDto.setSpecialityOther(specialityOther);
            personnelEditDto.setSubSpeciality(subspecialize);
            personnelEditDto.setProfessionType(professionType);
            personnelEditDto.setProfessionRegnNo(professionRegnNo);
            personnelEditDto.setDesignation(designation);
        }
        if (psnTypes.contains("PO")) {
            personnelEditDto.setDesignation(designation);
            personnelEditDto.setOfficeTelNo(officeTelNo);
        }
        PersonnelListDto newPerson = new PersonnelListDto();
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        if ("replace".equals(editSelect) && "new".equals(replaceName)) {
            newPerson.setIdNo(idNo1);
            newPerson.setIdType(idType1);
            newPerson.setPsnName(psnName1);
            newPerson.setSalutation(salutation1);
            newPerson.setSpeciality(specialty1);
            newPerson.setSpecialityOther(specialityOther1);
            newPerson.setSubSpeciality(subspecialize1);
            newPerson.setProfessionType(professionType1);
            newPerson.setProfessionRegnNo(professionRegnNo1);
            newPerson.setDesignation(designation1);
            newPerson.setEmailAddr(email1);
            newPerson.setMobileNo(mobile1);
            newPerson.setOfficeTelNo(officeTelNo1);
            if (StringUtil.isEmpty(email1)) {
                errMap.put("emailAddr1", "UC_CHKLMD001_ERR001");
            } else if (!StringUtil.isEmpty(email1)) {
                if (!email1.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                    errMap.put("emailAddr1", "CHKLMD001_ERR006");
                }
            }
            if (StringUtil.isEmpty(mobile1)) {
                errMap.put("mobileNo1", "UC_CHKLMD001_ERR001");
            } else if (!StringUtil.isEmpty(mobile1)) {
                if (!mobile1.matches("^[8|9][0-9]{7}$")) {
                    errMap.put("mobileNo1", "CHKLMD001_ERR004");
                }
            }
            if (psnTypes.contains("CGO") && StringUtil.isEmpty(professionType1)) {
                errMap.put("professionType1", "UC_CHKLMD001_ERR001");
            }
            if (psnTypes.contains("CGO") && StringUtil.isEmpty(salutation1)) {
                errMap.put("salutation1", "UC_CHKLMD001_ERR001");
            }
            if (psnTypes.contains("PO") && StringUtil.isEmpty(salutation1)) {
                errMap.put("salutation1", "UC_CHKLMD001_ERR001");
            }
            if (StringUtil.isEmpty(psnName1)) {
                errMap.put("psnName1", "UC_CHKLMD001_ERR001");
            }
            if (StringUtil.isEmpty(idType1)) {
                errMap.put("idType1", "UC_CHKLMD001_ERR001");
            }
            if (StringUtil.isEmpty(idNo1)) {
                errMap.put("idNo1", "UC_CHKLMD001_ERR001");
            } else {
                if ("FIN".equals(idType1)) {
                    boolean b = SgNoValidator.validateFin(idNo1);
                    if (!b) {
                        errMap.put("idNo1", "CHKLMD001_ERR005");
                    }
                }
                if ("NRIC".equals(idType1)) {
                    boolean b1 = SgNoValidator.validateNric(idNo1);
                    if (!b1) {
                        errMap.put("idNo1", "CHKLMD001_ERR005");
                    }
                }
            }
            if (psnTypes.contains("CGO") && StringUtil.isEmpty(designation1)) {
                errMap.put("designation1", "UC_CHKLMD001_ERR001");
            }
            if (psnTypes.contains("PO") && StringUtil.isEmpty(designation1)) {
                errMap.put("designation1", "UC_CHKLMD001_ERR001");
            }
            if (psnTypes.contains("CGO") && StringUtil.isEmpty(professionRegnNo1)) {
                errMap.put("professionRegnNo1", "UC_CHKLMD001_ERR001");
            }
            if (StringUtil.isEmpty(editSelect)) {
                errMap.put("editSelect1", "UC_CHKLMD001_ERR001");
            }
            if (psnTypes.contains("PO") && StringUtil.isEmpty(officeTelNo1)) {
                errMap.put("officeTelNo1", "UC_CHKLMD001_ERR001");
            }
            if (psnTypes.contains("PO") && !StringUtil.isEmpty(officeTelNo1)&&!officeTelNo1.matches("^[6][0-9]{7}$")) {
                errMap.put("officeTelNo1", "GENERAL_ERR0015");
            }
            if (psnTypes.contains("CGO") && StringUtil.isEmpty(specialty1)) {
                errMap.put("specialty1", "UC_CHKLMD001_ERR001");
            }
            if (psnTypes.contains("CGO") && !"other".equals(specialty1)&&StringUtil.isEmpty(specialityOther1)) {
                errMap.put("specialityOther1", "UC_CHKLMD001_ERR001");
            }
        }
        if ("update".equals(editSelect)) {
            if (StringUtil.isEmpty(email)) {
                errMap.put("emailAddr", "UC_CHKLMD001_ERR001");
            } else if (!StringUtil.isEmpty(email)) {
                if (!email.matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                    errMap.put("emailAddr", "CHKLMD001_ERR006");
                }
            }
            if (StringUtil.isEmpty(mobile)) {
                errMap.put("mobileNo", "UC_CHKLMD001_ERR001");
            } else if (!StringUtil.isEmpty(mobile)) {
                if (!mobile.matches("^[8|9][0-9]{7}$")) {
                    errMap.put("mobileNo", "CHKLMD001_ERR004");
                }
            }
            if (psnTypes.contains("CGO") && StringUtil.isEmpty(professionType)) {
                errMap.put("professionType", "UC_CHKLMD001_ERR001");
            }
            if (psnTypes.contains("CGO") && StringUtil.isEmpty(designation)) {
                errMap.put("designation", "UC_CHKLMD001_ERR001");
            }
            if (psnTypes.contains("PO") && StringUtil.isEmpty(designation)) {
                errMap.put("designation", "UC_CHKLMD001_ERR001");
            }
            if (psnTypes.contains("CGO") && StringUtil.isEmpty(professionRegnNo)) {
                errMap.put("professionRegnNo", "UC_CHKLMD001_ERR001");
            }
            if (StringUtil.isEmpty(editSelect)) {
                errMap.put("editSelect", "UC_CHKLMD001_ERR001");
            }
            if (psnTypes.contains("PO") && StringUtil.isEmpty(officeTelNo)) {
                errMap.put("officeTelNo", "UC_CHKLMD001_ERR001");
            }
            if (psnTypes.contains("PO") && !StringUtil.isEmpty(officeTelNo)&&!officeTelNo.matches("^[6][0-9]{7}$")) {
                errMap.put("officeTelNo", "GENERAL_ERR0015");
            }
            if (psnTypes.contains("CGO") && StringUtil.isEmpty(specialty)) {
                errMap.put("specialty", "UC_CHKLMD001_ERR001");
            }
            if (psnTypes.contains("CGO") && "other".equals(specialty)&&StringUtil.isEmpty(specialityOther)) {
                errMap.put("specialityOther", "UC_CHKLMD001_ERR001");
            }
        }
        if ("replace".equals(editSelect) && !"new".equals(replaceName)) {
            if (StringUtil.isEmpty(replaceName)) {
                errMap.put("replaceName", "UC_CHKLMD001_ERR001");
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
                newPerson.setSpeciality(psn.getSpeciality());
                newPerson.setSubSpeciality(psn.getSubSpeciality());
                newPerson.setProfessionType(psn.getProfessionType());
                newPerson.setProfessionRegnNo(psn.getProfRegNo());
                newPerson.setDesignation(psn.getDesignation());
                newPerson.setEmailAddr(psn.getEmailAddr());
                newPerson.setMobileNo(psn.getMobileNo());
                newPerson.setOfficeTelNo(psn.getOfficeTelNo());

                if (StringUtil.isEmpty(newPerson.getEmailAddr())) {
                    errMap.put("emailAddr2", "UC_CHKLMD001_ERR001");
                } else if (!StringUtil.isEmpty(newPerson.getEmailAddr())) {
                    if (!newPerson.getEmailAddr().matches("^[a-zA-Z0-9_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$")) {
                        errMap.put("emailAddr2", "CHKLMD001_ERR006");
                    }
                }
                if (StringUtil.isEmpty(newPerson.getMobileNo())) {
                    errMap.put("mobileNo2", "UC_CHKLMD001_ERR001");
                } else if (!StringUtil.isEmpty(newPerson.getMobileNo())) {
                    if (!newPerson.getMobileNo().matches("^[8|9][0-9]{7}$")) {
                        errMap.put("mobileNo2", "CHKLMD001_ERR004");
                    }
                }
                if (psnTypes.contains("CGO") && StringUtil.isEmpty(newPerson.getProfessionType())) {
                    errMap.put("professionType2", "UC_CHKLMD001_ERR001");
                }
                if (psnTypes.contains("CGO") && StringUtil.isEmpty(newPerson.getSalutation())) {
                    errMap.put("salutation2", "UC_CHKLMD001_ERR001");
                }
                if (psnTypes.contains("PO") && StringUtil.isEmpty(newPerson.getSalutation())) {
                    errMap.put("salutation2", "UC_CHKLMD001_ERR001");
                }
                if (StringUtil.isEmpty(newPerson.getPsnName())) {
                    errMap.put("psnName2", "UC_CHKLMD001_ERR001");
                }
                if (StringUtil.isEmpty(newPerson.getIdType())) {
                    errMap.put("idType2", "UC_CHKLMD001_ERR001");
                }
                if (StringUtil.isEmpty(newPerson.getIdNo())) {
                    errMap.put("idNo2", "UC_CHKLMD001_ERR001");
                }
                if (psnTypes.contains("CGO") && StringUtil.isEmpty(newPerson.getDesignation())) {
                    errMap.put("designation2", "UC_CHKLMD001_ERR001");
                }
                if (psnTypes.contains("PO") && StringUtil.isEmpty(newPerson.getDesignation())) {
                    errMap.put("designation2", "UC_CHKLMD001_ERR001");
                }
                if (psnTypes.contains("CGO") && StringUtil.isEmpty(newPerson.getProfessionRegnNo())) {
                    errMap.put("professionRegnNo2", "UC_CHKLMD001_ERR001");
                }
                if (StringUtil.isEmpty(editSelect)) {
                    errMap.put("editSelect2", "UC_CHKLMD001_ERR001");
                }
                if (psnTypes.contains("PO") && StringUtil.isEmpty(newPerson.getOfficeTelNo())) {
                    errMap.put("officeTelNo2", "UC_CHKLMD001_ERR001");
                }
                if (psnTypes.contains("PO") && !StringUtil.isEmpty(newPerson.getOfficeTelNo())&&!newPerson.getOfficeTelNo().matches("^[6][0-9]{7}$")) {
                    errMap.put("officeTelNo", "GENERAL_ERR0015");
                }
                if (psnTypes.contains("CGO") && StringUtil.isEmpty(newPerson.getSpeciality())) {
                    errMap.put("specialty2", "UC_CHKLMD001_ERR001");
                }
                if (psnTypes.contains("CGO") &&!"other".equals(newPerson.getSpeciality())&&StringUtil.isEmpty(newPerson.getSpecialityOther())) {
                    errMap.put("specialityOther2", "UC_CHKLMD001_ERR001");
                }
            }
        }
        if(StringUtil.isEmpty(editSelect)){
            errMap.put("editSelect", "UC_CHKLMD001_ERR001");
        }
        if (!errMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, "action_type", "valid");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setSessionAttr(bpc.request, "personnelEditDto", personnelEditDto);
            ParamUtil.setSessionAttr(bpc.request, "newPerson", newPerson);
            if (StringUtil.isEmpty(replaceName)) {
                ParamUtil.setSessionAttr(bpc.request, "replaceName", null);
            }
            ParamUtil.setRequestAttr(bpc.request, "replaceName", replaceName);
            return;
        }
        List<String> licenceIds = personnelEditDto.getLicenceIds();
        if (IaisCommonUtils.isEmpty(licenceIds)) {
            ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "There is ongoing application for the licence");
            return;
        }
        List<AppSubmissionDto> appSubmissionDtos = requestForChangeService.getAppSubmissionDtoByLicenceIds(licenceIds);
        List<AppSvcRelatedInfoDto> oldSvcDto = appSubmissionDtos.get(0).getAppSvcRelatedInfoDtoList();
        String appGroupNo = requestForChangeService.getApplicationGroupNumber(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
            if (appSubmissionDto != null) {
                appSubmissionDto.setLicenseeId(licenseeId);
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                String serviceName = appSvcRelatedInfoDtoList.get(0).getServiceName();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
                String svcId = hcsaServiceDto.getId();
                String svcCode = hcsaServiceDto.getSvcCode();
                appSvcRelatedInfoDtoList.get(0).setServiceId(svcId);
                appSvcRelatedInfoDtoList.get(0).setServiceCode(svcCode);
            }
        }
        List<AppSubmissionDto> appSubmissionDtos1 = IaisCommonUtils.genNewArrayList();
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
            NewApplicationHelper.setSubmissionDtoSvcData(bpc.request, appSubmissionDto);
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
            appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
            appSubmissionDto.setAppGrpNo(appGroupNo);
            appSubmissionDto.setAmount(0.0);
            String draftNo = appSubmissionDto.getDraftNo();
            if (StringUtil.isEmpty(draftNo)) {
                appSubmissionService.setDraftNo(appSubmissionDto);
            }
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                    appGrpPremisesDto.setNeedNewLicNo(false);
                }
            }
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
            appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            if ("replace".equals(editSelect)) {
                AppSubmissionDto appSubmissionDto2 = replacePersonnelDate(appSubmissionDto, newPerson);
                appSubmissionDto.setAutoRfc(false);
                appSubmissionDto.setAmount(0.0);
                appSubmissionDto.setAmountStr("$0.0");
                appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
                appSubmissionDtos1.add(appSubmissionDto2);
            } else {
                AppSubmissionDto appSubmissionDto1 = setPersonnelDate(appSubmissionDto, personnelEditDto);
                appSubmissionDto.setAutoRfc(true);
                appSubmissionDto.setAmount(0.0);
                appSubmissionDto.setAmountStr("$0.0");
                appSubmissionDtos1.add(appSubmissionDto1);
            }
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDtos1.get(0).getAppSvcRelatedInfoDtoList();
        if (appSvcRelatedInfoDtoList.equals(oldSvcDto)) {
            requestForChangeService.saveAppsBySubmissionDtos(appSubmissionDtos1);
        }
        ParamUtil.setRequestAttr(bpc.request, "action_type", "bank");
        ParamUtil.setRequestAttr(bpc.request, "AppSubmissionDto", appSubmissionDtos1.get(0));
        ParamUtil.setSessionAttr(bpc.request, "appSubmissionDtos", (Serializable) appSubmissionDtos1);
        log.debug(StringUtil.changeForLog("the do doPersonnelEdit end ...."));
    }

    public void preparePersonnelBank(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prePayment start ...."));
        Double dAmount = 0.0;
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto)ParamUtil.getRequestAttr(bpc.request, "AppSubmissionDto");
        ParamUtil.setRequestAttr(bpc.request, "AppSubmissionDto", appSubmissionDto);
        bpc.request.getSession().setAttribute("dAmount", "$" + dAmount);
        log.debug(StringUtil.changeForLog("the do prePayment end ...."));
    }

    public void jumpPersonnelBank(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do jumpBank start ...."));
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        if (StringUtil.isEmpty(payMethod)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, "prePayment");
            return;
        }
        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "AppSubmissionDtos");
        bpc.request.getSession().setAttribute("payMethod", payMethod);
        if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)) {
            String backUrl = "hcsa-licence-web/eservice/INTERNET/MohRfcPersonnelList/1/ack";
            StringBuffer url = new StringBuffer();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/payment-web/eservice/INTERNET/PaymentRequest")
                    .append("?amount=").append(MaskUtil.maskValue("amount", String.valueOf(appSubmissionDtos.get(0).getAmount())))
                    .append("&payMethod=").append(MaskUtil.maskValue("payMethod", payMethod))
                    .append("&reqNo=").append(MaskUtil.maskValue("reqNo", appSubmissionDtos.get(0).getAppGrpNo()))
                    .append("&backUrl=").append(MaskUtil.maskValue("backUrl", backUrl));
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            return;
        } else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)) {
            String appGrpId = appSubmissionDtos.get(0).getAppGrpId();
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS);
            serviceConfigService.updatePaymentStatus(appGrp);
            ParamUtil.setRequestAttr(bpc.request, "PmtStatus", ApplicationConsts.PAYMENT_METHOD_NAME_GIRO);
            ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "ack");
        }
        log.debug(StringUtil.changeForLog("the do jumpBank end ...."));
    }

    public void personnelDashboard(BaseProcessClass bpc) {
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        try {
            bpc.response.sendRedirect(tokenUrl);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * @param bpc
     * @Decription prepareAckPage
     */
    public void selectLicence(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do selectLicence start ...."));
        PremisesListQueryDto premisesListQueryDto = (PremisesListQueryDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.PREMISESLISTQUERYDTO);
        String premisesId = premisesListQueryDto.getPremisesId();
        List<LicenceDto> licenceDtoList = requestForChangeService.getLicenceDtoByPremisesId(premisesId);
        bpc.request.setAttribute("licenceDtoList", licenceDtoList);

        log.debug(StringUtil.changeForLog("the do selectLicence end ...."));
    }


    public void PrepareAckPage(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareAckPage start ...."));

        log.debug(StringUtil.changeForLog("the do prepareAckPage end ...."));
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
        bpc.request.getSession().setAttribute("dAmount", "$" + dAmount);
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
        if (StringUtil.isEmpty(payMethod)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, "prePayment");
            return;
        }
        List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "appSubmissionDtos");
        bpc.request.getSession().setAttribute("payMethod", payMethod);
        if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)) {
            String backUrl = "hcsa-licence-web/eservice/INTERNET/MohRfcPermisesList/1/doPayment";
            StringBuffer url = new StringBuffer();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/payment-web/eservice/INTERNET/PaymentRequest")
                    .append("?amount=").append(MaskUtil.maskValue("amount", String.valueOf(appSubmissionDtos.get(0).getAmount())))
                    .append("&payMethod=").append(MaskUtil.maskValue("payMethod", payMethod))
                    .append("&reqNo=").append(MaskUtil.maskValue("reqNo", appSubmissionDtos.get(0).getAppGrpNo()))
                    .append("&backUrl=").append(MaskUtil.maskValue("backUrl", backUrl));
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            return;
        } else if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)) {
            String appGrpId = appSubmissionDtos.get(0).getAppGrpId();
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS);
            serviceConfigService.updatePaymentStatus(appGrp);
            ParamUtil.setRequestAttr(bpc.request, "PmtStatus", ApplicationConsts.PAYMENT_METHOD_NAME_GIRO);
            ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "ack");
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


    /**/

    /**
     * @param bpc
     * @Decription doPayment
     */
    public void doPayment(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doPayment start ...."));
        String switchValue = "loading";
        String pmtStatus = ParamUtil.getString(bpc.request, "result");
        if (StringUtil.isEmpty(pmtStatus)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, "prePayment");
            return;
        }
        String result = ParamUtil.getMaskedString(bpc.request, "result");

        if (!StringUtil.isEmpty(result)) {
            log.debug(StringUtil.changeForLog("payment result:" + result));
            String pmtRefNo = ParamUtil.getMaskedString(bpc.request, "reqRefNo");
            if ("success".equals(result) && !StringUtil.isEmpty(pmtRefNo)) {
                log.info("credit card payment success");
                switchValue = "ack";
                //update status
                List<AppSubmissionDto> appSubmissionDtos = (List<AppSubmissionDto>) ParamUtil.getSessionAttr(bpc.request, "appSubmissionDtos");
                String grpId = appSubmissionDtos.get(0).getAppGrpId();
                ApplicationGroupDto appGrp = new ApplicationGroupDto();
                appGrp.setId(grpId);
                appGrp.setPmtRefNo(pmtRefNo);
                appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                serviceConfigService.updatePaymentStatus(appGrp);
                bpc.request.setAttribute("createDate", new Date());
            }

            bpc.request.setAttribute("pmtRefNo", pmtRefNo);
        }
        ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, switchValue);
        log.debug(StringUtil.changeForLog("the do doPayment end ...."));
    }

    public void dashboard(BaseProcessClass bpc) {
        StringBuilder url = new StringBuilder();
        url.append("https://").append(bpc.request.getServerName()).append("/main-web/eservice/INTERNET/MohInternetInbox");
        String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
        try {
            bpc.response.sendRedirect(tokenUrl);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void doSubmit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doSubmit start ...."));
        List<LicenceDto> selectLicence = (List<LicenceDto>) bpc.request.getSession().getAttribute("licenceDtoList");
        if (selectLicence != null) {
            for (LicenceDto string : selectLicence) {
                List<ApplicationDto> applicationDtos = requestForChangeService.getAppByLicIdAndExcludeNew(string.getId());
                if (!IaisCommonUtils.isEmpty(applicationDtos)) {
                    ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "loading");
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePremisesEdit");
                    ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "ACKMESSAGE");
                    return;
                }
            }
        }
        AppSubmissionDto oldAppSubmissionDtoappSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, "oldAppSubmissionDto");
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);
        PremisesListQueryDto premisesListQueryDto = (PremisesListQueryDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.PREMISESLISTQUERYDTO);
        List<AppGrpPremisesDto> appGrpPremisesDtoList1 = appSubmissionDto.getAppGrpPremisesDtoList();
        boolean premise = false;

        List<AppGrpPremisesDto> oldAppSubmissionDtoappSubmissionDtoAppGrpPremisesDtoList = oldAppSubmissionDtoappSubmissionDto.getAppGrpPremisesDtoList();
        for (AppGrpPremisesDto appGrpPremisesDto : oldAppSubmissionDtoappSubmissionDtoAppGrpPremisesDtoList) {
            String certIssuedDtStr = appGrpPremisesDto.getCertIssuedDtStr();
            if (StringUtil.isEmpty(certIssuedDtStr)) {
                appGrpPremisesDto.setCertIssuedDtStr("");
            }
            appGrpPremisesDto.setLicenceDtos(null);
        }
        if (appGrpPremisesDtoList1.equals(oldAppSubmissionDtoappSubmissionDtoAppGrpPremisesDtoList)) {
            premise = true;
        }
        if (premise) {
            ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "ack");
            return;
        }
        String licenceId = appSubmissionDto.getLicenceId();
        if (!StringUtil.isEmpty(licenceId)) {
            List<ApplicationDto> applicationDtos = requestForChangeService.getAppByLicIdAndExcludeNew(licenceId);
            if (!IaisCommonUtils.isEmpty(applicationDtos)) {
                ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "ack");
                ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "There is  ongoing application for the licence");
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
        appSubmissionDto.setIsNeedNewLicNo(AppConsts.YES);

        //amount
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        amendmentFeeDto.setChangeInLicensee(Boolean.FALSE);
        boolean isSame = compareLocation(premisesListQueryDto, appSubmissionDto.getAppGrpPremisesDtoList().get(0));
        boolean b = compareHciName(premisesListQueryDto, appSubmissionDto.getAppGrpPremisesDtoList().get(0));
        amendmentFeeDto.setChangeInHCIName(!b);
        amendmentFeeDto.setChangeInLocation(!isSame);
        FeeDto feeDto = appSubmissionService.getGroupAmendAmount(amendmentFeeDto);
        Double total = feeDto.getTotal();
        //
        if (selectLicence != null) {
            String appGrpNo = requestForChangeService.getApplicationGroupNumber(appType);
            for (LicenceDto string : selectLicence) {
                LicenceDto licenceDto = requestForChangeService.getLicenceById(licenceId);
                boolean grpLic = licenceDto.isGrpLic();
                AppSubmissionDto appSubmissionDtoByLicenceId = requestForChangeService.getAppSubmissionDtoByLicenceId(string.getId());
                appSubmissionService.transform(appSubmissionDtoByLicenceId, appSubmissionDto.getLicenseeId());
                if (isSame) {
                    List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList();
                    if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                            appGrpPremisesDto.setNeedNewLicNo(Boolean.FALSE);
                        }
                    }
                }
                appSubmissionDtoByLicenceId.setAmount(total);
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
                appSubmissionDtoByLicenceId.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
                appSubmissionDtoByLicenceId.setAppGrpNo(appGrpNo);
                appSubmissionDtoByLicenceId.setIsNeedNewLicNo(AppConsts.YES);
                PreOrPostInspectionResultDto preOrPostInspectionResultDto = appSubmissionService.judgeIsPreInspection(appSubmissionDtoByLicenceId);
                if (preOrPostInspectionResultDto == null) {
                    appSubmissionDtoByLicenceId.setPreInspection(true);
                    appSubmissionDtoByLicenceId.setRequirement(true);
                } else {
                    appSubmissionDtoByLicenceId.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
                    appSubmissionDtoByLicenceId.setRequirement(preOrPostInspectionResultDto.isRequirement());
                }
                appSubmissionService.setRiskToDto(appSubmissionDto);
                appSubmissionDtoByLicenceId.setAutoRfc(isSame);
                //update status
         /*  LicenceDto licenceDto = new LicenceDto();
            licenceDto.setId(appSubmissionDto.getLicenceId());
            licenceDto.setStatus(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
            requestForChangeService.upDateLicStatus(licenceDto);*/

                AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
                appEditSelectDto.setServiceEdit(false);
                appEditSelectDto.setDocEdit(false);
                appEditSelectDto.setPoEdit(false);
                appEditSelectDto.setPremisesListEdit(true);
                appEditSelectDto.setPremisesEdit(true);
                appSubmissionDtoByLicenceId.setAppEditSelectDto(appEditSelectDto);
                appSubmissionDtoByLicenceId.setChangeSelectDto(appEditSelectDto);
                //save data
                appSubmissionDtoByLicenceId.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
                appSubmissionDtoByLicenceId.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
                String draftNo = appSubmissionDtoByLicenceId.getDraftNo();
                if (StringUtil.isEmpty(draftNo)) {
                    appSubmissionService.setDraftNo(appSubmissionDtoByLicenceId);
                }
                if (grpLic) {
                    List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDtoByLicenceId.getAppGrpPremisesDtoList();
                    if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                        for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                            appGrpPremisesDto.setGroupLicenceFlag(string.getId());
                        }
                    }
                }
                appSubmissionDtoByLicenceId.setGroupLic(grpLic);
                appSubmissionDtoByLicenceId.setPartPremise(grpLic);
                if (0.0 == total) {
                    appSubmissionDtoByLicenceId.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
                }
                appSubmissionDtoByLicenceId.setGetAppInfoFromDto(true);
                oldPremiseToNewPremise(appSubmissionDtoByLicenceId);
                requestForChangeService.svcDocToPresmise(appSubmissionDtoByLicenceId);
                appSubmissionDtos.add(appSubmissionDtoByLicenceId);
            }

        }

        if (isSame) {
            ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "loading");
        } else {
            ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "loading");
        }
        String submissionId = generateIdClient.getSeqId().getEntity();
        AppSubmissionListDto appSubmissionListDto = new AppSubmissionListDto();
        Long l = System.currentTimeMillis();
        appSubmissionListDto.setEventRefNo(l.toString());
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
                            if (premisesIndexNo != premiseVal) {
                                appSvcLaboratoryDisciplinesDto.setPremiseVal(premisesIndexNo);
                            }
                        }

                    }
                }
            }
        }

    }

    private AmendmentFeeDto getAmendmentFeeDto(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto) {
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        boolean changeHciName = compareHciName(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
        boolean changeLocation = compareLocation(appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSubmissionDto.getAppGrpPremisesDtoList());
        amendmentFeeDto.setChangeInLicensee(Boolean.FALSE);
        amendmentFeeDto.setChangeInHCIName(!changeHciName);
        amendmentFeeDto.setChangeInLocation(!changeLocation);
        return amendmentFeeDto;
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


    private boolean compareLocation(List<AppGrpPremisesDto> appGrpPremisesDtos, List<AppGrpPremisesDto> oldAppGrpPremisesDtos) {
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if (length == oldLength) {
            for (int i = 0; i < length; i++) {
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if (!appGrpPremisesDto.getAddress().equals(oldAppGrpPremisesDto.getAddress())) {
                    return false;
                }
            }
        }
        //is same
        return true;
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
        appSubmissionDto = appSubmissionService.submitRequestInformation(appSubmissionRequestInformationDto, bpc.process);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, appSubmissionDto);

        ParamUtil.setRequestAttr(bpc.request, "isrfiSuccess", "Y");
        ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "The request for information save success");

        log.debug(StringUtil.changeForLog("the do doRequestForInformationSubmit end ...."));
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

    private String getPremisesVal(PremisesListQueryDto premisesListQueryDto) {
        String premisesVal = "";
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesListQueryDto.getPremisesType())) {
            premisesVal = premisesListQueryDto.getHciName();
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesListQueryDto.getPremisesType())) {
            premisesVal = premisesListQueryDto.getVehicleNo();
        }
        return premisesVal;
    }

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
        String oldAddress = premisesListQueryDto.getAddress();
        String newAddress = appGrpPremisesDto.getAddress();
        if (!oldAddress.equals(newAddress)) {
            return false;
        }
        return true;
    }

    private boolean compareHciName(PremisesListQueryDto premisesListQueryDto, AppGrpPremisesDto appGrpPremisesDto) {

        String newHciName = "";
        String oldHciName = "";
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesListQueryDto.getPremisesType())) {
            oldHciName = appGrpPremisesDto.getHciName();
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesListQueryDto.getPremisesType())) {
            oldHciName = appGrpPremisesDto.getConveyanceVehicleNo();
        }
        if (ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(premisesListQueryDto.getPremisesType())) {
            newHciName = appGrpPremisesDto.getHciName();
        } else if (ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(premisesListQueryDto.getPremisesType())) {
            newHciName = appGrpPremisesDto.getConveyanceVehicleNo();
        }
        if (!oldHciName.equals(newHciName)) {
            return false;
        }

        return true;
    }

    private AppSubmissionDto setPersonnelDate(AppSubmissionDto appSubmissionDto, PersonnelListDto personnelListDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            List<AppSvcCgoDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcCgoDtos)) {
                for (AppSvcCgoDto appSvcCgoDto : appSvcCgoDtos) {
                    if (personnelListDto.getIdNo().equals(appSvcCgoDto.getIdNo())) {
                        appSvcCgoDto.setEmailAddr(personnelListDto.getEmailAddr());
                        appSvcCgoDto.setMobileNo(personnelListDto.getMobileNo());
                        appSvcCgoDto.setDesignation(personnelListDto.getDesignation());
                        appSvcCgoDto.setProfRegNo(personnelListDto.getProfessionRegnNo());
                        appSvcCgoDto.setProfessionType(personnelListDto.getProfessionType());
                        appSvcCgoDto.setSpeciality(personnelListDto.getSpeciality());
                        appSvcCgoDto.setSubSpeciality(personnelListDto.getSubSpeciality());
                    }
                }
            }
            List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)) {
                for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtos) {
                    if (personnelListDto.getIdNo().equals(appSvcPrincipalOfficersDto.getIdNo())) {
                        appSvcPrincipalOfficersDto.setEmailAddr(personnelListDto.getEmailAddr());
                        appSvcPrincipalOfficersDto.setMobileNo(personnelListDto.getMobileNo());
                        appSvcPrincipalOfficersDto.setDesignation(personnelListDto.getDesignation());
                        appSvcPrincipalOfficersDto.setOfficeTelNo(personnelListDto.getOfficeTelNo());
                    }
                }
            }
            List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
            if (appSvcMedAlertPersonList != null) {
                for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcMedAlertPersonList) {
                    if (personnelListDto.getIdNo().equals(appSvcPrincipalOfficersDto.getIdNo())) {
                        appSvcPrincipalOfficersDto.setMobileNo(personnelListDto.getMobileNo());
                        appSvcPrincipalOfficersDto.setEmailAddr(personnelListDto.getEmailAddr());
                    }
                }
            }
        }
        return appSubmissionDto;
    }

    private AppSubmissionDto replacePersonnelDate(AppSubmissionDto appSubmissionDto, PersonnelListDto personnelListDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            //cgo
            List<AppSvcCgoDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcCgoDtos)) {
                for (AppSvcCgoDto appSvcCgoDto : appSvcCgoDtos) {
                    appSvcCgoDto.setIdNo(personnelListDto.getIdNo());
                    appSvcCgoDto.setIdType(personnelListDto.getIdType());
                    appSvcCgoDto.setName(personnelListDto.getPsnName());
                    appSvcCgoDto.setSalutation(personnelListDto.getSalutation());
                    appSvcCgoDto.setEmailAddr(personnelListDto.getEmailAddr());
                    appSvcCgoDto.setMobileNo(personnelListDto.getMobileNo());
                    appSvcCgoDto.setDesignation(personnelListDto.getDesignation());
                    appSvcCgoDto.setProfRegNo(personnelListDto.getProfessionRegnNo());
                    appSvcCgoDto.setProfessionType(personnelListDto.getProfessionType());
                    appSvcCgoDto.setSpeciality(personnelListDto.getSpeciality());
                    appSvcCgoDto.setSubSpeciality(personnelListDto.getSubSpeciality());
                }
            }
            //po Dpo
            List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)) {
                for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtos) {
                    appSvcPrincipalOfficersDto.setIdNo(personnelListDto.getIdNo());
                    appSvcPrincipalOfficersDto.setIdType(personnelListDto.getIdType());
                    appSvcPrincipalOfficersDto.setName(personnelListDto.getPsnName());
                    appSvcPrincipalOfficersDto.setSalutation(personnelListDto.getSalutation());
                    appSvcPrincipalOfficersDto.setEmailAddr(personnelListDto.getEmailAddr());
                    appSvcPrincipalOfficersDto.setMobileNo(personnelListDto.getMobileNo());
                    appSvcPrincipalOfficersDto.setDesignation(personnelListDto.getDesignation());
                    appSvcPrincipalOfficersDto.setOfficeTelNo(personnelListDto.getOfficeTelNo());
                }
            }
            //MAP
            List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = appSvcRelatedInfoDto.getAppSvcMedAlertPersonList();
            if (!IaisCommonUtils.isEmpty(appSvcMedAlertPersonList)) {
                for (AppSvcPrincipalOfficersDto appSvcMedAlertPerson : appSvcMedAlertPersonList) {
                    appSvcMedAlertPerson.setIdNo(personnelListDto.getIdNo());
                    appSvcMedAlertPerson.setIdType(personnelListDto.getIdType());
                    appSvcMedAlertPerson.setName(personnelListDto.getPsnName());
                    appSvcMedAlertPerson.setSalutation(personnelListDto.getSalutation());
                    appSvcMedAlertPerson.setMobileNo(personnelListDto.getMobileNo());
                    appSvcMedAlertPerson.setEmailAddr(personnelListDto.getEmailAddr());
                }
            }
        }
        return appSubmissionDto;
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