package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/26 14:56
 **/
public interface FeToBeRecFileService {
    /**
      * @author: shicheng
      * @Date 2019/12/26
      * @Param: null
      * @return: String
      * @Descripation: get User Submit Rectification Data To Json String
      */
    String getRecData();

    /**
      * @author: shicheng
      * @Date 2019/12/26
      * @Param: data
      * @return: void
      * @Descripation: Create Txt File By Rectification Data To Json String
      */
    void createDataTxt(String data);

    /**
      * @author: shicheng
      * @Date 2019/12/26
      * @Param: null
      * @return: void
      * @Descripation:
      */
    void compressFile();

    /**
      * @author: shicheng
      * @Date 2019/12/26
      * @Param: null
      * @return: void
      * @Descripation: get Doc PPT.... File
      */
    List<ApplicationDto> getDocFile();

    /**
      * @author: shicheng
      * @Date 2019/12/26
      * @Param: null
      * @return: void
      * @Descripation: change App Status
      */
    void changeStatus(List<ApplicationDto> applicationDtos);
}
