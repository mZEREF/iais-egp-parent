package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;

import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/12/27 14:15
 **/
public interface InspecSaveBeRecByService {
    /**
      * @author: shicheng
      * @Date 2019/12/27
      * @Param: applicationStatusFeToBeRectification, commonStatusActive
      * @return:
      * @Descripation:
      */
    List<ProcessFileTrackDto> getFileTypeAndStatus(String applicationStatusFeToBeRectification, String commonStatusActive);

    /**
      * @author: shicheng
      * @Date 2019/12/27
      * @Param: null
      * @return: void
      * @Descripation: delete UnZip File
      */
    void deleteUnZipFile();

    /**
      * @author: shicheng
      * @Date 2019/12/27
      * @Param: null
      * @return: void
      * @Descripation: compress File
      */
    List<String> compressFile(List<ProcessFileTrackDto> processFileTrackDtos);

    /**
      * @author: shicheng
      * @Date 2019/12/27
      * @Param: intranet, processFileTrackDtos, reportIds
      * @return: Boolean
      * @Descripation: save Data
      */
    void saveData(AuditTrailDto intranet, List<ProcessFileTrackDto> processFileTrackDtos, List<String> reportIds);

    /**
      * @author: shicheng
      * @Date 2021/3/2
      * @Param: processFileTrackDtos
      * @return: Map<String, List<ProcessFileTrackDto>>
      * @Descripation: key appId, value processFileTrackDtos
      */
    Map<String, List<ProcessFileTrackDto>> getProcessFileTrackDtosWithAppId(List<ProcessFileTrackDto> processFileTrackDtos);
}
