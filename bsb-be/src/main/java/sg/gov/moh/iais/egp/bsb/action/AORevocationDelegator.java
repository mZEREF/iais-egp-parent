package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.RevocationClient;
import sg.gov.moh.iais.egp.bsb.constant.revocationConstants.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.dto.revocation.ApprovalOfficerQueryResultsDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.BsbRoutingHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.RevocationDetailsDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Zhu Tangtang
 */
@Delegator(value = "AORevocationDelegator")
@Slf4j
public class AORevocationDelegator {

    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(ApprovalOfficerQueryResultsDto.class)
            .searchAttr(RevocationConstants.PARAM_APPLICATION_SEARCH)
            .resultAttr(RevocationConstants.PARAM_APPLICATION_SEARCH_RESULT)
            .sortFieldToMap("APPLICATION_NO", SearchParam.ASCENDING).build();

    @Autowired
    private RevocationClient revocationClient;

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws IllegalAccessException
     */
    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG, AuditTrailConsts.FUNCTION_ERROR_MESSAGES_MANAGEMENT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, RevocationConstants.class);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH_RESULT, null);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, null);
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareTaskListData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SearchParam param = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("bsbBe", "AOQueryApplication", param);
        SearchResult searchResult = revocationClient.doQuery(param).getEntity();
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH, param);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH_RESULT, searchResult);
    }

    /**
     * AutoStep: doSearch
     *
     * @param bpc
     */
    public void doSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;

        String facilityName = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_NAME);
        String facilityAddress = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_ADDRESS);
        String facilityClassification = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_CLASSIFICATION);
        String facilityType = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_TYPE);
        String processType = ParamUtil.getString(request, RevocationConstants.PARAM_PROCESS_TYPE);
        String applicationDt=ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_DATE);
        String applicationNo = ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_NO);
        String applicationType = ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_TYPE);
        String applicationStatus = ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_STATUS);

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

        Date applicationDate=null;

        if (StringUtil.isNotEmpty(facilityName)) {
            searchParam.addFilter(RevocationConstants.PARAM_FACILITY_NAME, facilityName, true);
        }
        if (StringUtil.isNotEmpty(facilityClassification)) {
            searchParam.addFilter(RevocationConstants.PARAM_FACILITY_CLASSIFICATION, facilityClassification, true);
        }
        if (StringUtil.isNotEmpty(facilityType)) {
            searchParam.addFilter(RevocationConstants.PARAM_FACILITY_TYPE, facilityType, true);
        }
        if (StringUtil.isNotEmpty(processType)) {
            searchParam.addFilter(RevocationConstants.PARAM_PROCESS_TYPE, processType, true);
        }
        if (StringUtil.isNotEmpty(applicationDt)) {
            applicationDate = Formatter.parseDate(ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_DATE));
            searchParam.addFilter(RevocationConstants.PARAM_APPLICATION_DATE, applicationDate, true);
        }
        if (StringUtil.isNotEmpty(applicationNo)) {
            searchParam.addFilter(RevocationConstants.PARAM_APPLICATION_NO, applicationNo, true);
        }
        if (StringUtil.isNotEmpty(applicationType)) {
            searchParam.addFilter(RevocationConstants.PARAM_APPLICATION_TYPE, applicationType, true);
        }
        if (StringUtil.isNotEmpty(applicationStatus)) {
            searchParam.addFilter(RevocationConstants.PARAM_APPLICATION_STATUS, applicationStatus, true);
        }
        if (StringUtil.isNotEmpty(facilityAddress)) {
            String[] strArr = facilityAddress.split("");
            String blockNo = strArr[0];
            String floorNo = strArr[1];
            String postalCode = strArr[3];

            String[] arr = strArr[2].split("-");
            String unitNo = arr[0];
            String streetName = arr[1];

            searchParam.addFilter(RevocationConstants.PARAM_BLOCK_NO, blockNo, true);
            searchParam.addFilter(RevocationConstants.PARAM_FLOOR_NO, floorNo, true);
            searchParam.addFilter(RevocationConstants.PARAM_POSTAL_CODE, postalCode, true);
            searchParam.addFilter(RevocationConstants.PARAM_UNIT_NO, unitNo, true);
            searchParam.addFilter(RevocationConstants.PARAM_STREET_NAME, streetName, true);
        }
        //Select back the query criteria
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_FACILITY_NAME, facilityName);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_FACILITY_CLASSIFICATION, facilityClassification);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_FACILITY_TYPE, facilityType);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_PROCESS_TYPE, processType);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_APPLICATION_DATE, applicationDate);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_APPLICATION_NO, applicationNo);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_APPLICATION_TYPE, applicationType);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_APPLICATION_STATUS, applicationStatus);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_FACILITY_ADDRESS, facilityAddress);
    }

    /**
     * AutoStep: doPaging
     *
     * @param bpc
     */
    public void doPaging(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam, bpc.request);
    }

    /**
     * AutoStep: doSorting
     *
     * @param bpc
     */
    public void doSorting(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam, bpc.request);
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc) {
        List<RevocationDetailsDto> list = new LinkedList<>();
        HttpServletRequest request = bpc.request;
        String appId = ParamUtil.getMaskedString(request, "appId");
        RevocationDetailsDto revocationDetailsDto = revocationClient.getApplicationById(appId).getEntity();
        list.add(revocationDetailsDto);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, (Serializable) list);

        List<BsbRoutingHistoryDto> historyDtoList = revocationClient.getAllHistory().getEntity();
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_PROCESSING_HISTORY, (Serializable) historyDtoList);
    }

    /**
     * AutoStep: approve
     * Change the status of bsb_facility to Revoked
     * Change the status of bsb_application to Approved
     *
     * @param bpc
     */
    public void approve(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        List<RevocationDetailsDto> list=(List<RevocationDetailsDto>) ParamUtil.getSessionAttr(request,RevocationConstants.PARAM_REVOCATION_DETAIL);
        String reason=ParamUtil.getString(request, "reason");
        String appId="";
        String facId="";
        for (RevocationDetailsDto revocationDetailsDto : list) {
            appId = revocationDetailsDto.getApplicationId();
            facId = revocationDetailsDto.getFacilityId();
        }
        revocationClient.updateApplicationStatusById(appId,"BSBAPST009");
        revocationClient.updateFacilityStatusById(facId,"FACSTA007");
    }

    /**
     * AutoStep: reject
     *
     * Change the status of bsb_application to Rejected
     *
     * @param bpc
     */
    public void reject(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        List<RevocationDetailsDto> list=(List<RevocationDetailsDto>) ParamUtil.getSessionAttr(request,RevocationConstants.PARAM_REVOCATION_DETAIL);
        String appId="";
        for (RevocationDetailsDto revocationDetailsDto : list) {
            appId = revocationDetailsDto.getApplicationId();
        }
        revocationClient.updateApplicationStatusById(appId,"BSBAPST008");

//        String reason = ParamUtil.getString(request, RevocationConstants.PARAM_REASON);
//        String remarks = ParamUtil.getString(request, RevocationConstants.PARAM_DOREMARKS);

//        revocationService.saveApplicationMisc(revocationDetailsDto);

        //get user name
//        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
//
//        BsbRoutingHistoryDto historyDto=new BsbRoutingHistoryDto();
//        historyDto.setAppStatus(result.getEntity().getStatus());
//        historyDto.setActionBy(loginContext.getUserName());
//        historyDto.setInternalRemarks(revocationDetailsDto.getRemarks());
//        historyDto.setApplicationNo(result.getEntity().getApplicationNo());
//        revocationService.saveHistory(historyDto);
    }

    /**
     * AutoStep: routeback
     *
     * Change the status of bsb_application to Pending DO
     *
     * @param bpc
     */
    public void routebackToDO(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        List<RevocationDetailsDto> list=(List<RevocationDetailsDto>) ParamUtil.getSessionAttr(request,RevocationConstants.PARAM_REVOCATION_DETAIL);
        String reason=ParamUtil.getString(request, "reason");
        String appId="";
        for (RevocationDetailsDto revocationDetailsDto : list) {
            appId = revocationDetailsDto.getApplicationId();
        }
        revocationClient.updateApplicationStatusById(appId,"BSBAPST001");
    }

    /**
     * AutoStep: routeToHM
     *
     * Change the status of bsb_application to Pending HM
     *
     * @param bpc
     */
    public void routeToHM(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        List<RevocationDetailsDto> list=(List<RevocationDetailsDto>) ParamUtil.getSessionAttr(request,RevocationConstants.PARAM_REVOCATION_DETAIL);
        String reason=ParamUtil.getString(request, "reason");
        String appId="";
        for (RevocationDetailsDto revocationDetailsDto : list) {
            appId = revocationDetailsDto.getApplicationId();
        }
        revocationClient.updateApplicationStatusById(appId,"BSBAPST003");
    }
}
