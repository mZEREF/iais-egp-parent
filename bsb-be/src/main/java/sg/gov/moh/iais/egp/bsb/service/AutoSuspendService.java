package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.client.SuspensionClient;
import sg.gov.moh.iais.egp.bsb.client.SystemBeBsbClient;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApprovalDto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

/**
 * @author tangtang
 * @date 2022/1/4 17:06
 */
@Service
@Slf4j
public class AutoSuspendService {
    private static final String FAC_CONFIG_WEEK_1       = "FBED363D-C1C1-4E83-8107-69D28695B20F";
    //param param_type
    private static final String WEEKS                   = "TPOF00004";
    //param value_type
    private static final String INT                     = "INT";

    private final SuspensionClient suspensionClient;
    private final SystemBeBsbClient systemBeBsbClient;

    public AutoSuspendService(SuspensionClient suspensionClient, SystemBeBsbClient systemBeBsbClient) {
        this.suspensionClient = suspensionClient;
        this.systemBeBsbClient = systemBeBsbClient;
    }

    public void startSuspend() {
        Map<String, LocalDate> dateMap = Maps.newLinkedHashMapWithExpectedSize(1);
        SystemParameterDto sysParDtoFacW1 = systemBeBsbClient.getParameterByRowguid(FAC_CONFIG_WEEK_1).getEntity();
        LocalDate remindDay = getRemindDay(sysParDtoFacW1);
        dateMap.put("oneWeek",remindDay);
        Map<String, List<ApprovalDto>> entity = suspensionClient.getNeedRemindApproval(dateMap).getEntity();
        //need suspended approval
        List<ApprovalDto> needSuspendedApprovalDtoList = suspensionClient.getNeedSuspendApprovals().getEntity();
        //need reinstate approval
        List<ApprovalDto> needReinstateApprovalDtoList = suspensionClient.getNeedReinstateApprovals().getEntity();

        //update approval data
        Map<String, List<ApprovalDto>> updateApprovalMap = Maps.newHashMapWithExpectedSize(2);
        updateApprovalMap.put("needSuspend",needSuspendedApprovalDtoList);
        updateApprovalMap.put("needReinstate",needReinstateApprovalDtoList);
        suspensionClient.updateAllNeedUpdateApproval(updateApprovalMap);

        //send notification to DO
        sendNotification(entity.get("oneWeek"));
    }

    private LocalDate getRemindDay(SystemParameterDto systemParameterDto){
        String value = systemParameterDto.getValue();
        String valueType = systemParameterDto.getValueType();
        String paramType = systemParameterDto.getParamType();
        if (INT.equals(valueType)){
            int configInt = Integer.parseInt(value);
            LocalDate today = LocalDate.now();
            if (WEEKS.equals(paramType)){
                return today.plus(configInt, ChronoUnit.WEEKS);
            }
            log.info("The config param type is {}", org.apache.commons.lang.StringUtils.normalizeSpace(paramType));
        }
        log.info("The config value type is {}" , org.apache.commons.lang.StringUtils.normalizeSpace(valueType));
        log.info("The config param type can only be week or month and the config value type can only be int.");
        return null;
    }

    /**
     * send notification to moh (the latest approval)
     */
    private void sendNotification(List<ApprovalDto> approvalDtoList){
        //send notification to moh
        if (!CollectionUtils.isEmpty(approvalDtoList)){
            //send notification to moh
            log.info("send notification!");
        }
    }
}
