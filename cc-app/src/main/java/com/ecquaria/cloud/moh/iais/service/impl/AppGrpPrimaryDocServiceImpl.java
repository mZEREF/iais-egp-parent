package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppGrpPrimaryDocService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * AppGrpPremisesDocServiceImpl
 *
 * @author suocheng
 * @date 10/10/2019
 */
@Service
@Slf4j
public class AppGrpPrimaryDocServiceImpl implements AppGrpPrimaryDocService {
    private static final String URL="iais-application:8881/iais-premisesdoc";
    private static final String URLREPO = "file-repository:8884";
    //get hcsa svc doc(comm/premise)
    private static final String URLADMIM ="system-admin-service:8886/hsca-svc-doc-config";
    private static final String URLSAVEMUL="iais-application:8881/iais-premisesdoc/appGrpPrimaryDocs";

    @Override
    public List<String> SaveFileToRepo(AppGrpPrimaryDocDto appGrpPrimaryDocDto) throws IOException {
        List<MultipartFile> fileList = new ArrayList();
        fileList.add(appGrpPrimaryDocDto.getFile());
        return  RestApiUtil.saveFile(URLREPO,fileList,IaisEGPHelper.getCurrentAuditTrailDto());
    }

    @Override
    public AppGrpPrimaryDocDto saveAppGrpPremisesDoc(AppGrpPrimaryDocDto appGrpPrimaryDocDto) {
        appGrpPrimaryDocDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return RestApiUtil.save(URL,appGrpPrimaryDocDto,AppGrpPrimaryDocDto.class);
    }

    @Override
    public List getAppGrpPrimaryDocDtosByAppGrpId(String appGrpId) {
        Map<String, Object> map = new HashMap<>();
        map.put("appGrpId", appGrpId);
        return RestApiUtil.getByReqParam(URL, map, List.class);
    }

    @Override
    public List<HcsaSvcDocConfigDto> getAllHcsaSvcCommonDocDtos() {
        log.debug("getAllHcsaSvcCommonDocDtos start......");
        Map<String,Object> map = new HashMap<>();
//        map.put("status", AppConsts.COMMON_STATUS_ACTIVE);
        List<HcsaSvcDocConfigDto> hcsaSvcDocConfigDtos = RestApiUtil.getListByReqParam(URLADMIM, map, HcsaSvcDocConfigDto.class);
        return hcsaSvcDocConfigDtos;
    }

    @Override
    public List<AppGrpPrimaryDocDto> saveAppGrpPremisesDocs(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList) {
        List<AppGrpPrimaryDocDto> list= RestApiUtil.save(URLSAVEMUL, appGrpPrimaryDocDtoList, List.class);
        return list;
    }
}
