package com.ecquaria.cloud.moh.iais.service;

import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/12/26 14:56
 **/
public interface FeToBeRecFileService {
    /**
      * @author: shicheng
      * @Date 2019/12/26
      * @Param: null
      * @return: void
      * @Descripation:
      */
    void compressFile(Map<String, String> appIdItemIdMap);

    /**
      * @author: shicheng
      * @Date 2019/12/26
      * @Param: null
      * @return: void
      * @Descripation: get Doc PPT.... File
      */
    Map<String, String> getDocFile();
}
