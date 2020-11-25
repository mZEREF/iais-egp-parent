package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.api.util.FileUtil;
import com.ecquaria.cloud.moh.iais.api.util.SFTPUtil;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.*;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SpecicalPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPaymentStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigFeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.sz.commons.util.Calculator;
import com.ecquaria.cloud.moh.iais.common.utils.XmlBindUtil;
import ecq.commons.config.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * ServiceConfigServiceImpl
 *
 * @author suocheng
 * @date 10/14/2019
 */
@Service
@Slf4j
public class ServiceConfigServiceImpl implements ServiceConfigService {
    @Autowired
    private FileRepoClient fileRepoClient;
    @Autowired
    private AppConfigClient appConfigClient;
    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private ApplicationFeClient applicationFeClient;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private HcsaConfigFeClient hcsaConfigFeClient;
    @Autowired
    private AppPaymentStatusClient appPaymentStatusClient;
    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;

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

    @Override
    public List<HcsaServiceDto> getHcsaServiceDtosById(List<String> ids) {

        return   appConfigClient.getHcsaService(ids).getEntity();
    }

    @Override
    public Set<String> getAppGrpPremisesTypeBySvcId(List<String> svcIds) {

        return   appConfigClient.getAppGrpPremisesTypeBySvcId(svcIds).getEntity();
    }

    @Override
    public PostCodeDto getPremisesByPostalCode(String postalCode) {
        return systemAdminClient.getPostCodeByCode(postalCode).getEntity();
    }

    @Override
    public String getSvcIdBySvcCode(String svcCode) {
        Map<String,Object> map = IaisCommonUtils.genNewHashMap();
        map.put("code", svcCode);

        return   appConfigClient.getServiceIdByCode(svcCode).getEntity();
    }

    @Override
    public Map<String,AppGrpPremisesDto> getAppGrpPremisesDtoByLoginId(String loginId) {
        List<AppGrpPremisesDto> appGrpPremisesDtos = licenceClient.getDistinctPremisesByLicenseeId(loginId).getEntity();
        Map<String,AppGrpPremisesDto> appGrpPremisesDtoMap = IaisCommonUtils.genNewHashMap();
        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
            if(!StringUtil.isEmpty(appGrpPremisesDto.getPremisesSelect())){
                NewApplicationHelper.setWrkTime(appGrpPremisesDto);
                appGrpPremisesDto.setExistingData(AppConsts.YES);
                appGrpPremisesDtoMap.put(appGrpPremisesDto.getPremisesSelect(),appGrpPremisesDto);
            }
        }
        return appGrpPremisesDtoMap;
    }

    @Override
    public String saveFileToRepo(MultipartFile file) throws IOException {
        //move file
        moveFile(file.getOriginalFilename(), sharedPath, file.getBytes());
        //
        FileRepoDto fileRepoDto = new FileRepoDto();
        fileRepoDto.setFileName(file.getOriginalFilename());
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();

        fileRepoDto.setAuditTrailDto(auditTrailDto);
        fileRepoDto.setRelativePath(sharedPath);
        String fileRepoStr = JsonUtil.parseToJson(fileRepoDto);
        //todo wait job ok => change method
        FeignResponseEntity<String> re = fileRepoClient.saveFiles(file, fileRepoStr);
        String str = "";
        if (re.getStatusCode() == HttpStatus.SC_OK) {
            str = re.getEntity();
        }
        return str;
    }

    @Override
    public List<HcsaSvcDocConfigDto> getAllHcsaSvcDocs(String serviceId) {
        Map<String,String> docMap = IaisCommonUtils.genNewHashMap();
        if(StringUtil.isEmpty(serviceId)){
            docMap.put("common", "0");
            docMap.put("premises", "1");
        }else{
            docMap.put("svc",serviceId);
            docMap.put("common", "0");
        }
        String docMapJson = JsonUtil.parseToJson(docMap);
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos =  appConfigClient.getHcsaSvcDocConfig(docMapJson).getEntity();
        return hcsaSvcDocConfigDtos;
    }

    @Override
    public List<HcsaSvcSubtypeOrSubsumedDto> loadLaboratoryDisciplines(String serviceId) {
        Map<String,Object> map = IaisCommonUtils.genNewHashMap();
        map.put("svcId", serviceId);

        return appConfigClient.listSubCorrelation(serviceId).getEntity();
    }


    @Override
    public List<HcsaSvcPersonnelDto> getGOSelectInfo(String serviceId, String psnType) {
        Map<String,Object> map = IaisCommonUtils.genNewHashMap();
        map.put("serviceId", serviceId);
        map.put("psnType", psnType);

        return  appConfigClient.getServiceType(serviceId,psnType).getEntity();
    }

    @Override
    public AppSvcCgoDto loadGovernanceOfficerByCgoId(String cgoId) {
        //to do
        return null;
    }

    @Override
    public byte[] downloadFile(String fileRepoId) {
        return fileRepoClient.getFileFormDataBase(fileRepoId).getEntity();
    }

    @Override
    public void updatePaymentStatus(ApplicationGroupDto appGrp) {
        appGrp.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        applicationFeClient.doPaymentUpDate(appGrp);
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoDraft(String draftNo) {
        return applicationFeClient.draftNumberGet(draftNo).getEntity();
    }

    @Override
    public List<HcsaServiceDto> getAllService(){
        return appConfigClient.allHcsaService().getEntity();
    }

    @Override
    public List<HcsaServiceDto> getServicesInActive(){
        return appConfigClient.getActiveServices().getEntity();
    }

    @Override
    public List<HcsaServiceStepSchemeDto> getHcsaServiceStepSchemesByServiceId(String serviceId) {
        return appConfigClient.getServiceStepsByServiceId(serviceId).getEntity();
    }
    @Override
    public List<HcsaServiceCorrelationDto> getCorrelation(){
        return appConfigClient.serviceCorrelation().getEntity();
    }

    private void moveFile(String fileName, String path, byte[] fileData) throws IOException {
        File file = new File(path+"/"+fileName);
        OutputStream fos = null;
        try {
            fos = Files.newOutputStream(Paths.get(file.getPath()));
            fos.write(fileData);
        } catch (FileNotFoundException e) {
            log.error(StringUtil.changeForLog("file not found"));
        }finally {
            if(fos!= null){
                fos.close();
            }
        }

    }

    @Override
    public List<HcsaSvcPersonnelDto> getSvcAllPsnConfig(List<HcsaServiceStepSchemeDto> svcStep, String svcId) {
        List<SpecicalPersonDto> specicalPersonDtos =IaisCommonUtils.genNewArrayList();
        SpecicalPersonDto specicalPersonDto = new SpecicalPersonDto();
        List<String> psnTypes = IaisCommonUtils.genNewArrayList();
        for(HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto:svcStep){
            if(svcId.equals(hcsaServiceStepSchemeDto.getServiceId())){
                String stepCode = hcsaServiceStepSchemeDto.getStepCode();
                if(HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(stepCode)){
                    psnTypes.add(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
                }else if(HcsaConsts.STEP_PRINCIPAL_OFFICERS.equals(stepCode)){
                    psnTypes.add(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                    psnTypes.add(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
                }else if(HcsaConsts.STEP_SERVICE_PERSONNEL.equals(stepCode)){
                    psnTypes.add(ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL);
                }else if(HcsaConsts.STEP_MEDALERT_PERSON.equals(stepCode)){
                    psnTypes.add(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
                }
            }
        }
        specicalPersonDto.setServiceId(svcId);
        specicalPersonDto.setType(psnTypes);
        specicalPersonDtos.add(specicalPersonDto);
        return appConfigClient.getServiceSpecificPerson(specicalPersonDtos).getEntity();
    }

    @Override
    public Map<String,List<HcsaSvcPersonnelDto>>  getAllSvcAllPsnConfig(List<HcsaServiceStepSchemeDto> svcStep, List<String> svcIds) {
        Map<String,List<HcsaSvcPersonnelDto>> allSvcAllPsnConfig = IaisCommonUtils.genNewHashMap();
        for(String svcId:svcIds){
            List<HcsaSvcPersonnelDto> oneSvcAllPsnConfig = getSvcAllPsnConfig(svcStep, svcId);
            allSvcAllPsnConfig.put(svcId, oneSvcAllPsnConfig);
        }
        return allSvcAllPsnConfig;
    }

    @Override
    public List<HcsaServiceStepSchemeDto> getHcsaServiceStepSchemesByServiceId(List<String> svcIds) {
        return appConfigClient.getServiceStepsByServiceIds(svcIds).getEntity();
    }

    @Override
    public List<HcsaServiceDto> getHcsaServiceByNames(List<String> names) {
        return appConfigClient.getHcsaServiceByNames(names).getEntity();
    }

    @Override
    public List<SelectOption> getPubHolidaySelect() {
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
            publicHolidayList.add(new SelectOption(Formatter.formatDate(pb.getFromDate()),
                    MasterCodeUtil.getCodeDesc(pb.getPhCode())));
        });
        return publicHolidayList;
    }

    @Override
    public HcsaServiceDto getHcsaServiceDtoById(String id) {
        return appConfigClient.getHcsaServiceDtoByServiceId(id).getEntity();
    }

    @Override
    public void paymentUpDateByGrpNo(ApplicationGroupDto appGrp) {
        applicationFeClient.paymentUpDateByGrpNo(appGrp);
    }

    @Override
    public HcsaServiceDto getActiveHcsaServiceDtoByName(String svcName) {
        return appConfigClient.getActiveHcsaServiceDtoByName(svcName).getEntity();
    }

    @Override
    public void updateAppGrpPmtStatus(ApplicationGroupDto appGrp) {
        appGrp.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        applicationFeClient.doPaymentUpDate(appGrp);
    }

    @Override
    public List<HcsaSvcDocConfigDto> getPrimaryDocConfigByVersion(Integer version) {
        String ver = "empty";
        if(version != null){
            ver = String.valueOf(version);
        }
        return appConfigClient.getPrimaryDocConfigByVersion(ver).getEntity();
    }

    @Override
    public List<HcsaSvcDocConfigDto> getPrimaryDocConfigByIds(List<String> ids) {
        return hcsaConfigFeClient.listSvcDocConfig(ids).getEntity();
    }

    @Override
    public List<HcsaSvcDocConfigDto> getPrimaryDocConfigById(String id) {
        return hcsaConfigFeClient.getPrimaryDocConfigList(id).getEntity();
    }

    @Override
    public void giroPaymentXmlUpdateByGrpNo(AppSubmissionDto appGrp) {
        //todo
         GiroPaymentXmlDto giroPaymentXmlDto = genGiroPaymentXmlDtoByAppGrp(appGrp);
         appPaymentStatusClient.updateGiroPaymentXmlDto(giroPaymentXmlDto);
    }

    private GiroPaymentXmlDto genGiroPaymentXmlDtoByAppGrp(AppSubmissionDto appGrp){
        GiroPaymentXmlDto giroPaymentXmlDto = new GiroPaymentXmlDto();
        giroPaymentXmlDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        //todo gen xml by appGroup;
        GiroGroupDataDto giroPaymentDto = new GiroGroupDataDto();
        giroPaymentDto.setAmount(appGrp.getAmount());
        giroPaymentDto.setResidualPayment(appGrp.getAmount());
        giroPaymentDto.setAppGroupNo(appGrp.getAppGrpNo());
        giroPaymentXmlDto.setXmlData(JsonUtil.parseToJson(giroPaymentDto));
        giroPaymentXmlDto.setXmlType(ApplicationConsts.GIRO_NEED_GEN_XML);
        giroPaymentXmlDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        return giroPaymentXmlDto;
    }

    @Override
    public void sendGiroXmlToSftp() {
        List<GiroPaymentXmlDto> giroPaymentXmlDtos =  appPaymentStatusClient.getGiroPaymentDtosByStatusAndXmlType(AppConsts.COMMON_STATUS_ACTIVE,ApplicationConsts.GIRO_NEED_GEN_XML).getEntity();
      if(IaisCommonUtils.isEmpty(giroPaymentXmlDtos)){
          return;
      }
        List<GiroPaymentXmlDto> giroPaymentXmlDtosGen = IaisCommonUtils.genNewArrayList();
      for(GiroPaymentXmlDto giroPaymentXmlDto : giroPaymentXmlDtos){
          GiroGroupDataDto giroGroupDataDto = JsonUtil.parseToObject(giroPaymentXmlDto.getXmlData(),GiroGroupDataDto.class);
          List<GiroPaymentDto> giroPaymentDtos = appPaymentStatusClient.getGiroPaymentDtosByPmtStatusAndAppGroupNo(AppConsts.COMMON_STATUS_ACTIVE,giroGroupDataDto.getAppGroupNo()).getEntity();
          if(!IaisCommonUtils.isEmpty(giroPaymentDtos)){
                double amount = 0.0;
              for(GiroPaymentDto giroPaymentDto : giroPaymentDtos){
                 if(ApplicationConsts.GIRO_BANK_PAYMENT_TYPE_MONEYPAY.equalsIgnoreCase(giroPaymentDto.getPmtType())){
                     amount = Calculator.add(amount,giroPaymentDto.getAmount());
                 }else {
                     amount =  Calculator.sub(amount,giroPaymentDto.getAmount());
                 }
              }
              if(giroGroupDataDto.getAmount() > amount){
                  giroGroupDataDto.setResidualPayment(Calculator.sub(giroGroupDataDto.getAmount(),amount));
              }else {
                  giroGroupDataDto.setResidualPayment(0.0d);
              }
              giroPaymentXmlDto.setXmlData(JsonUtil.parseToJson(giroGroupDataDto));
          }
          giroPaymentXmlDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
          giroPaymentXmlDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
          if(giroGroupDataDto.getResidualPayment() > 0){
              giroPaymentXmlDtosGen.add(giroPaymentXmlDto) ;
          }
      }
      //todo change filename
        String xml = genXmlByGiroPaymentXmlDtos(giroPaymentXmlDtosGen);
        String fileName = ApplicationConsts.GIRO_UPLOAD_FILE_PATH+  "uploadGiro"+Formatter.formatDateTime(new Date(),Formatter.DATE_REF_NUMBER)+".xml";
        if(genXmlFileToSftp(xml,fileName)){
            GiroPaymentXmlDto giroPaymentXmlDtoSend = new GiroPaymentXmlDto();
            giroPaymentXmlDtoSend.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            giroPaymentXmlDtoSend.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            giroPaymentXmlDtoSend.setXmlType(ApplicationConsts.GIRO_SEND_XML_SFTP);
            giroPaymentXmlDtoSend.setXmlData(xml);
            appPaymentStatusClient.updateGiroPaymentXmlDto(giroPaymentXmlDtoSend);
            appPaymentStatusClient.updateGiroPaymentXmlDtos(giroPaymentXmlDtosGen);
        }
    }
    private  String  genXmlByGiroPaymentXmlDtos(List<GiroPaymentXmlDto> giroPaymentXmlDtos){
        GiroXmlPaymentDto grioXmlPaymentDto = getGiroXmlPaymentDtoByGiroPaymentXmlDtos(giroPaymentXmlDtos);
        String xml ="";
        try{
            xml = XmlBindUtil.convertToXml(grioXmlPaymentDto);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return  xml;
    }
    private GiroXmlPaymentDto getGiroXmlPaymentDtoByGiroPaymentXmlDtos(List<GiroPaymentXmlDto> giroPaymentXmlDtos){
        GiroXmlPaymentDto grioXmlPaymentDto = new GiroXmlPaymentDto();
        //todo need change giroPaymentXmlDtos to grioXmlPaymentDto
        List<InputDetailDto> INPUT_DETAIL = IaisCommonUtils.genNewArrayList();
        for(GiroPaymentXmlDto giroPaymentXmlDto : giroPaymentXmlDtos){
            GiroGroupDataDto giroGroupDataDto = JsonUtil.parseToObject(giroPaymentXmlDto.getXmlData(),GiroGroupDataDto.class);
            InputDetailDto inputDetailDto = new InputDetailDto();
            inputDetailDto.setAPPLICATION_NUMBER( giroGroupDataDto.getAppGroupNo());
            inputDetailDto.setPAID_AMT(giroGroupDataDto.getResidualPayment());
            INPUT_DETAIL.add(inputDetailDto);
        }
        grioXmlPaymentDto.setINPUT_DETAIL(INPUT_DETAIL);
        return  grioXmlPaymentDto;
    }

    private boolean genXmlFileToSftp(String xmlData,String fileName){
        try{
            if( FileUtil.writeToFile(fileName,xmlData)){
                SFTPUtil.upload(fileName, Config.get("sftp.uploadfilefolder"));
                return true;
            }
             return false;
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return  false;
        }
    }

    @Override
    public void getGiroXmlFromSftpAndSaveXml() {
           try{
               //todo get sftp xmls to analyze
               String fileName = "uploadGiro20201125_151434325.xml";
               if(SFTPUtil.download(ApplicationConsts.GIRO_DOWN_FILE_PATH,fileName,Config.get("sftp.downloadfilefolder"))){
                   String xml = FileUtil.getString(ApplicationConsts.GIRO_DOWN_FILE_PATH+fileName);
                   GiroXmlPaymentBackDto giroXmlPaymentBackDto = (GiroXmlPaymentBackDto) XmlBindUtil.convertToObject(GiroXmlPaymentBackDto.class,xml);
                   getGiroPaymentDtosByGiroXmlPaymentBackDto(giroXmlPaymentBackDto);
                   saveXml( xml,AppConsts.COMMON_STATUS_ACTIVE,ApplicationConsts.GIRO_DOWN_XML_SFTP);
                   saveAppGroup(giroXmlPaymentBackDto.getINPUT_DETAIL());
               }
            }catch (Exception e){
               log.error(e.getMessage(),e);
           }
    }
    private void saveAppGroup( List<InputDetailBackDto> inputDetailBackDtos){
         if(!IaisCommonUtils.isEmpty(inputDetailBackDtos)){
             List<String> appNo = IaisCommonUtils.genNewArrayList();
             for(InputDetailBackDto inputDetailBackDto : inputDetailBackDtos){
                 if(!appNo.contains(inputDetailBackDto.getAPPLICATION_NUMBER())){
                     ApplicationGroupDto applicationGroupDto = applicationFeClient.getAppGrpByAppNo(inputDetailBackDto.getAPPLICATION_NUMBER()).getEntity();
                     List<GiroPaymentDto> giroPaymentDtos = appPaymentStatusClient.getGiroPaymentDtosByPmtStatusAndAppGroupNo(AppConsts.COMMON_STATUS_ACTIVE,inputDetailBackDto.getAPPLICATION_NUMBER()).getEntity();
                     if(!IaisCommonUtils.isEmpty(giroPaymentDtos)) {
                         double amount = 0.0;
                         for (GiroPaymentDto giroPaymentDto : giroPaymentDtos) {
                             if (ApplicationConsts.GIRO_BANK_PAYMENT_TYPE_MONEYPAY.equalsIgnoreCase(giroPaymentDto.getPmtType())) {
                                 amount = Calculator.add(amount, giroPaymentDto.getAmount());
                             } else {
                                 amount = Calculator.sub(amount, giroPaymentDto.getAmount());
                             }
                         }
                         if(amount >= applicationGroupDto.getAmount()){
                             applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS);
                             applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                             applicationFeClient.updateAppGrpPmtStatus(applicationGroupDto);
                         }
                     }
                 }
             }
         }
    }

    private List<GiroPaymentDto> getGiroPaymentDtosByGiroXmlPaymentBackDto(GiroXmlPaymentBackDto giroXmlPaymentBackDto){
        List<GiroPaymentDto> giroPaymentDtos = IaisCommonUtils.genNewArrayList();
        //todo need analyze giroXmlPaymentBackDto to giroPaymentDtos save db
        List<InputDetailBackDto> inputDetailBackDtos = giroXmlPaymentBackDto.getINPUT_DETAIL();
        for(InputDetailBackDto inputDetailBackDto : inputDetailBackDtos){
            GiroPaymentDto giroPaymentDto =new GiroPaymentDto();
            giroPaymentDto.setPmtStatus(AppConsts.COMMON_STATUS_ACTIVE);
            //todo get xml analyze  is pay or payback
            giroPaymentDto.setPmtType(ApplicationConsts.GIRO_BANK_PAYMENT_TYPE_MONEYPAY);
            giroPaymentDto.setAppGroupNo(inputDetailBackDto.getAPPLICATION_NUMBER());
            giroPaymentDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            //todo get xml analyze amount
            giroPaymentDto.setAmount(0.0d);
            //save giroPaymentDto
            appPaymentStatusClient.updateGiroPaymentDto(giroPaymentDto);
            giroPaymentDtos.add(giroPaymentDto);
        }



        return  giroPaymentDtos;
    }
    private GiroPaymentXmlDto saveXml(String xml,String status,String type){
        GiroPaymentXmlDto giroPaymentXmlDto = new GiroPaymentXmlDto();
        giroPaymentXmlDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        giroPaymentXmlDto.setXmlData(xml);
        giroPaymentXmlDto.setXmlType(type);
        giroPaymentXmlDto.setStatus(status);
       return appPaymentStatusClient.updateGiroPaymentXmlDto(giroPaymentXmlDto).getEntity();
    }
}
