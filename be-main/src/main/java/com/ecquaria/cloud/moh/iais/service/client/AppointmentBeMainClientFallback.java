package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonAvailabilityDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2020-01-06 15:15
 **/
public class AppointmentBeMainClientFallback implements AppointmentBeMainClient {
    @Override
    public FeignResponseEntity<List<PublicHolidayDto>> getActiveHoliday() {
        return IaisEGPHelper.getFeignResponseEntity("getActiveHoliday");
    }

    @Override
    public FeignResponseEntity<List<ApptBlackoutDateDto>> getAllByShortName(String shortName) {
        return IaisEGPHelper.getFeignResponseEntity("getAllByShortName",shortName);
    }

    @Override
    public FeignResponseEntity<List<ApptNonWorkingDateDto>> getNonWorkingDateListByWorkGroupId(String groupId) {
        return IaisEGPHelper.getFeignResponseEntity("getNonWorkingDateListByWorkGroupId",groupId);
    }

    @Override
    public FeignResponseEntity<ApptNonAvailabilityDateDto> createNonAvailability(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto) {
        return IaisEGPHelper.getFeignResponseEntity("createNonAvailability",apptNonAvailabilityDateDto);
    }

    @Override
    public FeignResponseEntity<ApptNonAvailabilityDateDto> updateNonAvailability(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateNonAvailability",apptNonAvailabilityDateDto);
    }

    @Override
    public FeignResponseEntity<ApptNonAvailabilityDateDto> getNonAvailabilityById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getNonAvailabilityById",id);
    }

    @Override
    public FeignResponseEntity<String> dateIsContainNonWork(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto) {
        return IaisEGPHelper.getFeignResponseEntity("dateIsContainNonWork",apptNonAvailabilityDateDto);
    }

    @Override
    public FeignResponseEntity<Map<Integer, Integer>> getWorkAndNonMap(List<Date> dates) {
        return IaisEGPHelper.getFeignResponseEntity("getWorkAndNonMap",dates);
    }

    @Override
    public FeignResponseEntity<Void> cancelTemp(@PathVariable("hours") int hours) {
        return IaisEGPHelper.getFeignResponseEntity("cancelTemp",hours);
    }
}
