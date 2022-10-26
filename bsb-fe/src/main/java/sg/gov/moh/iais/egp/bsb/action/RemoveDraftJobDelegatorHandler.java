package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import sg.gov.moh.iais.egp.bsb.client.DraftClient;
import sg.gov.moh.iais.egp.bsb.dto.entity.DraftDto;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
    public ReturnT<String> execute(String s) {
        try {
            removeInsDateDraftByConfigValue();
        } catch (Exception e){
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }

    public void removeInsDateDraftByConfigValue (){
        List<DraftDto> draftDtoList = draftClient.findAllActiveDraft().getEntity();
        Instant now = Instant.now();
        if(!IaisCommonUtils.isEmpty(draftDtoList)) {
            for(DraftDto dto : draftDtoList) {
                if(dto != null) {
                    try {
                        log.info(StringUtil.changeForLog("Remove  Draft Application Id = " + dto.getApplicationId()));
                        Instant createDt = dto.getCreatedAt().toInstant();
                        long days = createDt.until(now, ChronoUnit.DAYS);
                        if(days>30){
                            draftClient.doRemoveDraftByDraftAppId(dto.getApplicationId());
                        }
                    } catch (Exception e) {
                        JobLogger.log(e);
                        log.error(e.getMessage(), e);
                    }
                }
            }
        }
    }

}
