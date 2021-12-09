package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import sg.gov.moh.iais.egp.bsb.client.AutoRenewalClient;
import sg.gov.moh.iais.egp.bsb.client.SystemBeBsbClient;
import sg.gov.moh.iais.egp.bsb.constant.MasterCodeConstants;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApprovalDto;

import java.util.*;

/**
 * @author : LiRan
 * @date : 2021/12/1
 */
@Service
@Slf4j
public class AutoRenewalService {
    //system param, renewal facility registration
    private static final String FAC_MONTH_3 = "2546486C-4C94-426B-8AED-223F8B92A6D3";
    private static final String FAC_MONTH_2 = "796D9D18-AA3E-40CB-AF4D-8E2E88593E52";
    private static final String FAC_MONTH_1 = "D00F1AA6-FE6A-4E21-8F7A-671160756123";
    private static final String FAC_WEEK_2 = "19A11A7B-8A5A-437E-8F24-73507A9B0F1F";
    private static final String FAC_WEEK_1 = "FBED363D-C1C1-4E83-8107-69D28695B20F";
    //system param, renewal facility certifier registration
    private static final String FAC_CER_MONTH_6 = "276054C7-8BA2-4B73-B0A2-193701AEE527";
    private static final String FAC_CER_MONTH_5 = "E8392E23-6020-46BB-9861-BFFE07C3B689";
    private static final String FAC_CER_MONTH_4 = "6C072D49-2964-4E62-95AB-E12D29BB440D";
    private static final String FAC_CER_MONTH_3 = "6ECC9DE1-BD6D-4C65-9807-EAF214379E1E";
    private static final String FAC_CER_MONTH_2 = "965784C4-D4B9-4DC9-ABC3-1BBD2AC7CAB1";
    private static final String FAC_CER_MONTH_1 = "DA1331B1-F7FF-4002-9246-431E693FFD8E";

    //param param_type
    private static final String MONTH = "TPOF00005";
    private static final String WEEKS = "TPOF00004";

    //param value_type
    private static final String INT = "INT";

    private final SystemBeBsbClient systemBeBsbClient;
    private final AutoRenewalClient autoRenewalClient;

    public AutoRenewalService(SystemBeBsbClient systemBeBsbClient, AutoRenewalClient autoRenewalClient) {
        this.systemBeBsbClient = systemBeBsbClient;
        this.autoRenewalClient = autoRenewalClient;
    }

    public void startRenewal() {
        //facility registration approval system
        SystemParameterDto sysParDtoFacM3 = systemBeBsbClient.getParameterByRowguid(FAC_MONTH_3).getEntity();
        SystemParameterDto sysParDtoFacM2 = systemBeBsbClient.getParameterByRowguid(FAC_MONTH_2).getEntity();
        SystemParameterDto sysParDtoFacM1 = systemBeBsbClient.getParameterByRowguid(FAC_MONTH_1).getEntity();
        SystemParameterDto sysParDtoFacW2 = systemBeBsbClient.getParameterByRowguid(FAC_WEEK_2).getEntity();
        SystemParameterDto sysParDtoFacW1 = systemBeBsbClient.getParameterByRowguid(FAC_WEEK_1).getEntity();
        //facility certifier registration approval system
        SystemParameterDto sysParDtoFacCerM6 = systemBeBsbClient.getParameterByRowguid(FAC_CER_MONTH_6).getEntity();
        SystemParameterDto sysParDtoFacCerM5 = systemBeBsbClient.getParameterByRowguid(FAC_CER_MONTH_5).getEntity();
        SystemParameterDto sysParDtoFacCerM4 = systemBeBsbClient.getParameterByRowguid(FAC_CER_MONTH_4).getEntity();
        SystemParameterDto sysParDtoFacCerM3 = systemBeBsbClient.getParameterByRowguid(FAC_CER_MONTH_3).getEntity();
        SystemParameterDto sysParDtoFacCerM2 = systemBeBsbClient.getParameterByRowguid(FAC_CER_MONTH_2).getEntity();
        SystemParameterDto sysParDtoFacCerM1 = systemBeBsbClient.getParameterByRowguid(FAC_CER_MONTH_1).getEntity();
        List<Integer> facDayList = new ArrayList<>(5);
        List<Integer> facCerDayList = new ArrayList<>(6);
        Integer firstFacDay = getDay(sysParDtoFacM3);
        facDayList.add(firstFacDay);
        facDayList.add(getDay(sysParDtoFacM2));
        facDayList.add(getDay(sysParDtoFacM1));
        facDayList.add(getDay(sysParDtoFacW2));
        Integer latestFacDay = getDay(sysParDtoFacW1);
        facDayList.add(latestFacDay);

        Integer firstFacCerDay = getDay(sysParDtoFacCerM6);
        facCerDayList.add(firstFacCerDay);
        facCerDayList.add(getDay(sysParDtoFacCerM5));
        facCerDayList.add(getDay(sysParDtoFacCerM4));
        facCerDayList.add(getDay(sysParDtoFacCerM3));
        facCerDayList.add(getDay(sysParDtoFacCerM2));
        Integer latestFacCerDay = getDay(sysParDtoFacCerM1);
        facCerDayList.add(latestFacCerDay);
        //renewal notification approval
        Map<String, List<ApprovalDto>> approvalFacDtoMap = autoRenewalClient.approvalRenewal(facDayList, MasterCodeConstants.PROCESS_TYPE_FAC_REG).getEntity();
        Map<String, List<ApprovalDto>> approvalFacCerDtoMap = autoRenewalClient.approvalRenewal(facCerDayList, MasterCodeConstants.PROCESS_TYPE_FAC_CERTIFIER_REG).getEntity();
        //update suspended approval
        autoRenewalClient.updateSuspendedApproval().getEntity();
        //update expired approval
        autoRenewalClient.updateExpiredApproval().getEntity();
        if (!approvalFacDtoMap.isEmpty()){
            //renewal notice date, send notification to admin
            sendEmail(approvalFacDtoMap);
            sendEmail(approvalFacCerDtoMap);
            //if is the first notice date, update renewable to 'Y'
            List<ApprovalDto> firstFacApprovalDtoList = approvalFacDtoMap.get(firstFacDay+"");
            if (firstFacApprovalDtoList != null && !firstFacApprovalDtoList.isEmpty()){
                autoRenewalClient.updateRenewableApproval(firstFacApprovalDtoList);
            }
            List<ApprovalDto> firstFacCerApprovalDtoList = approvalFacDtoMap.get(firstFacCerDay+"");
            if (firstFacCerApprovalDtoList != null && !firstFacCerApprovalDtoList.isEmpty()){
                autoRenewalClient.updateRenewableApproval(firstFacCerApprovalDtoList);
            }
            //if is the latest notice date, send notification to moh officer
            List<ApprovalDto> latestFacApprovalDtoList = approvalFacDtoMap.get(latestFacDay+"");
            if (latestFacApprovalDtoList != null && !latestFacApprovalDtoList.isEmpty()){
                sendNotification(latestFacApprovalDtoList);
            }
            List<ApprovalDto> latestFacCerApprovalDtoList = approvalFacCerDtoMap.get(latestFacCerDay+"");
            if (latestFacCerApprovalDtoList != null && !latestFacCerApprovalDtoList.isEmpty()){
                sendNotification(latestFacCerApprovalDtoList);
            }
        }
    }

    private Integer getDay(SystemParameterDto systemParameterDto){
        String value = systemParameterDto.getValue();
        String valueType = systemParameterDto.getValueType();
        String paramType = systemParameterDto.getParamType();
        if (INT.equals(valueType)){
            int configInt = Integer.parseInt(value);
            Calendar todayCalendar = Calendar.getInstance();
            todayCalendar.setTime(new Date());
            if(MONTH.equals(paramType)){
                Calendar expectCalendar = Calendar.getInstance();
                expectCalendar.setTime(new Date());
                expectCalendar.add(Calendar.MONTH, configInt);
                return Integer.parseInt(String.valueOf((expectCalendar.getTimeInMillis() - todayCalendar.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
            }else if (WEEKS.equals(paramType)){
                Calendar expectCalendar = Calendar.getInstance();
                expectCalendar.setTime(new Date());
                expectCalendar.add(Calendar.WEEK_OF_MONTH, configInt);
                return Integer.parseInt(String.valueOf((expectCalendar.getTimeInMillis() - todayCalendar.getTimeInMillis()) / (24 * 60 * 60 * 1000)));
            }
        }
        return 0;
    }

    /**
     * send notification to applicant(all remind approval)
     */
    private void sendEmail(Map<String, List<ApprovalDto>> approvalDtoMap){
        approvalDtoMap.forEach((k,v) -> {
            for (ApprovalDto approvalDto : v) {
                //will do in future, this log will delete
                log.info("approval dto is {}", StringUtils.normalizeSpace(approvalDto.toString()));
            }
        });
    }

    /**
     * send notification to moh (the latest approval)
     */
    private void sendNotification(List<ApprovalDto> approvalDtoList){
        //will do in future, this log will delete
        log.info("approvalDtoList size {}", StringUtils.normalizeSpace(Integer.toString(approvalDtoList.size())));
    }
}