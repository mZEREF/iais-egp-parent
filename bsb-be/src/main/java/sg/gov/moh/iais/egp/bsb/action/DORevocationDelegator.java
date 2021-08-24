package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.egp.bsb.client.RevocationClient;
import sg.gov.moh.iais.egp.bsb.constant.RevocationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.entity.Application;
import sg.gov.moh.iais.egp.bsb.entity.ApplicationMisc;
import sg.gov.moh.iais.egp.bsb.entity.Facility;
import sg.gov.moh.iais.egp.bsb.entity.RoutingHistory;
import sg.gov.moh.iais.egp.bsb.util.JoinAddress;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;

/**
 * @author Zhu Tangtang
 */
@Delegator(value = "DORevocationDelegator")
@Slf4j
public class DORevocationDelegator {

    @Autowired
    private RevocationClient revocationClient;

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws IllegalAccessException
     */
    public void start(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_SYSTEM_CONFIG,
                AuditTrailConsts.FUNCTION_ERROR_MESSAGES_MANAGEMENT);
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, RevocationConstants.class);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, null);
    }

    /**
     * AutoStep: prepareData
     *
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc) {
        List<Application> list=new LinkedList<>();
        HttpServletRequest request = bpc.request;
        Application application = revocationClient.getApplicationById("ED1354B8-57FA-EB11-BE6E-000C298D317C").getEntity();
        //Do address processing
        String address = JoinAddress.joinAddress(application);
        application.getFacility().setFacilityAddress(address);
        list.add(application);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, list);
    }

    /**
     * AutoStep: doCreate
     *
     * @param bpc
     */
    public void save(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String facilityId = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_ID);

        Application resultDto = new Application();
        Facility facility = new Facility();
        facility.setId(facilityId);
        resultDto.setFacility(facility);
        resultDto.setAppType(RevocationConstants.PARAM_APPLICATION_TYPE_REVOCATION);
        resultDto.setProcessType("PROTYPE001");
        resultDto.setStatus(RevocationConstants.PARAM_APPLICATION_STATUS_PENDING_AO);
        resultDto.setApplicationDt(new Date());

        FeignResponseEntity<Application> result = revocationClient.saveApplication(resultDto);

        String reason = ParamUtil.getString(request, RevocationConstants.PARAM_REASON);
        String remarks = ParamUtil.getString(request, RevocationConstants.PARAM_DOREMARKS);

        ApplicationMisc miscDto = new ApplicationMisc();
        Application app=new Application();
        app.setId(result.getEntity().getId());
        miscDto.setRemarks(remarks);
        miscDto.setReasonContent(reason);
        miscDto.setApplication(app);
        miscDto.setReason(RevocationConstants.PARAM_REASON_TYPE_DO);

        ResponseDto<ApplicationMisc> miscResponseDto= revocationClient.saveApplicationMisc(miscDto).getEntity();
        if("INVALID_ARGS".equals(miscResponseDto.getErrorCode())) {
            ParamUtil.setRequestAttr(request, ERROR_INFO_ERROR_MSG, miscResponseDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG));
        }

        //get user name
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);

        RoutingHistory historyDto=new RoutingHistory();
        historyDto.setAppStatus(result.getEntity().getStatus());
        historyDto.setActionBy(loginContext.getUserName());
        historyDto.setInternalRemarks(miscDto.getRemarks());
        historyDto.setApplicationNo(result.getEntity().getApplicationNo());
        revocationClient.saveHistory(historyDto);
    }

    /**
     * AutoStep: updateNum
     *
     * @param bpc
     */
    public void updateNum(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        List<Application> list=new LinkedList<>();
        Application application = revocationClient.getApplicationById("ED1354B8-57FA-EB11-BE6E-000C298D317C").getEntity();
        //Do address processing
        String address = JoinAddress.joinAddress(application);
        application.getFacility().setFacilityAddress(address);
        list.add(application);
        ParamUtil.setRequestAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, list);
        //TODO update inventory method
    }

}
