package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschApptGrpPremsQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AppointmentUtil;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SysParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApptConfirmReSchDateService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
            .sortField("appRec.RECOM_IN_DATE").sortType(SearchParam.DESCENDING).pageNo(1).pageSize(SysParamUtil.getDefaultPageSize()).build();

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



    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    public void start(BaseProcessClass bpc)  {
        rescheduleParameter.setSortField("appRec.RECOM_IN_DATE");
        rescheduleParameter.setPageNo(1);
    }

    public void init(BaseProcessClass bpc)  {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
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
        QueryHelp.setMainSql("rescheduleQuery", "queryApptGrpPremises", rescheduleParam);

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String [] keyIds= (String[]) ParamUtil.getSessionAttr(bpc.request,"appIds");
        Set<String> keys=IaisCommonUtils.genNewHashSet() ;
        if(keyIds!=null){
            keys.addAll(Arrays.asList(keyIds));
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
                    String viewCorrId=reschApptGrpPremsQueryDto.getAppGrpId()+reschApptGrpPremsQueryDto.getPremisesAddress();
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
                    apptViewDto.setVehicleNo(reschApptGrpPremsQueryDto.getVehicleNo());
                    apptViewDto.setAppId(reschApptGrpPremsQueryDto.getId());
                    apptViewDto.setAppCorrId(reschApptGrpPremsQueryDto.getAppCorrId());
                    apptViewDto.setLicenseeId(reschApptGrpPremsQueryDto.getLicenseeId());
                    apptViewDto.setAddress(MiscUtil.getAddress(reschApptGrpPremsQueryDto.getBlkNo(),reschApptGrpPremsQueryDto.getStreetName(),reschApptGrpPremsQueryDto.getBuildingName(),reschApptGrpPremsQueryDto.getFloorNo(),reschApptGrpPremsQueryDto.getUnitNo(),reschApptGrpPremsQueryDto.getPostalCode()));
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
                ParamUtil.setRequestAttr(bpc.request, "apptViewDtos", apptViewDtos1);
                ParamUtil.setSessionAttr(bpc.request, "apptViewDtosMap", apptViewDtos);
                ParamUtil.setRequestAttr(bpc.request,"SearchResult",result);
            }
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
        SearchResultHelper.doPage(bpc.request,rescheduleParameter);
    }

    public void doReschedule(BaseProcessClass bpc)  {
        String [] keyIds=ParamUtil.getStrings(bpc.request,"appIds");
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

    public void preCommPool(BaseProcessClass bpc)  {
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
            String reason=ParamUtil.getString(bpc.request,"reason"+key);
            apptViewDto.setReason(reason);
            apptViewDtos1.add(apptViewDto);

            try {
                sendReschedulingSuccessEmail(apptViewDto);
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(), e);
            }

        }
        rescheduleService.updateAppStatusCommPool(apptViewDtos1);

    }


    public void sendReschedulingSuccessEmail(ApptViewDto apptViewDto) throws IOException, TemplateException {

        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
        ApplicationDto applicationDto=applicationFeClient.getApplicationById(apptViewDto.getAppId()).getEntity();


        //TODO Need to be replaced with appSubmissionDto, and set submit by id to it
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
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RESCHEDULING_SUCCESSFULLY_MSG);

            HcsaServiceDto svcDto = appConfigClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
            List<String> svcCode=IaisCommonUtils.genNewArrayList();
            svcCode.add(svcDto.getSvcCode());
            emailParam.setSvcCodeList(svcCode);
            emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            emailParam.setRefId(applicationDto.getApplicationNo());
            notificationHelper.sendNotification(emailParam);
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }
        //sms
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RESCHEDULING_SUCCESSFULLY_SMS);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);

        notificationHelper.sendNotification(emailParam);
    }

    public Map<String, String> validate(HttpServletRequest httpServletRequest , String[] apptIds) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        Map<String ,ApptViewDto> apptViewDtos= (Map<String, ApptViewDto>) ParamUtil.getSessionAttr(httpServletRequest,"apptViewDtosMap");

        for (String id:apptIds
             ) {
            String reason=ParamUtil.getString(httpServletRequest,"reason"+id);
            if("".equals(reason)||reason==null){
                String appId=apptViewDtos.get(id).getAppId();
                errMap.put("reason" + appId, MessageUtil.replaceMessage("GENERAL_ERR0006", "Reason for Request","field"));
            }
        }

        return errMap;
    }
}