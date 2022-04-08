package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.helper.ConfigHelper;
import com.ecquaria.cloud.moh.iais.api.util.FileUtil;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.grio.GrioConsts;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.Ack1.InputAck1Dto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.Ack1.InputHeaderAck1Dto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.Ack2OrAck3.InputAck2Or3Dto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.Ack2OrAck3.InputDataAck2Or3Dto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.Ack2OrAck3.InputHeaderAck2Or3Dto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.GiroDataXmlDto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.GiroGroupDataDto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.GiroPaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.GiroPaymentSendGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.GiroPaymentXmlDto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.GiroXmlPaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.InputDetailDto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.InputHeaderDto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.InputTrailerDto;
import com.ecquaria.cloud.moh.iais.common.dto.GrioXml.InvoiceDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SpecicalPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.XmlBindUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPaymentStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigFeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.sz.commons.util.Calculator;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;

    @Autowired
    private AppSubmissionService appSubmissionService;
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
        return feEicGatewayClient.getPostalCode(postalCode).getEntity();
    }

    @Override
    public String getSvcIdBySvcCode(String svcCode) {
        Map<String,Object> map = IaisCommonUtils.genNewHashMap();
        map.put("code", svcCode);

        return   appConfigClient.getServiceIdByCode(svcCode).getEntity();
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
        if (StringUtil.isEmpty(serviceId)) {
            return new ArrayList<>();
        }
        List<HcsaServiceStepSchemeDto> stepDtos = appConfigClient.getServiceStepsByServiceId(serviceId).getEntity();
        if (stepDtos != null && !stepDtos.isEmpty()) {
            stepDtos.stream()
                    .filter(dto -> HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(dto.getStepCode()))
                    .forEach(dto -> dto.setStepName(HcsaConsts.CLINICAL_DIRECTORS));
        }
        return stepDtos;
    }

    @Override
    public HcsaServiceStepSchemeDto getHcsaServiceStepSchemeByConds(String serviceId, String stepCode) {
        if (StringUtil.isEmpty(serviceId) || StringUtil.isEmpty(stepCode)) {
            return null;
        }
        List<HcsaServiceStepSchemeDto> stepDtos = appConfigClient.getServiceStepsByServiceId(serviceId).getEntity();
        if (stepDtos == null || stepDtos.isEmpty()) {
            return null;
        }
        return stepDtos.stream()
                .filter(dto -> stepCode.equals(dto.getStepCode()))
                .findAny()
                .orElse(null);
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
                }else if(HcsaConsts.STEP_VEHICLES.equals(stepCode)){
                    psnTypes.add(ApplicationConsts.PERSONNEL_VEHICLES);
                }else if(HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(stepCode)){
                    psnTypes.add(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR);
                }else if(HcsaConsts.STEP_CHARGES.equals(stepCode)){
                    psnTypes.add(ApplicationConsts.PERSONNEL_CHARGES);
                }else if(HcsaConsts.STEP_CHARGES_OTHER.equals(stepCode)){
                    psnTypes.add(ApplicationConsts.PERSONNEL_CHARGES_OTHER);
                } else if (HcsaConsts.STEP_SECTION_LEADER.equals(stepCode)) {
                    psnTypes.add(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
                } else if (HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER.equals(stepCode)) {
                    psnTypes.add(ApplicationConsts.PERSONNEL_PSN_KAH);
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
        try {
            publicHolidayDtoList = feEicGatewayClient.getpublicHoliday().getEntity();
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
    public HcsaServiceDto getActiveHcsaServiceDtoById(String serviceId){
       return appConfigClient.getActiveHcsaServiceDtoById(serviceId).getEntity();
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
         GiroPaymentXmlDto giroPaymentXmlDto = genGiroPaymentXmlDtoByAppGrp(appGrp);
         appPaymentStatusClient.createNewGiroPaymentXmlDto(giroPaymentXmlDto);
         return appGrp;
    }

    @Override
    public ApplicationGroupDto updateAppGrpPmtStatus(ApplicationGroupDto applicationGroupDto, String giroAccNo) {
        return applicationFeClient.updateAppGrpPmtStatus(applicationGroupDto, giroAccNo).getEntity();
    }

    private String genGiroTranNo(){
       return "GIROTRANS-"+Formatter.formatDateTime(new Date(),Formatter.DATE_REF_NUMBER).replace("_","");
    }
    private GiroPaymentXmlDto genGiroPaymentXmlDtoByAppGrp(AppSubmissionDto appGrp){
        GiroPaymentXmlDto giroPaymentXmlDto = new GiroPaymentXmlDto();
        giroPaymentXmlDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        String tranNo = genGiroTranNo();
        appGrp.setGiroTranNo(tranNo);
        GiroGroupDataDto giroPaymentDto = new GiroGroupDataDto();
        Double amount = (appGrp.getTotalAmountGroup() == null) ? appGrp.getAmount() : appGrp.getTotalAmountGroup();
        giroPaymentDto.setAmount(amount);
        giroPaymentDto.setResidualPayment(amount);
        giroPaymentDto.setAppGroupNo(appGrp.getAppGrpNo());
        giroPaymentXmlDto.setTag(appGrp.getAppGrpNo());
        giroPaymentDto.setGiroTranNo(tranNo);
        giroPaymentXmlDto.setXmlData(JsonUtil.parseToJson(giroPaymentDto));
        giroPaymentXmlDto.setXmlType(ApplicationConsts.GIRO_NEED_GEN_XML);
        giroPaymentXmlDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        //set giro account info
        List<String> accInfoList = getGiroAccountAndBICByLicId(appGrp.getLicenceId());
        if(IaisCommonUtils.isNotEmpty(accInfoList)){
            for(int i =0; i< accInfoList.size();i++){
                if(i==0){
                    giroPaymentXmlDto.setBankNo(accInfoList.get(i));
                }else if(i==1){
                    giroPaymentXmlDto.setSwiftBic(accInfoList.get(i));
                }else if(i==2){
                    giroPaymentXmlDto.setDdaRefNo(accInfoList.get(i));
                }
            }
        }
        return giroPaymentXmlDto;
    }

    @Override
    public void sendGiroXmlToSftp() {
        log.info("-------sendGiroXmlToSftp start---------");
       if(!AppConsts.YES.equalsIgnoreCase(ConfigHelper.getString("pay.giro.switch"))) {
           log.info("pay.giro.switch is closed");
           return;
       }
        changeOldGiroData();
        List<GiroPaymentXmlDto> giroPaymentXmlDtos =  appPaymentStatusClient.getGiroPaymentDtosByStatusAndXmlType(AppConsts.COMMON_STATUS_ACTIVE,ApplicationConsts.GIRO_NEED_GEN_XML).getEntity();
       if(IaisCommonUtils.isEmpty(giroPaymentXmlDtos)){
          return;
      }
        List<GiroPaymentXmlDto> giroPaymentXmlDtosGen = IaisCommonUtils.genNewArrayList();
        Map<String,String> mapTrans = IaisCommonUtils.genNewHashMap();
      for(GiroPaymentXmlDto giroPaymentXmlDto : giroPaymentXmlDtos){
          GiroGroupDataDto giroGroupDataDto = JsonUtil.parseToObject(giroPaymentXmlDto.getXmlData(),GiroGroupDataDto.class);
          mapTrans.put(giroGroupDataDto.getAppGroupNo(),giroGroupDataDto.getGiroTranNo());
          List<GiroPaymentDto> giroPaymentDtos = appPaymentStatusClient.getGiroPaymentDtosByPmtStatusAndAppGroupNo(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS,giroGroupDataDto.getAppGroupNo(),AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY).getEntity();
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
        GiroXmlPaymentDto grioXmlPaymentDto = getGiroXmlPaymentDtoByGiroPaymentXmlDtos(giroPaymentXmlDtosGen);
        if( grioXmlPaymentDto.getINPUT_TRAILER().getTotalNoOfTransactions() <= 0 ){
            log.info("-------- no find data need gen file ---------");
            return;
        }
        String xml = genXmlByGiroPaymentXmlDtos(grioXmlPaymentDto);
        String uploadGrioFileData = dbsFileDataByGiroXmlPaymentDto(grioXmlPaymentDto);
        String tag = genFileNameUploadSFTP();
        String path = ConfigHelper.getString("giro.sftp.uploadfilefolder",ApplicationConsts.GIRO_UPLOAD_FILE_PATH);
        String fileName =  path+ConfigHelper.getString("giro.sftp.linux.seperator")+ tag;
        if(genXmlFileToSftp(uploadGrioFileData,fileName, path)){
            createGiroPaymentSendGroupDto(tag,xml,grioXmlPaymentDto,mapTrans,giroPaymentXmlDtosGen);
            genFakeAck(grioXmlPaymentDto,tag);
        }
        log.info("-------sendGiroXmlToSftp end---------");
    }
    private void changeOldGiroData(){
        if(ConfigHelper.getBoolean("giro.old.data.change.flag",true) && !appPaymentStatusClient.checkOldGiroDataToNewData().getEntity()){
         List<String> groupNos = appPaymentStatusClient.getGroupNosFromOldGiro().getEntity();
         Map<String,List<String>> groupNoGiroAccMap = IaisCommonUtils.genNewHashMap();
         if(IaisCommonUtils.isNotEmpty(groupNos)){
             groupNos.stream().forEach(groupNo -> groupNoGiroAccMap.put(groupNo,getGiroAccountAndBICByGroupNo(groupNo)));
         }
         appPaymentStatusClient.changeOldGiroDataToNewData(groupNoGiroAccMap);
        }
    }

    private void createGiroPaymentSendGroupDto(String tag,String xml,GiroXmlPaymentDto grioXmlPaymentDto,Map<String,String> mapTrans,List<GiroPaymentXmlDto> giroPaymentXmlDtosGen){
        log.info(StringUtil.changeForLog("-----------createGiroPaymentSendGroupDto tag :" + tag +"---------------------------------"));
        GiroPaymentSendGroupDto giroPaymentSendGroupDto = new GiroPaymentSendGroupDto();
        GiroDataXmlDto giroDataXmlDto = new GiroDataXmlDto();
        giroDataXmlDto.setFileName(tag);
        giroDataXmlDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        giroDataXmlDto.setFileType(ApplicationConsts.GIRO_SEND_XML_SFTP);
        giroDataXmlDto.setXmlData(xml);
        giroPaymentSendGroupDto.setGiroDataXmlDto(giroDataXmlDto);
        giroPaymentSendGroupDto.setGiroPaymentXmlDtosGen(giroPaymentXmlDtosGen);
        giroPaymentSendGroupDto.setGiroPaymentDtos(getGiroPaymentDtosByInputDetailDtos(grioXmlPaymentDto.getINPUT_DETAIL(),mapTrans,giroPaymentXmlDtosGen));
        appPaymentStatusClient.createGiroPaymentSendGroupDto(giroPaymentSendGroupDto);
    }
    private List<GiroPaymentDto> getGiroPaymentDtosByInputDetailDtos(List<InputDetailDto> INPUT_DETAIL,Map<String,String> mapTrans,List<GiroPaymentXmlDto> giroPaymentXmlDtosGen){
        List<GiroPaymentDto> giroPaymentDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(INPUT_DETAIL)) {
            int i = 0;
            for (InputDetailDto inputDetailDto : INPUT_DETAIL) {
                GiroPaymentDto giroPaymentDto =  giroPaymentXmlDtosGen.get(i).getGiroPaymentDto() != null ?giroPaymentXmlDtosGen.get(i).getGiroPaymentDto() : new GiroPaymentDto();
                giroPaymentDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_GIRO);
                giroPaymentDto.setPmtType(ApplicationConsts.GIRO_BANK_PAYMENT_TYPE_MONEYPAY);
                giroPaymentDto.setAppGroupNo(inputDetailDto.getAppGroupNo());
                giroPaymentDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                giroPaymentDto.setAmount(Double.valueOf(inputDetailDto.getAmount()));
                giroPaymentDto.setAcctNo(inputDetailDto.getReceivingAccountNumber());
                giroPaymentDto.setPayDesc(inputDetailDto.getPaymentDetails());
                giroPaymentDto.setInvoiceNo(inputDetailDto.getInvoiceNo());
                giroPaymentDto.setGiroStatus(ApplicationConsts.GIRO_OUTGOING_TYPE);
                String txnRefNo = mapTrans.get(inputDetailDto.getAppGroupNo());
                if(!StringUtil.isEmpty(txnRefNo)){
                    giroPaymentDto.setTxnRefNo(txnRefNo);
                }
                giroPaymentDtos.add(giroPaymentDto);
                i++;
            }
        }
        return giroPaymentDtos;
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
        stringBuilder.append(inputHeaderAck1Dto).append(INPUT_TRAILER);
        String path = ConfigHelper.getString("giro.sftp.downloadfilefolder",ApplicationConsts.GIRO_DOWN_FILE_PATH);
        String fileName = path +ConfigHelper.getString("giro.sftp.linux.seperator")+ tag + '.'+ dateString + '.'+"ACK1";
        return genXmlFileToSftp(stringBuilder.toString(),fileName,path);
    }
    private boolean genAck02File(GiroXmlPaymentDto grioXmlPaymentDto,String tag){
        String dateString =  Formatter.formatDateTime(new Date(),Formatter.DATE_FILE_NAME);
        InputHeaderAck2Or3Dto inputHeaderAck2Dto = new InputHeaderAck2Or3Dto("HEADER", "D000001554503310",tag, dateString, "MINISTRY OF HEALTH","MIOFHE01", "ACTC","");
        InputTrailerDto INPUT_TRAILER = grioXmlPaymentDto.getINPUT_TRAILER();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(inputHeaderAck2Dto);
        List<InputDetailDto> INPUT_DETAIL = grioXmlPaymentDto.getINPUT_DETAIL();
        if(!IaisCommonUtils.isEmpty(INPUT_DETAIL)){
            for(InputDetailDto inputDetailDto : INPUT_DETAIL){
                InputDataAck2Or3Dto inputDataAck2Or3Dto = new InputDataAck2Or3Dto("DATA", "COL", "0010521098", "SGD","SGD", inputDetailDto.getParticulars(), inputDetailDto.getPaymentDate(), "client 1", "OCBCSGSGXXX", "SGD", inputDetailDto.getAmount(), "ACWC", "", inputDetailDto.getBatchID(), "", "0", inputDetailDto.getReceivingAccountNumber());
                stringBuilder.append(inputDataAck2Or3Dto);
            }
        }
        stringBuilder.append(INPUT_TRAILER);
        String path = ConfigHelper.getString("giro.sftp.downloadfilefolder",ApplicationConsts.GIRO_DOWN_FILE_PATH);
        String fileName = path+ConfigHelper.getString("giro.sftp.linux.seperator")+ tag + '.'+ dateString + '.'+"ACK2";
        return genXmlFileToSftp(stringBuilder.toString(),fileName,path);
    }
    private boolean genAck03File(GiroXmlPaymentDto grioXmlPaymentDto,String tag){
        String dateString =  Formatter.formatDateTime(new Date(),Formatter.DATE_FILE_NAME);
        InputHeaderAck2Or3Dto inputHeaderAck3Dto = new InputHeaderAck2Or3Dto("HEADER", "D000001554503310",tag, dateString, "MINISTRY OF HEALTH","MIOFHE01", "ACSP","");
        InputTrailerDto INPUT_TRAILER = grioXmlPaymentDto.getINPUT_TRAILER();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(inputHeaderAck3Dto);
        List<InputDetailDto> INPUT_DETAIL = grioXmlPaymentDto.getINPUT_DETAIL();
        if(!IaisCommonUtils.isEmpty(INPUT_DETAIL)){
            for(InputDetailDto inputDetailDto : INPUT_DETAIL){
                InputDataAck2Or3Dto inputDataAck2Or3Dto = new InputDataAck2Or3Dto("DATA", "COL", "0010521098", "SGD","SGD", inputDetailDto.getParticulars(),inputDetailDto.getPaymentDate() , "client 1", "OCBCSGSGXXX", "SGD", inputDetailDto.getAmount(), "ACCP", "", inputDetailDto.getBatchID(), "", "", "");
                stringBuilder.append(inputDataAck2Or3Dto);
            }
        }
        stringBuilder.append(INPUT_TRAILER);
        String path = ConfigHelper.getString("giro.sftp.downloadfilefolder",ApplicationConsts.GIRO_DOWN_FILE_PATH);
        String fileName = path +ConfigHelper.getString("giro.sftp.linux.seperator")+ tag + '.'+ dateString + '.'+"ACK3";
        return genXmlFileToSftp(stringBuilder.toString(),fileName,path);
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
        stringBuilder.append(grioXmlPaymentDto.getINPUT_HEADER());
        List<InputDetailDto> inputDetailDtos = grioXmlPaymentDto.getINPUT_DETAIL();
        if(!IaisCommonUtils.isEmpty(inputDetailDtos)){
            for(InputDetailDto inputDetailDto : inputDetailDtos){
                stringBuilder.append(inputDetailDto);
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
        double totalTransactionAmount = 0.0;
        String recordTypeData = ConfigHelper.getString("col.giro.dbs.data.record.type.dec","PAYMENT");
        String productTypeData =  ConfigHelper.getString("col.giro.dbs.data.product.type","COL");
        String paymentCurrency =  ConfigHelper.getString( "col.giro.dbs.data.account.currency","SGD");
        String originatingAccountNumber = ConfigHelper.getString("col.giro.dbs.data.account.originatingAccountNumber","0010521098");
        String newDateString   = Formatter.formatDateTime(new Date(),Formatter.DATE_CMS_INTERFACE);
        String receivingPartyName = ConfigHelper.getString( "col.giro.dbs.data.receiving.party.name","client 1");
        String transactionCode = ConfigHelper.getString( "col.giro.dbs.data.transaction.code","30");
        String paymentDetails =ConfigHelper.getString("col.giro.dbs.data.payment.details","M Log Trust");
        String purposeofPayment = ConfigHelper.getString("col.giro.dbs.data.purposeofpayment","SUPP");
        String deliveryMode = ConfigHelper.getString("col.giro.dbs.data.delivery.mode","");
        String email1 =ConfigHelper.getString("col.giro.dbs.data.email1","xxx@ecquaria.com");
        String phone1 = ConfigHelper.getString("col.giro.dbs.data.phonename1","88888888");
        InvoiceDetailsDto invoiceDetailsDto = new InvoiceDetailsDto();
        List<GiroPaymentXmlDto> giroPaymentXmlDtoNoneffectiveList = IaisCommonUtils.genNewArrayList();
        for(GiroPaymentXmlDto giroPaymentXmlDto : giroPaymentXmlDtos){
            GiroGroupDataDto giroGroupDataDto = JsonUtil.parseToObject(giroPaymentXmlDto.getXmlData(),GiroGroupDataDto.class);
            List<String> accountBicList = getGiroAccountAndBICByGroupNo(giroGroupDataDto.getAppGroupNo());
            if(IaisCommonUtils.isNotEmpty(accountBicList) && StringUtil.isNotEmpty(accountBicList.get(1))){
                String giroAccount = accountBicList.get(0);
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
                inputDetailDto.setReceivingBranchCode("");
                inputDetailDto.setClearingCode("");
                inputDetailDto.setBeneficiaryBankSWIFTBIC(accountBicList.get(1));
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
                inputDetailDto.setDdaReference(accountBicList.get(2));
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
                    //todo true data
                    String inNo = "1010039098";
                    invoiceDetailsDto.setSNo("001");
                    invoiceDetailsDto.setInvoiceNo(inNo);
                    invoiceDetailsDto.setInvDate(newDateString);
                    invoiceDetailsDto.setAmount(giroGroupDataDto.getResidualPayment());
                    invoiceDetailsDto.setTotal(giroGroupDataDto.getResidualPayment());
                    inputDetailDto.setInvoiceNo(inNo);
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
                giroPaymentXmlDtoNoneffectiveList.add(giroPaymentXmlDto);
                log.info(StringUtil.changeForLog("----------appgroupno :"+ giroGroupDataDto.getAppGroupNo() + " is no grio accout or bankcode is illegal-------------"));
            }
        }
        if(IaisCommonUtils.isNotEmpty(giroPaymentXmlDtoNoneffectiveList)){
            giroPaymentXmlDtos.removeAll(giroPaymentXmlDtoNoneffectiveList);
        }
        grioXmlPaymentDto.setINPUT_DETAIL(INPUT_DETAIL);
        inputTrailerDto.setTotalNoOfTransactions(totalNoOfTransactions);
        inputTrailerDto.setTotalTransactionAmount(StringUtil.changeDoubleToStringForTwoDecimals(totalTransactionAmount));
        String recordTypeTra = ConfigHelper.getString("col.giro.dbs.trailer.record.type.dec","TRAILER");
        inputTrailerDto.setRecordType(recordTypeTra);
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

    @Override
    public String getGiroAccountByGroupNo(String groupNo){

        ApplicationDto applicationDto = applicationFeClient.getApplicationDtoByAppNo(groupNo+"-01").getEntity();
        if( applicationDto == null){
            return "";
        }
        if(StringUtil.isNotEmpty(applicationDto.getOriginLicenceId())){
            List<String> licIds = Arrays.asList(applicationDto.getOriginLicenceId());
            List<GiroAccountInfoDto> giroAccountInfoDtos = licenceClient.getGiroAccountsByLicIds(licIds).getEntity();
            GiroAccountInfoDto orgGiroAccountInfoDto = null;
            if(IaisCommonUtils.isNotEmpty(giroAccountInfoDtos)){
                orgGiroAccountInfoDto = giroAccountInfoDtos.get(0);
            }
            if(orgGiroAccountInfoDto!= null && !StringUtil.isEmpty(orgGiroAccountInfoDto.getAcctNo())&& AppConsts.COMMON_STATUS_ACTIVE.equalsIgnoreCase(orgGiroAccountInfoDto.getStatus())){
                return orgGiroAccountInfoDto.getAcctNo();
            }else if(orgGiroAccountInfoDto!= null && StringUtil.isEmpty(orgGiroAccountInfoDto.getAcctNo())){
                return  ConfigHelper.getString("col.giro.test.account","");
            }
        }


        return "";
    }

    private List<String> getGiroAccountAndBICByGroupNo(String groupNo){
        ApplicationDto applicationDto = applicationFeClient.getApplicationDtoByAppNo(groupNo+"-01").getEntity();
        if( applicationDto == null){
            return null;
        }
        return getGiroAccountAndBICByLicId(applicationDto.getOriginLicenceId());
    }


    private List<String> getGiroAccountAndBICByLicId(String licId){
        if(StringUtil.isNotEmpty(licId)){
            List<String> licIds = Arrays.asList(licId);
            List<GiroAccountInfoDto> giroAccountInfoDtos = licenceClient.getGiroAccountsByLicIds(licIds).getEntity();
            GiroAccountInfoDto orgGiroAccountInfoDto = null;
            if(IaisCommonUtils.isNotEmpty(giroAccountInfoDtos)){
                orgGiroAccountInfoDto = giroAccountInfoDtos.get(0);
            }
            if(orgGiroAccountInfoDto!= null && !StringUtil.isEmpty(orgGiroAccountInfoDto.getAcctNo())&& AppConsts.COMMON_STATUS_ACTIVE.equalsIgnoreCase(orgGiroAccountInfoDto.getStatus())){
                // index 2 : dda refNo
                return Arrays.asList(orgGiroAccountInfoDto.getAcctNo(),MasterCodeUtil.getDecByCateIdAndCodeValue(MasterCodeUtil.CATE_ID_GIRO_BANK_CODE,orgGiroAccountInfoDto.getBankCode()),StringUtil.getNonNull(orgGiroAccountInfoDto.getCustomerReferenceNo()));
            }else if(orgGiroAccountInfoDto!= null && StringUtil.isEmpty(orgGiroAccountInfoDto.getAcctNo())){
                log.info(StringUtil.changeForLog("--------giro account is null------------"));
            }else {
                log.info(StringUtil.changeForLog("-------- giro account status is inactive or null-----------"));
            }
        }
        return null;
    }


    private boolean genXmlFileToSftp(String xmlData,String fileName,String path){
        try{
            if(!StringUtil.isEmpty(path)){
                if( FileUtil.writeToFile(fileName,xmlData)){
                    // SFTPUtil.upload(fileName,path);
                    return true;
                }
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
              sysnSaveGroupToBe();
              return;
          }
          for(GiroPaymentXmlDto giroPaymentXmlDto : giroPaymentXmlDtos){
              try{
                  String tag = giroPaymentXmlDto.getTag();
                  String fileName = tag;
                  String downloadfilefolder = ConfigHelper.getString("giro.sftp.downloadfilefolder",ApplicationConsts.GIRO_DOWN_FILE_PATH);
                  log.info(StringUtil.changeForLog("-----downloadfilefolder :" + downloadfilefolder+"-----------"));
                  String downPath = downloadfilefolder + ConfigHelper.getString("giro.sftp.linux.seperator");
                  log.info(StringUtil.changeForLog("-----downPath :" + downPath +"-----------"));
                      List<String> remoteFileNames = FileUtil.getRemoteFileNames(fileName,downloadfilefolder);
                      if(IaisCommonUtils.isEmpty(remoteFileNames)){
                          log.info(StringUtil.changeForLog("----- SFTP NO FIND FILE LIKE "+ fileName +"-----------"));
                      }else {
                          log.info(StringUtil.changeForLog("--------- find file tag:" + tag));
                          log.info(StringUtil.changeForLog("---remoteFileNames is "+ remoteFileNames));
                          if(FileUtil.checkFileNamesExistKeyWords(remoteFileNames,".ACK1",".ACK2")){
                              boolean ack01Stfp = true;
                              boolean ack02Stfp = true;

                              String ack01 =  FileUtil.getContentByPostfixNotation(".ACK1",downPath,remoteFileNames);
                              String ack02 =  FileUtil.getContentByPostfixNotation(".ACK2",downPath,remoteFileNames);
                              if(StringUtil.isEmpty(ack01)){
                                  ack01Stfp = false;
                              }else{
                                  log.info(StringUtil.changeForLog("--------ACK01 :" + ack01));
                              }
                              if(StringUtil.isEmpty(ack02)){
                                  ack02Stfp = false;
                              }else {
                                  log.info(StringUtil.changeForLog("--------ACK02 :" + ack02));
                              }
                              //change get ack 01 ack 02
                              InputAck1Dto inputAck1Dto = new InputAck1Dto();
                              inputAck1Dto.setDtoByStringAck1(ack01,inputAck1Dto);
                              if(inputAck1Dto.getINPUT_HEADER() == null || GrioConsts.ACK1_GROUP_LEVEL_REJECTED_VALUE.equalsIgnoreCase(inputAck1Dto.getINPUT_HEADER().getGroupStatus())){
                                  ack01Stfp = false;
                              }
                              saveAck(ack01,ApplicationConsts.GIRO_GET_ACK1_TYPE,tag,FileUtil.getFileName(remoteFileNames,".ACK1"),giroPaymentXmlDto.getXmlData(),null,inputAck1Dto.getINPUT_HEADER()!= null ? inputAck1Dto.getINPUT_HEADER().getGroupStatus() : null);
                              log.info(StringUtil.changeForLog("--------save ACK01 ok ,tag :" + tag+"------------------"));
                              InputAck2Or3Dto inputAck2Dto = new InputAck2Or3Dto();
                              inputAck2Dto.setDtoByStringAck(ack02, inputAck2Dto);
                              if(inputAck2Dto.getINPUT_HEADER() == null || GrioConsts.ACK2_GROUP_LEVEL_REJECTED_VALUE.equalsIgnoreCase(inputAck2Dto.getINPUT_HEADER().getGroupStatus())){
                                  ack02Stfp = false;
                              }
                              saveAck(ack02,ApplicationConsts.GIRO_GET_ACK2_TYPE,tag,FileUtil.getFileName(remoteFileNames,".ACK2"),giroPaymentXmlDto.getXmlData(),inputAck2Dto.getDATAS(),inputAck2Dto.getINPUT_HEADER() !=null ? inputAck2Dto.getINPUT_HEADER().getGroupStatus() : null);
                              log.info(StringUtil.changeForLog("--------save ACK02 ok ,tag :" + tag+"------------------"));
                              if( ack01Stfp && ack02Stfp){
                                  if(FileUtil.checkFileNamesExistKeyWords(remoteFileNames,".ACK3")){
                                      String ack03 =  FileUtil.getContentByPostfixNotation(".ACK3",downPath,remoteFileNames);
                                      log.info(StringUtil.changeForLog("--------ACK03 :" + ack03));
                                      //change get ack 03
                                      InputAck2Or3Dto inputAck3Dto = new InputAck2Or3Dto();
                                      String ack03Xml = inputAck3Dto.setDtoByStringAck(ack03, inputAck3Dto);
                                      saveAck(ack03,ApplicationConsts.GIRO_GET_ACK3_TYPE,tag,FileUtil.getFileName(remoteFileNames,".ACK3"),giroPaymentXmlDto.getXmlData(),inputAck3Dto.getDATAS(),inputAck3Dto.getINPUT_HEADER().getGroupStatus());
                                      log.info(StringUtil.changeForLog("--------save ACK03 ok ,tag :" + tag+"------------------"));
                                      List<InputDataAck2Or3Dto> DATAS = inputAck3Dto.getDATAS();
                                      if( !IaisCommonUtils.isEmpty(DATAS)){
                                          boolean noRejectPending = true;
                                          List<InputDataAck2Or3Dto> rejtDATAS = IaisCommonUtils.genNewArrayList();
                                          List<InputDataAck2Or3Dto> paySucDATAS = IaisCommonUtils.genNewArrayList();
                                          for(InputDataAck2Or3Dto inputDataAck2Or3Dto : DATAS){
                                              if(GrioConsts. ACK3_TRANSACTION_LEVEL_PENDING_VALUE.equalsIgnoreCase(inputDataAck2Or3Dto.getTransactionStatus()) ){
                                                  noRejectPending = false;
                                                  break;
                                              }else if(GrioConsts.ACK3_TRANSACTION_LEVEL_REJECTED_VALUE.equalsIgnoreCase(inputDataAck2Or3Dto.getTransactionStatus())){
                                                  rejtDATAS.add(inputDataAck2Or3Dto);
                                              }else {
                                                  paySucDATAS.add(inputDataAck2Or3Dto);
                                              }
                                          }
                                          if(noRejectPending){
                                              saveXml(ack03Xml,AppConsts.COMMON_STATUS_IACTIVE,ApplicationConsts.GIRO_DOWN_XML_SFTP,FileUtil.getFileName(remoteFileNames,".ACK3"),null);
                                              //save upload xml inactive
                                              giroPaymentXmlDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                                              giroPaymentXmlDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                                              appPaymentStatusClient.updateGiroDataXmlDto(giroPaymentXmlDto);
                                              //upload GiroPayment
                                              saveGiroPaymentDtosByDatas(paySucDATAS,ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS,ApplicationConsts.GIRO_GET_ACK3_TYPE);
                                              //upload app group
                                              saveAppGroupForTrue(paySucDATAS);
                                              saveGiroPaymentDtosByDatas(rejtDATAS,ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_FAIL,ApplicationConsts.GIRO_GET_ACK3_TYPE);
                                              rejectSaveAppGroupSendEmailStatus(rejtDATAS);
                                          }else {
                                              deleteListFileNameByAckTag(remoteFileNames,".ACK1");
                                              deleteListFileNameByAckTag(remoteFileNames,".ACK2");
                                          }
                                      }else {
                                          log.info(StringUtil.changeForLog("-----------------ACK3 DATA IS NULL"));
                                      }
                                  }else {
                                      deleteListFileNameByAckTag(remoteFileNames,".ACK1");
                                      deleteListFileNameByAckTag(remoteFileNames,".ACK2");
                                      log.info(StringUtil.changeForLog("-----------------ACK3 File No find --------------"));
                                  }
                              }else {
                                  log.info(StringUtil.changeForLog("----------file tag reject :" + tag));
                                  //fail need send email
                                  giroPaymentXmlDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                                  giroPaymentXmlDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                                  appPaymentStatusClient.updateGiroDataXmlDto(giroPaymentXmlDto);
                                  // Releasing the already inactive AppGroup
                                  List<InputDataAck2Or3Dto> DATAS  =  inputAck2Dto.getDATAS();
                                  if(IaisCommonUtils.isEmpty(DATAS)){
                                      DATAS = IaisCommonUtils.genNewArrayList();
                                      setInputDataAck2Or3DtosByGiroXmlPaymentDto(DATAS,giroPaymentXmlDto);
                                  }
                                  rejectSaveAppGroupSendEmailStatus(DATAS);
                                  // old code GrioConsts.GIRO_PAY_STATUS_FAILED
                                  saveGiroPaymentDtosByDatas(DATAS,ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_FAIL,ApplicationConsts.GIRO_GET_ACK2_TYPE);
                              }
                              FileUtil.deleteFilesByFileNames(remoteFileNames,downPath);
                          }
                  }
              }catch (Exception e){
                  log.error(e.getMessage(),e);
              }
          }
          sysnSaveGroupToBe();
        log.info("------------getGiroXmlFromSftpAndSaveXml end ----------");
    }
    private void saveAck(String ACK,String type,String tag,String fileName,String sendXmlData,List<InputDataAck2Or3Dto> datas,String groupStatus){
        GiroPaymentXmlDto giroPaymentXmlDto = new GiroPaymentXmlDto();
        giroPaymentXmlDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        giroPaymentXmlDto.setXmlData(ACK);
        giroPaymentXmlDto.setXmlType(type);
        giroPaymentXmlDto.setTag(tag+"_"+type);
        giroPaymentXmlDto.setFileName(fileName);
        giroPaymentXmlDto.setFileType(type);
        giroPaymentXmlDto.setOutcome(groupStatus);
        giroPaymentXmlDto.setInputDetailDtos(getPendingGiroGroupNos(sendXmlData));
        giroPaymentXmlDto.setDatas(datas);
        giroPaymentXmlDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
        appPaymentStatusClient.updateGiroAckByGiroPaymentXmlDto(giroPaymentXmlDto);
    }

    private List<InputDetailDto> getPendingGiroGroupNos(String sendXmlData){
        try {
            GiroXmlPaymentDto grioXmlPaymentDto = (GiroXmlPaymentDto) XmlBindUtil.convertToObject(GiroXmlPaymentDto.class,sendXmlData);
            return grioXmlPaymentDto.getINPUT_DETAIL();
        }catch (Exception e){
            log.error(StringUtil.changeForLog("-------------Failed convertToGiroXmlPaymentDto XML DATA :"+sendXmlData+"--------------------"));
        }
        return null;
    }
    private void deleteListFileNameByAckTag( List<String> remoteFileNames,String ackTag){
        int index = 0;
        int subscript = -1;
        for(String remoteFileName : remoteFileNames){
            if(remoteFileName.contains(ackTag)){
                subscript = index;
                break;
            }
            index++;
        }
        if(subscript != -1){
            remoteFileNames.remove(subscript);
        }
    }
    private void setInputDataAck2Or3DtosByGiroXmlPaymentDto( List<InputDataAck2Or3Dto> DATAS ,GiroPaymentXmlDto giroPaymentXmlDto){
        try{
            GiroXmlPaymentDto grioXmlPaymentDto = (GiroXmlPaymentDto)XmlBindUtil.convertToObject(GiroXmlPaymentDto.class,giroPaymentXmlDto.getXmlData());
            if(grioXmlPaymentDto != null && !IaisCommonUtils.isEmpty(grioXmlPaymentDto.getINPUT_DETAIL())){
                List<InputDetailDto> INPUT_DETAIL = grioXmlPaymentDto.getINPUT_DETAIL();
                for (InputDetailDto inputDetailDto : INPUT_DETAIL){
                    InputDataAck2Or3Dto inputDataAck2Or3Dto = new InputDataAck2Or3Dto();
                    inputDataAck2Or3Dto.setCustomerReference(inputDetailDto.getParticulars());
                    inputDataAck2Or3Dto.setAmount(inputDetailDto.getAmount());
                    inputDataAck2Or3Dto.setBatchId(inputDetailDto.getBatchID());
                    DATAS.add(inputDataAck2Or3Dto);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }
    private void rejectSaveAppGroupSendEmailStatus(List<InputDataAck2Or3Dto> DATAS){
        if(!IaisCommonUtils.isEmpty(DATAS)){
            for(InputDataAck2Or3Dto inputDataAck2Or3Dto : DATAS){
                String appGroupNo = inputDataAck2Or3Dto.getCustomerReference().replace(inputDataAck2Or3Dto.getBatchId(),"");
                ApplicationGroupDto applicationGroupDto = applicationFeClient.getAppGrpByAppNo(appGroupNo+"-01").getEntity();
                applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_FAIL);
                applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                appSubmissionService.sendEmailForGiroFailAndSMSAndMessage(applicationGroupDto);
                applicationFeClient.updateAppGrpPmtStatus(applicationGroupDto);
                //data sysn
                try{
                  saveAppGroupGiroSysnEic(applicationGroupDto);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }

        }
    }
    private List<GiroPaymentDto> saveGiroPaymentDtosByDatas(List<InputDataAck2Or3Dto> DATAS,String status,String ackType){
        List<GiroPaymentDto> giroPaymentDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(DATAS)){
            for(InputDataAck2Or3Dto inputDataAck2Or3Dto : DATAS){
                String AppGroupNo  = inputDataAck2Or3Dto.getCustomerReference().replace(inputDataAck2Or3Dto.getBatchId(),"");
                //  Old Code GrioConsts.GIRO_PAY_STATUS_PENDING
                List<GiroPaymentDto> girGetPays = appPaymentStatusClient.getGiroPaymentDtosByPmtStatusAndAppGroupNo(ApplicationConsts.PAYMENT_STATUS_PENDING_GIRO,AppGroupNo,AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY).getEntity();
                GiroPaymentDto giroPaymentDto;
                if(!IaisCommonUtils.isEmpty( girGetPays)){
                    giroPaymentDto = girGetPays.get(0);
                 }else {
                    log.info(StringUtil.changeForLog("------saveGiroPaymentDtosByDatas AppGroupNo :" +AppGroupNo + " no find pending data in db---------"));
                   continue;
                }
                giroPaymentDto.setPmtStatus(status);
                giroPaymentDto.setGiroStatus(ackType);
                giroPaymentDto.setPmtType(ApplicationConsts.GIRO_BANK_PAYMENT_TYPE_MONEYPAY);
                giroPaymentDto.setAppGroupNo(AppGroupNo);
                giroPaymentDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                giroPaymentDto.setAmount(Double.valueOf(inputDataAck2Or3Dto.getAmount()));
                //clear patch dirty data
                for (int i = 1; i < girGetPays.size(); i++) {
                     GiroPaymentDto giroPaymentDtoOther =  girGetPays.get(i);
                     giroPaymentDtoOther.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_FAIL_REMIND_OK);
                     giroPaymentDtoOther.setGiroStatus(ackType);
                }
                //save giroPaymentDto
                appPaymentStatusClient.updateGiroPaymentDtos(girGetPays);
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
        List<GiroPaymentDto> giroPaymentDtos = appPaymentStatusClient.getGiroPaymentDtosByPmtStatusAndAppGroupNo(ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS,applicationGroupDto.getGroupNo(),AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY).getEntity();
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
                applicationGroupDto.setPaymentDt(new Date());
                applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                applicationFeClient.updateAppGrpPmtStatus(applicationGroupDto);
                //sysn be create need sysn appgroup in xml
                saveXml( JsonUtil.parseToJson(applicationGroupDto),AppConsts.COMMON_STATUS_ACTIVE,ApplicationConsts.GIRO_PAY_SUCCESS_SYSN_BE,applicationGroupDto.getGroupNo(),giroPaymentDtos.get(0));
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
             appPaymentStatusClient.updateGiroDataXmlDto(giroPaymentXmlDto);
         }
       }
        log.info(" sysnSaveGroupToBe end");
    }


    @Override
    public String saveAppGroupGiroSysnEic(ApplicationGroupDto applicationGroupDto) {
        try {
            return feEicGatewayClient.callEicWithTrack(applicationGroupDto, feEicGatewayClient::saveAppGroupSysnEic,
                    "saveAppGroupSysnEic").getEntity();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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

    @Override
    public OrganizationDto findOrganizationByUen(String uen) {
        return organizationLienceseeClient.findOrganizationByUen(uen).getEntity();
    }

    private GiroPaymentXmlDto saveXml(String xml,String status,String type,String tag,GiroPaymentDto giroPaymentDto){
        GiroPaymentXmlDto giroPaymentXmlDto = new GiroPaymentXmlDto();
        giroPaymentXmlDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        giroPaymentXmlDto.setXmlData(xml);
        giroPaymentXmlDto.setXmlType(type);
        giroPaymentXmlDto.setFileType(type);
        giroPaymentXmlDto.setFileName(tag);
        giroPaymentXmlDto.setTag(tag);
        giroPaymentXmlDto.setStatus(status);
        giroPaymentXmlDto.setGiroPaymentDto(giroPaymentDto);
       return appPaymentStatusClient.updateGiroDataXmlDto(giroPaymentXmlDto).getEntity();
    }
}
