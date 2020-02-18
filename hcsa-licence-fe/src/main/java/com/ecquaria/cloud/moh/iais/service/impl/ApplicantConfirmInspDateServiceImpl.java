package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
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
        if(!StringUtil.isEmpty(appPremCorrId)) {
            List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = inspectionFeClient.getSystemDtosByAppPremCorrId(appPremCorrId).getEntity();
            if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtoList)){
                List<String> apptRefNos = new ArrayList<>();
                for(AppPremisesInspecApptDto aDto : appPremisesInspecApptDtoList){
                    apptRefNos.add(aDto.getApptRefNo());
                }

            }
        }
        return null;
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
