package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.utils.SingeFileUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/4/23 16:17
 */
@Delegator("mohAppealPrint")
@Slf4j
public class AppealPrintDelegator {

    public void prepareData(BaseProcessClass bpc){
        log.info("------>prepareData start<------");
        bpc.request.setAttribute("crud_action_type","appeal");
        Map<String, File> map = (Map<String, File>) bpc.request.getSession().getAttribute("seesion_files_map_ajax_feselectedFile");
        Map<String, PageShowFileDto> pageShowFileHashMap = (Map<String, PageShowFileDto>)bpc.request.getSession().getAttribute("pageShowFileHashMap");
        List<PageShowFileDto> pageShowFileDtos =new ArrayList<>(5);
        if(map!=null&&!map.isEmpty()){
            map.forEach((k,v)->{
                if(v!=null){
                    long length = v.length();
                    if(length>0){
                        Long size=length/1024;
                        AppPremisesSpecialDocDto premisesSpecialDocDto=new AppPremisesSpecialDocDto();
                        premisesSpecialDocDto.setDocName(v.getName());
                        SingeFileUtil singeFileUtil=SingeFileUtil.getInstance();
                        String fileMd5 = singeFileUtil.getFileMd5(v);
                        premisesSpecialDocDto.setMd5Code(fileMd5);
                        premisesSpecialDocDto.setDocSize(Integer.valueOf(size.toString()));
                        PageShowFileDto pageShowFileDto =new PageShowFileDto();
                        pageShowFileDto.setFileName(v.getName());
                        String e = k.substring(k.lastIndexOf('e') + 1);
                        pageShowFileDto.setIndex(e);
                        pageShowFileDto.setFileMapId("selectedFileDiv"+e);
                        pageShowFileDto.setSize(Integer.valueOf(size.toString()));
                        pageShowFileDto.setMd5Code(fileMd5);
                        premisesSpecialDocDto.setIndex(k);
                        pageShowFileDtos.add(pageShowFileDto);
                    }
                }else {
                    if(pageShowFileHashMap!=null){
                        PageShowFileDto pageShowFileDto = pageShowFileHashMap.get(k);
                        AppPremisesSpecialDocDto premisesSpecialDocDto=new AppPremisesSpecialDocDto();
                        premisesSpecialDocDto.setDocName(pageShowFileDto.getFileName());
                        premisesSpecialDocDto.setFileRepoId(pageShowFileDto.getFileUploadUrl());
                        premisesSpecialDocDto.setDocSize(pageShowFileDto.getSize());
                        premisesSpecialDocDto.setMd5Code(pageShowFileDto.getMd5Code());
                        premisesSpecialDocDto.setIndex(k.substring(k.lastIndexOf('e') + 1));
                        pageShowFileDtos.add(pageShowFileDto);
                    }
                }
            });
        }
        Collections.sort(pageShowFileDtos,(s1, s2)->s1.getFileMapId().compareTo(s2.getFileMapId()));
        bpc.request.getSession().setAttribute("pageShowFiles", pageShowFileDtos);
    }
    public void start(BaseProcessClass bpc){
        log.info("------>mohAppealPrint start<------");
    }
}
