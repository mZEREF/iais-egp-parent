package sg.gov.moh.iais.egp.bsb.ajax;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.egp.bsb.client.AppointmentClient;
import sg.gov.moh.iais.egp.bsb.client.BsbAppointmentClient;
import sg.gov.moh.iais.egp.bsb.client.OrganizationClient;
import sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentReviewDataDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.BsbAppointmentDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.BsbAppointmentUserDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.BsbApptInspectionDateDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionAppointmentDraftDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.TaskDto;
import sg.gov.moh.iais.egp.bsb.service.ApptInspectionDateService;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.*;

@Slf4j
@Controller
@RequestMapping("/online-appt")
public class OnlineApptAjaxController {


    private final BsbAppointmentClient bsbAppointmentClient;
    private final OrganizationClient organizationClient;
    private final AppointmentClient appointmentClient;
    private final ApptInspectionDateService apptInspectionDateService;

    public OnlineApptAjaxController(BsbAppointmentClient bsbAppointmentClient, OrganizationClient organizationClient, AppointmentClient appointmentClient, ApptInspectionDateService apptInspectionDateService) {
        this.bsbAppointmentClient = bsbAppointmentClient;
        this.organizationClient = organizationClient;
        this.appointmentClient = appointmentClient;
        this.apptInspectionDateService = apptInspectionDateService;
    }

    @PostMapping(value = "insp.date")
    public @ResponseBody
    Map<String, Object> getInspectionDate(HttpServletRequest request) {
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(3);
        BsbApptInspectionDateDto bsbApptInspectionDateDto = (BsbApptInspectionDateDto) ParamUtil.getSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO);
        List<InspectionAppointmentDraftDto> appPremInspApptDraftDtos = (List<InspectionAppointmentDraftDto>) ParamUtil.getSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DRAFT_DTO);
        boolean newInspDateFlag = getNewInspDateFlag(appPremInspApptDraftDtos);
        if (bsbApptInspectionDateDto != null && CollectionUtils.isEmpty(bsbApptInspectionDateDto.getInspectionDate())) {
            AppointmentReviewDataDto dto = (AppointmentReviewDataDto) ParamUtil.getSessionAttr(request, APPOINTMENT_REVIEW_DATA);
            String actionButtonFlag = bsbApptInspectionDateDto.getActionButtonFlag();
            //get inspection date
            if (AppConsts.SUCCESS.equals(actionButtonFlag)) {
                BsbAppointmentDto bsbAppointmentDto = bsbAppointmentClient.getApptStartEndDateByAppId(dto.getApplicationId()).getEntity();
                //Compares user specified time with the current time
                apptInspectionDateService.setStartEndDateNull(bsbAppointmentDto);
                //set system key
                bsbAppointmentDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
                //get Start date and End date when application no date
                apptInspectionDateService.setStartDtAndEndDt(bsbAppointmentDto);
                //get inspection date
                List<TaskDto> taskDtoList = bsbApptInspectionDateDto.getTaskDtos();
                //set manHours and inspectors
                List<BsbAppointmentUserDto> bsbAppointmentUserDtos = new ArrayList<>();
                getManHoursInspectors(bsbAppointmentUserDtos, taskDtoList);
                //If one person is doing multiple services at the same time, The superposition of time
                bsbAppointmentUserDtos = getOnePersonBySomeService(bsbAppointmentUserDtos);
                bsbAppointmentDto.setUsers(bsbAppointmentUserDtos);
                bsbApptInspectionDateDto.setBsbAppointmentDto(bsbAppointmentDto);
                boolean dateFlag = apptInspectionDateService.getStartEndDateFlag(bsbAppointmentDto);
                if (dateFlag && newInspDateFlag) {
                    //set Inspection date show, flag,
                    getNewInspDateData(bsbApptInspectionDateDto, bsbAppointmentDto, map, request, taskDtoList);
                } else if (!newInspDateFlag) {
                    //get Inspection date Draft
                    setInspDateDraftData(bsbApptInspectionDateDto, appPremInspApptDraftDtos);
                    bsbApptInspectionDateDto.setSysInspDateFlag(AppConsts.TRUE);
                    bsbApptInspectionDateDto.setSysSpecDateFlag(AppConsts.TRUE);
                    map.put(BUTTON_FLAG, AppConsts.TRUE);
                    map.put(SPEC_BUTTON_FLAG, AppConsts.TRUE);
                    map.put(INSP_DATE_LIST, bsbApptInspectionDateDto.getInspectionDate());
                } else {
                    map.put(BUTTON_FLAG, AppConsts.FALSE);
                    map.put(SPEC_BUTTON_FLAG, AppConsts.TRUE);
                    map.put(INSP_DATE_LIST, null);
                }
                //specific date dto
                BsbAppointmentDto bsbSpecificApptDto = new BsbAppointmentDto();
                bsbSpecificApptDto.setSubmitDt(bsbAppointmentDto.getSubmitDt());
                bsbSpecificApptDto.setUsers(bsbAppointmentDto.getUsers());
                bsbSpecificApptDto.setSysClientKey(bsbAppointmentDto.getSysClientKey());
                bsbApptInspectionDateDto.setBsbSpecificApptDto(bsbSpecificApptDto);
                ParamUtil.setSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO, bsbApptInspectionDateDto);
            }
        }
        return map;
    }

    private void setInspDateDraftData(BsbApptInspectionDateDto bsbApptInspectionDateDto, List<InspectionAppointmentDraftDto> appPremInspApptDraftDtos) {
        if (!IaisCommonUtils.isEmpty(appPremInspApptDraftDtos)) {
            log.info(StringUtil.changeForLog("Inspection Scheduling appPremInspApptDraftDtos Size ====" + appPremInspApptDraftDtos.size()));
            Map<String, List<ApptUserCalendarDto>> inspectionDateMap = new LinkedHashMap<>(4);
            for (InspectionAppointmentDraftDto appPremInspApptDraftDto : appPremInspApptDraftDtos) {
                if (appPremInspApptDraftDto != null) {
                    String apptRefNo = appPremInspApptDraftDto.getApptRefNo();
                    List<ApptUserCalendarDto> apptUserCalendarDtos = inspectionDateMap.computeIfPresent(apptRefNo, (s, apptUserCalendarDtos1) -> new ArrayList<>(1));
                    if (CollectionUtils.isEmpty(apptUserCalendarDtos)) {
                        apptUserCalendarDtos = new ArrayList<>();
                    }
                    ApptUserCalendarDto apptUserCalendarDto = new ApptUserCalendarDto();
                    List<Date> startSlot = new ArrayList<>(1);
                    List<Date> endSlot = new ArrayList<>(1);
                    Date inspStartDate = appPremInspApptDraftDto.getStartDate();
                    startSlot.add(inspStartDate);
                    Date inspEndDate = appPremInspApptDraftDto.getEndDate();
                    endSlot.add(inspEndDate);
                    OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(appPremInspApptDraftDto.getUserId()).getEntity();
                    //set
                    apptUserCalendarDto.setAppNo(appPremInspApptDraftDto.getApplicationNo());
                    apptUserCalendarDto.setApptRefNo(apptRefNo);
                    apptUserCalendarDto.setStartSlot(startSlot);
                    apptUserCalendarDto.setEndSlot(endSlot);
                    apptUserCalendarDto.setLoginUserId(orgUserDto.getUserId());
                    apptUserCalendarDtos.add(apptUserCalendarDto);
                    inspectionDateMap.put(apptRefNo, apptUserCalendarDtos);
                }
            }
            //set insp date string show
            setInspDateDraftStrShow(appPremInspApptDraftDtos, bsbApptInspectionDateDto);
            bsbApptInspectionDateDto.setInspectionDateMap(inspectionDateMap);
        }
    }

    private void setInspDateDraftStrShow(List<InspectionAppointmentDraftDto> appPremInspApptDraftDtos, BsbApptInspectionDateDto bsbApptInspectionDateDto) {
        InspectionAppointmentDraftDto appPremInspApptDraftDto = appPremInspApptDraftDtos.get(0);
        Date inspStartDate = appPremInspApptDraftDto.getStartDate();
        Date inspEndDate = appPremInspApptDraftDto.getEndDate();
        //date to str
        String inspStartDateStr = apptDateToStringShow(inspStartDate);
        String inspEndDateStr = apptDateToStringShow(inspEndDate);
        String inspectionDate = inspStartDateStr + " - " + inspEndDateStr;
        List<String> inspectionDates = new ArrayList<>(1);
        inspectionDates.add(inspectionDate);
        bsbApptInspectionDateDto.setInspectionDate(inspectionDates);
    }

    private void getNewInspDateData(BsbApptInspectionDateDto bsbApptInspectionDateDto, BsbAppointmentDto bsbAppointmentDto, Map<String, Object> map,
                                    HttpServletRequest request, List<TaskDto> taskDtoList) {
        try {
            bsbAppointmentDto.setResultNum(1);
            AppointmentDto appointmentDto = apptInspectionDateService.toAppointmentDto(bsbAppointmentDto);
            FeignResponseEntity<List<ApptRequestDto>> result = appointmentClient.getUserCalendarByUserId(appointmentDto);
            Map<String, Collection<String>> headers = result.getHeaders();
            //Has it been blown up
            if (headers != null && StringUtils.isEmpty(headers.get("fusing"))) {
                List<ApptRequestDto> apptRequestDtos = result.getEntity();
                if (!CollectionUtils.isEmpty(apptRequestDtos)) {
                    Map<String, List<ApptUserCalendarDto>> inspectionDateMap = new LinkedHashMap<>(apptRequestDtos.size());
                    for (ApptRequestDto apptRequestDto : apptRequestDtos) {
                        inspectionDateMap.put(apptRequestDto.getApptRefNo(), apptRequestDto.getUserClandars());
                    }
                    bsbApptInspectionDateDto.setSysInspDateFlag(AppConsts.TRUE);
                    bsbApptInspectionDateDto.setSysSpecDateFlag(AppConsts.TRUE);
                    getShowTimeStringList(inspectionDateMap, bsbApptInspectionDateDto);
                    //save inspection draft
                    List<InspectionAppointmentDraftDto> appPremInspApptDraftDtos = saveInspectionDateDraft(apptRequestDtos, taskDtoList);
                    ParamUtil.setSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DRAFT_DTO, (Serializable) appPremInspApptDraftDtos);
                    map.put(BUTTON_FLAG, AppConsts.TRUE);
                    map.put(SPEC_BUTTON_FLAG, AppConsts.TRUE);
                    map.put(INSP_DATE_LIST, bsbApptInspectionDateDto.getInspectionDate());
                } else {
                    map.put(BUTTON_FLAG, AppConsts.FALSE);
                    map.put(SPEC_BUTTON_FLAG, AppConsts.TRUE);
                    map.put(INSP_DATE_LIST, null);
                    bsbApptInspectionDateDto.setSysSpecDateFlag(AppConsts.TRUE);
                }
            } else {
                map.put(BUTTON_FLAG, AppConsts.FALSE);
                map.put(SPEC_BUTTON_FLAG, AppConsts.TRUE);
                map.put(INSP_DATE_LIST, null);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<InspectionAppointmentDraftDto> saveInspectionDateDraft(List<ApptRequestDto> apptRequestDtos, List<TaskDto> taskDtoList) {
        List<InspectionAppointmentDraftDto> appPremInspApptDraftDtos = new ArrayList<>();
        if (!IaisCommonUtils.isEmpty(taskDtoList) && !IaisCommonUtils.isEmpty(apptRequestDtos)) {
            appPremInspApptDraftDtos = new ArrayList<>(taskDtoList.size());
            List<String> appNoList = new ArrayList<>(taskDtoList.size());
            for (TaskDto taskDto : taskDtoList) {
                for (ApptRequestDto apptRequestDto : apptRequestDtos) {
                    if (taskDto != null && apptRequestDto != null) {
                        //get date
                        int endTimeSize = apptRequestDto.getUserClandars().get(0).getEndSlot().size();
                        Date inspStartDate = apptRequestDto.getUserClandars().get(0).getStartSlot().get(0);
                        Date inspEndDate = apptRequestDto.getUserClandars().get(0).getEndSlot().get(endTimeSize - 1);
                        //set data
                        String applicationNo = taskDto.getApplication().getApplicationNo();
                        if (!appNoList.contains(applicationNo)) {
                            appNoList.add(applicationNo);
                            InspectionAppointmentDraftDto draftDto = new InspectionAppointmentDraftDto();
                            draftDto.setApplicationNo(applicationNo);
                            draftDto.setApptRefNo(apptRequestDto.getApptRefNo());
                            draftDto.setStartDate(inspStartDate);
                            draftDto.setEndDate(inspEndDate);
                            draftDto.setUserId(taskDto.getUserId());
                            draftDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                            appPremInspApptDraftDtos.add(draftDto);
                        }
                    }
                }
            }
            appPremInspApptDraftDtos = bsbAppointmentClient.saveAppointmentDraft(appPremInspApptDraftDtos).getEntity();
        }
        return appPremInspApptDraftDtos;
    }

    private void getManHoursInspectors(List<BsbAppointmentUserDto> bsbAppointmentUserDtos, List<TaskDto> taskDtoList) {
        for (TaskDto tDto : taskDtoList) {
            BsbAppointmentUserDto bsbAppointmentUserDto = new BsbAppointmentUserDto();
            OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(tDto.getUserId()).getEntity();
            bsbAppointmentUserDto.setLoginUserId(orgUserDto.getUserId());
            bsbAppointmentUserDto.setWorkGrpName("BSB-Inspect");
            int manHours = 8;
            //Divide the time according to the number of people
            //get the tasks where status in (pending,read)
            double peopleCount = taskDtoList.size();
            int peopleHours = (int) Math.ceil((double) manHours / peopleCount);
            bsbAppointmentUserDto.setWorkHours(peopleHours);
            bsbAppointmentUserDtos.add(bsbAppointmentUserDto);
        }
    }

    private boolean getNewInspDateFlag(List<InspectionAppointmentDraftDto> appPremInspApptDraftDtos) {
        boolean newInspDateFlag = true;
        if (!CollectionUtils.isEmpty(appPremInspApptDraftDtos)) {
            newInspDateFlag = false;
            Date startDate = appPremInspApptDraftDtos.get(0).getStartDate();
            Date today = new Date();
            if (startDate == null) {
                newInspDateFlag = true;
            } else {
                if (startDate.before(today)) {
                    newInspDateFlag = true;
                }
            }
        }
        return newInspDateFlag;
    }

    private List<BsbAppointmentUserDto> getOnePersonBySomeService(List<BsbAppointmentUserDto> bsbAppointmentUserDtos) {
        List<BsbAppointmentUserDto> bsbAppointmentUserDtoList = null;
        if (!IaisCommonUtils.isEmpty(bsbAppointmentUserDtos)) {
            bsbAppointmentUserDtoList = new ArrayList<>(bsbAppointmentUserDtos.size());
            for (BsbAppointmentUserDto bsbAppointmentUserDto : bsbAppointmentUserDtos) {
                if (bsbAppointmentUserDtoList.isEmpty()) {
                    bsbAppointmentUserDtoList.add(bsbAppointmentUserDto);
                } else {
                    filterRepetitiveUser(bsbAppointmentUserDto, bsbAppointmentUserDtoList);
                }
            }
        }
        return bsbAppointmentUserDtoList;
    }

    private void filterRepetitiveUser(BsbAppointmentUserDto bsbAppointmentUserDto, List<BsbAppointmentUserDto> bsbAppointmentUserDtoList) {
        List<BsbAppointmentUserDto> appointmentUserDtos = new ArrayList<>(bsbAppointmentUserDtoList.size());
        boolean sameUserFlag = false;
        for (BsbAppointmentUserDto appointmentUserDto1 : bsbAppointmentUserDtoList) {
            String loginUserId = bsbAppointmentUserDto.getLoginUserId();
            String curLoginUserId = appointmentUserDto1.getLoginUserId();
            if (loginUserId.equals(curLoginUserId)) {
                int hours = bsbAppointmentUserDto.getWorkHours();
                int curHours = appointmentUserDto1.getWorkHours();
                int allHours = hours + curHours;
                appointmentUserDto1.setWorkHours(allHours);
                sameUserFlag = true;
            }
        }
        if (!sameUserFlag) {
            appointmentUserDtos.add(bsbAppointmentUserDto);
        }
        if (!CollectionUtils.isEmpty(appointmentUserDtos)) {
            for (BsbAppointmentUserDto auDto : appointmentUserDtos) {
                if (auDto != null) {
                    bsbAppointmentUserDtoList.add(auDto);
                }
            }
        }
    }

    private void getShowTimeStringList(Map<String, List<ApptUserCalendarDto>> inspectionDateMap, BsbApptInspectionDateDto bsbApptInspectionDateDto) {
        if (inspectionDateMap != null) {
            List<String> inspectionDates = new ArrayList<>(inspectionDateMap.size());
            for (Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()) {
                List<ApptUserCalendarDto> apptUserCalendarDtos = inspDateMap.getValue();
                int endTimeSize = apptUserCalendarDtos.get(0).getEndSlot().size();
                String inspStartDate = apptDateToStringShow(apptUserCalendarDtos.get(0).getStartSlot().get(0));
                String inspEndDate = apptDateToStringShow(apptUserCalendarDtos.get(0).getEndSlot().get(endTimeSize - 1));
                String inspectionDate = inspStartDate + " - " + inspEndDate;
                inspectionDates.add(inspectionDate);
            }
            bsbApptInspectionDateDto.setInspectionDate(inspectionDates);
            bsbApptInspectionDateDto.setInspectionDateMap(inspectionDateMap);
        } else {
            bsbApptInspectionDateDto.setInspectionDate(null);
            bsbApptInspectionDateDto.setSysInspDateFlag(AppConsts.FALSE);
        }
    }

    private String apptDateToStringShow(Date date) {
        String specificDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int minutes = cal.get(Calendar.MINUTE);
        if (minutes > 0) {
            cal.add(Calendar.HOUR_OF_DAY, 1);
        }
        int curHour24 = cal.get(Calendar.HOUR_OF_DAY);
        String hoursShow = "";
        if (curHour24 < 10) {
            hoursShow = "0";
        }
        specificDate = specificDate + " " + hoursShow + curHour24 + ":00";
        return specificDate;
    }
}