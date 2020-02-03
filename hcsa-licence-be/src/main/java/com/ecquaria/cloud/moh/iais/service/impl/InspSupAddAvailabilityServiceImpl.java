package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonAvailabilityDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.InspSupAddAvailabilityService;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * InspSupAddAvailabilityServiceImpl
 *
 * @author Shicheng
 * @date 2020/1/13 13:54
 **/
@Service
@Slf4j
public class InspSupAddAvailabilityServiceImpl implements InspSupAddAvailabilityService {

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private AppointmentClient appointmentClient;

    @Override
    public List<SelectOption> getRecurrenceOption() {
        List<SelectOption> recurrenceOption = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DATE_TYPE);
        return recurrenceOption;
    }

    @Override
    public OrgUserDto getOrgUserDtoById(String userId) {
        OrgUserDto orgUserDto = new OrgUserDto();
        if(!StringUtil.isEmpty(userId)) {
            orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
        }
        return orgUserDto;
    }

    @Override
    public ApptNonAvailabilityDateDto getApptNonAvailabilityDateDtoById(String nonAvaId) {
        ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = new ApptNonAvailabilityDateDto();
        if(!StringUtil.isEmpty(nonAvaId)) {
            apptNonAvailabilityDateDto = appointmentClient.getNonAvailabilityById(nonAvaId).getEntity();
        }
        return apptNonAvailabilityDateDto;
    }

    @Override
    public void deleteNonAvailabilityById(String removeId) {
        ApptNonAvailabilityDateDto apptNonAvailabilityDateDto = getApptNonAvailabilityDateDtoById(removeId);
        apptNonAvailabilityDateDto.setNonAvaStatus(AppConsts.COMMON_STATUS_DELETED);
        updateNonAvailability(apptNonAvailabilityDateDto);
    }

    @Override
    public ApptNonAvailabilityDateDto createNonAvailability(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto) {
        if(apptNonAvailabilityDateDto != null){
            return appointmentClient.createNonAvailability(apptNonAvailabilityDateDto).getEntity();
        }
        return null;
    }

    @Override
    public ApptNonAvailabilityDateDto updateNonAvailability(ApptNonAvailabilityDateDto apptNonAvailabilityDateDto) {
        if(apptNonAvailabilityDateDto != null){
            return appointmentClient.updateNonAvailability(apptNonAvailabilityDateDto).getEntity();
        }
        return null;
    }
}
