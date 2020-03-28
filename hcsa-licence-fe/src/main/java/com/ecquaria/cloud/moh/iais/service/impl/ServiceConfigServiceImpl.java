package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
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
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private ApplicationClient applicationClient;
    @Autowired
    private LicenceClient licenceClient;
    @Value("${iais.syncFileTracking.shared.path}")
    private String sharedPath;

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
        applicationClient.doPaymentUpDate(appGrp);
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoDraft(String draftNo) {
        return applicationClient.draftNumberGet(draftNo).getEntity();
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
        List<HcsaServiceStepSchemeDto> result = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos =  appConfigClient.getServiceStepsByServiceId(serviceId).getEntity();
        if(hcsaServiceStepSchemeDtos!=null && hcsaServiceStepSchemeDtos.size() > 0){
            for (HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto : hcsaServiceStepSchemeDtos){
                hcsaServiceStepSchemeDto.setStepName(MasterCodeUtil.getCodeDesc(hcsaServiceStepSchemeDto.getStepCode()));
                result.add(hcsaServiceStepSchemeDto);
            }
        }
        return result;
    }
    @Override
    public List<HcsaServiceCorrelationDto> getCorrelation(){
        return appConfigClient.serviceCorrelation().getEntity();
    }

    private void moveFile(String fileName, String path, byte[] fileData) throws IOException {
        File file = new File(path+"/"+fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
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
}
