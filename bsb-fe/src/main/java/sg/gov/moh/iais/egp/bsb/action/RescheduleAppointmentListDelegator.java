package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.BsbAppointmentClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentViewDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;

@Slf4j
@Delegator("rescheduleAppointmentListDelegator")
public class RescheduleAppointmentListDelegator {
    private final BsbAppointmentClient bsbAppointmentClient;

    public RescheduleAppointmentListDelegator(BsbAppointmentClient bsbAppointmentClient) {
        this.bsbAppointmentClient = bsbAppointmentClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_APPOINTMENT_LIST_DATA_LIST);
    }

    public void prepareData(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(MODULE_NAME, "Reschedule Appointments");
        HttpServletRequest request = bpc.request;
        ResponseDto<List<AppointmentViewDto>> resultDto = bsbAppointmentClient.searchRescheduleAppt();
        if (resultDto.ok()) {
            ParamUtil.setSessionAttr(request, KEY_APPOINTMENT_LIST_DATA_LIST, (Serializable) resultDto.getEntity());
        } else {
            log.info("Search Task List fail");
            ParamUtil.setRequestAttr(request, KEY_APPOINTMENT_LIST_DATA_LIST, new ArrayList<>());
            if(ERROR_CODE_VALIDATION_FAIL.equals(resultDto.getErrorCode())) {
                log.warn("Fail reason: {}", resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG));
            }
        }
        List<SelectOption> inspectionAppStatusOption = MasterCodeUtil.retrieveOptionsByCodes(MasterCodeConstants.INSPECTION_APP_STATUS.toArray(new String[0]));
        ParamUtil.setRequestAttr(request,"insAppStatus",inspectionAppStatusOption);
    }
}
