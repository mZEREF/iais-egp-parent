package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * LicenceViewServiceImpl
 *
 * @author suocheng
 * @date 12/16/2019
 */
@Service
@Slf4j
public class LicenceViewServiceImpl implements LicenceViewService {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Override
    public AppSubmissionDto getAppSubmissionByAppId(String appId) {

        return  applicationClient.getAppSubmissionByAppId(appId).getEntity();
    }

    @Override
    public AppEditSelectDto saveAppEditSelect(AppEditSelectDto appEditSelectDto) {
        return applicationClient.createAppEditSelectDto(appEditSelectDto).getEntity();
    }

    @Override
    public AppEditSelectDto saveAppEditSelectToFe(AppEditSelectDto appEditSelectDto) {
        return beEicGatewayClient.callEicWithTrack(appEditSelectDto, beEicGatewayClient::createAppEditSelectDto,
                "createAppEditSelectDto").getEntity();
    }

    @Override
    public void svcDocToPremise(AppSubmissionDto appSubmissionDto) {
        if(appSubmissionDto==null){
            return;
        }
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0);
        List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
        List<AppGrpPrimaryDocDto> dtoAppGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos= IaisCommonUtils.genNewArrayList();
        List<AppSvcDocDto> appSvcDocDtos=IaisCommonUtils.genNewArrayList();
        List<AppSvcDocDto> deleteSvcDoc=IaisCommonUtils.genNewArrayList();
        List<AppGrpPrimaryDocDto> deletePrimaryDoc=IaisCommonUtils.genNewArrayList();
        if(appSvcDocDtoLit!=null){
            for(AppSvcDocDto appSvcDocDto : appSvcDocDtoLit){
                String svcDocId = appSvcDocDto.getSvcDocId();
                String fileRepoId = appSvcDocDto.getFileRepoId();
                if(StringUtil.isEmpty(svcDocId)){
                    deleteSvcDoc.add(appSvcDocDto);
                    continue;
                }
                if(StringUtil.isEmpty(fileRepoId)){
                    deleteSvcDoc.add(appSvcDocDto);
                    continue;
                }
                HcsaSvcDocConfigDto entity = hcsaConfigClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
                if(entity!=null){
                    String serviceId = entity.getServiceId();
                    if(StringUtil.isEmpty(serviceId)){
                        AppGrpPrimaryDocDto appGrpPrimaryDocDto= new  AppGrpPrimaryDocDto();
                        appGrpPrimaryDocDto.setSvcDocId(svcDocId);
                        appGrpPrimaryDocDto.setSvcComDocId(svcDocId);
                        appGrpPrimaryDocDto.setSvcComDocName(entity.getDocTitle());
                        appGrpPrimaryDocDto.setDocName(appSvcDocDto.getDocName());
                        appGrpPrimaryDocDto.setAppGrpId(appSubmissionDto.getAppGrpId());
                        appGrpPrimaryDocDto.setDocSize(appSvcDocDto.getDocSize());
                        appGrpPrimaryDocDto.setFileRepoId(appSvcDocDto.getFileRepoId());
                        appGrpPrimaryDocDto.setMd5Code(appSvcDocDto.getMd5Code());
                        appGrpPrimaryDocDto.setVersion(appSvcDocDto.getVersion());
                        appGrpPrimaryDocDto.setSeqNum(appSvcDocDto.getSeqNum());
                        appGrpPrimaryDocDtos.add(appGrpPrimaryDocDto);
                        appSvcDocDtos.add(appSvcDocDto);
                    }else {
                        appSvcDocDto.setUpFileName(entity.getDocTitle());
                    }
                }
            }
            appSvcDocDtoLit.removeAll(deleteSvcDoc);
            appSvcDocDtoLit.removeAll(appSvcDocDtos);
            for(int i=0;i < appSvcDocDtoLit.size();i++){
                for(int j=0;j < appSvcDocDtoLit.size() && j!=i;j++){
                    if(appSvcDocDtoLit.get(i).getFileRepoId().equals(appSvcDocDtoLit.get(j).getFileRepoId())){
                        appSvcDocDtoLit.remove(appSvcDocDtoLit.get(i));
                        i--;
                        break;
                    }
                }
            }
        }
        if(dtoAppGrpPrimaryDocDtos!=null){
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto : dtoAppGrpPrimaryDocDtos){
                String fileRepoId = appGrpPrimaryDocDto.getFileRepoId();
                if(StringUtil.isEmpty(fileRepoId)){
                    deletePrimaryDoc.add(appGrpPrimaryDocDto);
                    continue;
                }
                if(StringUtil.isEmpty(appGrpPrimaryDocDto.getSvcComDocName())){
                    String svcDocId = appGrpPrimaryDocDto.getSvcDocId();
                    if(svcDocId!=null){
                        HcsaSvcDocConfigDto entity = hcsaConfigClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
                        if(entity!=null){
                            appGrpPrimaryDocDto.setSvcComDocName(entity.getDocTitle());
                        }
                    }
                }
            }
            dtoAppGrpPrimaryDocDtos.removeAll(deletePrimaryDoc);
            if(appGrpPrimaryDocDtos.isEmpty()){
                appGrpPrimaryDocDtos.addAll(dtoAppGrpPrimaryDocDtos);
            }else {
                List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList=IaisCommonUtils.genNewArrayList();
                for(AppGrpPrimaryDocDto appGrpPrimaryDocDto1 : dtoAppGrpPrimaryDocDtos){
                    for(AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtos){
                        String svcComDocName = appGrpPrimaryDocDto.getSvcComDocName();
                        String svcComDocName1 = appGrpPrimaryDocDto1.getSvcComDocName();
                        if(svcComDocName1!=null){
                            if(svcComDocName1.equals(svcComDocName)){
                                continue;
                            }else {
                                appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto1);
                            }
                        }else if(svcComDocName!=null){
                            if(svcComDocName.equals(svcComDocName1)){
                                continue;
                            }else {
                                appGrpPrimaryDocDtoList.add(appGrpPrimaryDocDto1);
                            }
                        }

                    }
                }
                appGrpPrimaryDocDtos.addAll(appGrpPrimaryDocDtoList);
            }
        }
        for(int i=0;i < appGrpPrimaryDocDtos.size();i++){
            for(int j=0;j < appGrpPrimaryDocDtos.size() && j != i;j++){
                if(appGrpPrimaryDocDtos.get(i).getFileRepoId().equals(appGrpPrimaryDocDtos.get(j).getFileRepoId())){
                    appGrpPrimaryDocDtos.remove(appGrpPrimaryDocDtos.get(i));
                    i--;
                    break;
                }
            }
        }
        appSubmissionDto.setAppGrpPrimaryDocDtos(appGrpPrimaryDocDtos);

    }
}
