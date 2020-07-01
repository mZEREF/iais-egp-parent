package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschApptGrpPremsQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.service.ApptConfirmReSchDateService;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    ApptConfirmReSchDateService rescheduleService;
    private FilterParameter rescheduleParameter = new FilterParameter.Builder()
            .clz(ReschApptGrpPremsQueryDto.class)
            .searchAttr("SearchParam")
            .resultAttr("SearchResult")
            .sortField("ADDRESS").sortType(SearchParam.ASCENDING).pageNo(1).pageSize(10).build();


    @Autowired
    private FeEicGatewayClient feEicGatewayClient;


    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    public void start(BaseProcessClass bpc)  {

    }

    public void init(BaseProcessClass bpc)  {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String licenseeId = loginContext.getLicenseeId();
        SearchParam rescheduleParam = IaisEGPHelper.getSearchParam(bpc.request, true,rescheduleParameter);
        StringBuilder stringBuilder=new StringBuilder();
        for (String appSt: IaisCommonUtils.APP_STATUS_NO_RESCHEDULE
             ) {
            stringBuilder.append(appSt).append(',');
        }
        rescheduleParam.addParam("appStatus_reschedule", "(app.status not in('"+stringBuilder.toString()+"'))");
        rescheduleParam.addParam("RESCHEDULE_COUNT", "(insAppt.RESCHEDULE_COUNT <="+1+")");
        Date dueDate;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,14);
        dueDate =calendar.getTime();
        rescheduleParam.addParam("RECOM_IN_DATE", "( appRec.RECOM_IN_DATE >= convert(datetime,"+ Formatter.formatDateTime(new Date(), SystemAdminBaseConstants.DATE_FORMAT)+") AND appRec.RECOM_IN_DATE <= convert(datetime,"+Formatter.formatDateTime(dueDate, SystemAdminBaseConstants.DATE_FORMAT)+"))");
        rescheduleParam.addFilter("licenseeId", licenseeId, true);
        QueryHelp.setMainSql("rescheduleQuery", "queryApptGrpPremises", rescheduleParam);

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);


        try {
            SearchResult<ReschApptGrpPremsQueryDto> result  = feEicGatewayClient.eicSearchApptReschPrem(rescheduleParam, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();

            List<ReschApptGrpPremsQueryDto> rows = result.getRows();
            if(rows!=null){
                LinkedHashMap<String ,ApptViewDto> apptViewDtos= new LinkedHashMap<>(rows.size());

                ParamUtil.setRequestAttr(bpc.request,"SearchResult",result);
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
                    apptViewDto.setSvcIds(svcIds);
                    apptViewDto.setVehicleNo(reschApptGrpPremsQueryDto.getVehicleNo());
                    apptViewDto.setAppId(reschApptGrpPremsQueryDto.getId());
                    apptViewDto.setAppCorrId(reschApptGrpPremsQueryDto.getAppCorrId());
                    apptViewDto.setLicenseeId(reschApptGrpPremsQueryDto.getLicenseeId());
                    apptViewDto.setAddress(reschApptGrpPremsQueryDto.getAddress());
                    apptViewDto.setInspStartDate(reschApptGrpPremsQueryDto.getRecomInDate());
                    apptViewDto.setFastTracking(reschApptGrpPremsQueryDto.getFastTracking());
                    apptViewDto.setViewCorrId(viewCorrId);
                    apptViewDtos.put(viewCorrId,apptViewDto);
                }
                List<ApptViewDto> apptViewDtos1=IaisCommonUtils.genNewArrayList();
                for (Map.Entry<String,ApptViewDto> entry:apptViewDtos.entrySet()
                     ) {
                    apptViewDtos1.add(entry.getValue());
                }
                ParamUtil.setRequestAttr(bpc.request, "apptViewDtos", apptViewDtos1);
                ParamUtil.setSessionAttr(bpc.request, "apptViewDtosMap", apptViewDtos);
            }
            ParamUtil.setRequestAttr(bpc.request,"SearchParam",rescheduleParam);
        }catch (Exception e){
            log.info(e.getMessage(),e);
        }

    }

    public void doSort(BaseProcessClass bpc)  {
        SearchResultHelper.doSort(bpc.request,rescheduleParameter);
    }
    public void doPage(BaseProcessClass bpc)  {SearchResultHelper.doPage(bpc.request,rescheduleParameter);
    }

    public void doReschedule(BaseProcessClass bpc)  {}

    public void preCommPool(BaseProcessClass bpc)  {
        String [] keyIds=ParamUtil.getStrings(bpc.request,"appIds");
        Map<String ,ApptViewDto> apptViewDtos= (Map<String, ApptViewDto>) ParamUtil.getSessionAttr(bpc.request,"apptViewDtosMap");

        List<ApptViewDto> apptViewDtos1=IaisCommonUtils.genNewArrayList();
        for (String key: keyIds
             ) {
            apptViewDtos1.add(apptViewDtos.get(key));
        }
        rescheduleService.updateAppStatusCommPool(apptViewDtos1);

    }


}
