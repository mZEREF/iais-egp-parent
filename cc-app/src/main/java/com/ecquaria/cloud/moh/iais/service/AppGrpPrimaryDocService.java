package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * AppGrpPrimaryDocService
 *
 * @author suocheng
 * @date 10/10/2019
 */
public interface AppGrpPrimaryDocService {
    List<String> SaveFileToRepo(List<MultipartFile> fileList) throws IOException;
    AppGrpPrimaryDocDto saveAppGrpPremisesDoc(AppGrpPrimaryDocDto appGrpPrimaryDocDto);
    List getAppGrpPrimaryDocDtosByAppGrpId(String appGrpId);
    Map<String,List<HcsaSvcDocConfigDto>> getAllHcsaSvcDocs(String serviceId);
    List<AppGrpPrimaryDocDto> saveAppGrpPremisesDocs(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList);
}
