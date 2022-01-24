package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssFileDto;

import java.util.List;
import java.util.Map;
/**
 * @author fanghao
 * @date 2022/01/17
 **/
public interface VssUploadFileService {

    String saveFile(VssFileDto vssFileDto ) ;
  /*  String changeStatus(VssFileDto vssFileDto, Map<String, List<String>> map);*/
    String compressFile(String vssTreId);


    void  getRelatedDocuments(VssFileDto vssFileDto) throws Exception;
  /*  boolean renameAndSave(String file,String vssTreId);*/

    void vssFile();
}
