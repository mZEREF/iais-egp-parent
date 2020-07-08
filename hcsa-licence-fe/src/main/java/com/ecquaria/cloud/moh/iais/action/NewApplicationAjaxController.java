package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremPhOpenPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.sql.SqlMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Wenkang
 * @date 2019/12/5 15:51
 */
@Controller
@Slf4j
public class NewApplicationAjaxController {

    public static final String SERVICEALLPSNCONFIGMAP = "ServiceAllPsnConfigMap";

    @Autowired
    private ServiceConfigService serviceConfigService;

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

    //=============================================================================
    //ajax method
    //=============================================================================
    /**
     * @param
     * @description: ajax
     * @author: zixia
     */
    @GetMapping(value = "/retrieve-address")
    public @ResponseBody
    PostCodeDto retrieveYourAddress(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the do loadPremisesByPostCode start ...."));
        String postalCode = ParamUtil.getDate(request, "postalCode");
        if(StringUtil.isEmpty(postalCode)){
            log.debug(StringUtil.changeForLog("postCode is null"));
            return null;
        }
        PostCodeDto postCodeDto = null;
        try {
            postCodeDto = serviceConfigService.getPremisesByPostalCode(postalCode);
        }catch (Exception e){
            log.debug(StringUtil.changeForLog("api exception"));
        }

        log.debug(StringUtil.changeForLog("the do loadPremisesByPostCode end ...."));
        return postCodeDto;
    }



    /**
     * @param
     * @description: ajax
     * @author: zixia
     */
    @RequestMapping(value = "/premises-html", method = RequestMethod.GET)
    public @ResponseBody String addPremisesHtml(HttpServletRequest request) {
        List<SelectOption> timeHourList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i< 24;i++){
            timeHourList.add(new SelectOption(String.valueOf(i), i<10?"0"+String.valueOf(i):String.valueOf(i)));
        }
        List<SelectOption> timeMinList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i< 60;i++){
            timeMinList.add(new SelectOption(String.valueOf(i), i<10?"0"+String.valueOf(i):String.valueOf(i)));
        }

        log.debug(StringUtil.changeForLog("the add premises html start ...."));
        String currentLength = ParamUtil.getRequestString(request, "currentLength");
        log.debug(StringUtil.changeForLog("currentLength : "+currentLength));

        String sql = SqlMap.INSTANCE.getSql("premises", "premisesHtml").getSqlStr();
        Set<String> premType = (Set<String>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.PREMISESTYPE);
        StringBuilder premTypeBuffer = new StringBuilder();

        for(String type:premType){
            String className = "";
            String width = "";
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(type)){
                className = "onSite";
                width = "width: 20%;";
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(type)){
                className = "conveyance";
                width = "width: 27%;";
            }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(type)){
                className = "offSite";
                width = "width: 19%;";
            }
            premTypeBuffer.append("<div class=\"col-xs-5 \" style=\"").append(width).append("\">")
                    .append("<div class=\"form-check\">").append("<input class=\"form-check-input premTypeRadio ").append(className).append("\"  type=\"radio\" name=\"premType").append(currentLength).append("\" value = ").append(type).append(" aria-invalid=\"false\">");
            if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(type)){
                premTypeBuffer.append(" <label class=\"form-check-label\" ><span class=\"check-circle\"></span>On-site<br/><span>(at a fixed address)</span></label>");
            }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(type)){
                premTypeBuffer.append(" <label class=\"form-check-label\" ><span class=\"check-circle\"></span>Conveyance<br/><span>(in a mobile clinic / ambulance)</span></label>");
            }else if(ApplicationConsts.PREMISES_TYPE_OFF_SITE.equals(type)){
                premTypeBuffer.append(" <label class=\"form-check-label\" ><span class=\"check-circle\"></span>Off-site<br/><span>(as tele-medicine)</span></label>");
            }
            premTypeBuffer.append("</div>")
                    .append("</div>");
        }

        //premiseSelect -- on-site
        List<SelectOption> premisesOnSite= (List) ParamUtil.getSessionAttr(request, "premisesSelect");
        Map<String,String> premisesOnSiteAttr = IaisCommonUtils.genNewHashMap();
        premisesOnSiteAttr.put("class", "premSelect");
        premisesOnSiteAttr.put("id", "onSiteSel");
        premisesOnSiteAttr.put("name", "onSiteSelect");
        premisesOnSiteAttr.put("style", "display: none;");
        String premOnSiteSelectStr = NewApplicationHelper.generateDropDownHtml(premisesOnSiteAttr, premisesOnSite, null, null);
        //premiseSelect -- conveyance
        List<SelectOption> premisesConv= (List) ParamUtil.getSessionAttr(request, "conveyancePremSel");
        Map<String,String> premisesConvAttr = IaisCommonUtils.genNewHashMap();
        premisesConvAttr.put("class", "premSelect");
        premisesConvAttr.put("id", "conveyanceSel");
        premisesConvAttr.put("name", "conveyanceSelect");
        premisesConvAttr.put("style", "display: none;");
        String premConvSelectStr = NewApplicationHelper.generateDropDownHtml(premisesConvAttr, premisesConv, null, null);
        //premisesSelect -- offSite
        List<SelectOption> premisesOffSite= (List) ParamUtil.getSessionAttr(request, "offSitePremSel");
        Map<String,String> premisesOffSiteAttr = IaisCommonUtils.genNewHashMap();
        premisesOffSiteAttr.put("class", "premSelect");
        premisesOffSiteAttr.put("id", "offSiteSel");
        premisesOffSiteAttr.put("name", "offSiteSelect");
        premisesOffSiteAttr.put("style", "display: none;");
        String premOffSiteSelectStr = NewApplicationHelper.generateDropDownHtml(premisesOffSiteAttr, premisesOffSite, null, null);

        List<SelectOption> addrTypes= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_ADDRESS_TYPE);
        //Address Type on-site
        Map<String,String> addrTypesAttr = IaisCommonUtils.genNewHashMap();
        addrTypesAttr.put("class", "onSiteAddressType");
        addrTypesAttr.put("id", "onSiteAddressType");
        addrTypesAttr.put("name", "onSiteAddressType");
        addrTypesAttr.put("style", "display: none;");
        String addrTypeSelectStr = NewApplicationHelper.generateDropDownHtml(addrTypesAttr, addrTypes,NewApplicationDelegator.FIRESTOPTION, null);
        //Address Type conveyance
        Map<String,String> conAddrTypesAttr = IaisCommonUtils.genNewHashMap();
        conAddrTypesAttr.put("class", "conveyanceAddressType");
        conAddrTypesAttr.put("id", "conveyanceAddressType");
        conAddrTypesAttr.put("name", "conveyanceAddrType");
        conAddrTypesAttr.put("style", "display: none;");
        String conAddrTypeSelectStr = NewApplicationHelper.generateDropDownHtml(conAddrTypesAttr, addrTypes, NewApplicationDelegator.FIRESTOPTION, null);
        //Address Type offSite
        Map<String,String> offSiteAddrTypesAttr = IaisCommonUtils.genNewHashMap();
        offSiteAddrTypesAttr.put("class", "offSiteAddressType");
        offSiteAddrTypesAttr.put("id", "offSiteAddressType");
        offSiteAddrTypesAttr.put("name", "offSiteAddrType");
        offSiteAddrTypesAttr.put("style", "display: none;");
        String offSiteAddrTypeSelectStr = NewApplicationHelper.generateDropDownHtml(offSiteAddrTypesAttr, addrTypes, NewApplicationDelegator.FIRESTOPTION, null);

        //onsite operation time
        Map<String,String> premiseHour = IaisCommonUtils.genNewHashMap();
        premiseHour.put("class", "onSiteStartHH");
        premiseHour.put("id", "onSiteStartHH");
        premiseHour.put("name", "onSiteStartHH");
        premiseHour.put("style", "display: none;");
        String onsitestarHH = NewApplicationHelper.generateDropDownHtml(premiseHour, timeHourList,"--", null);

        Map<String,String> premiseMinute = IaisCommonUtils.genNewHashMap();
        premiseMinute.put("class", "onSiteStartMM");
        premiseMinute.put("id", "onSiteStartMM");
        premiseMinute.put("name", "onSiteStartMM");
        premiseMinute.put("style", "display: none;");
        String onsitestarMM = NewApplicationHelper.generateDropDownHtml(premiseMinute, timeMinList,"--", null);

        Map<String,String> siteEndHH = IaisCommonUtils.genNewHashMap();
        siteEndHH.put("class", "onSiteEndHH");
        siteEndHH.put("id", "onSiteEndHH");
        siteEndHH.put("name", "onSiteEndHH");
        siteEndHH.put("style", "display: none;");
        String onsiteEndHH = NewApplicationHelper.generateDropDownHtml(siteEndHH, timeHourList,"--", null);

        Map<String,String> siteEndMM = IaisCommonUtils.genNewHashMap();
        siteEndMM.put("class", "onSiteEndMM");
        siteEndMM.put("id", "onSiteEndMM");
        siteEndMM.put("name", "onSiteEndMM");
        siteEndMM.put("style", "display: none;");
        String onsiteEndMM = NewApplicationHelper.generateDropDownHtml(siteEndMM, timeMinList,"--", null);

        //onsite ph
        String premName = currentLength;

        List<SelectOption> publicHolidayList = IaisCommonUtils.genNewArrayList();
        List<PublicHolidayDto> publicHolidayDtoList = IaisCommonUtils.genNewArrayList();

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        try {
            publicHolidayDtoList = feEicGatewayClient.getpublicHoliday(signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        publicHolidayDtoList.stream().forEach(pb -> {
            publicHolidayList.add(new SelectOption(Formatter.formatDate(pb.getFromDate()),pb.getDescription()));
        });

        Map<String,String> publicHoliday = IaisCommonUtils.genNewHashMap();
        publicHoliday.put("class", "onSitePubHoliday");
        publicHoliday.put("id", premName+"onSitePubHoliday0");
        publicHoliday.put("name", premName+"onSitePubHoliday0");
        publicHoliday.put("style", "display: none;");
        String publicHolidayDD = NewApplicationHelper.generateDropDownHtml(publicHoliday, publicHolidayList,"Please Select", null);

        Map<String,String> pbholidaystartHH = IaisCommonUtils.genNewHashMap();
        pbholidaystartHH.put("class", "onSitePbHolDayStartHH");
        pbholidaystartHH.put("id", premName+"onSitePbHolDayStartHH0");
        pbholidaystartHH.put("name", premName+"onSitePbHolDayStartHH0");
        pbholidaystartHH.put("style", "display: none;");
        String holidaystartHH = NewApplicationHelper.generateDropDownHtml(pbholidaystartHH, timeHourList,"--", null);

        Map<String,String> pbholidaystartMM = IaisCommonUtils.genNewHashMap();
        pbholidaystartMM.put("class", "onSitePbHolDayStartMM");
        pbholidaystartMM.put("id", premName+"onSitePbHolDayStartMM0");
        pbholidaystartMM.put("name", premName+"onSitePbHolDayStartMM0");
        pbholidaystartMM.put("style", "display: none;");
        String holidaystartMM = NewApplicationHelper.generateDropDownHtml(pbholidaystartMM, timeMinList,"--", null);

        Map<String,String> pbholidayendHH = IaisCommonUtils.genNewHashMap();
        pbholidayendHH.put("class", "onSitePbHolDayEndHH");
        pbholidayendHH.put("id", premName+"onSitePbHolDayEndHH0");
        pbholidayendHH.put("name", premName+"onSitePbHolDayEndHH0");
        pbholidayendHH.put("style", "display: none;");
        String holidayendHH = NewApplicationHelper.generateDropDownHtml(pbholidayendHH, timeHourList,"--", null);

        Map<String,String> pbholidayendMM = IaisCommonUtils.genNewHashMap();
        pbholidayendMM.put("class", "onSitePbHolDayEndMM");
        pbholidayendMM.put("id", premName+"onSitePbHolDayEndMM0");
        pbholidayendMM.put("name", premName+"onSitePbHolDayEndMM0");
        pbholidayendMM.put("style", "display: none;");
        String holidayendMM = NewApplicationHelper.generateDropDownHtml(pbholidayendMM, timeMinList,"--", null);


        //convyance operation time
        Map<String,String> conveyancestartHour = IaisCommonUtils.genNewHashMap();
        conveyancestartHour.put("class", "conveyanceStartHH");
        conveyancestartHour.put("id", "conveyanceStartHH");
        conveyancestartHour.put("name", "conveyanceStartHH");
        conveyancestartHour.put("style", "display: none;");
        String conveyancestartHH = NewApplicationHelper.generateDropDownHtml(conveyancestartHour, timeHourList,"--", null);

        Map<String,String> conveyancestartMin = IaisCommonUtils.genNewHashMap();
        conveyancestartMin.put("class", "conveyanceStartMM");
        conveyancestartMin.put("id", "conveyanceStartMM");
        conveyancestartMin.put("name", "conveyanceStartMM");
        conveyancestartMin.put("style", "display: none;");
        String conveyancestartMM = NewApplicationHelper.generateDropDownHtml(conveyancestartMin, timeMinList,"--", null);

        Map<String,String> conveyanceendHour = IaisCommonUtils.genNewHashMap();
        conveyanceendHour.put("class", "conveyanceEndHH");
        conveyanceendHour.put("id", "conveyanceEndHH");
        conveyanceendHour.put("name", "conveyanceEndHH");
        conveyanceendHour.put("style", "display: none;");
        String conveyanceendHH = NewApplicationHelper.generateDropDownHtml(conveyanceendHour, timeHourList,"--", null);

        Map<String,String> conveyanceendMin = IaisCommonUtils.genNewHashMap();
        conveyanceendMin.put("class", "conveyanceEndMM");
        conveyanceendMin.put("id", "conveyanceEndMM");
        conveyanceendMin.put("name", "conveyanceEndMM");
        conveyanceendMin.put("style", "display: none;");
        String conveyanceendMM = NewApplicationHelper.generateDropDownHtml(conveyanceendMin, timeMinList,"--", null);

        //convyance ph
        Map<String,String> convpublicHoliday = IaisCommonUtils.genNewHashMap();
        convpublicHoliday.put("class", "conveyancePubHoliday");
        convpublicHoliday.put("id", premName+"conveyancePubHoliday0");
        convpublicHoliday.put("name", premName+"conveyancePubHoliday0");
        convpublicHoliday.put("style", "display: none;");
        String convpublicHolidayDD = NewApplicationHelper.generateDropDownHtml(convpublicHoliday, publicHolidayList,"Please Select", null);

        Map<String,String> convpbholidaystartHH = IaisCommonUtils.genNewHashMap();
        convpbholidaystartHH.put("class", "conveyancePbHolDayStartHH");
        convpbholidaystartHH.put("id", premName+"conveyancePbHolDayStartHH0");
        convpbholidaystartHH.put("name", premName+"conveyancePbHolDayStartHH0");
        convpbholidaystartHH.put("style", "display: none;");
        String convholidaystartHH = NewApplicationHelper.generateDropDownHtml(convpbholidaystartHH, timeHourList,"--", null);

        Map<String,String> convpbholidaystartMM = IaisCommonUtils.genNewHashMap();
        convpbholidaystartMM.put("class", "conveyancePbHolDayStartMM");
        convpbholidaystartMM.put("id", premName+"conveyancePbHolDayStartMM0");
        convpbholidaystartMM.put("name", premName+"conveyancePbHolDayStartMM0");
        convpbholidaystartMM.put("style", "display: none;");
        String convholidaystartMM = NewApplicationHelper.generateDropDownHtml(convpbholidaystartMM, timeMinList,"--", null);

        Map<String,String> convpbholidayendHH = IaisCommonUtils.genNewHashMap();
        convpbholidayendHH.put("class", "conveyancePbHolDayEndHH");
        convpbholidayendHH.put("id", premName+"conveyancePbHolDayEndHH0");
        convpbholidayendHH.put("name", premName+"conveyancePbHolDayEndHH0");
        convpbholidayendHH.put("style", "display: none;");
        String convholidayendHH = NewApplicationHelper.generateDropDownHtml(convpbholidayendHH, timeHourList,"--", null);

        Map<String,String> convpbholidayendMM = IaisCommonUtils.genNewHashMap();
        convpbholidayendMM.put("class", "conveyancePbHolDayEndMM");
        convpbholidayendMM.put("id", premName+"conveyancePbHolDayEndMM0");
        convpbholidayendMM.put("name", premName+"conveyancePbHolDayEndMM0");
        convpbholidayendMM.put("style", "display: none;");
        String convholidayendMM = NewApplicationHelper.generateDropDownHtml(convpbholidayendMM, timeMinList,"--", null);


        //offSite operation time
        Map<String,String> offSiteStartHour = IaisCommonUtils.genNewHashMap();
        offSiteStartHour.put("class", "offSiteStartHH");
        offSiteStartHour.put("id", "offSiteStartHH");
        offSiteStartHour.put("name", "offSiteStartHH");
        offSiteStartHour.put("style", "display: none;");
        String offSiteStartHH = NewApplicationHelper.generateDropDownHtml(offSiteStartHour, timeHourList,"--", null);

        Map<String,String> offSiteStartMin = IaisCommonUtils.genNewHashMap();
        offSiteStartMin.put("class", "offSiteStartMM");
        offSiteStartMin.put("id", "offSiteStartMM");
        offSiteStartMin.put("name", "offSiteStartMM");
        offSiteStartMin.put("style", "display: none;");
        String offSiteStartMM = NewApplicationHelper.generateDropDownHtml(offSiteStartMin, timeMinList,"--", null);

        Map<String,String> offSiteEndHour = IaisCommonUtils.genNewHashMap();
        offSiteEndHour.put("class", "offSiteEndHH");
        offSiteEndHour.put("id", "offSiteEndHH");
        offSiteEndHour.put("name", "offSiteEndHH");
        offSiteEndHour.put("style", "display: none;");
        String offSiteEndHH = NewApplicationHelper.generateDropDownHtml(offSiteEndHour, timeHourList,"--", null);

        Map<String,String> offSiteEndMin = IaisCommonUtils.genNewHashMap();
        offSiteEndMin.put("class", "offSiteEndMM");
        offSiteEndMin.put("id", "offSiteEndMM");
        offSiteEndMin.put("name", "offSiteEndMM");
        offSiteEndMin.put("style", "display: none;");
        String offSiteEndMM = NewApplicationHelper.generateDropDownHtml(offSiteEndMin, timeMinList,"--", null);

        //offSite ph
        Map<String,String> offSitePublicHoliday = IaisCommonUtils.genNewHashMap();
        offSitePublicHoliday.put("class", "offSitePubHoliday");
        offSitePublicHoliday.put("id", premName+"offSitePubHoliday0");
        offSitePublicHoliday.put("name", premName+"offSitePubHoliday0");
        offSitePublicHoliday.put("style", "display: none;");
        String offSitePublicHolidayDD = NewApplicationHelper.generateDropDownHtml(offSitePublicHoliday, publicHolidayList,"Please Select", null);

        Map<String,String> offSitePhStartHH = IaisCommonUtils.genNewHashMap();
        offSitePhStartHH.put("class", "offSitePbHolDayStartHH");
        offSitePhStartHH.put("id", premName+"offSitePbHolDayStartHH0");
        offSitePhStartHH.put("name", premName+"offSitePbHolDayStartHH0");
        offSitePhStartHH.put("style", "display: none;");
        String offSitePhStartHHStr = NewApplicationHelper.generateDropDownHtml(offSitePhStartHH, timeHourList,"--", null);

        Map<String,String> offSitePhStartMM = IaisCommonUtils.genNewHashMap();
        offSitePhStartMM.put("class", "offSitePbHolDayStartMM");
        offSitePhStartMM.put("id", premName+"offSitePbHolDayStartMM0");
        offSitePhStartMM.put("name", premName+"offSitePbHolDayStartMM0");
        offSitePhStartMM.put("style", "display: none;");
        String offSitePhStartMMStr = NewApplicationHelper.generateDropDownHtml(offSitePhStartMM, timeMinList,"--", null);

        Map<String,String> offSitePhEndHH = IaisCommonUtils.genNewHashMap();
        offSitePhEndHH.put("class", "offSitePbHolDayEndHH");
        offSitePhEndHH.put("id", premName+"offSitePbHolDayEndHH0");
        offSitePhEndHH.put("name", premName+"offSitePbHolDayEndHH0");
        offSitePhEndHH.put("style", "display: none;");
        String offSitePhEndHHStr = NewApplicationHelper.generateDropDownHtml(offSitePhEndHH, timeHourList,"--", null);

        Map<String,String> offSitePhEndMM = IaisCommonUtils.genNewHashMap();
        offSitePhEndMM.put("class", "offSitePbHolDayEndMM");
        offSitePhEndMM.put("id", premName+"offSitePbHolDayEndMM0");
        offSitePhEndMM.put("name", premName+"offSitePbHolDayEndMM0");
        offSitePhEndMM.put("style", "display: none;");
        String offSitePhEndMMStr = NewApplicationHelper.generateDropDownHtml(offSitePhEndMM, timeMinList,"--", null);

        sql = sql.replace("(0)", currentLength);
        sql = sql.replace("(1)", premTypeBuffer.toString());
        sql = sql.replace("(2)", premOnSiteSelectStr);
        sql = sql.replace("(3)", premConvSelectStr);
        sql = sql.replace("(PREMOFFSITESELECT)", premOffSiteSelectStr);
        sql = sql.replace("(4)", addrTypeSelectStr);
        sql = sql.replace("(5)", conAddrTypeSelectStr);
        sql = sql.replace("(OFFSITEADDRTYPESELECT)", offSiteAddrTypeSelectStr);
        sql = sql.replace("(ONSITESTARHH)", onsitestarHH);
        sql = sql.replace("(ONSITESTARMM)", onsitestarMM);
        sql = sql.replace("(ONSITEENDHH)", onsiteEndHH);
        sql = sql.replace("(ONSITEENDMM)", onsiteEndMM);
        sql = sql.replace("(CONVEYANCESTARTHH)", conveyancestartHH);
        sql = sql.replace("(CONVEYANCESTARTMM)", conveyancestartMM);
        sql = sql.replace("(CONVEYANCEENDHH)", conveyanceendHH);
        sql = sql.replace("(CONVEYANCEENDMM)", conveyanceendMM);
        sql = sql.replace("(OFFSITESTARTHH)", offSiteStartHH);
        sql = sql.replace("(OFFSITESTARTMM)", offSiteStartMM);
        sql = sql.replace("(OFFSITEENDHH)", offSiteEndHH);
        sql = sql.replace("(OFFSITEENDMM)", offSiteEndMM);

        //ph
        sql = sql.replace("(PUBLICHOLIDAYDD)", publicHolidayDD);
        sql = sql.replace("(PBHOLDAYSTARTHH)", holidaystartHH);
        sql = sql.replace("(PBHOLDAYSTARTMM)", holidaystartMM);
        sql = sql.replace("(PBHOLDAYENDHH)", holidayendHH);
        sql = sql.replace("(PBHOLDAYENDMM)", holidayendMM);
        sql = sql.replace("(CONVPUBLICHOLIDAYDD)", convpublicHolidayDD);
        sql = sql.replace("(CONVEYANCEPBHOLDAYSTARTHH)", convholidaystartHH);
        sql = sql.replace("(CONVEYANCEPBHOLDAYSTARTMM)", convholidaystartMM);
        sql = sql.replace("(CONVEYANCEPBHOLDAYENDHH)", convholidayendHH);
        sql = sql.replace("(CONVEYANCEPBHOLDAYENDMM)", convholidayendMM);
        sql = sql.replace("(OFFSITEPUBLICHOLIDAYDD)", offSitePublicHolidayDD);
        sql = sql.replace("(OFFSITEPBHOLDAYSTARTHH)", offSitePhStartHHStr);
        sql = sql.replace("(OFFSITEPBHOLDAYSTARTMM)", offSitePhStartMMStr);
        sql = sql.replace("(OFFSITEPBHOLDAYENDHH)", offSitePhEndHHStr);
        sql = sql.replace("(OFFSITEPBHOLDAYENDMM)", offSitePhEndMMStr);

        //premises header val
        Integer premHeaderVal = Integer.parseInt(currentLength)+1;
        sql = sql.replace("(6)",String.valueOf(premHeaderVal));

        log.debug(StringUtil.changeForLog("the add premises html end ...."));
        return sql;
    }


    /**
     * @param
     * @description: ajax
     * @author: zixia
     */
    @RequestMapping(value = "/file-repo", method = RequestMethod.GET)
    public @ResponseBody void fileDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("file-repo start ...."));
        String fileRepoName = ParamUtil.getRequestString(request, "fileRepoName");
        String maskFileRepoIdName = ParamUtil.getRequestString(request, "filerepo");
        String fileRepoId = ParamUtil.getMaskedString(request, maskFileRepoIdName);
        if(StringUtil.isEmpty(fileRepoId)){
            log.debug(StringUtil.changeForLog("file-repo id is empty"));
            return;
        }
        byte[] fileData =serviceConfigService.downloadFile(fileRepoId);
        response.addHeader("Content-Disposition", "attachment;filename=" + fileRepoName);
        response.addHeader("Content-Length", "" + fileData.length);
        response.setContentType("application/x-octet-stream");
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(fileData);
        ops.close();
        ops.flush();
        log.debug(StringUtil.changeForLog("file-repo end ...."));
    }


    @RequestMapping(value = "/file-repo-popup", method = RequestMethod.GET)
    public @ResponseBody void filePopUpDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("filePopUpDownload start ...."));
        String fileRepoName = ParamUtil.getRequestString(request, "fileRepoName");
        String maskFileRepoIdName = ParamUtil.getRequestString(request, "filerepo");
        String fileRepoId = ParamUtil.getMaskedString(request, maskFileRepoIdName);
        if (StringUtil.isEmpty(fileRepoId)) {
            log.debug(StringUtil.changeForLog("file-repo id is empty"));
            return;
        }
        byte[] fileData = serviceConfigService.downloadFile(fileRepoId);
        response.setContentType("application/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileRepoName);
        response.addHeader("Content-Length", "" + fileData.length);
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(fileData);
        ops.close();
        ops.flush();
        log.debug(StringUtil.changeForLog("filePopUpDownload end ...."));
    }


    @PostMapping(value = "/governance-officer-html")
    public @ResponseBody Map<String,String> genGovernanceOfficerHtmlList(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("gen governance officer html start ...."));
        Map<String,String> resp = IaisCommonUtils.genNewHashMap();
        int canAddNumber = ParamUtil.getInt(request,"AddNumber");
        String hasNumber = ParamUtil.getRequestString(request,"HasNumber");
        String errMsg = "You are allowed to add up till only "+hasNumber+" CGO";
        if(canAddNumber>0){
            String sql = SqlMap.INSTANCE.getSql("governanceOfficer", "generateGovernanceOfficerHtml").getSqlStr();
            //assign cgo select
            List<SelectOption> cgoSelectList = NewApplicationHelper.genAssignPersonSel(request,true);
            Map<String,String> cgoSelectAttr = IaisCommonUtils.genNewHashMap();
            cgoSelectAttr.put("class", "assignSel");
            cgoSelectAttr.put("name", "assignSelect");
            cgoSelectAttr.put("style", "display: none;");
            String cgoSelectStr = NewApplicationHelper.generateDropDownHtml(cgoSelectAttr, cgoSelectList, null, null);

            //salutation
            List<SelectOption> salutationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
            Map<String,String> salutationAttr = IaisCommonUtils.genNewHashMap();
            salutationAttr.put("class", "salutationSel");
            salutationAttr.put("name", "salutation");
            salutationAttr.put("style", "display: none;");
            String salutationSelectStr = NewApplicationHelper.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION, null);

            //ID Type
            List<SelectOption> idTypeList = NewApplicationHelper.getIdTypeSelOp();
            Map<String,String>  idTypeAttr = IaisCommonUtils.genNewHashMap();
            idTypeAttr.put("class", "idTypeSel");
            idTypeAttr.put("name", "idType");
            idTypeAttr.put("style", "display: none;");
            String idTypeSelectStr = NewApplicationHelper.generateDropDownHtml(idTypeAttr, idTypeList, null, null);

            //Designation
            List<SelectOption> designationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DESIGNATION);
            Map<String,String> designationAttr = IaisCommonUtils.genNewHashMap();
            designationAttr.put("class", "designationSel");
            designationAttr.put("name", "designation");
            designationAttr.put("style", "display: none;");
            String designationSelectStr = NewApplicationHelper.generateDropDownHtml(designationAttr, designationList, NewApplicationDelegator.FIRESTOPTION, null);

            //Professional Regn Type
            List<SelectOption> proRegnTypeList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_PROFESSIONAL_TYPE);

            Map<String,String> proRegnTypeAttr = IaisCommonUtils.genNewHashMap();
            proRegnTypeAttr.put("class", "professionTypeSel");
            proRegnTypeAttr.put("name", "professionType");
            proRegnTypeAttr.put("style", "display: none;");
            String proRegnTypeSelectStr = NewApplicationHelper.generateDropDownHtml(proRegnTypeAttr, proRegnTypeList, NewApplicationDelegator.FIRESTOPTION, null);

            //Specialty
            List<SelectOption> specialtyList = (List<SelectOption>) ParamUtil.getSessionAttr(request, "SpecialtySelectList");

            Map<String,String> specialtyAttr = IaisCommonUtils.genNewHashMap();
            specialtyAttr.put("name", "specialty");
            specialtyAttr.put("class", "specialty");
            specialtyAttr.put("style", "display: none;");
            String specialtySelectStr = NewApplicationHelper.generateDropDownHtml(specialtyAttr, specialtyList, null, null);

            sql = sql.replace("(1)", cgoSelectStr);
            sql = sql.replace("(2)", salutationSelectStr);
            sql = sql.replace("(3)", idTypeSelectStr);
            sql = sql.replace("(4)", designationSelectStr);
            sql = sql.replace("(5)", proRegnTypeSelectStr);
            sql = sql.replace("(6)", specialtySelectStr);

            log.debug(StringUtil.changeForLog("gen governance officer html end ...."));
            resp.put("sucInfo",sql);
            resp.put("res","success");
        }else{
            resp.put("errInfo",errMsg);
        }
        return resp;

    }


    @GetMapping(value = "/public-holiday-html")
    public @ResponseBody String genPublicHolidayHtml(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("the genPublicHolidayHtml start ...."));
        String type = ParamUtil.getString(request,"type");
        String premVal = ParamUtil.getString(request,"premVal");
        String phLength = ParamUtil.getString(request,"phLength");
        List<SelectOption> timeHourList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i< 24;i++){
            timeHourList.add(new SelectOption(String.valueOf(i), i<10?"0"+String.valueOf(i):String.valueOf(i)));
        }
        List<SelectOption> timeMinList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i< 60;i++){
            timeMinList.add(new SelectOption(String.valueOf(i), i<10?"0"+String.valueOf(i):String.valueOf(i)));
        }

        String sql = SqlMap.INSTANCE.getSql("premises", "premises-ph").getSqlStr();

        List<SelectOption> publicHolidayList = IaisCommonUtils.genNewArrayList();
        List<PublicHolidayDto> publicHolidayDtoList = IaisCommonUtils.genNewArrayList();

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        try {
            publicHolidayDtoList = feEicGatewayClient.getpublicHoliday(signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        publicHolidayDtoList.stream().forEach(pb -> {
            publicHolidayList.add(new SelectOption(Formatter.formatDate(pb.getFromDate()),pb.getDescription()));
        });

        Map<String,String> phSelectAttr = IaisCommonUtils.genNewHashMap();

        phSelectAttr.put("class",type+"PubHoliday");
        phSelectAttr.put("id", premVal+"PubHoliday"+phLength);
        phSelectAttr.put("name", premVal+"PubHoliday"+phLength);
        phSelectAttr.put("style", "display: none;");
        String phSelectHtml = NewApplicationHelper.generateDropDownHtml(phSelectAttr, publicHolidayList,"Please Select", null);

        Map<String,String> phStartHourAttr = IaisCommonUtils.genNewHashMap();
        phStartHourAttr.put("class", type+"PbHolDayStartHH");
        phStartHourAttr.put("id", premVal+"PbHolDayStartHH"+phLength);
        phStartHourAttr.put("name", premVal+"PbHolDayStartHH"+phLength);
        phStartHourAttr.put("style", "display: none;");
        String phStartHourHtml = NewApplicationHelper.generateDropDownHtml(phStartHourAttr, timeHourList,"--", null);

        Map<String,String> phStartMinAttr = IaisCommonUtils.genNewHashMap();
        phStartMinAttr.put("class",  type+"PbHolDayStartMM");
        phStartMinAttr.put("id",  premVal+"PbHolDayStartMM"+phLength);
        phStartMinAttr.put("name", premVal+"PbHolDayStartMM"+phLength);
        phStartMinAttr.put("style", "display: none;");
        String phStartMinHtml = NewApplicationHelper.generateDropDownHtml(phStartMinAttr, timeMinList,"--", null);

        Map<String,String> phEndHourAttr = IaisCommonUtils.genNewHashMap();
        phEndHourAttr.put("class", type+"PbHolDayEndHH");
        phEndHourAttr.put("id", premVal+"PbHolDayEndHH"+phLength);
        phEndHourAttr.put("name", premVal+"PbHolDayEndHH"+phLength);
        phEndHourAttr.put("style", "display: none;");
        String phEndHourHtml = NewApplicationHelper.generateDropDownHtml(phEndHourAttr, timeHourList,"--", null);

        Map<String,String> phEndMinAttr = IaisCommonUtils.genNewHashMap();
        phEndMinAttr.put("class",  type+"PbHolDayEndMM");
        phEndMinAttr.put("id",  premVal+"PbHolDayEndMM"+phLength);
        phEndMinAttr.put("name", premVal+"PbHolDayEndMM"+phLength);
        phEndMinAttr.put("style", "display: none;");
        String phEndMinHtml = NewApplicationHelper.generateDropDownHtml(phEndMinAttr, timeMinList,"--", null);


        sql = sql.replace("(phSelect)", phSelectHtml);
        sql = sql.replace("(phStartHour)", phStartHourHtml);
        sql = sql.replace("(phStartMin)", phStartMinHtml);
        sql = sql.replace("(phEndHour)", phEndHourHtml);
        sql = sql.replace("(phEndMin)", phEndMinHtml);

        log.debug(StringUtil.changeForLog("the genPublicHolidayHtml start ...."));
        return sql;
    }
    /**
     * @param
     * @description: ajax
     * @author: zixia
     */
    @RequestMapping(value = "/psn-info", method = RequestMethod.GET)
    public @ResponseBody
    AppSvcCgoDto getPsnInfoByIdNo (HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("getPsnInfoByIdNo start ...."));
        String idNo = ParamUtil.getRequestString(request, "idNo");
        AppSvcCgoDto appSvcCgoDto = null;
        if(StringUtil.isEmpty(idNo)){
            return appSvcCgoDto;
        }
        AppSubmissionDto appSubmissionDto = ClinicalLaboratoryDelegator.getAppSubmissionDto(request);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtoList){
                List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
                if(!IaisCommonUtils.isEmpty(appSvcCgoDtoList)){
                    appSvcCgoDto = isExistIdNo(appSvcCgoDtoList, idNo);
                    if(appSvcCgoDto != null){
                        break;
                    }
                }
            }
        }
        log.debug(StringUtil.changeForLog("getPsnInfoByIdNo end ...."));
        return  appSvcCgoDto;
    }

    @PostMapping(value = "/nuclear-medicine-imaging-html")
    public @ResponseBody Map<String,String> addNuclearMedicineImagingHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add NuclearMedicineImaging html start ...."));
        int spMaxNumber = 0;
        Map<String,String> resp = IaisCommonUtils.genNewHashMap();
        int hasNumber = ParamUtil.getInt(request,"HasNumber");
        String errMsg = "You are allowed to add up till only "+hasNumber+" SP";
        Map<String,List<HcsaSvcPersonnelDto>> svcConfigInfo = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(request,SERVICEALLPSNCONFIGMAP);

        String svcId = (String) ParamUtil.getSessionAttr(request,NewApplicationDelegator.CURRENTSERVICEID);
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = svcConfigInfo.get(svcId);
        for(HcsaSvcPersonnelDto hcsaSvcPersonnelDto:hcsaSvcPersonnelDtos){
            if ("SVCPSN".equalsIgnoreCase(hcsaSvcPersonnelDto.getPsnType())){
                spMaxNumber = hcsaSvcPersonnelDto.getMaximumCount();
                break;
            }
        }

        if (spMaxNumber - hasNumber > 0){
            String sql = SqlMap.INSTANCE.getSql("servicePersonnel", "NuclearMedicineImaging").getSqlStr();
            String currentSvcCod = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSVCCODE);
            List<SelectOption> personnel = ClinicalLaboratoryDelegator.genPersonnelTypeSel(currentSvcCod);
            Map<String,String> personnelAttr = IaisCommonUtils.genNewHashMap();
            personnelAttr.put("name", "personnelSel");
            personnelAttr.put("class", "personnelSel");
            personnelAttr.put("style", "display: none;");
            String personnelSelectStr = NewApplicationHelper.generateDropDownHtml(personnelAttr, personnel, NewApplicationDelegator.FIRESTOPTION, null);

            List<SelectOption> designation = (List) ParamUtil.getSessionAttr(request, "NuclearMedicineImagingDesignation");
            Map<String,String> designationAttr = IaisCommonUtils.genNewHashMap();
            designationAttr.put("name", "designation");
            designationAttr.put("style", "display: none;");
            String designationSelectStr = NewApplicationHelper.generateDropDownHtml(designationAttr, designation, NewApplicationDelegator.FIRESTOPTION, null);
            sql = sql.replace("(0)", String.valueOf(hasNumber+1));
            sql = sql.replace("(1)", personnelSelectStr);
            sql = sql.replace("(2)", designationSelectStr);

            log.debug(StringUtil.changeForLog("the add NuclearMedicineImaging html end ...."));
            resp.put("sucInfo",sql);
            resp.put("res","success");
        }else{
            resp.put("errInfo",errMsg);
        }
        return resp;
    }



    @PostMapping(value = "/principal-officer-html")
    public @ResponseBody Map<String,String> addPrincipalOfficeHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add addPrincipalOfficeHtml html start ...."));
        int poMmaximumCount = 0;
        Map<String,String> resp = IaisCommonUtils.genNewHashMap();
        String svcId = (String) ParamUtil.getSessionAttr(request, NewApplicationDelegator.CURRENTSERVICEID);
        String sql = SqlMap.INSTANCE.getSql("principalOfficers", "generatePrincipalOfficersHtml").getSqlStr();
        int hasNumber = ParamUtil.getInt(request, "HasNumber");
        Map<String,List<HcsaSvcPersonnelDto>> svcConfigInfo = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(request,SERVICEALLPSNCONFIGMAP);
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = svcConfigInfo.get(svcId);
        for(HcsaSvcPersonnelDto hcsaSvcPersonnelDto:hcsaSvcPersonnelDtos){
            if ("PO".equalsIgnoreCase(hcsaSvcPersonnelDto.getPsnType())){
                poMmaximumCount = hcsaSvcPersonnelDto.getMaximumCount();
                break;
            }
        }
        String errMsg = "You are allowed to add up till only "+hasNumber+" PO";
        if (poMmaximumCount - hasNumber > 0){
            //assign select
            List<SelectOption> assignPrincipalOfficerSel = NewApplicationHelper.genAssignPersonSel(request, false);
            Map<String,String> assignPrincipalOfficerAttr = IaisCommonUtils.genNewHashMap();
            assignPrincipalOfficerAttr.put("name", "poSelect");
            assignPrincipalOfficerAttr.put("class", "poSelect");
            assignPrincipalOfficerAttr.put("style", "display: none;");
            String principalOfficerSelStr = NewApplicationHelper.generateDropDownHtml(assignPrincipalOfficerAttr, assignPrincipalOfficerSel, NewApplicationDelegator.FIRESTOPTION, null);

            //salutation
            List<SelectOption> salutationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
            Map<String,String> salutationAttr = IaisCommonUtils.genNewHashMap();
            salutationAttr.put("class", "salutation");
            salutationAttr.put("name", "salutation");
            salutationAttr.put("style", "display: none;");
            String salutationSelectStr = NewApplicationHelper.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION, null);

            //ID Type
            List<SelectOption> idTypeList = NewApplicationHelper.getIdTypeSelOp();
            Map<String,String>  idTypeAttr = IaisCommonUtils.genNewHashMap();
            idTypeAttr.put("class", "idType");
            idTypeAttr.put("name", "idType");
            idTypeAttr.put("style", "display: none;");
            String idTypeSelectStr = NewApplicationHelper.generateDropDownHtml(idTypeAttr, idTypeList, null, null);

            //Designation
            List<SelectOption> designationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DESIGNATION);
            Map<String,String> designationAttr = IaisCommonUtils.genNewHashMap();
            designationAttr.put("class", "designation");
            designationAttr.put("name", "designation");
            designationAttr.put("style", "display: none;");
            String designationSelectStr = NewApplicationHelper.generateDropDownHtml(designationAttr, designationList, NewApplicationDelegator.FIRESTOPTION, null);

            sql = sql.replace("(1)", principalOfficerSelStr);
            sql = sql.replace("(2)", salutationSelectStr);
            sql = sql.replace("(3)", idTypeSelectStr);
            sql = sql.replace("(4)", designationSelectStr);
            sql = sql.replace("(poOfficerCount)",String.valueOf(hasNumber+1));
            resp.put("sucInfo",sql);
            resp.put("res","success");
            log.debug(StringUtil.changeForLog("the add addPrincipalOfficeHtml html end ...."));
        }else{
            resp.put("errInfo",errMsg);
        }
        return resp;
    }


    @PostMapping(value = "/deputy-principal-officer-html")
    public @ResponseBody Map<String,String> addDeputyPrincipalOfficeHtml(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the add addDeputyPrincipalOfficeHtml html start ...."));
        String sql = SqlMap.INSTANCE.getSql("principalOfficers", "generateDeputyPrincipalOfficersHtml").getSqlStr();
        int dpoMmaximumCount = 0;
        int hasNumber = ParamUtil.getInt(request, "HasNumber");
        Map<String,String> resp = IaisCommonUtils.genNewHashMap();
        Map<String,List<HcsaSvcPersonnelDto>> svcConfigInfo = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(request,SERVICEALLPSNCONFIGMAP);

        String svcId = (String) ParamUtil.getSessionAttr(request,NewApplicationDelegator.CURRENTSERVICEID);
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = svcConfigInfo.get(svcId);
        for(HcsaSvcPersonnelDto hcsaSvcPersonnelDto:hcsaSvcPersonnelDtos){
            if ("DPO".equalsIgnoreCase(hcsaSvcPersonnelDto.getPsnType())){
                dpoMmaximumCount = hcsaSvcPersonnelDto.getMaximumCount();
                break;
            }
        }

        String errMsg = "You are allowed to add up till only "+dpoMmaximumCount+" DPO";
         if (dpoMmaximumCount - hasNumber > 0){
            //assign select
            List<SelectOption> assignPrincipalOfficerSel = NewApplicationHelper.genAssignPersonSel(request, false);
            Map<String,String> assignPrincipalOfficerAttr = IaisCommonUtils.genNewHashMap();
            assignPrincipalOfficerAttr.put("name", "deputyPoSelect");
            assignPrincipalOfficerAttr.put("class", "deputyPoSelect");
            assignPrincipalOfficerAttr.put("style", "display: none;");
            String principalOfficerSelStr = NewApplicationHelper.generateDropDownHtml(assignPrincipalOfficerAttr, assignPrincipalOfficerSel, NewApplicationDelegator.FIRESTOPTION, null);
            //salutation
            List<SelectOption> salutationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
            Map<String,String> salutationAttr = IaisCommonUtils.genNewHashMap();
            salutationAttr.put("class", "deputySalutation");
            salutationAttr.put("name", "deputySalutation");
            salutationAttr.put("style", "display: none;");
            String salutationSelectStr = NewApplicationHelper.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION, null);
            //ID Type
            List<SelectOption> idTypeList = NewApplicationHelper.getIdTypeSelOp();
            Map<String,String>  idTypeAttr = IaisCommonUtils.genNewHashMap();
            idTypeAttr.put("class", "deputyIdType");
            idTypeAttr.put("name", "deputyIdType");
            idTypeAttr.put("style", "display: none;");
            String idTypeSelectStr = NewApplicationHelper.generateDropDownHtml(idTypeAttr, idTypeList, null, null);

            //Designation
            List<SelectOption> designationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DESIGNATION);
            Map<String,String> designationAttr = IaisCommonUtils.genNewHashMap();
            designationAttr.put("class", "deputyDesignation");
            designationAttr.put("name", "deputyDesignation");
            designationAttr.put("style", "display: none;");
            String designationSelectStr = NewApplicationHelper.generateDropDownHtml(designationAttr, designationList, NewApplicationDelegator.FIRESTOPTION, null);

            sql = sql.replace("(1)", salutationSelectStr);
            sql = sql.replace("(2)", idTypeSelectStr);
            sql = sql.replace("(3)", designationSelectStr);
            sql = sql.replace("(4)", principalOfficerSelStr);
            sql = sql.replace("(dpoOfficerCount)", String.valueOf(hasNumber+1));
            log.debug(StringUtil.changeForLog("the add addDeputyPrincipalOfficeHtml html end ...."));
            resp.put("sucInfo",sql);
            resp.put("res","success");
        }else{
            resp.put("errInfo",errMsg);
        }
        return resp;
    }


    @RequestMapping(value = "/lic-premises", method = RequestMethod.GET)
    public @ResponseBody AppGrpPremisesDto getLicPremisesInfo(HttpServletRequest request) {
        log.debug(StringUtil.changeForLog("the getLicPremisesInfo start ...."));
        String premIndexNo = ParamUtil.getString(request,"premIndexNo");
        if(StringUtil.isEmpty(premIndexNo)){
            return null;
        }
        Map<String,AppGrpPremisesDto> licAppGrpPremisesDtoMap = (Map<String, AppGrpPremisesDto>) ParamUtil.getSessionAttr(request,NewApplicationDelegator.LICAPPGRPPREMISESDTOMAP);
        AppGrpPremisesDto appGrpPremisesDto = licAppGrpPremisesDtoMap.get(premIndexNo);
        appGrpPremisesDto = NewApplicationHelper.setWrkTime(appGrpPremisesDto);
        //set dayName
        if(appGrpPremisesDto != null){
            List<AppPremPhOpenPeriodDto> appPremPhOpenPeriodDtos = appGrpPremisesDto.getAppPremPhOpenPeriodList();
            if(!IaisCommonUtils.isEmpty(appPremPhOpenPeriodDtos)){
                List<SelectOption> publicHolidayList = serviceConfigService.getPubHolidaySelect();
                for(AppPremPhOpenPeriodDto appPremPhOpenPeriodDto:appPremPhOpenPeriodDtos){
                    String dayName = appPremPhOpenPeriodDto.getDayName();
                    String phDateStr = appPremPhOpenPeriodDto.getPhDateStr();
                    if(StringUtil.isEmpty(dayName) && !StringUtil.isEmpty(phDateStr)){
                        dayName = NewApplicationHelper.getPhName(publicHolidayList,phDateStr);
                        appPremPhOpenPeriodDto.setDayName(dayName);
                    }
                }
            }
        }

        licAppGrpPremisesDtoMap.put(premIndexNo,appGrpPremisesDto);
        ParamUtil.setSessionAttr(request, NewApplicationDelegator.LICAPPGRPPREMISESDTOMAP, (Serializable) licAppGrpPremisesDtoMap);
        log.debug(StringUtil.changeForLog("the getLicPremisesInfo end ...."));
        return appGrpPremisesDto;
    }

    /**
     *
     * @param request
     * @return
     */
    @GetMapping(value = "/psn-select-info")
    public @ResponseBody AppSvcPrincipalOfficersDto getPsnSelectInfo(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("the getNewPsnInfo start ...."));
        String idType = ParamUtil.getString(request,"idType");
        String idNo = ParamUtil.getString(request,"idNo");
        String psnType = ParamUtil.getString(request,"psnType");
        if(StringUtil.isEmpty(idNo) || StringUtil.isEmpty(idType)){
            return null;
        }
        String psnKey = idType+","+idNo;
        Map<String,AppSvcPrincipalOfficersDto> psnMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.PERSONSELECTMAP);
        AppSvcPrincipalOfficersDto psn = psnMap.get(psnKey);
        if(psn == null){
            log.info(StringUtil.changeForLog("can not get data from PersonSelectMap ..."));
            return new AppSvcPrincipalOfficersDto();
        }
        String currentSvcCode = (String) ParamUtil.getSessionAttr(request,NewApplicationDelegator.CURRENTSVCCODE);
        if(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)){
            List<SelectOption> specialityOpts = NewApplicationHelper.genSpecialtySelectList(currentSvcCode,false);
            List<SelectOption> selectOptionList = psn.getSpcOptList();
            if(!IaisCommonUtils.isEmpty(selectOptionList)){
                for(SelectOption sp:selectOptionList){
                    if(!specialityOpts.contains(sp) && !sp.getValue().equals("other")){
                        specialityOpts.add(sp);
                    }
                }
            }
            //set other
            specialityOpts.add(new SelectOption("other", "Others"));
            psn.setSpcOptList(specialityOpts);
            Map<String,String> specialtyAttr = IaisCommonUtils.genNewHashMap();
            specialtyAttr.put("name", "specialty");
            specialtyAttr.put("class", "specialty");
            specialtyAttr.put("style", "display: none;");
            String specialityHtml = NewApplicationHelper.generateDropDownHtml(specialtyAttr, specialityOpts, null, psn.getSpeciality());
            psn.setSpecialityHtml(specialityHtml);
        }
        log.debug(StringUtil.changeForLog("the getNewPsnInfo end ...."));
        return psn;
    }


    @PostMapping(value = "/med-alert-person-html")
    public @ResponseBody Map<String,String> genMedAlertPersonHtml(HttpServletRequest request){
        log.debug(StringUtil.changeForLog("the genMedAlertPersonHtml start ...."));
        String sql = SqlMap.INSTANCE.getSql("medAlertPerson", "generateMedAlertPersonHtml").getSqlStr();
        int mapMaximumCount = 0;
        int hasNumber = ParamUtil.getInt(request, "HasNumber");
        Map<String,String> resp = IaisCommonUtils.genNewHashMap();
        Map<String,List<HcsaSvcPersonnelDto>> svcConfigInfo = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(request,SERVICEALLPSNCONFIGMAP);
        for (Map.Entry<String, List<HcsaSvcPersonnelDto>> stringListEntry : svcConfigInfo.entrySet()){
            List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtoList = stringListEntry.getValue();
            for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto:hcsaSvcPersonnelDtoList) {
                if (ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equalsIgnoreCase(hcsaSvcPersonnelDto.getPsnType())){
                    mapMaximumCount = hcsaSvcPersonnelDto.getMaximumCount();
                    break;
                }
            }
        }
        String errMsg = "You are allowed to add up till only "+mapMaximumCount+" MedAlert Person";
        //mapMaximumCount = 4;
        if (mapMaximumCount - hasNumber > 0){
            //assign select
            List<SelectOption> assignPrincipalOfficerSel = ClinicalLaboratoryDelegator.getAssignMedAlertSel(true);
            Map<String,String> assignPrincipalOfficerAttr = IaisCommonUtils.genNewHashMap();
            assignPrincipalOfficerAttr.put("name", "assignSel");
            assignPrincipalOfficerAttr.put("class", "assignSel");
            assignPrincipalOfficerAttr.put("style", "display: none;");
            String principalOfficerSelStr = NewApplicationHelper.generateDropDownHtml(assignPrincipalOfficerAttr, assignPrincipalOfficerSel, null, null);
            //salutation
            List<SelectOption> salutationList= MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_SALUTATION);
            Map<String,String> salutationAttr = IaisCommonUtils.genNewHashMap();
            salutationAttr.put("class", "salutation");
            salutationAttr.put("name", "salutation");
            salutationAttr.put("style", "display: none;");
            String salutationSelectStr = NewApplicationHelper.generateDropDownHtml(salutationAttr, salutationList, NewApplicationDelegator.FIRESTOPTION, null);
            //ID Type
            List<SelectOption> idTypeList = NewApplicationHelper.getIdTypeSelOp();
            Map<String,String>  idTypeAttr = IaisCommonUtils.genNewHashMap();
            idTypeAttr.put("class", "idType");
            idTypeAttr.put("name", "idType");
            idTypeAttr.put("style", "display: none;");
            String idTypeSelectStr = NewApplicationHelper.generateDropDownHtml(idTypeAttr, idTypeList, null, null);
            //pre mode
            List<SelectOption> medAlertSelectList = ClinicalLaboratoryDelegator.getMedAlertSelectList();
            Map<String,String>   medAlertAttr = IaisCommonUtils.genNewHashMap();
            medAlertAttr.put("class", "preferredMode");
            medAlertAttr.put("name", "preferredMode");
            medAlertAttr.put("style", "display: none;");
            String medAlertSelectStr = NewApplicationHelper.generateDropDownHtml(medAlertAttr, medAlertSelectList, null, null);

            sql = sql.replace("(0)",String.valueOf(hasNumber));
            sql = sql.replace("(1)", principalOfficerSelStr);
            sql = sql.replace("(2)", salutationSelectStr);
            sql = sql.replace("(3)", idTypeSelectStr);
            sql = sql.replace("(4)", medAlertSelectStr);
            resp.put("sucInfo",sql);
            resp.put("res","success");
        }else{
            resp.put("errInfo",errMsg);
        }

        log.debug(StringUtil.changeForLog("the genMedAlertPersonHtml end ...."));
        return resp;
    }



    //=============================================================================
    //private method
    //=============================================================================
    private AppSvcCgoDto isExistIdNo(List<AppSvcCgoDto> appSvcCgoDtoList, String idNo){
        for (AppSvcCgoDto appSvcCgoDto:appSvcCgoDtoList){
            if(idNo.equals(appSvcCgoDto.getIdNo())){
                log.info(StringUtil.changeForLog("had matching dto"));
                return appSvcCgoDto;
            }
        }
        return  null;
    }
}
