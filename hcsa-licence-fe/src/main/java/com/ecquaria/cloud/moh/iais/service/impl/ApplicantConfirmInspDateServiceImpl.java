package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicantConfirmInspDateService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionFeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/2/15 17:21
 **/
@Service
@Slf4j
public class ApplicantConfirmInspDateServiceImpl implements ApplicantConfirmInspDateService {

    @Autowired
    private InspectionFeClient inspectionFeClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private LicenceClient licenceClient;

    /**
     *System Date
     */
    @Override
    public ApptFeConfirmDateDto getApptSystemDate(String appPremCorrId) {
        ApptFeConfirmDateDto apptFeConfirmDateDto = new ApptFeConfirmDateDto();
        if(!StringUtil.isEmpty(appPremCorrId)) {
            ApplicationDto applicationDto = applicationClient.getApplicationByCorreId(appPremCorrId).getEntity();
            apptFeConfirmDateDto.setApplicationDto(applicationDto);
            List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = inspectionFeClient.getSystemDtosByAppPremCorrId(appPremCorrId).getEntity();
            if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtoList)){
                List<String> apptRefNos = new ArrayList<>();
                for(AppPremisesInspecApptDto aDto : appPremisesInspecApptDtoList){
                    apptRefNos.add(aDto.getApptRefNo());
                }

            }
            apptFeConfirmDateDto.setAppPremisesInspecApptDtoList(appPremisesInspecApptDtoList);
            apptFeConfirmDateDto.setAppPremisesInspecApptDto(appPremisesInspecApptDtoList.get(0));
        }
        return apptFeConfirmDateDto;
    }

    @Override
    public void confirmInspectionDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
        String checkDate = apptFeConfirmDateDto.getCheckDate();
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = new ArrayList<>();
        for(AppPremisesInspecApptDto apptDto : apptFeConfirmDateDto.getAppPremisesInspecApptDtoList()){
            if(!(apptDto.getId().equals(checkDate))){
                appPremisesInspecApptDtoList.add(apptDto);
            }
        }
        apptFeConfirmDateDto.setAppPremisesInspecApptDtoList(appPremisesInspecApptDtoList);
        setApptUpdateList(apptFeConfirmDateDto, apptInspectionDateDto);
        setUpdateApplicationDto(apptFeConfirmDateDto, apptInspectionDateDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
        setCreateHistoryDto(apptFeConfirmDateDto, apptInspectionDateDto);
        setCreateInspectionStatus(apptInspectionDateDto, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
    }

    @Override
    public ApptFeConfirmDateDto getApptNewSystemDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {

        return apptFeConfirmDateDto;
    }

    @Override
    public ApptFeConfirmDateDto confirmNewDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
        Map<String, Date> newDateMap = apptFeConfirmDateDto.getInspectionNewDateMap();
        Date checkDate = newDateMap.get(apptFeConfirmDateDto.getCheckNewDate());
        //todo set refNo
        setApptUpdateList(apptFeConfirmDateDto, apptInspectionDateDto);
        apptFeConfirmDateDto.setSaveDate(checkDate);
        setApptCreateList(apptFeConfirmDateDto, apptInspectionDateDto);
        setUpdateApplicationDto(apptFeConfirmDateDto, apptInspectionDateDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
        setCreateHistoryDto(apptFeConfirmDateDto, apptInspectionDateDto);
        setCreateInspectionStatus(apptInspectionDateDto, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
        return apptFeConfirmDateDto;
    }

    @Override
    public void saveAccSpecificDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
        setUpdateApplicationDto(apptFeConfirmDateDto, apptInspectionDateDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
        setCreateHistoryDto(apptFeConfirmDateDto, apptInspectionDateDto);
        setCreateInspectionStatus(apptInspectionDateDto, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
    }

    private void setCreateInspectionStatus(ApptInspectionDateDto apptInspectionDateDto, String status) {
        AppInspectionStatusDto appInspectionStatusDto = new AppInspectionStatusDto();
        appInspectionStatusDto.setStatus(status);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        apptInspectionDateDto.setAppInspectionStatusDto(appInspectionStatusDto);
    }

    private void setCreateHistoryDto(ApptFeConfirmDateDto apptFeConfirmDateDto, ApptInspectionDateDto apptInspectionDateDto) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(apptFeConfirmDateDto.getApplicationNo());
        appPremisesRoutingHistoryDto.setSubStage(HcsaConsts.ROUTING_STAGE_PRE);
        appPremisesRoutingHistoryDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
        appPremisesRoutingHistoryDto.setAppStatus(apptFeConfirmDateDto.getApplicationDto().getStatus());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        apptInspectionDateDto.setAppPremisesRoutingHistoryDto(appPremisesRoutingHistoryDto);
    }

    private void setUpdateApplicationDto(ApptFeConfirmDateDto apptFeConfirmDateDto, ApptInspectionDateDto apptInspectionDateDto, String status) {
        ApplicationDto applicationDto = apptFeConfirmDateDto.getApplicationDto();
        applicationDto.setStatus(status);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        applicationDto = applicationClient.updateApplication(applicationDto).getEntity();
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        apptInspectionDateDto.setApplicationDto(applicationDto);
    }

    private void setApptCreateList(ApptFeConfirmDateDto apptFeConfirmDateDto, ApptInspectionDateDto apptInspectionDateDto) {
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = new ArrayList<>();
        AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
        appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesInspecApptDto.setId(null);
        appPremisesInspecApptDto.setSpecificInspDate(apptFeConfirmDateDto.getSaveDate());
        appPremisesInspecApptDto.setApptRefNo(apptFeConfirmDateDto.getApptRefNo());
        appPremisesInspecApptDto.setAppCorrId(apptFeConfirmDateDto.getAppPremCorrId());
        appPremisesInspecApptDto.setStartDate(apptFeConfirmDateDto.getAppPremisesInspecApptDtoList().get(0).getStartDate());
        appPremisesInspecApptDto.setEndDate(apptFeConfirmDateDto.getAppPremisesInspecApptDtoList().get(0).getEndDate());
        appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);
        appPremisesInspecApptDtoList = inspectionFeClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoList).getEntity();
        for(AppPremisesInspecApptDto apptDto : appPremisesInspecApptDtoList){
            apptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        apptInspectionDateDto.setAppPremisesInspecApptCreateList(appPremisesInspecApptDtoList);
    }

    private void setApptUpdateList(ApptFeConfirmDateDto apptFeConfirmDateDto, ApptInspectionDateDto apptInspectionDateDto) {
        for(AppPremisesInspecApptDto apptDto : apptFeConfirmDateDto.getAppPremisesInspecApptDtoList()){
            apptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            apptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = inspectionFeClient.updateAppPremisesInspecApptDtoList(apptFeConfirmDateDto.getAppPremisesInspecApptDtoList()).getEntity();
        if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtoList)){
            for(AppPremisesInspecApptDto appPremisesInspecApptDto : appPremisesInspecApptDtoList){
                appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            }
            apptInspectionDateDto.setAppPremisesInspecApptUpdateList(appPremisesInspecApptDtoList);
        }
    }

    @Override
    public List<SelectOption> getInspectionDateHours() {
        List<SelectOption> hourOption = new ArrayList<>();
        for(int i = 1; i < 13; i++){
            SelectOption so = new SelectOption(i + "", i + "");
            hourOption.add(so);
        }
        return hourOption;
    }

    @Override
    public List<SelectOption> getAmPmOption() {
        List<SelectOption> amPmOption = new ArrayList<>();
        SelectOption so1 = new SelectOption(Formatter.DAY_AM, "am");
        SelectOption so2 = new SelectOption(Formatter.DAY_PM, "pm");
        amPmOption.add(so1);
        amPmOption.add(so2);
        return amPmOption;
    }

    @Override
    public void rejectSystemDateAndCreateTask(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
        setApptUpdateList(apptFeConfirmDateDto, apptInspectionDateDto);
        setApptCreateList(apptFeConfirmDateDto, apptInspectionDateDto);
        setUpdateApplicationDto(apptFeConfirmDateDto, apptInspectionDateDto, ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING);
        setCreateHistoryDto(apptFeConfirmDateDto, apptInspectionDateDto);
        setCreateInspectionStatus(apptInspectionDateDto, InspectionConstants.INSPECTION_STATUS_PENDING_RE_APPOINTMENT_INSPECTION_DATE);
    }

    /**
     * Specific Date
     */
    @Override
    public ApptFeConfirmDateDto getSpecificDateDto(String appPremCorrId) {
        ApptFeConfirmDateDto apptFeConfirmDateDto = new ApptFeConfirmDateDto();
        if(!StringUtil.isEmpty(appPremCorrId)){
            AppPremisesInspecApptDto appPremisesInspecApptDto = inspectionFeClient.getSpecificDtoByAppPremCorrId(appPremCorrId).getEntity();
            ApplicationDto applicationDto = applicationClient.getApplicationByCorreId(appPremCorrId).getEntity();
            LicenceDto licenceDto = licenceClient.getLicBylicId(applicationDto.getLicenceId()).getEntity();
            if(appPremisesInspecApptDto != null){
                apptFeConfirmDateDto.setSpecificDate(appPremisesInspecApptDto.getSpecificInspDate());
                apptFeConfirmDateDto.setApptStartDate(appPremisesInspecApptDto.getStartDate());
                apptFeConfirmDateDto.setApptEndDate(appPremisesInspecApptDto.getEndDate());
                apptFeConfirmDateDto.setAppPremCorrId(appPremCorrId);
                apptFeConfirmDateDto.setApptRefNo(appPremisesInspecApptDto.getApptRefNo());
                apptFeConfirmDateDto.setApplicationNo(applicationDto.getApplicationNo());
                apptFeConfirmDateDto.setApplicationType(applicationDto.getApplicationType());

                SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                String licenceStartDate = format.format(licenceDto.getStartDate());
                String licenceEndDate = format.format(licenceDto.getEndDate());
                String licensePeriod = licenceStartDate + " - " + licenceEndDate;
                apptFeConfirmDateDto.setLicencePeriod(licensePeriod);
                String specificDate = apptDateToStringShow(appPremisesInspecApptDto.getSpecificInspDate());
                apptFeConfirmDateDto.setSpecificDateShow(specificDate);
            }
        }
        return apptFeConfirmDateDto;
    }

    @Override
    public void rejectSpecificDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = new ArrayList<>();
        AppPremisesInspecApptDto appPremisesInspecApptDto = apptFeConfirmDateDto.getAppPremisesInspecApptDto();
        appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
        appPremisesInspecApptDto = inspectionFeClient.updateAppPremisesInspecApptDto(appPremisesInspecApptDto).getEntity();
        appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);
        apptInspectionDateDto.setAppPremisesInspecApptUpdateList(appPremisesInspecApptDtoList);
        setUpdateApplicationDto(apptFeConfirmDateDto, apptInspectionDateDto, ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING);
        setCreateHistoryDto(apptFeConfirmDateDto, apptInspectionDateDto);
        setCreateInspectionStatus(apptInspectionDateDto, InspectionConstants.INSPECTION_STATUS_PENDING_RE_APPOINTMENT_INSPECTION_DATE);
    }

    private String apptDateToStringShow(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("d MMM");
        String specificDate = format.format(date);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int curHour24 = cal.get(Calendar.HOUR_OF_DAY);
        if(curHour24 > 12){
            int hours = curHour24 - 12;
            specificDate = specificDate + " " + hours + ":00" + "PM";
        } else {
            specificDate = specificDate + " " + curHour24 + ":00" + "AM";
        }
        return specificDate;
    }
}
