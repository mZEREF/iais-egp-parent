package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschApptGrpPremsQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.dto.ApptViewDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.service.RescheduleService;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

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
    RescheduleService rescheduleService;
    private FilterParameter rescheduleParameter = new FilterParameter.Builder()
            .clz(ReschApptGrpPremsQueryDto.class)
            .searchAttr("SearchParam")
            .resultAttr("SearchResult")
            .sortField("BLK_NO").sortType(SearchParam.ASCENDING).build();


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
        SearchResultHelper.doSort(bpc.request, rescheduleParameter);
        SearchParam rescheduleParam = SearchResultHelper.getSearchParam(bpc.request, rescheduleParameter,true);
        rescheduleParam.addFilter("licenseeId", licenseeId, true);
        QueryHelp.setMainSql("rescheduleQuery", "queryApptGrpPremises", rescheduleParam);

        SearchResult<ReschApptGrpPremsQueryDto> result= rescheduleService.searchApptReschPrem(rescheduleParam);

        List<ReschApptGrpPremsQueryDto> rows = result.getRows();
        List<ApptViewDto> apptViewDtos= IaisCommonUtils.genNewArrayList();
        List<String> apptRefNos=IaisCommonUtils.genNewArrayList();
        for (ReschApptGrpPremsQueryDto reschApptGrpPremsQueryDto : rows) {
            apptRefNos.add(reschApptGrpPremsQueryDto.getApptRefNo());
        }
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        Map<String, List<ApptUserCalendarDto>> apptInspDateMap = feEicGatewayClient.getAppointmentByApptRefNo(apptRefNos, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
        for (ReschApptGrpPremsQueryDto reschApptGrpPremsQueryDto : rows) {
            ApptViewDto apptViewDto=new ApptViewDto();
            apptViewDto.setAppId(reschApptGrpPremsQueryDto.getId());
            apptViewDto.setAppCorrId(reschApptGrpPremsQueryDto.getAppCorrId());
            apptViewDto.setLicenseeId(reschApptGrpPremsQueryDto.getLicenseeId());
            apptViewDto.setAddress(MiscUtil.getAddress(reschApptGrpPremsQueryDto.getBlkNo(),reschApptGrpPremsQueryDto.getStreetName(),reschApptGrpPremsQueryDto.getBuildingName(),reschApptGrpPremsQueryDto.getFloorNo(),reschApptGrpPremsQueryDto.getUnitNo(),reschApptGrpPremsQueryDto.getPostalCode()));
            try {
                apptViewDto.setInspStartDate(apptInspDateMap.get(reschApptGrpPremsQueryDto.getApptRefNo()).get(0).getStartSlot().get(0));
            }catch (NullPointerException e){
                log.info(e.getMessage(),e);
            }
            apptViewDtos.add(apptViewDto);
        }
        ParamUtil.setRequestAttr(bpc.request, "apptViewDtos", apptViewDtos);



    }

    public void doReschedule(BaseProcessClass bpc)  {}

    public void preCommPool(BaseProcessClass bpc)  {}

    public void preRoundRobin(BaseProcessClass bpc)  {}

}
