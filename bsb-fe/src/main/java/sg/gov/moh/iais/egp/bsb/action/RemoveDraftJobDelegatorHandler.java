package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppPremInspApptDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Component;
import sg.gov.moh.iais.egp.bsb.client.DraftClient;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApplicationDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.DraftDto;
import sop.util.DateUtil;

import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Date;
import java.util.List;

@JobHandler(value="removeDraftJobDelegatorHandler")
@Component
@Slf4j
public class RemoveDraftJobDelegatorHandler extends IJobHandler {
    private final DraftClient draftClient;

    public RemoveDraftJobDelegatorHandler(DraftClient draftClient) {
        this.draftClient = draftClient;
    }

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            removeInspDateDraftByConfigValue();
        }catch (Exception e){
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }

    public void removeInspDateDraftByConfigValue (){
        List<DraftDto> draftDtoList = draftClient.retrieveDraftDtoAll().getEntity();
        Instant now = Instant.now();
        if(!IaisCommonUtils.isEmpty(draftDtoList)) {
            for(DraftDto dto : draftDtoList) {
                if(dto != null) {
                    try {
                        log.info(StringUtil.changeForLog("Remove  Draft Application Id = " + dto.getApplicationId()));
                        JobLogger.log(StringUtil.changeForLog("Remove Draft Application Id = " + dto.getApplicationId()));
                        ApplicationDto applicationDto = draftClient.retrieveDraftApplicationById(dto.getApplicationId()).getEntity();
                        log.info(StringUtil.changeForLog(" Retrieve Draft Application ApplicationDt = " + applicationDto.getApplicationDt()));
                        JobLogger.log(StringUtil.changeForLog("Retrieve Draft Application ApplicationDt = " + applicationDto.getApplicationDt()));
                        Instant appDt = applicationDto.getApplicationDt().toInstant();
                        long days = appDt.until(now, ChronoUnit.DAYS);
                        if(days>30){
                            draftClient.doRemoveDraft(dto.getApplicationId());
                        }
                    } catch (Exception e) {
                        JobLogger.log(e);
                        log.error(e.getMessage(), e);
                        continue;
                    }
                }
            }
        }
    }

}
