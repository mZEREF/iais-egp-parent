package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.LogUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbAppointmentClient;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.ApptSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.BsbAppointmentDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.BsbAppointmentUserDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.SearchResultDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.doreschedule.OfficerRescheduleDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.ApptInspectionDateService;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_CODE_VALIDATION_FAIL;
import static sg.gov.moh.iais.egp.bsb.constant.ResponseConstants.ERROR_INFO_ERROR_MSG;
import static sg.gov.moh.iais.egp.bsb.constant.module.ModuleCommonConstants.*;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.KEY_TASK_LIST_DATA_LIST;
import static sg.gov.moh.iais.egp.bsb.constant.module.TaskModuleConstants.KEY_TASK_LIST_PAGE_INFO;


@Slf4j
@Delegator("bsbRescheduleApptDelegator")
public class BsbRescheduleApptDelegator {
    public static final String KEY_SEARCH_DTO = "searchDto";
    public static final String OFFICER_RESCHEDULE_DTO = "officerRescheduleDto";

    private final BsbAppointmentClient bsbAppointmentClient;
    private final ApptInspectionDateService apptInspectionDateService;

    public BsbRescheduleApptDelegator(BsbAppointmentClient bsbAppointmentClient, ApptInspectionDateService apptInspectionDateService) {
        this.bsbAppointmentClient = bsbAppointmentClient;
        this.apptInspectionDateService = apptInspectionDateService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        request.getSession().removeAttribute(KEY_SEARCH_DTO);

        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_RESCHEDULE);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApptSearchDto searchDto = getSearchDto(request);
        ParamUtil.setSessionAttr(request, KEY_SEARCH_DTO, searchDto);

        ResponseDto<SearchResultDto> resultDto = bsbAppointmentClient.searchRescheduleAppt(searchDto);

        if (resultDto.ok()) {
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_PAGE_INFO, resultDto.getEntity().getPageInfo());
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_DATA_LIST, resultDto.getEntity().getAppointmentViewDtos());
        } else {
            log.info("Search Reschedule Appointment List fail");
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_PAGE_INFO, PageInfo.emptyPageInfo(searchDto));
            ParamUtil.setRequestAttr(request, KEY_TASK_LIST_DATA_LIST, new ArrayList<>());
            if (ERROR_CODE_VALIDATION_FAIL.equals(resultDto.getErrorCode())) {
                log.warn("Fail reason: {}", resultDto.getErrorInfos().get(ERROR_INFO_ERROR_MSG));
            }
        }
        ParamUtil.setRequestAttr(request, BACK_URL, BACK_URL_TASK_LIST);
    }

    public void search(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ApptSearchDto searchDto = bindModel(request, getSearchDto(request));
        searchDto.setPage(0);
        ParamUtil.setSessionAttr(request, KEY_SEARCH_DTO, searchDto);
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
        ParamUtil.setSessionAttr(request, KEY_SEARCH_DTO, searchDto);
    }

    private ApptSearchDto getSearchDto(HttpServletRequest request) {
        ApptSearchDto dto = (ApptSearchDto) ParamUtil.getSessionAttr(request, KEY_SEARCH_DTO);
        if (dto == null) {
            dto = new ApptSearchDto();
            dto.defaultPaging();

            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            dto.setUserId(loginContext.getUserId());
        }
        return dto;
    }

    private static ApptSearchDto bindModel(HttpServletRequest request, ApptSearchDto dto) {
        dto.setSearchAppNo(request.getParameter("searchAppNo"));
        dto.setSearchAppType(request.getParameter("searchAppType"));
        dto.setSearchAppStatus(request.getParameter("searchAppStatus"));

        if (dto.getUserId() == null) {
            LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
            dto.setUserId(loginContext.getUserId());
        }
        return dto;
    }

    public void prepareRescheduleData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerRescheduleDto dto = getRescheduleDto(request);
        if (!StringUtils.hasLength(dto.getAppId())) {
            String maskedAppId = ParamUtil.getString(request, "maskedAppId");
            if (log.isInfoEnabled()) {
                log.info("masked application id: [{}]", LogUtil.escapeCrlf(maskedAppId));
            }
            String appId = MaskUtil.unMaskValue("maskedAppId", maskedAppId);
            if (appId == null || appId.equals(maskedAppId)) {
                throw new IaisRuntimeException("Invalid masked application ID");
            }
            dto.setAppId(appId);
            apptInspectionDateService.setInspectionInfoDto(appId,request);
        }
        dto = apptInspectionDateService.getReScheduleNewDateInfo(request,dto);
        ParamUtil.setSessionAttr(request,OFFICER_RESCHEDULE_DTO,dto);
        ParamUtil.setRequestAttr(request, BACK_URL, BACK_URL_RESCHEDULE_APPOINTMENT);
    }

    public void preSwitch(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        OfficerRescheduleDto dto = getRescheduleDto(request);
        String processDec = ParamUtil.getRequestString(request, "processDec");
        dto.setProcessDec(processDec);
        ParamUtil.setSessionAttr(request,OFFICER_RESCHEDULE_DTO,dto);
    }

    public void specificDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        List<SelectOption> startHours = apptInspectionDateService.getInspectionDateStartHours();
        List<SelectOption> endHours = apptInspectionDateService.getInspectionDateEndHours();
        ParamUtil.setSessionAttr(request, KEY_START_HOURS_OPTION, (Serializable) startHours);
        ParamUtil.setSessionAttr(request, KEY_END_HOURS_OPTION, (Serializable) endHours);
    }

    public void validateFormData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        OfficerRescheduleDto rescheduleDto = getRescheduleDto(request);
        BsbAppointmentDto appointmentDto = rescheduleDto.getAppointmentDto();
        List<String> userIds = rescheduleDto.getUserIds();
        List<BsbAppointmentUserDto> appointmentUserDtos = new ArrayList<>();
        for (String userId : userIds) {
            BsbAppointmentUserDto appointmentUserDto = new BsbAppointmentUserDto();
            appointmentUserDto.setLoginUserId(userId);
            appointmentUserDto.setWorkHours(8);
            //
            appointmentUserDto.setWorkGrpName("BSB-Inspect");
            appointmentUserDtos.add(appointmentUserDto);
        }
        appointmentDto.setUsers(appointmentUserDtos);
        appointmentDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        appointmentDto = apptInspectionDateService.getRescheduleValidateValue(rescheduleDto,request);

        //validation
        rescheduleDto.setModule("rescheduleSpecifyDate");
        ValidationResultDto validationResultDto = bsbAppointmentClient.validateRescheduleData(rescheduleDto);
        String actionType = apptInspectionDateService.ensureValResult(actionValue,validationResultDto,request,appointmentDto);
        ParamUtil.setRequestAttr(request, KEY_ACTION_TYPE, actionType);
        ParamUtil.setSessionAttr(request,OFFICER_RESCHEDULE_DTO,rescheduleDto);
    }

    public OfficerRescheduleDto getRescheduleDto(HttpServletRequest request){
        OfficerRescheduleDto dto = (OfficerRescheduleDto) ParamUtil.getSessionAttr(request,OFFICER_RESCHEDULE_DTO);
        if (dto == null){
            dto = new OfficerRescheduleDto();
        }
        return dto;
    }

    public void saveRescheduleDate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InspectionInfoDto inspectionInfoDto = (InspectionInfoDto) ParamUtil.getSessionAttr(request, INSPECTION_INFO_DTO);
        OfficerRescheduleDto dto = getRescheduleDto(request);
        if (PROCESS_DEC_SPECIFY_NEW_DATE.equals(dto.getProcessDec())) {
            log.info("user specify date");
            apptInspectionDateService.saveRescheduleSpecificDate(dto,inspectionInfoDto);
        } else if (PROCESS_DEC_CONFIRM_DATE.equals(dto.getProcessDec())) {
            apptInspectionDateService.saveRescheduleSystemInspectionDate(request,inspectionInfoDto);
        }
        ParamUtil.setRequestAttr(request, BACK_URL, BACK_URL_RESCHEDULE_APPOINTMENT);
    }
}
