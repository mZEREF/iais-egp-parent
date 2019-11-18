package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
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

import static com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts.HCSASVCDOCURL;
import static com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts.URLREPO;


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

    @Override
    public List<String> saveFileToRepo(List<MultipartFile> fileList) throws IOException {
        List<MultipartFile> multipartFileList = new ArrayList();
        multipartFileList.add(fileList.get(0));
        return RestApiUtil.saveFile(URLREPO,multipartFileList,IaisEGPHelper.getCurrentAuditTrailDto());
    }

    @Override
    public AppGrpPrimaryDocDto saveAppGrpPremisesDoc(AppGrpPrimaryDocDto appGrpPrimaryDocDto) {
        return RestApiUtil.save(URL,appGrpPrimaryDocDto,AppGrpPrimaryDocDto.class);
    }

    @Override
    public List getAppGrpPrimaryDocDtosByAppGrpId(String appGrpId) {
        Map<String, Object> map = new HashMap<>();
        map.put("appGrpId", appGrpId);
        return RestApiUtil.getByReqParam(URL, map, List.class);
    }

    @Override
    public Map<String,List<HcsaSvcDocConfigDto>> getAllHcsaSvcDocs(String serviceId) {
        log.debug("getAllHcsaSvcDocs start......");
        //common doc
        Map<String,Object> common = new HashMap<>();
        //common.put("serviceId", "");==>serviceId null
        common.put("flag", false);//==>false =0
        List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigDtos = RestApiUtil.getListByReqParam(HCSASVCDOCURL, common, HcsaSvcDocConfigDto.class);

        //premises doc
        Map<String,Object> premises = new HashMap<>();
        //premises.put("serviceId", "");==>serviceId null
        premises.put("flag", true);//==>true =1
        List<HcsaSvcDocConfigDto> premHcsaSvcDocConfigDtos = RestApiUtil.getListByReqParam(HCSASVCDOCURL, premises, HcsaSvcDocConfigDto.class);
        Map<String,List<HcsaSvcDocConfigDto>> map = new HashMap<>();
        map.put("common", commonHcsaSvcDocConfigDtos);
//        map.put("premises", premHcsaSvcDocConfigDtos);
        return map;
    }

}
