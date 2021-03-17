package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.giro.GiroDeductionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.GiroDeductionBeService;
import com.ecquaria.cloud.moh.iais.service.client.GiroDeductionClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @Process: MohBeGiroDeduction
 *
 * @author Shicheng
 * @date 2020/10/19 10:31
 **/
@Delegator(value = "giroDeductionBeDelegator")
@Slf4j
public class GiroDeductionBeDelegator {
    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(GiroDeductionDto.class)
            .searchAttr("giroDedSearchParam")
            .resultAttr("giroDedSearchResult")
            .sortField("APP_GROUP_NO").build();
    @Autowired
    private GiroDeductionBeService giroDeductionBeService;
    @Autowired
    private GiroDeductionClient giroDeductionClient;
    @Autowired
    private GiroDeductionBeDelegator(GiroDeductionBeService giroDeductionBeService){
        this.giroDeductionBeService = giroDeductionBeService;
    }

    /**
     * StartStep: beGiroDeductionStart
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionStart(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionStart start ...."));
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_LOAD_LEVELING, AuditTrailConsts.MODULE_GIRO_DEDUCTION);
        filterParameter.setPageSize(SystemParamUtil.getDefaultPageSize());
        filterParameter.setPageNo(1);
        removeSession(bpc.request);
    }

    private void removeSession(HttpServletRequest request){
        request.getSession().removeAttribute("giroDedSearchResult");
        request.getSession().removeAttribute("giroDedSearchParam");
    }
    /**
     * StartStep: beGiroDeductionInit
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionInit(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionInit start ...."));
    }

    /**
     * StartStep: beGiroDeductionPre
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionPre(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionPre start ...."));
        SearchParam searchParam = SearchResultHelper.getSearchParam(bpc.request, filterParameter, true);
        String transactionId = bpc.request.getParameter("transactionId");
        String bankAccountNo = bpc.request.getParameter("bankAccountNo");
        String group_no = bpc.request.getParameter("applicationNo");
        String paymentRefNo = bpc.request.getParameter("paymentRefNo");
        String paymentAmount = bpc.request.getParameter("paymentAmount");
        String paymentDescription = bpc.request.getParameter("paymentDescription");
        String hci_name = bpc.request.getParameter("hci_name");
        if(!StringUtil.isEmpty(group_no)){
            searchParam.addFilter("groupNo",group_no,true);
        }
        if(!StringUtil.isEmpty(paymentDescription)){
            searchParam.addFilter("desc",paymentDescription,true);
        }
        if(!StringUtil.isEmpty(paymentAmount)){
            searchParam.addFilter("amount",paymentAmount,true);
        }
        if(!StringUtil.isEmpty(bankAccountNo)){
            searchParam.addFilter("acctNo",bankAccountNo,true);
        }
        if(!StringUtil.isEmpty(hci_name)){
            searchParam.addFilter("hciName",hci_name,true);
        }
        if(!StringUtil.isEmpty(paymentRefNo)){
            searchParam.addFilter("refNo",paymentRefNo,true);
        }
        if(!StringUtil.isEmpty(transactionId)){
            searchParam.addFilter("invoiceNo",transactionId,true);
        }
        QueryHelp.setMainSql("giroPayee", "searchGiroDeduction", searchParam);
        SearchResult<GiroDeductionDto> body = giroDeductionClient.giroDeductionDtoSearchResult(searchParam).getEntity();
        ParamUtil.setSessionAttr(bpc.request, "giroDedSearchResult", body);
        ParamUtil.setSessionAttr(bpc.request, "giroDedSearchParam", searchParam);

    }

    /**
     * StartStep: beGiroDeductionStep
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionStep(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionStep start ...."));
    }

    /**
     * StartStep: beGiroDeductionDoSearch
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionDoSearch(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionDoSearch start ...."));
    }

    /**
     * StartStep: beGiroDeductionSort
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionSort(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionSort start ...."));
    }

    /**
     * StartStep: beGiroDeductionPage
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionPage(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionPage start ...."));
        filterParameter.setPageNo(1);
        SearchResultHelper.doPage(bpc.request, filterParameter);
    }

    /**
     * StartStep: beGiroDeductionQuery
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionQuery(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionQuery start ...."));
    }

    /**
     * StartStep: beGiroDeductionRetVali
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionRetVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the beGiroDeductionRetVali start ...."));
        String[] appGroupNos = ParamUtil.getStrings(bpc.request,"giroDueCheck");
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String retValiError = "giroDeductionError";
        List<String> appGroupList = IaisCommonUtils.genNewArrayList();
        if(appGroupNos != null && appGroupNos.length > 0){
            //todo appGroup payment status validate
            for(int i = 0; i < appGroupNos.length; i++){
                appGroupList.add(appGroupNos[i]);
            }
        } else {
            errMap.put(retValiError, "GENERAL_ERR0006");
        }
        if(errMap != null && errMap.size() > 0){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
        } else {
            ParamUtil.setRequestAttr(bpc.request,"appGroupList", appGroupList);
            ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
        }
    }

    /**
     * StartStep: beGiroDeductionRetrigger
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionRetrigger(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionRetrigger start ...."));
        List<String> appGroupList = (List<String>)ParamUtil.getRequestAttr(bpc.request, "appGroupList");
        giroDeductionBeService.sendMessageEmail(appGroupList);
        giroDeductionClient.updateDeductionDtoSearchResultUseGroups(appGroupList);
    }
}
