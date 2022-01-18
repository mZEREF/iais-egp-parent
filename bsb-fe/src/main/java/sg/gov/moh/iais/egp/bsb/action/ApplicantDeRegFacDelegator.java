package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.DeRegOrCancellationClient;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.deregorcancellation.DeRegistrationFacilityDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;


/**
 * @author : LiRan
 * @date : 2022/1/10
 */
@Slf4j
@Delegator("applicantDeRegFacDelegator")
public class ApplicantDeRegFacDelegator {
    private static final String MODULE_NAME = "Renewal Facility Registration";
    private static final String FUNCTION_NAME = "Renewal Facility Registration";

    private static final String KEY_EDIT_APP_ID = "editId";
    private static final String KEY_DEREGISTRATION_FACILITY_DTO = "deRegistrationFacilityDto";

    private final DeRegOrCancellationClient deRegOrCancellationClient;

    @Autowired
    public ApplicantDeRegFacDelegator(DeRegOrCancellationClient deRegOrCancellationClient) {
        this.deRegOrCancellationClient = deRegOrCancellationClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute("");
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME);
    }

    public void init(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String maskApprovalId = request.getParameter(KEY_EDIT_APP_ID);
        if (StringUtils.hasLength(maskApprovalId)){
            if (log.isInfoEnabled()) {
                log.info("masked approval ID: {}", org.apache.commons.lang.StringUtils.normalizeSpace(maskApprovalId));
            }
            String approvalId = MaskUtil.unMaskValue(KEY_EDIT_APP_ID, maskApprovalId);
            if (approvalId != null && !maskApprovalId.equals(approvalId)){
                ResponseDto<DeRegistrationFacilityDto> resultDto = deRegOrCancellationClient.getDeRegistrationFacilityData(approvalId);
                if (resultDto.ok()){
                    DeRegistrationFacilityDto deRegistrationFacilityDto = resultDto.getEntity();
                    ParamUtil.setSessionAttr(request, KEY_DEREGISTRATION_FACILITY_DTO, deRegistrationFacilityDto);
                }
            }
        }
    }

    public void validCommit(BaseProcessClass bpc) {
        //do nothing now
    }

    public void preparePreview(BaseProcessClass bpc) {
        //do nothing now
    }

    public void doSaveSubmit(BaseProcessClass bpc) {
        //do nothing now
    }

    public void doSaveDraft(BaseProcessClass bpc) {
        //do nothing now
    }
}
