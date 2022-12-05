package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.MasterCodePair;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArMgrQueryPatientDto;
import com.ecquaria.cloud.moh.iais.common.helper.dataSubmission.DsHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.service.AssistedReproductionService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.impl.ArManagementService;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * ArManagementDelegate
 *
 * @author Jinhua
 * @date 2022/11/30 10:41
 */
@Slf4j
@Delegator("arManagementDelegate")
public class ArManagementDelegate {
    private static final String STR_SEARCH_PARAM_ATTR                = "arMgrSearchParam";

    @Autowired
    private AssistedReproductionService assistedReproductionService;
    @Autowired
    private ArManagementService arManagementService;

    /**
     * StartStep: Start
     *
     * @param bpc
     * @throws
     */
    public void start(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_DATA_SUBMISSION, AuditTrailConsts.FUNCTION_AR_DS_MANAGEMENT);
    }

    /**
     * AutoStep: Init
     *
     * @param bpc
     * @throws
     */
    public void init(BaseProcessClass bpc) {
        List<SelectOption> centerOpts = assistedReproductionService.genPremisesOptions(DataSubmissionConsts.DS_AR, null);
        ParamUtil.setSessionAttr(bpc.request, "arMgrCenterOptsAttr", (Serializable) centerOpts);
        Set<String> stgsSet = IaisCommonUtils.genNewHashSet();
        stgsSet.addAll(DsHelper.getAllARCycleStages());
        stgsSet.addAll(DsHelper.getAllIUICycleStages());
        stgsSet.addAll(DsHelper.getAllOFOCycleStages());
        stgsSet.addAll(DsHelper.getAllSFOCycleStages());
        List<SelectOption> stageOpts = MasterCodeUtil.retrieveOptionsByCodes(stgsSet.toArray(new String[stgsSet.size()]));
        ParamUtil.setSessionAttr(bpc.request, "arMgrStageOptsAttr", (Serializable) stageOpts);
        ParamUtil.setSessionAttr(bpc.request, STR_SEARCH_PARAM_ATTR, initSearchParam());
        ParamUtil.setSessionAttr(bpc.request, "patientParam", null);
    }

    /**
     * AutoStep: Query
     *
     * @param bpc
     * @throws
     */
    public void query(BaseProcessClass bpc) {
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, STR_SEARCH_PARAM_ATTR);
        QueryHelp.setMainSql("arManagement","searchLockedPatientInfo", searchParam);
        SearchResult<ArMgrQueryPatientDto> searchResult = arManagementService.queryPatient(searchParam);
        ParamUtil.setSessionAttr(bpc.request, "arMgrSearchResult", searchResult);
    }

    /**
     * AutoStep: Search
     *
     * @param bpc
     * @throws
     */
    public void search(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String arCenterFilter = ParamUtil.getString(request,"arCenterFilter");
        String submissionNoFilter = ParamUtil.getString(request,"submissionNoFilter");
        String cycleStageFilter = ParamUtil.getString(request,"cycleStageFilter");
        String submitDateFromFilter = ParamUtil.getString(request,"submitDateFromFilter");
        String submitDateToFilter = ParamUtil.getString(request,"submitDateToFilter");
        SearchParam searchParam = initSearchParam();
        if (StringUtil.isNotEmpty(arCenterFilter)) {
            searchParam.addFilter("arCenterFilter", arCenterFilter, true);
        }
        if (StringUtil.isNotEmpty(submissionNoFilter)) {
            searchParam.addFilter("submissionNoFilter", submissionNoFilter, true);
        }
        if (StringUtil.isNotEmpty(cycleStageFilter)) {
            searchParam.addFilter("cycleStageFilter", cycleStageFilter, true);
        }
        if (StringUtil.isNotEmpty(submitDateFromFilter)) {
            searchParam.addFilter("submitDateFromFilter", submitDateFromFilter, true);
        }
        if (StringUtil.isNotEmpty(submitDateToFilter)) {
            Date submitDateTo = Formatter.parseDate(submitDateToFilter);
            Calendar cal = Calendar.getInstance();
            cal.setTime(submitDateTo);
            cal.add(Calendar.DAY_OF_MONTH, 1);
            searchParam.addParam("submitDateToFilter", submitDateToFilter);
            searchParam.addFilter("submitDateToFilter", Formatter.formatDate(cal.getTime()));
        }
        ParamUtil.setSessionAttr(request, STR_SEARCH_PARAM_ATTR, searchParam);
    }

    /**
     * AutoStep: Unlock
     *
     * @param bpc
     * @throws
     */
    public void unlock(BaseProcessClass bpc) {
        String[] unlockNos = ParamUtil.getStrings(bpc.request, "subId");
        if (unlockNos != null && unlockNos.length > 0) {

        }
    }

    /**
     * AutoStep: Sorting
     *
     * @param bpc
     * @throws
     */
    public void sorting(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, STR_SEARCH_PARAM_ATTR);
        CrudHelper.doSorting(searchParam,bpc.request);
    }

    /**
     * AutoStep: Paging
     *
     * @param bpc
     * @throws
     */
    public void paging(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request, STR_SEARCH_PARAM_ATTR);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * AutoStep: Back
     *
     * @param bpc
     * @throws
     */
    public void back(BaseProcessClass bpc) {

    }

    private SearchParam initSearchParam() {
        SearchParam searchParam = new SearchParam(ArMgrQueryPatientDto.class.getName());
        searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
        searchParam.setPageNo(1);
        searchParam.setSortField("PATIENT_ID");
        MasterCodePair mcp = new MasterCodePair("ID_TYPE", "ID_TYPE_DESC",
                MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DS_ID_TYPE_DTV));
        searchParam.addMasterCode(mcp);
        mcp = new MasterCodePair("NATIONALITY", "NATIONALITY_DESC",
                MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_NATIONALITY));
        searchParam.addMasterCode(mcp);

        return searchParam;
    }
}
