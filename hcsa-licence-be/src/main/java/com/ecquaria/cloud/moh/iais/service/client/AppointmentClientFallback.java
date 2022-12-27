package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonAvailabilityDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.KpiCountDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2020-01-06 15:15
 **/
public class AppointmentClientFallback implements AppointmentClient{
    @Override
    public FeignResponseEntity<List<PublicHolidayDto>> getActiveHoliday() {
        return IaisEGPHelper.getFeignResponseEntity("getActiveHoliday");
    }

    @Override
    public FeignResponseEntity<List<ApptBlackoutDateDto>> getAllByShortName(String shortName) {
        return IaisEGPHelper.getFeignResponseEntity("getAllByShortName", shortName);
    }

    @Override
    public FeignResponseEntity<List<ApptNonWorkingDateDto>> getNonWorkingDateListByWorkGroupId(String iaisClientKey, String groupId) {
        return IaisEGPHelper.getFeignResponseEntity("getNonWorkingDateListByWorkGroupId", iaisClientKey, groupId);
    }

    @Override
    public FeignResponseEntity<ApptNonAvailabilityDateDto> createNonAvailability(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto) {
        return IaisEGPHelper.getFeignResponseEntity("createNonAvailability", apptNonAvailabilityDateDto);
    }

    @Override
    public FeignResponseEntity<ApptNonAvailabilityDateDto> updateNonAvailability(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateNonAvailability", apptNonAvailabilityDateDto);
    }

    @Override
    public FeignResponseEntity<ApptNonAvailabilityDateDto> getNonAvailabilityById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getNonAvailabilityById", id);
    }

    @Override
    public FeignResponseEntity<String> dateIsContainNonWork(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto) {
        return IaisEGPHelper.getFeignResponseEntity("dateIsContainNonWork", apptNonAvailabilityDateDto);
    }

    @Override
    public FeignResponseEntity<List<String>> getIdByAgencyUserId(String userId) {
        return IaisEGPHelper.getFeignResponseEntity("getIdByAgencyUserId", userId);
    }

    @Override
    public FeignResponseEntity<List<ApptRequestDto>> getUserCalendarByUserId(AppointmentDto appointmentDto) {
        return IaisEGPHelper.getFeignResponseEntity("getUserCalendarByUserId", appointmentDto);
    }

    @Override
    public FeignResponseEntity<String> isAvailableAppointmentDates(String strSpecDate) {
        return IaisEGPHelper.getFeignResponseEntity("isAvailableAppointmentDates", strSpecDate);
    }

    @Override
    public FeignResponseEntity<Map<Integer, Integer>> getWorkAndNonMap(KpiCountDto kpiCountDto) {
        return IaisEGPHelper.getFeignResponseEntity("getWorkAndNonMap", kpiCountDto);
    }

    @Override
    public FeignResponseEntity<Map<String, List<String>>> validateUserCalendar(AppointmentDto appointmentDto) {
        return IaisEGPHelper.getFeignResponseEntity("validateUserCalendar", appointmentDto);
    }

    @Override
    public FeignResponseEntity<String> saveManualUserCalendar(AppointmentDto appointmentDto) {
        return IaisEGPHelper.getFeignResponseEntity("saveManualUserCalendar", appointmentDto);
    }

    @Override
    public FeignResponseEntity<Void> updateUserCalendarStatus(ApptCalendarStatusDto apptCalDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateUserCalendarStatus", apptCalDto);
    }

    @Override
    public FeignResponseEntity<List<Date>> getHolidays() {
        return IaisEGPHelper.getFeignResponseEntity("getHolidays");
    }

    @Override
    public FeignResponseEntity<List<ApptUserCalendarDto>> getCalenderByApptRefNoAndStatus(ApptUserCalendarDto apptUserCalendarDto) {
        return IaisEGPHelper.getFeignResponseEntity("getCalenderByApptRefNoAndStatus", apptUserCalendarDto);
    }

    @Override
    public FeignResponseEntity<List<ApptUserCalendarDto>> getCalenderByApptRefNoAndStatusOrderByTimeSlot(ApptUserCalendarDto apptUserCalendarDto) {
        return IaisEGPHelper.getFeignResponseEntity("getCalenderByApptRefNoAndStatusOrderByTimeSlot", apptUserCalendarDto);
    }
}
