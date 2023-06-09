package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import com.ecquaria.cloud.moh.iais.service.RequestForInformationService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Delegator(value = "changeTcuDateDelegator")
@Slf4j
public class ChangeTcuDateDelegator {
    private static final String keyFilterParam = "filterParam";
    private static final String keyTcuDateFrom = "tcuDateFrom";
    private static final String keyTcuDateTo = "tcuDateTo";
    private static final String keyPsnTypeOptions = "psnTypeOptions";
    private static final String keyPsnName = "psn_name";
    private static final String keyPsnType = "psn_type";
    private static final String PARAM_CHKL_ITEM_CHECKBOX = "itemCheckbox";
    private static final String keyNewTcuDateDates = "newTcuDate";
    private static final String keyNewTcuDateRemarks = "newTcuDateRemarks";
    private static final String ACTION_PREMISE_LIST = "premiseList";
    private static final String ACTION_CHANGE_TCU_DATE = "changeTCUDate";
    private static final String ACTION_SUBMIT = "submit";

    private final String CRUD_TYPE_TCU = "crud_type_tcu";

    private final LicenceService licenceService;
    private final RequestForInformationService requestForInformationService;
    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(LicPremisesQueryDto.class)
            .searchAttr(HcsaLicenceBeConstant.SEARCH_PARAM_CHANGE_TUC_DATE)
            .resultAttr(HcsaLicenceBeConstant.SEARCH_RESULT_CHANGE_TUC_DATE)
            .sortField("tcu_date").sortType(SearchParam.ASCENDING).build();



    @Autowired
    public ChangeTcuDateDelegator(LicenceService licenceService, RequestForInformationService requestForInformationService) {
        this.licenceService = licenceService;
        this.requestForInformationService = requestForInformationService;
    }

    public void start(BaseProcessClass bpc) {
        HttpSession session = bpc.getSession();

        session.removeAttribute(HcsaLicenceBeConstant.SEARCH_PARAM_CHANGE_TUC_DATE);
        session.removeAttribute(HcsaLicenceBeConstant.SEARCH_RESULT_CHANGE_TUC_DATE);
        session.removeAttribute(keyFilterParam);
        session.removeAttribute(keyTcuDateFrom);
        session.removeAttribute(keyTcuDateTo);
        session.removeAttribute(keyPsnName);
        session.removeAttribute(keyPsnType);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_AUDIT_INSPECTION, AuditTrailConsts.FUNCTION_CHANGE_TCU_DATE);
        ParamUtil.setSessionAttr(bpc.request, HcsaLicenceBeConstant.KEY_SVC_TYPE_OPTIONS, (Serializable) requestForInformationService.getLicSvcTypeOption());
        List<SelectOption> persionTypeOptions = IaisCommonUtils.genNewArrayList();
        persionTypeOptions.add(new SelectOption("CGO","Clinical Governance Officer"));
        persionTypeOptions.add(new SelectOption("KAH","Key Appointment Holder"));
        persionTypeOptions.add(new SelectOption("PO","Principal Officer"));
        ParamUtil.setSessionAttr(bpc.request, keyPsnTypeOptions, (Serializable) persionTypeOptions);
    }

    public void preSwitch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String action = ParamUtil.getRequestString(request, CRUD_TYPE_TCU);
        if (StringUtil.isEmpty(action)) {
            action = ACTION_PREMISE_LIST;
            ParamUtil.setRequestAttr(request, CRUD_TYPE_TCU, action);
        }
        log.info("----- preSwitch action:{} -----", action);
    }

    public void prePremiseList(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("changeTCUDate", "listLicenceInfo", searchParam);
        SearchResult<LicPremisesQueryDto> searchResult = licenceService.searchLicencesInChangeTCUDate(searchParam);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_PARAM_CHANGE_TUC_DATE, searchParam);
        ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_RESULT_CHANGE_TUC_DATE, searchResult);

        LicPremisesQueryDto filterParam = (LicPremisesQueryDto) ParamUtil.getSessionAttr(request, keyFilterParam);
        if (filterParam == null) {
            filterParam = new LicPremisesQueryDto();
            ParamUtil.setSessionAttr(request, keyFilterParam, filterParam);
        }
    }

    public void premiseListAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String action = ParamUtil.getRequestString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if (ACTION_CHANGE_TCU_DATE.equals(action)) {
            String[] checkBoxItemIds = ParamUtil.getStrings(request, PARAM_CHKL_ITEM_CHECKBOX);
            if (checkBoxItemIds == null || checkBoxItemIds.length == 0) {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("premiseItems", MessageUtil.getMessageDesc("GENERAL_ERR0067")));
                action = ACTION_PREMISE_LIST;
            } else {
                SearchResult<LicPremisesQueryDto> searchResult = (SearchResult<LicPremisesQueryDto>) ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.SEARCH_RESULT_CHANGE_TUC_DATE);
                List<LicPremisesQueryDto> checkedLicPremisesQueryDtos = searchResult
                        .getRows()
                        .stream()
                        .filter(licPremisesQueryDto ->
                                Arrays.stream(checkBoxItemIds).anyMatch(checkBoxItemId -> checkBoxItemId.equals(licPremisesQueryDto.getLicenceId()))
                        ).collect(Collectors.toList());
                searchResult.setRows(checkedLicPremisesQueryDtos);
                action = ACTION_CHANGE_TCU_DATE;
                ParamUtil.setSessionAttr(request, HcsaLicenceBeConstant.SEARCH_RESULT_CHANGE_TUC_DATE, searchResult);
            }
        }
        ParamUtil.setRequestAttr(request, CRUD_TYPE_TCU, action);
        log.info("----- premiseListAction action:{} -----", action);
    }

    public void preChangeTCUDate(BaseProcessClass bpc) {
        log.info(StringUtil.changeForLog("preChangeTCUDate-->"));
    }

    @SneakyThrows
    public void changeTCUDateAction(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String action = ParamUtil.getRequestString(request, IaisEGPConstant.CRUD_ACTION_TYPE);

        if (ACTION_SUBMIT.equals(action)) {
            SearchResult<LicPremisesQueryDto> searchResult = (SearchResult<LicPremisesQueryDto>) ParamUtil.getSessionAttr(request, HcsaLicenceBeConstant.SEARCH_RESULT_CHANGE_TUC_DATE);
            int size = searchResult.getRows().size();
            List<String> newTcuDates = IaisCommonUtils.genNewArrayList(size);
            List<String> newTcuDateRemarks = IaisCommonUtils.genNewArrayList(size);

            Map<String, String> errMap = validateChangeTCUDate(request, size, newTcuDates, newTcuDateRemarks);

            ParamUtil.setRequestAttr(request, keyNewTcuDateDates, newTcuDates);
            ParamUtil.setRequestAttr(request, keyNewTcuDateRemarks, newTcuDateRemarks);

            if (!errMap.isEmpty()) {
                action = ACTION_CHANGE_TCU_DATE;
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            } else {
                saveNewTCUDate(searchResult, size, newTcuDates, newTcuDateRemarks);
            }
        }

        ParamUtil.setRequestAttr(request, CRUD_TYPE_TCU, action);
    }

    private void saveNewTCUDate(SearchResult<LicPremisesQueryDto> searchResult, int size, List<String> newTcuDates, List<String> newTcuDateRemarks) {
        List<LicPremisesDto> licPremisesDtos = licenceService.getPremisesByLicIds(searchResult.getRows().stream().map(LicPremisesQueryDto::getLicenceId).collect(Collectors.toList()));
        if (IaisCommonUtils.isNotEmpty(licPremisesDtos)) {
            for (int i = 0; i < size; i++) {
                LicPremisesDto licPremisesDto = licPremisesDtos.get(i);
                licPremisesDto.setIsTcuNeeded(Integer.valueOf(AppConsts.YES));
                licPremisesDto.setTcuDate(IaisEGPHelper.parseToDate(newTcuDates.get(i), AppConsts.DEFAULT_DATE_FORMAT));
                licPremisesDto.setTcuDateRemarks(newTcuDateRemarks.get(i));
            }
            licenceService.saveLicPremises(licPremisesDtos);
        }
    }

    private Map<String, String> validateChangeTCUDate(HttpServletRequest request, int size, List<String> newTcuDates, List<String> newTcuDateRemarks) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();

        for (int i = 0; i < size; i++) {
            String newTcuDate = ParamUtil.getString(request, keyNewTcuDateDates + i);
            String newTcuDateRemark = ParamUtil.getString(request, keyNewTcuDateRemarks + i);

            newTcuDates.add(newTcuDate);
            newTcuDateRemarks.add(newTcuDateRemark);

            if (StringUtil.isEmpty(newTcuDate)) {
                errMap.put(keyNewTcuDateDates + i, "GENERAL_ERR0006");
            } else {
                Date newDate = IaisEGPHelper.parseToDate(newTcuDate, AppConsts.DEFAULT_DATE_FORMAT);
                if (!Objects.isNull(newDate) && IaisEGPHelper.getCompareDate(newDate, new Date()) > 1) {
                    errMap.put(keyNewTcuDateDates + i, MessageUtil.replaceMessage("AUDIT_ERR011", "New TCU Date", "field"));
                }
            }
            if (StringUtil.isNotEmpty(newTcuDateRemark) && newTcuDateRemark.length() > 300) {
                errMap.put(keyNewTcuDateRemarks + i, AppValidatorHelper.repLength("Remarks", "300"));
            }
        }
        return errMap;
    }

    public void submit(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request, CRUD_TYPE_TCU, ACTION_PREMISE_LIST);
    }

    public void download(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request, CRUD_TYPE_TCU, ACTION_PREMISE_LIST);
    }

    @SneakyThrows
    public void search(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        LicPremisesQueryDto filterParam = getFilterParamFromPage(request);
        String tcuDateFromStr = ParamUtil.getString(request, keyTcuDateFrom);
        Date fromDate = Formatter.parseDate(tcuDateFromStr);
        String tcuDateFrom = Formatter.formatDateTime(fromDate, SystemAdminBaseConstants.DATE_FORMAT);
        String tcuDateToStr = ParamUtil.getString(request, keyTcuDateTo);
        Date toDate = Formatter.parseDate(tcuDateToStr);
        String tcuDateTo = Formatter.formatDateTime(toDate, SystemAdminBaseConstants.DATE_FORMAT);
        String psnName = ParamUtil.getString(request, keyPsnName);
        String psnType = ParamUtil.getString(request, keyPsnType);
        ParamUtil.setSessionAttr(request, keyTcuDateFrom, tcuDateFromStr);
        ParamUtil.setSessionAttr(request, keyTcuDateTo, tcuDateToStr);
        ParamUtil.setSessionAttr(request, keyPsnName, psnName);
        ParamUtil.setSessionAttr(request, keyPsnType, psnType);

        ParamUtil.setRequestAttr(request, CRUD_TYPE_TCU, ACTION_PREMISE_LIST);

        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        validateSearch(filterParam, tcuDateFromStr, fromDate, tcuDateToStr, toDate, errMap);
        if (!errMap.isEmpty()) {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            return;
        }

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        if (StringUtil.isNotEmpty(filterParam.getLicenceNo())) {
            searchParam.addFilter("licence_no", filterParam.getLicenceNo(), true);
        }

        if (StringUtil.isNotEmpty(filterParam.getHciCode())) {
            searchParam.addFilter("hci_code", filterParam.getHciCode(), true);
        }

        if (StringUtil.isNotEmpty(filterParam.getBusinessName())) {
            searchParam.addFilter("business_name", filterParam.getBusinessName(), true);
        }

        if (StringUtil.isNotEmpty(filterParam.getPostalCode())) {
            searchParam.addFilter("postal_code", filterParam.getPostalCode(), true);
        }

        if (StringUtil.isNotEmpty(filterParam.getAddress())) {
            searchParam.addFilter("address", filterParam.getAddress(), true);
        }

        if (StringUtil.isNotEmpty(filterParam.getServiceName())) {
            searchParam.addFilter("svc_name", filterParam.getServiceName(), true);
        }

        if (StringUtil.isNotEmpty(tcuDateFrom)) {
            searchParam.addFilter("date_from", tcuDateFrom, true);
        }

        if (StringUtil.isNotEmpty(tcuDateTo)) {
            searchParam.addFilter("date_to", tcuDateTo, true);
        }

        if (StringUtil.isNotEmpty(psnName)) {
            searchParam.addFilter("psn_name", psnName, true);
        }

        if (StringUtil.isNotEmpty(psnType)) {
            List<String> psnTypes = IaisCommonUtils.genNewArrayList();
            psnTypes.add(psnType);
            //ESA MTS use
            if("CGO".equals(psnType)){
                psnTypes.add("CD");
            }
            searchParam.addFilter("psn_type", psnTypes, true);
        }
    }

    private void validateSearch(LicPremisesQueryDto filterParam, String tcuDateFromStr, Date fromDate, String tcuDateToStr, Date toDate, Map<String, String> errMap) {
        if (StringUtil.isNotEmpty(tcuDateToStr) && StringUtil.isNotEmpty(tcuDateFromStr) && fromDate.after(toDate)) {
            errMap.put("tcuDateFrom", "AUDIT_ERR010");
        }
        log.info(StringUtil.changeForLog("validateSearch" + filterParam));
    }

    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam, bpc.request);
        ParamUtil.setRequestAttr(request, CRUD_TYPE_TCU, ACTION_PREMISE_LIST);
    }

    public void changePage(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam, bpc.request);
        ParamUtil.setRequestAttr(request, CRUD_TYPE_TCU, ACTION_PREMISE_LIST);
    }

    private LicPremisesQueryDto getFilterParamFromPage(HttpServletRequest request) {
        LicPremisesQueryDto filterParam = new LicPremisesQueryDto();
        filterParam.setLicenceNo(ParamUtil.getRequestString(request, "licenceNo"));
        filterParam.setHciCode(ParamUtil.getRequestString(request, "hciCode"));
        filterParam.setBusinessName(ParamUtil.getRequestString(request, "businessName"));
        filterParam.setPostalCode(ParamUtil.getRequestString(request, "postalCode"));
        filterParam.setServiceName(ParamUtil.getRequestString(request, "serviceName"));
        ParamUtil.setSessionAttr(request, keyFilterParam, filterParam);
        return filterParam;
    }

}
