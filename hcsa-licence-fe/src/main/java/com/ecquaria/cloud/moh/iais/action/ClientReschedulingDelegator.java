package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschApptGrpPremsQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserRoleAccessMatrixDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AppointmentUtil;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HalpSearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApptConfirmReSchDateService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigFeClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ClientReschedulingDelegator
 *
 * @author junyu
 * @date 2020/6/18
 */
@Slf4j
@Delegator("clientReschedulingDelegator")
public class ClientReschedulingDelegator {
    @Autowired
    private ApptConfirmReSchDateService rescheduleService;
    private FilterParameter rescheduleParameter = new FilterParameter.Builder()
            .clz(ReschApptGrpPremsQueryDto.class)
            .searchAttr("SearchParam")
            .resultAttr("SearchResult")
            .sortField("appt.RECOM_IN_DATE").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(SystemParamUtil.getDefaultPageSize()).build();

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private ApplicationFeClient applicationFeClient;
    @Autowired
    private AppConfigClient appConfigClient;
    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;

    @Autowired
    private HcsaConfigFeClient hcsaConfigClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    public void start(BaseProcessClass bpc)  {
        rescheduleParameter.setSortField("appt.RECOM_IN_DATE");
        rescheduleParameter.setPageNo(1);
        rescheduleParameter.setPageSize(SystemParamUtil.getDefaultPageSize());
        ParamUtil.setSessionAttr(bpc.request,"appIds",null);
        ParamUtil.setSessionAttr(bpc.request, "apptViewDtosMap", null);
    }

    public void init(BaseProcessClass bpc)  {
        ParamUtil.setSessionAttr(bpc.request,"DashboardTitle","Scheduled Appointments");

        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        List<UserRoleAccessMatrixDto> userRoleAccessMatrixDtos = loginContext.getRoleMatrixes().get(RoleConsts.USER_ROLE_ORG_USER);
        String licenseeId = loginContext.getLicenseeId();
        rescheduleParameter.setPageNo(0);
        SearchParam rescheduleParam = SearchResultHelper.getSearchParam(bpc.request, rescheduleParameter,true);
        StringBuilder stringBuilder=new StringBuilder();
        List<String> appStatus=IaisCommonUtils.genNewArrayList();
        for (String appSt: AppointmentUtil.getNoReschdulingAppStatus()
        ) {
            appStatus.add(appSt);
            stringBuilder.append(appSt).append("','");
        }
        //rescheduleParam.addParam("appStatus_reschedule", "(app.status not in('"+stringBuilder.toString()+"'))");
        //rescheduleParam.addParam("RESCHEDULE_COUNT", "(insAppt.RESCHEDULE_COUNT <"+systemParamConfig.getRescheduleMaxCount()+")");
        Date dateRange;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,systemParamConfig.getRescheduleDateRange());
        dateRange =calendar.getTime();
        //rescheduleParam.addParam("RECOM_IN_DATE", "( appRec.RECOM_IN_DATE >= convert(datetime,'"+ Formatter.formatDateTime(dateRange, SystemAdminBaseConstants.DATE_FORMAT)+"') )");
        rescheduleParam.addFilter("licenseeId", licenseeId, true);
        HalpSearchResultHelper.setParamByFieldOrSearch(rescheduleParam,"appServicesShow",HcsaServiceCacheHelper.controlServices(3,userRoleAccessMatrixDtos),"appt.code");
        QueryHelp.setMainSql("rescheduleQuery", "queryApptGrpPremises", rescheduleParam);
        String repalceService = getRepalceService();
        rescheduleParam.setMainSql(rescheduleParam.getMainSql().replace("repalceService",repalceService));
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String [] keyIds= (String[]) ParamUtil.getSessionAttr(bpc.request,"appIds");
        Set<String> keys=IaisCommonUtils.genNewHashSet() ;
        if(keyIds!=null){
            keys.addAll(Arrays.asList(keyIds));
        }
        String pageNo = ParamUtil.getString(bpc.request, "pageJumpNoTextchangePage");
        String pageSize = ParamUtil.getString(bpc.request, "pageJumpNoPageSize");
        int pgNo;
        int pgSize;
        if(StringUtil.isEmpty(pageNo)){
            pgNo=1;
        }else {
            pgNo=Integer.parseInt(pageNo);
        }
        if(StringUtil.isEmpty(pageSize)){
            pgSize=SystemParamUtil.getDefaultPageSize();
        }else {
            pgSize=Integer.parseInt(pageSize);
        }

        try {
            SearchResult<ReschApptGrpPremsQueryDto> result  = feEicGatewayClient.eicSearchApptReschPrem(rescheduleParam, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();

            List<ReschApptGrpPremsQueryDto> rows = result.getRows();
            if(rows!=null){
                LinkedHashMap<String ,ApptViewDto> apptViewDtos= new LinkedHashMap<>(rows.size());

                for (ReschApptGrpPremsQueryDto reschApptGrpPremsQueryDto : rows) {
                    ApptViewDto apptViewDto=new ApptViewDto();
                    List<String> svcIds=IaisCommonUtils.genNewArrayList();
                    String viewCorrId=reschApptGrpPremsQueryDto.getAppGrpId()+reschApptGrpPremsQueryDto.getHciCode();
                    if("1".equals(reschApptGrpPremsQueryDto.getFastTracking())){
                        viewCorrId=viewCorrId+reschApptGrpPremsQueryDto.getId();
                    }
                    if(apptViewDtos.get(viewCorrId)==null){
                        svcIds.add(reschApptGrpPremsQueryDto.getSvcId());
                    }else {
                        apptViewDtos.get(viewCorrId).getSvcIds().add(reschApptGrpPremsQueryDto.getSvcId());
                        svcIds=apptViewDtos.get(viewCorrId).getSvcIds();
                    }
                    String dateRang=new SimpleDateFormat(SystemAdminBaseConstants.DATE_FORMAT).format(dateRange);
                    String inDate=new SimpleDateFormat(SystemAdminBaseConstants.DATE_FORMAT).format(reschApptGrpPremsQueryDto.getRecomInDate());
                    boolean flag1=inDate.compareTo(dateRang)>=0;
                    boolean flag2=!appStatus.contains(reschApptGrpPremsQueryDto.getApplicationStatus());
                    boolean flag3=reschApptGrpPremsQueryDto.getRescheduleCount()<systemParamConfig.getRescheduleMaxCount();
                    boolean flag4=reschApptGrpPremsQueryDto.getInsApptStatus()!=null;
                    if (flag1 && flag2&&flag3&&flag4){
                        apptViewDto.setCanReschedule(Boolean.TRUE);
                    }else {
                        apptViewDto.setCanReschedule(Boolean.FALSE);
                    }
                    apptViewDto.setSvcIds(svcIds);
                    apptViewDto.setAppGrpId(reschApptGrpPremsQueryDto.getAppGrpPremId());
                    apptViewDto.setApplicationNo(reschApptGrpPremsQueryDto.getApplicationNo());
                    apptViewDto.setVehicleNo(reschApptGrpPremsQueryDto.getVehicleNo());
                    apptViewDto.setAppId(reschApptGrpPremsQueryDto.getId());
                    apptViewDto.setAppCorrId(reschApptGrpPremsQueryDto.getAppCorrId());
                    apptViewDto.setLicenseeId(reschApptGrpPremsQueryDto.getLicenseeId());
                    apptViewDto.setAddress(reschApptGrpPremsQueryDto.getAddress());
                    apptViewDto.setInspStartDate(reschApptGrpPremsQueryDto.getRecomInDate());
                    apptViewDto.setHciCode(reschApptGrpPremsQueryDto.getHciCode());
                    apptViewDto.setHciName(reschApptGrpPremsQueryDto.getHciName());
                    apptViewDto.setFastTracking(reschApptGrpPremsQueryDto.getFastTracking());
                    apptViewDto.setViewCorrId(viewCorrId);
                    apptViewDto.setChecked(Boolean.FALSE);
                    if(keyIds!=null){
                        if(keys.contains(viewCorrId)){
                            apptViewDto.setChecked(Boolean.TRUE);
                        }
                    }
                    apptViewDtos.put(viewCorrId,apptViewDto);
                }
                List<ApptViewDto> apptViewDtos1=IaisCommonUtils.genNewArrayList();
                for (Map.Entry<String,ApptViewDto> entry:apptViewDtos.entrySet()
                ) {
                    apptViewDtos1.add(entry.getValue());
                }
                List<ApptViewDto> apptViewDtos2=IaisCommonUtils.genNewArrayList();
                for(int i=0;i<pgSize;i++){
                    if(apptViewDtos1.size()>((pgNo-1)*pgSize+i)){
                        apptViewDtos2.add(apptViewDtos1.get((pgNo-1)*pgSize+i));
                    }
                }
                result.setRowCount(apptViewDtos1.size());
                ParamUtil.setRequestAttr(bpc.request, "apptViewDtos", apptViewDtos2);
                ParamUtil.setSessionAttr(bpc.request, "apptViewDtosMap", apptViewDtos);
                ParamUtil.setRequestAttr(bpc.request,"SearchResult",result);
            }


            rescheduleParam.setPageNo(pgNo);
            rescheduleParam.setPageSize(pgSize);
            ParamUtil.setRequestAttr(bpc.request,"SearchParam",rescheduleParam);
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }

    }

    public void doSort(BaseProcessClass bpc)  {
        SearchResultHelper.doSort(bpc.request,rescheduleParameter);
        rescheduleParameter.setPageNo(1);
    }
    public void doPage(BaseProcessClass bpc)  {
        int pageSize = rescheduleParameter.getPageSize();
        SearchResultHelper.doPage(bpc.request,rescheduleParameter);
        if(rescheduleParameter.getPageSize()!=pageSize){
            rescheduleParameter.setPageNo(1);
        }
    }

    public void doReschedule(BaseProcessClass bpc)  {
        String [] keyIds=ParamUtil.getStrings(bpc.request,"appIds");
        ParamUtil.setSessionAttr(bpc.request,"DashboardTitle","Appointment Rescheduling");

        if(keyIds==null){
            keyIds= (String[]) ParamUtil.getSessionAttr(bpc.request,"appIds");
        }
        Set<String> keys=IaisCommonUtils.genNewHashSet() ;
        if(keyIds!=null){
            keys.addAll(Arrays.asList(keyIds));
        }
        Map<String ,ApptViewDto> apptViewDtos= (Map<String, ApptViewDto>) ParamUtil.getSessionAttr(bpc.request,"apptViewDtosMap");

        List<ApptViewDto> apptViewDtos1=IaisCommonUtils.genNewArrayList();
        for (Map.Entry<String,ApptViewDto> entry:apptViewDtos.entrySet()
        ) {
            if(keys.contains(entry.getKey())){
                apptViewDtos1.add(entry.getValue());
            }
        }
        ParamUtil.setRequestAttr(bpc.request, "apptViewDtos", apptViewDtos1);
        ParamUtil.setSessionAttr(bpc.request,"appIds",keyIds);
    }

    public void preCommPool(BaseProcessClass bpc) throws ParseException {
        String [] keyIds= (String[]) ParamUtil.getSessionAttr(bpc.request,"appIds");
        ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "Y");
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        errorMap=validate(bpc.request,keyIds);
        if (!errorMap.isEmpty()) {
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, "N");
            //
            return;
        }
        Map<String ,ApptViewDto> apptViewDtos= (Map<String, ApptViewDto>) ParamUtil.getSessionAttr(bpc.request,"apptViewDtosMap");

        List<ApptViewDto> apptViewDtos1=IaisCommonUtils.genNewArrayList();
        for (String key: keyIds
        ) {
            ApptViewDto apptViewDto=apptViewDtos.get(key);
            apptViewDtos1.add(apptViewDto);

        }
        rescheduleService.updateAppStatusCommPool(apptViewDtos1);
        for (ApptViewDto apptViewDto:apptViewDtos1
        ) {
            try {
                sendReschedulingSuccessEmail(apptViewDto);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }

    }


    public void sendReschedulingSuccessEmail(ApptViewDto apptViewDto)  {

        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
        ApplicationDto applicationDto=applicationFeClient.getApplicationById(apptViewDto.getAppId()).getEntity();


        ApplicationGroupDto applicationGroupDto = applicationFeClient.getApplicationGroup(applicationDto.getAppGrpId()).getEntity();
        if (applicationGroupDto != null){
            OrgUserDto orgUserDto = organizationLienceseeClient.retrieveOneOrgUserAccount(applicationGroupDto.getSubmitBy()).getEntity();
            if (orgUserDto != null){
                emailMap.put("ApplicantName", orgUserDto.getDisplayName());
            }
        }


        emailMap.put("DDMMYYYY", Formatter.formatDate(new Date()));
        emailMap.put("email_address1", systemParamConfig.getSystemAddressOne());
        emailMap.put("email_address2", systemParamConfig.getSystemAddressTwo());
        emailMap.put("time", Formatter.formatTime(new Date()));
        emailMap.put("HCI_Name", apptViewDto.getHciName());
        emailMap.put("HCI_Code", apptViewDto.getHciCode());
        emailMap.put("premises_address", apptViewDto.getAddress());
        emailMap.put("contact_no", systemParamConfig.getSystemPhoneNumber());
        emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RESCHEDULING_SUCCESSFULLY);
        emailParam.setTemplateContent(emailMap);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationDto.getApplicationNo());
        //email
        notificationHelper.sendNotification(emailParam);
        //msg
        try {
            EmailParam msgParam = new EmailParam();
            msgParam.setQueryCode(applicationDto.getApplicationNo());
            msgParam.setReqRefNum(applicationDto.getApplicationNo());
            msgParam.setRefId(applicationDto.getApplicationNo());
            msgParam.setTemplateContent(emailMap);
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RESCHEDULING_SUCCESSFULLY_MSG);

            HcsaServiceDto svcDto = appConfigClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
            List<String> svcCode=IaisCommonUtils.genNewArrayList();
            svcCode.add(svcDto.getSvcCode());
            msgParam.setSvcCodeList(svcCode);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            msgParam.setRefId(applicationDto.getApplicationNo());
            notificationHelper.sendNotification(msgParam);
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }
        //sms
        EmailParam smsParam = new EmailParam();
        smsParam.setQueryCode(applicationDto.getApplicationNo());
        smsParam.setReqRefNum(applicationDto.getApplicationNo());
        smsParam.setRefId(applicationDto.getApplicationNo());
        smsParam.setTemplateContent(emailMap);
        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RESCHEDULING_SUCCESSFULLY_SMS);
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);

        notificationHelper.sendNotification(smsParam);
    }

    public Map<String, String> validate(HttpServletRequest httpServletRequest , String[] apptIds) throws ParseException {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        Map<String ,ApptViewDto> apptViewDtos= (Map<String, ApptViewDto>) ParamUtil.getSessionAttr(httpServletRequest,"apptViewDtosMap");
        for (String id:apptIds
        ) {

            String reason=ParamUtil.getString(httpServletRequest,"reason"+id);

            String appId=apptViewDtos.get(id).getAppId();
            if("".equals(reason)||reason==null){
                errMap.put("reason" + appId, MessageUtil.replaceMessage("GENERAL_ERR0006", "Reason for Request","field"));
            }
            Date inspStDate=Formatter.parseDate(ParamUtil.getString(httpServletRequest, "newStartDate"+id));
            Date inspEndDate=Formatter.parseDate(ParamUtil.getString(httpServletRequest, "newEndDate"+id));
            apptViewDtos.get(id).setReason(reason);

            String inspStartDate = Formatter.formatDateTime(inspStDate, SystemAdminBaseConstants.DATE_FORMAT);
            String inspToDate = Formatter.formatDateTime(inspEndDate, SystemAdminBaseConstants.DATE_FORMAT);
            String nowDate = Formatter.formatDateTime(new Date(), SystemAdminBaseConstants.DATE_FORMAT);
            if(!StringUtil.isEmpty(inspStartDate)&&!StringUtil.isEmpty(inspToDate)){
                Calendar cal = Calendar.getInstance();
                cal.setTime(inspStDate);
                cal.add(Calendar.DAY_OF_MONTH, 2);
                String inspStartDate_3 = Formatter.formatDateTime(cal.getTime(), SystemAdminBaseConstants.DATE_FORMAT);
                if( inspStartDate_3.compareTo(inspToDate)>0){
                    errMap.put("newDate" + appId,MessageUtil.getMessageDesc("OAPPT_ERR013"));
                }
                if(inspStartDate.compareTo(nowDate)<0){
                    errMap.put("newDate" + appId,MessageUtil.getMessageDesc("OAPPT_ERR015"));
                }
            }else {
                errMap.put("newDate" + appId,MessageUtil.replaceMessage("GENERAL_ERR0006", "New Date","field"));
            }

        }
        boolean err=false;
        for (String id:apptIds
        ) {

            String reason=ParamUtil.getString(httpServletRequest,"reason"+id);

            String appId=apptViewDtos.get(id).getAppId();
            if("".equals(reason)||reason==null){
                errMap.put("reason" + appId, MessageUtil.replaceMessage("GENERAL_ERR0006", "Reason for Request","field"));
            }
            Date inspStDate=Formatter.parseDate(ParamUtil.getString(httpServletRequest, "newStartDate"+id));
            Date inspEndDate=Formatter.parseDate(ParamUtil.getString(httpServletRequest, "newEndDate"+id));
            String inspNewDate=  ParamUtil.getString(httpServletRequest, "newDate"+id);
            apptViewDtos.get(id).setReason(reason);
            boolean changeReason=false;
            if(apptViewDtos.get(id).getReason()!=null){
                changeReason=!apptViewDtos.get(id).getReason().equals(reason);
            }
            boolean changeStartDate=false;
            if(apptViewDtos.get(id).getSpecificStartDate()!=null){
                changeStartDate=!apptViewDtos.get(id).getSpecificStartDate().equals(inspStDate);
            }
            boolean changeEndDate=false;
            if(apptViewDtos.get(id).getSpecificEndDate()!=null){
                changeEndDate=!apptViewDtos.get(id).getSpecificEndDate().equals(inspEndDate);
            }
            boolean hasNewDate=inspNewDate==null;

            if(changeStartDate||changeEndDate||changeReason||hasNewDate){
                err=true;
            }
        }
        if(errMap.isEmpty()){
            String OAPPT_ERR012Msg =  MessageUtil.getMessageDesc("OAPPT_ERR012");
            for (String id:apptIds){
                Date inspStDate=Formatter.parseDate(ParamUtil.getString(httpServletRequest, "newStartDate"+id));
                Date inspEndDate=Formatter.parseDate(ParamUtil.getString(httpServletRequest, "newEndDate"+id));

                if(err)
                {
                    apptViewDtos.get(id).setSpecificStartDate(inspStDate);
                    apptViewDtos.get(id).setSpecificEndDate(inspEndDate);
                    AppointmentDto appointmentDto=new AppointmentDto();
                    ApptViewDto apptViewDto= apptViewDtos.get(id);
                    List<ApptViewDto> apptViewDtos1=IaisCommonUtils.genNewArrayList();
                    apptViewDtos1.add(apptViewDto);
                    List<AppPremisesCorrelationDto> appPremisesCorrelationDtos= applicationFeClient.appPremCorrDtosByApptViewDtos(apptViewDtos1).getEntity();
                    List<String> appNoList=IaisCommonUtils.genNewArrayList();
                    for (AppPremisesCorrelationDto apc:appPremisesCorrelationDtos
                    ) {
                        ApplicationDto applicationDto = applicationFeClient.getApplicationByCorreId(apc.getId()).getEntity();
                        appNoList.add(applicationDto.getApplicationNo());
                    }

                    appointmentDto.setAppNoList(appNoList);
                    appointmentDto.setStartDate(Formatter.formatDateTime(inspStDate, "dd/MM/yyyy HH:mm:ss"));
                    appointmentDto.setEndDate(Formatter.formatDateTime(inspEndDate, "dd/MM/yyyy HH:mm:ss"));
                    HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                    HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                    List<ApptRequestDto> result  = feEicGatewayClient.genInspApptRescheduleDate(appointmentDto, signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization()).getEntity();
                    if(result!=null&&result.size()!=0){
                        if(result.get(0).getUserClandars()!=null){
                            apptViewDtos.get(id).setInspNewDate(result.get(0).getUserClandars().get(0).getStartSlot().get(0));
                            List<ApptUserCalendarDto> userIds=IaisCommonUtils.genNewArrayList();
                            userIds.addAll(result.get(0).getUserClandars());
                            apptViewDtos.get(id).setUserIds(userIds);
                            apptViewDtos.get(id).setApptRefNo(result.get(0).getApptRefNo());
                        }else {
                            apptViewDtos.get(id).setInspNewDate(null);
                            apptViewDtos.get(id).setUserIds(null);
                            apptViewDtos.get(id).setApptRefNo(null);
                            errMap.put("inspDate" + apptViewDtos.get(id).getAppId(),OAPPT_ERR012Msg);
                        }
                    }else {
                        apptViewDtos.get(id).setInspNewDate(null);
                        apptViewDtos.get(id).setUserIds(null);
                        errMap.put("inspDate" + apptViewDtos.get(id).getAppId(),OAPPT_ERR012Msg);
                        apptViewDtos.get(id).setApptRefNo(null);
                    }
                }
            }

        }else {
            for (String id:apptIds){
                Date inspStDate=Formatter.parseDate(ParamUtil.getString(httpServletRequest, "newStartDate"+id));
                Date inspEndDate=Formatter.parseDate(ParamUtil.getString(httpServletRequest, "newEndDate"+id));
                apptViewDtos.get(id).setSpecificStartDate(inspStDate);
                apptViewDtos.get(id).setSpecificEndDate(inspEndDate);

            }
        }
        if(err){
            errMap.put("newTESTDate","1");
        }
        ParamUtil.setSessionAttr(httpServletRequest, "apptViewDtosMap", (Serializable) apptViewDtos);

        return errMap;
    }

    public  String getRepalceService(){
        List<HcsaServiceDto> hcsaServiceDtos = hcsaConfigClient.getActiveServices().getEntity();
        if(IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(" ( CASE app.service_id ");
        for(HcsaServiceDto hcsaServiceDto :hcsaServiceDtos){
            stringBuilder.append(" WHEN '").append(hcsaServiceDto.getId()).append("' Then '").append(hcsaServiceDto.getSvcCode()).append("'  ");
        }
        stringBuilder.append("ELSE  'N/A' END )");
        return  stringBuilder.toString();
    }
}