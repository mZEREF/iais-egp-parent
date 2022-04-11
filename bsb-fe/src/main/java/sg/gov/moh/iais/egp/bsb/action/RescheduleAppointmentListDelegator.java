package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import lombok.extern.slf4j.Slf4j;
import sg.gov.moh.iais.egp.bsb.client.BsbAppointmentClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentViewDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.ApptSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.SearchResultDto;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_ADDITIONAL;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.KEY_ACTION_VALUE;

@Slf4j
@Delegator("rescheduleAppointmentListDelegator")
public class RescheduleAppointmentListDelegator {
    private final BsbAppointmentClient bsbAppointmentClient;

    public RescheduleAppointmentListDelegator(BsbAppointmentClient bsbAppointmentClient) {
        this.bsbAppointmentClient = bsbAppointmentClient;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_APPOINTMENT_SEARCH_DTO);
        request.getSession().removeAttribute(KEY_APPOINTMENT_LIST_DATA_LIST);
    }

    public void prepareData(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction(MODULE_NAME, "Reschedule Appointments");

        HttpServletRequest request = bpc.request;
        ApptSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_APPOINTMENT_SEARCH_DTO, searchDto);

        ResponseDto<SearchResultDto> resultDto = bsbAppointmentClient.searchRescheduleAppt(searchDto);

        if (resultDto.ok()) {
            List<AppointmentViewDto> appointmentDtoList = resultDto.getEntity().getAppointmentDtos();
            ParamUtil.setRequestAttr(request, KEY_APPOINTMENT_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setSessionAttr(request, KEY_APPOINTMENT_LIST_DATA_LIST, (Serializable) appointmentDtoList);
        } else {
            log.info("Search Task List fail");
            ParamUtil.setRequestAttr(request, KEY_APPOINTMENT_PAGE_INFO, PageInfo.emptyPageInfo());
            ParamUtil.setRequestAttr(request, KEY_APPOINTMENT_LIST_DATA_LIST, new ArrayList<>());
            if(ERROR_CODE_VALIDATION_FAIL.equals(resultDto.getErrorCode())) {
                log.warn("Fail reason: {}", resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG));
            }
        }
        List<SelectOption> inspectionAppStatusOption = MasterCodeUtil.retrieveOptionsByCodes(MasterCodeConstants.INSPECTION_APP_STATUS.toArray(new String[0]));
        ParamUtil.setRequestAttr(request,"insAppStatus",inspectionAppStatusOption);
    }

    public void sort(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApptSearchDto searchDto = getSearchDto(request);
        String field = ParamUtil.getString(request, KEY_ACTION_VALUE);
        String sortType = ParamUtil.getString(request, KEY_ACTION_ADDITIONAL);
        searchDto.changeSort(field, sortType);
        ParamUtil.setSessionAttr(request, KEY_APPOINTMENT_SEARCH_DTO, searchDto);
    }

    public void page(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApptSearchDto searchDto = getSearchDto(request);
        String actionValue = ParamUtil.getString(request, KEY_ACTION_VALUE);
        switch (actionValue) {
            case "changeSize":
                int pageSize = ParamUtil.getInt(request, KEY_PAGE_SIZE);
                searchDto.setPage(0);
                searchDto.setSize(pageSize);
                break;
            case "changePage":
                int pageNo = ParamUtil.getInt(request, KEY_PAGE_NO);
                searchDto.setPage(pageNo - 1);
                break;
            default:
                log.warn("page, action_value is invalid: {}", actionValue);
                break;
        }
        ParamUtil.setSessionAttr(request, KEY_APPOINTMENT_SEARCH_DTO, searchDto);
    }

    private ApptSearchDto getSearchDto(HttpServletRequest request) {
        ApptSearchDto searchDto = (ApptSearchDto) ParamUtil.getSessionAttr(request, KEY_APPOINTMENT_SEARCH_DTO);
        return searchDto == null ? getDefaultSearchDto() : searchDto;
    }

    private ApptSearchDto getDefaultSearchDto() {
        ApptSearchDto dto = new ApptSearchDto();
        dto.defaultPaging();
        return dto;
    }
}
