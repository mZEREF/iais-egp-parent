package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.giro.GiroDeductionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceConfigDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.SystemParamUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.GiroDeductionBeService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.GiroDeductionClient;
import com.ecquaria.cloud.moh.iais.service.impl.ConfigServiceImpl;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

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
    private GiroDeductionClient giroDeductionClient;
    private final static String CSV="csv";

    private static final String [] STATUS={"PDNG","CMSTAT001","FAIL"};
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

    private final static String[] HEADERS = { "HCI Name", "Application No.","Transaction Reference No.","Invoice No.","Bank Account No.","Payment Status","Payment Amount"};
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
        String param = (String)bpc.request.getAttribute("param");
        SearchParam searchParam = SearchResultHelper.getSearchParam(bpc.request, filterParameter, true);;
        if("Y".equals(param)){
            searchParam=(SearchParam)bpc.request.getAttribute("searchParam");
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
        SearchParam searchParam = SearchResultHelper.getSearchParam(request, filterParameter, true);
        String txnRefNo = request.getParameter("txnRefNo");
        String bankAccountNo = request.getParameter("bankAccountNo");
        String group_no = request.getParameter("applicationNo");
        String paymentAmount = request.getParameter("paymentAmount");
        String hci_name = request.getParameter("hci_name");
        if(!StringUtil.isEmpty(group_no)){
            searchParam.addFilter("groupNo",group_no.trim(),true);
        }

        if(!StringUtil.isEmpty(paymentAmount)){
            searchParam.addFilter("amount",paymentAmount.trim(),true);
        }
        if(!StringUtil.isEmpty(bankAccountNo)){
            searchParam.addFilter("acctNo",bankAccountNo.trim(),true);
        }
        if(!StringUtil.isEmpty(hci_name)){
            searchParam.addFilter("hciName",hci_name.trim(),true);
        }
        if(!StringUtil.isEmpty(txnRefNo)){
            searchParam.addFilter("txnRefNo",txnRefNo.trim(),true);
        }
        bpc.request.setAttribute("searchParam",searchParam);
        bpc.request.setAttribute("param","Y");
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
        Reader reader=new FileReader(FileUtils.multipartFileToFile(file));
        Iterable<CSVRecord> parse = CSVFormat.DEFAULT.withHeader("HCI Name", "Application No.","Transaction Reference No.","Invoice No.","Bank Account No.","Payment Status","Payment Amount").parse(reader);
        Map<String,String> map=new HashMap<>();
        try {
            for(CSVRecord record:parse){
                String s = record.get("Application No.");
                if("Application No.".equals(s)){
                    continue;
                }
                String payment_status = record.get("Payment Status");
                map.put(s,payment_status);
            }
        }catch (Exception e){
            bpc.request.setAttribute("message",MessageUtil.getMessageDesc("GENERAL_ACK020"));
        }
        List<GiroDeductionDto> giroDeductionDtoList= IaisCommonUtils.genNewArrayList();
        map.forEach((k,v)->{
            GiroDeductionDto giroDeductionDto=new GiroDeductionDto();
            giroDeductionDto.setAppGroupNo(k);
            giroDeductionDto.setPmtStatus(v);
            giroDeductionDtoList.add(giroDeductionDto);
        });
        if (!giroDeductionDtoList.isEmpty()){
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            beEicGatewayClient.updateDeductionDtoSearchResultUseGroups(giroDeductionDtoList, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
            bpc.request.setAttribute("message", MessageUtil.getMessageDesc("GENERAL_ACK021"));
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
       /* giroDeductionClient.updateDeductionDtoSearchResultUseGroups(appGroupList);*/
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
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.EXCEL
                .withHeader(HEADERS))) {
            rows.forEach(v->{
                try {
                    printer.printRecord(v.getHciName().replace("<br>", " "),v.getAppGroupNo(),v.getTxnRefNo(),v.getInvoiceNo()
                    ,v.getAcctNo(),v.getPmtStatus(),"$" + v.getAmount());
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            });
        }
        response.setContentType("application/x-octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename="+l+".csv" );
        File file=new File("classpath:"+l+".csv");
        try ( OutputStream ops = new BufferedOutputStream(response.getOutputStream());
              InputStream in =Files.newInputStream(Paths.get(file.getPath()))){
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
