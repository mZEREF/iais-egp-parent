package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.api.util.FileUtil;
import com.ecquaria.cloud.moh.iais.api.util.SFTPUtil;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.grio.GrioConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.*;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.Ack1.InputAck1Dto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.Ack1.InputHeaderAck1Dto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.Ack2OrAck3.InputAck2Or3Dto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.Ack2OrAck3.InputDataAck2Or3Dto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.Ack2OrAck3.InputHeaderAck2Or3Dto;
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
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgGiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.XmlBindUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.sz.commons.util.Calculator;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    @Value("${spring.application.name}")
    private String currentApp;
    @Value("${iais.current.domain}")
    private String currentDomain;
    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;
    @Autowired
    private AppEicClient appEicClient;
    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;
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
        FileRepoDto fileRepoDto = new FileRepoDto();
        fileRepoDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
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
    public ApplicationGroupDto updateAppGrpPmtStatus(ApplicationGroupDto appGrp) {
        appGrp.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationFeClient.updateAppGrpPmtStatus(appGrp).getEntity();
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
    public AppSubmissionDto giroPaymentXmlUpdateByGrpNo(AppSubmissionDto appGrp) {
        if(!AppConsts.YES.equalsIgnoreCase(ConfigHelper.getString("pay.giro.switch"))) {
            log.info("pay.giro.switch is closed");
            appGrp.setPmtStatus( ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS);
            return appGrp;
        }else {
            appGrp.setPmtStatus( ApplicationConsts.PAYMENT_STATUS_PENDING_GIRO);
        }
        //todo
         GiroPaymentXmlDto giroPaymentXmlDto = genGiroPaymentXmlDtoByAppGrp(appGrp);
         appPaymentStatusClient.updateGiroPaymentXmlDto(giroPaymentXmlDto);
         return appGrp;
    }

    private String genGiroTranNo(){
       return "GIROTRANS-"+Formatter.formatDateTime(new Date(),Formatter.DATE_REF_NUMBER).replace("_","");
    }
    private GiroPaymentXmlDto genGiroPaymentXmlDtoByAppGrp(AppSubmissionDto appGrp){
        GiroPaymentXmlDto giroPaymentXmlDto = new GiroPaymentXmlDto();
        giroPaymentXmlDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        //todo gen xml by appGroup;
        String tranNo = genGiroTranNo();
        appGrp.setGiroTranNo(tranNo);
        GiroGroupDataDto giroPaymentDto = new GiroGroupDataDto();
        giroPaymentDto.setAmount(appGrp.getAmount());
        giroPaymentDto.setResidualPayment(appGrp.getAmount());
        giroPaymentDto.setAppGroupNo(appGrp.getAppGrpNo());
        giroPaymentXmlDto.setTag(appGrp.getAppGrpNo());
        giroPaymentDto.setGiroTranNo(tranNo);
        giroPaymentXmlDto.setXmlData(JsonUtil.parseToJson(giroPaymentDto));
        giroPaymentXmlDto.setXmlType(ApplicationConsts.GIRO_NEED_GEN_XML);
        giroPaymentXmlDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        return giroPaymentXmlDto;
    }

    @Override
    public void sendGiroXmlToSftp() {
        log.info("-------sendGiroXmlToSftp start---------");
       if(!AppConsts.YES.equalsIgnoreCase(ConfigHelper.getString("pay.giro.switch"))) {
           log.info("pay.giro.switch is closed");
           return;
       }
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
        GiroXmlPaymentDto grioXmlPaymentDto = getGiroXmlPaymentDtoByGiroPaymentXmlDtos(giroPaymentXmlDtos);
        String xml = genXmlByGiroPaymentXmlDtos(grioXmlPaymentDto);
        String uploadGrioFileData = dbsFileDataByGiroXmlPaymentDto(grioXmlPaymentDto);
        String tag = genFileNameUploadSFTP();
        String fileName = ApplicationConsts.GIRO_UPLOAD_FILE_PATH+ConfigHelper.getString("giro.sftp.linux.seperator")+ tag;
        if(genXmlFileToSftp(uploadGrioFileData,fileName, ConfigHelper.getString("giro.sftp.uploadfilefolder"))){
            GiroPaymentXmlDto giroPaymentXmlDtoSend = new GiroPaymentXmlDto();
            giroPaymentXmlDtoSend.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            giroPaymentXmlDtoSend.setTag(tag);
            giroPaymentXmlDtoSend.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            giroPaymentXmlDtoSend.setXmlType(ApplicationConsts.GIRO_SEND_XML_SFTP);
            giroPaymentXmlDtoSend.setXmlData(xml);
            appPaymentStatusClient.updateGiroPaymentXmlDto(giroPaymentXmlDtoSend);
            appPaymentStatusClient.updateGiroPaymentXmlDtos(giroPaymentXmlDtosGen);
            genFakeAck(grioXmlPaymentDto,tag );
        }
        log.info("-------sendGiroXmlToSftp end---------");
    }
    private boolean genFakeAck(GiroXmlPaymentDto grioXmlPaymentDto,String tag){
        log.info("-----genFakeAck start ----");
       if(!AppConsts.YES.equalsIgnoreCase(ConfigHelper.getString("grio.ack.get.ok"))){
          return genAck01File(grioXmlPaymentDto,tag)&& genAck02File(grioXmlPaymentDto,tag)&& genAck03File(grioXmlPaymentDto,tag);
        }else {
           log.info("----- grio.ack.get.ok is 1,true ack----");
       }
        log.info("-----genFakeAck end ----");
        return  true;
    }

    private boolean genAck01File(GiroXmlPaymentDto grioXmlPaymentDto,String tag){
        String dateString =  Formatter.formatDateTime(new Date(),Formatter.DATE_FILE_NAME);
        InputHeaderAck1Dto inputHeaderAck1Dto = new InputHeaderAck1Dto("HEADER", "15545033",dateString, "DBSSSGSG", tag, "UFF.001.003.01", "MINISTRY OF HEALTH", "SG","MIOFHE01", "ACTC","");
        InputTrailerDto INPUT_TRAILER = grioXmlPaymentDto.getINPUT_TRAILER();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(inputHeaderAck1Dto.toString()).append(INPUT_TRAILER.toString());
        String fileName = ApplicationConsts.GIRO_DOWN_FILE_PATH +ConfigHelper.getString("giro.sftp.linux.seperator")+ tag + '.'+ dateString + '.'+"ACK1";
        return genXmlFileToSftp(stringBuilder.toString(),fileName,ConfigHelper.getString("giro.sftp.downloadfilefolder"));
    }
    private boolean genAck02File(GiroXmlPaymentDto grioXmlPaymentDto,String tag){
        String dateString =  Formatter.formatDateTime(new Date(),Formatter.DATE_FILE_NAME);
        InputHeaderAck2Or3Dto inputHeaderAck2Dto = new InputHeaderAck2Or3Dto("HEADER", "D000001554503310",tag, dateString, "MINISTRY OF HEALTH","MIOFHE01", "ACTC","");
        InputTrailerDto INPUT_TRAILER = grioXmlPaymentDto.getINPUT_TRAILER();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(inputHeaderAck2Dto.toString());
        List<InputDetailDto> INPUT_DETAIL = grioXmlPaymentDto.getINPUT_DETAIL();
        if(!IaisCommonUtils.isEmpty(INPUT_DETAIL)){
            for(InputDetailDto inputDetailDto : INPUT_DETAIL){
                InputDataAck2Or3Dto inputDataAck2Or3Dto = new InputDataAck2Or3Dto("DATA", "COL", "0010521098", "SGD","SGD", inputDetailDto.getParticulars(), inputDetailDto.getPaymentDate(), "client 1", "OCBCSGSGXXX", "SGD", inputDetailDto.getAmount(), "ACWC", "", inputDetailDto.getBatchID(), "", "0", inputDetailDto.getReceivingAccountNumber());
                stringBuilder.append(inputDataAck2Or3Dto.toString());
            }
        }
        stringBuilder.append(INPUT_TRAILER.toString());
        String fileName = ApplicationConsts.GIRO_DOWN_FILE_PATH +ConfigHelper.getString("giro.sftp.linux.seperator")+ tag + '.'+ dateString + '.'+"ACK2";
        return genXmlFileToSftp(stringBuilder.toString(),fileName,ConfigHelper.getString("giro.sftp.downloadfilefolder"));
    }
    private boolean genAck03File(GiroXmlPaymentDto grioXmlPaymentDto,String tag){
        String dateString =  Formatter.formatDateTime(new Date(),Formatter.DATE_FILE_NAME);
        InputHeaderAck2Or3Dto inputHeaderAck3Dto = new InputHeaderAck2Or3Dto("HEADER", "D000001554503310",tag, dateString, "MINISTRY OF HEALTH","MIOFHE01", "ACSP","");
        InputTrailerDto INPUT_TRAILER = grioXmlPaymentDto.getINPUT_TRAILER();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(inputHeaderAck3Dto.toString());
        List<InputDetailDto> INPUT_DETAIL = grioXmlPaymentDto.getINPUT_DETAIL();
        if(!IaisCommonUtils.isEmpty(INPUT_DETAIL)){
            for(InputDetailDto inputDetailDto : INPUT_DETAIL){
                InputDataAck2Or3Dto inputDataAck2Or3Dto = new InputDataAck2Or3Dto("DATA", "COL", "0010521098", "SGD","SGD", inputDetailDto.getParticulars(),inputDetailDto.getPaymentDate() , "client 1", "OCBCSGSGXXX", "SGD", inputDetailDto.getAmount(), "ACCP", "", inputDetailDto.getBatchID(), "", "", "");
                stringBuilder.append(inputDataAck2Or3Dto.toString());
            }
        }
        stringBuilder.append(INPUT_TRAILER.toString());
        String fileName = ApplicationConsts.GIRO_DOWN_FILE_PATH +ConfigHelper.getString("giro.sftp.linux.seperator")+ tag + '.'+ dateString + '.'+"ACK3";
        return genXmlFileToSftp(stringBuilder.toString(),fileName,ConfigHelper.getString("giro.sftp.downloadfilefolder"));
    }
    private String genFileNameUploadSFTP(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(ConfigHelper.getString("col.giro.dbs.genfile.prefix","UFF2.FORMAT362")).
                append('.').append(ConfigHelper.getString("col.giro.dbs.genfile.HQ_ID","MIOFHE01")).
                append('.').append(ConfigHelper.getString("col.giro.dbs.genfile.subsi_ID","MIOFHE01"))
                .append('.').append(ConfigHelper.getString("col.giro.dbs.genfile.tag","GIRO_"))
                .append(Formatter.formatDateTime(new Date(),Formatter.DATE_FILE_NAME)).
                append('.').append(ConfigHelper.getString("col.giro.dbs.genfile.suffix","csv.DBSSSGSG"));
           return stringBuilder.toString();
    }
    private String dbsFileDataByGiroXmlPaymentDto(GiroXmlPaymentDto grioXmlPaymentDto){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(grioXmlPaymentDto.getINPUT_HEADER().toString());
        List<InputDetailDto> inputDetailDtos = grioXmlPaymentDto.getINPUT_DETAIL();
        if(!IaisCommonUtils.isEmpty(inputDetailDtos)){
            for(InputDetailDto inputDetailDto : inputDetailDtos){
                stringBuilder.append(inputDetailDto.toString());
            }
        }
        stringBuilder.append(grioXmlPaymentDto.getINPUT_TRAILER());
        return stringBuilder.toString();
    }
    private  String  genXmlByGiroPaymentXmlDtos(GiroXmlPaymentDto grioXmlPaymentDto){
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
        grioXmlPaymentDto.setINPUT_HEADER(getInputHeaderDto());
        List<InputDetailDto> INPUT_DETAIL = IaisCommonUtils.genNewArrayList();
        InputTrailerDto inputTrailerDto = new InputTrailerDto();
        int totalNoOfTransactions = giroPaymentXmlDtos.size();
        inputTrailerDto.setTotalNoOfTransactions(totalNoOfTransactions);
        double totalTransactionAmount = 0.0;
        String recordTypeData = ConfigHelper.getString("col.giro.dbs.data.record.type.dec","PAYMENT");
        String productTypeData =  ConfigHelper.getString("col.giro.dbs.data.product.type","COL");
        String paymentCurrency =  ConfigHelper.getString( "col.giro.dbs.data.account.currency","SGD");
        String originatingAccountNumber = ConfigHelper.getString("col.giro.dbs.data.account.originatingAccountNumber","0010521098");
        String newDateString   = Formatter.formatDateTime(new Date(),Formatter.DATE_CMS_INTERFACE);
        String receivingPartyName = ConfigHelper.getString( "col.giro.dbs.data.receiving.party.name","client 1");
        String beneficiaryBankSWIFTBIC = ConfigHelper.getString( "col.giro.dbs.data.beneficiar.bank","OCBCSGSGXXX");
        String transactionCode = ConfigHelper.getString( "col.giro.dbs.data.transaction.code","30");
        String ddaReference = ConfigHelper.getString("col.giro.dbs.data.dda.reference","TM199206031W");
        String paymentDetails =ConfigHelper.getString("col.giro.dbs.data.payment.details","M Log Trust");
        String purposeofPayment = ConfigHelper.getString("col.giro.dbs.data.purposeofpayment","SUPP");
        String deliveryMode = ConfigHelper.getString("col.giro.dbs.data.delivery.mode","");
        String email1 =ConfigHelper.getString("col.giro.dbs.data.email1","xxx@ecquaria.com");
        String phone1 = ConfigHelper.getString("col.giro.dbs.data.phonename1","88888888");
        for(GiroPaymentXmlDto giroPaymentXmlDto : giroPaymentXmlDtos){
            GiroGroupDataDto giroGroupDataDto = JsonUtil.parseToObject(giroPaymentXmlDto.getXmlData(),GiroGroupDataDto.class);
            String giroAccount = getGiroAccountByGroupNo(giroGroupDataDto.getAppGroupNo());
            if(!StringUtil.isEmpty(giroAccount)){
                InputDetailDto inputDetailDto = new InputDetailDto();
                inputDetailDto.setAppGroupNo(giroGroupDataDto.getAppGroupNo());
                inputDetailDto.setRecordType(recordTypeData);
                inputDetailDto.setProductType(productTypeData);
                inputDetailDto.setOriginatingAccountNumber(originatingAccountNumber);
                inputDetailDto.setOriginatingAccountCurrency(paymentCurrency);
                //confirm Not required at present
                inputDetailDto.setCustomerReferenceOrBatchReference("");
                inputDetailDto.setPaymentCurrency(paymentCurrency);
                inputDetailDto.setBatchID(giroPaymentXmlDto.getBatchId());
                inputDetailDto.setPaymentDate(newDateString);
                inputDetailDto.setBankCharges("");
                inputDetailDto.setDebitAccountforBankCharges("");
                inputDetailDto.setReceivingPartyName( receivingPartyName);
                inputDetailDto.setPayableTo("");
                inputDetailDto.setReceivingPartyAddress1("");
                inputDetailDto.setReceivingPartyAddress2("");
                inputDetailDto.setReceivingPartyAddress3("");
                inputDetailDto.setReceivingAccountNumber(giroAccount);
                inputDetailDto.setCountrySpecific("");
                inputDetailDto.setReceivingBankCode("");
                inputDetailDto. setReceivingBranchCode("");
                inputDetailDto. setClearingCode("");
                inputDetailDto.setBeneficiaryBankSWIFTBIC(beneficiaryBankSWIFTBIC);
                inputDetailDto.setBeneficiaryBankName("");
                inputDetailDto.setBeneficiaryBankAddress("");
                inputDetailDto.setBeneficiaryBankCountry("");
                inputDetailDto.setRoutingCode("");
                inputDetailDto.setIntermediaryBankSWIFTBIC("");
                inputDetailDto.setAmountCurrency("");
                inputDetailDto.setAmount(StringUtil.changeDoubleToStringForTwoDecimals(giroGroupDataDto.getResidualPayment()));
                inputDetailDto.setFxContractReference1("");
                inputDetailDto.setAmounttobeUtilized1("");
                inputDetailDto.setFxContractReference2("");
                inputDetailDto.setAmounttobeUtilized2 ("");
                inputDetailDto.setTransactionCode(transactionCode);
                inputDetailDto.setParticulars(giroGroupDataDto.getAppGroupNo()+ giroPaymentXmlDto.getBatchId());
                inputDetailDto.setDdaReference(ddaReference);
                inputDetailDto.setPaymentDetails(paymentDetails);
                inputDetailDto.setInstructiontoOrderingBank("");
                inputDetailDto.setBeneficiaryResidentStatus("");
                inputDetailDto.setBeneficiaryCategory("");
                inputDetailDto.setTransactionRelationship("");
                inputDetailDto.setPayeeRole("");
                inputDetailDto.setRemitterIdentity("");
                inputDetailDto.setPurposeofPayment(purposeofPayment);
                inputDetailDto.setSupplementaryInfo("");
                inputDetailDto.setDeliveryMode(deliveryMode);
                inputDetailDto.setPrintAtLocation("");
                inputDetailDto.setPayableLocation("");
                inputDetailDto.setMailtoPartyName("");
                inputDetailDto.setMailtoPartyAddress1("");
                inputDetailDto.setMailtoPartyAddress2("");
                inputDetailDto.setMailtoPartyAddress3("");
                inputDetailDto.setReservedField("");
                inputDetailDto.setPostalCode("");
                if("E".equalsIgnoreCase(inputDetailDto.getDeliveryMode())){
                    inputDetailDto.setEmail1(email1);
                }else {
                    inputDetailDto.setEmail1("");
                }
                inputDetailDto.setEmail2("");
                inputDetailDto.setEmail3("");
                inputDetailDto.setEmail4("");
                inputDetailDto.setEmail5("");
                if("F".equalsIgnoreCase(inputDetailDto.getDeliveryMode())){
                    inputDetailDto.setPhoneNumber1(phone1);
                }else {
                    inputDetailDto.setPhoneNumber1("");
                }
                inputDetailDto.setPhoneNumber2("");
                inputDetailDto.setPhoneNumber3("");
                inputDetailDto.setPhoneNumber4("");
                inputDetailDto.setPhoneNumber5("");
                if("E".equalsIgnoreCase(inputDetailDto.getDeliveryMode())){
                    InvoiceDetailsDto invoiceDetailsDto = new InvoiceDetailsDto();
                    //todo true data
                    invoiceDetailsDto.setSNo("001");
                    invoiceDetailsDto.setInvoiceNo("1010039098");
                    invoiceDetailsDto.setInvDate(newDateString);
                    invoiceDetailsDto.setAmount(giroGroupDataDto.getResidualPayment());
                    invoiceDetailsDto.setTotal(giroGroupDataDto.getResidualPayment());
                    inputDetailDto.setInvoiceDetails( invoiceDetailsDto.toString());
                }else {
                    inputDetailDto.setInvoiceDetails("");
                }
                inputDetailDto.setClientReference1("");
                inputDetailDto.setClientReference2("");
                inputDetailDto.setClientReference3("");
                inputDetailDto.setClientReference4("");
                INPUT_DETAIL.add(inputDetailDto);
                totalTransactionAmount = Calculator.add(totalTransactionAmount,giroGroupDataDto.getResidualPayment());
            }else {
                 totalNoOfTransactions--;
                log.info(StringUtil.changeForLog("----------appgroupno :"+ giroGroupDataDto.getAppGroupNo() + " is no grio accout -------------"));
            }
        }
        grioXmlPaymentDto.setINPUT_DETAIL(INPUT_DETAIL);
        inputTrailerDto.setTotalTransactionAmount(StringUtil.changeDoubleToStringForTwoDecimals(totalTransactionAmount));
        String recordTypeTra = ConfigHelper.getString("col.giro.dbs.trailer.record.type.dec","TRAILER");
        inputTrailerDto.setRecordType(  recordTypeTra);
        grioXmlPaymentDto.setINPUT_TRAILER(inputTrailerDto);
        return  grioXmlPaymentDto;
    }
    private InputHeaderDto getInputHeaderDto(){
        InputHeaderDto inputHeaderDto = new InputHeaderDto();
         String recordType = ConfigHelper.getString("col.giro.dbs.head.record.type.dec","HEADER");
        inputHeaderDto.setRecordType(recordType);
        String fileCreationDate = Formatter.formatDateTime(new Date(),Formatter.DATE_CMS_INTERFACE);
        inputHeaderDto.setFileCreationDate(fileCreationDate);
        String organizationID = ConfigHelper.getString("col.giro.organization.id","MIOFHE01");
        inputHeaderDto.setOrganizationID(organizationID);
        String senderName = ConfigHelper.getString("col.giro.sender.name","MINISTRY OF HEALTH");
        inputHeaderDto.setSenderName(senderName);
        return inputHeaderDto;
    }
    private String getGiroAccountByGroupNo(String groupNo){
        ApplicationGroupDto applicationGroupDto = applicationFeClient.getAppGrpByAppNo(groupNo+"-01").getEntity();
        if( applicationGroupDto == null){
            return "";
        }
        String licenseeId = applicationGroupDto.getLicenseeId();
        OrgGiroAccountInfoDto orgGiroAccountInfoDto = organizationLienceseeClient.getGiroAccByLicenseeId(licenseeId).getEntity();;
        if(orgGiroAccountInfoDto!= null && !StringUtil.isEmpty(orgGiroAccountInfoDto.getAcctNo())&& AppConsts.COMMON_STATUS_ACTIVE.equalsIgnoreCase(orgGiroAccountInfoDto.getStatus())){
            return orgGiroAccountInfoDto.getAcctNo();
        }
        return "";
    }
    private boolean genXmlFileToSftp(String xmlData,String fileName,String path){
        try{
            if( FileUtil.writeToFile(fileName,xmlData)){
                SFTPUtil.upload(fileName,path);
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
        log.info("------------getGiroXmlFromSftpAndSaveXml start ----------");
        if(!AppConsts.YES.equalsIgnoreCase(ConfigHelper.getString("pay.giro.switch"))) {
            log.info("pay.giro.switch is closed");
            return;
        }
        List<GiroPaymentXmlDto> giroPaymentXmlDtos =  appPaymentStatusClient.getGiroPaymentDtosByStatusAndXmlType(AppConsts.COMMON_STATUS_ACTIVE,ApplicationConsts.GIRO_SEND_XML_SFTP).getEntity();
          if(IaisCommonUtils.isEmpty( giroPaymentXmlDtos)){
              log.info("getGiroXmlFromSftpAndSaveXml is null");
              return;
          }
          for(GiroPaymentXmlDto giroPaymentXmlDto : giroPaymentXmlDtos){
              try{
                  String tag = giroPaymentXmlDto.getTag();
                  String fileName = tag;
                  String downPath = ApplicationConsts.GIRO_DOWN_FILE_PATH+ConfigHelper.getString("giro.sftp.linux.seperator");
                  String downloadfilefolder = ConfigHelper.getString("giro.sftp.downloadfilefolder");
                      List<String> remoteFileNames = SFTPUtil.getRemoteFileNames(fileName,downloadfilefolder);
                      if(IaisCommonUtils.isEmpty(remoteFileNames)){
                          log.info(StringUtil.changeForLog("----- SFTP NO FIND FILE LIKE "+ fileName +"-----------"));
                      }else {
                          for(String remoteFileName : remoteFileNames){
                              SFTPUtil.download(downPath,remoteFileName,downloadfilefolder);
                          }
                          boolean ack01Stfp = true;
                          boolean ack02Stfp = true;

                          String ack01 =  FileUtil.getContentByPostfixNotation(".ACK1",downPath,remoteFileNames);
                          String ack02 =  FileUtil.getContentByPostfixNotation(".ACK2",downPath,remoteFileNames);
                          if(StringUtil.isEmpty(ack01)){
                              ack01Stfp = false;
                          }
                          if(StringUtil.isEmpty(ack02)){
                              ack02Stfp = false;
                          }
                          //change get ack 01 ack 02
                          InputAck1Dto inputAck1Dto = new InputAck1Dto();
                          inputAck1Dto.setDtoByStringAck1(ack01,inputAck1Dto);
                          if(inputAck1Dto.getINPUT_HEADER() == null || GrioConsts.ACK1_GROUP_LEVEL_REJECTED_VALUE.equalsIgnoreCase(inputAck1Dto.getINPUT_HEADER().getGroupStatus())){
                              ack01Stfp = false;
                          }
                          InputAck2Or3Dto inputAck2Dto = new InputAck2Or3Dto();
                          inputAck2Dto.setDtoByStringAck(ack02, inputAck2Dto);
                          if(inputAck2Dto.getINPUT_HEADER() == null || GrioConsts.ACK2_GROUP_LEVEL_REJECTED_VALUE.equalsIgnoreCase(inputAck2Dto.getINPUT_HEADER().getGroupStatus())){
                              ack02Stfp = false;
                          }
                          if( ack01Stfp && ack02Stfp){
                              String ack03 =  FileUtil.getContentByPostfixNotation(".ACK3",downPath,remoteFileNames);
                              //change get ack 03
                              InputAck2Or3Dto inputAck3Dto = new InputAck2Or3Dto();
                              String ack03Xml = inputAck3Dto.setDtoByStringAck(ack03, inputAck3Dto);
                              List<InputDataAck2Or3Dto> DATAS = inputAck3Dto.getDATAS();
                              if( !IaisCommonUtils.isEmpty(DATAS)){
                                  boolean noRejectPending = true;
                                  for(InputDataAck2Or3Dto inputDataAck2Or3Dto : DATAS){
                                          if(GrioConsts. ACK3_TRANSACTION_LEVEL_PENDING_VALUE.equalsIgnoreCase(inputDataAck2Or3Dto.getTransactionStatus()) ||
                                                  GrioConsts.ACK3_TRANSACTION_LEVEL_REJECTED_VALUE.equalsIgnoreCase(inputDataAck2Or3Dto.getTransactionStatus())){
                                              noRejectPending = false;
                                              break;
                                          }
                                  }
                                  if(noRejectPending){
                                      saveXml(ack03Xml,AppConsts.COMMON_STATUS_ACTIVE,ApplicationConsts.GIRO_DOWN_XML_SFTP,tag);
                                      //save upload xml inactive
                                      giroPaymentXmlDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                                      giroPaymentXmlDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                                      appPaymentStatusClient.updateGiroPaymentXmlDto(giroPaymentXmlDto);
                                      //upload GiroPayment
                                      saveGiroPaymentDtosByDatas(DATAS);
                                      //upload app group
                                      saveAppGroupForTrue(DATAS);
                                  }
                              }
                          }else {
                              //fail need ranew gen uff file
                              giroPaymentXmlDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                              giroPaymentXmlDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                              appPaymentStatusClient.updateGiroPaymentXmlDto(giroPaymentXmlDto);
                              // Releasing the already inactive AppGroup
                              List<InputDataAck2Or3Dto> DATAS  =  inputAck2Dto.getDATAS();
                              reActiveAppGroup(DATAS);
                          }
                  }
              }catch (Exception e){
                  log.error(e.getMessage(),e);
              }
          }
          sysnSaveGroupToBe();
        log.info("------------getGiroXmlFromSftpAndSaveXml end ----------");
    }
    private void reActiveAppGroup(List<InputDataAck2Or3Dto> DATAS){
        if(!IaisCommonUtils.isEmpty(DATAS)){
            for(InputDataAck2Or3Dto inputDataAck2Or3Dto : DATAS){
                List<GiroPaymentXmlDto> giroPaymentXmlDtoList = appPaymentStatusClient.getGiroPaymentDtosByStatusAndXmlType(inputDataAck2Or3Dto.getCustomerReference().replace(inputDataAck2Or3Dto.getBatchId(),""),AppConsts.COMMON_STATUS_IACTIVE,ApplicationConsts.GIRO_NEED_GEN_XML).getEntity();
                for(GiroPaymentXmlDto giroPaymentXmlDto :giroPaymentXmlDtoList){
                    giroPaymentXmlDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    giroPaymentXmlDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    appPaymentStatusClient.updateGiroPaymentXmlDto(giroPaymentXmlDto);
                }
            }

        }
    }
    private List<GiroPaymentDto> saveGiroPaymentDtosByDatas(List<InputDataAck2Or3Dto> DATAS){
        List<GiroPaymentDto> giroPaymentDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(DATAS)){
            for(InputDataAck2Or3Dto inputDataAck2Or3Dto : DATAS){
                GiroPaymentDto giroPaymentDto = new GiroPaymentDto();
                giroPaymentDto.setPmtStatus(AppConsts.COMMON_STATUS_ACTIVE);
                giroPaymentDto.setPmtType(ApplicationConsts.GIRO_BANK_PAYMENT_TYPE_MONEYPAY);
                giroPaymentDto.setAppGroupNo(inputDataAck2Or3Dto.getCustomerReference().replace(inputDataAck2Or3Dto.getBatchId(),""));
                giroPaymentDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                giroPaymentDto.setAmount(Double.valueOf(inputDataAck2Or3Dto.getAmount()));
                //save giroPaymentDto
                appPaymentStatusClient.updateGiroPaymentDto(giroPaymentDto);
                giroPaymentDtos.add(giroPaymentDto);
            }
        }
        return  giroPaymentDtos;
    }
    private void saveAppGroupForTrue(List<InputDataAck2Or3Dto> DATAS){
        if(!IaisCommonUtils.isEmpty(DATAS)){
            List<String> appGNos = IaisCommonUtils.genNewArrayList();
            for(InputDataAck2Or3Dto inputDataAck2Or3Dto : DATAS){
                String appGNo  = inputDataAck2Or3Dto.getCustomerReference().replace(inputDataAck2Or3Dto.getBatchId(),"") ;
                if(!appGNos.contains(appGNo)){
                    appGNos.add(appGNo);
                    ApplicationGroupDto applicationGroupDto = applicationFeClient.getAppGrpByAppNo(appGNo+"-01").getEntity();
                    upDateForAppGroupForPaySuccess(applicationGroupDto);
                }
            }
        }
    }
    private void upDateForAppGroupForPaySuccess( ApplicationGroupDto applicationGroupDto){
        List<GiroPaymentDto> giroPaymentDtos = appPaymentStatusClient.getGiroPaymentDtosByPmtStatusAndAppGroupNo(AppConsts.COMMON_STATUS_ACTIVE,applicationGroupDto.getGroupNo()).getEntity();
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
                //sysn be create need sysn appgroup in xml
                saveXml( JsonUtil.parseToJson(applicationGroupDto),AppConsts.COMMON_STATUS_ACTIVE,ApplicationConsts.GIRO_PAY_SUCCESS_SYSN_BE,applicationGroupDto.getGroupNo());
            }
        }
    }
    @Override
    public void sysnSaveGroupToBe(){
        log.info(" sysnSaveGroupToBe start");
        List<GiroPaymentXmlDto> giroPaymentXmlDtos =  appPaymentStatusClient.getGiroPaymentDtosByStatusAndXmlType(AppConsts.COMMON_STATUS_ACTIVE,ApplicationConsts.GIRO_PAY_SUCCESS_SYSN_BE).getEntity();
        if(IaisCommonUtils.isEmpty(giroPaymentXmlDtos)){
            return;
        }
        for(GiroPaymentXmlDto giroPaymentXmlDto : giroPaymentXmlDtos){
         ApplicationGroupDto applicationGroupDto =  JsonUtil.parseToObject(giroPaymentXmlDto.getXmlData(), ApplicationGroupDto.class);
         applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
         //sysn be
            String sysn = AppConsts.NO;
         try{
             sysn = saveAppGroupGiroSysnEic(applicationGroupDto);
         }catch (Exception e){
             log.error(e.getMessage(),e);
         }
         if( AppConsts.YES.equalsIgnoreCase(sysn)){
             giroPaymentXmlDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
             giroPaymentXmlDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
             appPaymentStatusClient.updateGiroPaymentXmlDto(giroPaymentXmlDto);
         }
       }
        log.info(" sysnSaveGroupToBe end");
    }
    @Override
    public String saveAppGroupGiroSysnEic(ApplicationGroupDto applicationGroupDto){
        try {
            EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, ServiceConfigServiceImpl.class.getName(),
                    "saveAppGroupGiroSysnEic",currentApp + "-" + currentDomain
                    , ApplicationGroupDto.class.getName(), JsonUtil.parseToJson(applicationGroupDto));
            String eicRefNo = eicRequestTrackingDto.getRefNo();
            String saveSuccess = feEicGatewayClient.saveAppGroupSysnEic(applicationGroupDto).getEntity();
            //get eic record
            eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
            //update eic record status
            eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
            List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
            eicRequestTrackingDtos.add(eicRequestTrackingDto);
            appEicClient.updateStatus(eicRequestTrackingDtos);
            return saveSuccess;
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return AppConsts.NO;
    }

    @Override
    public List<HcsaServiceCorrelationDto> getActiveSvcCorrelation() {
        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtos = appConfigClient.getActiveSvcCorrelation().getEntity();
        List<HcsaServiceCorrelationDto> newHcsaServiceCorrelationDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(hcsaServiceCorrelationDtos)){
            List<String> baseSpecIdList = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtos){
                String baseSpecId = hcsaServiceCorrelationDto.getBaseSvcId() + hcsaServiceCorrelationDto.getSpecifiedSvcId();
                if(!baseSpecIdList.contains(baseSpecId)){
                    newHcsaServiceCorrelationDtos.add(hcsaServiceCorrelationDto);
                    baseSpecIdList.add(baseSpecId);
                }
            }
        }

        return newHcsaServiceCorrelationDtos;
    }

    @Override
    public List<HcsaSvcSubtypeOrSubsumedDto> getSvcSubtypeOrSubsumedByIdList(List<String> idList) {
        return appConfigClient.getSvcSubtypeOrSubsumedByIdList(idList).getEntity();
    }

    @Override
    public List<HcsaServiceDto> getActiveHcsaSvcByNames(List<String> names) {
        List<HcsaServiceDto> result = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceDto> hcsaServiceDtos = appConfigClient.getHcsaServiceByNames(names).getEntity();
        if(!IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                if(AppConsts.COMMON_STATUS_ACTIVE.equals(hcsaServiceDto.getStatus())){
                    result.add(hcsaServiceDto);
                }
            }
        }
        return result;
    }


    @Override
    public HcsaServiceDto getServiceDtoById(String id) {
        return hcsaConfigFeClient.getServiceDtoById(id).getEntity();
    }

    private GiroPaymentXmlDto saveXml(String xml,String status,String type,String tag){
        GiroPaymentXmlDto giroPaymentXmlDto = new GiroPaymentXmlDto();
        giroPaymentXmlDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        giroPaymentXmlDto.setXmlData(xml);
        giroPaymentXmlDto.setXmlType(type);
        giroPaymentXmlDto.setTag(tag);
        giroPaymentXmlDto.setStatus(status);
       return appPaymentStatusClient.updateGiroPaymentXmlDto(giroPaymentXmlDto).getEntity();
    }
}
