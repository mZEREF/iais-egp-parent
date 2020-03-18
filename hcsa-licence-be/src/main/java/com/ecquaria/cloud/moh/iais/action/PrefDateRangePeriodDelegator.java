package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import com.ecquaria.cloud.moh.iais.service.PrefDateRangePeriodService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: yichen
 * @date time:12/24/2019 5:06 PM
 * @description:
 */

@Delegator(value = "prefDateRangePeriodDelegator")
@Slf4j
public class PrefDateRangePeriodDelegator {

    private static final String PERIOD_AFTER_APP_ATTR = "periodAfterApp";
    private static final String PERIOD_BEFORE_APP_ATTR = "periodBeforeExp";
    private static final String NON_REPLY_WINDOW_ATTR = "nonReplyWindow";
    private static final String SERVICE_NAME_ATTR = "svcName";
    private static final String PREF_PERIOD_SEARCH = "prefPeriodSearch";
    private static final String PREF_PERIOD_RESULT = "prefPeriodResult";
    private static final String REQUEST_PERIOD_ATTR = "requestPeriodAttr";


    @Autowired
    private HcsaChklService hcsaChklService;

    @Autowired
    private PrefDateRangePeriodService periodService;

    private FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(HcsaServicePrefInspPeriodQueryDto.class).searchAttr(PREF_PERIOD_SEARCH).resultAttr(PREF_PERIOD_RESULT).
                    sortField("id").sortType(SearchParam.ASCENDING).build();

    /**
     * StartStep: preLoad
     *
     * @param bpc
     * @throws
     */
    public void preLoad(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        log.debug(StringUtil.changeForLog("Define Preferred Date Range Period START ...."));
        AuditTrailHelper.auditFunction("hcsa-licence-be", "Define Preferred Date Range Period");
        preSelectOption(bpc.request);


        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);

        QueryHelp.setMainSql("hcsaconfig", "getPrefInspPeriodList",searchParam);

        SearchResult<HcsaServicePrefInspPeriodQueryDto> hcsaServicePrefInspPeriodList = periodService.getHcsaServicePrefInspPeriodList(searchParam);
        ParamUtil.setSessionAttr(request, PREF_PERIOD_SEARCH, searchParam);
        ParamUtil.setSessionAttr(request, PREF_PERIOD_RESULT, hcsaServicePrefInspPeriodList);
    }

    /**
     * setup option to web page
     * @param request
     */
    private void preSelectOption(HttpServletRequest request){
        List<String> svcNames = hcsaChklService.listServiceName();
        List<SelectOption> svcNameSelect = IaisCommonUtils.genNewArrayList();

        for (String s : svcNames){
            svcNameSelect.add(new SelectOption(s,s));
        }

        ParamUtil.setRequestAttr(request, "svcNameSelect", svcNameSelect);

    }

    /**
     * StartStep: doCancel
     *
     * @param bpc
     * @throws
     */
    public void doCancel(BaseProcessClass bpc){

    }

    /**
     * StartStep: preUpdateData
     *
     * @param bpc
     * @throws
     */
    public void preUpdateData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String id = ParamUtil.getRequestString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        if (StringUtil.isEmpty(id)){
            return;
        }

        SearchResult<HcsaServicePrefInspPeriodQueryDto> hcsaServicePrefInspPeriodList = (SearchResult<HcsaServicePrefInspPeriodQueryDto>) ParamUtil.getSessionAttr(request, "prefPeriodResult");
        if (hcsaServicePrefInspPeriodList != null && hcsaServicePrefInspPeriodList.getRows() != null){
            List<HcsaServicePrefInspPeriodQueryDto> periodQueryList = hcsaServicePrefInspPeriodList.getRows();
            for (HcsaServicePrefInspPeriodQueryDto periodQuery : periodQueryList){
                if (id.equals(periodQuery.getId())){
                    HcsaServicePrefInspPeriodDto periodDto = new HcsaServicePrefInspPeriodDto();
                    periodDto.setId(periodQuery.getId());
                    periodDto.setPeriodAfterApp(periodQuery.getPeriodAfterApp());
                    periodDto.setPeriodBeforeExp(periodQuery.getPeriodBeforeExp());
                    periodDto.setSvcCode(periodQuery.getSvcCode());
                    periodDto.setVersion(periodQuery.getVersion());
                    periodDto.setNonReplyWindow(periodQuery.getNonReplyWindow());
                    periodDto.setStatus(periodQuery.getStatus());
                    ParamUtil.setSessionAttr(request, REQUEST_PERIOD_ATTR, periodDto);
                }
            }

        }



    }

    /**
     * StartStep: doBack
     *
     * @param bpc
     * @throws
     */
    public void doBack(BaseProcessClass bpc){

    }

    /**
     * StartStep: submitPrefDate
     *
     * @param bpc
     * @throws
     */
    public void submitPrefDate(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        String periodAfterApp = ParamUtil.getString(request, PERIOD_AFTER_APP_ATTR );
        String periodBeforeExp = ParamUtil.getString(request, PERIOD_BEFORE_APP_ATTR );
        String nonReplyWindow = ParamUtil.getString(request, NON_REPLY_WINDOW_ATTR );

        ParamUtil.setRequestAttr(request, PERIOD_AFTER_APP_ATTR , periodAfterApp);
        ParamUtil.setRequestAttr(request, PERIOD_BEFORE_APP_ATTR , periodBeforeExp);
        ParamUtil.setRequestAttr(request, NON_REPLY_WINDOW_ATTR , nonReplyWindow);


        HcsaServicePrefInspPeriodDto period = (HcsaServicePrefInspPeriodDto) ParamUtil.getSessionAttr(request, "requestPeriodAttr");
        try {
            period.setPeriodAfterApp(transformToDay(Integer.parseInt(periodAfterApp)));
            period.setPeriodBeforeExp(transformToDay(Integer.parseInt(periodBeforeExp)));
            period.setNonReplyWindow(Integer.parseInt(nonReplyWindow));
        }catch (NumberFormatException e){
            HashMap<String, String> errorMap = new HashMap<>(1);
            errorMap.put("numberError", MessageCodeKey.GENERAL_ERR0002);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            return;
        }

        ValidationResult validationResult = WebValidationHelper.validateProperty(period, "update");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }

        Boolean result = periodService.savePrefInspPeriod(period);
        if (result){
	        ParamUtil.setRequestAttr(request,"ackMsg", "You have successfully update a block-out period.");
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            return;
        }else {
            HashMap<String, String> errorMap = new HashMap<>(1);
            errorMap.put("dataError", MessageCodeKey.GENERAL_ERR0001);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            return;
        }
    }

    private Integer transformToDay(Integer week){
        if (week == null){
            return null;
        }

        return week * 7;
    }

    private HashMap<String, String> validateResult(HttpServletRequest request, SearchParam searchParam){
        HashMap<String, String> errorMap = new HashMap<>(1);
        String svcName = ParamUtil.getString(request, SERVICE_NAME_ATTR );
        String periodAfterApp = ParamUtil.getString(request, PERIOD_AFTER_APP_ATTR );
        String periodBeforeExp = ParamUtil.getString(request, PERIOD_BEFORE_APP_ATTR );
        String nonReplyWindow = ParamUtil.getString(request, NON_REPLY_WINDOW_ATTR );

        ParamUtil.setRequestAttr(request, SERVICE_NAME_ATTR , svcName);
        ParamUtil.setRequestAttr(request, PERIOD_AFTER_APP_ATTR , periodAfterApp);
        ParamUtil.setRequestAttr(request, PERIOD_BEFORE_APP_ATTR , periodBeforeExp);
        ParamUtil.setRequestAttr(request, NON_REPLY_WINDOW_ATTR , nonReplyWindow);

        if (!StringUtil.isEmpty(svcName)){
            searchParam.addFilter(SERVICE_NAME_ATTR, svcName, true);
        }

        try {
            if (!StringUtil.isEmpty(periodAfterApp)){
                int afterApp = Integer.parseInt(periodAfterApp);
                searchParam.addFilter(PERIOD_AFTER_APP_ATTR, afterApp, true);
            }

            if (!StringUtil.isEmpty(periodBeforeExp)){
                int beforeExp = Integer.parseInt(periodBeforeExp);
                searchParam.addFilter(PERIOD_BEFORE_APP_ATTR, beforeExp, true);
            }

            if (!StringUtil.isEmpty(nonReplyWindow)){
                int nonRepyWindow = Integer.parseInt(nonReplyWindow);
                searchParam.addFilter(NON_REPLY_WINDOW_ATTR, nonRepyWindow, true);
            }
        }catch (NumberFormatException e){
            errorMap.put("numberError", MessageCodeKey.GENERAL_ERR0002);
            return errorMap;
        }

        return errorMap;
    }

    /**
     * StartStep: doSearch
     *
     * @param bpc
     * @throws
     */
    public void doSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

        HashMap<String, String> errorMap = validateResult(request, searchParam);
        if (errorMap != null && !errorMap.isEmpty()){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        }else {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }

    }

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws
     */
    public void startStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        log.debug(StringUtil.changeForLog("Pref Date Range Period start ...."));
        AuditTrailHelper.auditFunction("hcsa-licence-be", "Pref Date Range Period");

        ParamUtil.setSessionAttr(request, PREF_PERIOD_SEARCH , null);
        ParamUtil.setSessionAttr(request, PREF_PERIOD_RESULT , null);
    }

    /**
     * AutoStep: sortRecords
     * @param bpc
     * @throws IllegalAccessException
     */
    public void sortRecords(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,bpc.request);
    }

    /**
     * AutoStep: changePage
     * @param bpc
     * @throws IllegalAccessException
     */
    public void changePage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

}
