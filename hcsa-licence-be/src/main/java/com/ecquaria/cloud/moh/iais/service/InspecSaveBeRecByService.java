package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;

import java.util.List;

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
    void compressFile(List<ProcessFileTrackDto> processFileTrackDtos);

    /**
      * @author: shicheng
      * @Date 2019/12/27
      * @Param: intranet
      * @return: Boolean
      * @Descripation: save Data
      */
    Boolean saveData(AuditTrailDto intranet, List<ProcessFileTrackDto> processFileTrackDtos);
}
