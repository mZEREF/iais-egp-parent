package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptFeConfirmDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicantConfirmInspDateService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionFeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrgEicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    @Autowired
    private OrgEicClient orgEicClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private AppConfigClient appConfigClient;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private AppEicClient appEicClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Value("${iais.email.sender}")
    private String mailSender;

    /**
     *System Date
     */
    @Override
    public ApptFeConfirmDateDto getApptSystemDate(String appPremCorrId) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        ApptFeConfirmDateDto apptFeConfirmDateDto = new ApptFeConfirmDateDto();
        //get All CorrDto From Same Premises
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationClient.getLastAppPremisesCorrelationDtoByCorreId(appPremCorrId).getEntity();
        //set All TaskRefNo (AppPremCorrIds)
        List<String> taskRefNo = getTaskRefNoList(appPremisesCorrelationDtos);
        apptFeConfirmDateDto.setTaskRefNo(taskRefNo);

        if(!StringUtil.isEmpty(appPremCorrId)) {
            List<ApplicationDto> applicationDtos = applicationClient.getPremisesApplicationsByCorreId(appPremCorrId).getEntity();
            apptFeConfirmDateDto.setApplicationDtos(applicationDtos);
            List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = inspectionFeClient.getSystemDtosByAppPremCorrIdList(taskRefNo).getEntity();
            apptFeConfirmDateDto.setAppPremisesInspecApptDtoList(appPremisesInspecApptDtoList);
            apptFeConfirmDateDto.setAppPremisesInspecApptDto(appPremisesInspecApptDtoList.get(0));
            if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtoList)){
                List<String> apptRefNos = IaisCommonUtils.genNewArrayList();
                for(AppPremisesInspecApptDto aDto : appPremisesInspecApptDtoList){
                    apptRefNos.add(aDto.getApptRefNo());
                }
                Set<String> apptRefNoSet = new HashSet<>(apptRefNos);
                apptRefNos = new ArrayList<>(apptRefNoSet);
                //save eic record
                ApptAppInfoShowDto apptAppInfoShowDto = new ApptAppInfoShowDto();
                apptAppInfoShowDto.setApptRefNo(apptRefNos);
                EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "getApptSystemDate",
                        "hcsa-licence-web-internet", ApptAppInfoShowDto.class.getName(), JsonUtil.parseToJson(apptAppInfoShowDto));
                String eicRefNo = eicRequestTrackingDto.getRefNo();
                List<ApptRequestDto> apptRequestDtos = feEicGatewayClient.getAppointmentByApptRefNo(apptRefNos, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization()).getEntity();
                Map<String, List<ApptUserCalendarDto>> apptInspDateMap = new LinkedHashMap<>(apptRequestDtos.size());
                if(!IaisCommonUtils.isEmpty(apptRequestDtos)){
                    for(ApptRequestDto apptRequestDto : apptRequestDtos){
                        apptInspDateMap.put(apptRequestDto.getApptRefNo(), apptRequestDto.getUserClandars());
                    }
                }
                //get eic record
                eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
                //update eic record status
                eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
                eicRequestTrackingDtos.add(eicRequestTrackingDto);
                appEicClient.updateStatus(eicRequestTrackingDtos);

                if(apptInspDateMap != null) {
                    apptFeConfirmDateDto.setApptInspDateMap(apptInspDateMap);
                    setSystemDateMap(apptFeConfirmDateDto);
                }
            }

            apptFeConfirmDateDto.setAppPremCorrId(appPremCorrId);
        }
        return apptFeConfirmDateDto;
    }

    private List<String> getTaskRefNoList(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos) {
        List<String> taskRefNo = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appPremisesCorrelationDtos)){
            for(AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos){
                taskRefNo.add(appPremisesCorrelationDto.getId());
            }
        }
        return taskRefNo;
    }

    private void setSystemDateMap(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        Map<String, Date> inspectionDateMap = IaisCommonUtils.genNewHashMap();
        List<SelectOption> inspectionDate = IaisCommonUtils.genNewArrayList();

        for(Map.Entry<String, List<ApptUserCalendarDto>> apptInspDateMap : apptFeConfirmDateDto.getApptInspDateMap().entrySet()){
            String apptRefNo = apptInspDateMap.getKey();
            List<ApptUserCalendarDto> apptUserCalendarDtoList = apptInspDateMap.getValue();
            int timeSize = apptUserCalendarDtoList.get(0).getEndSlot().size();
            Date startDate = apptUserCalendarDtoList.get(0).getStartSlot().get(0);
            Date endDate = apptUserCalendarDtoList.get(0).getEndSlot().get(timeSize - 1);
            String inspStartDate = apptDateToStringShow(startDate);
            String inspEndDate = apptDateToStringShow(endDate);
            String dateStr = inspStartDate + " - " + inspEndDate;
            for (AppPremisesInspecApptDto appPremisesInspecApptDto : apptFeConfirmDateDto.getAppPremisesInspecApptDtoList()) {
                if(appPremisesInspecApptDto.getApptRefNo().equals(apptRefNo)){
                    inspectionDateMap.put(appPremisesInspecApptDto.getId(), startDate);
                    SelectOption so = new SelectOption(appPremisesInspecApptDto.getId(), dateStr);
                    inspectionDate.add(so);
                    break;
                }
            }
        }

        apptFeConfirmDateDto.setInspectionDate(inspectionDate);
        apptFeConfirmDateDto.setInspectionDateMap(inspectionDateMap);
    }

    @Override
    public void confirmInspectionDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
        //get selected date
        String checkDate = apptFeConfirmDateDto.getCheckDate();
        //Exclude a selected date
        List<String> cancelRefNo = IaisCommonUtils.genNewArrayList();
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = IaisCommonUtils.genNewArrayList();
        String apptRefNo = "";
        for(AppPremisesInspecApptDto apptDto : apptFeConfirmDateDto.getAppPremisesInspecApptDtoList()){
            if(apptDto.getId().equals(checkDate)){
                apptRefNo = apptDto.getApptRefNo();
                break;
            }
        }
        for(AppPremisesInspecApptDto apptDto : apptFeConfirmDateDto.getAppPremisesInspecApptDtoList()){
            if(!(apptDto.getApptRefNo().equals(apptRefNo))){
                appPremisesInspecApptDtoList.add(apptDto);
                cancelRefNo.add(apptDto.getApptRefNo());
            }
        }
        //if get new 3 Appt Date need remove
        Map<String, String> refNoMap = apptFeConfirmDateDto.getRefNoMap();
        cancelRefNo = removeNewApptDate(refNoMap, cancelRefNo);
        //filter
        Set<String> cancelRefNoSet = new HashSet<>(cancelRefNo);
        cancelRefNo = new ArrayList<>(cancelRefNoSet);
        apptFeConfirmDateDto.setAppPremisesInspecApptDtoList(appPremisesInspecApptDtoList);
        //update appt
        setApptUpdateList(apptFeConfirmDateDto, apptInspectionDateDto);
        //update application
        setUpdateApplicationDto(apptFeConfirmDateDto, apptInspectionDateDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
        //set history
        setCreateHistoryDto(apptFeConfirmDateDto, apptInspectionDateDto);
        //set some data to update inspection status
        setCreateInspectionStatus(apptInspectionDateDto, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
        //set some data to update recommendation
        setRecommendationDto(apptFeConfirmDateDto, apptInspectionDateDto);
        apptInspectionDateDto.setRefNo(apptFeConfirmDateDto.getTaskRefNo());
        //set audit trail
        AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
        appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        apptInspectionDateDto.setAppPremisesInspecApptDto(appPremisesInspecApptDto);

        //save eic record
        EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "confirmInspectionDate",
                "hcsa-licence-web-internet", ApptInspectionDateDto.class.getName(), JsonUtil.parseToJson(apptInspectionDateDto));
        String eicRefNo = eicRequestTrackingDto.getRefNo();
        feEicGatewayClient.apptFeDataUpdateCreateBe(apptInspectionDateDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        //get eic record
        eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
        //update eic record status
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
        eicRequestTrackingDtos.add(eicRequestTrackingDto);
        appEicClient.updateStatus(eicRequestTrackingDtos);
        //create task
        apptFeConfirmDateDto.setEmailDto(null);
        createApptDateTask(apptFeConfirmDateDto, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION);
        //cancel or confirm appointment date
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
        ccCalendarStatusEic(apptCalendarStatusDto);
    }

    private List<String> removeNewApptDate(Map<String, String> refNoMap, List<String> cancelRefNo) {
        if(refNoMap != null){
            for(Map.Entry<String, String> map : refNoMap.entrySet()){
                cancelRefNo.add(map.getValue());
            }
        }
        return cancelRefNo;
    }

    private void ccCalendarStatusEic(ApptCalendarStatusDto apptCalendarStatusDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        //save eic record
        EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "ccCalendarStatusEic",
                "hcsa-licence-web-internet", ApptCalendarStatusDto.class.getName(), JsonUtil.parseToJson(apptCalendarStatusDto));
        String eicRefNo = eicRequestTrackingDto.getRefNo();
        feEicGatewayClient.updateUserCalendarStatus(apptCalendarStatusDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        //get eic record
        eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
        //update eic record status
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
        eicRequestTrackingDtos.add(eicRequestTrackingDto);
        appEicClient.updateStatus(eicRequestTrackingDtos);
    }

    private void setRecommendationDto(ApptFeConfirmDateDto apptFeConfirmDateDto, ApptInspectionDateDto apptInspectionDateDto) {
        List<AppPremisesRecommendationDto> appPremisesRecommendationDtos = IaisCommonUtils.genNewArrayList();
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appPremisesRecommendationDto.setRecomDecision(InspectionConstants.PROCESS_DECI_ALLOW_SYSTEM_TO_PROPOSE_DATE);
        appPremisesRecommendationDto.setRecomInDate(apptFeConfirmDateDto.getSaveDate());
        appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
        appPremisesRecommendationDtos.add(appPremisesRecommendationDto);
        apptInspectionDateDto.setAppPremisesRecommendationDtos(appPremisesRecommendationDtos);
    }

    @Override
    public ApptFeConfirmDateDto getApptNewSystemDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        if(IaisCommonUtils.isEmpty(apptFeConfirmDateDto.getInspectionNewDate())) {
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            //get new Inspection date
            AppPremisesInspecApptDto appPremisesInspecApptDto = apptFeConfirmDateDto.getAppPremisesInspecApptDtoList().get(0);
            List<AppointmentUserDto> appointmentUserDtos = getUserCalendarDtosByMap(apptFeConfirmDateDto.getApptInspDateMap());
            String appGroupId = apptFeConfirmDateDto.getApplicationDtos().get(0).getAppGrpId();
            ApplicationGroupDto applicationGroupDto = applicationClient.getApplicationGroup(appGroupId).getEntity();

            AppointmentDto appointmentDto = new AppointmentDto();
            appointmentDto.setUsers(appointmentUserDtos);
            appointmentDto.setSubmitDt(applicationGroupDto.getSubmitDt());
            appointmentDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
            appointmentDto.setStartDate(Formatter.formatDateTime(appPremisesInspecApptDto.getStartDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));
            appointmentDto.setEndDate(Formatter.formatDateTime(appPremisesInspecApptDto.getEndDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));

            //save eic record
            EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "getApptNewSystemDate",
                    "hcsa-licence-web-internet", AppointmentDto.class.getName(), JsonUtil.parseToJson(appointmentDto));
            String eicRefNo = eicRequestTrackingDto.getRefNo();
            List<ApptRequestDto> apptRequestDtos = feEicGatewayClient.getUserCalendarByUserId(appointmentDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();
            Map<String, List<ApptUserCalendarDto>> appInspDateMap = new LinkedHashMap<>(apptRequestDtos.size());
            if(!IaisCommonUtils.isEmpty(apptRequestDtos)){
                for(ApptRequestDto apptRequestDto : apptRequestDtos){
                    appInspDateMap.put(apptRequestDto.getApptRefNo(), apptRequestDto.getUserClandars());
                }
            }
            //get eic record
            eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
            //update eic record status
            eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
            eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
            eicRequestTrackingDtos.add(eicRequestTrackingDto);
            appEicClient.updateStatus(eicRequestTrackingDtos);

            Map<String, Date> inspectionNewDateMap = IaisCommonUtils.genNewHashMap();
            List<SelectOption> inspectionNewDate = IaisCommonUtils.genNewArrayList();
            Map<String, String> refMap = IaisCommonUtils.genNewHashMap();
            int index = 0;
            if (appInspDateMap != null) {
                for (Map.Entry<String, List<ApptUserCalendarDto>> map : appInspDateMap.entrySet()) {
                    String apptRefNo = map.getKey();
                    List<ApptUserCalendarDto> apptUserCalendarDtoList = map.getValue();
                    int timeSize = apptUserCalendarDtoList.get(0).getEndSlot().size();
                    Date startDate = apptUserCalendarDtoList.get(0).getStartSlot().get(0);
                    Date endDate = apptUserCalendarDtoList.get(0).getEndSlot().get(timeSize - 1);
                    String inspStartDate = apptDateToStringShow(startDate);
                    String inspEndDate = apptDateToStringShow(endDate);
                    String dateStr = inspStartDate + " - " + inspEndDate;
                    //key is inspectionNewDateMap.checkNewDate
                    inspectionNewDateMap.put(index + "", startDate);
                    refMap.put(index + "", apptRefNo);
                    SelectOption so = new SelectOption(index + "", dateStr);
                    inspectionNewDate.add(so);
                    index++;
                }
                apptFeConfirmDateDto.setInspectionNewDate(inspectionNewDate);
                apptFeConfirmDateDto.setInspectionNewDateMap(inspectionNewDateMap);
                apptFeConfirmDateDto.setRefNoMap(refMap);
            }
        }
        return apptFeConfirmDateDto;
    }

    private List<AppointmentUserDto> getUserCalendarDtosByMap(Map<String, List<ApptUserCalendarDto>> apptInspDateMap) {
        List<AppointmentUserDto> appointmentUserDtos = IaisCommonUtils.genNewArrayList();
        for(Map.Entry<String, List<ApptUserCalendarDto>> map : apptInspDateMap.entrySet()){
            List<ApptUserCalendarDto> apptUserCalendarDtos = map.getValue();
            for(ApptUserCalendarDto apptUserCalendarDto : apptUserCalendarDtos) {
                AppointmentUserDto appointmentUserDto = new AppointmentUserDto();
                appointmentUserDto.setWorkHours(apptUserCalendarDto.getStartSlot().size());
                appointmentUserDto.setWorkGrpName(apptUserCalendarDto.getGroupName());
                appointmentUserDto.setLoginUserId(apptUserCalendarDto.getLoginUserId());
                appointmentUserDtos.add(appointmentUserDto);
            }
        }
        //filter same appointmentUserDtos
        appointmentUserDtos = filterApptUsers(appointmentUserDtos);
        return appointmentUserDtos;
    }

    private List<AppointmentUserDto> filterApptUsers(List<AppointmentUserDto> appointmentUserDtos) {
        List<AppointmentUserDto> appointmentUserDtoList = IaisCommonUtils.genNewArrayList();
        List<String> loginUserIds = null;
        for(AppointmentUserDto appointmentUserDto : appointmentUserDtos){
            if(IaisCommonUtils.isEmpty(loginUserIds)){
                loginUserIds = IaisCommonUtils.genNewArrayList();
                appointmentUserDtoList.add(appointmentUserDto);
                loginUserIds.add(appointmentUserDto.getLoginUserId());
            } else {
                if(!loginUserIds.contains(appointmentUserDto.getLoginUserId())){
                    appointmentUserDtoList.add(appointmentUserDto);
                    loginUserIds.add(appointmentUserDto.getLoginUserId());
                }
            }
        }
        return appointmentUserDtoList;
    }

    @Override
    public ApptFeConfirmDateDto confirmNewDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
        //get selected key
        String key = apptFeConfirmDateDto.getCheckNewDate();
        //get selected date
        Map<String, Date> newDateMap = apptFeConfirmDateDto.getInspectionNewDateMap();
        Date checkDate = newDateMap.get(key);
        apptFeConfirmDateDto.setSaveDate(checkDate);
        //get selected refNo
        Map<String, String> refNoMap = apptFeConfirmDateDto.getRefNoMap();
        String refNo = refNoMap.get(key);
        apptFeConfirmDateDto.setApptRefNo(refNo);
        //get cancel apptRefNo List
        List<String> cancelRefNo = IaisCommonUtils.genNewArrayList();
        List<String> confirmRefNo = IaisCommonUtils.genNewArrayList();
        confirmRefNo.add(refNo);
        List<AppPremisesInspecApptDto> apptInspectionDateDtos = apptFeConfirmDateDto.getAppPremisesInspecApptDtoList();
        if(!IaisCommonUtils.isEmpty(apptInspectionDateDtos)) {
            for (AppPremisesInspecApptDto appPremisesInspecApptDto : apptInspectionDateDtos) {
                cancelRefNo.add(appPremisesInspecApptDto.getApptRefNo());
            }
        }
        for(Map.Entry<String, String> apptRefNoMap : refNoMap.entrySet()){
            String apptRefNo = apptRefNoMap.getValue();
            if(!refNo.equals(apptRefNo)){
                cancelRefNo.add(apptRefNo);
            }
        }
        //filter
        Set<String> cancelRefNoSet = new HashSet<>(cancelRefNo);
        cancelRefNo = new ArrayList<>(cancelRefNoSet);

        setApptUpdateList(apptFeConfirmDateDto, apptInspectionDateDto);
        setApptCreateList(apptFeConfirmDateDto, apptInspectionDateDto, null);
        setUpdateApplicationDto(apptFeConfirmDateDto, apptInspectionDateDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
        setCreateHistoryDto(apptFeConfirmDateDto, apptInspectionDateDto);
        setCreateInspectionStatus(apptInspectionDateDto, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
        setRecommendationDto(apptFeConfirmDateDto, apptInspectionDateDto);
        apptInspectionDateDto.setRefNo(apptFeConfirmDateDto.getTaskRefNo());
        AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
        appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        apptInspectionDateDto.setAppPremisesInspecApptDto(appPremisesInspecApptDto);

        //save eic record
        EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "confirmNewDate",
                "hcsa-licence-web-internet", ApptInspectionDateDto.class.getName(), JsonUtil.parseToJson(apptInspectionDateDto));
        String eicRefNo = eicRequestTrackingDto.getRefNo();
        feEicGatewayClient.apptFeDataUpdateCreateBe(apptInspectionDateDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        //get eic record
        eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
        //update eic record status
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
        eicRequestTrackingDtos.add(eicRequestTrackingDto);
        appEicClient.updateStatus(eicRequestTrackingDtos);

        apptFeConfirmDateDto.setEmailDto(null);
        createApptDateTask(apptFeConfirmDateDto, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION);
        //cancel or confirm appointment date
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
        apptCalendarStatusDto.setConfirmRefNums(confirmRefNo);
        ccCalendarStatusEic(apptCalendarStatusDto);
        return apptFeConfirmDateDto;
    }

    @Override
    public void saveAccSpecificDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        setUpdateApplicationDto(apptFeConfirmDateDto, apptInspectionDateDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
        setCreateHistoryDto(apptFeConfirmDateDto, apptInspectionDateDto);
        setCreateInspectionStatus(apptInspectionDateDto, InspectionConstants.INSPECTION_STATUS_PENDING_PRE);
        setRecommendationDto(apptFeConfirmDateDto, apptInspectionDateDto);
        apptInspectionDateDto.setRefNo(apptFeConfirmDateDto.getTaskRefNo());
        AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
        appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        apptInspectionDateDto.setAppPremisesInspecApptDto(appPremisesInspecApptDto);

        //save eic record
        EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "saveAccSpecificDate",
                "hcsa-licence-web-internet", ApptInspectionDateDto.class.getName(), JsonUtil.parseToJson(apptInspectionDateDto));
        String eicRefNo = eicRequestTrackingDto.getRefNo();
        feEicGatewayClient.apptFeDataUpdateCreateBe(apptInspectionDateDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        //get eic record
        eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
        //update eic record status
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
        eicRequestTrackingDtos.add(eicRequestTrackingDto);
        appEicClient.updateStatus(eicRequestTrackingDtos);

        apptFeConfirmDateDto.setEmailDto(null);
        createApptDateTask(apptFeConfirmDateDto, TaskConsts.TASK_PROCESS_URL_PRE_INSPECTION);
    }

    @Override
    public String getAppPremCorrIdByAppNo(String applicationNo) {
        String appPremCorrId = applicationClient.getCorrelationByAppNo(applicationNo).getEntity().getId();
        return appPremCorrId;
    }

    private void setCreateInspectionStatus(ApptInspectionDateDto apptInspectionDateDto, String status) {
        List<AppInspectionStatusDto> appInspectionStatusDtos = IaisCommonUtils.genNewArrayList();
        AppInspectionStatusDto appInspectionStatusDto = new AppInspectionStatusDto();
        appInspectionStatusDto.setStatus(status);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusDtos.add(appInspectionStatusDto);
        apptInspectionDateDto.setAppInspectionStatusDtos(appInspectionStatusDtos);
    }

    private void setCreateHistoryDto(ApptFeConfirmDateDto apptFeConfirmDateDto, ApptInspectionDateDto apptInspectionDateDto) {
        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = IaisCommonUtils.genNewArrayList();
        for(ApplicationDto applicationDto : apptFeConfirmDateDto.getApplicationDtos()) {
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
            appPremisesRoutingHistoryDto.setApplicationNo(applicationDto.getApplicationNo());
            appPremisesRoutingHistoryDto.setSubStage(HcsaConsts.ROUTING_STAGE_PRE);
            appPremisesRoutingHistoryDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
            appPremisesRoutingHistoryDto.setAppStatus(applicationDto.getStatus());
            appPremisesRoutingHistoryDto.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
            appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
            appPremisesRoutingHistoryDtos.add(appPremisesRoutingHistoryDto);
        }
        apptInspectionDateDto.setAppPremisesRoutingHistoryDtos(appPremisesRoutingHistoryDtos);
    }

    private void setUpdateApplicationDto(ApptFeConfirmDateDto apptFeConfirmDateDto, ApptInspectionDateDto apptInspectionDateDto, String status) {
        List<ApplicationDto> applicationDtos = apptFeConfirmDateDto.getApplicationDtos();
        for(ApplicationDto applicationDto : applicationDtos) {
            applicationDto.setStatus(status);
            applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            applicationDto = applicationClient.updateApplication(applicationDto).getEntity();
            applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        apptInspectionDateDto.setApplicationDtos(applicationDtos);
    }

    private void setApptCreateList(ApptFeConfirmDateDto apptFeConfirmDateDto, ApptInspectionDateDto apptInspectionDateDto, String processDo) {
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = IaisCommonUtils.genNewArrayList();
        for(String appPremCorrId : apptFeConfirmDateDto.getTaskRefNo()) {
            AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
            appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appPremisesInspecApptDto.setId(null);
            if (InspectionConstants.SWITCH_ACTION_REJECT.equals(processDo)) {
                appPremisesInspecApptDto.setSpecificInspDate(apptFeConfirmDateDto.getSaveDate());
                appPremisesInspecApptDto.setApptRefNo(null);
            } else if (StringUtil.isEmpty(processDo)) {
                appPremisesInspecApptDto.setSpecificInspDate(null);
                appPremisesInspecApptDto.setApptRefNo(apptFeConfirmDateDto.getApptRefNo());
            }
            appPremisesInspecApptDto.setAppCorrId(appPremCorrId);
            appPremisesInspecApptDto.setStartDate(apptFeConfirmDateDto.getAppPremisesInspecApptDtoList().get(0).getStartDate());
            appPremisesInspecApptDto.setEndDate(apptFeConfirmDateDto.getAppPremisesInspecApptDtoList().get(0).getEndDate());
            appPremisesInspecApptDto.setReason(apptFeConfirmDateDto.getReason());
            appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);
        }
        appPremisesInspecApptDtoList = inspectionFeClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoList).getEntity();

        for(AppPremisesInspecApptDto apptDto : appPremisesInspecApptDtoList){
            apptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        apptInspectionDateDto.setAppPremisesInspecApptCreateList(appPremisesInspecApptDtoList);
    }

    private void setApptUpdateList(ApptFeConfirmDateDto apptFeConfirmDateDto, ApptInspectionDateDto apptInspectionDateDto) {
        for(AppPremisesInspecApptDto apptDto : apptFeConfirmDateDto.getAppPremisesInspecApptDtoList()){
            apptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            apptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = inspectionFeClient.updateAppPremisesInspecApptDtoList(apptFeConfirmDateDto.getAppPremisesInspecApptDtoList()).getEntity();
        if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtoList)){
            for(AppPremisesInspecApptDto appPremisesInspecApptDto : appPremisesInspecApptDtoList){
                appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            }
            apptInspectionDateDto.setAppPremisesInspecApptUpdateList(appPremisesInspecApptDtoList);
        }
    }

    private void createApptDateTask(ApptFeConfirmDateDto apptFeConfirmDateDto, String processUrl) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String applicationNo = getApplicationByCorrId(apptFeConfirmDateDto.getAppPremCorrId());
        TaskDto taskDto = new TaskDto();
        taskDto.setRefNo(apptFeConfirmDateDto.getAppPremCorrId());
        taskDto.setApplicationNo(applicationNo);
        taskDto.setProcessUrl(processUrl);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        apptFeConfirmDateDto.setTaskDto(taskDto);
        //set list and map
        apptFeConfirmDateDto = setAppNoListByCorrIds(apptFeConfirmDateDto);
        //save eic record
        EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.ORGANIZATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "createApptDateTask",
                "hcsa-licence-web-internet", ApptFeConfirmDateDto.class.getName(), JsonUtil.parseToJson(apptFeConfirmDateDto));
        String eicRefNo = eicRequestTrackingDto.getRefNo();
        feEicGatewayClient.createFeReplyTask(apptFeConfirmDateDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        //get eic record
        eicRequestTrackingDto = orgEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
        //update eic record status
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
        eicRequestTrackingDtos.add(eicRequestTrackingDto);
        orgEicClient.updateStatus(eicRequestTrackingDtos);
    }

    private ApptFeConfirmDateDto setAppNoListByCorrIds(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        List<String> appNoList = IaisCommonUtils.genNewArrayList();
        Map<String, String> appNoCorrIdMap = IaisCommonUtils.genNewHashMap();
        List<String> refNoList = apptFeConfirmDateDto.getTaskRefNo();
        if(!IaisCommonUtils.isEmpty(refNoList)){
            Set<String> refNoSet = new HashSet<>(refNoList);
            refNoList = new ArrayList<>(refNoSet);
            for(String refNo : refNoList){
                String appNo = getApplicationByCorrId(refNo);
                appNoList.add(appNo);
                appNoCorrIdMap.put(appNo, refNo);
            }
        }
        apptFeConfirmDateDto.setAppNoList(appNoList);
        apptFeConfirmDateDto.setAppNoCorrIdMap(appNoCorrIdMap);
        return apptFeConfirmDateDto;
    }

    private String getApplicationByCorrId(String appPremCorrId) {
        String appNo = "";
        if(!StringUtil.isEmpty(appPremCorrId)){
            ApplicationDto applicationDto = applicationClient.getApplicationByCorreId(appPremCorrId).getEntity();
            appNo = applicationDto.getApplicationNo();
        }
        return appNo;
    }

    @Override
    public List<SelectOption> getInspectionDateHours() {
        List<SelectOption> hourOption = IaisCommonUtils.genNewArrayList();
        for(int i = 1; i < 13; i++){
            SelectOption so = new SelectOption(i + "", i + "");
            hourOption.add(so);
        }
        return hourOption;
    }

    @Override
    public List<SelectOption> getAmPmOption() {
        List<SelectOption> amPmOption = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(Formatter.DAY_AM, Formatter.DAY_AM);
        SelectOption so2 = new SelectOption(Formatter.DAY_PM, Formatter.DAY_PM);
        amPmOption.add(so1);
        amPmOption.add(so2);
        return amPmOption;
    }

    @Override
    public void rejectSystemDateAndCreateTask(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        //get cancel apptRefNo List
        List<String> cancelRefNo = IaisCommonUtils.genNewArrayList();
        List<AppPremisesInspecApptDto> apptInspectionDateDtos = apptFeConfirmDateDto.getAppPremisesInspecApptDtoList();
        if(!IaisCommonUtils.isEmpty(apptInspectionDateDtos)) {
            for (AppPremisesInspecApptDto appPremisesInspecApptDto : apptInspectionDateDtos) {
                cancelRefNo.add(appPremisesInspecApptDto.getApptRefNo());
            }
        }
        //if get new 3 Appt Date need remove
        Map<String, String> refNoMap = apptFeConfirmDateDto.getRefNoMap();
        cancelRefNo = removeNewApptDate(refNoMap, cancelRefNo);
        //filter
        Set<String> cancelRefNoSet = new HashSet<>(cancelRefNo);
        cancelRefNo = new ArrayList<>(cancelRefNoSet);

        setApptUpdateList(apptFeConfirmDateDto, apptInspectionDateDto);
        setApptCreateList(apptFeConfirmDateDto, apptInspectionDateDto, InspectionConstants.SWITCH_ACTION_REJECT);
        setUpdateApplicationDto(apptFeConfirmDateDto, apptInspectionDateDto, ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING);
        setCreateHistoryDto(apptFeConfirmDateDto, apptInspectionDateDto);
        setCreateInspectionStatus(apptInspectionDateDto, InspectionConstants.INSPECTION_STATUS_PENDING_RE_APPOINTMENT_INSPECTION_DATE);
        apptInspectionDateDto.setAppPremisesRecommendationDtos(null);
        apptInspectionDateDto.setRefNo(null);

        //save eic record
        EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "rejectSystemDateAndCreateTask",
                "hcsa-licence-web-internet", ApptInspectionDateDto.class.getName(), JsonUtil.parseToJson(apptInspectionDateDto));
        String eicRefNo = eicRequestTrackingDto.getRefNo();
        feEicGatewayClient.apptFeDataUpdateCreateBe(apptInspectionDateDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        //get eic record
        eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
        //update eic record status
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
        eicRequestTrackingDtos.add(eicRequestTrackingDto);
        appEicClient.updateStatus(eicRequestTrackingDtos);
        //send email and create task
        sendEmailRequestNewDate(apptFeConfirmDateDto);
        createApptDateTask(apptFeConfirmDateDto, TaskConsts.TASK_PROCESS_URL_RE_CONFIRM_INSPECTION_DATE);
        //cancel or confirm appointment date
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
        ccCalendarStatusEic(apptCalendarStatusDto);
    }

    private void sendEmailRequestNewDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        try{
            String appPremCorrId = apptFeConfirmDateDto.getAppPremCorrId();
            ApplicationDto applicationDto = applicationClient.getApplicationByCorreId(appPremCorrId).getEntity();
            String appType = applicationDto.getApplicationType();
            String appNo = applicationDto.getApplicationNo();
            String appGroupId = applicationDto.getAppGrpId();
            String licenceId = applicationDto.getOriginLicenceId();
            String licenceDueDateStr = "-";
            if(!StringUtil.isEmpty(licenceId)){
                LicenceDto licenceDto = licenceClient.getLicBylicId(licenceId).getEntity();
                Date licenceDueDate = licenceDto.getExpiryDate();
                licenceDueDateStr = Formatter.formatDateTime(licenceDueDate, "yyyy/MM/dd");
            }
            List<String> appGroupIds = IaisCommonUtils.genNewArrayList();
            appGroupIds.add(appGroupId);
            List<ApplicationGroupDto> applicationGroupDtos = applicationClient.getApplicationGroupsByIds(appGroupIds).getEntity();
            Date submitDate = new Date();
            if(!IaisCommonUtils.isEmpty(applicationGroupDtos)){
                ApplicationGroupDto applicationGroupDto = applicationGroupDtos.get(0);
                if(applicationGroupDto != null){
                    if(applicationGroupDto.getSubmitDt() != null){
                        submitDate = applicationGroupDto.getSubmitDt();
                    }
                }
            }
            String submitDtStr = Formatter.formatDateTime(submitDate, "yyyy/MM/dd HH:mm:ss");
            AppGrpPremisesDto appGrpPremisesDto = applicationClient.getAppGrpPremisesByCorrId(appPremCorrId).getEntity();
            String hciName = appGrpPremisesDto.getHciName();
            if(StringUtil.isEmpty(hciName)){
                hciName = "-";
            }
            String hciCode = appGrpPremisesDto.getHciCode();
            if(StringUtil.isEmpty(hciCode)){
                hciCode = "-";
            }
            String serviceId = applicationDto.getServiceId();
            HcsaServiceDto hcsaServiceDto = appConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
            String serviceName = hcsaServiceDto.getSvcName();
            Date date = apptFeConfirmDateDto.getSaveDate();
            String dateStr = "no date";
            if(date != null){
                dateStr = Formatter.formatDateTime(date, "yyyy/MM/dd");
                StringBuilder html = new StringBuilder(dateStr);
                html.append(' ');
                html.append(apptFeConfirmDateDto.getAmPm());
                dateStr = html.toString();
            }
            List<SelectOption> inspDateOption = apptFeConfirmDateDto.getInspectionDate();
            List<String> dateStrList = IaisCommonUtils.genNewArrayList();
            if(!IaisCommonUtils.isEmpty(inspDateOption)){
                for(SelectOption so : inspDateOption){
                    String inspDateStr = so.getText();
                    dateStrList.add(inspDateStr);
                }
            } else {
                dateStrList.add("-");
            }
            Map<String, Object> map = IaisCommonUtils.genNewHashMap();
            map.put("hciName", hciName);
            map.put("hciCode", hciCode);
            String appTypeShow = MasterCodeUtil.getCodeDesc(appType);
            map.put("appType", appTypeShow);
            map.put("appDate", submitDtStr);
            map.put("serviceName", serviceName);
            map.put("licence_due_date", licenceDueDateStr);
            map.put("applicationNo", appNo);
            map.put("dateStrList", dateStrList);
            map.put("fe_date", dateStr);
            map.put("officer_name", "officer_name");
            StringBuilder sb = new StringBuilder("MOH HALP - [Internal] Licensee Request to Reschedule Inspection Appointment Date for ");
            sb.append(appNo);

            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_REJECT_APPT_REQUEST_A_DATE);
            emailParam.setTemplateContent(map);
            emailParam.setQueryCode(appNo);
            emailParam.setReqRefNum(appNo);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            emailParam.setRefId(appNo);
            emailParam.setSubject(sb.toString());
            notificationHelper.sendNotification(emailParam);
            EmailParam smsParam = new EmailParam();
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_REJECT_APPT_REQUEST_A_DATE);
            smsParam.setQueryCode(appNo);
            smsParam.setReqRefNum(appNo);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            smsParam.setRefId(appNo);
            smsParam.setSubject(sb.toString());
            notificationHelper.sendNotification(smsParam);
            EmailParam msgParam = new EmailParam();
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_REJECT_APPT_REQUEST_A_DATE);
            msgParam.setTemplateContent(map);
            msgParam.setQueryCode(appNo);
            msgParam.setReqRefNum(appNo);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            msgParam.setRefId(appNo);
            msgParam.setSubject(sb.toString());
            notificationHelper.sendNotification(emailParam);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
    }

    /**
     * Specific Date
     */
    @Override
    public ApptFeConfirmDateDto getSpecificDateDto(String appPremCorrId) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        ApptFeConfirmDateDto apptFeConfirmDateDto = new ApptFeConfirmDateDto();
        //get All CorrDto From Same Premises
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = applicationClient.getLastAppPremisesCorrelationDtoByCorreId(appPremCorrId).getEntity();
        //set All TaskRefNo (AppPremCorrIds)
        List<String> taskRefNo = getTaskRefNoList(appPremisesCorrelationDtos);
        apptFeConfirmDateDto.setTaskRefNo(taskRefNo);
        List<ApplicationDto> applicationDtos = applicationClient.getPremisesApplicationsByCorreId(appPremCorrId).getEntity();
        apptFeConfirmDateDto.setApplicationDtos(applicationDtos);
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = inspectionFeClient.getSystemDtosByAppPremCorrIdList(taskRefNo).getEntity();
        if(!StringUtil.isEmpty(appPremCorrId)){
            AppPremisesInspecApptDto appPremisesInspecApptDto = appPremisesInspecApptDtoList.get(0);
            apptFeConfirmDateDto.setAppPremisesInspecApptDto(appPremisesInspecApptDto);

            //get licence periods
            apptFeConfirmDateDto = getLicencePeriods(apptFeConfirmDateDto, applicationDtos);

            if(appPremisesInspecApptDto != null){
                List<String> apptRefNos = IaisCommonUtils.genNewArrayList();
                apptRefNos.add(appPremisesInspecApptDto.getApptRefNo());

                //save eic record
                ApptAppInfoShowDto apptAppInfoShowDto = new ApptAppInfoShowDto();
                apptAppInfoShowDto.setApptRefNo(apptRefNos);
                EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "getApptSystemDate",
                        "hcsa-licence-web-internet", ApptAppInfoShowDto.class.getName(), JsonUtil.parseToJson(apptAppInfoShowDto));
                String eicRefNo = eicRequestTrackingDto.getRefNo();
                List<ApptRequestDto> apptRequestDtos = feEicGatewayClient.getAppointmentByApptRefNo(apptRefNos, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization()).getEntity();
                Map<String, List<ApptUserCalendarDto>> apptInspDateMap = new LinkedHashMap<>(apptRequestDtos.size());
                if(!IaisCommonUtils.isEmpty(apptRequestDtos)){
                    for(ApptRequestDto apptRequestDto : apptRequestDtos){
                        apptInspDateMap.put(apptRequestDto.getApptRefNo(), apptRequestDto.getUserClandars());
                    }
                }
                //get eic record
                eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
                //update eic record status
                eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
                eicRequestTrackingDtos.add(eicRequestTrackingDto);
                appEicClient.updateStatus(eicRequestTrackingDtos);

                Date specStartInspDate = new Date();
                Date specEndInspDate;
                String dateStr = "-";
                if(apptInspDateMap != null){
                    for(Map.Entry<String, List<ApptUserCalendarDto>> map : apptInspDateMap.entrySet()){
                        List<ApptUserCalendarDto> apptUserCalendarDtoList = map.getValue();
                        int daySize = apptUserCalendarDtoList.size();
                        int timeSize = apptUserCalendarDtoList.get(daySize - 1).getEndSlot().size();
                        specStartInspDate = apptUserCalendarDtoList.get(0).getStartSlot().get(0);
                        specEndInspDate = apptUserCalendarDtoList.get(daySize - 1).getEndSlot().get(timeSize - 1);
                        String inspStartDate = apptDateToStringShow(specStartInspDate);
                        String inspEndDate = apptDateToStringShow(specEndInspDate);
                        dateStr = inspStartDate + " - " + inspEndDate;
                    }
                }
                apptFeConfirmDateDto.setSpecificDate(specStartInspDate);
                apptFeConfirmDateDto.setSaveDate(specStartInspDate);
                apptFeConfirmDateDto.setApptStartDate(appPremisesInspecApptDto.getStartDate());
                apptFeConfirmDateDto.setApptEndDate(appPremisesInspecApptDto.getEndDate());
                apptFeConfirmDateDto.setAppPremCorrId(appPremCorrId);
                apptFeConfirmDateDto.setApptRefNo(appPremisesInspecApptDto.getApptRefNo());
                apptFeConfirmDateDto.setSpecificDateShow(dateStr);
                apptFeConfirmDateDto.setApptInspDateMap(apptInspDateMap);
            }
        }
        apptFeConfirmDateDto.setAppPremisesInspecApptDtoList(appPremisesInspecApptDtoList);
        return apptFeConfirmDateDto;
    }

    private ApptFeConfirmDateDto getLicencePeriods(ApptFeConfirmDateDto apptFeConfirmDateDto, List<ApplicationDto> applicationDtos) {
        List<String> licencePeriods = IaisCommonUtils.genNewArrayList();
        for(ApplicationDto applicationDto : applicationDtos) {
            if(!StringUtil.isEmpty(applicationDto.getOriginLicenceId())) {
                LicenceDto licenceDto = licenceClient.getLicBylicId(applicationDto.getOriginLicenceId()).getEntity();
                if (licenceDto != null) {
                    String licenceStartDate = Formatter.formatDateTime(licenceDto.getStartDate(), AppConsts.DEFAULT_DATE_FORMAT);
                    String licenceEndDate = Formatter.formatDateTime(licenceDto.getEndDate(), AppConsts.DEFAULT_DATE_FORMAT);
                    String licensePeriod = licenceStartDate + " - " + licenceEndDate;
                    licencePeriods.add(licensePeriod);
                }
            }
        }
        if(IaisCommonUtils.isEmpty(licencePeriods)){
            licencePeriods.add("-");
        }
        apptFeConfirmDateDto.setLicencePeriods(licencePeriods);
        return apptFeConfirmDateDto;
    }

    @Override
    public void rejectSpecificDate(ApptFeConfirmDateDto apptFeConfirmDateDto) {
        ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = apptFeConfirmDateDto.getAppPremisesInspecApptDtoList();
        //get cancel apptRefNo List
        List<String> cancelRefNo = IaisCommonUtils.genNewArrayList();
        for(AppPremisesInspecApptDto appPremisesInspecApptDto : appPremisesInspecApptDtoList) {
            cancelRefNo.add(appPremisesInspecApptDto.getApptRefNo());
            appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
            appPremisesInspecApptDto = inspectionFeClient.updateAppPremisesInspecApptDto(appPremisesInspecApptDto).getEntity();
            appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }
        apptInspectionDateDto.setAppPremisesInspecApptUpdateList(appPremisesInspecApptDtoList);
        setUpdateApplicationDto(apptFeConfirmDateDto, apptInspectionDateDto, ApplicationConsts.APPLICATION_STATUS_PENDING_RE_APPOINTMENT_SCHEDULING);
        setCreateHistoryDto(apptFeConfirmDateDto, apptInspectionDateDto);
        setCreateInspectionStatus(apptInspectionDateDto, InspectionConstants.INSPECTION_STATUS_PENDING_RE_APPOINTMENT_INSPECTION_DATE);
        apptInspectionDateDto.setAppPremisesRecommendationDtos(null);
        apptInspectionDateDto.setRefNo(null);

        //save eic record
        EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.ApplicantConfirmInspDateServiceImpl", "rejectSpecificDate",
                "hcsa-licence-web-internet", ApptInspectionDateDto.class.getName(), JsonUtil.parseToJson(apptInspectionDateDto));
        String eicRefNo = eicRequestTrackingDto.getRefNo();
        feEicGatewayClient.apptFeDataUpdateCreateBe(apptInspectionDateDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        //get eic record
        eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
        //update eic record status
        eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
        eicRequestTrackingDtos.add(eicRequestTrackingDto);
        appEicClient.updateStatus(eicRequestTrackingDtos);

        apptFeConfirmDateDto.setEmailDto(null);
        createApptDateTask(apptFeConfirmDateDto, TaskConsts.TASK_PROCESS_URL_RE_CONFIRM_INSPECTION_DATE);
        //cancel or confirm appointment date
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
        ccCalendarStatusEic(apptCalendarStatusDto);
    }

    private String apptDateToStringShow(Date date) {
        String specificDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int minutes = cal.get(Calendar.MINUTE);
        if(minutes > 0){
            cal.add(Calendar.HOUR_OF_DAY, 1);
        }
        int curHour24 = cal.get(Calendar.HOUR_OF_DAY);
        String hoursShow = "";
        if(curHour24 < 10){
            hoursShow = "0";
        }
        specificDate = specificDate + " " + hoursShow + curHour24 + ":00";
        return specificDate;
    }
}
