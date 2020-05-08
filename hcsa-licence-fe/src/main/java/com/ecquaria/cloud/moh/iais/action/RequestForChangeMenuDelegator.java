package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.RedirectUtil;
import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.RequestForChangeService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

    private FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(PersonnelListQueryDto.class)
            .searchAttr("PersonnelSearchParam")
            .resultAttr("PersonnelSearchResult")
            .sortField("NAME").sortType(SearchParam.ASCENDING).build();
    @Autowired
    RequestForChangeService requestForChangeService;
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private ServiceConfigService serviceConfigService;


    /**
     * @param bpc
     * @Decription start
     */
    public void start(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do start start ...."));
        String appNo = ParamUtil.getMaskedString(bpc.request, "appNo");
        AuditTrailHelper.auditFunction("hcsa-application", "hcsa application");

        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, null);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, null);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG, null);
        ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.OLDAPPSUBMISSIONDTO, null);
        requestForInformation(bpc, appNo);

        log.debug(StringUtil.changeForLog("the do start end ...."));
    }

    /**
     * @param bpc
     * @Decription personnleListStart
     */
    public void personnleListStart(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do personnleListStart start ...."));
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
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, action);
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
        if ("dosubmit".equals(switchValue)) {
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
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        List<PremisesListQueryDto> premisesDtos = requestForChangeService.getPremisesList(licenseeId);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.PREMISESLISTDTOS, (Serializable) premisesDtos);
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

    }

    public void doPage(BaseProcessClass bpc) {

    }

    public void doSearch(BaseProcessClass bpc) {

    }

    /**
     * @param bpc
     * @Decription preparePremisesEdit
     */
    public void preparePremisesEdit(BaseProcessClass bpc) {
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
            if (rfi == null) {
                List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
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
        appSubmissionDto.setAppGrpPremisesDtoList(reloadPremisesDtoList);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        ParamUtil.setRequestAttr(bpc.request, RfcConst.RELOADPREMISES, reloadPremisesDtoList);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, appSubmissionDto);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE, "Premises Amendment");
        log.debug(StringUtil.changeForLog("the do preparePremisesEdit end ...."));
    }


    /**
     * @param bpc
     * @Decription doPremisesList
     */
    public void doPremisesList(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do doPremisesList start ...."));
       /*     String crudActionType = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE);
        if(StringUtil.isEmpty(crudActionType)){
            crudActionType = "back";
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, crudActionType);
            return;
        }*/
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
                    appSubmissionDto = requestForChangeService.getAppSubmissionDtoByLicenceId(premisesListQueryDto.getLicenceId(),null);
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

        if (!ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(status)) {
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
        Map<String, String> errorMap = NewApplicationDelegator.doValidatePremiss(bpc);

        if (errorMap.size() > 0) {
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePremisesEdit");
            return;
        }
        ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "dosubmit");
        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, appSubmissionDto);
        //test
        //ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM_VALUE, "prePremisesEdit");
        log.debug(StringUtil.changeForLog("the do doPremisesEdit end ...."));
    }


    /**
     * @param bpc
     * @Decription preparePersonnel  personnel List entrance
     */
    public void preparePersonnel(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the preparePersonnel start ...."));
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        SearchResult personnelSearchResult = (SearchResult)ParamUtil.getSessionAttr(bpc.request, "PersonnelSearchResult");
        if(personnelSearchResult!=null){
            List<String> psnTypes = (List<String>)ParamUtil.getSessionAttr(bpc.request, "psnTypes");
            SearchParam searchParam = IaisEGPHelper.getSearchParam(bpc.request, true, filterParameter);
            SqlHelper.builderInSql(searchParam, "T2.PSN_TYPE", "psnType",psnTypes);
            QueryHelp.setMainSql("applicationPersonnelQuery", "appPersonnelQuery", searchParam);
            SearchResult searchResult = requestForChangeService.psnDoQuery(searchParam);
            List<PersonnelListQueryDto> licenseeKeyApptPersonDtoList = searchResult.getRows();
            Map<String, List<PersonnelListQueryDto>> personnelListMap = IaisCommonUtils.genNewHashMap();
            for (PersonnelListQueryDto personnelListQueryDto : licenseeKeyApptPersonDtoList) {
                String idNo = personnelListQueryDto.getIdNo();
                List<PersonnelListQueryDto> personnelListQueryDtos = personnelListMap.get(idNo);
                if (IaisCommonUtils.isEmpty(personnelListQueryDtos)) {
                    personnelListQueryDtos = IaisCommonUtils.genNewArrayList();
                }
                personnelListQueryDtos.add(personnelListQueryDto);
                personnelListMap.put(idNo, personnelListQueryDtos);
            }
            List<SelectOption> personelRoles = getPsnType();
            ParamUtil.setRequestAttr(bpc.request, "PersonnelRoleList", personelRoles);
            ParamUtil.setSessionAttr(bpc.request, RfcConst.PERSONNELLISTMAP, (Serializable) personnelListMap);
            ParamUtil.setRequestAttr(bpc.request, "PersonnelSearchResult", personnelSearchResult);
            ParamUtil.setSessionAttr(bpc.request, "PersonnelSearchParam", searchParam);
            return;
        }


        SearchParam searchParam = SearchResultHelper.getSearchParam(bpc.request, filterParameter, true);
        searchParam.addFilter("licenseeId", licenseeId, true);
        QueryHelp.setMainSql("applicationPersonnelQuery", "appPersonnelQuery", searchParam);
        SearchResult searchResult = requestForChangeService.psnDoQuery(searchParam);
        if (!StringUtil.isEmpty(searchResult)) {
            ParamUtil.setSessionAttr(bpc.request, "PersonnelSearchParam", searchParam);
            ParamUtil.setRequestAttr(bpc.request, "PersonnelSearchResult", searchResult);
        }
        List<PersonnelListQueryDto> licenseeKeyApptPersonDtoList = searchResult.getRows();
        Map<String, List<PersonnelListQueryDto>> personnelListMap = IaisCommonUtils.genNewHashMap();
        for (PersonnelListQueryDto personnelListQueryDto : licenseeKeyApptPersonDtoList) {
            String idNo = personnelListQueryDto.getIdNo();
            List<PersonnelListQueryDto> personnelListQueryDtos = personnelListMap.get(idNo);
            if (IaisCommonUtils.isEmpty(personnelListQueryDtos)) {
                personnelListQueryDtos = IaisCommonUtils.genNewArrayList();
            }
            personnelListQueryDtos.add(personnelListQueryDto);
            personnelListMap.put(idNo, personnelListQueryDtos);
        }
        List<SelectOption> personelRoles = getPsnType();
        ParamUtil.setRequestAttr(bpc.request, "PersonnelRoleList", personelRoles);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.PERSONNELLISTMAP, (Serializable) personnelListMap);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE, "PersonnelList");
        log.debug(StringUtil.changeForLog("the do preparePersonnelList end ...."));
        log.debug(StringUtil.changeForLog("the preparePersonnel end ...."));
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
        String psnType = ParamUtil.getRequestString(bpc.request, "psnTypes");
        //ParamUtil.setRequestAttr(bpc.request, "psnTypes", psnTypes);
        List<String> psnTypes = IaisCommonUtils.genNewArrayList();
        if(StringUtil.isEmpty(psnType)){
            psnTypes.add("PO");
            psnTypes.add("DPO");
            psnTypes.add("CGO");
        }else {
            psnTypes.add(psnType);
        }

        SearchParam searchParam = IaisEGPHelper.getSearchParam(bpc.request, true, filterParameter);
        SqlHelper.builderInSql(searchParam, "T2.PSN_TYPE", "psnType",psnTypes);
        QueryHelp.setMainSql("applicationPersonnelQuery", "appPersonnelQuery", searchParam);
        SearchResult searchResult = requestForChangeService.psnDoQuery(searchParam);
        ParamUtil.setSessionAttr(bpc.request, "PersonnelSearchParam", searchParam);
        ParamUtil.setSessionAttr(bpc.request, "PersonnelSearchResult", searchResult);
        ParamUtil.setSessionAttr(bpc.request, "psnType", psnType);
        ParamUtil.setSessionAttr(bpc.request, "psnTypes", (Serializable) psnTypes);
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
        String hiddenIndex = ParamUtil.getString(bpc.request,"hiddenIndex");
        String idNo = ParamUtil.getMaskedString(bpc.request,"personnelNo"+hiddenIndex);
        Map<String,List<PersonnelListQueryDto>> personnelListMap  = (Map<String, List<PersonnelListQueryDto>>) ParamUtil.getSessionAttr(bpc.request,RfcConst.PERSONNELLISTMAP);
        List<PersonnelListQueryDto> personnelEditList = personnelListMap.get(idNo);
        ParamUtil.setSessionAttr(bpc.request,RfcConst.PERSONNELEDITLIST, (Serializable) personnelEditList);
        log.debug(StringUtil.changeForLog("the do doPersonnelList end ...."));
        List<SelectOption> idTypeSelectList = NewApplicationHelper.getIdTypeSelOp();
        ParamUtil.setRequestAttr(bpc.request, ClinicalLaboratoryDelegator.DROPWOWN_IDTYPESELECT, idTypeSelectList);
        ParamUtil.setSessionAttr(bpc.request, RfcConst.PERSONNELEDITLIST, (Serializable) personnelEditList);
        ParamUtil.setRequestAttr(bpc.request, HcsaLicenceFeConstant.DASHBOARDTITLE, "Personnel Amendment");
        log.debug(StringUtil.changeForLog("the do preparePersonnelEdit end ...."));
    }


    /**
     * @param bpc
     * @Decription doPersonnelEdit
     */
    public void doPersonnelEdit(BaseProcessClass bpc) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("the do doPersonnelEdit start ...."));
        List<PersonnelListQueryDto> personnelEditList = (List<PersonnelListQueryDto>) ParamUtil.getSessionAttr(bpc.request, RfcConst.PERSONNELEDITLIST);
        String email = ParamUtil.getString(bpc.request, "emailAddress");
        String mobile = ParamUtil.getString(bpc.request, "mobileNo");
        String designation = ParamUtil.getString(bpc.request, "designation");
        String professionType = ParamUtil.getString(bpc.request, "professionType");
        String professionRegnNo = ParamUtil.getString(bpc.request, "professionRegnNo");
        PersonnelListQueryDto personnelListQueryDto = personnelEditList.get(0);
        personnelListQueryDto.setEmailAddr(email);
        personnelListQueryDto.setMobileNo(mobile);
        personnelListQueryDto.setDesignation(designation);
        personnelListQueryDto.setProfessionType(professionType);
        personnelListQueryDto.setProfessionRegnNo(professionRegnNo);
        for (PersonnelListQueryDto item : personnelEditList) {
            String licenceId = item.getLicenceId();
            if (!StringUtil.isEmpty(licenceId)) {
                List<ApplicationDto> applicationDtos = requestForChangeService.getAppByLicIdAndExcludeNew(licenceId);
                if (!IaisCommonUtils.isEmpty(applicationDtos)) {
                    ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "loading");
                    ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preAck");
                    ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "There is  ongoing application for the licence");
                    return;
                }
            }
        }
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
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
        if (StringUtil.isEmpty(professionType)) {
            errMap.put("professionType", "UC_CHKLMD001_ERR001");
        }
        if (StringUtil.isEmpty(designation)) {
            errMap.put("designation", "UC_CHKLMD001_ERR001");
        }
        if (StringUtil.isEmpty(professionRegnNo)) {
            errMap.put("professionRegnNo", "UC_CHKLMD001_ERR001");
        }
        if (!errMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "loading");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, "preparePersonnelEdit");
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            return;
        }
        List<String> licenceIds = IaisCommonUtils.genNewArrayList();
        for (PersonnelListQueryDto item : personnelEditList) {
            licenceIds.add(item.getLicenceId());
        }
        List<AppSubmissionDto> appSubmissionDtos = requestForChangeService.getAppSubmissionDtoByLicenceIds(licenceIds,null);
        String appGroupNo = requestForChangeService.getApplicationGroupNumber(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        List<String> names = IaisCommonUtils.genNewArrayList();
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
            if (appSubmissionDto != null) {
                appSubmissionDto.setLicenseeId(licenseeId);
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
        }
        List<HcsaServiceDto> hcsaServiceDtoList = serviceConfigService.getHcsaServiceByNames(names);
        ParamUtil.setSessionAttr(bpc.request, AppServicesConsts.HCSASERVICEDTOLIST, (Serializable) hcsaServiceDtoList);
        for (AppSubmissionDto appSubmissionDto : appSubmissionDtos) {
            NewApplicationHelper.setSubmissionDtoSvcData(bpc.request, appSubmissionDto);
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
            appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
            appSubmissionDto.setAppGrpNo(appGroupNo);
            appSubmissionDto.setAmount(0.0);
            appSubmissionDto.setAutoRfc(true);
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
            appSubmissionDto = setPersonnelDate(appSubmissionDto, personnelListQueryDto);
        }
        requestForChangeService.saveAppsBySubmissionDtos(appSubmissionDtos);
        log.debug(StringUtil.changeForLog("the do doPersonnelEdit end ...."));
    }


    /**
     * @param bpc
     * @Decription prepareAckPage
     */
    public void selectLicence(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do selectLicence start ...."));
        PremisesListQueryDto premisesListQueryDto = (PremisesListQueryDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.PREMISESLISTQUERYDTO);
        String premisesId = premisesListQueryDto.getPremisesId();
      /*  List<LicenceDto> licenceDtoList = requestForChangeService.getLicenceDtoByPremisesId(premisesId);
        bpc.request.setAttribute("licenceDtoList",licenceDtoList);
*/
        log.debug(StringUtil.changeForLog("the do selectLicence end ...."));
    }


    public void prepareAckPage(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prepareAckPage start ...."));
        String[] licenceNames = bpc.request.getParameterValues("licenceName");
        if (licenceNames != null) {
            for (String string : licenceNames) {
                if (string.startsWith("G")) {


                } else if (string.startsWith("L")) {


                }

            }
        }

        log.debug(StringUtil.changeForLog("the do prepareAckPage end ...."));
    }

    private ApplicationGroupDto getApplicationGroupDto() {
        ApplicationGroupDto applicationGroupDto = new ApplicationGroupDto();


        return applicationGroupDto;
    }

    /**
     * @param bpc
     * @Decription prePayment
     */
    public void prePayment(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do prePayment start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);
        if (!StringUtil.isEmpty(appSubmissionDto.getAmount())) {
            DecimalFormat decimalFormat = new DecimalFormat("###,###");
            String amountStr = "$" + decimalFormat.format(appSubmissionDto.getAmount());
            appSubmissionDto.setAmountStr(amountStr);
        }
        log.debug(StringUtil.changeForLog("the do prePayment end ...."));
    }

    /**
     * @param bpc
     * @Decription jumpBank
     */
    public void jumpBank(BaseProcessClass bpc) throws IOException {
        log.debug(StringUtil.changeForLog("the do jumpBank start ...."));
        String payMethod = ParamUtil.getString(bpc.request, "payMethod");
        if (StringUtil.isEmpty(payMethod)) {
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE_FORM, "prePayment");
            return;
        }
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);

        if ("Credit".equals(payMethod)) {
            String backUrl = "hcsa-licence-web/eservice/INTERNET/MohRfcPermisesList/1/doPayment";
            StringBuffer url = new StringBuffer();
            url.append("https://").append(bpc.request.getServerName())
                    .append("/payment-web/eservice/INTERNET/PaymentRequest")
                    .append("?amount=").append(MaskUtil.maskValue("amount", String.valueOf(appSubmissionDto.getAmount())))
                    .append("&payMethod=").append(MaskUtil.maskValue("payMethod", payMethod))
                    .append("&reqNo=").append(MaskUtil.maskValue("reqNo", appSubmissionDto.getAppGrpNo()))
                    .append("&backUrl=").append(MaskUtil.maskValue("backUrl", backUrl));
            String tokenUrl = RedirectUtil.changeUrlToCsrfGuardUrlUrl(url.toString(), bpc.request);
            bpc.response.sendRedirect(tokenUrl);
            return;
        } else if ("GIRO".equals(payMethod)) {
            String appGrpId = appSubmissionDto.getAppGrpId();
            ApplicationGroupDto appGrp = new ApplicationGroupDto();
            appGrp.setId(appGrpId);
            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_GIRO);
            serviceConfigService.updatePaymentStatus(appGrp);
            ParamUtil.setRequestAttr(bpc.request, "PmtStatus", "GIRO");
        }

        log.debug(StringUtil.changeForLog("the do jumpBank end ...."));
    }

    /**
     * @param bpc
     * @Decription toBank
     */
    public void toBank(BaseProcessClass bpc) {

    }


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
                AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);
                String appGrpId = appSubmissionDto.getAppGrpId();
                ApplicationGroupDto appGrp = new ApplicationGroupDto();
                appGrp.setId(appGrpId);
                appGrp.setPmtRefNo(pmtRefNo);
                appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                serviceConfigService.updatePaymentStatus(appGrp);
            }
        }


        ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, switchValue);
        log.debug(StringUtil.changeForLog("the do doPayment end ...."));
    }


    /**
     * @param bpc
     * @Decription doSubmit
     */
    public void doSubmit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);
        PremisesListQueryDto premisesListQueryDto = (PremisesListQueryDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.PREMISESLISTQUERYDTO);
        String licenceId = appSubmissionDto.getLicenceId();
        if (!StringUtil.isEmpty(licenceId)) {
            List<ApplicationDto> applicationDtos = requestForChangeService.getAppByLicIdAndExcludeNew(licenceId);
            if (!IaisCommonUtils.isEmpty(applicationDtos)) {
                ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "ack");
                ParamUtil.setRequestAttr(bpc.request, ACKMESSAGE, "There is  ongoing application for the licence");
                log.info(StringUtil.changeForLog("There is  ongoing application for the licence"));
                return;
            }
        }


        String appType = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE;
        if (!StringUtil.isEmpty(appSubmissionDto.getAppType())) {
            appType = appSubmissionDto.getAppType();
        }
        String appGroupNo = requestForChangeService.getApplicationGroupNumber(appType);
        appSubmissionDto.setAppGrpNo(appGroupNo);
        appSubmissionDto.setIsNeedNewLicNo(AppConsts.YES);

        //amount
        AmendmentFeeDto amendmentFeeDto = new AmendmentFeeDto();
        amendmentFeeDto.setChangeInLicensee(false);
        amendmentFeeDto.setChangeInHCIName(false);
        boolean isSame = compareLocation(premisesListQueryDto, appSubmissionDto.getAppGrpPremisesDtoList().get(0));
        if (isSame) {
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                for (AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtos) {
                    appGrpPremisesDto.setNeedNewLicNo(false);
                }
            }
        }
        amendmentFeeDto.setChangeInLocation(!isSame);
        //

        FeeDto feeDto = appSubmissionService.getGroupAmendAmount(amendmentFeeDto);
        if (feeDto != null) {
            appSubmissionDto.setAmount(feeDto.getTotal());
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

        appSubmissionDto.setAutoRfc(isSame);
       /* //update status
        LicenceDto licenceDto = new LicenceDto();
        licenceDto.setId(appSubmissionDto.getLicenceId());
        licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_REQUEST_FOR_CHANGE);
        requestForChangeService.upDateLicStatus(licenceDto);*/

        AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
        appEditSelectDto.setPremisesListEdit(true);
        appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        appSubmissionDto.setChangeSelectDto(appEditSelectDto);
        //save data
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);

        appSubmissionDto = appSubmissionService.submitRequestChange(appSubmissionDto, bpc.process);

        ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, appSubmissionDto);
        if (isSame) {
            ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "ack");
        } else {
            ParamUtil.setRequestAttr(bpc.request, RfcConst.SWITCH_VALUE, "loading");
        }


        log.debug(StringUtil.changeForLog("the do doSubmit end ...."));

    }

    /**
     * @param bpc
     * @Decription doRequestForInformationSubmit
     */
    public void doRequestForInformationSubmit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do doRequestForInformationSubmit start ...."));
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO);
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, RfcConst.OLDAPPSUBMISSIONDTO);

        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        oldAppSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppSubmissionRequestInformationDto appSubmissionRequestInformationDto = new AppSubmissionRequestInformationDto();
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

    private AppSubmissionDto setPersonnelDate(AppSubmissionDto appSubmissionDto, PersonnelListQueryDto personnelListQueryDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            List<AppSvcCgoDto> appSvcCgoDtos = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcCgoDtos)) {
                for (AppSvcCgoDto appSvcCgoDto : appSvcCgoDtos) {
                    if (personnelListQueryDto.getIdNo().equals(appSvcCgoDto.getIdNo())) {
                        appSvcCgoDto.setEmailAddr(personnelListQueryDto.getEmailAddr());
                        appSvcCgoDto.setMobileNo(personnelListQueryDto.getMobileNo());
                   /* appSvcCgoDto.setDesignation(personnelListQueryDto.getDesignation());
                    appSvcCgoDto.setProfessionType(personnelListQueryDto.getProfessionType());
                    appSvcCgoDto.setProfessionRegoNo(personnelListQueryDto.getProfessionRegnNo());*/
                    }
                }
            }


            List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtos = appSvcRelatedInfoDto.getAppSvcPrincipalOfficersDtoList();
            if (!IaisCommonUtils.isEmpty(appSvcPrincipalOfficersDtos)) {
                for (AppSvcPrincipalOfficersDto appSvcPrincipalOfficersDto : appSvcPrincipalOfficersDtos) {
                    if (personnelListQueryDto.getIdNo().equals(appSvcPrincipalOfficersDto.getIdNo())) {
                        appSvcPrincipalOfficersDto.setEmailAddr(personnelListQueryDto.getEmailAddr());
                        appSvcPrincipalOfficersDto.setMobileNo(personnelListQueryDto.getMobileNo());
                        appSvcPrincipalOfficersDto.setDesignation(personnelListQueryDto.getDesignation());
                    }

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
                appSubmissionDto.setNeedEditController(true);
                for (AppGrpPremisesDto appGrpPremisesDto1 : appSubmissionDto.getAppGrpPremisesDtoList()) {
                    NewApplicationHelper.setWrkTime(appGrpPremisesDto1);
                }

                AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) CopyUtil.copyMutableObject(appSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request, RfcConst.APPSUBMISSIONDTO, appSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request, RfcConst.OLDAPPSUBMISSIONDTO, oldAppSubmissionDto);
                ParamUtil.setSessionAttr(bpc.request, NewApplicationDelegator.REQUESTINFORMATIONCONFIG, "test");
            }


        }
        log.debug(StringUtil.changeForLog("the do requestForInformationLoading end ...."));
    }

}
