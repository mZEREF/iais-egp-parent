package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.dto.revocation.ApprovalOfficerQueryDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.BsbRoutingHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.RevocationDetailsDto;
import sg.gov.moh.iais.egp.bsb.client.RevocationClient;
import sg.gov.moh.iais.egp.bsb.dto.revocation.AOQueryResultDto;
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
        // get search DTO
        ApprovalOfficerQueryDto searchDto=(ApprovalOfficerQueryDto)ParamUtil.getSessionAttr(request,RevocationConstants.PARAM_APPLICATION_SEARCH);
        if (searchDto==null) {
            searchDto = new ApprovalOfficerQueryDto();
        }
        AOQueryResultDto searchResult = revocationClient.doQuery(searchDto).getEntity();
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH_RESULT, (Serializable) searchResult.getBsbInboxes());
    }

    /**
     * AutoStep: doSearch
     *
     * @param bpc
     */
    public void doSearch(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        ApprovalOfficerQueryDto searchDto = new ApprovalOfficerQueryDto();

        String facilityName = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_NAME);
        String facilityAddress = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_ADDRESS);
        String facilityClassification = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_CLASSIFICATION);
        String facilityType = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_TYPE);
        String processType = ParamUtil.getString(request, RevocationConstants.PARAM_PROCESS_TYPE);
        String applicationDt=ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_DATE);
        String applicationNo = ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_NO);
        String applicationType = ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_TYPE);
        String applicationStatus = ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_STATUS);

        Date applicationDate=null;

        searchDto.setFacilityName(facilityName);
        searchDto.setFacilityClassification(facilityClassification);
        searchDto.setFacilityType(facilityType);
        searchDto.setProcessType(processType);
        searchDto.setApplicationNo(applicationNo);
        searchDto.setApplicationType(applicationType);
        searchDto.setApplicationStatus(applicationStatus);
        if (StringUtil.isNotEmpty(applicationDt)) {
            applicationDate = Formatter.parseDate(ParamUtil.getString(request, RevocationConstants.PARAM_APPLICATION_DATE));
            searchDto.setApplicationDate(applicationDate);
        }
        if (StringUtil.isNotEmpty(facilityAddress)) {
            String[] strArr = facilityAddress.split("");
            String blockNo = strArr[0];
            String streetName = strArr[1];
            String postalCode = strArr[3];
            searchDto.setBlockNo(blockNo);
            searchDto.setStreetName(streetName);
            searchDto.setPostalCode(postalCode);

            String[] arr = strArr[2].split("-");
            String floorNo = arr[0];
            String unitNo = arr[1];
            searchDto.setFloorNo(floorNo);
            searchDto.setUnitNo(unitNo);
        }
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_APPLICATION_SEARCH, searchDto);
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
    }

    /**
     * AutoStep: doSorting
     *
     * @param bpc
     */
    public void doSorting(BaseProcessClass bpc) {
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
