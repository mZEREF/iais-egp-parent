package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.AuditTcuListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * AuditTaskBatchjob
 *
 * @author Guyin
 * @date 09/03/2020
 */
@Delegator("SystemAddTCUBatchJob")
@Slf4j
public class AuditTaskBatchjob {

    private static String FREQUENCY = "3";//MSOO1
    private static String CHECKFORTCUS = "Wednesday";//MSOO2
    // MSOO3
    private static String MONTHSFORPRESETTCU = "1";//MSOO4

    @Autowired
    AuditTcuListService auditTcuListService;
    @Autowired
    AuditSystemListService auditSystemListService;

    public void start(BaseProcessClass bpc) {

    }

    public void doBatchJob(BaseProcessClass bpc) {

        if(CHECKFORTCUS.equals(getWeekOfDate())){
            //get license
            List<AuditTaskDataFillterDto> auditTaskDataDtos = auditTcuListService.getAuditTcuList();
            List<LicPremisesRecommendationDto> licPremisesRecommendationDtoList = new ArrayList<>();
            auditSystemListService.getInspectors(auditTaskDataDtos);
            Date date = new Date();
            for (AuditTaskDataFillterDto item :auditTaskDataDtos
                 ) {
                Calendar c = Calendar.getInstance();
                c.setTime(item.getTcuDate());
                c.add(Calendar.MONTH, -Integer.valueOf(MONTHSFORPRESETTCU));
                boolean before = date.before(c.getTime());
                if(before){
                    LicPremisesRecommendationDto licPremisesRecommendationDto = new LicPremisesRecommendationDto();
                    licPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    licPremisesRecommendationDto.setVersion(1);
                    licPremisesRecommendationDto.setLicPremId(item.getId());
                    licPremisesRecommendationDto.setRemarks("audit");
                    licPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_TCU);
                    licPremisesRecommendationDtoList.add(licPremisesRecommendationDto);
                }
            }
            if(licPremisesRecommendationDtoList != null){
                auditTcuListService.saveAuditTcuList(licPremisesRecommendationDtoList);
            }

        }


    }

    private static String getWeekOfDate() {
        Date dt = new Date();
        String[] weekDays = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0){
            w = 0;
        }
        return weekDays[w];
    }

}
