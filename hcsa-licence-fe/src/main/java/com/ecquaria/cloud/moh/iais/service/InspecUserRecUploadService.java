package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/23 15:25
 **/
public interface InspecUserRecUploadService {

    /**
      * @author: shicheng
      * @Date 2019/12/23
      * @Param: appNo
      * @return: ChecklistItemDto
      * @Descripation: get Checklist Question and Clause
      */
    List<ChecklistItemDto> getQuesAndClause(String appNo);
}
