package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.giro.GiroDeductionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.GiroDeductionBeService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.impl.ConfigServiceImpl;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Process: MohBeGiroDeduction
 *
 * @author Shicheng
 * @date 2020/10/19 10:31
 **/
@Delegator(value = "giroDeductionBeDelegator")
@Slf4j
public class GiroDeductionBeDelegator {
    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(GiroDeductionDto.class)
            .searchAttr("giroDedSearchParam")
            .resultAttr("giroDedSearchResult")
            .sortField("APP_GROUP_NO").build();
    @Autowired
    private GiroDeductionBeService giroDeductionBeService;
    @Autowired
    private ApplicationClient applicationClient;
    private final static String CSV="csv";

    protected static final String [] STATUS={"PMT01","PMT03","PMT09"};
/*
    protected static final String [] PAYMENT_DEC={MasterCodeUtil.getCodeDesc("PMT01"),MasterCodeUtil.getCodeDesc("PMT03"),MasterCodeUtil.getCodeDesc("PMT09")};
*/
    @Autowired
    private GiroDeductionBeDelegator(GiroDeductionBeService giroDeductionBeService){
        this.giroDeductionBeService = giroDeductionBeService;
    }
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;
    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;
    @Autowired
    ApplicationService applicationService;

    private final static String[] HEADERS = {"S/N","HCI Name", "Application No.","Transaction Reference No.","Bank Account No.","Payment Status","Payment Amount"};
    /**
     * StartStep: beGiroDeductionStart
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionStart(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionStart start ...."));
        ParamUtil.setSessionAttr(bpc.request,"saveRetriggerOK",null);
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_LOAD_LEVELING, AuditTrailConsts.MODULE_GIRO_DEDUCTION);
        filterParameter.setPageSize(SystemParamUtil.getDefaultPageSize());
        filterParameter.setPageNo(1);
        removeSession(bpc.request);
    }

    private void removeSession(HttpServletRequest request){
        request.getSession().removeAttribute("giroDedSearchResult");
        request.getSession().removeAttribute("giroDedSearchParam");
    }
    /**
     * StartStep: beGiroDeductionInit
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionInit(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionInit start ...."));
    }

    /**
     * StartStep: beGiroDeductionPre
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionPre(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionPre start ...."));
        //init loading request ==null
        HttpServletRequest request = determineType(bpc.request);
        SearchParam searchParam = SearchResultHelper.getSearchParam(request, filterParameter, true);
        String transactionId = request.getParameter("txnRefNo");
        String bankAccountNo = request.getParameter("bankAccountNo");
        String group_no = request.getParameter("applicationNo");
        String paymentRefNo = request.getParameter("paymentRefNo");
        String paymentAmount = request.getParameter("paymentAmount");
        String paymentDescription = request.getParameter("paymentDescription");
        String hci_name = request.getParameter("hci_name");
        if(!StringUtil.isEmpty(group_no)){
            searchParam.addFilter("groupNo",group_no,true);
        }
        if(!StringUtil.isEmpty(paymentDescription)){
            searchParam.addFilter("desc",paymentDescription,true);
        }
        if(!StringUtil.isEmpty(paymentAmount)){
            searchParam.addFilter("amount",paymentAmount,true);
        }
        if(!StringUtil.isEmpty(bankAccountNo)){
            searchParam.addFilter("acctNo",bankAccountNo,true);
        }
        if(!StringUtil.isEmpty(hci_name)){
            searchParam.addFilter("hciName",hci_name,true);
        }
        if(!StringUtil.isEmpty(paymentRefNo)){
            searchParam.addFilter("refNo",paymentRefNo,true);
        }
        if(!StringUtil.isEmpty(transactionId)){
            searchParam.addFilter("txnRefNo",transactionId,true);
        }
        QueryHelp.setMainSql("giroPayee", "searchGiroDeduction", searchParam);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        SearchResult<GiroDeductionDto> body = beEicGatewayClient.giroDeductionDtoSearchResult(searchParam, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
        ParamUtil.setSessionAttr(bpc.request, "giroDedSearchResult", body);
        ParamUtil.setSessionAttr(bpc.request, "giroDedSearchParam", searchParam);
        String saveRetriggerOK = (String) ParamUtil.getSessionAttr(bpc.request,"saveRetriggerOK");
        if( !StringUtil.isEmpty(saveRetriggerOK)){
            ParamUtil.setRequestAttr(bpc.request,"saveRetriggerOK",saveRetriggerOK);
            ParamUtil.setSessionAttr(bpc.request,"saveRetriggerOK",null);
        }
    }

    private HttpServletRequest determineType(HttpServletRequest request){
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest)request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        if(multipartHttpServletRequest!=null){
            return multipartHttpServletRequest;
        }
        return request;
    }
    /**
     * StartStep: beGiroDeductionStep
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionStep(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionStep start ...."));
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String beGiroDeductionType =request.getParameter("beGiroDeductionType");
        bpc.request.setAttribute("beGiroDeductionType",beGiroDeductionType);
    }

    /**
     * StartStep: beGiroDeductionDoSearch
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionDoSearch(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionDoSearch start ...."));
    }

    /**
     * StartStep: beGiroDeductionSort
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionSort(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionSort start ...."));
    }

    /**
     * StartStep: beGiroDeductionPage
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionPage(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionPage start ...."));
        filterParameter.setPageNo(1);
        SearchResultHelper.doPage(bpc.request, filterParameter);
    }

    /**
     * StartStep: beGiroDeductionQuery
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionQuery(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionQuery start ...."));
    }

    /**
     * StartStep: beGiroDeductionRetVali
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionRetVali(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the beGiroDeductionRetVali start ...."));
        String[] appGroupNos = ParamUtil.getStrings(bpc.request,"giroDueCheck");
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        String retValiError = "giroDeductionError";
        List<String> appGroupList = IaisCommonUtils.genNewArrayList();
        if(appGroupNos != null && appGroupNos.length > 0){
            //todo appGroup payment status validate
            for(int i = 0; i < appGroupNos.length; i++){
                appGroupList.add(appGroupNos[i]);
            }
        } else {
            errMap.put(retValiError, "GENERAL_ERR0006");
            ParamUtil.setRequestAttr(bpc.request, "RGP_ACK001", AppConsts.FALSE);
        }
        if(errMap != null && errMap.size() > 0){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
        } else {
            ParamUtil.setRequestAttr(bpc.request,"appGroupList", appGroupList);
            ParamUtil.setRequestAttr(bpc.request,"flag", AppConsts.TRUE);
        }
    }

    public void uploadCsv(BaseProcessClass bpc) throws Exception{
        MultipartHttpServletRequest request = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        CommonsMultipartFile file = (CommonsMultipartFile) request.getFile("selectedFile");
        String name = file.getOriginalFilename();
        String substring = name.substring(name.lastIndexOf('.')+1);
        if(!CSV.equals(substring.toLowerCase())|| name.length()>100){
            bpc.request.setAttribute("message",MessageUtil.getMessageDesc("GENERAL_ACK022"));
            return;
        }
        Reader reader=new FileReader(FileUtils.multipartFileToFile(file, request.getSession().getId()));
        Iterable<CSVRecord> parse = CSVFormat.DEFAULT.withHeader("S/N","HCI Name", "Application No.","Transaction Reference No.","Bank Account No.","Payment Status","Payment Amount").parse(reader);
        Map<String,String> map=new HashMap<>();
        List<String> list=new ArrayList<>(HEADERS.length);
        String GENERAL_ACK020 =MessageUtil.getMessageDesc("GENERAL_ACK020");
        try {
            for(CSVRecord record:parse){
                for(String v : HEADERS){
                    String s = record.get(v);
                    if(Arrays.asList(HEADERS).contains(s)){
                        list.add(s);
                    }
                }
                String s = record.get("Application No.");
                if(Arrays.asList(HEADERS).contains(s)){
                    continue;
                }
                String payment_status_code = record.get("Payment Status");
                String payment_status = payment_status_code;
                payment_status = encryptionPayment(payment_status);
                if(payment_status.equals(payment_status_code)){
                    bpc.request.setAttribute("message",GENERAL_ACK020);
                    return;
                }
                map.put(s,payment_status);
            }
        }catch (Exception e){
            bpc.request.setAttribute("message",GENERAL_ACK020);
        }

        if(!list.equals(Arrays.asList(HEADERS))){
            bpc.request.setAttribute("message",GENERAL_ACK020);
            return;
        }
        List<GiroDeductionDto> giroDeductionDtoList= IaisCommonUtils.genNewArrayList();
        List<ApplicationGroupDto> applicationGroupDtos=new ArrayList<>(map.size());
        map.forEach((k,v)->{
            GiroDeductionDto giroDeductionDto=new GiroDeductionDto();
            ApplicationGroupDto applicationGroupDto=new ApplicationGroupDto();
            giroDeductionDto.setAppGroupNo(k);
            applicationGroupDto.setGroupNo(k);
            giroDeductionDto.setPmtStatus(v);
            applicationGroupDto.setPmtStatus(v);
            giroDeductionDtoList.add(giroDeductionDto);
            applicationGroupDtos.add(applicationGroupDto);
        });
        if (!giroDeductionDtoList.isEmpty()){
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            List<GiroDeductionDto> entity = beEicGatewayClient.updateDeductionDtoSearchResultUseGroups(giroDeductionDtoList, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();
            //update group status
            List<ApplicationGroupDto> applicationGroupDtoList = applicationClient.updateBeGroupStatus(applicationGroupDtos).getEntity();
            updateFeApplicationGroupStatus(applicationGroupDtoList);
            for (ApplicationGroupDto appGrp:applicationGroupDtoList
            ) {
                List<ApplicationDto> applicationDtoList=applicationService.getApplicaitonsByAppGroupId(appGrp.getId());
                for (ApplicationDto app:applicationDtoList
                     ) {
                    app.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_PAYMENT_RESUBMIT);
                    applicationService.callEicInterApplication(app);
                }
            }
            String general_ack021 = MessageUtil.getMessageDesc("GENERAL_ACK021");
            if(entity!=null&&entity.isEmpty()){
                general_ack021=general_ack021.replace("{num}","0");
                general_ack021=general_ack021.replace("were","was");
                general_ack021=general_ack021.replace("records","record");
            }else if(entity!=null&&entity.size()==1){
                general_ack021=general_ack021.replace("{num}","1");
                general_ack021=general_ack021.replace("records","record");
                general_ack021=general_ack021.replace("were","was");
            }else if(entity!=null&&entity.size()>1){
                general_ack021=general_ack021.replace("{num}",String.valueOf(entity.size()));

            }
            bpc.request.setAttribute("message", general_ack021);
        }else{
            bpc.request.setAttribute("message", "The file is empty");
        }
    }
    public void download(BaseProcessClass bpc) throws Exception {

    }

    /**
     * StartStep: beGiroDeductionRetrigger
     *
     * @param bpc
     * @throws
     */
    public void beGiroDeductionRetrigger(BaseProcessClass bpc){
        log.info(StringUtil.changeForLog("the beGiroDeductionRetrigger start ...."));
        List<String> appGroupList = (List<String>)ParamUtil.getRequestAttr(bpc.request, "appGroupList");
        List<GiroDeductionDto> giroDeductionDtoList=new ArrayList<>(appGroupList.size());
        for(String str: appGroupList){
            GiroDeductionDto giroDeductionDto=new GiroDeductionDto();
            giroDeductionDto.setAppGroupNo(str);
            giroDeductionDtoList.add(giroDeductionDto);
        }
        List<ApplicationGroupDto> applicationGroupDtos = giroDeductionBeService.sendMessageEmail(appGroupList);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
       /* giroDeductionClient.updateDeductionDtoSearchResultUseGroups(giroDeductionDtoList);*/
        beEicGatewayClient.updateDeductionDtoSearchResultUseGroups(giroDeductionDtoList, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        beEicGatewayClient.updateFeApplicationGroupStatus(applicationGroupDtos, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
        ParamUtil.setSessionAttr(bpc.request,"saveRetriggerOK",AppConsts.YES);
    }

    @GetMapping(value = "/generatorFileCsv")
    @ResponseBody
    public void generatorFileCsv(HttpServletRequest request, HttpServletResponse response) throws IOException {
        SearchResult<GiroDeductionDto> giroDedSearchResult =(SearchResult<GiroDeductionDto>)request.getSession().getAttribute("giroDedSearchResult");

        List<GiroDeductionDto> rows = giroDedSearchResult.getRows();
        long l = System.currentTimeMillis();
        FileWriter out = new FileWriter("classpath:"+l+".csv");
        AtomicInteger integer=new AtomicInteger(1);
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL
                .withHeader(HEADERS))) {
            rows.forEach(v->{
                try {
                    printer.printRecord(integer.get(),v.getHciName().replace("<br>", String.valueOf((char)10)+""),v.getAppGroupNo(),v.getTxnRefNo()
                    ,v.getAcctNo(),decryptPayment(v.getPmtStatus()),"$" + v.getAmount());
                    integer.getAndIncrement();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
        response.setContentType("application/x-octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename="+l+".csv" );
        try (OutputStream ops = new BufferedOutputStream(response.getOutputStream());
             InputStream in = java.nio.file.Files.newInputStream(Paths.get("classpath:"+l+".csv"))){
            byte buffer[] = new byte[1024];
            int len ;
            while((len=in.read(buffer))>0){
                ops.write(buffer, 0, len);
            }
            ops.flush();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }


    }

    private String decryptPayment(String payment){
        switch (payment){
            case "PMT09": return "Failed";
            case "PMT01":return "Successful";
            case "PMT03":return "Pending";
            default:return payment;
        }
    }
    private String encryptionPayment(String payment){
        switch (payment){
            case "Failed": return "PMT09";
            case "Successful":return "PMT01";
            case "Pending":return "PMT03";
            default:return payment;
        }
    }
    private void updateDeductionDtoSearchResultUseGroups(List<GiroDeductionDto> giroDeductionDtoList){
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        beEicGatewayClient.updateDeductionDtoSearchResultUseGroups(giroDeductionDtoList, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }
    private void eicGateway(List<GiroDeductionDto> giroDeductionDtoList){
        EicRequestTrackingDto postSaveTrack = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, ConfigServiceImpl.class.getName(),
                "updateDeductionDtoSearchResultUseGroups", currentApp + "-" + currentDomain,
                HcsaServiceConfigDto.class.getName(), JsonUtil.parseToJson(giroDeductionDtoList));
        AuditTrailDto intenet = AuditTrailHelper.getCurrentAuditTrailDto();
        FeignResponseEntity<EicRequestTrackingDto> fetchResult = eicRequestTrackingHelper.getAppEicClient().getPendingRecordByReferenceNumber(postSaveTrack.getRefNo());
        if (fetchResult != null && HttpStatus.SC_OK == fetchResult.getStatusCode()) {
            log.info(StringUtil.changeForLog("------"+JsonUtil.parseToJson(fetchResult)));
            EicRequestTrackingDto entity = fetchResult.getEntity();
            if (AppConsts.EIC_STATUS_PENDING_PROCESSING.equals(entity.getStatus())){
                updateDeductionDtoSearchResultUseGroups(giroDeductionDtoList);
                entity.setProcessNum(1);
                Date now = new Date();
                entity.setFirstActionAt(now);
                entity.setLastActionAt(now);
                entity.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                entity.setAuditTrailDto(intenet);
                eicRequestTrackingHelper.getAppEicClient().saveEicTrack(entity);

            }
        } else {
            log.info(StringUtil.changeForLog("------ null----"));
        }


    }

    private void updateFeApplicationGroupStatus(List<ApplicationGroupDto> applicationGroupDtos){
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        beEicGatewayClient.updateFeApplicationGroupStatus(applicationGroupDtos, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }
}
