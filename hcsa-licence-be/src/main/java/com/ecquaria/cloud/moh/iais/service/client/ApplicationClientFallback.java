package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.http.HttpHeaders;

/**
 * ApplicationClientFallback
 *
 * @author suocheng
 * @date 11/26/2019
 */
public class ApplicationClientFallback {
    FeignResponseEntity<ApplicationViewDto> getAppViewByNo( String appNo){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    FeignResponseEntity<ApplicationDto> getAppByNo( String appNo){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    FeignResponseEntity<Void> getDownloadFile( ApplicationListFileDto applicationListDtos){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    FeignResponseEntity<List<ApplicationDto>> getApplicationDto(){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    FeignResponseEntity<ApplicationGroupDto> getAppById( String appGroupId){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    FeignResponseEntity<ApplicationGroupDto> updateApplication( ApplicationGroupDto applicationGroupDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    FeignResponseEntity<List<ApplicationDto>> getGroupAppsByNo( String appGropId){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    FeignResponseEntity<ApplicationDto> updateApplication( ApplicationDto applicationDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    FeignResponseEntity<AppPremisesRoutingHistoryDto> create( AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto ){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    FeignResponseEntity<List<ApplicationLicenceDto>> getGroup( Integer day){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }


    FeignResponseEntity<List<ApplicationGroupDto>> updateApplications(List<ApplicationGroupDto> applicationGroupDtos){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
    public FeignResponseEntity<BroadcastApplicationDto> createBroadcast(BroadcastApplicationDto broadcastApplicationDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<List<AppSvcPremisesScopeDto>> getAppSvcPremisesScopeListByCorreId(String correId){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<String> saveAdhocChecklist(AdhocCheckListConifgDto adhocConfigDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<List<AppSvcPremisesScopeDto>> getAdhocByAppPremCorrId(AdhocCheckListConifgDto adhocConfigDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<AdhocCheckListConifgDto> saveAppAdhocConfig(AdhocCheckListConifgDto adhocConfigDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<AdhocCheckListConifgDto> getAdhocConfigByAppPremCorrId(AdhocCheckListConifgDto adhocConfigDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<List<AdhocChecklistItemDto>> saveAdhocItems(AdhocCheckListConifgDto adhocConfigDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<AppSubmissionDto> getAppSubmissionByAppId(String appId){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
