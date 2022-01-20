package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.client.AutoRenewalClient;
import sg.gov.moh.iais.egp.bsb.client.SystemBeBsbClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApprovalDto;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

/**
 * @author : LiRan
 * @date : 2021/12/1
 */
@Service
@Slf4j
public class AutoRenewalService {
    //system param, renewal facility registration
    private static final String FAC_CONFIG_MONTH_3      = "2546486C-4C94-426B-8AED-223F8B92A6D3";
    private static final String FAC_CONFIG_MONTH_2      = "796D9D18-AA3E-40CB-AF4D-8E2E88593E52";
    private static final String FAC_CONFIG_MONTH_1      = "D00F1AA6-FE6A-4E21-8F7A-671160756123";
    private static final String FAC_CONFIG_WEEK_2       = "19A11A7B-8A5A-437E-8F24-73507A9B0F1F";
    private static final String FAC_CONFIG_WEEK_1       = "FBED363D-C1C1-4E83-8107-69D28695B20F";
    //system param, renewal facility certifier registration
    private static final String FAC_CONFIG_CER_MONTH_6  = "276054C7-8BA2-4B73-B0A2-193701AEE527";
    private static final String FAC_CONFIG_CER_MONTH_5  = "E8392E23-6020-46BB-9861-BFFE07C3B689";
    private static final String FAC_CONFIG_CER_MONTH_4  = "6C072D49-2964-4E62-95AB-E12D29BB440D";
    private static final String FAC_CONFIG_CER_MONTH_3  = "6ECC9DE1-BD6D-4C65-9807-EAF214379E1E";
    private static final String FAC_CONFIG_CER_MONTH_2  = "965784C4-D4B9-4DC9-ABC3-1BBD2AC7CAB1";
    private static final String FAC_CONFIG_CER_MONTH_1  = "DA1331B1-F7FF-4002-9246-431E693FFD8E";
    //param param_type
    private static final String MONTH                   = "TPOF00005";
    private static final String WEEKS                   = "TPOF00004";
    //param value_type
    private static final String INT                     = "INT";
    //Help us find the data corresponding to config
    private static final String FAC_MONTH_3             = "facMonth3";
    private static final String FAC_MONTH_2             = "facMonth2";
    private static final String FAC_MONTH_1             = "facMonth1";
    private static final String FAC_WEEK_2              = "facWeek2";
    private static final String FAC_WEEK_1              = "facWeek1";
    private static final String FAC_CER_MONTH_6         = "facCerMonth6";
    private static final String FAC_CER_MONTH_5         = "facCerMonth5";
    private static final String FAC_CER_MONTH_4         = "facCerMonth4";
    private static final String FAC_CER_MONTH_3         = "facCerMonth3";
    private static final String FAC_CER_MONTH_2         = "facCerMonth2";
    private static final String FAC_CER_MONTH_1         = "facCerMonth1";

    private static final String ALL_NEED_SUSPENDED      = "suspended";
    private static final String ALL_NEED_EXPIRED        = "expired";


    private final SystemBeBsbClient systemBeBsbClient;
    private final AutoRenewalClient autoRenewalClient;

    public AutoRenewalService(SystemBeBsbClient systemBeBsbClient, AutoRenewalClient autoRenewalClient) {
        this.systemBeBsbClient = systemBeBsbClient;
        this.autoRenewalClient = autoRenewalClient;
    }

    public void startRenewal() {
        //facility registration approval system
        SystemParameterDto sysParDtoFacM3 = systemBeBsbClient.getParameterByRowguid(FAC_CONFIG_MONTH_3).getEntity();
        SystemParameterDto sysParDtoFacM2 = systemBeBsbClient.getParameterByRowguid(FAC_CONFIG_MONTH_2).getEntity();
        SystemParameterDto sysParDtoFacM1 = systemBeBsbClient.getParameterByRowguid(FAC_CONFIG_MONTH_1).getEntity();
        SystemParameterDto sysParDtoFacW2 = systemBeBsbClient.getParameterByRowguid(FAC_CONFIG_WEEK_2).getEntity();
        SystemParameterDto sysParDtoFacW1 = systemBeBsbClient.getParameterByRowguid(FAC_CONFIG_WEEK_1).getEntity();
        //facility certifier registration approval system
        SystemParameterDto sysParDtoFacCerM6 = systemBeBsbClient.getParameterByRowguid(FAC_CONFIG_CER_MONTH_6).getEntity();
        SystemParameterDto sysParDtoFacCerM5 = systemBeBsbClient.getParameterByRowguid(FAC_CONFIG_CER_MONTH_5).getEntity();
        SystemParameterDto sysParDtoFacCerM4 = systemBeBsbClient.getParameterByRowguid(FAC_CONFIG_CER_MONTH_4).getEntity();
        SystemParameterDto sysParDtoFacCerM3 = systemBeBsbClient.getParameterByRowguid(FAC_CONFIG_CER_MONTH_3).getEntity();
        SystemParameterDto sysParDtoFacCerM2 = systemBeBsbClient.getParameterByRowguid(FAC_CONFIG_CER_MONTH_2).getEntity();
        SystemParameterDto sysParDtoFacCerM1 = systemBeBsbClient.getParameterByRowguid(FAC_CONFIG_CER_MONTH_1).getEntity();

        Map<String, LocalDate> facDateMap = Maps.newLinkedHashMapWithExpectedSize(5);
        Map<String, LocalDate> facCerDateMap = Maps.newLinkedHashMapWithExpectedSize(6);
        log.info("The facility registration remind date is {}", org.apache.commons.lang.StringUtils.normalizeSpace(facDateMap.toString()));
        log.info("The facility certifier registration remind date is {}", org.apache.commons.lang.StringUtils.normalizeSpace(facCerDateMap.toString()));

        facDateMap.put(FAC_MONTH_3, getRemindDay(sysParDtoFacM3));
        facDateMap.put(FAC_MONTH_2, getRemindDay(sysParDtoFacM2));
        facDateMap.put(FAC_MONTH_1, getRemindDay(sysParDtoFacM1));
        facDateMap.put(FAC_WEEK_2, getRemindDay(sysParDtoFacW2));
        facDateMap.put(FAC_WEEK_1, getRemindDay(sysParDtoFacW1));
        facCerDateMap.put(FAC_CER_MONTH_6, getRemindDay(sysParDtoFacCerM6));
        facCerDateMap.put(FAC_CER_MONTH_5, getRemindDay(sysParDtoFacCerM5));
        facCerDateMap.put(FAC_CER_MONTH_4, getRemindDay(sysParDtoFacCerM4));
        facCerDateMap.put(FAC_CER_MONTH_3, getRemindDay(sysParDtoFacCerM3));
        facCerDateMap.put(FAC_CER_MONTH_2, getRemindDay(sysParDtoFacCerM2));
        facCerDateMap.put(FAC_CER_MONTH_1, getRemindDay(sysParDtoFacCerM1));

        //renewal remind approval
        Map<String, List<ApprovalDto>> approvalFacDtoMap = autoRenewalClient.approvalRenewal(facDateMap, MasterCodeConstants.PROCESS_TYPE_FAC_REG).getEntity();
        Map<String, List<ApprovalDto>> approvalFacCerDtoMap = autoRenewalClient.approvalRenewal(facCerDateMap, MasterCodeConstants.PROCESS_TYPE_FAC_CERTIFIER_REG).getEntity();

        //suspended approval
        List<ApprovalDto> suspendedApprovalDtoList = autoRenewalClient.getSuspendedApprovalList().getEntity();
        //expired approval
        List<ApprovalDto> expiredApprovalDtoList = autoRenewalClient.getExpiredApprovalList().getEntity();

        //update approval data
        //1.first remind day approval, update renewable to 'Y'
        Map<String, List<ApprovalDto>> updateApprovalMap = Maps.newHashMapWithExpectedSize(4);
        updateApprovalMap.put(FAC_MONTH_3, approvalFacDtoMap.get(FAC_MONTH_3));
        updateApprovalMap.put(FAC_CER_MONTH_6, approvalFacCerDtoMap.get(FAC_CER_MONTH_6));
        //2.suspended approval, update status to suspended
        updateApprovalMap.put(ALL_NEED_SUSPENDED, suspendedApprovalDtoList);
        //3.expired approval, update status to expired, renewable to 'N'
        updateApprovalMap.put(ALL_NEED_EXPIRED, expiredApprovalDtoList);
        autoRenewalClient.updateAllNeedUpdateApproval(updateApprovalMap);

        //send notification to applicant
        sendNotification(approvalFacDtoMap);
        sendNotification(approvalFacCerDtoMap);

        //send notification to moh
        sendNotification(approvalFacDtoMap.get(FAC_WEEK_1));
        sendNotification(approvalFacCerDtoMap.get(FAC_CER_MONTH_1));

        //send notification to applicant(this method the logic is wrong, need to update)
        sendNotification(suspendedApprovalDtoList);
        sendNotification(expiredApprovalDtoList);
    }

    private LocalDate getRemindDay(SystemParameterDto systemParameterDto){
        String value = systemParameterDto.getValue();
        String valueType = systemParameterDto.getValueType();
        String paramType = systemParameterDto.getParamType();
        if (INT.equals(valueType)){
            int configInt = Integer.parseInt(value);
            LocalDate today = LocalDate.now();
            if (MONTH.equals(paramType)){
                return today.plus(configInt, ChronoUnit.MONTHS);
            }else if (WEEKS.equals(paramType)){
                return today.plus(configInt, ChronoUnit.WEEKS);
            }
            log.info("The config param type is {}", org.apache.commons.lang.StringUtils.normalizeSpace(paramType));
        }
        log.info("The config value type is {}" , org.apache.commons.lang.StringUtils.normalizeSpace(valueType));
        log.info("The config param type can only be week or month and the config value type can only be int.");
        return null;
    }

    /**
     * send notification to applicant(all remind approval)
     */
    private void sendNotification(Map<String, List<ApprovalDto>> approvalDtoMap){
        for (Map.Entry<String, List<ApprovalDto>> entry : approvalDtoMap.entrySet()) {
            if (!CollectionUtils.isEmpty(entry.getValue())){ //has need remind approval, need notification
                //send notification to applicant
                log.info("send notification!");
            }
        }
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