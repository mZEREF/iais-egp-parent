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
import sg.gov.moh.iais.egp.bsb.dto.revocation.AOQueryInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.ApplicationMiscDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.BsbRoutingHistoryDto;
import sg.gov.moh.iais.egp.bsb.dto.revocation.RevocationDetailsDto;
import sg.gov.moh.iais.egp.bsb.entity.Application;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
        Application application = revocationClient.getApplicationById("B856D31C-E3F0-EB11-8B7D-000C293F0C99").getEntity();
        //Do address processing
        String address = joinAddress(application);
        application.getFacility().setFacilityAddress(address);
        list.add(application);
        ParamUtil.setSessionAttr(request, RevocationConstants.PARAM_REVOCATION_DETAIL, (Serializable) list);
    }

    /**
     * AutoStep: doCreate
     *
     * @param bpc
     */
    public void save(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String facilityId = ParamUtil.getString(request, RevocationConstants.PARAM_FACILITY_ID);

        AOQueryInfoDto resultDto = new AOQueryInfoDto();
        resultDto.setApplicationNo("APP0000012");//Every time plus one
        resultDto.setFacilityId(facilityId);
        resultDto.setAppType("BSBAPTY006");
        resultDto.setProcessType("PROTYPE001");
        resultDto.setStatus("BSBAPST002");
        resultDto.setApplicationDt(new Date());

        FeignResponseEntity<AOQueryInfoDto> result = revocationClient.saveApplication(resultDto);

        String reason = ParamUtil.getString(request, RevocationConstants.PARAM_REASON);
        String remarks = ParamUtil.getString(request, RevocationConstants.PARAM_DOREMARKS);

        ApplicationMiscDto miscDto = new ApplicationMiscDto();
        miscDto.setRemarks(remarks);
        miscDto.setReasonContent(reason);
        miscDto.setApplicationId(result.getEntity().getId());
        miscDto.setReason("REASON02");

        revocationClient.saveApplicationMisc(miscDto);

        //get user name
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);

        BsbRoutingHistoryDto historyDto=new BsbRoutingHistoryDto();
        historyDto.setAppStatus(result.getEntity().getStatus());
        historyDto.setActionBy(loginContext.getUserName());
        historyDto.setInternalRemarks(miscDto.getRemarks());
        historyDto.setApplicationNo(result.getEntity().getApplicationNo());
        revocationClient.saveHistory(historyDto);
    }

    private String joinAddress(Application application){
        String blockNo = application.getFacility().getBlkNo();
        String streetName = application.getFacility().getStreetName();
        String floorNo = application.getFacility().getFloorNo();
        String unitNo = application.getFacility().getUnitNo();
        String postalCode = application.getFacility().getPostalCode();
        StringBuilder builder=new StringBuilder();
        String facilityAddress="";
        if (blockNo!=null){
            builder.append(blockNo).append(" ");
        }else{
            builder.append("? ");
        }

        if (streetName!=null){
            builder.append(streetName).append(" ");
        }else{
            builder.append("? ");
        }

        if (floorNo!=null){
            builder.append(floorNo).append("-");
        }else{
            builder.append("?-");
        }

        if (unitNo!=null){
            builder.append(unitNo).append(" ");
        }else{
            builder.append("? ");
        }

        if (postalCode!=null){
            builder.append(postalCode).append(" ");
        }else{
            builder.append("?");
        }
        facilityAddress=builder.toString();

        return facilityAddress;
    }

}
